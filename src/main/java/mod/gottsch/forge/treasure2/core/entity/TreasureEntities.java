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
package mod.gottsch.forge.treasure2.core.entity;


import mod.gottsch.forge.treasure2.core.entity.monster.*;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Mark Gottschling on Jul 22, 2021
 *
 */
public class TreasureEntities {
	public static final String BOUND_SOUL = "bound_soul";
	public static final String WOOD_CHEST_MIMIC = "wood_chest_mimic";
	public static final String PIRATE_CHEST_MIMIC = "pirate_chest_mimic";
	public static final String VIKING_CHEST_MIMIC = "viking_chest_mimic";
	public static final String CAULDRON_CHEST_MIMIC = "cauldron_chest_mimic";
	public static final String CRATE_CHEST_MIMIC = "crate_chest_mimic";
	public static final String MOLDY_CRATE_CHEST_MIMIC = "moldy_crate_chest_mimic";
	public static final String CARDBOARD_BOX_MIMIC = "cardboard_box_mimic";
	public static final String MILK_CRATE_MIMIC = "milk_crate_mimic";
	public static final String BARREL_MIMIC = "barrel_mimic";
	public static final String VANILLA_CHEST_MIMIC = "vanilla_chest_mimic";
	public static final String WITHERWOOD_GOLEM = "witherwood_golem";

	public static final RegistryObject<EntityType<BoundSoul>> BOUND_SOUL_ENTITY_TYPE = Registration.ENTITIES.register(BOUND_SOUL, () -> EntityType.Builder.of(BoundSoul::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(BOUND_SOUL));

	public static final RegistryObject<EntityType<WitherwoodGolem>> WITHERWOOD_GOLEM_ENTITY_TYPE = Registration.ENTITIES.register(WITHERWOOD_GOLEM, () -> EntityType.Builder.of(WitherwoodGolem::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(WITHERWOOD_GOLEM));
	
	public static final RegistryObject<EntityType<WoodChestMimic>> WOOD_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(WOOD_CHEST_MIMIC, () -> EntityType.Builder.of(WoodChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(WOOD_CHEST_MIMIC));
	
	public static final RegistryObject<EntityType<PirateChestMimic>> PIRATE_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(PIRATE_CHEST_MIMIC, () -> EntityType.Builder.of(PirateChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(PIRATE_CHEST_MIMIC));
	
	public static final RegistryObject<EntityType<VikingChestMimic>> VIKING_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(VIKING_CHEST_MIMIC, () -> EntityType.Builder.of(VikingChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(VIKING_CHEST_MIMIC));
	
	public static final RegistryObject<EntityType<CauldronChestMimic>> CAULDRON_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(CAULDRON_CHEST_MIMIC, () -> EntityType.Builder.of(CauldronChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(CAULDRON_CHEST_MIMIC));
	
	public static final RegistryObject<EntityType<CrateChestMimic>> CRATE_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(CRATE_CHEST_MIMIC, () -> EntityType.Builder.of(CrateChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(CRATE_CHEST_MIMIC));
	
	public static final RegistryObject<EntityType<MoldyCrateChestMimic>> MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(MOLDY_CRATE_CHEST_MIMIC, () -> EntityType.Builder.of(MoldyCrateChestMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(MOLDY_CRATE_CHEST_MIMIC));

	public static final RegistryObject<EntityType<CardboardBoxMimic>> CARDBOARD_BOX_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(CARDBOARD_BOX_MIMIC, () -> EntityType.Builder.of(CardboardBoxMimic::new, MobCategory.MONSTER)
			.sized(1F, 1.25F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(CARDBOARD_BOX_MIMIC));

	public static final RegistryObject<EntityType<MilkCrateMimic>> MILK_CRATE_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(MILK_CRATE_MIMIC, () -> EntityType.Builder.of(MilkCrateMimic::new, MobCategory.MONSTER)
			.sized(0.875F, 0.875F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(MILK_CRATE_MIMIC));

	public static final RegistryObject<EntityType<BarrelMimic>> BARREL_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(BARREL_MIMIC, () -> EntityType.Builder.of(BarrelMimic::new, MobCategory.MONSTER)
			.sized(1F, 1F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(BARREL_MIMIC));

	public static final RegistryObject<EntityType<VanillaChestMimic>> VANILLA_CHEST_MIMIC_ENTITY_TYPE = Registration.ENTITIES.register(VANILLA_CHEST_MIMIC, () -> EntityType.Builder.of(VanillaChestMimic::new, MobCategory.MONSTER)
			.sized(1F, 1F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(VANILLA_CHEST_MIMIC));

	public static void register(IEventBus bus) {
		// cycle through all block and create items
		Registration.registerEntities(bus);
	}
}
