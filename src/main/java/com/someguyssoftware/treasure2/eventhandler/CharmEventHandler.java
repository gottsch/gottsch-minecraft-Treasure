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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 
 * @author Mark Gottschling on Dec 19, 2021
 *
 */
public class CharmEventHandler {
	private IEquipmentCharmHandler equipmentCharmHandler;

	/**
	 * 
	 * @param handler
	 */
	public CharmEventHandler(IEquipmentCharmHandler handler) {
		equipmentCharmHandler = handler;
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

	/**
	 * 
	 * @param event
	 */
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
		/*
		 * a list of charm contexts to execute
		 */
		List<CharmContext> charmsToExecute;

		// gather all charms
		charmsToExecute = gatherCharms(event, player);

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
	private List<CharmContext> gatherCharms(Event event, EntityPlayerMP player) {
		final List<CharmContext> contexts = new ArrayList<>(5);

		// check each hand
		for (EnumHand hand : EnumHand.values()) {
			ItemStack heldStack = player.getHeldItem(hand);
			if (heldStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
				contexts.addAll(getCharmsFromStack(event, hand, -1, heldStack, false));
			}
		}

		// check equipment slots
		List<CharmContext> equipmentContexts = getEquipmentCharmHandler().handleEquipmentCharms(event, player);
		contexts.addAll(equipmentContexts);

		return contexts;
	}

	/**
	 * 
	 * @param event
	 * @param hand
	 * @param itemStack
	 * @param isPouch
	 * @return
	 */
	private List<CharmContext> getCharmsFromStack(Event event, EnumHand hand, int slot, ItemStack itemStack, boolean isPouch) {
		final List<CharmContext> contexts = new ArrayList<>(5);
		ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		if (cap.isExecuting()) {
			for (InventoryType type : InventoryType.values()) {
				AtomicInteger index = new AtomicInteger();
				for (int i = 0; i < cap.getCharmEntities().get(type).size(); i++) {
					ICharmEntity entity = ((List<ICharmEntity>)cap.getCharmEntities().get(type)).get(i);
					if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
						continue;
					}
					index.set(i);
					CharmContext context = new CharmContext.Builder().with($ -> {
						$.hand = hand;
						$.slot = slot;
						$.slotProviderId = isPouch ? "treasure2" : "minecraft";
						$.itemStack = itemStack;
						$.capability = cap;
						$.type = type;
						$.index = index.get();
						$.entity = entity;						
					}).build();
					contexts.add(context);
				}
			}
		}
		return contexts;
	}

	/**
	 * 
	 * @param event
	 * @param player
	 * @param contexts
	 */
	private static void executeCharms(Event event, EntityPlayerMP player, List<CharmContext> contexts) {
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
					// add the charm type to the monitored list
					executeOnceCharmTypes.add(charm.getType());
				}
			}

			// if charm is executable and executes successfully
			if (context.getEntity().getCharm().update(player.world, new Random(), new Coords(player.getPosition()), player, event, context.getEntity())) {
				// TODO handle the durability of the adornment
				processUsage(player.world, player, event, context);

				// send state message to client
				CharmMessageToClient message = new CharmMessageToClient(player.getUUID(player.getGameProfile()).toString(), context);
				//				Treasure.logger.debug("Message to client -> {}", message);
				Treasure.simpleNetworkWrapper.sendTo(message, player);
			}

			// remove if innate and empty
			if (context.getType() ==InventoryType.INNATE
					&& context.getEntity().getMana() <= 0.0 && context.getCapability().isSocketable()) {
								Treasure.logger.debug("charm is empty -> remove");
								// TODO call cap.remove() -> recalcs highestLevel
								// locate the charm from context and remove
								context.getCapability().remove(context.getType(), context.getIndex());
			}
			
			// remove if uses are empty and the capability is bindable ie. charm, not adornment
			// NOTE this leaves empty charms on non-bindables for future recharge
			// TODO re-enable in next versions when charms are upgraded/refactored.
//						if (context.getEntity().getValue() <= 0.0 && context.getCapability().isBindable()) {
			//				Treasure.logger.debug("charm is empty -> remove");
			//				// TODO call cap.remove() -> recalcs highestLevel
			//				// locate the charm from context and remove
			//				//				context.getCapability().getCharmEntities()[context.getType().getValue()].remove(context.getIndex());
			//				context.getCapability().remove(context.getType(), context.getIndex());
//						}

		});
	}

	private static void processUsage(World world, EntityPlayerMP player, Event event, CharmContext context) {
		// TODO call capability.getDecrementor.apply() or something like that.
		ItemStack stack = context.getItemStack();
		// get capability
		if (stack.hasCapability(TreasureCapabilities.DURABILITY, null)) {
			IDurabilityCapability cap = stack.getCapability(TreasureCapabilities.DURABILITY, null);
			if (cap.isInfinite()) {
				return;
			}
			stack.setItemDamage(stack.getItemDamage() + 1);
			if (stack.getItemDamage() >= cap.getDurability()) {
				// break key;
				stack.shrink(1);
			}
		}
	}

	public IEquipmentCharmHandler getEquipmentCharmHandler() {
		return equipmentCharmHandler;
	}

}
