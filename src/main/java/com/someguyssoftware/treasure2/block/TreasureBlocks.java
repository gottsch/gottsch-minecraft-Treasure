/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItemGroups;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CompressorChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldSkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;
import com.someguyssoftware.treasure2.tileentity.SkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SpiderChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;
import com.someguyssoftware.treasure2.tileentity.VikingChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WitherChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WoodChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Mark Gottschling on Aug 17, 2020
 *
 */
public class TreasureBlocks {

	public static final Block WOOD_CHEST;
	public static final Block CRATE_CHEST;
	public static final Block MOLDY_CRATE_CHEST;
	public static final Block IRONBOUND_CHEST;
	public static final Block PIRATE_CHEST;
	public static final Block IRON_STRONGBOX;
	public static final Block GOLD_STRONGBOX;
	public static final Block DREAD_PIRATE_CHEST;
	public static final Block WITHER_CHEST;
	public static final Block WITHER_CHEST_TOP;
	public static final Block SAFE;
	public static final Block COMPRESSOR_CHEST;
	public static final Block SKULL_CHEST;
	public static final Block GOLD_SKULL_CHEST;
	public static final Block CAULDRON_CHEST;
	public static final Block SPIDER_CHEST;
	public static final Block VIKING_CHEST;

	public static List<Block> BLOCKS = new ArrayList<>(100);
	public static final Set<BlockItem> ITEM_BLOCKS = new HashSet<>();

	static {
		/** initialize chest shapes/bounds */
		// standard chest bounds
		VoxelShape vanillaChestShape = Block.makeCuboidShape(1, 0, 1, 15, 14, 15);
		VoxelShape[] standardChestBounds = new VoxelShape[] {vanillaChestShape, vanillaChestShape, vanillaChestShape, vanillaChestShape};

		// banded chest bounds
		VoxelShape[] bandedBounds = new VoxelShape[4];
		bandedBounds[0] = Block.makeCuboidShape(1.5, 0, 2, 14, 12.5, 14);
		bandedBounds[1] = Block.makeCuboidShape(2, 0, 1.5, 14, 14, 12.5);
		bandedBounds[2] = bandedBounds[0];
		bandedBounds[3] = bandedBounds[1];
		
		// safe chest bounds
		VoxelShape[] safeBounds = new VoxelShape[4];
		safeBounds[0] = Block.makeCuboidShape(2, 0, 2, 14, 13, 14);
		safeBounds[1] = safeBounds[0];
		safeBounds[2] = safeBounds[0];
		safeBounds[3] = safeBounds[0];

		// strongbox bounds
		VoxelShape[] strongboxBounds = new VoxelShape[4];
		strongboxBounds[0] = Block.makeCuboidShape(3, 0, 4, 13, 7.5, 12); // S
		strongboxBounds[1] = Block.makeCuboidShape(4, 0, 3, 12, 7.5, 13); // W
		strongboxBounds[2] = Block.makeCuboidShape(3, 0, 4, 13, 7.5, 12); // N
		strongboxBounds[3] = Block.makeCuboidShape(4, 0, 3, 12, 7.5, 13); // E

		// compressor chest bounds
		VoxelShape[] compressorChestBounds = new VoxelShape[4];
		compressorChestBounds[0] = Block.makeCuboidShape(4.5, 0, 4.5, 11.5, 7, 11.5); // S
		compressorChestBounds[1] = compressorChestBounds[0]; // W
		compressorChestBounds[2] = compressorChestBounds[0]; // N
		compressorChestBounds[3] = compressorChestBounds[0]; // E

		// skull chest bounds
		VoxelShape[] skullChestBounds = new VoxelShape[4];
		skullChestBounds[0] = Block.makeCuboidShape(4, 0, 4, 12, 9.092, 12); // S
		skullChestBounds[1] = skullChestBounds[0]; // W
		skullChestBounds[2] = skullChestBounds[0]; // N
		skullChestBounds[3] = skullChestBounds[0]; // E

		// spider chest bounds
		VoxelShape spiderChestShape = Block.makeCuboidShape(1, 0, 0, 15, 16, 16);
		VoxelShape[] spiderChestBounds = new VoxelShape[] {
				spiderChestShape, spiderChestShape, spiderChestShape, spiderChestShape
				};

		// viking chest bounds
		VoxelShape[] vikingChestBounds = new VoxelShape[] {
				Block.makeCuboidShape(1, 0, 3, 15, 15, 13), // S
				Block.makeCuboidShape(3, 0, 1, 13, 15, 15), // W
				Block.makeCuboidShape(1, 0, 3, 15, 15, 13), // N
				Block.makeCuboidShape(3, 0, 1, 13, 15, 15)
		};
		
		// wither chest bounds
		VoxelShape[] witherChestBounds = new VoxelShape[4];
		witherChestBounds[0] = Block.makeCuboidShape(1, 0, 1, 15, 16, 15);
		witherChestBounds[1] = witherChestBounds[0]; // W
		witherChestBounds[2] = witherChestBounds[0]; // N
		witherChestBounds[3] = witherChestBounds[0]; // E

		/** initialize chests */
		WOOD_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.WOOD_CHEST_ID, WoodChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.COMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.5F))
				.setBounds(standardChestBounds);

		CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CRATE_CHEST_ID, CrateChestTileEntity.class,
				TreasureChestTypes.CRATE, Rarity.UNCOMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.5F))
				.setBounds(standardChestBounds);

		MOLDY_CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.MOLDY_CRATE_CHEST_ID, MoldyCrateChestTileEntity.class,
				TreasureChestTypes.CRATE, Rarity.COMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F))
				.setBounds(standardChestBounds);

		IRONBOUND_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.IRONBOUND_CHEST_ID, IronboundChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.UNCOMMON, Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(3.0F))
				.setBounds(bandedBounds);

		PIRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.PIRATE_CHEST_ID, PirateChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.SCARCE, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(3.0F))
				.setBounds(standardChestBounds);

		SAFE = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SAFE_ID, SafeTileEntity.class,
				TreasureChestTypes.SAFE, Rarity.RARE, Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(4.0F)).setBounds(safeBounds);

		IRON_STRONGBOX = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.IRON_STRONGBOX_ID,
				IronStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.SCARCE, Block.Properties.create(Material.IRON, MaterialColor.IRON)
				.hardnessAndResistance(4.0F)).setBounds(strongboxBounds);

		GOLD_STRONGBOX = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.GOLD_STRONGBOX_ID,
				GoldStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.RARE, Block.Properties.create(Material.IRON, MaterialColor.IRON)
				.hardnessAndResistance(4.0F)).setBounds(strongboxBounds);

		DREAD_PIRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.DREAD_PIRATE_CHEST_ID,
				DreadPirateChestTileEntity.class, TreasureChestTypes.STANDARD, Rarity.EPIC, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(4.0F)).setBounds(standardChestBounds);

		COMPRESSOR_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.COMPRESSOR_CHEST_ID,
				CompressorChestTileEntity.class, TreasureChestTypes.COMPRESSOR, Rarity.EPIC, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(3.0F)).setBounds(compressorChestBounds);		

		SKULL_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SKULL_CHEST_ID, SkullChestTileEntity.class,
				TreasureChestTypes.SKULL, Rarity.SCARCE, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(3.0F)).setBounds(skullChestBounds);

		GOLD_SKULL_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.GOLD_SKULL_CHEST_ID,
				GoldSkullChestTileEntity.class, TreasureChestTypes.SKULL, Rarity.RARE, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(3.0F)).setBounds(skullChestBounds);

		CAULDRON_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CAULDRON_CHEST_ID,
				CauldronChestTileEntity.class, TreasureChestTypes.TOP_SPLIT, Rarity.EPIC, Block.Properties.create(Material.IRON, MaterialColor.IRON)
				.hardnessAndResistance(3.0F)).setBounds(spiderChestBounds);

		SPIDER_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SPIDER_CHEST_ID,
				SpiderChestTileEntity.class, TreasureChestTypes.SINGLE_STANDARD, Rarity.RARE, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(3.0F)).setBounds(standardChestBounds);

		VIKING_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.VIKING_CHEST_ID,
				VikingChestTileEntity.class, TreasureChestTypes.VIKING, Rarity.UNCOMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(3.0F)).setBounds(vikingChestBounds);

		// TODO WITHER CHEST
		WITHER_CHEST = new WitherChestBlock(Treasure.MODID, TreasureConfig.ChestID.WITHER_CHEST_ID, 
				WitherChestTileEntity.class,	TreasureChestTypes.ARMOIRE, Rarity.SCARCE, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
						.hardnessAndResistance(2.5F)).setBounds(witherChestBounds);

		WITHER_CHEST_TOP = new WitherChestTopBlock(Treasure.MODID, TreasureConfig.ChestID.WITHER_CHEST_TOP_ID, Block.Properties.create(Material.WOOD, MaterialColor.WOOD)
				.hardnessAndResistance(2.5F));

		// TODO MIMICS

		// TODO GRAVESTONES

		// TODO ORES

		// TODO WISHING WELL BLOCKS

		// TODO WITHER BIOME BLOCKS

		// TODO FALLING BLOCKS

		// TODO PAINTINGS

		// TODO PROXIMITY BLOCKS

		// add blocks to the list
		BLOCKS.add(WOOD_CHEST);
		BLOCKS.add(CRATE_CHEST);
		BLOCKS.add(MOLDY_CRATE_CHEST);
		BLOCKS.add(IRONBOUND_CHEST);
		BLOCKS.add(PIRATE_CHEST);
		BLOCKS.add(SAFE);
		BLOCKS.add(IRON_STRONGBOX);
		BLOCKS.add(GOLD_STRONGBOX);
		BLOCKS.add(DREAD_PIRATE_CHEST);
		BLOCKS.add(COMPRESSOR_CHEST);
		BLOCKS.add(SKULL_CHEST);
		BLOCKS.add(GOLD_SKULL_CHEST);
		BLOCKS.add(CAULDRON_CHEST);
		BLOCKS.add(SPIDER_CHEST);
		BLOCKS.add(VIKING_CHEST);
//		BLOCKS.add(WITHER_CHEST); // TODO this will have to be removed later when chests are registered by rarity unless using a table that flags wither chest
		BLOCKS.add(WITHER_CHEST_TOP);
	}

	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (Block block : BLOCKS) {
				registry.register(block);
				// map chest blocks to the rarity map
				if (block instanceof StandardChestBlock) {
					TreasureData.CHESTS_BY_RARITY.put(((StandardChestBlock) block).getRarity(), block);
					TreasureData.CHESTS_BY_NAME.put(block.getRegistryName().getPath(), block);
				}
			}
			// special case registry
			registry.register(WITHER_CHEST);
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			for (Block b : BLOCKS) {
				BlockItem itemBlock = new BlockItem(b, new Item.Properties().group(TreasureItemGroups.MOD_ITEM_GROUP));
				final ResourceLocation registryName = Preconditions.checkNotNull(b.getRegistryName(),
						"Block %s has null registry name", b);
				registry.register(itemBlock.setRegistryName(registryName));
				ITEM_BLOCKS.add(itemBlock);
			}
			
			// special case registry
			BlockItem itemBlock = new BlockItem(WITHER_CHEST, new Item.Properties().group(TreasureItemGroups.MOD_ITEM_GROUP));
			final ResourceLocation registryName = Preconditions.checkNotNull(WITHER_CHEST.getRegistryName(),
					"Block %s has null registry name", WITHER_CHEST);
			registry.register(itemBlock.setRegistryName(registryName));
			ITEM_BLOCKS.add(itemBlock);
		}
	}
}
