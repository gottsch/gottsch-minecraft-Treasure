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
	public static final String PIRATE_CHEST_ID = "pirate_chest";
	public static final String IRON_STRONGBOX_ID = "iron_strongbox";
	public static final String GOLD_STRONGBOX_ID = "gold_strongbox";
	public static final String SAFE_ID = "safe";
	public static final String DREAD_PIRATE_CHEST_ID = "dread_pirate_chest";
	public static final String COMPRESSOR_CHEST_ID = "compressor_chest";
	public static final String WITHER_CHEST_ID = "wither_chest";
	public static final String WITHER_CHEST_TOP_ID = "wither_chest_top";
	public static final String SKULL_CHEST_ID = "skull_chest";
	public static final String GOLD_SKULL_CHEST_ID = "gold_skull_chest";
	
	// mimics
	public static final String WOOD_MIMIC_ID = "wood_mimic";
	
	// locks
	public static final String WOOD_LOCK_ID = "wood_lock";
	public static final String STONE_LOCK_ID = "stone_lock";
	public static final String IRON_LOCK_ID = "iron_lock";
	public static final String GOLD_LOCK_ID = "gold_lock";
	public static final String DIAMOND_LOCK_ID = "diamond_lock";
	public static final String EMERALD_LOCK_ID = "emerald_lock";
	public static final String RUBY_LOCK_ID = "ruby_lock";
	public static final String SAPPHIRE_LOCK_ID = "sapphire_lock";
	public static final String SPIDER_LOCK_ID = "spider_lock";
	public static final String WITHER_LOCK_ID = "wither_lock";
	
	// keys
	public static final String WOOD_KEY_ID = "wood_key";
	public static final String IRON_KEY_ID = "iron_key";
	public static final String GOLD_KEY_ID = "gold_key";
	public static final String DIAMOND_KEY_ID = "diamond_key";
	
	public static final String STONE_KEY_ID = "stone_key";
	public static final String EMERALD_KEY_ID = "emerald_key";
	public static final String RUBY_KEY_ID = "ruby_key";
	public static final String SAPPHIRE_KEY_ID = "sapphire_key";
	public static final String JEWELLED_KEY_ID = "jewelled_key";
	public static final String METALLURGISTS_KEY_ID = "metallurgists_key";
	public static final String SKELETON_KEY_ID = "skeleton_key";
	public static final String WITHER_KEY_ID = "wither_key";
	public static final String SPIDER_KEY_ID = "spider_key";
	
	public static final String PILFERERS_LOCK_PICK_ID = "pilferers_lock_pick";
	public static final String THIEFS_LOCK_PICK_ID = "thiefs_lock_pick";
	
	public static final String KEY_RING_ID = "key_ring";
	
	public static final String GOLD_COIN_ID = "gold_coin";
	public static final String SILVER_COIN_ID = "silver_coin";
	
	// weapons / armor
	public static final String SKULL_SWORD_ID = "skull_sword";
	public static final String EYE_PATCH_ID = "eye_patch";
	
	// wither items
	public static final String WITHER_ROOT_ITEM_ID = "wither_root_item";
	public static final String WITHER_STICK_ITEM_ID = "wither_stick_item";
	
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
	
	public static final String GRAVESTONE3_STONE_ID = "gravestone3_stone";
	public static final String GRAVESTONE3_COBBLESTONE_ID = "gravestone3_cobblestone";
	public static final String GRAVESTONE3_MOSSY_COBBLESTONE_ID = "gravestone3_mossy_cobblestone";
	public static final String GRAVESTONE3_POLISHED_GRANITE_ID = "gravestone3_polished_granite";
	public static final String GRAVESTONE3_POLISHED_ANDESITE_ID = "gravestone3_polished_andesite";
	public static final String GRAVESTONE3_POLISHED_DIORITE_ID = "gravestone3_polished_diorite";
	public static final String GRAVESTONE3_OBSIDIAN_ID = "gravestone3_obsidian";
	
	public static final String SKULL_CROSSBONES_ID = "skull_and_crossbones";		
	public static final String WISHING_WELL_BLOCK_ID = "wishing_well_block";
	public static final String FOG_BLOCK_ID = "fog";
	public static final String HIGH_FOG_BLOCK_ID = "high_fog";
	public static final String MED_FOG_BLOCK_ID = "med_fog";
	public static final String LOW_FOG_BLOCK_ID = "low_fog";
	public static final String WITHER_FOG_ID = "wither_fog";
	public static final String HIGH_WITHER_FOG_ID = "high_wither_fog";
	public static final String MED_WITHER_FOG_ID = "med_wither_fog";
	public static final String LOW_WITHER_FOG_ID = "low_wither_fog";
	public static final String POISON_FOG_ID = "poison_fog";
	public static final String HIGH_POISON_FOG_ID = "high_poison_fog";
	public static final String MED_POISON_FOG_ID = "med_poison_fog";
	public static final String LOW_POISON_FOG_ID = "low_poison_fog";
	
	public static final String WITHER_LOG_ID = "wither_log";
	public static final String WITHER_BRANCH_ID = "wither_branch";
	public static final String WITHER_ROOT_ID = "wither_root";
	public static final String WITHER_BROKEN_LOG_ID = "wither_broken_log";
	public static final String WITHER_LOG_SOUL_ID = "wither_log_soul";
	
	// paintings
	public static final String PAINTING_BLOCKS_BRICKS_ID = "painting_blocks_bricks";
	public static final String PAINTING_BLOCKS_COBBLESTONE_ID = "painting_blocks_cobblestone";
	public static final String PAINTING_BLOCKS_DIRT_ID = "painting_blocks_dirt";
	public static final String PAINTING_BLOCKS_LAVA_ID = "painting_blocks_lava";
	public static final String PAINTING_BLOCKS_SAND_ID = "painting_blocks_sand";
	public static final String PAINTING_BLOCKS_WATER_ID = "painting_blocks_water";
	public static final String PAINTING_BLOCKS_WOOD_ID = "painting_blocks_wood";
	
	public static final String SPANISH_MOSS_BLOCK_ID = "spanish_moss";
	public static final String SPANISH_MOSS_ITEM_ID = "spanish_moss";
	
	public static final String WITHER_PLANKS_ID = "wither_planks";
	
	public static final String SAPPHIRE_ORE_ID = "sapphire";
	public static final String SAPPHIRE_ID = "sapphire";

	public static final String RUBY_ORE_ID = "ruby_ore";
	public static final String RUBY_ID = "ruby";
	
	public static final String TREASURE_TOOL_ITEM_ID = "treasure_tool";
	
	// TEs
	public static final String WOOD_CHEST_TE_ID = "wood_chest_tile_entity";
	public static final String CRATE_CHEST_TE_ID = "crate_chest_tile_entity";
	public static final String MOLDY_CRATE_CHEST_TE_ID = "crate_chest_moldy_tile_entity";
	public static final String IRONBOUND_CHEST_TE_ID = "ironbound_chest_tile_entity";
	public static final String PIRATE_CHEST_TE_ID = "pirate_chest_tile_entity";
	public static final String IRON_STRONGBOX_TE_ID = "iron_strongbox_tile_entity";
	public static final String GOLD_STRONGBOX_TE_ID = "gold_strongbox_tile_entity";
	public static final String SAFE_TE_ID = "safe_tile_entity";
	public static final String DREAD_PIRATE_CHEST_TE_ID = "dread_pirate_chest_tile_entity";
	public static final String COMPRESSOR_CHEST_TE_ID = "compressor_chest_tile_entity";
	public static final String WITHER_CHEST_TE_ID = "wither_chest_tile_entity";
	public static final String SKULL_CHEST_TE_ID = "skull_chest_tile_entity";
	public static final String GOLD_SKULL_CHEST_TE_ID = "gold_skull_chest_tile_entity";
	
	/*
	 * mod settings
	 */
	public static final String MODS_FOLDER = "mods";	
	
	public static String treasureFolder;
	public static boolean enableKeyBreaks = true;
	public static boolean enableFog = true;
	public static boolean enableWitherFog = true;
	public static boolean enablePoisonFog = true;
	public static boolean enableLockDrops = true;
	
	/*
	 * other mod enablements for loot tables
	 */
//	public static boolean enableMoCreatures = true;
	
	/*
	 *  world gen
	 */
	public static int minDistancePerChest;
	public static int minChunksPerChest;
	public static int minChunksPerWell;
	
	// graves/markers properties
	public static boolean isGravestonesAllowed;
	public static int minGravestonesPerChest;
	public static int maxGravestonesPerChest;
	public static int gravestoneFogProbability;
	
	// wither properties
	public static double witherBranchItemGenProbability;
	public static double witherRootItemGenProbability;
	
	// TODO add wells properties
	
	// TODO add wandering antiquities peddler properties
	
	// TODO add treasure/unique items properties
	
	// biome type white/black lists
	public static String[] generalChestBiomeWhiteList;
	public static String[] generalChestBiomeBlackList;

	// foreign mod enablements
	public static String[] enableForeignModIDs;
	public static String[] availableForeignModLootTables;
			
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
		// TODO change this to be just the base folder. make private. use getter(). append MODID
        treasureFolder = config.getString("treasureFolder", "03-mod", "mods/" + Treasure.MODID + "/", "Where default Treasure folder is located.");
        enableKeyBreaks = config.getBoolean("enableKeyBreaks", "03-mod", true, "Enables/Disable whether a Key can break when attempting to unlock a Lock.");
        enableFog = config.getBoolean("enableFog", "03-mod", true, "Enables/Disable whether a fog is generated (ex. around graves/tombstones and wither trees)");
        enableWitherFog = config.getBoolean("enableWitherFog", "03-mod", true, "Enables/Disable whether a wither fog is generated (ex. around wither trees)");
        enablePoisonFog = config.getBoolean("enablePoisonFog", "03-mod", true, "Enables/Disable whether a poison fog is generated (ex. around wither trees)");
        enableLockDrops = config.getBoolean("enableLockDrops", "03-mod", true, "Enables/Disable whether a Lock item is dropped when unlocked by Key item.");
//        enableMoCreatures = config.getBoolean("enableMoCreatures", "03-mod", false, "Enables/Disable whether MoCreatures mod is installed and it's items can be used in loot tables.");

        
        // white/black lists
        config.setCategoryComment("04-gen", "World generation properties.");    
        generalChestBiomeWhiteList = config.getStringList("generalChestBiomeWhiteList", "04-gen", new String[]{}, "Allowable Biome Types for general Chest generation. Must match the Type identifer(s).");
        generalChestBiomeBlackList = config.getStringList("generalChestBiomeBlackList", "04-gen", new String[]{"ocean"}, "Disallowable Biome Types for general Chest generation. Must match the Type identifer(s).");
        
      	minDistancePerChest = config.getInt("minDistancePerChest", "04-gen", 75, 0, 32000, "");
      	minChunksPerChest = config.getInt("minChunksPerChest", "04-gen", 35, 0, 32000, "");
      	minChunksPerWell = config.getInt("minChunksPerWell", "04-gen", 400, 100, 32000, "");
      	
        isGravestonesAllowed = config.getBoolean("isGravestonesAllowed", "04-gen", true, "");
        minGravestonesPerChest = config.getInt("minGravestonesPerChest", "04-gen", 4, 1, 5, "The minimun of Treasure chest markers (gravestones, bones).");
        maxGravestonesPerChest = config.getInt("maxGravesstonesPerChest", "04-gen", 8, 1, 10, "The maximum of Treasure chest markers (gravestones, bones).");
        gravestoneFogProbability = config.getInt("gravestoneFogProbability", "04-gen", 50, 0, 100, "The probability that a gravestone will have fog."); 
        
        // wells
        minChunksPerChest = config.getInt("minChunksPerChest", "04-gen", 500, 100, 32000, "");
        
        // wither items
        witherRootItemGenProbability = config.getFloat("witherRootGenProbability", "04-gen", 50.0F, 0.0F, 100.0F, "");
        witherBranchItemGenProbability = config.getFloat("witherBranchGenProbability", "04-gen", 50.0F, 0.0F, 100.0F, "");
        
        // foreign mod enablements
        enableForeignModIDs = config.getStringList("enableForeignModIDs", "04-gen", new String[]{"mocreatures"}, "Add mod's MODID to this list to enable custom loot tables for a mod.");
        availableForeignModLootTables = config.getStringList("availableForeignModLootTables", "04-gen", new String[]{"mocreatures"}, "A list of mods that have prebuilt loot tables available. Note: used for informational purposes only.");
        
        // the the default values
       if(config.hasChanged()) {
    	   config.save();
       }
       
		return config;
	}
}
