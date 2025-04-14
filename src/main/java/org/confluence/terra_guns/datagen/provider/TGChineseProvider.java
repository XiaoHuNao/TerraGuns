package org.confluence.terra_guns.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.mixin.accessor.LanguageProviderAccessor;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.confluence.terra_guns.common.init.TGItems.HAND_GUN;

public class TGChineseProvider extends LanguageProvider {
    public TGChineseProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        addItem(HAND_GUN, "手枪");
    }

    @Override
    public void addItem(Supplier<? extends Item> key, @NotNull String name) {
        String descriptionId = key.get().getDescriptionId();
        if (!((LanguageProviderAccessor) this).getData().containsKey(descriptionId)) {
            super.add(descriptionId, name);
        }
    }
}
