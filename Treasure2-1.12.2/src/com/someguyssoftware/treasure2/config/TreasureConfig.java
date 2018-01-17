/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;

import com.someguyssoftware.gottschcore.config.AbstractConfig;
import com.someguyssoftware.gottschcore.mod.IMod;

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
	public static final String WOODEN_CHEST_ID = "wooden_chest";
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
	public static boolean enableKeyBreaks = true;


	
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
        config.setCategoryComment("03-treasure", "General Treasure! mod properties.");   
        enableKeyBreaks = config.getBoolean("enableKeyBreaks", "03-treasure", true, "Enables/Disable whether a Key can break when attempting to unlock a Lock.");
        
        // the the default values
       if(config.hasChanged()) {
    	   config.save();
       }
       
		return config;
	}
}
