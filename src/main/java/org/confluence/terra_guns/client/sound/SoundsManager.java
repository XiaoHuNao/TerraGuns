package org.confluence.terra_guns.client.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGSoundEvents;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SoundsManager {
    protected static Map<BaseGun, SoundEvent> soundMap = new HashMap<>();
    static {
        soundMap.put(TGItems.HAND_GUN.get(), TGSoundEvents.GUN_AUTO.get());
        soundMap.put(TGItems.SHOTGUN.get(), TGSoundEvents.SHOTGUN_MULTI.get());
    }

    public static void putSound(Supplier<BaseGun> item, Supplier<SoundEvent> soundEvent){
        soundMap.put(item.get(), soundEvent.get());
    }

    public static void putSound(BaseGun item, SoundEvent soundEvent){
        soundMap.put(item, soundEvent);
    }

    public static SoundEvent getSound(ItemStack itemStack) {
        return getSound(itemStack.getItem());
    }
    public static SoundEvent getSound(Item item) {
        return soundMap.get(item);
    }
}
