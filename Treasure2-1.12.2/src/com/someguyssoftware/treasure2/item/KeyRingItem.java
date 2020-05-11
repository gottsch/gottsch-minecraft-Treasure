/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
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
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
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
	public static final String IS_OPEN = "isOpen";

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
	 * Called when the item is crafted (not added via Creative).
	 * Initializes the item with a tag compound inital values.
	 */
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		if (WorldInfo.isServerSide(worldIn)) {			
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
			}
			stack.getTagCompound().setBoolean(USED_ON_CHEST, false);
			stack.getTagCompound().setBoolean(IS_OPEN, false);
		}
		super.onCreated(stack, worldIn, playerIn);
	}
	
	/**
	 * Call before the block is activated. (Isn't called for right click on non-item ie AIR)
	 * Initializes the item with a tag compound inital values.
	 * This *helps* initialize the itemStack's tag compound when it is added to the players inventory via the creative gui.
	 * You still have to use on a block before you can open the inventory.
	 * 
	 *   This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @param hand Which hand the item is being held in.
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
	 */
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (WorldInfo.isServerSide(world)) {			
			ItemStack heldItem = player.getHeldItem(hand);
			if (!heldItem.hasTagCompound()) {
				heldItem.setTagCompound(new NBTTagCompound());
			}
			heldItem.getTagCompound().setBoolean(USED_ON_CHEST, false);
		}
        return EnumActionResult.PASS;
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

		boolean isKeyBroken = false;
		
		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
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
								if (TreasureConfig.KEYS_LOCKS.enableLockDrops) {
									InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(lock));
								}
								// don't break the key
								breakKey = false;
							}


							// TODO move to a method in KeyItem
							if (breakKey) {
								if (key.isBreakable()  && TreasureConfig.KEYS_LOCKS.enableKeyBreaks) {
									// break key;
									keyStack.shrink(1);
									player.sendMessage(new TextComponentString("Key broke."));
									worldIn.playSound(player, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);
									// the key is broken, do not attempt to damage it.
									isKeyBroken = true;
									// if the keyStack > 0, then reset the damage - don't break a brand new key and leave the used one
									if (keyStack.getCount() > 0) {
										keyStack.setItemDamage(0);
									}
								}
								else {
									player.sendMessage(new TextComponentString("Failed to unlock."));
								}						
							}
							if (key.isDamageable() && !isKeyBroken) {
								keyStack.damageItem(1, player);
							}
							else {
								Treasure.logger.debug("Key in keyring is NOT damageable.");
							}			

							/*
							 * write inventory to the key ring and
							 *  reset a flag that the item was used on a treasure chest, because it is overwritten by the ItemStackHelper
							 */
							ItemStackHelper.saveAllItems(heldItem.getTagCompound(), inv.getItems());
							heldItem.getTagCompound().setBoolean(USED_ON_CHEST, true);
							
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
		if (WorldInfo.isClientSide(worldIn)) {			
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
	 * 
	 */
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		// NOTE only works on 'Q' press, not mouse drag and drop
		if (item.getTagCompound().getBoolean(IS_OPEN)) {
			return false;
		}
		else {
			return super.onDroppedByPlayer(item, player);
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
