/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.capability.PouchCapability;
import mod.gottsch.forge.treasure2.core.capability.TreasureCapabilities;
import mod.gottsch.forge.treasure2.core.inventory.PouchContainerMenu;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

/**
 * @author Mark Gottschling on May 13, 2020
 *
 */
public class PouchItem extends Item implements MenuProvider {
	/**
	 * 
	 * @param properties
	 */
	public PouchItem(Properties properties) {
		super(properties.stacksTo(1));
	}


	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		PouchCapability provider =  new PouchCapability();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("pouch")).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
			return InteractionResultHolder.fail(player.getItemInHand(hand));		}

		// open the chest
		NetworkHooks.openGui((ServerPlayer)player, this, player.blockPosition());
		
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
		
		// get the held item
		ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (heldItem == null || !(heldItem.getItem() instanceof PouchItem)) {
			heldItem = player.getItemInHand(InteractionHand.OFF_HAND);
			if (heldItem == null || !(heldItem.getItem() instanceof PouchItem))
				return null;
		}

		// create inventory from item
		IItemHandler inventory = null;
		Optional<IItemHandler> handler = heldItem.getCapability(TreasureCapabilities.POUCH).map(h -> h);
		if (handler.isPresent()) {
			inventory = handler.get();
		}
		else {
			return null;
		}
		// open the container
		return new PouchContainerMenu(windowId, playerInventory, inventory);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent(LangUtil.item("pouch"));
	}

	////////////////////////
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		ItemStackHandler cap = (ItemStackHandler) stack.getCapability(TreasureCapabilities.POUCH).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
		try {
			nbt = cap.serializeNBT();
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		super.readShareTag(stack, nbt);

		if (nbt instanceof CompoundTag) {
			ItemStackHandler cap = (ItemStackHandler) stack.getCapability(TreasureCapabilities.POUCH).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
			cap.deserializeNBT((CompoundTag) nbt);
		}
	}
}


