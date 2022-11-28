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

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import mod.gottsch.forge.treasure2.core.block.entity.WoodChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.lock.LockLayouts;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
			LockLayouts.STANDARD, Rarity.COMMON, Properties.of(Material.METAL, MaterialColor.WOOD).strength(2.5F).lightLevel(light)));
	
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
	
	// other
	public static final RegistryObject<Block> SPANISH_MOSS = Registration.BLOCKS.register("spanish_moss", () -> new SpanishMossBlock(Properties.of(Material.WOOD, MaterialColor.WOOD)));
	public static final RegistryObject<Block> WISHING_WELL = Registration.BLOCKS.register("wishing_well_block", () -> new WishingWellBlock(
			Properties.of(Material.STONE, MaterialColor.STONE).strength(2.0F).sound(SoundType.STONE)));
	public static final RegistryObject<Block> DESERT_WISHING_WELL = Registration.BLOCKS.register("desert_wishing_well_block", () -> new WishingWellBlock(
			Properties.of(Material.STONE, MaterialColor.STONE).strength(2.0F).sound(SoundType.STONE)));
	
	/**
	 * 
	 */
	public static void register() {
		// cycle through all block and create items
		Registration.registerBlocks();
	}	

}
