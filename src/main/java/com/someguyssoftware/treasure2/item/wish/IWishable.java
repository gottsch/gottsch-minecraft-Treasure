/**
 * 
 */
package com.someguyssoftware.treasure2.item.wish;

import java.util.List;
import java.util.Optional;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.wish.IWishProvider;
import com.someguyssoftware.treasure2.wish.IWishProviderFunction;

import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
public interface IWishable {

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param wishProviders
	 * @return
	 */
	default public Optional<IWishProvider> getWishProvider(World world, ICoords coords, List<IWishProvider> wishProviders) {
		for(IWishProvider provider : wishProviders) {
			if (provider.isValidAt(world, coords)) {
				return Optional.of(provider);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 
	 * @param world
	 * @param coords
	 * @param wishProviders
	 * @param function
	 * @return
	 */
	default public Optional<IWishProvider> getWishProvider(World world, ICoords coords, List<IWishProvider> wishProviders, IWishProviderFunction function) {
		return Optional.ofNullable(function.getProvider(world, coords, wishProviders));
	}
}
