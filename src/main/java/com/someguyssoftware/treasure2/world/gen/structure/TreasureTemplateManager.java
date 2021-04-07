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
import com.mojang.datafixers.DataFixer;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplateManager;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Custom template manager that create TreasureTemplates instead of Template.
 * 
 * @author Mark Gottschling on Jan 19, 2019
 *
 */
public class TreasureTemplateManager extends GottschTemplateManager {

	private static final Map<ResourceLocation, TemplateHolder> templatesByResourceLocation = new HashMap<>();
	
	private static final Table<IMetaArchetype, IMetaType, List<TemplateHolder>> templatesByArchetypeType = HashBasedTable.create();

	private static final Table<String, ResourceLocation, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();

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

		// build and expose template/structure folders
//		if (TreasureConfig.GENERAL.enableDefaultTemplatesCheck.get()) {
//			buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
//		}
	}

	/**
	 * 
	 */
	public void init(ServerWorld world) {
		// initialize table
		for (IMetaArchetype archetype : StructureArchetype.values()) {
			for (IMetaType type : com.someguyssoftware.treasure2.meta.StructureType.values()) {
				templatesByArchetypeType.put(archetype, type, new ArrayList<>(5));
			}
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
	public void register(String modID, List<String> locations) {
		for (String location : locations) {
			Treasure.LOGGER.debug("registering template -> {}", location);
			// get template files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);

			// load each ResourceLocation and map it.
			for (ResourceLocation loc : locs) {
				// get the path to the resource
				Path path = Paths.get(loc.getPath());
				if (Treasure.LOGGER.isDebugEnabled()) {
					Treasure.LOGGER.debug("path to template resource loc -> {}", path.toString());
				}

				// build the key for the meta manager to look at
				ResourceLocation metaResourceLocation = new ResourceLocation(
						modID + ":" + TreasureMetaRegistry.getMetaManager().getBaseResourceFolder() + "/structures/" + path.getFileName().toString().replace(".nbt", ".json"));
				String key = metaResourceLocation.toString();
				Treasure.LOGGER.debug("Using key to find meta -> {}", key);
				
				// look for IMeta in DecayManager by treasure2:structures/treasure2/surface/x.nbt
				StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(key);
				if (meta == null) {
					// there isn't a meta found for resource, skip to next template
					Treasure.LOGGER.info("Unable to locate meta file for resource -> {}", key);
					continue;
				}				
				if (meta.getArchetypes() == null || meta.getArchetypes().isEmpty() || meta.getType() == null) {
					Treasure.LOGGER.info("Meta file not properly configured. -> {}", key);
					continue;
				}
				
				// TODO interrogate the archetype to determine the marker scan list and/or replacement map to use
				
				// load template
				Template template = load(loc, getMarkerScanList(), getReplacementMap()); // TODO the marker scan list and replace list should be determined before this call
				// add the id to the map
				if (template == null) {
					Treasure.LOGGER.debug("unable to load custom template  with key -> {}", loc.toString());
					continue;
				}
				Treasure.LOGGER.debug("loaded custom template  with key -> {}", loc.toString());
					
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
								getMod().getId() + ":" + TreasureDecayRegistry.getDecayManager().getBaseResourceFolder()+ "/" + ruleSetName + ".json");
						decayRuleSetResourceLocation.add(resourceLocation);
						Treasure.LOGGER.debug("Using key to find decay ruleset -> {}", decayRuleSetResourceLocation.toString());
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
					mapToTemplatesByArchetypeBiome(metaResourceLocation, loc, decayRuleSetResourceLocation, archetype, meta.getType(), template);
				}
			}
		}
		if (Treasure.LOGGER.isDebugEnabled()) {
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
//				if (!BiomeDictionary.hasType(biome, Type.END)
//						&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
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
//					if (biome != null && !BiomeDictionary.hasType(biome, Type.END)
//							&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
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
//							&& !BiomeDictionary.hasType(biome, Type.END)
//							&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
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
	 * @param world
	 * @param random
	 * @param key
	 * @param biome
	 * @return
	 */
	public TemplateHolder getTemplate(IWorld world, Random random, StructureArchetype archetype, StructureType type, Biome biome) {
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
//				Biome biome = Biome.getBiome(entry.getKey());
				Biome biome = ForgeRegistries.BIOMES.getValue(entry.getKey());
				String biomeName = "";
				if (biome != null) {
//					biomeName = WorldInfo.isClientDistribution() ? biome.getDisplayName().getString() : biome.getRegistryName().toString() ;
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
	public static Map<ResourceLocation, TemplateHolder> getTemplatesByResourceLocationMap() {
		return templatesByResourceLocation;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Table<String, ResourceLocation, List<TemplateHolder>> getTemplatesByArchetypeTypeBiomeTable() {
		return templatesByArchetypeTypeBiome;
	}
}