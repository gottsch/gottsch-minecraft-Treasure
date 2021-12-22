package com.someguyssoftware.treasure2.eventhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.treasure2.capability.ICharmInventoryCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HotbarEquipmentCharmHandler implements IEquipmentCharmHandler {

	/**
	 * Without redoing a big chunk of functionality, temporarily pass in the event handler
	 * so the methods can be accessed. (TODO fix for v2.0)
	 */
	@Override
	public List<CharmContext> handleEquipmentCharms(Event event, EntityPlayerMP player) {
		final List<CharmContext> contexts = new ArrayList<>(5);
		AtomicInteger adornmentCount = new AtomicInteger(0);
		AtomicInteger slot = new AtomicInteger(-1);

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
			slot.set(hotbarSlot);
			ItemStack inventoryStack = player.inventory.getStackInSlot(hotbarSlot);
			if (inventoryStack != player.getHeldItemMainhand()) {
				if (inventoryStack.hasCapability(TreasureCapabilities.CHARM_INVENTORY, null)) {
					ICharmInventoryCapability cap = inventoryStack.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
					for (ICharmEntity entity : cap.getCharmEntities()) {
						if (!entity.getCharm().getRegisteredEvent().equals(event.getClass())) {
							continue;
						}
						CharmContext context = new CharmContext.Builder().with($ -> {
							$.slotProviderId = "minecraft";
							$.slot = slot.get();
							$.itemStack = inventoryStack;
							$.capability = cap;
							$.entity = entity;
						}).build();
						contexts.add(context);
					}
				}

				////			if (player.inventory.getStackInSlot(hotbarSlot) != player.getHeldItemMainhand()) {
				//				Optional<CharmContext> context = handler.getCharmContext(player, hotbarSlot);
				//				if (context.isPresent()) {
				//					if (context.get().type == CharmContext.Type.ADORNMENT) {
				//						// Treasure.logger.debug("is a hotbar adornment -> {} @ slot -> {}", context.get().itemStack.getItem().getRegistryName(), context.get().slot);
				//						// at this point, we know the item in slot x has charm capabilities
				//						doCharms(context.get(), player, event, nonMultipleUpdateCharms);
				//					}
				//				}				
			}
		}
		return contexts;
	}
}
