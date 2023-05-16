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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.structure.*;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;


/**
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TreasureTemplateRegistry {
	public static final String JAR_TEMPLATES_ROOT = "data/treasure2/structures/";
	public static final String DATAPACKS_LOOT_TABLES_ROOT = "data/treasure2/structures/";
	
	@Deprecated
	private static final String TEMPLATES_FOLDER = "structures";

	private static final Set<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	protected static final Gson GSON_INSTANCE;
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";
		
	private final static Table<IStructureCategory, IStructureType, List<TemplateHolder>> TABLE = HashBasedTable.create();
//	private final static Table<String, ResourceLocation, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();
	
	/*
	 * standard list of marker blocks to scan for 
	 */
	private static List<Block> markerScanList;

	/*
	 * 
	 */
	private static Map<StructureMarkers, Block> markerMap;
	
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
		
        // setup standard list of markers
        markerMap = Maps.newHashMapWithExpectedSize(10);
        markerMap.put(StructureMarkers.CHEST, Blocks.CHEST);
        markerMap.put(StructureMarkers.BOSS_CHEST, Blocks.ENDER_CHEST);
        markerMap.put(StructureMarkers.SPAWNER, Blocks.SPAWNER);
        markerMap.put(StructureMarkers.ENTRANCE, Blocks.GOLD_BLOCK);
        markerMap.put(StructureMarkers.OFFSET, Blocks.REDSTONE_BLOCK);
        markerMap.put(StructureMarkers.PROXIMITY_SPAWNER, Blocks.IRON_BLOCK);
        markerMap.put(StructureMarkers.NULL, Blocks.BEDROCK);
        
        // default marker scan list
        markerScanList = Arrays.asList(new Block[] {
    			markerMap.get(StructureMarkers.CHEST),
    			markerMap.get(StructureMarkers.BOSS_CHEST),
    			markerMap.get(StructureMarkers.SPAWNER),
    			markerMap.get(StructureMarkers.ENTRANCE),
    			markerMap.get(StructureMarkers.OFFSET),
    			markerMap.get(StructureMarkers.PROXIMITY_SPAWNER)
    			});
	}
	
	/**
	 * 
	 */
	private TreasureTemplateRegistry() {
		
	}
	
	/**
	 * 
	 * @param world
	 */
	public static void create(ServerLevel world) {
		// get path to world save folder
		TreasureTemplateRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save).getWorldDir();
			setWorldSaveFolder(path.toFile());
		}
		else {
			// TODO throw error
		}
		
		// init water marker map
        // setup standard list of markers
        waterMarkerMap = Maps.newHashMap(getMarkerMap());
        waterMarkerMap.put(StructureMarkers.NULL, Blocks.AIR);// <-- this is the difference between default

		// initialize table
		for (IStructureCategory category : StructureCategory.values()) {
			for (IStructureType type : StructureType.values()) {
				TABLE.put(category, type, new ArrayList<>(5));
			}
		}
	}
	
	/**
	 * 
	 */
	public void clear() {
//		templatesByArchetypeTypeBiome.clear();
		TABLE.clear();
	}
	
	/**
	 * 
	 * @param modID
	 */
	public static void register(String modID) {
		REGISTERED_MODS.add(modID);
		
		Treasure.LOGGER.debug("reading templates...");
		Path jarPath = ModList.get().getModFileById(modID).getFile().getFilePath();
		Treasure.LOGGER.debug("jar path -> {}", jarPath);
		registerFromJar(jarPath);
	}
	
	/**
	 * 
	 * @param jarPath
	 */
	private static void registerFromJar(Path jarPath) {
//		StructureType.getNames().forEach(category -> {
			List<Path> lootTablePaths;
			try {
				// get all the paths in folder
				lootTablePaths = ModUtil.getPathsFromResourceJAR(jarPath, JAR_TEMPLATES_ROOT);

				for (Path path : lootTablePaths) {
					// ensure each file ends with .nbt
					if (path.getFileName().toString().endsWith(".nbt")) {
						Treasure.LOGGER.debug("structure path -> {}", path);
						// load the shell from the jar
						Optional<GottschTemplate> template = loadFromJar(path, getMarkerScanList(), getReplacementMap()); // ie readTemplate
						// register
						if (template.isPresent()) {
							registerTemplate(path, template.get());
						}
					}
				}
			} catch (Exception e) {
				Treasure.LOGGER.error("error: " , e);
				e.printStackTrace();
			}
//		});		
	}
	
	/**
	 * 
	 * @param path
	 * @param template
	 */
	private static void registerTemplate(Path path, GottschTemplate template) {
		// extract the category

		String categoryToken = path.getName(3).toString();
		Optional<IStructureCategory> category = TreasureApi.getStructureCategory(categoryToken);
		if (category.isEmpty()) { 
			return;
		}
		
		String typeToken = null;
		Optional<IStructureType> type = Optional.empty();
		if (path.getNameCount() > 4) {
			typeToken = path.getName(4).toString();
			type = TreasureApi.getStructureType(typeToken);
			if (type.isEmpty()) {
				return;
			}
		}
		
		// convert to resource location
		ResourceLocation resourceLocation = asResourceLocation(path);
		Treasure.LOGGER.debug("resource location -> {}", resourceLocation);
		
		// setup the template holder
		TemplateHolder holder = new TemplateHolder()
//				.setMetaLocation(metaResourceLocation)
				.setLocation(resourceLocation)
//				.setDecayRuleSetLocation(decayRuleSetResourceLocation)
				.setTemplate(template);				
		
		if (path.getNameCount() > 5) {
			for (int i = 5; i < path.getNameCount(); i++) {
				holder.getTags().add(path.getName(i).toString());
			}
		}
		
		// add to table
		if (!TABLE.contains(category, type)) {
			TABLE.put(category.get(), type.get(),new ArrayList<>());
		}
		TABLE.get(category.get(), type.get()).add(holder);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static ResourceLocation asResourceLocation(Path path) {
		Treasure.LOGGER.debug("path in -> {}", path.toString());

		// extract the namespace (moot - should always be value of Treasure.MOD_ID)
		String namespace = path.getName(1).toString();
		// get everything after loot_tables/ as the name
		String name = path.toString().replace("\\", "/");
		if (name.startsWith("/")) {
			name = name.substring(1, name.length());
		}
		name = name.substring(name.indexOf("structures/") + 12).replace(".nbt", "");
		return new ResourceLocation(namespace, name);
	}
	
	/**
	 * reads a template from the minecraft jar
	 */
	public static Optional<GottschTemplate> loadFromJar(Path resourceFilePath, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		InputStream inputStream = null;

		try {
			Treasure.LOGGER.debug("Attempting to load template from jar -> {}", resourceFilePath);
			inputStream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(resourceFilePath.toString());
			return readTemplateFromStream(inputStream, markerBlocks, replacementBlocks);

			// TODO change from Throwable
		} catch (Throwable e) {
			Treasure.LOGGER.error("error reading resource: ", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return Optional.empty();
	}
	
	/**
	 * reads a template from an input stream
	 */
	private static Optional<GottschTemplate> readTemplateFromStream(InputStream stream, List<Block> markerBlocks, 
			Map<BlockState, BlockState> replacementBlocks) throws IOException {
		
		CompoundTag nbt = NbtIo.readCompressed(stream);

		if (!nbt.contains("DataVersion", 99)) {
			nbt.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());//500);
		}

		GottschTemplate template = new GottschTemplate();
		template.load(nbt, markerBlocks, replacementBlocks);
//		Treasure.LOGGER.debug("adding template to map with key -> {}", id);
//		this.getTemplates().put(id, template);
		return Optional.ofNullable(template);
	}
	
	////////////////////////  ////////////////////////  /////////////////////////////
	/**
	 * 
	 * @param resourceFilePath
	 * @return
	 */
//	@Deprecated
//	public static Optional<LootTableShell> loadFromJar(Path resourceFilePath) {
//		Optional<LootTableShell> resourceLootTable = Optional.empty();
//		try (InputStream resourceStream = Treasure.class.getClassLoader().getResourceAsStream(resourceFilePath.toString());
//				Reader reader = new InputStreamReader(resourceStream, StandardCharsets.UTF_8)) {
//			resourceLootTable =  Optional.of(loadLootTable(reader));
//		}
//		catch(Exception e) {
//			Treasure.LOGGER.error(String.format("Couldn't load resource loot table %s ", resourceFilePath), e);
//		}	
//		return resourceLootTable;
//	}
	
	
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
//				load(mod);
			});
		}
	}
	
	/**
	 * TODO only called once from onWorldLoad event
	 * @param modID
	 */
//	@Deprecated
//	public static void load(String modID) {
//		// don't reload for session
//		if (LOADED_MODS.containsKey(modID)) {
//			return;
//		}
//		
//		ResourceManifest manifest = null;
//		boolean worldSaveMetaLoaded = false;
//		// read from file location
//		File manifestFile = Paths.get(getWorldSaveFolder().getPath(), "datapacks", "data", Treasure.MODID, "structures", "manifest.json").toFile();
//		if (manifestFile.exists()) {
//			if (manifestFile.isFile()) {
//				String json;
//				try {
//					json = com.google.common.io.Files.toString(manifestFile, StandardCharsets.UTF_8);
//					manifest = new GsonBuilder().create().fromJson(json, ResourceManifest.class);
//					worldSaveMetaLoaded = true;
//					Treasure.LOGGER.debug("loaded template manifest from file system");
//				}
//				catch (Exception e) {
//					Treasure.LOGGER.warn("Couldn't load template manifest from {}", manifestFile, e);
//				}
//			}
//		}
//
//		// TODO check datapack (.zip) files
//		////////
//		
//		if (!worldSaveMetaLoaded) {
//			try {
//				// load default built-in meta manifest
////				Path manifestPath = Paths.get("data", modID, TEMPLATES_FOLDER, "manifest.json");
//				manifest = ITreasureResourceRegistry.<ResourceManifest>readResourcesFromStream(
//						Objects.requireNonNull(Treasure.instance.getClass().getClassLoader().getResourceAsStream("data/" + modID + "/" + TEMPLATES_FOLDER + "/manifest.json")), ResourceManifest.class);
//				Treasure.LOGGER.debug("loaded template manifest from jar");
//			}
//			catch(Exception e) {
//				Treasure.LOGGER.warn("Unable to load template resources");
//			}
//		}
//		
//		// load template files
//		if (manifest != null) {
//			LOADED_MODS.put(modID, true);
//			register(modID, manifest.getResources());
//		}
//	}
	
	/**
	 * 
	 * @param modID
	 * @param resourcePaths
	 */
//	@Deprecated
//	public static void register(String modID, List<String> resourcePaths) {
//		Treasure.LOGGER.debug("registering template resources");
//		// create folders if not exist
//		createTemplateFolder(modID);
//		Treasure.LOGGER.debug("created templates folder");
//		
//		List<ResourceLocation> resourceLocations = getResourceLocations(modID, resourcePaths);
//		Treasure.LOGGER.debug("acquired template resource locations -> {}", resourceLocations);
//		// load each ResourceLocation as LootTable and map it.
//		resourceLocations.forEach(loc -> {
//			// need to test for world save version first
//			Treasure.LOGGER.debug("loading template resource loc -> {}", loc.toString());
//						
//			tableTemplate(modID, loc, loadTemplate(loc, getMarkerScanList(), getReplacementMap()));
//		});
//	}
	
	/**
	 * Load template file from classpath or file system
	 * @param server
	 * @param templatePath
	 * @return
	 */
//	@Deprecated
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

	public static Map<StructureMarkers, Block> getMarkerMap() {
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
