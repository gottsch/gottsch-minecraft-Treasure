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
import mod.gottsch.forge.treasure2.core.rarity.RarityTagAssociation;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
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
 * this class handles loading the custom rarity association data from data packs.
 * @author by Mark Gottschling on 8/25/2025
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RarityItemAssociationDataHandler extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String DATA_DIRECTORY = "rarity_associations/item";

    public RarityItemAssociationDataHandler() {
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
        Treasure.LOGGER.info("loading rarity item associations from data packs...");

        RarityTagAssociationRegistry.Items.clear();

        jsonElementMap.forEach((location, jsonElement) -> {
            try {
                // deserialize the JSON element into our RarityItemAssociation object using the Codec
                RarityTagAssociation association = RarityTagAssociation.CODEC
                        .parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, Treasure.LOGGER::error);

                // determine the type of association and process accordingly
                Path path = Paths.get(location.getPath());

                // extract top-level key
//                String parentKey = path.getName(0).toString().trim().toLowerCase();

                        if (path.getNameCount() > 1) {
                            String typeKey = path.subpath(0, path.getNameCount() - 1).toString(); //getName(1).toString();
                            RarityTagAssociationRegistry.Items.register(typeKey, association);
                        }


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
        event.addListener(new RarityItemAssociationDataHandler());
    }


}