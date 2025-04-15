package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.confluence.terra_guns.TerraGuns;

import static org.confluence.terra_guns.common.init.TGItems.*;

public class TGChineseProvider extends LanguageProvider {
    public TGChineseProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("tooltip.terra_guns.damage", "远程伤害：%s");
        add("tooltip.terra_guns.critical", "暴击率：%s%%");
        add("tooltip.terra_guns.knockback", "击退力：%s");

        addItem(HAND_GUN, "手枪");
        addItem(MUSKET_BULLET, "火枪子弹");
        addItem(ENDLESS_MUSKET_POUCH, "无尽火枪袋");

        add("key.terra_guns.shoot", "射击");
        add("key.terra_guns.aim", "瞄准");

        add("creative_tab.terra_guns.gun_tab", "泰拉枪械");
        add("death.attack.bullet_damage", "%1%s 被枪击");
    }
}
