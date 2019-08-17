/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.someguyssoftware.gottschcore.enums.Direction;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.printer.ChestNBTPrettyPrinter;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestBlock extends AbstractChestBlock {

	/*
	 * The GUIID;
	 */
	private int chestGuiID = GuiHandler.STANDARD_CHEST_GUIID;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 */
	public TreasureChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
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
	public TreasureChestBlock(String modID, String name, Material material, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, material, te, type, rarity);
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

//	/**
//	 * Information that is displayed in the Chat when the user performs some action on the chest.
//	 * Format:
//	 * 		Item Name, (vanilla minecraft)
//	 * 		State [Locked | Unlocked] [color = Dark Red | Green],
//	 * 		Requires: X Key [color = Yellow | Gold]
//	 * @param te
//	 * @return
//	 */
//	public String getInfo(AbstractTreasureChestTileEntity te) {
//
//		final String STATE_LABEL = "State: ";
//		final String REQUIRES_LABEL = "Requires: ";
//		final String LOCKED_STATE = "Locked";
//		final String UNLOCKED_STATE = "Unlocked";
//
//		super.addInformation(stack, worldIn, tooltip, flagIn);
//		
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.category", getCategory()));
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_uses", getMaxDamage()));
//		
//		StringBuilder builder = new StringBuilder();
//		if (te.hasCustomName()) {
//			builder.append(TextFormatting.WHITE).append(te.getCustomName()).append(", ");
//		}
//		if (te.hasLocks()) {
//			builder.append(STATE_LABEL).append(TextFormatting.DARK_RED).append(LOCKED_STATE);
//			builder.append(TextFormatting.WHITE).append(", ");
//			builder.append(REQUIRES_LABEL);
//			for (LockState lockState : te.getLockStates()) {
//				if (lockState.getLock() != null) {
//					builder.append(TextFormatting.GOLD).append(lockState.getLock().getUnlocalizedName())
//					.append(" ");
//				}
//			}
//		}
//		else {
//			builder.append(STATE_LABEL).append(TextFormatting.GREEN).append(UNLOCKED_STATE);
//		}			
//		return builder.toString();
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

//	/**
//	 * 
//	 * @param state
//	 * @return
//	 */
//	@Override
//	public boolean hasTileEntity(IBlockState state) {
//		return true;
//	}
//
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

		boolean shouldRotate = false;
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
				//		        	tcte.setCustomName(stack.getItem().getUnlocalizedName());
				tcte.setCustomName(stack.getDisplayName());
			}

			// read in nbt
			if (stack.hasTagCompound()) {
				tcte.readFromItemStackNBT(stack.getTagCompound());
				forceUpdate = true;

				// get the old tcte facing direction
				oldPersistedChestDirection = Direction.fromFacing(EnumFacing.getFront(tcte.getFacing()));

				
				// dump stack NBT
				if (Treasure.logger.isDebugEnabled()) {
					dump(stack.getTagCompound(), new Coords(pos), "STACK ITEM -> CHEST NBT");
				}
			}

			// get the direction the block is facing.
			Direction direction = Direction.fromFacing(placer.getHorizontalFacing().getOpposite());

			// rotate the lock states
			shouldUpdate = rotateLockStates(worldIn, pos, oldPersistedChestDirection.getRotation(direction)); // old -> Direction.NORTH //

//			Treasure.logger.debug("New lock states ->");
//			for (LockState ls : tcte.getLockStates()) {
//				Treasure.logger.debug(ls);
//			}
			
			// update the TCTE facing
			tcte.setFacing(placer.getHorizontalFacing().getOpposite().getIndex());
		}
		if ((forceUpdate || shouldUpdate) && tcte != null) {
			// update the client
			tcte.sendUpdates();
		}
	}

	/**
	 * 
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		AbstractTreasureChestTileEntity te = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(pos);
		
		// TODO dump TE

		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
			return true;
		}
		else {
			// TODO message
		}
		
		boolean isLocked = false;
		// determine if chest is locked
		if (te.hasLocks()) {
			isLocked = true;
		}
		
		// open the chest
		if (!isLocked) {
			playerIn.openGui(Treasure.instance, getChestGuiID(), worldIn, pos.getX(), pos.getY(),	pos.getZ());
		}

		return true;
	}

	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		AbstractTreasureChestTileEntity te = (AbstractTreasureChestTileEntity) worldIn.getTileEntity(pos);

		Treasure.logger.debug("Breaking block....!");
		if (te != null && te.getInventoryProxy() != null) {
			// unlocked!
			if (!te.hasLocks()) {
//				Treasure.logger.debug("Not locked, dropping all items!");
				
				/*
				 * spawn inventory items
				 */
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)te.getInventoryProxy());

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);
				Treasure.logger.debug("Item being created from chest -> {}", chestItem.getItem().getRegistryName());
				InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), chestItem);

				/*
				 *  write the properties to the nbt
				 */
				if (!chestItem.hasTagCompound()) {
					chestItem.setTagCompound(new NBTTagCompound());
				}		
				te.writePropertiesToNBT(chestItem.getTagCompound());
			}
			else {
				Treasure.logger.debug("[BreakingBlock] Chest is locked, save locks and items to NBT");

				/*
				 * spawn chest item
				 */

				if (WorldInfo.isServerSide(worldIn)) {
					ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);

					// give the chest a tag compound
//					Treasure.logger.debug("[BreakingBlock]Saving chest items:");

					NBTTagCompound nbt = new NBTTagCompound();
					nbt = te.writeToNBT(nbt);
					chestItem.setTagCompound(nbt);
					
					InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), chestItem);
					
					//TEST  log all items in item
//					NonNullList<ItemStack> items = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
//					ItemStackHelper.loadAllItems(chestItem.getTagCompound(), items);
//					for (ItemStack stack : items) {
//						Treasure.logger.debug("[BreakingBlock] item in chest item -> {}", stack.getDisplayName());
//					}
				}
			}

			// remove the tile entity 
			worldIn.removeTileEntity(pos);
		}
	}

	/**
	 * Handled by breakBlock()
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	/**
	 * 
	 * @param tagCompound
	 */
	private void dump(NBTTagCompound tag, ICoords coords, String title) {
		ChestNBTPrettyPrinter printer  =new ChestNBTPrettyPrinter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");
		
		String filename = String.format("chest-nbt-%s-%s.txt", 
				formatter.format(new Date()), 
				coords.toShortString().replaceAll(" ", "-"));

		Path path = Paths.get(TreasureConfig.treasureFolder, "dumps").toAbsolutePath();
		try {
			Files.createDirectories(path);			
		} catch (IOException e) {
			Treasure.logger.error("Couldn't create directories for dump files:", e);
			return;
		}
		String s = printer.print(tag, Paths.get(path.toString(), filename), title);
		Treasure.logger.debug(s);
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
//	public TreasureChestBlock setTileEntityClass(Class<?> tileEntityClass) {
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
//	public TreasureChestBlock setChestType(TreasureChestType chestType) {
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
//	public TreasureChestBlock setBounds(AxisAlignedBB[] bounds) {
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
//	public TreasureChestBlock setRarity(Rarity rarity) {
//		this.rarity = rarity;
//		return this;
//	}

	/**
	 * @return the chestGuiID
	 */
	public int getChestGuiID() {
		return chestGuiID;
	}

	/**
	 * @param chestGuiID the chestGuiID to set
	 */
	public TreasureChestBlock setChestGuiID(int chestGuiID) {
		this.chestGuiID = chestGuiID;
		return this;
	}

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

}
