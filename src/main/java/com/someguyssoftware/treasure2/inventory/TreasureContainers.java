/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Mark Gottschling on Aug 18, 2020
 *
 */
public class TreasureContainers {
	public static ContainerType<StandardChestContainer> standardChestContainerType;
	public static ContainerType<StrongboxChestContainer> strongboxChestContainerType;
	public static ContainerType<SkullChestContainer> skullChestContainerType;
	public static ContainerType<CompressorChestContainer> compressorChestContainerType;
	public static ContainerType<WitherChestContainer> witherChestContainerType;
	
	
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)	
	public static class RegistrationHandler {		
		
		@SubscribeEvent
		public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
			standardChestContainerType = IForgeContainerType.create(StandardChestContainer::create);
			standardChestContainerType.setRegistryName("standard_chest_container");
			event.getRegistry().register(standardChestContainerType);
			
			strongboxChestContainerType = IForgeContainerType.create(StrongboxChestContainer::create);
			strongboxChestContainerType.setRegistryName("strongbox_chest_container");
			event.getRegistry().register(strongboxChestContainerType);
			
			skullChestContainerType = IForgeContainerType.create(SkullChestContainer::create);
			skullChestContainerType.setRegistryName("skull_chest_container");
			event.getRegistry().register(skullChestContainerType);
			
			compressorChestContainerType = IForgeContainerType.create(CompressorChestContainer::create);
			compressorChestContainerType.setRegistryName("compressor_chest_container");
			event.getRegistry().register(compressorChestContainerType);
			
			witherChestContainerType = IForgeContainerType.create(WitherChestContainer::create);
			witherChestContainerType.setRegistryName("wither_chest_container");
			event.getRegistry().register(witherChestContainerType);
		}
	}
}
