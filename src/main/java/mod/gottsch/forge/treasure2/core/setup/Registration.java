/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.setup;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Mark Gottschling on Aug 13, 2022
 *
 */
public class Registration {
	/*
	 * deferred registries
	 */
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Treasure.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Treasure.MODID);
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Treasure.MODID);
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Treasure.MODID);

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Treasure.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Treasure.MODID);
	
    // item properties convenience property
	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties();


	
	// TODO just call this method, unless one of the TreasureX.register() methods actually does more
	// processing than just being a proxy
	public static void init() {
//		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
//		BLOCKS.register(eventBus);
//		ITEMS.register(eventBus);	
//		BLOCK_ENTITIES.register(eventBus);
//		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static void registerBlocks(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
	public static void registerItems(IEventBus bus) {
		ITEMS.register(bus);
	}

	public static void registerBlockEntities(IEventBus bus) {
		BLOCK_ENTITIES.register(bus);
	}

	public static void registerContainers(IEventBus bus) {
		CONTAINERS.register(bus);
	}
	
	public static void registerParticles(IEventBus bus) {
		PARTICLES.register(bus);
	}

	public static void registerEntities(IEventBus bus) {
		ENTITIES.register(bus);
	}
}
