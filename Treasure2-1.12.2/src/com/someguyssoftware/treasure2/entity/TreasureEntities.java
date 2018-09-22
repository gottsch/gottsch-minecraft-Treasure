/**
 * 
 */
package com.someguyssoftware.treasure2.entity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

/**
 * @author Mark Gottschling on Aug 18, 2018
 *
 */
public class TreasureEntities {

	/**
	 * 
	 * @author Mark Gottschling on Aug 18, 2018
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID)
	public static class RegistrationHandler {

		/**
		 * 
		 * @param event
		 */
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
			// register the entities
			EntityEntry entry = EntityEntryBuilder.create()
				    .entity(WoodMimicEntity.class)
				    .id(new ResourceLocation("wood_mimic"), WoodMimicEntity.MOB_ID)
				    .name("wood_mimic")
				    .egg(0xFFFFFF, 0xAAAAAA)
				    .tracker(64, 20, false)
				    .build();
			event.getRegistry().register(entry);	
			
			// add data fixers
			DataFixer dataFixer = new DataFixer(1343);
	        dataFixer = new net.minecraftforge.common.util.CompoundDataFixer(dataFixer);
			WoodMimicEntity.registerFixesWoodenMimic(dataFixer);
		}
	}
}
