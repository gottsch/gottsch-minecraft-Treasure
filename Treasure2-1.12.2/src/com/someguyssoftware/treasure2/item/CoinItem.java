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
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
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

	public static final int MAX_STACK_SIZE = 8;
	private Coins coin;
	
	/**
	 * 
	 */
	public CoinItem (String modID, String name)	 {
		super();
		this.setItemName(modID, name);	
		this.setMaxStackSize(MAX_STACK_SIZE);
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
		World world = entityItem.getEntityWorld();
		if (WorldInfo.isClientSide(world)) {
			return super.onEntityItemUpdate(entityItem);
		}
		
		// get the position
		ICoords coords = new Coords(entityItem.getPosition());
		Cube cube = new Cube(world, coords);
//		Block block = world.getBlockState(coords).getBlock();
		Block block = cube.toBlock();
		int numWishingWellBlocks = 0;
		// check if in water
		if (cube.equalsBlock(Blocks.WATER)) {
			// check if the water block is adjacent to 2 wishing well blocks
			ICoords checkCoords = coords.add(-1, 0, -1);
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					Cube checkCube = new Cube(world, checkCoords);
					if (checkCube.equalsBlock(TreasureBlocks.WISHING_WELL_BLOCK)) {
						numWishingWellBlocks++;
					}					
					if (numWishingWellBlocks >= 2) {
						break;
					}
				}
			}
			
			List<LootTable> lootTables = new ArrayList<>();
			if (numWishingWellBlocks >=2) {
				Random random = new Random();
				List<Rarity> rarityList = null;
				// determine coin type
				if (getCoin() == Coins.SILVER) {
//					rarityList = Arrays.asList(new Rarity[] {Rarity.UNCOMMON, Rarity.SCARCE});
//					lootTables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.UNCOMMON));
//					lootTables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.SCARCE));
					lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.UNCOMMON));
					lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.SCARCE));
				}
				else if (getCoin() == Coins.GOLD) {					
//					rarityList = Arrays.asList(new Rarity[] {Rarity.SCARCE, Rarity.RARE});
//					lootTables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.SCARCE));
//					lootTables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.RARE));
					lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.SCARCE));
					lootTables.addAll(Treasure.LOOT_TABLES.getLootTableByRarity(Rarity.RARE));
				}
				
				// TODO handle if loot tables is null or size = 0
				
				// select a table
				LootTable table = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
				
				// generate a list of itemStacks from the table pools
				List<ItemStack> list =table.generateLootFromPools(random, Treasure.LOOT_TABLES.getContext());

				// select one item randomly
				ItemStack stack = list.get(list.size()-1);
				
				// spawn the item 
//				ItemStack stack = InventoryPopulator.toItemStack(random,  item);
				if (stack != null) {
					InventoryHelper.spawnItemStack(world, (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), stack);
				}

				// remove the item entity
				entityItem.setDead();
				return true;
			}
		}
		
		return super.onEntityItemUpdate(entityItem);
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
