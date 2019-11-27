/**
 * 
 */
package com.someguyssoftware.treasure2.persistence;

import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.GemOreWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WellWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WitherTreeWorldGenerator;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class GenDataPersistence extends WorldSavedData {
	public static final String GEN_DATA_KEY = "treasureGenData";
	
	/**
	 * Empty constructor
	 */
	public GenDataPersistence() {
		super(GEN_DATA_KEY);
	}
	
	/**
	 * 
	 * @param key
	 */
	public GenDataPersistence(String key) {
		super(key);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		Treasure.logger.debug("Loading Treasure! saved gen data...");

		// get the world generators
		ChestWorldGenerator chestGen = (ChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.CHEST);
		WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WELL);
		WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WITHER_TREE);
		GemOreWorldGenerator gemGen = (GemOreWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.GEM);
		
		// treasure
		NBTTagCompound treasureGen = tag.getCompoundTag("treasureGenerator");
		chestGen.setChunksSinceLastChest(treasureGen.getInteger("chunksSinceLastChest"));
		// load all the chunks since last rarity chest properites
		NBTTagList chunksSinceTagList = tag.getTagList("chunksSinceLastRarityChest", 10);
		for (int i = 0; i < chunksSinceTagList.tagCount(); i++) {
			NBTTagCompound chunkTag = chunksSinceTagList.getCompoundTagAt(i);
			int count = chunkTag.getInteger("count");
			String key = chunkTag.getString("key");
			chestGen.getChunksSinceLastRarityChest().put(Rarity.valueOf(key), count);
		}
		
		///// Well /////
		wellGen.setChunksSinceLastWell(treasureGen.getInteger("chunksSinceLastWell"));
		
		///// Wither Tree /////
		witherGen.setChunksSinceLastTree(treasureGen.getInteger("chunksSinceLastTree"));
		
		///// Gem Ore /////
		gemGen.setChunksSinceLastOre(treasureGen.getInteger("chunksSinceLastOre"));
		
		///// ChestConfig Registry /////
		ChestRegistry chestRegistry = ChestRegistry.getInstance();
		Treasure.logger.debug("ChestConfig Registry size before loading -> {}", chestRegistry.getValues().size());
		chestRegistry.clear();
		// load the chest registry
		NBTTagList chestRegistryTagList = tag.getTagList("chestRegistry", 10);
		for (int i = 0; i < chestRegistryTagList.tagCount(); i++) {
			NBTTagCompound chunkTag = chunksSinceTagList.getCompoundTagAt(i);
			String key = chunkTag.getString("key");
			String rarity = chunkTag.getString("rarity");
			NBTTagCompound coords = chunkTag.getCompoundTag("coords");
			int x = coords.getInteger("x");
			int y = coords.getInteger("y");
			int z = coords.getInteger("z");
			chestRegistry.register(key, new ChestInfo(Rarity.getByValue(rarity), new Coords(x, y, z)));
		}
		Treasure.logger.debug("ChestConfig Registry size after loading -> {}", chestRegistry.getValues().size());
	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {

		try {
			// create a treasure compound			
			NBTTagCompound treasureGen = new NBTTagCompound();
			
			///// Chests //////
			// get the chest world generator
			ChestWorldGenerator chestGen = (ChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.CHEST);
			
			// add the chest gen last count to the treasure compound
			treasureGen.setInteger("chunksSinceLastChest", chestGen.getChunksSinceLastChest());
			tag.setTag("treasureGenerator", treasureGen);
			
			NBTTagList chunksSinceTagList = new NBTTagList();
			for (Entry<Rarity, Integer> since : chestGen.getChunksSinceLastRarityChest().entrySet()) {
				NBTTagCompound entry = new NBTTagCompound();
				NBTTagString key = new NBTTagString(since.getKey().name());
				NBTTagInt count = new NBTTagInt(since.getValue());
				entry.setTag("key", key);
				entry.setTag("count", count);
				
				// add entry to list
				chunksSinceTagList.appendTag(entry);
			}
			treasureGen.setTag("chunksSinceLastRarityChest", chunksSinceTagList);
			
			///// Well ////
			// get the well world generator
			WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WELL);
			
			// add the chest gen last count to the treasure compound
			treasureGen.setInteger("chunksSinceLastWell", wellGen.getChunksSinceLastWell());
			
			//// Wither Tree /////
			WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WITHER_TREE);
			
			// add the chest gen last count to the treasure compound
			treasureGen.setInteger("chunksSinceLastTree", witherGen.getChunksSinceLastTree());
			
			//// Gem Ore ////
			GemOreWorldGenerator gemGen = (GemOreWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.GEM);
			treasureGen.setInteger("chunksSinceLastOre", gemGen.getChunksSinceLastOre());
			
			///// ChestConfig Registry /////
			NBTTagList chestRegistryTagList = new NBTTagList();
			ChestRegistry chestRegistry = ChestRegistry.getInstance();
			for (ChestInfo element : chestRegistry.getValues()) {
				NBTTagCompound entry = new NBTTagCompound();
				NBTTagString key = new NBTTagString(element.getCoords().toShortString());
				NBTTagString rarity = new NBTTagString(element.getRarity().getValue());
				NBTTagCompound coords = new NBTTagCompound();
				NBTTagInt x = new NBTTagInt(element.getCoords().getX());
				NBTTagInt y = new NBTTagInt(element.getCoords().getY());
				NBTTagInt z = new NBTTagInt(element.getCoords().getZ());
				
				coords.setTag("x", x);
				coords.setTag("y", y);
				coords.setTag("z", z);
				
				entry.setTag("key", key);
				entry.setTag("rarity", rarity);
				entry.setTag("coords", coords);
				
				// add entry to list
				chestRegistryTagList.appendTag(entry);
			}
			treasureGen.setTag("chestRegistry", chestRegistryTagList);
		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.logger.error("An exception occurred:", e);
		}
		
		return tag;
	}

	/**
	 * NOTE world.loadItemData is cached to a HashMap, so you don't have to worry about performing too many get()s that read from the disk.
	 * @param world
	 * @return
	 */
	public static GenDataPersistence get(World world) {
		
		GenDataPersistence data = (GenDataPersistence)world.loadData(GenDataPersistence.class, GEN_DATA_KEY);
		
		if (data == null) {
			data = new GenDataPersistence();
			world.setData(GEN_DATA_KEY, data);
		}
		return data;
	}
}
