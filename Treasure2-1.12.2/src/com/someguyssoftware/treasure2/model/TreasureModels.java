package com.someguyssoftware.treasure2.model;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Treasure.MODID, value =  Side.CLIENT)
public class TreasureModels {	
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		// TAB
		registerItemModel(TreasureItems.TREASURE_TAB);
		// There isn't a block model json for chests so you won't be able to get the item from block.
		// CHESTS
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WOOD_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.CRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.MOLDY_CRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.IRONBOUND_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.PIRATE_CHEST));
		
		// GRAVESONES
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_STONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_MOSSY_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_POLISHED_GRANITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_POLISHED_ANDESITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_POLISHED_DIORITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_OBSIDIAN));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_STONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_MOSSY_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_POLISHED_GRANITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_POLISHED_ANDESITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_POLISHED_DIORITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_OBSIDIAN));
		
		// COINS
		registerItemModel(TreasureItems.GOLD_COIN);
		registerItemModel(TreasureItems.SILVER_COIN);
		// LOCKS
		registerItemModel(TreasureItems.WOOD_LOCK);
		registerItemModel(TreasureItems.STONE_LOCK);
		registerItemModel(TreasureItems.IRON_LOCK);
		registerItemModel(TreasureItems.GOLD_LOCK);
		registerItemModel(TreasureItems.DIAMOND_LOCK);
		registerItemModel(TreasureItems.EMERALD_LOCK);
		// KEYS
		registerItemModel(TreasureItems.WOOD_KEY);
		registerItemModel(TreasureItems.STONE_KEY);
		registerItemModel(TreasureItems.IRON_KEY);
		registerItemModel(TreasureItems.GOLD_KEY);
		registerItemModel(TreasureItems.DIAMOND_KEY);	
		registerItemModel(TreasureItems.EMERALD_KEY);
		registerItemModel(TreasureItems.METALLURGISTS_KEY);
		registerItemModel(TreasureItems.SKELETON_KEY);
		
//		// variants
//		Item gravestoneItem = Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1);
//		ModelResourceLocation itemModelResourceLocation = 
//				   new ModelResourceLocation("treasure2:gravestone1_t1_e1", "inventory");
//		ModelLoader.setCustomModelResourceLocation(gravestoneItem,  0, itemModelResourceLocation);

	
	}
	
	/**
	 * Register the default model for an {@link Item}.
	 *
	 * @param item The item
	 */
	private static void registerItemModel(Item item) {
		final ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> location));			
	}
}
