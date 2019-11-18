package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.world.World;

public interface IPitGenerator<RESULT extends IGeneratorResult<?>> {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateBase(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public boolean generatePit(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateEntrance(World world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public int getOffsetY();
	public void setOffsetY(int i);
}