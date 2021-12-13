/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerBlockEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.IServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.DungeonHooks;

/**
 * 
 * @author Mark Gottschling on Mar 7, 2018
 *
 */
public abstract class AbstractPitGenerator implements IPitGenerator<GeneratorResult<ChestGeneratorData>> {

	protected static final int OFFSET_Y = 5;
	protected static final int SURFACE_OFFSET_Y = 6;
	protected static final Block DEFAULT_LOG = Blocks.OAK_LOG;
	
	private RandomWeightedCollection<Block> blockLayers = new RandomWeightedCollection<>();
	private int offsetY = OFFSET_Y;

	/**
	 * 
	 */
	public AbstractPitGenerator() {
		super();
		// standard set of block layers
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25,  Blocks.SAND);
		getBlockLayers().add(15, Blocks.GRAVEL);
		getBlockLayers().add(10, DEFAULT_LOG);
	}

	/**
	 * 
	 */
	@Override
	public boolean generateBase(IServerLevel world, Random random, ICoords surfaceCorods, ICoords spawnCoords) {
		Treasure.LOGGER.debug("generating base ...");
		// at chest level
		buildLayer(world, spawnCoords, Blocks.AIR);
		
		// above the chest
		buildAboveChestLayers(world, random, spawnCoords);
		
		return true;
	}
	
	@Override
	public boolean generatePit(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		buildPit(world, random, spawnCoords, surfaceCoords, getBlockLayers());
		return true;
	}
	
	@Override
	public boolean generateEntrance(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		Treasure.LOGGER.debug("generating entrance ...");
		// pit enterance
		buildLogLayer(world, random, surfaceCoords.add(0, -3, 0), DEFAULT_LOG);
		buildLayer(world, surfaceCoords.add(0, -4, 0), Blocks.SAND);
		buildLogLayer(world, random, surfaceCoords.add(0, -5, 0), DEFAULT_LOG);
		return true;
	}
	
	/**
	 * Pits spawn underground. The spawnCoords is the location of the bottom of the pit and is the spot where the pit begins generated.
	 * Pits generate upward towards the surface.
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
		result.getData().setSpawnCoords(spawnCoords);
		result.getData().getChestContext().setCoords(spawnCoords);
		
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		BlockState blockState = world.getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.LOGGER.debug("Spawn coords is in cavern.");
			inCavern = true;
		}

		if (inCavern) {
			Treasure.LOGGER.debug("Shaft is in cavern... finding ceiling.");
			spawnCoords = GenUtil.findUndergroundCeiling(world, spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.LOGGER.warn("Exiting: Unable to locate cavern ceiling.");
				return result.fail();
			}
			result.getData().setSpawnCoords(spawnCoords);
			// update the chest coords in the result
			result.getData().getChestContext().setCoords(spawnCoords);
		}
	
		// generate shaft
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.LOGGER.debug("Distance to ySurface =" + yDist);
	
		if (yDist > getMinSurfaceToSpawnDistance()) {
			Treasure.LOGGER.debug("Generating shaft @ " + spawnCoords.toShortString());

			generateBase(world, random, surfaceCoords, spawnCoords);
			
			// pit enterance
			generateEntrance(world, random, surfaceCoords, spawnCoords);

			// build the pit
			generatePit(world, random, surfaceCoords, spawnCoords);
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			Treasure.LOGGER.debug("less than 2, generate simple short pit gen");
			// simple short pit
			result = new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}
		return result.success();
	}	
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int getMinSurfaceToSpawnDistance() {
		return 6;
	}

	/**
	 * 
	 * @param world
	 * @param spawnCoords
	 */
	public void buildAboveChestLayers(IServerLevel world, Random random, ICoords spawnCoords) {
		buildLayer(world, spawnCoords.add(0, 1, 0), Blocks.AIR);
		buildLayer(world, spawnCoords.add(0, 2, 0), Blocks.AIR);
		buildLogLayer(world, random, spawnCoords.add(0, 3, 0), DEFAULT_LOG);
		buildLayer(world, spawnCoords.add(0, 4, 0), Blocks.SAND);
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	public ICoords buildPit(IServerLevel world, Random random, ICoords coords, ICoords surfaceCoords, RandomWeightedCollection<Block> col) {
		Treasure.LOGGER.debug("generating pit ...");
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + getOffsetY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			BlockContext cube = new BlockContext(world, new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}
			
			// get the next type of block layer to build
			Block block = col.next();
			if (block == DEFAULT_LOG) {
				// special log build layer
				nextCoords = buildLogLayer(world, random, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
				// ie. LayerBuilder.build(world, coords, block)
			}
			else {
				nextCoords = buildLayer(world, cube.getCoords(), block);
			}
			expectedCoords = cube.getCoords().add(0, 1, 0);
			
			// check if the return coords is different than the anticipated coords and resolve
			yIndex = autoCorrectIndex(yIndex, nextCoords, expectedCoords);
		}		
		return nextCoords;
	}
	
	/**
	 * 
	 * @param index
	 * @param coords
	 * @param expectedCoords
	 * @return
	 */
	protected int autoCorrectIndex(final int index, final ICoords coords, final ICoords expectedCoords) {
		int newIndex = index;
		if (!coords.equals(expectedCoords)) {
			// find the difference in y int and add to yIndex;
			Treasure.LOGGER.debug("Next coords does not equal expected coords. next: {}; expected: {}", coords.toShortString(), expectedCoords.toShortString());
			// NOTE the difference should = 1, there remove 1 from the diff to find unexpected difference
			int diff = coords.getY() - expectedCoords.getY() - 1;
			if (diff > 0) {
				newIndex = coords.getY();
				Treasure.LOGGER.debug("Difference of: {}. Updating yIndex to {}", diff, newIndex);
			}
		}
		return newIndex;
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLayer(IServerLevel world, ICoords coords, Block block) {
		Treasure.LOGGER.debug("Building layer from {} @ {} ", block.getRegistryName(), coords.toShortString());
		GenUtil.replaceWithBlock(world, coords, block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 0), block);
		GenUtil.replaceWithBlock(world, coords.add(0, 0, 1), block);
		GenUtil.replaceWithBlock(world, coords.add(1, 0, 1), block);
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLogLayer(final IServerLevel world, final Random random, final ICoords coords, final Block block) {
		Treasure.LOGGER.debug("building log layer from {} @ {} ", block.getRegistryName(), coords.toShortString());
		// ensure that block is of type LOG/LOG2
		if (!(block instanceof RotatedPillarBlock)) {
			Treasure.LOGGER.debug("block is not a log");
            return coords;
        }

		// randomly select the axis the logs are facing (0 = Z, 1 = X);
		int axis = random.nextInt(2);
		BlockState blockState = block.defaultBlockState();
		if (axis == 0) {
			blockState = blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		}
		else {
			blockState = blockState.setValue(RotatedPillarBlock.AXIS,  Direction.Axis.X);
		}

		// core 4-square
		GenUtil.replaceWithBlockState(world, coords, blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 0), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(0, 0, 1), blockState);
		GenUtil.replaceWithBlockState(world, coords.add(1, 0, 1), blockState);
		
		if (axis == 0) {			
			// north of
			GenUtil.replaceWithBlockState(world, coords.add(0, 0, -1), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(1, 0, -1), blockState);
			
			// south of
			GenUtil.replaceWithBlockState(world, coords.add(0, 0, 2), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(1, 0, 2), blockState);
		}
		else {
			// west of
			GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 0), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(-1, 0, 1), blockState);
			// east of 
			GenUtil.replaceWithBlockState(world, coords.add(2, 0, 0), blockState);
			GenUtil.replaceWithBlockState(world, coords.add(2, 0, 1), blockState);
		}
		Treasure.LOGGER.debug("log level complete");
		return coords.add(0, 1, 0);
	}
	
	/**
	 * 
	 * @param world
	 * @param spawnCoords
	 * @param mob
	 */
	public void spawnMob(IServerLevel world, ICoords spawnCoords, String mobName) {
		Mob mob = null;
		switch (mobName) {
		case "zombie":
			mob = new ZombieEntity((Level) world);
			break;
		case "skeleton":
			mob = new SkeletonEntity(EntityType.SKELETON, (Level) world);
			break;
		}
    	mob.moveTo((double)spawnCoords.getX() + 0.5D,  (double)spawnCoords.getY(), (double)spawnCoords.getZ() + 0.5D, 0.0F, 0.0F);
		if (!world.addFreshEntity(mob)) {
			Treasure.LOGGER.debug("unable to spawn entity in world -> {}", mob.getName());
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 */
	public void spawnRandomMob(IServerLevel world, Random random, ICoords spawnCoords) {
		WorldInfo.setBlock(world, spawnCoords, TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState());
//    	world.setBlock(spawnCoords.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getBlockEntity(spawnCoords.toPos());
    	if (te == null) {
    		Treasure.LOGGER.debug("proximity spawner TE is null @ {}", spawnCoords.toShortString());
    		return;
    	}
    	EntityType<?> mobEntityType = DungeonHooks.getRandomDungeonMob(random);
    	Treasure.LOGGER.debug("spawn mob entity -> {}", mobEntityType);
    	if (mobEntityType != null) {
    		Treasure.LOGGER.debug("spawn mob -> {}", mobEntityType.getRegistryName());
	    	te.setMobName(mobEntityType.getRegistryName());
	    	te.setMobNum(new Quantity(1, 1));
	    	te.setProximity(3D);
    	}
	}
	
	/**
	 * @return the blockLayers
	 */
	public RandomWeightedCollection<Block> getBlockLayers() {
		return blockLayers;
	}

	/**
	 * @param blockLayers the blockLayers to set
	 */
	public void setBlockLayers(RandomWeightedCollection<Block> blockLayers) {
		this.blockLayers = blockLayers;
	}

	@Override
	public int getOffsetY() {
		return offsetY;
	}

	@Override
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
}