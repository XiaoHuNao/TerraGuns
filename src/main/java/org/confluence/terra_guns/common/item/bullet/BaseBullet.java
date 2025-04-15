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
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGDamageTypes;
import org.confluence.terra_guns.common.init.TGDataComponents;

import java.util.List;

public class BaseBullet extends Item {
    private BulletPropertyComponent component;
    public BaseBullet(Properties properties, float damage, float velocity, float velocityMultiplier, float knockback, ModRarity rarity, boolean infinity) {
        super(properties.component(TGDataComponents.BULLET_PROPERTY_COMPONENT.get(), new BulletPropertyComponent(damage, velocity, velocityMultiplier, knockback, rarity, infinity)));

        this.component = new BulletPropertyComponent(damage, velocity, velocityMultiplier, knockback, rarity, infinity);
    }

    public void tick(BaseBulletEntity baseBulletEntity){
    }

    public void hitEffect(Entity player, Entity entity, float amount) {
        entity.hurt(TGDamageTypes.of(entity.level(), TGDamageTypes.BULLET_DAMAGE, player), amount);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", component.damage()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", component.knockback()).withStyle(ChatFormatting.GRAY));
    }
}
