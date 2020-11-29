/**
 * 
 */
package com.someguyssoftware.treasure2.wish;

import java.util.List;

import com.someguyssoftware.gottschcore.positional.ICoords;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
@FunctionalInterface
public interface IWishProviderFunction {
	IWishProvider getProvider(World world, ICoords coords, List<IWishProvider> providers);
}
