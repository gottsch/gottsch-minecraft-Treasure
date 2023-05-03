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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.mojang.datafixers.DataFixer;
import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplateManager;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Custom template manager that create TreasureTemplates instead of Template.
 * 
 * @author Mark Gottschling on Jan 19, 2019
 *
 */
public class TreasureTemplateManager extends GottschTemplateManager {
	private static final String SAVE_FORMAT_LEVEL_SAVE_SRG_NAME = "field_71310_m";
	
	private final Map<ResourceLocation, TemplateHolder> templatesByResourceLocation = new HashMap<>();
	
	private final Table<IMetaArchetype, IMetaType, List<TemplateHolder>> templatesByArchetypeType = HashBasedTable.create();

	private final Table<String, ResourceLocation, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();

	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("surface", "subterranean", "submerged", "float", "wells");
	
	/*
	 * use this map when structures are submerged instead of the default marker map
	 */
	private Map<StructureMarkers, Block> waterMarkerMap;
	
	private File worldSaveFolder;
	
	/**
	 * 
	 * @param baseFolder
	 * @param fixer
	 */
	public TreasureTemplateManager(IMod mod, String resourceFolder, DataFixer fixer) {
		super(mod, resourceFolder, fixer);
		Treasure.LOGGER.debug("creating a TreasureTemplateManager");

		// init water marker map
        // setup standard list of markers
        waterMarkerMap = Maps.newHashMap(getMarkerMap());
        waterMarkerMap.put(StructureMarkers.NULL, Blocks.AIR);// <-- this is the difference between default

		// initialize table
		for (IMetaArchetype archetype : StructureArchetype.values()) {
			for (IMetaType type : com.someguyssoftware.treasure2.meta.StructureType.values()) {
				templatesByArchetypeType.put(archetype, type, new ArrayList<>(5));
			}
		}
	}
	
	public void init(ServerWorld world) {
		Object save = ObfuscationReflectionHelper.getPrivateValue(MinecraftServer.class, world.getServer(), SAVE_FORMAT_LEVEL_SAVE_SRG_NAME);
		if (save instanceof SaveFormat.LevelSave) {
			Path path = ((SaveFormat.LevelSave) save).getWorldDir();
			setWorldSaveFolder(path.toFile());
		}
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
	 * @param resourcePaths
	 */
	public void register(String modID, List<String> resourcePaths) {
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
						
			tableTemplate(modID, loc, load(loc, getMarkerScanList(), getReplacementMap()));
		});
	}
	
	/**
	 * Overridden due to a difference in source paths from GottschCore
	 */
	@Override
	public boolean readTemplate(ResourceLocation location, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		Treasure.LOGGER.debug("template path -> {}", location);
		// this is the file system path
		Path path = Paths.get(getWorldSaveFolder().getPath(), "datapacks", Treasure.MODID, "data",  location.getNamespace(), "structures", location.getPath());
		File file1 = path.toFile();
		Treasure.LOGGER.debug("template file path -> {}", file1.getAbsoluteFile());
		if (!file1.exists()) {
			Treasure.LOGGER.debug("file does not exist, read from jar -> {}", file1.getAbsolutePath());
			return readTemplateFromJar(location, markerBlocks, replacementBlocks);
		} else {
			Treasure.LOGGER.debug("reading template from file system using file path -> {}", file1.getAbsolutePath());
			InputStream inputstream = null;
			boolean flag;

			try {
				inputstream = new FileInputStream(file1);
				this.readTemplateFromStream(location.toString(), inputstream, markerBlocks, replacementBlocks);
				return true;
			} catch (Throwable var10) {
				flag = false;
			} finally {
				IOUtils.closeQuietly(inputstream);
			}

			return flag;
		}
	}
	
	/**
	 * reads a template from the minecraft jar
	 */
	private boolean readTemplateFromJar(ResourceLocation id, List<Block> markerBlocks, Map<BlockState, BlockState> replacementBlocks) {
		String s = id.getNamespace();
		String s1 = id.getPath();
		InputStream inputstream = null;
		boolean flag;

		try {
			String relativePath = "data/" + id.getNamespace() + "/structures/" + id.getPath();
			Treasure.LOGGER.debug("Attempting to load template {} from jar -> {}", id, relativePath);
			inputstream = Treasure.instance.getClass().getClassLoader().getResourceAsStream(relativePath);
			this.readTemplateFromStream(id.toString(), inputstream, markerBlocks, replacementBlocks);
			return true;
			// TODO change from Throwable
		} catch (Throwable e) {
			Treasure.LOGGER.error("error reading resource: ", e);
			flag = false;
		} finally {
			IOUtils.closeQuietly(inputstream);
		}

		return flag;
	}
	
	/**
	 * reads a template from an inputstream
	 */
	private void readTemplateFromStream(String id, InputStream stream, List<Block> markerBlocks, 
			Map<BlockState, BlockState> replacementBlocks) throws IOException {
		
		CompoundNBT nbt = CompressedStreamTools.readCompressed(stream);

		if (!nbt.contains("DataVersion", 99)) {
			nbt.putInt("DataVersion", 500);
		}

		GottschTemplate template = new GottschTemplate();
		template.load(NBTUtil.update(getFixer(), DefaultTypeReferences.STRUCTURE, nbt, nbt.getInt("DataVersion")), markerBlocks, replacementBlocks);
		Treasure.LOGGER.debug("adding template to map with key -> {}", id);
		this.getTemplates().put(id, template);
	}
	
	/**
	 * 
	 * @param modID
	 */
	private void createTemplateFolder(String modID) {
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
	 * @param resourceLocation
	 * @param template
	 */
	private void tableTemplate(String modID, ResourceLocation resourceLocation, Template template) {
		if (template != null) {
			Path path = Paths.get(resourceLocation.getPath().toString());
			// build the key for the meta manager to look at
			ResourceLocation metaResourceLocation = new ResourceLocation(modID, 
					"structures" + "/" + path.getFileName().toString().replace(".nbt", ".json"));
			String key = metaResourceLocation.toString();
			Treasure.LOGGER.debug("Using key to find meta -> {}", key);
			
			/*
			 *  look for IMeta in MetaManager by treasure2:structures/x.nbt
			 */
			StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(key);
			if (meta == null) {
				// there isn't a meta found for resource, skip to next template
				Treasure.LOGGER.info("Unable to locate meta file for resource -> {}", key);
				return;
			}				
			if (meta.getArchetypes() == null || meta.getArchetypes().isEmpty() || meta.getType() == null) {
				Treasure.LOGGER.info("Meta file not properly configured. -> {}", key);
				return;
			}
			
			/*
			 *  determine if the meta decayRuleSetName is populated
			 */
			List<ResourceLocation> decayRuleSetResourceLocation = new ArrayList<>();
			if (meta.getDecayRuleSetName() != null && meta.getDecayRuleSetName().size() > 0) {
				// build the keys for the meta manager to look at
				for (String ruleSetName : meta.getDecayRuleSetName()) {
					ResourceLocation decayResourceLocation = new ResourceLocation(modID,	"rulesets/" + ruleSetName + ".json");
					decayRuleSetResourceLocation.add(decayResourceLocation);
					Treasure.LOGGER.debug("Using key to find decay ruleset -> {}", decayRuleSetResourceLocation.toString());
				}
			}
			
			// setup the template holder
			TemplateHolder holder = new TemplateHolder()
					.setMetaLocation(metaResourceLocation)
					.setLocation(resourceLocation)
					.setDecayRuleSetLocation(decayRuleSetResourceLocation)
					.setTemplate(template);			
			
			// map by resource location
			getTemplatesByResourceLocationMap().put(resourceLocation, holder);
			
			// map according to meta archetype, type
			for (IMetaArchetype archetype : meta.getArchetypes()) {					
				Treasure.LOGGER.debug("Using meta to map archetype type -> {}", meta.toString());										
				if (!templatesByArchetypeType.contains(archetype, meta.getType())) {
					templatesByArchetypeType.put(archetype, meta.getType(), new ArrayList<>(3));
				}
				this.templatesByArchetypeType.get(archetype, meta.getType()).add(holder);
				
				Treasure.LOGGER.debug("Registered holder -> location -> {}, meta -> {}, decay -> {}",
						holder.getLocation(), 
						holder.getMetaLocation(),
						holder.getDecayRuleSetLocation());

				// TODO could move the wrapping for into this method instead, then could lose the archetype that is passed in. ***!!!
				mapToTemplatesByArchetypeBiome(metaResourceLocation, resourceLocation, decayRuleSetResourceLocation, archetype, meta.getType(), template);
			}
		}
		else {
			Treasure.LOGGER.debug("unable to table meta from -> {}", resourceLocation);
		}
	}
	
	/**
	 * 
	 * @param metaResourceLocation
	 * @param location
	 * @param archetype
	 * @param template
	 */
	private void mapToTemplatesByArchetypeBiome(ResourceLocation metaResourceLocation, 
			ResourceLocation location, List<ResourceLocation> decayResourceLocation, IMetaArchetype archetype, IMetaType type, 
			Template template) {

		// build mapping key
		String key = archetype.getName() + ":" + type.getName();
		
		// find the meta for the template
		StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(metaResourceLocation.toString());
		
		// create a holder for the template
		TemplateHolder holder = new TemplateHolder()
				.setMetaLocation(metaResourceLocation)
				.setDecayRuleSetLocation(decayResourceLocation)
				.setLocation(location)
				.setTemplate(template);
		
		// map according biomes - @see DungeonConfigManager @ line 55.
		// TODO somehow extract the indexing of biomes to a helper class
		if (meta.getBiomeWhiteList().contains("*")
				|| (meta.getBiomeWhiteList().isEmpty() && meta.getBiomeBlackList().isEmpty())) {
			Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValues();

			for (Biome biome : biomes) {
				if (biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NETHER) {
					if (!templatesByArchetypeTypeBiome.contains(key, biome.getRegistryName())) {
						templatesByArchetypeTypeBiome.put(key, biome.getRegistryName(), new ArrayList<>(3));
					}					
					templatesByArchetypeTypeBiome.get(key, biome.getRegistryName()).add(holder);
				}
			}
		} else {
			if (!meta.getBiomeWhiteList().isEmpty()) {
				for (String b : meta.getBiomeWhiteList()) {
					String biomeName = b.trim().toLowerCase();
					ResourceLocation biomeLoc = new ResourceLocation(biomeName);
					Biome biome = ForgeRegistries.BIOMES.getValue(biomeLoc);
					if (biome == null) {
						Treasure.LOGGER.debug("Unable to locate biome for name -> {}", biomeName);
						continue;
					}
					if (biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NETHER) {
						if (!templatesByArchetypeTypeBiome.contains(key, biomeLoc)) {
							templatesByArchetypeTypeBiome.put(key, biomeLoc, new ArrayList<>(3));
						}
						templatesByArchetypeTypeBiome.get(key, biomeLoc).add(holder);
					}
				}
			} else if (!meta.getBiomeBlackList().isEmpty()) {
				/*
				 * add all the black listed biome IDs (ResourceLocations) to a list
				 */
				List<ResourceLocation> blackListBiomeIDs = new ArrayList<>();
				for (String b : meta.getBiomeBlackList()) {
					ResourceLocation biomeLocation = new ResourceLocation(b.trim().toLowerCase());
					Biome biome = ForgeRegistries.BIOMES.getValue(biomeLocation);
					if (biome != null) {
						blackListBiomeIDs.add(biomeLocation);
					}
					else {
						Treasure.LOGGER.debug("Unable to locate biome for name -> {}", b);
						continue;
					}
				}
				 // get the set of all biomes
				Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValues();
				// for each biome is the list
				for (Biome biome : biomes) {
					if (!blackListBiomeIDs.contains(biome.getRegistryName())
							&& biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NETHER) {
						if (!templatesByArchetypeTypeBiome.contains(key, biome.getRegistryName())) {
							templatesByArchetypeTypeBiome.put(key, biome.getRegistryName(), new ArrayList<>(3));
						}
						templatesByArchetypeTypeBiome.get(key, biome.getRegistryName()).add(holder);
					}
				}
			}
		}		
	}


	
	/**
	 * 
	 * @param random
	 * @param archetype
	 * @param type
	 * @param biome
	 * @return
	 */
	public TemplateHolder getTemplate(Random random, StructureArchetype archetype, StructureType type, Biome biome) {
		// get structure by archetype (subterranean) and type (room)
		String key =archetype.getName()	+ ":" + type.getName();
		
		List<TemplateHolder> templateHolders = getTemplatesByArchetypeTypeBiomeTable().get(key, biome.getRegistryName());
		if (templateHolders == null || templateHolders.isEmpty()) {
			Treasure.LOGGER.debug("could not find template holders for archetype:type, biome -> {} {}", key, biome.getRegistryName());
			return null;
		}
		
		TemplateHolder holder = templateHolders.get(random.nextInt(templateHolders.size()));
		if (holder == null) {
			Treasure.LOGGER.debug("could not find random template holder.");
			return null;
		}

		Treasure.LOGGER.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());

		return holder;
	}
	
	/**
	 * 
	 */
	@Override
	public GottschTemplateManager loadAll(List<String> locations) {
		// prevent this function from executing as the implementation version should be
		// used.
		return this;
	}

	/**
	 * writes the template to an external folder
	 */
	public boolean writeTemplate(@Nullable MinecraftServer server, ResourceLocation id) {
		String s = id.getPath();

		if (server != null && this.getTemplates().containsKey(s)) {
			File file1 = new File(this.getBaseResourceFolder());

			if (!file1.exists()) {
				if (!file1.mkdirs()) {
					return false;
				}
			} else if (!file1.isDirectory()) {
				return false;
			}

			File file2 = new File(file1, s + ".nbt");
			Template template = this.getTemplates().get(s);
			OutputStream outputstream = null;
			boolean flag;

			try {
				CompoundNBT nbttagcompound = template.save(new CompoundNBT());
				outputstream = new FileOutputStream(file2);
				CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
				return true;
			} catch (Throwable var13) {
				flag = false;
			} finally {
				IOUtils.closeQuietly(outputstream);
			}

			return flag;
		} else {
			return false;
		}
	}

	/**
	 * 
	 */
	public void dump() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");

		String filename = String.format("treasure-template-mgr-%s.txt", formatter.format(new Date()));

		Path path = Paths.get(getMod().getConfig().getConfigFolder(), getMod().getId(), "dumps").toAbsolutePath();
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			Treasure.LOGGER.error("Couldn't create directories for dump files:", e);
			return;
		}

		// setup a divider line
		char[] chars = new char[75];
		Arrays.fill(chars, '*');
		String div = new String(chars) + "\n";

		StringBuilder sb = new StringBuilder();

		String format = "**    %1$-33s: %2$-30s  **\n";
		String format2 = "**    %1$-15s: %2$-15s: %3$-33s  **\n";
		String heading = "**  %1$-67s  **\n";
		sb.append(div).append(String.format("**  %-67s  **\n", "TEMPLATE MANAGER")).append(div)
				.append(String.format(heading, "[Template By Type Map]"));
		for (Map.Entry<String, Template> entry : getTemplates().entrySet()) {
			sb.append(String.format(format, entry.getKey(), entry.getValue().getAuthor()));
		}
		sb.append(div);
		sb.append(String.format(heading, "[Template by Archetype:Type | Biome]"));
		Map<String, Map<ResourceLocation, List<TemplateHolder>>> map = getTemplatesByArchetypeTypeBiomeTable().rowMap();
		if (map == null || map.isEmpty()) { Treasure.LOGGER.debug("template biome map is null/empty");}
		Treasure.LOGGER.debug("biome map.size -> {}", map.size());
		
		for (String row : map.keySet()) {
			Treasure.LOGGER.debug("template biome row key -> {}", row);
			Map<ResourceLocation, List<TemplateHolder>> tmp = map.get(row);
			for (Entry<ResourceLocation, List<TemplateHolder>> entry : tmp.entrySet()) {
				String templateNames = entry.getValue().stream().map(a -> a.getLocation().toString()).collect(Collectors.joining(", "));
				Biome biome = ForgeRegistries.BIOMES.getValue(entry.getKey());
				String biomeName = "";
				if (biome != null) {
					biomeName = biome.getRegistryName().toString();
				}
				else {
					biomeName = String.format("No biome for {}", entry.getKey());
				}
				sb.append(String.format(format2, row, biomeName, templateNames));
			}
		}
		
		try {
			Files.write(Paths.get(path.toString(), filename), sb.toString().getBytes());
		} catch (IOException e) {
			Treasure.LOGGER.error("Error writing TreasureTemplateManager to dump file", e);
		}
	}
	
	public ICoords getOffset(Random random, TemplateHolder holder, StructureMarkers marker) {
		ICoords offsetCoords = ((GottschTemplate)holder.getTemplate()).findCoords(random, getMarkerMap().get(marker));
		return offsetCoords;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<ResourceLocation, TemplateHolder> getTemplatesByResourceLocationMap() {
		return templatesByResourceLocation;
	}
	
	/**
	 * 
	 * @return
	 */
	public Table<String, ResourceLocation, List<TemplateHolder>> getTemplatesByArchetypeTypeBiomeTable() {
		return templatesByArchetypeTypeBiome;
	}
	
	public File getWorldSaveFolder() {
		return worldSaveFolder;
	}

	public void setWorldSaveFolder(File worldSaveFolder) {
		this.worldSaveFolder = worldSaveFolder;
	}
}