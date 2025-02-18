package org.confluence.terra_guns.common.item.gun;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaGunItem extends CustomGunItem {
    private final int manaCost;

    public ManaGunItem(int manaCost) {
        this.manaCost = manaCost;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(itemInHand);
        }
        if (player instanceof ServerPlayer serverPlayer && consumeMana(serverPlayer)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemInHand);
        }
        return InteractionResultHolder.fail(itemInHand);
    }

    private boolean consumeMana(ServerPlayer player) {
        return false; // Confluence mixin here
    }
}
