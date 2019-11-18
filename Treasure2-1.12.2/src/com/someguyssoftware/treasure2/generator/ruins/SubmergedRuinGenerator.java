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
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
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
	
	/**
	 * 
	 */
	public SubmergedRuinGenerator() {}
	
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random,
			ICoords originalSpawnCoords) {
		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);
	
		// TODO can abstract to AbstractRuinGenerator which Submerged and Ruin implement.
		// TODO create a method selectTemplate() in abstract that will be overridden by concrete classes, provided the archetype and type
		
		/*
		 * Setup
		 */
		// get the biome ID
		Biome biome = world.getBiome(originalSpawnCoords.toPos());
		
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		generator.setNullBlock(Blocks.AIR);
		
		// get the template holder from the given archetype, type and biome
		TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplate(world, random, StructureArchetype.SUBMERGED, StructureType.RUIN, biome);
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

		// check if it has 50% land
		if (!WorldInfo.isSolidBase(world, actualSpawnCoords, 2, 2, 30)) {
			Treasure.logger.debug("Coords [{}] does not meet solid base requires for {} x {}", actualSpawnCoords.toShortString(), 3, 3);
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
				
		GeneratorResult<TemplateGeneratorData> genResult = generator.generate(world, random, holder, placement, originalSpawnCoords);
		 if (!genResult.isSuccess()) return result.fail();

		Treasure.logger.debug("submerged gen result -> {}", genResult);
			
		// interrogate info for spawners and any other special block processing (except chests that are handler by caller
		List<ICoords> spawnerCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.SPAWNER));
		List<ICoords> proximityCoords = (List<ICoords>) genResult.getData().getMap().get(GenUtil.getMarkerBlock(StructureMarkers.PROXIMITY_SPAWNER));
		
		// populate vanilla spawners
		buildVanillaSpawners(world, random, genResult.getData().getSpawnCoords(), spawnerCoords);
		
		// populate proximity spawners
		buildOneTimeSpawners(world, random, genResult.getData().getSpawnCoords(), proximityCoords, new Quantity(1,2), 5D);
		
		result.setData(genResult.getData());

		return result.success();
	}
	
	@Override
	public void buildOneTimeSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> proximityCoords, Quantity quantity, double d) {
		for (ICoords c : proximityCoords) {
			ICoords c2 = spawnCoords.add(c);
	    	world.setBlockState(c2.toPos(), TreasureBlocks.PROXIMITY_SPAWNER.getDefaultState());
	    	ProximitySpawnerTileEntity te = (ProximitySpawnerTileEntity) world.getTileEntity(c2.toPos());
	    	ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
	    	te.setMobName(r);
	    	te.setMobNum(new Quantity(1, 2));
	    	te.setProximity(5D);
		}
	}

	@Override
	public void buildVanillaSpawners(World world, Random random, ICoords spawnCoords, List<ICoords> spawnerCoords) {
		for (ICoords c : spawnerCoords) {
			ICoords c2 = spawnCoords.add(c);
			world.setBlockState(c2.toPos(), Blocks.MOB_SPAWNER.getDefaultState());
			TileEntityMobSpawner te = (TileEntityMobSpawner) world.getTileEntity(c2.toPos());
			ResourceLocation r = DungeonHooks.getRandomDungeonMob(random);
			te.getSpawnerBaseLogic().setEntityId(r);
		}
	}
}
