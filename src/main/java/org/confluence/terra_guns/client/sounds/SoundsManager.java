package org.confluence.terra_guns.client.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGSoundEvents;

import java.util.HashMap;
import java.util.Map;

public class SoundsManager {
    protected static Map<Item, SoundEvent> soundMap = new HashMap<>();
    static {
        soundMap.put(TGItems.HAND_GUN.get(), TGSoundEvents.GUN_AUTO.get());
    }

    public static SoundEvent getSound(ItemStack itemStack) {
        return getSound(itemStack.getItem());
    }
    public static SoundEvent getSound(Item item) {
        return soundMap.get(item);
    }
}
