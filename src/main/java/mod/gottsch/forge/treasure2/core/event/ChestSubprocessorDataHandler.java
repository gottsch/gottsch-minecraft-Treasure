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
package mod.gottsch.forge.treasure2.core.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.ChestSubprocessorDataRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * this class handles loading the custom chest subprocessor data from data packs.
 * @author by Mark Gottschling on 8/28/2025
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChestSubprocessorDataHandler extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String DATA_DIRECTORY = "subprocessors/chest";

    public ChestSubprocessorDataHandler() {
        super(GSON, DATA_DIRECTORY);
    }

    /**
     * This is the core method that gets called when a data pack reload happens.
     * We will parse the JSON files here.
     *
     * @param jsonElementMap The parsed JSON data.
     * @param resourceManager The resource manager.
     * @param profilerFiller The profiler.
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Treasure.LOGGER.info("loading chest subprocessors from data packs...");

        ChestSubprocessorDataRegistry.clear();

        jsonElementMap.forEach((location, jsonElement) -> {
            try {
                // deserialize the JSON element into our RarityItemAssociation object using the Codec
                ChestSubprocessorData data = ChestSubprocessorData.CODEC
                        .parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, Treasure.LOGGER::error);

                // determine the type of association and process accordingly
                Path path = Paths.get(location.getPath());

                // extract top-level key [aquatic|terranean]
                String parent = path.getName(0).toString().trim().toLowerCase();

                // convert parent to FeatureType
                IFeatureType type = FeatureType.getByValue(parent);
                IRarity rarity = TreasureRarities.getRarityByName(data.getRarity()).orElseGet(() -> {
                    Treasure.LOGGER.warn("unable to locate rarity {}, using default common.");
                    return TreasureRarities.COMMON.get();
                });


                // TODO this is wrong i think
                ChestSubprocessorDataRegistry.register(type, rarity, data);

                // TODO will have to register on subKey. ChestProcessors would have to lookup
                //  to this Registry to get the data. Which in turn would dicate which ChestSubprocessor to use.
//                if (path.getNameCount() > 1) {
//                    String typeKey = path.subpath(0, path.getNameCount() - 1).toString(); //getName(1).toString();
//                    RarityTagAssociationRegistry.Item.register(typeKey, association);
//                }

            } catch (Exception e) {
                Treasure.LOGGER.error("failed to parse rarity association JSON for {}: {}", location, e.getMessage());
            }
        });
//        Treasure.LOGGER.info("loaded {} rarity item associations.", RarityTagAssociationRegistry.Items.size);
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        // register rarity data custom reload listener with the data pack manager.
        // this is the correct place to do it in Forge 1.20.1.
        event.addListener(new ChestSubprocessorDataHandler());
    }


}