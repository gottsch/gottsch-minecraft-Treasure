package com.someguyssoftware.treasure2.model;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Treasure.MODID, value = Side.CLIENT)
public class TreasureModels {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		// TAB
		registerItemModel(TreasureItems.TREASURE_TAB);
		// There isn't a block model json for chests so you won't be able to get the
		// item from block.
		// CHESTS
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WOOD_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.CRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.MOLDY_CRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.IRONBOUND_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.PIRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.IRON_STRONGBOX));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GOLD_STRONGBOX));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.SAFE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.DREAD_PIRATE_CHEST));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WHALE_BONE_PIRATE_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.COMPRESSOR_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GOLD_SKULL_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.SKULL_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.CAULDRON_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.SPIDER_CHEST));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.VIKING_CHEST));
		
		// MIMICS
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WOOD_MIMIC));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.PIRATE_MIMIC));

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
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_STONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_MOSSY_COBBLESTONE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_POLISHED_GRANITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_POLISHED_ANDESITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_POLISHED_DIORITE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_OBSIDIAN));

		registerItemModel(Item.getItemFromBlock(TreasureBlocks.SKULL_CROSSBONES));
		registerItemModel(TreasureItems.SKELETON);

		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WISHING_WELL_BLOCK));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.DESERT_WISHING_WELL_BLOCK));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.FOG_BLOCK));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.HIGH_FOG_BLOCK));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.MED_FOG_BLOCK));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.LOW_FOG_BLOCK));
//		
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.HIGH_WITHER_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.MED_WITHER_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.LOW_WITHER_FOG));
//
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.POISON_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.HIGH_POISON_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.MED_POISON_FOG));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.LOW_POISON_FOG));

		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_LOG));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_BROKEN_LOG));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_LOG_SOUL));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_BRANCH));
//		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_ROOT));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.WITHER_PLANKS));

		// ORES/GEMS
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.SAPPHIRE_ORE));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.RUBY_ORE));

		// OTHER
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.FALLING_GRASS));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.FALLING_SAND));
		registerItemModel(Item.getItemFromBlock(TreasureBlocks.FALLING_RED_SAND));
		
		// COINS
		registerItemModel(TreasureItems.GOLD_COIN);
		registerItemModel(TreasureItems.SILVER_COIN);
		// PEARLS
		registerItemModel(TreasureItems.WHITE_PEARL);
		registerItemModel(TreasureItems.BLACK_PEARL);
		// LOCKS
		registerItemModel(TreasureItems.WOOD_LOCK);
		registerItemModel(TreasureItems.STONE_LOCK);
		registerItemModel(TreasureItems.IRON_LOCK);
		registerItemModel(TreasureItems.GOLD_LOCK);
		registerItemModel(TreasureItems.DIAMOND_LOCK);
		registerItemModel(TreasureItems.EMERALD_LOCK);
		registerItemModel(TreasureItems.RUBY_LOCK);
		registerItemModel(TreasureItems.SAPPHIRE_LOCK);
		registerItemModel(TreasureItems.SPIDER_LOCK);
		registerItemModel(TreasureItems.WITHER_LOCK);

		// KEYS
		registerItemModel(TreasureItems.WOOD_KEY);
		registerItemModel(TreasureItems.STONE_KEY);
		registerItemModel(TreasureItems.IRON_KEY);
		registerItemModel(TreasureItems.GOLD_KEY);
		registerItemModel(TreasureItems.DIAMOND_KEY);
		registerItemModel(TreasureItems.EMERALD_KEY);
		registerItemModel(TreasureItems.RUBY_KEY);
		registerItemModel(TreasureItems.SAPPHIRE_KEY);
		registerItemModel(TreasureItems.JEWELLED_KEY);
		registerItemModel(TreasureItems.METALLURGISTS_KEY);
		registerItemModel(TreasureItems.SKELETON_KEY);
		registerItemModel(TreasureItems.SPIDER_KEY);
		registerItemModel(TreasureItems.WITHER_KEY);
		registerItemModel(TreasureItems.PILFERERS_LOCK_PICK);
		registerItemModel(TreasureItems.THIEFS_LOCK_PICK);
		registerItemModel(TreasureItems.KEY_RING);

		// WITHER ITEMS
		registerItemModel(TreasureItems.WITHER_STICK_ITEM);
		registerItemModel(TreasureItems.WITHER_ROOT_ITEM);

		// WEAPONS
		registerItemModel(TreasureItems.SKULL_SWORD);
		registerItemModel(TreasureItems.EYE_PATCH);

		// PAINTINGS
		registerItemModel(TreasureItems.PAINTING_BLOCKS_BRICKS);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_COBBLESTONE);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_DIRT);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_LAVA);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_SAND);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_WATER);
		registerItemModel(TreasureItems.PAINTING_BLOCKS_WOOD);

		// OTHER
		registerItemModel(TreasureItems.SPANISH_MOSS);
		registerItemModel(TreasureItems.SAPPHIRE);
		registerItemModel(TreasureItems.RUBY);
		registerItemModel(TreasureItems.TREASURE_TOOL);

		// FOOD
//		registerItemModel(TreasureItems.OYSTER_MEAT);
//		registerItemModel(TreasureItems.OYSTER_STEW);
//		registerItemModel(TreasureItems.CLAM_MEAT);
//		registerItemModel(TreasureItems.CLAM_STEW);

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

	/**
	 * Register the {@link IBlockColor} handlers.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerBlockColourHandlers(final ColorHandlerEvent.Block event) {
		final BlockColors blockColors = event.getBlockColors();

		// Use the grass colour of the biome or the default grass colour
		final IBlockColor grassColourHandler = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null) {
				return BiomeColorHelper.getGrassColorAtPos(blockAccess, pos);
			}

			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
		};

		blockColors.registerBlockColorHandler(grassColourHandler, TreasureBlocks.FALLING_GRASS);
	}

	/**
	 * Register the {@link IItemColor} handlers
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) {
		final BlockColors blockColors = event.getBlockColors();
		final ItemColors itemColors = event.getItemColors();

		// Use the Block's colour handler for an ItemBlock
		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			@SuppressWarnings("deprecation")
			final IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return blockColors.colorMultiplier(state, null, null, tintIndex);
		};

		itemColors.registerItemColorHandler(itemBlockColourHandler, TreasureBlocks.FALLING_GRASS);
	}
}
