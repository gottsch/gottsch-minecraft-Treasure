package com.someguyssoftware.treasure2.world.gen.structure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplateManager;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * Custom template manager that create TreasureTemplates instead of Template.
 * 
 * @author Mark Gottschling on Jan 19, 2019
 *
 */
public class TreasureTemplateManager extends GottschTemplateManager {

	private final Map<ResourceLocation, TemplateHolder> templatesByResourceLocation = new HashMap<>();
	
	private final Table<IMetaArchetype, IMetaType, List<TemplateHolder>> templatesByArchetypeType = HashBasedTable.create();

	private final Table<String, Integer, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();

	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("surface", "subterranean", "submerged", "float", "wells");
	
	/*
	 * use this map when structures are submerged instead of the default marker map
	 */
	private Map<StructureMarkers, Block> waterMarkerMap;
	
	/**
	 * 
	 * @param baseFolder
	 * @param fixer
	 */
	public TreasureTemplateManager(IMod mod, String resourceFolder, DataFixer fixer) {
		super(mod, resourceFolder, fixer);
		Treasure.logger.debug("creating a TreasureTemplateManager");

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

		// build and expose template/structure folders
		if (TreasureConfig.MOD.enableDefaultTemplatesCheck) {
			buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
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
	 * Loads and registers the template from the file system.
	 * 
	 * @param modID
	 */
	public void register(String modID) {
		for (String location : FOLDER_LOCATIONS) {
			Treasure.logger.debug("registering templates under location -> {}", location);
			// get template files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);

			// load each ResourceLocation and map it.
			for (ResourceLocation loc : locs) {
				// get the path to the resource
				Path path = Paths.get(loc.getResourcePath());
				if (Treasure.logger.isDebugEnabled()) {
					Treasure.logger.debug("path to template resource loc -> {}", path.toString());
				}

				// build the key for the meta manager to look at
				ResourceLocation metaResourceLocation = new ResourceLocation(
						getMod().getId() + ":" + Treasure.META_MANAGER.getBaseResourceFolder()+ "/" + modID + "/structures/" + path.getFileName().toString().replace(".nbt", ".json"));
				String key = metaResourceLocation.toString();
				Treasure.logger.debug("Using key to find meta -> {}", key);
				
				// look for IMeta in DecayManager by treasure2:structures/treasure2/surface/x.nbt
				StructureMeta meta = (StructureMeta) Treasure.META_MANAGER.getMetaMap().get(key);
				if (meta == null) {
					// there isn't a meta found for resource, skip to next template
					Treasure.logger.info("Unable to locate meta file for resource -> {}", key);
					continue;
				}				
				if (meta.getArchetypes() == null || meta.getArchetypes().isEmpty() || meta.getType() == null) {
					Treasure.logger.info("Meta file not properly configured. -> {}", key);
					continue;
				}
				
				// TODO interrogate the archetype to determine the marker scan list and/or replacement map to use
				
				// load template
				Treasure.logger.debug("attempted to load custom template  with key -> {}", loc.toString());
				Template template = load(loc, getMarkerScanList(), getReplacementMap()); // TODO the marker scan list and replace list should be determined before this call
				// add the id to the map
				if (template == null) {
					// TODO message
					continue;
				}
				Treasure.logger.debug("loaded custom template  with key -> {}", loc.toString());
					
				// TODO have the template. for now in Treasure, wrap in TreasureTemplate that has an offset or verticalOffset property in the template
				// set that value to either the meta value if any. then the the template .... maybe getTemplate should be getTemplateHolder
				
				// TODO future state: first determine if parent/child - if child, need to find the parent template holder in map - how?
				// probably needs to be mapped by meta, then it can be mapped otherwise				
				
				// determine if the meta decayRuleSetName is populated
				List<ResourceLocation> decayRuleSetResourceLocation = new ArrayList<>();
				if (meta.getDecayRuleSetName() != null && meta.getDecayRuleSetName().size() > 0) {
					// build the keys for the meta manager to look at
					for (String ruleSetName : meta.getDecayRuleSetName()) {
						ResourceLocation resourceLocation = new ResourceLocation(
								getMod().getId() + ":" + Treasure.DECAY_MANAGER.getBaseResourceFolder()+ "/" + modID + "/" + ruleSetName + ".json");
						decayRuleSetResourceLocation.add(resourceLocation);
						Treasure.logger.debug("Using key to find decay ruleset -> {}", decayRuleSetResourceLocation.toString());
					}
				}
				
				// 1/27/20 - moved outside the loop
				TemplateHolder holder = new TemplateHolder()
						.setMetaLocation(metaResourceLocation)
						.setLocation(loc)
						.setDecayRuleSetLocation(decayRuleSetResourceLocation)
						.setTemplate(template);			
				
				// map by resource location
				getTemplatesByResourceLocationMap().put(loc, holder);
				
				// map according to meta archetype, type
				for (IMetaArchetype archetype : meta.getArchetypes()) {								
					Treasure.logger.debug("Using meta to map archetype type -> {}", meta.toString());										
					if (!templatesByArchetypeType.contains(archetype, meta.getType())) {
						templatesByArchetypeType.put(archetype, meta.getType(), new ArrayList<>(3));
					}
					this.templatesByArchetypeType.get(archetype, meta.getType()).add(holder);
					
					Treasure.logger.debug("Registered holder -> location -> {}, meta -> {}, decay -> {}",
							holder.getLocation(), 
							holder.getMetaLocation(),
							holder.getDecayRuleSetLocation());

					// TODO could move the wrapping for into this method instead, then could lose the archetype that is passed in. ***!!!
					mapToTemplatesByArchetypeBiome(metaResourceLocation, loc, decayRuleSetResourceLocation, archetype, meta.getType(), template);
				}
			}
		}
		if (Treasure.logger.isDebugEnabled()) {
			dump();
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
		StructureMeta meta = (StructureMeta) Treasure.META_MANAGER.getMetaMap().get(metaResourceLocation.toString());
		
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
			Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValuesCollection();
			for (Biome biome : biomes) {
				if (!BiomeDictionary.hasType(biome, Type.END)
						&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
					Integer biomeID = Biome.getIdForBiome(biome);
					if (!templatesByArchetypeTypeBiome.contains(key, biomeID)) {
						templatesByArchetypeTypeBiome.put(key, biomeID, new ArrayList<>(3));
					}					
					templatesByArchetypeTypeBiome.get(key, biomeID).add(holder);
				}
			}
		} else {
			if (!meta.getBiomeWhiteList().isEmpty()) {
				for (String b : meta.getBiomeWhiteList()) {
					String biomeName = b.trim().toLowerCase();
					Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeName));
					if (biome == null) {
						Treasure.logger.debug("Unable to locate biome for name -> {}", biomeName);
						continue;
					}
					if (biome != null && !BiomeDictionary.hasType(biome, Type.END)
							&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (!templatesByArchetypeTypeBiome.contains(key, biomeID)) {
							templatesByArchetypeTypeBiome.put(key, biomeID, new ArrayList<>(3));
						}
						templatesByArchetypeTypeBiome.get(key, biomeID).add(holder);
					}
				}
			} else if (!meta.getBiomeBlackList().isEmpty()) {
				/*
				 * add all the black listed biome IDs to a list
				 */
				List<Integer> blackListBiomeIDs = new ArrayList<>();
				for (String b : meta.getBiomeBlackList()) {
					Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(b.trim().toLowerCase()));
					if (biome != null) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (biomeID != null) blackListBiomeIDs.add(biomeID);
					}
					else {
						Treasure.logger.debug("Unable to locate biome for name -> {}", b);
						continue;
					}
				}
				// get the set of all biomes
				Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValuesCollection();
				// for each biome is the list
				for (Biome biome : biomes) {
					if (!blackListBiomeIDs.contains(Biome.getIdForBiome(biome))
							&& !BiomeDictionary.hasType(biome, Type.END)
							&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (!templatesByArchetypeTypeBiome.contains(key, biomeID)) {
							templatesByArchetypeTypeBiome.put(key, biomeID, new ArrayList<>(3));
						}
						templatesByArchetypeTypeBiome.get(key, biomeID).add(holder);
					}
				}
			}
		}		
	}

	/**
	 * @param world
	 * @param random
	 * @param key
	 * @param biome
	 * @return
	 */
	public TemplateHolder getTemplate(World world, Random random, StructureArchetype archetype, StructureType type, Biome biome) {
		// get structure by archetype (subterranean) and type (room)
		String key =archetype.getName()	+ ":" + type.getName();
		
		Integer biomeID = Biome.getIdForBiome(biome);
		
		List<TemplateHolder> templateHolders = getTemplatesByArchetypeTypeBiomeTable().get(key, biomeID);
		if (templateHolders == null || templateHolders.isEmpty()) {
			Treasure.logger.debug("could not find template holders for archetype:type, biome -> {} {}", key, biomeID);
			return null;
		}
		
		TemplateHolder holder = templateHolders.get(random.nextInt(templateHolders.size()));
		if (holder == null) {
			Treasure.logger.debug("could not find random template holder.");
			return null;
		}

		Treasure.logger.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());

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
		String s = id.getResourcePath();

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
				NBTTagCompound nbttagcompound = template.writeToNBT(new NBTTagCompound());
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
			Treasure.logger.error("Couldn't create directories for dump files:", e);
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
		Map<String, Map<Integer, List<TemplateHolder>>> map = getTemplatesByArchetypeTypeBiomeTable().rowMap();
		if (map == null || map.isEmpty()) { Treasure.logger.debug("template biome map is null/empty");}
		Treasure.logger.debug("biome map.size -> {}", map.size());
		
		for (String row : map.keySet()) {
			Treasure.logger.debug("template biome row key -> {}", row);
			Map<Integer, List<TemplateHolder>> tmp = map.get(row);
			for (Entry<Integer, List<TemplateHolder>> entry : tmp.entrySet()) {
				String templateNames = entry.getValue().stream().map(a -> a.getLocation().toString()).collect(Collectors.joining(", "));
				Biome biome = Biome.getBiome(entry.getKey());
				String biomeName = "";
				if (biome != null) {
					biomeName = WorldInfo.isClientSide()  ? biome.getBiomeName() : String.valueOf(Biome.getIdForBiome(biome)) ;
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
			Treasure.logger.error("Error writing TreasureTemplateManager to dump file", e);
		}
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
	public Table<String, Integer, List<TemplateHolder>> getTemplatesByArchetypeTypeBiomeTable() {
		return templatesByArchetypeTypeBiome;
	}
}