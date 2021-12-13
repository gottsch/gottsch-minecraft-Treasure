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
package com.someguyssoftware.treasure2.block;

import java.util.LinkedList;
import java.util.List;

import com.someguyssoftware.gottschcore.block.ModContainerBlock;
import com.someguyssoftware.gottschcore.spatial.Heading;
import com.someguyssoftware.gottschcore.spatial.Rotate;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class AbstractChestBlock extends ModContainerBlock implements ITreasureBlock {
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<?> tileEntityClass;

	/*
	 * the concrete object of the tile entity
	 */
	private AbstractTreasureChestTileEntity tileEntity;

	/*
	 * the type of chest
	 */
	private TreasureChestType chestType;

	/*
	 * the rarity of the chest
	 */
	private Rarity rarity;

	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];

	/**
	 * 
	 */
	public AbstractChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, 
			TreasureChestType type, Rarity rarity, Block.Properties properties) {
		
		super(modID, name, properties);
		setTileEntityClass(te);
		setChestType(type);
		setRarity(rarity);

		// set the default shapes/shape
		VoxelShape shape = Block.box(1, 0, 1, 15, 14, 15);
		setBounds(
				new VoxelShape[] {
						shape, 	// N
						shape,  	// E
						shape,  	// S
						shape	// W
				});

		// set the tile entity reference
		try {
			setTileEntity((AbstractTreasureChestTileEntity)getTileEntityClass().newInstance());
		}
		catch(Exception e) {
			Treasure.LOGGER.warn("Unable to create reference AbstractTreasureChestTileEntity object.");
		}

	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		AbstractTreasureChestTileEntity chestBlockEntity = null;
		try {
			chestBlockEntity = (AbstractTreasureChestTileEntity) getTileEntityClass().newInstance();

			// setup lock states
			List<LockState> lockStates = new LinkedList<>();

			for (int i = 0; i < chestType.getSlots().length; i++) {
				LockState lockState = new LockState();
				lockState.setSlot(chestType.getSlots()[i]);
				// add in order of slot indexes
				lockStates.add(lockState.getSlot().getIndex(), lockState);
			}
			chestBlockEntity.setLockStates(lockStates);
//			Treasure.LOGGER.info("AbstractChestBlock | createNewTileEntity | lockStates -> {}", chestTileEntity.getLockStates());
		}
		catch(Exception e) {
			Treasure.LOGGER.error(e);
		}
//		Treasure.LOGGER.info("AbstractChestBlock | createNewTileEntity | tileEntity -> {} @ {}", chestTileEntity, chestTileEntity.getPos());
		return chestBlockEntity;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	// TODO make interface and add to interface
	/**
	 * Convenience method.
	 * @param state
	 * @return
	 */
	public static Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH:
			return bounds[0];
		case EAST:
			return bounds[1];
		case SOUTH:
			return bounds[2];
		case WEST:
			return bounds[3];
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockState = this.defaultBlockState().setValue(FACING,
				context.getHorizontalDirection().getOpposite());
		return blockState;
	}
	
	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		Treasure.LOGGER.debug("Placing chest from item");

		boolean shouldRotate = false;
		boolean shouldUpdate = false;
		boolean forceUpdate = false;
		AbstractTreasureChestTileEntity tcte = null;
		Heading oldPersistedChestDirection = Heading.NORTH;

		// face the block towards the player (there isn't really a front)
		worldIn.setBlock(pos, state.setValue(FACING, placer.getDirection().getOpposite()), 3);
		BlockEntity te = worldIn.getBlockEntity(pos);
		if (te != null && te instanceof AbstractTreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (AbstractTreasureChestTileEntity) te;

			// set the name of the chest
			if (stack.hasCustomHoverName()) {
				tcte.setCustomName(stack.getDisplayName());
			}
			
			// read in nbt
			if (stack.hasTag()) {
				tcte.readFromItemStackNBT(stack.getTag());
				forceUpdate = true;

				// get the old tcte facing direction
				oldPersistedChestDirection = Heading.fromDirection(tcte.getFacing()); // TODO might be byHorizontalIndex

				// dump stack NBT
//				if (Treasure.LOGGER.isDebugEnabled()) {
//					dump(stack.getTag(), new Coords(pos), "STACK ITEM -> CHEST NBT");
//				}
			}

			// get the direction the block is facing.
			Heading direction = Heading.fromDirection(placer.getDirection().getOpposite());

			// rotate the lock states
			shouldUpdate = rotateLockStates(worldIn, pos, oldPersistedChestDirection.getRotation(direction)); // old ->
			// Direction.NORTH
			// //

						Treasure.LOGGER.debug("New lock states ->");
						for (LockState ls : tcte.getLockStates()) {
							Treasure.LOGGER.debug(ls);
						}

			// update the TCTE facing
			tcte.setFacing(placer.getDirection().getOpposite());
		}
		if ((forceUpdate || shouldUpdate) && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

	@Override
	public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_,
			InteractionHand p_60507_, BlockHitResult p_60508_) {
		// TODO Auto-generated method stub
		return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
	}
	/**
	 * 
	 */
	@Override
	   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
			InteractionHand handIn, BlockRayTraceResult hit) {

		AbstractTreasureChestTileEntity tileEntity = (AbstractTreasureChestTileEntity) world.getBlockEntity(pos);

		// exit if on the client
		if (WorldInfo.isClientSide(world)) {
			return InteractionResult.SUCCESS;
		}

		boolean isLocked = false;
		// determine if chest is locked
		if (!tileEntity.hasLocks()) {
			// get the container provider
			INamedContainerProvider namedContainerProvider = this.getContainer(state, world, pos);			
			// open the chest
			NetworkHooks.openGui((ServerPlayer)player, namedContainerProvider, (packetBuffer)->{});
			// NOTE: (packetBuffer)->{} is just a do-nothing because we have no extra data to send
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void destroy(LevelAccessor worldIn, BlockPos pos, BlockState state) {
		Treasure.LOGGER.info("block destroyed by player. should happen after block is broken/replaced");
		super.destroy(worldIn, pos, state);
	}


	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		Treasure.LOGGER.debug("Removing block....!");

		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		AbstractTreasureChestTileEntity te = null;
		if (tileEntity instanceof AbstractTreasureChestTileEntity) {
			te = (AbstractTreasureChestTileEntity)tileEntity;
		}

		if (te != null) {
			// unlocked!
			if (!te.hasLocks()) {
				/*
				 * spawn inventory items
				 */
				InventoryHelper.dropContents(worldIn, pos, (Inventory) te);

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.byBlock(this), 1);
				Treasure.LOGGER.debug("Item being created from chest -> {}", chestItem.getItem().getRegistryName());
				InventoryHelper.dropItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
						chestItem);

				/*
				 * write the properties to the nbt
				 */
				if (!chestItem.hasTag()) {
					chestItem.setTag(new CompoundTag());
				}
				te.writePropertiesToNBT(chestItem.getTag());
			} else {
				Treasure.LOGGER.debug("[BreakingBlock] ChestConfig is locked, save locks and items to NBT");

				/*
				 * spawn chest item
				 */

				if (WorldInfo.isServerSide(worldIn)) {
					ItemStack chestItem = new ItemStack(Item.byBlock(this), 1);

					// give the chest a tag compound
					//					Treasure.LOGGER.debug("[BreakingBlock]Saving chest items:");

					CompoundTag nbt = new CompoundTag();
					nbt = te.save(nbt);
					chestItem.setTag(nbt);

					InventoryHelper.dropItemStack(worldIn, (double) pos.getX(), (double) pos.getY(),
							(double) pos.getZ(), chestItem);

					// TEST log all items in item
					//					NonNullList<ItemStack> items = NonNullList.<ItemStack>setValueSize(27, ItemStack.EMPTY);
					//					ItemStackHelper.loadAllItems(chestItem.getTagCompound(), items);
					//					for (ItemStack stack : items) {
					//						Treasure.LOGGER.debug("[BreakingBlock] item in chest item -> {}", stack.getDisplayName());
					//					}
				}
			}

			// remove the tile entity
			worldIn.removeBlockEntity(pos);
		}
		else {
			// default to regular block break;
			//			super.breakBlock(worldIn, pos, state);
			//			worldIn.destroyBlock()
			//			super.onBlockHarvested(worldIn, pos, state, player);
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	// replaces getItemDropped
	@Deprecated
	public static void spawnDrops(BlockState state, Level worldIn, BlockPos pos) {
		return;
	}

//	@Override
//	public boolean allowsMovement(BlockState state, LevelAccessor worldIn, BlockPos pos, PathType type) {
//		return false;
//	}
	
	/**
	 * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
	 * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
	 * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
	 */
//	@Override
//	public BlockRenderType getRenderType(BlockState state) {
//		return BlockRenderType.ENTITYBLOCK_ANIMATED;
//	}
	
	@Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.ENTITYBLOCK_ANIMATED;
   }

	/**
	 * Gets whether the provided {@link VoxelShape} is opaque
	 * used by the renderer to control lighting and visibility of other blocks.
	 * set to false because this block doesn't fill the entire 1x1x1 space
	 */
	public static boolean isOpaque(VoxelShape shape) {
		return false;
	}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @param rotate
	 * @return
	 */
	public boolean  rotateLockStates(LevelAccessor world, BlockPos pos, Rotate rotate) {
		// TODO replace pos with ICoords
		boolean hasRotated = false;
		boolean shouldRotate = false;
		if (rotate != Rotate.NO_ROTATE) shouldRotate = true;
		//		Treasure.LOGGER.debug("Rotate to:" + rotate);

		AbstractTreasureChestTileEntity tcte = null;
		BlockEntity te = world.getBlockEntity(pos);
		if (te != null && te instanceof AbstractTreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (AbstractTreasureChestTileEntity) te;
		}
		else {
			return false;
		}

		try {
			for (LockState lockState : tcte.getLockStates()) {
				if (lockState != null && lockState.getSlot() != null) {
					//					Treasure.LOGGER.debug("Original lock state:" + lockState);
					// if a rotation is needed
					if (shouldRotate) {
						ILockSlot newSlot = lockState.getSlot().rotate(rotate);
						//						Treasure.LOGGER.debug("New slot position:" + newSlot);
						lockState.setSlot(newSlot);
						// set the flag to indicate the lockStates have rotated
						hasRotated = true;
					}
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error updating lock states: ", e);
		}
		return hasRotated;
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
//	@Override
//	public boolean hasTileEntity(BlockState state) {
//		return true;
//	}

	/**
	 * Returns the blockstate setValue the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#setValueRotation(Rotation)} whenever possible. Implementing/overriding is
	 * fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate setValue the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#setValueMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	/**
	 * @return the tileEntityClass
	 */
	public Class<?> getTileEntityClass() {
		return tileEntityClass;
	}

	/**
	 * @param tileEntityClass the tileEntityClass to set
	 */
	public void setTileEntityClass(Class<?> tileEntityClass) {
		this.tileEntityClass = tileEntityClass;
	}

	/**
	 * @return the tileEntity
	 */
	public AbstractTreasureChestTileEntity getTileEntity() {
		return tileEntity;
	}

	/**
	 * @param tileEntity the tileEntity to set
	 */
	public AbstractChestBlock setTileEntity(AbstractTreasureChestTileEntity tileEntity) {
		this.tileEntity = tileEntity;
		return this;
	}

	/**
	 * @return the chestType
	 */
	public TreasureChestType getChestType() {
		return chestType;
	}

	/**
	 * @param chestType the chestType to set
	 */
	public AbstractChestBlock setChestType(TreasureChestType chestType) {
		this.chestType = chestType;
		return this;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public AbstractChestBlock setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	public VoxelShape[] getBounds() {
		return bounds;
	}

	public AbstractChestBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
