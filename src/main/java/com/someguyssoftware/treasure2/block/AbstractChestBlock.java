/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModContainerBlock;
import com.someguyssoftware.gottschcore.spatial.Rotate;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class AbstractChestBlock<E extends TileEntity> extends ModContainerBlock implements ITreasureBlock {
	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class);

	/*
	 * OLD way
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<?> tileEntityClass;

	/**
	 * NEW way
	 */
//	protected final TileEntityType<? extends E> tileEntityType;
	
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
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
//	private AxisAlignedBB[] bounds = new AxisAlignedBB[4];

	private VoxelShape[] bounds = new VoxelShape[4];
	
	/**
	 * 
	 */
	public AbstractChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity, Block.Properties properties) {
		super(modID, name, properties);
		setTileEntityClass(te);
		setChestType(type);
		setRarity(rarity);

		VoxelShape shape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 14.0D, 14.0D, 14.0D);
		// set the default bounds
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

	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		AbstractTreasureChestTileEntity chestTileEntity = null;
		try {
			chestTileEntity = (AbstractTreasureChestTileEntity) getTileEntityClass().newInstance();

			// setup lock states
			List<LockState> lockStates = new LinkedList<>();

			for (int i = 0; i < chestType.getSlots().length; i++) {
				LockState lockState = new LockState();
				lockState.setSlot(chestType.getSlots()[i]);
				// add in order of slot indexes
				lockStates.add(lockState.getSlot().getIndex(), lockState);
			}
			chestTileEntity.setLockStates(lockStates);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return chestTileEntity;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.get(FACING)) {
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
	
	/**
	 * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
	 * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
	 * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	/**
	 * Gets whether the provided {@link VoxelShape} is opaque
	 * used by the renderer to control lighting and visibility of other blocks.
	 * set to false because this block doesn't fill the entire 1x1x1 space
	 */
	public static boolean isOpaque(VoxelShape shape) {
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	//	@Override
	//	public boolean isFullCube(BlockState state) {
	//		return false;
	//	}

	//	@Override
	//	public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
	//		return false;
	//	}

	/**
	 * 
	 * @param world
	 * @param pos
	 * @param rotate
	 * @return
	 */
	public boolean  rotateLockStates(World world, BlockPos pos, Rotate rotate) {
		boolean hasRotated = false;
		boolean shouldRotate = false;
		if (rotate != Rotate.NO_ROTATE) shouldRotate = true;
		//		Treasure.LOGGER.debug("Rotate to:" + rotate);

		AbstractTreasureChestTileEntity tcte = null;
		TileEntity te = world.getTileEntity(pos);
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
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	/**
	 * 
	 */
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing().getOpposite());
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	 * fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
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

	/**
	 * @return the bounds
	 */
//	public AxisAlignedBB[] getBounds() {
//		return bounds;
//	}

	public VoxelShape[] getBounds() {
		return bounds;
	}
	
	/**
	 * @param bounds the bounds to set
	 */
//	public AbstractChestBlock  setBounds(AxisAlignedBB[] bounds) {
//		this.bounds = bounds;
//		return this;
//	}
	
	public AbstractChestBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}

}
