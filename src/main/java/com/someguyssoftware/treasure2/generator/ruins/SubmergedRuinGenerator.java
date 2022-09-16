/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;

/**
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubmergedRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {

	private static final double REQUIRED_BASE_SIZE = 50;
	private static final double REQUIRED_WATER_SIZE = 30;

	/**
	 * 
	 */
	public SubmergedRuinGenerator() {}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords) {
		return generate(world, generator, random, originalSpawnCoords, null, null);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords, IDecayRuleSet decayRuleSet) {
		return generate(world, generator, random, originalSpawnCoords, null, decayRuleSet);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator generator, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder) {
		return generate(world, generator, random, originalSpawnCoords, holder, null);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(IServerWorld world, ChunkGenerator chunkGenerator, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder, IDecayRuleSet decayRuleSet) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		// TODO create a method selectTemplate() in abstract that will be overridden by concrete classes, provided the archetype and type

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator templateGenerator = new TemplateGenerator();
		templateGenerator.setNullBlock(Blocks.AIR);

		// get the template holder from the given archetype, type and biome
		if (holder == null) {
			holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SUBMERGED, StructureType.RUIN);
		}
		if (holder == null) return result.fail();		

		GottschTemplate template = (GottschTemplate) holder.getTemplate();
		Treasure.LOGGER.debug("selected template holder -> {} : {}", holder.getLocation(), holder.getMetaLocation());
		if (template == null) {
			Treasure.LOGGER.debug("could not find random template");
			return result.fail();
		}
		
		// find the 'entrance' block
		ICoords entranceCoords = TreasureTemplateRegistry.getManager().getOffset(random, holder, StructureMarkers.ENTRANCE);
		if (entranceCoords == null) {
			Treasure.LOGGER.debug("Unable to locate entrance position.");
			return result.fail();
		}
		Treasure.LOGGER.debug("entrance coords -> {}", entranceCoords.toShortString());
		
		// select a random rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("rotation used -> {}", rotation);

		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);

		// NOTE these values are still relative to origin (spawnCoords);
		ICoords newEntrance = new Coords(GottschTemplate.transformedVec3d(placement, entranceCoords.toVec3d()));
		Treasure.LOGGER.debug("new entrance coords -> {}", newEntrance.toShortString());
	
		// determine the rotated spawn coords
		ICoords templateSize = new Coords(holder.getTemplate().getSize(placement.getRotation()));
//		ICoords rotatedSpawnCoords = generator.getTransformedSpawnCoords(originalSpawnCoords, templateSize, placement);

		/*
		 * we want to align the new entrance (rotated structure) to the center of the chunk ie. original spawn
		 */
		BlockPos transformedSize = template.getSize(rotation);
		ICoords alignedSpawnCoords = align(originalSpawnCoords, newEntrance, transformedSize, placement);
		Treasure.LOGGER.debug("aligned spawn coords -> {}", alignedSpawnCoords.toShortString());

		// NOTE these checks don't really belong in a generator as their task is to just generate.
		// however, the template is unknown outside this call and thus the rotate, placement, size and rotated coords would be unknown.
		/**
		 * Environment Checks
		 */
		if (chunkGenerator == null) {
			alignedSpawnCoords = WorldInfo.getOceanFloorSurfaceCoords(world, alignedSpawnCoords);
		}
		else {
			alignedSpawnCoords = WorldInfo.getOceanFloorSurfaceCoords(world, chunkGenerator, alignedSpawnCoords);
		}
		Treasure.LOGGER.debug("ocean floor coords -> {}", alignedSpawnCoords.toShortString());

		// check if it has % land
		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(world, alignedSpawnCoords, templateSize.getX(), templateSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.LOGGER.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", REQUIRED_BASE_SIZE, alignedSpawnCoords.toShortString(), templateSize.getX(), templateSize.getY());
					return result.fail();
				}
				else {
					alignedSpawnCoords = alignedSpawnCoords.add(0, -1, 0);
				}
			}
			else {
				break;
			}
		}
		// TODO check if it has % air/water
		// TODO get the height of template, - don't get the immediate plane above spawn because we want the template "buried" a little bit - unless the template isn't very tall
		
		int offset = 1;
		if (templateSize.getY() > 6) {
			offset = 3;
		}
		else if (templateSize.getY() >=4) {
			offset =2;
		}

		Treasure.LOGGER.debug("checking for {} % water using offset of -> {} at coords -> {} for dimensions -> {} x {}", REQUIRED_WATER_SIZE, offset, alignedSpawnCoords.add(0, offset, 0), templateSize.getX(), templateSize.getZ());
		if (!WorldInfo.isLiquidBase(world, alignedSpawnCoords.add(0, offset, 0), templateSize.getX(), templateSize.getZ(), REQUIRED_WATER_SIZE)) {
			Treasure.LOGGER.debug("Coords -> [{}] does not meet {} % water base requirements for size -> {} x {}", alignedSpawnCoords.toShortString(), REQUIRED_WATER_SIZE, templateSize.getX(), templateSize.getZ());
			return result.fail();
		}

		/**
		 * Build
		 */
		// update original spawn coords' y-value to be that of aligned spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place, because it will perform the rotation
		// when generating and need it to be at the correct height.
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), alignedSpawnCoords.getY(), originalSpawnCoords.getZ());

		// NOTE don't like this here and then AGAIN in TemplateGenerator
		
		// RESEARCH - what was the point of this?
		// get the rule set from the meta which is in the holder
//		StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(holder.getMetaLocation().toString());
//		if (meta == null) {
//			Treasure.LOGGER.debug("Unable to locate meta data for template -> {}", holder.getLocation());
//			return result.fail();
//		}

		// setup the decay ruleset and processor
		IDecayProcessor decayProcessor = null;
//		if (decayRuleSet == null && holder.getDecayRuleSetLocation() != null && holder.getDecayRuleSetLocation().size() > 0) {
//			// create a decay processor with a random rule set
//			decayRuleSet = TreasureDecayRegistry.getDecayManager().getRuleSetMap().get(holder.getDecayRuleSetLocation().get(random.nextInt(holder.getDecayRuleSetLocation().size())).toString());
//			Treasure.LOGGER.debug("decayRuleSet -> {}", decayRuleSet.getName());
//			// if decayRuleSet is null the processor should be null
//		}
//		if (decayRuleSet != null) {
//			decayProcessor = new DecayProcessor(Treasure.instance.getInstance(), decayRuleSet);
//			decayProcessor.setBackFillBlockLayer1(Blocks.GRAVEL.defaultBlockState());
//		}

		GeneratorResult<TemplateGeneratorData> genResult = templateGenerator.generate(world, random, decayProcessor, holder, placement, originalSpawnCoords);
		if (!genResult.isSuccess()) return result.fail();

		Treasure.LOGGER.debug("submerged gen result -> {}", genResult);

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockContext> bossChestContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.BOSS_CHEST));
		List<BlockContext> chestContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
		List<BlockContext> spawnerContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockContext> proximityContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));

		/*
		 *  NOTE currently only 1 chest is allowed per structure - the rest are ignored.
		 */
		// check if there is a boss chest(s)
		BlockContext chestContext = null;
		if (bossChestContexts != null && bossChestContexts.size() > 0) {
			if (bossChestContexts.size() > 1) {
				chestContext = bossChestContexts.get(random.nextInt(bossChestContexts.size()));
			}
			else {
				chestContext = bossChestContexts.get(0);
			}			
		}
		
		// TODO turn these checks into methods
		// if a boss chest wasn't found, search for regular chests
		if (chestContext == null) {
			if (chestContexts != null && chestContexts.size() > 0) {
				if (chestContexts.size() > 1) {
					chestContext = chestContexts.get(random.nextInt(chestContexts.size()));
				}
				else {
					chestContext = chestContexts.get(0);
				}			
			}			
		}
	
//		ICoords chestCoords = null;
//		if (chestContext != null) {
//			chestCoords = chestContext.getCoords();
//		}
		
		// populate vanilla spawners
//		buildVanillaSpawners(world, random, spawnerCoords);

		// populate proximity spawners
//		buildOneTimeSpawners(world, random, proximityCoords, new Quantity(1,2), 5D);

		buildVanillaSpawners(world, random, spawnerContexts);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, proximityContexts, new Quantity(1,2), 5D);
		
//		result.setData(genResult.getData());
		// copy all data from genResult
//		result.setData((ChestGeneratorData2) genResult.getData());
		result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
		
		// update with chest context
		result.getData().setChestContext(chestContext);

		return result.success();
	}	
	
	private ICoords align(ICoords spawnCoords, ICoords newEntrance, BlockPos transformedSize, PlacementSettings placement) {
		ICoords startCoords = null;
		// NOTE work with rotations only for now
		
		// first offset spawnCoords by newEntrance
		startCoords = spawnCoords.add(-newEntrance.getX(), 0, -newEntrance.getZ());
		
		return startCoords;
	}
}