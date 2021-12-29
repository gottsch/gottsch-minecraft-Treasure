/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.world.gen.structure;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Dec 20, 2019
 *
 */
public class TreasureDecayManager {
	// set to empty/blank list as there is only one location. current design of methods must take in a location or list of locations.
	protected static final Gson GSON_INSTANCE = new GsonBuilder().create();
	private final Map<String, IDecayRuleSet> ruleSetMap = Maps.<String, IDecayRuleSet>newHashMap();

	private File worldSaveFolder;
	
	/**
	 * 
	 * @param mod
	 * @param resourceFolder
	 */
	public TreasureDecayManager() {}

	/**
	 * 
	 */
	public void clear() {
		getRuleSetMap().clear();
	}

	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	public void register(String modID, List<String> resourcePaths) {
		Treasure.logger.debug("registering template resources");
		// create folders if not exist
		createDecayFolder(modID);
		Treasure.logger.debug("created decay folder");
		
		List<ResourceLocation> resourceLocations = getResourceLocations(modID, resourcePaths);
		Treasure.logger.debug("acquired resource locations -> {}", resourceLocations);
		// load each ResourceLocation as ruleset and map it.
		resourceLocations.forEach(loc -> {
			// need to test for world save version first
			Treasure.logger.debug("register decay -> loading decay resource loc -> {}", loc.toString());						
			tableDecay(loc, load(loc));
		});
	}
	
	/**
	 * 
	 * @param modID
	 * @param location
	 */
	private void createDecayFolder(String modID) {

		/*
		 *  build a path to the specified location
		 *  ie ../[WORLD SAVE]/data/decay/[MODID]/rulesets
		 */
		Path folder = Paths.get(getWorldSaveFolder().getPath(), "data/decay", modID, "rulesets").toAbsolutePath();

		if (Files.notExists(folder)) {
			Treasure.logger.debug("decay folder \"{}\" will be created.", folder.toString());
			try {
				Files.createDirectories(folder);

			} catch (IOException e) {
				Treasure.logger.warn("Unable to create decay folder \"{}\"", folder.toString());
			}
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */
	public List<ResourceLocation> getResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}	
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Optional<IDecayRuleSet> load(ResourceLocation resource) {
		// attempt to load from file system
		Optional<IDecayRuleSet> decay = loadFromWorldSave(getWorldSaveFolder(), resource);
		if (!decay.isPresent()) {
			return loadFromJar(resource);
		}
		return decay;
	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */
	private Optional<IDecayRuleSet> loadFromJar(ResourceLocation resource) {		
		Optional<IDecayRuleSet> resourceMeta = Optional.empty();
		String relativePath = "decay/" + resource.getResourceDomain() + "/" + resource.getResourcePath();
		Treasure.logger.debug("Attempting to load decay {} from jar -> {}", resource, relativePath);
		try (InputStream resourceStream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(relativePath);
				Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
			resourceMeta =  Optional.of(load(reader));
		}
		catch(Exception e) {
			Treasure.logger.error(String.format("Couldn't load resource meta %s ", relativePath), e);
		}		
		return resourceMeta;
	}
	
	/**
	 * 
	 * @param folder
	 * @param resource
	 * @return
	 */
	private Optional<IDecayRuleSet> loadFromWorldSave(File folder, ResourceLocation resource) {
		if (folder == null) {
			return Optional.empty();
		}
		else {
			File metaFile = Paths.get(folder.getPath(), "data", "decay", resource.getResourceDomain(), resource.getResourcePath()).toFile();
			Treasure.logger.debug("Attempting to load decay {} from {}", resource, metaFile);
			
			if (metaFile.exists()) {
				if (metaFile.isFile()) {
					String json;
					try {
						json = com.google.common.io.Files.toString(metaFile, StandardCharsets.UTF_8);
					}
					catch (IOException e) {
						Treasure.logger.warn("Couldn't load decay {} from {}", resource, metaFile, e);
						return Optional.empty();
					}

					try {
						return Optional.of(load(json));
					}
					catch (IllegalArgumentException | JsonParseException e) {
						Treasure.logger.error("Couldn't load meta {} from {}", resource, metaFile, e);
						return Optional.empty();
					}
				}
				else {
					Treasure.logger.warn("Expected to find meta {} at {} but it was a folder.", resource, metaFile);
					return Optional.empty();
				}
			}
			else {
				Treasure.logger.warn("Expected to find meta {} at {} but it doesn't exist.", resource, metaFile);
				return Optional.empty();
			}
		}
	}
	
	/**
	 * 
	 * @param reader
	 * @return
	 */
	public IDecayRuleSet load(Reader reader) {
		return GSON_INSTANCE.fromJson(reader, DecayRuleSet.class);
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public IDecayRuleSet load(String json) throws IllegalArgumentException, JsonParseException {
		return GSON_INSTANCE.fromJson(json, DecayRuleSet.class);
	}
	
	private void tableDecay(ResourceLocation resourceLocation, Optional<IDecayRuleSet> decay) {
		if (decay.isPresent()) {
			Treasure.logger.debug("tabling meta: key -> {}, meta -> {}", resourceLocation.toString(), decay);
			// add meta to map
			this.getRuleSetMap().put(resourceLocation.toString(), decay.get());
		}
		else {
			Treasure.logger.debug("unable to table meta from -> {}", resourceLocation);
		}
	}
	
	/**
	 * Call in WorldEvent.Load() event handler. Loads and registers ruleset files from
	 * the file system.
	 * 
	 * @param modID
	 */
//	public void register(String modID) {
//		// set location to empty because there is only one location where decay files are.
//		String location = "";
//		Treasure.logger.debug("registering ruleset files from location -> {}", location);
//		// get loot table files as ResourceLocations from the file system location
//		List<ResourceLocation> locs = getResourceLocations(modID, location);
//
//		// load each ResourceLocation as DecayRuleSet and map it.
//		for (ResourceLocation loc : locs) {
//			Path path = Paths.get(loc.getResourcePath());
//			if (Treasure.logger.isDebugEnabled()) {
//				Treasure.logger.debug("path to ruleset resource loc -> {}", path.toString());
//			}
//
//			// load ruleset
//			Treasure.logger.debug("attempted to load custom ruleset file  with key -> {}", loc.toString());
//			IDecayRuleSet ruleset = load(loc);
//			// add the id to the map
//			if (ruleset == null) {
//				Treasure.logger.debug("Unable to locate ruleset file -> {}", loc.toString());
//				continue;
//			}
//			Treasure.logger.debug("loaded custom ruleset file  with key -> {}", loc.toString());
//			// TODO doesn't do anything with them rulesets
//		
//		}
//	}
	
	public File getWorldSaveFolder() {
		return worldSaveFolder;
	}

	public void setWorldSaveFolder(File worldSaveFolder) {
		this.worldSaveFolder = worldSaveFolder;
	}

	public Map<String, IDecayRuleSet> getRuleSetMap() {
		return ruleSetMap;
	}
}
