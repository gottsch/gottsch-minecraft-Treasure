package mod.gottsch.forge.treasure2.core.event;

import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class WorldEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onWorldLoad(WorldEvent.Load event) {
		Treasure.LOGGER.info("In world load event");
		
		if (WorldInfo.isServerSide((Level)event.getWorld())) {
			// NOTE this has to happen here or some event AFTER the FMLCommonSetup
			// when all blocks, items, etc are registered and tags are read in.
			
			// TODO make similar to old T2, when you register a mod in main, but then action occurs here.
			// register all the keys
//			TreasureApi.registerKey(TreasureItems.WOOD_KEY);
		}
	}
}
