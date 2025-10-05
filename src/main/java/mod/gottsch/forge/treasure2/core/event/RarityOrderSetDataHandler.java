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
import mod.gottsch.forge.treasure2.core.rarity.RarityOrderSet;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeight;
import mod.gottsch.forge.treasure2.core.rarity.RarityWeightsManager;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.RarityOrderRegistry;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishables;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
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
 * this class handles loading the custom rarity weight data from data packs.
 * @author by Mark Gottschling on 9/4/2025
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RarityOrderSetDataHandler extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String DATA_DIRECTORY = "rarity_order_sets";
    private static final String CORE_DIRECTORY = "core";
    private static final String SPECIALTY_DIRECTORY = "specialty";

    public RarityOrderSetDataHandler() {
        super(GSON, DATA_DIRECTORY);
    }

    /**
     * this is the core method that gets called when a data pack reload happens.
     *
     * @param jsonElementMap the parsed JSON data.
     * @param resourceManager the resource manager.
     * @param profilerFiller the profiler.
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Treasure.LOGGER.info("loading rarity order sets from data packs...");

        RarityOrderRegistry.clear();

        jsonElementMap.forEach((location, jsonElement) -> {
            try {
                // deserialize the JSON element into our RarityEntry object using the Codec
                RarityOrderSet rarityOrderSet = RarityOrderSet.CODEC
                        .parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, Treasure.LOGGER::error);

                // determine the type of association and process accordingly
                Path path = Paths.get(location.getPath());

                // extract top-level key
                String parentKey = path.getName(0).toString().trim().toLowerCase();
                if (CORE_DIRECTORY.equalsIgnoreCase(parentKey)) {
                    // for each rarity order in the set
                    rarityOrderSet.rarityOrders().forEach(RarityOrderRegistry::registerCore);
                    RarityOrderRegistry.sortCore();
                }

                // TODO if specialty orders are implemented

            } catch (Exception e) {
                Treasure.LOGGER.error("failed to parse rarity weight JSON for {}: {}", location, e.getMessage());
            }
        });
        Treasure.LOGGER.info("loaded {} rarity weight.", TreasureWishables.getRarityAssociations().size());
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        // register rarity data custom reload listener with the data pack manager.
        // this is the correct place to do it in Forge 1.20.1.
        event.addListener(new RarityOrderSetDataHandler());
    }


}