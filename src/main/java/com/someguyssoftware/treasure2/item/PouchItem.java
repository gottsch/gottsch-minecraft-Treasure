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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.*;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.inventory.PouchContainer;
import com.someguyssoftware.treasure2.inventory.PouchInventory;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ActionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Mark Gottschling on May 13, 2020
 *
 */
public class PouchItem extends ModItem implements INamedContainerProvider {

	public PouchItem(String modID, String name, Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP).stacksTo(1));
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
		PouchCapabilityProvider provider =  new PouchCapabilityProvider();
		return provider;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslatableComponent("tooltip.label.pouch").withStyle(ChatFormatting.GOLD));
	}

	@Override
	public ActionResult<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		// exit if on the client
		if (WorldInfo.isClientSide(worldIn)) {			
			return new ActionResult<ItemStack>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
		}

		// get the container provider
		INamedContainerProvider namedContainerProvider = this;

		// open the chest
		NetworkHooks.openGui((ServerPlayer)playerIn, namedContainerProvider, (packetBuffer)->{});
		// NOTE: (packetBuffer)->{} is just a do-nothing because we have no extra data to send

		return new ActionResult<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory inventory, Player player) {
		// get the held item
		ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
		if (heldItem == null || !(heldItem.getItem() instanceof PouchItem)) {
			heldItem = player.getItemInHand(Hand.OFF_HAND);
			if (heldItem == null || !(heldItem.getItem() instanceof PouchItem))
				return null;
		}

		// create inventory from item
		IInventory itemInventory = new PouchInventory(heldItem);
		// open the container
		return new PouchContainer(windowID, TreasureContainers.POUCH_CONTAINER_TYPE, inventory, itemInventory);
	}

	@Override
	public TextComponent getDisplayName() {
		return new TranslatableComponent("item.treasure2.pouch");
	}

	////////////////////////
	/**
	 * NOTE getShareTag() and readShareTag() are required to sync item capabilities server -> client. I needed this when holding charms in hands and then swapping hands.
	 */
	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		IItemHandler cap = stack.getCapability(POUCH_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));

		try {

		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		super.readShareTag(stack, nbt);

		if (nbt instanceof CompoundTag) {
			IItemHandler cap = stack.getCapability(POUCH_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));

			CompoundTag tag = (CompoundTag) nbt;

		}
	}
}


