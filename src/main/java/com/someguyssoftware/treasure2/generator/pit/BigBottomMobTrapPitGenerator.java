package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.DungeonHooks;


/**
 * Generates lava blocks outside the main pit to prevent players from digging down on the edges
 * @author Mark Gottschling on Dec 9, 2018
 *
 */
public class BigBottomMobTrapPitGenerator extends AbstractPitGenerator {
	
	/**
	 * 
	 */
	public BigBottomMobTrapPitGenerator() {
		getBlockLayers().add(50, Blocks.AIR);
		getBlockLayers().add(25,  Blocks.SAND);
		getBlockLayers().add(15, Blocks.GRAVEL);
		getBlockLayers().add(10, DEFAULT_LOG);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	@Override
	public GeneratorResult<ChestGeneratorData> generate(IWorld world, Random random, ICoords surfaceCoords, ICoords spawnCoords) {
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
			// update chest coords
			result.getData().setSpawnCoords(spawnCoords);
			result.getData().getChestContext().setCoords(spawnCoords);
		}
	
		// generate shaft
		int yDist = (surfaceCoords.getY() - spawnCoords.getY()) - 2;
		Treasure.LOGGER.debug("Distance to ySurface =" + yDist);
	
		ICoords nextCoords = null;
		if (yDist > 6) {			
			Treasure.LOGGER.debug("Generating shaft @ " + spawnCoords.toShortString());
			// at chest level
			nextCoords = build6WideLayer(world, random, spawnCoords, Blocks.AIR);
			
			// above the chest
			nextCoords = build6WideLayer(world, random, nextCoords, Blocks.AIR);
			nextCoords = build6WideLayer(world, random, nextCoords, Blocks.AIR);
			nextCoords = buildLogLayer(world, random, nextCoords, DEFAULT_LOG);
			nextCoords = buildLayer(world, nextCoords, Blocks.SAND);
			
			// shaft enterance
			buildLogLayer(world, random, surfaceCoords.add(0, -3, 0), DEFAULT_LOG);
			buildLayer(world, surfaceCoords.add(0, -4, 0), Blocks.SAND);
			buildLogLayer(world, random, surfaceCoords.add(0, -5, 0), DEFAULT_LOG);

			// build the trap
			buildTrapLayer(world, random, spawnCoords, null);
			
			// build the pit
			// NOTE must add nextCoords by Y_OFFSET, because the AbstractPitGen.buildPit() starts with the Y_OFFSET, which is above the standard chest area.
			buildPit(world, random, nextCoords.down(OFFSET_Y), surfaceCoords, getBlockLayers());
		}			
		// shaft is only 2-6 blocks long - can only support small covering
		else if (yDist >= 2) {
			// simple short pit
			result = new SimpleShortPitGenerator().generate(world, random, surfaceCoords, spawnCoords);
		}		
		Treasure.LOGGER.debug("Generated Big Bottom Mob Trap Pit at " + spawnCoords.toShortString());
		return result.success();
	}	

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	private ICoords build6WideLayer(IWorld world, Random random, ICoords coords, Block block) {
		ICoords startCoords = coords.add(-2, 0, -2);
		for (int x = startCoords.getX(); x < startCoords.getX() + 6; x++) {
			for (int z = startCoords.getZ(); z < startCoords.getZ() + 6; z++) {
				GenUtil.replaceWithBlockState(world, new Coords(x, coords.getY(), z), block.getDefaultState());
			}
		}
		return coords.add(0, 1, 0);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	public ICoords buildTrapLayer(final IWorld world, final Random random, final ICoords coords, final Block block) {
  	
		// spawn random registered mobs on either side of the chest
    	world.setBlockState(coords.add(-1, 0, 0).toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState(), 3);
    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getTileEntity(coords.add(-1, 0, 0).toPos());
    	EntityType<?> r = DungeonHooks.getRandomDungeonMob(random);
    	te.setMobName(r.getRegistryName());
    	te.setMobNum(new Quantity(2, 4));
    	te.setProximity(5D);
    	te.setSpawnRange(1.5D);
    	Treasure.LOGGER.debug("placed proximity spawner @ {}", coords.add(-1,0,0).toShortString());
    	
    	world.setBlockState(coords.add(1, 0, 0).toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState(), 3);
    	te = (ProximitySpawnerTileEntity) world.getTileEntity(coords.add(1, 0, 0).toPos());
    	r = DungeonHooks.getRandomDungeonMob(random);
    	te.setMobName(r.getRegistryName());
    	te.setMobNum(new Quantity(2, 4));
    	te.setProximity(5.5D);		// slightly larger proximity to fire first without entity collision
    	te.setSpawnRange(2D);
    	Treasure.LOGGER.debug("placed proximity spawner @ {}", coords.add(1,0,0).toShortString());
    	
		return coords;
	}
	
}