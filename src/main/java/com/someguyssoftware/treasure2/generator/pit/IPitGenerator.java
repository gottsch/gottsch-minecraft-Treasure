package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.world.IServerWorld;
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
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateBase(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public boolean generatePit(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateEntrance(IServerWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public int getOffsetY();
	public void setOffsetY(int i);

	int getMinSurfaceToSpawnDistance();
}