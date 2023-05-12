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
package mod.gottsch.forge.treasure2.core.registry;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;

import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.IRegionPlacement;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.registry.manifest.ResourceManifest;
import mod.gottsch.forge.treasure2.core.structure.IStructureType;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.structure.TemplateHolder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;


/**
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TreasureTemplateRegistry {
	private static final String TEMPLATES_FOLDER = "structures";

	private static final Set<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	protected static final Gson GSON_INSTANCE;
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";
		
	private final Table<IRegionPlacement, IStructureType, List<TemplateHolder>> templatesByArchetypeType = HashBasedTable.create();
	private final Table<String, ResourceLocation, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();
	
	/*
	 * standard list of marker blocks to scan for 
	 */
	private static List<Block> markerScanList;

	/*
	 * 
	 */
	private Map<StructureMarkers, Block> markerMap;
	
	/*
	 * standard list of replacements blocks.
	 * NOTE needs to be <IBlockState, IBlockState> (for v1.12.x anyway)
	 */
	private static Map<BlockState, BlockState> replacementMap;
	
	/*
	 * use this map when structures are submerged instead of the default marker map
	 */
	private static Map<StructureMarkers, Block> waterMarkerMap;
	
	private static ServerLevel world;
	
	/*
	 * the path to the world save folder
	 */
	private static File worldSaveFolder;
	
	static {
//		TEMPLATE_MANAGER = new TreasureTemplateManager(Treasure.instance, "/structures",
//				DataFixesManager.getDataFixer());
		REGISTERED_MODS = Sets.newHashSet();
		LOADED_MODS = Maps.newHashMap();
		
		GSON_INSTANCE = new GsonBuilder().create();
	}
	
	/**
	 * 
	 */
	private TreasureTemplateRegistry() {}
	
	/**
	 * 
	 * @param world
	 */
	public static void create(ServerLevel world) {
		TreasureTemplateRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save).getWorldDir();
			setWorldSaveFolder(path.toFile());
		}
		
		// init water marker map
        // setup standard list of markers
//        waterMarkerMap = Maps.newHashMap(getMarkerMap());
//        waterMarkerMap.put(StructureMarkers.NULL, Blocks.AIR);// <-- this is the difference between default
//
//		// initialize table
//		for (IMetaArchetype archetype : StructureArchetype.values()) {
//			for (IMetaType type : com.someguyssoftware.treasure2.meta.StructureType.values()) {
//				templatesByArchetypeType.put(archetype, type, new ArrayList<>(5));
//			}
//		}
	}
	
	/**
	 * 
	 */
	public void clear() {
		templatesByArchetypeTypeBiome.clear();
		templatesByArchetypeType.clear();
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(String modID) {
		REGISTERED_MODS.add(modID);
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerLevel) event.getWorld())) {
			Treasure.LOGGER.debug("template registry world load");
			TreasureTemplateRegistry.create((ServerLevel) event.getWorld());
			
			REGISTERED_MODS.forEach(mod -> {
				Treasure.LOGGER.debug("registering mod -> {}", mod);
				load(mod);
			});
		}
	}
	
	public static void load(String modID) {
		// don't reload for session
		if (LOADED_MODS.containsKey(modID)) {
			return;
		}
		
		ResourceManifest manifest = null;
		boolean worldSaveMetaLoaded = false;
		// read from file location
		File manifestFile = Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", modID, "structures", "manifest.json").toFile();
		if (manifestFile.exists()) {
			if (manifestFile.isFile()) {
				String json;
				try {
					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
					manifest = new GsonBuilder().create().fromJson(json, ResourceManifest.class);
					worldSaveMetaLoaded = true;
					Treasure.LOGGER.debug("loaded template manifest from file system");
				}
				catch (Exception e) {
					Treasure.LOGGER.warn("Couldn't load template manifest from {}", manifestFile, e);
				}
			}
		}

		if (!worldSaveMetaLoaded) {
			try {
				// load default built-in meta manifest
//				Path manifestPath = Paths.get("data", modID, TEMPLATES_FOLDER, "manifest.json");
				manifest = ITreasureResourceRegistry.<ResourceManifest>readResourcesFromStream(
						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID + "/" + TEMPLATES_FOLDER + "/manifest.json")), ResourceManifest.class);
				Treasure.LOGGER.debug("loaded template manifest from jar");
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("Unable to load template resources");
			}
		}
		
		// load template files
		if (manifest != null) {
			LOADED_MODS.put(modID, true);
			register(modID, manifest.getResources());
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
	public static void register(String modID, List<String> resourcePaths) {
		Treasure.LOGGER.debug("registering template resources");
		// create folders if not exist
		createTemplateFolder(modID);
		Treasure.LOGGER.debug("created templates folder");
		
		List<ResourceLocation> resourceLocations = getResourceLocations(modID, resourcePaths);
		Treasure.LOGGER.debug("acquired template resource locations -> {}", resourceLocations);
		// load each ResourceLocation as LootTable and map it.
		resourceLocations.forEach(loc -> {
			// need to test for world save version first
			Treasure.LOGGER.debug("loading template resource loc -> {}", loc.toString());
						
//			tableTemplate(modID, loc, loadTemplate(loc, getMarkerScanList(), getReplacementMap()));
		});
	}
	
	/**
	 * Load template file from classpath or file system
	 * @param server
	 * @param templatePath
	 * @return
	 */
//	public static StructureTemplate loadTemplate(ResourceLocation templatePath, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
//		String key = templatePath.toString();
//		
//		if (this.getTemplates().containsKey(key)) {
//			GottschCore.LOGGER.debug("read template from master map using key -> {}", key);
//			return this.templates.get(key);
//		}
//
//		this.readTemplate(templatePath, markerBlocks, replacementBlocks);
//		if (this.templates.get(key) != null) {
//			GottschCore.LOGGER.debug("Loaded template from -> {}", key);
//		}
//		else {
//			GottschCore.LOGGER.debug("Unable to read template from -> {}", key);
//		}
//		return this.templates.containsKey(key) ? (StructureTemplate) this.templates.get(key) : null;
//	}
	
	/**
	 * 
	 * @param resource
	 * @return
	 */	
//	protected static Optional<LootTableShell> loadLootTable(ResourceLocation resource) {
//		// attempt to load from file system
//		Optional<LootTableShell> shell = loadLootTableFromWorldSave(getWorldSaveFolder(), resource);
//		if (!shell.isPresent()) {
//			return loadLootTableFromJar(resource);
//		}
//		return shell;
//	}
	
	/**
	 * 
	 * @param modID
	 * @param resources
	 * @return
	 */
	public static List<ResourceLocation> getResourceLocations(String modID, List<String> resources) {
		List<ResourceLocation> resourceLocations = new ArrayList<>();
		resources.forEach(resource -> resourceLocations.add(new ResourceLocation(modID, resource)));
		return resourceLocations;
	}
	
	/**
	 * 
	 * @param modID
	 */
	private static void createTemplateFolder(String modID) {
		List<Path> folders = Arrays.asList(
				Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", Treasure.MODID, "structures", "submerged"),
				Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", Treasure.MODID, "structures", "subterranean"),
				Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", Treasure.MODID, "structures", "surface"),
				Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data", Treasure.MODID, "structures", "wells")
				);
		/*
		 *  build a path to the specified location
		 *  ie ../[WORLD SAVE]/datapacks/[MODID]/templates
		 */
		folders.forEach(folder -> {
			if (Files.notExists(folder)) {
				Treasure.LOGGER.debug("template folder \"{}\" will be created.", folder.toString());
				try {
					Files.createDirectories(folder);

				} catch (IOException e) {
					Treasure.LOGGER.warn("Unable to create template folder \"{}\"", folder.toString());
				}
			}
		});
	}
	
	/**
	 * Convenience method.
	 * @param key
	 * @return
	 */
//	public static TemplateHolder get(Level world, Random random, IRegionPlacement archetype, IStructureType type, Biome biome) {
//		return getTemplate(random, archetype, type, biome);
//	}
	
	public static File getWorldSaveFolder() {
		return TreasureTemplateRegistry.worldSaveFolder;
	}

	public static void setWorldSaveFolder(File worldSaveFolder) {
		TreasureTemplateRegistry.worldSaveFolder = worldSaveFolder;
	}
	
	public static List<Block> getMarkerScanList() {
		return markerScanList;
	}

	public void setMarkerScanList(List<Block> scanList) {
		this.markerScanList = scanList;
	}

	public Map<StructureMarkers, Block> getMarkerMap() {
		return markerMap;
	}

	public void setMarkerMap(Map<StructureMarkers, Block> markerMap) {
		this.markerMap = markerMap;
	}
	
	public static Map<BlockState, BlockState> getReplacementMap() {
		return replacementMap;
	}

	public void setReplacementMap(Map<BlockState, BlockState> replacementMap) {
		this.replacementMap = replacementMap;
	}
}
