package org.confluence.terra_guns.common.item.bullet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.api.event.BulletEvent;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGDamageTypes;
import org.confluence.terra_guns.common.init.TGDataComponents;

import java.util.List;

public class BaseBullet extends Item {
    private final BulletPropertyComponent component;

    public BaseBullet(Properties properties, float damage, float velocity, float velocityMultiplier, float knockback, ModRarity rarity, int penetrate, boolean infinity) {
        super(properties);
        BulletPropertyComponent component = new BulletPropertyComponent(damage, velocity, velocityMultiplier, knockback, penetrate, rarity, infinity);
        properties.component(TGDataComponents.BULLET_PROPERTY_COMPONENT.get(), component);

        this.components = Properties.COMPONENT_INTERNER.intern(properties.components.build());
        this.component = component;
    }

    public void tick(BaseBulletEntity baseBulletEntity) {
    }

    public void onHitBlock(BaseBulletEntity bulletEntity, BlockHitResult result) {
        bulletEntity.discard();
    }

    public void onHitEntity(BaseBulletEntity bulletEntity, EntityHitResult result) {
        Entity entity = result.getEntity();
        entity.hurt(TGDamageTypes.of(entity.level(), TGDamageTypes.BULLET_DAMAGE, bulletEntity.getOwner()), bulletEntity.damage);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", component.damage()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", component.knockback()).withStyle(ChatFormatting.GRAY));
    }
}
