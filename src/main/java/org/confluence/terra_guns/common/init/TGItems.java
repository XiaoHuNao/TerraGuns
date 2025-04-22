package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
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
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final DeferredItem<BaseGun> ANIM_ITEM = ITEMS.registerItem("anim_item", BaseGun::new);
    public static final DeferredItem<BaseGun> HAND_GUN = ITEMS.registerItem("hand_gun", BaseGun::new);
}
