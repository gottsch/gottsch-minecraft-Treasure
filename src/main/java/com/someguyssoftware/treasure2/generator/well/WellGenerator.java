/**
 * 
 */
package com.someguyssoftware.treasure2.generator.well;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.gottschcore.world.gen.structure.PlacementSettings;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.IWellsConfig;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureArchetype;
import com.someguyssoftware.treasure2.meta.StructureType;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

/**
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class WellGenerator implements IWellGenerator<GeneratorResult<GeneratorData>> {

	@Override
	public GeneratorResult<GeneratorData> generate(IWorld world, Random random,
			ICoords originalSpawnCoords, IWellsConfig config) {
		return generate(world, random, originalSpawnCoords, null, config);
	}
	
	@Override
	public GeneratorResult<GeneratorData> generate(IWorld world, Random random,
			ICoords originalSpawnCoords, TemplateHolder templateHolder, IWellsConfig config) {
		/*
		 * Setup
		 */
		GeneratorResult<GeneratorData> result = new GeneratorResult<>(GeneratorData.class);
		
		// get the biome ID
		Biome biome = world.getBiome(originalSpawnCoords.toPos());
		
		// create the generator
		TemplateGenerator generator = new TemplateGenerator();
		generator.setNullBlock(Blocks.BEDROCK);
		
		// get the template
		if (templateHolder == null) {
			Treasure.LOGGER.debug("find well template by archetype -> {}, type -> {}, biome -> {}", StructureArchetype.SURFACE, StructureType.WELL, biome.getRegistryName());
			templateHolder = TreasureTemplateRegistry.getTemplateManager().getTemplate(world, random, StructureArchetype.SURFACE, StructureType.WELL, biome);
		}
		Treasure.LOGGER.debug("templateHolder -> {}", templateHolder);
		if (templateHolder == null) return result.fail();
				
		// select a random rotation
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		Treasure.LOGGER.debug("with rotation -> {}", rotation);
		// setup placement
		PlacementSettings placement = new PlacementSettings();
		placement.setRotation(rotation).setRandom(random);
		
		ICoords templateSize = new Coords(templateHolder.getTemplate().transformedSize(rotation));
		ICoords actualSpawnCoords = generator.getTransformedSpawnCoords(originalSpawnCoords, templateSize, placement);
			
		/*
		 * Environment Checks
		 */
		// 1. determine y-coord of land surface for the actual spawn coords
//		actualSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(actualSpawnCoords.getX(), 255, actualSpawnCoords.getZ()));
		actualSpawnCoords = WorldInfo.getDryLandSurfaceCoords(world, actualSpawnCoords.withY(255));
		if (actualSpawnCoords == null || actualSpawnCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.LOGGER.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return result.fail(); 
		}
		Treasure.LOGGER.debug("actual spawn coords after dry land surface check -> {}", actualSpawnCoords);
		
		// 2. check if it has 50% land
		if (!WorldInfo.isSolidBase(world, actualSpawnCoords, 3, 3, 50)) {
			Treasure.LOGGER.debug("Coords [{}] does not meet solid base requires for {} x {}", actualSpawnCoords.toShortString(), 3, 3);
			return result.fail();
		}	
		
		/*
		 * Build
		 */
		// update original spawn coords' y-value to be that of actual spawn coords.
		// this is the coords that need to be supplied to the template generator to allow
		// the structure to generator in the correct place
		originalSpawnCoords = new Coords(originalSpawnCoords.getX(), actualSpawnCoords.getY(), originalSpawnCoords.getZ());
		Treasure.LOGGER.debug("Well original spawn coords -> {}", originalSpawnCoords.toShortString());
		// build well
		 GeneratorResult<TemplateGeneratorData> genResult = generator.generate(world, random, templateHolder,  placement, originalSpawnCoords);
		Treasure.LOGGER.debug("Well gen  structure result -> {}", genResult.isSuccess());
		 if (!genResult.isSuccess()) {
			 Treasure.LOGGER.debug("failing well gen.");
			return result.fail();
		}
		
		// get the rotated/transformed size
		//BlockPos transformedSize = holder.getTemplate().transformedSize(rotation);
		ICoords transformedSize = genResult.getData().getSize();
		Treasure.LOGGER.debug("Well transformed size -> {}", transformedSize.toShortString());
		// add flowers around well
		addDecorations(world, random, genResult.getData().getSpawnCoords(), transformedSize.getX(), transformedSize.getZ());
		 
		// TODO add chest if any
				
		// add the structure data to the result
		result.setData(genResult.getData());

		Treasure.LOGGER.info("CHEATER! Wishing Well at coords: {}", result.getData().getSpawnCoords().toShortString());
		
		return result.success();
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param width
	 * @param depth
	 */
	public void addDecorations(IWorld world, Random random, ICoords coords, int width, int depth) {
		ICoords startCoords = coords.add(-1, 0, -1);
	
		// TODO change to scan the entire size (x,z) of well footprint and detect the edges ... place flowers adjacent to edge blocks. 
		
		// north of well
		for (int widthIndex = 0; widthIndex <= width + 1; widthIndex++) {
			if (RandomHelper.randomInt(0, 1) == 0) {
//				ICoords decoCoords = startCoords.add(widthIndex, 0, 0);
				addDecoration(world, random, startCoords.add(widthIndex, 0, 0));
			}
		}
		
		// south of well
		startCoords = coords.add(-1, 0, depth);
		for (int widthIndex = 0; widthIndex <= width + 1; widthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(world, random, startCoords.add(widthIndex, 0, 0));
			}
		}
		
		// west of well
		startCoords = coords.add(-1, 0, 0);
		for (int depthIndex = 0; depthIndex < depth-1; depthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(world, random, startCoords.add(0, 0, depthIndex));
			}
		}
		
		// east of well
		startCoords = coords.add(width, 0, 0);
		for (int depthIndex = 0; depthIndex < depth-1; depthIndex++) {
			if (RandomHelper.randomInt(0,  1) == 0) {
				addDecoration(world, random, startCoords.add(0, 0, depthIndex));
			}
		}
	}
	
	@Override
	public void addDecoration(IWorld world, Random random, ICoords coords) {
		BlockState blockState = null;
		ICoords markerCoords = WorldInfo.getDryLandSurfaceCoords(world, coords);
		
		if (markerCoords == null || markerCoords == WorldInfo.EMPTY_COORDS) {
			Treasure.LOGGER.debug("Returning due to marker coords == null or EMPTY_COORDS");
			return;
		}
		BlockContext markerContext = new BlockContext(world, markerCoords);
		if (!markerContext.isAir() && !markerContext.isReplaceable()) {
			Treasure.LOGGER.debug("Returning due to marker coords is not air nor replaceable.");
			return;
		}
		
		markerContext = new BlockContext(world, markerCoords.add(0, -1, 0));
		Treasure.LOGGER.debug("Marker on block: {}", markerContext.getState());
		if (markerContext.equalsBlock(Blocks.GRASS_BLOCK) || markerContext.equalsBlock(Blocks.DIRT)) {
			blockState = FLOWERS.get(random.nextInt(FLOWERS.size())).getDefaultState();
		}
		else if (markerContext.equalsBlock(Blocks.COARSE_DIRT)) {
			blockState = Blocks.TALL_GRASS.getDefaultState();
		}
		else if (markerContext.equalsBlock(Blocks.MYCELIUM) || markerContext.equalsBlock(Blocks.PODZOL)) {
			blockState = MUSHROOMS.get(random.nextInt(MUSHROOMS.size())).getDefaultState();			
		}
		else {
			blockState = TALL_PLANTS.get(random.nextInt(TALL_PLANTS.size())).getDefaultState();
		}				
		// set the block state
		world.setBlockState(coords.toPos(), blockState, 3);
	}
}