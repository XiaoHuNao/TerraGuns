package org.confluence.terra_guns.common.data.gen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraGuns.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        bulletsItem(TGItems.MUSKET_BULLET);
    }

    private void gunsItem(Holder<Item> item) {
        String path = item.getKey().location().getPath();
        this.withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "item/" + path));
    }

    private void bulletsItem(Holder<Item> item) {
        String path = item.getKey().location().getPath();
        this.withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "item/" + path));
    }
}
