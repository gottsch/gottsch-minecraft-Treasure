/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestTypes;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.monster.PirateMimicEntity;
import com.someguyssoftware.treasure2.entity.monster.WoodMimicEntity;
import com.someguyssoftware.treasure2.enums.FogHeight;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.MimicChestItemBlock;
import com.someguyssoftware.treasure2.item.TreasureChestItemBlock;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CompressorChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldSkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxTileEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.MistEmitterTileEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.ProximitySpawnerTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;
import com.someguyssoftware.treasure2.tileentity.SkullChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SpiderChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.VikingChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.WitherChestTileEntity;
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
 * @author Mark Gottschling on Dec 22, 2017
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
	public static final Block COMPRESSOR_CHEST;
	public static final Block WITHER_CHEST;
	public static final Block WITHER_CHEST_TOP;
	public static final Block SKULL_CHEST;
	public static final Block GOLD_SKULL_CHEST;
	public static final Block CAULDRON_CHEST;
	public static final Block SPIDER_CHEST;
	public static final Block VIKING_CHEST;
//	public static final Block WHALE_BONE_PIRATE_CHEST;
//	public static final Block OYSTER_CHEST;
//	public static final Block CLAM_CHEST;

	// MIMIC CHESTS
	public static final Block WOOD_MIMIC;
	public static final Block PIRATE_MIMIC;

	// unimplemented chests
	public static final Block VASE = null;
	public static final Block INVISIBLE_CHEST = null;
	public static final Block SAMURAI_CHEST = null;
	public static final Block OBSIDIAN_CHEST = null;

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
	public static final Block SKELETON;

	// wells
	public static final Block WISHING_WELL_BLOCK;
	public static final Block DESERT_WISHING_WELL_BLOCK;

	// fogs
//	public static final FogBlock FOG_BLOCK;
//	public static final FogBlock HIGH_FOG_BLOCK;
//	public static final FogBlock MED_FOG_BLOCK;
//	public static final FogBlock LOW_FOG_BLOCK;
//
//	public static final FogBlock WITHER_FOG;
//	public static final FogBlock HIGH_WITHER_FOG;
//	public static final FogBlock MED_WITHER_FOG;
//	public static final FogBlock LOW_WITHER_FOG;
//
//	public static final FogBlock POISON_FOG;
//	public static final FogBlock HIGH_POISON_FOG;
//	public static final FogBlock MED_POISON_FOG;
//	public static final FogBlock LOW_POISON_FOG;

	public static Map<FogHeight, FogBlock> fogMap = new HashMap<>();;
	public static Map<FogHeight, FogBlock> witherFogMap = new HashMap<>();
	public static Map<FogHeight, FogBlock> poisonFogMap = new HashMap<>();

	// ores
	public static final OreBlock RUBY_ORE;
	public static final OreBlock SAPPHIRE_ORE;

	// wither
	public static final Block WITHER_LOG;
	public static final Block WITHER_BRANCH;
	public static final Block WITHER_ROOT;
	public static final Block WITHER_BROKEN_LOG;
	public static final Block WITHER_LOG_SOUL;
	public static final Block WITHER_PLANKS;

	// other
	public static final Block SPANISH_MOSS;
	public static final Block FALLING_GRASS;
	public static final Block FALLING_SAND;
	public static final Block FALLING_RED_SAND;
	
	// treasures: paintings
	public static final AbstractPaintingBlock PAINTING_BLOCKS_BRICKS;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_COBBLESTONE;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_DIRT;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_LAVA;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_SAND;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_WATER;
	public static final AbstractPaintingBlock PAINTING_BLOCKS_WOOD;

	public static final ProximityBlock PROXIMITY_SPAWNER;

	// initialize blocks
	static {
		// standard chest bounds
		AxisAlignedBB vanilla = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
		AxisAlignedBB[] stdChestBounds = new AxisAlignedBB[4];
		stdChestBounds[0] = vanilla; // S
		stdChestBounds[1] = vanilla; // W
		stdChestBounds[2] = vanilla; // N
		stdChestBounds[3] = vanilla; // E

		WOOD_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.WOOD_CHEST_ID, WoodChestTileEntity.class,
				TreasureChestTypes.STANDARD, Rarity.COMMON).setBounds(stdChestBounds).setHardness(2.5F);

		IRONBOUND_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.IRONBOUND_CHEST_ID,
				IronboundChestTileEntity.class, TreasureChestTypes.STANDARD, Rarity.UNCOMMON).setBounds(stdChestBounds)
						.setHardness(3.0F);

		PIRATE_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.PIRATE_CHEST_ID,
				PirateChestTileEntity.class, TreasureChestTypes.STANDARD, Rarity.SCARCE).setBounds(stdChestBounds)
						.setHardness(3.0F);

		CRATE_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.CRATE_CHEST_ID, CrateChestTileEntity.class,
				TreasureChestTypes.CRATE, Rarity.UNCOMMON).setBounds(stdChestBounds).setHardness(2.5F);

		MOLDY_CRATE_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.MOLDY_CRATE_CHEST_ID,
				MoldyCrateChestTileEntity.class, TreasureChestTypes.CRATE, Rarity.COMMON).setBounds(stdChestBounds)
						.setHardness(2.0F);

		// safe chest bounds
		AxisAlignedBB[] safeBounds = new AxisAlignedBB[4];
		safeBounds[0] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[1] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[2] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);
		safeBounds[3] = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.8125D, 0.875D);

		SAFE = new TreasureChestBlock(Treasure.MODID, TreasureConfig.SAFE_ID, SafeTileEntity.class,
				TreasureChestTypes.SAFE, Rarity.RARE).setBounds(safeBounds).setHardness(4.0F);

		// create new strongbox bounds
		AxisAlignedBB[] strongboxBounds = new AxisAlignedBB[4];
		strongboxBounds[0] = new AxisAlignedBB(0.1875D, 0.0D, 0.25D, 0.825D, 0.5D, 0.75D); // S
		strongboxBounds[1] = new AxisAlignedBB(0.25D, 0.0D, 0.1875D, 0.75D, 0.5D, 0.825D); // W
		strongboxBounds[2] = new AxisAlignedBB(0.1875D, 0.0D, 0.25D, 0.825D, 0.5D, 0.75D); // N
		strongboxBounds[3] = new AxisAlignedBB(0.25D, 0.0D, 0.1875D, 0.75D, 0.5D, 0.825D); // E

		IRON_STRONGBOX = new TreasureChestBlock(Treasure.MODID, TreasureConfig.IRON_STRONGBOX_ID,
				IronStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.SCARCE)
						.setChestGuiID(GuiHandler.STRONGBOX_CHEST_GUIID).setBounds(strongboxBounds).setHardness(4.0F);

		GOLD_STRONGBOX = new TreasureChestBlock(Treasure.MODID, TreasureConfig.GOLD_STRONGBOX_ID,
				GoldStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.RARE)
						.setChestGuiID(GuiHandler.STRONGBOX_CHEST_GUIID).setBounds(strongboxBounds).setHardness(4.0F);

		DREAD_PIRATE_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.DREAD_PIRATE_CHEST_ID,
				DreadPirateChestTileEntity.class, TreasureChestTypes.STANDARD, Rarity.EPIC).setBounds(stdChestBounds)
						.setHardness(4.0F);

		AxisAlignedBB compressorBB = new AxisAlignedBB(0.28125D, 0.0D, 0.28125D, 0.71875D, 0.4375D, 0.71875D);
		AxisAlignedBB[] compressorChestBounds = new AxisAlignedBB[4];
		compressorChestBounds[0] = compressorBB; // S
		compressorChestBounds[1] = compressorBB; // W
		compressorChestBounds[2] = compressorBB; // N
		compressorChestBounds[3] = compressorBB; // E

		COMPRESSOR_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.COMPRESSOR_CHEST_ID,
				CompressorChestTileEntity.class, TreasureChestTypes.COMPRESSOR, Rarity.EPIC)
						.setChestGuiID(GuiHandler.COMPRESSOR_CHEST_GUIID).setBounds(compressorChestBounds)
						.setHardness(3.0F);

		AxisAlignedBB witherBounds = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.6563D, 0.9375D);
		// .875
		AxisAlignedBB[] witherChestBounds = new AxisAlignedBB[4];
		witherChestBounds[0] = witherBounds; // S
		witherChestBounds[1] = witherBounds; // W
		witherChestBounds[2] = witherBounds; // N
		witherChestBounds[3] = witherBounds; // E

		WITHER_CHEST = new WitherChestBlock(Treasure.MODID, TreasureConfig.WITHER_CHEST_ID, WitherChestTileEntity.class,
				TreasureChestTypes.ARMOIRE, Rarity.SCARCE).setChestGuiID(GuiHandler.WITHER_CHEST_GUIID)
						.setBounds(witherChestBounds).setHardness(2.5F);

		WITHER_CHEST_TOP = new WitherChestTopBlock(Treasure.MODID, TreasureConfig.WITHER_CHEST_TOP_ID)
				.setHardness(2.5F);

		AxisAlignedBB skullBB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.56825D, 0.75D);
		AxisAlignedBB[] skullChestBounds = new AxisAlignedBB[4];
		skullChestBounds[0] = skullBB; // S
		skullChestBounds[1] = skullBB; // W
		skullChestBounds[2] = skullBB; // N
		skullChestBounds[3] = skullBB; // E

		SKULL_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.SKULL_CHEST_ID, SkullChestTileEntity.class,
				TreasureChestTypes.SKULL, Rarity.SCARCE).setChestGuiID(GuiHandler.SKULL_CHEST_GUIID)
						.setBounds(skullChestBounds).setHardness(3.0F);

		GOLD_SKULL_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.GOLD_SKULL_CHEST_ID,
				GoldSkullChestTileEntity.class, TreasureChestTypes.SKULL, Rarity.RARE)
						.setChestGuiID(GuiHandler.SKULL_CHEST_GUIID).setBounds(skullChestBounds).setHardness(3.0F);

		CAULDRON_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.CAULDRON_CHEST_ID, Material.IRON,
				CauldronChestTileEntity.class, TreasureChestTypes.TOP_SPLIT, Rarity.EPIC)
						.setChestGuiID(GuiHandler.STANDARD_CHEST_GUIID).setHardness(3.0F);

//		WHALE_BONE_PIRATE_CHEST = new TreasureChestBlock(
//				Treasure.MODID,
//				TreasureConfig.WHALE_BONE_PIRATE_CHEST_ID,
//				WhaleBonePirateChestTileEntity.class,
//				TreasureChestTypes.STANDARD, 
//				Rarity.EPIC)
//				.setBounds(stdChestBounds)
//				.setHardness(4.0F);

		AxisAlignedBB molluscBB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.375D, 0.9375D);
		AxisAlignedBB[] molluscChestBounds = new AxisAlignedBB[4];
		molluscChestBounds[0] = molluscBB; // S
		molluscChestBounds[1] = molluscBB; // W
		molluscChestBounds[2] = molluscBB; // N
		molluscChestBounds[3] = molluscBB; // E

//		OYSTER_CHEST = new TreasureChestBlock(
//				Treasure.MODID,
//				TreasureConfig.OYSTER_CHEST_ID,
//				OysterChestTileEntity.class,
//				TreasureChestTypes.LOW_RISE, // TODO may have to create own
//				Rarity.EPIC)
//				.setChestGuiID(GuiHandler.MOLLUSCS_CHEST_GUIID)
//				.setBounds(molluscChestBounds)
//				.setHardness(3.0F);

//		CLAM_CHEST = new TreasureChestBlock(
//				Treasure.MODID,
//				TreasureConfig.CLAM_CHEST_ID,
//				ClamChestTileEntity.class,
//				TreasureChestTypes.LOW_RISE, // TODO may have to create own
//				Rarity.RARE)
//				.setChestGuiID(GuiHandler.MOLLUSCS_CHEST_GUIID)
//				.setBounds(molluscChestBounds)
//				.setHardness(3.0F);

		SPIDER_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.SPIDER_CHEST_ID,
				SpiderChestTileEntity.class, TreasureChestTypes.SINGLE_STANDARD, Rarity.RARE).setHardness(3.0F);

		VIKING_CHEST = new TreasureChestBlock(Treasure.MODID, TreasureConfig.VIKING_CHEST_ID,
				VikingChestTileEntity.class, TreasureChestTypes.VIKING, Rarity.UNCOMMON).setBounds(
						new AxisAlignedBB[] {
								new AxisAlignedBB(0, 0, 0.125, 1, 0.9375, 0.875), // S
								new AxisAlignedBB(0.125, 0, 0, 0.875, 0.9375, 1), // W
								new AxisAlignedBB(0, 0, 0.125, 1, 0.9375, 0.875), // N
								new AxisAlignedBB(0.125, 0, 0, 0.875, 0.9375, 1)}
						).setHardness(3.0F);
		
		// map the chests by rarity
		chests = ArrayListMultimap.create();

		// mimics
		WOOD_MIMIC = new MimicChestBlock(Treasure.MODID, TreasureConfig.WOOD_MIMIC_ID, WoodChestTileEntity.class,
				WoodMimicEntity.class, TreasureChestTypes.STANDARD, Rarity.COMMON).setBounds(stdChestBounds)
						.setHardness(2.5F);

		PIRATE_MIMIC = new MimicChestBlock(Treasure.MODID, TreasureConfig.PIRATE_MIMIC_ID, PirateChestTileEntity.class,
				PirateMimicEntity.class, TreasureChestTypes.STANDARD, Rarity.SCARCE).setBounds(stdChestBounds)
						.setHardness(2.5F);

		// gravestone bounds
		AxisAlignedBB[] gbs = new AxisAlignedBB[4];
		gbs[0] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 0.75D, 0.675D); // S
		gbs[1] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 0.75D, 0.875D); // W
		gbs[2] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 0.75D, 0.675D); // N
		gbs[3] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 0.75D, 0.875D); // E

		// Gravestones
		GRAVESTONE1_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_STONE_ID, Material.ROCK)
				.setBounds(gbs);
		GRAVESTONE1_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_COBBLESTONE_ID,
				Material.ROCK).setBounds(gbs);
		GRAVESTONE1_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE1_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE1_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE1_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE1_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs);
		GRAVESTONE1_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE1_OBSIDIAN_ID,
				Material.ROCK).setBounds(gbs);

		AxisAlignedBB[] gbs2 = new AxisAlignedBB[4];
		gbs2[0] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.375D, 0.675D); // S
		gbs2[1] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.375D, 0.875D); // W
		gbs2[2] = new AxisAlignedBB(0.125D, 0.0D, 0.375D, 0.875D, 1.375D, 0.675D); // N
		gbs2[3] = new AxisAlignedBB(0.375D, 0.0D, 0.125D, 0.675D, 1.375D, 0.875D); // E

		// Gravestones
		GRAVESTONE2_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_STONE_ID, Material.ROCK)
				.setBounds(gbs2);
		GRAVESTONE2_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_COBBLESTONE_ID,
				Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE2_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE2_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE2_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE2_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE2_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE2_OBSIDIAN_ID,
				Material.ROCK).setBounds(gbs2);

		GRAVESTONE3_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_STONE_ID, Material.ROCK)
				.setBounds(gbs2);
		GRAVESTONE3_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_COBBLESTONE_ID,
				Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE3_MOSSY_COBBLESTONE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE3_POLISHED_GRANITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE3_POLISHED_ANDESITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID,
				TreasureConfig.GRAVESTONE3_POLISHED_DIORITE_ID, Material.ROCK).setBounds(gbs2);
		GRAVESTONE3_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.GRAVESTONE3_OBSIDIAN_ID,
				Material.ROCK).setBounds(gbs2);

		// other
		SKULL_CROSSBONES = new SkullAndBonesBlock(Treasure.MODID, TreasureConfig.SKULL_CROSSBONES_ID, Material.ROCK);

		SKELETON = new SkeletonBlock(Treasure.MODID, TreasureConfig.SKELETON_ID, Material.ROCK);

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
		gravestones.add(SKELETON);

		// FOG
//		FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.FOG_BLOCK_ID, TreasureItems.FOG, fogMap)
//				.setFogHeight(FogHeight.FULL_FOG);
//		HIGH_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.HIGH_FOG_BLOCK_ID, TreasureItems.FOG, fogMap)
//				.setFogHeight(FogHeight.HIGH_FOG);
//		MED_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.MED_FOG_BLOCK_ID, TreasureItems.FOG, fogMap)
//				.setFogHeight(FogHeight.MEDIUM_FOG);
//		LOW_FOG_BLOCK = new FogBlock(Treasure.MODID, TreasureConfig.LOW_FOG_BLOCK_ID, TreasureItems.FOG, fogMap)
//				.setFogHeight(FogHeight.LOW_FOG);
//
//		WITHER_FOG = new WitherFogBlock(Treasure.MODID, TreasureConfig.WITHER_FOG_ID, TreasureItems.FOG, witherFogMap)
//				.setFogType(FogType.WITHER).setFogHeight(FogHeight.FULL_FOG);
//		HIGH_WITHER_FOG = new WitherFogBlock(Treasure.MODID, TreasureConfig.HIGH_WITHER_FOG_ID, TreasureItems.FOG,
//				witherFogMap).setFogType(FogType.WITHER).setFogHeight(FogHeight.HIGH_FOG);
//		MED_WITHER_FOG = new WitherFogBlock(Treasure.MODID, TreasureConfig.MED_WITHER_FOG_ID, TreasureItems.FOG,
//				witherFogMap).setFogType(FogType.WITHER).setFogHeight(FogHeight.MEDIUM_FOG);
//		LOW_WITHER_FOG = new WitherFogBlock(Treasure.MODID, TreasureConfig.LOW_WITHER_FOG_ID, TreasureItems.FOG,
//				witherFogMap).setFogType(FogType.WITHER).setFogHeight(FogHeight.LOW_FOG);
//
//		POISON_FOG = new PoisonFogBlock(Treasure.MODID, TreasureConfig.POISON_FOG_ID, TreasureItems.FOG, poisonFogMap)
//				.setFogType(FogType.POISON).setFogHeight(FogHeight.FULL_FOG);
//		HIGH_POISON_FOG = new PoisonFogBlock(Treasure.MODID, TreasureConfig.HIGH_POISON_FOG_ID, TreasureItems.FOG,
//				poisonFogMap).setFogType(FogType.POISON).setFogHeight(FogHeight.HIGH_FOG);
//		MED_POISON_FOG = new PoisonFogBlock(Treasure.MODID, TreasureConfig.MED_POISON_FOG_ID, TreasureItems.FOG,
//				poisonFogMap).setFogType(FogType.POISON).setFogHeight(FogHeight.MEDIUM_FOG);
//		LOW_POISON_FOG = new PoisonFogBlock(Treasure.MODID, TreasureConfig.LOW_POISON_FOG_ID, TreasureItems.FOG,
//				poisonFogMap).setFogType(FogType.POISON).setFogHeight(FogHeight.LOW_FOG);
//
//		fogMap.put(FogHeight.FULL_FOG, TreasureBlocks.FOG_BLOCK);
//		fogMap.put(FogHeight.HIGH_FOG, TreasureBlocks.HIGH_FOG_BLOCK);
//		fogMap.put(FogHeight.MEDIUM_FOG, TreasureBlocks.MED_FOG_BLOCK);
//		fogMap.put(FogHeight.LOW_FOG, TreasureBlocks.LOW_FOG_BLOCK);
//
//		witherFogMap.put(FogHeight.FULL_FOG, TreasureBlocks.WITHER_FOG);
//		witherFogMap.put(FogHeight.HIGH_FOG, TreasureBlocks.HIGH_WITHER_FOG);
//		witherFogMap.put(FogHeight.MEDIUM_FOG, TreasureBlocks.MED_WITHER_FOG);
//		witherFogMap.put(FogHeight.LOW_FOG, TreasureBlocks.LOW_WITHER_FOG);
//
//		poisonFogMap.put(FogHeight.FULL_FOG, TreasureBlocks.POISON_FOG);
//		poisonFogMap.put(FogHeight.HIGH_FOG, TreasureBlocks.HIGH_POISON_FOG);
//		poisonFogMap.put(FogHeight.MEDIUM_FOG, TreasureBlocks.MED_POISON_FOG);
//		poisonFogMap.put(FogHeight.LOW_FOG, TreasureBlocks.LOW_POISON_FOG);

		// ORES/GEMS
		SAPPHIRE_ORE = new OreBlock(Treasure.MODID, TreasureConfig.SAPPHIRE_ORE_ID, Material.ROCK);
		RUBY_ORE = new OreBlock(Treasure.MODID, TreasureConfig.RUBY_ORE_ID, Material.ROCK);

		// other
		WISHING_WELL_BLOCK = new WishingWellBlock(Treasure.MODID, TreasureConfig.WISHING_WELL_BLOCK_ID, Material.ROCK);
		DESERT_WISHING_WELL_BLOCK = new DesertWishingWellBlock(Treasure.MODID,
				TreasureConfig.DESERT_WISHING_WELL_BLOCK_ID, Material.ROCK);
		WITHER_LOG = new WitherLogBlock(Treasure.MODID, TreasureConfig.WITHER_LOG_ID);
		WITHER_BRANCH = new WitherBranchBlock(Treasure.MODID, TreasureConfig.WITHER_BRANCH_ID);
		WITHER_ROOT = new WitherRootBlock(Treasure.MODID, TreasureConfig.WITHER_ROOT_ID);
		WITHER_BROKEN_LOG = new WitherBrokenLogBlock(Treasure.MODID, TreasureConfig.WITHER_BROKEN_LOG_ID);
		WITHER_LOG_SOUL = new WitherLogSoulBlock(Treasure.MODID, TreasureConfig.WITHER_LOG_SOUL_ID);
		WITHER_PLANKS = new WitherPlanksBlock(Treasure.MODID, TreasureConfig.WITHER_PLANKS_ID);
		SPANISH_MOSS = new SpanishMossBlock(Treasure.MODID, TreasureConfig.SPANISH_MOSS_BLOCK_ID);
		FALLING_GRASS = new FallingGrassBlock(Treasure.MODID, TreasureConfig.FALLING_GRASS_ID);
		FALLING_SAND = new FallingSandBlock(Treasure.MODID, TreasureConfig.FALLING_SAND_ID);
		FALLING_RED_SAND = new FallingRedSandBlock(Treasure.MODID, TreasureConfig.FALLING_RED_SAND_ID);
		
		// treasures: paintings
		AxisAlignedBB[] pbs = new AxisAlignedBB[4];
		pbs[0] = new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.0625D); // S
		pbs[1] = new AxisAlignedBB(0.9375D, 0D, 0D, 1D, 1D, 1D); // W
		pbs[2] = new AxisAlignedBB(0D, 0D, 0.9375D, 1D, 1D, 1D); // N
		pbs[3] = new AxisAlignedBB(0D, 0.D, 0D, 0.0625D, 1D, 1D); // E

		PAINTING_BLOCKS_BRICKS = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_BRICKS_ID,
				Material.CLOTH, Rarity.EPIC).setBounds(pbs);
		PAINTING_BLOCKS_COBBLESTONE = new Painting1x1Block(Treasure.MODID,
				TreasureConfig.PAINTING_BLOCKS_COBBLESTONE_ID, Material.CLOTH, Rarity.SCARCE).setBounds(pbs);
		PAINTING_BLOCKS_DIRT = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_DIRT_ID,
				Material.CLOTH, Rarity.SCARCE).setBounds(pbs);
		PAINTING_BLOCKS_LAVA = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_LAVA_ID,
				Material.CLOTH, Rarity.EPIC).setBounds(pbs);
		PAINTING_BLOCKS_SAND = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_SAND_ID,
				Material.CLOTH, Rarity.RARE).setBounds(pbs);
		PAINTING_BLOCKS_WATER = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_WATER_ID,
				Material.CLOTH, Rarity.SCARCE).setBounds(pbs);
		PAINTING_BLOCKS_WOOD = new Painting1x1Block(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_WOOD_ID,
				Material.CLOTH, Rarity.RARE).setBounds(pbs);

		// proximity blocks
		PROXIMITY_SPAWNER = new ProximityBlock(Treasure.MODID, TreasureConfig.PROXIMITY_SPAWNER_ID,
				ProximitySpawnerTileEntity.class);

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

			final Block[] blocks = { WOOD_CHEST, CRATE_CHEST, MOLDY_CRATE_CHEST, IRONBOUND_CHEST, PIRATE_CHEST,
					IRON_STRONGBOX, GOLD_STRONGBOX, SAFE, DREAD_PIRATE_CHEST,
//					WHALE_BONE_PIRATE_CHEST,
					COMPRESSOR_CHEST,
					SPIDER_CHEST, 
					VIKING_CHEST,
					WOOD_MIMIC, 
					PIRATE_MIMIC, 
					GRAVESTONE1_STONE,
					GRAVESTONE1_COBBLESTONE, GRAVESTONE1_MOSSY_COBBLESTONE, GRAVESTONE1_POLISHED_GRANITE,
					GRAVESTONE1_POLISHED_ANDESITE, GRAVESTONE1_POLISHED_DIORITE, GRAVESTONE1_OBSIDIAN,
					GRAVESTONE2_STONE, GRAVESTONE2_COBBLESTONE, GRAVESTONE2_MOSSY_COBBLESTONE,
					GRAVESTONE2_POLISHED_GRANITE, GRAVESTONE2_POLISHED_ANDESITE, GRAVESTONE2_POLISHED_DIORITE,
					GRAVESTONE2_OBSIDIAN, GRAVESTONE3_STONE, GRAVESTONE3_COBBLESTONE, GRAVESTONE3_MOSSY_COBBLESTONE,
					GRAVESTONE3_POLISHED_GRANITE, GRAVESTONE3_POLISHED_ANDESITE, GRAVESTONE3_POLISHED_DIORITE,
					GRAVESTONE3_OBSIDIAN, SKULL_CROSSBONES, SKELETON, WISHING_WELL_BLOCK, DESERT_WISHING_WELL_BLOCK,
//					FOG_BLOCK, HIGH_FOG_BLOCK, MED_FOG_BLOCK, LOW_FOG_BLOCK, WITHER_FOG, HIGH_WITHER_FOG,
//					MED_WITHER_FOG, LOW_WITHER_FOG, POISON_FOG, HIGH_POISON_FOG, MED_POISON_FOG, LOW_POISON_FOG,
					WITHER_LOG, WITHER_BRANCH, WITHER_ROOT, WITHER_BROKEN_LOG, WITHER_LOG_SOUL, WITHER_CHEST_TOP,
					WITHER_PLANKS, SPANISH_MOSS, PAINTING_BLOCKS_BRICKS, PAINTING_BLOCKS_COBBLESTONE,
					PAINTING_BLOCKS_DIRT, PAINTING_BLOCKS_LAVA, PAINTING_BLOCKS_SAND, PAINTING_BLOCKS_WATER,
					PAINTING_BLOCKS_WOOD, SAPPHIRE_ORE, RUBY_ORE, 
					FALLING_GRASS,
					FALLING_SAND,
					FALLING_RED_SAND
				};
			
			registry.registerAll(blocks);
			// register speciality chests separately (so they aren't in the rarity map)
			registry.register(WITHER_CHEST);
			registry.register(SKULL_CHEST);
			registry.register(GOLD_SKULL_CHEST);
			registry.register(CAULDRON_CHEST);
			registry.register(PROXIMITY_SPAWNER);

			// TODO oyster and clam go here with speciality until chests have a meta file
			// mapping
//			registry.register(OYSTER_CHEST);
//			registry.register(CLAM_CHEST);

			// map the block by rarity
			for (Block block : blocks) {
				if (block instanceof TreasureChestBlock) {
					chests.put(((TreasureChestBlock) block).getRarity(), block);
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

			final ItemBlock[] items = { new TreasureChestItemBlock(WOOD_CHEST), new TreasureChestItemBlock(CRATE_CHEST),
					new TreasureChestItemBlock(MOLDY_CRATE_CHEST), new TreasureChestItemBlock(IRONBOUND_CHEST),
					new TreasureChestItemBlock(PIRATE_CHEST), new TreasureChestItemBlock(IRON_STRONGBOX),
					new TreasureChestItemBlock(GOLD_STRONGBOX), new TreasureChestItemBlock(SAFE),
					new TreasureChestItemBlock(DREAD_PIRATE_CHEST),
//					new TreasureChestItemBlock(WHALE_BONE_PIRATE_CHEST),
					new TreasureChestItemBlock(COMPRESSOR_CHEST), new TreasureChestItemBlock(WITHER_CHEST),
					new TreasureChestItemBlock(SKULL_CHEST), new TreasureChestItemBlock(GOLD_SKULL_CHEST),
					new TreasureChestItemBlock(CAULDRON_CHEST),
					new TreasureChestItemBlock(SPIDER_CHEST), 
					new TreasureChestItemBlock(VIKING_CHEST), 
					new MimicChestItemBlock(WOOD_MIMIC),
					new MimicChestItemBlock(PIRATE_MIMIC),

					// TODO update with GravestonIetmBlock
					new ItemBlock(GRAVESTONE1_STONE), new ItemBlock(GRAVESTONE1_COBBLESTONE),
					new ItemBlock(GRAVESTONE1_MOSSY_COBBLESTONE), new ItemBlock(GRAVESTONE1_POLISHED_GRANITE),
					new ItemBlock(GRAVESTONE1_POLISHED_ANDESITE), new ItemBlock(GRAVESTONE1_POLISHED_DIORITE),
					new ItemBlock(GRAVESTONE1_OBSIDIAN), new ItemBlock(GRAVESTONE2_STONE),
					new ItemBlock(GRAVESTONE2_COBBLESTONE), new ItemBlock(GRAVESTONE2_MOSSY_COBBLESTONE),
					new ItemBlock(GRAVESTONE2_POLISHED_GRANITE), new ItemBlock(GRAVESTONE2_POLISHED_ANDESITE),
					new ItemBlock(GRAVESTONE2_POLISHED_DIORITE), new ItemBlock(GRAVESTONE2_OBSIDIAN),
					new ItemBlock(GRAVESTONE3_STONE), new ItemBlock(GRAVESTONE3_COBBLESTONE),
					new ItemBlock(GRAVESTONE3_MOSSY_COBBLESTONE), new ItemBlock(GRAVESTONE3_POLISHED_GRANITE),
					new ItemBlock(GRAVESTONE3_POLISHED_ANDESITE), new ItemBlock(GRAVESTONE3_POLISHED_DIORITE),
					new ItemBlock(GRAVESTONE3_OBSIDIAN), new ItemBlock(SKULL_CROSSBONES),
					new ItemBlock(WISHING_WELL_BLOCK), new ItemBlock(DESERT_WISHING_WELL_BLOCK),
//					new ItemBlock(FOG_BLOCK), new ItemBlock(HIGH_FOG_BLOCK), new ItemBlock(MED_FOG_BLOCK),
//					new ItemBlock(LOW_FOG_BLOCK), new ItemBlock(WITHER_FOG), new ItemBlock(HIGH_WITHER_FOG),
//					new ItemBlock(MED_WITHER_FOG), new ItemBlock(LOW_WITHER_FOG), new ItemBlock(POISON_FOG),
//					new ItemBlock(HIGH_POISON_FOG), new ItemBlock(MED_POISON_FOG), new ItemBlock(LOW_POISON_FOG),
					new ItemBlock(WITHER_LOG), new ItemBlock(WITHER_BROKEN_LOG), new ItemBlock(WITHER_LOG_SOUL),
					new ItemBlock(WITHER_PLANKS), new ItemBlock(SAPPHIRE_ORE), new ItemBlock(RUBY_ORE),
					new ItemBlock(PROXIMITY_SPAWNER),
					new ItemBlock(FALLING_GRASS),
					new ItemBlock(FALLING_SAND),
					new ItemBlock(FALLING_RED_SAND)
			};

			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(),
						"Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}

			// register the tile entities
			GameRegistry.registerTileEntity(WoodChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.WOOD_CHEST_TE_ID));
			GameRegistry.registerTileEntity(CrateChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.CRATE_CHEST_TE_ID));
			GameRegistry.registerTileEntity(MoldyCrateChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.MOLDY_CRATE_CHEST_TE_ID));
			GameRegistry.registerTileEntity(IronboundChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.IRONBOUND_CHEST_TE_ID));
			GameRegistry.registerTileEntity(PirateChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.PIRATE_CHEST_TE_ID));
			GameRegistry.registerTileEntity(IronStrongboxTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.IRON_STRONGBOX_TE_ID));
			GameRegistry.registerTileEntity(GoldStrongboxTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.GOLD_STRONGBOX_TE_ID));
			GameRegistry.registerTileEntity(SafeTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.SAFE_TE_ID));
			GameRegistry.registerTileEntity(DreadPirateChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.DREAD_PIRATE_CHEST_TE_ID));
			GameRegistry.registerTileEntity(CompressorChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.COMPRESSOR_CHEST_TE_ID));
			GameRegistry.registerTileEntity(WitherChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.WITHER_CHEST_TE_ID));
			GameRegistry.registerTileEntity(SkullChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.SKULL_CHEST_TE_ID));
			GameRegistry.registerTileEntity(GoldSkullChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.GOLD_SKULL_CHEST_TE_ID));
			GameRegistry.registerTileEntity(CauldronChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.CAULDRON_CHEST_TE_ID));
			GameRegistry.registerTileEntity(SpiderChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.SPIDER_CHEST_TE_ID));
			GameRegistry.registerTileEntity(VikingChestTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.VIKING_CHEST_TE_ID));
			
			GameRegistry.registerTileEntity(ProximitySpawnerTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.PROXIMITY_SPAWNER_TE_ID));
			GameRegistry.registerTileEntity(GravestoneProximitySpawnerTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.GRAVESTONE_PROXIMITY_SPAWNER_TE_ID));
			GameRegistry.registerTileEntity(MistEmitterTileEntity.class,
					new ResourceLocation(Treasure.MODID + ":" + TreasureConfig.GRAVESTONE_TE_ID));
//			GameRegistry.registerTileEntity(WhaleBonePirateChestTileEntity.class, new ResourceLocation(Treasure.MODID+":"+TreasureConfig.WHALE_BONE_PIRATE_CHEST_TE_ID));
//			GameRegistry.registerTileEntity(OysterChestTileEntity.class, new ResourceLocation(Treasure.MODID+":"+TreasureConfig.OYSTER_CHEST_TE_ID));	
//			GameRegistry.registerTileEntity(ClamChestTileEntity.class, new ResourceLocation(Treasure.MODID+":"+TreasureConfig.CLAM_CHEST_TE_ID));	

		}
	}
}
