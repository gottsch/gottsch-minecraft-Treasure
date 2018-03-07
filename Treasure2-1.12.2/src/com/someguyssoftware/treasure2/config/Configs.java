/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.Wells;

/**
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class Configs {
	private static final String TREASURE_CONFIG_DIR = "treasure2";
	
	public static TreasureConfig modConfig;
	private static Map<Rarity, IChestConfig> defaultChestConfigs = new HashMap<>();
	public static Map<Rarity, IChestConfig> chestConfigs = new HashMap<>();
	public static Map<Wells, IWellConfig> defaultWellConfigs = new HashMap<>();
	public static Map<Wells, IWellConfig> wellConfigs = new HashMap<>();
	static {
		initDefaultChestConfigs();
		initDefaultWellConfigs();
	}
	
	/**
	 * 
	 * @param mod
	 * @param configDir
	 */
	public static void init(IMod mod, File configDir) {
		// create and load the config files
		modConfig = new TreasureConfig(Treasure.instance, configDir, TREASURE_CONFIG_DIR, "general.cfg");
		chestConfigs.put(Rarity.COMMON, new ChestConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "common-chest.cfg", defaultChestConfigs.get(Rarity.COMMON)));
		chestConfigs.put(Rarity.UNCOMMON, new ChestConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "uncommon-chest.cfg", defaultChestConfigs.get(Rarity.UNCOMMON)));
		chestConfigs.put(Rarity.SCARCE, new ChestConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "scarce-chest.cfg", defaultChestConfigs.get(Rarity.SCARCE)));
		chestConfigs.put(Rarity.RARE, new ChestConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "rare-chest.cfg", defaultChestConfigs.get(Rarity.RARE)));
		chestConfigs.put(Rarity.EPIC, new ChestConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "epic-chest.cfg", defaultChestConfigs.get(Rarity.EPIC)));

		wellConfigs.put(Wells.WISHING_WELL, new WellConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "wishing-well.cfg", defaultWellConfigs.get(Wells.WISHING_WELL)));
		wellConfigs.put(Wells.CANOPY_WISHING_WELL, new WellConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "canopy-wishing-well.cfg", defaultWellConfigs.get(Wells.WISHING_WELL)));
		wellConfigs.put(Wells.WOOD_DRAW_WISHING_WELL, new WellConfig(mod.getInstance(), configDir, TREASURE_CONFIG_DIR, "wood-draw-wishing-well.cfg", defaultWellConfigs.get(Wells.WISHING_WELL)));

	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static void  initDefaultChestConfigs() {
		
		defaultChestConfigs.put(Rarity.COMMON, new ChestConfig()
				.setChestAllowed(true)
				.setAboveGroundAllowed(true)
				.setBelowGroundAllowed(true)
				.setChunksPerChest(75)
				.setGenProbability(85)
				.setMinYSpawn(50)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"plains", "ocean"})
				);
		
		defaultChestConfigs.put(Rarity.UNCOMMON, new ChestConfig()
				.setChestAllowed(true)
				.setAboveGroundAllowed(true)
				.setBelowGroundAllowed(true)
				.setChunksPerChest(150)
				.setGenProbability(75)
				.setMinYSpawn(40)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"plains", "ocean"})
				);
		
		defaultChestConfigs.put(Rarity.SCARCE, new ChestConfig()
				.setChestAllowed(true)
				.setAboveGroundAllowed(false)
				.setBelowGroundAllowed(true)
				.setChunksPerChest(300)
				.setGenProbability(50)
				.setMinYSpawn(30)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"ocean"})
				);
		
		defaultChestConfigs.put(Rarity.RARE, new ChestConfig()
				.setChestAllowed(true)
				.setAboveGroundAllowed(false)
				.setBelowGroundAllowed(true)
				.setChunksPerChest(500)
				.setGenProbability(25)
				.setMinYSpawn(20)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"ocean"})
				);
		
		defaultChestConfigs.put(Rarity.EPIC, new ChestConfig()
				.setChestAllowed(true)
				.setAboveGroundAllowed(false)
				.setBelowGroundAllowed(true)
				.setChunksPerChest(800)
				.setGenProbability(15)
				.setMinYSpawn(10)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"ocean"})
				);
	}
	
	@SuppressWarnings("unchecked")
	private static void  initDefaultWellConfigs() {
		defaultWellConfigs.put(Wells.WISHING_WELL,  new WellConfig()
				.setWellAllowed(true)
				.setChunksPerWell(500)
				.setGenProbability(80)
				.setRawBiomeWhiteList(new String[] {""})
				.setRawBiomeBlackList(new String[] {"ocean"})
				);
	}
}
