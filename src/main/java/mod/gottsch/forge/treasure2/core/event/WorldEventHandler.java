package mod.gottsch.forge.treasure2.core.event;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureTemplateRegistry;
import mod.gottsch.forge.treasure2.core.world.feature.TreasureConfiguredFeatures;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureOreGeneration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class WorldEventHandler {

	private static boolean isLoaded = false;
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load event) {
		Treasure.LOGGER.info("In world load event");
		
		if (WorldInfo.isServerSide((Level)event.getWorld())) {
			/* 
			 * NOTE:
			 *  this has to happen here or some event AFTER the FMLCommonSetup
			 *  when all blocks, items, etc are registered and tags are read in.
			 */

			ResourceLocation dimension = WorldInfo.getDimension((Level) event.getWorld());			
			Treasure.LOGGER.info("In world load event for dimension {}", dimension.toString());
			
			if (!isLoaded && Config.SERVER.integration.dimensionsWhiteList.get().contains(dimension.toString())) {
				// register mod's loot tables
				TreasureLootTableRegistry.onWorldLoad(event);
				TreasureTemplateRegistry.onWorldLoad(event);				
				TreasureSavedData.get((Level)event.getWorld());
			}
			
			isLoaded = true;
		}
	}
	
	@SubscribeEvent
	public static void onBiomeLoading(final BiomeLoadingEvent event) {
		/* 
		 * NOTE: 
		 * generation must occur in the correct order according to GenerationStep.Decoration
		 */
		TreasureOreGeneration.generateOres(event);

		if (event.getCategory() != BiomeCategory.OCEAN) {
			// generate surface/terrestrial chests
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TreasureConfiguredFeatures.TERRESTRIAL_CHEST_PLACED.getHolder().get());
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TreasureConfiguredFeatures.WELL_PLACED.getHolder().get());

		}
		else {
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TreasureConfiguredFeatures.SUBAQUEOUS_CHEST_PLACED.getHolder().get());
		}
	}
}
