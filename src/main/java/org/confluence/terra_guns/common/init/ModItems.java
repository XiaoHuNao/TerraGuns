package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BulletItem;
import org.confluence.terra_guns.common.item.gun.GunItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEM_GUNS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraGuns.MODID);
    public static final DeferredRegister<Item> ITEM_BULLETS = DeferredRegister.create(ForgeRegistries.ITEMS, TerraGuns.MODID);

    public static final RegistryObject<Item> GUN = ITEM_GUNS.register("gun", GunItem::new);

    //MusketBullet
    public static final RegistryObject<Item> MUSKET_BULLET = ITEM_BULLETS.register("musket_bullet", () -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));


    public static class Tab {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraGuns.MODID);

        public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TAB.register("terra_guns_tab",
                () -> CreativeModeTab.builder().icon(() -> Items.DIAMOND.getDefaultInstance())
                        .title(Component.translatable("creativetab.terra_guns"))
                        .displayItems((parameters, output) -> {
                            ITEM_GUNS.getEntries().stream().map(RegistryObject::get).forEach(item -> output.accept(item.getDefaultInstance()));
                            ITEM_BULLETS.getEntries().stream().map(RegistryObject::get).forEach(item -> output.accept(item.getDefaultInstance()));
                        })
                        .build()
        );
    }



}
