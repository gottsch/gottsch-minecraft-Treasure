/**
 *
 */
package com.someguyssoftware.treasure2.sound;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * The mod's sound events. Each name matches an entry in assets/treasure2/sounds.json.
 *
 * @author Mark Gottschling on 10/16/2026
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID)
public class TreasureSounds {
	public static final SoundEvent AMBIENT_MIMIC = create("ambient_mimic");

	private TreasureSounds() {}

	private static SoundEvent create(String name) {
		ResourceLocation location = new ResourceLocation(Treasure.MODID, name);
		return new SoundEvent(location).setRegistryName(location);
	}

	@SubscribeEvent
	public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(AMBIENT_MIMIC);
	}
}
