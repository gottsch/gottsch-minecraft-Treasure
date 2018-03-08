/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.gottschcore.config.AbstractConfig;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import scala.actors.threadpool.Arrays;

/**
 * Based off of GottschCore IConfig/AbstractConfig class but does not inherit from it to avoid all the extraneous properties
 * and methods that are not needed for this class.
 * As well, there aren't any static probably as their will be multiple instances based on different attributes, such as Rarity.
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public class ChestConfig implements IChestConfig {
	private IMod mod;
	private Configuration forgeConfiguration;
	
	// chest
	private boolean chestAllowed;
	private int chunksPerChest;
	private double genProbability;
	private int minYSpawn;
	private boolean aboveGroundAllowed;
	private boolean belowGroundAllowed;
	
	// biome type white/black lists
	private  String[] rawBiomeWhiteList;
	private  String[] rawBiomeBlackList;
	
	private List<BiomeTypeHolder> biomeWhiteList;
	private List<BiomeTypeHolder> biomeBlackList;
	
	/**
	 * Empty constructor
	 */
	public ChestConfig() {
		biomeWhiteList = new ArrayList<>(5);
		biomeBlackList = new ArrayList<>(5);
	}
	
	/**
	 * 
	 * @param configDir
	 * @param modDir
	 * @param filename
	 */
	public ChestConfig(IMod mod, File configDir, String modDir, String filename, IChestConfig defaults) {
		this();
		this.mod = mod;
		// build the path to the minecraft config directory
		String configPath = (new StringBuilder()).append(configDir).append("/").append(modDir).append("/").toString();
		// create the config file
		File configFile = new File((new StringBuilder()).append(configPath).append(filename).toString());
		// load the config file
		Configuration configuration = load(configFile, defaults);
		this.forgeConfiguration = configuration;
	}

	/**
	 * 
	 * @param file
	 * @param defaults
	 * @return
	 */
	public Configuration load(File file, IChestConfig defaults) {
		// load the config file
		Configuration config = IChestConfig.super.load(file);
		// ge the modid
		String modid = mod.getClass().getAnnotation(Mod.class).modid();
		
		config.setCategoryComment("01-enable", "Enablements.");
        chestAllowed = config.getBoolean("chestAllowed", "01-enable", defaults.isChestAllowed(), "");
        aboveGroundAllowed = config.getBoolean("isAboveGroundAllowed", "01-enable", defaults.isAboveGroundAllowed(), "");
        belowGroundAllowed = config.getBoolean("isBelowGroundAllowed", "01-enable", defaults.isBelowGroundAllowed(), "");
        
        // gen props
    	chunksPerChest = config.getInt("chunksPerChest", "02-gen", defaults.getChunksPerChest(), 50, 32000, "");
    	genProbability = config.getFloat("genProbability", "02-gen", (float)defaults.getGenProbability(), 0.0F, 100.0F, "");
    	minYSpawn = config.getInt("minYSpawn", "02-gen", defaults.getMinYSpawn(), 5, 250, "");
    	
        // white/black lists
        rawBiomeWhiteList = config.getStringList("biomeWhiteList", "02-gen", (String[]) defaults.getRawBiomeWhiteList(), "Allowable Biome Types for general Chest generation. Must match the Type identifer(s).");
        rawBiomeBlackList = config.getStringList("biomeBlackList", "02-gen", (String[]) defaults.getRawBiomeBlackList(), "Disallowable Biome Types for general Chest generation. Must match the Type identifer(s).");
              
        // update the config if it has changed.
       if(config.hasChanged()) {
    	   config.save();
       }

		BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
		BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
       
		return config;
	}

	/**
	 * @return the chestAllowed
	 */
	@Override
	public boolean isChestAllowed() {
		return chestAllowed;
	}

	/**
	 * @param chestAllowed the chestAllowed to set
	 */
	@Override
	public IChestConfig setChestAllowed(boolean chestAllowed) {
		this.chestAllowed = chestAllowed;
		return this;
	}

	/**
	 * @return the chunksPerChest
	 */
	@Override
	public  int getChunksPerChest() {
		return chunksPerChest;
	}

	/**
	 * @param chunksPerChest the chunksPerChest to set
	 */
	@Override
	public IChestConfig setChunksPerChest(int chunksPerChest) {
		this.chunksPerChest = chunksPerChest;
		return this;
	}

	/**
	 * @return the genProbability
	 */
	@Override
	public double getGenProbability() {
		return genProbability;
	}

	/**
	 * @param genProbability the genProbability to set
	 */
	@Override
	public  IChestConfig setGenProbability(double genProbability) {
		this.genProbability = genProbability;
		return this;
	}

	/**
	 * @return the minYSpawn
	 */
	@Override
	public int getMinYSpawn() {
		return minYSpawn;
	}

	/**
	 * @param minYSpawn the minYSpawn to set
	 */
	@Override
	public  IChestConfig setMinYSpawn(int minYSpawn) {
		this.minYSpawn = minYSpawn;
		return this;
	}

	/**
	 * @return the aboveGroundAllowed
	 */
	@Override
	public  boolean isAboveGroundAllowed() {
		return aboveGroundAllowed;
	}

	/**
	 * @param aboveGroundAllowed the aboveGroundAllowed to set
	 */
	@Override
	public  IChestConfig setAboveGroundAllowed(boolean aboveGroundAllowed) {
		this.aboveGroundAllowed = aboveGroundAllowed;
		return this;
	}

	/**
	 * @return the belowGroundAllowed
	 */
	@Override
	public  boolean isBelowGroundAllowed() {
		return belowGroundAllowed;
	}

	/**
	 * @param belowGroundAllowed the belowGroundAllowed to set
	 */
	@Override
	public  IChestConfig setBelowGroundAllowed(boolean belowGroundAllowed) {
		this.belowGroundAllowed = belowGroundAllowed;
		return this;
	}

	/**
	 * @return the biomeWhiteList
	 */
	@Override
	public List<BiomeTypeHolder> getBiomeWhiteList() {
		return biomeWhiteList;
	}

	/**
	 * @param biomeWhiteList the biomeWhiteList to set
	 */
	@Override
	public void setBiomeWhiteList(List<BiomeTypeHolder> biomeWhiteList) {
		this.biomeWhiteList = biomeWhiteList;
	}

	/**
	 * @return the biomeBlackList
	 */
	@Override
	public List<BiomeTypeHolder> getBiomeBlackList() {
		return biomeBlackList;
	}

	/**
	 * @param biomeBlackList the biomeBlackList to set
	 */
	@Override
	public void setBiomeBlackList(List<BiomeTypeHolder> biomeBlackList) {
		this.biomeBlackList = biomeBlackList;
	}

	/**
	 * @return the rawBiomeWhiteList
	 */
	@Override
	public String[] getRawBiomeWhiteList() {
		return rawBiomeWhiteList;
	}

	/**
	 * @param rawBiomeWhiteList the rawBiomeWhiteList to set
	 */
	@Override
	public IChestConfig setRawBiomeWhiteList(String[] rawBiomeWhiteList) {
		this.rawBiomeWhiteList = rawBiomeWhiteList;
		return this;
	}

	/**
	 * @return the rawBiomeBlackList
	 */
	@Override
	public String[] getRawBiomeBlackList() {
		return rawBiomeBlackList;
	}

	/**
	 * @param rawBiomeBlackList the rawBiomeBlackList to set
	 */
	@Override
	public IChestConfig setRawBiomeBlackList(String[] rawBiomeBlackList) {
		this.rawBiomeBlackList = rawBiomeBlackList;
		return this;
	}

}
