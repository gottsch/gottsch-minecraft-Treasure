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
public interface IWishProvider {

	public ItemStack provideWish(World world, Random random, EntityItem entityItem, ICoords coords);

	public boolean isValidAt(World world, ICoords coords);
}
