package org.confluence.terra_guns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.api.client.animation.HandAnimationAction;
import org.confluence.terra_guns.api.client.animation.HandAnimationApi;
import org.confluence.terra_guns.api.client.animation.HandAnimationChannel;
import org.confluence.terra_guns.api.client.animation.HandAnimationProfile;
import org.confluence.terra_guns.common.definition.FireMode;
import org.confluence.terra_guns.common.definition.GunDefinition;
import org.confluence.terra_guns.common.enchantment.GunEnchantmentService;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.init.TGEnchantments;
import org.confluence.terra_guns.common.init.TGTags;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationProcessor;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

/**
 * Item-side representation of a gun.
 *
 * <p>The item owns its immutable definition, tooltip and client animation
 * hooks. Server-side firing is handled by the combat services, so projectile
 * entity creation never leaks into the item class.</p>
 */
public class BaseGun extends Item implements GeoItem {
    protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GunDefinition definition;
    private final HandAnimationProfile animationProfile;

    public BaseGun(Properties properties, GunDefinition definition) {
        this(properties, definition, HandAnimationProfile.legacy());
    }

    public BaseGun(Properties properties, GunDefinition definition, HandAnimationProfile animationProfile) {
        super(prepareProperties(properties, definition));
        this.definition = definition;
        this.animationProfile = animationProfile;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    private static Properties prepareProperties(Properties properties, GunDefinition definition) {
        properties.stacksTo(1);
        properties.component(TGDataComponents.GUN_PROPERTY_COMPONENT.get(), definition.component());
        return properties;
    }

    public BaseGun(Properties properties, int cooldown, float damage, float velocity, float knockback,
                   float critical, int penetrate, float inaccuracy, ModRarity rarity) {
        this(properties, new GunDefinition(cooldown, damage, velocity, knockback, critical, penetrate,
                inaccuracy, rarity, FireMode.MANUAL));
    }

    public BaseGun(Properties properties, int cooldown, float damage, float velocity, float knockback,
                   float critical, float inaccuracy, ModRarity rarity) {
        this(properties, cooldown, damage, velocity, knockback, critical, 0, inaccuracy, rarity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", definition.damage())
                .withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.critical",
                        String.format("%.1f", definition.critical() * 100)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", definition.knockback())
                .withStyle(ChatFormatting.GRAY));

        if (GunEnchantmentService.getLevel(stack, TGEnchantments.EMERGENCY_MELEE) > 0) {
            int remainingTicks = context.level() == null
                    ? 0
                    : GunEnchantmentService.getEmergencyMeleeCooldownRemaining(stack, context.level());
            if (remainingTicks > 0) {
                tooltipComponents.add(Component.translatable(
                        "tooltip.terra_guns.emergency_melee.cooldown",
                        (remainingTicks + 19) / 20
                ).withStyle(ChatFormatting.GRAY));
            } else {
                tooltipComponents.add(Component.translatable(
                        "tooltip.terra_guns.emergency_melee.cooldown.ready"
                ).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public int getCooldown() {
        return definition.cooldown();
    }

    public GunDefinition getDefinition() {
        return definition;
    }

    public HandAnimationProfile getAnimationProfile() {
        return animationProfile;
    }

    public boolean isAutomatic(ItemStack stack) {
        return !stack.is(TGTags.MANUAL_GUN)
                && (definition.fireMode() == FireMode.AUTOMATIC || stack.is(TGTags.AUTOMATIC_GUN));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        for (HandAnimationChannel channel : animationProfile.channels()) {
            AnimationController<BaseGun> controller = new AnimationController<>(this, channel.name(), state -> {
                if (!state.getController().isPlayingTriggeredAnimation()) {
                    channel.idle().ifPresent(idle -> state.getController().setAnimation(idle.rawAnimation()));
                }
                return PlayState.CONTINUE;
            });
            channel.animations().forEach((action, clip) ->
                    controller.triggerableAnim(action.id(), channel.triggeredAnimation(action)));
            controllers.add(controller);
        }
    }

    public void fireAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        // Shooting has priority over inspection. Inspection and shooting may
        // live on different animation channels, so triggering "shoot" alone
        // would leave the inspection channel playing underneath it.
        HandAnimationApi.stop(this, itemStack, serverPlayer, animationProfile, HandAnimationAction.INSPECT);
        playAnimator(itemStack, serverPlayer, HandAnimationAction.SHOOT);

        // Shell is deliberately independent from the recoil animation.  This
        // gun model has one Shell bone, so restarting it before the previous
        // ejection has finished would visibly teleport the casing back to the
        // chamber on every automatic shot.  Let the current casing finish;
        // the next shot will start a new one once the bone is available.
        long instanceId = GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel());
        if (!isAnimationPlaying(instanceId, HandAnimationAction.EJECT_SHELL)) {
            playAnimator(itemStack, serverPlayer, HandAnimationAction.EJECT_SHELL);
        }
    }

    public void pickAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        playAnimator(itemStack, serverPlayer, HandAnimationAction.DRAW);
    }

    public void reloadAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        playAnimator(itemStack, serverPlayer, HandAnimationAction.RELOAD);
    }

    public void putAwayAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        HandAnimationApi.stop(this, itemStack, serverPlayer, animationProfile, HandAnimationAction.EJECT_SHELL);
        playAnimator(itemStack, serverPlayer, HandAnimationAction.PUT_AWAY);
    }

    public void inspectAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        // Inspect owns the Shell track in hand_gun.animation.json.  Stop a
        // previous firing ejection before handing that bone back to inspect.
        HandAnimationApi.stop(this, itemStack, serverPlayer, animationProfile, HandAnimationAction.EJECT_SHELL);
        playAnimator(itemStack, serverPlayer, HandAnimationAction.INSPECT);
    }

    public boolean playAnimator(ItemStack itemStack, ServerPlayer serverPlayer, HandAnimationAction action) {
        return HandAnimationApi.play(this, itemStack, serverPlayer, animationProfile, action);
    }

    public boolean isAnimationPlaying(long instanceId, HandAnimationAction action) {
        return cache.getManagerForId(instanceId).getAnimationControllers().values().stream()
                .filter(AnimationController::isPlayingTriggeredAnimation)
                .map(AnimationController::getCurrentAnimation)
                .filter(animation -> animation != null)
                .map(AnimationProcessor.QueuedAnimation::animation)
                .anyMatch(animation -> animationProfile.isAnimation(action, animation.name()));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }
}
