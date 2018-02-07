/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.block.AbstractModContainerBlock;
import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.enums.Rotate;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.KeyItem;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestBlock extends AbstractModContainerBlock {
	public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.class);
	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<?> tileEntityClass;

	/*
	 * the type of chest
	 */
	private TreasureChestType chestType;

	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];


	/**
	 * 
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 */
	public TreasureChestBlock(String modID, String name, Class<? extends TileEntity> te, TreasureChestType type) {
		this(modID, name, Material.WOOD, te, type);
	}

	/**
	 * TODO need the bounds
	 * @param modID
	 * @param name
	 * @param material
	 * @param te
	 * @param type
	 */
	public TreasureChestBlock(String modID, String name, Material material, Class<? extends TileEntity> te, TreasureChestType type) {
		this(modID, name, material);
		setTileEntityClass(te);
		setChestType(type);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	private TreasureChestBlock(String modID, String name, Material material) {
		super(modID, name, material);

	}

	/**
	 * TODO TEMP display... come up with better info
	 * Format:
	 * 		Item Name, (vanilla minecraft)
	 * 		State [Locked | Unlocked] [color = Dark Red | Green],
	 * 		Requires: X Key [color = Yellow | Gold]
	 * @param te
	 * @return
	 */
	public String getInfo(TreasureChestTileEntity te) {

		final String STATE_LABEL = "State: ";
		final String REQUIRES_LABEL = "Requires: ";
		final String LOCKED_STATE = "Locked";
		final String UNLOCKED_STATE = "Unlocked";

		StringBuilder builder = new StringBuilder();
		if (te.hasCustomName()) {
			builder.append(TextFormatting.WHITE).append(te.getCustomName()).append(", ");
		}
		if (te.hasLocks()) {
			builder.append(STATE_LABEL).append(TextFormatting.DARK_RED).append(LOCKED_STATE);
			builder.append(TextFormatting.WHITE).append(", ");
			builder.append(REQUIRES_LABEL);
			for (LockState lockState : te.getLockStates()) {
				if (lockState.getLock() != null) {
					builder.append(TextFormatting.GOLD).append(lockState.getLock().getUnlocalizedName())
					.append(" ");
				}
			}
		}
		else {
			builder.append(STATE_LABEL).append(TextFormatting.GREEN).append(UNLOCKED_STATE);
		}			
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World, int)
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TreasureChestTileEntity chestTileEntity = null;
		try {
			chestTileEntity = (TreasureChestTileEntity) getTileEntityClass().newInstance();

			// setup lock states
			List<LockState> lockStates = new LinkedList<>();

			// TODO sort slots by index - need comparator
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

	/**
	 * Render using a TESR.
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

		boolean shouldRotate = false;
		boolean shouldUpdate = false;
		TreasureChestTileEntity tcte = null;

		// face the block towards the palyer (there isn't really a front)
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 3);
        
		if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			if (te != null && te instanceof TreasureChestTileEntity) {
				// get the backing tile entity
				tcte = (TreasureChestTileEntity) te;
				// set the name of the chest
		        if (stack.hasDisplayName()) {
		            tcte.setCustomName(stack.getDisplayName());
		        }
		        // read in nbt
		        if (stack.hasTagCompound()) {
		        	tcte.readFromNBT(stack.getTagCompound());
		        }
				// get the direction the block is facing.
				Direction direction = Direction.fromFacing(placer.getHorizontalFacing().getOpposite());
				// get the rotation needed (from default: NORTH)
				Rotate rotate = Direction.NORTH.getRotation(direction);
				if (rotate != Rotate.NO_ROTATE) shouldRotate = true;
				Treasure.logger.debug("Rotate to:" + rotate);
				try {
					for (LockState lockState : tcte.getLockStates()) {
						if (lockState != null && lockState.getSlot() != null) {
							Treasure.logger.debug("Original lock state:" + lockState);
							// if a rotation is needed
							if (shouldRotate) {
								ILockSlot newSlot = lockState.getSlot().rotate(rotate);
								Treasure.logger.debug("New slot position:" + newSlot);
								lockState.setSlot(newSlot);
								// set the update flag
								shouldUpdate = true;
							}
						}
					}
				}
				catch(Exception e) {
					Treasure.logger.error("Error updating lock states: ", e);
				}
			}
		}

		if (shouldUpdate && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

	//	@SuppressWarnings("deprecation")
	//	@Override
	//	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
	//		return state.withProperty(FACING, getFacing(worldIn, pos));
	//	}

	/**
	 * 
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		//		super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

		TreasureChestTileEntity te = (TreasureChestTileEntity) worldIn.getTileEntity(pos);

		// exit if on the client
		if (worldIn.isRemote) {			
			return true;
		}
		
		boolean isLocked = false;
		// determine if chest is locked
		if (te.hasLocks()) {
			isLocked = true;
		}
		
//		try {
//			// get the item held in player's hand
//			ItemStack heldItem = playerIn.getHeldItem(hand);	
//			if (isLocked) {
//				// if the player is holding a key
//				if (heldItem != null && heldItem.getItem() instanceof KeyItem) {
//					KeyItem key = (KeyItem) heldItem.getItem();
//					boolean breakKey = true;
//					// check if held item is a key that opens a lock (only first lock that key fits is unlocked).
//					for (LockState lockState : te.getLockStates()) {
//						if (lockState.getLock() != null) {
//							if (key.unlock(lockState.getLock())) {
//								LockItem lock = lockState.getLock();
//								// remove the lock
//								lockState.setLock(null);
//								// play noise
//								//								worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
//								worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
//								// update the client
//								te.sendUpdates();
//								// spawn the lock
//								InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(lock));
//								// don't break the key
//								breakKey = false;
//								// exit the loop
//								break;
//							}
//						}
//					}
//
//					// check key's breakability
//					if (breakKey) {
//						if (key.isBreakable()  && TreasureConfig.enableKeyBreaks) {
//							// break key;
//							heldItem.shrink(1);
//							//	worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.break", 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
//							playerIn.sendMessage(new TextComponentString("Key broke."));
//							worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);
//
//							if (heldItem.getCount() <=0) {
//								IInventory inventory = playerIn.inventory;
//								inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
//							}							
//						}
//						else {
//							playerIn.sendMessage(new TextComponentString("Failed to unlock."));
//						}
//					}
//					// test if still locked
//					if (!te.hasLocks()) {
//						isLocked = false;		
//						// TODO for future: set the owner of the chest to the player. when unlocking locks, keys no longer break.
//					}
//				}
//				else if (heldItem != null && heldItem.getItem() instanceof LockItem) {
//					// handle the lock
//					// NOTE don't use the return boolean as the locked flag here, as the chest is already locked and if the method was
//					// unsuccessful it could state the chest is unlocked.
//					handleHeldLock(te, playerIn, heldItem);
//				}
//				else {
//					// Display message and do nothing
//					StringBuilder builder = new StringBuilder();
//					builder
//					.append(TextFormatting.WHITE).append("The chest is ").append(TextFormatting.BOLD).append(TextFormatting.GOLD)
//					.append("Locked").append(TextFormatting.WHITE).append(".");
//					playerIn.sendMessage(new TextComponentString(builder.toString()));
//					//			       return false;
//				}
//			}
//			// not locked
//			else {
//				// if the player is holding a lock
//				if (heldItem != null && heldItem.getItem() instanceof LockItem) {
//					// handle the lock
//					isLocked = handleHeldLock(te, playerIn, heldItem);
//				}
//			}
			
			// open the chest
			if (!isLocked) {
				playerIn.openGui(Treasure.instance, GuiHandler.TREASURE_CHEST_GUIID, worldIn, pos.getX(), pos.getY(),	pos.getZ());
			}
//		}
//		catch (Exception e) {
//			Treasure.logger.error("gui error: ", e);
//		}
		return true;
	}

	/**
	 * 
	 * @param te
	 * @param player
	 * @param heldItem
	 */
	private boolean handleHeldLock(TreasureChestTileEntity te, EntityPlayer player, ItemStack heldItem) {
		boolean isLocked = false;
		LockItem lock = (LockItem) heldItem.getItem();		
		// add the lock to the first lockstate that has an available slot
		for(LockState lockState : te.getLockStates()) {
			if (lockState != null && lockState.getLock() == null) {
				lockState.setLock(lock);
				te.sendUpdates();
				// decrement item in hand
				heldItem.shrink(1);
				if (heldItem.getCount() <=0) {
					IInventory inventory = player.inventory;
					inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
				isLocked = true;
				break;
			}
		}
		return isLocked;
	}

	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TreasureChestTileEntity te = (TreasureChestTileEntity) worldIn.getTileEntity(pos);

		if (te != null && te.getInventoryProxy() != null) {
			// unlocked!
			if (!te.hasLocks()) {
				/*
				 * spawn inventory items
				 */
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)te.getInventoryProxy());

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);
				InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), chestItem);

				/*
				 *  write the locked state to the nbt
				 */
				if (!chestItem.hasTagCompound()) {
					chestItem.setTagCompound(new NBTTagCompound());
				}		        
				te.writePropertiesToNBT(chestItem.getTagCompound());
			}
			else {
				// for each item in chest add to the new entity item's NBT

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);
				if (!worldIn.isRemote) {
					InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), chestItem);
				}
				// give the chest a tag compound
				Treasure.logger.trace("Saving chest items:");
				chestItem.setTagCompound(new NBTTagCompound());
				te.writeToNBT(chestItem.getTagCompound());
			}

			worldIn.updateComparatorOutputLevel(pos, this);

			// remove the tile entity 
			worldIn.removeTileEntity(pos);
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
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
	 * Handled by breakBlock()
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
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
	public TreasureChestBlock setTileEntityClass(Class<?> tileEntityClass) {
		this.tileEntityClass = tileEntityClass;
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
	public TreasureChestBlock setChestType(TreasureChestType chestType) {
		this.chestType = chestType;
		return this;
	}

	/**
	 * @return the locks
	 */
	//	public LockState[] getLocks() {
	//		return locks;
	//	}
	//
	//	/**
	//	 * @param locks the locks to set
	//	 */
	//	public TreasureChestBlock setLocks(LockState[] locks) {
	//		this.locks = locks;
	//		return this;
	//	}

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public TreasureChestBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}

}
