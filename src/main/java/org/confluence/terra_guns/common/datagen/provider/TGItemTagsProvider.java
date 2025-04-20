package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TGItemTagsProvider extends ItemTagsProvider {
    public TGItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper helper) {
        super(output, provider, completableFuture, TerraGuns.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TGItems.BULLETS.getEntries().forEach(item -> tag(TGTags.AMMO).add(item.get()));
        TGItems.GUNS.getEntries().forEach(item -> tag(TGTags.GUN).add(item.get()));
    }
}
