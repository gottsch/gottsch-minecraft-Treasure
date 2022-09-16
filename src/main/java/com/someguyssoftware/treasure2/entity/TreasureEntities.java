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
package com.someguyssoftware.treasure2.entity;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.monster.BoundSoulEntity;
import com.someguyssoftware.treasure2.gui.model.entity.BoundSoulModel;
import com.someguyssoftware.treasure2.gui.render.entity.BoundSoulRenderer;
import com.someguyssoftware.treasure2.item.TreasureItemGroups;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
/**
 * @author Mark Gottschling on Jul 22, 2021
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureEntities {
	
	// list to contain all entity types
	private static final List<EntityType<?>> ALL = new ArrayList<>();
	
	public static final EntityType<BoundSoulEntity> BOUND_SOUL_ENTITY_TYPE = make(ModUtils.asLocation(TreasureConfig.EntityID.BOUND_SOUL_ID), BoundSoulEntity::new, EntityClassification.MONSTER, 0.6F, 1.9F);

	    private static <E extends Entity> EntityType<E> make(ResourceLocation id, EntityType.IFactory<E> factory, EntityClassification classification, float width, float height) {
			return build(id, makeBuilder(factory, classification).sized(width, height));
		}
	    
	    /**
	     * 
	     * @param <E>
	     * @param id
	     * @param builder
	     * @return
	     */
		@SuppressWarnings("unchecked")
		private static <E extends Entity> EntityType<E> build(ResourceLocation id, EntityType.Builder<E> builder) {
			boolean cache = SharedConstants.CHECK_DATA_FIXER_SCHEMA;
			SharedConstants.CHECK_DATA_FIXER_SCHEMA = false;
			EntityType<E> entityType = (EntityType<E>) builder.build(id.toString()).setRegistryName(id);
			SharedConstants.CHECK_DATA_FIXER_SCHEMA = cache;
			ALL.add(entityType);
			return entityType;
		}

		private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification) {
			return makeBuilder(factory, classification, 80);
		}
		
		private static <E extends Entity> EntityType.Builder<E> makeBuilder(EntityType.IFactory<E> factory, EntityClassification classification, int trackingRange) {
			return EntityType.Builder.of(factory, classification)
					.sized(0.6F, 1.8F)
					.setTrackingRange(trackingRange)
					.setUpdateInterval(3)
					.setShouldReceiveVelocityUpdates(true);
		}
		
		/**
		 * 
		 * @param type
		 * @param color1
		 * @param color2
		 * @return
		 */
		private static Item spawnEgg(EntityType<?> type, int color1, int color2) {
			ResourceLocation eggId = new ResourceLocation(type.getRegistryName().getNamespace(), type.getRegistryName().getPath() + "_spawn_egg");
			return new SpawnEggItem(type, color1, color2, new Item.Properties().tab(TreasureItemGroups.TREASURE_ITEM_GROUP)).setRegistryName(eggId);
		}
		
		@SubscribeEvent
		public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
			IForgeRegistry<Item> registry = event.getRegistry();
			registry.register(spawnEgg(BOUND_SOUL_ENTITY_TYPE, 0xCCC333, 0x221F6D));
		}
		
		@SubscribeEvent
		public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
			event.getRegistry().registerAll(ALL.toArray(new EntityType<?>[0]));
			EntitySpawnPlacementRegistry.register(BOUND_SOUL_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		}
		
		@SubscribeEvent
		public static void addEntityAttributes(EntityAttributeCreationEvent event) {
			event.put(BOUND_SOUL_ENTITY_TYPE, BoundSoulEntity.registerAttributes().build());
			
		}
		
		@OnlyIn(Dist.CLIENT)
		public static void registerEntityRenderers() {
			RenderingRegistry.registerEntityRenderingHandler(BOUND_SOUL_ENTITY_TYPE, m -> new BoundSoulRenderer(m, new BoundSoulModel(), 0.5F));
		}

}
