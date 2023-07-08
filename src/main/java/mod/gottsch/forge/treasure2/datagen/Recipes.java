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
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * 
 * @author Mark Gottschling on Nov 26, 2022
 *
 */
public class Recipes extends RecipeProvider {

		public Recipes(PackOutput output) {
			super(output);
		}

		@Override
		protected void buildRecipes(Consumer<FinishedRecipe> recipe) {
			/*
			 * treasure tool
			 */
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.TREASURE_TOOL.get())
	        .pattern("  i")
	        .pattern(" s ")
	        .pattern("x  ")
	        .define('i', Items.IRON_INGOT)
	        .define('s', Items.STICK)
	        .define('x', Items.STONE)
	        .unlockedBy("has_stick", InventoryChangeTrigger.TriggerInstance.hasItems(Items.STICK))
	        .save(recipe);	   
	        
	        // pilferer's lock pick
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.PILFERERS_LOCK_PICK.get())
	        .pattern("xt")
	        .pattern("x ")
	        .define('x', Items.IRON_NUGGET)
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // thief's lock pick
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.THIEFS_LOCK_PICK.get())
	        .pattern("xt")
	        .pattern("x ")
	        .define('x', Items.IRON_INGOT)
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // keyring
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.KEY_RING.get())
	        .pattern("kx ")
	        .pattern("x x")
	        .pattern(" x ")
	        .define('x', Items.IRON_INGOT)
	        .define('k', Ingredient.of(TreasureItems.WOOD_KEY.get(), 
	        		TreasureItems.STONE_KEY.get(),
	        		TreasureItems.IRON_KEY.get()))
	        .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
	        .save(recipe);
	        
	        // pouch
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.POUCH.get())
	        .pattern(" ct")
	        .pattern(" x ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('c', Ingredient.of(
	        		TreasureItems.COPPER_COIN.get(),
	        		TreasureItems.SILVER_COIN.get(),
	        		TreasureItems.GOLD_COIN.get()))
	        .define('x', Items.LEATHER)
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // spider key
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.SPIDER_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" e ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.IRON_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('e', Items.SPIDER_EYE)
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // topaz key
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.TOPAZ_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" g ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('g', TreasureItems.TOPAZ.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // onyx key
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.ONYX_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" g ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('g', TreasureItems.ONYX.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);   
	        
	        // ruby key
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.RUBY_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" r ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('r', TreasureItems.RUBY.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);
	        
	        // sapphire
	        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, TreasureItems.SAPPHIRE_KEY.get())
	        .pattern("kt ")
	        .pattern(" d ")
	        .pattern(" s ")
	        .define('t', TreasureItems.TREASURE_TOOL.get())
	        .define('k', TreasureItems.GOLD_KEY.get())
	        .define('d', Items.GLOWSTONE_DUST)
	        .define('s', TreasureItems.SAPPHIRE.get())
	        .unlockedBy("has_tool", InventoryChangeTrigger.TriggerInstance.hasItems(TreasureItems.TREASURE_TOOL.get()))
	        .save(recipe);

	        // copper weapons smelting
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.CHIPPED_COPPER_SHORT_SWORD.get()), RecipeCategory.COMBAT, 
	                Items.COPPER_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.CHIPPED_COPPER_SHORT_SWORD.get()).build()))
	        .save(recipe, "copper_ingot_from_chipped_short_sword");
			
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.COPPER_SHORT_SWORD.get()), RecipeCategory.COMBAT, 
	                Items.COPPER_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.COPPER_SHORT_SWORD.get()).build()))
	        .save(recipe, "copper_ingot_from_short_sword");
			
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.COPPER_BROAD_AXE.get()), RecipeCategory.COMBAT, 
	                Items.COPPER_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.COPPER_BROAD_AXE.get()).build()))
	        .save(recipe, "copper_ingot_from_broad_axe");
			
			// iron weapons smelting
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.CHIPPED_IRON_SHORT_SWORD.get()), RecipeCategory.COMBAT, 
	                Items.IRON_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.CHIPPED_IRON_SHORT_SWORD.get()).build()))
	        .save(recipe, "iron_ingot_from_chipped_short_sword");
			
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.IRON_SHORT_SWORD.get()), RecipeCategory.COMBAT, 
	                Items.IRON_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.IRON_SHORT_SWORD.get()).build()))
	        .save(recipe, "iron_ingot_from_short_sword");
			
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.IRON_BROAD_AXE.get()), RecipeCategory.COMBAT, 
	                Items.IRON_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.IRON_BROAD_AXE.get()).build()))
	        .save(recipe, "iron_ingot_from_broad_axe");
			
			SimpleCookingRecipeBuilder.smelting(Ingredient.of(TreasureItems.IRON_DWARVEN_AXE.get()), RecipeCategory.COMBAT, 
	                Items.IRON_INGOT, 1.0f, 200)
	        .unlockedBy("has_weapon", inventoryTrigger(ItemPredicate.Builder.item().of(TreasureItems.IRON_DWARVEN_AXE.get()).build()))
	        .save(recipe, "iron_ingot_from_dwarven_axe");
		}
}
