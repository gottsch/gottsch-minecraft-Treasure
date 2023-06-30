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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 14, 2022
 *
 */
public class TreasureBlockEntities {
	
	public static final RegistryObject<BlockEntityType<WoodChestBlockEntity>> WOOD_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("wood_chest_block_entity", 
					() -> BlockEntityType.Builder.of(WoodChestBlockEntity::new, TreasureBlocks.WOOD_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CrateChestBlockEntity>> CRATE_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("crate_chest_block_entity", 
					() -> BlockEntityType.Builder.of(CrateChestBlockEntity::new, TreasureBlocks.CRATE_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<MoldyCrateChestBlockEntity>> MOLDY_CRATE_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("moldy_crate_chest_block_entity", 
					() -> BlockEntityType.Builder.of(MoldyCrateChestBlockEntity::new, TreasureBlocks.MOLDY_CRATE_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<IronboundChestBlockEntity>> IRONBOUND_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("ironbound_chest_block_entity", 
					() -> BlockEntityType.Builder.of(IronboundChestBlockEntity::new, TreasureBlocks.IRONBOUND_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<PirateChestBlockEntity>> PIRATE_CHEST_BLOCK_ENTITY_TYPE = Registration.BLOCK_ENTITIES.register("pirate_chest_block_entity", () -> BlockEntityType.Builder.of(PirateChestBlockEntity::new, TreasureBlocks.PIRATE_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SafeBlockEntity>> SAFE_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("safe_block_entity", 
					() -> BlockEntityType.Builder.of(SafeBlockEntity::new, TreasureBlocks.SAFE.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<IronStrongboxBlockEntity>> IRON_STRONGBOX_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("iron_strongbox_block_entity", 
					() -> BlockEntityType.Builder.of(IronStrongboxBlockEntity::new, TreasureBlocks.IRON_STRONGBOX.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<GoldStrongboxBlockEntity>> GOLD_STRONGBOX_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("gold_strongbox_block_entity", 
					() -> BlockEntityType.Builder.of(GoldStrongboxBlockEntity::new, TreasureBlocks.GOLD_STRONGBOX.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<DreadPirateChestBlockEntity>> DREAD_PIRATE_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("dread_pirate_chest_block_entity", 
					() -> BlockEntityType.Builder.of(DreadPirateChestBlockEntity::new, TreasureBlocks.DREAD_PIRATE_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CompressorChestBlockEntity>> COMPRESSOR_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("compressor_chest_block_entity", 
					() -> BlockEntityType.Builder.of(CompressorChestBlockEntity::new, TreasureBlocks.COMPRESSOR_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SkullChestBlockEntity>> SKULL_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("skull_chest_block_entity", 
					() -> BlockEntityType.Builder.of(SkullChestBlockEntity::new, TreasureBlocks.SKULL_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<GoldSkullChestBlockEntity>> GOLD_SKULL_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("gold_skull_chest_block_entity", 
					() -> BlockEntityType.Builder.of(GoldSkullChestBlockEntity::new, TreasureBlocks.GOLD_SKULL_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CrystalSkullChestBlockEntity>> CRYSTAL_SKULL_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("crystal_skull_chest_block_entity", 
					() -> BlockEntityType.Builder.of(CrystalSkullChestBlockEntity::new, TreasureBlocks.CRYSTAL_SKULL_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CauldronChestBlockEntity>> CAULDRON_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("cauldron_chest_block_entity", 
					() -> BlockEntityType.Builder.of(CauldronChestBlockEntity::new, TreasureBlocks.CAULDRON_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SpiderChestBlockEntity>> SPIDER_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("spider_chest_block_entity", 
					() -> BlockEntityType.Builder.of(SpiderChestBlockEntity::new, TreasureBlocks.SPIDER_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<VikingChestBlockEntity>> VIKING_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("viking_chest_block_entity", 
					() -> BlockEntityType.Builder.of(VikingChestBlockEntity::new, TreasureBlocks.VIKING_CHEST.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CardboardBoxBlockEntity>> CARDBOARD_BOX_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("cardboard_box_block_entity", 
					() -> BlockEntityType.Builder.of(CardboardBoxBlockEntity::new, TreasureBlocks.CARDBOARD_BOX.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<MilkCrateBlockEntity>> MILK_CRATE_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("milk_crate_block_entity", 
					() -> BlockEntityType.Builder.of(MilkCrateBlockEntity::new, TreasureBlocks.MILK_CRATE.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<WitherChestBlockEntity>> WITHER_CHEST_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("wither_chest_block_entity", 
					() -> BlockEntityType.Builder.of(WitherChestBlockEntity::new, TreasureBlocks.WITHER_CHEST.get()).build(null));
	
	
	public static final RegistryObject<BlockEntityType<TreasureProximitySpawnerBlockEntity>> TREASURE_PROXIMITY_SPAWNER_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("treasure_proximity_spawner", 
			() -> BlockEntityType.Builder.of(TreasureProximitySpawnerBlockEntity::new, 
					TreasureBlocks.PROXIMITY_SPAWNER.get()
				).build(null));
	
	public static final RegistryObject<BlockEntityType<GravestoneProximitySpawnerBlockEntity>> GRAVESTONE_PROXIMITY_SPAWNER_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("gravestone_proximity_spawner", 
			() -> BlockEntityType.Builder.of(GravestoneProximitySpawnerBlockEntity::new, 
					TreasureBlocks.GRAVESTONE1_SPAWNER_STONE.get(),
					TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE.get(),
					TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN.get()
				).build(null));
	
	public static final RegistryObject<BlockEntityType<MistEmitterBlockEntity>> MIST_EMITTER_BLOCK_ENTITY_TYPE = 
			Registration.BLOCK_ENTITIES.register("mist_emitter", 
				() -> BlockEntityType.Builder.of(MistEmitterBlockEntity::new, 
						TreasureBlocks.WITHER_SOUL_LOG.get()
					).build(null));

	
	public static void register(IEventBus bus) {
		// cycle through all block and create items
		Registration.registerBlockEntities(bus);
	}
}
