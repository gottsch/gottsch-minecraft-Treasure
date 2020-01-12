/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.IWishingWellBlock;
import com.someguyssoftware.treasure2.enums.Pearls;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster.SpecialLootTables;

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
 * @author Mark Gottschling on Aug 18, 2019
 *
 */
public class PearlItem extends ModItem {

	public static final int MAX_STACK_SIZE = 8;
	private Pearls pearl;
	
	/**
	 * 
	 */
	public PearlItem (String modID, String name)	 {
		super();
		this.setItemName(modID, name);	
		this.setMaxStackSize(MAX_STACK_SIZE);
		this.setCreativeTab(Treasure.TREASURE_TAB);
		this.pearl = Pearls.WHITE;
	}
	
	/**
	 * 
	 * @param pearl
	 */
	public PearlItem(String modID, String name, Pearls pearl) {
		this(modID, name);
		this.setPearl(pearl);
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
			
			List<LootTable> lootTables = new ArrayList<>();
			if (numWishingWellBlocks >=2) {
				Random random = new Random();

				// determine pearl type
				if (getPearl() == Pearls.WHITE) {
					lootTables.add(Treasure.LOOT_TABLES.getSpecialLootTable(SpecialLootTables.WHITE_PEARL_WELL));
				}
				else if (getPearl() == Pearls.BLACK) {
					lootTables.add(Treasure.LOOT_TABLES.getSpecialLootTable(SpecialLootTables.BLACK_PEARL_WELL));
				}
				
				ItemStack stack = null;
				// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
				if (lootTables == null || lootTables.size() == 0) {
					stack = new ItemStack(Items.DIAMOND);
				}
				else {
					// select a table
					LootTable table = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
					
					// generate a list of itemStacks from the table pools
					List<ItemStack> list =table.generateLootFromPools(random, Treasure.LOOT_TABLES.getContext());

					// select one item randomly
					stack = list.get(random.nextInt(list.size()));
				}				
				
				// spawn the item 
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
	 * @return the pearl
	 */
	public Pearls getPearl() {
		return pearl;
	}
	/**
	 * @param pearl the pearl to set
	 */
	public PearlItem setPearl(Pearls pearl) {
		this.pearl = pearl;
		return this;
	}
}
