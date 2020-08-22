/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Mark Gottschling on Aug 16, 2020
 *
 */
public class TreasureTileEntities {
	/* 
	 * NOTE can't have final properties as the creation of the tile entity can't happen until after the creation of the blocks.
	 */
	public static TileEntityType<WoodChestTileEntity> woodChestTileEntityType;
	public static TileEntityType<CrateChestTileEntity> crateChestTileEntityType;
	public static TileEntityType<MoldyCrateChestTileEntity> moldyCrateChestTileEntityType;
	public static TileEntityType<IronboundChestTileEntity> ironboundChestTileEntityType;

	
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		@SubscribeEvent
		  public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
			// you probably don't need a datafixer --> null should be fine
			woodChestTileEntityType = TileEntityType.Builder
				.create(WoodChestTileEntity::new, TreasureBlocks.WOOD_CHEST)
				.build(null);
			woodChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.WOOD_CHEST_TE_ID);
		    event.getRegistry().register(woodChestTileEntityType);
		    
			crateChestTileEntityType = TileEntityType.Builder
				.create(CrateChestTileEntity::new, TreasureBlocks.CRATE_CHEST)
				.build(null);
			crateChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.CRATE_CHEST_TE_ID);
		    event.getRegistry().register(crateChestTileEntityType);
		    
			moldyCrateChestTileEntityType = TileEntityType.Builder
					.create(MoldyCrateChestTileEntity::new, TreasureBlocks.MOLDY_CRATE_CHEST)
					.build(null);
			moldyCrateChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.MOLDY_CRATE_CHEST_TE_ID);
			event.getRegistry().register(moldyCrateChestTileEntityType);
			
			ironboundChestTileEntityType = TileEntityType.Builder
					.create(IronboundChestTileEntity::new, TreasureBlocks.IRONBOUND_CHEST)
					.build(null);
			ironboundChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.IRONBOUND_CHEST_TE_ID);
		    event.getRegistry().register(ironboundChestTileEntityType);
		  }
	}
}
