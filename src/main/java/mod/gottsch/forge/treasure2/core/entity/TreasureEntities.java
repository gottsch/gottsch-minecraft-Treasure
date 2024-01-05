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
package mod.gottsch.forge.treasure2.core.entity;

import java.util.ArrayList;
import java.util.List;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoulEntity;
import mod.gottsch.forge.treasure2.core.entity.monster.CardboardBoxMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.CauldronChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.CrateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.MoldyCrateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.PirateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.VikingChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.WoodChestMimic;
import mod.gottsch.forge.treasure2.core.gui.model.entity.BoundSoulModel;
import mod.gottsch.forge.treasure2.core.gui.render.entity.BoundSoulRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.CardboardBoxMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.CauldronChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.CrateChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.MoldyCrateChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.PirateChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.VikingChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.entity.WoodChestMimicRenderer;
import mod.gottsch.forge.treasure2.core.item.TreasureItemGroups;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
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
	public static final EntityType<WoodChestMimic> WOOD_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("wood_chest_mimic"), WoodChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<PirateChestMimic> PIRATE_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("pirate_chest_mimic"), PirateChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<VikingChestMimic> VIKING_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("viking_chest_mimic"), VikingChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<CrateChestMimic> CRATE_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("crate_chest_mimic"), CrateChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<MoldyCrateChestMimic> MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("moldy_crate_chest_mimic"), MoldyCrateChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<CauldronChestMimic> CAULDRON_CHEST_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("cauldron_chest_mimic"), CauldronChestMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);
	public static final EntityType<CardboardBoxMimic> CARDBOARD_BOX_MIMIC_ENTITY_TYPE = make(ModUtils.asLocation("cardboard_box_mimic"), CardboardBoxMimic::new, EntityClassification.MONSTER, 0.875F, 0.875F);

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
		registry.register(spawnEgg(WOOD_CHEST_MIMIC_ENTITY_TYPE, 0x9f844d, 0x54442c));
		registry.register(spawnEgg(PIRATE_CHEST_MIMIC_ENTITY_TYPE, 0x010101, 0x3b3b3b));
		registry.register(spawnEgg(VIKING_CHEST_MIMIC_ENTITY_TYPE, 0x642e1e, 0x753c27));
		registry.register(spawnEgg(CAULDRON_CHEST_MIMIC_ENTITY_TYPE, 0x6e5c30, 0x4a4a4a));
		registry.register(spawnEgg(CRATE_CHEST_MIMIC_ENTITY_TYPE, 0x6e5c60, 0x434343));
		registry.register(spawnEgg(MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE, 0x635360, 0x464646));
		registry.register(spawnEgg(CARDBOARD_BOX_MIMIC_ENTITY_TYPE, 0x6f5e60, 0x404040));
		
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(ALL.toArray(new EntityType<?>[0]));
		EntitySpawnPlacementRegistry.register(BOUND_SOUL_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(WOOD_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(PIRATE_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(VIKING_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(CAULDRON_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(CRATE_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		EntitySpawnPlacementRegistry.register(CARDBOARD_BOX_MIMIC_ENTITY_TYPE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
		
	}
	
	@SubscribeEvent
	public static void addEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(BOUND_SOUL_ENTITY_TYPE, BoundSoulEntity.registerAttributes().build());
		event.put(WOOD_CHEST_MIMIC_ENTITY_TYPE, WoodChestMimic.registerAttributes().build());
		event.put(PIRATE_CHEST_MIMIC_ENTITY_TYPE, PirateChestMimic.registerAttributes().build());
		event.put(VIKING_CHEST_MIMIC_ENTITY_TYPE, VikingChestMimic.registerAttributes().build());
		event.put(CAULDRON_CHEST_MIMIC_ENTITY_TYPE, CauldronChestMimic.registerAttributes().build());
		event.put(CRATE_CHEST_MIMIC_ENTITY_TYPE, CrateChestMimic.registerAttributes().build());
		event.put(MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE, MoldyCrateChestMimic.registerAttributes().build());
		event.put(CARDBOARD_BOX_MIMIC_ENTITY_TYPE, CardboardBoxMimic.registerAttributes().build());
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(BOUND_SOUL_ENTITY_TYPE, m -> new BoundSoulRenderer(m, new BoundSoulModel(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(WOOD_CHEST_MIMIC_ENTITY_TYPE, m -> new WoodChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(PIRATE_CHEST_MIMIC_ENTITY_TYPE, m -> new PirateChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(VIKING_CHEST_MIMIC_ENTITY_TYPE, m -> new VikingChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CAULDRON_CHEST_MIMIC_ENTITY_TYPE, m -> new CauldronChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CRATE_CHEST_MIMIC_ENTITY_TYPE, m -> new CrateChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE, m -> new MoldyCrateChestMimicRenderer(m, 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(CARDBOARD_BOX_MIMIC_ENTITY_TYPE, m -> new CardboardBoxMimicRenderer(m, 0.5F));
		
	}

}
