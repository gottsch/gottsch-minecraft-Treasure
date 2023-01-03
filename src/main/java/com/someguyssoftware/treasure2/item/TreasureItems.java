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
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.adornment.AdornmentSize;
import com.someguyssoftware.treasure2.adornment.TreasureAdornmentRegistry;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
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
import com.someguyssoftware.treasure2.config.TreasureConfig.KeyID;
import com.someguyssoftware.treasure2.config.TreasureConfig.LockID;
import com.someguyssoftware.treasure2.enums.AdornmentType;
import com.someguyssoftware.treasure2.enums.Category;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2.SpecialLootTables;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.material.CharmableMaterial;
import com.someguyssoftware.treasure2.material.TreasureArmorMaterial;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.rune.AngelsRune;
import com.someguyssoftware.treasure2.rune.DoubleChargeRune;
import com.someguyssoftware.treasure2.rune.GreaterManaRune;
import com.someguyssoftware.treasure2.rune.IRuneEntity;
import com.someguyssoftware.treasure2.rune.TreasureRunes;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Mark Gottschling on Aug 12, 2020
 * This class has the register event handler for all custom items.
 * This class uses @Mod.EventBusSubscriber so the event handler has to be static
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureItems {
	// deferred registries
	final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Treasure.MODID);

	// tabs
	public static RegistryObject<Item> TREASURE_TAB = ITEMS.register("treasure_tab", () -> new ModItem(new Item.Properties()));
	public static RegistryObject<Item> ADORNMENTS_TAB = ITEMS.register("adornments_tab", () -> new ModItem(new Item.Properties()));

	// keys
	public static RegistryObject<KeyItem> WOOD_KEY = ITEMS.register(KeyID.WOOD_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.woodKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.COMMON)
			.setCraftable(false));

	public static RegistryObject<KeyItem> STONE_KEY = ITEMS.register(KeyID.STONE_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.stoneKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.COMMON)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMBER_KEY = ITEMS.register(KeyID.EMBER_KEY_ID, () -> new EmberKey(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.emberKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.SCARCE)
			.setCraftable(false));   

	public static RegistryObject<KeyItem> LEAF_KEY = ITEMS.register(KeyID.LEAF_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.leafKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.UNCOMMON)
			.setCraftable(false));    

	public static RegistryObject<KeyItem> LIGHTNING_KEY = ITEMS.register(KeyID.LIGHTNING_KEY_ID, () -> new LightningKey(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.lightningKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.SCARCE)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> IRON_KEY = ITEMS.register(KeyID.IRON_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.ironKeyMaxUses.get()))
			.setCategory(Category.METALS)
			.setRarity(Rarity.UNCOMMON)
			.setCraftable(false));

	public static RegistryObject<KeyItem> GOLD_KEY = ITEMS.register(KeyID.GOLD_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.goldKeyMaxUses.get()))
			.setCategory(Category.METALS)
			.setRarity(Rarity.SCARCE)
			.setCraftable(false));

	public static RegistryObject<KeyItem> METALLURGISTS_KEY = ITEMS.register(KeyID.METALLURGISTS_KEY_ID, () -> new MetallurgistsKey(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.metallurgistsKeyMaxUses.get()))
			.setCategory(Category.METALS)
			.setRarity(Rarity.RARE)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> DIAMOND_KEY = ITEMS.register(KeyID.DIAMOND_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.diamondKeyMaxUses.get()))
			.setCategory(Category.GEMS)
			.setRarity(Rarity.RARE)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> EMERALD_KEY = ITEMS.register(KeyID.EMERALD_KEY_ID, () -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.emeraldKeyMaxUses.get()))
			.setCategory(Category.GEMS)
			.setRarity(Rarity.RARE)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> RUBY_KEY = ITEMS.register(KeyID.RUBY_KEY_ID, 
			() -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.rubyKeyMaxUses.get()))
			.setCategory(Category.GEMS)
			.setRarity(Rarity.EPIC)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> SAPPHIRE_KEY = ITEMS.register(KeyID.SAPPHIRE_KEY_ID, 
			() -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.sapphireKeyMaxUses.get()))
			.setCategory(Category.GEMS)
			.setRarity(Rarity.EPIC)
			.setBreakable(false)
			.setCraftable(true));

	public static RegistryObject<KeyItem> JEWELLED_KEY = ITEMS.register(KeyID.JEWELLED_KEY_ID, 
			() -> new JewelledKey(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.jewelledKeyMaxUses.get()))
			.setCategory(Category.GEMS)
			.setRarity(Rarity.EPIC)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> SKELETON_KEY = ITEMS.register(KeyID.SKELETON_KEY_ID, 
			() -> new SkeletonKey(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.skeletonKeyMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.RARE)
			.setBreakable(false)
			.setCraftable(false));

	public static RegistryObject<KeyItem> WITHER_KEY = ITEMS.register(KeyID.WITHER_KEY_ID, 
			() -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.witherKeyMaxUses.get()))
			.setCategory(Category.WITHER)
			.setRarity(Rarity.RARE)
			.setBreakable(false)
			.setCraftable(true));


	public static RegistryObject<KeyItem> SPIDER_KEY = ITEMS.register(KeyID.SPIDER_KEY_ID, 
			() -> new KeyItem(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.spiderKeyMaxUses.get()))
			.setCategory(Category.MOB)
			.setRarity(Rarity.SCARCE)
			.setBreakable(true)
			.setCraftable(true));

	public static RegistryObject<KeyItem> PILFERERS_LOCK_PICK = ITEMS.register(KeyID.PILFERERS_LOCK_PICK_ID, 
			() -> new PilferersLockPick(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.pilferersLockPickMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.COMMON)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(32));

	public static RegistryObject<KeyItem> THIEFS_LOCK_PICK = ITEMS.register(KeyID.THIEFS_LOCK_PICK_ID, 
			() -> new ThiefsLockPick(new Item.Properties().durability(TreasureConfig.KEYS_LOCKS.thiefsLockPickMaxUses.get()))
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.UNCOMMON)
			.setBreakable(true)
			.setCraftable(true)
			.setSuccessProbability(48));

	// FUTURE
	public static RegistryObject<KeyItem> DRAGON_KEY;
	public static RegistryObject<KeyItem> MASTER_KEY;

	public static RegistryObject<KeyRingItem> KEY_RING = ITEMS.register(KeyID.KEY_RING_ID, () -> new KeyRingItem(new Item.Properties()));

	// locks
	public static final Supplier<Item.Properties> LOCK_ITEM_PROPERTIES = () -> new Item.Properties().tab(TreasureItemGroups.TREASURE_ITEM_GROUP);
	public static RegistryObject<LockItem> WOOD_LOCK = ITEMS.register(LockID.WOOD_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {WOOD_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.COMMON));
	public static RegistryObject<LockItem> STONE_LOCK = ITEMS.register(LockID.STONE_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {STONE_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.COMMON));
	public static RegistryObject<LockItem> EMBER_LOCK = ITEMS.register(LockID.EMBER_LOCK_ID, () -> new EmberLock(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {EMBER_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.SCARCE));
	public static RegistryObject<LockItem> LEAF_LOCK = ITEMS.register(LockID.LEAF_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {LEAF_KEY.get(), LIGHTNING_KEY.get()})
			.setCategory(Category.ELEMENTAL)
			.setRarity(Rarity.UNCOMMON));  
	public static RegistryObject<LockItem> IRON_LOCK = ITEMS.register(LockID.IRON_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {IRON_KEY.get(), METALLURGISTS_KEY.get()})
			.setCategory(Category.METALS)
			.setRarity(Rarity.UNCOMMON));
	public static RegistryObject<LockItem> GOLD_LOCK = ITEMS.register(LockID.GOLD_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {GOLD_KEY.get(), METALLURGISTS_KEY.get()})
			.setCategory(Category.METALS)
			.setRarity(Rarity.SCARCE));
	public static RegistryObject<LockItem> DIAMOND_LOCK = ITEMS.register(LockID.DIAMOND_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {DIAMOND_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(Category.GEMS)
			.setRarity(Rarity.RARE));
	public static RegistryObject<LockItem> EMERALD_LOCK = ITEMS.register(LockID.EMERALD_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {EMERALD_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(Category.GEMS)
			.setRarity(Rarity.RARE));
	public static RegistryObject<LockItem> RUBY_LOCK = ITEMS.register(LockID.RUBY_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {RUBY_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(Category.GEMS)
			.setRarity(Rarity.EPIC));
	public static RegistryObject<LockItem> SAPPHIRE_LOCK = ITEMS.register(LockID.SAPPHIRE_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SAPPHIRE_KEY.get(), JEWELLED_KEY.get()})
			.setCategory(Category.GEMS)
			.setRarity(Rarity.EPIC));
	public static RegistryObject<LockItem> SPIDER_LOCK = ITEMS.register(LockID.SPIDER_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {SPIDER_KEY.get()})
			.setCategory(Category.MOB)
			.setRarity(Rarity.SCARCE));
	public static RegistryObject<LockItem> WITHER_LOCK = ITEMS.register(LockID.WITHER_LOCK_ID, () -> new LockItem(LOCK_ITEM_PROPERTIES.get(), new KeyItem[] {WITHER_KEY.get()})
			.setCategory(Category.WITHER)
			.setRarity(Rarity.SCARCE));

	// coins
	public static RegistryObject<Item> COPPER_COIN = ITEMS.register(TreasureConfig.ItemID.COPPER_COIN_ID, () -> new WealthItem(new Item.Properties()));
	public static RegistryObject<Item> SILVER_COIN = ITEMS.register(TreasureConfig.ItemID.SILVER_COIN_ID, () -> new WealthItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
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
	});
	public static RegistryObject<Item> GOLD_COIN = ITEMS.register(TreasureConfig.ItemID.GOLD_COIN_ID, () -> new WealthItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			List<LootTableShell> lootTables = new ArrayList<>();
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE));
			lootTables.addAll(TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE));
			return lootTables;
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.SCARCE));
			return new ItemStack(keys.get(random.nextInt(keys.size())));
		}
	});
	
	// gems
	public static RegistryObject<Item> TOPAZ = ITEMS.register(TreasureConfig.ItemID.TOPAZ_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.SCARCE);
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.SCARCE));
			return new ItemStack(keys.get(random.nextInt(keys.size())));
		}
	});
	public static RegistryObject<Item> ONYX = ITEMS.register(TreasureConfig.ItemID.ONYX_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE);
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.RARE));
			return new ItemStack(keys.get(random.nextInt(keys.size())));
		}
	});
	public static RegistryObject<Item> RUBY = ITEMS.register(TreasureConfig.ItemID.RUBY_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.RARE);
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.RARE));
			return new ItemStack(keys.get(random.nextInt(keys.size())));
		}
	});
	public static RegistryObject<Item> SAPPHIRE = ITEMS.register(TreasureConfig.ItemID.SAPPHIRE_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			return TreasureLootTableRegistry.getLootTableMaster().getLootTableByRarity(Rarity.EPIC);
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.EPIC));
			return new ItemStack(keys.get(random.nextInt(keys.size())));
		}
	});
	public static RegistryObject<Item> WHITE_PEARL = ITEMS.register(TreasureConfig.ItemID.WHITE_PEARL_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			List<LootTableShell> lootTables = new ArrayList<>();
			lootTables.add(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WHITE_PEARL_WELL));
			return lootTables;
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			return new ItemStack(Items.DIAMOND);
		}
	});
	public static RegistryObject<Item> BLACK_PEARL = ITEMS.register(TreasureConfig.ItemID.BLACK_PEARL_ID, () -> new GemItem(new Item.Properties()) {
		@Override
		public List<LootTableShell> getLootTables() {
			List<LootTableShell> lootTables = new ArrayList<>();
			lootTables.add(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.BLACK_PEARL_WELL));
			return lootTables;
		}
		@Override
		public ItemStack getDefaultLootKey (Random random) {
			return new ItemStack(Items.EMERALD);
		}			
	});
	
	// charms
	public static RegistryObject<CharmItem> CHARM_BOOK = ITEMS.register("charm_book", () -> new CharmBook(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
				$.innate = true;
				$.imbuing = true;
				$.source = true;
				$.executing = false;
				$.baseMaterial = TreasureCharmableMaterials.CHARM_BOOK.getName();
				$.sourceItem = Items.AIR.getRegistryName();
			}).build();
			// TEMP test with charm
			cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get().createEntity());
			return new CharmableCapabilityProvider(cap);
		}
	});
	
	// pouch
	public static RegistryObject<PouchItem> POUCH = ITEMS.register("pouch", () -> new PouchItem(new Item.Properties()));
	
	// adornments
	public static RegistryObject<Adornment> ANGELS_RING;
	public static RegistryObject<Item> RING_OF_FORTITUDE;
	public static RegistryObject<Item> PEASANTS_FORTUNE;
	public static RegistryObject<Item> GOTTSCHS_RING_OF_MOON;
	public static RegistryObject<Item> GOTTSCHS_AMULET_OF_HEAVENS;
	public static RegistryObject<Item> CASTLE_RING;
	public static RegistryObject<Item> SHADOWS_GIFT;
	public static RegistryObject<Item> BRACELET_OF_WONDER;
	public static RegistryObject<Item> RING_OF_LIFE_DEATH;

	public static RegistryObject<Item> MEDICS_TOKEN;
	public static RegistryObject<Item> SALANDAARS_WARD;	
	public static RegistryObject<Item> ADEPHAGIAS_BOUNTY;
	public static RegistryObject<Item> MIRTHAS_TORCH;	
	//	public static RegistryObject<Item> DWARVEN_TALISMAN;
	//	public static Adornment MINERS_FRIEND;

	public static RegistryObject<Adornment> POCKET_WATCH;

	// runestones
	public static RegistryObject<RunestoneItem> MANA_RUNESTONE = ITEMS.register("mana_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_MANA.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	
	public static RegistryObject<RunestoneItem> GREATER_MANA_RUNESTONE = ITEMS.register("greater_mana_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_GREATER_MANA.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	
	public static RegistryObject<RunestoneItem> DURABILITY_RUNESTONE = ITEMS.register("durability_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_DURABILITY.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	public static RegistryObject<RunestoneItem> QUALITY_RUNESTONE = ITEMS.register("quality_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_QUALITY.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	public static RegistryObject<RunestoneItem> EQUIP_MANA_RUNESTONE = ITEMS.register("equip_mana_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_EQUIP_AS_MANA.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	public static RegistryObject<RunestoneItem> ANVIL_RUNESTONE = ITEMS.register("anvil_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
				//						$.sourceItem = AMETHYST.getRegistryName();
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_ANVIL.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}
	});
	public static RegistryObject<RunestoneItem> ANGELS_RUNESTONE = ITEMS.register("angels_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_ANGELS.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}		
	});
	public static RegistryObject<RunestoneItem> PERSISTENCE_RUNESTONE = ITEMS.register("persistence_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_PERSISTENCE.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}		
	});
	public static RegistryObject<RunestoneItem> SOCKETS_RUNESTONE = ITEMS.register("sockets_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_SOCKETS.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}		
	});
	public static RegistryObject<RunestoneItem> DOUBLE_CHARGE_RUNESTONE = ITEMS.register("double_charge_runestone", () -> new RunestoneItem(new Item.Properties()) {
		public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
			IRunestonesCapability cap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
				$.bindable = true;
			}).build();
			cap.add(InventoryType.INNATE, TreasureRunes.RUNE_OF_DOUBLE_CHARGE.createEntity());	
			return new RunestonesCapabilityProvider(cap);
		}		
	});


	// wither items
	public static RegistryObject<WitherStickItem> WITHER_STICK_ITEM = ITEMS.register(TreasureConfig.ItemID.WITHER_STICK_ITEM_ID, () -> new WitherStickItem(TreasureBlocks.WITHER_BRANCH, new Item.Properties()));
	
	public static RegistryObject<WitherRootItem> WITHER_ROOT_ITEM = ITEMS.register(TreasureConfig.ItemID.WITHER_ROOT_ITEM_ID, () -> new WitherRootItem(TreasureBlocks.WITHER_ROOT, new Item.Properties()));

	// other
	public static final RegistryObject<SwordItem> SKULL_SWORD = ITEMS.register(TreasureConfig.ItemID.SKULL_SWORD_ID, 
			() -> new SwordItem(TreasureItemTier.SKULL, 3, -2.4F, new Item.Properties().tab(TreasureItemGroups.TREASURE_ITEM_GROUP)));
	
	public static final RegistryObject<Item> EYE_PATCH = ITEMS.register(TreasureConfig.ItemID.EYE_PATCH_ID, 
			() ->  new DyeableArmorItem(TreasureArmorMaterial.PATCH, EquipmentSlotType.HEAD, (new Item.Properties()).tab(TreasureItemGroups.TREASURE_ITEM_GROUP)));

	//	public static RegistryObject<SpanishMossItem> SPANISH_MOSS = ITEMS.register("spanish_moss", () -> new SpanishMossItem(new Item.Properties()));

	public static RegistryObject<SkeletonItem> SKELETON = ITEMS.register(TreasureConfig.ItemID.SKELETON_ITEM_ID, () -> new SkeletonItem(TreasureBlocks.SKELETON, new Item.Properties()));

	public static RegistryObject<Item> TREASURE_TOOL = ITEMS.register("treasure_tool", () -> new TreasureToolItem(new Item.Properties()));
	
	// TODO potions

	// key map
	public static Multimap<Rarity, KeyItem> keys = ArrayListMultimap.create();
	// lock map
	public static Multimap<Rarity, LockItem> locks = ArrayListMultimap.create();

	/*
	 *  items caches
	 */
	public static final Map<ResourceLocation, Item> ALL_ITEMS = new HashMap<>();
	public static final Map<ResourceLocation, CharmItem> CHARM_ITEMS = new HashMap<>();
	public static final Map<ResourceLocation, Adornment> ADORNMENT_ITEMS = new HashMap<>();

	static {				
		ANGELS_RING = ITEMS.register("angels_ring", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
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
							$.sourceItem = WHITE_PEARL.get().getRegistryName();
							$.levelModifier = new GreatAdornmentLevelModifier();
						}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(AegisCharm.AEGIS_TYPE, 16))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(GreaterHealingCharm.HEALING_TYPE, 16))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(FireImmunityCharm.FIRE_IMMUNITY_TYPE, 16))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000);
				durabilityCap.setMaxRepairs(1);
				durabilityCap.setRepairs(1);

				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
					$.socketable = false;
				}).build();
				IRuneEntity runeEntity = TreasureRunes.RUNE_OF_ANGELS.createEntity();
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((AngelsRune)runeEntity.getRunestone()).initCapabilityApply(cap, durabilityCap, runeEntity);

				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		RING_OF_FORTITUDE = ITEMS.register("ring_of_fortitude", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
					$.innate = true;
					$.imbuable = true;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.namedByCharm = false;
					$.namedByMaterial = false;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem = BLACK_PEARL.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(AegisCharm.AEGIS_TYPE, 16))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 16))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();

				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (mythical)
		SHADOWS_GIFT = ITEMS.register("shadows_gift", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {

				/*
				 *  add enchantment. kinda hacky
				 */
				if (!EnchantmentHelper.hasVanishingCurse(stack)) {
					stack.enchant(Enchantments.VANISHING_CURSE, 1);
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
					$.sourceItem = BLACK_PEARL.get().getRegistryName();
					$.levelModifier = new NoLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(DrainCharm.DRAIN_TYPE, 25))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.BLACK);
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (mythical)
		RING_OF_LIFE_DEATH = ITEMS.register("ring_of_life_death", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				/*
				 *  add enchantment. kinda hacky
				 */
				if (!EnchantmentHelper.hasVanishingCurse(stack)) {
					stack.enchant(Enchantments.VANISHING_CURSE, 1);
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
					$.sourceItem = WHITE_PEARL.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(LifeStrikeCharm.LIFE_STRIKE_TYPE, 25))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(CheatDeathCharm.TYPE, 25))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(RuinCurse.RUIN_TYPE, 15))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.BLOOD);
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		CASTLE_RING = ITEMS.register("castle_ring", () -> new NamedAdornment(AdornmentType.RING, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
					$.innate = true;
					$.imbuable = true;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.namedByCharm = false;
					$.namedByMaterial = false;
					$.baseMaterial = TreasureCharmableMaterials.SILVER.getName();
					$.sourceItem = SAPPHIRE.get().getRegistryName();
					$.levelModifier = new NoLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 10))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 10))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000, TreasureCharmableMaterials.SILVER);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		PEASANTS_FORTUNE = ITEMS.register("peasants_fortune", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(0, 0, 2).with($ -> { // use STONE as source Item so it can't be upgraded
					$.innate = false;
					$.imbuable = false;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.IRON.getName();
					$.sourceItem =  Item.byBlock(Blocks.STONE).getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();

				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();

				IDurabilityCapability durabilityCap = new DurabilityCapability(500, 500, TreasureCharmableMaterials.IRON);
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		/*
		 * special 4million download ring. will auto place in your backpack on new world for 1 month (Dec 2021).
		 * (legendary)
		 */
		GOTTSCHS_RING_OF_MOON = ITEMS.register("gottschs_ring_of_moon", () -> new NamedAdornment(AdornmentType.RING, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				if (!EnchantmentHelper.hasVanishingCurse(stack)) {
					stack.enchant(Enchantments.VANISHING_CURSE, 1);
				}
				ICharmableCapability cap = new CharmableCapability.Builder(4, 0, 0).with($ -> {
					$.innate = true;
					$.imbuable = false;
					$.socketable = false;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.BLACK.getName();
					$.sourceItem =  SAPPHIRE.get().getRegistryName();
					$.levelModifier = new NoLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(HealingCharm.TYPE, 21))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(DrainCharm.DRAIN_TYPE, 21))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 21))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(LifeStrikeCharm.LIFE_STRIKE_TYPE, 21))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);

				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();							
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		/*
		 * special 5 million download ring. will auto place in your backpack on new world for 1 month (June 15-July 15 2021).
		 * (legendary)
		 */
		GOTTSCHS_AMULET_OF_HEAVENS = ITEMS.register("gottschs_amulet_of_heavens", () -> new NamedAdornment(AdornmentType.NECKLACE, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				if (!EnchantmentHelper.hasVanishingCurse(stack)) {
					stack.enchant(Enchantments.VANISHING_CURSE, 1);
				}
				ICharmableCapability cap = new CharmableCapability.Builder(0, 0, 1).with($ -> {
					$.innate = true;
					$.imbuable = false;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem =  SAPPHIRE.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();

				cap.getCharmEntities().get(InventoryType.SOCKET).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(GreaterHealingCharm.HEALING_TYPE, 25))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);

				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(2, 0, 0).with($ -> {
					$.socketable = false;
				}).build();					
				IRuneEntity runeEntity = TreasureRunes.RUNE_OF_DOUBLE_CHARGE.createEntity();	
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((DoubleChargeRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
				runeEntity = TreasureRunes.RUNE_OF_ANGELS.createEntity();
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((AngelsRune)runeEntity.getRunestone()).initCapabilityApply(cap, durabilityCap, runeEntity);
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (legendary)
		BRACELET_OF_WONDER = ITEMS.register("bracelet_of_wonder", () -> new NamedAdornment(AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				if (!EnchantmentHelper.hasVanishingCurse(stack)) {
					stack.enchant(Enchantments.VANISHING_CURSE, 1);
				}
				ICharmableCapability cap = new CharmableCapability.Builder(2, 1, 1).with($ -> {
					$.innate = true;
					$.imbuable = true;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem =  BLACK_PEARL.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 24))).get().createEntity());
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ReflectionCharm.REFLECTION_TYPE, 24))).get().createEntity());

				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);

				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 1).with($ -> {
					$.socketable = true;
				}).build();							
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (epic)
		MEDICS_TOKEN = ITEMS.register("medics_token", () -> new NamedAdornment(AdornmentType.NECKLACE, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
					$.innate = true;
					$.imbuable = false;
					$.socketable = false;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem = SAPPHIRE.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(GreaterHealingCharm.HEALING_TYPE, 20))).get().createEntity());
				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
					$.socketable = false;
				}).build();
				IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (epic)
		ADEPHAGIAS_BOUNTY = ITEMS.register("adephagias_bounty", () -> new NamedAdornment(AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
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
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(SatietyCharm.SATIETY_TYPE, 20))).get().createEntity());
				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
					$.socketable = false;
				}).build();
				IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (epic)
		SALANDAARS_WARD = ITEMS.register("salandaars_ward", () -> new NamedAdornment(AdornmentType.NECKLACE, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
					$.innate = true;
					$.imbuable = false;
					$.socketable = false;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem = RUBY.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(ShieldingCharm.SHIELDING_TYPE, 20))).get().createEntity());
				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(1, 0, 0).with($ -> {
					$.socketable = false;
				}).build();
				IRuneEntity runeEntity = TreasureRunes.RUNE_OF_GREATER_MANA.createEntity();	
				runestonesCap.add(InventoryType.INNATE, runeEntity);
				((GreaterManaRune)runeEntity.getRunestone()).initCapabilityApply(cap, runeEntity);
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});

		// (epic)
		MIRTHAS_TORCH = ITEMS.register("mirthas_torch", () -> new NamedAdornment(AdornmentType.BRACELET, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(1, 0, 0).with($ -> {
					$.innate = true;
					$.imbuable = false;
					$.socketable = false;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem = WHITE_PEARL.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(IlluminationCharm.ILLUMINATION_TYPE, 20))).get().createEntity());
				IDurabilityCapability durabilityCap = new DurabilityCapability();
				durabilityCap.setInfinite(true);
				return new AdornmentCapabilityProvider(cap, durabilityCap);
			}
		});

		POCKET_WATCH = ITEMS.register("pocket_watch", () -> new NamedAdornment(AdornmentType.POCKET, TreasureAdornmentRegistry.GREAT, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
				ICharmableCapability cap = new CharmableCapability.Builder(0, 0, 2).with($ -> {
					$.innate = false;
					$.imbuable = false;
					$.socketable = true;
					$.source = false;
					$.executing = true;
					$.baseMaterial = TreasureCharmableMaterials.GOLD.getName();
					$.sourceItem = TOPAZ.get().getRegistryName();
					$.levelModifier = new GreatAdornmentLevelModifier();
				}).build();
				IDurabilityCapability durabilityCap = new DurabilityCapability(1000, 1000, TreasureCharmableMaterials.GOLD);
				IRunestonesCapability runestonesCap = new RunestonesCapability.Builder(0, 0, 2).with($ -> {
					$.socketable = true;
				}).build();
				return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
			}
		});
	}

	/**
	 * The actual event handler that registers the custom items.
	 *
	 * @param event The event this event handler handles
	 */
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		keys.put(WOOD_KEY.get().getRarity(), WOOD_KEY.get());
		keys.put(STONE_KEY.get().getRarity(), STONE_KEY.get());
		keys.put(EMBER_KEY.get().getRarity(), EMBER_KEY.get());
		keys.put(LEAF_KEY.get().getRarity(), LEAF_KEY.get());
		keys.put(LIGHTNING_KEY.get().getRarity(), LIGHTNING_KEY.get());
		keys.put(IRON_KEY.get().getRarity(), IRON_KEY.get());
		keys.put(GOLD_KEY.get().getRarity(), GOLD_KEY.get());
		keys.put(DIAMOND_KEY.get().getRarity(), DIAMOND_KEY.get());
		keys.put(EMERALD_KEY.get().getRarity(), EMERALD_KEY.get());
		keys.put(RUBY_KEY.get().getRarity(), RUBY_KEY.get());
		keys.put(SAPPHIRE_KEY.get().getRarity(), SAPPHIRE_KEY.get());
		keys.put(JEWELLED_KEY.get().getRarity(), JEWELLED_KEY.get());
		keys.put(METALLURGISTS_KEY.get().getRarity(), METALLURGISTS_KEY.get());
		keys.put(SKELETON_KEY.get().getRarity(), SKELETON_KEY.get());
		keys.put(SPIDER_KEY.get().getRarity(), SPIDER_KEY.get());
		keys.put(WITHER_KEY.get().getRarity(), WITHER_KEY.get());

		locks.put(WOOD_LOCK.get().getRarity(), WOOD_LOCK.get());
		locks.put(STONE_LOCK.get().getRarity(), STONE_LOCK.get());
		locks.put(EMBER_LOCK.get().getRarity(), EMBER_LOCK.get());
		locks.put(LEAF_LOCK.get().getRarity(), LEAF_LOCK.get());
		locks.put(IRON_LOCK.get().getRarity(), IRON_LOCK.get());
		locks.put(GOLD_LOCK.get().getRarity(), GOLD_LOCK.get());
		locks.put(DIAMOND_LOCK.get().getRarity(), DIAMOND_LOCK.get());
		locks.put(EMERALD_LOCK.get().getRarity(), EMERALD_LOCK.get());
		locks.put(RUBY_LOCK.get().getRarity(), RUBY_LOCK.get());
		locks.put(SAPPHIRE_LOCK.get().getRarity(), SAPPHIRE_LOCK.get());
		locks.put(SPIDER_LOCK.get().getRarity(), SPIDER_LOCK.get());
		// NOTE wither lock is a special and isn't used in the general locks list

		// ADORNMENTS		
		TreasureAdornmentRegistry.register(TreasureCharmableMaterials.GOLD.getName(), TOPAZ.get().getRegistryName(), POCKET_WATCH.get());
		
		List<Item> adornments;
		AdornmentItemsBuilder builder = new AdornmentItemsBuilder(Treasure.MODID);
		adornments = builder.useBaseDefaults().build();
		adornments.addAll(builder.useSourceDefaults().build());
		adornments.addAll(builder.clear().useGreatDefaults().build());
		adornments.addAll(builder.useSourceDefaults().build());
		
		List<Item> charms = new ArrayList<>();
		charms.add(createCharm(TreasureCharmableMaterials.COPPER, Items.AIR));
		charms.add(createCharm(TreasureCharmableMaterials.SILVER, Items.AIR));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.AIR));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, TOPAZ.get()));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, ONYX.get()));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.DIAMOND));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, Items.EMERALD));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, RUBY.get()));
		charms.add(createCharm(TreasureCharmableMaterials.GOLD, SAPPHIRE.get()));
		charms.add(createCharm(TreasureCharmableMaterials.LEGENDARY, Items.AIR));
		charms.add(createCharm(TreasureCharmableMaterials.MYTHICAL, Items.AIR));
		
		// RUNESTONE
		TreasureRunes.register(TreasureRunes.RUNE_OF_MANA, MANA_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_GREATER_MANA, GREATER_MANA_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_DURABILITY, DURABILITY_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_QUALITY, QUALITY_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_EQUIP_AS_MANA, EQUIP_MANA_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_ANVIL, ANVIL_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_ANGELS, ANGELS_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_PERSISTENCE, PERSISTENCE_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_SOCKETS, SOCKETS_RUNESTONE.get());
		TreasureRunes.register(TreasureRunes.RUNE_OF_DOUBLE_CHARGE, DOUBLE_CHARGE_RUNESTONE.get());

		// add charms
		charms.forEach(charm -> {
			ALL_ITEMS.put(charm.getRegistryName(), charm);
			CHARM_ITEMS.put(charm.getRegistryName(), (CharmItem) charm);
			event.getRegistry().register(charm);
		});

		// add adornments
		adornments.forEach(a -> {
			ALL_ITEMS.put(a.getRegistryName(), a); // NOTE this ITEMS was a local cache
			ADORNMENT_ITEMS.put(a.getRegistryName(), (Adornment) a);
			event.getRegistry().register(a);
		});
	}

	/**
	 * 
	 */
	public static void register() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(eventBus);		
	}

	// TODO move charms code in here
	public static class CharmItemsBuilder {
		
	}
	
	/*
	 * 
	 */
	public static class AdornmentItemsBuilder {
		protected List<AdornmentType> types = new ArrayList<>();
		protected List<AdornmentSize> sizes = new ArrayList<>();
		protected List<CharmableMaterial> materials = new ArrayList<>();
		protected List<ResourceLocation> sources = new ArrayList<>();

		protected Map<CharmableMaterial, Integer> materialInnates = Maps.newHashMap();		
		protected Map<AdornmentSize, ILevelModifier> levelModifiers = Maps.newHashMap();

		protected String modid;
		
		/**
		 * 
		 */
		public AdornmentItemsBuilder(String modid) {
			this.modid = modid;
		}

		public AdornmentItemsBuilder clear() {
			types.clear();
			sizes.clear();
			materials.clear();
			sources.clear();
			materialInnates.clear();
			levelModifiers.clear();
			return this;
		}
		
		/*
		 * convenience setup
		 */
		public AdornmentItemsBuilder useBaseDefaults() {
			types(AdornmentType.BRACELET, AdornmentType.NECKLACE, AdornmentType.RING);
			sizes(TreasureAdornmentRegistry.STANDARD);
			materials(TreasureCharmableMaterials.IRON, 
				TreasureCharmableMaterials.COPPER, 
				TreasureCharmableMaterials.SILVER, 
				TreasureCharmableMaterials.GOLD);
			mapInnate(TreasureCharmableMaterials.IRON, 0);
			mapInnate(TreasureCharmableMaterials.COPPER, 1);
			mapInnate(TreasureCharmableMaterials.SILVER, 2);
			mapInnate(TreasureCharmableMaterials.GOLD, 3);
			mapLevelModifier(TreasureAdornmentRegistry.STANDARD, new NoLevelModifier());
			mapLevelModifier(TreasureAdornmentRegistry.GREAT, new GreatAdornmentLevelModifier());
			mapLevelModifier(TreasureAdornmentRegistry.LORDS, new LordsAdornmentLevelModifier());
			return this;
		}
		
		/*
		 * Sources have to be newly constructed resource locations,
		 * because the items are deferred and aren't registered yet.
		 * (Only the vanilla minecraft items are registered at this point)
		 */
		public AdornmentItemsBuilder useSourceDefaults() {
			sources(
					Items.DIAMOND.getRegistryName(),
					Items.EMERALD.getRegistryName(),
					new ResourceLocation(Treasure.MODID, "topaz"),
					new ResourceLocation(Treasure.MODID, "onyx"),
					new ResourceLocation(Treasure.MODID, "ruby"),
					new ResourceLocation(Treasure.MODID, "sapphire"),
					new ResourceLocation(Treasure.MODID, "white_pearl"),
					new ResourceLocation(Treasure.MODID, TreasureConfig.ItemID.BLACK_PEARL_ID)
					);
			return this;
		}
		
		public AdornmentItemsBuilder useGreatDefaults() {
			types(AdornmentType.BRACELET, AdornmentType.NECKLACE, AdornmentType.RING);
			sizes(TreasureAdornmentRegistry.GREAT);
			materials(
					TreasureCharmableMaterials.IRON, 
					TreasureCharmableMaterials.COPPER, 
					TreasureCharmableMaterials.SILVER, 
					TreasureCharmableMaterials.GOLD,
					TreasureCharmableMaterials.BLOOD,
					TreasureCharmableMaterials.BLACK
					);
			mapInnate(TreasureCharmableMaterials.IRON, 0);
			mapInnate(TreasureCharmableMaterials.COPPER, 1);
			mapInnate(TreasureCharmableMaterials.SILVER, 2);
			mapInnate(TreasureCharmableMaterials.GOLD, 3);
			mapInnate(TreasureCharmableMaterials.BLOOD, 2);
			mapInnate(TreasureCharmableMaterials.BLACK, 3);
			mapLevelModifier(TreasureAdornmentRegistry.STANDARD, new NoLevelModifier());
			mapLevelModifier(TreasureAdornmentRegistry.GREAT, new GreatAdornmentLevelModifier());
			mapLevelModifier(TreasureAdornmentRegistry.LORDS, new LordsAdornmentLevelModifier());
			
			return this;
		}
		
		public AdornmentItemsBuilder types(AdornmentType... types) {
			getTypes().addAll(Arrays.asList(types));
			return this;
		}
		
		public AdornmentItemsBuilder sizes(AdornmentSize... sizes) {
			getSizes().addAll(Arrays.asList(sizes));
			return this;
		}
		
		public AdornmentItemsBuilder materials(CharmableMaterial... materials) {
			getMaterials().addAll(Arrays.asList(materials));
			return this;
		}
		
		public AdornmentItemsBuilder sources(ResourceLocation... sources) {
			getSources().addAll(Arrays.asList(sources));
			return this;
		}
		
		public AdornmentItemsBuilder mapInnate(CharmableMaterial material, int num) {
			getMaterialInnates().put(material, num);
			return this;
		}
		
		public AdornmentItemsBuilder mapLevelModifier(AdornmentSize size, ILevelModifier modifier) {
			getLevelModifiers().put(size, modifier);
			return this;
		}
		
		/**
		 * Legacy method.
		 * For use in Treasure2 1.16.5 in the current setup, where the adornments
		 * are created in the RegistryEvent and registered directly with the registry.
		 * ie. NOT DeferredRegistry
		 * This allows previously deferred items to be registered and used within
		 * the adornment creation.
		 * @return
		 */
		public List<Item> build() {
			List<Item> adornments = new ArrayList<>();
			
			if (types.isEmpty()) return adornments;
			if (sizes.isEmpty()) return adornments;
			
			List<ResourceLocation> tempSources = new ArrayList<>();
			if (sources.isEmpty()) {
				tempSources.add(Items.AIR.getRegistryName());
			}
			else {
				tempSources.addAll(sources);
			}
			
			// create the adornment
			types.forEach(type -> {
				sizes.forEach(size -> {
					materials.forEach(material -> {
						tempSources.forEach(source -> {
							Adornment a = createAdornment(type, material, size, source);
							Treasure.LOGGER.debug("adding adornment item -> {}", a.getRegistryName());
							adornments.add(a);
							TreasureAdornmentRegistry.register(material.getName(), source, a);
						});
					});
				});
			});
			return adornments;
		}		
	
		/**
		 * Deferred build.
		 * This method returns a list of String/Supplier Pairs.
		 * This method can be used in a static call of a class to construct RegistryObjects
		 * The String in the Pair is used as the name (without the namespace)
		 * NOTE adornments are deferred and thus are not registered in
		 * TreasureAdornmentRegistry yet. That would have to happen in a post init like event.
		 * @return
		 */
		public List<Pair<String, Supplier<Adornment>>> deferredBuild() {
			List<Pair<String, Supplier<Adornment>>> adornments = new ArrayList<>();
			
			if (types.isEmpty()) return adornments;
			if (sizes.isEmpty()) return adornments;
			
			List<ResourceLocation> tempSources = new ArrayList<>();
			if (sources.isEmpty()) {
				tempSources.add(Items.AIR.getRegistryName());
			}
			else {
				tempSources.addAll(sources);
			}
			
			// create the adornment
			types.forEach(type -> {
				sizes.forEach(size -> {
					materials.forEach(material -> {
						tempSources.forEach(source -> {
							// build the name
							String name = (size == TreasureAdornmentRegistry.STANDARD ? "" : size.getName() + "_") + (source == Items.AIR.getRegistryName() ? "" :  source.getPath() + "_") + material.getName().getPath() + "_" + type.toString();
							// build the adornment supplier
							Supplier<Adornment> a = deferredCreateAdornment(type, material, size, source);
							Treasure.LOGGER.debug("adding deferred adornment item -> {}", name);
							
							// build a pair
							Pair<String, Supplier<Adornment>> pair = Pair.of(name, a);
							adornments.add(pair);
						});
					});
				});
			});
			return adornments;
		}

		/**
		 * Legacy method.
		 * For use in Treasure2 1.16.5 in the current setup, where the adornments
		 * are created in the RegistryEvent and registered directly with the registry.
		 * ie. NOT DeferredRegistry
		 * This allows previously deferred items to be registered and used within
		 * the adornment creation.
		 * @param name
		 * @param type
		 * @param material
		 * @param size
		 * @param source
		 * @return
		 */
		public Adornment createAdornment(AdornmentType type, CharmableMaterial material,	AdornmentSize size, ResourceLocation source) {
			String name = (size == TreasureAdornmentRegistry.STANDARD ? "" : size.getName() + "_") + (source == Items.AIR.getRegistryName() ? "" :  source.getPath() + "_") + material.getName().getPath() + "_" + type.toString();
			@SuppressWarnings("deprecation")
			Adornment a = new Adornment(modid, name, type, size, new Item.Properties().tab(TreasureItemGroups.ADORNMENTS_TAB)) {
				public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
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

					return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
				}
			};
			// return the adornment
			return a;
		}
		
		public Supplier<Adornment> deferredCreateAdornment(AdornmentType type, CharmableMaterial material,	AdornmentSize size, ResourceLocation source) {
			return () -> new Adornment(type, size, new Item.Properties().tab(TreasureItemGroups.ADORNMENTS_TAB)) {
				@Override
				public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
					try {
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
					
					return new AdornmentCapabilityProvider(cap, runestonesCap, durabilityCap);
					}
					catch(Exception e) {
						Treasure.LOGGER.error("failed in initCaps", e);
					}
					return null;
				}
			};
		}
		
		public List<AdornmentType> getTypes() {
			return types;
		}

		public List<AdornmentSize> getSizes() {
			return sizes;
		}

		public List<CharmableMaterial> getMaterials() {
			return materials;
		}

		public List<ResourceLocation> getSources() {
			return sources;
		}

		public Map<CharmableMaterial, Integer> getMaterialInnates() {
			return materialInnates;
		}

		public Map<AdornmentSize, ILevelModifier> getLevelModifiers() {
			return levelModifiers;
		}

		public String getModid() {
			return modid;
		}
	}

	private static CharmItem createCharm(CharmableMaterial material, Item source) {
		String name = (source == Items.AIR ? 
				material.getName().getPath() : source.getRegistryName().getPath())  + "_charm";
		Treasure.LOGGER.debug("creating charmItem -> {}", name);
		CharmItem charm = new CharmItem(Treasure.MODID, name, new Item.Properties()) {
			public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
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
				cap.getCharmEntities().get(InventoryType.INNATE).add(TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get().createEntity());

				return new CharmableCapabilityProvider(cap);
			}
		};
		// register the charm
//		ITEMS.register(name, () -> charm);
		// return the charm
		return charm;
	}

	/**
	 * Register the {@link IItemColor} handlers.
	 *
	 * @param event The event
	 */
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerItemColours(final ColorHandlerEvent.Item event) {
		final BlockColors blockColors = event.getBlockColors();
		final ItemColors itemColors = event.getItemColors();

		// Use the Block's colour handler for an ItemBlock
		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			final BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
			return blockColors.getColor(state, null, null, tintIndex);
		};

		itemColors.register(itemBlockColourHandler, TreasureBlocks.FALLING_GRASS);
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
			ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE).map(c -> c).orElseThrow(() -> new IllegalStateException());
			if (cap != null) {
				Treasure.LOGGER.debug("name -> {}, charm level -> {}, level -> {}", itemStack.getDisplayName().getString(), cap.getMaxCharmLevel(), level);
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
			charmItem = TreasureItems.CHARM_BOOK.get();
		}
		else {
			charmItem = getCharmItemByLevel(level);
		}
		
		// get the charm
		Optional<ICharm> charm = TreasureCharmRegistry.get(ModUtils.asLocation(Charm.Builder.makeName(charmName, level)));
		if (!charm.isPresent()) {
			return Optional.empty();
		}

		/*
		 *  add charm to charmItem
		 *  note: an itemStack is being created, call the initCapabilities() method. ensure to remove any charms
		 */
		ItemStack charmStack = new ItemStack(charmItem);
		charmStack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
			cap.clearCharms();
			cap.add(InventoryType.INNATE, charm.get().createEntity());
		});
		return Optional.of(charmStack);
	}
	
	public static Comparator<CharmItem> charmLevelComparator = new Comparator<CharmItem>() {
		@Override
		public int compare(CharmItem p1, CharmItem p2) {
			ItemStack stack1 = new ItemStack(p1);
			ItemStack stack2 = new ItemStack(p2);
			// use p1 < p2 because the sort should be ascending
			if (stack1.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap.getMaxCharmLevel()).orElse(0) 
					> stack2.getCapability(TreasureCapabilities.CHARMABLE).map(cap -> cap.getMaxCharmLevel()).orElse(0)) {
				// greater than
				return 1;
			}
			else {
				// less than
				return -1;
			}
		}
	};

	/**
	 * 
	 * @author Mark Gottschling on Aug 12, 2020
	 *
	 */
	public static class ModItemGroup extends ItemGroup {
		private final Supplier<ItemStack> iconSupplier;

		public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		public ItemStack makeIcon() {
			return iconSupplier.get();
		}
	}
}
