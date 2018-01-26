/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.inventory.InventoryPopulator;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * TODO this needs to be Abstract
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class ChestGenerator {
	protected static int UNDERGROUND_OFFSET = 5;
	
	/**
	 * 
	 */
	public ChestGenerator() {}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param rarity
	 * @param config
	 * @return
	 */
	public boolean generate(World world, Random random, ICoords coords, Rarity rarity, IChestConfig config) {
		
		ICoords chestCoords = null;
		ICoords markerCoords = null;
		ICoords spawnCoords = null;
		boolean isGenerated = false;
		
		RandomHelper.checkProbability(new Random(), 80);
	
		// 1. determine y-coord of land for markers
		markerCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) return false; 
		
		// 2. switch on above or below ground
		if (config.isAboveGroundAllowed() && random.nextInt(2) == 1) {
			// set the chest coords to the surface pos
			chestCoords = new Coords(markerCoords);			
		}
		else if (config.isBelowGroundAllowed()) {
			// determine spawn coords below ground
			spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());
			if (spawnCoords == null) {
				return false;
			}
			
			// 3. build the pit
			isGenerated = SimplePitGenerator.generate(world, random, markerCoords, spawnCoords);
			
			// 4. build the room
			
		}
		else { return false; }

		// if successfully gen the pit
		if (isGenerated) {
			Treasure.logger.debug("isGenerated = TRUE");
			// most common property/rotator
			
			// TODO query for the rarity chest, get the items
			//===================
			// select a chest from the rarity (or category)
			List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(rarity);
			Block chest = chestList.get(random.nextInt(chestList.size()));			
			GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

			// get the backing tile entity of the chest 
			TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

			// if tile entity failed to create, remove the chest
			if (te == null || !(te instanceof TreasureChestTileEntity)) {
				// remove chest
				world.setBlockToAir(chestCoords.toPos());
				Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
				return false;
			}

			// select the loot container by rarity
			// TODO query to load the selected rarity chests
			List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(rarity);
			LootContainer container = null;
			if (containers != null && !containers.isEmpty()) {
				/*
				 * get a random container
				 */
				if (containers.size() == 1) {
					container = containers.get(0);
				}
				else {
					container = containers.get(random.nextInt(containers.size()));
				}
				
//					// load weighted collection with all containers
//					RandomWeightedCollection<LootContainer> weighted = new RandomWeightedCollection<>();
//					for (LootContainer c : containers) { weighted.add(c.getw, item);

				Treasure.logger.info("Chosen chest container:" + container);
			}
			else {
				// remove chest
			}
			// populate the chest with items
			InventoryPopulator pop = new InventoryPopulator();
			pop.populate(((TreasureChestTileEntity)te).getInventoryProxy(), container);

			// place markers (above chest or shaft)
//			GenUtil.placeMarkers(world, random, markerCoords.toBlockPos());
						
			Treasure.logger.info("CHEATER! {} chest at coords: {}", rarity, chestCoords.toShortString());
			return true;
		}		
		return false;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param pos
	 * @param spawnYMin
	 * @return
	 */
	public ICoords getUndergroundSpawnPos(World world, Random random, ICoords pos, int spawnYMin) {
		ICoords spawnPos = null;
		
		// spawn location under ground
		if (pos.getY() > (spawnYMin + UNDERGROUND_OFFSET)) {
			int ySpawn = random.nextInt(pos.getY()
					- (spawnYMin + UNDERGROUND_OFFSET))
					+ spawnYMin;
			
			spawnPos = new Coords(pos.getX(), ySpawn, pos.getZ());
			// get floor pos (if in a cavern or tunnel etc)
			spawnPos = WorldInfo.getDryLandSurfaceCoords(world, spawnPos);
		}
		return spawnPos;
	}
}
