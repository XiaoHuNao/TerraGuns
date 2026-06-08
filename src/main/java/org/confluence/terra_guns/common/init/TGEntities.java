package org.confluence.terra_guns.common.init;

import PortLib.extensions.net.minecraftforge.registries.DeferredRegister.PortDeferredRegisterExtension;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.entity.bullet.BaseBulletEntity;
import org.confluence.terra_guns.common.entity.bullet.CustomBulletEntity;

public class TGEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, TerraGuns.MODID);

    public static final RegistryObject<EntityType<BaseBulletEntity>> BASE_BULLET_ENTITY = PortDeferredRegisterExtension.register(ENTITY_TYPES, "base_bullet", id -> EntityType.Builder.<BaseBulletEntity>of(BaseBulletEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).build(id.toString()));
    public static final RegistryObject<EntityType<CustomBulletEntity>> GRAVITY_BULLET_ENTITY = PortDeferredRegisterExtension.register(ENTITY_TYPES, "gravity_bullet", id -> EntityType.Builder.<CustomBulletEntity>of(CustomBulletEntity::new, MobCategory.MISC).sized(0.1f, 0.1f).build(id.toString()));
}
