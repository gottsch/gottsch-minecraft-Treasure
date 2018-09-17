/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2018
 *
 */
// TODO need an AbstractTreasureBlock that has rotateLocks etc because MimicChestBlock
// will not extend TreasureChestBlock
public class MimicChestBlock extends AbstractChestBlock {
//	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.class);
//	/*
//	 *  the class of the tileEntityClass this BlockChest should use.
//	 */
//	private Class<?> tileEntityClass;
//	
	/*
	 * the class of the mimic this BlockChest should spawn.
	 */
	private Class<? extends EntityMob> mimicClass;
	
//
//	/*
//	 * the concrete object of the tile entity
//	 */
//	private AbstractTreasureChestTileEntity tileEntity;
//	
//	/*
//	 * the type of chest
//	 */
//	private TreasureChestType chestType;
//
//	/*
//	 * the rarity of the chest
//	 */
//	private Rarity rarity;
//	
//	/*
//	 * An array of AxisAlignedBB bounds for the bounding box
//	 */
//	AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 */
	public MimicChestBlock(String modID, String name, 
			Class<? extends AbstractTreasureChestTileEntity> te,
			TreasureChestType type,
			Rarity rarity) {
		this(modID, name, Material.WOOD, te, type, rarity);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param te
	 * @param type
	 */
	public MimicChestBlock(String modID, String name, Material material, 
			Class<? extends AbstractTreasureChestTileEntity> te, 
			TreasureChestType type, 
			Rarity rarity) {
		super(modID, name, material, te, type, rarity);
//		super(modID, name, material);
//		setTileEntityClass(te);
//		setChestType(type);
//		setRarity(rarity);
//		setCreativeTab(Treasure.TREASURE_TAB);
//		
//		// set the default bounds
//		setBounds(
//			new AxisAlignedBB[] {new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
//			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
//			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
//			new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)	// W
//			});
//		
//		// set the tile entity reference
//		try {
//			setTileEntity((AbstractTreasureChestTileEntity)getTileEntityClass().newInstance());
//		}
//		catch(Exception e) {
//			Treasure.logger.warn("Unable to create reference AbstractTreasureChestTileEntity object.");
//		}
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
//	private MimicChestBlock(String modID, String name, Material material) {
//		super(modID, name, material);
//	}

//	/* (non-Javadoc)
//	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
//	 */
//	@Override
//	public TileEntity createNewTileEntity(World worldIn, int meta) {
//		AbstractTreasureChestTileEntity chestTileEntity = null;
//		try {
//			chestTileEntity = (AbstractTreasureChestTileEntity) getTileEntityClass().newInstance();
//
//			// setup lock states
//			List<LockState> lockStates = new LinkedList<>();
//
//			for (int i = 0; i < chestType.getSlots().length; i++) {
//				LockState lockState = new LockState();
//				lockState.setSlot(chestType.getSlots()[i]);
//				// add in order of slot indexes
//				lockStates.add(lockState.getSlot().getIndex(), lockState);
//			}
//			chestTileEntity.setLockStates(lockStates);
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		return chestTileEntity;
//	}
//
//	/**
//	 * 
//	 * @param state
//	 * @return
//	 */
//	@Override
//	public boolean hasTileEntity(IBlockState state) {
//		return true;
//	}

//	/**
//	 * 
//	 */
//	@SideOnly(Side.CLIENT)
//	public BlockRenderLayer getBlockLayer() {
//		return BlockRenderLayer.CUTOUT_MIPPED;
//	}
//
//	// used by the renderer to control lighting and visibility of other blocks.
//	// set to false because this block doesn't fill the entire 1x1x1 space
//	@Override
//	public boolean isOpaqueCube(IBlockState state) {
//		return false;
//	}
//
//	// used by the renderer to control lighting and visibility of other blocks, also by
//	// (eg) wall or fence to control whether the fence joins itself to this block
//	// set to false because this block doesn't fill the entire 1x1x1 space
//	@Override
//	public boolean isFullCube(IBlockState state) {
//		return false;
//	}
//
//	/**
//	 * Render using a TESR.
//	 */
//	@Override
//	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
//		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
//	}

//	/**
//	 * 
//	 */
//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//		if (state.getValue(FACING) == EnumFacing.NORTH) {
//			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
//		}
//		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
//			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
//		}
//		else if (state.getValue(FACING) == EnumFacing.EAST) {
//			return bounds[EnumFacing.EAST.getHorizontalIndex()];
//		}
//		else if (state.getValue(FACING) == EnumFacing.WEST) {
//			return bounds[EnumFacing.WEST.getHorizontalIndex()];
//		}
//		else {		
//			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
//		}
//	}

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		Treasure.logger.debug("Placing chest from item");

//		boolean shouldRotate = false;
		boolean shouldUpdate = false;
		boolean forceUpdate = false;
		AbstractTreasureChestTileEntity tcte = null;
		Direction oldPersistedChestDirection = Direction.NORTH;

		// face the block towards the player (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);
		TileEntity te = worldIn.getTileEntity(pos);
		if (te != null && te instanceof AbstractTreasureChestTileEntity) {				
			// get the backing tile entity
			tcte = (AbstractTreasureChestTileEntity) te;

			// set the name of the chest
			if (stack.hasDisplayName()) {
				tcte.setCustomName(stack.getDisplayName());
			}

//			// read in nbt
//			if (stack.hasTagCompound()) {
//
//				tcte.readFromItemStackNBT(stack.getTagCompound());
//				forceUpdate = true;
//
//				// get the old tcte facing direction
//				oldPersistedChestDirection = Direction.fromFacing(EnumFacing.getFront(tcte.getFacing()));
//
//				// dump stack NBT
//				if (Treasure.logger.isDebugEnabled()) {
//					dump(stack.getTagCompound(), new Coords(pos), "STACK ITEM -> CHEST NBT");
//				}
//			}

			// get the direction the block is facing.
//			Direction direction = Direction.fromFacing(placer.getHorizontalFacing().getOpposite());

			// rotate the lock states
//			shouldUpdate = rotateLockStates(worldIn, pos, oldPersistedChestDirection.getRotation(direction)); // old -> Direction.NORTH //
			
			// update the TCTE facing
			tcte.setFacing(placer.getHorizontalFacing().getOpposite().getIndex());
		}
		if ((forceUpdate || shouldUpdate) && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

//	/**
//	 * 
//	 * @param world
//	 * @param pos
//	 * @param rotate
//	 * @return
//	 */
//	public boolean  rotateLockStates(World world, BlockPos pos, Rotate rotate) {
//		boolean hasRotated = false;
//		boolean shouldRotate = false;
//		if (rotate != Rotate.NO_ROTATE) shouldRotate = true;
//		Treasure.logger.debug("Rotate to:" + rotate);
//		
//		AbstractTreasureChestTileEntity tcte = null;
//		TileEntity te = world.getTileEntity(pos);
//		if (te != null && te instanceof AbstractTreasureChestTileEntity) {
//			// get the backing tile entity
//			tcte = (AbstractTreasureChestTileEntity) te;
//		}
//		else {
//			return false;
//		}
//		
//		try {
//			for (LockState lockState : tcte.getLockStates()) {
//				if (lockState != null && lockState.getSlot() != null) {
////					Treasure.logger.debug("Original lock state:" + lockState);
//					// if a rotation is needed
//					if (shouldRotate) {
//						ILockSlot newSlot = lockState.getSlot().rotate(rotate);
////						Treasure.logger.debug("New slot position:" + newSlot);
//						lockState.setSlot(newSlot);
//						// set the flag to indicate the lockStates have rotated
//						hasRotated = true;
//					}
//				}
//			}
//		}
//		catch(Exception e) {
//			Treasure.logger.error("Error updating lock states: ", e);
//		}
//		return hasRotated;
//	}

	/**
	 * 
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		AbstractTreasureChestTileEntity te = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(pos);

		// exit if on the client
		if (worldIn.isRemote) {			
			return true;
		}

		// get the loot
		NonNullList<ItemStack> items = te.getItems();
		
		// get the facing direction of the chest
		EnumFacing chestFacing = state.getValue(FACING);
		Treasure.logger.debug("Mimic facing -> {}", chestFacing);
		float yaw = 0.0F;
		switch(chestFacing) {
		case NORTH:
			yaw = 180.0F;
			break;
		case EAST:
			yaw = -90.0F;
			break;
		case WEST:
			yaw = 90.0F;
			break;
		case SOUTH:
			yaw = 0.0F;
			break;
		}
		Treasure.logger.debug("Mimic yaw -> {}", yaw);
		
		// remove the tile entity
		worldIn.setBlockToAir(pos);
		
		/*
		 * spawn the mimic
		 */
		// TODO need a generic base class for MimicEntity
    	EntityMob mimic = null;
		try {
			mimic = getMimicClass().getConstructor(World.class).newInstance(worldIn);
		} catch (Exception e) {
			Treasure.logger.error("Error creating mimic:", e);
		}
		
		if (mimic != null) {
	    	 EntityLiving entityLiving = (EntityLiving)mimic;
//	    	 entityLiving.setLocationAndAngles((double)pos.getX() + 0.5D,  (double)pos.getY(), (double)pos.getZ() + 0.5D, 45F, 0.0F);
	    	 
	    	 // TODO why doesn't this set the initial rotation!!!
	         entityLiving.setLocationAndAngles((double)pos.getX() + 0.5D,  (double)pos.getY(), (double)pos.getZ() + 0.5D, 45F, 0.0F);
	    	 worldIn.spawnEntity(entityLiving);
	         entityLiving.setLocationAndAngles((double)pos.getX() + 0.5D,  (double)pos.getY(), (double)pos.getZ() + 0.5D, 45F, 0.0F);
		}

		// remove the tile entity 
		worldIn.removeTileEntity(pos);
					
		return true;
	}
	
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}
	
	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// do nothing
	}

//	/**
//	 * Convert the given metadata into a BlockState for this Block
//	 */
//	@Override
//	public IBlockState getStateFromMeta(int meta) {
//		EnumFacing enumfacing = EnumFacing.getFront(meta);
//
//		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
//			enumfacing = EnumFacing.NORTH;
//		}
//		return this.getDefaultState().withProperty(FACING, enumfacing);
//	}
//
//	/**
//	 * Convert the BlockState into the correct metadata value
//	 */
//	@Override
//	public int getMetaFromState(IBlockState state) {
//		return ((EnumFacing)state.getValue(FACING)).getIndex();
//	}
//
//	/**
//	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
//	 * blockstate.
//	 */
//	@Override
//	public IBlockState withRotation(IBlockState state, Rotation rot) {
//		return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
//	}
//
//	/**
//	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
//	 * blockstate.
//	 */
//	@Override
//	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
//		return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
//	}
//	
//	/**
//	 * 
//	 */
//	@Override
//	protected BlockStateContainer createBlockState() {
//		return new BlockStateContainer(this, new IProperty[] {FACING});
//	}

	/**
	 * Handled by breakBlock()
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
//	/**
//	 * @return the tileEntityClass
//	 */
//	public Class<?> getTileEntityClass() {
//		return tileEntityClass;
//	}
//
//	/**
//	 * @param tileEntityClass the tileEntityClass to set
//	 */
//	public MimicChestBlock setTileEntityClass(Class<?> tileEntityClass) {
//		this.tileEntityClass = tileEntityClass;
//		return this;
//	}
//
//	/**
//	 * @return the chestType
//	 */
//	public TreasureChestType getChestType() {
//		return chestType;
//	}
//
//	/**
//	 * @param chestType the chestType to set
//	 */
//	public MimicChestBlock setChestType(TreasureChestType chestType) {
//		this.chestType = chestType;
//		return this;
//	}
//
//	/**
//	 * @return the bounds
//	 */
//	public AxisAlignedBB[] getBounds() {
//		return bounds;
//	}
//
//	/**
//	 * @param bounds the bounds to set
//	 */
//	public MimicChestBlock setBounds(AxisAlignedBB[] bounds) {
//		this.bounds = bounds;
//		return this;
//	}
//
//	/**
//	 * @return the rarity
//	 */
//	public Rarity getRarity() {
//		return rarity;
//	}
//
//	/**
//	 * @param rarity the rarity to set
//	 */
//	public MimicChestBlock setRarity(Rarity rarity) {
//		this.rarity = rarity;
//		return this;
//	}
//
//	/**
//	 * @return the tileEntity
//	 */
//	public AbstractTreasureChestTileEntity getTileEntity() {
//		return tileEntity;
//	}
//
//	/**
//	 * @param tileEntity the tileEntity to set
//	 */
//	public void setTileEntity(AbstractTreasureChestTileEntity tileEntity) {
//		this.tileEntity = tileEntity;
//	}
//
	/**
	 * @return the mimicClass
	 */
	public Class<? extends EntityMob> getMimicClass() {
		return mimicClass;
	}

	/**
	 * @param mimicClass the mimicClass to set
	 */
	public MimicChestBlock setMimicClass(Class<? extends EntityMob> mimicClass) {
		this.mimicClass = mimicClass;
		return this;
	}

}
