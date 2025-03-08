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

    public static final DeferredItem<Item> FLINTLOCK_PISTOL = registerNormalGun("flintlock_pistol", 16);
    public static final DeferredItem<Item> MUSKET = registerNormalGun("musket", 32);
    public static final DeferredItem<Item> THE_UNDERTAKER = registerNormalGun("the_undertaker", 20);
    public static final DeferredItem<Item> REVOLVER = registerNormalGun("revolver", 22);
    public static final DeferredItem<Item> MINISHARK = registerNormalGun("minishark", 8);
    public static final DeferredItem<Item> FLARE_GUN = registerNormalGun("flare_gun", 18);
    public static final DeferredItem<Item> BLOWGUN = registerNormalGun("blowgun", 35);
    public static final DeferredItem<Item> BLOWPIPE = registerNormalGun("blowpipe", 8);
    public static final DeferredItem<Item> HANGGUN = registerNormalGun("handgun", 15);
    public static final DeferredItem<Item> ONYX_BLASTER = registerNormalGun("onyx_blaster", 48);
    public static final DeferredItem<Item> PHOENIX_BLASTER = registerNormalGun("phoenix_blaster", 14);
    public static final DeferredItem<Item> SANDGUN = registerNormalGun("sandgun", 14);
    public static final DeferredItem<Item> SLIME_GUN = registerNormalGun("slime_gun", 12);
    public static final DeferredItem<Item> SNIPER_RIFLE = registerNormalGun("sniper_rifle", 36);
    public static final DeferredItem<Item> SNOWBALL_CANNON = registerNormalGun("snowball_cannon", 19);
    public static final DeferredItem<Item> STAR_CANNON = registerNormalGun("star_cannon", 14);
    public static final DeferredItem<Item> TACTICAL_SHOTGUN = registerNormalGun("tactical_shotgun", 34);
    public static final DeferredItem<Item> UZI = registerNormalGun("uzi", 9);
    public static final DeferredItem<Item> BOOMSTICK = registerShotGun("boomstick", 3, 10);
    public static final DeferredItem<Item> SHOTGUN = registerShotGun("shotgun", 3, 10);

    public static final DeferredItem<Item> MUSKET_BULLET = ITEM_BULLETS.registerItem("musket_bullet", properties -> new AmmoItem(7.0F, 2.0F, 2.0F, 0));
    public static final DeferredItem<Item> METEOR_BULLET = ITEM_BULLETS.registerItem("meteor_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> SILVER_BULLET = ITEM_BULLETS.registerItem("silver_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> CRYSTAL_BULLET = ITEM_BULLETS.registerItem("crystal_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> CURSED_BULLET = ITEM_BULLETS.registerItem("cursed_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> CHLOROPHYTE_BULLET = ITEM_BULLETS.registerItem("chlorophyte_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> HIGH_VELOCITY_BULLET = ITEM_BULLETS.registerItem("high_velocity_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> ICHOR_BULLET = ITEM_BULLETS.registerItem("ichor_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> VENOM_BULLET = ITEM_BULLETS.registerItem("venom_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> PARTY_BULLET = ITEM_BULLETS.registerItem("party_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> NANO_BULLET = ITEM_BULLETS.registerItem("nano_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> EXPLODING_BULLET = ITEM_BULLETS.registerItem("exploding_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> GOLDEN_BULLET = ITEM_BULLETS.registerItem("golden_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> ENDLESS_MUSKET_POUCH = ITEM_BULLETS.registerItem("endless_musket_pouch", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0) {
        @Override
        public boolean isInfinite(Player shooter, ItemStack ammoStack, ItemStack gunStack) {
            return true;
        }
    });
    public static final DeferredItem<Item> LUMINITE_BULLET = ITEM_BULLETS.registerItem("luminite_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));
    public static final DeferredItem<Item> TUNGSTEN_BULLET = ITEM_BULLETS.registerItem("tungsten_bullet", properties -> new AmmoItem(7.0F, 4.0F, 2.0F, 0));

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

    public static DeferredItem<Item> registerNormalGun(String name, int duration) {
        return ITEM_GUNS.registerItem(name, properties -> new GeoGunItem<>() {
            @Override
            protected int getUseDelay(Player shooter, ItemStack gunStack, ItemStack ammoStack) {
                return duration;
            }
        });
    }

    public static DeferredItem<Item> registerShotGun(String name, int bulletCount, float inaccuracy) {
        return ITEM_GUNS.registerItem(name, properties -> new ShotgunItem(bulletCount, inaccuracy));
    }
}
