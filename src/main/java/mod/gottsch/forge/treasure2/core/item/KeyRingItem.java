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

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlockProxy;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityHandler;
import mod.gottsch.forge.treasure2.core.capability.IKeyRingHandler;
import mod.gottsch.forge.treasure2.core.capability.KeyRingCapability;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.inventory.KeyRingContainerMenu;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingItem extends Item implements MenuProvider {

	/**
	 * 
	 * @param properties
	 */
	public KeyRingItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		KeyRingCapability provider = new KeyRingCapability();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("key_lock.key_ring")).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {

		// exit if on the client
		if (WorldInfo.isClientSide(context.getLevel())) {			
			return InteractionResult.FAIL;
		}

		BlockPos chestPos = context.getClickedPos();		
		Block block = context.getLevel().getBlockState(chestPos).getBlock();

		// test if the block is a chest proxy (ex. multiple block chests like Wither Chest)
		if (block instanceof ITreasureChestBlockProxy) {
			chestPos = ((ITreasureChestBlockProxy)block).getChestPos(chestPos);
			block = context.getLevel().getBlockState(chestPos).getBlock();
		}

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
				ItemStack heldItem = context.getPlayer().getItemInHand(context.getHand());
				IItemHandler handler = 	heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(IllegalStateException::new);

				// cycle through all keys in key ring until one is able to fit lock and use it to unlock the lock.
				for (int i = 0; i < KeyRingCapability.INVENTORY_SIZE; i++) {
					ItemStack keyStack = handler.getStackInSlot(i);
					if (keyStack != null && keyStack != ItemStack.EMPTY)  {
						KeyItem key = (KeyItem) keyStack.getItem();
						Treasure.LOGGER.debug("using key from keyring -> {}", key.getRegistryName());
						boolean breakKey = true;
						boolean fitsLock = false;
						LockState lockState = null;
						boolean isKeyBroken = false;

						// check if this key is one that opens a lock (only first lock that key fits is unlocked).
						lockState = key.fitsFirstLock(chestBlockEntity.getLockStates());
						if (lockState != null) {
							fitsLock = true;
						}
						Treasure.LOGGER.debug("key fits lock -> {}", lockState);

						if (fitsLock) {
							if (key.unlock(lockState.getLock())) {
								// unlock the lock
								doUnlock(context, (AbstractTreasureChestBlockEntity)chestBlockEntity, key, lockState);

								// update the client
								chestBlockEntity.sendUpdates();

								// don't break the key
								breakKey = false;
							}

							IDurabilityHandler cap = keyStack.getCapability(DURABILITY).orElseThrow(IllegalStateException::new);
							// TODO make into method in KeyItem
							if (breakKey) {
								if ((key.isBreakable() ||  key.anyLockBreaksKey(chestBlockEntity.getLockStates(), key))  && Config.SERVER.keysAndLocks.enableKeyBreaks.get()) {
									// this damage block is considering if a key has been merged with another key.
									// it is only 'breaking' 1 key's worth of damage
									// ex k1(1/10d) + k2(0/10d) = k3(1/20d), only apply 9 damage
									// so k3 = (10/20d) ie 1 key's worth damage was applied.
									int damage = keyStack.getDamageValue() + (keyStack.getMaxDamage() - (keyStack.getDamageValue() % keyStack.getMaxDamage()));
									keyStack.setDamageValue(damage);
									if (keyStack.getDamageValue() >= cap.getDurability()) {
										// break key;
										keyStack.shrink(1);
									}								

									key.doKeyBreakEffects(context.getLevel(), context.getPlayer(), chestPos);

									// the key is broken, do not attempt to damage it.
									isKeyBroken = true;
								}
								else if (!fitsLock) {
									key.doKeyNotFitEffects(context.getLevel(), context.getPlayer(), chestPos);
								}
								else {
									key.doKeyUnableToUnlockEffects(context.getLevel(), context.getPlayer(), chestPos);
								}						
							}
							if (key.isDamageable(keyStack) && !isKeyBroken) {
								keyStack.setDamageValue(keyStack.getDamageValue() + 1);
								if (keyStack.getDamageValue() >= cap.getDurability()) {
									keyStack.shrink(1);
								}
							}
							// key unlocked a lock, end loop (ie only unlock 1 lock at a time)
							break;
						}
					}
				}
			} catch (Exception e) {
				Treasure.LOGGER.error("error: ", e);
			}
		}
		else {
			// open gui
			return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
		}

		// this should prevent the onItemRightClick from happening./
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		// exit if on the client
		if (WorldInfo.isClientSide(level)) {			
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		}

		// open the chest
		NetworkHooks.openGui((ServerPlayer)player, this, player.blockPosition());
		// NOTE: (packetBuffer)->{} is just a do-nothing because we have no extra data to send

		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	/**
	 * 
	 * @param context
	 * @param chestBlockEntity
	 * @param lockState
	 */
	public void doUnlock(UseOnContext context, AbstractTreasureChestBlockEntity chestBlockEntity,	KeyItem key, LockState lockState) {
		key.doUnlock(context, chestBlockEntity, lockState);	
	}

	/**
	 * 
	 */
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player) {		
		// NOTE only works on 'Q' press, not mouse drag and drop
		IKeyRingHandler cap = stack.getCapability(TreasureCapabilities.KEY_RING).orElseThrow(IllegalStateException::new);

		if (cap.isOpen()) {
			return false;
		}
		else {
			return super.onDroppedByPlayer(stack, player);
		}
	}

	/**
	 * 
	 */
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		// get the held item
		ItemStack keyRingItem = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
			keyRingItem = player.getItemInHand(InteractionHand.OFF_HAND);
			if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem))
				return null;
		}

		// create inventory from item
		IItemHandler inventory = null;
		Optional<IItemHandler> handler = keyRingItem.getCapability(TreasureCapabilities.KEY_RING_INV).map(h -> h);
		if (handler.isPresent()) {
			inventory = handler.get();
		}
		else {
			return null;
		}
		// open the container
		return new KeyRingContainerMenu(windowId, playerInventory, inventory);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("item.treasure2.key_ring");
	}

	////////////////////////
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
	public CompoundTag getShareTag(ItemStack stack) {

		CompoundTag dataTag = null;
		Optional<IKeyRingHandler> dataHandler = stack.getCapability(TreasureCapabilities.KEY_RING).map(h -> h);
		if (dataHandler.isPresent()) {
			dataTag = dataHandler.get().save();
		}

		CompoundTag itemTag = new CompoundTag();		
		Optional<IItemHandler> itemHandler = stack.getCapability(TreasureCapabilities.KEY_RING_INV).map(h -> h);
		if (itemHandler.isPresent()) {
			itemTag =((ItemStackHandler) itemHandler.get()).serializeNBT();
		}

		CompoundTag tag = new CompoundTag();		
		tag.put("keyRing", dataTag);
		tag.put("inventory", itemTag);
		return tag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag compound) {
		super.readShareTag(stack, compound);

		if (compound instanceof CompoundTag) {
			if (compound.contains("inventory")) {
				stack.getCapability(TreasureCapabilities.KEY_RING_INV).ifPresent(cap -> {
					((ItemStackHandler)cap).deserializeNBT(compound.getCompound("inventory"));
				});
			}

			if (compound.contains("keyRing")) {
				stack.getCapability(TreasureCapabilities.KEY_RING).ifPresent(cap -> {
					cap.load(compound.getCompound("keyRing"));
				});
			}
		}
	}
}
