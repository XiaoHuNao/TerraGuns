package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.function.Supplier;

/**
 * tr2mc数值转换
 * 时间 三分之一
 * 伤害 五分之一
 * 速度 八分之一
 */
public class TGItems {
    public static final DeferredRegister.Items GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredRegister.Items BULLETS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final Supplier<BaseGun> HAND_GUN = GUNS.registerItem("hand_gun", properties -> new BaseGun(properties, 5, 5.2f, 1.25f, 3, 0.04f, 1, ModRarity.GREEN));

    public static final Supplier<BaseBullet> MUSKET_BULLET = BULLETS.registerItem("musket_bullet", properties -> new BaseBullet(properties.stacksTo(99), 1.5f, 0.5f, 2, 2, ModRarity.WHITE, false));
    public static final Supplier<BaseBullet> ENDLESS_MUSKET_POUCH = BULLETS.registerItem("endless_musket_pouch", properties -> new BaseBullet(properties.stacksTo(1), 1.5f, 0.5f, 2, 2, ModRarity.GREEN, true));

}
