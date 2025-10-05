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
import mod.gottsch.forge.treasure2.core.mobset.MobSetData;
import mod.gottsch.forge.treasure2.core.mobset.MobSetDataRegistry;
import mod.gottsch.forge.treasure2.core.mobset.WeightedMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author by Mark Gottschling on 9/18/2025
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobSetDataHandler extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String DATA_DIRECTORY = "mobsets";

    public MobSetDataHandler() {
        super(GSON, DATA_DIRECTORY);
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        // register rarity data custom reload listener with the data pack manager.
        // this is the correct place to do it in Forge 1.20.1.
        event.addListener(new MobSetDataHandler());
    }

    /**
     *
     * @param jsonElementMap  The parsed JSON data.
     * @param resourceManager The resource manager.
     * @param profilerFiller  The profiler.
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Treasure.LOGGER.info("loading mobsets from data packs...");

//        MobSetDataRegistry.clear();
//
//        jsonElementMap.forEach((location, jsonElement) -> {
//            try {
//                // deserialize the JSON element into our MobSetData object using the Codec
//                MobSetData data = MobSetData.CODEC
//                        .parse(JsonOps.INSTANCE, jsonElement)
//                        .getOrThrow(false, Treasure.LOGGER::error);
//
//                // TODO fetch any existing mobset
//
//                MobSetDataRegistry.get(data.getId())
//                        .ifPresentOrElse(mobSetData -> {
//                                    if (mobSetData.isReplace()) {
//                                        MobSetDataRegistry.register(mobSetData);
//                                    } else {
//                                        // update properties
//                                        mobSetData.getCount().setMin(data.getCount().getMin());
//                                        mobSetData.getCount().setMax(data.getCount().getMax());
//                                        // check if in the mobSetData mobs list
//                                        data.getMobs().forEach(weightedMob -> {
//                                            // TODO if found, update mobSetData with weightedMob
//                                            // TODO else, add to mobSetData mobs list
//                                        });
//                                    }
//
//                                },
//                                // register net new mob set
//                                () -> MobSetDataRegistry.register(data)
//                        );
//
//                Treasure.LOGGER.debug("registering mobset data -> {}", data.getId().toString());
//                MobSetDataRegistry.register(data);
//
//            } catch (Exception e) {
//                Treasure.LOGGER.error("failed to parse mobset data JSON for {}: {}", location, e.getMessage());
//            }
//        });
        jsonElementMap.forEach((location, jsonElement) -> {
            try {
                // deserialize the JSON element into our MobSetData object
                MobSetData newData = MobSetData.CODEC
                        .parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, Treasure.LOGGER::error); // Java 17+ method reference for cleaner logging

                // fetch existing data and decide whether to replace, merge, or register new
                MobSetDataRegistry.get(newData.getId())
                        .ifPresentOrElse(
                                existingData -> {
                                    MobSetData finalData = existingData.isReplace()
                                            ? newData
                                            : mergeMobSetData(existingData, newData); // delegate merging to a helper method
                                    // replace existing data with final data
                                    MobSetDataRegistry.register(finalData);
                                },
                                // register net new mob set
                                () -> MobSetDataRegistry.register(newData)
                        );
                Treasure.LOGGER.debug("registered mobset data -> {}", newData.getId());
            } catch (Exception e) {
                Treasure.LOGGER.error("failed to parse mobset data JSON for {}: {}", location, e.getMessage());
            }
        });
    }

    /**
     * merges new MobSetData into an existing MobSetData object.
     * this is where the core logic from the original TODOs is implemented.
     * assumes MobSetData has appropriate setters or a builder for creating a new instance.
     */
    private MobSetData mergeMobSetData(MobSetData existingData, MobSetData newData) {
        // 1. update/replace Count
        MobSetData result = existingData.withCount(newData.getCount());

        // 2. merge mobs: use a Map for efficient lookups and updates
        Map<ResourceLocation, WeightedMob> mergedMobs = result.getMobs().stream()
                .collect(Collectors.toMap(
                        WeightedMob::id,
                        Function.identity(),
                        (oldMob, newMob) -> oldMob // should not happen with distinct IDs
                ));

        // for each new mob, either update the existing one or add it
        newData.getMobs().forEach(newMob -> {
            mergedMobs.compute(newMob.getId(), (mobId, existingMob) -> {
                if (existingMob != null) {
                    // mob found: update the existing mob with properties from the new mob.
                    return existingMob.withWeight(newMob.getWeight()); // Example update
                } else {
                    // Mob not found: Add the new mob.
                    return newMob;
                }
            });
        });

        // create the final MobSetData with the merged list of Mobs
        return result.withMobs(mergedMobs.values().stream().toList());
    }
}