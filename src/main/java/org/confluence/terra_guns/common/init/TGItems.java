package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.function.Supplier;

public class TGItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final Supplier<BaseGun> HAND_GUN = ITEMS.registerItem("hand_gun", properties -> new BaseGun(properties, 15, 26, 10, 3, 0.04f, 1, ModRarity.GREEN));

    public static final Supplier<BaseBullet> MUSKET_BULLET = ITEMS.registerItem("musket_bullet", properties -> new BaseBullet(properties, 7, 4, 2, 2, ModRarity.WHITE, false));

}
