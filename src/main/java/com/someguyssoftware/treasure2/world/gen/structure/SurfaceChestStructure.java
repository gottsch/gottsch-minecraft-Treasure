/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.world.gen.structure;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ChestInfo;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.TemplateManager;

/**
 * @author Mark Gottschling on Jun 18, 2021
 *
 */
public class SurfaceChestStructure  extends Structure<NoFeatureConfig> {
	private static final ResourceLocation CHEST_PIECE_LOCATION = new ResourceLocation(Treasure.MODID, "chest");
	protected static int UNDERGROUND_OFFSET = 5;
	
	private Rarity rarity;

	/**
	 * 
	 * @param codec
	 */
	public SurfaceChestStructure(Codec<NoFeatureConfig> codec) {
		this(Rarity.COMMON, codec);
	}

	/**
	 * 
	 * @param rarity
	 * @param codec
	 */
	public SurfaceChestStructure(Rarity rarity, Codec<NoFeatureConfig> codec) {
		super(codec);
		this.rarity = rarity;
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return  SurfaceChestStructure.Start::new;
	}

	/**
	 *        : WARNING!!! DO NOT FORGET THIS METHOD!!!! :
	 * If you do not override step method, your structure WILL crash the biome as it is being parsed!
	 *
	 * Generation stage for when to generate the structure. there are 10 stages you can pick from!
	 * This surface structure stage places the structure before plants and ores are generated.
	 */
	@Override
	public GenerationStage.Decoration step() {
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}

	/**
	 * || ONLY WORKS IN FORGE 34.1.12+ ||
	 *
	 * This method allows us to have mobs that spawn naturally over time in our structure.
	 * No other mobs will spawn in the structure of the same entity classification.
	 * The reason you want to match the classifications is so that your structure's mob
	 * will contribute to that classification's cap. Otherwise, it may cause a runaway
	 * spawning of the mob that will never stop.
	 *
	 * NOTE: getDefaultSpawnList is for monsters only and getDefaultCreatureSpawnList is
	 *       for creatures only. If you want to add entities of another classification,
	 *       use the StructureSpawnListGatherEvent to add water_creatures, water_ambient,
	 *       ambient, or misc mobs. Use that event to add/remove mobs from structures
	 *       that are not your own.
	 *
	 * NOTE 2: getSpecialEnemies and getSpecialAnimals are the vanilla methods that Forge does
	 *         not hook up. Do not use those methods or else the mobs won't spawn. You would
	 *         have to manually implement spawning for them. Stick with Forge's Default form
	 *         as it is easier to use that.
	 */
	private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.ILLUSIONER, 100, 4, 9),
			new MobSpawnInfo.Spawners(EntityType.VINDICATOR, 100, 4, 9)
			);
	@Override
	public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
		return STRUCTURE_MONSTERS;
	}

	private static final List<MobSpawnInfo.Spawners> STRUCTURE_CREATURES = ImmutableList.of(
			new MobSpawnInfo.Spawners(EntityType.SHEEP, 30, 10, 15),
			new MobSpawnInfo.Spawners(EntityType.RABBIT, 100, 1, 2)
			);
	@Override
	public List<MobSpawnInfo.Spawners> getDefaultCreatureSpawnList() {
		return STRUCTURE_CREATURES;
	}

	/*
	 * This is where extra checks can be done to determine if the structure can spawn here.
	 * This only needs to be overridden if you're adding additional spawn conditions.
	 *
	 * Fun fact, if you set your structure separation/spacing to be 0/1, you can use
	 * isFeatureChunk to return true only if certain chunk coordinates are passed in
	 * which allows you to spawn structures only at certain coordinates in the world.
	 *
	 * Notice how the biome is also passed in. Though, you are not going to
	 * do any biome checking here as you should've added this structure to
	 * the biomes you wanted already with the biome load event.
	 * 
	 * Basically, this method is used for determining if the land is at a suitable height,
	 * if certain other structures are too close or not, or some other restrictive condition.
	 *
	 * For example, Pillager Outposts added a check to make sure it cannot spawn within 10 chunk of a Village.
	 * (Bedrock Edition seems to not have the same check)
	 * 
	 * 
	 * Also, please for the love of god, do not do dimension checking here. If you do and
	 * another mod's dimension is trying to spawn your structure, the locate
	 * command will make minecraft hang forever and break the game.
	 *
	 * Instead, use the addDimensionalSpacing method in StructureTutorialMain class.
	 * If you check for the dimension there and do not add your structure's
	 * spacing into the chunk generator, the structure will not spawn in that dimension!
	 */
	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
		BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);

		// grab height of land. Will stop at first non-air block.
		int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);

		// grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
		// in nether, it will be netherrack, lava, and air. End will only be endstone and air. It depends on what block
		// the chunk generator will place for that dimension.
		IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());

		// combine the column of blocks with land height and you get the top block itself which you can test.
		ICoords spawnCoords = new Coords(centerOfChunk.above(landHeight));
		BlockState topBlock = columnOfBlocks.getBlockState(spawnCoords.toPos());

		// now we test to make sure our structure is not spawning on water or other fluids.
		// you can do height check instead too to make it spawn at high elevations.
		Treasure.LOGGER.debug("testing if surface @ {} is good -> {}", new Coords(centerOfChunk.getX(), landHeight, centerOfChunk.getZ()).toShortString(), topBlock.getFluidState().isEmpty());
		return topBlock.getFluidState().isEmpty(); //landHeight > 100;

	}



	/**
	 * Handles calling up the structure's pieces class and height that structure will spawn at.
	 */
	public static class Start extends StructureStart<NoFeatureConfig>  {
		boolean isSurfaceChest = false;
		boolean isStructure = false;

		public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
			super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
		}

		@Override
		public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
			// Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
			int x = (chunkX << 4) + 7;
			int z = (chunkZ << 4) + 7;

			// NOTE don't need to get the height here if using the JigsawManager, just need x, z and can use y = 0
			int y = chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG) + 1;

			/*
			 * We pass this into addPieces to tell it where to generate the structure.
			 * If addPieces's last parameter is true, blockpos's Y value is ignored and the
			 * structure will spawn at terrain height instead. Set that parameter to false to
			 * force the structure to spawn at blockpos's Y value instead. You got options here!
			 */
			ICoords spawnCoords = new Coords(x, y, z);
			Treasure.LOGGER.debug("generatePieces - spawning surface chest @ {}", spawnCoords.toShortString());

			// NOTE: it is actuall this.pieces that has to be non-null in order to generate. don't reall need jigsawManager
			// TODO add a noop piece here so the placeInChunk will be executed.
			//	            JigsawManager.addPieces(
			//	                    dynamicRegistryManager,
			//	                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
			//	                            // The path to the starting Template Pool JSON file to read.
			//	                            //
			//	                            // Note, this is "structure_tutorial:run_down_house/start_pool" which means
			//	                            // the game will automatically look into the following path for the template pool:
			//	                            // "resources/data/structure_tutorial/worldgen/template_pool/run_down_house/start_pool.json"
			//	                            // This is why your pool files must be in "data/<modid>/worldgen/template_pool/<the path to the pool here>"
			//	                            // because the game automatically will check in worldgen/template_pool for the pools.
			//	                            .get(new ResourceLocation(Treasure.MODID, "noop/noop")),
			//
			//	                            // How many pieces outward from center can a recursive jigsaw structure spawn.
			//	                            // Our structure is only 1 piece outward and isn't recursive so any value of 1 or more doesn't change anything.
			//	                            // However, I recommend you keep this a decent value like 10 so people can use datapacks to add additional pieces to your structure easily.
			//	                            // But don't make it too large for recursive structures like villages or you'll crash server due to hundreds of pieces attempting to generate!
			//	                            10),
			//	                    AbstractVillagePiece::new,
			//	                    chunkGenerator,
			//	                    templateManagerIn,
			//	                    blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
			//	                    this.pieces, // The list that will be populated with the jigsaw pieces after this method.
			//	                    this.random,
			//	                    false, // Special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
			//	                        // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
			//	                    true);  // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
			//	                         // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.

			// get the config for the rarity
			Rarity rarity = ((SurfaceChestStructure)this.getFeature()).getRarity();
			IChestConfig chestConfig = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
			if (chestConfig == null) {
				Treasure.LOGGER.warn("Unable to locate a chest for rarity {}.", rarity);
				return;
			}
			Treasure.LOGGER.debug("config for rarity -> {} = {}", rarity, chestConfig);


			// determine if above ground or below ground
			this.isSurfaceChest = false;
			if (chestConfig.isSurfaceAllowed() && RandomHelper.checkProbability(random, TreasureConfig.CHESTS.surfaceChests.surfaceChestProbability.get())) {
				this.isSurfaceChest = true;
			}
			else if (!chestConfig.isSubterraneanAllowed()) {
				return;
			}

			this.isStructure = false;
			TreasureStructurePiece piece = null;
			if (this.isSurfaceChest && RandomHelper.checkProbability(random, TreasureConfig.GENERAL.surfaceStructureProbability.get())) {
				Treasure.LOGGER.debug("setting up for structure...");
				this.isStructure = true;
				// select a template
				TemplateHolder holder = selectTemplate( random,spawnCoords, StructureArchetype.SURFACE, StructureType.RUIN, biome);
				// create structure piece
				piece = new TreasureStructurePiece(templateManagerIn, holder.getLocation(), 
						spawnCoords.toPos(), Rotation.values()[random.nextInt(Rotation.values().length)], 0);
				this.pieces.add(piece);
				this.calculateBoundingBox();
			}
			else {
				// TODO will use pit structure ?? need to know now, also need to flag this differently than isStructure because we need the piece to be generated AND manually build the shaft
				Treasure.LOGGER.debug("setting up for pit...");
				piece = new TreasureStructurePiece(templateManagerIn,CHEST_PIECE_LOCATION, spawnCoords.toPos(), Rotation.NONE, 0);
				this.pieces.add(piece);
				// create a 1x1x1 bound box
	            MutableBoundingBox mbb = new MutableBoundingBox(new int[] {x, y, z, x +1, y + 1, z + 1});
	            this.boundingBox = mbb;
			}
//			this.pieces.add(piece);
//			this.calculateBoundingBox();

			///////////////////////
			// TESTING create throwable piece and add to list - WORKS!
			//	            TreasureStructurePiece defaultPiece = new TreasureStructurePiece(templateManagerIn,CHEST_PIECE_LOCATION, spawnCoords.toPos(), Rotation.NONE, 0);
			//	            this.pieces.add(defaultPiece);
			//            
			//	            // sets the bounds of the structure once you are finished.
			//	            MutableBoundingBox mbb = new MutableBoundingBox(new int[] {x, y, z, x +1, y + 1, z + 1});
			//	            this.boundingBox = mbb;
			Treasure.LOGGER.debug("bounding box -> {}", this.boundingBox.toString());

		}

		/**
		 * 
		 */
		@Override
		public void placeInChunk(ISeedReader reader, StructureManager structureManager, ChunkGenerator chunkGenerator,
				Random random, MutableBoundingBox mbb, ChunkPos chunkPos) {
			Treasure.LOGGER.debug("in placeInChunk @ chunkpos -> {}", chunkPos.toString());

			if (this.isStructure) {
				Treasure.LOGGER.debug("placeInChunk: isStructure -> {}", isStructure);
				super.placeInChunk(reader, structureManager, chunkGenerator, random, mbb, chunkPos);
				// TODO if pit structure,  get the enterance pos and build shaft
				return;
			}

			// Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
			int x = (chunkPos.x << 4) + 7;
			int z = (chunkPos.z << 4) + 7;

			// TODO need to get terrain height again - only if it is not a structure ie a pit
			int y = chunkGenerator.getFirstOccupiedHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
			ICoords spawnCoords = new Coords(x, y + 1, z);
			Treasure.LOGGER.debug("placeInChunk: spawnCoords -> {}", spawnCoords.toShortString());
			// TODO should this check live? dont' have access to world here
			// serverWorld.dimension().equals(World.OVERWORLD)){
			// 3. check against all registered chests
			if (isRegisteredChestWithinDistance(reader.getLevel(), spawnCoords, TreasureConfig.CHESTS.surfaceChests.minDistancePerChest.get())) {
				Treasure.LOGGER.debug("The distance to the nearest treasure chest is less than the minimun required.");
				return;
			}

			Rarity rarity = ((SurfaceChestStructure)this.getFeature()).getRarity();
			Treasure.LOGGER.debug("placeInChunk: rarity -> {}", rarity);
			GeneratorResult<GeneratorData> result = null;
			// NOTE this call is using old-way Treasure2 structure generation (markers are small and fit in the 3x3 grid and can be generatd in one chunk pass)
			result = generate(reader, random, spawnCoords, rarity, TreasureData.CHEST_GENS.get(rarity, WorldGenerators.SURFACE_CHEST).next(), TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity));
			Treasure.LOGGER.debug("result -> {}", result);
			
			Treasure.LOGGER.debug("placeInChunk - spawning surface chest @ {}", spawnCoords.toShortString());
			//	        	super.placeInChunk(reader, p_230366_2_, chunkGenerator, p_230366_4_, p_230366_5_, chunkPos);
//			reader.setBlock(spawnCoords.toPos(), Blocks.CHEST.defaultBlockState(), 3);
//			reader.setBlock(spawnCoords.add(16, 0, 0).toPos(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
			//	        	reader.setBlock(spawnCoords.add(32, 0, 0).toPos(), Blocks.EMERALD_BLOCK.defaultBlockState(), 3);
		}
		
		/**
		 * TODO move to abstract or interface
		 * @param random
		 * @param coords
		 * @param archetype
		 * @param type
		 * @param biome
		 * @return
		 */
		public TemplateHolder selectTemplate(Random random, ICoords coords, StructureArchetype archetype, StructureType type, Biome biome) {
			TemplateHolder holder = TreasureTemplateRegistry.getTemplateManager().getTemplate(random, archetype, type, biome);
			return holder;
		}

		/**
		 * TODO move to abstract or interface
		 * @param random
		 * @return
		 */
		public static IPitGenerator<GeneratorResult<ChestGeneratorData>> selectPitGenerator(Random random) {
//			PitTypes pitType = RandomHelper.checkProbability(random, TreasureConfig.PITS.pitStructureProbability.get()) ? PitTypes.STRUCTURE : PitTypes.STANDARD;
			PitTypes pitType = PitTypes.STANDARD;
			Treasure.LOGGER.debug("using pit type -> {}", pitType);
			List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerators = TreasureData.PIT_GENS.row(pitType).values().stream()
					.collect(Collectors.toList());
			IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenerators.get(random.nextInt(pitGenerators.size()));
			Treasure.LOGGER.debug("Using PitType: {}, Gen: {}", pitType, pitGenerator.getClass().getSimpleName());

			return pitGenerator;
		}

		/**
		 * 
		 * @param reader
		 * @param random
		 * @param coords
		 * @param rarity
		 * @param chestGenerator
		 * @param config
		 * @return
		 */
		private GeneratorResult<GeneratorData> generate(ISeedReader reader, Random random, ICoords coords, Rarity rarity,
				IChestGenerator chestGenerator, IChestConfig config) {

			// result to return to the caller
			GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
			// result from chest generation
			GeneratorResult<ChestGeneratorData> genResult = new GeneratorResult<>(ChestGeneratorData.class);

			ICoords chestCoords = null;

			// 1. collect location data points
			ICoords surfaceCoords = coords;
			Treasure.LOGGER.debug("surface coords -> {}", surfaceCoords.toShortString());
			if (!WorldInfo.isValidY(surfaceCoords)) {
				Treasure.LOGGER.debug("surface coords are invalid @ {}", surfaceCoords.toShortString());
				return result.fail();
			}

			if (this.isSurfaceChest) {
				// set the chest coords to the surface pos
				chestCoords = new Coords(surfaceCoords);
				Treasure.LOGGER.debug("surface chest coords -> {}", chestCoords);
			}
			else {
				Treasure.LOGGER.debug("else generate pit");
				genResult = generatePit(reader, random, rarity, surfaceCoords, config);
				Treasure.LOGGER.debug("result -> {}", genResult.toString());
				if (!genResult.isSuccess()) {
					return result.fail();
				}
				chestCoords = genResult.getData().getChestContext().getCoords();
			}

			// if chest isn't generated, then fail
			if (chestCoords == null) {
				Treasure.LOGGER.debug("chest coords were not provided in result -> {}", genResult.toString());
				return result.fail();
			}

			// add markers
			chestGenerator.addMarkers(reader, random,surfaceCoords, isSurfaceChest);

			GeneratorResult<ChestGeneratorData> chestResult = chestGenerator.generate(reader, random, chestCoords, rarity, genResult.getData().getChestContext().getState());
			if (!chestResult.isSuccess()) {
				return result.fail();
			}

			Treasure.LOGGER.info("CHEATER! {} chest at coords: {}", rarity, surfaceCoords.toShortString());
			result.setData(chestResult.getData());
			return result.success();
		}
		
		/**
		 * Land Only
		 * @param world
		 * @param random
		 * @param chestRarity
		 * @param markerCoords
		 * @param config
		 * @return
		 */
		public GeneratorResult<ChestGeneratorData> generatePit(IServerWorld world, Random random, Rarity chestRarity, ICoords markerCoords, IChestConfig config) {
			GeneratorResult<ChestGeneratorData> result = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);
			GeneratorResult<ChestGeneratorData> pitResult = new GeneratorResult<ChestGeneratorData>(ChestGeneratorData.class);

			// check if it has 50% land
			if (!WorldInfo.isSolidBase(world, markerCoords, 2, 2, 50)) {
				Treasure.LOGGER.debug("Coords [{}] does not meet solid base requires for {} x {}", markerCoords.toShortString(), 3, 3);
				return result.fail();
			}

			// TODO change config instead of minYSpawn have maxDepth (ex =60 blocks from surface). this will prevent a mountain top pit from being 200 blocks deep.
			// determine spawn coords below ground
			ICoords spawnCoords = getUndergroundSpawnPos(world, random, markerCoords, config.getMinYSpawn());

			if (spawnCoords == null || spawnCoords == WorldInfo.EMPTY_COORDS) {
				Treasure.LOGGER.debug("Unable to spawn underground @ {}", markerCoords);
				return result.fail();
			}
			Treasure.LOGGER.debug("Below ground @ {}", spawnCoords.toShortString());
			result.getData().setSpawnCoords(markerCoords);

			// select a pit generator
			IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = selectPitGenerator(random);
			Treasure.LOGGER.debug("Using pit generator -> {}", pitGenerator.getClass().getSimpleName());
			
			// 3. build the pit
			pitResult = pitGenerator.generate(world, random, markerCoords, spawnCoords);

			if (!pitResult.isSuccess()) return result.fail();

			result.setData(pitResult.getData());
			Treasure.LOGGER.debug("Is pit generated: {}", pitResult.isSuccess());
			return result.success();
		}
		
		/**
		 * @param world
		 * @param random
		 * @param pos
		 * @param spawnYMin
		 * @return
		 */
		public static ICoords getUndergroundSpawnPos(IServerWorld world, Random random, ICoords pos, int spawnYMin) {
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
		 * @param world
		 * @param coords
		 * @param minDistance
		 * @return
		 */
		public boolean isRegisteredChestWithinDistance(World world, ICoords coords, int minDistance) {

			double minDistanceSq = minDistance * minDistance;

			// get a list of dungeons
			List<ChestInfo> infos = TreasureData.CHEST_REGISTRIES.get(WorldInfo.getDimension(world).toString()).getValues();

			if (infos == null || infos.size() == 0) {
				Treasure.LOGGER.debug("Unable to locate the ChestConfig Registry or the Registry doesn't contain any values");
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
	}

	/**
	 * 
	 * @return
	 */
	public Rarity getRarity() {
		return rarity;
	}
}
