/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.LinkedList;
import java.util.List;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.enums.Rotate;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Sep 16, 2018
 *
 */
public abstract class AbstractChestBlock extends AbstractModContainerBlock implements ITreasureBlock {
	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.class);
	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<?> tileEntityClass;

	/*
	 * the concrete object of the tile entity
	 */
	private ITreasureChestTileEntity tileEntity;
	
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
	private AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 */
	public AbstractChestBlock(String modID, String name, Material material, Class<? extends ITreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		this(modID, name, material);
		setTileEntityClass(te);
		setChestType(type);
		setRarity(rarity);
		setCreativeTab(Treasure.TREASURE_TAB);
		
		// set the default bounds
		setBounds(
			new AxisAlignedBB[] {new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)	// W
			});
		
		// set the tile entity reference
		try {
			setTileEntity((ITreasureChestTileEntity)getTileEntityClass().newInstance());
		}
		catch(Exception e) {
			Treasure.logger.warn("Unable to create reference ITreasureChestTileEntity object.");
		}

	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	protected AbstractChestBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		ITreasureChestTileEntity chestTileEntity = null;
		try {
			chestTileEntity = (ITreasureChestTileEntity) getTileEntityClass().newInstance();

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
		return (TileEntity) chestTileEntity;
	}

	/**
	 * 
	 */
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

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	/**
	 * Render using a TESR.
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
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
//		Treasure.logger.debug("Rotate to:" + rotate);
		
		ITreasureChestTileEntity tcte = null;
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof ITreasureChestTileEntity) {
			// get the backing tile entity
			tcte = (ITreasureChestTileEntity) te;
		}
		else {
			return false;
		}
		
		try {
			for (LockState lockState : tcte.getLockStates()) {
				if (lockState != null && lockState.getSlot() != null) {
//					Treasure.logger.debug("Original lock state:" + lockState);
					// if a rotation is needed
					if (shouldRotate) {
						ILockSlot newSlot = lockState.getSlot().rotate(rotate);
//						Treasure.logger.debug("New slot position:" + newSlot);
						lockState.setSlot(newSlot);
						// set the flag to indicate the lockStates have rotated
						hasRotated = true;
					}
				}
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Error updating lock states: ", e);
		}
		return hasRotated;
	}
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		}
		else {		
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}
	
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	}
	
	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
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
	public ITreasureChestTileEntity getTileEntity() {
		return tileEntity;
	}

	/**
	 * @param tileEntity the tileEntity to set
	 */
	public AbstractChestBlock setTileEntity(ITreasureChestTileEntity tileEntity) {
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
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public AbstractChestBlock  setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}

}
