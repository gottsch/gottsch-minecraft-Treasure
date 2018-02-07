/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends ModItem {
	/*
	 * The category that the key belongs to
	 */
	private Category category;
	
	/*
	 * The rarity of the key
	 */
	private Rarity rarity;	

	/*
	 * Is the key craftable
	 */
	private boolean craftable;
	
	/*
	 * Can the key break attempting to unlock a lock
	 */
	private boolean breakable;
	
	// TODO need a LockMessage - an enum to indicate the action that resulted from unlock, ex. SUCCESS, FAIL, DESTROY, NO_FIT, etc.
	// instead of just a true/false result.

	/*
	 * The probability of a successful unlocking
	 */
	private double successProbability;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public KeyItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
		setCategory(Category.BASIC);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setCraftable(false);
		setMaxDamage(25);
		setSuccessProbability(100D);		
	}

	/**
	 * Format:
	 * 		Item Name (vanilla minecraft)
	 * 		Rarity: [COMMON | UNCOMMON | SCARCE | RARE| EPIC]  [color = Gold] 
	 * 		Category:  [...] [color = Gold]
	 * 		Max Uses: [n] [color = Gold]
	 * 		Breakable: [Yes | No] [color = Dark Red | Green]
	 * 		Craftable: [Yes | No] [color = Green | Dark Red]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.category", getCategory()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.max_uses", getMaxDamage()));

		// is breakable tooltip
		String breakable = "";
		if (isBreakable()) {
			breakable = TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.yes");
		}
		else {
			breakable =TextFormatting.GREEN + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.breakable", breakable));
		
		String craftable = "";
		if (isCraftable()) {
			craftable = TextFormatting.GREEN + I18n.translateToLocal("tooltip.yes");
		}
		else {
			craftable =TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(	I18n.translateToLocalFormatted("tooltip.label.craftable", craftable));
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
			TileEntity te = worldIn.getTileEntity(pos);
			if (te == null || !(te instanceof TreasureChestTileEntity)) {
				Treasure.logger.warn("Null or incorrect TileEntity");
				return EnumActionResult.FAIL;
			}
			TreasureChestTileEntity tcte = (TreasureChestTileEntity)te;
						
			// exit if on the client
			if (worldIn.isRemote) {			
				return EnumActionResult.FAIL;
			}

			// determine if chest is locked
			if (!tcte.hasLocks()) {
				return EnumActionResult.SUCCESS;
			}
			
			try {
				ItemStack heldItem = player.getHeldItem(hand);	
				boolean breakKey = true;
				boolean fitsLock = false;
				LockState lockState = null;
				
				// check if this key is one that opens a lock (only first lock that key fits is unlocked).
				for (LockState ls : tcte.getLockStates()) {
					if (ls.getLock() != null) {
						lockState = ls;
						if (lockState.getLock().acceptsKey(this) || fitsLock(lockState.getLock())) {
							fitsLock = true;
							break;
						}
					}
				}
				
				if (fitsLock) {
					if (unlock(lockState.getLock())) {
						LockItem lock = lockState.getLock();
						// remove the lock
						lockState.setLock(null);
						// play noise
						worldIn.playSound(player, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// update the client
						tcte.sendUpdates();
						// spawn the lock
						InventoryHelper.spawnItemStack(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), new ItemStack(lock));
						// don't break the key
						breakKey = false;
					}
				}
						
				// check key's breakability
				if (breakKey) {
					if (isBreakable()  && TreasureConfig.enableKeyBreaks) {
						// break key;
						heldItem.shrink(1);
						player.sendMessage(new TextComponentString("Key broke."));
						worldIn.playSound(player, pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);

//						if (heldItem.getCount() <=0) {
//							IInventory inventory = player.inventory;
//							inventory.setInventorySlotContents(player.inventory.currentItem, null);
//						}		
					}
					else {
						player.sendMessage(new TextComponentString("Failed to unlock."));
						// TODO test if it is damagable
						heldItem.damageItem(1, player);
					}						
				}
			}
			catch (Exception e) {
				Treasure.logger.error("error: ", e);
			}			
		}		
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	/**
	 * This method is a secondary check against a lock item.
	 * Override this method to overrule LockItem.acceptsKey() if this is a key with special abilities.
	 * @param lockItem
	 * @return
	 */
	public boolean fitsLock(LockItem lockItem) {
		return false;
	}
	
	/**
	 * 
	 * @param lockItem
	 * @return
	 */
	public boolean unlock(LockItem lockItem) {	
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			Treasure.logger.debug("Lock accepts key");
			if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
				Treasure.logger.debug("Unlock attempt met probability");
				return true;
			}
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
	public KeyItem setRarity(Rarity rarity) {
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
	public KeyItem setCraftable(boolean craftable) {
		this.craftable = craftable;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KeyItem [rarity=" + rarity + ", craftable=" + craftable + "]";
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
	public KeyItem setCategory(Category category) {
		this.category = category;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isBreakable() {
		return breakable;
	}
	
	/**
	 * 
	 * @param breakable
	 */
	public KeyItem setBreakable(boolean breakable) {
		this.breakable = breakable;
		return this;
	}

	/**
	 * @param damage the uses to set
	 */
	public KeyItem setMaxDamage(int damage) {
		super.setMaxDamage(damage);
		return this;
	}

	/**
	 * @return the successProbability
	 */
	public double getSuccessProbability() {
		return successProbability;
	}

	/**
	 * @param successProbability the successProbability to set
	 */
	public KeyItem setSuccessProbability(double successProbability) {
		this.successProbability = successProbability;
		return this;
	}
}
