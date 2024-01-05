/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.sound;

import mod.gottsch.forge.treasure2.core.Treasure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 */
public class TreasureSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Treasure.MODID);
	
	public static final RegistryObject<SoundEvent> AMBIENT_MIMIC = registerSoundEvent("ambient_mimic");
	public static final RegistryObject<SoundEvent> AMBIENT_MIMIC2 = registerSoundEvent("ambient_mimic2");
	public static final RegistryObject<SoundEvent> AMBIENT_MIMIC3 = registerSoundEvent("ambient_mimic3");
		
	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Treasure.MODID, name)));
	}
	
	public static void register() {
		SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
