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
import com.someguyssoftware.gottschcore.world.gen.structure.DecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayRuleSet;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.DungeonHooks;

/**
 * 
 * @author Mark Gottschling on Dec 13, 2019
 *
 */
public class SurfaceRuinGenerator implements IRuinGenerator<GeneratorResult<TemplateGeneratorData>> {
	
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
		
		// check if it has 30% land
		if (!WorldInfo.isSolidBase(world, actualSpawnCoords, templateSize.getX(), templateSize.getZ(), 30)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", actualSpawnCoords.toShortString(), templateSize.getX(), templateSize.getZ());
			return result.fail();
		}
		
		for (int i = 0; i < 3; i++) {
			if (!WorldInfo.isSolidBase(world, actualSpawnCoords, templateSize.getX(), templateSize.getZ(), 50)) {
				if (i == 3) {
					Treasure.logger.debug("Coords -> [{}] does not meet {}% solid base requirements for size -> {} x {}", 50, originalSpawnCoords.toShortString(), templateSize.getX(), templateSize.getY());
					return result.fail();
				}
				else {
					originalSpawnCoords = originalSpawnCoords.add(0, -1, 0);
				}
			}
			else {
				continue;
			}
		}

		/**
		 * Build
		 */
		// update original spawn coords' y-value to be that of actual spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), actualSpawnCoords.getY(), originalSpawnCoords.getZ());
		
		Treasure.logger.debug("holder.location -> {}", holder.getLocation());
		Treasure.logger.debug("holder.meta -> {}", holder.getMetaLocation());
		Treasure.logger.debug("holder.decay -> {}", holder.getDecayRuleSetLocation());
		
		// NOTE don't like this here and then AGAIN in TemplateGenerator
		// get the rule set from the meta which is in the holder
		StructureMeta meta = (StructureMeta) Treasure.META_MANAGER.getMetaMap().get(holder.getMetaLocation().toString());
		if (meta == null) {
			Treasure.logger.debug("Unable to locate meta data for template -> {}", holder.getLocation());
			return result.fail();
		}
		
		// setup the decay ruleset and processor
		IDecayProcessor decayProcessor = null;
		if (decayRuleSet == null && holder.getDecayRuleSetLocation() != null) {
			// create a decay processor
			decayRuleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(holder.getDecayRuleSetLocation().toString());
			Treasure.logger.debug("decayRuleSet -> {}", decayRuleSet);
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
		
		// TODO these are wrong because we are adding the *new rotation calculated* spawnCoords and adding to the relative coords of the spawners
		// TODO they need to be relative to the original plus the offset.... this should be accomplished when records to real-world coords
		// populate vanilla spawners
		buildVanillaSpawners(world, random, spawnerCoords);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, proximityCoords, new Quantity(1,2), 5D);
		
		result.setData(genResult.getData());

		return result.success();
	}
}
