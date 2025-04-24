package org.confluence.terra_guns.common.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.TGItems;
import org.confluence.terra_guns.common.init.TGTags;
import org.confluence.terra_guns.common.item.gun.BaseGun;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static org.confluence.terra_guns.common.init.TGItems.*;

public class TGItemTagsProvider extends ItemTagsProvider {
    public TGItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper helper) {
        super(output, provider, completableFuture, TerraGuns.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        BULLETS.getEntries().forEach(item -> tag(TGTags.AMMO).add(item.get()));
        GUNS.getEntries().forEach(item -> tag(TGTags.GUN).add(item.get()));

        addAutomatic(MINISHARK);

        addManual(HAND_GUN);
        addManual(SHOTGUN);
        addManual(FLINTLOCK_PISTOL);
        addManual(BOOMSTICK);
        addManual(THE_UNDERTAKER);
        addManual(MUSKET);
    }

    protected IntrinsicTagAppender<Item> addAutomatic(Supplier<BaseGun> gunSupplier) {
        return tag(TGTags.AUTOMATIC_GUN).add(gunSupplier.get());
    }

    protected IntrinsicTagAppender<Item> addManual(Supplier<BaseGun> gunSupplier) {
        return tag(TGTags.MANUAL_GUN).add(gunSupplier.get());
    }
}
