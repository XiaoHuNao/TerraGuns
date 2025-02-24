package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;

import java.util.function.Supplier;

public final class TGSoundEvents {
    public static final DeferredRegister<SoundEvent> EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, TerraGuns.MODID);

    public static final Supplier<SoundEvent> LASER = register("laser");
    public static final Supplier<SoundEvent> SHOOT = register("shoot");

    private static Supplier<SoundEvent> register(String name) {
        return EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(TerraGuns.asResource(name)));
    }
}
