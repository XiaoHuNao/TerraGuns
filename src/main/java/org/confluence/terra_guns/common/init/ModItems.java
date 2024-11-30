package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BulletItem;
import org.confluence.terra_guns.common.item.gun.CustomGunItem;
import org.confluence.terra_guns.common.item.gun.ShotgunItem;
import org.confluence.terra_guns.common.util.TrUtil;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEM_GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    ;
    public static final DeferredRegister.Items ITEM_BULLETS = DeferredRegister.createItems(TerraGuns.MODID);


    public static final DeferredItem<Item> RED_RYDER = ITEM_GUNS.registerItem("red_ryder",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(38))
    );
    public static final DeferredItem<Item> FLINTLOCK_PISTOL = ITEM_GUNS.registerItem("flintlock_pistol",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(16))
    );
    public static final DeferredItem<Item> MUSKET = ITEM_GUNS.registerItem("musket",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(32))
    );
    public static final DeferredItem<Item> THE_UNDERTAKER = ITEM_GUNS.registerItem("the_undertaker",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(20))
    );
    public static final DeferredItem<Item> REVOLVER = ITEM_GUNS.registerItem("revolver",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(22))
    );
    public static final DeferredItem<Item> MINISHARK = ITEM_GUNS.registerItem("mini_shark",
            (properties) -> new CustomGunItem().setUseDuration(stack -> TrUtil.trToMcTick(8))
    );
    public static final DeferredItem<Item> BOOMSTICK = ITEM_GUNS.registerItem("boom_stick",
            (properties) -> new ShotgunItem(3, 10.0F)
    );


    public static final DeferredItem<Item> MUSKET_BULLET = ITEM_BULLETS.registerItem("musket_bullet", (properties) -> new BulletItem(7.0F, 2.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> METEOR_BULLET = ITEM_BULLETS.registerItem("meteor_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> SILVER_BULLET = ITEM_BULLETS.registerItem("silver_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> CRYSTAL_BULLET = ITEM_BULLETS.registerItem("crystal_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> CURSED_BULLET = ITEM_BULLETS.registerItem("cursed_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> CHLOROPHYTE_BULLET = ITEM_BULLETS.registerItem("chlorophyte_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> HV_BULLET = ITEM_BULLETS.registerItem("high_velocity_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> ICHOR_BULLET = ITEM_BULLETS.registerItem("ichor_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> VENOM_BULLET = ITEM_BULLETS.registerItem("venom_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> PARTY_BULLET = ITEM_BULLETS.registerItem("party_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> NANO_BULLET = ITEM_BULLETS.registerItem("nano_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> EXPLODING_BULLET = ITEM_BULLETS.registerItem("exploding_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> GOLDEN_BULLET = ITEM_BULLETS.registerItem("golden_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> ENDLESS_MUSKET_POUCH = ITEM_BULLETS.registerItem("endless_musket_pouch", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> LUMINITE_BULLET = ITEM_BULLETS.registerItem("luminite_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
    public static final DeferredItem<Item> TUNGSTEN_BULLET = ITEM_BULLETS.registerItem("tungsten_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));


    public static class Tab {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraGuns.MODID);

        public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TAB.register("terra_guns_tab",
                () -> CreativeModeTab.builder().icon(Items.DIAMOND::getDefaultInstance)
                        .title(Component.translatable("creativetab.terra_guns"))
                        .displayItems((parameters, output) -> {
                            ITEM_GUNS.getEntries().forEach(itemDeferredHolder -> output.accept(itemDeferredHolder.get().getDefaultInstance()));
                            ITEM_BULLETS.getEntries().forEach(itemDeferredHolder -> output.accept(itemDeferredHolder.get().getDefaultInstance()));
                        })
                        .build()
        );
    }
}
