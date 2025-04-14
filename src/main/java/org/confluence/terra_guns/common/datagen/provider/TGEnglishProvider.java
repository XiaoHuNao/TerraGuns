package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;

import java.util.function.Consumer;

public class TGEnglishProvider extends LanguageProvider {
    public TGEnglishProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        Consumer<DeferredHolder<Item, ? extends Item>> itemAction = item -> add(item.get(), LibUtils.toTitleCase(item.getId().getPath()));

        add("tooltip.terra_guns.damage", "Ranged Damage: %s");
        add("tooltip.terra_guns.critical", "Critical Strike Chance: %s%%");
        add("tooltip.terra_guns.knockback", "Knockback: %s");
        TGItems.ITEMS.getEntries().forEach(itemAction);

        add("key.terra_guns.shoot", "Shoot");
        add("key.terra_guns.aim", "Aim");

        add("creative_tab.terra_guns.gun_tab", "Terra Guns");
        add("death.attack.bullet_damage", "%1%s was shot");
    }
}
