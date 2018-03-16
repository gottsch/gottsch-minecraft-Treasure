/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.armor.ModArmorBuilder;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.item.ModSwordBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Coins;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
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
	public static LockItem RUBY_LOCK;
	public static LockItem SAPPHIRE_LOCK;
	public static LockItem SPIDER_LOCK;
	
	// keys
	public static KeyItem WOOD_KEY;
	public static KeyItem STONE_KEY;
	public static KeyItem IRON_KEY;
	public static KeyItem GOLD_KEY;
	public static KeyItem DIAMOND_KEY;
	public static KeyItem EMERALD_KEY;
	public static KeyItem METALLURGISTS_KEY;
	
	public static KeyItem WITHER_KEY;
	public static KeyItem RUBY_KEY;
	public static KeyItem SAPPHIRE_KEY;
	public static KeyItem BONE_KEY;
	public static KeyItem SKELETON_KEY;
	public static KeyItem SPIDER_KEY;
	public static KeyItem DRAGON_KEY;
	public static KeyItem MASTER_KEY;

	public static KeyItem PILFERERS_LOCK_PICK;
	public static KeyItem THIEFS_LOCK_PICK;
	
	public static KeyRingItem KEY_RING;
	
	// swords
	public static Item SKULL_SWORD;
	
	// armor
	public static Item EYE_PATCH;
	
	/*
	 * Materials
	 */
	// SKULL //
	public static final ToolMaterial SKULL_TOOL_MATERIAL = EnumHelper.addToolMaterial("SKULL", 2, 1800, 9.0F, 4.0F, 25);
	// FOG
	public static final Material FOG = new MaterialTransparent(MapColor.AIR);
	
	// chest holder
	public static Multimap<Rarity, LockItem> locks;

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
				.setCraftable(true)
				.setMaxDamage(10);

		STONE_KEY = new KeyItem(Treasure.MODID, TreasureConfig.STONE_KEY_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON)
				.setCraftable(true)
				.setMaxDamage(10);

		IRON_KEY = new KeyItem(Treasure.MODID, TreasureConfig.IRON_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.UNCOMMON)
				.setCraftable(true)
				.setMaxDamage(10);
		
		GOLD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.GOLD_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.SCARCE)
				.setCraftable(true)
				.setMaxDamage(15);

		DIAMOND_KEY = new KeyItem(Treasure.MODID, TreasureConfig.DIAMOND_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(20);

		EMERALD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.EMERALD_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(10);
		
		RUBY_KEY = new KeyItem(Treasure.MODID, TreasureConfig.RUBY_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(5);
		
		SAPPHIRE_KEY = new KeyItem(Treasure.MODID, TreasureConfig.SAPPHIRE_KEY_ID)
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(5);
		
		METALLURGISTS_KEY = new MetallurgistsKey(Treasure.MODID, TreasureConfig.METALLURGISTS_KEY_ID)
				.setCategory(Category.METALS)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(25);
		
		SKELETON_KEY = new SkeletonKey(Treasure.MODID, TreasureConfig.SKELETON_KEY_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(false)
				.setMaxDamage(5);
		
		SPIDER_KEY = new KeyItem(Treasure.MODID, TreasureConfig.SPIDER_KEY_ID)
				.setCategory(Category.MOB)
				.setRarity(Rarity.SCARCE)
				.setBreakable(true)
				.setCraftable(true)
				.setMaxDamage(5);
		
		WITHER_KEY = new KeyItem(Treasure.MODID, TreasureConfig.WITHER_KEY_ID)
				.setCategory(Category.WITHER)
				.setRarity(Rarity.RARE)
				.setBreakable(false)
				.setCraftable(true)
				.setMaxDamage(5);
		
		PILFERERS_LOCK_PICK = new PilferersLockPick(Treasure.MODID, TreasureConfig.PILFERERS_LOCK_PICK_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.COMMON)
				.setBreakable(true)
				.setCraftable(true)
				.setMaxDamage(10)
				.setSuccessProbability(24);
		
		THIEFS_LOCK_PICK = new ThiefsLockPick(Treasure.MODID, TreasureConfig.THIEFS_LOCK_PICK_ID)
				.setCategory(Category.BASIC)
				.setRarity(Rarity.UNCOMMON)
				.setBreakable(true)
				.setCraftable(true)
				.setMaxDamage(10)
				.setSuccessProbability(32);
		
		// KEY RING
		KEY_RING = new KeyRingItem(Treasure.MODID, TreasureConfig.KEY_RING_ID);
		
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
				.setRarity(Rarity.RARE);
		RUBY_LOCK = new LockItem(Treasure.MODID, TreasureConfig.RUBY_LOCK_ID, new KeyItem[] {RUBY_KEY})
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC);
		SAPPHIRE_LOCK = new LockItem(Treasure.MODID, TreasureConfig.SAPPHIRE_LOCK_ID, new KeyItem[] {SAPPHIRE_KEY})
				.setCategory(Category.GEMS)
				.setRarity(Rarity.EPIC);
		SPIDER_LOCK = new LockItem(Treasure.MODID, TreasureConfig.SPIDER_LOCK_ID, new KeyItem[] {SPIDER_KEY})
				.setCategory(Category.POTION)
				.setRarity(Rarity.SCARCE);
		
		locks =	ArrayListMultimap.create();
		locks.put(Rarity.COMMON, WOOD_LOCK);
		locks.put(Rarity.COMMON, STONE_LOCK);
		locks.put(Rarity.UNCOMMON, IRON_LOCK);
		locks.put(Rarity.SCARCE, GOLD_LOCK);
		locks.put(DIAMOND_LOCK.getRarity(), DIAMOND_LOCK);
		locks.put(EMERALD_LOCK.getRarity(), EMERALD_LOCK);
		locks.put(RUBY_LOCK.getRarity(), RUBY_LOCK);
		locks.put(SAPPHIRE_LOCK.getRarity(), SAPPHIRE_LOCK);
		locks.put(Rarity.SCARCE, SPIDER_LOCK);
		
		// other
		ModSwordBuilder builder = new ModSwordBuilder();
		SKULL_SWORD = builder
				.withModID(Treasure.MODID)
				.withName(TreasureConfig.SKULL_SWORD_ID)
				.withMaterial(SKULL_TOOL_MATERIAL)
				.withRepairItem(Items.BONE)
				.withCreativeTab(Treasure.TREASURE_TAB)
				.build();

		// NOTE if going to add lots of different armor, tools and swords then use a List<Pair<>> or "props" object. See MetalsItems.java
		ModArmorBuilder armorBuilder = new ModArmorBuilder();
		EYE_PATCH = armorBuilder
				.withModID(Treasure.MODID)
				.withName(TreasureConfig.EYE_PATCH_ID)
				.withMaterial(ItemArmor.ArmorMaterial.LEATHER)
				.withRenderIndex(2)
				.withSlot(EntityEquipmentSlot.HEAD)
				.withTexture("textures/models/armor/eye_patch.png")
				.withRepairItem(Items.LEATHER)
				.withCreativeTab(Treasure.TREASURE_TAB)
				.build();
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
					RUBY_LOCK,
					SAPPHIRE_LOCK,
					SPIDER_LOCK,
					WOOD_KEY,
					STONE_KEY,
					IRON_KEY,
					GOLD_KEY,
					DIAMOND_KEY,
					EMERALD_KEY,
					RUBY_KEY,
					SAPPHIRE_KEY,
					METALLURGISTS_KEY,
					SKELETON_KEY,
					SPIDER_KEY,
					PILFERERS_LOCK_PICK,
					THIEFS_LOCK_PICK,
					KEY_RING,
					SKULL_SWORD,
					EYE_PATCH
			};
			registry.registerAll(items);		
		}
	}
}
