/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureChestItem extends ItemBlock {

	public TreasureChestItem(Block block) {
		super(block);

		// TODO move to ModItemBlock - set through constructor
        this.setRegistryName(block.getRegistryName());
   
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    /**
     * NOTE Item has subtypes so need a way to return the unlocalized name of the subtype item.
     */
    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
    	// TODO using metadata here means that there can only be 16 max unique chest types.
//        return "tile.treasure.chest." + OldTreasureChestType.VALUES[itemstack.getMetadata()].name().toLowerCase(Locale.US);
    	return null;
    }
}
