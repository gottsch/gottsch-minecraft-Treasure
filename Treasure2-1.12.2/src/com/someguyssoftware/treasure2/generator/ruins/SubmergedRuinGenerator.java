/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubmergedRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {

	private static final double REQUIRED_BASE_SIZE = 50;

	/**
	 * 
	 */
	public SubmergedRuinGenerator() {}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords) {
		return generate(world, random, originalSpawnCoords, null, null);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords, IDecayRuleSet decayRuleSet) {
		return generate(world, random, originalSpawnCoords, null, decayRuleSet);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder) {
		return generate(world, random, originalSpawnCoords, holder, null);
	}

	@Override
	public GeneratorResult<ChestGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords, TemplateHolder holder, IDecayRuleSet decayRuleSet) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

		// TODO create a method selectTemplate() in abstract that will be overridden by concrete classes, provided the archetype and type

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		generator.setNullBlock(Blocks.AIR);

		// get the template holder from the given archetype, type and biome
		if (holder == null) {
			holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SUBMERGED, StructureType.RUIN);
		}
		if (holder == null) return result.fail();		

		// select a random rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.logger.debug("rotation used -> {}", rotation);

		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);

		// determine the actual spawn coords
		ICoords templateSize = new Coords(holder.getTemplate().transformedSize(placement.getRotation()));
		ICoords actualSpawnCoords = generator.getTransformedSpawnCoords(originalSpawnCoords, templateSize, placement);

		// NOTE these checks don't really belong in a generator as their task is to just generate.
		// however, the template is unknown outside this call and thus the rotate, placement, size and actual coords would be unknown.
		/**
		 * Environment Checks
		 */
		actualSpawnCoords = WorldInfo.getOceanFloorSurfaceCoords(world, actualSpawnCoords);
		Treasure.logger.debug("ocean floor coords -> {}", actualSpawnCoords.toShortString());

		// check if it has % land
		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(world, actualSpawnCoords, templateSize.getX(), templateSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.logger.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", REQUIRED_BASE_SIZE, originalSpawnCoords.toShortString(), templateSize.getX(), templateSize.getY());
					return result.fail();
				}
				else {
					actualSpawnCoords = actualSpawnCoords.add(0, -1, 0);
				}
			}
			else {
				break;
			}
		}

		/**
		 * Build
		 */
		// update original spawn coords' y-value to be that of actual spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), actualSpawnCoords.getY(), originalSpawnCoords.getZ());

		// NOTE don't like this here and then AGAIN in TemplateGenerator
		// get the rule set from the meta which is in the holder
		StructureMeta meta = (StructureMeta) Treasure.META_MANAGER.getMetaMap().get(holder.getMetaLocation().toString());
		if (meta == null) {
			Treasure.logger.debug("Unable to locate meta data for template -> {}", holder.getLocation());
			return result.fail();
		}

		// setup the decay ruleset and processor
		IDecayProcessor decayProcessor = null;
		if (decayRuleSet == null && holder.getDecayRuleSetLocation() != null && holder.getDecayRuleSetLocation().size() > 0) {
			// create a decay processor with a random rule set
			decayRuleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(holder.getDecayRuleSetLocation().get(random.nextInt(holder.getDecayRuleSetLocation().size())).toString());
			Treasure.logger.debug("decayRuleSet -> {}", decayRuleSet.getName());
			// if decayRuleSet is null the processor should be null
		}
		if (decayRuleSet != null) {
			decayProcessor = new DecayProcessor(Treasure.instance.getInstance(), decayRuleSet);
			decayProcessor.setBackFillBlockLayer1(Blocks.GRAVEL.getDefaultState());
		}

		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(world, random, decayProcessor, holder, placement, originalSpawnCoords);
		if (!genResult.isSuccess()) return result.fail();

		Treasure.logger.debug("submerged gen result -> {}", genResult);

		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<BlockContext> bossChestContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.BOSS_CHEST));
		List<BlockContext> chestContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
		List<BlockContext> spawnerContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<BlockContext> proximityContexts =
				(List<BlockContext>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		//		List<ICoords> spawnerCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		//		List<ICoords> proximityCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));

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
}
