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
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureItemGroups;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;
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
	
	public static List<Block> BLOCKS = new ArrayList<>(100);
	public static final Set<BlockItem> ITEM_BLOCKS = new HashSet<>();
	
	static {
		VoxelShape vanillaChestShape = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 14.0D, 14.0D, 14.0D);
		VoxelShape[] standardChestBounds = new VoxelShape[] {vanillaChestShape, vanillaChestShape, vanillaChestShape, vanillaChestShape};
		
		WOOD_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.WOOD_CHEST_ID, WoodChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.COMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.5F))
				.setBounds(standardChestBounds);
		
		CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CRATE_CHEST_ID, CrateChestTileEntity.class,
				TreasureChestTypes.CRATE, Rarity.UNCOMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.5F));
		
		MOLDY_CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.MOLDY_CRATE_CHEST_ID, MoldyCrateChestTileEntity.class,
				TreasureChestTypes.CRATE, Rarity.COMMON, Block.Properties.create(Material.WOOD, MaterialColor.WOOD).hardnessAndResistance(2.0F));
		
		IRONBOUND_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.IRONBOUND_CHEST_ID, IronboundChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.UNCOMMON, Block.Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(3.0F))
				.setBounds(standardChestBounds);
		
		BLOCKS.add(WOOD_CHEST);
		BLOCKS.add(CRATE_CHEST);
		BLOCKS.add(MOLDY_CRATE_CHEST);
		BLOCKS.add(IRONBOUND_CHEST);
	}
	
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {


		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();
			for (Block b : BLOCKS) {
				registry.register(b);
			}
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
		}
	}
}
