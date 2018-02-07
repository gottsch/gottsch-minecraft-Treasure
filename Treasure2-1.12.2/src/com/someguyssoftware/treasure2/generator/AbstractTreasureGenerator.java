/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.inventory.InventoryPopulator;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 1, 2018
 *
 */
public abstract class AbstractTreasureGenerator {
	protected static int UNDERGROUND_OFFSET = 5;
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chestRarity
	 * @param config
	 * @return
	 */
	public boolean generate(World world, Random random, ICoords coords, Rarity chestRarity, IChestConfig config) {
		
		ICoords chestCoords = null;
		ICoords markerCoords = null;
		ICoords spawnCoords = null;
		boolean isGenerated = false;
		
		RandomHelper.checkProbability(random, config.getGenProbability());
	
		// 1. determine y-coord of land for markers
		markerCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		Treasure.logger.debug("Marker Coords @ {}", markerCoords.toShortString());
		if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.logger.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return false; 
		}
		
		Treasure.logger.debug("Test for above/below");
		// 2. switch on above or below ground
		if (config.isAboveGroundAllowed() && random.nextInt(2) == 1) {
			// set the chest coords to the surface pos
			chestCoords = new Coords(markerCoords);
			Treasure.logger.debug("Above ground @ {}", chestCoords.toShortString());
		}
		else if (config.isBelowGroundAllowed()) {
			// determine spawn coords below ground
			spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());
			Treasure.logger.debug("Below ground @ {}", spawnCoords.toShortString());
			if (spawnCoords == null) {
				return false;
			}
			
			// TODO select a pit generator
			// 3. build the pit
			isGenerated = SimplePitGenerator.generate(world, random, markerCoords, spawnCoords);
			Treasure.logger.debug("Is pit generated: {}", isGenerated);
			// 4. build the room
			
			// 5. update the chest coords
			chestCoords = new Coords(spawnCoords);
		}
		else { return false; }

		// if successfully gen the pit
		if (isGenerated) {
			Treasure.logger.debug("isGenerated = TRUE");
			
			LootContainer container = selectContainer(random, chestRarity);
			if (container == null || container == LootContainer.EMPTY_CONTAINER) {
				Treasure.logger.warn("Unable to select a container.");
				return false;
			}
			
			// select a chest from the rarity
			TreasureChestBlock chest = selectChest(random, chestRarity);	
			if (chest == null) {
				Treasure.logger.warn("Unable to select a chest for rarite {} and container {}.", chestRarity, container.getName());
				return false;
			}
			Treasure.logger.debug("Choosen chest: {}", chest.getUnlocalizedName());
			
			// place the chest in the world
			TileEntity te = placeInWorld(world, random, chest, chestCoords);
			if (te == null) return false;

//			// select the loot container by rarity
//			// TODO add dao methods to get by rarity list, category list, or name
//			List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(chestRarity);
//			LootContainer container = null;
//			if (containers != null && !containers.isEmpty()) {
//				/*
//				 * get a random container
//				 */
//				if (containers.size() == 1) {
//					container = containers.get(0);
//				}
//				else {
//					container = containers.get(random.nextInt(containers.size()));
//				}
//				
////					// load weighted collection with all containers
////					RandomWeightedCollection<LootContainer> weighted = new RandomWeightedCollection<>();
////					for (LootContainer c : containers) { weighted.add(c.getw, item);
//
//				Treasure.logger.info("Chosen chest container:" + container);
//			}
//			else {
//				// remove chest
//			}
			
			// populate the chest with items
			InventoryPopulator pop = new InventoryPopulator();
			pop.populate(((TreasureChestTileEntity)te).getInventoryProxy(), container);

			// add locks
			addLocks(random, chest, (TreasureChestTileEntity)te, chestRarity);
			
			// place markers (above chest or shaft)
			GenUtil.placeMarkers(world, random, markerCoords.toPos());
						
			Treasure.logger.info("CHEATER! {} chest at coords: {}", chestRarity, chestCoords.toShortString());
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
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(rarity);
		TreasureChestBlock chest = (TreasureChestBlock) chestList.get(RandomHelper.randomInt(random, 0, chestList.size()-1));	
		return chest;
	}
	
	/**
	 * v1.0 - Currently chests aren't mapped by Category
	 * @param random
	 * @param category
	 * @return
	 */
	public TreasureChestBlock  selectChest(final Random random, final Category category) {
//		List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(chestRarity);
//		TreasureChestBlock chest = (TreasureChestBlock) chestList.get(random.nextInt(chestList.size()));	
//		return chest;
		return null;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	public TileEntity placeInWorld(World world, Random random, TreasureChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof TreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
	
	/**
	 * 
	 * @param chestRarity
	 * @return
	 */
	public LootContainer selectContainer(Random random, final Rarity chestRarity) {
		LootContainer container = LootContainer.EMPTY_CONTAINER;
		// select the loot container by rarity
		List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(chestRarity);
		if (containers != null && !containers.isEmpty()) {
			/*
			 * get a random container
			 */
			if (containers.size() == 1) {
				container = containers.get(0);
			}
			else {
				container = containers.get(RandomHelper.randomInt(random, 0, containers.size()-1));
			}
			Treasure.logger.info("Chosen chest container:" + container);
		}
		return container;
	}
	
	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * @param chest
	 */
	public void addLocks(Random random, TreasureChestBlock chest, TreasureChestTileEntity te, Rarity rarity) {
		// get the chest type
		TreasureChestType type = chest.getChestType();
		// get the lock states
		List<LockState> lockStates = te.getLockStates();	
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 0, type.getMaxLocks());
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		for (int i = 0; i < numLocks; i++) {
			// select a rarity lock
			List<LockItem> locks = (List<LockItem>) TreasureItems.locks.get(rarity);
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size()-1));
			Treasure.logger.debug("adding lock: {}", lock);
			// add the lock to the chest
			lockStates.get(i).setLock(lock);				
		}
	}
}
