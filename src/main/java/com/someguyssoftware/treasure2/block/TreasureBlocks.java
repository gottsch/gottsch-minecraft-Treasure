/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
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
import com.someguyssoftware.treasure2.tileentity.CardboardBoxBlockEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.CompressorChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.CrateChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.CrystalSkullChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.DreadPirateChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.GoldSkullChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.GoldStrongboxBlockEntity;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerBlockEntity;
import com.someguyssoftware.treasure2.tileentity.IronStrongboxBlockEntity;
import com.someguyssoftware.treasure2.tileentity.IronboundChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.MilkCrateBlockEntity;
import com.someguyssoftware.treasure2.tileentity.MoldyCrateChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.PirateChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.SafeBlockEntity;
import com.someguyssoftware.treasure2.tileentity.SkullChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.SpiderChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.VikingChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.WitherChestBlockEntity;
import com.someguyssoftware.treasure2.tileentity.WoodChestBlockEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
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

	public static Block WOOD_CHEST;
	public static Block CRATE_CHEST;
	public static Block MOLDY_CRATE_CHEST;
	public static Block IRONBOUND_CHEST;
	public static Block PIRATE_CHEST;
	public static Block IRON_STRONGBOX;
	public static Block GOLD_STRONGBOX;
	public static Block DREAD_PIRATE_CHEST;
	public static Block WITHER_CHEST;
	public static Block WITHER_CHEST_TOP;
	public static Block SAFE;
	public static Block COMPRESSOR_CHEST;
	public static Block SKULL_CHEST;
	public static Block GOLD_SKULL_CHEST;
	public static Block CAULDRON_CHEST;
	public static Block SPIDER_CHEST;
    public static Block VIKING_CHEST;
    public static Block CARDBOARD_BOX;
    public static Block MILK_CRATE;
    public static Block CRYSTAL_SKULL_CHEST;

    public static ProximityBlock PROXIMITY_SPAWNER;
    public static GravestoneSpawnerBlock GRAVESTONE1_SPAWNER_STONE;
    public static GravestoneSpawnerBlock GRAVESTONE2_SPAWNER_COBBLESTONE;
    public static GravestoneSpawnerBlock GRAVESTONE3_SPAWNER_OBSIDIAN;
    public static Block WISHING_WELL_BLOCK;
    public static Block DESERT_WISHING_WELL_BLOCK;
	
	// gravestones
	public static Block GRAVESTONE1_STONE;
	public static Block GRAVESTONE1_COBBLESTONE;
	public static Block GRAVESTONE1_MOSSY_COBBLESTONE;
	public static Block GRAVESTONE1_POLISHED_GRANITE;
	public static Block GRAVESTONE1_POLISHED_ANDESITE;
	public static Block GRAVESTONE1_POLISHED_DIORITE;
	public static Block GRAVESTONE1_OBSIDIAN;
	public static Block GRAVESTONE1_SMOOTH_QUARTZ;	
	public static Block GRAVESTONE2_STONE;
	public static Block GRAVESTONE2_COBBLESTONE;
	public static Block GRAVESTONE2_MOSSY_COBBLESTONE;
	public static Block GRAVESTONE2_POLISHED_GRANITE;
	public static Block GRAVESTONE2_POLISHED_ANDESITE;
	public static Block GRAVESTONE2_POLISHED_DIORITE;
	public static Block GRAVESTONE2_OBSIDIAN;
	public static Block GRAVESTONE2_SMOOTH_QUARTZ;	
	public static Block GRAVESTONE3_STONE;
	public static Block GRAVESTONE3_COBBLESTONE;
	public static Block GRAVESTONE3_MOSSY_COBBLESTONE;
	public static Block GRAVESTONE3_POLISHED_GRANITE;
	public static Block GRAVESTONE3_POLISHED_ANDESITE;
	public static Block GRAVESTONE3_POLISHED_DIORITE;
	public static Block GRAVESTONE3_OBSIDIAN;
	public static Block GRAVESTONE3_SMOOTH_QUARTZ;	
	public static Block SKULL_CROSSBONES;
	public static Block SKELETON;
    
	public static Block WITHER_BRANCH;
	public static Block WITHER_ROOT;
	public static Block WITHER_LOG;
	public static Block WITHER_BROKEN_LOG;
	public static Block WITHER_SOUL_LOG;
	public static Block WITHER_PLANKS;
	public static Block SPANISH_MOSS;
	
	public static Block TOPAZ_ORE;
	public static Block ONYX_ORE;
	public static Block RUBY_ORE;
	public static Block SAPPHIRE_ORE;
	
	public static Block FALLING_GRASS;
	public static Block FALLING_SAND;
	public static Block FALLING_RED_SAND;
	
	public static final List<Block> BLOCKS = new ArrayList<>(100);
	public static final Set<BlockItem> ITEM_BLOCKS = new HashSet<>();
	public static final List<Block> GRAVESTONES = new ArrayList<>(20);
	public static final List<Block> GRAVESTONE_SPAWNERS = new ArrayList<>(5);

	/**
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {

		/**
		 * 
		 * @param event
		 */
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			/*
			 *  block shapes
			 */
			VoxelShape vanillaChestShape = Block.box(1, 0, 1, 15, 14, 15);
			VoxelShape[] standardChestBounds = new VoxelShape[] {vanillaChestShape, vanillaChestShape, vanillaChestShape, vanillaChestShape};
			
			// banded chest shapes
			VoxelShape[] bandedBounds = new VoxelShape[4];
			bandedBounds[0] = Block.box(1.5, 0, 2, 14, 12.5, 14);
			bandedBounds[1] = Block.box(2, 0, 1.5, 14, 14, 12.5);
			bandedBounds[2] = bandedBounds[0];
			bandedBounds[3] = bandedBounds[1];
			
			// safe chest shapes
			VoxelShape[] safeBounds = new VoxelShape[4];
			safeBounds[0] = Block.box(2, 0, 2, 14, 13, 14);
			safeBounds[1] = safeBounds[0];
			safeBounds[2] = safeBounds[0];
			safeBounds[3] = safeBounds[0];

			// strongbox shapes
			VoxelShape[] strongboxBounds = new VoxelShape[4];
			strongboxBounds[0] = Block.box(3, 0, 4, 13, 7.5, 12); // S
			strongboxBounds[1] = Block.box(4, 0, 3, 12, 7.5, 13); // W
			strongboxBounds[2] = Block.box(3, 0, 4, 13, 7.5, 12); // N
			strongboxBounds[3] = Block.box(4, 0, 3, 12, 7.5, 13); // E

			// compressor chest shapes
			VoxelShape[] compressorChestBounds = new VoxelShape[4];
			compressorChestBounds[0] = Block.box(4.5, 0, 4.5, 11.5, 7, 11.5); // S
			compressorChestBounds[1] = compressorChestBounds[0]; // W
			compressorChestBounds[2] = compressorChestBounds[0]; // N
			compressorChestBounds[3] = compressorChestBounds[0]; // E

			// skull chest shapes
			VoxelShape[] skullChestBounds = new VoxelShape[4];
			skullChestBounds[0] = Block.box(4, 0, 4, 12, 9.092, 12); // S
			skullChestBounds[1] = skullChestBounds[0]; // W
			skullChestBounds[2] = skullChestBounds[0]; // N
			skullChestBounds[3] = skullChestBounds[0]; // E

			// spider chest shapes
			VoxelShape spiderChestShape = Block.box(1, 0, 0, 15, 16, 16);
			VoxelShape[] spiderChestBounds = new VoxelShape[] {
					spiderChestShape, spiderChestShape, spiderChestShape, spiderChestShape
					};

			// viking chest shapes
			VoxelShape[] vikingChestBounds = new VoxelShape[] {
					Block.box(1, 0, 3, 15, 15, 13), // S
					Block.box(3, 0, 1, 13, 15, 15), // W
					Block.box(1, 0, 3, 15, 15, 13), // N
					Block.box(3, 0, 1, 13, 15, 15)
			};
			
			// wither chest shapes
			VoxelShape[] witherChestBounds = new VoxelShape[4];
			witherChestBounds[0] = Block.box(1, 0, 1, 15, 16, 15);
			witherChestBounds[1] = witherChestBounds[0]; // W
			witherChestBounds[2] = witherChestBounds[0]; // N
			witherChestBounds[3] = witherChestBounds[0]; // E

			// wither branch shapes
//			VoxelShape[] witherBranchBounds = new VoxelShape[] {
//					Block.box(4, 0, 0, 12, 8, 16),	// S
//					Block.box(0, 0, 4, 16, 8, 12),	// W
//					Block.box(4, 0, 0, 12, 8, 16),	// N
//					Block.box(0, 0, 4, 16, 8, 12),	// E
//			};
			
			// wither root shapes
//			VoxelShape[] witherRootBounds = new VoxelShape[] {
//					Block.box(3, 0, 0, 13, 4, 15),	// S
//					Block.box(0, 0, 4, 15, 4, 12),	// W
//					Block.box(3, 0, 0, 13, 4, 15),	// N
//					Block.box(0, 0, 4, 15, 4, 12),	// E
//			};
			
			// cardboard box bound
	        VoxelShape cardboardBoxShape = Block.box(1, 0, 1, 15, 15, 15);
	        VoxelShape[] cardboardBoxBounds = new VoxelShape[] {
	            cardboardBoxShape, cardboardBoxShape, cardboardBoxShape, cardboardBoxShape
	        };

	        // milk crate shapes
	        VoxelShape milkCrateShape = Block.box(2.75, 0, 2.75, 13.25, 10.25, 13.25);
	        VoxelShape[] milkCrateBounds = new VoxelShape[] {
	            milkCrateShape, milkCrateShape, milkCrateShape, milkCrateShape
	        };

			// gravestone shapes
			VoxelShape[] gravestoneBounds = TreasureShapeBuilder.buildGravestone1();
	        VoxelShape[] gravestoneBounds2 = TreasureShapeBuilder.buildGravestone2();
	        VoxelShape[] gravestoneBounds3 = TreasureShapeBuilder.buildGravestone3();
	        
			/*
			 *  initialize blocks
			 */
			WOOD_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.WOOD_CHEST_ID, WoodChestTileEntity.class,
					TreasureChestTypes.STANDARD, Rarity.COMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F))
					.setBounds(standardChestBounds);
			
			CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CRATE_CHEST_ID, CrateChestTileEntity.class,
					TreasureChestTypes.CRATE, Rarity.UNCOMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F))
					.setBounds(standardChestBounds);

			MOLDY_CRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.MOLDY_CRATE_CHEST_ID, MoldyCrateChestTileEntity.class,
					TreasureChestTypes.CRATE, Rarity.COMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F))
					.setBounds(standardChestBounds);

			IRONBOUND_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.IRONBOUND_CHEST_ID, IronboundChestTileEntity.class,
					TreasureChestTypes.STANDARD, Rarity.UNCOMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F))
					.setBounds(bandedBounds);

			PIRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.PIRATE_CHEST_ID, PirateChestTileEntity.class,
					TreasureChestTypes.STANDARD, Rarity.SCARCE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F))
					.setBounds(standardChestBounds);

			SAFE = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SAFE_ID, SafeTileEntity.class,
					TreasureChestTypes.SAFE, Rarity.RARE, Block.Properties.of(Material.METAL, MaterialColor.METAL).strength(4.0F)).setBounds(safeBounds);

			IRON_STRONGBOX = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.IRON_STRONGBOX_ID,
					IronStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.SCARCE, Block.Properties.of(Material.METAL, MaterialColor.METAL)
					.strength(4.0F)).setBounds(strongboxBounds);

			GOLD_STRONGBOX = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.GOLD_STRONGBOX_ID,
					GoldStrongboxTileEntity.class, TreasureChestTypes.STRONGBOX, Rarity.RARE, Block.Properties.of(Material.METAL, MaterialColor.METAL)
					.strength(4.0F)).setBounds(strongboxBounds);

			DREAD_PIRATE_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.DREAD_PIRATE_CHEST_ID,
					DreadPirateChestTileEntity.class, TreasureChestTypes.STANDARD, Rarity.EPIC, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(4.0F)).setBounds(standardChestBounds);

			COMPRESSOR_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.COMPRESSOR_CHEST_ID,
					CompressorChestTileEntity.class, TreasureChestTypes.COMPRESSOR, Rarity.EPIC, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(compressorChestBounds);		

			SKULL_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SKULL_CHEST_ID, SkullChestTileEntity.class,
					TreasureChestTypes.SKULL, Rarity.SCARCE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(skullChestBounds);

			GOLD_SKULL_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.GOLD_SKULL_CHEST_ID,
					GoldSkullChestTileEntity.class, TreasureChestTypes.SKULL, Rarity.RARE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(skullChestBounds);

	        CRYSTAL_SKULL_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CRYSTAL_SKULL_CHEST_ID,
					CrystalSkullChestTileEntity.class, TreasureChestTypes.SKULL, Rarity.RARE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(skullChestBounds);

			CAULDRON_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CAULDRON_CHEST_ID,
					CauldronChestTileEntity.class, TreasureChestTypes.TOP_SPLIT, Rarity.EPIC, Block.Properties.of(Material.METAL, MaterialColor.METAL)
					.strength(3.0F)).setBounds(spiderChestBounds);

			SPIDER_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.SPIDER_CHEST_ID,
					SpiderChestTileEntity.class, TreasureChestTypes.SINGLE_STANDARD, Rarity.RARE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(standardChestBounds);
			
			VIKING_CHEST = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.VIKING_CHEST_ID,
					VikingChestTileEntity.class, TreasureChestTypes.VIKING, Rarity.UNCOMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(3.0F)).setBounds(vikingChestBounds);

	        CARDBOARD_BOX = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.CARDBOARD_BOX_ID, CardboardBoxTileEntity.class,
	                TreasureChestTypes.TOP_SPLIT, Rarity.COMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
	                .strength(2.5F)).setBounds(cardboardBoxBounds);

			MILK_CRATE = new StandardChestBlock(Treasure.MODID, TreasureConfig.ChestID.MILK_CRATE_ID, MilkCrateTileEntity.class,
	                TreasureChestTypes.MILK_CRATE, Rarity.COMMON, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
	                .strength(2.5F)).setBounds(milkCrateBounds);

			// WITHER CHEST
			WITHER_CHEST = new WitherChestBlock(Treasure.MODID, TreasureConfig.ChestID.WITHER_CHEST_ID, 
					WitherChestTileEntity.class,	TreasureChestTypes.ARMOIRE, Rarity.SCARCE, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
							.strength(2.5F)).setBounds(witherChestBounds);

			WITHER_CHEST_TOP = new WitherChestTopBlock(Treasure.MODID, TreasureConfig.ChestID.WITHER_CHEST_TOP_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
					.strength(2.5F));

			// TODO MIMICS

			// GRAVESTONES
			GRAVESTONE1_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_STONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_MOSSY_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_POLISHED_GRANITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_POLISHED_ANDESITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_POLISHED_DIORITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_OBSIDIAN_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE1_SMOOTH_QUARTZ = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_SMOOTH_QUARTZ_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
			GRAVESTONE2_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_STONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_MOSSY_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_POLISHED_GRANITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_POLISHED_ANDESITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_POLISHED_DIORITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_OBSIDIAN_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE2_SMOOTH_QUARTZ = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_SMOOTH_QUARTZ_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
			GRAVESTONE3_STONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_STONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_MOSSY_COBBLESTONE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_MOSSY_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_POLISHED_GRANITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_POLISHED_GRANITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_POLISHED_ANDESITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_POLISHED_ANDESITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_POLISHED_DIORITE = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_POLISHED_DIORITE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_OBSIDIAN = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_OBSIDIAN_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);
			GRAVESTONE3_SMOOTH_QUARTZ = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_SMOOTH_QUARTZ_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds3);

	        // gravestone spawners
	        GRAVESTONE1_SPAWNER_STONE = (GravestoneSpawnerBlock) new GravestoneSpawnerBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE1_SPAWNER_STONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds);
	        GRAVESTONE2_SPAWNER_COBBLESTONE = (GravestoneSpawnerBlock) new GravestoneSpawnerBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE2_SPAWNER_COBBLESTONE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
	        GRAVESTONE3_SPAWNER_OBSIDIAN = (GravestoneSpawnerBlock) new GravestoneSpawnerBlock(Treasure.MODID, TreasureConfig.BlockID.GRAVESTONE3_SPAWNER_OBSIDIAN_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds(gravestoneBounds2);
	        
	        // other markers
	        SKULL_CROSSBONES = new GravestoneBlock(Treasure.MODID, TreasureConfig.BlockID.SKULL_CROSSBONES_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE)).setBounds( TreasureShapeBuilder.buildSkullCrossbones());
	        SKELETON = new SkeletonBlock(Treasure.MODID, TreasureConfig.BlockID.SKELETON_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F).sound(SoundType.STONE));
	                
			// ORES
			TOPAZ_ORE = new TreasureOreBlock(Treasure.MODID, TreasureConfig.BlockID.TOPAZ_ORE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F, 5.0F).harvestLevel(3)); 
			ONYX_ORE = new TreasureOreBlock(Treasure.MODID, TreasureConfig.BlockID.ONYX_ORE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F, 5.0F).harvestLevel(3));
			RUBY_ORE = new TreasureOreBlock(Treasure.MODID, TreasureConfig.BlockID.RUBY_ORE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F, 5.0F).harvestLevel(3));
			SAPPHIRE_ORE = new TreasureOreBlock(Treasure.MODID, TreasureConfig.BlockID.SAPPHIRE_ORE_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(3.0F, 5.0F).harvestLevel(3));		
			
			// WISHING WELL BLOCKS
	        WISHING_WELL_BLOCK = new WishingWellBlock(Treasure.MODID, TreasureConfig.BlockID.WISHING_WELL_BLOCK_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(2.0F).sound(SoundType.STONE));
	        DESERT_WISHING_WELL_BLOCK = new WishingWellBlock(Treasure.MODID, TreasureConfig.BlockID.DESERT_WISHING_WELL_BLOCK_ID, Block.Properties.of(Material.STONE, MaterialColor.STONE)
	                .strength(2.0F).sound(SoundType.STONE));

			// WITHER BIOME BLOCKS
	        WITHER_BRANCH = new WitherBranchBlock(Treasure.MODID, TreasureConfig.BlockID.WITHER_BRANCH_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD));
	        WITHER_ROOT = new WitherRootBlock(Treasure.MODID, TreasureConfig.BlockID.WITHER_ROOT_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD));
	        WITHER_LOG = new WitherLogBlock(Treasure.MODID, TreasureConfig.BlockID.WITHER_LOG_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).sound(SoundType.WOOD));
	        WITHER_BROKEN_LOG = new WitherBrokenLogBlock(Treasure.MODID, TreasureConfig.BlockID.WITHER_BROKEN_LOG_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD));
	        WITHER_SOUL_LOG = new WitherSoulLog(Treasure.MODID, TreasureConfig.BlockID.WITHER_SOUL_LOG_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
	        		.strength(3.0F).sound(SoundType.WOOD));
	        WITHER_PLANKS = new TreasureBlock(Treasure.MODID, TreasureConfig.BlockID.WITHER_PLANKS_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD)
	        		.strength(2.5F));
	        
	        SPANISH_MOSS = new SpanishMossBlock(Treasure.MODID, TreasureConfig.BlockID.SPANISH_MOSS_ID, Block.Properties.of(Material.WOOD, MaterialColor.WOOD));

	        // TODO FALLING BLOCKS
	        FALLING_GRASS = new FallingGrassBlock(Treasure.MODID, TreasureConfig.BlockID.FALLING_GRASS_ID, Block.Properties.of(Material.DIRT, MaterialColor.WOOD)
	        		.strength(0.6F).sound(SoundType.GRAVEL));
	        
	        FALLING_SAND = new FallingSandBlock(Treasure.MODID, TreasureConfig.BlockID.FALLING_SAND_ID, Block.Properties.of(Material.SAND, MaterialColor.WOOD)
	        		.strength(0.6F).sound(SoundType.SAND));
	        
	        FALLING_RED_SAND= new FallingRedSandBlock(Treasure.MODID, TreasureConfig.BlockID.FALLING_RED_SAND_ID, Block.Properties.of(Material.SAND, MaterialColor.WOOD)
	        		.strength(0.6F).sound(SoundType.SAND));
	        
			// PAINTINGS
	        // NOTE not adding paintings
	        
			// proximity blocks
	        PROXIMITY_SPAWNER = new ProximityBlock(Treasure.MODID, TreasureConfig.BlockID.PROXIMITY_SPAWNER_ID,
					Block.Properties.of(Material.AIR).noCollission().noDrops());

	        /*
	         * add blocks to list
	         */
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
	        BLOCKS.add(CRYSTAL_SKULL_CHEST);
			BLOCKS.add(CAULDRON_CHEST);
			BLOCKS.add(SPIDER_CHEST);
	        BLOCKS.add(VIKING_CHEST);
	        BLOCKS.add(CARDBOARD_BOX);
	        BLOCKS.add(MILK_CRATE);
	        
	        BLOCKS.add(GRAVESTONE1_STONE);
	        BLOCKS.add(GRAVESTONE1_COBBLESTONE);
			BLOCKS.add(GRAVESTONE1_MOSSY_COBBLESTONE);
			BLOCKS.add(GRAVESTONE1_POLISHED_ANDESITE);
			BLOCKS.add(GRAVESTONE1_POLISHED_DIORITE);
			BLOCKS.add(GRAVESTONE1_POLISHED_GRANITE);
			BLOCKS.add(GRAVESTONE1_OBSIDIAN);
			BLOCKS.add(GRAVESTONE1_SMOOTH_QUARTZ);
			BLOCKS.add(GRAVESTONE2_STONE);
	        BLOCKS.add(GRAVESTONE2_COBBLESTONE);
			BLOCKS.add(GRAVESTONE2_MOSSY_COBBLESTONE);
			BLOCKS.add(GRAVESTONE2_POLISHED_ANDESITE);
			BLOCKS.add(GRAVESTONE2_POLISHED_DIORITE);
			BLOCKS.add(GRAVESTONE2_POLISHED_GRANITE);
			BLOCKS.add(GRAVESTONE2_OBSIDIAN);
			BLOCKS.add(GRAVESTONE2_SMOOTH_QUARTZ);
			BLOCKS.add(GRAVESTONE3_STONE);
	        BLOCKS.add(GRAVESTONE3_COBBLESTONE);
			BLOCKS.add(GRAVESTONE3_MOSSY_COBBLESTONE);
			BLOCKS.add(GRAVESTONE3_POLISHED_ANDESITE);
			BLOCKS.add(GRAVESTONE3_POLISHED_DIORITE);
			BLOCKS.add(GRAVESTONE3_POLISHED_GRANITE);
			BLOCKS.add(GRAVESTONE3_OBSIDIAN);
	        BLOCKS.add(GRAVESTONE3_SMOOTH_QUARTZ);
	        BLOCKS.add(SKULL_CROSSBONES);
	        BLOCKS.add(WISHING_WELL_BLOCK);
	        BLOCKS.add(DESERT_WISHING_WELL_BLOCK);
	        BLOCKS.add(TOPAZ_ORE);
	        BLOCKS.add(ONYX_ORE);
	        BLOCKS.add(RUBY_ORE);
	        BLOCKS.add(SAPPHIRE_ORE);
	        BLOCKS.add(WITHER_BROKEN_LOG);
	        BLOCKS.add(WITHER_LOG);
	        BLOCKS.add(WITHER_SOUL_LOG);
	        BLOCKS.add(WITHER_PLANKS);
	        BLOCKS.add(SPANISH_MOSS);
	        BLOCKS.add(FALLING_GRASS);
	        BLOCKS.add(FALLING_SAND);
	        BLOCKS.add(FALLING_RED_SAND);
	        BLOCKS.add(GRAVESTONE1_SPAWNER_STONE);
	        BLOCKS.add(GRAVESTONE2_SPAWNER_COBBLESTONE);
	        BLOCKS.add(GRAVESTONE3_SPAWNER_OBSIDIAN);
	        
	        /*
	         * add gravestones/markers to list
	         */
	        GRAVESTONES.add(GRAVESTONE1_STONE);
	        GRAVESTONES.add(GRAVESTONE1_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE1_MOSSY_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE1_POLISHED_ANDESITE);
			GRAVESTONES.add(GRAVESTONE1_POLISHED_DIORITE);
			GRAVESTONES.add(GRAVESTONE1_POLISHED_GRANITE);
			GRAVESTONES.add(GRAVESTONE1_OBSIDIAN);
			GRAVESTONES.add(GRAVESTONE1_SMOOTH_QUARTZ);
			GRAVESTONES.add(GRAVESTONE2_STONE);
	        GRAVESTONES.add(GRAVESTONE2_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE2_MOSSY_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE2_POLISHED_ANDESITE);
			GRAVESTONES.add(GRAVESTONE2_POLISHED_DIORITE);
			GRAVESTONES.add(GRAVESTONE2_POLISHED_GRANITE);
			GRAVESTONES.add(GRAVESTONE2_OBSIDIAN);
			GRAVESTONES.add(GRAVESTONE2_SMOOTH_QUARTZ);
			GRAVESTONES.add(GRAVESTONE3_STONE);
	        GRAVESTONES.add(GRAVESTONE3_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE3_MOSSY_COBBLESTONE);
			GRAVESTONES.add(GRAVESTONE3_POLISHED_ANDESITE);
			GRAVESTONES.add(GRAVESTONE3_POLISHED_DIORITE);
			GRAVESTONES.add(GRAVESTONE3_POLISHED_GRANITE);
			GRAVESTONES.add(GRAVESTONE3_OBSIDIAN);
	        GRAVESTONES.add(GRAVESTONE3_SMOOTH_QUARTZ);
	        GRAVESTONES.add(SKULL_CROSSBONES);
	        
	        GRAVESTONE_SPAWNERS.add(GRAVESTONE1_SPAWNER_STONE);
	        GRAVESTONE_SPAWNERS.add(GRAVESTONE2_SPAWNER_COBBLESTONE);
	        GRAVESTONE_SPAWNERS.add(GRAVESTONE3_SPAWNER_OBSIDIAN);
	        
	        /*
	         * register blocks
	         */
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
			registry.register(WITHER_CHEST_TOP);
			registry.register(WITHER_BRANCH);  // can be in main list
			registry.register(WITHER_ROOT);  // can be in main list
//			registry.register(WITHER_SOUL_LOG);
			 registry.register(PROXIMITY_SPAWNER);
			 registry.register(SKELETON); // can be in main list
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			// add all specialty blocks to an array
			final Block[] specialtyBlocks = { 
					WITHER_CHEST			
			};
			
			// TODO need this list ?
			final Block[] nonCreativeBlocks = {
					WITHER_CHEST_TOP,
					WITHER_BRANCH, // -- create an item for it
					WITHER_ROOT, // -- create an item for it
					PROXIMITY_SPAWNER, // --> not added to Treasure tab, not visible in creative
					SKELETON // --> create an item for it
			};
			
			for (Block b : BLOCKS) {
				BlockItem itemBlock = new BlockItem(b, new Item.Properties().tab(TreasureItemGroups.MOD_ITEM_GROUP));
				final ResourceLocation registryName = Preconditions.checkNotNull(b.getRegistryName(),
						"Block %s has null registry name", b);
				registry.register(itemBlock.setRegistryName(registryName));
				ITEM_BLOCKS.add(itemBlock);
			}
			
			for (Block b : specialtyBlocks) {
				BlockItem itemBlock = new BlockItem(b, new Item.Properties().tab(TreasureItemGroups.MOD_ITEM_GROUP));
				final ResourceLocation registryName = Preconditions.checkNotNull(b.getRegistryName(),
						"Block %s has null registry name", b);
				registry.register(itemBlock.setRegistryName(registryName));
				ITEM_BLOCKS.add(itemBlock);
			}
			
			// special case registry
//			BlockItem itemBlock = new BlockItem(WITHER_CHEST, new Item.Properties().group(TreasureItemGroups.MOD_ITEM_GROUP));
//			final ResourceLocation registryName = Preconditions.checkNotNull(WITHER_CHEST.getRegistryName(),
//					"Block %s has null registry name", WITHER_CHEST);
//			registry.register(itemBlock.setRegistryName(registryName));
//			ITEM_BLOCKS.add(itemBlock);
		}
		
		/**
		 * Register the {@link IBlockColor} handlers.
		 *
		 * @param event The event
		 */
		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent
		public static void registerBlockColors(ColorHandlerEvent.Block event) {
			event.getBlockColors().register(
					(state, reader, pos, color) -> {
						return (reader != null && pos != null) ? BiomeColors.getAverageGrassColor(reader, pos)  : GrassColors.get(0.5D, 1.0D);
					},
					TreasureBlocks.FALLING_GRASS);
		}
	}
}
