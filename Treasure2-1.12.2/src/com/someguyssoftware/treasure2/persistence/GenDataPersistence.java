/**
 * 
 */
package com.someguyssoftware.treasure2.persistence;

import java.util.Map.Entry;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.worldgen.ChestWorldGenerator;
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
		ChestWorldGenerator chestGen = (ChestWorldGenerator) Treasure.worldGenerators.get("chest");
		WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.worldGenerators.get("well");
		WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.worldGenerators.get("witherTree");
		
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
		
		///// Wells /////
		wellGen.setChunksSinceLastWell(treasureGen.getInteger("chunksSinceLastWell"));
		
		///// Wither Tree /////
		witherGen.setChunksSinceLastTree(treasureGen.getInteger("chunksSinceLastTree"));
		
//		NBTTagList registryTagList = tag.getTagList("registry", 10);
//		
//		// process each meta in the list
//		for (int i = 0; i < registryTagList.tagCount(); i++) {
//			NBTTagCompound infoTag = registryTagList.getCompoundTagAt(i);
//			
//			// retrieve data from NBT
//			int dx = infoTag.getInteger("x");
//			int dy = infoTag.getInteger("y");
//			int dz = infoTag.getInteger("z");
//			int minX = infoTag.getInteger("minX");
//			int minY = infoTag.getInteger("minY");
//			int minZ = infoTag.getInteger("minZ");
//			int maxX = infoTag.getInteger("maxX");
//			int maxY = infoTag.getInteger("maxY");
//			int maxZ = infoTag.getInteger("maxZ");			
//			int levels = infoTag.getInteger("levels");
//			String theme = infoTag.getString("theme");
//			String pattern = infoTag.getString("pattern");
//			String size = infoTag.getString("size");
//			String levelSize = infoTag.getString("levelSize");
//			String direction = infoTag.getString("direction");
//			int bossX = infoTag.getInteger("bossChestX");
//			int bossY = infoTag.getInteger("bossChestY");
//			int bossZ = infoTag.getInteger("bossChestZ");
//			
			try {		
//				// create a meta
//				DungeonInfo info = new DungeonInfo();
//				info.setCoords(new Coords(dx, dy, dz));
//				info.setMinX(minX);
//				info.setMinY(minY);
//				info.setMinZ(minZ);
//				info.setMaxX(maxX);
//				info.setMaxY(maxY);
//				info.setMaxZ(maxZ);
//				info.setLevels(levels);
//				info.setThemeName(theme);
//				if (!pattern.equals("")) info.setPattern(BuildPattern.valueOf(pattern));
//				if (!size.equals("")) info.setSize(BuildSize.valueOf(size));
//				if (!levelSize.equals("")) info.setLevelSize(BuildSize.valueOf(levelSize));
//				if (!direction.equals("")) info.setDirection(BuildDirection.valueOf(direction));
//				info.setBossChestCoords(new Coords(bossX, bossY, bossZ));
//			
//				// register the meta
//				DungeonRegistry.getInstance().register(info.getCoords().toShortString(), info);
			}
			catch(Exception e) {
				Treasure.logger.error(e);					
			}
//		}
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
			ChestWorldGenerator chestGen = (ChestWorldGenerator) Treasure.worldGenerators.get("chest");
			
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
			
			///// Wells ////
			// get the well world generator
			WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.worldGenerators.get("well");
			
			// add the chest gen last count to the treasure compound
			treasureGen.setInteger("chunksSinceLastWell", wellGen.getChunksSinceLastWell());
			
			//// Wither Tree /////
			WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.worldGenerators.get("witherTree");
			
			// add the chest gen last count to the treasure compound
			treasureGen.setInteger("chunksSinceLastTree", witherGen.getChunksSinceLastTree());
			
			// write the Dungeon Registry to NBT
//			NBTTagList registryTagList = new NBTTagList();
//			for (DungeonInfo info : DungeonRegistry.getInstance().getEntries()) {
//				NBTTagCompound infoTag = new NBTTagCompound();
//
//				if (info != null) {
//					if (info.getCoords() != null) {
//						infoTag.setInteger("x", info.getCoords().getX());
//						infoTag.setInteger("y", info.getCoords().getY());
//						infoTag.setInteger("z", info.getCoords().getZ());
//					}
//					infoTag.setInteger("minX", info.getMinX());
//					infoTag.setInteger("minY", info.getMinY());
//					infoTag.setInteger("minZ", info.getMinZ());
//					infoTag.setInteger("maxX", info.getMaxX());
//					infoTag.setInteger("maxY", info.getMaxY());
//					infoTag.setInteger("maxZ", info.getMaxZ());
//					
//					infoTag.setInteger("levels", info.getLevels());
//					if (info.getThemeName() != null) {
//						infoTag.setString("theme", info.getThemeName());
//					}
//					if (info.getPattern() != null) {
//						infoTag.setString("pattern", info.getPattern().name());
//					}
//					if (info.getSize() != null) {
//						infoTag.setString("size", info.getSize().name());
//					}
//					if (info.getLevelSize() != null) {
//						infoTag.setString("levelSize", info.getLevelSize().name());
//					}
//					if (info.getDirection() != null) {
//						infoTag.setString("direction", info.getDirection().name());
//					}
//					if (info.getBossChestCoords() != null) {
//						infoTag.setInteger("bossChestX", info.getBossChestCoords().getX());
//						infoTag.setInteger("bossChestY", info.getBossChestCoords().getY());
//						infoTag.setInteger("bossChestZ", info.getBossChestCoords().getZ());
//					}
//
//				}
//				
//				// add the poi to the list
//				registryTagList.appendTag(infoTag);
//			}
//			
//			// add the poi regsitry to the main tag
//			tag.setTag("registry", registryTagList);	
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
