package org.confluence.terra_guns.common.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terra_guns.common.init.TGItems;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.confluence.terra_guns.TerraGuns.MODID;

public class TGEnglishProvider extends LanguageProvider {
    public TGEnglishProvider(PackOutput output) {
        super(output, MODID, "en_us");
    }
    @Override
    protected void addTranslations() {
        Consumer<DeferredHolder<Item, ? extends Item>> action = item -> add(item.get(), toTitleCase(item.getId().getPath()));
        TGItems.ITEM_GUNS.getEntries().forEach(action);
        TGItems.ITEM_BULLETS.getEntries().forEach(action);
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
