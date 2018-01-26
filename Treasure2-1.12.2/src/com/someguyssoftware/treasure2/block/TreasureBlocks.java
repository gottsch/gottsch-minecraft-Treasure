/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WoodChestTileEntity;

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
	
	// CHESTS
	public static final Block WOOD_CHEST;
	public static final Block IRONBOUND_CHEST;
	public static final Block PIRATE_CHEST;
	// chest holder
	public static Multimap<Rarity, Block> chests;
	
	// initialize chests
	static {
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
		AxisAlignedBB[] bounds = new 	AxisAlignedBB[4];
		bounds[0] = vanilla;
		bounds[1] = vanilla;
		bounds[2] = vanilla;
		bounds[3] = vanilla;

		WOOD_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.WOOD_CHEST_ID, 
				WoodChestTileEntity.class,
				TreasureChestTypes.STANDARD)
				.setBounds(bounds)
				.setHardness(2.5F);

		IRONBOUND_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.IRONBOUND_CHEST_ID, 
				IronboundChestTileEntity.class,
				TreasureChestTypes.STANDARD)
				.setBounds(bounds)
				.setHardness(3.0F);

		PIRATE_CHEST = new TreasureChestBlock(
				Treasure.MODID,
				TreasureConfig.PIRATE_CHEST_ID,
				PirateChestTileEntity.class,
				TreasureChestTypes.STANDARD)
				.setBounds(bounds)
				.setHardness(3.0F);

		// map the chests by rarity
		chests = ArrayListMultimap.create();
		chests.put(Rarity.COMMON, WOOD_CHEST);
		chests.put(Rarity.UNCOMMON, IRONBOUND_CHEST);
		chests.put(Rarity.SCARCE, PIRATE_CHEST);
		
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
					WOOD_CHEST,
					IRONBOUND_CHEST,
					PIRATE_CHEST
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
					new ItemBlock(WOOD_CHEST),
					new ItemBlock(IRONBOUND_CHEST),
					new ItemBlock(PIRATE_CHEST)
			};
			
			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}
			
			// register the tile entities
			GameRegistry.registerTileEntity(TreasureChestTileEntity.class, "treasureChestTileEntity");
			GameRegistry.registerTileEntity(WoodChestTileEntity.class, TreasureConfig.WOOD_CHEST_TE_ID);
			GameRegistry.registerTileEntity(IronboundChestTileEntity.class, TreasureConfig.IRONBOUND_CHEST_TE_ID);
			GameRegistry.registerTileEntity(PirateChestTileEntity.class, TreasureConfig.PIRATE_CHEST_TE_ID);
		}	
	}
}
