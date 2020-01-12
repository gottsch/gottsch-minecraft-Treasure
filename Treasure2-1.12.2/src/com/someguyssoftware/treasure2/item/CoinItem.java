/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.IWishingWellBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Sep 13, 2014
 *
 */
public class CoinItem extends ModItem {
	private static final int MAX_CUSTOM_STACK_SIZE = 64;
	public static final int MAX_STACK_SIZE = 8;
		
	private Coins coin;
	
	/**
	 * 
	 */
	public CoinItem (String modID, String name)	 {
		super();
		this.setItemName(modID, name);	
//		this.setMaxStackSize(MAX_STACK_SIZE);
		this.setMaxStackSize(Math.min(MAX_CUSTOM_STACK_SIZE, TreasureConfig.COINS.maxStackSize));
		this.setCreativeTab(Treasure.TREASURE_TAB);
		// set the coin to gold by default
		this.coin = Coins.GOLD;
	}
	
	/**
	 * 
	 * @param coin
	 */
	public CoinItem(String modID, String name, Coins coin) {
		this(modID, name);
		this.setCoin(coin);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);		
		tooltip.add(TextFormatting.GOLD + I18n.translateToLocal("tooltip.label.coin"));
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		// get the item stack or number of items.
		ItemStack entityItemStack = entityItem.getItem();
		
		World world = entityItem.getEntityWorld();
		if (WorldInfo.isClientSide(world)) {
			return super.onEntityItemUpdate(entityItem);
		}
		
		// get the position
		ICoords coords = new Coords(entityItem.getPosition());
		Cube cube = new Cube(world, coords);
		int numWishingWellBlocks = 0;
		// check if in water
		if (cube.equalsBlock(Blocks.WATER)) {
			// check if the water block is adjacent to 2 wishing well blocks
			ICoords checkCoords = coords.add(-1, 0, -1);
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					Cube checkCube = new Cube(world, checkCoords);
//					if (checkCube.equalsBlock(TreasureBlocks.WISHING_WELL_BLOCK)) {
					if (checkCube.toBlock() instanceof IWishingWellBlock) {
						numWishingWellBlocks++;
					}					
					if (numWishingWellBlocks >= 2) {
						break;
					}
				}
			}
			
			if (numWishingWellBlocks >=2) {
				Random random = new Random();
				for (int itemIndex = 0; itemIndex < entityItemStack.getCount(); itemIndex++) {
					// generate an item for each item in the stack
					generateLootItem(world, random, entityItem, coords);
				}
				return true;
			}
		}
		
		return super.onEntityItemUpdate(entityItem);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param entityItem
	 * @param coords
	 */
	private void generateLootItem(World world, Random random, EntityItem entityItem, ICoords coords) {
		List<LootTable> lootTables = new ArrayList<>();

		// determine coin type
		if (getCoin() == Coins.SILVER) {
			lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.UNCOMMON));
			lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.SCARCE));
		}
		else if (getCoin() == Coins.GOLD) {					
			lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.SCARCE));
			lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.RARE));
		}
		
		ItemStack stack = null;
		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
		if (lootTables == null || lootTables.size() == 0) {
			stack = new ItemStack(Items.APPLE);
		}
		else {
			// select a table
			LootTable table = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
			
			// generate a list of itemStacks from the table pools
			List<ItemStack> list =table.generateLootFromPools(random, Treasure.LOOT_TABLES.getContext());

			// select one item randomly
			stack = list.get(RandomHelper.randomInt(0, list.size()-1));
		}				
		
		// spawn the item 
		if (stack != null) {
			InventoryHelper.spawnItemStack(world, (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), stack);
		}
		// remove the item entity
		entityItem.setDead();
	}

	/**
	 * @return the coin
	 */
	public Coins getCoin() {
		return coin;
	}
	/**
	 * @param coin the coin to set
	 */
	public CoinItem setCoin(Coins coin) {
		this.coin = coin;
		return this;
	}
}
