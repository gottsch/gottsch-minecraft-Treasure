/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;

import com.someguyssoftware.gottschcore.config.AbstractConfig;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.material.MapColor;
import net.minecraftforge.common.config.Configuration;

/**
 * 
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureConfig extends AbstractConfig {

	/*
	 *  IDs
	 */
	// tab
	public static final String TREASURE_TAB_ID = "treasure_tab";
	// chests
	public static final String WOOD_CHEST_ID = "wood_chest";
	public static final String CRATE_CHEST_ID = "crate_chest";
	public static final String MOLDY_CRATE_CHEST_ID = "crate_chest_moldy";
	public static final String IRONBOUND_CHEST_ID = "ironbound_chest";
	
	// locks
	public static final String WOOD_LOCK_ID = "wood_lock";
	public static final String STONE_LOCK_ID = "stone_lock";
	public static final String IRON_LOCK_ID = "iron_lock";
	public static final String GOLD_LOCK_ID = "gold_lock";
	public static final String DIAMOND_LOCK_ID = "diamond_lock";
	public static final String EMERALD_LOCK_ID = "emerald_lock";
	
	// keys
	public static final String WOOD_KEY_ID = "wood_key";
	public static final String IRON_KEY_ID = "iron_key";
	public static final String GOLD_KEY_ID = "gold_key";
	public static final String DIAMOND_KEY_ID = "diamond_key";
	public static final String STONE_KEY_ID = "stone_key";
	public static final String EMERALD_KEY_ID = "emerald_key";
	public static final String METALLURGISTS_KEY_ID = "metallurgists_key";
	public static final String SKELETON_KEY_ID = "skeleton_key";
	public static final String WITHER_KEY_ID = "wither_key";
	
	public static final String PILFERERS_LOCK_PICK_ID = "pilferers_lock_pick";
	public static final String THIEFS_LOCK_PICK_ID = "thiefs_lock_pick";
	
	public static final String GOLD_COIN_ID = "gold_coin";
	public static final String SILVER_COIN_ID = "silver_coin";
	public static final String PIRATE_CHEST_ID = "pirate_chest";
	
	// GRAVESTONES
	public static final String GRAVESTONE1_STONE_ID = "gravestone1_stone";
	public static final String GRAVESTONE1_COBBLESTONE_ID = "gravestone1_cobblestone";
	public static final String GRAVESTONE1_MOSSY_COBBLESTONE_ID = "gravestone1_mossy_cobblestone";
	public static final String GRAVESTONE1_POLISHED_GRANITE_ID = "gravestone1_polished_granite";
	public static final String GRAVESTONE1_POLISHED_ANDESITE_ID = "gravestone1_polished_andesite";
	public static final String GRAVESTONE1_POLISHED_DIORITE_ID = "gravestone1_polished_diorite";
	public static final String GRAVESTONE1_OBSIDIAN_ID = "gravestone1_obsidian";
	
	public static final String GRAVESTONE2_STONE_ID = "gravestone2_stone";
	public static final String GRAVESTONE2_COBBLESTONE_ID = "gravestone2_cobblestone";
	public static final String GRAVESTONE2_MOSSY_COBBLESTONE_ID = "gravestone2_mossy_cobblestone";
	public static final String GRAVESTONE2_POLISHED_GRANITE_ID = "gravestone2_polished_granite";
	public static final String GRAVESTONE2_POLISHED_ANDESITE_ID = "gravestone2_polished_andesite";
	public static final String GRAVESTONE2_POLISHED_DIORITE_ID = "gravestone2_polished_diorite";
	public static final String GRAVESTONE2_OBSIDIAN_ID = "gravestone2_obsidian";
	
	// TEs
	public static final String WOOD_CHEST_TE_ID = "wood_chest_tile_entity";
	public static final String CRATE_CHEST_TE_ID = "crate_chest_tile_entity";
	public static final String MOLDY_CRATE_CHEST_TE_ID = "crate_chest_moldy_tile_entity";
	public static final String IRONBOUND_CHEST_TE_ID = "ironbound_chest_tile_entity";
	public static final String PIRATE_CHEST_TE_ID = "pirate_chest_tile_entity";


		

	public static boolean enableKeyBreaks = true;	
	public static String treasureFolder;

	/*
	 *  world gen
	 */
	public static int minDistancePerChest;
	public static int minChunksPerChest;
	
	// graves/markers properties
	public static boolean isGravestonesAllowed;
	public static int minGravestonesPerChest;
	public static int maxGravestonesPerChest;
	
	// TODO add wells properties
	
	// TODO add wandering antiquities peddler properties
	
	// TODO add treasure/unique items properties
	
	// biome type white/black lists
	public static String[] generalChestBiomeWhiteList;
	public static String[] generalChestBiomeBlackList;

	
	/**
	 * @param mod
	 * @param configDir
	 * @param modDir
	 * @param filename
	 */
	public TreasureConfig(IMod mod, File configDir, String modDir, String filename) {
		super(mod, configDir, modDir, filename);
	}

	/**
	 * 
	 */
	@Override
	public Configuration load(File file) {
		// load the config file
		Configuration config = super.load(file);
		
		// add mod specific settings here
        treasureFolder = config.getString("treasureFolder", "03-mod", "mods/" + Treasure.MODID + "/", "Where default Treasure folder is located.");
        enableKeyBreaks = config.getBoolean("enableKeyBreaks", "03-mod", true, "Enables/Disable whether a Key can break when attempting to unlock a Lock.");

        // white/black lists
        config.setCategoryComment("04-gen", "World generation properties.");    
        generalChestBiomeWhiteList = config.getStringList("generalChestBiomeWhiteList", "04-gen", new String[]{}, "Allowable Biome Types for general Chest generation. Must match the Type identifer(s).");
        generalChestBiomeBlackList = config.getStringList("generalChestBiomeBlackList", "04-gen", new String[]{"ocean"}, "Disallowable Biome Types for general Chest generation. Must match the Type identifer(s).");
        
      	minDistancePerChest = config.getInt("minDistancePerChest", "04-gen", 75, 0, 32000, "");
      	minChunksPerChest = config.getInt("minChunksPerChest", "04-gen", 35, 0, 32000, "");
      	
        isGravestonesAllowed = config.getBoolean("isGravestonesAllowed", "04-gen", true, "");
        minGravestonesPerChest = config.getInt("minGravestonesPerChest", "04-gen", 4, 1, 5, "The minimun of Treasure chest markers (gravestones, bones).");
        maxGravestonesPerChest = config.getInt("maxGravesstonesPerChest", "04-gen", 8, 1, 10, "The maximum of Treasure chest markers (gravestones, bones).");
         
        // the the default values
       if(config.hasChanged()) {
    	   config.save();
       }
       
		return config;
	}
}
