/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlockProxy;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.capability.DurabilityCapability;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.effects.IKeyEffects;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

/**
 * 
 * @author Mark Gottschling on Jan 11, 2018
 *
 */
public class KeyItem extends Item implements IKeyEffects {
	public static final int DEFAULT_MAX_USES = 25;
	public static final String DURABILITY_TAG = "treasure2:durability";
	
	/*
	 * The category that the key belongs to
	 */
	private KeyLockCategory category;

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
//	private boolean damageable;

	/*
	 * The probability of a successful unlocking
	 */
	private double successProbability;

	/*
	 * A list of predicates that determine if a key fits into a lock.
	 */
	private List<Predicate<LockItem >> fitsLock;
	
	/*
	 * A list of predicates that determine if a key will break a lock.
	 */
	private List<Predicate<LockItem >> breaksLock;

	/**
	 * 
	 * @param properties
	 */
	public KeyItem(Item.Properties properties) {
		super(properties.defaultDurability(DEFAULT_MAX_USES));
		setCategory(KeyLockCategory.ELEMENTAL);
		setBreakable(true);
		setCraftable(false);
		setSuccessProbability(90D);	
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		DurabilityCapability provider = new DurabilityCapability();
		LazyOptional<IDurabilityHandler> cap = provider.getCapability(TreasureCapabilities.DURABILITY, null);
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
	public CompoundTag getShareTag(ItemStack stack) {
		super.getShareTag(stack);

		CompoundTag capabilityTag = null;

		IDurabilityHandler handler = stack.getCapability(DURABILITY).map(h -> h).orElse(null);
		if (handler != null) {
			capabilityTag = handler.save();
		}
		CompoundTag stackTag = stack.getOrCreateTag();
		// NOTE must ensure to add the capability tag to the original stack tag.
		if (capabilityTag != null) {
			stackTag.put(DURABILITY_TAG, capabilityTag);
		}
		return stackTag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
		super.readShareTag(stack, tag);

		if (tag.contains(DURABILITY_TAG)) {
			IDurabilityHandler handler = stack.getCapability(DURABILITY).map(h -> h).orElse(null);
			if (handler != null) {
				handler.load(tag.get(DURABILITY_TAG));
			}
		}
	}

	/**
	 * Format:
	 * 		Item Name (vanilla minecraft)
	 * 		Rarity: [...]  [color = Gold] 
	 * 		Category:  [...] [color = Gold]
	 * 		Uses Remaining: [n]
	 * 		Max Uses: [n] [color = Gold]
	 * 		Breakable: [Yes | No] [color = Dark Red | Green]
	 * 		Craftable: [Yes | No] [color = Green | Dark Red]
	 * 	 	Damageable: [Yes | No] [color = Dark Red | Green]
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {

		if (stack.getCapability(DURABILITY).isPresent()) {
			stack.getCapability(DURABILITY).ifPresent(cap -> {
				if (cap.isInfinite()) {
					tooltip.add(new TranslatableComponent(LangUtil.tooltip("cap.durability.amount.infinite"), cap.getDurability() - stack.getDamageValue(), cap.getDurability()));					
				}
				else {
					tooltip.add(new TranslatableComponent(LangUtil.tooltip("cap.durability.amount"), cap.getDurability() - stack.getDamageValue(), cap.getDurability()));
				}
			});
		}
		else {
			tooltip.add(new TranslatableComponent(LangUtil.tooltip("cap.durability.amount"), stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()));
		}
		
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.rarity"), ChatFormatting.BLUE + new TranslatableComponent(getRarity().getValue().toLowerCase()).getString().toUpperCase() ));
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.category"), ChatFormatting.GOLD + new TranslatableComponent(getCategory().toString().toLowerCase()).getString().toUpperCase()));

		LangUtil.appendAdvancedHoverText(tooltip, tt -> {
			
			// is breakable tooltip
			MutableComponent breakable = null;
			if (isBreakable()) {
				breakable = new TranslatableComponent(LangUtil.tooltip("boolean.yes")).withStyle(ChatFormatting.DARK_RED);
			}
			else {
				breakable = new TranslatableComponent(LangUtil.tooltip("boolean.no")).withStyle(ChatFormatting.GREEN);
			}
			tooltip.add(
					new TranslatableComponent(LangUtil.tooltip("key_lock.breakable"), breakable));

			MutableComponent craftable = null;
			if (isCraftable()) {
				craftable = new TranslatableComponent(LangUtil.tooltip("boolean.yes")).withStyle(ChatFormatting.GREEN);
			}
			else {
				craftable = new TranslatableComponent(LangUtil.tooltip("boolean.no")).withStyle(ChatFormatting.DARK_RED);
			}
			tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.craftable"), craftable));
		
			appendHoverSpecials(stack, worldIn, tooltip, flag);
			appendHoverExtras(stack, worldIn, tooltip, flag);
		});
		// NOTE adding curse here makes it unremovable.
		// TODO adding curse AFTER the HOLD lambda only adds it once to the tooltip.
		// if added BEFORE, like in initCapabilities(), it is added twice to the tooltip
		// TEMP fix for double Curses displayed
		appendCurse(stack, tooltip);
	}

	public void appendCurse(ItemStack stack, List<Component> tooltip) {

	}

	public  void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
	}
	
	public void appendHoverExtras(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack The current ItemStack
	 * @return 
	 * @return 0.0 for 100% (no damage / full bar), 1.0 for 0% (fully damaged / empty bar)
	 */
	@Override
	public int getBarWidth(ItemStack stack) {
		return stack.getCapability(DURABILITY).map(cap -> {
			return Math.round(13.0F - (float)stack.getDamageValue() * 13.0F / (float)cap.getDurability());
		}).orElse(Math.round(13.0F - (float)stack.getDamageValue() * 13.0F / (float)this.getMaxDamage(stack)));
	}

	/**
	 * 
	 */
	@Override
	public boolean isValidRepairItem(ItemStack itemToRepair, ItemStack resourceItem) {
		return resourceItem.getItem() == this || super.isValidRepairItem(itemToRepair, resourceItem);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		// exit if on the client
		if (WorldInfo.isClientSide(context.getLevel())) {			
			return InteractionResult.FAIL;
		}
		
		BlockPos chestPos = context.getClickedPos();
		BlockState state = context.getLevel().getBlockState(chestPos);
		Block block = state.getBlock();

		// test if the block is a chest proxy (ex. multiple block chests like Wither Chest)
		if (block instanceof ITreasureChestBlockProxy) {
			chestPos = ((ITreasureChestBlockProxy)block).getChestPos(chestPos);
			state = context.getLevel().getBlockState(chestPos);
			block = state.getBlock();
		}

		// determine if block at pos is a treasure chest
		if (block instanceof AbstractTreasureChestBlock) {
			// get the tile entity
			BlockEntity blockEntity = context.getLevel().getBlockEntity(chestPos);
			if (blockEntity == null || !(blockEntity instanceof ITreasureChestBlockEntity)) {
				Treasure.LOGGER.warn("null or incorrect blockEntity");
				return InteractionResult.FAIL;
			}
			ITreasureChestBlockEntity chestBlockEntity = (ITreasureChestBlockEntity)blockEntity;

			// determine if chest is locked
			if (!chestBlockEntity.hasLocks()) {
				return InteractionResult.SUCCESS;
			}

			try {
				ItemStack heldItemStack = context.getPlayer().getItemInHand(context.getHand());	
				boolean breakKey = true;
				boolean fitsLock = false;
				LockState lockState = null;
				boolean isKeyBroken = false;
				// check if this key is one that opens a lock (only first lock that key fits is unlocked).
				lockState = fitsFirstLock(chestBlockEntity.getLockStates());
				if (lockState != null) {
					fitsLock = true;
				}

				if (fitsLock) {
					if (unlock(lockState.getLock())) {
						// unlock the lock
						doUnlock(context, chestBlockEntity, lockState);

						if (!state.getValue(AbstractTreasureChestBlock.DISCOVERED)) {
							chestBlockEntity = ((AbstractTreasureChestBlock) block).discovered((AbstractTreasureChestBlockEntity) chestBlockEntity, state, context.getLevel(), chestPos, context.getPlayer());
						}
						
						// update the client
						chestBlockEntity.sendUpdates();

						// don't break the key
						breakKey = false;
					}
				}

				IDurabilityHandler cap = heldItemStack.getCapability(DURABILITY).orElseThrow(IllegalStateException::new);
				
				// check key's breakability
				if (breakKey) {
					if (!context.getPlayer().isCreative() && (isBreakable() || anyLockBreaksKey(chestBlockEntity.getLockStates(), this)) && Config.SERVER.keysAndLocks.enableKeyBreaks.get()) {
						// this damage block is considering if a key has been merged with another key.
						// it is only 'breaking' 1 key's worth of damage
						// ex k1(1/10d) + k2(0/10d) = k3(1/20d), only apply 9 damage
						// so k3 = (10/20d) ie 1 key's worth damage was applied.
						int damage = heldItemStack.getDamageValue() + (heldItemStack.getMaxDamage() - (heldItemStack.getDamageValue() % heldItemStack.getMaxDamage()));
						heldItemStack.setDamageValue(damage);
						if (heldItemStack.getDamageValue() >= cap.getDurability()) {
							// break key;
							heldItemStack.shrink(1);
						}

						// do effects
						doKeyBreakEffects(context.getLevel(), context.getPlayer(), chestPos);

						// flag the key as broken
						isKeyBroken = true;
					}
					else if (!fitsLock) {
						doKeyNotFitEffects(context.getLevel(), context.getPlayer(), chestPos);
					}
					else {
						doKeyUnableToUnlockEffects(context.getLevel(), context.getPlayer(), chestPos);
					}						
				}

				// user attempted to use key - increment the damage
				if (!context.getPlayer().isCreative() && isDamageable(heldItemStack) && !isKeyBroken) {
					heldItemStack.setDamageValue(heldItemStack.getDamageValue() + 1);
					if (heldItemStack.getDamageValue() >= cap.getDurability()) {
						heldItemStack.shrink(1);
					}
				}
			} catch (Exception e) {
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
	public void doUnlock(UseOnContext context, ITreasureChestBlockEntity chestTileEntity, LockState lockState) {
		LockItem lock = lockState.getLock();		
		lock.doUnlock(context.getLevel(), context.getPlayer(), context.getClickedPos(), lockState);

		if (!breaksLock(lock)) {
			// spawn the lock
			lock.dropLock(context.getLevel(), context.getClickedPos());
		}
	}

	/**
	 * This method is a secondary check against a lock item.
	 * Add predicates to the fistsLock list to overrule LockItem.acceptsKey()
	 *  if this is a key with special abilities.
	 * @param lockItem
	 * @return
	 */
	public boolean fitsLock(LockItem lockItem) {
		if (getFitsLock() == null || getFitsLock().isEmpty()) {
			return false;
		}
		for (Predicate<LockItem> p : this.getFitsLock()) {
			boolean result = p.test(lockItem);
			if (!result) {
				return false;
			}
		}
		return true;
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
			Treasure.LOGGER.debug("lock -> {} accepts key -> {}", lockItem.getRegistryName(), this.getRegistryName());
			if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
				Treasure.LOGGER.debug("unlock attempt met probability");
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
		if (getBreaksLock() == null || getBreaksLock().isEmpty()) {
			return false;
		}
		for (Predicate<LockItem> p : this.getBreaksLock()) {
			boolean result = p.test(lockItem);
			if (!result) {
				return false;
			}
		}
		return true;
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
	public IRarity getRarity() {
		IRarity rarity = KeyLockRegistry.getRarityByKey(this);
		if (rarity == null) {
			return Rarity.NONE;
		}
		return rarity;
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
		return "KeyItem [rarity=" + getRarity() + ", craftable=" + craftable + "]";
	}

	/**
	 * @return the category
	 */
	public KeyLockCategory getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public KeyItem setCategory(KeyLockCategory category) {
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

	public KeyItem addFitsLock(Predicate<LockItem> p) {
		if (fitsLock == null) {
			fitsLock = new ArrayList<>();
		}
		fitsLock.add(p);
		return this;
	}

	public List<Predicate<LockItem>> getFitsLock() {
		return this.fitsLock;
	}

	public KeyItem addBreaksLock(Predicate<LockItem> p) {
		if (breaksLock == null) {
			breaksLock = new ArrayList<>();
		}
		breaksLock.add(p);
		return this;
	}
	
	public List<Predicate<LockItem>> getBreaksLock() {
		return breaksLock;
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
	 * Convenience method
	 * @return the damageable
	 */
	public boolean isDamageable(ItemStack stack) {
		IDurabilityHandler handler = stack.getCapability(TreasureCapabilities.DURABILITY).map(h -> h).orElse(null);
		if (handler != null) {
			return !handler.isInfinite();
		}
		return true;
	}

	/**
	 * @param damageable the damageable to set
	 */
//	public void setDamageable(boolean damageable) {
//		this.damageable = damageable;
//	}
}
