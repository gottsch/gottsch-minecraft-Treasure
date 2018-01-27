/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.treasure2.block.Gravestone.EnumEngraving;
import com.someguyssoftware.treasure2.block.Gravestone.EnumTexture;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling on Feb 3, 2016
 *
 */
public class GravestoneBlockItem extends ItemBlock {

	/**
	 * 
	 * @param block
	 */
	public GravestoneBlockItem(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String subname = "";
		int meta = stack.getItemDamage();
		// 0-3 = t1/e1
		// 4-7 = t2/e1
		// 8-11 = t1/e2
		// 12-15 = t2/e2
		
        if ((meta & 4) > 0 && (meta & 8) < 1) {
        	subname = EnumTexture.TEXTURE2.getName() + "." + EnumEngraving.ENGRAVING1.getName();
        }
        else if ((meta & 8) == 8 && (meta & 12) == 8) {
        	subname = EnumTexture.TEXTURE1.getName() + "." + EnumEngraving.ENGRAVING2.getName();
        }
        else if ((meta & 12) > 8) {
        	subname = EnumTexture.TEXTURE2.getName() + "." + EnumEngraving.ENGRAVING2.getName();
        }
        else {
        	subname = EnumTexture.TEXTURE1.getName() + "." + EnumEngraving.ENGRAVING1.getName();
        }        
		return super.getUnlocalizedName(stack) + "." + subname;
	}
}
