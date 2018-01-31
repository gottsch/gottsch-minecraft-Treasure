/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import net.minecraft.block.material.Material;
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
	
	// gravestone holder
	public static List<Block> gravestones;
	
	// GRAVESTONES
	public static final Block GRAVESTONE1_STONE;
	public static final Block GRAVESTONE1_COBBLESTONE;
	public static final Block GRAVESTONE1_MOSSY_COBBLESTONE;
	public static final Block GRAVESTONE1_POLISHED_GRANITE;
	public static final Block GRAVESTONE1_POLISHED_ANDESITE;
	public static final Block GRAVESTONE1_POLISHED_DIORITE;
	public static final Block GRAVESTONE1_OBSIDIAN;
	public static final Block GRAVESTONE2_STONE;
	public static final Block GRAVESTONE2_COBBLESTONE;
	public static final Block GRAVESTONE2_MOSSY_COBBLESTONE;
	public static final Block GRAVESTONE2_POLISHED_GRANITE;
	public static final Block GRAVESTONE2_POLISHED_ANDESITE;
	public static final Block GRAVESTONE2_POLISHED_DIORITE;
	public static final Block GRAVESTONE2_OBSIDIAN;
	
	// initialize blocks
	static {
		// chest bounds
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
		AxisAlignedBB[] bounds = new 	AxisAlignedBB[4];
		bounds[0] = vanilla; // S
		bounds[1] = vanilla; // W
		bounds[2] = vanilla; // N
		bounds[3] = vanilla; // E

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
		
		// gravestone bounds
		AxisAlignedBB[] gbs = new 	AxisAlignedBB[4];
		gbs[0] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 0.75D, 0.675D); // S
		gbs[1] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 0.75D, 0.875D); // W
		gbs[2] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 0.75D, 0.675D); // N
		gbs[3] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 0.75D, 0.875D); // E
		
		// Gravestones
		GRAVESTONE1_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_STONE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_COBBLESTONE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_OBSIDIAN_ID, Material.ROCK).setBounds(gbs);
		
		AxisAlignedBB[] gbs2 = new 	AxisAlignedBB[4];
		gbs2[0] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.125D, 0.675D); // S
		gbs2[1] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.125D, 0.875D); // W
		gbs2[2] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.125D, 0.675D); // N
		gbs2[3] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.125D, 0.875D); // E
		
		// Gravestones
		GRAVESTONE2_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_STONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_OBSIDIAN_ID, Material.ROCK).setBounds(gbs2);
		
		// add all the gravestones to the list
		gravestones = new ArrayList<>();
		gravestones.add(GRAVESTONE1_STONE);
		gravestones.add(GRAVESTONE1_COBBLESTONE);
		gravestones.add(GRAVESTONE1_MOSSY_COBBLESTONE);
		gravestones.add(GRAVESTONE1_POLISHED_ANDESITE);
		gravestones.add(GRAVESTONE1_POLISHED_DIORITE);
		gravestones.add(GRAVESTONE1_POLISHED_GRANITE);
		gravestones.add(GRAVESTONE1_OBSIDIAN);
		gravestones.add(GRAVESTONE2_STONE);
		gravestones.add(GRAVESTONE2_COBBLESTONE);
		gravestones.add(GRAVESTONE2_MOSSY_COBBLESTONE);
		gravestones.add(GRAVESTONE2_POLISHED_ANDESITE);
		gravestones.add(GRAVESTONE2_POLISHED_DIORITE);
		gravestones.add(GRAVESTONE2_POLISHED_GRANITE);
		gravestones.add(GRAVESTONE2_OBSIDIAN);
		
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
					PIRATE_CHEST,
					GRAVESTONE1_STONE,
					GRAVESTONE1_COBBLESTONE,
					GRAVESTONE1_MOSSY_COBBLESTONE,
					GRAVESTONE1_POLISHED_GRANITE,
					GRAVESTONE1_POLISHED_ANDESITE,
					GRAVESTONE1_POLISHED_DIORITE,
					GRAVESTONE1_OBSIDIAN,
					GRAVESTONE2_STONE,
					GRAVESTONE2_COBBLESTONE,
					GRAVESTONE2_MOSSY_COBBLESTONE,
					GRAVESTONE2_POLISHED_GRANITE,
					GRAVESTONE2_POLISHED_ANDESITE,
					GRAVESTONE2_POLISHED_DIORITE,
					GRAVESTONE2_OBSIDIAN
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
					// TODO update with ChestItemBlock so we can use addInformation() for display and mouse over  purposes.
					new ItemBlock(WOOD_CHEST),
					new ItemBlock(IRONBOUND_CHEST),
					new ItemBlock(PIRATE_CHEST),
					// TODO update with GravestonIetmBlock
					new ItemBlock(GRAVESTONE1_STONE),
					new ItemBlock(GRAVESTONE1_COBBLESTONE),
					new ItemBlock(GRAVESTONE1_MOSSY_COBBLESTONE),
					new ItemBlock(GRAVESTONE1_POLISHED_GRANITE),
					new ItemBlock(GRAVESTONE1_POLISHED_ANDESITE),
					new ItemBlock(GRAVESTONE1_POLISHED_DIORITE),
					new ItemBlock(GRAVESTONE1_OBSIDIAN),
					new ItemBlock(GRAVESTONE2_STONE),
					new ItemBlock(GRAVESTONE2_COBBLESTONE),
					new ItemBlock(GRAVESTONE2_MOSSY_COBBLESTONE),
					new ItemBlock(GRAVESTONE2_POLISHED_GRANITE),
					new ItemBlock(GRAVESTONE2_POLISHED_ANDESITE),
					new ItemBlock(GRAVESTONE2_POLISHED_DIORITE),
					new ItemBlock(GRAVESTONE2_OBSIDIAN)
			};
			
			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}
			
			// TODO for gravestone and anything with variants, probably need to use this method instead of the the register model method
//	        ModelLoader.setCustomModelResourceLocation(chestItem, type.ordinal(),
//	        		new ModelResourceLocation(chestItem.getRegistryName(), "variant=" + type.getName()));
	        
			// register the tile entities
			GameRegistry.registerTileEntity(TreasureChestTileEntity.class, "treasureChestTileEntity");
			GameRegistry.registerTileEntity(WoodChestTileEntity.class, TreasureConfig.WOOD_CHEST_TE_ID);
			GameRegistry.registerTileEntity(IronboundChestTileEntity.class, TreasureConfig.IRONBOUND_CHEST_TE_ID);
			GameRegistry.registerTileEntity(PirateChestTileEntity.class, TreasureConfig.PIRATE_CHEST_TE_ID);
		}	
	}
}
