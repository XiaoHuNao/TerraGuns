package org.confluence.terra_guns.common.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.item.gun.BaseGun;

import java.util.function.Supplier;

public class TGItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TerraGuns.MODID);
    public static final Supplier<BaseGun> HAND_GUN = ITEMS.registerItem("hand_gun", BaseGun::new);
}
