package org.confluence.terra_guns.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.confluence.terra_guns.common.datagen.provider.TGChineseProvider;
import org.confluence.terra_guns.common.datagen.provider.TGEnglishProvider;

import java.util.concurrent.CompletableFuture;

import static org.confluence.terra_guns.TerraGuns.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        boolean client = event.includeClient();
        generator.addProvider(client, new TGChineseProvider(output));
        generator.addProvider(client, new TGEnglishProvider(output));

        boolean server = event.includeServer();
    }
}