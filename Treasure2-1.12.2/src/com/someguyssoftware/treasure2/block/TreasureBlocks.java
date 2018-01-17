/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.tileentity.TestChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureBlocks {
	// TODO register chest blocks
	public static final Block TEST_CHEST = new TestChest().setRegistryName(
			Treasure.MODID, "testChest").
			setUnlocalizedName("testChest");
	
	// CHESTS
	public static final Block WOODEN_CHEST;
	
	// initialize chests
	static {
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
		AxisAlignedBB[] bounds = new 	AxisAlignedBB[4];
		bounds[0] = vanilla;
		bounds[1] = vanilla;
		bounds[2] = vanilla;
		bounds[3] = vanilla;
		
	WOODEN_CHEST = new TreasureChestBlock(
			Treasure.MODID, 
			TreasureConfig.WOODEN_CHEST_ID, 
			TreasureChestTileEntity.class,
			TreasureChestTypes.STANDARD)
			.setBounds(bounds)
			.setHardness(2.5F);
	}
			
	
	/**
	 * 
	 * @author Mark Gottschling onJan 10, 2018
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID)
	public static class RegistrationHandler {
		public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();
		
		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			final Block[] blocks = {
					WOODEN_CHEST,
//					TEST_CHEST
			};
			registry.registerAll(blocks);			
		}
		
		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			final ItemBlock[] items = {
					new ItemBlock(WOODEN_CHEST),
					new ItemBlock(TEST_CHEST)
			};
			
			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}
			
			// register the tile entities
			GameRegistry.registerTileEntity(TestChestTileEntity.class, "testChest");
			GameRegistry.registerTileEntity(TreasureChestTileEntity.class, "treasureChestTileEntity");
		}	
	}
}
