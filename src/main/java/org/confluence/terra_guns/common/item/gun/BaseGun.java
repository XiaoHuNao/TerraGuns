package org.confluence.terra_guns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.NeoForge;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.api.event.GunEvent;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.impl.AmmoDataManager;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseGun extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GunPropertyComponent component;

    public BaseGun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, int penetrate, ModRarity rarity) {
        super(properties.stacksTo(1).component(TGDataComponents.GUN_PROPERTY_COMPONENT.get(), new GunPropertyComponent(cooldown, damage, velocity, knockback, critical, penetrate, rarity)));
        this.component = new GunPropertyComponent(cooldown, damage, velocity, knockback, critical, penetrate, rarity);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public BaseGun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, ModRarity rarity) {
        this(properties, cooldown, damage, velocity, knockback, critical, 1, rarity);
    }

    public void shoot(ServerPlayer player, ItemStack bullet) {
        ServerLevel serverLevel = player.serverLevel();
        BulletPropertyComponent bulletComponent = bullet.get(TGDataComponents.BULLET_PROPERTY_COMPONENT);
        if (bulletComponent == null) return;

        AmmoDataManager ammoDataManager = new AmmoDataManager(this.component, bulletComponent);
        GunEvent.AmmoDataEvent ammoDataEvent = new GunEvent.AmmoDataEvent(player, this, ammoDataManager);
        NeoForge.EVENT_BUS.post(ammoDataEvent);

        List<BaseBulletEntity> baseBulletEntities = prepareBulletEntity(player, bullet, ammoDataEvent.getDamage(), ammoDataEvent.getKnockback(), ammoDataEvent.getVelocity(), ammoDataEvent.getPenetrate());
        baseBulletEntities.forEach(serverLevel::addFreshEntity);
    }

    protected List<BaseBulletEntity> prepareBulletEntity(ServerPlayer player, ItemStack bullet, float damage, float knockback, float velocity, int penetrate){
        List<BaseBulletEntity> baseBulletEntities = new ArrayList<>();

        BaseBulletEntity baseBulletEntity = new BaseBulletEntity(player.serverLevel(), ((BaseBullet) bullet.getItem()), damage, knockback, penetrate);
        baseBulletEntity.setOwner(player);
        baseBulletEntity.moveTo(player.getX(), player.getEyeY() - 0.1, player.getZ(), player.getXRot(), player.getYRot());
        baseBulletEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, velocity, 0);

        baseBulletEntities.add(baseBulletEntity);
        return baseBulletEntities;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", component.damage()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.critical", String.format("%.1f", component.critical() * 100)).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", component.knockback()).withStyle(ChatFormatting.GRAY));
    }

    public int getCooldown() {
        return component.cooldown();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<BaseGun> gun = new AnimationController<>(this, "gun", state -> PlayState.CONTINUE);
        gun.triggerableAnim("gun_fire", RawAnimation.begin().then("fire", Animation.LoopType.PLAY_ONCE));
        controllers.add(gun);
    }

    public void fireAnimator(ItemStack itemStack, ServerPlayer serverPlayer){
        this.triggerAnim(serverPlayer, GeoItem.getOrAssignId(itemStack, serverPlayer.serverLevel()), "gun", "gun_fire");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
