package org.confluence.terra_guns.common.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BulletItem;
import org.confluence.terra_guns.common.item.gun.GunItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEM_GUNS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraGuns.MODID);
    public static final DeferredRegister<Item> ITEM_BULLETS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraGuns.MODID);

    public static final RegistryObject<Item> GUN = ITEM_GUNS.register("gun", GunItem::new);

    public static final RegistryObject<Item> BULLET = ITEM_BULLETS.register("bullet", () -> new BulletItem(1.0F, 0.0F, 1.0F, 0.0F));






}
