/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import mod.gottsch.forge.treasure2.core.block.entity.CardboardBoxBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.CauldronChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.CompressorChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.CrateChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.CrystalSkullChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.DreadPirateChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.GoldSkullChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.GoldStrongboxBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.IronStrongboxBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.IronboundChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.MilkCrateBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.MoldyCrateChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.PirateChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.SafeBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.SkullChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.SpiderChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.VikingChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.WitherChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.WoodChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.lock.LockLayouts;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 14, 2022
 *
 */
public class TreasureBlocks {
	
	// functional interfaces
	static ToIntFunction<BlockState> light = (state) -> {
		if (Config.SERVER.effects.enableUndiscoveredEffects.get()
				&& ITreasureChestBlock.getUndiscovered(state)) {
			return 14;
		}
		return 0;
	};
	
	// chests
	public static final RegistryObject<Block> WOOD_CHEST = Registration.BLOCKS.register("wood_chest", () -> new StandardChestBlock(WoodChestBlockEntity.class,
			LockLayouts.STANDARD, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> CRATE_CHEST = Registration.BLOCKS.register("crate_chest", () -> new StandardChestBlock(CrateChestBlockEntity.class,
			LockLayouts.CRATE, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> MOLDY_CRATE_CHEST = Registration.BLOCKS.register("crate_chest_moldy", () -> new StandardChestBlock(MoldyCrateChestBlockEntity.class,
			LockLayouts.CRATE, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> IRONBOUND_CHEST = Registration.BLOCKS.register("ironbound_chest", () -> new StandardChestBlock(IronboundChestBlockEntity.class,
			LockLayouts.STANDARD, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> PIRATE_CHEST = Registration.BLOCKS.register("pirate_chest", () -> new StandardChestBlock(PirateChestBlockEntity.class,
			LockLayouts.STANDARD, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> SAFE = Registration.BLOCKS.register("safe", () -> new StandardChestBlock(SafeBlockEntity.class,
			LockLayouts.SAFE, Properties.of(Material.METAL, MaterialColor.METAL).strength(4.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> IRON_STRONGBOX = Registration.BLOCKS.register("iron_strongbox", () -> new StandardChestBlock(IronStrongboxBlockEntity.class,
			LockLayouts.STRONGBOX, Properties.of(Material.METAL, MaterialColor.METAL).strength(4.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> GOLD_STRONGBOX = Registration.BLOCKS.register("gold_strongbox", () -> new StandardChestBlock(GoldStrongboxBlockEntity.class,
			LockLayouts.STRONGBOX, Properties.of(Material.METAL, MaterialColor.METAL).strength(4.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> DREAD_PIRATE_CHEST = Registration.BLOCKS.register("dread_pirate_chest", () -> new StandardChestBlock(DreadPirateChestBlockEntity.class,
			LockLayouts.STANDARD, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(4.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> COMPRESSOR_CHEST = Registration.BLOCKS.register("compressor_chest", () -> new StandardChestBlock(CompressorChestBlockEntity.class,
			LockLayouts.COMPRESSOR, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> SKULL_CHEST = Registration.BLOCKS.register("skull_chest", () -> new StandardChestBlock(SkullChestBlockEntity.class,
			LockLayouts.SKULL, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> GOLD_SKULL_CHEST = Registration.BLOCKS.register("gold_skull_chest", () -> new StandardChestBlock(GoldSkullChestBlockEntity.class,
			LockLayouts.SKULL, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> CRYSTAL_SKULL_CHEST = Registration.BLOCKS.register("crystal_skull_chest", () -> new StandardChestBlock(CrystalSkullChestBlockEntity.class,
			LockLayouts.SKULL, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> CAULDRON_CHEST = Registration.BLOCKS.register("cauldron_chest", () -> new StandardChestBlock(CauldronChestBlockEntity.class,
			LockLayouts.TOP_SPLIT, Properties.of(Material.METAL, MaterialColor.METAL).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> SPIDER_CHEST = Registration.BLOCKS.register("spider_chest", () -> new StandardChestBlock(SpiderChestBlockEntity.class,
			LockLayouts.SINGLE_STANDARD, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)));
	
	public static final RegistryObject<Block> VIKING_CHEST = Registration.BLOCKS.register("viking_chest", () -> new StandardChestBlock(VikingChestBlockEntity.class,
			LockLayouts.VIKING, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F).lightLevel(light)).setBounds(TreasureShapeBuilder.buildVikingChest()));
	
	public static final RegistryObject<Block> CARDBOARD_BOX = Registration.BLOCKS.register("cardboard_box", () -> new StandardChestBlock(CardboardBoxBlockEntity.class,
			LockLayouts.TOP_SPLIT, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> MILK_CRATE = Registration.BLOCKS.register("milk_crate", () -> new StandardChestBlock(MilkCrateBlockEntity.class,
			LockLayouts.MILK_CRATE, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> WITHER_CHEST = Registration.BLOCKS.register("wither_chest", () -> new WitherChestBlock(WitherChestBlockEntity.class,
			LockLayouts.ARMOIRE, Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
	public static final RegistryObject<Block> WITHER_CHEST_TOP = Registration.BLOCKS.register("wither_chest_top", () -> new WitherChestTopBlock(Properties.of(Material.WOOD, MaterialColor.WOOD).strength(2.5F)));
	
	// ore
	public static final Supplier<Properties> ORE_PROPS = () -> Properties.of(Material.STONE, MaterialColor.STONE).strength(3.0F, 5.0F);
	public static final Supplier<Properties> DEEPSLATE_ORE_PROPS = () -> Properties.of(Material.STONE, MaterialColor.STONE).strength(3.0F, 6.0F);
	
	// TODO look up deepslate hardness
	public static final RegistryObject<Block> TOPAZ_ORE = Registration.BLOCKS.register("topaz_ore", () -> new Block(ORE_PROPS.get()));
	public static final RegistryObject<Block> DEEPSLATE_TOPAZ_ORE = Registration.BLOCKS.register("deepslate_topaz_ore", () -> new Block(DEEPSLATE_ORE_PROPS.get()));
	
	public static final RegistryObject<Block> ONYX_ORE = Registration.BLOCKS.register("onyx_ore", () -> new Block(ORE_PROPS.get()));
	public static final RegistryObject<Block> DEEPSLATE_ONYX_ORE = Registration.BLOCKS.register("deepslate_onyx_ore", () -> new Block(DEEPSLATE_ORE_PROPS.get()));
	
	public static final RegistryObject<Block> RUBY_ORE = Registration.BLOCKS.register("ruby_ore", () -> new Block(ORE_PROPS.get()));
	public static final RegistryObject<Block> DEEPSLATE_RUBY_ORE = Registration.BLOCKS.register("deepslate_ruby_ore", () -> new Block(DEEPSLATE_ORE_PROPS.get()));
	
	public static final RegistryObject<Block> SAPPHIRE_ORE = Registration.BLOCKS.register("sapphire_ore", () -> new Block(ORE_PROPS.get()));
	public static final RegistryObject<Block> DEEPSLATE_SAPPHIRE_ORE = Registration.BLOCKS.register("deepslate_sapphire_ore", () -> new Block(DEEPSLATE_ORE_PROPS.get()));
	
	// gravestones
	public static final RegistryObject<Block> GRAVESTONE1_STONE = Registration.BLOCKS.register("gravestone1_stone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE1_COBBLESTONE = Registration.BLOCKS.register("gravestone1_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE1_MOSSY_COBBLESTONE = Registration.BLOCKS.register("gravestone1_mossy_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE1_POLISHED_GRANITE = Registration.BLOCKS.register("gravestone1_polished_granite", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE1_OBSIDIAN = Registration.BLOCKS.register("gravestone1_obsidian", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE1_SMOOTH_QUARTZ = Registration.BLOCKS.register("gravestone1_smooth_quartz", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	
	public static final RegistryObject<Block> GRAVESTONE2_STONE = Registration.BLOCKS.register("gravestone2_stone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE2_COBBLESTONE = Registration.BLOCKS.register("gravestone2_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE2_MOSSY_COBBLESTONE = Registration.BLOCKS.register("gravestone2_mossy_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE2_POLISHED_GRANITE = Registration.BLOCKS.register("gravestone2_polished_granite", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE2_OBSIDIAN = Registration.BLOCKS.register("gravestone2_obsidian", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE2_SMOOTH_QUARTZ = Registration.BLOCKS.register("gravestone2_smooth_quartz", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	
	public static final RegistryObject<Block> GRAVESTONE3_STONE = Registration.BLOCKS.register("gravestone3_stone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	public static final RegistryObject<Block> GRAVESTONE3_COBBLESTONE = Registration.BLOCKS.register("gravestone3_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	public static final RegistryObject<Block> GRAVESTONE3_MOSSY_COBBLESTONE = Registration.BLOCKS.register("gravestone3_mossy_cobblestone", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	public static final RegistryObject<Block> GRAVESTONE3_POLISHED_GRANITE = Registration.BLOCKS.register("gravestone3_polished_granite", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	public static final RegistryObject<Block> GRAVESTONE3_OBSIDIAN = Registration.BLOCKS.register("gravestone3_obsidian", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	public static final RegistryObject<Block> GRAVESTONE3_SMOOTH_QUARTZ = Registration.BLOCKS.register("gravestone3_smooth_quartz", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));
	
	public static final RegistryObject<Block> SKULL_CROSSBONES = Registration.BLOCKS.register("skull_and_crossbones", () -> new GravestoneBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildSkullCrossbones()));
	
	public static final RegistryObject<Block> SKELETON = Registration.BLOCKS.register("skeleton", () -> new SkeletonBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE).strength(3.0F).sound(SoundType.STONE)));
	
	// gravestone spawners
	public static final RegistryObject<Block> GRAVESTONE1_SPAWNER_STONE = Registration.BLOCKS.register("gravestone1_spawner_stone", () -> new GravestoneSpawnerBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone1()));
	public static final RegistryObject<Block> GRAVESTONE2_SPAWNER_COBBLESTONE = Registration.BLOCKS.register("gravestone2_spawner_cobblestone", () -> new GravestoneSpawnerBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone2()));
	public static final RegistryObject<Block> GRAVESTONE3_SPAWNER_OBSIDIAN = Registration.BLOCKS.register("gravestone3_spawner_obsidian", () -> new GravestoneSpawnerBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE)
            .strength(3.0F).sound(SoundType.STONE)).setBounds(TreasureShapeBuilder.buildGravestone3()));

	
	// other
	public static final RegistryObject<Block> SPANISH_MOSS = Registration.BLOCKS.register("spanish_moss", () -> new SpanishMossBlock(Properties.of(Material.WOOD, MaterialColor.WOOD)));
	public static final RegistryObject<Block> WISHING_WELL = Registration.BLOCKS.register("wishing_well_block", () -> new WishingWellBlock(
			Properties.of(Material.STONE, MaterialColor.STONE).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> DESERT_WISHING_WELL = Registration.BLOCKS.register("desert_wishing_well_block", () -> new WishingWellBlock(
			Properties.of(Material.STONE, MaterialColor.STONE).strength(2.0F).sound(SoundType.STONE)));
	
	// collections
	public static final List<RegistryObject<Block>> CHESTS = new ArrayList<>(25);
	public static final List<RegistryObject<Block>> GRAVESTONES = new ArrayList<>(25);
	public static final List<RegistryObject<Block>> GRAVESTONE_SPAWNERS = new ArrayList<>(3);
	
	static {
		CHESTS.add(WOOD_CHEST);
		CHESTS.add(CRATE_CHEST);
		CHESTS.add(MOLDY_CRATE_CHEST);
		CHESTS.add(IRONBOUND_CHEST);
		CHESTS.add(PIRATE_CHEST);
		CHESTS.add(SAFE);
		CHESTS.add(IRON_STRONGBOX);
		CHESTS.add(GOLD_STRONGBOX);
		CHESTS.add(DREAD_PIRATE_CHEST);
		CHESTS.add(COMPRESSOR_CHEST);
		CHESTS.add(SKULL_CHEST);
		CHESTS.add(GOLD_SKULL_CHEST);
		CHESTS.add(CRYSTAL_SKULL_CHEST);
		CHESTS.add(CAULDRON_CHEST);
		CHESTS.add(SPIDER_CHEST);
		CHESTS.add(VIKING_CHEST);
		CHESTS.add(CARDBOARD_BOX);
		CHESTS.add(MILK_CRATE);
		CHESTS.add(WITHER_CHEST);		
		
		GRAVESTONES.add(GRAVESTONE1_STONE);
		GRAVESTONES.add(GRAVESTONE1_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE1_MOSSY_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE1_POLISHED_GRANITE);
		GRAVESTONES.add(GRAVESTONE1_OBSIDIAN);
		GRAVESTONES.add(GRAVESTONE1_SMOOTH_QUARTZ);
		
		GRAVESTONES.add(GRAVESTONE2_STONE);
		GRAVESTONES.add(GRAVESTONE2_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE2_MOSSY_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE2_POLISHED_GRANITE);
		GRAVESTONES.add(GRAVESTONE2_OBSIDIAN);
		GRAVESTONES.add(GRAVESTONE2_SMOOTH_QUARTZ);
		
		GRAVESTONES.add(GRAVESTONE3_STONE);
		GRAVESTONES.add(GRAVESTONE3_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE3_MOSSY_COBBLESTONE);
		GRAVESTONES.add(GRAVESTONE3_POLISHED_GRANITE);
		GRAVESTONES.add(GRAVESTONE3_OBSIDIAN);
		GRAVESTONES.add(GRAVESTONE3_SMOOTH_QUARTZ);
		GRAVESTONES.add(SKULL_CROSSBONES);
		
		GRAVESTONE_SPAWNERS.add(GRAVESTONE1_SPAWNER_STONE);
		GRAVESTONE_SPAWNERS.add(GRAVESTONE2_SPAWNER_COBBLESTONE);
		GRAVESTONE_SPAWNERS.add(GRAVESTONE3_SPAWNER_OBSIDIAN);
	}
	
	/**
	 * 
	 */
	public static void register() {
		// cycle through all block and create items
		Registration.registerBlocks();
	}	

}