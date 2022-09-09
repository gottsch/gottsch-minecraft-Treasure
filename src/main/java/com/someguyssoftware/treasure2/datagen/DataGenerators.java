/*
 * This file is part of  Bone Steel.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Bone Steel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bone Steel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Bone Steel.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.datagen;


import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * 
 * @author Mark Gottschling on Aug 17, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
//            generator.addProvider(new Recipes(generator));
//            generator.addProvider(new LootTables(generator));
        	TreasureBlockTagsProvider blockTags = new TreasureBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new TreasureItemTagsProvider(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
//            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModelsProvider(generator, event.getExistingFileHelper()));
//            generator.addProvider(new LanguageGen(generator, "en_us"));
        }
    }
}