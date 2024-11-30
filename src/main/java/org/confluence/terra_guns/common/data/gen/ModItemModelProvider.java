package org.confluence.terra_guns.common.data.gen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.ModItems;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, TerraGuns.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        ModItems.ITEM_BULLETS.getEntries().stream().map(RegistryObject::get).forEach(this::simpleItem);
        simpleItem(ModItems.MUSKET_BULLET.get());
    }

    private void simpleItem(Item item) {
        String path = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).getPath();
        this.withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(TerraGuns.MODID, "item/" + path));
    }
}
