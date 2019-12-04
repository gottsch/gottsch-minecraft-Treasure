package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

/*
 * 
 */
public class ChestConfig implements IChestConfig {
	@Comment({"Enable/Disable generating chests associated with this rarity."})
	@Name("00. Enable chests for rarity.")
	public boolean enableChest = true;

	@Comment({"The number of chunks generated before the chest spawn is attempted."})
	@Name("01. Chunks per chest spawn:")
	@RangeInt(min = 50, max = 32000)
	public int chunksPerChest = 75;

	@Comment({"The probability that a chest will spawn."})
	@Name("02. Probability of chest spawn:")
	@RangeDouble(min = 0.0, max = 100.0)
	public double genProbability = 50.0;

	@Comment({"The minimum depth (y-axis) that a chest can generate at."})
	@Name("03. Min. y-value for spawn location:")
	@RangeInt(min = 5, max = 250)
	public int minYSpawn = 25;

	@Comment({"The probability that a chest will be a mimic.", "NOTE: only common Wooden ChestConfig have mimics avaiable."})
	@Name("04. Mimic probability:")
	@RangeDouble(min = 0.0, max = 100.0)
	public double mimicProbability = 0.0;

	// TODO most likely going to be removed with the use of meta files / archetype : type : biome categorizations
	@Name("05. Enable surface spawn:")
	public boolean surfaceAllowed = true;

	@Name("06. Enable subterranean spawn:")
	public boolean subterraneanAllowed = true;

	@Name("biomes")
	@Comment({"Biome white and black list properties."})
	public BiomesConfig biomes = new BiomesConfig(
			new String[] {},
			new String[] {"ocean", "deep_ocean", "deep_frozen_ocean", 
					"cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean"},
			new String[] {},
			new String[] {"ocean", "deep_ocean"});

	/*
	 * 
	 */
	public ChestConfig(boolean isAllowed, int chunksPer, double probability, int minYSpawn,
			String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
		this.enableChest = isAllowed;
		this.chunksPerChest = chunksPer;
		this.genProbability = probability;
		this.minYSpawn = minYSpawn;
		this.biomes = new BiomesConfig(whiteList, blackList, typeWhiteList, typeBlackList);
	}

	@Override
	public boolean isEnableChest() {
		return enableChest;
	}

	@Override
	public int getChunksPerChest() {
		return chunksPerChest;
	}

	@Override
	public double getGenProbability() {
		return genProbability;
	}

	@Override
	public int getMinYSpawn() {
		return minYSpawn;
	}

	@Override
	public boolean isSurfaceAllowed() {
		return surfaceAllowed;
	}

	@Override
	public boolean isSubterraneanAllowed() {
		return subterraneanAllowed;
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeWhiteList() {
		return biomes.getTypeWhiteList();
	}

	@Override
	public List<BiomeTypeHolder> getBiomeTypeBlackList() {
		return biomes.getTypeBlackList();
	}

	@Override
	public double getMimicProbability() {
		return mimicProbability;
	}

	@Override
	public List<Biome> getBiomeWhiteList() {
		return biomes.getWhiteList();
	}

	@Override
	public List<Biome> getBiomeBlackList() {
		return biomes.getBlackList();
	}
}

