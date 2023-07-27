/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlockProxy;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.effects.ILockEffects;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public class LockItem extends Item implements ILockEffects {

	/*
	 * The category that the lock belongs to
	 */
	private KeyLockCategory category;

	/*
	 * Flag if the lock is craftable
	 */
	private boolean craftable;

	/*
	 * a list of keys that unlock the lock
	 */
	private List<KeyItem> keys = new ArrayList<>(3);

	/*
	 * A list of predicates that determine if a key will break a lock.
	 */
	private List<Predicate<KeyItem >> breaksKey;

	/**
	 * 
	 * @param properties
	 * @param keys
	 */
	public LockItem(Item.Properties properties, KeyItem[] keys) {
		this(properties);
		getKeys().addAll(Arrays.asList(keys));
	}

	/**
	 * 
	 * @param properties
	 */
	public LockItem(Item.Properties properties) {
		super(properties);
		setCategory(KeyLockCategory.ELEMENTAL);
		setCraftable(false);
	}

	/**
	 * 
	 * @param item
	 * @param keys
	 */
	//	@Deprecated
	//	public LockItem(String modID, String name, Item.Properties properties, KeyItem[] keys) {
	//		this(modID, name, properties);
	//		getKeys().addAll(Arrays.asList(keys));
	//	}

	/**
	 * 
	 * @param item
	 */
	//	@Deprecated
	//	public LockItem(String modID, String name, Item.Properties properties) {
	//		super(modID, name, properties.tab(TreasureItems.TREASURE_ITEM_GROUP));
	//		setCategory(KeyLockCategory.ELEMENTAL);
	//		setRarity(Rarity.COMMON);
	//		setCraftable(false);
	//	}

	/**
	 * Format: Item Name (vanilla minecraft) Rarity: [...] [color = Dark Blue]
	 * Category: [...] [color = Gold] Craftable: [Yes | No] [color = Green | Dark Red] 
	 * Accepts Keys: [list] [color = Gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, worldIn, tooltip, flag);

		tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.rarity"),
				ChatFormatting.DARK_BLUE + new TranslatableComponent(getRarity().getValue().toLowerCase()).getString().toUpperCase() ));
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.category"), ChatFormatting.GOLD + new TranslatableComponent(getCategory().toString().toLowerCase()).getString().toUpperCase()));

		LangUtil.appendAdvancedHoverText(tooltip, tt -> {
			MutableComponent craftable = null;
			if (isCraftable()) {
				craftable = new TranslatableComponent(LangUtil.tooltip("boolean.yes")).withStyle(ChatFormatting.GREEN);
			} else {
				craftable = new TranslatableComponent(LangUtil.tooltip("boolean.no")).withStyle(ChatFormatting.DARK_RED);
			}
			tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.craftable"), craftable));
			tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.accepts_keys")));
			getKeys().forEach(key -> {
				tooltip.add(new TextComponent("- ")
						.append(new TranslatableComponent(key.getDescription().getString()).withStyle(ChatFormatting.DARK_GREEN)));
			});

			appendHoverSpecials(stack, worldIn, tooltip, flag);
			appendHoverExtras(stack, worldIn, tooltip, flag);
		});
	}

	public  void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
	}

	public void appendHoverExtras(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
	}

	/**
	 * 
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {

		BlockPos chestPos = context.getClickedPos();		
		Block block = context.getLevel().getBlockState(chestPos).getBlock();
		// if the block is a proxy of a chest (ie wither chest top block or other special block)
		if (block instanceof ITreasureChestBlockProxy) {
			Treasure.LOGGER.info("LockItem | onItemUse | block is an ITreasureChestProxy");
			chestPos = ((ITreasureChestBlockProxy) block).getChestPos(chestPos);
			block = context.getLevel().getBlockState(chestPos).getBlock();
		}

		// determine if block at pos is a treasure chest
		if (block instanceof AbstractTreasureChestBlock) {
			// get the tile entity
			AbstractTreasureChestBlockEntity blockEntity = (AbstractTreasureChestBlockEntity) context.getLevel().getBlockEntity(chestPos);
			Treasure.LOGGER.info("LockItem | onItemUse | blockEntity -> {}", blockEntity);
			// exit if on the client
			if (WorldInfo.isClientSide(context.getLevel())) {
				return InteractionResult.FAIL;
			}

			try {
				ItemStack heldItem = context.getPlayer().getItemInHand(context.getHand());
				/*
				 * handle the lock
				 * NOTE don't use the return boolean as the locked flag here, as the chest is
				 * already locked and if the method was unsuccessful it could state the chest is unlocked.
				 */				 
				handleHeldLock(blockEntity, context.getPlayer(), heldItem);
			} catch (Exception e) {
				Treasure.LOGGER.error("error: ", e);
			}
		}
		return super.useOn(context);
	}

	/**
	 * 
	 * @param blockEntity
	 * @param player
	 * @param heldItem
	 * @return flag indicating if a lock was added
	 */
	public boolean handleHeldLock(AbstractTreasureChestBlockEntity blockEntity, Player player, ItemStack heldItem) {
		boolean lockedAdded = false;
		LockItem lock = (LockItem) heldItem.getItem();
		Treasure.LOGGER.info("LockItem | handleHeldLock | lock -> {}", lock);
		Treasure.LOGGER.info("LockItem | handleHeldLock | lockState -> {}", blockEntity.getLockStates());
		// add the lock to the first lockstate that has an available slot
		for (LockState lockState : blockEntity.getLockStates()) {
			Treasure.LOGGER.info("handleHeldLock | lockState -> {}", lockState);
			if (lockState != null && lockState.getLock() == null) {
				lockState.setLock(lock);

				doLockedEffects(blockEntity.getLevel(), player, blockEntity.getBlockPos(), lockState);						 

				blockEntity.sendUpdates();
				// decrement item in hand
				heldItem.shrink(1);
				lockedAdded = true;
				break;
			}
		}
		return lockedAdded;
	}

	/**
	 * 
	 * @param level
	 * @param player
	 * @param chestPos
	 * @param chestTileEntity
	 * @param lockState
	 */
	public void doUnlock(Level level, Player player, BlockPos chestPos, LockState lockState) {

		doUnlockedEffects(level, player, chestPos, lockState);

		// remove the lock
		lockState.setLock(null);		
	}

	/**
	 * 
	 * @param level
	 * @param pos
	 */
	public void dropLock(Level level, BlockPos pos) {
		if (Config.SERVER.keysAndLocks.enableLockDrops.get()) {
			Containers.dropItemStack(
					level, 
					pos.getX(), 
					pos.getY(), 
					pos.getZ(), 
					new ItemStack(this));
		}
	}

	/**
	 * 
	 * @param keyItem
	 * @return
	 */
	public boolean acceptsKey(KeyItem keyItem) {
		return getKeys().contains(keyItem);
	}

	/**
	 * 
	 * @param keyItem
	 * @return
	 */
	public boolean breaksKey(KeyItem keyItem) {
		if (getBreaksKey() == null || getBreaksKey().isEmpty()) {
			return false;
		}
		for (Predicate<KeyItem> p : this.getBreaksKey()) {
			boolean result = p.test(keyItem);
			if (!result) {
				return false;
			}
		}
		return true;
	}

	public LockItem addBreaksKey(Predicate<KeyItem> p) {
		if (breaksKey == null) {
			breaksKey = new ArrayList<>();
		}
		breaksKey.add(p);
		return this;
	}

	public List<Predicate<KeyItem>> getBreaksKey() {
		return breaksKey;
	}

	/**
	 * @return the rarity
	 */
	public IRarity getRarity() {
		IRarity rarity = KeyLockRegistry.getRarityByLock(this);
		if (rarity == null) {
			return Rarity.NONE;
		}
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	//	public LockItem setRarity(IRarity rarity) {
	//		this.rarity = rarity;
	//		return this;
	//	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockItem [name=" + getRegistryName() + ", rarity=" + getRarity() + ", craftable=" + craftable + ", keys="
				+ keys + "]";
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
	public LockItem setCategory(KeyLockCategory category) {
		this.category = category;
		return this;
	}
}
