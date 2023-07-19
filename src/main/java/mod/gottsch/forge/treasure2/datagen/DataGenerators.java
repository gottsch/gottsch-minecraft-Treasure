/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.datagen;

import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
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
        }
        if (event.includeClient()) {
        	 generator.addProvider(true, new BlockStates(output, event.getExistingFileHelper()));
            generator.addProvider(true, new ItemModelsProvider(output, event.getExistingFileHelper()));
            generator.addProvider(true, new LanguageGen(output, "en_us"));
        }
    }
}