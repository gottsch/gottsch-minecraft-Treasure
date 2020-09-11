/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends ModItem {
	public static final int DEFAULT_MAX_USES = 25;
	
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

	/*
	 * Can the key take damage and lose durability
	 */
	private boolean damageable;
	
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
		setDamageable(true);
		setCraftable(false);
		setMaxDamage(DEFAULT_MAX_USES);
		setSuccessProbability(90D);	
		setMaxStackSize(1); // 12/3/2018: set to max 1 because keys are damaged and don't stack well.
	}

	/**
	 * Format:
	 * 		Item Name (vanilla minecraft)
	 * 		Rarity: [COMMON | UNCOMMON | SCARCE | RARE| EPIC]  [color = Gold] 
	 * 		Category:  [...] [color = Gold]
	 * 		Max Uses: [n] [color = Gold]
	 * 		Breakable: [Yes | No] [color = Dark Red | Green]
	 * 		Craftable: [Yes | No] [color = Green | Dark Red]
	 * 	 	Damageable: [Yes | No] [color = Dark Red | Green]
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
		
		String damageable = "";
		if (isDamageable()) {
			damageable = TextFormatting.DARK_RED + I18n.translateToLocal("tooltip.yes");
		}
		else {
			damageable =TextFormatting.GREEN + I18n.translateToLocal("tooltip.no");
		}
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.damageable", damageable));
	}
		
	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		BlockPos chestPos = pos;
		// determine if block at pos is a treasure chest
		Block block = worldIn.getBlockState(chestPos).getBlock();
		if (block instanceof ITreasureChestProxy) {
			chestPos = ((ITreasureChestProxy)block).getChestPos(chestPos);
			block = worldIn.getBlockState(chestPos).getBlock();
		}
		
		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			TileEntity te = worldIn.getTileEntity(chestPos);
			if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
				Treasure.logger.warn("Null or incorrect TileEntity");
				return EnumActionResult.FAIL;
			}
			AbstractTreasureChestTileEntity tcte = (AbstractTreasureChestTileEntity)te;
						
			// exit if on the client
			if (WorldInfo.isClientSide(worldIn)) {			
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
				boolean isKeyBroken = false;
				// check if this key is one that opens a lock (only first lock that key fits is unlocked).
				lockState = fitsFirstLock(tcte.getLockStates());
				if (lockState != null) {
					fitsLock = true;
				}
				
				if (fitsLock) {
					if (unlock(lockState.getLock())) {
						LockItem lock = lockState.getLock();
						// remove the lock
						lockState.setLock(null);
						// play noise
						worldIn.playSound(player, chestPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// update the client
						tcte.sendUpdates();
						// spawn the lock
						if (TreasureConfig.KEYS_LOCKS.enableLockDrops) {
							InventoryHelper.spawnItemStack(worldIn, (double)chestPos.getX(), (double)chestPos.getY(), (double)chestPos.getZ(), new ItemStack(lock));
						}
						// don't break the key
						breakKey = false;
					}
				}
						
				// check key's breakability
				if (breakKey) {
					if (isBreakable()  && TreasureConfig.KEYS_LOCKS.enableKeyBreaks) {
						// break key;
						heldItem.shrink(1);
						player.sendMessage(new TextComponentString("Key broke."));
						worldIn.playSound(player, chestPos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// flag the key as broken
						isKeyBroken = true;
						// if the keyStack > 0, then reset the damage - don't break a brand new key and leave the used one
						if (heldItem.getCount() > 0) {
							heldItem.setItemDamage(0);
						}
					}
					else {
						player.sendMessage(new TextComponentString("Failed to unlock."));
					}						
				}
				
				// user attempted to use key - increment the damage
				if (isDamageable() && !isKeyBroken) {
						heldItem.damageItem(1, player);
						if (heldItem.getItemDamage() == heldItem.getMaxDamage()) {
							heldItem.shrink(1);
					}
				}
			}
			catch (Exception e) {
				Treasure.logger.error("error: ", e);
			}			
		}		
		
		return super.onItemUse(player, worldIn, chestPos, hand, facing, hitX, hitY, hitZ);
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
	 * @param lockStates
	 * @return
	 */
	public LockState  fitsFirstLock(List<LockState> lockStates) {
		LockState lockState = null;
		// check if this key is one that opens a lock (only first lock that key fits is unlocked).
		for (LockState ls : lockStates) {
			if (ls.getLock() != null) {
				lockState = ls;
				if (lockState.getLock().acceptsKey(this) || fitsLock(lockState.getLock())) {
					return ls;
				}
			}
		}
		return null; // <-- TODO should return EMPTY_LOCKSTATE
	}
	
	/**
	 * 
	 * @param lockItem
	 * @return
	 */
	public boolean unlock(LockItem lockItem) {	
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			Treasure.logger.debug("Lock -> {} accepts key -> {}", lockItem.getRegistryName(), this.getRegistryName());
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

	/**
	 * @return the damageable
	 */
	public boolean isDamageable() {
		return damageable;
	}

	/**
	 * @param damageable the damageable to set
	 */
	public void setDamageable(boolean damageable) {
		this.damageable = damageable;
	}
}
