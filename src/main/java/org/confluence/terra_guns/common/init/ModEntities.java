package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.entity.SimpleItemModelProjectile;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, TerraGuns.MODID);

//    public static final RegistryObject<EntityType<AmmoEntity>> AMMO = ENTITIES.register("ammo", () -> EntityType.Builder.<AmmoEntity>of(AmmoEntity::new, MobCategory.MISC)
//            .sized(0.25F, 0.25F).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
//            .build(TerraGuns.asResource("ammo").toString()));
//    public static final RegistryObject<EntityType<SimpleItemModelProjectile>> AMMO = ENTITIES.register("ammo", () -> EntityType.Builder.<SimpleItemModelProjectile>of(SimpleItemModelProjectile::new, MobCategory.MISC)
//            .sized(0.25F, 0.25F).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
//            .build(TerraGuns.asResource("ammo").toString()));
    //SimpleItemModelProjectile
    public static final DeferredHolder<EntityType<?>, EntityType<SimpleItemModelProjectile>> SIMPLE_ITEM_MODEL_PROJECTILE = ENTITIES.register("simple_item_model_projectile", () -> EntityType.Builder.<SimpleItemModelProjectile>of(SimpleItemModelProjectile::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).setUpdateInterval(2).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
            .build(TerraGuns.asResource("simple_item_model_projectile").toString()));
}
