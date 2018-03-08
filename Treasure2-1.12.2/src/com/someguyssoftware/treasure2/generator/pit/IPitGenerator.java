package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IPitGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public boolean generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

}