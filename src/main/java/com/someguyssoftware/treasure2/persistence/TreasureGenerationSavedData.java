/**
 * 
 */
package com.someguyssoftware.treasure2.persistence;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import com.google.common.collect.ListMultimap;
import com.mojang.datafixers.types.templates.CompoundList;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.registry.ChestRegistry;
import com.someguyssoftware.treasure2.world.gen.feature.SurfaceChestFeature;
import com.someguyssoftware.treasure2.world.gen.feature.TreasureFeatures;

import net.minecraft.item.crafting.Ingredient.TagList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapData;
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

//	private static final String SURFACE_CHEST_GEN_TAG_NAME = "surfaceChestGen";
//	private static final String SUBMERGED_CHEST_GEN_TAG_NAME = "submergedChestGen";
//	private static final String OASIS_GEN_TAG_NAME = "oasisGen";
	private static final String KEY_TAG_NAME = "key";
//	private static final String COUNT_TAG_NAME = "count";
//	private static final String CHUNKS_SINCE_LAST_CHEST_TAG_NAME = "chunksSinceLastChest";
//	private static final String CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME = "chunksSinceLastRarityChest";
	private static final String CHEST_REGISTRY_TAG_NAME = "chestRegistry";
//	private static final String OASIS_REGISTRY_TAG_NAME = "oasisRegistry";
//	private static final String CHUNKS_SINCE_LAST_OASIS_TAG_NAME = "chunksSinceLastOasis";
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
	public void read(CompoundNBT tag) {
		Treasure.LOGGER.debug("loading treasure2 saved gen data...");

		// treasure generation tag
		CompoundNBT genTag = tag.getCompound(TREASURE_GEN_TAG_NAME);

		///// Chests //////
		// for each feature
		TreasureFeatures.FEATURES.forEach(feature -> {
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
					for (int index = 0; index < rarityTagList.size(); index++) {
						CompoundNBT rarityTag = dimTagList.getCompound(index);
						String rarityID = rarityTag.getString(RARITY_TAG_NAME);
						Rarity rarity = Rarity.getByValue(rarityID);
						if (rarity != null) {
							int chunksSinceRarityFeature = rarityTag.getInt(CHUNKS_SINCE_LAST_RARITY_FEATURE_TAG_NAME);
							if (feature.getChunksSinceLastDimensionRarityFeature().containsKey(dimensionID)) {
								feature.getChunksSinceLastDimensionRarityFeature().get(dimensionID).put(rarity, chunksSinceRarityFeature);
							}
						}
					}
				});
			}
		});
		
		// TODO
		///// ChestConfig Registry /////
		TreasureData.CHEST_REGISTRIES.entrySet().forEach(entry -> {
			
		});
//		ChestRegistry chestRegistry = ChestRegistry.getInstance();
//		Treasure.LOGGER.debug("ChestConfig Registry size before loading -> {}", chestRegistry.getValues().size());
//		chestRegistry.clear();
//		// load the chest registry
//		NBTTagList chestRegistryTagList = treasureGen.getTagList(CHEST_REGISTRY_TAG_NAME, 10);
//		for (int i = 0; i < chestRegistryTagList.tagCount(); i++) {
//			NBTTagCompound chunkTag = chestRegistryTagList.getCompoundTagAt(i);
//			String key = chunkTag.getString(KEY_TAG_NAME);
//			String rarity = chunkTag.getString(RARITY_TAG_NAME);
//			NBTTagCompound coords = chunkTag.getCompoundTag(COORDS_TAG_NAME);
//			int x = coords.getInteger("x");
//			int y = coords.getInteger("y");
//			int z = coords.getInteger("z");
//			chestRegistry.register(key, new ChestInfo(Rarity.getByValue(rarity), new Coords(x, y, z)));
//		}
		
//		///// Chests /////
//		/// Surface Chests ///
//		if (surfaceTag != null) {
//			surfaceChestGen.setChunksSinceLastChest(surfaceTag.getInteger(CHUNKS_SINCE_LAST_CHEST_TAG_NAME));
//			NBTTagList chunksSinceTagList = surfaceTag.getTagList(CHUNKS_SINCE_LAST_RARITY_CHEST_TAG_NAME, 10);
//			// load all the chunks since last rarity chest properites
//			for (int i = 0; i < chunksSinceTagList.tagCount(); i++) {
//				NBTTagCompound chunkTag = chunksSinceTagList.getCompoundTagAt(i);
//				int count = chunkTag.getInteger(COUNT_TAG_NAME);
//				String key = chunkTag.getString(KEY_TAG_NAME);
//				surfaceChestGen.getChunksSinceLastRarityChest().put(Rarity.valueOf(key), count);
//			}
//		}

//		
//		/// Oasis ///
//		NBTTagCompound oasisTag = treasureGen.getCompoundTag(OASIS_GEN_TAG_NAME);
//		if (oasisTag != null) {
//			NBTTagList dimTagList = oasisTag.getTagList(DIMENSIONS_TAG_NAME, 10);
//			for (int i = 0; i < dimTagList.tagCount(); i++) {
//				NBTTagCompound dimTag = dimTagList.getCompoundTagAt(i);
//				int dimensionID = dimTag.getInteger(DIMENSION_ID_TAG_NAME);
//				int chunksSince = dimTag.getInteger(CHUNKS_SINCE_LAST_OASIS_TAG_NAME);
//				oasisGen.getChunksSinceLastDimensionOasis().put(dimensionID, chunksSince);
//				
//				NBTTagList biomeTagList = dimTag.getTagList(BIOMES_TAG_NAME, 10);
//				for (int k = 0; k < biomeTagList.tagCount(); k++) {
//					NBTTagCompound biomeTag = dimTagList.getCompoundTagAt(k);
//					int biomeID = biomeTag.getInteger(BIOME_ID_TAG_NAME);
//					int chunksSinceBiome = biomeTag.getInteger(CHUNKS_SINCE_LAST_OASIS_TAG_NAME);
//					oasisGen.getChunksSinceLastDimensionBiomeOasis().get(dimensionID).put(biomeID, chunksSinceBiome);
//				}
//			}
//		}
//		
//		///// Well /////
//		wellGen.setChunksSinceLastWell(treasureGen.getInteger("chunksSinceLastWell"));
//		
//		///// Wither Tree /////
//		witherGen.setChunksSinceLastTree(treasureGen.getInteger("chunksSinceLastTree"));
//		
//		///// Gem Ore /////
//		gemGen.setChunksSinceLastOre(treasureGen.getInteger("chunksSinceLastOre"));
//		
//		///// ChestConfig Registry /////
//		ChestRegistry chestRegistry = ChestRegistry.getInstance();
//		Treasure.LOGGER.debug("ChestConfig Registry size before loading -> {}", chestRegistry.getValues().size());
//		chestRegistry.clear();
//		// load the chest registry
//		NBTTagList chestRegistryTagList = treasureGen.getTagList(CHEST_REGISTRY_TAG_NAME, 10);
//		for (int i = 0; i < chestRegistryTagList.tagCount(); i++) {
//			NBTTagCompound chunkTag = chestRegistryTagList.getCompoundTagAt(i);
//			String key = chunkTag.getString(KEY_TAG_NAME);
//			String rarity = chunkTag.getString(RARITY_TAG_NAME);
//			NBTTagCompound coords = chunkTag.getCompoundTag(COORDS_TAG_NAME);
//			int x = coords.getInteger("x");
//			int y = coords.getInteger("y");
//			int z = coords.getInteger("z");
//			chestRegistry.register(key, new ChestInfo(Rarity.getByValue(rarity), new Coords(x, y, z)));
//		}
//		Treasure.LOGGER.debug("ChestConfig Registry size after loading -> {}", chestRegistry.getValues().size());
//
//		// Oasis Registry
//		OasisRegistry oasisRegistry = OasisRegistry.getInstance();
//		oasisRegistry.clear();
//		NBTTagList oasisRegistryDimensionTagList = treasureGen.getTagList(OASIS_REGISTRY_TAG_NAME, 10);
//		for (int dimIndex = 0; dimIndex < oasisRegistryDimensionTagList.tagCount(); dimIndex++) {
//			NBTTagCompound dimTag = oasisRegistryDimensionTagList.getCompoundTagAt(dimIndex);
//			int dimensionID = dimTag.getInteger(DIMENSION_ID_TAG_NAME);
//			// get the registry list
//			NBTTagList oasisRegistryTagList = dimTag.getTagList("registry", 10);
//			for (int registryIndex = 0; registryIndex < oasisRegistryTagList.tagCount(); registryIndex++) {
//				NBTTagCompound registryTag = oasisRegistryTagList.getCompoundTagAt(registryIndex);
//				String key = registryTag.getString(KEY_TAG_NAME);
//				int biomeID = registryTag.getInteger(BIOME_ID_TAG_NAME);
//				NBTTagCompound coordsTag = registryTag.getCompoundTag(COORDS_TAG_NAME);
//				int x = coordsTag.getInteger("x");
//				int y = coordsTag.getInteger("y");
//				int z = coordsTag.getInteger("z");
//				oasisRegistry.register(dimensionID, key, new OasisInfo(new Coords(x, y, z), dimensionID, biomeID));
//			}
//		}	
	}

	/*
	 * NOTE thrown exceptions are silently handled, so they need to be caught here instead
	 *  (non-Javadoc)
	 * @see net.minecraft.world.WorldSavedData#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public CompoundNBT write(CompoundNBT tag) {
		try {
			// create a treasure compound			
			CompoundNBT genTag = new CompoundNBT();
			
			// add main treasure tag
			tag.put(TREASURE_GEN_TAG_NAME, genTag);
			
			///// Chests //////
			// for each feature
			TreasureFeatures.FEATURES.forEach(feature -> {
				CompoundNBT featureTag = new CompoundNBT();
				
				// add the feature gen last count to the treasure compound for each dimension
				ListNBT dimTagList = new ListNBT();
				for (Entry<String, Integer> entry : feature.getChunksSinceLastDimensionFeature().entrySet()) {
					Treasure.LOGGER.debug("feature dimension ID -> {}", entry.getKey());
					
					CompoundNBT dimensionTag = new CompoundNBT();
					dimensionTag.putString(DIMENSION_ID_TAG_NAME, entry.getKey());
					dimensionTag.putInt(CHUNKS_SINCE_LAST_FEATURE_TAG_NAME, entry.getValue());
					Treasure.LOGGER.debug("chunks since last feature -> {}", entry.getValue());
					
					// get the rarity map
					if (feature.getChunksSinceLastDimensionRarityFeature().containsKey(entry.getKey())) {
						Map<Rarity, Integer> rarityMap = feature.getChunksSinceLastDimensionRarityFeature().get(entry.getKey());
						Treasure.LOGGER.debug("rarity map size -> {}", rarityMap.size());
						
						ListNBT rarityTagList = new ListNBT();
						for (Entry<Rarity, Integer> rarityEntry : rarityMap.entrySet()) {
							CompoundNBT rarityTag = new CompoundNBT();
							rarityTag.putString(RARITY_TAG_NAME, rarityEntry.getKey().getValue());
							rarityTag.putInt(CHUNKS_SINCE_LAST_RARITY_FEATURE_TAG_NAME, rarityEntry.getValue());
							Treasure.LOGGER.debug("chunks since last rarity {} feature -> {}", rarityEntry.getKey(), rarityEntry.getValue());
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
			ListNBT chestRegistries = new ListNBT();
			TreasureData.CHEST_REGISTRIES.entrySet().forEach(entry -> {
				String dimensionName = entry.getKey();
				ChestRegistry registry = entry.getValue();
				CompoundNBT dimTag = new CompoundNBT();
				dimTag.putString(DIMENSION_ID_TAG_NAME, dimensionName);
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
//			///// Oasis ////
//			// get the oasis world generators
//			OasisWorldGenerator oasisGen = (OasisWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.OASIS);
//			// create a new compound
//			NBTTagCompound oasisTag = new NBTTagCompound();
//			
//			// add the oasis gen last count to the treasure compound for each dimension
//			NBTTagList dimTagList = new NBTTagList();
//			for (Entry<Integer, Integer> entry : oasisGen.getChunksSinceLastDimensionOasis().entrySet()) {
//				Treasure.LOGGER.debug("oasis dimension ID -> {}", entry.getKey());
//				
//				NBTTagCompound dimTag = new NBTTagCompound();
//				dimTag.setInteger(DIMENSION_ID_TAG_NAME, entry.getKey());
//				dimTag.setInteger(CHUNKS_SINCE_LAST_OASIS_TAG_NAME, entry.getValue());
//				Treasure.LOGGER.debug("chunks since last oasis -> {}", entry.getValue());
//				Map<Integer, Integer> biomeMap = oasisGen.getChunksSinceLastDimensionBiomeOasis().get(entry.getKey());
//				Treasure.LOGGER.debug("oasis biome map size -> {}", biomeMap.size());
//				
//				NBTTagList biomeTagList = new NBTTagList();
//				for (Entry<Integer, Integer> biomeEntry : biomeMap.entrySet()) {
//					NBTTagCompound biomeTag = new NBTTagCompound();
//					biomeTag.setInteger(BIOME_ID_TAG_NAME, biomeEntry.getKey());
//					biomeTag.setInteger(CHUNKS_SINCE_LAST_OASIS_TAG_NAME, biomeEntry.getValue());
//					Treasure.LOGGER.debug("chunks since last biome {} oasis -> {}", biomeEntry.getKey(), biomeEntry.getValue());
//					biomeTagList.appendTag(biomeTag);
//				}
//				dimTag.setTag(BIOMES_TAG_NAME, biomeTagList);
//				dimTagList.appendTag(dimTag);
//			}
//
//			oasisTag.setTag(DIMENSIONS_TAG_NAME, dimTagList);
//			treasureGen.setTag(OASIS_GEN_TAG_NAME, oasisTag);
//			
//			///// Well ////
//			// get the well world generator
//			WellWorldGenerator wellGen = (WellWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.WELL);
//			
//			// add the chest gen last count to the treasure compound
//			treasureGen.setInteger("chunksSinceLastWell", wellGen.getChunksSinceLastWell());
//			
//			//// Wither Tree /////
//			WitherTreeWorldGenerator witherGen = (WitherTreeWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.WITHER_TREE);
//			
//			// add the chest gen last count to the treasure compound
//			treasureGen.setInteger("chunksSinceLastTree", witherGen.getChunksSinceLastTree());
//			
//			//// Gem Ore ////
//			GemOreWorldGenerator gemGen = (GemOreWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.GEM);
//			treasureGen.setInteger("chunksSinceLastOre", gemGen.getChunksSinceLastOre());
//			
//			///// ChestConfig Registry /////
//			NBTTagList chestRegistryTagList = new NBTTagList();
//			ChestRegistry chestRegistry = ChestRegistry.getInstance();
//			for (ChestInfo element : chestRegistry.getValues()) {
//				NBTTagCompound entry = new NBTTagCompound();
//				NBTTagString key = new NBTTagString(element.getCoords().toShortString());
//				NBTTagString rarity = new NBTTagString(element.getRarity().getValue());
//				NBTTagCompound coords = new NBTTagCompound();
//				NBTTagInt x = new NBTTagInt(element.getCoords().getX());
//				NBTTagInt y = new NBTTagInt(element.getCoords().getY());
//				NBTTagInt z = new NBTTagInt(element.getCoords().getZ());
//				
//				coords.setTag("x", x);
//				coords.setTag("y", y);
//				coords.setTag("z", z);
//				
//				entry.setTag(KEY_TAG_NAME, key);
//				entry.setTag(RARITY_TAG_NAME, rarity);
//				entry.setTag(COORDS_TAG_NAME, coords);
//				
//				// add entry to list
//				chestRegistryTagList.appendTag(entry);
//			}
//			// delete current tag
//			treasureGen.removeTag(CHEST_REGISTRY_TAG_NAME);
//			// add new values
//			treasureGen.setTag(CHEST_REGISTRY_TAG_NAME, chestRegistryTagList);
//			
//			///// Oasis Registry (multi-dimensional) /////			
//			OasisRegistry oasisRegistry = OasisRegistry.getInstance();
//			NBTTagList oasisRegistryDimensionTagList = new NBTTagList();
//			for (Integer dimensionKey : oasisRegistry.getDimensionKeys()) {
//				ListMultimap<String, OasisInfo> dimensionMap = oasisRegistry.getDimensionEntry(dimensionKey);
//				NBTTagCompound dimTag = new NBTTagCompound();
//				dimTag.setInteger(DIMENSION_ID_TAG_NAME, dimensionKey);
//				NBTTagList oasisRegistryTagList = new NBTTagList();
//				for(Entry<String, OasisInfo> entry : dimensionMap.entries()) {
//					OasisInfo oasisInfo = entry.getValue();
//					NBTTagCompound oasisEntry = new NBTTagCompound();					
//					NBTTagString key = new NBTTagString(entry.getKey());
//					NBTTagInt biomeID = new NBTTagInt(oasisInfo.getBiomeID());
//					NBTTagCompound coords = new NBTTagCompound();
//					NBTTagInt x = new NBTTagInt(oasisInfo.getCoords().getX());
//					NBTTagInt y = new NBTTagInt(oasisInfo.getCoords().getY());
//					NBTTagInt z = new NBTTagInt(oasisInfo.getCoords().getZ());
//					
//					coords.setTag("x", x);
//					coords.setTag("y", y);
//					coords.setTag("z", z);
//					
//					oasisEntry.setTag(KEY_TAG_NAME, key);
//					oasisEntry.setTag(BIOME_ID_TAG_NAME, biomeID);
//					oasisEntry.setTag(COORDS_TAG_NAME, coords);
					
//					oasisRegistryTagList.appendTag(oasisEntry);
//				}
//				dimTag.setTag("registry", oasisRegistryTagList);
//				oasisRegistryDimensionTagList.appendTag(dimTag);
//			}
//
//			// delete current tag
//			treasureGen.removeTag(OASIS_REGISTRY_TAG_NAME);
//			// add new values
//			treasureGen.setTag(OASIS_REGISTRY_TAG_NAME, oasisRegistryDimensionTagList);
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
		DimensionSavedDataManager storage = ((ServerWorld)world).getSavedData();
		TreasureGenerationSavedData data = (TreasureGenerationSavedData) storage.getOrCreate(TreasureGenerationSavedData::new, GEN_DATA_KEY);
		
		if (data == null) {
			data = new TreasureGenerationSavedData();
			storage.set(data);
		}
		return data;
	}
}