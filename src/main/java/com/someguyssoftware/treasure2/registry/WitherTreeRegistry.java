/**
 * 
 */
package com.someguyssoftware.treasure2.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

/**
 * 
 * @author Mark Gottschling on Mar 5, 2021
 *
 */
public class WitherTreeRegistry {
	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";
	private static final String WITHER_TREE_REGISTRY_TAG_NAME ="witherTreeRegistry";
	private static final String BIOME_ID_TAG_NAME = "biomeID";
	private static final String COORDS_TAG_NAME = "coords";
	
	private static WitherTreeRegistry instance = new WitherTreeRegistry();
	private Map<Integer, LinkedList<WitherTreeInfo>> registry;
	
	/**
	 * 
	 */
	private WitherTreeRegistry() {
		registry = new HashMap<>();
	}
	
	/**
	 * 
	 * @return
	 */
	public static WitherTreeRegistry getInstance() {
		return instance;
	}

	/**
	 * 
	 * @param dimensionID
	 * @param coords
	 * @param biomeID
	 */
	public void register(Integer dimensionID, ICoords coords, Integer biomeID) {
		register(dimensionID, new WitherTreeInfo(coords, dimensionID, biomeID));
	}
	
	/**
	 * Registers a WitherTreeInfo with a key.
	 * @param key
	 * @param info
	 */
	private synchronized void register(final Integer dimensionID, final WitherTreeInfo info) {
		Treasure.logger.debug("Registering wither tree in dimension -> {}, info -> {}", dimensionID, info);
		// get the registry for the dimension
		if (!registry.containsKey(dimensionID)) {
			registry.put(dimensionID, new LinkedList<>());
		}
		
		LinkedList<WitherTreeInfo> witherTrees = registry.get(dimensionID);
		// test the size		
		if (witherTrees.size() >= TreasureConfig.WITHER_TREE.witherTreeRegistrySize) {
			// remove the first element
			unregister(dimensionID);
		}
		
		// register
		witherTrees.add(info);
	}
	
	/**
	 * 
	 * @param key
	 */
	public synchronized void unregister(final Integer dimensionID) {
		if (registry.containsKey(dimensionID)) {
			LinkedList<WitherTreeInfo> witherTrees = registry.get(dimensionID);
			// remove the head
			if (witherTrees.size() > 0) {
				witherTrees.pop();
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public WitherTreeInfo get(final Integer dimensionID, final int index) {
		WitherTreeInfo info = null;
		if (registry.containsKey(dimensionID)) {
			LinkedList<WitherTreeInfo> list = registry.get(dimensionID);
			if (index < list.size()) {
				info = list.get(index);
			}
		}
		return info;
	}
	
	/**
	 * 
	 * @return
	 */
	public Set<Integer> getDimensionKeys() {
		return registry.keySet();
	}
	
	/**
	 * This will not update parent collection.
	 * @param dimensionID
	 * @return
	 */
	public LinkedList<WitherTreeInfo> getDimensionEntry(final Integer dimensionID) {
		return registry.get(dimensionID);
	}
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	public List<WitherTreeInfo> getValues(final Integer dimensionID) {
		if (registry.containsKey(dimensionID)) {
			return new LinkedList<>(registry.get(dimensionID));
		}
		return new ArrayList<>();
	}
	
	/**
	 * 
	 */
	public void clear() {
		registry.clear();
	}
	
	/**
	 * 
	 * @param tag
	 */
	public void read(NBTTagCompound tag) {
		clear();
		NBTTagList witherTreeRegistryDimensionTagList = tag.getTagList(WITHER_TREE_REGISTRY_TAG_NAME, 10);
		for (int index = 0; index < witherTreeRegistryDimensionTagList.tagCount(); index++) {
			NBTTagCompound dimTag = witherTreeRegistryDimensionTagList.getCompoundTagAt(index);
			int dimensionID = dimTag.getInteger(DIMENSION_ID_TAG_NAME);
			NBTTagList infoTagList = dimTag.getTagList("infoList", 10);
			for (int infoIndex = 0; infoIndex < infoTagList.tagCount(); infoIndex++) {
				NBTTagCompound infoTag = infoTagList.getCompoundTagAt(infoIndex);
				int biomeID = infoTag.getInteger(BIOME_ID_TAG_NAME);
				NBTTagCompound coordsTag = infoTag.getCompoundTag(COORDS_TAG_NAME);
				int x = coordsTag.getInteger("x");
				int y = coordsTag.getInteger("y");
				int z = coordsTag.getInteger("z");
				register(dimensionID, new WitherTreeInfo(new Coords(x, y, z), dimensionID, biomeID));
			}
		}	
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	public NBTTagCompound write(NBTTagCompound tag) {
		
		NBTTagList witherTreeRegistryDimensionTagList = new NBTTagList();
		for (Integer dimensionKey : getDimensionKeys()) {
			LinkedList<WitherTreeInfo> infoList = getDimensionEntry(dimensionKey);
			NBTTagCompound dimTag = new NBTTagCompound();
			dimTag.setInteger(DIMENSION_ID_TAG_NAME, dimensionKey);
			NBTTagList infoTagList = new NBTTagList();
			for(WitherTreeInfo info : infoList) {
				NBTTagCompound infoTag = new NBTTagCompound();					
				NBTTagInt biomeID = new NBTTagInt(info.getBiomeID());
				NBTTagCompound coords = new NBTTagCompound();
				NBTTagInt x = new NBTTagInt(info.getCoords().getX());
				NBTTagInt y = new NBTTagInt(info.getCoords().getY());
				NBTTagInt z = new NBTTagInt(info.getCoords().getZ());
				
				coords.setTag("x", x);
				coords.setTag("y", y);
				coords.setTag("z", z);

				infoTag.setTag(BIOME_ID_TAG_NAME, biomeID);
				infoTag.setTag(COORDS_TAG_NAME, coords);
				
				infoTagList.appendTag(infoTag);
			}
			dimTag.setTag("infoList", infoTagList);
			witherTreeRegistryDimensionTagList.appendTag(dimTag);
		}
		// delete current tag
		tag.removeTag(WITHER_TREE_REGISTRY_TAG_NAME);
		// add new values
		tag.setTag(WITHER_TREE_REGISTRY_TAG_NAME, witherTreeRegistryDimensionTagList);
		
		return tag;		
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Mar 5, 2021
	 *
	 */
	public class WitherTreeInfo {
		private ICoords coords;
		private Integer dimensionID;
		private Integer biomeID;
		
		/**
		 * 
		 */
		public WitherTreeInfo() {}

		/**
		 * 
		 * @param coords
		 * @param dimensionID
		 * @param biomeID
		 */
		public WitherTreeInfo(ICoords coords, Integer dimensionID, Integer biomeID) {
			super();
			this.coords = coords;
			this.dimensionID = dimensionID;
			this.biomeID = biomeID;
		}

		public ICoords getCoords() {
			return coords;
		}

		public void setCoords(ICoords coords) {
			this.coords = coords;
		}

		public Integer getDimensionID() {
			return dimensionID;
		}

		public void setDimensionID(Integer dimensionID) {
			this.dimensionID = dimensionID;
		}

		public Integer getBiomeID() {
			return biomeID;
		}

		public void setBiomeID(Integer biomeID) {
			this.biomeID = biomeID;
		}
	}
}
