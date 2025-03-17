package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.AmmoItem;
import org.confluence.terra_guns.common.item.gun.GeoGunItem;
import org.confluence.terra_guns.common.item.gun.ShotgunItem;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class TGItems {
    public static final DeferredRegister.Items ITEM_GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredRegister.Items ITEM_BULLETS = DeferredRegister.createItems(TerraGuns.MODID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TerraGuns.MODID);

    public static final DeferredItem<Item> FLINTLOCK_PISTOL = registerNormalGun("flintlock_pistol", /*14*/ 9.3f, 5.35f, 16, 5.75f, 0.04f);
    public static final DeferredItem<Item> MUSKET = registerNormalGun("musket", /*31*/ 20.7f, 9, 32, 5.25f, 0.11f);
    public static final DeferredItem<Item> THE_UNDERTAKER = registerNormalGun("the_undertaker", /*19*/ 12.7f, 6, 2, 20, 0.04f);
    public static final DeferredItem<Item> REVOLVER = registerNormalGun("revolver", /*20*/ 13.3f, 16, 22, 4, 0.09f);
    public static final DeferredItem<Item> MINISHARK = registerNormalGun("minishark", /*6*/ 4, 7, 8, 0, 0.04f);
    public static final DeferredItem<Item> FLARE_GUN = registerNormalGun("flare_gun", 0, 6, 18, 0, 0);
    public static final DeferredItem<Item> BLOWGUN = registerNormalGun("blowgun", /*27*/ 18, 13, 35,4, 4);
    public static final DeferredItem<Item> BLOWPIPE = registerNormalGun("blowpipe", /*9*/ 6, 11, 8, 3.5f, 0.04f);
    public static final DeferredItem<Item> HANGGUN = registerNormalGun("handgun", /*25*/ 16.7f, 10, 15, 1, 0.04f);
    public static final DeferredItem<Item> ONYX_BLASTER = registerNormalGun("onyx_blaster", /*24*/ 16, 14, 48, 6.5f, 0.04f);
    public static final DeferredItem<Item> PHOENIX_BLASTER = registerNormalGun("phoenix_blaster", /*30*/ 20, 13, 14, 2, 0.04f);
    public static final DeferredItem<Item> SANDGUN = registerNormalGun("sandgun", /*30*/ 20, 24, 14, 5, 0.04f);
    public static final DeferredItem<Item> SLIME_GUN = registerNormalGun("slime_gun", 0, 24, 12, 0, 0);
    public static final DeferredItem<Item> SNIPER_RIFLE = registerNormalGun("sniper_rifle", /*185*/ 123.3f, 16, 36, 8, 0.29f);
    public static final DeferredItem<Item> SNOWBALL_CANNON = registerNormalGun("snowball_cannon", /*10*/ 6.5f, 11, 19, 1, 0.04f);
    public static final DeferredItem<Item> STAR_CANNON = registerNormalGun("star_cannon", /*55*/ 36.7f, 14, 14, 3, 0.04f);
    public static final DeferredItem<Item> TACTICAL_SHOTGUN = registerNormalGun("tactical_shotgun", /*29*/ 19.3f, 6, 34, 7, 0.04f);
    public static final DeferredItem<Item> UZI = registerNormalGun("uzi", /*30*/ 20, 13, 9, 3.5f, 0.04f);
    public static final DeferredItem<Item> BOOMSTICK = registerShotGun("boomstick", 3, /*14*/ 9.3f, 5.35f, 40, 5.75f, 0.04f, 10);
    public static final DeferredItem<Item> SHOTGUN = registerShotGun("shotgun", 3, /*24*/ 16, 7, 45, 6.5f ,0.04f, 10);

    public static final DeferredItem<Item> MUSKET_BULLET = ITEM_BULLETS.registerItem("musket_bullet", properties -> new AmmoItem(/*7*/ 4.6F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> METEOR_BULLET = ITEM_BULLETS.registerItem("meteor_bullet", properties -> new AmmoItem(/*8*/ 5.3F, 3.0F, 2.0F, 0));
    public static final DeferredItem<Item> SILVER_BULLET = ITEM_BULLETS.registerItem("silver_bullet", properties -> new AmmoItem(/*9*/ 6.0F, 4.5F, 3.0F, 0));
    public static final DeferredItem<Item> CRYSTAL_BULLET = ITEM_BULLETS.registerItem("crystal_bullet", properties -> new AmmoItem(/*9*/ 6.0F, 5.0F, 1.0F, 0));
    public static final DeferredItem<Item> CURSED_BULLET = ITEM_BULLETS.registerItem("cursed_bullet", properties -> new AmmoItem(/*12*/ 8.0F, 5.0F, 4.0F, 0));
    public static final DeferredItem<Item> CHLOROPHYTE_BULLET = ITEM_BULLETS.registerItem("chlorophyte_bullet", properties -> new AmmoItem(/*9*/ 6.0F, 5.0F, 4.5F, 0));
    public static final DeferredItem<Item> HIGH_VELOCITY_BULLET = ITEM_BULLETS.registerItem("high_velocity_bullet", properties -> new AmmoItem(/*11*/ 7.3F, 4.0F, 8.0F, 0));
    public static final DeferredItem<Item> ICHOR_BULLET = ITEM_BULLETS.registerItem("ichor_bullet", properties -> new AmmoItem(/*13*/ 8.7F, 5.25F, 4.0F, 0));
    public static final DeferredItem<Item> VENOM_BULLET = ITEM_BULLETS.registerItem("venom_bullet", properties -> new AmmoItem(/*15*/ 10.0F, 5.3F, 4.1F, 0));
    public static final DeferredItem<Item> PARTY_BULLET = ITEM_BULLETS.registerItem("party_bullet", properties -> new AmmoItem(/*10*/ 6.7F, 5.1F, 5.0F, 0));
    public static final DeferredItem<Item> NANO_BULLET = ITEM_BULLETS.registerItem("nano_bullet", properties -> new AmmoItem(/*15*/ 10.0F, 4.6F, 3.6F, 0));
    public static final DeferredItem<Item> EXPLODING_BULLET = ITEM_BULLETS.registerItem("exploding_bullet", properties -> new AmmoItem(/*10*/ 6.7F, 4.7F, 6.6F, 0));
    public static final DeferredItem<Item> GOLDEN_BULLET = ITEM_BULLETS.registerItem("golden_bullet", properties -> new AmmoItem(/*10*/ 6.7F, 4.6F, 3.6F, 0));
    public static final DeferredItem<Item> ENDLESS_MUSKET_POUCH = ITEM_BULLETS.registerItem("endless_musket_pouch", properties -> new AmmoItem(/*7*/ 4.7F, 4.0F, 2.0F, 0) {
        @Override
        public boolean isInfinite(Player shooter, ItemStack ammoStack, ItemStack gunStack) {
            return true;
        }
    });
    public static final DeferredItem<Item> LUMINITE_BULLET = ITEM_BULLETS.registerItem("luminite_bullet", properties -> new AmmoItem(/*20*/ 13.3F, 2.0F, 3.0F, 0));
    public static final DeferredItem<Item> TUNGSTEN_BULLET = ITEM_BULLETS.registerItem("tungsten_bullet", properties -> new AmmoItem(/*9*/ 6.0F, 4.5F, 4.0F, 0));

    public static void register(IEventBus eventBus) {
        ITEM_GUNS.register(eventBus);
        ITEM_BULLETS.register(eventBus);
        if (!TerraGuns.IS_CONFLUENCE_LOADED) {
            CREATIVE_MODE_TAB.register(eventBus);
            CREATIVE_MODE_TAB.register("terra_guns_tab",
                    () -> CreativeModeTab.builder().icon(STAR_CANNON.get()::getDefaultInstance)
                            .title(Component.translatable("creativetab.terra_guns"))
                            .displayItems((parameters, output) -> {
                                ITEM_GUNS.getEntries().forEach(itemDeferredHolder -> output.accept(itemDeferredHolder.get()));
                                ITEM_BULLETS.getEntries().forEach(itemDeferredHolder -> output.accept(itemDeferredHolder.get()));
                            }).build());
        }
    }

    public static DeferredItem<Item> registerNormalGun(String name, float baseDamage, float weaponSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        return ITEM_GUNS.registerItem(name, properties -> new GeoGunItem<>(baseDamage, weaponSpeed, useDelay, knockBack, crit, inaccuracy));
    }
    public static DeferredItem<Item> registerNormalGun(String name, float baseDamage, float weaponSpeed, int useDelay, float knockBack, float crit) {
        return ITEM_GUNS.registerItem(name, properties -> new GeoGunItem<>(baseDamage, weaponSpeed, useDelay, knockBack, crit));
    }

    public static DeferredItem<Item> registerShotGun(String name, int bulletCount, float damage, float weaponSpeed, int useDelay, float knockBack, float crit) {
        return ITEM_GUNS.registerItem(name, properties -> new ShotgunItem(bulletCount, damage, weaponSpeed, useDelay, knockBack, crit));
    }
    public static DeferredItem<Item> registerShotGun(String name, int bulletCount, float damage, float weaponSpeed, int useDelay, float knockBack, float crit, float inaccuracy) {
        return ITEM_GUNS.registerItem(name, properties -> new ShotgunItem(bulletCount, damage, weaponSpeed, useDelay, knockBack, crit, inaccuracy));
    }
}
