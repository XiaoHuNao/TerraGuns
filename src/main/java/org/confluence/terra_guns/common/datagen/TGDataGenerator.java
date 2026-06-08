package org.confluence.terra_guns.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.confluence.terra_guns.common.datagen.provider.*;
import org.confluence.terra_guns.common.init.TGDamageTypes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.confluence.terra_guns.TerraGuns.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TGDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean client = event.includeClient();
        generator.addProvider(client, new TGChineseProvider(output));
        generator.addProvider(client, new TGEnglishProvider(output));
        generator.addProvider(client, new TGItemModelProvider(output, helper));

        boolean server = event.includeServer();
        CompletableFuture<HolderLookup.Provider> lookup = generator.addProvider(server, new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), new RegistrySetBuilder()
                .add(Registries.DAMAGE_TYPE, TGDamageTypes::bootstrap), Set.of(MODID))).getRegistryProvider();
        TGBlockTagsProvider blockTagsProvider = generator.addProvider(server, new TGBlockTagsProvider(output, lookup, helper));
        generator.addProvider(server, new TGItemTagsProvider(output, lookup, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new TGDamageTypeTagsProvider(output, lookup, helper));
    }
}
