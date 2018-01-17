/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.enums.Rotate;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TestChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling onJan 5, 2018
 *
 */
@Deprecated
public class TestChest extends Block {
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	/*
	 * the type of chest
	 */
	private TreasureChestType chestType;



	public TestChest() {
		super(Material.WOOD);
		// set the default direction to north
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);   // the block will appear on the Blocks tab in creative
		this.setHardness(2.5F);
		this.chestType = TreasureChestTypes.STANDARD;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	// Called when the block is placed or loaded client side to get the tile entity for the block
	// Should return a new instance of the tile entity for the block
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		TestChestTileEntity e = new TestChestTileEntity();

		// setup lock states
		LockState[] lockStates = new LockState[chestType.getMaxLocks()];
		for (int i = 0; i < chestType.getSlots().length; i++) {
			LockState lockState = new LockState();
			lockState.setSlot(chestType.getSlots()[i]);
			lockStates[i] = lockState;
		}
		
		// TEST add a lock to each slot
		lockStates[0].setLock(TreasureItems.IRON_LOCK);
		lockStates[1].setLock(TreasureItems.GOLD_LOCK);
		lockStates[2].setLock(TreasureItems.DIAMOND_LOCK);
//		
//		lockState.setSlot(new LockSlot(0, Direction.NORTH, 0.5F, 0.5F, 0.05F, 0F));
//		lockState.setLock(TreasureItems.IRON_LOCK);
//		locks[0] = lockState;
		
		e.setLockStates(lockStates);
		Treasure.logger.debug("Created tile entity: " + e);
		return e;
	}

	// The following methods aren't particularly relevant to this example.  See MBE01, MBE02, MBE03 for more information.
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks, also by
	// (eg) wall or fence to control whether the fence joins itself to this block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	// render using a BakedModel
	// not required because the default (super method) is MODEL
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

@Override
public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
	// TODO Auto-generated method stub
	super.onBlockAdded(worldIn, pos, state);
	

}

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		boolean shouldUpdate = false;
		TestChestTileEntity tcte = null;
		// face the teleport ladder towards the palyer (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);

		if (!worldIn.isRemote) {
			Treasure.logger.debug("On the server");
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && te instanceof TestChestTileEntity) {
				Treasure.logger.debug("It is a TestChestTileEntity.");
				// get the backing tile entity
				tcte = (TestChestTileEntity) te;
				Treasure.logger.debug("LockStates.size:" + tcte.getLockStates().length);
				Treasure.logger.debug("Chest is facing:" + placer.getHorizontalFacing().getOpposite());
				// get the direction the block is facing.
				Direction direction = Direction.fromFacing(placer.getHorizontalFacing().getOpposite());
				// get the rotation needed (from default: NORTH)
				Rotate rotate = Direction.NORTH.getRotation(direction);
				Treasure.logger.debug("Rotate to:" + rotate);
				try {
					for (LockState lockState : tcte.getLockStates()) {
						if (lockState != null && lockState.getLock() != null) { // <--- need to test again a lock, not just the state (because a slot should exist)
							Treasure.logger.debug("Original lock state:" + lockState);
							// TODO need to determine if the facing differs from the default (ie. not facing north)
							// TODO only need to determine the rotate once, and every element rotates from that.
							// determine the rotation from default
//							Rotate rotate = lockState.getSlot().getFace().getRotation(direction);
//							Treasure.logger.debug("Rotate to:" + rotate);
							// if a rotation is needed
							if (rotate != Rotate.NO_ROTATE) {
								ILockSlot newSlot = lockState.getSlot().rotate(rotate);
								Treasure.logger.debug("New slot position:" + newSlot);
								lockState.setSlot(newSlot);
							}
							// TEMP change the icon to test it the client side is being updated
//							lockState.setLock(TreasureItems.GOLD_LOCK);
							shouldUpdate = true;
						}
					}
				}
				catch(Exception e) {
					Treasure.logger.error("Error updating lock states: ", e);
				}
			}
		}
		Treasure.logger.debug("Should update? " + shouldUpdate);
		Treasure.logger.debug("TCTE? " + tcte);
		
		if (shouldUpdate && tcte != null) {
			// update the client
			tcte.sendUpdates();
			Treasure.logger.debug("Should have updated client");
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}
	/**
	 * 
	 */
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
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
	public void setChestType(TreasureChestType chestType) {
		this.chestType = chestType;
	}
}
