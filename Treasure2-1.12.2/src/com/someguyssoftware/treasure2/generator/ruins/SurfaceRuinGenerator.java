/**
 * 
 */
package com.someguyssoftware.treasure2.generator.ruins;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
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

import net.minecraft.block.material.Material;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

/**
 * 
 * @author Mark Gottschling on Dec 13, 2019
 *
 */
public class SurfaceRuinGenerator implements IRuinGenerator<GeneratorResult<ChestGeneratorData>> {
	
	private static final double REQUIRED_BASE_SIZE = 45;
	private static final double REQUIRED_AIR_SIZE = 30;

	/**
	 * 
	 */
	public SurfaceRuinGenerator() {}
	
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

		/*
		 * Setup
		 */
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		
		// get the template holder from the given archetype, type and biome
		if (holder == null) {
			holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SURFACE, StructureType.RUIN);
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
		
//		if (chestCoords != null) {
//			// move the chest coords to the first solid block beneath it.
//			chestCoords = getSolidSurfaceCoords(world, chestCoords.up(1));
//			if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
//		}
//		genResult.getData().setChestCoords(chestCoords);
//		
//		
//		if (proximityCoords != null)
//			Treasure.logger.debug("Proximity spawners size -> {}", proximityCoords.size());
//		else
//			Treasure.logger.debug("No proximity spawners found.");
//		
//		// populate vanilla spawners
//		buildVanillaSpawners(world, random, spawnerCoords);
//		
//		// populate proximity spawners
//		buildOneTimeSpawners(world, random, proximityCoords, new Quantity(1,2), 5D);
//		
//		result.setData(genResult.getData());

		ICoords chestCoords = null;
		if (chestContext != null) {
			// move the chest coords to the first solid block beneath it.
			chestCoords = getSolidSurfaceCoords(world, chestContext.getCoords());
			if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
			chestContext.setCoords(chestCoords);
		}
		if (chestCoords == null) {
			return result.fail();
		}
		
		if (proximityContexts != null)
			Treasure.logger.debug("Proximity spawners size -> {}", proximityContexts.size());
		else
			Treasure.logger.debug("No proximity spawners found.");
		
		// populate vanilla spawners
		buildVanillaSpawners(world, random, spawnerContexts);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, proximityContexts, new Quantity(1,2), 5D);
		
		// copy all data from genResult
//		result.setData((IGeneratorData) genResult.getData()); // <-- this should work
		result.getData().setSpawnCoords(genResult.getData().getSpawnCoords());
		
		// update with chest context
		result.getData().setChestContext(chestContext);
		
		return result.success();
	}
	
	/**
	 * NOTE candidate for GottschCore
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords getSolidSurfaceCoords(final World world, final ICoords coords) {
		boolean isSurfaceBlock = false;
		ICoords newCoords = coords;
		
		while (!isSurfaceBlock) {
			// get the cube that is 1 below current position
			Cube cube = new Cube(world, newCoords.down(1));
//			Treasure.logger.debug("below block -> {} @ {}", cube.getState().getBlock().getRegistryName(), cube.getCoords().toShortString());
			// exit if not valid Y coordinate
			if (!WorldInfo.isValidY(cube.getCoords())) {
				return WorldInfo.EMPTY_COORDS;
			}	

			if (cube.equalsMaterial(Material.AIR) || cube.isReplaceable()
					|| cube.equalsMaterial(Material.LEAVES) || cube.isLiquid()
					|| cube.isBurning(world)) {
//				Treasure.logger.debug("block is air, leaves, replacable, liquid or burning");
				newCoords = newCoords.down(1);
			}
			else {
				isSurfaceBlock = true;
			}		
		}
		return newCoords;
	}
}
