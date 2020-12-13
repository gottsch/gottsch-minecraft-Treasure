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
import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

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
	public TreasureChestBlock(String modID, String name, Class<? extends ITreasureChestTileEntity> te,
			TreasureChestType type, Rarity rarity) {
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
	public TreasureChestBlock(String modID, String name, Material material,
			Class<? extends ITreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
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

	/**
	 * Called just after the player places a block.
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
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
				// tcte.setCustomName(stack.getItem().getUnlocalizedName());
				tcte.setCustomName(stack.getDisplayName());
			}

			// read in nbt
			if (stack.hasTagCompound()) {
				tcte.readFromItemStackNBT(stack.getTagCompound());
				forceUpdate = true;

				// get the old tcte facing direction
				oldPersistedChestDirection = Direction.fromFacing(EnumFacing.getFront(tcte.getFacing()));

				// dump stack NBT
				if (Treasure.logger.isDebugEnabled() && WorldInfo.isServerSide(worldIn)) {
					dump(stack.getTagCompound(), new Coords(pos), "STACK ITEM -> CHEST NBT");
				}
			}

			// get the direction the block is facing.
			Direction direction = Direction.fromFacing(placer.getHorizontalFacing().getOpposite());

			// rotate the lock states
			shouldUpdate = rotateLockStates(worldIn, pos, oldPersistedChestDirection.getRotation(direction));
			// old -> Direction.NORTH

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

		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {
			return true;
		}

		boolean isLocked = false;
		// determine if chest is locked
		if (te.hasLocks()) {
			isLocked = true;
		}

		// open the chest
		if (!isLocked) {
			playerIn.openGui(Treasure.instance, getChestGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		Treasure.logger.debug("Breaking block....!");
		
		TileEntity tileEntity = worldIn.getTileEntity(pos);
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
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) te);

				/*
				 * spawn chest item
				 */
				ItemStack chestItem = new ItemStack(Item.getItemFromBlock(this), 1);
				Treasure.logger.debug("Item being created from chest -> {}", chestItem.getItem().getRegistryName());
				InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
						chestItem);

				/*
				 * write the properties to the nbt
				 */
				if (!chestItem.hasTagCompound()) {
					chestItem.setTagCompound(new NBTTagCompound());
				}
				te.writePropertiesToNBT(chestItem.getTagCompound());
			} else {
				Treasure.logger.debug("[BreakingBlock] ChestConfig is locked, save locks and items to NBT");

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

					InventoryHelper.spawnItemStack(worldIn, (double) pos.getX(), (double) pos.getY(),
							(double) pos.getZ(), chestItem);

					// TEST log all items in item
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
		else {
			// default to regular block break;
			super.breakBlock(worldIn, pos, state);
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
		ChestNBTPrettyPrinter printer = new ChestNBTPrettyPrinter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyymmdd");

		String filename = String.format("chest-nbt-%s-%s.txt", formatter.format(new Date()),
				coords.toShortString().replaceAll(" ", "-"));

		Path path = Paths.get(TreasureConfig.LOGGING.folder, "dumps").toAbsolutePath();
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			Treasure.logger.error("Couldn't create directories for dump files:", e);
			return;
		}
		String s = printer.print(tag, Paths.get(path.toString(), filename), title);
		Treasure.logger.debug(s);
	}

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

}
