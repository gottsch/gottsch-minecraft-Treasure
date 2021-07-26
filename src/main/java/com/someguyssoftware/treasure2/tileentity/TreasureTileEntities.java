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
	 * These are not *real* constants even though they have a name format like constants because of the above restriction.
	 */
	public static TileEntityType<WoodChestTileEntity> WOOD_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<CrateChestTileEntity> CRATE_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<MoldyCrateChestTileEntity> MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<IronboundChestTileEntity> IRONBOUND_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<PirateChestTileEntity> PIRATE_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<IronStrongboxTileEntity> IRON_STRONGBOX_TILE_ENTITY_TYPE;
	public static TileEntityType<GoldStrongboxTileEntity> GOLD_STRONGBOX_TILE_ENTITY_TYPE;
	public static TileEntityType<DreadPirateChestTileEntity> DREAD_PIRATE_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<WitherChestTileEntity> WITHER_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<SafeTileEntity> SAFE_TILE_ENTITY_TYPE;
	public static TileEntityType<CompressorChestTileEntity> COMPRESSOR_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<SkullChestTileEntity> SKULL_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<GoldSkullChestTileEntity> GOLD_SKULL_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<CrystalSkullChestTileEntity> CRYSTAL_SKULL_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<CauldronChestTileEntity> CAULDRON_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<SpiderChestTileEntity> SPIDER_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<VikingChestTileEntity> VIKING_CHEST_TILE_ENTITY_TYPE;
	public static TileEntityType<CardboardBoxTileEntity> CARDBOARD_BOX_TILE_ENTITY_TYPE;
	public static TileEntityType<MilkCrateTileEntity> MILK_CRATE_TILE_ENTITYT_TYPE;

//	public static TileEntityType<ProximitySpawnerTileEntity> proximityTileEntityType;
	public static TileEntityType<TreasureProximitySpawnerTileEntity> PROXIMITY_TILE_ENTITY_TYPE;
	public static TileEntityType<GravestoneProximitySpawnerTileEntity> GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE;
	public static TileEntityType<MistEmitterTileEntity> MIST_EMITTER_TILE_ENTITY_TYPE;

	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void onTileEntityTypeRegistration(final RegistryEvent.Register<TileEntityType<?>> event) {
//			proximityTileEntityType = TileEntityType.Builder
//					.of(() -> new ProximitySpawnerTileEntity(proximityTileEntityType), TreasureBlocks.PROXIMITY_SPAWNER)
//					.build(null);
			PROXIMITY_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(TreasureProximitySpawnerTileEntity::new, TreasureBlocks.PROXIMITY_SPAWNER)
					.build(null);
			PROXIMITY_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.PROXIMITY_SPAWNER_TE_ID);
			event.getRegistry().register(PROXIMITY_TILE_ENTITY_TYPE);

			GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(GravestoneProximitySpawnerTileEntity::new, 
							TreasureBlocks.GRAVESTONE1_SPAWNER_STONE,
							TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE,
							TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN)
					.build(null);
			GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.GRAVESTONE_PROXIMITY_SPAWNER_TE_ID);
			event.getRegistry().register(GRAVESTONE_PROXIMITY_SPAWNER_TILE_ENTITY_TYPE);
			
			MIST_EMITTER_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(() -> new MistEmitterTileEntity(MIST_EMITTER_TILE_ENTITY_TYPE), TreasureBlocks.WITHER_SOUL_LOG)
					.build(null);
			MIST_EMITTER_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.MIST_EMITTER_TE_ID);
			event.getRegistry().register(MIST_EMITTER_TILE_ENTITY_TYPE);

			// you probably don't need a datafixer --> null should be fine
			WOOD_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(WoodChestTileEntity::new, TreasureBlocks.WOOD_CHEST)
					.build(null);
			WOOD_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.WOOD_CHEST_TE_ID);
			event.getRegistry().register(WOOD_CHEST_TILE_ENTITY_TYPE);

			CRATE_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(CrateChestTileEntity::new, TreasureBlocks.CRATE_CHEST)
					.build(null);
			CRATE_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.CRATE_CHEST_TE_ID);
			event.getRegistry().register(CRATE_CHEST_TILE_ENTITY_TYPE);

			MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(MoldyCrateChestTileEntity::new, TreasureBlocks.MOLDY_CRATE_CHEST)
					.build(null);
			MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.MOLDY_CRATE_CHEST_TE_ID);
			event.getRegistry().register(MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE);

			IRONBOUND_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(IronboundChestTileEntity::new, TreasureBlocks.IRONBOUND_CHEST)
					.build(null);
			IRONBOUND_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.IRONBOUND_CHEST_TE_ID);
			event.getRegistry().register(IRONBOUND_CHEST_TILE_ENTITY_TYPE);

			PIRATE_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(PirateChestTileEntity::new, TreasureBlocks.PIRATE_CHEST)
					.build(null);
			PIRATE_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.PIRATE_CHEST_TE_ID);
			event.getRegistry().register(PIRATE_CHEST_TILE_ENTITY_TYPE);

			IRON_STRONGBOX_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(IronStrongboxTileEntity::new, TreasureBlocks.IRON_STRONGBOX)
					.build(null);
			IRON_STRONGBOX_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.IRON_STRONGBOX_TE_ID);
			event.getRegistry().register(IRON_STRONGBOX_TILE_ENTITY_TYPE);

			GOLD_STRONGBOX_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(GoldStrongboxTileEntity::new, TreasureBlocks.GOLD_STRONGBOX)
					.build(null);
			GOLD_STRONGBOX_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.GOLD_STRONGBOX_TE_ID);
			event.getRegistry().register(GOLD_STRONGBOX_TILE_ENTITY_TYPE);

			DREAD_PIRATE_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(DreadPirateChestTileEntity::new, TreasureBlocks.DREAD_PIRATE_CHEST)
					.build(null);
			DREAD_PIRATE_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.DREAD_PIRATE_CHEST_TE_ID);
			event.getRegistry().register(DREAD_PIRATE_CHEST_TILE_ENTITY_TYPE);
			
			//wither
			WITHER_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(WitherChestTileEntity::new, TreasureBlocks.WITHER_CHEST)
					.build(null);
			WITHER_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.WITHER_CHEST_TE_ID);
			event.getRegistry().register(WITHER_CHEST_TILE_ENTITY_TYPE);

			SAFE_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(SafeTileEntity::new, TreasureBlocks.SAFE)
					.build(null);
			SAFE_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.SAFE_TE_ID);
			event.getRegistry().register(SAFE_TILE_ENTITY_TYPE);

			COMPRESSOR_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(CompressorChestTileEntity::new, TreasureBlocks.COMPRESSOR_CHEST)
					.build(null);
			COMPRESSOR_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.COMPRESSOR_CHEST_TE_ID);
			event.getRegistry().register(COMPRESSOR_CHEST_TILE_ENTITY_TYPE);

			SKULL_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(SkullChestTileEntity::new, TreasureBlocks.SKULL_CHEST)
					.build(null);
			SKULL_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.SKULL_CHEST_TE_ID);
			event.getRegistry().register(SKULL_CHEST_TILE_ENTITY_TYPE);

			GOLD_SKULL_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(GoldSkullChestTileEntity::new, TreasureBlocks.GOLD_SKULL_CHEST)
					.build(null);
			GOLD_SKULL_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.GOLD_SKULL_CHEST_TE_ID);
			event.getRegistry().register(GOLD_SKULL_CHEST_TILE_ENTITY_TYPE);

			CRYSTAL_SKULL_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(CrystalSkullChestTileEntity::new, TreasureBlocks.CRYSTAL_SKULL_CHEST)
					.build(null);
			CRYSTAL_SKULL_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.CRYSTAL_SKULL_CHEST_TE_ID);
			event.getRegistry().register(CRYSTAL_SKULL_CHEST_TILE_ENTITY_TYPE);

			CAULDRON_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(CauldronChestTileEntity::new, TreasureBlocks.CAULDRON_CHEST)
					.build(null);
			CAULDRON_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.CAULDRON_CHEST_TE_ID);
			event.getRegistry().register(CAULDRON_CHEST_TILE_ENTITY_TYPE);

			// spider
			SPIDER_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(SpiderChestTileEntity::new, TreasureBlocks.SPIDER_CHEST)
					.build(null);
			SPIDER_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.SPIDER_CHEST_TE_ID);
			event.getRegistry().register(SPIDER_CHEST_TILE_ENTITY_TYPE);

			// viking
			VIKING_CHEST_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(VikingChestTileEntity::new, TreasureBlocks.VIKING_CHEST)
					.build(null);
			VIKING_CHEST_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.VIKING_CHEST_TE_ID);
			event.getRegistry().register(VIKING_CHEST_TILE_ENTITY_TYPE);

			// cardboard box
			CARDBOARD_BOX_TILE_ENTITY_TYPE = TileEntityType.Builder
					.of(CardboardBoxTileEntity::new, TreasureBlocks.CARDBOARD_BOX)
					.build(null);
			CARDBOARD_BOX_TILE_ENTITY_TYPE.setRegistryName(TreasureConfig.TileEntityID.CARDBOARD_BOX_TE_ID);
			event.getRegistry().register(CARDBOARD_BOX_TILE_ENTITY_TYPE);

			// milk crate
			MILK_CRATE_TILE_ENTITYT_TYPE = TileEntityType.Builder
					.of(MilkCrateTileEntity::new, TreasureBlocks.MILK_CRATE)
					.build(null);
			MILK_CRATE_TILE_ENTITYT_TYPE.setRegistryName(TreasureConfig.TileEntityID.MILK_CRATE_TE_ID);
			event.getRegistry().register(MILK_CRATE_TILE_ENTITYT_TYPE);
		}
	}
}
