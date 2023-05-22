/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.template;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.BlockInfoContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

// TODO getMarkerBlock should be in TemplateGenerator as well (passed in)
// TODO move TemplateGenerator to world.gen.structure (in gottschcore)

/**
 * 
 * @author Mark Gottschling on Jan 24, 2019
 *
 */
public class TemplateGenerator implements ITemplateGenerator<GeneratorResult<TemplateGeneratorData>> {
	// facing property of a vanilla chest
	private static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final EnumProperty<Direction> CHEST_FACING = DirectionProperty.create("facing", Direction.class);

	private Block nullBlock;

	// TODO constructor should probably take in null block or list of null blocks,
	// markersblocks, and replacement blocks.
	//		--> then you would have specific TemplateGenerators created for the specific templates.
	//		--> better to pass everything into the generate() method ??
	public TemplateGenerator() {
		// TODO need a list of null blocks
		// use the default null block
		setNullBlock(GeneratorUtil.getMarkerBlock(StructureMarkers.NULL));
	}
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context,  GottschTemplate template,
			PlacementSettings placement, ICoords coords) {
		return generate(context, template, placement, coords, () -> new HashMap<BlockState, BlockState>(), Coords.EMPTY);
	}
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context,  GottschTemplate template,
			PlacementSettings placement, ICoords coords, ICoords offset) {
		return generate(context, template, placement, coords, () -> new HashMap<BlockState, BlockState>(), offset);
	}
	
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context,  GottschTemplate template,
			PlacementSettings placement, ICoords coords, Supplier<Map<BlockState, BlockState>> consumerReplacmentMap,
			ICoords offsetCoords) {

		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);
		Treasure.LOGGER.debug("template size -> {}", template.getSize());

		// find the offset block
//		int offset = 0;
		// TODO how to get the offset at this point? don't have the resourcelocation, the holder, or value
		if (offsetCoords == null || offsetCoords == Coords.EMPTY) {
			offsetCoords = template.findCoords(context.random(), GeneratorUtil.getMarkerBlock(StructureMarkers.OFFSET));
//			if (offsetCoords != null) {
//				offset = -offsetCoords.getY();
//			}
			// if offset is still null/empty
			if (offsetCoords == null || offsetCoords == Coords.EMPTY) {
				offsetCoords = new Coords(0, 0, 0);
			}
		}
		
		// update the spawn coords with the offset
		ICoords spawnCoords = coords.add(offsetCoords);
//		Treasure.LOGGER.debug("spawn coords with offset -> {}", spawnCoords);
		
		// build the replacement map
		Map<BlockState, BlockState> m = consumerReplacmentMap.get();
		m.putAll(TreasureTemplateRegistry.getReplacementMap());
		
		// generate the structure
		template.placeInWorld(context.level(), spawnCoords.toPos(), spawnCoords.toPos(), placement, 
					getNullBlock(), m, context.random(), 3);


		// process all structure markers, positioning absolutely
		for (Entry<Block, BlockInfoContext> entry : template.getTagBlockMap().entries()) {
			BlockInfoContext blockContext = getAbsoluteTransformedContext(entry.getValue(), spawnCoords, placement);
			result.getData().getMap().put(entry.getKey(), blockContext);
			Treasure.LOGGER.debug("new: adding to structure info absoluted transformed coords -> {} : {}",
					entry.getKey().getRegistryName(), blockContext.getCoords().toShortString());
		}

		// get the transformed size
		BlockPos transformedSize = template.getSize(placement.getRotation());
//		Treasure.LOGGER.debug("transformed size -> {}", transformedSize.toString());

		// calculate the new spawn coords - that includes the rotation, and negates the
		// Y offset
		spawnCoords = getTransformedSpawnCoords(spawnCoords, new Coords(transformedSize), placement).add(offsetCoords.negate()); //.add(0, -offset, 0);

//		Treasure.LOGGER.debug("spawn coords after rotation -> " + spawnCoords);
		// update result data
		result.getData().setSpawnCoords(spawnCoords);
		result.getData().setSize(new Coords(transformedSize));

		return result.success();
	}

	/**
	 * 
	 * @param contextIn
	 * @param spawnCoords
	 * @param placement
	 * @return
	 */
	private BlockInfoContext getAbsoluteTransformedContext(BlockInfoContext contextIn, ICoords spawnCoords,
			PlacementSettings placement) {

		// get the absolute coords of chest
		ICoords coords = new Coords(GottschTemplate.transform(placement, contextIn.getCoords()));
		coords = spawnCoords.add(coords);

		// get the block state of the chest
		BlockState state;
		BlockState chestState = contextIn.getState();
		chestState = chestState.mirror(placement.getMirror());
		chestState = chestState.rotate(placement.getRotation());
		if (chestState.getProperties().contains(FACING)) {
			BlockState modState = TreasureBlocks.WOOD_CHEST.get().defaultBlockState().setValue(CHEST_FACING,
					(Direction) chestState.getValue(FACING));
			state = modState;
		} else {
			state = contextIn.getState();
		}

		return new BlockInfoContext(contextIn.getBlockInfo(), coords, state);
	}

	/**
	 * 
	 * @param coords
	 * @param size
	 * @param placement
	 * @return
	 */
	public ICoords getTransformedSpawnCoords(final ICoords coords, final ICoords size,
			final PlacementSettings placement) {

		ICoords spawnCoords = null;
		int x = 0;
		int z = 0;
		switch (placement.getRotation()) {
		case NONE:
			x = coords.getX();
			z = coords.getZ();
			break;
		case CLOCKWISE_90:
			x = coords.getX() - (size.getZ() - 1);
			z = coords.getZ();
			break;
		case CLOCKWISE_180:
			x = coords.getX() - (size.getX() - 1);
			z = coords.getZ() - (size.getZ() - 1);
			break;
		case COUNTERCLOCKWISE_90:
			x = coords.getX();
			z = coords.getZ() - (size.getX() - 1);
			break;
		default:
			break;
		}
		spawnCoords = new Coords(x, coords.getY(), z);
		return spawnCoords;
	}

	@Override
	public Block getNullBlock() {
		if (nullBlock == null) {
			nullBlock = GeneratorUtil.getMarkerBlock(StructureMarkers.NULL);
		}
		return nullBlock;
	}

	@Override
	public void setNullBlock(Block nullBlock) {
		this.nullBlock = nullBlock;
	}

}