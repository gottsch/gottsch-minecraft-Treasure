/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.datagen;

import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import mod.gottsch.forge.treasure2.datagen.loot.TreasureBlockLootTables;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 
 * @author Mark Gottschling on Nov 6, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        if (event.includeServer()) {
            generator.addProvider(true, new Recipes(output));
        	TreasureBlockTagsProvider blockTags = new TreasureBlockTagsProvider(output, lookupProvider, event.getExistingFileHelper());
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new TreasureItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), event.getExistingFileHelper()));
            generator.addProvider(true, new TreasureBiomeTagsProvider(output, lookupProvider, event.getExistingFileHelper()));
//            generator.addProvider(true, new TreasureWorldGenProvider(output, lookupProvider));
            generator.addProvider(true, TreasureLootTableProvider.create(output));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new TreasureBlockStateProvider(output, event.getExistingFileHelper()));
            generator.addProvider(true, new ItemModelsProvider(output, event.getExistingFileHelper()));
            generator.addProvider(true, new LanguageGen(output, "en_us"));
            generator.addProvider(true, new JapaneseLanguageGen(output, "ja_jp"));
        }

        // This is where you add your custom TagsProvider.
        // It's crucial to pass the correct parameters from the event and link the dependencies.
        generator.addProvider(
                event.includeServer(),
                new TreasureRarityTagsProvider(
                        output,
                        event.getLookupProvider(),
//                            event.getLookupProvider().thenApply(p -> TagKey.create(Registration.RARITIES_REGISTRY_KEY, new ResourceLocation(Treasure.MODID, "example_tag"))),
                        event.getExistingFileHelper()
                )

        );
    }
}