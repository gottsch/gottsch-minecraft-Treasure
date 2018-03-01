/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

/**
 * @author Mark Gottschling on Sep 19, 2014
 *
 */
public class WishingWellBlock extends ModBlock {
	// logger
	public static Logger logger = LogManager.getLogger(WishingWellBlock.class);
	
	/**
	 * 
	 * @param material
	 */
	public WishingWellBlock(String modID, String name, Material material) {
		super(modID, name, material	);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		this.setHardness(2.0F);	
	}

	/**
	 * Drops vanilla mossy cobblestone instead of wishing well block i.e. the well loses it's magic on break.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE);
    }
//	
//	@Override
//	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
//		// get the item held in player's hand
//		ItemStack heldItem = player.getHeldItem();
//		ItemStack wishStack = null;
//		
//		// if the player is holding a coin
//		if (heldItem != null && heldItem.getItem() instanceof CoinItem) {
//			CoinItem coin = (CoinItem) heldItem.getItem();
//			switch (coin.getCoin()) {
//			case SILVER:		
//				// generate lesser item
//				wishStack = generateItemFromSilverCoin();
//				
//				logger.debug("Using SILVER coin for loot!");
//				break;
//			case GOLD:
//				// generate greater item
//				wishStack = generateItemFromGoldCoin();
//				logger.debug("Using GOLD coin for loot!");
//				break;
//			default:
//				break;
//			}
//			
//			// remove coin;
//			heldItem.stackSize--;
//			// play sound
//			world.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.break", 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
//
//			// remove item from inventory if it's stack size is 0
//			if (heldItem.stackSize <=0) {
//				IInventory inventory = player.inventory;
//				inventory.setInventorySlotContents(player.inventory.currentItem, null);
//			}
//
//            // create an entity of the wish item
//			if (wishStack != null) {
//				Random random = new Random();
//				float f = random.nextFloat() * 0.8F + 0.1F;
//               float f1 = random.nextFloat() * 0.8F + 0.1F;
//               float f2 = random.nextFloat() * 0.8F + 0.1F;
//
//               // create item                
//               EntityItem entityItem = new EntityItem(world, (double)((float)pos.getX() + f), (double)((float)pos.getY() + f1), (double)((float)pos.getZ() + f2), wishStack);
//               
//				// place the entity in the world
//               if (!world.isRemote) {
//            	   world.spawnEntityInWorld(entityItem);
//               }				      
//			}
//			
//			return true;
//		}
//	
//        return false;
//    }
//
//	// FUTURE list should be pulled from JSON config file
//	/**
//	 * 
//	 * @return
//	 */
//	private ItemStack generateItemFromSilverCoin() {
//		Random random = new Random();
//		ItemStack stack = null;
//		
//		int itemIndex = random.nextInt(15);
//		switch(itemIndex) {
//		case 0:
//			stack = new ItemStack(Items.iron_chestplate,1);
//			stack = TreasureChestUtil.addEnchantment(stack);			
//			break;
//		case 1:
//			stack = new ItemStack(Items.iron_leggings,1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 2:
//			stack = new ItemStack(Items.iron_helmet,1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 3:
//			stack = new ItemStack(Items.iron_boots,1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 4:
//			stack = new ItemStack(Items.iron_sword,1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 5:
//			stack = new ItemStack(TreasureItems.silverCoin, random.nextInt(3) + 1);
//			break;
//		case 6:
//			stack = new ItemStack(Items.diamond, random.nextInt(3) + 1);
//			break;
//		case 7:
//		case 8:
//			stack = new ItemStack(Items.potionitem, 1, TreasureChestUtil.getPotions()[random.nextInt(TreasureChestUtil.getPotions().length)]);
//			break;
//		case 9:
//			stack = new ItemStack(Items.iron_pickaxe, 1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 10:
//			stack = new ItemStack(Items.tnt_minecart, random.nextInt(5) + 1);
//			break;
//		case 11:
//			stack = new ItemStack(Items.arrow, random.nextInt(32) + 1);
//			break;
//		case 12:
//			stack = new ItemStack(Items.ender_pearl, random.nextInt(2) + 1);
//			break;
//		case 13:
//			stack = new ItemStack(Items.iron_ingot, random.nextInt(10) + 1);
//			break;
//		case 14:
//			stack = new ItemStack(Items.gold_ingot, random.nextInt(5) + 1);
//			break;
//		default:
//			break;
//		}
//		
//		return stack;
//	}
//	
//	// FUTURE list should be pulled from JSON config file
//	/**
//	 * 
//	 * @return
//	 */
//	private ItemStack generateItemFromGoldCoin() {
//		Random random = new Random();
//		ItemStack stack = null;
//		
//		int itemIndex = random.nextInt(30);
//		switch(itemIndex) {
//		case 0:
//			stack = new ItemStack(Items.diamond_chestplate, 1);						
//			// enchant the item
//			stack = TreasureChestUtil.addEnchantment(stack);			
//			break;
//		case 1:
//			stack = new ItemStack(Items.iron_chestplate, 1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 2:
//			stack = new ItemStack(Items.diamond_leggings, 1);						
//			// enchant the item
//			stack = TreasureChestUtil.addEnchantment(stack);			
//			break;
//		case 3:
//			stack = new ItemStack(Items.iron_leggings, 1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;	
//		case 4:
//			stack = new ItemStack(Items.diamond_helmet, 1);						
//			// enchant the item
//			stack = TreasureChestUtil.addEnchantment(stack);			
//			break;
//		case 5:
//			stack = new ItemStack(Items.iron_helmet, 1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 6:
//			stack = new ItemStack(Items.diamond_boots, 1);						
//			// enchant the item
//			stack = TreasureChestUtil.addEnchantment(stack);			
//			break;
//		case 7:
//			stack = new ItemStack(Items.iron_boots, 1);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;
//		case 8:
//			stack = TreasureChestUtil.addEnchantment(new ItemStack(Items.diamond_sword, 1));
//			break;
//		case 9:
//			stack = TreasureChestUtil.addEnchantment(new ItemStack(Items.iron_sword, 1));
//			stack = TreasureChestUtil.addEnchantment(stack);
//			break;			
//		case 10:
//			stack = new ItemStack(Items.ghast_tear, random.nextInt(2) + 1);
//			break;
//		case 11:
//			stack = new ItemStack(Items.ender_eye, random.nextInt(3) + 1); 
//			break;
//		case 12:
//			stack = new ItemStack(Items.blaze_rod, random.nextInt(3) + 1); 
//			break;		
//		case 13:
//			stack = new ItemStack(Items.golden_apple, random.nextInt(3) + 1); 
//			break;
//		case 14:
//			stack = new ItemStack(TreasureItems.silverCoin, random.nextInt(3) + 1);
//			break;
//		case 15:
//			stack = new ItemStack(TreasureItems.goldCoin, random.nextInt(3) + 1);
//			break;
//		case 16:
//			stack = new ItemStack(Items.diamond, random.nextInt(5) + 1);
//			break;
//		case 17:
//			stack = new ItemStack(Items.emerald, random.nextInt(3) + 1);
//			break;			
//		case 18:
//			stack = new ItemStack(Items.experience_bottle, random.nextInt(3) + 1);
//			break;
//		case 19:
//			stack = new ItemStack(TreasureItems.pirateKey, random.nextInt(3) + 1);
//			break;
//		case 20:
//			stack = new ItemStack(TreasureItems.skeletonKey, random.nextInt(3) + 1);
//			break;
//		case 21: // lucky 21!
//			stack = new ItemStack(TreasureItems.skullSword, 1);
//			break;
//		case 22:
//			stack = new ItemStack(Items.iron_ingot, random.nextInt(15) + 1);
//			break;
//		case 23:
//			stack = new ItemStack(Items.gold_ingot, random.nextInt(10) + 1);
//			break;
//		case 24:
//			stack = new ItemStack(Items.arrow, random.nextInt(30) + 34);
//			break;	
//		case 25: // potion slots
//		case 26:
//		case 27:
//		case 28:
//		case 29:
//			stack = new ItemStack(Items.potionitem, 1, TreasureChestUtil.getPotions()[random.nextInt(TreasureChestUtil.getPotions().length)]);
//			break;
//		default:
//			break;
//		}
//		
//		return stack;
//	}
}
