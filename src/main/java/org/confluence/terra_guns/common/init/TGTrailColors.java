package org.confluence.terra_guns.common.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.confluence.terra_guns.common.init.TGItems.*;

public class TGTrailColors {
    protected static Map<String, Integer> colorMap = new HashMap<>();

    public static void init() {
        // Colors are sampled from the corresponding item textures. Keep the
        // alpha opaque because the trail/head renderer applies its own fade.
        putColor(MUSKET_BULLET, 0xFF9A8E87);
        putColor(METEOR_SHOT, 0xFFD65A4B);
        putColor(SILVER_BULLET, 0xFFB4CDD8);
        putColor(CRYSTAL_BULLET, 0xFF9E68FF);
        putColor(CURSED_BULLET, 0xFFD8F53E);
        putColor(CHLOROPHYTE_BULLET, 0xFF65D64D);
        putColor(HIGH_VELOCITY_BULLET, 0xFFEAC76A);
        putColor(ICHOR_BULLET, 0xFFF4B951);
        putColor(VENOM_BULLET, 0xFFB277E0);
        putColor(PARTY_BULLET, 0xFF58D98B);
        putColor(NANO_BULLET, 0xFF14D5F0);
        putColor(EXPLODING_BULLET, 0xFFE34C42);
        putColor(GOLDEN_BULLET, 0xFFF0C86B);
        putColor(LUMINITE_BULLET, 0xFF5CE6C2);
        putColor(TUNGSTEN_BULLET, 0xFF85AA73);
    }

    public static void putColor(String item) {
        putColor(item, 0xFFFD3E03);
    }

    public static void putColor(String item, int red, int green, int blue, int alpha) {
        putColor(item, FastColor.ARGB32.color(red, green, blue, alpha));
    }

    public static void putColor(Supplier<? extends Item> item, int color) {
        putColor(item.get(), color);
    }

    public static void putColor(Item item, int color) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        colorMap.put(path, color);
    }

    public static void putColor(String item, int color) {
        colorMap.put(item, color);
    }

    public static int getColor(ItemStack itemStack) {
        try {
            return getColor((BaseBullet) itemStack.getItem());
        } catch (NullPointerException e) {
            TerraGuns.LOGGER.error("Can't find trail color", e);
        }
        return 0;
    }

    public static int getColor(BaseBullet item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        String selectID = item.colorID() == null ? path : item.colorID();
        return getColor(selectID);
    }

    public static int getColor(String item) {
        return colorMap.getOrDefault(item, 0xFFFD3E03);
    }
}
