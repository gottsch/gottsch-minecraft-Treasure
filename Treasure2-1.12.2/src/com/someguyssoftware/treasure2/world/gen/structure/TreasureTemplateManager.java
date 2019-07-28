package com.someguyssoftware.treasure2.world.gen.structure;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplateManager;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.gen.structure.template.Template;


/**
 * Custom template manager that create TreasureTemplates instead of Template.
 * @author Mark Gottschling on Jan 19, 2019
 *
 */
// TODO redo this. doesn't work well for specials like witch's den
public class TreasureTemplateManager extends GottschTemplateManager {
	
	/*
	 * NOTE this enum is key for external templates. all templates are
	 * categorized and placed into folders according to the type.
	 */
	public enum StructureType {
		ABOVEGROUND,
		UNDERGROUND
	};
	
	private final Map<StructureType, List<Template>> templatesByType = Maps.<StructureType, List<Template>>newHashMap();
	/** NOTE not in use yet. can organize tempaltes by type and rarity */
	private final Table<StructureType, Rarity, List<Template>> templateTable = HashBasedTable.create();
	
	/*
	 * builtin underground locations for structures
	 */
	private static List<String> UNDERGROUND_LOCATIONS = Arrays.asList(new String [] {
			"treasure2:underground/basic1",
			"treasure2:underground/basic2",
			"treasure2:underground/basic3",
			"treasure2:underground/basic4",
			"treasure2:underground/basic5",
			"treasure2:underground/cave1",
			"treasure2:underground/cave2",
			"treasure2:underground/cobb1",
			"treasure2:underground/crypt1"
	});

	/*
	 * builtin aboveground locations for structures
	 */
	private static List<String> ABOVEGROUND_LOCATIONS = Arrays.asList(new String[] {
			"treasure2:aboveground/crypt2",
			"treasure2:aboveground/crypt3"
	});

	private static List<String> FOLDER_LOCATIONS = ImmutableList.of(
			"surface",
			"subterranean",
			"submerged",
			"float"
			);
	
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
        
       // TODO replace with loading from external
        // load all the builtin (jar) structure templates
        loadAll(UNDERGROUND_LOCATIONS, StructureType.UNDERGROUND);
        loadAll(ABOVEGROUND_LOCATIONS, StructureType.ABOVEGROUND);
        
        // load external structures
//        for (StructureType customLocation : StructureType.values()) {
//        	createTemplateFolder(customLocation.name().toLowerCase());
//        	loadAll(Arrays.asList(customLocation.name().toLowerCase()), customLocation);
//        }
        
        // build and expose template/structure folders
        buildAndExpose(getBaseResourceFolder(), Treasure.MODID, FOLDER_LOCATIONS);
    }

	/**
	 * Loads and registers the template from the file system.
	 * @param modID
	 */
	public void register(String modID) {
		for (String location : FOLDER_LOCATIONS) {
			Treasure.logger.debug("registering template -> {}", location);
			// get loot table files as ResourceLocations from the file system location
			List<ResourceLocation> locs = getResourceLocations(modID, location);
			
			// load each ResourceLocation as LootTable and map it.
			for (ResourceLocation loc : locs) {
				Path path = Paths.get(loc.getResourcePath());
				if (Treasure.logger.isDebugEnabled()) {
					Treasure.logger.debug("path to template resource loc -> {}", path.toString());
				}
				
				// load template
				Treasure.logger.debug("attempted to load custom template  with key -> {} : {}", location, location);
				Template template = load(loc, getScanList());
				// add the id to the map
				if (template == null) {
					// TODO  message
					continue;
				}
				Treasure.logger.debug("loaded custom template  with key -> {} : {}", location, location);
				
				// TODO look for IMeta in MetaManager by file name ie x.nbt or treasure2:structures/treasure2/surface/x.nbt
				// TODO map according to meta archetype, type
				
			}	
		}
	}

	/**
	 * Convenience method.
	 * @param type
	 * @return
	 */
	public List<Template> getTemplatesByType(StructureType type) {
		List<Template> templates = getTemplatesByType().get(type);
		return templates;
	}

	@Override
	public GottschTemplateManager loadAll(List<String> locations) {
		// prevent this function from executing as the implementation version should be used.
		return this;
	}
	
	/**
	 * TODO goes away with IMeta files
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