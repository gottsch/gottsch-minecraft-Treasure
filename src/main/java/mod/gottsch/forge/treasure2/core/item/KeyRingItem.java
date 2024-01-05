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
package mod.gottsch.forge.treasure2.core.item;

import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.DURABILITY;
import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.KEY_RING_CAPABILITY;
import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.KEY_RING_INVENTORY_CAPABILITY;
import static mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities.POUCH;

import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.capability.IDurabilityCapability;
import mod.gottsch.forge.treasure2.core.capability.IKeyRingCapability;
import mod.gottsch.forge.treasure2.core.capability.KeyRingCapabilityProvider;
import mod.gottsch.forge.treasure2.core.capability.KeyRingCapabilityStorage;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.inventory.KeyRingContainer;
import mod.gottsch.forge.treasure2.core.inventory.KeyRingInventory;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import mod.gottsch.forge.treasure2.core.tileentity.ITreasureChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingItem extends ModItem implements IKeyEffects, INamedContainerProvider {
	private static final KeyRingCapabilityStorage CAPABILITY_STORAGE = new KeyRingCapabilityStorage();
	
	/**
	 * 
	 * @param properties
	 */
	public KeyRingItem(Properties properties) {
		super(properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP)
				.stacksTo(1));
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	@Deprecated
	public KeyRingItem(String modID, String name, Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP)
				.stacksTo(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		KeyRingCapabilityProvider provider = new KeyRingCapabilityProvider();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("tooltip.label.key_ring").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		boolean isKeyBroken = false;

		// exit if on the client
		if (WorldInfo.isClientSide(context.getLevel())) {			
			return ActionResultType.FAIL;
		}

		/*
		 *  use the key ring to unlock locks
		 */
		
		// determine if block at pos is a treasure chest
		Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
		if (block instanceof AbstractChestBlock) {
			// get the tile entity
			TileEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
			if (tileEntity == null || !(tileEntity instanceof ITreasureChestTileEntity)) {
				Treasure.LOGGER.warn("Null or incorrect TileEntity");
				return ActionResultType.FAIL;
			}
			ITreasureChestTileEntity chestTileEntity = (ITreasureChestTileEntity)tileEntity;

			// determine if chest is locked
			if (!chestTileEntity.hasLocks()) {
				return ActionResultType.SUCCESS;
			}

			try {
				ItemStack heldItem = context.getPlayer().getItemInHand(context.getHand());
				IItemHandler inventoryCapability = heldItem.getCapability(KEY_RING_INVENTORY_CAPABILITY, null).orElseThrow(IllegalStateException::new);

				// cycle through all keys in key ring until one is able to fit lock and use it to unlock the lock.
				for (int i = 0; i < KeyRingInventory.INVENTORY_SIZE; i++) {
					ItemStack keyStack = inventoryCapability.getStackInSlot(i);
					if (keyStack != null && keyStack.getItem() != Items.AIR)  {
						KeyItem key = (KeyItem) keyStack.getItem();
						Treasure.LOGGER.debug("Using key from keyring: {}", key.getRegistryName());
						boolean breakKey = true;
						boolean fitsLock = false;
						LockState lockState = null;

						// check if this key is one that opens a lock (only first lock that key fits is unlocked).
						lockState = key.fitsFirstLock(chestTileEntity.getLockStates());
						if (lockState != null) {
							fitsLock = true;
						}
						Treasure.LOGGER.debug("key fits lock: {}", lockState);

						// TODO move to a method in KeyItem
						if (fitsLock) {
							if (key.unlock(lockState.getLock())) {
//								LockItem lock = lockState.getLock();								
//								doKeyUnlockEffects(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity, lockState);
								
								// unlock the lock
								doUnlock(context, (AbstractTreasureChestTileEntity)chestTileEntity, key, lockState);
								
								// update the client
								chestTileEntity.sendUpdates();

								// don't break the key
								breakKey = false;
							}

							IDurabilityCapability cap = keyStack.getCapability(DURABILITY).orElseThrow(IllegalStateException::new);
							// TODO move to a method in KeyItem
							if (breakKey) {
								if ((key.isBreakable() ||  key.anyLockBreaksKey(chestTileEntity.getLockStates(), key))  && TreasureConfig.KEYS_LOCKS.enableKeyBreaks.get()) {
									int damage = keyStack.getDamageValue() + (getMaxDamage() - (keyStack.getDamageValue() % getMaxDamage()));
									keyStack.setDamageValue(damage);
									if (keyStack.getDamageValue() >= cap.getDurability()) {
										// break key;
										keyStack.shrink(1);
									}									
									// the key is broken, do not attempt to damage it.
									isKeyBroken = true;
									
			                        key.doKeyBreakEffects(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity);
								}
								else if (!fitsLock) {
									doKeyNotFitEffects(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity);
								}
								else {
									doKeyUnableToUnlockEffects(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity);
								}						
							}
							if (key.isDamageable() && !isKeyBroken) {
								keyStack.setDamageValue(keyStack.getDamageValue() + 1);
								if (keyStack.getDamageValue() >= cap.getDurability()) {
									keyStack.shrink(1);
								}
							}
							else {
								Treasure.LOGGER.debug("Key in keyring is NOT damageable.");
							}			

							// key unlocked a lock, end loop (ie only unlock 1 lock at a time
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
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
			return new ActionResult<ItemStack>(ActionResultType.FAIL, playerIn.getItemInHand(handIn));
		}

		// get the container provider
		INamedContainerProvider namedContainerProvider = this;

		// open the chest
		NetworkHooks.openGui((ServerPlayerEntity)playerIn, namedContainerProvider, (packetBuffer)->{});
		// NOTE: (packetBuffer)->{} is just a do-nothing because we have no extra data to send

		return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
	}

	/**
	 * 
	 * @param context
	 * @param chestTileEntity
	 * @param lockState
	 */
	public void doUnlock(ItemUseContext context, AbstractTreasureChestTileEntity chestTileEntity,	KeyItem key, LockState lockState) {
		LockItem lock = lockState.getLock();		
		 lock.doUnlock(context.getLevel(), context.getPlayer(), context.getClickedPos(), chestTileEntity, lockState);
				
		if (!key.breaksLock(lock)) {
			// spawn the lock
			lock.dropLock(context.getLevel(), context.getClickedPos());
		}		
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, PlayerEntity player) {		
		// NOTE only works on 'Q' press, not mouse drag and drop
		IKeyRingCapability cap = stack.getCapability(KEY_RING_CAPABILITY, null).orElseThrow(IllegalStateException::new);

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
	public Container createMenu(int windowID, PlayerInventory inventory, PlayerEntity player) {
		// get the held item
		ItemStack keyRingItem = player.getItemInHand(Hand.MAIN_HAND);
		if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem)) {
			keyRingItem = player.getItemInHand(Hand.OFF_HAND);
			if (keyRingItem == null || !(keyRingItem.getItem() instanceof KeyRingItem))
				return null;
		}

		// create inventory from item
		IInventory itemInventory = new KeyRingInventory(keyRingItem);
		// open the container
		return new KeyRingContainer(windowID, TreasureContainers.KEY_RING_CONTAINER_TYPE, inventory, itemInventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("item.treasure2.key_ring");
	}
	
	////////////////////////
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
	public CompoundNBT getShareTag(ItemStack stack) {
		
		CompoundNBT keyRingTag;
		keyRingTag = (CompoundNBT) CAPABILITY_STORAGE.writeNBT(
				TreasureCapabilities.KEY_RING_CAPABILITY,
				stack.getCapability(TreasureCapabilities.KEY_RING_CAPABILITY).orElse(null),
				null);
		
		CompoundNBT inventoryTag = null;
		ItemStackHandler cap = (ItemStackHandler) stack.getCapability(POUCH).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
		try {
			inventoryTag = cap.serializeNBT();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		
		CompoundNBT tag = new CompoundNBT();
		tag.put("keyRing", keyRingTag);
		tag.put("inventory", inventoryTag);
		return tag;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
		super.readShareTag(stack, nbt);

		if (nbt instanceof CompoundNBT) {
			
			if (nbt.contains("keyRing")) {
				CompoundNBT tag = nbt.getCompound("keyRing");
				CAPABILITY_STORAGE.readNBT(
						TreasureCapabilities.KEY_RING_CAPABILITY, 
						stack.getCapability(TreasureCapabilities.KEY_RING_CAPABILITY).orElse(null),
						null,
						tag);
			}
			
			if (nbt.contains("inventory")) {
				ItemStackHandler cap = (ItemStackHandler) stack.getCapability(POUCH).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
				cap.deserializeNBT((CompoundNBT) nbt.getCompound("inventory"));
			}
		}
	}
}
