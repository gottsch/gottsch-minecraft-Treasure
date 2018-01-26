/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureItems {
	// tab
	public static Item TREASURE_TAB;
	// coins
	public static Item GOLD_COIN;
	public static Item SILVER_COIN;
	// locks
	public static LockItem WOOD_LOCK;
	public static LockItem STONE_LOCK;
	public static LockItem IRON_LOCK;
	public static LockItem GOLD_LOCK;
	public static LockItem DIAMOND_LOCK;
	public static LockItem EMERALD_LOCK;
	// keys
	public static KeyItem WOOD_KEY;
	public static KeyItem STONE_KEY;
	public static KeyItem IRON_KEY;
	public static KeyItem GOLD_KEY;
	public static KeyItem DIAMOND_KEY;
	public static KeyItem EMERALD_KEY;
	public static KeyItem METALLURGISTS_KEY;
	
	static {
		// TAB
		TREASURE_TAB = new ModItem().setItemName(Treasure.MODID, TreasureConfig.TREASURE_TAB_ID);
		
		// COINS
		GOLD_COIN = new CoinItem(Treasure.MODID, TreasureConfig.GOLD_COIN_ID);
		SILVER_COIN = new CoinItem(Treasure.MODID, TreasureConfig.SILVER_COIN_ID, Coins.SILVER);
		
		// KEYS
		WOOD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.WOOD_KEY_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON)
				.setCraftable(true);

		STONE_KEY = new KeyItem(Treasure.MODID, TreasureConfig.STONE_KEY_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON)
				.setCraftable(true);

		IRON_KEY = new KeyItem(Treasure.MODID, TreasureConfig.IRON_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.UNCOMMON)
				.setCraftable(true);
		
		GOLD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.GOLD_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.SCARCE)
				.setCraftable(true);

		DIAMOND_KEY = new KeyItem(Treasure.MODID, TreasureConfig.DIAMOND_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false);

		EMERALD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.EMERALD_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC)
				.setBreakable(false)
				.setCraftable(false);
		
		METALLURGISTS_KEY = new KeyItem(Treasure.MODID, TreasureConfig.METALLURGISTS_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false);
		
		// LOCKS
		WOOD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.WOOD_LOCK_ID, new KeyItem[] {WOOD_KEY})
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON);
		STONE_LOCK = new LockItem(Treasure.MODID, TreasureConfig.STONE_LOCK_ID, new KeyItem[] {STONE_KEY})
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON);
		IRON_LOCK = new LockItem(Treasure.MODID, TreasureConfig.IRON_LOCK_ID, new KeyItem[] {IRON_KEY})
				.setCategory(Category.METALS)
				.setRarity(Rarity.UNCOMMON);
		GOLD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.GOLD_LOCK_ID, new KeyItem[] {GOLD_KEY})
				.setCategory(Category.METALS)
				.setRarity(Rarity.SCARCE);
		DIAMOND_LOCK = new LockItem(Treasure.MODID, TreasureConfig.DIAMOND_LOCK_ID, new KeyItem[] {DIAMOND_KEY})
				.setCategory(Category.GEMS)
				.setRarity(Rarity.RARE);
		EMERALD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.EMERALD_LOCK_ID, new KeyItem[] {EMERALD_KEY})
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC);
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Jan 12, 2018
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID)
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			
			final IForgeRegistry<Item> registry = event.getRegistry();
	
			final Item[] items = {
					TREASURE_TAB,
					SILVER_COIN,
					GOLD_COIN,
					WOOD_LOCK,
					STONE_LOCK,
					IRON_LOCK,
					GOLD_LOCK,
					DIAMOND_LOCK,
					EMERALD_LOCK,
					WOOD_KEY,
					STONE_KEY,
					IRON_KEY,
					GOLD_KEY,
					DIAMOND_KEY,
					EMERALD_KEY,
					METALLURGISTS_KEY
			};
			registry.registerAll(items);		
		}
	}
}
