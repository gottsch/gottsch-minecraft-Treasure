package com.someguyssoftware.treasure2.eventhandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.CharmContext;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HotbarEquipmentCharmHandler implements IEquipmentCharmHandler {
	private static final int MAX_HOTBAR_CHARMS = 4;

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
				if (inventoryStack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
					ICharmableCapability cap = inventoryStack.getCapability(TreasureCapabilities.CHARMABLE, null);
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
									$.slotProviderId = "minecraft";
									$.slot = slot.get();
									$.itemStack = inventoryStack;
									$.capability = cap;
									$.type = type;
									$.index = index.get();
									$.entity = entity;
								}).build();
								contexts.add(context);
							}
						}
						adornmentCount.getAndIncrement();
						if (adornmentCount.get() >= MAX_HOTBAR_CHARMS) {
							break;
						}
					}
				}		
			}
		}
		return contexts;
	}
}
