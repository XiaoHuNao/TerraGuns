package org.confluence.terra_guns.common.item.gun;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.init.TGDataComponents;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;

import javax.annotation.Nullable;
import java.util.List;

public class BaseGun extends Item {
    private final GunPropertyComponent component;

    public BaseGun(Properties properties, int cooldown, float damage, float velocity, float knockback, float critical, int penetrate, ModRarity rarity) {
        super(properties.stacksTo(1));
        this.component = new GunPropertyComponent(cooldown, damage, velocity, knockback, critical, penetrate, rarity);

        this.components().getOrDefault(TGDataComponents.GUN_PROPERTY_COMPONENT.get(), component);
    }

    public void shoot(
            ServerLevel level,
            LivingEntity shooter,
            List<ItemStack> projectileItems,
            float velocity,
            float inaccuracy,
            @Nullable LivingEntity target
    ) {
        float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F / (float) (projectileItems.size() - 1);
        float f2 = (float) ((projectileItems.size() - 1) % 2) * f1 / 2.0F;
        float f3 = 1.0F;

        for (int i = 0; i < projectileItems.size(); i++) {
            ItemStack itemstack = projectileItems.get(i);
            if (!itemstack.isEmpty()) {
                float f4 = f2 + f3 * (float) ((i + 1) / 2) * f1;
                f3 = -f3;
                Projectile projectile = new BaseBulletEntity(level);
                projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + f4, 0.0F, velocity, inaccuracy);

                level.addFreshEntity(projectile);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.damage", component.damage()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.critical", component.critical() * 100).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.terra_guns.knockback", component.knockback()).withStyle(ChatFormatting.GRAY));
    }

    public int getPenetrate() {
        return component.penetrate();
    }

    public int getCooldown() {
        return component.cooldown();
    }
}
