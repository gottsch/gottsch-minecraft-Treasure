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
package com.someguyssoftware.treasure2.eventhandler;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.PouchCapabilityProvider;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.IPouch;
import com.someguyssoftware.treasure2.item.PouchType;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;
import com.someguyssoftware.treasure2.item.wish.IWishable;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
public class PlayerEventHandler {
	private static final String FIRST_JOIN_NBT_KEY = "treasure2.firstjoin";
	private static final String PATCHOULI_MODID = "patchouli";
	private static final String PATCHOULI_GUIDE_BOOK_ID = "patchouli:guide_book";
	private static final String PATCHOULI_GUIDE_TAG_ID = "patchouli:book";
	private static final String TREASURE2_GUIDE_TAG_VALUE = "treasure2:guide";

	// reference to the mod.
	private IMod mod;

	/**
	 * 
	 */
	public PlayerEventHandler(IMod mod) {
		setMod(mod);
	}

//	@SubscribeEvent
//	public void addToInventory(PlayerEvent.PlayerLoggedInEvent event) {
//		// check if config is enabled
//		if (!TreasureConfig.MOD.enableSpecialRewards) {
//			return;
//		}
//
//		if (event.player.isCreative()) {
//			return;
//		}
//
//		// check if during the correct timeframe
////		if (LocalDate.now().getMonth() != Month.DECEMBER || Year.now().getValue() != 2021) {
////			return;			
////		}
//
//		NBTTagCompound playerData = event.player.getEntityData();
//		NBTTagCompound persistentNbt;
//		if (!playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
//			playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistentNbt = new NBTTagCompound()));
//		} else {
//			persistentNbt = playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
//		}
//
//		if (!persistentNbt.hasKey(FIRST_JOIN_NBT_KEY)) {
//			persistentNbt.setBoolean(FIRST_JOIN_NBT_KEY, true);
//			// add all items to players inventory on first join
//			ItemStack ring = new ItemStack(TreasureItems.GOTTSCHS_RING_OF_MOON, 1);
//			event.player.inventory.addItemStackToInventory(ring);
//		}
//	}

	//	TEMP remove until patchouli book is complete.
	//    @SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		// check if config is enabled
		if (!TreasureConfig.MOD.enableStartingBook) {
			return;
		}

		if (event.player.isCreative()) {
			return;
		}

		NBTTagCompound data = event.player.getEntityData();
		NBTTagCompound persistent;
		if (!data.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			data.setTag(EntityPlayer.PERSISTED_NBT_TAG, (persistent = new NBTTagCompound()));
		} else {
			persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}

		// check if the patchouli mod is installed
		if (Loader.isModLoaded(PATCHOULI_MODID)) {
			if (!persistent.hasKey(FIRST_JOIN_NBT_KEY)) {
				persistent.setBoolean(FIRST_JOIN_NBT_KEY, true);
				// create the book
				Item guideBook = Item.REGISTRY.getObject(new ResourceLocation(PATCHOULI_GUIDE_BOOK_ID));
				ItemStack stack = new ItemStack(guideBook);
				if (!stack.hasTagCompound()) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString(PATCHOULI_GUIDE_TAG_ID, TREASURE2_GUIDE_TAG_VALUE);
					stack.setTagCompound(tag);
				}
				event.player.inventory.addItemStackToInventory(stack);
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteraction(LivingUpdateEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {

			// get the player
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			processCharms(event, player);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithDamage(LivingDamageEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof EntityPlayer) {
			// get the player
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			processCharms(event, player);
		}		
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithAttack(LivingHurtEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getEntityWorld())) {
			return;
		}

		// if player is source or destination of hurt
		EntityPlayerMP player = null;
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			player = (EntityPlayerMP) event.getSource().getTrueSource();
		}
		else if (event.getEntityLiving() instanceof  EntityPlayer) {
			player = (EntityPlayerMP) event.getEntityLiving();
		}
		
		if (player != null) {
			// get the player
			processCharms(event, player);
		}
	}

	@SubscribeEvent
	public void checkCharmsInteractionWithBlock(BlockEvent.HarvestDropsEvent event) {
		if (WorldInfo.isClientSide(event.getWorld())) {
			return;
		}

		if (event.getHarvester() == null) {
			return;
		}

		// if the harvested blcok has a tile entity then don't process
		// NOTE this may exclude non-inventory blocks
		IBlockState harvestedState = event.getState();
		Block harvestedBlock = harvestedState.getBlock();
		if (harvestedBlock.hasTileEntity(harvestedState)) {
			return;
		}

		// get the player
		EntityPlayerMP player = (EntityPlayerMP) event.getHarvester();
		processCharms(event, player);
	}

	/**
	 * 
	 * @param event
	 * @param player
	 */
	private void processCharms(Event event, EntityPlayerMP player) {
		final List<String> nonMultipleUpdateCharms = new ArrayList<>(5);

		// check each hand
		Optional<CharmContext> context = null;
		for (EnumHand hand : EnumHand.values()) {
			context = getCharmContext(player, hand);
			if (context.isPresent()) {
				if (context.get().type == CharmedType.CHARM || context.get().type == CharmedType.ADORNMENT) {
					doCharms(context.get(), player, event, nonMultipleUpdateCharms);
				}
				else {
					doPouch(context.get(), player, event, nonMultipleUpdateCharms);
				}
			}
		}

		// check hotbar - get the context at each slot
		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			if (player.inventory.getStackInSlot(hotbarSlot) != player.getHeldItemMainhand()) {
				context = getCharmContext(player, hotbarSlot);
				if (context.isPresent()) {
					if (context.get().type == CharmedType.ADORNMENT) {
						// Treasure.logger.debug("is a hotbar adornment -> {} @ slot -> {}", context.get().itemStack.getItem().getRegistryName(), context.get().slot);
						// at this point, we know the item in slot x has charm capabilities
						doCharms(context.get(), player, event, nonMultipleUpdateCharms);
					}
				}
			}
		}

		// TODO future integration check Baubles equiped

	}

	/**
	 * 
	 * @param context
	 * @param player
	 * @param event
	 */
	public void doPouch(CharmContext context, EntityPlayerMP player, Event event, final List<String> nonMultipleUpdateCharms) {
		// get the capability of the pouch
		IItemHandler cap = context.itemStack.getCapability(PouchCapabilityProvider.INVENTORY_CAPABILITY, null);
		// TODO this slots bit could be better. Maybe pouch item should have number of slots property
		// scan the first 3 slots of pouch (this only works for pouches... what if in future there are other focuses ?
		int slots = context.itemStack.getItem() == TreasureItems.LUCKY_POUCH ? 1 : 
			context.itemStack.getItem() == TreasureItems.APPRENTICES_POUCH ? 2 : 3;
		for (int focusIndex = 0; focusIndex < slots; focusIndex++) {
			ItemStack itemStack = cap.getStackInSlot(focusIndex);
			// update the context to the specific charm
			context.itemStack = itemStack;
			context.slot = focusIndex;
			// TODO a way around instanceof check if to add a property(s) to the charmCapability - boolean charmed and/or boolean charmable.
			// see 1.16.5 version
			if (itemStack.getItem() instanceof ICharmed) {
				context.capability = itemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
				doCharms(context, player, event, nonMultipleUpdateCharms);
			}
			else if (itemStack.getItem() instanceof ICharmable) {
				context.capability = itemStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
				doCharms(context, player, event, nonMultipleUpdateCharms);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param player
	 * @param event
	 */
	private void doCharms(CharmContext context, EntityPlayerMP player, Event event, final List<String> nonMultipleUpdateCharms) {
		List<ICharmEntity> removeInstances = new ArrayList<>(3);
		ICharmInventoryCapability capability = context.capability;
		List<ICharmEntity> charmEntities = capability.getCharmEntities();
		for (ICharmEntity charmEntity : charmEntities) {
			boolean isCharmUpdatable = true;
			ICharm charm = (ICharm)charmEntity.getCharm();
			// test the charm against the event
			if (!charm.getRegisteredEvent().equals(event.getClass())) {
				//				Treasure.logger.debug("charm type -> {} is not register for this event -> {}",charm.getType(), event.getClass().getSimpleName());
				continue;
			}

			// Treasure.logger.debug("{} charm allows multiple updates -> {}", charm.getName(), charm.isAllowMultipleUpdates());
			if (!charm.isEffectStackable()) {
				// Treasure.logger.debug("{} charm denies multiple updates", charm.getName());
				// check if in list
				if (nonMultipleUpdateCharms.contains(charm.getType())) {
					// Treasure.logger.debug("blacklist contains charm type -> {}", charm.getType());
					isCharmUpdatable = false;
				}
			}
			else {
				// Treasure.logger.debug("blacklist doesn't contain charm type -> {}", charm.getType());
				nonMultipleUpdateCharms.add(charm.getType());
			}

			// Treasure.logger.debug("is charm {} updatable -> {}", charm.getName(), isCharmUpdatable);
			if (isCharmUpdatable && 
					charmEntity.getCharm().update(player.world, new Random(), new Coords((int)player.posX, (int)player.posY, (int)player.posZ), player, event, charmEntity)) {
				// send state message to client
				CharmMessageToClient message = new CharmMessageToClient(player.getName(), charmEntity, context.hand, context.slot);
				//				Treasure.logger.debug("Message to client -> {}", message);
				Treasure.simpleNetworkWrapper.sendTo(message, player);
			}

			// mark Charm if instanceof ICharmable and no uses remain
			if (charmEntity.getValue() <= 0.0 && charmEntity.getCharm() instanceof ICharmable) {
				Treasure.logger.debug("charm is empty, add to remove list");
				removeInstances.add(charmEntity);
			}
		}

		// remove any charms that have no uses remaining
		if (!removeInstances.isEmpty()) {
			removeInstances.forEach(instance -> {
				charmEntities.remove(instance);
				// TODO send message to client to remove charm";
			});
		}
	}

	// TODO ugh, these are terrible. port code from 1.16.5 version
	/**
	 * 
	 * @param player
	 * @param hand
	 * @return
	 */
	private Optional<CharmContext> getCharmContext(EntityPlayerMP player, EnumHand hand) {
		CharmContext context = new CharmContext();

		ItemStack heldStack = player.getHeldItem(hand);		
		context.hand = hand;
		context.itemStack = heldStack;

		if (heldStack.isEmpty()) {
			return Optional.empty();
		}

		Optional<CharmedType> type = getType(heldStack);
		if (!type.isPresent()) {
			return Optional.empty();
		}
		context.type = type.get();
		switch(context.type) {
		// TODO either case now uses the same capability
		case ADORNMENT:
			context.capability = heldStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			break;
		case CHARM:
			context.capability = heldStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			break;		
		}	
		return Optional.of(context);
	}

	@SuppressWarnings("incomplete-switch")
	private Optional<CharmContext> getCharmContext(EntityPlayerMP player, int hotbarSlot) {
		CharmContext context = new CharmContext();

		ItemStack stack = player.inventory.getStackInSlot(hotbarSlot);
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		Optional<CharmedType> type = getType(stack);
		if (!type.isPresent()) {
			return Optional.empty();
		}

		context.hotbarSlot = hotbarSlot;
		context.slot = hotbarSlot;
		context.itemStack = stack;
		context.type = type.get();
		switch(context.type) {
		case ADORNMENT:
			context.capability = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			break;
		case CHARM:
			context.capability = stack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
			break;		
		}		
		return Optional.of(context);
	}

	/**
	 * 
	 * @param stack
	 * @return
	 */
	private Optional<CharmedType> getType(ItemStack stack) {
		CharmedType type = null;
		if (stack.getItem() instanceof ICharmed) {
			type = CharmedType.CHARM;
		}
		else if (stack.getItem() instanceof ICharmable) {
			type = CharmedType.ADORNMENT;
		}
		else if(stack.getItem() instanceof IPouch && ((IPouch)stack.getItem()).getPouchType() == PouchType.ARCANE) {
			type = CharmedType.FOCUS;
		}

		return Optional.ofNullable(type);
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().world)) {
			return;
		}

		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			NBTTagCompound nbt = new NBTTagCompound();
			//			nbt.setString(IWishable.DROPPED_BY_KEY, event.getPlayer().getName());
			Treasure.logger.debug("player {}'s uuid -> {}", event.getPlayer().getName(), EntityPlayer.getUUID(event.getPlayer().getGameProfile()).toString());
			nbt.setString(IWishable.DROPPED_BY_KEY, EntityPlayer.getUUID(event.getPlayer().getGameProfile()).toString());
			stack.setTagCompound(nbt);			
		}		
	}

	/**
	 * @return the mod
	 */
	public IMod getMod() {
		return mod;
	}

	/**
	 * @param mod the mod to set
	 */
	public void setMod(IMod mod) {
		this.mod = mod;
	}

	/**
	 * 
	 * @author Mark Gottschling on Apr 30, 2020
	 *
	 */
	private class CharmContext {
		EnumHand hand;
		Integer slot;
		Integer hotbarSlot;
		ItemStack itemStack;
		CharmedType type;
		ICharmInventoryCapability capability;

		CharmContext() {}
	}

	private enum CharmedType {
		CHARM,
		FOCUS,
		ADORNMENT
	}
}
