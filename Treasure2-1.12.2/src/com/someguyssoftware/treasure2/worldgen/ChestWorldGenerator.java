 /**
 * 
 */
package com.someguyssoftware.treasure2.worldgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.Configs;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.AbstractChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CauldronChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ClamChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.EpicChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.GoldSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.OysterChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.RareChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ScarceChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.SkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.UncommonChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.AirPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.BigBottomMobTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaSideTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.MobTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.StructurePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.TntTrapPitGenerator;
import com.someguyssoftware.treasure2.persistence.GenDataPersistence;
import com.someguyssoftware.treasure2.registry.ChestRegistry;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
@Getter @Setter
public class ChestWorldGenerator implements IWorldGenerator {
	private int chunksSinceLastChest;
	private Map<Rarity, Integer> chunksSinceLastRarityChest;
	
	// the chest chestGeneratorsMap
	private Map<Rarity, AbstractChestGenerator> chestGeneratorsMap = new HashMap<>();
	private Map<Rarity, RandomWeightedCollection<IChestGenerator<GeneratorResult<GeneratorData>>>> chestCollectionGeneratorsMap = new HashMap<>();
	// TEMP
	private Map<Rarity, RandomWeightedCollection<IChestGenerator<GeneratorResult<GeneratorData>>>> oceanChestCollectionGeneratorsMap = new HashMap<>();

	// the pit chestGeneratorsMap
	public static Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGens =  HashBasedTable.create();
	
	/**
	 * 
	 */
	public ChestWorldGenerator() {
		try {
			init();
		} catch (Exception e) {
			Treasure.logger.error("Unable to instantiate ChestGenerator:", e);
		}
	}
	
	private void init() {
		// initialize chunks since last array
		chunksSinceLastChest = 0;
		chunksSinceLastRarityChest = new HashMap<>(Rarity.values().length);
		for (Rarity rarity : Rarity.values()) {
			chunksSinceLastRarityChest.put(rarity, 0);
		}

		// setup the chest  chestGeneratorsMap
		chestGeneratorsMap.put(Rarity.COMMON, new CommonChestGenerator());
		chestGeneratorsMap.put(Rarity.UNCOMMON, new UncommonChestGenerator());
		chestGeneratorsMap.put(Rarity.SCARCE, new ScarceChestGenerator());
		chestGeneratorsMap.put(Rarity.RARE, new RareChestGenerator());
		chestGeneratorsMap.put(Rarity.EPIC, new EpicChestGenerator());
				
		chestCollectionGeneratorsMap.put(Rarity.COMMON, new RandomWeightedCollection<>());
		chestCollectionGeneratorsMap.put(Rarity.UNCOMMON, new RandomWeightedCollection<>());
		chestCollectionGeneratorsMap.put(Rarity.SCARCE, new RandomWeightedCollection<>());
		chestCollectionGeneratorsMap.put(Rarity.RARE, new RandomWeightedCollection<>());
		chestCollectionGeneratorsMap.put(Rarity.EPIC, new RandomWeightedCollection<>());
		
		oceanChestCollectionGeneratorsMap.put(Rarity.COMMON, new RandomWeightedCollection<>());
		oceanChestCollectionGeneratorsMap.put(Rarity.UNCOMMON, new RandomWeightedCollection<>());
		oceanChestCollectionGeneratorsMap.put(Rarity.SCARCE, new RandomWeightedCollection<>());
		oceanChestCollectionGeneratorsMap.put(Rarity.RARE, new RandomWeightedCollection<>());
		oceanChestCollectionGeneratorsMap.put(Rarity.EPIC, new RandomWeightedCollection<>());
		
		chestCollectionGeneratorsMap.get(Rarity.COMMON).add(1, new CommonChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.UNCOMMON).add(1, new UncommonChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.SCARCE).add(75, new ScarceChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.SCARCE).add(25, new SkullChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.RARE).add(85, new RareChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.RARE).add(15, new GoldSkullChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.EPIC).add(85, new EpicChestGenerator());
		chestCollectionGeneratorsMap.get(Rarity.EPIC).add(15, new CauldronChestGenerator());
		
		oceanChestCollectionGeneratorsMap.get(Rarity.COMMON).add(1, new CommonChestGenerator()); // really unnecessary
		oceanChestCollectionGeneratorsMap.get(Rarity.UNCOMMON).add(1, new UncommonChestGenerator()); // really unnecessary
		oceanChestCollectionGeneratorsMap.get(Rarity.SCARCE).add(75, new ScarceChestGenerator());
		oceanChestCollectionGeneratorsMap.get(Rarity.SCARCE).add(25, new SkullChestGenerator());
//		oceanChestCollectionGeneratorsMap.get(Rarity.SCARCE).add(2500, new OysterChestGenerator());
		oceanChestCollectionGeneratorsMap.get(Rarity.RARE).add(85, new RareChestGenerator());
		oceanChestCollectionGeneratorsMap.get(Rarity.RARE).add(15, new GoldSkullChestGenerator());
//		oceanChestCollectionGeneratorsMap.get(Rarity.RARE).add(1500, new ClamChestGenerator());
		oceanChestCollectionGeneratorsMap.get(Rarity.EPIC).add(85, new EpicChestGenerator());
		oceanChestCollectionGeneratorsMap.get(Rarity.EPIC).add(15, new CauldronChestGenerator());
//		oceanChestCollectionGeneratorsMap.get(Rarity.EPIC).add(1500, new OysterChestGenerator());
		
		// setup pit generators map
		pitGens.put(PitTypes.STANDARD, Pits.SIMPLE_PIT, new SimplePitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.SIMPLE_PIT, new StructurePitGenerator(new SimplePitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.TNT_TRAP_PIT, new TntTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.TNT_TRAP_PIT, new StructurePitGenerator(new TntTrapPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.AIR_PIT,  new AirPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.AIR_PIT, new StructurePitGenerator(new AirPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.LAVA_TRAP_PIT, new LavaTrapPitGenerator());
		// NONE for STRUCTURE
		
		pitGens.put(PitTypes.STANDARD, Pits.MOB_TRAP_PIT, new MobTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.MOB_TRAP_PIT, new StructurePitGenerator(new MobTrapPitGenerator()));
				
		pitGens.put(PitTypes.STANDARD, Pits.LAVA_SIDE_TRAP_PIT, new LavaSideTrapPitGenerator());
		pitGens.put(PitTypes.STRUCTURE, Pits.LAVA_SIDE_TRAP_PIT, new StructurePitGenerator(new LavaSideTrapPitGenerator()));
		
		pitGens.put(PitTypes.STANDARD, Pits.BIG_BOTTOM_MOB_TRAP_PIT, new BigBottomMobTrapPitGenerator());
		// NONE for STRUCTURE
		
	}

	/**
	 * 
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch(world.provider.getDimension()){
		case 0:
		    generateInOverworld(world, random, chunkX, chunkZ);
		    break;
	    default:
	    	break;
		}
	}		


	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	private void generateInOverworld(World world, Random random, int chunkX, int chunkZ) {
		
		// increment the chunk counts
		chunksSinceLastChest++;
		for (Rarity rarity : Rarity.values()) {
			Integer i = chunksSinceLastRarityChest.get(rarity);
			chunksSinceLastRarityChest.put(rarity, ++i);
		}

		// test if min chunks was met
     	if (chunksSinceLastChest > TreasureConfig.minChunksPerChest) {
     		/*
     		 * get current chunk position
     		 */            
            // spawn @ middle of chunk
            int xSpawn = (chunkX * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
            int zSpawn = (chunkZ * WorldInfo.CHUNK_SIZE) + WorldInfo.CHUNK_RADIUS;
            
            // the get first surface y (could be leaves, trunk, water, etc)
            int ySpawn = world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightValue(WorldInfo.CHUNK_RADIUS, WorldInfo.CHUNK_RADIUS);
            ICoords coords = new Coords(xSpawn, ySpawn, zSpawn);

	    	// determine what type to generate
        	Rarity rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
			IChestConfig chestConfig = Configs.chestConfigs.get(rarity);
			if (chestConfig == null) {
				Treasure.logger.warn("Unable to locate a chest for rarity {}.", rarity);
				return;
			}
//			Treasure.logger.debug("Chunks since last {} chest: {}", rarity,  chunksSinceLastRarityChest.get(rarity) );
//			Treasure.logger.debug("Chunks per {} chest: {}", rarity, chestConfig.getChunksPerChest());
    		if (chunksSinceLastRarityChest.get(rarity) >= chestConfig.getChunksPerChest()) {
    			
				// 1. test if chest meets the probability criteria
				if (!RandomHelper.checkProbability(random, chestConfig.getGenProbability())) {
//					Treasure.logger.debug("Chest does not meet generate probability.");
					return;
				}
				
				// 2. test if the override (global) biome is allowed
				Biome biome = world.getBiome(coords.toPos());

			    if (!BiomeHelper.isBiomeAllowed(biome, chestConfig.getBiomeWhiteList(), chestConfig.getBiomeBlackList())) {
			    	if (Treasure.logger.isDebugEnabled()) {
			    		if (WorldInfo.isClientSide(world)) {
			    			Treasure.logger.debug("{} is not a valid biome @ {}", biome.getBiomeName(), coords.toShortString());
			    		}
			    		else {
			    			Treasure.logger.debug("Biome for {} is not valid @ {}",rarity.getValue(), coords.toShortString());
			    		}
			    	}
			    	return;
			    }
			    
     			// 3. check against all registered chests
     			if (isRegisteredChestWithinDistance(world, coords, TreasureConfig.minDistancePerChest)) {
   					Treasure.logger.debug("The distance to the nearest treasure chest is less than the minimun required.");
     				return;
     			}
     			     			
    			// reset chunks since last common chest regardless of successful generation - makes more rare and realistic and configurable generation.
    			chunksSinceLastRarityChest.put(rarity, 0);
 			
    			// generate the chest/pit/chambers
				Treasure.logger.debug("Attempting to generate pit/chest.");
				// TODO swap generator maps here depending on biome ie if ocean/deep ocean use the oceanChestCollectionGeneratorsMap else normal map
				GeneratorResult<GeneratorData> result = null;
				if (biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN) {
					result = oceanChestCollectionGeneratorsMap.get(rarity).next().generate(world, random, coords, rarity, Configs.chestConfigs.get(rarity));
				}
				else {
					result = chestCollectionGeneratorsMap.get(rarity).next().generate(world, random, coords, rarity, Configs.chestConfigs.get(rarity)); 
				}
				
    			if (result.isSuccess()) {
    				// add to registry
    				ChestRegistry.getInstance().register(coords.toShortString(), new ChestInfo(rarity, coords));
    				// reset the chunk counts
        			chunksSinceLastChest = 0;
    			}
    		}

	     	// save world data
    		GenDataPersistence savedData = GenDataPersistence.get(world);
	    	if (savedData != null) {
	    		savedData.markDirty();
	    	}
     	}
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	@SuppressWarnings("unused")
	private void generateNether(World world, Random random, int i, int j) {}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param i
	 * @param j
	 */
	@SuppressWarnings("unused")
	private void generateEnd(World world, Random random, int i, int j) {}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @param minDistance
	 * @return
	 */
	public boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {
		
		double minDistanceSq = minDistance * minDistance;
		
		// get a list of dungeons
		List<ChestInfo> infos = ChestRegistry.getInstance().getEntries();

		if (infos == null || infos.size() == 0) {
			Treasure.logger.debug("Unable to locate the Chest Registry or the Registry doesn't contain any values");
			return false;
		}
		
		for (ChestInfo info : infos) {
			// calculate the distance to the poi
			double distance = coords.getDistanceSq(info.getCoords());
			if (distance < minDistanceSq) {
				return true;
			}
		}
		return false;
	}
	
//	/**
//	 * @return the chunksSinceLastChest
//	 */
//	public int getChunksSinceLastChest() {
//		return chunksSinceLastChest;
//	}
//
//	/**
//	 * @param chunksSinceLastChest the chunksSinceLastChest to set
//	 */
//	public void setChunksSinceLastChest(int chunksSinceLastChest) {
//		this.chunksSinceLastChest = chunksSinceLastChest;
//	}
//
//	/**
//	 * @return the chunksSinceLastRarityChest
//	 */
//	public Map<Rarity, Integer> getChunksSinceLastRarityChest() {
//		return chunksSinceLastRarityChest;
//	}
//
//	/**
//	 * @param chunksSinceLastRarityChest the chunksSinceLastRarityChest to set
//	 */
//	public void setChunksSinceLastRarityChest(Map<Rarity, Integer> chunksSinceLastRarityChest) {
//		this.chunksSinceLastRarityChest = chunksSinceLastRarityChest;
//	}
//
//	/**
//	 * @return the chestGeneratorsMap
//	 */
//	public Map<Rarity, AbstractChestGenerator> getGenerators() {
//		return chestGeneratorsMap;
//	}
//
//	/**
//	 * @param chestGeneratorsMap the chestGeneratorsMap to set
//	 */
//	public void setGenerators(Map<Rarity, AbstractChestGenerator> generators) {
//		this.chestGeneratorsMap = generators;
//	}
//
//	/**
//	 * @return the chestCollectionGeneratorsMap
//	 */
//	public Map<Rarity, RandomWeightedCollection<IChestGenerator<GeneratorResult<GeneratorData>>>> getChestCollectionGeneratorsMap() {
//		return chestCollectionGeneratorsMap;
//	}
//
//	/**
//	 * @param chestCollectionGeneratorsMap the chestCollectionGeneratorsMap to set
//	 */
//	public void setChestCollectionGeneratorsMap(Map<Rarity, RandomWeightedCollection<IChestGenerator<GeneratorResult<GeneratorData>>>> gens) {
//		this.chestCollectionGeneratorsMap = gens;
//	}
}
