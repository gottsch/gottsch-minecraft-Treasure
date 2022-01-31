/**
 * 
 */
package com.someguyssoftware.treasure2.item.wish;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2;
import com.someguyssoftware.treasure2.wish.IWishProvider;
import com.someguyssoftware.treasure2.wish.IWishProviderFunction;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
public interface IWishable {
	public static final String DROPPED_BY_KEY = "droppedBy";
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
	default public Optional<List<LootTableShell>> buildInjectedLootTableList(String key, Rarity rarity) {
		return Optional.ofNullable(Treasure.LOOT_TABLE_MASTER.getLootTableByKeyRarity(TreasureLootTableMaster2.ManagedTableType.INJECT, key, rarity));
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param list
	 * @param lootContext
	 * @return
	 */
	default public List<ItemStack> getLootItems(World world, Random random, List<LootTableShell> list, LootContext lootContext) {
		return Treasure.LOOT_TABLE_MASTER.getInjectedLootItems(world, random, list, lootContext);
	}
	
	/**
	 * 
	 * @return
	 */
	default public LootContext getLootContext() {
		return Treasure.LOOT_TABLE_MASTER.getContext();
	}
	
	/**
	 * 
	 * @param world
	 * @param player
	 * @return
	 */
	default public LootContext getLootContext(World world, EntityPlayer player) {
		if (player == null) return getLootContext();
		LootContext lootContext = new LootContext.Builder((WorldServer) world)
				.withLuck(player.getLuck())
				.withPlayer(player)
				.build();
		return lootContext;
	}
	
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
