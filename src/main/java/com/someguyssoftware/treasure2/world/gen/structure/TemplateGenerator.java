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

import java.util.Map.Entry;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.gottschcore.world.gen.structure.GottschTemplate;
import com.someguyssoftware.gottschcore.world.gen.structure.IDecayProcessor;
import com.someguyssoftware.gottschcore.world.gen.structure.StructureMarkers;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.TemplateGeneratorData;
import com.someguyssoftware.treasure2.meta.StructureMeta;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;

// TODO getMarkerBlock should be in TemplateGenerator as well (passed in)
// TODO move TemplateGenerator to world.gen.structure (in gottschcore)
// TODO structure gen should probably pass in the replacement map

/**
 * 
 * @author Mark Gottschling on Jan 24, 2019
 *
 */
public class TemplateGenerator implements ITemplateGenerator<GeneratorResult<TemplateGeneratorData>> {
	// facing property of a vanilla chest
	private static final PropertyDirection FACING = BlockHorizontal.FACING;
	private static final PropertyEnum<EnumFacing> CHEST_FACING = PropertyDirection.create("facing", EnumFacing.class);

	private Block nullBlock;

	// TODO constructor should probably take in null block or list of null blocks,
	// markersblocks, and replacement blocks
	public TemplateGenerator() {
		// use the default null block
		setNullBlock(GenUtil.getMarkerBlock(StructureMarkers.NULL));
	}

	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random, TemplateHolder templateHolder,
			PlacementSettings placement, ICoords coords) {
		return generate(world, random, null, templateHolder, placement, coords);
	}

	/**
	 * 
	 */
	@Override
	public GeneratorResult<TemplateGeneratorData> generate(World world, Random random, IDecayProcessor decayProcessor,
			TemplateHolder templateHolder, PlacementSettings placement, ICoords coords) {

		GeneratorResult<TemplateGeneratorData> result = new GeneratorResult<>(TemplateGeneratorData.class);

		GottschTemplate template = (GottschTemplate) templateHolder.getTemplate();
		Treasure.LOGGER.debug("template size -> {}", template.getSize());
		// get the meta
		StructureMeta meta = (StructureMeta) TreasureMetaRegistry.get(templateHolder.getMetaLocation().toString());
		if (meta == null) {
			Treasure.LOGGER.debug("Unable to locate meta data for template -> {}", templateHolder.getLocation());
			return result.fail();
		}
		Treasure.LOGGER.debug("meta -> {}", meta);

		// if the meta provides a null block, use it
		if (meta.getNullBlockName() != null && !meta.getNullBlockName().equals("")) {
			Treasure.LOGGER.debug("setting the null block to -> {}", meta.getNullBlockName());
			setNullBlock(Block.getBlockFromName(meta.getNullBlockName()));
		}

		// find the offset block
		int offset = 0;
		ICoords offsetCoords = null;
		if (meta.getOffset() != null) {
			// NOTE going to need to negate meta offset since a negative value will be
			// provided for downward movement, whereas
			// an offset derived from a template will always be positive and thus is negated
			// later to correct the positioning.
			offsetCoords = new Coords(0, -meta.getOffset().getY(), 0);
			Treasure.LOGGER.debug("Using meta offset coords -> {}", offsetCoords);
		} else {
			offsetCoords = template.findCoords(random, GenUtil.getMarkerBlock(StructureMarkers.OFFSET));
		}

		if (offsetCoords != null) {
			offset = -offsetCoords.getY();
		}

		// update the spawn coords with the offset
		ICoords spawnCoords = coords.add(0, offset, 0);

		// generate the structure
		if (decayProcessor == null) {
			Treasure.LOGGER.debug("no decay processor found.");
			template.addBlocksToWorld(world, spawnCoords.toPos(), placement, getNullBlock(),
					TreasureTemplateRegistry.getManager().getReplacementMap(), 3);
		} else {
			decayProcessor.setDecayStartY(Math.abs(offset));
			template.addBlocksToWorld(world, spawnCoords.toPos(), decayProcessor, placement, getNullBlock(),
					TreasureTemplateRegistry.getManager().getReplacementMap(), 3);
		}

		// process all markers and adding them to the result data (absolute positioned)
//		for (Entry<Block, StructureMarkerContext> entry : template.getMarkerMap().entries()) {
//			ICoords c = new Coords(GottschTemplate.transformedCoords(placement, entry.getValue().getCoords()));
//			c = spawnCoords.add(c);
//			result.getData().getMap().put(entry.getKey(), c);
//			Treasure.logger.debug("adding to structure info absoluted transformed coords -> {} : {}", entry.getKey().getLocalizedName(), c.toShortString());
//		}
//		for (Entry<Block, BlockContext> entry : template.getTagBlockMap().entries()) {
//			ICoords c = new Coords(GottschTemplate.transformedCoords(placement, entry.getValue().getCoords()));
//			c = spawnCoords.add(c);
//			result.getData().getMap().put(entry.getKey(), new BlockContext(c, entry.getValue().getState()));
//			Treasure.logger.debug("old: adding to structure info absoluted transformed coords -> {} : {}",
//					entry.getKey().getLocalizedName(), c.toShortString());
//		}

		// TODO this will be replaced with the below
		// find the chest and update chest coords (absolute positioned)
//		List<StructureMarkerContext> contextList = (List<StructureMarkerContext>) template.getMarkerMap().get(GenUtil.getMarkerBlock(StructureMarkers.CHEST));
//		if (!contextList.isEmpty()) {
//			StructureMarkerContext context = contextList.get(0);
//			ICoords chestCoords = new Coords(GottschTemplate.transformedCoords(placement, context.getCoords()));
//			// get the absolute coords of chest
//			chestCoords = spawnCoords.add(chestCoords);
//			// set the chest coords in the result data
//			result.getData().setChestCoords(chestCoords);
//			// get the block state of the chest
//			IBlockState chestState = context.getState();
//			chestState = chestState.withMirror(placement.getMirror());
//			chestState = chestState.withRotation(placement.getRotation());
//			 if (chestState.getProperties().containsKey(FACING)) {
//				 IBlockState modState= TreasureBlocks.WOOD_CHEST.getDefaultState().withProperty(CHEST_FACING, (EnumFacing)chestState.getProperties().get(FACING));
//				 result.getData().setChestState(modState);
////				 Treasure.logger.debug("saving chest state -> {}", modState.toString());
//			 }
//		}

		// process all strcture markers, positioning absolutely
		for (Entry<Block, BlockContext> entry : template.getTagBlockMap().entries()) {
			BlockContext context = getAbsoluteTransformedContext(entry.getValue(), spawnCoords, placement);
			result.getData().getMap().put(entry.getKey(), context);
			Treasure.LOGGER.debug("new: adding to structure info absoluted transformed coords -> {} : {}",
					entry.getKey().getLocalizedName(), context.getCoords().toShortString());
		}

		// get the transformed size
		BlockPos transformedSize = template.transformedSize(placement.getRotation());
//		Treasure.logger.debug("transformed size -> {}", transformedSize.toString());

		// calculate the new spawn coords - that includes the rotation, and negates the
		// Y offset
		spawnCoords = getTransformedSpawnCoords(spawnCoords, new Coords(transformedSize), placement).add(0, -offset, 0);

		Treasure.LOGGER.debug("spawn coords after rotation -> " + spawnCoords);
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
	private BlockContext getAbsoluteTransformedContext(BlockContext contextIn, ICoords spawnCoords,
			PlacementSettings placement) {
		BlockContext context = new BlockContext();

		// get the absolute coords of chest
		ICoords coords = new Coords(GottschTemplate.transformedCoords(placement, contextIn.getCoords()));
		coords = spawnCoords.add(coords);
		context.setCoords(coords);

		// get the block state of the chest
		IBlockState chestState = contextIn.getState();
		chestState = chestState.withMirror(placement.getMirror());
		chestState = chestState.withRotation(placement.getRotation());
		if (chestState.getProperties().containsKey(FACING)) {
			IBlockState modState = TreasureBlocks.WOOD_CHEST.getDefaultState().withProperty(CHEST_FACING,
					(EnumFacing) chestState.getProperties().get(FACING));
			context.setState(modState);
		} else {
			context.setState(contextIn.getState());
		}
		return context;
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
			nullBlock = GenUtil.getMarkerBlock(StructureMarkers.NULL);
		}
		return nullBlock;
	}

	@Override
	public void setNullBlock(Block nullBlock) {
		this.nullBlock = nullBlock;
	}

}