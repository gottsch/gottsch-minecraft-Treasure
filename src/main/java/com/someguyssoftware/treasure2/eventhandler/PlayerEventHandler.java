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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.item.IWishable;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;
import com.someguyssoftware.treasure2.network.TreasureNetworking;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {
	private static final String CURIOS_ID = "curios";
	private static final List<String> CURIOS_SLOTS = Arrays.asList("necklace", "bracelet", "ring", "charm");

	/*
	 * Subscribing to multiple types of Living events for Charm Interactions so that instanceof doesn't have to be called everytime.
	 */

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void checkCharmsInteraction(LivingUpdateEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().level)) {
			return;
		}

		// do something to player every update tick:
		if (event.getEntity() instanceof PlayerEntity) {

			// get the player
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			processCharms(event, player);
		}
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithDamage(LivingDamageEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().level)) {
			return;
		}

		// NOTE mimic checkCharms...(LivingHurEvent) for checking the player entity, IFF a charm causes a mob damage.
		
		// do something to player every update tick:
		if (event.getEntity() instanceof PlayerEntity) {
			// get the player
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			processCharms(event, player);
		}		
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithAttack(LivingHurtEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().level)) {
			return;
		}
		
//		if (event.getSource().getDirectEntity() instanceof PlayerEntity) {
		
		ServerPlayerEntity player = null;
		if (event.getEntity() instanceof PlayerEntity) {
			player = (ServerPlayerEntity) event.getEntity();
		}
		else if (event.getSource().getEntity() instanceof PlayerEntity) {
			player = (ServerPlayerEntity) event.getSource().getEntity();
		}
		
		if (player != null) {
			Treasure.LOGGER.debug("entity -> {}, source -> {}", event.getEntity().getName().getString(), player.getName().getString());
//		if (event.getEntity() instanceof PlayerEntity) {
			Treasure.LOGGER.debug("hurt damage amount -> {}", event.getAmount());
			// get the player
//			ServerPlayerEntity player = (ServerPlayerEntity) event.getSource().getDirectEntity();
//			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
			processCharms(event, player);
		}
	}

	// Maybe use BlockEvent.BreakBlock and then use Global Loot Modifiers??
	//	@SubscribeEvent
	//	public void checkCharmsInteractionWithBlock(BlockEvent.HarvestDropsEvent event) {
	//		if (WorldInfo.isClientSide(event.getWorld())) {
	//			return;
	//		}
	//
	//		if (event.getHarvester() == null) {
	//			return;
	//		}
	//
	//		// if the harvested blcok has a tile entity then don't process
	//		// NOTE this may exclude non-inventory blocks
	//		IBlockState harvestedState = event.getState();
	//		Block harvestedBlock = harvestedState.getBlock();
	//		if (harvestedBlock.hasTileEntity(harvestedState)) {
	//			return;
	//		}
	//
	//		// get the player
	//		EntityPlayerMP player = (EntityPlayerMP) event.getHarvester();
	//		processCharms(event, player);
	//	}

	/**
	 * 
	 * @param event
	 * @param player
	 */
	private static void processCharms(Event event, ServerPlayerEntity player) {
		/*
		 * a list of charm contexts to execute
		 */
		List<CharmContext> charmsToExecute;

		// gather all charms
		charmsToExecute = gatherCharms(event, player);

		// TODO filter charms ??

		// sort charms
		Collections.sort(charmsToExecute, CharmContext.priorityComparator);

		// execute charms
		executeCharms(event, player, charmsToExecute);
	}

	/**
	 * Examine and collect  all Charms (not CharmEntity nor CharmItems) that the player has in valid slots.
	 * @param event
	 * @param player
	 * @return
	 */
	private static List<CharmContext> gatherCharms(Event event, ServerPlayerEntity player) {
		final List<CharmContext> contexts = new ArrayList<>(5);

		// get the slot provider - curios (general slots) or minecraft (hotbar)
		String slotProviderId  = ModList.get().isLoaded(CURIOS_ID) ? CURIOS_ID : "minecaft";
		
		// check each hand
		for (Hand hand : Hand.values()) {
			ItemStack heldStack = player.getItemInHand(hand);
//			Treasure.LOGGER.debug("is executing -> {}", heldStack.getCapability(CHARMABLE).map(cap -> cap.isExecuting()).orElse(false));
			if (heldStack.getCapability(CHARMABLE).map(cap -> cap.isExecuting()).orElse(false)) {
				heldStack.getCapability(CHARMABLE).ifPresent(cap -> {
					for (InventoryType type : InventoryType.values()) {
						AtomicInteger index = new AtomicInteger();
						// requires indexed for-loop
						for (int i = 0; i < cap.getCharmEntities()[type.getValue()].size(); i++) {
							ICharmEntity entity =  cap.getCharmEntities()[type.getValue()].get(i);
							// OR just check with the charm for allowable event
							//						if (!TreasureCharms.isCharmEventRegistered(event.getClass(), entity.getCharm().getType())) {
							if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
//								Treasure.LOGGER.debug("charm type -> {} is not register for this event -> {}", entity.getCharm().getType(), event.getClass().getSimpleName());
								continue;
							}
							index.set(i);
							CharmContext context  = new CharmContext.Builder().with($ -> {
								$.hand = hand;
								$.itemStack = heldStack;
								$.capability = cap;
								$.type = type;
								$.index = index.get();
								$.entity = entity;
							}).build();
							contexts.add(context);
						}
					}
				});
			}
		}

		// check slots
		if (slotProviderId.equals(CURIOS_ID)) {
			// check curio slots
			LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
			handler.ifPresent(itemHandler -> {
				// curios type names -> head, necklace, back, bracelet, hands, ring, belt, charm, feet
				CURIOS_SLOTS.forEach(slot -> {
					Optional<ICurioStacksHandler> stacksOptional = itemHandler.getStacksHandler(slot);
					stacksOptional.ifPresent(stacksHandler -> {
						ItemStack curiosStack = stacksHandler.getStacks().getStackInSlot(0);
						curiosStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							for (InventoryType type : InventoryType.values()) {
								AtomicInteger index = new AtomicInteger();
								// requires indexed for-loop
								for (int i = 0; i < cap.getCharmEntities()[type.getValue()].size(); i++) {
									ICharmEntity entity =  cap.getCharmEntities()[type.getValue()].get(i);
									//									if (!TreasureCharms.isCharmEventRegistered(event.getClass(), entity.getCharm().getType())) {
									if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
										//									Treasure.LOGGER.debug("charm type -> {} is not register for this event -> {}", entity.getCharm().getType(), event.getClass().getSimpleName());
										continue;
									}
									index.set(i);
									CharmContext curiosContext = new CharmContext.Builder().with($ -> {
										$.slotProviderId = slotProviderId;
										$.slot = slot;
										$.itemStack = curiosStack;
										$.capability = cap;
										$.type = type;
										$.index = index.get();
										$.entity = entity;
									}).build();
									contexts.add(curiosContext);
								}
							}
						});
					});
				});
			});
		}
		else {			
			// TODO check hotbar slots
			// TODO only allow IAdornments from hotbar
		}
		return contexts;
	}

	/**
	 * 
	 * @param event
	 * @param player
	 * @param contexts
	 */
	private static void executeCharms(Event event, ServerPlayerEntity player, List<CharmContext> contexts) {
		/*
		 * a list of charm types that are non-stackable that should not be executed more than once.
		 */
		final List<String> executeOnceCharmTypes = new ArrayList<>(5);

		contexts.forEach(context -> {
			ICharm charm = (ICharm)context.getEntity().getCharm();
			if (!charm.isEffectStackable()) {
				// check if this charm type is already in the monitored list
				if (executeOnceCharmTypes.contains(charm.getType())) {
					return;
				}
				else {
					Treasure.LOGGER.debug("adding charm to execute once list");
					// add the charm type to the monitored list
					executeOnceCharmTypes.add(charm.getType());
				}
			}

			// if charm is executable and executes successfully
			if (context.getEntity().getCharm().update(player.level, new Random(), new Coords(player.position()), player, event, context.getEntity())) {
				// send state message to client
				CharmMessageToClient message = new CharmMessageToClient(player.getStringUUID(), context);
				TreasureNetworking.simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), message);
			}

			// remove if uses are empty and the capability is bindable ie. charm, not adornment
			// NOTE this leaves empty charms on non-bindables for future recharge
			if (context.getEntity().getValue() <= 0.0 && context.getCapability().isBindable()) {
				Treasure.LOGGER.debug("charm is empty -> remove");
				// TODO call cap.remove() -> recalcs highestLevel
				// locate the charm from context and remove
				//				context.getCapability().getCharmEntities()[context.getType().getValue()].remove(context.getIndex());
				context.getCapability().remove(context.getType(), context.getIndex());
			}
		});
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().level)) {
			return;
		}
		//		Treasure.LOGGER.debug("is remote? -> {}", !event.getPlayer().level.isClientSide);
		//		Treasure.LOGGER.debug("{} tossing item -> {}", event.getPlayer().getName().getString(), event.getEntityItem().getItem().getDisplayName().getString());
		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof IWishable) {
			ItemStack stack = event.getEntityItem().getItem();
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString(IWishable.DROPPED_BY_KEY, event.getPlayer().getName().getString());
			//			Treasure.LOGGER.debug("adding tag to wishable stack...");
			stack.setTag(nbt);			
		}		
	}

	// TODO test - remove
	//	@SubscribeEvent
	//	public void onItemInfo(ItemTooltipEvent event) {
	//		if (event.getItemStack().getItem() == Items.EMERALD) {
	//			event.getToolTip().add(new TranslationTextComponent("tooltip.label.coin").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
	//		}
	//	}
}
