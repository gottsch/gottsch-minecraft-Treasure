/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.gottschcore.world.gen.structure.StructureMarkers;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.SkeletonBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;


/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class GeneratorUtil {
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

		/**
		 * convenience method
		 * 
		 * @param offset
		 * @return
		 */
		public static Block getMarkerBlock(StructureMarkers marker) {
			return TreasureTemplateRegistry.getMarkerMap().get(marker);
		}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param block
	 * @return
	 */
	public static boolean replaceWithBlock(ServerLevelAccessor world, ICoords coords, Block block) {
		// don't change if old block is air
		BlockContext context = new BlockContext(world, coords);
		if (context.isAir()) {
			return false;
		}
		world.setBlock(coords.toPos(), block.defaultBlockState(), 3);
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param blockState
	 * @return
	 */
	public static boolean replaceWithBlockState(ServerLevelAccessor world, ICoords coords, BlockState blockState) {
		// don't change if old block is air
		BlockContext context = new BlockContext(world, coords);
		if (context.isAir()) {
			return false;
		}
		world.setBlock(coords.toPos(), blockState, 3);
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param coords
	 * @return
	 */
	public static boolean replaceBlockWithChest(IWorldGenContext context, Block chest, ICoords coords, boolean discovered) {
		// get the old state
		BlockState oldState = context.level().getBlockState(coords.toPos());

		if (oldState.getProperties().contains(FACING)) {
			// set the new state
			return placeChest(context.level(), chest, coords, (Direction) oldState.getValue(FACING), discovered);

		} else {
			return placeChest(context.level(), chest, coords, Direction.from2DDataValue(context.random().nextInt(4)), discovered);
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chest
	 * @param state
	 * @return
	 */
	public static boolean replaceBlockWithChest(IWorldGenContext context, ICoords coords, 
			Block chest,	BlockState state, boolean discovered) {
		if (state.getProperties().contains(FACING)) {
			return placeChest(context.level(), chest, coords, (Direction) state.getValue(FACING), discovered);
		}

		if (state.getBlock() == Blocks.CHEST) {
			Direction direction = (Direction) state.getValue(ChestBlock.FACING);
			return placeChest(context.level(), chest, coords, direction, discovered);
		}

		// else do generic
		return replaceBlockWithChest(context, chest, coords, discovered);
	}

	/**
	 * 
	 * @param world
	 * @param chest
	 * @param pos
	 * @return
	 */
	public static boolean placeChest(ServerLevelAccessor world, Block chest, ICoords coords, Direction direction, boolean discovered) {
		// check if spawn pos is valid
		if (!WorldInfo.isHeightValid(coords)) {
			Treasure.LOGGER.debug("cannot place chest due to invalid height -> {}", coords.toShortString());
			return false;
		}

		try {
			BlockPos pos = coords.toPos();
			// create and place the chest
			WorldInfo.setBlock(world, coords, chest
					.defaultBlockState()
					.setValue(FACING, direction)
					.setValue(AbstractTreasureChestBlock.DISCOVERED, discovered));
			Treasure.LOGGER.debug("placed chest -> {} into world at coords -> {} with prop -> {}",
					chest.getClass().getSimpleName(), coords.toShortString(), direction);

			// get the direction the block is facing.
			Heading heading = Heading.fromDirection(direction);
			((AbstractTreasureChestBlock) chest).rotateLockStates(world, coords, Heading.NORTH.getRotation(heading));

			// get the tile entity
			BlockEntity blockEntity = (BlockEntity) world.getBlockEntity(pos);

			if (blockEntity == null) {
				// remove the chest block
				WorldInfo.setBlock(world, coords, Blocks.AIR.defaultBlockState());
				Treasure.LOGGER.warn("unable to create treasure chest's BlockEntity, removing chest.");
				return false;
			}
			// update the facing
			((AbstractTreasureChestBlockEntity) blockEntity).setFacing(direction.get3DDataValue());
		} catch (Exception e) {
			Treasure.LOGGER.error("An error occurred placing chest: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 */
	public static void placeSkeleton(IWorldGenContext context, ICoords coords) {
		// select a random facing direction
		Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
		ICoords coords2 = new Coords(coords.toPos().relative(facing));

		BlockContext blockContext = new BlockContext(context.level(), coords);
		BlockContext blockContext2 = new BlockContext(context.level(), coords2);

		boolean flag = blockContext.isReplaceable();
		boolean flag1 = blockContext2.isReplaceable();
		boolean flag2 = flag || blockContext.isAir();
		boolean flag3 = flag1 || blockContext2.isAir();

		if (flag2 && flag3
				&& context.level().getBlockState(coords.down(1).toPos()).isSolidRender(context.level(), coords.down(1).toPos())
				&& context.level().getBlockState(coords2.down(1).toPos()).isSolidRender(context.level(), coords2.down(1).toPos())) {
			BlockState skeletonState = TreasureBlocks.SKELETON.get().defaultBlockState()
					.setValue(SkeletonBlock.FACING, facing.getOpposite())
					.setValue(SkeletonBlock.PART, SkeletonBlock.EnumPartType.BOTTOM);

			context.level().setBlock(coords.toPos(), skeletonState, 3);
			context.level().setBlock(coords2.toPos(),
					skeletonState.setValue(SkeletonBlock.PART, SkeletonBlock.EnumPartType.TOP), 3);

			context.level().blockUpdated(coords.toPos(), blockContext.getState().getBlock());
			context.level().blockUpdated(coords2.toPos(), blockContext.getState().getBlock());
		}
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @return
	 */
	public static ICoords findSubterraneanCeiling(ServerLevelAccessor world, ICoords coords) {
		// TODO a better solution would take in the ChunkGenerator and find the
		// surface height. if coords exceeds the surface height, then fail.
		final int CEILING_FAIL_SAFE = 50;
		int ceilingHeight = 1;

		// find the ceiling of the cavern
		while (world.isEmptyBlock(coords.toPos())) {
			ceilingHeight++;
			if (ceilingHeight > world.getHeight() || ceilingHeight == CEILING_FAIL_SAFE) {
				return null;
			}
			coords = coords.add(0, 1, 0);
		}
		// add 1 height to the final pos
		coords = coords.add(0, 1, 0);
		return coords;
	}
}