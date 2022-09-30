/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.item;

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.DURABILITY;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.ITreasureChestProxy;
import com.someguyssoftware.treasure2.capability.DurabilityCapabilityProvider;
import com.someguyssoftware.treasure2.capability.DurabilityCapabilityStorage;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends ModItem implements IKeyEffects {
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

	private static final DurabilityCapabilityStorage CAPABILITY_STORAGE = new DurabilityCapabilityStorage();
	
	/**
	 * 
	 * @param properties
	 */
	public KeyItem(Item.Properties properties) {
		super(properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP).defaultDurability(DEFAULT_MAX_USES));
		setCategory(Category.ELEMENTAL);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setDamageable(true);
		setCraftable(false);
		setSuccessProbability(90D);	
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public KeyItem(String modID, String name, Item.Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP).defaultDurability(DEFAULT_MAX_USES));
		setCategory(Category.ELEMENTAL);
		setRarity(Rarity.COMMON);
		setBreakable(true);
		setDamageable(true);
		setCraftable(false);
		setSuccessProbability(90D);	
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        DurabilityCapabilityProvider provider = new DurabilityCapabilityProvider();
        LazyOptional<IDurabilityCapability> cap = provider.getCapability(DURABILITY, null);
        cap.ifPresent(c -> {
        	c.setDurability(stack.getMaxDamage());
        });
		return provider;
	}
	
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands
	 * or having the client update when the Anvil GUI is open.
	 */
	@Override
    public CompoundNBT getShareTag(ItemStack stack) {
		CompoundNBT nbt = null;
		// read effective max damage cap -> write nbt
		nbt = (CompoundNBT) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.DURABILITY, 
				stack.getCapability(TreasureCapabilities.DURABILITY).map(cap -> cap).orElse(null),
				null);
		return nbt;
	}
	
	@Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        super.readShareTag(stack, nbt);
        // read nbt -> write key item
        CAPABILITY_STORAGE.readNBT(
        		TreasureCapabilities.DURABILITY, 
				stack.getCapability(TreasureCapabilities.DURABILITY).map(cap -> cap).orElse(null),
				null,
				nbt);
    }
	
	/**
	 * Format:
	 * 		Item Name (vanilla minecraft)
	 * 		Rarity: [COMMON | UNCOMMON | SCARCE | RARE| EPIC]  [color = Gold] 
	 * 		Category:  [...] [color = Gold]
	 * 		Uses Remaining: [n]
	 * 		Max Uses: [n] [color = Gold]
	 * 		Breakable: [Yes | No] [color = Dark Red | Green]
	 * 		Craftable: [Yes | No] [color = Green | Dark Red]
	 * 	 	Damageable: [Yes | No] [color = Dark Red | Green]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);

		tooltip.add(new TranslationTextComponent("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
		tooltip.add(new TranslationTextComponent("tooltip.label.category", TextFormatting.GOLD + getCategory().toString()));

		stack.getCapability(DURABILITY).ifPresent(cap -> {
			tooltip.add(new TranslationTextComponent("tooltip.label.uses", cap.getDurability() - stack.getDamageValue(), cap.getDurability()));
		});

		tooltip.add(new TranslationTextComponent("tooltip.label.max_uses", TextFormatting.GOLD + String.valueOf(getMaxDamage())));

		// is breakable tooltip
		ITextComponent breakable = null;
		if (isBreakable()) {
			breakable = new TranslationTextComponent("tooltip.yes").withStyle(TextFormatting.DARK_RED);
		}
		else {
			breakable = new TranslationTextComponent("tooltip.no").withStyle(TextFormatting.GREEN);
		}
		tooltip.add(
				new TranslationTextComponent("tooltip.label.breakable", breakable));

		ITextComponent craftable = null;
		if (isCraftable()) {
			craftable = new TranslationTextComponent("tooltip.yes").withStyle(TextFormatting.GREEN);
		}
		else {
			craftable = new TranslationTextComponent("tooltip.no").withStyle(TextFormatting.DARK_RED);
		}
		tooltip.add(new TranslationTextComponent("tooltip.label.craftable", craftable));

		ITextComponent damageable = null;
		if (isDamageable()) {
			damageable = new TranslationTextComponent("tooltip.yes").withStyle(TextFormatting.DARK_RED);
		}
		else {
			damageable = new TranslationTextComponent("tooltip.no").withStyle(TextFormatting.GREEN);
		}
		tooltip.add(
				new TranslationTextComponent("tooltip.label.damageable", damageable));
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return stack.getCapability(DURABILITY).map(cap -> {
			return (double)stack.getDamageValue() / (double) cap.getDurability();
		}).orElse((double)stack.getDamageValue() / (double)stack.getMaxDamage());
	}

	/**
	 * 
	 */
	@Override
	public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack resourceItem) {
		return resourceItem.getItem() == this || super.isValidRepairItem(itemToRepair, resourceItem);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		BlockPos chestPos = context.getClickedPos();
		// determine if block at pos is a treasure chest
		Block block = context.getLevel().getBlockState(chestPos).getBlock();
		if (block instanceof ITreasureChestProxy) {
			chestPos = ((ITreasureChestProxy)block).getChestPos(chestPos);
			block = context.getLevel().getBlockState(chestPos).getBlock();
		}

		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			TileEntity tileEntity = context.getLevel().getBlockEntity(chestPos);
			if (tileEntity == null || !(tileEntity instanceof AbstractTreasureChestTileEntity)) {
				Treasure.LOGGER.warn("Null or incorrect TileEntity");
				return ActionResultType.FAIL;
			}
			AbstractTreasureChestTileEntity chestTileEntity = (AbstractTreasureChestTileEntity)tileEntity;

			// exit if on the client
			if (WorldInfo.isClientSide(context.getLevel())) {			
				return ActionResultType.FAIL;
			}

			// determine if chest is locked
			if (!chestTileEntity.hasLocks()) {
				return ActionResultType.SUCCESS;
			}

			try {
				ItemStack heldItemStack = context.getPlayer().getItemInHand(context.getHand());	
				boolean breakKey = true;
				boolean fitsLock = false;
				LockState lockState = null;
				boolean isKeyBroken = false;
				// check if this key is one that opens a lock (only first lock that key fits is unlocked).
				lockState = fitsFirstLock(chestTileEntity.getLockStates());
				if (lockState != null) {
					fitsLock = true;
				}

				if (fitsLock) {
					if (unlock(lockState.getLock())) {
						// unlock the lock
						doUnlock(context, chestTileEntity, lockState);
						
						// update the client
						chestTileEntity.sendUpdates();
						
						// don't break the key
						breakKey = false;
					}
				}

				IDurabilityCapability cap = heldItemStack.getCapability(DURABILITY).orElseThrow(IllegalStateException::new);
				
				// check key's breakability
				if (breakKey) {
					if ((isBreakable() || anyLockBreaksKey(chestTileEntity.getLockStates(), this)) && TreasureConfig.KEYS_LOCKS.enableKeyBreaks.get()) {
						
						// TODO make method breakAndShrink() using caps
						int damage = heldItemStack.getDamageValue() + (getMaxDamage() - (heldItemStack.getDamageValue() % getMaxDamage()));
                        heldItemStack.setDamageValue(damage);
                        if (heldItemStack.getDamageValue() >= cap.getDurability()) {
							// break key;
							heldItemStack.shrink(1);
                        }
                        
                        // do effects
                        doKeyBreakEffects(context.getLevel(), context.getPlayer(), chestPos, chestTileEntity);
                        
						// flag the key as broken
						isKeyBroken = true;
					}
					else if (!fitsLock) {
						doKeyNotFitEffects(context.getLevel(), context.getPlayer(), chestPos, chestTileEntity);
					}
					else {
						doKeyUnableToUnlockEffects(context.getLevel(), context.getPlayer(), chestPos, chestTileEntity);
					}						
				}

				// user attempted to use key - increment the damage
				if (isDamageable() && !isKeyBroken) {
					 heldItemStack.setDamageValue(heldItemStack.getDamageValue() + 1);
                    if (heldItemStack.getDamageValue() >= cap.getDurability()) {
                        heldItemStack.shrink(1);
                    }
				}
			}
			catch (Exception e) {
				Treasure.LOGGER.error("error: ", e);
			}			
		}		

		return super.useOn(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param chestTileEntity
	 * @param lockState
	 */
	public void doUnlock(ItemUseContext context, AbstractTreasureChestTileEntity chestTileEntity,	LockState lockState) {
		LockItem lock = lockState.getLock();		
		 lock.doUnlock(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity, lockState);
				
		if (!breaksLock(lock)) {
			// spawn the lock
			lock.dropLock(context.getLevel(), context.getClickedPos());
		}		
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
	 * 
	 * @param lockItem
	 * @return
	 */
	public boolean breaksLock(LockItem lockItem) {
		return false;
	}

	/**
	 * 
	 * @param lockStates
	 * @param key
	 * @return
	 */
	public boolean anyLockBreaksKey(List<LockState> lockStates, KeyItem key) {
		for (LockState ls : lockStates) {
			if (ls.getLock() != null) {
				if (ls.getLock().breaksKey(key)) {
					return true;
				}				
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
