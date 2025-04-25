package org.confluence.terra_guns.impl;

import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.confluence.terra_guns.common.init.TGItems.*;

public class TrailColorProvider {
    protected static Map<BaseBullet, Integer> colorMap = new HashMap<>();
    static {
        putColor(MUSKET_BULLET, 0xFFFD3E03);
    }

    public static void putColor(Supplier<BaseBullet> item){
        putColor(item, 0xFFFD3E03);
    }

    public static void putColor(Supplier<BaseBullet> item, int color){
        putColor(item.get(), color);
    }

    public static void putColor(BaseBullet item, int red, int green, int blue, int alpha){
        putColor(item, FastColor.ARGB32.color(red, green, blue, alpha));
    }

    public static void putColor(BaseBullet item, int color){
        colorMap.put(item, color);
    }

    public static int getColor(ItemStack itemStack) {
        return getColor((BaseBullet) itemStack.getItem());
    }

    public static int getColor(BaseBullet item) {
        return colorMap.get(item);
    }
}
