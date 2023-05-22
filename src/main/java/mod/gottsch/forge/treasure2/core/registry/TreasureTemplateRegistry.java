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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.StructureConfiguration.StructMeta;
import mod.gottsch.forge.treasure2.core.structure.*;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;


/**
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TreasureTemplateRegistry {
	public static final String JAR_TEMPLATES_ROOT = "data/treasure2/structures/";
	public static final String DATAPACKS_TEMPLATES_ROOT = "data/treasure2/structures/";

	@Deprecated
	private static final String TEMPLATES_FOLDER = "structures";

	private static final Set<String> REGISTERED_MODS;
	private static final Map<String, Boolean> LOADED_MODS;
	protected static final Gson GSON_INSTANCE;
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "f_129744_";

	/*
	 * All structure templates by resource location.
	 */
	private final static Map<ResourceLocation, TemplateHolder> MAP = new HashMap<>();
	private final static Map<ResourceLocation, TemplateHolder> DATAPACK_MAP = new HashMap<>();

	/*
	 * All structure templates by category and type.
	 */
	private final static Table<IStructureCategory, IStructureType, List<TemplateHolder>> TABLE = HashBasedTable.create();
	private final static Table<IStructureCategory, IStructureType, List<TemplateHolder>> DATAPACK_TABLE = HashBasedTable.create();


	public static class AccessKey {
		public IStructureCategory category;
		public IStructureType type;
		public AccessKey(IStructureCategory category, IStructureType type) {
			this.category = category;
			this.type = type;
		}
	}

	/*
	 * Whitelist Guava Table by Type, Biome (resource location) -> TemplateHolder list.
	 */
	private final static Table<AccessKey, ResourceLocation, List<TemplateHolder>> WHITELIST_TABLE = HashBasedTable.create();
	private final static Table<AccessKey, ResourceLocation, List<TemplateHolder>> BLACKLIST_TABLE = HashBasedTable.create();

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
		REGISTERED_MODS = Sets.newHashSet();
		LOADED_MODS = Maps.newHashMap();

		GSON_INSTANCE = new GsonBuilder().create();

		// initialize table
		for (IStructureCategory category : StructureCategory.values()) {
			for (IStructureType type : StructureType.values()) {
				TABLE.put(category, type, new ArrayList<>(5));
				DATAPACK_TABLE.put(category, type, new ArrayList<>(3));
			}
		}

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

		// init water marker map
		// setup standard list of markers
		waterMarkerMap = Maps.newHashMap(getMarkerMap());
		waterMarkerMap.put(StructureMarkers.NULL, Blocks.AIR);// <-- this is the difference between default

        replacementMap = Maps.newHashMap();
        replacementMap.put(Blocks.WHITE_WOOL.defaultBlockState(), Blocks.AIR.defaultBlockState());
        replacementMap.put(Blocks.BLACK_STAINED_GLASS.defaultBlockState(), Blocks.SPAWNER.defaultBlockState());
	}

	/**
	 * 
	 */
	private TreasureTemplateRegistry() {	}

	/**
	 * 
	 * @param world
	 */
	public static void create(ServerLevel world) {
		// get path to world save folder
		TreasureTemplateRegistry.world = world;
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof LevelStorageSource.LevelStorageAccess) {
			Path path = ((LevelStorageSource.LevelStorageAccess) save).getWorldDir().resolve("datapacks");
			setWorldSaveFolder(path.toFile());
		}
		else {
			// TODO throw error
		}	

	}

	/**
	 * 
	 */
	public void clear() {
		TABLE.clear();
		DATAPACK_TABLE.clear();
		MAP.clear();
		DATAPACK_MAP.clear();
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
			// don't include the last element as that is the file name
			for (int i = 5; i < path.getNameCount()-1; i++) {
				holder.getTags().add(path.getName(i).toString());
			}
		}

		// add to map
		MAP.put(resourceLocation, holder);

		Treasure.LOGGER.debug("adding template to table with category, type -> {}", category.get(), type.get());
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
		//		Treasure.LOGGER.debug("path in -> {}", path.toString());

		// extract the namespace (moot - should always be value of Treasure.MOD_ID)
		String namespace = path.getName(1).toString();
		// get everything after loot_tables/ as the name
		String name = path.toString().replace("\\", "/");
		if (name.startsWith("/")) {
			name = name.substring(1, name.length());
		}
		name = name.substring(name.indexOf("structures/") + 11).replace(".nbt", "");
		return new ResourceLocation(namespace, name);
	}

	/**
	 * reads a template from the minecraft jar
	 */
	public static Optional<GottschTemplate> loadFromJar(Path resourceFilePath, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		InputStream inputStream = null;

		try {
			Treasure.LOGGER.debug("attempting to load template from jar -> {}", resourceFilePath);
			inputStream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(resourceFilePath.toString());
			return loadTemplateFromStream(inputStream, markerBlocks, replacementBlocks);

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
	private static Optional<GottschTemplate> loadTemplateFromStream(InputStream stream, List<Block> markerBlocks, 
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

	/**
	 * 
	 * @param event
	 */
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isClientSide() && WorldInfo.isSurfaceWorld((ServerLevel) event.getWorld())) {
			Treasure.LOGGER.debug("template registry world load event...");
			create((ServerLevel) event.getWorld());

			//			REGISTERED_MODS.forEach(mod -> {
			//				Treasure.LOGGER.debug("registering mod -> {}", mod);
			//				load(mod);
			loadDataPacks(getMarkerScanList(), getReplacementMap());
			registerAccesslists(Config.structureConfiguration.getStructMetas());

			//			});
		}
	}

	/**
	 * 
	 * @param zipPath
	 * @param resourceFilePath
	 * @return
	 */
	public static Optional<LootTableShell> loadFromZip(Path zipPath, Path resourceFilePath) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();

		return resourceLootTable;
	}

	/**
	 * 
	 * @param zipFile
	 * @param resourceFilePath
	 * @return
	 */
	public static Optional<LootTableShell> loadFromZip(ZipFile zipFile, Path resourceFilePath) {
		Optional<LootTableShell> resourceLootTable = Optional.empty();

		return resourceLootTable;
	}

	/**
	 * Only load once - not per  registered mod.
	 * @param markerBlocks
	 * @param replacementBlocks
	 */
	public static void loadDataPacks(List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		String worldSaveFolderPathName = getWorldSaveFolder().toString();

		StructureCategory.getNames().forEach(category -> {
			List<Path> templatePaths;
			// build the path
			Path folderPath = Paths.get(worldSaveFolderPathName, DATAPACKS_TEMPLATES_ROOT, category.toLowerCase());

			try {
				templatePaths = ModUtil.getPathsFromFlatDatapacks(folderPath);

				for (Path path : templatePaths) {
					Treasure.LOGGER.debug("template path from fs -> {}", path);
					// load the shell from the jar
					Optional<GottschTemplate> shell = loadFromFileSystem(path, markerBlocks, replacementBlocks);
					// extra step - strip the beginning path from the path, so it is just data/treasure2/...
					String p = path.toString().replace(worldSaveFolderPathName, "");
					// register
					registerDatapacksTemplate(Paths.get(p), shell.get());
				}
			} catch(NoSuchFileException e) {
				// silently sallow exception
			} catch (Exception e) {
				Treasure.LOGGER.error("An error occurred attempting to register a loot table from the world save datapacks folder: ", e);
			}			
		});

		/*
		 *  load/register datapacks .zip files from world save folder
		 */
		Treasure.LOGGER.debug("loading datapack template files ...");
		// get all .zip files in the folder (non-recursive)
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(worldSaveFolderPathName))) {
			for (Path jarPath : stream) {
				Treasure.LOGGER.debug("datapack path -> {}", jarPath);
				if (Files.isRegularFile(jarPath, new LinkOption[] {})) {
					if (jarPath.getFileName().toString().endsWith(".zip")) {
						// process this zip file
						Treasure.LOGGER.debug("datapack file -> {}", jarPath.toString());
						try (ZipFile zipFile = new ZipFile(jarPath.toFile())) {
							StructureCategory.getNames().forEach(category -> {
								List<Path> templatePaths;				
								try {
									templatePaths = ModUtil.getPathsFromResourceJAR(jarPath, DATAPACKS_TEMPLATES_ROOT + category.toLowerCase());

									for (Path path : templatePaths) {
										Treasure.LOGGER.debug("datapack template path -> {}", path);
										// load the shell from the jar
										Optional<GottschTemplate> shell = loadFromZip(zipFile, path, markerBlocks, replacementBlocks);
										// register
										registerDatapacksTemplate(path, shell.get());
									}
								} catch (Exception e) {
									// minimal message
									Treasure.LOGGER.warn("warning: unable to load datapack -> {}", jarPath + "/" + DATAPACKS_TEMPLATES_ROOT + category.toLowerCase());
								}
							});
						}
					}
				}
			}
		} catch(Exception e) {
			Treasure.LOGGER.error("error: unable to load datapack:", e);
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static Optional<GottschTemplate> loadFromFileSystem(Path path, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		try {
			File file = path.toFile();
			if (file.exists()) {
				InputStream inputStream = new FileInputStream(file);
				Optional<GottschTemplate> template = loadTemplateFromStream(inputStream, markerBlocks, replacementBlocks);
				return template;
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to loot table manifest");
		}	
		return Optional.empty();
	}

	/**
	 * 
	 * @param zipFile
	 * @param resourceFilePath
	 * @param markerBlocks
	 * @param replacementBlocks
	 * @return
	 */
	public static Optional<GottschTemplate> loadFromZip(ZipFile zipFile, Path resourceFilePath, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		Optional<GottschTemplate> resourceTemplate = Optional.empty();

		try {
			ZipEntry zipEntry = zipFile.getEntry(resourceFilePath.toString());
			InputStream stream = zipFile.getInputStream(zipEntry);
			//			Reader reader = new InputStreamReader(stream);
			// load the loot table
			//			resourceTemplate =  Optional.of(loadLootTable(reader));
			resourceTemplate = loadTemplateFromStream(stream, markerBlocks, replacementBlocks);
			// close resources
			stream.close();

		} catch(Exception e) {
			Treasure.LOGGER.error(String.format("Couldn't load resource loot table from zip file ->  {}", resourceFilePath), e);
		}
		return resourceTemplate;
	}

	/**
	 * 
	 * @param path
	 * @param template
	 */
	private static void registerDatapacksTemplate(Path path, GottschTemplate template) {
		// extract the category
		String categoryToken = path.getName(3).toString();
		Optional<IStructureCategory> category = TreasureApi.getStructureCategory(categoryToken);
		if (category.isEmpty()) {
			Treasure.LOGGER.warn("structure category -> '{}' is not registgered", categoryToken);
			return;
		}

		String typeToken = null;
		Optional<IStructureType> type = Optional.empty();
		if (path.getNameCount() > 4) {
			typeToken = path.getName(4).toString();
			type = TreasureApi.getStructureType(typeToken);
			if (type.isEmpty()) {
				Treasure.LOGGER.warn("structure type -> '{}' is not registgered", typeToken);
				return;
			}
		}

		// convert to resource location
		ResourceLocation resourceLocation = asResourceLocation(path);
		Treasure.LOGGER.debug("resource location -> {}", resourceLocation);

		// setup the template holder
		TemplateHolder holder = new TemplateHolder()
				.setLocation(resourceLocation)
				.setTemplate(template);				

		if (path.getNameCount() > 5) {
			for (int i = 5; i < path.getNameCount(); i++) {
				holder.getTags().add(path.getName(i).toString());
			}
		}

		// add to map
		DATAPACK_MAP.put(resourceLocation, holder);

		// add to table
		if (!DATAPACK_TABLE.contains(category.get(), type.get())) {
			DATAPACK_TABLE.put(category.get(), type.get(),new ArrayList<>());
		}
		DATAPACK_TABLE.get(category.get(), type.get()).add(holder);
		Treasure.LOGGER.debug("tabling datapack template -> [{}, {}] -> {}", category.get(), type.get(), holder.getLocation().toString());
	}

	public static Collection<TemplateHolder> getTemplate(StructureType well) {
		List<TemplateHolder> templateHolders = new ArrayList<>();
		DATAPACK_TABLE.column(well).forEach((key, list) -> {
			templateHolders.addAll(list);		
		});

		if (templateHolders.isEmpty()) {
			TABLE.column(well).forEach((key, list) -> {
				templateHolders.addAll(list);
			});
		}
		return templateHolders;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Optional<TemplateHolder> getTemplate(ResourceLocation name) {
		TemplateHolder templateHolder = DATAPACK_MAP.get(name);
		if (templateHolder == null) {
			templateHolder = MAP.get(name);
		}
		return Optional.ofNullable(templateHolder);
	}
	
	/**
	 * 
	 * @param random
	 * @param category
	 * @param type
	 * @return
	 */
	public static List<TemplateHolder> getTemplate(IStructureCategory category, IStructureType type) {
		List<TemplateHolder> templateHolders = DATAPACK_TABLE.get(category, type);
		if (templateHolders == null) {
			templateHolders = TABLE.get(category, type);
		}

		if (templateHolders == null || templateHolders.isEmpty()) {
			Treasure.LOGGER.debug("could not find template holders for category -> {}, type -> {}", category, type);
		}
		Treasure.LOGGER.debug("selected template holders -> {} ", templateHolders);

		if (templateHolders == null) {
			templateHolders = new ArrayList<>();
		}
		return templateHolders;
	}

	/**
	 * 
	 * @param category
	 * @param type
	 * @param biome
	 * @return
	 */
	public static List<TemplateHolder> getTemplate(IStructureCategory category, IStructureType type, ResourceLocation biome) {

		AccessKey key = new AccessKey(category, type);
		
		// TODO get from white list
		List<TemplateHolder> whitelistHolders = WHITELIST_TABLE.get(key, biome);
		List<TemplateHolder> blacklistHolders = BLACKLIST_TABLE.get(key, biome);
		
		// grab all templates by category + type
		List<TemplateHolder> templateHolders = DATAPACK_TABLE.get(category, type);
		if (templateHolders == null) {
			templateHolders = TABLE.get(category, type);
		}

		// filter out any in the black list
		templateHolders = templateHolders.stream().filter(h -> blacklistHolders.stream().noneMatch(b -> b.getLocation().equals(h.getLocation()))).collect(Collectors.toList());

		if (templateHolders == null || templateHolders.isEmpty()) {
			Treasure.LOGGER.debug("could not find template holders for category -> {}, type -> {}", category, type);
		}
		Treasure.LOGGER.debug("selected template holders -> {} ", templateHolders);

		if (templateHolders == null) {
			templateHolders = new ArrayList<>();
		}
		return templateHolders;
	}

	/**
	 * @param structMetaList
	 */
	public static void registerAccesslists(List<StructMeta> structMetaList) {

		// clear lists
		BLACKLIST_TABLE.clear();
		WHITELIST_TABLE.clear();

		// process each struct meta in the list
		structMetaList.forEach(meta -> {
			// create location for template
			ResourceLocation templateLocation = ModUtil.asLocation(meta.getName());
			Path templatePath = Paths.get(templateLocation.getPath());

			// extract the category
			String categoryToken = templatePath.getName(0).toString();
			Optional<IStructureCategory> category = TreasureApi.getStructureCategory(categoryToken);
			if (category.isEmpty()) {
				Treasure.LOGGER.warn("structure category -> '{}' is not registgered", categoryToken);
				return;
			}

			String typeToken = templatePath.getName(1).toString();
			Optional<IStructureType> type = TreasureApi.getStructureType(typeToken);
			if (type.isEmpty()) {
				Treasure.LOGGER.warn("structure type -> '{}' is not registgered", typeToken);
				return;
			}

			// build key
			AccessKey key = new AccessKey(category.get(), type.get());

			// get the template holder
			Optional<TemplateHolder> holder = getHolderByResourceLocation(templateLocation);
			if (holder.isPresent()) {
				// process black list
				meta.getBiomeBlacklist().forEach(biomeName -> {
					// create resource location
					ResourceLocation biomeLocation = ModUtil.asLocation(biomeName);
					// ensure that it is indeed a biome
					Biome biome = ForgeRegistries.BIOMES.getValue(biomeLocation);
					if (biome == null) {
						Treasure.LOGGER.warn("biome -> '{}' is not registgered", biomeLocation);
						return;
					}						
					if (!BLACKLIST_TABLE.contains(key, biomeLocation)) {
						BLACKLIST_TABLE.put(key, biomeLocation, new ArrayList<>());
					}
					BLACKLIST_TABLE.get(key, biomeLocation).add(holder.get());
				});

				// process white list
				meta.getBiomeWhitelist().forEach(biomeName -> {
					// create resource location
					ResourceLocation biomeLocation = ModUtil.asLocation(biomeName);
					if (!WHITELIST_TABLE.contains(key, biomeLocation)) {
						WHITELIST_TABLE.put(key, biomeLocation, new ArrayList<>());
					}
					WHITELIST_TABLE.get(key, biomeLocation).add(holder.get());
				});				
			}
		});
	}

	private static Optional<TemplateHolder> getHolderByResourceLocation(ResourceLocation templateLocation) {
		// first check datapacks
		TemplateHolder holder = DATAPACK_MAP.get(templateLocation);
		if (holder == null) {
			holder = MAP.get(templateLocation);
		}
		return Optional.ofNullable(holder);
	}

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
		TreasureTemplateRegistry.markerScanList = scanList;
	}

	public static Map<StructureMarkers, Block> getMarkerMap() {
		return markerMap;
	}

	public void setMarkerMap(Map<StructureMarkers, Block> markerMap) {
		TreasureTemplateRegistry.markerMap = markerMap;
	}

	public static Map<BlockState, BlockState> getReplacementMap() {
		return replacementMap;
	}

	public void setReplacementMap(Map<BlockState, BlockState> replacementMap) {
		TreasureTemplateRegistry.replacementMap = replacementMap;
	}

	public static ICoords getOffset(Random random, GottschTemplate template) {
		ICoords offsetCoords = template.findCoords(random, getMarkerMap().get(StructureMarkers.OFFSET));
		return offsetCoords;
	}

	public static ICoords getOffsetFrom(Random random, GottschTemplate template, StructureMarkers marker) {
		ICoords offsetCoords = template.findCoords(random, getMarkerMap().get(marker));
		return offsetCoords;
	}
}
