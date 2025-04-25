package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.lib.common.component.ModRarity;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.bullet.BaseBullet;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.confluence.terra_guns.common.item.gun.Shotgun;

import java.util.function.Supplier;

/**
 * tr2mc数值转换
 * 时间 三分之一
 * 伤害 五分之一
 * 速度 八分之一
 * 击退 二十分之一
 */
public class TGItems {
    public static final DeferredRegister.Items GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredRegister.Items BULLETS = DeferredRegister.createItems(TerraGuns.MODID);

    public static final DeferredItem<BaseGun> HAND_GUN = GUNS.registerItem("hand_gun", properties -> new BaseGun(properties, 5, 5.2f, 1.25f, 0.15f, 0.04f, ModRarity.GREEN)); // 手枪
    public static final DeferredItem<BaseGun> FLINTLOCK_PISTOL = GUNS.registerItem("flintlock_pistol", properties -> new BaseGun(properties, 5, 2.6f, 0.6f, 0.05f, 0.04f, ModRarity.BLUE)); // 燧发枪
    public static final DeferredItem<BaseGun> THE_UNDERTAKER = GUNS.registerItem("the_undertaker", properties -> new BaseGun(properties, 8, 3.8f, 0.75f, 0.1f, 0.04f, ModRarity.BLUE)); // 夺命枪
    public static final DeferredItem<BaseGun> MUSKET = GUNS.registerItem("musket", properties -> new BaseGun(properties, 10, 6.2f, 1.1f, 0.25f, 0.11f, ModRarity.BLUE)); // 火枪

    public static final DeferredItem<BaseGun> BOOMSTICK = GUNS.registerItem("boomstick", properties -> new Shotgun(properties, 13, 2.8f, 0.66f, 0.28f, 0.04f, ModRarity.GREEN, 3, 4)); // 三发猎枪
    public static final DeferredItem<BaseGun> SHOTGUN = GUNS.registerItem("shotgun", properties -> new Shotgun(properties, 15, 4.8f, 0.9f, 0.375f, 0.04f, ModRarity.LIGHT_RED, 3, 5)); // 霰弹枪

    public static final DeferredItem<BaseGun> MINISHARK = GUNS.registerItem("minishark", properties -> new BaseGun(properties, 3, 1.2f, 1, 0, 0.04f, ModRarity.GREEN)); // 迷你鲨


    public static final DeferredItem<BaseBullet> MUSKET_BULLET = BULLETS.registerItem("musket_bullet", properties -> new BaseBullet(properties.stacksTo(99), 1.5f, 0.5f, 2, 0.1f, ModRarity.WHITE, 0, false));
    public static final DeferredItem<BaseBullet> ENDLESS_MUSKET_POUCH = BULLETS.registerItem("endless_musket_pouch", properties -> new BaseBullet(properties.stacksTo(1), 1.5f, 0.5f, 2, 0.1f, ModRarity.GREEN, 0, true));

}
