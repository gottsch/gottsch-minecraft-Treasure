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

import java.util.Map;
import java.util.function.Supplier;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.gen.structure.GottschTemplate;
import mod.gottsch.forge.gottschcore.world.gen.structure.PlacementSettings;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.TemplateGeneratorData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


/**
 * 
 * @author Mark Gottschling on Jul 6, 2019
 *
 */
public interface ITemplateGenerator<RESULT extends IGeneratorResult<?>> {

	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context, GottschTemplate template, 
			PlacementSettings settings, ICoords spawnCoords);
	
	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context, GottschTemplate template, 
			PlacementSettings settings, ICoords spawnCoords, ICoords offset);

	public GeneratorResult<TemplateGeneratorData> generate(IWorldGenContext context, GottschTemplate template,
			PlacementSettings placement, ICoords coords, Supplier<Map<BlockState, BlockState>> consumerReplacmentMap, ICoords offset);
	
	/**
	 * NOTE not 100% sure that this  belongs here
	 * @param coords
	 * @param entranceCoords
	 * @param size
	 * @param placement
	 * @return
	 */
	public static ICoords alignEntranceToCoords(ICoords coords, ICoords entranceCoords) {
//		ICoords startCoords = null;
		// NOTE this alignment works differently than  surface structure align - that is taking in the SIZE
		// of the rotated structure. This method takes in an offset coords of the rotated structure

		// NOTE this method is using a offset from the spawnpoint. so the rotation doesn't matter,
		// it will always be a subtraction from the spawn coords. ie .add(-c) or .subtract(c)

		// TODO this line is wrong - it only takes into account 1 rotation (90cw)
		// first offset coords by entrance
		return coords.add(-entranceCoords.getX(), 0, -entranceCoords.getZ());

		// make adjustments for the rotation. REMEMBER that pits are 2x2
//		return switch (placement.getRotation()) {
//		case CLOCKWISE_90 ->
//				coords.add(entranceCoords.getX()-1, 0, -(entranceCoords.getZ()-1));
////			startCoords = startCoords.add(1, 0, 0);
//		case CLOCKWISE_180 ->
//				coords.add(entranceCoords.getX()-1, 0, entranceCoords.getZ()-1);
////			startCoords = startCoords.add(1, 0, 1);
//		case COUNTERCLOCKWISE_90 -> coords.add(-(entranceCoords.getX()-1), 0, entranceCoords.getZ()-1);
////			startCoords = startCoords.add(0, 0, 1);
//		default -> coords;
//		};
	}
	
	public Block getNullBlock();
	public void setNullBlock(Block nullBlock);

}