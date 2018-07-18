/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public class WitherTreeConfig implements IWitherTreeConfig {
	private IMod mod;
	private Configuration forgeConfiguration;
	
	private int chunksPerTree;
	private double genProbability;
	private int maxTrunkSize;
	private int minSupportingTrees;
	private int maxSupportingTrees;
	
	// biome type white/black lists
	private  String[] rawBiomeWhiteList;
	private  String[] rawBiomeBlackList;
	
	private List<BiomeTypeHolder> biomeWhiteList;
	private List<BiomeTypeHolder> biomeBlackList;
	
	/**
	 * 
	 */
	public WitherTreeConfig() {
		biomeWhiteList = new ArrayList<>(5);
		biomeBlackList = new ArrayList<>(5);
	}

	/**
	 * 
	 * @param instance
	 * @param configDir
	 * @param modDir
	 * @param filename
	 * @param defaults
	 */
	public WitherTreeConfig(IMod mod, File configDir, String modDir, String filename, 	IWitherTreeConfig defaults) {
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
	public Configuration load(File file, IWitherTreeConfig defaults) {
		// load the config file
		Configuration config = IWitherTreeConfig.super.load(file);
		// ge the modid
		String modid = mod.getClass().getAnnotation(Mod.class).modid();
		
		config.setCategoryComment("01-enable", "Enablements.");
//        wellAllowed = config.getBoolean("wellAllowed", "01-enable", defaults.isWellAllowed(), "");
// 
//        // gen props
    	chunksPerTree = config.getInt("chunksPerTree", "02-gen", defaults.getChunksPerTree(), 200, 32000, "");
    	maxTrunkSize = config.getInt("maxTrunkSize", "02-gen", defaults.getMaxTrunkSize(), 7, 20, "");
    	minSupportingTrees = config.getInt("minSupportingTrees", "02-gen", defaults.getMinSupportingTrees(), 0, 30, "");
    	maxSupportingTrees = config.getInt("maxSupportingTrees", "02-gen", defaults.getMaxSupportingTrees(), 0, 30, "");
    	genProbability = config.getFloat("genProbability", "02-gen", (float)defaults.getGenProbability(), 0.0F, 100.0F, "");
    	    	
        // white/black lists
		Treasure.logger.debug("config: {}", config);
		Treasure.logger.debug("defaults: {}", defaults);
        rawBiomeWhiteList = config.getStringList("biomeWhiteList", "02-gen", (String[]) defaults.getRawBiomeWhiteList(), "Allowable Biome Types for Wither Tree generation. Must match the Type identifer(s).");
        rawBiomeBlackList = config.getStringList("biomeBlackList", "02-gen", (String[]) defaults.getRawBiomeBlackList(), "Disallowable Biome Types for Wither Tree generation. Must match the Type identifer(s).");
              
        // update the config if it has changed.
       if(config.hasChanged()) {
    	   config.save();
       }

		BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
		BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
       
		return config;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IBiomeListConfig#getBiomeWhiteList()
	 */
	@Override
	public List<BiomeTypeHolder> getBiomeWhiteList() {
		return biomeWhiteList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IBiomeListConfig#setBiomeWhiteList(java.util.List)
	 */
	@Override
	public void setBiomeWhiteList(List<BiomeTypeHolder> biomeWhiteList) {
		this.biomeWhiteList = biomeWhiteList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IBiomeListConfig#getBiomeBlackList()
	 */
	@Override
	public List<BiomeTypeHolder> getBiomeBlackList() {
		return biomeBlackList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IBiomeListConfig#setBiomeBlackList(java.util.List)
	 */
	@Override
	public void setBiomeBlackList(List<BiomeTypeHolder> biomeBlackList) {
		this.biomeBlackList = biomeBlackList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IWitherTreeConfig#getRawBiomeBlackList()
	 */
	@Override
	public String[] getRawBiomeBlackList() {
		return this.rawBiomeBlackList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IWitherTreeConfig#setRawBiomeBlackList(java.lang.String[])
	 */
	@Override
	public IWitherTreeConfig setRawBiomeBlackList(String[] rawBiomeBlackList) {
		this.rawBiomeBlackList = rawBiomeBlackList;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IWitherTreeConfig#getRawBiomeWhiteList()
	 */
	@Override
	public String[] getRawBiomeWhiteList() {
		return rawBiomeWhiteList;
	}

	/* (non-Javadoc)
	 * @see com.someguyssoftware.treasure2.config.IWitherTreeConfig#setRawBiomeWhiteList(java.lang.String[])
	 */
	@Override
	public IWitherTreeConfig setRawBiomeWhiteList(String[] rawBiomeWhiteList) {
		this.rawBiomeWhiteList = rawBiomeWhiteList;
		return this;
	}

	/**
	 * @return the chunksPerTree
	 */
	@Override
	public int getChunksPerTree() {
		return chunksPerTree;
	}

	/**
	 * @param chunksPerTree the chunksPerTree to set
	 */
	@Override
	public IWitherTreeConfig setChunksPerTree(int chunksPerTree) {
		this.chunksPerTree = chunksPerTree;
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
	public IWitherTreeConfig setGenProbability(double genProbability) {
		this.genProbability = genProbability;
		return this;
	}

	/**
	 * @return the maxSupportingTrees
	 */
	@Override
	public int getMaxSupportingTrees() {
		return maxSupportingTrees;
	}

	/**
	 * @param maxSupportingTrees the maxSupportingTrees to set
	 */
	@Override
	public IWitherTreeConfig setMaxSupportingTrees(int maxSupportingTrees) {
		this.maxSupportingTrees = maxSupportingTrees;
		return this;
	}

	/**
	 * @return the maxTrunkSize
	 */
	@Override
	public int getMaxTrunkSize() {
		return maxTrunkSize;
	}

	/**
	 * @param maxTrunkSize the maxTrunkSize to set
	 */
	@Override
	public IWitherTreeConfig setMaxTrunkSize(int maxTrunkSize) {
		this.maxTrunkSize = maxTrunkSize;
		return this;
	}

	/**
	 * @return the minSupportingTrees
	 */
	@Override
	public int getMinSupportingTrees() {
		return minSupportingTrees;
	}

	/**
	 * @param minSupportingTrees the minSupportingTrees to set
	 */
	@Override
	public IWitherTreeConfig setMinSupportingTrees(int minSupportingTrees) {
		this.minSupportingTrees = minSupportingTrees;
		return this;
	}
}
