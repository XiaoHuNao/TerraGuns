package org.confluence.terra_guns.datagen.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.lib.mixin.accessor.LanguageProviderAccessor;
import org.confluence.lib.util.LibUtils;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TGEnglishProvider extends LanguageProvider {
    public TGEnglishProvider(PackOutput output) {
        super(output, TerraGuns.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        Consumer<DeferredHolder<Item, ? extends Item>> itemAction = item -> add(item.get(), LibUtils.toTitleCase(item.getId().getPath()));
        TGItems.ITEMS.getEntries().forEach(itemAction);
    }

    @Override
    public void add(Item key, @NotNull String name) {
        String descriptionId = key.getDescriptionId();
        if (!((LanguageProviderAccessor) this).getData().containsKey(descriptionId)) {
            super.add(descriptionId, name);
        }
    }
}
