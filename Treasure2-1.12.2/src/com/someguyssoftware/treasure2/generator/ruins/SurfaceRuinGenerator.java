/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.measurement.Quantity;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.DecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * 
 * @author Mark Gottschling on Dec 13, 2019
 *
 */
public class SurfaceRuinGenerator implements IRuinGenerator<GeneratorResult<TemplateGeneratorData>> {
	
	private static final double REQUIRED_BASE_SIZE = 80;
	private static final double REQUIRED_AIR_SIZE = 60;

	/**
	 * 
	 */
	public SurfaceRuinGenerator() {}
	
	// TODO create a generate() version that takes the template in as a param and the decay processor ... or they are member properties  and they method checks them first.
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords) {
		return generate(world, random, originalSpawnCoords, null);
	}
		
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords, IDecayRuleSet decayRuleSet) {		
		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		
		// get the template holder from the given archetype, type and biome
		TemplateHolder holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SURFACE, StructureType.RUIN);
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

		Treasure.logger.debug("original coords -> {}",originalSpawnCoords.toShortString());
		Treasure.logger.debug("actual coords -> {}", actualSpawnCoords.toShortString());
		
		// NOTE these checks don't really belong in a generator as their task is to just generate.
		// however, the template is unknown outside this call and thus the rotate, placement, size and actual coords would be unknown.
		/**
		 * Environment Checks
		 */
		actualSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, actualSpawnCoords);
		Treasure.logger.debug("surface coords -> {}", actualSpawnCoords.toShortString());
		if (actualSpawnCoords == WorldInfo.EMPTY_COORDS) {
			return result.fail();
		}
		
		// check if it has % land base
		for (int i = 0; i < 3; i++) {
			Treasure.logger.debug("finding solid base index -> {} at coords -> {}", i, actualSpawnCoords.toShortString());
			if (!WorldInfo.isSolidBase(world, actualSpawnCoords, templateSize.getX(), templateSize.getZ(), REQUIRED_BASE_SIZE)) {
				if (i == 2) {
					Treasure.logger.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", originalSpawnCoords.toShortString(), REQUIRED_BASE_SIZE, templateSize.getX(), templateSize.getZ());
					return result.fail();
				}
				else {
					actualSpawnCoords = actualSpawnCoords.add(0, -1, 0);
					Treasure.logger.debug("move actual spawn coords down for solid base check -> {}", actualSpawnCoords.toShortString());
				}
			}
			else {
				break;
			}
		}
		Treasure.logger.debug("using solid base coords -> {}", actualSpawnCoords.toShortString());
		
		// check if the plane above the actual spawn coords is % air
		Treasure.logger.debug("checking for {} % air at coords -> {} for dimensions -> {} x {}", REQUIRED_AIR_SIZE, actualSpawnCoords.add(0, 1, 0), templateSize.getX(), templateSize.getZ());
		if (!WorldInfo.isAirBase(world, actualSpawnCoords.add(0, 1, 0), templateSize.getX(), templateSize.getZ(), REQUIRED_AIR_SIZE)) {
			Treasure.logger.debug("Coords -> [{}] does not meet {} % air base requirements for size -> {} x {}", originalSpawnCoords.toShortString(), REQUIRED_AIR_SIZE, templateSize.getX(), templateSize.getZ());
			return result.fail();
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
			// create a decay processor
			decayRuleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(holder.getDecayRuleSetLocation().get(random.nextInt(holder.getDecayRuleSetLocation().size())).toString());
			Treasure.logger.debug("decayRuleSet -> {}", decayRuleSet.getName());
			// if decayRuleSet is null the processor should be null
		}
		if (decayRuleSet != null) {
			decayProcessor = new DecayProcessor(Treasure.instance.getInstance(), decayRuleSet);
		}
		
		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(world, random, decayProcessor, holder, placement, originalSpawnCoords);
		 if (!genResult.isSuccess()) return result.fail();

		Treasure.logger.debug("surface gen result -> {}", genResult);
		// get the chest coords
		ICoords chestCoords = genResult.getData().getChestCoords();
		if (chestCoords != null) {
			// move the chest coords to the first dry land beneath it.
			chestCoords = WorldInfo.getDryLandSurfaceCoords(world, chestCoords);
			if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
		}
		genResult.getData().setChestCoords(chestCoords);
		
		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<ICoords> spawnerCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<ICoords> proximityCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));

		// populate vanilla spawners
		buildVanillaSpawners(world, random, spawnerCoords);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, proximityCoords, new Quantity(1,2), 5D);
		
		result.setData(genResult.getData());

		return result.success();
	}
}
