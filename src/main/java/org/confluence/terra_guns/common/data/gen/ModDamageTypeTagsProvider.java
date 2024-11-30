package org.confluence.terra_guns.common.data.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.init.ModDamageTypes;
import org.confluence.terra_guns.common.init.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;


public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper helper) {
        super(output, future, TerraGuns.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModTags.DamageTypes.RANGED_ATTACK).add(ModDamageTypes.RANGED_ATTACK,
                DamageTypes.ARROW,
                DamageTypes.FIREBALL,
                DamageTypes.THROWN,
                DamageTypes.TRIDENT,
                DamageTypes.ON_FIRE,
                DamageTypes.WITHER_SKULL
        );
    }
}