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
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Fogs;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.TreasureChestItemBlock;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;
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
	public static final Block CRATE_CHEST;
	public static final Block MOLDY_CRATE_CHEST;
	public static final Block IRONBOUND_CHEST;
	public static final Block PIRATE_CHEST;
	public static final Block IRON_STRONGBOX;
	public static final Block GOLD_STRONGBOX;
	public static final Block SAFE;
	public static final Block DREAD_PIRATE_CHEST;

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
	public static final Block GRAVESTONE3_STONE;
	public static final Block GRAVESTONE3_COBBLESTONE;
	public static final Block GRAVESTONE3_MOSSY_COBBLESTONE;
	public static final Block GRAVESTONE3_POLISHED_GRANITE;
	public static final Block GRAVESTONE3_POLISHED_ANDESITE;
	public static final Block GRAVESTONE3_POLISHED_DIORITE;
	public static final Block GRAVESTONE3_OBSIDIAN;
	public static final Block SKULL_CROSSBONES;

	// other
	public static final Block WISHING_WELL_BLOCK;
	public static final FogBlock FOG_BLOCK;
	public static final FogBlock HIGH_FOG_BLOCK;
	public static final FogBlock MED_FOG_BLOCK;
	public static final FogBlock LOW_FOG_BLOCK;

	// initialize blocks
	static {
		// standard chest bounds
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
		AxisAlignedBB[] stdChestBounds = new AxisAlignedBB[4];
		stdChestBounds[0] = vanilla; // S
		stdChestBounds[1] = vanilla; // W
		stdChestBounds[2] = vanilla; // N
		stdChestBounds[3] = vanilla; // E

		WOOD_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.WOOD_CHEST_ID, 
				WoodChestTileEntity.class,
				TreasureChestTypes.STANDARD,
				Rarity.COMMON)
				.setBounds(stdChestBounds)
				.setHardness(2.5F);

		IRONBOUND_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.IRONBOUND_CHEST_ID, 
				IronboundChestTileEntity.class,
				TreasureChestTypes.STANDARD, 
				Rarity.UNCOMMON)
				.setBounds(stdChestBounds)
				.setHardness(3.0F);

		PIRATE_CHEST = new TreasureChestBlock(
				Treasure.MODID,
				TreasureConfig.PIRATE_CHEST_ID,
				PirateChestTileEntity.class,
				TreasureChestTypes.STANDARD, 
				Rarity.SCARCE)
				.setBounds(stdChestBounds)
				.setHardness(3.0F);

		CRATE_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.CRATE_CHEST_ID, 
				CrateChestTileEntity.class,
				TreasureChestTypes.CRATE,
				Rarity.UNCOMMON)
				.setBounds(stdChestBounds)
				.setHardness(2.5F);

		MOLDY_CRATE_CHEST = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.MOLDY_CRATE_CHEST_ID, 
				MoldyCrateChestTileEntity.class,
				TreasureChestTypes.CRATE,
				Rarity.COMMON)
				.setBounds(stdChestBounds)
				.setHardness(2.0F);

		// safe chest bounds
		AxisAlignedBB[] safeBounds = new AxisAlignedBB[4];
		safeBounds[0] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[1] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[2] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[3] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);

		SAFE = new TreasureChestBlock(
				Treasure.MODID, 
				TreasureConfig.SAFE_ID, 
				SafeTileEntity.class,
				TreasureChestTypes.SAFE,
				Rarity.RARE)
				.setBounds(safeBounds)
				.setHardness(4.0F);

		// create new strongbox bounds
		AxisAlignedBB[] strongboxBounds = new 	AxisAlignedBB[4];
		strongboxBounds[0] = new AxisAlignedBB(0.1875D, 0.0D, 0.25D, 0.825D, 0.5D, 0.75D); //S
		strongboxBounds[1] = new AxisAlignedBB(0.25D, 0.0D, 0.1875D, 0.75D, 0.5D, 0.825D); //W
		strongboxBounds[2] = new AxisAlignedBB(0.1875D, 0.0D, 0.25D, 0.825D, 0.5D, 0.75D); //N
		strongboxBounds[3] = new AxisAlignedBB(0.25D, 0.0D, 0.1875D, 0.75D, 0.5D, 0.825D); //E

		IRON_STRONGBOX = new TreasureChestBlock(
				Treasure.MODID,
				TreasureConfig.IRON_STRONGBOX_ID,
				IronStrongboxTileEntity.class,
				TreasureChestTypes.STRONGBOX, Rarity.SCARCE)
				.setChestGuiID(GuiHandler.STRONGBOX_CHEST_GUIID)
				.setBounds(strongboxBounds)
				.setHardness(4.0F);

		GOLD_STRONGBOX = new TreasureChestBlock(
				Treasure.MODID,
				TreasureConfig.GOLD_STRONGBOX_ID,
				GoldStrongboxTileEntity.class,
				TreasureChestTypes.STRONGBOX, Rarity.RARE)
				.setChestGuiID(GuiHandler.STRONGBOX_CHEST_GUIID)
				.setBounds(strongboxBounds)
				.setHardness(4.0F);

		DREAD_PIRATE_CHEST = new TreasureChestBlock(
				Treasure.MODID,
				TreasureConfig.DREAD_PIRATE_CHEST_ID,
				DreadPirateChestTileEntity.class,
				TreasureChestTypes.STANDARD, 
				Rarity.EPIC)
				.setBounds(stdChestBounds)
				.setHardness(4.0F);
		
		// map the chests by rarity
		chests = ArrayListMultimap.create();
		//		chests.put(((TreasureChestBlock)WOOD_CHEST).getRarity(), WOOD_CHEST);
		//		chests.put(Rarity.COMMON, CRATE_CHEST_MOLDY);
		//		chests.put(Rarity.UNCOMMON, CRATE_CHEST);
		//		chests.put(Rarity.UNCOMMON, IRONBOUND_CHEST);
		//		chests.put(Rarity.SCARCE, PIRATE_CHEST);

		// TEMP
		chests.put(Rarity.RARE, PIRATE_CHEST);
		chests.put(Rarity.EPIC, PIRATE_CHEST);

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
		gbs2[0] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.375D, 0.675D); // S
		gbs2[1] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.375D, 0.875D); // W
		gbs2[2] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.375D, 0.675D); // N
		gbs2[3] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.375D, 0.875D); // E

		// Gravestones
		GRAVESTONE2_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_STONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_OBSIDIAN_ID, Material.ROCK).setBounds(gbs2);

		GRAVESTONE3_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_STONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_OBSIDIAN_ID, Material.ROCK).setBounds(gbs2);

		// other
		SKULL_CROSSBONES = new SkullAndBonesBlock(
				Treasure.MODID, 
				TreasureConfig.SKULL_CROSSBONES_ID, 
				Material.ROCK)
				.setBounds(stdChestBounds);

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
		gravestones.add(GRAVESTONE3_STONE);
		gravestones.add(GRAVESTONE3_COBBLESTONE);
		gravestones.add(GRAVESTONE3_MOSSY_COBBLESTONE);
		gravestones.add(GRAVESTONE3_POLISHED_ANDESITE);
		gravestones.add(GRAVESTONE3_POLISHED_DIORITE);
		gravestones.add(GRAVESTONE3_POLISHED_GRANITE);
		gravestones.add(GRAVESTONE3_OBSIDIAN);
		gravestones.add(SKULL_CROSSBONES);

		// other
		WISHING_WELL_BLOCK = new WishingWellBlock(Treasure.MODID, TreasureConfig.WISHING_WELL_BLOCK_ID, Material.ROCK);
		FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.FOG_BLOCK_ID, TreasureItems.FOG).setFog(Fogs.FULL_FOG);
		HIGH_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.HIGH_FOG_BLOCK_ID, TreasureItems.FOG).setFog(Fogs.HIGH_FOG);
		MED_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.MED_FOG_BLOCK_ID, TreasureItems.FOG).setFog(Fogs.MEDIUM_FOG);
		LOW_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.LOW_FOG_BLOCK_ID, TreasureItems.FOG).setFog(Fogs.LOW_FOG);
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
					CRATE_CHEST,
					MOLDY_CRATE_CHEST,
					IRONBOUND_CHEST,
					PIRATE_CHEST,
					IRON_STRONGBOX,
					GOLD_STRONGBOX,
					SAFE,
					DREAD_PIRATE_CHEST,
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
					GRAVESTONE2_OBSIDIAN,
					GRAVESTONE3_STONE,
					GRAVESTONE3_COBBLESTONE,
					GRAVESTONE3_MOSSY_COBBLESTONE,
					GRAVESTONE3_POLISHED_GRANITE,
					GRAVESTONE3_POLISHED_ANDESITE,
					GRAVESTONE3_POLISHED_DIORITE,
					GRAVESTONE3_OBSIDIAN,
					SKULL_CROSSBONES,
					WISHING_WELL_BLOCK,
					FOG_BLOCK,
					HIGH_FOG_BLOCK,
					MED_FOG_BLOCK,
					LOW_FOG_BLOCK
			};
			registry.registerAll(blocks);	

			// map the block by rarity
			for (Block block : blocks) {
				if (block instanceof TreasureChestBlock) {
					chests.put(((TreasureChestBlock)block).getRarity(), block);
				}
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

			final ItemBlock[] items = {
					new TreasureChestItemBlock(WOOD_CHEST),
					new TreasureChestItemBlock(CRATE_CHEST),
					new TreasureChestItemBlock(MOLDY_CRATE_CHEST),
					new TreasureChestItemBlock(IRONBOUND_CHEST),
					new TreasureChestItemBlock(PIRATE_CHEST),
					new TreasureChestItemBlock(IRON_STRONGBOX),
					new TreasureChestItemBlock(GOLD_STRONGBOX),
					new TreasureChestItemBlock(SAFE),
					new TreasureChestItemBlock(DREAD_PIRATE_CHEST),
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
					new ItemBlock(GRAVESTONE2_OBSIDIAN),
					new ItemBlock(GRAVESTONE3_STONE),
					new ItemBlock(GRAVESTONE3_COBBLESTONE),
					new ItemBlock(GRAVESTONE3_MOSSY_COBBLESTONE),
					new ItemBlock(GRAVESTONE3_POLISHED_GRANITE),
					new ItemBlock(GRAVESTONE3_POLISHED_ANDESITE),
					new ItemBlock(GRAVESTONE3_POLISHED_DIORITE),
					new ItemBlock(GRAVESTONE3_OBSIDIAN),
					new ItemBlock(SKULL_CROSSBONES),
					new ItemBlock(WISHING_WELL_BLOCK),
					new ItemBlock(FOG_BLOCK),
					new ItemBlock(HIGH_FOG_BLOCK),
					new ItemBlock(MED_FOG_BLOCK),
					new ItemBlock(LOW_FOG_BLOCK)
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
			//			GameRegistry.registerTileEntity(AbstractTreasureChestTileEntity.class, "treasureChestTileEntity");
			GameRegistry.registerTileEntity(WoodChestTileEntity.class, TreasureConfig.WOOD_CHEST_TE_ID);
			GameRegistry.registerTileEntity(CrateChestTileEntity.class, TreasureConfig.CRATE_CHEST_TE_ID);
			GameRegistry.registerTileEntity(MoldyCrateChestTileEntity.class, TreasureConfig.MOLDY_CRATE_CHEST_TE_ID);
			GameRegistry.registerTileEntity(IronboundChestTileEntity.class, TreasureConfig.IRONBOUND_CHEST_TE_ID);
			GameRegistry.registerTileEntity(PirateChestTileEntity.class, TreasureConfig.PIRATE_CHEST_TE_ID);
			GameRegistry.registerTileEntity(IronStrongboxTileEntity.class, TreasureConfig.IRON_STRONGBOX_TE_ID);
			GameRegistry.registerTileEntity(GoldStrongboxTileEntity.class, TreasureConfig.GOLD_STRONGBOX_TE_ID);
			GameRegistry.registerTileEntity(SafeTileEntity.class, TreasureConfig.SAFE_TE_ID);
			GameRegistry.registerTileEntity(DreadPirateChestTileEntity.class, TreasureConfig.DREAD_PIRATE_CHEST_TE_ID);
		}	
	}
}
