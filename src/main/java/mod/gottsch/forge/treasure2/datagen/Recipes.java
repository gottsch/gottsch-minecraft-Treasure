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
package mod.gottsch.forge.treasure2.datagen;

import java.util.function.Consumer;

import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 
 * @author Mark Gottschling on Nov 26, 2022
 *
 */
public class Recipes extends RecipeProvider {

		public Recipes(DataGenerator generator) {
			super(generator);
		}

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipe) {
			/*
			 * treasure tool
			 */
	        ShapedRecipeBuilder.shaped(TreasureItems.TREASURE_TOOL.get())
	        .pattern("  i")
	        .pattern(" s ")
	        .pattern("x  ")
	        .define('i', Items.IRON_INGOT)
	        .define('s', Items.STICK)
	        .define('x', Items.STONE)
	        .group("treasure2")
	        .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
	        .save(recipe);	   
	        
	        // pilferer's lock pick
	        ShapedRecipeBuilder.shaped(TreasureItems.PILFERERS_LOCK_PICK.get())
	        .pattern("xt")
	        .pattern("x ")
	        .define('x', Items.IRON_NUGGET)
	        .define('t', Items.STICK)
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // thief's lock pick
	        ShapedRecipeBuilder.shaped(TreasureItems.THIEFS_LOCK_PICK.get())
	        .pattern("xt")
	        .pattern("x ")
	        .define('x', Items.IRON_INGOT)
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // keyring
	        ShapedRecipeBuilder.shaped(TreasureItems.KEY_RING.get())
	        .pattern("kx ")
	        .pattern("x x")
	        .pattern(" x ")
	        .define('x', Items.IRON_INGOT)
	        .define('k', Ingredient.of(TreasureItems.WOOD_KEY.get(), 
	        		TreasureItems.STONE_KEY.get(),
	        		TreasureItems.IRON_KEY.get()))
	        .group("treasure2")
	        .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
	        .save(recipe);
	        
	        // pouch
	        ShapedRecipeBuilder.shaped(TreasureItems.POUCH.get())
	        .pattern(" ct")
	        .pattern(" x ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('c', Ingredient.of(
	        		TreasureItems.COPPER_COIN.get(),
	        		TreasureItems.SILVER_COIN.get(),
	        		TreasureItems.GOLD_COIN.get()))
	        .define('x', Items.LEATHER)
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // spider key
	        ShapedRecipeBuilder.shaped(TreasureItems.SPIDER_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" e ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.IRON_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('e', Items.SPIDER_EYE)
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // ruby key
	        ShapedRecipeBuilder.shaped(TreasureItems.RUBY_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" r ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('r', TreasureItems.RUBY.get())
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // sapphire
	        ShapedRecipeBuilder.shaped(TreasureItems.SAPPHIRE_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" s ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('s', TreasureItems.SAPPHIRE.get())
	        .group("treasure2")
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
		}
}
