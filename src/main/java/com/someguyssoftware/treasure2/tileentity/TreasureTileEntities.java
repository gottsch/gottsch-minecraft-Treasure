/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;

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
	public static TileEntityType<PirateChestTileEntity> pirateChestTileEntityType;
	public static TileEntityType<IronStrongboxTileEntity> ironStrongboxTileEntityType;
	public static TileEntityType<GoldStrongboxTileEntity> goldStrongboxTileEntityType;
	public static TileEntityType<DreadPirateChestTileEntity> dreadPirateChestTileEntityType;
	public static TileEntityType<WitherChestTileEntity> witherChestTileEntityType;
	public static TileEntityType<SafeTileEntity> safeTileEntityType;
	public static TileEntityType<CompressorChestTileEntity> compressorChestTileEntityType;
	public static TileEntityType<SkullChestTileEntity> skullChestTileEntityType;
	public static TileEntityType<GoldSkullChestTileEntity> goldSkullChestTileEntityType;
	public static TileEntityType<CauldronChestTileEntity> cauldronChestTileEntityType;
	public static TileEntityType<SpiderChestTileEntity> spiderChestTileEntityType;
	public static TileEntityType<VikingChestTileEntity> vikingChestTileEntityType;

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

			pirateChestTileEntityType = TileEntityType.Builder
					.create(PirateChestTileEntity::new, TreasureBlocks.PIRATE_CHEST)
					.build(null);
			pirateChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.PIRATE_CHEST_TE_ID);
			event.getRegistry().register(pirateChestTileEntityType);

			ironStrongboxTileEntityType = TileEntityType.Builder
					.create(IronStrongboxTileEntity::new, TreasureBlocks.IRON_STRONGBOX)
					.build(null);
			ironStrongboxTileEntityType.setRegistryName(TreasureConfig.TileEntityID.IRON_STRONGBOX_TE_ID);
			event.getRegistry().register(ironStrongboxTileEntityType);
			
			goldStrongboxTileEntityType = TileEntityType.Builder
					.create(GoldStrongboxTileEntity::new, TreasureBlocks.GOLD_STRONGBOX)
					.build(null);
			goldStrongboxTileEntityType.setRegistryName(TreasureConfig.TileEntityID.GOLD_STRONGBOX_TE_ID);
			event.getRegistry().register(goldStrongboxTileEntityType);

			dreadPirateChestTileEntityType = TileEntityType.Builder
					.create(DreadPirateChestTileEntity::new, TreasureBlocks.DREAD_PIRATE_CHEST)
					.build(null);
			dreadPirateChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.DREAD_PIRATE_CHEST_TE_ID);
			event.getRegistry().register(dreadPirateChestTileEntityType);
			//wither
			witherChestTileEntityType = TileEntityType.Builder
					.create(WitherChestTileEntity::new, TreasureBlocks.WITHER_CHEST)
					.build(null);
			witherChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.WITHER_CHEST_TE_ID);
			event.getRegistry().register(witherChestTileEntityType);

			safeTileEntityType = TileEntityType.Builder
					.create(SafeTileEntity::new, TreasureBlocks.SAFE)
					.build(null);
			safeTileEntityType.setRegistryName(TreasureConfig.TileEntityID.SAFE_TE_ID);
			event.getRegistry().register(safeTileEntityType);

			compressorChestTileEntityType = TileEntityType.Builder
					.create(CompressorChestTileEntity::new, TreasureBlocks.COMPRESSOR_CHEST)
					.build(null);
			compressorChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.COMPRESSOR_CHEST_TE_ID);
			event.getRegistry().register(compressorChestTileEntityType);

			skullChestTileEntityType = TileEntityType.Builder
					.create(SkullChestTileEntity::new, TreasureBlocks.SKULL_CHEST)
					.build(null);
			skullChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.SKULL_CHEST_TE_ID);
			event.getRegistry().register(skullChestTileEntityType);

			goldSkullChestTileEntityType = TileEntityType.Builder
					.create(GoldSkullChestTileEntity::new, TreasureBlocks.GOLD_SKULL_CHEST)
					.build(null);
			goldSkullChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.GOLD_SKULL_CHEST_TE_ID);
			event.getRegistry().register(goldSkullChestTileEntityType);
			
			cauldronChestTileEntityType = TileEntityType.Builder
					.create(CauldronChestTileEntity::new, TreasureBlocks.CAULDRON_CHEST)
					.build(null);
			cauldronChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.CAULDRON_CHEST_TE_ID);
			event.getRegistry().register(cauldronChestTileEntityType);
			
			// spider
			spiderChestTileEntityType = TileEntityType.Builder
					.create(SpiderChestTileEntity::new, TreasureBlocks.SPIDER_CHEST)
					.build(null);
			spiderChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.SPIDER_CHEST_TE_ID);
			event.getRegistry().register(spiderChestTileEntityType);
			
			// viking
			vikingChestTileEntityType = TileEntityType.Builder
					.create(VikingChestTileEntity::new, TreasureBlocks.VIKING_CHEST)
					.build(null);
			vikingChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.VIKING_CHEST_TE_ID);
			event.getRegistry().register(vikingChestTileEntityType);
		}
	}
}
