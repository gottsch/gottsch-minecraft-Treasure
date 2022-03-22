/**
 * 
 */
package com.someguyssoftware.treasure2.wish;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
@Deprecated
public class WishingWell implements IWishProvider {

	@Override
	public ItemStack provideWish(World world, Random random, EntityItem entityItem, ICoords coords) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValidAt(World world, ICoords coords) {
		// TODO Auto-generated method stub
		return false;
	}

}
