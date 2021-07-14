/**
 * 
 */
package com.someguyssoftware.treasure2.persistence;

import java.util.Map;
import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.world.gen.feature.TreasureFeatures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class TreasureGenerationSavedData extends WorldSavedData {	
	private static final String TREASURE_GEN_TAG_NAME = "treasureGenerator";
	
	public static final String GEN_DATA_KEY = Treasure.MODID + ":generationData";

	private static final String KEY_TAG_NAME = "key";
//	private static final String COUNT_TAG_NAME = "count";
//	private static final String CHUNKS_SINCE_LAST_CHEST_TAG_NAME = "chunksSinceLastChest";
//	private static final String CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME = "chunksSinceLastRarityChest";
	private static final String CHEST_REGISTRY_TAG_NAME = "chestRegistry";
	private static final String COORDS_TAG_NAME = "coords";

	private static final String RARITY_TAG_NAME = "rarity";
	private static final String RARITIES_TAG_NAME = "rarities";
	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";
	private static final String DIMENSIONS_TAG_NAME = "dimensions";
	private static final String BIOME_ID_TAG_NAME = "biomeID";
	private static final String BIOMES_TAG_NAME = "biomes";
	
	// New tags names
	private static final String CHUNKS_SINCE_LAST_FEATURE_TAG_NAME = "chunksSinceLastFeature";
	private static final String CHUNKS_SINCE_LAST_RARITY_FEATURE_TAG_NAME = "chunksSinceLastRarityFeature";
	
	/**
	 * Empty constructor
	 */
	public TreasureGenerationSavedData() {
		super(GEN_DATA_KEY);
	}
	
	/**
	 * 
	 * @param key
	 */
	public TreasureGenerationSavedData(String key) {
		super(key);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void load(CompoundNBT tag) {
		Treasure.LOGGER.debug("loading treasure2 saved gen data...");

		// treasure generation tag
		CompoundNBT genTag = tag.getCompound(TREASURE_GEN_TAG_NAME);

		///// Chests //////
		// for each feature
		TreasureFeatures.PERSISTED_FEATURES.forEach(feature -> {
			Treasure.LOGGER.debug("loading feature -> {}", ((IForgeRegistryEntry)feature).getRegistryName().toString());
			CompoundNBT featureTag = genTag.getCompound(((IForgeRegistryEntry)feature).getRegistryName().toString());
			if (featureTag != null) {
				ListNBT dimTagList = featureTag.getList(DIMENSIONS_TAG_NAME, 10);
				dimTagList.forEach(dimTag -> {
					String dimensionID = ((CompoundNBT) dimTag).getString(DIMENSION_ID_TAG_NAME);
					Treasure.LOGGER.debug("loading dimension -> {}", dimensionID);
					int chunksSince = ((CompoundNBT) dimTag).getInt(CHUNKS_SINCE_LAST_FEATURE_TAG_NAME);
					feature.getChunksSinceLastDimensionFeature().put(dimensionID, chunksSince);
					Treasure.LOGGER.debug("loading chunks since -> {}", chunksSince);
					ListNBT rarityTagList = ((CompoundNBT) dimTag).getList(RARITIES_TAG_NAME, 10);
					if (rarityTagList != null && !rarityTagList.isEmpty()) {
						Treasure.LOGGER.debug("rarity tag list size -> {}", rarityTagList.size());
						for (int index = 0; index < rarityTagList.size(); index++) {
							CompoundNBT rarityTag = rarityTagList.getCompound(index);
							String rarityID = rarityTag.getString(RARITY_TAG_NAME);
							Treasure.LOGGER.debug("loading rarity ID -> {}", rarityID);
							Rarity rarity = Rarity.getByValue(rarityID);
							if (rarity != null) {
								int chunksSinceRarityFeature = rarityTag.getInt(CHUNKS_SINCE_LAST_RARITY_FEATURE_TAG_NAME);
								Treasure.LOGGER.debug("loading chunks since last rarity -> {} chunks -> {}", rarity, chunksSinceRarityFeature);
								if (feature.getChunksSinceLastDimensionRarityFeature().containsKey(dimensionID)) {
									feature.getChunksSinceLastDimensionRarityFeature().get(dimensionID).put(rarity, chunksSinceRarityFeature);
								}
							}
						}
					}					
				});
			}
		});
		
        ///// ChestConfig Registry /////
        ListNBT chestRegistriesTag = genTag.getList("chestRegistries", 10);
        if (chestRegistriesTag != null) {
        	Treasure.LOGGER.debug("loading chest registries...");
            chestRegistriesTag.forEach(dimTag -> {
                String dimensionID = ((CompoundNBT) dimTag).getString(DIMENSION_ID_TAG_NAME);
                Treasure.LOGGER.debug("loading dimension -> {}", dimensionID);
                // get the registry
                ListNBT registries = ((CompoundNBT) dimTag).getList(CHEST_REGISTRY_TAG_NAME, 10);
                registries.forEach(registryTag -> {
                    String key = ((CompoundNBT)registryTag).getString(KEY_TAG_NAME);
                    String rarity = ((CompoundNBT)registryTag).getString(RARITY_TAG_NAME);
                    CompoundNBT coords = ((CompoundNBT)registryTag).getCompound(COORDS_TAG_NAME);
                    int x = coords.getInt("x");
                    int y = coords.getInt("y");
                    int z = coords.getInt("z");
                    Treasure.LOGGER.debug("loading registry entry -> k:{} r:{} x:{} y:{} z:{}", key, rarity, x, y, z);
                    TreasureData.CHEST_REGISTRIES.get(dimensionID).register(key, new ChestInfo(Rarity.getByValue(rarity), new Coords(x, y, z)));
                });
                    
            });
        }
//		
//		///// Well /////
//		wellGen.setChunksSinceLastWell(treasureGen.getInteger("chunksSinceLastWell"));
//		
//		///// Wither Tree /////
//		witherGen.setChunksSinceLastTree(treasureGen.getInteger("chunksSinceLastTree"));

	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public CompoundNBT save(CompoundNBT tag) {
		try {
			// create a treasure compound			
			CompoundNBT genTag = new CompoundNBT();
			
			// add main treasure tag
			tag.put(TREASURE_GEN_TAG_NAME, genTag);
			
			///// Features //////
			TreasureFeatures.PERSISTED_FEATURES.forEach(feature -> {
				Treasure.LOGGER.debug("saving feature -> {}", ((IForgeRegistryEntry)feature).getRegistryName().toString());
				CompoundNBT featureTag = new CompoundNBT();
				// add the feature gen last count to the treasure compound for each dimension
				ListNBT dimTagList = new ListNBT();
				for (Entry<String, Integer> entry : feature.getChunksSinceLastDimensionFeature().entrySet()) {
					Treasure.LOGGER.debug("saving feature dimension ID -> {}", entry.getKey());
					
					CompoundNBT dimensionTag = new CompoundNBT();
					dimensionTag.putString(DIMENSION_ID_TAG_NAME, entry.getKey());
					dimensionTag.putInt(CHUNKS_SINCE_LAST_FEATURE_TAG_NAME, entry.getValue());
					Treasure.LOGGER.debug("saving chunks since last feature -> {}", entry.getValue());
					
					// get the rarity map
					if (feature.getChunksSinceLastDimensionRarityFeature() != null && feature.getChunksSinceLastDimensionRarityFeature().containsKey(entry.getKey())) {
						Map<Rarity, Integer> rarityMap = feature.getChunksSinceLastDimensionRarityFeature().get(entry.getKey());
						Treasure.LOGGER.debug("saving rarity map size -> {}", rarityMap.size());
						
						ListNBT rarityTagList = new ListNBT();
						for (Entry<Rarity, Integer> rarityEntry : rarityMap.entrySet()) {
							CompoundNBT rarityTag = new CompoundNBT();
							rarityTag.putString(RARITY_TAG_NAME, rarityEntry.getKey().getValue());
							rarityTag.putInt(CHUNKS_SINCE_LAST_RARITY_FEATURE_TAG_NAME, rarityEntry.getValue());
							Treasure.LOGGER.debug("saving chunks since last rarity -> {} ({}) chunks -> {}", rarityEntry.getKey(), rarityEntry.getKey().getValue(), rarityEntry.getValue());
							rarityTagList.add(rarityTag);
						}						
						dimensionTag.put(RARITIES_TAG_NAME, rarityTagList);
					}
					dimTagList.add(dimensionTag);
				}
				
				featureTag.put(DIMENSIONS_TAG_NAME, dimTagList);
				genTag.put(((IForgeRegistryEntry)feature).getRegistryName().toString(), featureTag);
			});
			
			///// ChestConfig Registry /////
			Treasure.LOGGER.debug("saving chest registry");
			ListNBT chestRegistries = new ListNBT();
			TreasureData.CHEST_REGISTRIES.entrySet().forEach(entry -> {
				String dimensionName = entry.getKey();
				ChestRegistry registry = entry.getValue();
				CompoundNBT dimTag = new CompoundNBT();
				dimTag.putString(DIMENSION_ID_TAG_NAME, dimensionName);
				Treasure.LOGGER.debug("saving chest dimension ID -> {}", dimensionName);
				ListNBT chestRegistryTagList = new ListNBT();
				registry.getValues().forEach(chestInfo -> {
					CompoundNBT chestInfoEntry = new CompoundNBT();
					CompoundNBT coords = new CompoundNBT();
					coords.putInt("x", chestInfo.getCoords().getX());
					coords.putInt("y", chestInfo.getCoords().getY());
					coords.putInt("z", chestInfo.getCoords().getZ());
					
					chestInfoEntry.putString(KEY_TAG_NAME, chestInfo.getCoords().toShortString());
					chestInfoEntry.putString(RARITY_TAG_NAME, chestInfo.getRarity().getValue());
					chestInfoEntry.put(COORDS_TAG_NAME, coords);
//					Treasure.LOGGER.debug("saving chest key -> {}", chestInfo.getCoords().toShortString());
//					Treasure.LOGGER.debug("saving chest rarity -> {}", chestInfo.getRarity().getValue());
//					Treasure.LOGGER.debug("saving chest coords -> {} {} {}", chestInfo.getCoords().getX(), chestInfo.getCoords().getY(), chestInfo.getCoords().getZ());
					// add entry to list
					chestRegistryTagList.add(chestInfoEntry);
				});
				dimTag.put(CHEST_REGISTRY_TAG_NAME, chestRegistryTagList);
				chestRegistries.add(dimTag);
			});
			// delete current tag
			genTag.remove("chestRegistries");
			// add new values
			genTag.put("chestRegistries", chestRegistries);

//			/// Submerged Chests ///
//			// add the submerged chest gen last count to the treasure compound
//			submergedTag.setInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME, submergedChestGen.getChunksSinceLastChest());			
//			chunksSinceTagList = new NBTTagList();
//			for (Entry<Rarity, Integer> since : submergedChestGen.getChunksSinceLastRarityChest().entrySet()) {
//				NBTTagCompound entry = new NBTTagCompound();
//				NBTTagString key = new NBTTagString(since.getKey().name());
//				NBTTagInt count = new NBTTagInt(since.getValue());
//				entry.setTag(KEY_TAG_NAME, key);
//				entry.setTag(COUNT_TAG_NAME, count);				
//				// add entry to list
//				chunksSinceTagList.appendTag(entry);
//			}			
//			submergedTag.setTag(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, chunksSinceTagList);
//			
//			// add chest gen tags to main tag
//			treasureGen.setTag(SURFACE_CHEST_GEN_TAG_NAME, surfaceTag);
//			treasureGen.setTag(SUBMERGED_CHEST_GEN_TAG_NAME, submergedTag);
//			


//			//// Wither Tree /////
//			WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.WITHER_TREE);
//			
//			// add the chest gen last count to the treasure compound
//			treasureGen.setInteger("chunksSinceLastTree", witherGen.getChunksSinceLastTree());

		}
		catch(Exception e) {
			e.printStackTrace();
			Treasure.LOGGER.error("An exception occurred:", e);
		}
//		
		return tag;
	}

	/**
	 * @param world
	 * @return
	 */
	public static TreasureGenerationSavedData get(IWorld world) {
		DimensionSavedDataManager storage = ((ServerWorld)world).getDataStorage();
		TreasureGenerationSavedData data = (TreasureGenerationSavedData) storage.computeIfAbsent(TreasureGenerationSavedData::new, GEN_DATA_KEY);
		
		if (data == null) {
			data = new TreasureGenerationSavedData();
			storage.set(data);
		}
		return data;
	}
}