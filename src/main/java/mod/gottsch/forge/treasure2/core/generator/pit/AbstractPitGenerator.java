/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.pit;

import java.util.Optional;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.block.entity.ProximitySpawnerBlockEntity;
import mod.gottsch.forge.gottschcore.random.WeightedCollection;
import mod.gottsch.forge.gottschcore.size.Quantity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
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
	
	private WeightedCollection<Integer, Block> blockLayers = new WeightedCollection<>();
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
	public boolean generateBase(IWorldGenContext context, ICoords surfaceCorods, ICoords spawnCoords) {
		Treasure.LOGGER.debug("generating base ...");
		// at chest level
		buildLayer(context, spawnCoords, Blocks.AIR);
		
		// above the chest
		buildAboveChestLayers(context, spawnCoords);
		
		return true;
	}
	
	@Override
	public boolean generatePit(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		buildPit(context, spawnCoords, surfaceCoords, getBlockLayers());
		return true;
	}
	
	@Override
	public boolean generateEntrance(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		Treasure.LOGGER.debug("generating entrance ...");
		// pit enterance
		buildLogLayer(context, surfaceCoords.add(0, -3, 0), DEFAULT_LOG);
		buildLayer(context, surfaceCoords.add(0, -4, 0), Blocks.SAND);
		buildLogLayer(context, surfaceCoords.add(0, -5, 0), DEFAULT_LOG);
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
	public Optional<GeneratorResult<ChestGeneratorData>> generate(IWorldGenContext context, ICoords surfaceCoords, ICoords spawnCoords) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);		
		result.getData().setSpawnCoords(spawnCoords);
		// by default the chest is placed at the spawn coords. the pit is centered around this point.
		result.getData().setCoords(spawnCoords);
		
		// is the chest placed in a cavern
		boolean inCavern = false;
		
		// check above if there is a free space - chest may have spawned in underground cavern, ravine, dungeon etc
		BlockState blockState = context.level().getBlockState(spawnCoords.add(0, 1, 0).toPos());
		
		// if there is air above the origin, then in cavern. (pos in isAir() doesn't matter)
		if (blockState == null || blockState.getMaterial() == Material.AIR) {
			Treasure.LOGGER.debug("spawn coords is in a cavern.");
			inCavern = true;
		}

		if (inCavern) {
			Treasure.LOGGER.debug("shaft is in cavern... finding ceiling.");
			spawnCoords = GeneratorUtil.findSubterraneanCeiling(context.level(), spawnCoords.add(0, 1, 0));
			if (spawnCoords == null) {
				Treasure.LOGGER.warn("unable to locate cavern ceiling.");
				return Optional.empty();
			}
			result.getData().setSpawnCoords(spawnCoords);
			// update the chest coords in the result
			result.getData().setCoords(spawnCoords);
		}
	
		// generate shaft
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.LOGGER.debug("distance to surface =" + yDist);
	
		if (yDist > getMinSurfaceToSpawnDistance()) {
			Treasure.LOGGER.debug("generating pit at -> {}", spawnCoords.toShortString());

			generateBase(context, surfaceCoords, spawnCoords);
			
			// pit enterance
			generateEntrance(context, surfaceCoords, spawnCoords);

			// build the pit
			generatePit(context, surfaceCoords, spawnCoords);
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			Treasure.LOGGER.debug("less than 2, generate simple short pit gen");
			// simple short pit
			return new SimpleShortPitGenerator().generate(context, surfaceCoords, spawnCoords);
		}
		return Optional.ofNullable(result);
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
	public void buildAboveChestLayers(IWorldGenContext context, ICoords spawnCoords) {
		buildLayer(context, spawnCoords.add(0, 1, 0), Blocks.AIR);
		buildLayer(context, spawnCoords.add(0, 2, 0), Blocks.AIR);
		buildLogLayer(context, spawnCoords.add(0, 3, 0), DEFAULT_LOG);
		buildLayer(context, spawnCoords.add(0, 4, 0), Blocks.SAND);
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param spawnCoords
	 * @param surfaceCoords
	 * @return
	 */
	public ICoords buildPit(IWorldGenContext context, ICoords coords, ICoords surfaceCoords, WeightedCollection<Integer, Block> col) {
		Treasure.LOGGER.debug("generating pit ...");
		ICoords nextCoords = null;
		ICoords expectedCoords = null;
		
		// randomly fill shaft
		for (int yIndex = coords.getY() + getOffsetY(); yIndex <= surfaceCoords.getY() - SURFACE_OFFSET_Y; yIndex++) {
			
			// if the block to be replaced is air block then skip to the next pos
			BlockContext cube = new BlockContext(context.level(), new Coords(coords.getX(), yIndex, coords.getZ()));
			if (cube.isAir()) {
				continue;
			}
			
			// get the next type of block layer to build
			Block block = col.next();
			if (block == DEFAULT_LOG) {
				// special log build layer
				nextCoords = buildLogLayer(context, cube.getCoords(), block); // could have difference classes and implement buildLayer differently
				// ie. LayerBuilder.build(world, coords, block)
			}
			else {
				nextCoords = buildLayer(context, cube.getCoords(), block);
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
	public ICoords buildLayer(IWorldGenContext context, ICoords coords, Block block) {
		Treasure.LOGGER.debug("Building layer from {} @ {} ", block.getRegistryName(), coords.toShortString());
		GeneratorUtil.replaceWithBlock(context.level(), coords, block);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(1, 0, 0), block);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(0, 0, 1), block);
		GeneratorUtil.replaceWithBlock(context.level(), coords.add(1, 0, 1), block);
		
		return coords.add(0, 1, 0);
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildLogLayer(IWorldGenContext context, final ICoords coords, final Block block) {
		Treasure.LOGGER.debug("building log layer from {} @ {} ", block.getRegistryName(), coords.toShortString());
		// ensure that block is of type LOG/LOG2
		if (!(block instanceof RotatedPillarBlock)) {
			Treasure.LOGGER.debug("block is not a log");
            return coords;
        }

		// randomly select the axis the logs are facing (0 = Z, 1 = X);
		int axis = context.random().nextInt(2);
		BlockState blockState = block.defaultBlockState();
		if (axis == 0) {
			blockState = blockState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
		}
		else {
			blockState = blockState.setValue(RotatedPillarBlock.AXIS,  Direction.Axis.X);
		}

		// core 4-square
		ServerLevelAccessor level = context.level();
		GeneratorUtil.replaceWithBlockState(level, coords, blockState);
		GeneratorUtil.replaceWithBlockState(level, coords.add(1, 0, 0), blockState);
		GeneratorUtil.replaceWithBlockState(level, coords.add(0, 0, 1), blockState);
		GeneratorUtil.replaceWithBlockState(level, coords.add(1, 0, 1), blockState);
		
		if (axis == 0) {			
			// north of
			GeneratorUtil.replaceWithBlockState(level, coords.add(0, 0, -1), blockState);
			GeneratorUtil.replaceWithBlockState(level, coords.add(1, 0, -1), blockState);
			
			// south of
			GeneratorUtil.replaceWithBlockState(level, coords.add(0, 0, 2), blockState);
			GeneratorUtil.replaceWithBlockState(level, coords.add(1, 0, 2), blockState);
		}
		else {
			// west of
			GeneratorUtil.replaceWithBlockState(level, coords.add(-1, 0, 0), blockState);
			GeneratorUtil.replaceWithBlockState(level, coords.add(-1, 0, 1), blockState);
			// east of 
			GeneratorUtil.replaceWithBlockState(level, coords.add(2, 0, 0), blockState);
			GeneratorUtil.replaceWithBlockState(level, coords.add(2, 0, 1), blockState);
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
	public void spawnMob(ServerLevel world, ICoords spawnCoords, String mobName) {
		Mob mob = null;
		switch (mobName) {
		case "zombie":
			mob = new Zombie(world);
			break;
		case "skeleton":
			mob = new Skeleton(EntityType.SKELETON, world);
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
	public void spawnRandomMob(IWorldGenContext context, ICoords spawnCoords) {
		WorldInfo.setBlock(context.level(), spawnCoords, TreasureBlocks.PROXIMITY_SPAWNER.get().defaultBlockState());
//    	context.level().setBlock(spawnCoords.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.defaultBlockState(), 3);
    	ProximitySpawnerBlockEntity blockEntity = (ProximitySpawnerBlockEntity) context.level().getBlockEntity(spawnCoords.toPos());
    	if (blockEntity == null) {
    		Treasure.LOGGER.debug("proximity spawner TE is null @ {}", spawnCoords.toShortString());
    		return;
    	}
    	EntityType<?> mobEntityType = DungeonHooks.getRandomDungeonMob(context.random());
    	Treasure.LOGGER.debug("spawn mob entity -> {}", mobEntityType);
    	if (mobEntityType != null) {
    		Treasure.LOGGER.debug("spawn mob -> {}", mobEntityType.getRegistryName());
	    	blockEntity.setMobName(mobEntityType.getRegistryName());
	    	blockEntity.setMobNum(new Quantity(1, 1));
	    	blockEntity.setProximity(3D);
    	}
	}
	
	/**
	 * @return the blockLayers
	 */
	public WeightedCollection<Integer, Block> getBlockLayers() {
		return blockLayers;
	}

	/**
	 * @param blockLayers the blockLayers to set
	 */
	public void setBlockLayers(WeightedCollection<Integer, Block> blockLayers) {
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