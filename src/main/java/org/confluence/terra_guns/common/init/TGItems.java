package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredItem;
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
 * 击退 五分之二
 */
public class TGItems {
    public static final DeferredRegister.Items GUNS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredRegister.Items BULLETS = DeferredRegister.createItems(TerraGuns.MODID);

    public static final DeferredItem<BaseGun> HAND_GUN = GUNS.registerItem("hand_gun", properties -> new BaseGun(properties, 5, 5.2f, 1.25f, 1.2f, 0.04f, ModRarity.GREEN)); // 手枪
    public static final DeferredItem<BaseGun> SHOTGUN = GUNS.registerItem("shotgun", properties -> new BaseGun(properties, 15, 4.8f, 0.9f, 3f, 0.04f, ModRarity.LIGHT_RED)); // 霰弹枪
    public static final DeferredItem<BaseGun> FLINTLOCK_PISTOL = GUNS.registerItem("flintlock_pistol", properties -> new BaseGun(properties, 5, 2.6f, 0.6f, 0.4f, 0.04f, ModRarity.BLUE)); // 燧发枪
    public static final DeferredItem<BaseGun> BOOMSTICK = GUNS.registerItem("boomstick", properties -> new BaseGun(properties, 13, 2.8f, 0.66f, 2.3f, 0.04f, ModRarity.GREEN)); // 三发猎枪
    public static final DeferredItem<BaseGun> THE_UNDERTAKER = GUNS.registerItem("the_undertaker", properties -> new BaseGun(properties, 8, 3.8f, 0.75f, 0.8f, 0.04f, ModRarity.BLUE)); // 夺命枪
    public static final DeferredItem<BaseGun> MUSKET = GUNS.registerItem("musket", properties -> new BaseGun(properties, 10, 6.2f, 1.1f, 2.1f, 0.11f, ModRarity.BLUE)); // 火枪


    public static final DeferredItem<BaseBullet> MUSKET_BULLET = BULLETS.registerItem("musket_bullet", properties -> new BaseBullet(properties.stacksTo(99), 1.5f, 0.5f, 2, 0.8f, ModRarity.WHITE, false));
    public static final DeferredItem<BaseBullet> ENDLESS_MUSKET_POUCH = BULLETS.registerItem("endless_musket_pouch", properties -> new BaseBullet(properties.stacksTo(1), 1.5f, 0.5f, 2, 0.8f, ModRarity.GREEN, true));

}
