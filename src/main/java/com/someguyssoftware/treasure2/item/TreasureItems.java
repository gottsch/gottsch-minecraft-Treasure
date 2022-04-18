/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.armor.ModArmorBuilder;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.item.ModSwordBuilder;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.adornment.TreasureAdornmentRegistry;
import com.someguyssoftware.treasure2.capability.AdornmentCapabilityProvider;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.DurabilityCapability;
import com.someguyssoftware.treasure2.capability.ICharmableCapability;
import com.someguyssoftware.treasure2.capability.IDurabilityCapability;
import com.someguyssoftware.treasure2.capability.IRunestonesCapability;
import com.someguyssoftware.treasure2.capability.InventoryType;
import com.someguyssoftware.treasure2.capability.RunestonesCapability;
import com.someguyssoftware.treasure2.capability.RunestonesCapabilityProvider;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.capability.modifier.GreatAdornmentLevelModifier;
import com.someguyssoftware.treasure2.capability.modifier.ILevelModifier;
import com.someguyssoftware.treasure2.capability.modifier.LordsAdornmentLevelModifier;
import com.someguyssoftware.treasure2.capability.modifier.NoLevelModifier;
import com.someguyssoftware.treasure2.charm.AegisCharm;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.CheatDeathCharm;
import com.someguyssoftware.treasure2.charm.DrainCharm;
import com.someguyssoftware.treasure2.charm.FireImmunityCharm;
import com.someguyssoftware.treasure2.charm.GreaterHealingCharm;
import com.someguyssoftware.treasure2.charm.HealingCharm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.IlluminationCharm;
import com.someguyssoftware.treasure2.charm.LifeStrikeCharm;
import com.someguyssoftware.treasure2.charm.ReflectionCharm;
import com.someguyssoftware.treasure2.charm.RuinCurse;
import com.someguyssoftware.treasure2.charm.SatietyCharm;
import com.someguyssoftware.treasure2.charm.ShieldingCharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2.SpecialLootTables;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.rune.AngelsRune;
import com.someguyssoftware.treasure2.rune.GreaterManaRune;
import com.someguyssoftware.treasure2.rune.IRuneEntity;
import com.someguyssoftware.treasure2.rune.TreasureRunes;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author Mark Gottschling onDec 22, 2017
 *
 */
public class TreasureItems {
	// tab
	public static Item TREASURE_TAB;
	public static Item ADORNMENTS_TAB;

	// keys
	public static KeyItem WOOD_KEY;
	public static KeyItem STONE_KEY;
	public static KeyItem EMBER_KEY;
	public static KeyItem LEAF_KEY;
	public static KeyItem LIGHTNING_KEY;
	public static KeyItem IRON_KEY;
	public static KeyItem GOLD_KEY;
	public static KeyItem METALLURGISTS_KEY;
	public static KeyItem DIAMOND_KEY;
	public static KeyItem EMERALD_KEY;
	public static KeyItem RUBY_KEY;
	public static KeyItem SAPPHIRE_KEY;
	public static KeyItem JEWELLED_KEY;
	public static KeyItem WITHER_KEY;
	public static KeyItem BONE_KEY;
	public static KeyItem SKELETON_KEY;
	public static KeyItem SPIDER_KEY;
	public static KeyItem DRAGON_KEY;
	public static KeyItem MASTER_KEY;
	public static KeyItem PILFERERS_LOCK_PICK;
	public static KeyItem THIEFS_LOCK_PICK;
	public static KeyRingItem KEY_RING;
	public static PouchItem POUCH;

	// locks
	public static LockItem WOOD_LOCK;
	public static LockItem STONE_LOCK;
	public static LockItem EMBER_LOCK;
	public static LockItem LEAF_LOCK;
	public static LockItem IRON_LOCK;
	public static LockItem GOLD_LOCK;
	public static LockItem DIAMOND_LOCK;
	public static LockItem EMERALD_LOCK;
	public static LockItem RUBY_LOCK;
	public static LockItem SAPPHIRE_LOCK;
	public static LockItem SPIDER_LOCK;
	public static LockItem WITHER_LOCK;

	// coins
	public static WealthItem COPPER_COIN;
	public static WealthItem SILVER_COIN;
	public static WealthItem GOLD_COIN;	

	// adornments

	// TODO make these medium tier specials - like level 12-15
	//    public static Item SILVER_SIGNET_RING;
	public static Adornment FOOLS_COIN;
	public static Adornment ANGELS_RING;
	public static Adornment RING_OF_FORTITUDE;
	public static Adornment PEASANTS_FORTUNE;
	public static Adornment GOTTSCHS_RING_OF_MOON;
	public static Adornment CASTLE_RING;
	public static Adornment SHADOWS_GIFT;
	public static Adornment BRACELET_OF_WONDER;
	public static Adornment RING_OF_LIFE_DEATH;

	public static Adornment MEDICS_TOKEN;
	public static Adornment SALANDAARS_WARD;	
	public static Adornment ADEPHAGIAS_BOUNTY;
	public static Adornment MIRTHAS_TORCH;

	public static Adornment DWARVEN_TALISMAN;
	public static Adornment MINERS_FRIEND;

	public static Adornment POCKET_WATCH;

	// gems
	public static Item AMETHYST;
	public static Item ONYX;
	public static Item SAPPHIRE;
	public static Item RUBY;
	public static Item WHITE_PEARL;
	public static Item BLACK_PEARL;

	// charns
	public static CharmItem CHARM_BOOK;

	// runestones
	public static RunestoneItem MANA_RUNESTONE;
	public static RunestoneItem GREATER_MANA_RUNESTONE;
	public static RunestoneItem DURABILITY_RUNESTONE;
	public static RunestoneItem QUALITY_RUNESTONE;
	public static RunestoneItem EQUIP_MANA_RUNESTONE;
	public static RunestoneItem ANVIL_RUNESTONE;
	public static RunestoneItem ANGELS_RUNESTONE;
	public static RunestoneItem PERSISTENCE_RUNESTONE;
	public static RunestoneItem SOCKETS_RUNESTONE;

	// wither items
	public static Item WITHER_STICK_ITEM;
	public static Item WITHER_ROOT_ITEM;

	// swords
	public static Item SKULL_SWORD;

	// armor
	public static Item EYE_PATCH;

	// potions
	public static PotionType EXTRA_STRONG_HEALING;
	public static PotionType EXTRA_STRONG_STRENGTH;
	public static PotionType EXTRA_STRONG_LEAPING;
	public static PotionType EXTRA_STRONG_SWIFTNESS;
	public static PotionType EXTRA_STRONG_REGENERATION;
	public static PotionType EXTRA_STRONG_POISON;

	// paintings
	public static Item PAINTING_BLOCKS_BRICKS;
	public static Item PAINTING_BLOCKS_COBBLESTONE;
	public static Item PAINTING_BLOCKS_DIRT;
	public static Item PAINTING_BLOCKS_LAVA;
	public static Item PAINTING_BLOCKS_SAND;
	public static Item PAINTING_BLOCKS_WATER;
	public static Item PAINTING_BLOCKS_WOOD;

	// other
	public static Item SPANISH_MOSS;
	public static Item TREASURE_TOOL;
	public static Item SKELETON;

	/*
	 * Materials
	 */
	// SKULL //
	public static final ToolMaterial SKULL_TOOL_MATERIAL = EnumHelper.addToolMaterial("SKULL", 2, 1800, 9.0F, 4.0F, 25);

	// key map
	public static Multimap<Rarity, KeyItem> keys;

	// lock map
	public static Multimap<Rarity, LockItem> locks;

	// items caches
	public static final Map<ResourceLocation, Item> ITEMS = new HashMap<>();
	public static final Map<ResourceLocation, CharmItem> CHARM_ITEMS = new HashMap<>();
	// item stacks caches
	public static final Map<ResourceLocation, Adornment> ADORNMENT_ITEMS = new HashMap<>();

	static {
		//		MINERS_FRIEND = new CharmedGemItem(Treasure.MODID, TreasureConfig.MINERS_FRIEND_ID) {
		//			public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {				
		//				CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
		//				ICharmInventoryCapability cap = provider.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		//				cap.getCharmEntities().add(TreasureCharms.GLORIOUS_ILLUMINATION.createEntity());
		//				cap.getCharmEntities().add(TreasureCharms.GRAND_HARVESTING.createEntity());
		//				return provider;
		//			}
		//		};
		//		MINERS_FRIEND.setCreativeTab(Treasure.TREASURE_TAB);
		//
		//		FOOLS_COIN = new CharmedCoinItem(Treasure.MODID, "fools_coin", Coins.SILVER) {
		//			public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {				
		//				CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
		//				ICharmInventoryCapability cap = provider.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		//				cap.getCharmEntities().add(TreasureCharms.HEALING.createEntity());
		//				cap.getCharmEntities().add(TreasureCharms.DECAY.createEntity());
		//				return provider;
		//			}
		//		};
		//		FOOLS_COIN.setCreativeTab(Treasure.TREASURE_TAB);

		//
		//		DWARVEN_TALISMAN = new CharmedCoinItem(Treasure.MODID, "dwarven_talisman", Coins.GOLD) {
		//			public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {				
		//				CharmInventoryCapabilityProvider provider =  new CharmInventoryCapabilityProvider();
		//				ICharmInventoryCapability cap = provider.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		//				cap.getCharmEntities().add(TreasureCharms.GLORIOUS_HARVESTING.createEntity());
		//				return provider;
		//			}
		//		};
		//		DWARVEN_TALISMAN.setCreativeTab(Treasure.TREASURE_TAB);

		// 2x max slots. 1x slots. 1x charms. level 12
		//        CASTLE_RING = (Item) new Adornment(Treasure.MODID, "castle_ring", AdornmentType.RING).setMaxSlots(2).setLevel(12);

		//
		//        BRACELET_OF_WONDER = (Item) new Adornment(Treasure.MODID, "bracelet_of_wonder", AdornmentType.BRACELET) {
		//            public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {				
		////            	CharmInventoryCapabilityProvider charmableProvider =  new CharmInventoryCapabilityProvider();
		//            	ICapabilityProvider provider = BaublesIntegration.isEnabled() ? new BaublesIntegration.AdornmentProvider(AdornmentType.BRACELET) : new CharmInventoryCapabilityProvider();
		//                ICharmInventoryCapability charmCap = provider.getCapability(TreasureCapabilities.CHARM_INVENTORY, null);
		//                charmCap.getCharmEntities().add(TreasureCharms.SHIELDING_13.createEntity());
		//                charmCap.getCharmEntities().add(TreasureCharms.REFLECTION_10.createEntity());
		//                charmCap.setSlots(4);
		//                return provider;
		//            }
		//        }.setMaxSlots(4).setLevel(10);
		//

		//            
		//        	@SuppressWarnings("deprecation")
		//        	@Override
		//        	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
		//        		tooltip.add(TextFormatting.GOLD.toString() + "" + I18n.translateToLocal("tooltip.charm.gottschs_ring_of_moon.special"));
		//        		tooltip.add("");
		//       			addCharmedInfo(stack, world, tooltip, flag);
		//        		addSlotsInfo(stack, world, tooltip, flag);
		//        	}
		//        }.setMaxSlots(4).setLevel(20)).setCreativeTab(null);




	}

	/**
	 * 
	 * @author Mark Gottschling on Nov 9, 2018
	 *
	 */
	@Mod.EventBusSubscriber(modid = Treasure.MODID)
	public static class PotionRegistrationHandler {
		@SubscribeEvent
		public static void registerPotions(final RegistryEvent.Register<PotionType> event) {
			final IForgeRegistry<PotionType> registry = event.getRegistry();
			registry.register(EXTRA_STRONG_HEALING);
			registry.register(EXTRA_STRONG_LEAPING);
			registry.register(EXTRA_STRONG_POISON);
			registry.register(EXTRA_STRONG_REGENERATION);
			registry.register(EXTRA_STRONG_STRENGTH);
			registry.register(EXTRA_STRONG_SWIFTNESS);
		}
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

			// TAB
			TREASURE_TAB = new ModItem().setItemName(Treasure.MODID, TreasureConfig.TREASURE_TAB_ID);
			ADORNMENTS_TAB = new ModItem().setItemName(Treasure.MODID, "adornments_tab");

			// KEYS
			WOOD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.WOOD_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.COMMON)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.woodKeyMaxUses);

			STONE_KEY = new KeyItem(Treasure.MODID, TreasureConfig.STONE_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.COMMON)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.stoneKeyMaxUses);

			EMBER_KEY = new EmberKey(Treasure.MODID, TreasureConfig.EMBER_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.SCARCE)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.emberKeyMaxUses);      

			LEAF_KEY = new KeyItem(Treasure.MODID, TreasureConfig.LEAF_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.UNCOMMON)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.leafKeyMaxUses);      

			LIGHTNING_KEY = new LightningKey(Treasure.MODID, TreasureConfig.LIGHTNING_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.SCARCE)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.lightningKeyMaxUses);

			IRON_KEY = new KeyItem(Treasure.MODID, TreasureConfig.IRON_KEY_ID)
					.setCategory(Category.METALS)
					.setRarity(Rarity.UNCOMMON)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.ironKeyMaxUses);

			GOLD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.GOLD_KEY_ID)
					.setCategory(Category.METALS)
					.setRarity(Rarity.SCARCE)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.goldKeyMaxUses);

			DIAMOND_KEY = new KeyItem(Treasure.MODID, TreasureConfig.DIAMOND_KEY_ID)
					.setCategory(Category.GEMS)
					.setRarity(Rarity.RARE)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.diamondKeyMaxUses);

			EMERALD_KEY = new KeyItem(Treasure.MODID, TreasureConfig.EMERALD_KEY_ID)
					.setCategory(Category.GEMS)
					.setRarity(Rarity.RARE)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.emeraldKeyMaxUses);

			RUBY_KEY = new KeyItem(Treasure.MODID, TreasureConfig.RUBY_KEY_ID)
					.setCategory(Category.GEMS)
					.setRarity(Rarity.EPIC)
					.setBreakable(false)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.rubyKeyMaxUses);

			SAPPHIRE_KEY = new KeyItem(Treasure.MODID, TreasureConfig.SAPPHIRE_KEY_ID)
					.setCategory(Category.GEMS)
					.setRarity(Rarity.EPIC)
					.setBreakable(false)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.sapphireKeyMaxUses);

			JEWELLED_KEY = new JewelledKey(Treasure.MODID, TreasureConfig.JEWELLED_KEY_ID)
					.setCategory(Category.GEMS)
					.setRarity(Rarity.EPIC)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.jewelledKeyMaxUses);

			METALLURGISTS_KEY = new MetallurgistsKey(Treasure.MODID, TreasureConfig.METALLURGISTS_KEY_ID)
					.setCategory(Category.METALS)
					.setRarity(Rarity.RARE)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.metallurgistsKeyMaxUses);

			SKELETON_KEY = new SkeletonKey(Treasure.MODID, TreasureConfig.SKELETON_KEY_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.RARE)
					.setBreakable(false)
					.setCraftable(false)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.skeletonKeyMaxUses);

			SPIDER_KEY = new KeyItem(Treasure.MODID, TreasureConfig.SPIDER_KEY_ID)
					.setCategory(Category.MOB)
					.setRarity(Rarity.SCARCE)
					.setBreakable(true)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.spiderKeyMaxUses);

			WITHER_KEY = new KeyItem(Treasure.MODID, TreasureConfig.WITHER_KEY_ID)
					.setCategory(Category.WITHER)
					.setRarity(Rarity.RARE)
					.setBreakable(false)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.witherKeyMaxUses);

			PILFERERS_LOCK_PICK = new PilferersLockPick(Treasure.MODID, TreasureConfig.PILFERERS_LOCK_PICK_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.COMMON)
					.setBreakable(true)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.pilferersLockPickMaxUses)
					.setSuccessProbability(32);

			THIEFS_LOCK_PICK = new ThiefsLockPick(Treasure.MODID, TreasureConfig.THIEFS_LOCK_PICK_ID)
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.UNCOMMON)
					.setBreakable(true)
					.setCraftable(true)
					.setMaxDamage(TreasureConfig.KEYS_LOCKS.thiefsLockPickMaxUses)
					.setSuccessProbability(48);

			// KEY SILVER_RING
			KEY_RING = new KeyRingItem(Treasure.MODID, TreasureConfig.KEY_RING_ID);

			keys = ArrayListMultimap.create();
			keys.put(WOOD_KEY.getRarity(), WOOD_KEY);
			keys.put(STONE_KEY.getRarity(), STONE_KEY);
			keys.put(EMBER_KEY.getRarity(), EMBER_KEY);
			keys.put(LEAF_KEY.getRarity(), LEAF_KEY);
			keys.put(LIGHTNING_KEY.getRarity(), LIGHTNING_KEY);
			keys.put(IRON_KEY.getRarity(), IRON_KEY);
			keys.put(GOLD_KEY.getRarity(), GOLD_KEY);
			keys.put(DIAMOND_KEY.getRarity(), DIAMOND_KEY);
			keys.put(EMERALD_KEY.getRarity(), EMERALD_KEY);
			keys.put(RUBY_KEY.getRarity(), RUBY_KEY);
			keys.put(SAPPHIRE_KEY.getRarity(), SAPPHIRE_KEY);
			keys.put(JEWELLED_KEY.getRarity(), JEWELLED_KEY);
			keys.put(METALLURGISTS_KEY.getRarity(), METALLURGISTS_KEY);
			keys.put(SKELETON_KEY.getRarity(), SKELETON_KEY);
			keys.put(SPIDER_KEY.getRarity(), SPIDER_KEY);
			keys.put(WITHER_KEY.getRarity(), WITHER_KEY);
			//			keys.put(PILFERERS_LOCK_PICK.getRarity(), WOOD_KEY);
			//			keys.put(THIEFS_LOCK_PICK.getRarity(), WOOD_KEY);

			// LOCKS
			WOOD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.WOOD_LOCK_ID, new KeyItem[] {WOOD_KEY, LIGHTNING_KEY})
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.COMMON);
			STONE_LOCK = new LockItem(Treasure.MODID, TreasureConfig.STONE_LOCK_ID, new KeyItem[] {STONE_KEY, LIGHTNING_KEY})
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.COMMON);
			EMBER_LOCK = new EmberLock(Treasure.MODID, TreasureConfig.EMBER_LOCK_ID, new KeyItem[] {EMBER_KEY, LIGHTNING_KEY})
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.SCARCE);
			LEAF_LOCK = new LockItem(Treasure.MODID, TreasureConfig.LEAF_LOCK_ID, new KeyItem[] {LEAF_KEY, LIGHTNING_KEY})
					.setCategory(Category.ELEMENTAL)
					.setRarity(Rarity.UNCOMMON);    	
			IRON_LOCK = new LockItem(Treasure.MODID, TreasureConfig.IRON_LOCK_ID, new KeyItem[] {IRON_KEY, METALLURGISTS_KEY})
					.setCategory(Category.METALS)
					.setRarity(Rarity.UNCOMMON);
			GOLD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.GOLD_LOCK_ID, new KeyItem[] {GOLD_KEY, METALLURGISTS_KEY})
					.setCategory(Category.METALS)
					.setRarity(Rarity.SCARCE);
			DIAMOND_LOCK = new LockItem(Treasure.MODID, TreasureConfig.DIAMOND_LOCK_ID, new KeyItem[] {DIAMOND_KEY, JEWELLED_KEY})
					.setCategory(Category.GEMS)
					.setRarity(Rarity.RARE);
			EMERALD_LOCK = new LockItem(Treasure.MODID, TreasureConfig.EMERALD_LOCK_ID, new KeyItem[] {EMERALD_KEY, JEWELLED_KEY})
					.setCategory(Category.GEMS)
					.setRarity(Rarity.RARE);
			RUBY_LOCK = new LockItem(Treasure.MODID, TreasureConfig.RUBY_LOCK_ID, new KeyItem[] {RUBY_KEY, JEWELLED_KEY})
					.setCategory(Category.GEMS)
					.setRarity(Rarity.EPIC);
			SAPPHIRE_LOCK = new LockItem(Treasure.MODID, TreasureConfig.SAPPHIRE_LOCK_ID, new KeyItem[] {SAPPHIRE_KEY, JEWELLED_KEY})
					.setCategory(Category.GEMS)
					.setRarity(Rarity.EPIC);
			SPIDER_LOCK = new LockItem(Treasure.MODID, TreasureConfig.SPIDER_LOCK_ID, new KeyItem[] {SPIDER_KEY})
					.setCategory(Category.POTION)
					.setRarity(Rarity.SCARCE);
			WITHER_LOCK = new LockItem(Treasure.MODID, TreasureConfig.WITHER_LOCK_ID, new KeyItem[] {WITHER_KEY})
					.setCategory(Category.WITHER)
					.setRarity(Rarity.SCARCE);

			locks = ArrayListMultimap.create();
			locks.put(WOOD_LOCK.getRarity(), WOOD_LOCK);
			locks.put(STONE_LOCK.getRarity(), STONE_LOCK);
			locks.put(EMBER_LOCK.getRarity(), EMBER_LOCK);
			locks.put(LEAF_LOCK.getRarity(), LEAF_LOCK);
			locks.put(IRON_LOCK.getRarity(), IRON_LOCK);
			locks.put(GOLD_LOCK.getRarity(), GOLD_LOCK);
			locks.put(DIAMOND_LOCK.getRarity(), DIAMOND_LOCK);
			locks.put(EMERALD_LOCK.getRarity(), EMERALD_LOCK);
			locks.put(RUBY_LOCK.getRarity(), RUBY_LOCK);
			locks.put(SAPPHIRE_LOCK.getRarity(), SAPPHIRE_LOCK);
			locks.put(SPIDER_LOCK.getRarity(), SPIDER_LOCK);
			// NOTE wither lock is a special and isn't used in the general locks list

			// COINS
			COPPER_COIN = new WealthItem(Treasure.MODID, "copper_coin");
			SILVER_COIN = new WealthItem(Treasure.MODID, TreasureConfig.SILVER_COIN_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					Treasure.LOGGER.debug("getting silver loot tables");
					List<LootTableShell> lootTables = new ArrayList<>();
					lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.UNCOMMON));
					lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
					return lootTables;
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.UNCOMMON));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};
			GOLD_COIN = new WealthItem(Treasure.MODID, TreasureConfig.GOLD_COIN_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					List<LootTableShell> lootTables = new ArrayList<>();
					lootTables.addAll(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
					lootTables.addAll(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE));
					return lootTables;
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.SCARCE));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};

			// GEMS
			AMETHYST = new GemItem(Treasure.MODID, TreasureConfig.AMETHYST_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					return /*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE);
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.SCARCE));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};
			ONYX = new GemItem(Treasure.MODID, TreasureConfig.ONYX_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					return /*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE);
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.RARE));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};
			RUBY = new GemItem(Treasure.MODID, TreasureConfig.RUBY_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					return /*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE);
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.RARE));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};
			SAPPHIRE = new GemItem(Treasure.MODID, TreasureConfig.SAPPHIRE_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					return /*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.EPIC);
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.EPIC));
					return new ItemStack(keys.get(random.nextInt(keys.size())));
				}
			};
			WHITE_PEARL = new GemItem(Treasure.MODID, TreasureConfig.WHITE_PEARL_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					List<LootTableShell> lootTables = new ArrayList<>();
					lootTables.add(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WHITE_PEARL_WELL));
					return lootTables;
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					return new ItemStack(Items.DIAMOND);
				}
			};
			BLACK_PEARL = new GemItem(Treasure.MODID, TreasureConfig.BLACK_PEARL_ID) {
				@Override
				public List<LootTableShell> getLootTables() {
					List<LootTableShell> lootTables = new ArrayList<>();
					lootTables.add(/*TreasureLootTableRegistry.getLootTableMaster()*/TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.BLACK_PEARL_WELL));
					return lootTables;
				}
				@Override
				public ItemStack getDefaultLootKey (Random random) {
					return new ItemStack(Items.EMERALD);
				}			
			};

			CHARM_BOOK = new CharmBook(Treasure.MODID, "charm_book") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
						$.innate = true;
						$.imbuing = true;
						$.source = true;
						$.executing = false;
						$.baseMaterial = TreasureCharmableMaterials.CHARM_BOOK.getName();
						$.sourceItem = Items.AIR.getRegistryName();
					}).build();
					// TEMP test with charm
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get().createEntity());

					return new CharmableCapabilityProvider(cap);
				}
			};

			// POUCHES
			POUCH = new PouchItem(Treasure.MODID, TreasureConfig.POUCH_ID);

			// ADORNMENTS
			// create all adornment item combinations
			List<Item> adornments = new SetupItems().createAdornments();
			
			ANGELS_RING = new NamedAdornment(Treasure.MODID, "angels_ring", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(3, 1, 1)
							.with($ -> {
								$.innate = true;
								$.imbuable = true;
								$.socketable = true;
								$.source = false;
								$.executing = true;
								$.namedByCharm = false;
								$.namedByMaterial = false;
								$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
								$.sourceItem = WHITE_PEARL.getRegistryName();
								$.levelModifier = new GreatAdornmentLevelModifier();
							}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(AegisCharm.AEGIS_TYPE, 16))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(GreaterHealingCharm.HEALING_TYPE, 16))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(FireImmunityCharm.FIRE_IMMUNITY_TYPE, 16))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000);
					durabilityCap.setMaxRepairs(1);
					durabilityCap.setRepairs(1);

					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.socketable = false;
					}).build();
					IRuneEntity runeEntity = TreasureRunes.RUNE_OF_ANGELS.createEntity();
					runestonesCap.add(InventoryType.INNATE, runeEntity);
					((AngelsRune)runeEntity.getRunestone()).initCapabilityApply(cap, durabilityCap, runeEntity);

					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			RING_OF_FORTITUDE = new NamedAdornment(Treasure.MODID, "ring_of_fortitude", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
						$.innate = true;
						$.imbuable = true;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.namedByCharm = false;
						$.namedByMaterial = false;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = BLACK_PEARL.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(AegisCharm.AEGIS_TYPE, 16))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 16))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			// (mythical)
			SHADOWS_GIFT = new NamedAdornment(Treasure.MODID, "shadows_gift", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

					/*
					 *  add enchantment. kinda hacky
					 */
					if (!EnchantmentHelper.hasVanishingCurse(stack)) {
						stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
					}

					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 1).with($ -> {
						$.innate = true;
						$.imbuable = true;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.namedByCharm = false;
						$.namedByMaterial = false;
						$.baseMaterial = TreasureCharmableMaterials.BLACK.getName();
						$.sourceItem = BLACK_PEARL.getRegistryName();
						$.levelModifier = new NoLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(DrainCharm.DRAIN_TYPE, 25))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.BLACK);
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};
			
			// (mythical)
			RING_OF_LIFE_DEATH = new NamedAdornment(Treasure.MODID, "ring_of_life_death", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {

					/*
					 *  add enchantment. kinda hacky
					 */
					if (!EnchantmentHelper.hasVanishingCurse(stack)) {
						stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
					}

					ICharmableCapability cap = new CharmableCapability.Builder(3, 1, 1).with($ -> {
						$.innate = true;
						$.imbuable = true;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.namedByCharm = false;
						$.namedByMaterial = false;
						$.baseMaterial = TreasureCharmableMaterials.BLOOD.getName();
						$.sourceItem = WHITE_PEARL.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(LifeStrikeCharm.LIFE_STRIKE_TYPE, 25))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(CheatDeathCharm.TYPE, 25))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(RuinCurse.RUIN_TYPE, 15))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.BLOOD);
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			CASTLE_RING = new NamedAdornment(Treasure.MODID, "castle_ring", AdornmentType.RING) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
						$.innate = true;
						$.imbuable = true;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.namedByCharm = false;
						$.namedByMaterial = false;
						$.baseMaterial = TreasureCharmableMaterials.SILVER.getName();
						$.sourceItem = SAPPHIRE.getRegistryName();
						$.levelModifier = new NoLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 10))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 10))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000, TreasureCharmableMaterials.SILVER);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			PEASANTS_FORTUNE = new NamedAdornment(Treasure.MODID, "peasants_fortune", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(0, 0, 2).with($ -> { // use STONE as source Item so it can't be upgraded
						$.innate = false;
						$.imbuable = false;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.IRON.getName();
						$.sourceItem =  Item.getItemFromBlock(Blocks.STONE).getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();

					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();

					IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.IRON);
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			/*
			 * special 4million download ring. will auto place in your backpack on new world for 1 month (Dec 2021).
			 * (legendary)
			 */
			GOTTSCHS_RING_OF_MOON = new NamedAdornment(Treasure.MODID, "gottschs_ring_of_moon", AdornmentType.RING, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					if (!EnchantmentHelper.hasVanishingCurse(stack)) {
						stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
					}
					ICharmableCapability cap = new CharmableCapability.Builder(4, 0, 0).with($ -> {
						$.innate = true;
						$.imbuable = false;
						$.socketable = false;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.BLACK.getName();
						$.sourceItem =  SAPPHIRE.getRegistryName();
						$.levelModifier = new NoLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 21))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(DrainCharm.DRAIN_TYPE, 21))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 21))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(LifeStrikeCharm.LIFE_STRIKE_TYPE, 21))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);

					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();							

					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.RING, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};
			
			// (legendary)
			BRACELET_OF_WONDER = new NamedAdornment(Treasure.MODID, "bracelet_of_wonder", AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					if (!EnchantmentHelper.hasVanishingCurse(stack)) {
						stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
					}
					ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
						$.innate = true;
						$.imbuable = true;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem =  BLACK_PEARL.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 24))).get().createEntity());
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 24))).get().createEntity());

					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);

					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
						$.socketable = true;
					}).build();							

					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.BRACELET, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			// (epic)
			MEDICS_TOKEN = new NamedAdornment(Treasure.MODID, "medics_token", AdornmentType.NECKLACE, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
						$.innate = true;
						$.imbuable = false;
						$.socketable = false;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = SAPPHIRE.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(GreaterHealingCharm.HEALING_TYPE, 20))).get().createEntity());
					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.socketable = false;
					}).build();
					IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
					runestonesCap.add(InventoryType.INNATE, runeEntity);
					((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
					
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.NECKLACE, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			// (epic)
			ADEPHAGIAS_BOUNTY = new NamedAdornment(Treasure.MODID, "adephagias_bounty", AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
						$.innate = true;
						$.imbuable = false;
						$.socketable = false;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = Items.EMERALD.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(SatietyCharm.SATIETY_TYPE, 20))).get().createEntity());
					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.socketable = false;
					}).build();
					IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
					runestonesCap.add(InventoryType.INNATE, runeEntity);
					((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
					
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.BRACELET, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			// (epic)
			SALANDAARS_WARD = new NamedAdornment(Treasure.MODID, "salandaars_ward", AdornmentType.NECKLACE, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
						$.innate = true;
						$.imbuable = false;
						$.socketable = false;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = RUBY.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 20))).get().createEntity());
					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.socketable = false;
					}).build();
					IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
					runestonesCap.add(InventoryType.INNATE, runeEntity);
					((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.NECKLACE, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};

			// (epic)
			MIRTHAS_TORCH = new NamedAdornment(Treasure.MODID, "mirthas_torch", AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
						$.innate = true;
						$.imbuable = false;
						$.socketable = false;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = WHITE_PEARL.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(IlluminationCharm.ILLUMINATION_TYPE, 20))).get().createEntity());
					IDurabilityCapability durabilityCap = new DurabilityCapability();
					durabilityCap.setInfinite(true);
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.BRACELET, cap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, durabilityCap);
				}
			};

			POCKET_WATCH = new NamedAdornment(Treasure.MODID, "pocket_watch", AdornmentType.POCKET, TreasureAdornmentRegistry.GREAT) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					ICharmableCapability cap = new CharmableCapability.Builder(0, 0, 2).with($ -> {
						$.innate = false;
						$.imbuable = false;
						$.socketable = true;
						$.source = false;
						$.executing = true;
						$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
						$.sourceItem = AMETHYST.getRegistryName();
						$.levelModifier = new GreatAdornmentLevelModifier();
					}).build();
					IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000, TreasureCharmableMaterials.GOLD);
					IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 2).with($ -> {
						$.socketable = true;
					}).build();
					
					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(AdornmentType.POCKET, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};
			adornments.add(POCKET_WATCH);
			TreasureAdornmentRegistry.register(TreasureCharmableMaterials.GOLD.getName(), AMETHYST.getRegistryName(), POCKET_WATCH);

			// CHARMS
			List<Item> charms = new ArrayList<>();
			charms.add(createCharm(TreasureCharmableMaterials.COPPER, Items.AIR));
			charms.add(createCharm(TreasureCharmableMaterials.SILVER, Items.AIR));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.AIR));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, AMETHYST));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, ONYX));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.DIAMOND));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.EMERALD));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, RUBY));
			charms.add(createCharm(TreasureCharmableMaterials.GOLD, SAPPHIRE));
			charms.add(createCharm(TreasureCharmableMaterials.LEGENDARY, Items.AIR));
			charms.add(createCharm(TreasureCharmableMaterials.MYTHICAL, Items.AIR));
			
			// RUNESTONES
			MANA_RUNESTONE = new RunestoneItem(Treasure.MODID, "mana_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_MANA.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_MANA, MANA_RUNESTONE);
			
			GREATER_MANA_RUNESTONE = new RunestoneItem(Treasure.MODID, "greater_mana_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_GREATER_MANA.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_GREATER_MANA, GREATER_MANA_RUNESTONE);

			DURABILITY_RUNESTONE = new RunestoneItem(Treasure.MODID, "durability_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_DURABILITY.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_DURABILITY, DURABILITY_RUNESTONE);

			QUALITY_RUNESTONE = new RunestoneItem(Treasure.MODID, "quality_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_QUALITY.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_QUALITY, QUALITY_RUNESTONE);

			EQUIP_MANA_RUNESTONE = new RunestoneItem(Treasure.MODID, "equip_mana_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_EQUIP_AS_MANA.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_EQUIP_AS_MANA, EQUIP_MANA_RUNESTONE);

			ANVIL_RUNESTONE = new RunestoneItem(Treasure.MODID, "anvil_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
						//						$.sourceItem = AMETHYST.getRegistryName();
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_ANVIL.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_ANVIL, ANVIL_RUNESTONE);

			ANGELS_RUNESTONE = new RunestoneItem(Treasure.MODID, "angels_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_ANGELS.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}		
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_ANGELS, ANGELS_RUNESTONE);

			PERSISTENCE_RUNESTONE = new RunestoneItem(Treasure.MODID, "persistence_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_PERSISTENCE.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}		
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_PERSISTENCE, PERSISTENCE_RUNESTONE);

			SOCKETS_RUNESTONE = new RunestoneItem(Treasure.MODID, "sockets_runestone") {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
						$.bindable = true;
					}).build();
					cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_SOCKETS.createEntity());	
					return new RunestonesCapabilityProvider(cap);
				}		
			};
			TreasureRunes.register(TreasureRunes.RUNE_OF_SOCKETS, SOCKETS_RUNESTONE);

			// OTHER
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

			// wither items
			WITHER_STICK_ITEM = new WitherStickItem(Treasure.MODID, TreasureConfig.WITHER_STICK_ITEM_ID);
			WITHER_ROOT_ITEM = new WitherRootItem(Treasure.MODID, TreasureConfig.WITHER_ROOT_ITEM_ID);

			// potions
			EXTRA_STRONG_HEALING = new PotionType("healing", 
					new PotionEffect[] {new PotionEffect(MobEffects.INSTANT_HEALTH, 1, 2)}).setRegistryName(Treasure.MODID, "extra_strong_healing");
			EXTRA_STRONG_STRENGTH = new PotionType("strength", 
					new PotionEffect[] {new PotionEffect(MobEffects.STRENGTH, 1800, 2)}).setRegistryName(Treasure.MODID, "extra_strong_strength");
			EXTRA_STRONG_LEAPING = new PotionType("leaping", 
					new PotionEffect[] {new PotionEffect(MobEffects.JUMP_BOOST, 1800, 2)}).setRegistryName(Treasure.MODID, "extra_strong_leaping");
			EXTRA_STRONG_SWIFTNESS = new PotionType("swiftness", 
					new PotionEffect[] {new PotionEffect(MobEffects.SPEED, 1800, 2)}).setRegistryName(Treasure.MODID, "extra_strong_swiftness");
			EXTRA_STRONG_REGENERATION = new PotionType("regeneration", 
					new PotionEffect[] {new PotionEffect(MobEffects.REGENERATION, 450, 2)}).setRegistryName(Treasure.MODID, "extra_strong_regeneration");
			EXTRA_STRONG_POISON = new PotionType("poison", 
					new PotionEffect[] {new PotionEffect(MobEffects.POISON, 432, 2)}).setRegistryName(Treasure.MODID, "extra_strong_poison");

			SPANISH_MOSS = new SpanishMossItem(Treasure.MODID, TreasureConfig.SPANISH_MOSS_ITEM_ID);

			PAINTING_BLOCKS_DIRT = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_DIRT_ID, Rarity.SCARCE)
					.setPaintingName("Dirt").setCollectionName("Blocks").setCollectionIssue("1")	.setCollectionSize("7").setArtist("o2xygeno");
			PAINTING_BLOCKS_COBBLESTONE = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_COBBLESTONE_ID, Rarity.SCARCE)
					.setPaintingName("Cobblestone").setCollectionName("Blocks").setCollectionIssue("2").setCollectionSize("7").setArtist("o2xygeno");		
			PAINTING_BLOCKS_WATER = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_WATER_ID, Rarity.SCARCE)
					.setPaintingName("Water").setCollectionName("Blocks").setCollectionIssue("3")	.setCollectionSize("7").setArtist("o2xygeno");

			PAINTING_BLOCKS_SAND = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_SAND_ID, Rarity.RARE)
					.setPaintingName("Sand").setCollectionName("Blocks").setCollectionIssue("4").setCollectionSize("7").setArtist("o2xygeno");
			PAINTING_BLOCKS_WOOD = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_WOOD_ID, Rarity.RARE)
					.setPaintingName("Wood").setCollectionName("Blocks").setCollectionIssue("5")	.setCollectionSize("7").setArtist("o2xygeno");

			PAINTING_BLOCKS_BRICKS = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_BRICKS_ID, Rarity.EPIC)
					.setPaintingName("Bricks").setCollectionName("Blocks").setCollectionIssue("6").setCollectionSize("7").setArtist("o2xygeno");
			PAINTING_BLOCKS_LAVA = new PaintingItem(Treasure.MODID, TreasureConfig.PAINTING_BLOCKS_LAVA_ID, Rarity.EPIC)
					.setPaintingName("Lava").setCollectionName("Blocks").setCollectionIssue("7").setCollectionSize("7").setArtist("o2xygeno");

			SKELETON = new SkeletonItem(Treasure.MODID, TreasureConfig.SKELETON_ID);

			TREASURE_TOOL = new TreasureToolItem(Treasure.MODID, TreasureConfig.TREASURE_TOOL_ITEM_ID);

			////////////////////////////////

			final IForgeRegistry<Item> registry = event.getRegistry();

			final Item[] items = {
					TREASURE_TAB,
					ADORNMENTS_TAB,
					COPPER_COIN,
					SILVER_COIN,
					GOLD_COIN,
					//					FOOLS_COIN,
					MEDICS_TOKEN,
					SALANDAARS_WARD,
					//					DWARVEN_TALISMAN,
					//					MINERS_FRIEND,
					ADEPHAGIAS_BOUNTY,
					MIRTHAS_TORCH,
					//                    SILVER_SIGNET_RING,
					//                    CASTLE_RING,
					ANGELS_RING,
					RING_OF_FORTITUDE,
					BRACELET_OF_WONDER,
					GOTTSCHS_RING_OF_MOON,
					SHADOWS_GIFT,
					RING_OF_LIFE_DEATH,
					CASTLE_RING,
					PEASANTS_FORTUNE,
//					POCKET_WATCH,
					WHITE_PEARL,
					BLACK_PEARL,
					WOOD_LOCK,
					STONE_LOCK,
					EMBER_LOCK,
					LEAF_LOCK,
					IRON_LOCK,
					GOLD_LOCK,
					DIAMOND_LOCK,
					EMERALD_LOCK,
					RUBY_LOCK,
					SAPPHIRE_LOCK,
					SPIDER_LOCK,
					WITHER_LOCK,
					WOOD_KEY,
					STONE_KEY,
					EMBER_KEY,
					LEAF_KEY,
					LIGHTNING_KEY,
					IRON_KEY,
					GOLD_KEY,
					DIAMOND_KEY,
					EMERALD_KEY,
					RUBY_KEY,
					SAPPHIRE_KEY,
					JEWELLED_KEY,
					METALLURGISTS_KEY,
					SKELETON_KEY,
					SPIDER_KEY,
					WITHER_KEY,
					PILFERERS_LOCK_PICK,
					THIEFS_LOCK_PICK,
					KEY_RING,
					POUCH,
					//					LUCKY_POUCH,
					//					APPRENTICES_POUCH,
					//					MASTERS_POUCH,
					SKULL_SWORD,
					EYE_PATCH,
					WITHER_STICK_ITEM,
					WITHER_ROOT_ITEM,
					SPANISH_MOSS,
					TREASURE_TOOL,
					PAINTING_BLOCKS_BRICKS,
					PAINTING_BLOCKS_COBBLESTONE,
					PAINTING_BLOCKS_DIRT,
					PAINTING_BLOCKS_LAVA,
					PAINTING_BLOCKS_SAND,
					PAINTING_BLOCKS_WATER,
					PAINTING_BLOCKS_WOOD,
					AMETHYST,
					ONYX,
					SAPPHIRE,
					RUBY,
					CHARM_BOOK,
					MANA_RUNESTONE,
					GREATER_MANA_RUNESTONE,
					DURABILITY_RUNESTONE,
					QUALITY_RUNESTONE,
					EQUIP_MANA_RUNESTONE,
					ANVIL_RUNESTONE,
					ANGELS_RUNESTONE,
					PERSISTENCE_RUNESTONE,
					SOCKETS_RUNESTONE,
					SKELETON
			};
			registry.registerAll(items);

			// create and add charm items
			/*
			 * COPPER_CHARM
			 * SILVER_CHARM
			 * GOLD_CHARM
			 * DIAMOND_CHARM
			 * EMERALD_CHARM
			 * RUBY_CHARM
			 * SAPPHIRE_CHARM
			 * BLOOD_CHARM ?
			 * BONE_CHARM ?
			 * ATIUM_CHARM ?
			 */

			// add charms
			charms.forEach(charm -> {
				ITEMS.put(charm.getRegistryName(), charm);
				CHARM_ITEMS.put(charm.getRegistryName(), (CharmItem) charm);
				registry.register(charm);
			});

			// add adornments
			adornments.forEach(a -> {
				ITEMS.put(a.getRegistryName(), a);
				ADORNMENT_ITEMS.put(a.getRegistryName(), (Adornment) a);
				registry.register(a);
			});

			// register to the ore dictionary
			OreDictionary.registerOre("amethyst", TreasureItems.AMETHYST);
			OreDictionary.registerOre("onyx", TreasureItems.ONYX);

			OreDictionary.registerOre("gemAmethyst", TreasureItems.AMETHYST);
			OreDictionary.registerOre("gemOnyx", TreasureItems.ONYX);

			OreDictionary.registerOre("sapphire", TreasureItems.SAPPHIRE);
			OreDictionary.registerOre("ruby", TreasureItems.RUBY);

			OreDictionary.registerOre("gemSapphire", TreasureItems.SAPPHIRE);
			OreDictionary.registerOre("gemRuby", TreasureItems.RUBY);
		}
	}

	/*
	 * 
	 */
	public static class SetupItems {
		List<AdornmentType> types = Arrays.asList(AdornmentType.BRACELET, AdornmentType.NECKLACE, AdornmentType.RING);
		List<AdornmentSize> sizes = Arrays.asList(TreasureAdornmentRegistry.STANDARD);
		List<CharmableMaterial> materials = Arrays.asList(
				TreasureCharmableMaterials.IRON, 
				TreasureCharmableMaterials.COPPER, 
				TreasureCharmableMaterials.SILVER, 
				TreasureCharmableMaterials.GOLD);

		List<ResourceLocation> sources = Arrays.asList(
				Items.DIAMOND.getRegistryName(),
				Items.EMERALD.getRegistryName(),
				TreasureItems.AMETHYST.getRegistryName(),
				TreasureItems.ONYX.getRegistryName(),
				TreasureItems.RUBY.getRegistryName(),
				TreasureItems.SAPPHIRE.getRegistryName(),
				TreasureItems.WHITE_PEARL.getRegistryName(),
				TreasureItems.BLACK_PEARL.getRegistryName());

		Map<CharmableMaterial, Integer> materialInnates = Maps.newHashMap();		
		Map<AdornmentSize, ILevelModifier> levelModifiers = Maps.newHashMap();

		public SetupItems() {
			materialInnates.put(TreasureCharmableMaterials.IRON, 0);
			materialInnates.put(TreasureCharmableMaterials.COPPER, 1);
			materialInnates.put(TreasureCharmableMaterials.SILVER, 2);
			materialInnates.put(TreasureCharmableMaterials.GOLD, 3);

			levelModifiers.put(TreasureAdornmentRegistry.STANDARD, new NoLevelModifier());
			levelModifiers.put(TreasureAdornmentRegistry.GREAT, new GreatAdornmentLevelModifier());
			levelModifiers.put(TreasureAdornmentRegistry.LORDS, new LordsAdornmentLevelModifier());	
		}

		/**
		 * 
		 * @return
		 */
		public List<Item> createAdornments() {
			List<Item> adornments = new ArrayList<>();

			// create base adornments
			types.forEach(type -> {
				sizes.forEach(size -> {
					materials.forEach(material -> {
						Adornment a = createAdornment(type, material, size, Items.AIR.getRegistryName());
						a.setCreativeTab(Treasure.ADORNMENTS_TAB);
//						Treasure.logger.debug("adding adornment item -> {}", a.getRegistryName());
						adornments.add(a);
						TreasureAdornmentRegistry.register(material.getName(), Items.AIR.getRegistryName(), a);
					});
				});
			});

			// create adornments with gems
			types.forEach(type -> {
				sizes.forEach(size -> {
					materials.forEach(material -> {
						sources.forEach(source -> {
							Adornment a = createAdornment(type, material, size, source);
							a.setCreativeTab(Treasure.ADORNMENTS_TAB);
//							Treasure.logger.debug("adding adornment item -> {}", a.getRegistryName());
							adornments.add(a);
							TreasureAdornmentRegistry.register(material.getName(), source, a);
						});
					});
				});
			});

			// some one-offs (great rings no gems)
			AdornmentSize size = TreasureAdornmentRegistry.GREAT;
			List<CharmableMaterial> materials2 = Arrays.asList(
					TreasureCharmableMaterials.IRON, 
					TreasureCharmableMaterials.COPPER, 
					TreasureCharmableMaterials.SILVER, 
					TreasureCharmableMaterials.GOLD,
					TreasureCharmableMaterials.BLOOD,
					TreasureCharmableMaterials.BLACK
					);
			materialInnates.put(TreasureCharmableMaterials.BLOOD, 2);
			materialInnates.put(TreasureCharmableMaterials.BLACK, 3);

			ResourceLocation source = Items.AIR.getRegistryName();
			materials2.forEach(material -> {
				types.forEach(type -> {
					Adornment a = createAdornment(type, material, size, source);
					a.setCreativeTab(Treasure.ADORNMENTS_TAB);
					Treasure.LOGGER.debug("adding adornment item -> {}", a.getRegistryName());
					adornments.add(a);
					TreasureAdornmentRegistry.register(material.getName(), source, a);					
				});
			});
			
			// some one-offs (great rings with gems)
			materials2 = Arrays.asList(
					TreasureCharmableMaterials.IRON,
					TreasureCharmableMaterials.COPPER,
					TreasureCharmableMaterials.SILVER, 
					TreasureCharmableMaterials.GOLD,
					TreasureCharmableMaterials.BLOOD,
					TreasureCharmableMaterials.BLACK
					);

			materials2.forEach(material -> {
				sources.forEach(s -> {
					types.forEach(type -> {
						Adornment a = createAdornment(type, material, size, s);
						a.setCreativeTab(Treasure.ADORNMENTS_TAB);
//						Treasure.logger.debug("adding adornment item -> {}", a.getRegistryName());
						adornments.add(a);
						TreasureAdornmentRegistry.register(material.getName(), s, a);						
					});
				});
			});

			return adornments;
		}

		/**
		 * 
		 * @param name
		 * @param type
		 * @param material
		 * @param size
		 * @param source
		 * @return
		 */
		private Adornment createAdornment(AdornmentType type, CharmableMaterial material,	AdornmentSize size, ResourceLocation source) {
			String name = (size == TreasureAdornmentRegistry.STANDARD ? "" : size.getName() + "_") + (source == Items.AIR.getRegistryName() ? "" :  source.getResourcePath() + "_") + material.getName().getResourcePath() + "_" + type.toString();
			Adornment a = new Adornment(Treasure.MODID, name, type, size) {
				public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
					// calculate the innate size
					int innateSize = materialInnates.get(material);
					if (size == TreasureAdornmentRegistry.GREAT && material == TreasureCharmableMaterials.IRON) {
						innateSize++;
					}
					ICharmableCapability cap = new CharmableCapability.Builder(innateSize, 1, 1)
							.with($ -> {
								$.innate = material == TreasureCharmableMaterials.IRON ? false : true;
								$.imbuable = (material == TreasureCharmableMaterials.IRON || material == TreasureCharmableMaterials.COPPER) ? false : true;
								$.socketable = true;
								$.source = false;
								$.executing = true;
								$.namedByCharm = true;
								$.namedByMaterial = true;
								$.baseMaterial = material.getName();
								$.levelModifier = levelModifiers.get(size);
								$.sourceItem = source;
							}).build();

					IRunestonesCapability runestonesCap = new RunestonesCapability(0, 0, 0);
					if (cap.getMaxCharmLevel() >= 8) {
						runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
							$.socketable = true;
						}).build();
					}
					int durability = (innateSize + 2) * material.getMaxLevel() * material.getDurability();
					IDurabilityCapability durabilityCap = new DurabilityCapability(durability, durability);
					durabilityCap.setMaxRepairs(material.getMaxRepairs());
					durabilityCap.setRepairs(durabilityCap.getMaxRepairs());

					return BaublesIntegration.isEnabled()
							? new BaublesIntegration.BaubleAdornmentCapabilityProvider(type, cap, runestonesCap, durabilityCap) 
									: new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};
			return a;
		}
	}

	private static CharmItem createCharm(CharmableMaterial material, Item source) {
		String name = (source == Items.AIR ? 
				material.getName().getResourcePath() : source.getRegistryName().getResourcePath())  + "_charm";
		Treasure.LOGGER.debug("creating charmItem -> {}", name);
		CharmItem charm = new CharmItem(Treasure.MODID, name) {
			public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
					$.innate= true;
					$.bindable = true;
					$.source = true;
					$.executing = true;
					$.namedByMaterial = true;
					$.baseMaterial = material.getName();
					$.sourceItem =source.getRegistryName();
				}).build();
				// TEMP test with charm
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get().createEntity());

				return new CharmableCapabilityProvider(cap);
			}
		};
		return charm;
	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	public static CharmItem getCharmItemByLevel(int level) {
		CharmItem resultItem = null;
		List<CharmItem> charms = new ArrayList<>(CHARM_ITEMS.values());
		Collections.sort(charms, charmLevelComparator);
		for (Item item : charms) {
			Treasure.LOGGER.debug("charm item -> {}", ((CharmItem)item).getRegistryName());
			ItemStack itemStack = new ItemStack(item);			
			// get the capability
			ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
			if (cap != null) {
				Treasure.LOGGER.debug("name -> {}, charm level -> {}, level -> {}", itemStack.getDisplayName(), cap.getMaxCharmLevel(), level);
				if (cap.getMaxCharmLevel() >= level) {
					resultItem = (CharmItem)item;
					break;
				}
			}
		}
		return resultItem;
	}
	
	/**
	 * 
	 * @param name
	 * @param level
	 * @return
	 */
	public static Optional<ItemStack> getCharm(String charmName, int level, int itemType) {
		Item charmItem = null;
		if (itemType == 1) {
			charmItem = TreasureItems.CHARM_BOOK;
		}
		else {
			charmItem = getCharmItemByLevel(level);
		}
		
		// get the charm
		Optional<ICharm> charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(charmName, level)));
		if (!charm.isPresent()) {
			return Optional.empty();
		}

		/*
		 *  add charm to charmItem
		 *  note: an itemStack is being created, call the initCapabilities() method. ensure to remove any charms
		 */
		ItemStack charmStack = new ItemStack(charmItem);
		ICharmableCapability cap = charmStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		cap.clearCharms();
		cap.add(InventoryType.INNATE, charm.get().createEntity());
		return Optional.of(charmStack);
	}

	public static Comparator<CharmItem> charmLevelComparator = new Comparator<CharmItem>() {
		@Override
		public int compare(CharmItem p1, CharmItem p2) {
			ItemStack stack1 = new ItemStack(p1);
			ItemStack stack2 = new ItemStack(p2);
			// use p1 < p2 because the sort should be ascending
			if (stack1.getCapability(TreasureCapabilities.CHARMABLE, null).getMaxCharmLevel() > stack2.getCapability(TreasureCapabilities.CHARMABLE, null).getMaxCharmLevel()) {
				// greater than
				return 1;
			}
			else {
				// less than
				return -1;
			}
		}
	};
}
