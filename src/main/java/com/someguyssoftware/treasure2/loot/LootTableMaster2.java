/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.loot.LootTableMaster;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.version.BuildVersion;
import com.someguyssoftware.gottschcore.version.VersionChecker;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * @author Mark Gottschling on Dec 1, 2020
 *
 */
public class LootTableMaster2 {
	public static Logger LOGGER = LogManager.getLogger(Treasure.logger.getName());

	public static final String LOOT_TABLES_FOLDER = "loot_tables";
	private static final String BASE_FOLDER = "base";
	private static final String INJECT_FOLDER = "inject";

	private static final Gson GSON_INSTANCE = (new GsonBuilder())
			.registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).create();

	private final IMod mod;
	private File worldDataBaseFolder;

	/**
	 * 
	 */
	public LootTableMaster2(IMod mod) {
		this.mod = mod;
	}

	/**
	 * From WorldServer.class: this.lootTable = new LootTableManager(new File(new
	 * File(this.saveHandler.getWorldDirectory(), "data"), "loot_tables"));
	 */
	public LootTableMaster2(IMod mod, File folder) {
		this.mod = mod;
		this.worldDataBaseFolder = folder;
	}

	/**
	 * Call in WorldEvent.Load event handler.
	 * @param world
	 */
	public void init(WorldServer world) {
		Path path = Paths.get(world.getSaveHandler().getWorldDirectory().getPath(), "data", "loot_tables");
		Treasure.logger.debug("path to world data loot tables -> {}", path.toAbsolutePath());
		setWorldDataBaseFolder(path.toFile());
	}

	/**
	 * CREATES CONFIG FOLDERS/RESOURCES - on mod/manager creation
	 * Creates all the necessary folder and resources before actual loading of loot tables.
	 * Call in your @Mod class in preInt() or int().
	 * 
	 * @param resourceRootPath
	 * @param modID
	 */
	public void buildAndExpose(String resourceRootPath, String modID, List<String> locations) {
		GottschCore.logger.debug("loot table folder locations -> {}", locations);
		// create paths to custom loot tables if they don't exist
		for (String location : locations) {
			GottschCore.logger.debug("buildAndExpose location -> {}", location);
			createLootTableFolder(modID, location);
			exposeLootTable(resourceRootPath, modID, location);
		}
	}

	/**
	 * ONE STOP SHOP to tell where the resource is and to add to the maps and to register with minecraft
	 * 
	 * Call in WorldEvent.Load event handler.
	 * Overide this method if you have a different cache mechanism.
	 * @param location
	 */
	public void register(WorldServer world, String modID, List<String> locations) {
		// TODO copy files from config location to world data location if not there already
		

		for (String location : locations) {
			// get loot table files as ResourceLocations from the world data location
			List<ResourceLocation> locs = getLootTablesResourceLocations(modID, location);
		}
		
		// register it with MC
//		ResourceLocation newLoc = LootTableList.register(loc);
		
		// TODO finish
	}

	/**
	 * 
	 * @param modID
	 * @param location
	 */
	protected void createLootTableFolder(String modID, String location) {
		// ensure that the requried properties (modID) is not null
		if (modID == null || modID.isEmpty()) {
			modID = getMod().getId();
		}

		/*
		 *  build a path to the specified location
		 *  ie ../[CONFIG FOLDER]/[MODID]/[LOOT_TABLES]/[location]
		 */
		Path configPath = Paths.get(getMod().getConfig().getConfigFolder());
		Path folder = Paths.get(configPath.toString(), modID, LOOT_TABLES_FOLDER, ((location != null && !location.isEmpty()) ? /*(location + "/")*/location : "")).toAbsolutePath();

		if (Files.notExists(folder)) {
			GottschCore.logger.debug("loot tables folder \"{}\" will be created.", folder.toString());
			try {
				Files.createDirectories(folder);

			} catch (IOException e) {
				GottschCore.logger.warn("Unable to create loot tables folder \"{}\"", folder.toString());
			}
		}
	}
	
	/**
	 * 
	 * @param modID
	 * @param location
	 */
	protected void createWorldDataLootTableFolder(String modID, String location) {
		// ensure that the requried properties (modID) is not null
		if (modID == null || modID.isEmpty()) {
			modID = getMod().getId();
		}

		/*
		 *  build a path to the specified location
		 *  ie ../[WORLD DATA]/[LOOT_TABLES]/[MOD ID]/[location]
		 */
		Path worldDataFilePath = Paths.get(getWorldDataBaseFolder().toString(), modID, location).toAbsolutePath();
		
		if (Files.notExists(worldDataFilePath)) {
			GottschCore.logger.debug("loot tables folder \"{}\" will be created.", worldDataFilePath.toString());
			try {
				Files.createDirectories(worldDataFilePath);

			} catch (IOException e) {
				GottschCore.logger.warn("Unable to create world data loot tables folder \"{}\"", worldDataFilePath.toString());
			}
		}
	}

	/**
	 * 
	 * @param modID
	 * @param location
	 */
	protected void exposeLootTable(String resourceRootPath, String modID, String location) {
		// ensure that the requried properties are not null
		if (modID == null || modID.isEmpty())
			modID = getMod().getId();
		location = (location != null && !location.equals("")) ? (location + "/") : "";

		Stream<Path> walk = null;
		FileSystem fs = getResourceAsFileSystem(resourceRootPath, modID, location);
		if (fs == null)
			return;

		try {
			// get the base path of the resource
			Path resourceBasePath = fs.getPath(resourceRootPath, modID, location);
			Path folder = Paths.get(getMod().getConfig().getConfigFolder(), modID, LOOT_TABLES_FOLDER, location).toAbsolutePath();

			boolean isFirst = true;
			// proces all the files in the folder
			walk = Files.walk(resourceBasePath, 1);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
				Path resourceFilePath = it.next();
				// String tableName = resourceFilePath.getFileName().toString();
				 GottschCore.logger.debug("mod loot_table -> {}", resourceFilePath.toString());
				// check the first file, which is actually the given directory itself
				if (isFirst) {
					// create the file system folder if it doesn't exist
					if (Files.notExists(folder)) {
						createLootTableFolder(modID, location);
					}
				} else {
					if (Files.isDirectory(resourceFilePath)) {
						GottschCore.logger.debug("resource is a folder -> {}", resourceFilePath.toString());
						continue;
					}
					
					// test if file exists on the file system
					Path fileSystemFilePath = Paths.get(folder.toString(), resourceFilePath.getFileName().toString()).toAbsolutePath();
					GottschCore.logger.debug("config loot table path -> {}", fileSystemFilePath.toString());

					if (Files.notExists(fileSystemFilePath)) {
						// copy from resource/classpath to file path
						copyResourceToFileSystem(resourceFilePath, fileSystemFilePath);
					}
					else {
						boolean isCurrent = false;
						try {
							isCurrent = isFileSystemVersionCurrent(resourceFilePath, fileSystemFilePath);
						}
						catch(Exception e) {
							GottschCore.logger.warn(e.getMessage(), e);
							continue;
						}
						
						GottschCore.logger.error("is file system (config) loot table current -> {}", isCurrent);
						if (!isCurrent) {
							Files.move(
									fileSystemFilePath, 
									Paths.get(folder.toString(), resourceFilePath.getFileName().toString() + ".bak").toAbsolutePath(), 
									StandardCopyOption.REPLACE_EXISTING);
							copyResourceToFileSystem(resourceFilePath, fileSystemFilePath);
						}
					}
				}
				isFirst = false;
			}
		} catch (Exception e) {
			GottschCore.logger.error("error:", e);
		} finally {
			// close the stream
			if (walk != null) {
				walk.close();
			}
		}

		// close the file system
		if (fs != null && fs.isOpen()) {
			try {
				fs.close();
			} catch (IOException e) {
				GottschCore.logger.debug("An error occurred attempting to close the FileSystem:", e);
			}
		}
	}
	
	/**
	 * 
	 * @param resourceFilePath
	 * @param fileSystemFilePath
	 */
	private void copyResourceToFileSystem(Path resourceFilePath, Path fileSystemFilePath) {
		InputStream is = LootTableMaster.class.getResourceAsStream(resourceFilePath.toString());
		try (FileOutputStream fos = new FileOutputStream(fileSystemFilePath.toFile())) {
			byte[] buf = new byte[2048];
			int r;
			while ((r = is.read(buf)) != -1) {
				fos.write(buf, 0, r);
			}
			} catch (IOException e) {
				GottschCore.logger.error("Error exposing resource to file system.", e);
			}
	}

	/**
	 * 
	 * @param configFilePath
	 * @param worldDataFilePath
	 * @return
	 */
	protected boolean isWorldDataVersionCurrent(Path configFilePath, Path worldDataFilePath) {
		boolean result = true;
		GottschCore.logger.debug("Verifying the most current version for the world data loot table -> {} ...", worldDataFilePath.getFileName());
		
		// config loot table
		String configJson;
		try {
			configJson = com.google.common.io.Files.toString(configFilePath.toFile(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			LOGGER.warn("Couldn't load config loot table from {}", configFilePath.toString(), e);
			return false;
		}
		
		//  world data config table
		String worldDataJson;
		try {
			worldDataJson = com.google.common.io.Files.toString(worldDataFilePath.toFile(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			LOGGER.warn("Couldn't load world data loot table from {}", worldDataFilePath.toString(), e);
			return false;
		}
		
		LootTableShell configLootTable = loadLootTable(configJson);
		LootTableShell worldDataLootTable = loadLootTable(worldDataJson);

		GottschCore.logger.debug("\n\t...config loot table -> {}\n\t...version -> {}\n\t...world data loot table -> {}\n\t...version -> {}",
				configFilePath.toString(),
				configLootTable.getVersion(),
				worldDataFilePath.toString(),
				worldDataLootTable.getVersion());
		
		// compare versions
		if (configLootTable != null && worldDataLootTable != null) {
			BuildVersion configVersion = new BuildVersion(configLootTable.getVersion());
			BuildVersion worldDataVersion = new BuildVersion(worldDataLootTable.getVersion());			
			result = VersionChecker.checkVersion(configVersion, worldDataVersion); // if 1st > 2nd, then false;
		}
		
		// compare modified dates
		if (result && configFilePath.toFile().lastModified() > worldDataFilePath.toFile().lastModified()) {
			result = false;
		}
		return result;	
	}
	
	/**
	 * 
	 * @param resourceFilePath
	 * @param fileSystemFilePath
	 * @return
	 * @throws Exception
	 */
	protected boolean isFileSystemVersionCurrent(Path resourceFilePath, Path fileSystemFilePath) throws Exception {
		boolean result = true;
		GottschCore.logger.debug("Verifying the most current version for the loot table -> {} ...", fileSystemFilePath.getFileName());

		// file system loot table - can't load as a resource at this location
		String configJson;
		try {
			configJson = com.google.common.io.Files.toString(fileSystemFilePath.toFile(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			LOGGER.warn("Couldn't load config loot table from {}", fileSystemFilePath.toString(), e);
			return false;
		}
		LootTableShell fsLootTable = loadLootTable(configJson);
		
		// turn file path into a resource location
		ResourceLocation loc = new ResourceLocation(getMod().getId(), resourceFilePath.toString().replace(".json", ""));
		
		// jar resource loot table
		URL url = LootTableManager.class.getResource(resourceFilePath.toString());
		if (url == null) {
			GottschCore.logger.debug("Unable to get resource -> {}", resourceFilePath.toString());
			return false;
		}
		
		String resourceJson;
		try {
			resourceJson = Resources.toString(url, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO make custom exception
			throw new Exception(String.format("Couldn't load loot table %s from %s", resourceFilePath, url), e);
		}

		LootTableShell resourceLootTable = null;
		try {
			resourceLootTable = loadLootTable(resourceJson);
		} catch (IllegalArgumentException | JsonParseException e) {
			// TODO make custom exception
			throw new Exception(String.format("Couldn't load loot table %s from %s", resourceFilePath, url), e);
		}
		
		GottschCore.logger.debug("\n\t...file system loot table -> {}\n\t...version -> {}\n\t...resource loot table -> {}\n\t...version -> {}",
				fileSystemFilePath.toString(),
				fsLootTable.getVersion(),
				loc.toString(),
				resourceLootTable.getVersion());
		
		// compare versions
		if (resourceLootTable != null && fsLootTable != null) {
			BuildVersion resourceVersion = new BuildVersion(resourceLootTable.getVersion());
			BuildVersion fsVersion = new BuildVersion(fsLootTable.getVersion());			
			result = VersionChecker.checkVersion(resourceVersion, fsVersion); // if 1st > 2nd, then false;
		}
		return result;
	}
	
	/**
	 * 
	 * @param modID
	 * @param location
	 * @return
	 */
	protected FileSystem getResourceAsFileSystem(String resourceRootPath, String modID, String location) {
		FileSystem fs = null;
		Map<String, String> env = new HashMap<>();
		URI uri = null;

		// get the asset resource folder that is unique to this mod
		resourceRootPath = "/" + resourceRootPath.replaceAll("^/|/$", "") + "/";
		URL url = GottschCore.class.getResource(resourceRootPath + modID + "/" + location);
		if (url == null) {
			GottschCore.logger.error("Unable to locate resource {}", resourceRootPath + modID + "/" + location);
			return null;
		}

		// convert to a uri
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			GottschCore.logger.error("An error occurred during loot table processing:", e);
			return null;
		}

		// split the uri into 2 parts - jar path and folder path within jar
		String[] array = uri.toString().split("!");
		try {
			fs = FileSystems.newFileSystem(URI.create(array[0]), env);
		} catch (IOException e) {
			GottschCore.logger.error("An error occurred during loot table processing:", e);
			return null;
		}

		return fs;
	}
	
	/**
	 * Gather all json files at location as a ResourceLocation list.
	 * @param modIDIn
	 * @param locationIn
	 * @return
	 */
	public List<ResourceLocation> getLootTablesResourceLocations(String modIDIn, String locationIn) {
		// ensure that the requried properties are not null
		final String modID = (modIDIn == null || modIDIn.isEmpty()) ? getMod().getId() : modIDIn;
		final String location= (locationIn != null && !locationIn.equals("")) ? (locationIn + "/") : "";

		List<ResourceLocation> locs = new ArrayList<>();
		Path path = Paths.get(getWorldDataBaseFolder().getPath(), modID, location).toAbsolutePath();

		 GottschCore.logger.debug("Path to world data loot table -> {}", path.toString());
		// check if path/folder exists
		if (Files.notExists(path)) {
			GottschCore.logger.debug("Unable to locate -> {}", path.toString());
			return locs;
		}

		try {
			Files.walk(path).filter(Files::isRegularFile).forEach(f -> {
				 GottschCore.logger.debug("World data loot table file -> {}", f.toAbsolutePath().toString());
				 /*
				  * only add .json files. (not .bak or anything else)
				  */
				 if (FilenameUtils.getExtension(f.getFileName().toString()).equals("json")) {
					ResourceLocation loc = 
							new ResourceLocation(modID + ":" + location + f.getFileName().toString().replace(".json", ""));
					GottschCore.logger.debug("Resource location -> {}", loc);
					locs.add(loc);
				 }
			});
		} catch (IOException e) {
			GottschCore.logger.error("Error processing custom loot table:", e);
		}
		return locs;
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public LootTableShell loadLootTable(String json) throws IllegalArgumentException, JsonParseException {
		return GSON_INSTANCE.fromJson(json, LootTableShell.class);
	}
	
	/**
	 * TODO change File path to Path path and File baseFolder to Path baseFolder
	 * @param resource
	 * @return
	 */
	public Optional<LootTableShell> loadLootTable(File folder, ResourceLocation resource) {

		if (getWorldDataBaseFolder() == null) {
			return Optional.empty();
		}
		else {
//			File lootTableFile = new File(new File(getWorldDataBaseFolder(), resource.getResourceDomain()),
//					resource.getResourcePath() + ".json");

			File lootTableFile = Paths.get(folder.getPath(), resource.getResourceDomain(), resource.getResourcePath() + ".json").toFile();
			LOGGER.debug("Attempting to load loot table {} from {}", resource, lootTableFile);
			
			if (lootTableFile.exists()) {
				if (lootTableFile.isFile()) {
					String json;
					try {
						json = com.google.common.io.Files.toString(lootTableFile, StandardCharsets.UTF_8);
					}
					catch (IOException e) {
						LOGGER.warn("Couldn't load loot table {} from {}", resource, lootTableFile, e);
						return Optional.empty();
					}

					try {
						return Optional.of(loadLootTable(json));
					}
					catch (IllegalArgumentException | JsonParseException e) {
						LOGGER.error("Couldn't load loot table {} from {}", resource, lootTableFile, e);
						return Optional.empty();
					}
				}
				else {
					LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", resource, lootTableFile);
					return Optional.empty();
				}
			}
			else {
				LOGGER.warn("Expected to find loot table {} at {} but it doesn't exist.", resource, lootTableFile);
				return Optional.empty();
			}
		}
	}

	public File getWorldDataBaseFolder() {
		return worldDataBaseFolder;
	}

	public void setWorldDataBaseFolder(File baseFolder) {
		this.worldDataBaseFolder = baseFolder;
	}

	public IMod getMod() {
		return mod;
	}
	
	public Gson getGsonInstance() {
		return GSON_INSTANCE;
	}
}
