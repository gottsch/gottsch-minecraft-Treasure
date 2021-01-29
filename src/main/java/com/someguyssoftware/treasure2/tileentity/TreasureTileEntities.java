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
	public static TileEntityType<CrystalSkullChestTileEntity> crystalSkullChestTileEntityType;
	public static TileEntityType<CauldronChestTileEntity> cauldronChestTileEntityType;
	public static TileEntityType<SpiderChestTileEntity> spiderChestTileEntityType;
	public static TileEntityType<VikingChestTileEntity> vikingChestTileEntityType;
	public static TileEntityType<CardboardBoxTileEntity> cardboardBoxTileEntityType;
	public static TileEntityType<MilkCrateTileEntity> milkCrateTileEntityType;

//	public static TileEntityType<ProximitySpawnerTileEntity> proximityTileEntityType;
	public static TileEntityType<TreasureProximitySpawnerTileEntity> proximityTileEntityType;
	public static TileEntityType<MistEmitterTileEntity> mistEmitterTileEntityType;

	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
//			proximityTileEntityType = TileEntityType.Builder
//					.create(() -> new ProximitySpawnerTileEntity(proximityTileEntityType), TreasureBlocks.PROXIMITY_SPAWNER)
//					.build(null);
			proximityTileEntityType = TileEntityType.Builder
					.create(TreasureProximitySpawnerTileEntity::new, TreasureBlocks.PROXIMITY_SPAWNER)
					.build(null);
			proximityTileEntityType.setRegistryName(TreasureConfig.TileEntityID.PROXIMITY_SPAWNER_TE_ID);
			event.getRegistry().register(proximityTileEntityType);

			mistEmitterTileEntityType = TileEntityType.Builder
					.create(() -> new MistEmitterTileEntity(mistEmitterTileEntityType), TreasureBlocks.WITHER_SOUL_LOG)
					.build(null);
			mistEmitterTileEntityType.setRegistryName(TreasureConfig.TileEntityID.MIST_EMITTER_TE_ID);
			event.getRegistry().register(mistEmitterTileEntityType);

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

			crystalSkullChestTileEntityType = TileEntityType.Builder
					.create(CrystalSkullChestTileEntity::new, TreasureBlocks.CRYSTAL_SKULL_CHEST)
					.build(null);
			crystalSkullChestTileEntityType.setRegistryName(TreasureConfig.TileEntityID.CRYSTAL_SKULL_CHEST_TE_ID);
			event.getRegistry().register(crystalSkullChestTileEntityType);

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

			// cardboard box
			cardboardBoxTileEntityType = TileEntityType.Builder
					.create(CardboardBoxTileEntity::new, TreasureBlocks.CARDBOARD_BOX)
					.build(null);
			cardboardBoxTileEntityType.setRegistryName(TreasureConfig.TileEntityID.CARDBOARD_BOX_TE_ID);
			event.getRegistry().register(cardboardBoxTileEntityType);

			// milk crate
			milkCrateTileEntityType = TileEntityType.Builder
					.create(MilkCrateTileEntity::new, TreasureBlocks.MILK_CRATE)
					.build(null);
			milkCrateTileEntityType.setRegistryName(TreasureConfig.TileEntityID.MILK_CRATE_TE_ID);
			event.getRegistry().register(milkCrateTileEntityType);
		}
	}
}
