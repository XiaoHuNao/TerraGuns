package org.confluence.terra_guns.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;

import java.util.function.Supplier;

public class TGDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TerraGuns.MODID);

    public static final Supplier<DataComponentType<GunPropertyComponent>> GUN_PROPERTY_COMPONENT = DATA_COMPONENTS.registerComponentType("gun_property", GunPropertyComponent::fastBuilder);
    public static final Supplier<DataComponentType<BulletPropertyComponent>> BULLET_PROPERTY_COMPONENT = DATA_COMPONENTS.registerComponentType("bullet_property", BulletPropertyComponent::fastBuilder);
    public static final Supplier<DataComponentType<Long>> EMERGENCY_MELEE_COOLDOWN_END = DATA_COMPONENTS.registerComponentType(
            "emergency_melee_cooldown_end",
            builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
    );
}

