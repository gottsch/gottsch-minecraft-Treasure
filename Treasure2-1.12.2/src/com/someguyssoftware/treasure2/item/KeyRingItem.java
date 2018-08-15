/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.inventory.KeyRingInventory;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingItem extends ModItem {
	private static final String USED_ON_CHEST = "usedOnChest";

	/*
	 * The GUIID;
	 */
	private int keyRingGuiID = GuiHandler.KEY_RING_GUIID;

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public KeyRingItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.key_ring", TextFormatting.GOLD));
	}

	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		// exit if on the client
		if (worldIn.isRemote) {			
			return EnumActionResult.FAIL;
		}

		// use the key ring to unlock locks
		// determine if block at pos is a treasure chest
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block instanceof TreasureChestBlock) {
			// get the tile entity
			TileEntity te = worldIn.getTileEntity(pos);
			if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
				Treasure.logger.warn("Null or incorrect TileEntity");
				return EnumActionResult.FAIL;
			}
			AbstractTreasureChestTileEntity tcte = (AbstractTreasureChestTileEntity)te;

			ItemStack heldItem = player.getHeldItem(hand);
			if (!heldItem.hasTagCompound()) {
				heldItem.setTagCompound(new NBTTagCompound());
			}
			
			/*
			 *  set a flag that the item was used on a treasure chest. this is used to determine
			 *  if the keyring inventory should open or not.
			 */
			heldItem.getTagCompound().setBoolean(USED_ON_CHEST, true);

			// determine if chest is locked
			if (!tcte.hasLocks()) {
				return EnumActionResult.SUCCESS;
			}

			try {
				KeyRingInventory inv = new KeyRingInventory(heldItem);
				// cycle through all keys in key ring until one is able to fit lock and use it to unlock the lock.
				for (int i = 0; i < inv.getSizeInventory(); i++) {
					ItemStack keyStack = inv.getStackInSlot(i);
					if (keyStack != null && keyStack.getItem() != Items.AIR)  {								
						KeyItem key = (KeyItem) keyStack.getItem();
						Treasure.logger.debug("Using key from keyring: {}", key.getUnlocalizedName());
						boolean breakKey = true;
						//	boolean fitsLock = false;
						LockState lockState = null;

						// check if this key is one that opens a lock (only first lock that key fits is unlocked).
						lockState = key.fitsFirstLock(tcte.getLockStates());

						Treasure.logger.debug("key fits lock: {}", lockState);

						// TODO move to a method in KeyItem
						if (lockState != null) {
							if (key.unlock(lockState.getLock())) {
								LockItem lock = lockState.getLock();
								// remove the lock
								lockState.setLock(null);
								// play noise
								worldIn.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
								// update the client
								tcte.sendUpdates();
								// spawn the lock
								if (TreasureConfig.enableLockDrops) {
									InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(lock));
								}
								// don't break the key
								breakKey = false;
							}

							// TODO move to a method in KeyItem
							if (breakKey) {
								if (key.isBreakable()  && TreasureConfig.enableKeyBreaks) {
									// break key;
									heldItem.shrink(1);
									player.sendMessage(new TextComponentString("Key broke."));
									worldIn.playSound(player, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);

								}
								else {
									player.sendMessage(new TextComponentString("Failed to unlock."));
									if (isDamageable()) {
										heldItem.damageItem(1, player);
									}
								}						
							}
							// key unlocked a lock, end loop (ie only unlock 1 lock at a time
							break;
						}
					}
				}				
			}
			catch (Exception e) {
				Treasure.logger.error("error: ", e);
			}

		}
		// this should prevent the onItemRightClick from happening./
		return EnumActionResult.PASS;
	}

	/**
	 * 
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		// exit if on the client
		if (worldIn.isRemote) {			
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}

		boolean useOnChest = false;
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (stack.hasTagCompound()) {
			useOnChest = stack.getTagCompound().getBoolean(USED_ON_CHEST);
		}
		// exit if item already used on chest
		if (useOnChest) {
			stack.getTagCompound().setBoolean(USED_ON_CHEST, false);
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}
		else {		
			BlockPos pos = playerIn.getPosition();
			playerIn.openGui(Treasure.instance, getKeyRingGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}

	/**
	 * @return the keyRingGuiID
	 */
	public int getKeyRingGuiID() {
		return keyRingGuiID;
	}

	/**
	 * @param keyRingGuiID the keyRingGuiID to set
	 */
	public void setKeyRingGuiID(int keyRingGuiID) {
		this.keyRingGuiID = keyRingGuiID;
	}
}
