package org.confluence.terra_guns.common.data.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraGuns.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        TGItems.ITEM_BULLETS.getEntries().forEach(this::bulletsItem);
    }

    private void gunsItem(DeferredHolder<Item, Item> item) {
        String path = item.getId().getPath();
        withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", TerraGuns.asResource("item/" + path));
    }

    private void bulletsItem(DeferredHolder<Item, ? extends Item> item) {
        String path = item.getId().getPath();
        withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", TerraGuns.asResource("item/" + path));
    }
}
