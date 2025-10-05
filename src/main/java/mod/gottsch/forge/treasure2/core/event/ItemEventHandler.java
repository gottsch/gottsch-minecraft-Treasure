/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.event;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.entity.item.ExplosionProofItemEntity;
import mod.gottsch.forge.treasure2.core.item.TreasureChestBlockItem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling on Sep 4, 2021
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ItemEventHandler {
	// caching mechanism
	public static Item lastTossed;
	public static boolean isWishable;

	/**
	 * wrap Treasure Chest BlockItems in a explosion proof ItemEntity on join.
	 * @param event
	 */
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		if(!event.getLevel().isClientSide() && event.getEntity() instanceof ItemEntity && !(event.getEntity() instanceof ExplosionProofItemEntity)) {
			ItemEntity itemEntity = (ItemEntity) event.getEntity();

			if (itemEntity.getItem().getItem() instanceof TreasureChestBlockItem) {
				ExplosionProofItemEntity newItemEntity = new ExplosionProofItemEntity(event.getLevel(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), itemEntity.getItem());
				newItemEntity.setDeltaMovement(itemEntity.getDeltaMovement());
				newItemEntity.setDefaultPickUpDelay();
				event.getLevel().addFreshEntity(newItemEntity);
				event.setCanceled(true);
			}
		}
	}

	/**
	 * prevent Treasure Chest BlockItems that have been tossed into Wishing Wells from taking Lightning damage.
	 * @param event
	 */
	@SubscribeEvent
	public static void onLightningStrike(EntityStruckByLightningEvent event) {
		if(event.getEntity() instanceof ExplosionProofItemEntity) {
			event.setCanceled(true);
		}
	}
}