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
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
	public KeyItem(String modID, String name, Item.Properties properties) {
		super(modID, name, 
				properties
				.group(TreasureItemGroups.MOD_ITEM_GROUP)
//				.maxStackSize(1) // can't have damage AND stacksize.
				.maxDamage(DEFAULT_MAX_USES));

		setCategory(Category.BASIC);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setDamageable(true);
		setCraftable(false);
		setSuccessProbability(90D);	
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
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(new TranslationTextComponent("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
		tooltip.add(new TranslationTextComponent("tooltip.label.category", TextFormatting.GOLD + getCategory().toString()));
		tooltip.add(new TranslationTextComponent("tooltip.label.max_uses", TextFormatting.GOLD + String.valueOf(getMaxDamage())));

		// is breakable tooltip
		ITextComponent breakable = null;
		if (isBreakable()) {
			breakable = new TranslationTextComponent("tooltip.yes").applyTextStyle(TextFormatting.DARK_RED);
		}
		else {
			breakable = new TranslationTextComponent("tooltip.no").applyTextStyle(TextFormatting.GREEN);
		}
		tooltip.add(
				new TranslationTextComponent("tooltip.label.breakable", breakable));
		
		ITextComponent craftable = null;
		if (isCraftable()) {
			craftable = new TranslationTextComponent("tooltip.yes").applyTextStyle(TextFormatting.GREEN);
		}
		else {
			craftable = new TranslationTextComponent("tooltip.no").applyTextStyle(TextFormatting.DARK_RED);
		}
		tooltip.add(new TranslationTextComponent("tooltip.label.craftable", craftable));
		
		ITextComponent damageable = null;
		if (isDamageable()) {
			damageable = new TranslationTextComponent("tooltip.yes").applyTextStyle(TextFormatting.DARK_RED);
		}
		else {
			damageable = new TranslationTextComponent("tooltip.no").applyTextStyle(TextFormatting.GREEN);
		}
		tooltip.add(
				new TranslationTextComponent("tooltip.label.damageable", damageable));
	}
		
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockPos chestPos = context.getPos();
		// determine if block at pos is a treasure chest
		Block block = context.getWorld().getBlockState(chestPos).getBlock();
		if (block instanceof ITreasureChestProxy) {
			chestPos = ((ITreasureChestProxy)block).getChestPos(chestPos);
			block = context.getWorld().getBlockState(chestPos).getBlock();
		}
		
		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			TileEntity te = context.getWorld().getTileEntity(chestPos);
			if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
				Treasure.LOGGER.warn("Null or incorrect TileEntity");
				return ActionResultType.FAIL;
			}
			AbstractTreasureChestTileEntity tcte = (AbstractTreasureChestTileEntity)te;
						
			// exit if on the client
			if (WorldInfo.isClientSide(context.getWorld())) {			
				return ActionResultType.FAIL;
			}

			// determine if chest is locked
			if (!tcte.hasLocks()) {
				return ActionResultType.SUCCESS;
			}
			
			try {
				ItemStack heldItem = context.getPlayer().getHeldItem(context.getHand());	
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
						context.getWorld().playSound(context.getPlayer(), chestPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// update the client
						tcte.sendUpdates();
						// spawn the lock
						if (TreasureConfig.KEYS_LOCKS.enableLockDrops.get()) {
							InventoryHelper.spawnItemStack(context.getWorld(), (double)chestPos.getX(), (double)chestPos.getY(), (double)chestPos.getZ(), new ItemStack(lock));
						}
						// don't break the key
						breakKey = false;
					}
				}
						
				// check key's breakability
				if (breakKey) {
					if (isBreakable()  && TreasureConfig.KEYS_LOCKS.enableKeyBreaks.get()) {
						// break key;
						heldItem.shrink(1);
						context.getPlayer().sendMessage(new StringTextComponent("Key broke."));
						context.getWorld().playSound(context.getPlayer(), chestPos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 0.3F, 0.6F);
						// flag the key as broken
						isKeyBroken = true;
						// if the keyStack > 0, then reset the damage - don't break a brand new key and leave the used one
						if (heldItem.getCount() > 0) {
							heldItem.setDamage(0);
						}
					}
					else {
						context.getPlayer().sendMessage(new StringTextComponent("Failed to unlock."));
					}						
				}
				
				// user attempted to use key - increment the damage
				if (isDamageable() && !isKeyBroken) {
						heldItem.damageItem(1,  context.getPlayer(), p -> {
							p.sendBreakAnimation(EquipmentSlotType.MAINHAND);
						});
						if (heldItem.getDamage() == heldItem.getMaxDamage()) {
							heldItem.shrink(1);
					}
				}
			}
			catch (Exception e) {
				Treasure.LOGGER.error("error: ", e);
			}			
		}		
		
		return super.onItemUse(context);
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
			Treasure.LOGGER.debug("Lock -> {} accepts key -> {}", lockItem.getRegistryName(), this.getRegistryName());
			if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
				Treasure.LOGGER.debug("Unlock attempt met probability");
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
