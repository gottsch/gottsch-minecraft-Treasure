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
 * @author Mark Gottschling on Aug 13, 2019
 *
 */
public class SubmergedRuinGenerator implements IRuinGenerator<GeneratorResult<TemplateGeneratorData>> {
	
	private static final double REQUIRED_BASE_SIZE = 50;

	/**
	 * 
	 */
	public SubmergedRuinGenerator() {}
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords) {
		return generate(world, random, originalSpawnCoords, null);
	}
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords, IDecayRuleSet decayRuleSet) {
		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);
	
		// TODO create a method selectTemplate() in abstract that will be overridden by concrete classes, provided the archetype and type
		
		/*
		 * Setup
		 */
		// get the biome ID
//		Biome biome = world.getBiome(originalSpawnCoords.toPos());
		
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		generator.setNullBlock(Blocks.AIR);
		
		// get the template holder from the given archetype, type and biome
//		TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplate(world, random, StructureArchetype.SUBMERGED, StructureType.RUIN, biome);
		TemplateHolder holder = selectTemplate(world, random, originalSpawnCoords, StructureArchetype.SUBMERGED, StructureType.RUIN);
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

		Treasure.logger.debug("submerged gen result -> {}", genResult);
			
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
