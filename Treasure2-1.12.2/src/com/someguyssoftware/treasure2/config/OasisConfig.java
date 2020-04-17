package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;

/**
 * 
 * @author Mark Gottschling on Apr 8, 2020
 *
 */
public class OasisConfig implements IOasisConfig {
	@Comment({ "Toggle to allow/disallow the spawn of oasis." })
	@Name("01. Enabled oasis:")
	public boolean enableOasis = true;
	
	@Comment({"The minimum number of chunks generated before another attempt to spawn the oasis is made.", "Note: this value is per biome."})
	@Name("02. Chunks per oasis spawn:")
	@RangeInt(min = 100, max = 32000)
	public int chunksPerOasis = 200;
	
	@Comment({ "The probability that the oasis will generate." })
	@Name("03. Generation probability:")
	@RangeDouble(min = 0.0, max = 100.0)
	public double genProbability = 80.0;
	
	@Comment({"The number of trees per chunk (16x16 blocks) in an oasis."})
	@Name("04. Trees per chunk:")
	@RangeInt(min = 0, max = 256)
	public int treesPerChunk = 15;
	
	@Comment({ "The rate at which the number of trees per chunk grows with the size of the oasis." })
	@Name("05. Trees per chunk size factor:")
	@RangeDouble(min = 0.0, max = 5.0)
	public double treesPerChunkSizeFactor = 1.3D;
	
	@Name("biomes")
	@Comment({"Biome white and black list properties."})
	public BiomesConfig biomes = new BiomesConfig(
			new String[] {},
			new String[] {},
			new String[] {},
			new String[] {});

	
	
	/*
	 * 
	 */
	public OasisConfig(boolean isAllowed, int chunksPer, double probability,
			String[] whiteList, String[] blackList, String[] typeWhiteList, String[] typeBlackList) {
		this.enableOasis = isAllowed;
		this.chunksPerOasis = chunksPer;
		this.genProbability = probability;
		this.biomes = new BiomesConfig(whiteList, blackList, typeWhiteList, typeBlackList);
	}
	
	@Override
	public void init() {
		this.biomes.init();
	}

	@Override
	public boolean isEnableOasis() {
		return enableOasis;
	}

	@Override
	public int getChunksPerOasis() {
		return chunksPerOasis;
	}

	@Override
	public double getGenProbability() {
		return genProbability;
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
	public List<Biome> getBiomeWhiteList() {
		return biomes.getWhiteList();
	}

	@Override
	public List<Biome> getBiomeBlackList() {
		return biomes.getBlackList();
	}

	@Override
	public int getTreesPerChunk() {
		return treesPerChunk;
	}

	@Override
	public void setTreesPerChunk(int treesPerChunk) {
		this.treesPerChunk = treesPerChunk;
	}

	@Override
	public double getTreesPerChunkSizeFactor() {
		return treesPerChunkSizeFactor;
	}

	@Override
	public void setTreesPerChunkSizeFactor(double treesPerChunkSizeFactor) {
		this.treesPerChunkSizeFactor = treesPerChunkSizeFactor;
	}
}
