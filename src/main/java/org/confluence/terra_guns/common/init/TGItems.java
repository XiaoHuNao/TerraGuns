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

import java.util.function.Supplier;

public class TGItems {
    public static final DeferredRegister.Items ITEM_GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredRegister.Items ITEM_BULLETS = DeferredRegister.createItems(TerraGuns.MODID);


    public static final DeferredItem<Item> FLINTLOCK_PISTOL = registerNormalGun("flintlock_pistol", 16);
    public static final DeferredItem<Item> MUSKET = registerNormalGun("musket", 32);
    public static final DeferredItem<Item> THE_UNDERTAKER = registerNormalGun("the_undertaker", 20);
    public static final DeferredItem<Item> REVOLVER = registerNormalGun("revolver", 22);
    public static final DeferredItem<Item> MINISHARK = registerNormalGun("minishark", 8);
    public static final DeferredItem<Item> FLARE_GUN = registerNormalGun("flare_gun", 18);
    public static final DeferredItem<Item> BEE_GUN = registerNormalGun("bee_gun", 12);
    public static final DeferredItem<Item> BLOWGUN = registerNormalGun("blowgun", 35);
    public static final DeferredItem<Item> BLOWPIPE = registerNormalGun("blowpipe", 8);
    public static final DeferredItem<Item> HANGGUN = registerNormalGun("handgun", 15);
    public static final DeferredItem<Item> ONYX_BLASTER = registerNormalGun("onyx_blaster", 48);
    public static final DeferredItem<Item> PHOENIX_BLASTER = registerNormalGun("phoenix_blaster", 14);
    public static final DeferredItem<Item> SANDGUN = registerNormalGun("sandgun", 14);
    public static final DeferredItem<Item> SLIME_GUN = registerNormalGun("slime_gun", 12);
    public static final DeferredItem<Item> SNIPER_RIFLE = registerNormalGun("sniper_rifle", 36);
    public static final DeferredItem<Item> SNOWBALL_CANNON = registerNormalGun("snowball_cannon", 19);
    public static final DeferredItem<Item> SPACE_GUN = registerNormalGun("space_gun", 17);
    public static final DeferredItem<Item> STAR_CANNON = registerNormalGun("star_cannon", 14);
    public static final DeferredItem<Item> TACTICAL_SHOTGUN = registerNormalGun("tactical_shotgun", 34);
    public static final DeferredItem<Item> UZI = registerNormalGun("uzi", 9);
    public static final DeferredItem<Item> BOOMSTICK = registerShotGun("boomstick", 3, 10);
    public static final DeferredItem<Item> SHOTGUN = registerShotGun("shotgun", 3, 10);

    public static final DeferredItem<Item> MUSKET_BULLET = ITEM_BULLETS.registerItem("musket_bullet", (properties) -> new BulletItem(7.0F, 2.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> METEOR_BULLET = ITEM_BULLETS.registerItem("meteor_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> SILVER_BULLET = ITEM_BULLETS.registerItem("silver_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> CRYSTAL_BULLET = ITEM_BULLETS.registerItem("crystal_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> CURSED_BULLET = ITEM_BULLETS.registerItem("cursed_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> CHLOROPHYTE_BULLET = ITEM_BULLETS.registerItem("chlorophyte_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> HV_BULLET = ITEM_BULLETS.registerItem("high_velocity_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> ICHOR_BULLET = ITEM_BULLETS.registerItem("ichor_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> VENOM_BULLET = ITEM_BULLETS.registerItem("venom_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> PARTY_BULLET = ITEM_BULLETS.registerItem("party_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> NANO_BULLET = ITEM_BULLETS.registerItem("nano_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> EXPLODING_BULLET = ITEM_BULLETS.registerItem("exploding_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> GOLDEN_BULLET = ITEM_BULLETS.registerItem("golden_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> ENDLESS_MUSKET_POUCH = ITEM_BULLETS.registerItem("endless_musket_pouch", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> LUMINITE_BULLET = ITEM_BULLETS.registerItem("luminite_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));
//    public static final DeferredItem<Item> TUNGSTEN_BULLET = ITEM_BULLETS.registerItem("tungsten_bullet", (properties) -> new BulletItem(7.0F, 4.0F, 2.0F, 2.0F));


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

    public static DeferredItem<Item> registerNormalGun(String name, int duration) {
        return ITEM_GUNS.registerItem(name,
                (properties) -> new CustomGunItem().setUseDuration(stack -> duration)
        );
    }

    public static DeferredItem<Item> registerShotGun(String name, int bulletCount, float inaccuracy) {
        return ITEM_GUNS.registerItem(name,
                (properties) -> new ShotgunItem(bulletCount, inaccuracy)
        );
    }

    public static DeferredItem<Item> registerShotGun(String name, float projectileSpeed, int bulletCount, float inaccuracy) {
        return ITEM_GUNS.registerItem(name,
                (properties) -> new ShotgunItem(projectileSpeed, bulletCount, inaccuracy)
        );
    }
}
