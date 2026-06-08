package org.confluence.terra_guns.common.init;

import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.component.BulletPropertyComponent;
import org.confluence.terra_guns.common.component.GunPropertyComponent;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.registries.PortDataComponentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;

public class TGDataComponents {
    public static void init() {}

    public static final PortDataComponentRegistration DATA_COMPONENTS = PortRegisterHandler.dataComponent(TerraGuns.MODID);

    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<GunPropertyComponent>> GUN_PROPERTY_COMPONENT = DATA_COMPONENTS.builder("gun_property", GunPropertyComponent::fastBuilder);
    public static final PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<BulletPropertyComponent>> BULLET_PROPERTY_COMPONENT = DATA_COMPONENTS.builder("bullet_property", BulletPropertyComponent::fastBuilder);
}

