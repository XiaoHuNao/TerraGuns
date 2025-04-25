package org.confluence.terra_guns.impl;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.confluence.terra_guns.common.init.TGItems.*;
import static org.confluence.terra_guns.common.init.TGSoundEvents.*;

public class SoundsProvider {
    protected static Map<BaseGun, SoundEvent> soundMap = new HashMap<>();
    static {
        putSound(HAND_GUN, GUN_AUTO);
        putSound(SHOTGUN, SHOTGUN_MULTI);
        putSound(FLINTLOCK_PISTOL);
        putSound(BOOMSTICK, SHOTGUN_MULTI);
        putSound(THE_UNDERTAKER);
        putSound(MUSKET);
        putSound(MINISHARK);
    }

    public static void putSound(Supplier<BaseGun> item){
        putSound(item, GUN_GENERIC);
    }

    public static void putSound(Supplier<BaseGun> item, Supplier<SoundEvent> soundEvent){
        putSound(item.get(), soundEvent.get());
    }

    public static void putSound(BaseGun item, SoundEvent soundEvent){
        soundMap.put(item, soundEvent);
    }

    public static SoundEvent getSound(ItemStack itemStack) {
        return getSound((BaseGun) itemStack.getItem());
    }
    public static SoundEvent getSound(BaseGun item) {
        return soundMap.get(item);
    }
}
