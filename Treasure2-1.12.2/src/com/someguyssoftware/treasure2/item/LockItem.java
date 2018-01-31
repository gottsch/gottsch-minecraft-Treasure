/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public class LockItem extends ModItem {
	
	/*
	 * The category that the lock belongs to
	 */
	private Category category;

	/*
	 * The rarity of the lock
	 */
	private Rarity rarity;	

	/*
	 * Flag if the lock is craftable
	 */
	private boolean craftable;

	/*
	 * a list of keys that unlock the lock
	 */
	private List<KeyItem> keys = new ArrayList<>(3);
	
	/**
	 * 
	 * @param item
	 * @param keys
	 */
	public LockItem(String modID, String name, KeyItem[] keys) {
		this(modID, name);
		getKeys().addAll(Arrays.asList(keys));
	}
		
	/**
	 * 
	 * @param item
	 */
	public LockItem(String modID, String name) {
		setItemName(modID, name);
		setCategory(Category.BASIC);
		setRarity(Rarity.COMMON);
		setCraftable(false);
		setCreativeTab(Treasure.TREASURE_TAB);
		// TODO successChance (0.0F - 1.0F)
		Random random = new Random();
		random.nextFloat();
		// TODO add checkProbability(random, float) to RandomHelper()
	}
	
	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		// determine if block at pos is a treasure chest
		Block block = worldIn.getBlockState(pos).getBlock();
		if (block instanceof TreasureChestBlock) {
			// get the tile entity
			TreasureChestTileEntity te = (TreasureChestTileEntity) worldIn.getTileEntity(pos);
						
			// exit if on the client
			if (worldIn.isRemote) {			
				return EnumActionResult.FAIL;
			}
			
			try {
				ItemStack heldItem = player.getHeldItem(hand);
				// handle the lock
				// NOTE don't use the return boolean as the locked flag here, as the chest is already locked and if the method was
				// unsuccessful it could state the chest is unlocked.
				handleHeldLock(te, player, heldItem);
			}
			catch (Exception e) {
				Treasure.logger.error("error: ", e);
			}
		}		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
		
	/**
	 * 
	 * @param te
	 * @param player
	 * @param heldItem
	 * @return flag indicating if a lock was added
	 */
	private boolean handleHeldLock(TreasureChestTileEntity te, EntityPlayer player, ItemStack heldItem) {
		boolean lockedAdded = false;
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
				lockedAdded = true;
				break;
			}
		}
		return lockedAdded;
	}
	
	/**
	 * 
	 * @param keyItem
	 * @return
	 */
	public boolean acceptsKey(KeyItem keyItem) {
		Treasure.logger.debug("Testing keys against keyItem {}", keyItem);
		for (KeyItem k : getKeys()) {
			Treasure.logger.debug("Current key: {}", k);
			if (k == keyItem) return true;
		}
		return false;
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
	public LockItem setRarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	/**
	 * @return the craftable
	 */
	public boolean isCraftable() {
		return craftable;
	}

	/**
	 * @param craftable the craftable to set
	 */
	public LockItem setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}

	/**
	 * @return the keys
	 */
	public List<KeyItem> getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public LockItem setKeys(List<KeyItem> keys) {
		this.keys = keys;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockItem [name=" + getRegistryName() + ", rarity=" + rarity + ", craftable=" + craftable + ", keys=" + keys + "]";
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public LockItem setCategory(Category category) {
		this.category = category;
		return this;
	}
}
