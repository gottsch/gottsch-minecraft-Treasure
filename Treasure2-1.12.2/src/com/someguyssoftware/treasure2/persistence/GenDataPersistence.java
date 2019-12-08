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
import com.someguyssoftware.treasure2.worldgen.GemOreWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SubmergedChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;
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
	private static final String TREASURE_GEN_TAG_NAME = "treasureGenerator";
	
	public static final String GEN_DATA_KEY = "treasureGenData";

	private static final String SURFACE_CHEST_GEN_TAG_NAME = "surfaceChestGen";
	private static final String SUBMERGED_CHEST_GEN_TAG_NAME = "submergedChestGen";
	private static final String KEY_TAG_NAME = "key";
	private static final String COUNT_TAG_NAME = "count";
	private static final String CHUNKS_SINCE_LAST_CHEST_TAG_NAME = "chunksSinceLastChest";
	private static final String CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME = "chunksSinceLastRarityChest";
	private static final String CHEST_REGISTRY_TAG_NAME = "chestRegistry";
	private static final String COORDS_TAG_NAME = "coords";

	private static final String RARITY_TAG_NAME = "rarity";
	
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
		SurfaceChestWorldGenerator surfaceChestGen = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SURFACE_CHEST);
		SubmergedChestWorldGenerator submergedChestGen = (SubmergedChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SUBMERGED_CHEST);
		WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WELL);
		WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.WITHER_TREE);
		GemOreWorldGenerator gemGen = (GemOreWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.GEM);
		
		// treasure
		NBTTagCompound treasureGen = tag.getCompoundTag(TREASURE_GEN_TAG_NAME);
		NBTTagCompound surfaceTag = treasureGen.getCompoundTag(SURFACE_CHEST_GEN_TAG_NAME);
		NBTTagCompound submergedTag = treasureGen.getCompoundTag(SUBMERGED_CHEST_GEN_TAG_NAME);
	
		///// Chests /////
		/// Surface Chests ///
		if (surfaceTag != null) {
			surfaceChestGen.setChunksSinceLastChest(surfaceTag.getInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME));
			NBTTagList chunksSinceTagList = surfaceTag.getTagList(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, 10);
			// load all the chunks since last rarity chest properites
			for (int i = 0; i < chunksSinceTagList.tagCount(); i++) {
				NBTTagCompound chunkTag = chunksSinceTagList.getCompoundTagAt(i);
				int count = chunkTag.getInteger(COUNT_TAG_NAME);
				String key = chunkTag.getString(KEY_TAG_NAME);
				surfaceChestGen.getChunksSinceLastRarityChest().put(Rarity.valueOf(key), count);
			}
		}
		
		/// Submerged Chests ///
		if (submergedTag != null) {
			submergedChestGen.setChunksSinceLastChest(submergedTag.getInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME));
			NBTTagList chunksSinceTagList = submergedTag.getTagList(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, 10);
			// load all the chunks since last rarity chest properites
			for (int i = 0; i < chunksSinceTagList.tagCount(); i++) {
				NBTTagCompound chunkTag = chunksSinceTagList.getCompoundTagAt(i);
				int count = chunkTag.getInteger(COUNT_TAG_NAME);
				String key = chunkTag.getString(KEY_TAG_NAME);
				submergedChestGen.getChunksSinceLastRarityChest().put(Rarity.valueOf(key), count);
			}
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
		NBTTagList chestRegistryTagList = treasureGen.getTagList(CHEST_REGISTRY_TAG_NAME, 10);
		for (int i = 0; i < chestRegistryTagList.tagCount(); i++) {
			NBTTagCompound chunkTag = chestRegistryTagList.getCompoundTagAt(i);
			String key = chunkTag.getString(KEY_TAG_NAME);
			String rarity = chunkTag.getString(RARITY_TAG_NAME);
			NBTTagCompound coords = chunkTag.getCompoundTag(COORDS_TAG_NAME);
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
			
			// add main treasure tag
			tag.setTag(TREASURE_GEN_TAG_NAME, treasureGen);
			
			///// Chests //////
			// get the chest world generators
			SurfaceChestWorldGenerator surfaceChestGen = (SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SURFACE_CHEST);
			SubmergedChestWorldGenerator submergedChestGen = (SubmergedChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGenerators.SUBMERGED_CHEST);
			
			// create a new compounds
			NBTTagCompound surfaceTag = new NBTTagCompound();
			NBTTagCompound submergedTag = new NBTTagCompound();
			
			/// Surface Chests ///
			// add the surface chest gen last count to the treasure compound
			surfaceTag.setInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME, surfaceChestGen.getChunksSinceLastChest());			
			
			// TODO these 2 blocks could become one method
			NBTTagList chunksSinceTagList = new NBTTagList();
			for (Entry<Rarity, Integer> since : surfaceChestGen.getChunksSinceLastRarityChest().entrySet()) {
				NBTTagCompound entry = new NBTTagCompound();
				NBTTagString key = new NBTTagString(since.getKey().name());
				NBTTagInt count = new NBTTagInt(since.getValue());
				entry.setTag(KEY_TAG_NAME, key);
				entry.setTag(COUNT_TAG_NAME, count);				
				// add entry to list
				chunksSinceTagList.appendTag(entry);
			}
			surfaceTag.setTag(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, chunksSinceTagList);
			
			/// Submerged Chests ///
			// add the submerged chest gen last count to the treasure compound
			submergedTag.setInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME, submergedChestGen.getChunksSinceLastChest());			
			chunksSinceTagList = new NBTTagList();
			for (Entry<Rarity, Integer> since : submergedChestGen.getChunksSinceLastRarityChest().entrySet()) {
				NBTTagCompound entry = new NBTTagCompound();
				NBTTagString key = new NBTTagString(since.getKey().name());
				NBTTagInt count = new NBTTagInt(since.getValue());
				entry.setTag(KEY_TAG_NAME, key);
				entry.setTag(COUNT_TAG_NAME, count);				
				// add entry to list
				chunksSinceTagList.appendTag(entry);
			}			
			submergedTag.setTag(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, chunksSinceTagList);
			
			// add chest gen tags to main tag
			treasureGen.setTag(SURFACE_CHEST_GEN_TAG_NAME, surfaceTag);
			treasureGen.setTag(SUBMERGED_CHEST_GEN_TAG_NAME, submergedTag);
						
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
				
				entry.setTag(KEY_TAG_NAME, key);
				entry.setTag(RARITY_TAG_NAME, rarity);
				entry.setTag(COORDS_TAG_NAME, coords);
				
				// add entry to list
				chestRegistryTagList.appendTag(entry);
			}
			treasureGen.setTag(CHEST_REGISTRY_TAG_NAME, chestRegistryTagList);
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
