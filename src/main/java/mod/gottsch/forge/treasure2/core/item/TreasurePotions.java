/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Level III potions, found only in Epic and better chests (as in 1.12.2). The loot tables name them
 * as treasure2:extra_strong_*; until these were registered, each one came out as an Uncraftable Potion.
 * Each takes its vanilla potion's name, so it shows as, e.g., Potion of Healing.
 *
 * @author Mark Gottschling on 10/19/2026
 */
public class TreasurePotions {
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Treasure.MODID);

	public static final RegistryObject<Potion> EXTRA_STRONG_HEALING = POTIONS.register("extra_strong_healing",
			() -> new Potion("healing", new MobEffectInstance(MobEffects.HEAL, 1, 2)));
	public static final RegistryObject<Potion> EXTRA_STRONG_STRENGTH = POTIONS.register("extra_strong_strength",
			() -> new Potion("strength", new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 2)));
	public static final RegistryObject<Potion> EXTRA_STRONG_LEAPING = POTIONS.register("extra_strong_leaping",
			() -> new Potion("leaping", new MobEffectInstance(MobEffects.JUMP, 1800, 2)));
	public static final RegistryObject<Potion> EXTRA_STRONG_SWIFTNESS = POTIONS.register("extra_strong_swiftness",
			() -> new Potion("swiftness", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 2)));
	public static final RegistryObject<Potion> EXTRA_STRONG_REGENERATION = POTIONS.register("extra_strong_regeneration",
			() -> new Potion("regeneration", new MobEffectInstance(MobEffects.REGENERATION, 450, 2)));
	public static final RegistryObject<Potion> EXTRA_STRONG_POISON = POTIONS.register("extra_strong_poison",
			() -> new Potion("poison", new MobEffectInstance(MobEffects.POISON, 432, 2)));

	public static void register(IEventBus bus) {
		POTIONS.register(bus);
	}
}
