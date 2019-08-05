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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplateManager;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
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

	/*
	 * NOTE this enum is key for external templates. all templates are categorized
	 * and placed into folders according to the type.
	 */
	public enum StructureType {
		ABOVEGROUND, UNDERGROUND
	};

	private final Map<StructureType, List<Template>> templatesByType = Maps.<StructureType, List<Template>>newHashMap();
	/** NOTE not in use yet. can organize tempaltes by type and rarity */
	private final Table<StructureType, Rarity, List<Template>> templateTable = HashBasedTable.create();

	private final Table<IMetaArchetype, IMetaType, List<TemplateHolder>> templatesByArchetypeType = HashBasedTable.create();

	private final Table<String, Integer, List<TemplateHolder>> templatesByArchetypeTypeBiome = HashBasedTable.create();

	/*
	 * builtin underground locations for structures
	 */
	private static List<String> UNDERGROUND_LOCATIONS = Arrays.asList(new String[] { "treasure2:underground/basic1",
			"treasure2:underground/basic2", "treasure2:underground/basic3", "treasure2:underground/basic4",
			"treasure2:underground/basic5", "treasure2:underground/cave1", "treasure2:underground/cave2",
			"treasure2:underground/cobb1", "treasure2:underground/crypt1" });

	/*
	 * builtin aboveground locations for structures
	 */
	private static List<String> ABOVEGROUND_LOCATIONS = Arrays
			.asList(new String[] { "treasure2:aboveground/crypt2", "treasure2:aboveground/crypt3" });

	private static List<String> FOLDER_LOCATIONS = ImmutableList.of("surface", "subterranean", "submerged", "float");

	/**
	 * 
	 * @param baseFolder
	 * @param fixer
	 */
	public TreasureTemplateManager(IMod mod, String resourceFolder, DataFixer fixer) {
		super(mod, resourceFolder, fixer);
		Treasure.logger.debug("creating a TreasureTemplateManager");

		// init maps
		for (StructureType structureType : StructureType.values()) {
			getTemplatesByType().put(structureType, new ArrayList<Template>(5));
		}

		// initialize table
		for (IMetaArchetype archetype : StructureArchetype.values()) {
			for (IMetaType type : com.someguyssoftware.treasure2.meta.StructureType.values()) {
				templatesByArchetypeType.put(archetype, type, new ArrayList<>(5));
			}
		}

		// TODO replace with loading from external
		// load all the builtin (jar) structure templates
		loadAll(UNDERGROUND_LOCATIONS, StructureType.UNDERGROUND);
		loadAll(ABOVEGROUND_LOCATIONS, StructureType.ABOVEGROUND);
		if (Treasure.logger.isDebugEnabled()) {
			dump();
		}
		// load external structures
		// for (StructureType customLocation : StructureType.values()) {
		// createTemplateFolder(customLocation.name().toLowerCase());
		// loadAll(Arrays.asList(customLocation.name().toLowerCase()), customLocation);
		// }

		// build and expose template/structure folders
		buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
	}

	/**
	 * 
	 */
	public void clear() {
		templateTable.clear();
		templatesByArchetypeTypeBiome.clear();
		templatesByArchetypeType.clear();
		templatesByType.clear();
	}
	
	/**
	 * Loads and registers the template from the file system.
	 * 
	 * @param modID
	 */
	public void register(String modID) {
		for (String location : FOLDER_LOCATIONS) {
			Treasure.logger.debug("registering templates under location -> {}", location);
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);

			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getResourcePath());
				if (Treasure.logger.isDebugEnabled()) {
					Treasure.logger.debug("path to template resource loc -> {}", path.toString());
				}

				// load template
				Treasure.logger.debug("attempted to load custom template  with key -> {}", loc.toString());
				Template template = load(loc, getScanList());
				// add the id to the map
				if (template == null) {
					// TODO message
					continue;
				}
				Treasure.logger.debug("loaded custom template  with key -> {}", loc.toString());

				// TODO not thought out enough. Need a holder object for Template that contains
				// the key to the meta

				// TODO this is wrong; need to point to treasure2:meta/treasure2/structures/<file>.json
				// build the key for the meta manager to look at
				ResourceLocation metaResourceLocation = new ResourceLocation(
						getMod().getId() + ":" + Treasure.META_MANAGER.getBaseResourceFolder()+ "/" + modID + "/structures/" + path.getFileName().toString().replace(".nbt", ".json"));
				String key = metaResourceLocation.toString();
				Treasure.logger.debug("Using key to find meta -> {}", key);
				
				// look for IMeta in MetaManager by file name ie x.nbt or
				// treasure2:structures/treasure2/surface/x.nbt
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
				
				// TODO future state: first determine if parent/child - if child, need to find the parent template holder in map - how?
				// probably needs to be mapped by meta, then it can be mapped otherwise
				
				
				// map according to meta archetype, type
				for (IMetaArchetype archetype : meta.getArchetypes()) {
					TemplateHolder holder = new TemplateHolder()
							.setMetaLocation(metaResourceLocation)
							.setLocation(loc)
							.setTemplate(template);
					Treasure.logger.debug("Using meta to map archetype type -> {}", meta.toString());
					
					
					if (!templatesByArchetypeType.contains(archetype, meta.getType())) {
						templatesByArchetypeType.put(archetype, meta.getType(), new ArrayList<>(3));
					}
					this.templatesByArchetypeType.get(archetype, meta.getType()).add(holder);

					// TODO could move the wrapping for into this method instead, then could lose the archetype that is passed in. ***!!!
					mapToTemplatesByArchetypeBiome(metaResourceLocation, loc, archetype, meta.getType(), template);
				}
			}
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
			ResourceLocation location, IMetaArchetype archetype, IMetaType type, Template template) {

		// build mapping key
		String key = archetype.getName() + ":" + type.getName();
		
		// find the meta for the template
		StructureMeta meta = (StructureMeta) Treasure.META_MANAGER.getMetaMap().get(metaResourceLocation.toString());
		// create a holder for the template
		TemplateHolder holder = new TemplateHolder()
				.setMetaLocation(metaResourceLocation)
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
					if (!BiomeDictionary.hasType(biome, Type.END)
							&& !BiomeDictionary.hasType(biome, Type.NETHER)) {
						Integer biomeID = Biome.getIdForBiome(biome);
						if (!templatesByArchetypeTypeBiome.contains(key, biomeID)) {
							templatesByArchetypeTypeBiome.put(key, biomeID, new ArrayList<>(3));
						}
						templatesByArchetypeTypeBiome.get(key, biomeID).add(holder);
					}
				}
			} else if (!meta.getBiomeBlackList().isEmpty()) {
				Set<Biome> biomes = (Set<Biome>) ForgeRegistries.BIOMES.getValuesCollection();
				for (Biome biome : biomes) {
					if (!meta.getBiomeBlackList().contains(biome.getBiomeName().toLowerCase())
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
	 * Convenience method.
	 * 
	 * @param type
	 * @return
	 */
	public List<Template> getTemplatesByType(StructureType type) {
		List<Template> templates = getTemplatesByType().get(type);
		return templates;
	}

	@Override
	public GottschTemplateManager loadAll(List<String> locations) {
		// prevent this function from executing as the implementation version should be
		// used.
		return this;
	}

	/**
	 * TODO goes away with IMeta files
	 * 
	 * @param locations
	 * @param type
	 */
	public void loadAll(List<String> locations, StructureType type) {
		Treasure.logger.debug("loading all typed structures -> {}", type.name());
		for (String location : locations) {
			Treasure.logger.debug("loading from -> {}", location);
			Template template = load(new ResourceLocation(location), getScanList());
			Treasure.logger.debug("loaded template  with key -> {} : {}", location, location);
			// add the id to the map
			if (template != null) {
				Treasure.logger.debug("adding tempate to typed map -> {} : {}", type.name(), location);
				getTemplatesByType().get(type).add(template);
			}
		}
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

		Path path = Paths.get(getMod().getConfig().getModsFolder(), getMod().getId(), "dumps").toAbsolutePath();
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
		String heading = "**  %1$-67s  **\n";
		sb.append(div).append(String.format("**  %-67s  **\n", "TEMPLATE MANAGER")).append(div)
				.append(String.format(heading, "[Template By Type Map]"));
		for (Map.Entry<String, Template> entry : getTemplates().entrySet()) {
			sb.append(String.format(format, entry.getKey(), entry.getValue().getAuthor()));
		}
		sb.append(div);
		sb.append(String.format(heading, "[Template by Archetype:Type | Biome]"));
		
		try {
			Files.write(Paths.get(path.toString(), filename), sb.toString().getBytes());
		} catch (IOException e) {
			Treasure.logger.error("Error writing TreasureTemplateManager to dump file", e);
		}
	}

	/**
	 * @return the templateTable
	 */
	public Table<StructureType, Rarity, List<Template>> getTemplateTable() {
		return templateTable;
	}

	/**
	 * @return the templatesByType
	 */
	public Map<StructureType, List<Template>> getTemplatesByType() {
		return templatesByType;
	}
}