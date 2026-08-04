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
import org.confluence.terra_guns.common.definition.FireMode;
import org.confluence.terra_guns.common.definition.GunDefinition;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.init.TGTags;
import org.confluence.terra_guns.util.AnimUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
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

    public BaseGun(Properties properties, GunDefinition definition) {
        super(prepareProperties(properties, definition));
        this.definition = definition;
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
    }

    public int getCooldown() {
        return definition.cooldown();
    }

    public GunDefinition getDefinition() {
        return definition;
    }

    public boolean isAutomatic(ItemStack stack) {
        return !stack.is(TGTags.MANUAL_GUN)
                && (definition.fireMode() == FireMode.AUTOMATIC || stack.is(TGTags.AUTOMATIC_GUN));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<BaseGun> gun = new AnimationController<>(this, "gun", state -> PlayState.CONTINUE);
        gun.triggerableAnim("gun_fire", RawAnimation.begin().then("fire", Animation.LoopType.DEFAULT));
        gun.triggerableAnim("gun_pick", RawAnimation.begin().then("pick up", Animation.LoopType.DEFAULT));
        gun.triggerableAnim("gun_reload", RawAnimation.begin().then("reloading", Animation.LoopType.DEFAULT));
        controllers.add(gun);
    }

    public void fireAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        AnimUtil.stopAndPlayAnim(this, itemStack, serverPlayer, "gun", "gun_fire");
    }

    public void pickAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        AnimUtil.stopAndPlayAnim(this, itemStack, serverPlayer, "gun", "gun_pick");
    }

    public void reloadAnimator(ItemStack itemStack, ServerPlayer serverPlayer) {
        AnimUtil.stopAndPlayAnim(this, itemStack, serverPlayer, "gun", "gun_reload");
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
