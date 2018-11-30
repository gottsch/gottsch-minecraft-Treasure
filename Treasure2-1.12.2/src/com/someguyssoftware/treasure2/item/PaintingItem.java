/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractPaintingBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Nov 9, 2018
 *
 */
public class PaintingItem extends ModItem {
	private String paintingName;
	private String collectionName;
	private String collectionIssue;
	private String collectionSize;
	private String artist;
	private Rarity rarity;
	
	private Block paintingBlock;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public PaintingItem(String modID, String name) {
		setItemName(modID, name);
		setCreativeTab(Treasure.TREASURE_TAB);
		setRarity(Rarity.SCARCE);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param rarity
	 */
	public PaintingItem(String modID, String name, Rarity rarity) {
		this(modID, name);
		setRarity(rarity);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		// add all the painting info here
		tooltip.add(I18n.translateToLocalFormatted("tooltip.label.rarity", TextFormatting.DARK_BLUE + getRarity().toString()));
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.painting.name", TextFormatting.WHITE + getPaintingName()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.painting.collection", TextFormatting.GOLD +getCollectionName()));
//		tooltip.add(I18n.translateToLocalFormatted("tooltip.painting.artist", TextFormatting.DARK_AQUA + getArtist()));
		tooltip.add(I18n.translateToLocalFormatted("tooltip.painting.issue", getCollectionIssue(), getCollectionSize()));
		
	}

	/**
	 * 
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.PASS;
		}
      else if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
	    	return EnumActionResult.PASS;
	    }
		
		//  need to get the correct block
		Block block = getPaintingBlock();
		
		BlockPos p = null;
		switch (facing) {
		case SOUTH:
			p = pos.south();
			break;
		case WEST:
			p = pos.west();
			break;
		case NORTH:
			p = pos.north();
			break;
		case EAST:
			p = pos.east();
			break;
		default:
			p = pos.north();
			break;
		}

		ItemStack heldItem = player.getHeldItem(hand);
		this.placeBlock(worldIn, p, block, player, heldItem);
		heldItem.shrink(1);
		return EnumActionResult.SUCCESS;
	}

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @param itemStack
	 */
	public void placeBlock(World world, BlockPos pos, Block block, EntityPlayer player, ItemStack itemStack) {
		// set the block
		if (block.canPlaceBlockAt(world, pos)) {
			world.setBlockState(pos, block.getDefaultState());
			block.onBlockPlacedBy(world, pos, block.getDefaultState(), player, itemStack);
		}
	}

	/**
	 * @return the paintingBlock
	 */
	public Block getPaintingBlock() {
		return paintingBlock;
	}

	/**
	 * @param paintingBlock the paintingBlock to set
	 */
	public PaintingItem setPaintingBlock(Block paintingBlock) {
		this.paintingBlock = paintingBlock;
		return this;
	}

	/**
	 * @return the paintingName
	 */
	public String getPaintingName() {
		return paintingName;
	}

	/**
	 * @param paintingName the paintingName to set
	 */
	public PaintingItem setPaintingName(String paintingName) {
		this.paintingName = paintingName;
		return this;
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName the collectionName to set
	 */
	public PaintingItem setCollectionName(String collectionName) {
		this.collectionName = collectionName;
		return this;
	}

	/**
	 * @return the collectionIssue
	 */
	public String getCollectionIssue() {
		return collectionIssue;
	}

	/**
	 * @param collectionIssue the collectionIssue to set
	 */
	public PaintingItem setCollectionIssue(String collectionIssue) {
		this.collectionIssue = collectionIssue;
		return this;
	}

	/**
	 * @return the collectionSize
	 */
	public String getCollectionSize() {
		return collectionSize;
	}

	/**
	 * @param collectionSize the collectionSize to set
	 */
	public PaintingItem setCollectionSize(String collectionSize) {
		this.collectionSize = collectionSize;
		return this;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist the artist to set
	 */
	public PaintingItem setArtist(String artist) {
		this.artist = artist;
		return this;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}
}
