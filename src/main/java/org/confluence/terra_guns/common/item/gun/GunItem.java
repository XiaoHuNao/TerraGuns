package org.confluence.terra_guns.common.item.gun;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.confluence.terra_guns.api.IBullet;
import org.confluence.terra_guns.api.IGun;
import org.confluence.terra_guns.common.entity.BaseAmmoEntity;
import org.confluence.terra_guns.common.init.ModAttributes;
import org.confluence.terra_guns.common.init.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class GunItem extends ProjectileWeaponItem implements IGun {
    public GunItem() {
        super(new Properties());
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        ItemStack ammo = player.getProjectile(gun);

        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(gun);
        }
        boolean flag = !ammo.isEmpty();

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(gun);
        } else {
            player.startUsingItem(hand);
//            player.getCooldowns().addCooldown(this, getUseDelay(gun,ammo, player));
            return InteractionResultHolder.consume(gun);
        }
    }

    private int getUseDelay(ItemStack gun, ItemStack ammo, Player player) {
        return 5;
    }

    public int getPower(ItemStack stack){
        return 0;
    }

    public int getUseDuration(ItemStack stack) {
        return getPower(stack) == 0 ? 1 : getPower(stack);
    }
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack gunStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player){
            ItemStack ammoStack = player.getProjectile(gunStack);
            boolean bulletFree = shouldConsumeAmmo(level, player,gunStack,ammoStack);

            if (!ammoStack.isEmpty() || bulletFree) {
                if (ammoStack.isEmpty()) {
                    ammoStack = ModItems.MUSKET_BULLET.get().getDefaultInstance();
                }

                if (ammoStack.getItem() instanceof IBullet bullet && gunStack.getItem() instanceof IGun gun) {
                    if (!level.isClientSide) {
                        serverShoot((ServerLevel)level, player,gunStack,ammoStack,bullet, gun, bulletFree);
                    }else {
                        gun.clientShoot((ClientLevel) level, player, gunStack, ammoStack);
                        bullet.clientShoot((ClientLevel) level, player, gunStack, ammoStack);
                    }
                }
            }
        }
        return gunStack;
    }



    @Override
    public void serverShoot(ServerLevel level, Player player, ItemStack gunStack, ItemStack ammoStack, IBullet bullet, IGun gun, boolean bulletFree) {
        Projectile projectile = bullet.createProjectile(level, player,gunStack, ammoStack);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, bullet.getProjectileSpeed(player,projectile, gunStack), bullet.getInaccuracy(player,projectile, gunStack));
        bullet.setFinalDamage((bullet.getBaseDamage() + bullet.getBonusDamage(player,projectile, gunStack)) * bullet.getDamageMultiplier(player,projectile, gunStack));
        bullet.modifyFinalProjectile(projectile, player, gunStack);

        if (projectile instanceof BaseAmmoEntity ammoEntity){
            ammoEntity.damageAndKnockback(bullet.getFinalDamage(), bullet.getKnockBack());
        }

        gun.consume(gunStack, player);
        if (bulletFree){
            bullet.consume(ammoStack, player);
        }

        level.addFreshEntity(projectile);
    }

    @Override
    public void clientShoot(ClientLevel level, Player player, ItemStack gunStack, ItemStack ammoStack) {
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) * 0.5F);
    }

    @Override
    public void consume(ItemStack gunStack, Player player) {
        gunStack.hurtAndBreak(1, player, (p_289501_) -> {
            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
        });
    }

    @Override
    public boolean shouldConsumeAmmo(Level level, Player player, ItemStack gunStack, ItemStack ammoStack) {
        boolean isInfinite = (ammoStack.getItem() instanceof IBullet bullet && bullet.isInfinite(player,gunStack));
        boolean consumeRate = player.getAttribute(ModAttributes.AMMO_CONSUME_RATE.get()).getValue() > level.random.nextDouble();
        boolean instabuild = player.getAbilities().instabuild;
        return consumeRate || instabuild;
    }



    @Override
    @NotNull
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof IBullet && ((IBullet)stack.getItem()).hasAmmo(stack);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
