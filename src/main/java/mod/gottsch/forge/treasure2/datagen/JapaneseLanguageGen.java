/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.datagen;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * 
 * @author Mark Gottschling on Apr 6, 2022
 *
 */
public class JapaneseLanguageGen extends LanguageProvider {

    public JapaneseLanguageGen(PackOutput output, String locale) {
        super(output, Treasure.MODID, locale);
    }
    
    @Override
    protected void addTranslations() {
    	// tabs
        add("itemGroup." + Treasure.MODID, "Treasure2");

        // keys
//        add(TreasureItems.WOOD_KEY.get(), "Wood Key");
//        add(TreasureItems.STONE_KEY.get(), "Stone Key");
//        add(TreasureItems.LEAF_KEY.get(), "Leaf Key");
        add(TreasureItems.EMBER_KEY.get(), "残り火のカギ");
//        add(TreasureItems.LIGHTNING_KEY.get(), "Lightning Key");
//
        add(TreasureItems.IRON_KEY.get(), "鉄のカギ");
        add(TreasureItems.GOLD_KEY.get(), "金のカギ");
//        add(TreasureItems.METALLURGISTS_KEY.get(), "Metallurgists Key");
//
        add(TreasureItems.DIAMOND_KEY.get(), "ダイヤモンドのカギ");
        add(TreasureItems.EMERALD_KEY.get(), "エメラルドのカギ");
//        add(TreasureItems.TOPAZ_KEY.get(), "Topaz Key");
//        add(TreasureItems.ONYX_KEY.get(), "Onyx Key");
//        add(TreasureItems.RUBY_KEY.get(), "Ruby Key");
//        add(TreasureItems.SAPPHIRE_KEY.get(), "Sapphire Key");
        add(TreasureItems.JEWELLED_KEY.get(), "宝石飾りのカギ");
//
//        add(TreasureItems.SPIDER_KEY.get(), "Spider Key");
//        add(TreasureItems.WITHER_KEY.get(), "Wither Key");
//
//        add(TreasureItems.SKELETON_KEY.get(), "Skeleton Key");
//        add(TreasureItems.PILFERERS_LOCK_PICK.get(), "Pilferer's Lock Pick");
//        add(TreasureItems.THIEFS_LOCK_PICK.get(), "Thief's Lock Pick");
//        add(TreasureItems.ONE_KEY.get(), "The One Key");
//        // locks
//        add(TreasureItems.WOOD_LOCK.get(), "Wood Lock");
//        add(TreasureItems.STONE_LOCK.get(), "Stone Lock");
//        add(TreasureItems.LEAF_LOCK.get(), "Leaf Lock");
        add(TreasureItems.EMBER_LOCK.get(), "残り火の錠前");
        add(TreasureItems.IRON_LOCK.get(), "鉄の錠前");
        add(TreasureItems.GOLD_LOCK.get(), "金の錠前");
//
        add(TreasureItems.DIAMOND_LOCK.get(), "ダイヤモンドの錠前");
        add(TreasureItems.EMERALD_LOCK.get(), "エメラルドの錠前");
//        add(TreasureItems.TOPAZ_LOCK.get(), "Topaz Lock");
//        add(TreasureItems.ONYX_LOCK.get(), "Onyx Lock");
//        add(TreasureItems.RUBY_LOCK.get(), "Ruby Lock");
//        add(TreasureItems.SAPPHIRE_LOCK.get(), "Sapphire Lock");
//
//        add(TreasureItems.SPIDER_LOCK.get(), "Spider Lock");
//        add(TreasureItems.WITHER_LOCK.get(), "Wither Lock");
//
//        // key ring
        add(TreasureItems.KEY_RING.get(), "キーホルダー");
//
//        // coins
        add(TreasureItems.COPPER_COIN.get(), "銅貨");
//        add(TreasureItems.SILVER_COIN.get(), "Silver Coin");
        add(TreasureItems.GOLD_COIN.get(), "金貨");
//
//        add(TreasureItems.TOPAZ.get(), "Topaz");
//        add(TreasureItems.ONYX.get(), "Onyx");
//        add(TreasureItems.RUBY.get(), "Ruby");
//        add(TreasureItems.SAPPHIRE.get(), "Sapphire");
//        add(TreasureItems.WHITE_PEARL.get(), "White Pearl");
        add(TreasureItems.BLACK_PEARL.get(), "黒真珠");
//
//        // other
//        add(TreasureItems.TREASURE_TOOL.get(), "Treasure Tool");
//        add(TreasureItems.POUCH.get(), "Pouch");
//        add(TreasureBlocks.SPANISH_MOSS.get(), "Spanish Moss");
//        add(TreasureBlocks.WISHING_WELL.get(), "Wishing Well Stone");
//        add(TreasureBlocks.WISHING_WELL_COBBLESTONE.get(), "Wishing Well Cobblestone");
//        add(TreasureBlocks.WISHING_WELL_STONE_BRICKS.get(), "Wishing Well Stone Bricks");
//        add(TreasureBlocks.WISHING_WELL_MOSSY_STONE_BRICKS.get(), "Wishing Well Mossy Stone Bricks");
//        add(TreasureBlocks.DESERT_WISHING_WELL.get(), "Desert Wishing Well Stone");
        add(TreasureItems.EYE_PATCH.get(), "眼帯");
        add(TreasureItems.CLOVER.get(), "四葉のクローバー");
//
//        // swords
        add(TreasureItems.COPPER_SHORT_SWORD.get(), "銅の短剣");
        add(TreasureItems.CHIPPED_COPPER_SHORT_SWORD.get(), "欠けた銅の短剣");
        add(TreasureItems.IRON_SHORT_SWORD.get(), "鉄の短剣");
        add(TreasureItems.CHIPPED_IRON_SHORT_SWORD.get(), "欠けた鉄の短剣");
//        add(TreasureItems.STEEL_SHORT_SWORD.get(), "Steel Short Sword");
        add(TreasureItems.CHIPPED_STEEL_SHORT_SWORD.get(), "欠けた鋼の短剣");
//        add(TreasureItems.STEEL_SWORD.get(), "Steel Sword");
        add(TreasureItems.IRON_BROADSWORD.get(), "鉄の大剣");
//        add(TreasureItems.STEEL_BROADSWORD.get(), "Steel Broadsword");
        add(TreasureItems.COPPER_RAPIER.get(), "銅のレイピア");
//
//        // specials
//        add(TreasureItems.SKULL_SWORD.get(), "Skull Sword");
        add(TreasureItems.BLACK_SWORD.get(), "黒剣");
//        add(TreasureItems.SWORD_OMENS.get(), "The Sword of Omens");
//        add(TreasureItems.SWORD_POWER.get(), "The Sword of Power");
        add(TreasureItems.CALLANDOR.get(), "霊剣カランドル");
//        add(TreasureItems.ORCUS.get(), "Orcus");
//        add(TreasureItems.SNAKE_EYES_KATANA.get(), "Snake Eye's Katana");
//        add(TreasureItems.STORM_SHADOWS_KATANA.get(), "Storm Shadow's Daisho");
//        add(TreasureItems.OATHBRINGER.get(), "Oathbringer");
//        add(TreasureItems.MJOLNIR.get(), "Mjolnir");
        add(TreasureItems.AXE_DURIN.get(), "ドゥリンの戦斧");
        add(TreasureItems.HEADSMANS_AXE.get(), "首長の長斧");
//
//        add(TreasureItems.STEEL_MACHETE.get(), "Steel Machete");
//        add(TreasureItems.SHADOW_MACHETE.get(), "Shadow Machete");
//
        add(TreasureItems.IRON_FALCHION.get(), "鉄のファルシオン");
//        add(TreasureItems.STEEL_FALCHION.get(), "Steel Falchion");
//        add(TreasureItems.SHADOW_FALCHION.get(), "Shadow Falchion");
//
//        // maces/hammers
        add(TreasureItems.IRON_MACE.get(), "鉄のメイス");
//        add(TreasureItems.STEEL_MACE.get(), "Steel Mace");
//
//        // axes
        add(TreasureItems.COPPER_BROAD_AXE.get(), "銅の大斧");
        add(TreasureItems.IRON_BROAD_AXE.get(), "鉄の大斧");
//        add(TreasureItems.STEEL_BROAD_AXE.get(), "Steel Broad Axe");
        add(TreasureItems.IRON_DWARVEN_AXE.get(), "鉄のドワーフ斧");
        
        // chests
        add(TreasureBlocks.WOOD_CHEST.get(), "木のチェスト");
        add(TreasureBlocks.CRATE_CHEST.get(), "木箱");
        add(TreasureBlocks.MOLDY_CRATE_CHEST.get(), "カビた木箱");
        add(TreasureBlocks.IRONBOUND_CHEST.get(), "鉄張りのチェスト");
        add(TreasureBlocks.SAFE.get(), "金庫");
        add(TreasureBlocks.PIRATE_CHEST.get(), "海賊のチェスト");
        add(TreasureBlocks.IRON_STRONGBOX.get(), "鉄の保管庫");
        add(TreasureBlocks.GOLD_STRONGBOX.get(), "金の保管庫");
        add(TreasureBlocks.DREAD_PIRATE_CHEST.get(), "大海賊のチェスト");
        add(TreasureBlocks.COMPRESSOR_CHEST.get(), "圧縮チェスト");
        add(TreasureBlocks.SKULL_CHEST.get(), "ドクロのチェスト");
        add(TreasureBlocks.GOLD_SKULL_CHEST.get(), "金ドクロのチェスト");
        add(TreasureBlocks.CRYSTAL_SKULL_CHEST.get(), "水晶ドクロのチェスト");
        add(TreasureBlocks.CAULDRON_CHEST.get(), "大釜チェスト");
        add(TreasureBlocks.SPIDER_CHEST.get(), "クモのチェスト");
        add(TreasureBlocks.VIKING_CHEST.get(), "バイキングのチェスト");
        add(TreasureBlocks.CARDBOARD_BOX.get(), "段ボール箱");
        add(TreasureBlocks.MILK_CRATE.get(), "ミルク木箱");
        add(TreasureBlocks.BARREL_CHEST.get(), "宝入りの樽");
        add(TreasureBlocks.VANILLA_CHEST.get(), "宝のチェスト");
        add(TreasureBlocks.WITHER_CHEST.get(), "ウィザーのチェスト");
        add(TreasureBlocks.WITHER_CHEST_TOP.get(), "ウィザーのチェスト");

//        // legacy wither
//        add(TreasureBlocks.WITHER_BRANCH.get(), "Witherwood Branch");
//        add(TreasureBlocks.WITHER_ROOT.get(), "Witherwood Root");
//        add(TreasureBlocks.WITHER_LOG.get(), "Witherwood Log");
//        add(TreasureBlocks.WITHER_BROKEN_LOG.get(), "Witherwood Broken Log");
//        add(TreasureBlocks.WITHER_SOUL_LOG.get(), "Witherwood Soul Log");
//        add(TreasureBlocks.WITHER_PLANKS.get(), "Witherwood Planks");
//
//        // current wither
//        add(TreasureBlocks.WITHERWOOD_BRANCH.get(), "Witherwood Branch");
//        add(TreasureBlocks.WITHERWOOD_ROOT.get(), "Witherwood Root");
//
//        add(TreasureBlocks.WITHERWOOD_LOG.get(), "Witherwood Log");
//        add(TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(), "Witherwood Broken Log");
//        add(TreasureBlocks.WITHERWOOD_SOUL_LOG.get(), "Witherwood Soul Log");
//        add(TreasureBlocks.WITHERWOOD_PLANKS.get(), "Witherwood Planks");
//        add(TreasureBlocks.WITHERWOOD_SLAB.get(), "Witherwood Slab");
//        add(TreasureBlocks.WITHERWOOD_STAIRS.get(), "Witherwood Stairs");
//        add(TreasureBlocks.WITHERWOOD_WOOD.get(), "Witherwood Wood");
//        add(TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(), "Stripped Witherwood Log");
//        add(TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(), "Stripped Witherwood Wood");
//        add(TreasureBlocks.WITHERWOOD_FENCE.get(), "Witherwood Fence");
//        add(TreasureBlocks.WITHERWOOD_FENCE_GATE.get(), "Witherwood Fence Gate");
//        add(TreasureBlocks.WITHERWOOD_BUTTON.get(), "Witherwood Button");
//        add(TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get(), "Witherwood Pressure Plate");
//        add(TreasureBlocks.WITHERWOOD_DOOR.get(), "Witherwood Door");
//        add(TreasureBlocks.WITHERWOOD_TRAPDOOR.get(), "Witherwood Trap Door");
//        add(TreasureBlocks.WITHERWOOD_SIGN.get(), "Witherwood Sign");
//        add(TreasureBlocks.WITHERWOOD_HANGING_SIGN.get(), "Witherwood Hanging Sign");
//        add(TreasureBlocks.WITHERWOOD_TWIG.get(), "Witherwood Twig");
//
//        add(TreasureBlocks.STRANGLE_VINES.get(), "Strangle Vine");
//        add(TreasureBlocks.STRANGLE_VINES_PLANT.get(), "Strangle Vine Plant");
//
//        // ore
//        add(TreasureBlocks.TOPAZ_ORE.get(), "Topaz Ore");
//        add(TreasureBlocks.ONYX_ORE.get(), "Onyx Ore");
//        add(TreasureBlocks.RUBY_ORE.get(), "Ruby Ore");
//        add(TreasureBlocks.SAPPHIRE_ORE.get(), "Sapphire Ore");
//
//        add(TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get(), "Deepslate Topaz Ore");
//        add(TreasureBlocks.DEEPSLATE_ONYX_ORE.get(), "Deepslate Onyx Ore");
//        add(TreasureBlocks.DEEPSLATE_RUBY_ORE.get(), "Deepslate Ruby Ore");
//        add(TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), "Deepslate Sapphire Ore");
//
//        // gravestones
        add(TreasureBlocks.GRAVESTONE1_STONE.get(), "墓石");
//        add(TreasureBlocks.GRAVESTONE1_COBBLESTONE.get(), "Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE1_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE1_POLISHED_GRANITE.get(), "Granite Gravestone");
//        add(TreasureBlocks.GRAVESTONE1_OBSIDIAN.get(), "Obsidian Gravestone");
//        add(TreasureBlocks.GRAVESTONE1_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_STONE.get(), "Stone Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_COBBLESTONE.get(), "Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_POLISHED_GRANITE.get(), "Granite Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_OBSIDIAN.get(), "Obsidian Gravestone");
//        add(TreasureBlocks.GRAVESTONE2_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_STONE.get(), "Stone Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_COBBLESTONE.get(), "Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_POLISHED_GRANITE.get(), "Granite Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_OBSIDIAN.get(), "Obsidian Gravestone");
//        add(TreasureBlocks.GRAVESTONE3_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
//        add(TreasureBlocks.SKULL_CROSSBONES.get(), "Skull and Crossbones");
//        add(TreasureBlocks.SKELETON.get(), "Skeleton");
//        add(TreasureBlocks.CLOVER.get(), "Four Leaf Clover");
//
//        add(TreasureBlocks.GRAVESTONE1_SPAWNER_STONE.get(), "Stone Gravestone Spawner");
//        add(TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE.get(), "Cobblestone Gravestone Spawner");
//        add(TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN.get(), "Obsidian Gravestone Spawner");
//
//        add(TreasureBlocks.FALLING_GRASS.get(), "Grass");
//        add(TreasureBlocks.FALLING_SAND.get(), "Sand");
//        add(TreasureBlocks.FALLING_RED_SAND.get(), "Red Sand");
//
//        // mobs
        add(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), "地縛霊");
        add(TreasureEntities.WITHERWOOD_GOLEM_ENTITY_TYPE.get(), "ウィザーウッドゴーレム");
        add(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), "チェストミミック");
        add(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "海賊チェストミミック");
        add(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), "イバキングミミック");
        add(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), "大釜ミミック");
        add(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "木箱ミミック");
        add(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "苔むした木箱ミミック");
        add(TreasureEntities.CARDBOARD_BOX_MIMIC_ENTITY_TYPE.get(), "段ボールミミック");
        add(TreasureEntities.MILK_CRATE_MIMIC_ENTITY_TYPE.get(), "ミルク入れミミック");
        add(TreasureEntities.BARREL_MIMIC_ENTITY_TYPE.get(), "樽ミミック");
        add(TreasureEntities.VANILLA_CHEST_MIMIC_ENTITY_TYPE.get(), "宝箱ミミック");

        // eggs
        add(TreasureItems.BOUND_SOUL_EGG.get(), "地縛霊のスポーンエッグ");
        add(TreasureItems.WITHERWOOD_GOLEM_EGG.get(), "ウィザーウッドゴーレムのスポーンエッグ");
        add(TreasureItems.WOOD_CHEST_MIMIC_EGG.get(), "チェストミミックのスポーンエッグ");
        add(TreasureItems.PIRATE_CHEST_MIMIC_EGG.get(), "海賊チェストミミックのスポーンエッグ");
        add(TreasureItems.VIKING_CHEST_MIMIC_EGG.get(), "バイキングミミックのスポーンエッグ");
        add(TreasureItems.CAULDRON_CHEST_MIMIC_EGG.get(), "大釜ミミックのスポーンエッグ");
        add(TreasureItems.CRATE_CHEST_MIMIC_EGG.get(), "木箱ミミックのスポーンエッグ");
        add(TreasureItems.MOLDY_CRATE_CHEST_MIMIC_EGG.get(), "苔むした木箱ミミックのスポーンエッグ");
        add(TreasureItems.CARDBOARD_BOX_MIMIC_EGG.get(), "段ボールミミックのスポーンエッグ");
        add(TreasureItems.MILK_CRATE_MIMIC_EGG.get(), "ミルク入れミミックのスポーンエッグ");
        add(TreasureItems.BARREL_MIMIC_EGG.get(), "樽ミミックのスポーンエッグ");
        add(TreasureItems.VANILLA_CHEST_MIMIC_EGG.get(), "宝箱ミミックのスポーンエッグ");
//
//        /*
//         *  Util.tooltips
//         */
//        // general
//        add(LangUtil.tooltip("boolean.yes"), "Yes");
//        add(LangUtil.tooltip("boolean.no"), "No");
//        add(LangUtil.tooltip("hold_shift"), "Hold [SHIFT] to expand");
//        add(LangUtil.tooltip("treasure_tool"), "Required for most Treasure2 recipes");
//        add(LangUtil.tooltip("pouch"), "Holds small valuables - coins, gems, charms, etc.");
//        add(LangUtil.tooltip("wishable"), "Can be thrown into Wishing Wells for loot");
//        add(LangUtil.tooltip("clover"), "Can use on certain blocks, like Mossy Cobblestone, to transform them into Wishing Well blocks.~All adjacent block with in a 4 block radius will be transformed.");
//
//        // keys and locks
//        add(LangUtil.tooltip("key_lock.rarity"), "Rarity: %s");
//        add(LangUtil.tooltip("key_lock.category"), "Category: %s");
//        add(LangUtil.tooltip("key_lock.craftable"), "Craftable: %s");
//        add(LangUtil.tooltip("key_lock.breakable"), "Breakable: %s");
//        add(LangUtil.tooltip("key_lock.damageable"), "Damageable: %s");
//        add(LangUtil.tooltip("key_lock.accepts_keys"), "Accepts Keys:");
//        add(LangUtil.tooltip("key_lock.specials"), "Specials: %s");
//        add(LangUtil.tooltip("key_lock.skeleton_key.specials"), "Opens COMMON, UNCOMMON, SCARCE~and RARE locks (excluding Wither)");
//        add(LangUtil.tooltip("key_lock.ember_key.specials"), "Destroys Wood and Leaf Locks");
//        add(LangUtil.tooltip("key_lock.ember_lock.specials"), "Destroys All Keys regardless of breakability (excluding Ember and Lightning)");
//        add(LangUtil.tooltip("key_lock.lightning_key.specials"), "Opens any lock in ELEMENTAL category");
//        add(LangUtil.tooltip("key_lock.metallurgists_key.specials"), "Opens any lock in METALS category");
//        add(LangUtil.tooltip("key_lock.jewelled_key.specials"), "Opens any lock in GEMS category");
//        add(LangUtil.tooltip("key_lock.pilferers_lock_pick.specials"), "Opens COMMON (%s%%) and UNCOMMON (%s%%) locks");
//        add(LangUtil.tooltip("key_lock.thiefs_lock_pick.specials"), "Opens COMMON (%s%%), UNCOMMON (%s%%) and SCARCE (%s%%) locks");
//        add(LangUtil.tooltip("key_lock.one_key.specials"), "Opens ALL locks");
//        add(LangUtil.tooltip("key_lock.one_key.lore"), "One Key to rule them all.");
//        add(LangUtil.tooltip("key_lock.key_ring"), "Container for keys");
//
//        // chests
//        add(LangUtil.tooltip("chest.locked"), "Locked!");
//        add(LangUtil.tooltip("chest.usage"), "Can be thrown into Wishing Well to remove all locks... for a price.");
//        add(LangUtil.tooltip("chest.rarity"), "Rarity: %s");
//        add(LangUtil.tooltip("chest.max_locks"), "Max Locks: %s");
//        add(LangUtil.tooltip("chest.container_size"), "Inventory Size: %s");
//
//        // capabilities
//        add(LangUtil.tooltip("cap.durability.amount"), "Durability: [%s/%s]");
//        add(LangUtil.tooltip("cap.durability.amount.infinite"), "Durability: Infinite");
//        add(LangUtil.tooltip("cap.durability.repairs"), "R[%s/%s]");
//        add(LangUtil.tooltip("cap.spell.recharges"), "R[%s/%s]");
//
//        // weapons
//        add(LangUtil.tooltip("weapons.black_sword.lore"), "Sword of the Avatar. Host of the daemon Arcadion.");
//        add(LangUtil.tooltip("weapons.sword_of_omens.lore"), "'Thunder... Thunder... Thunder... ThunderCats HO!'");
//        add(LangUtil.tooltip("weapons.sword_of_power.lore"), "'By the power of Grayskull.... I have the power!'");
//
//        add(LangUtil.tooltip("weapons.orcus.lore"), "'Fair lady, throw those costly robes aside,~"
//        		+ "No longer may you glory in your pride.~"
//        		+ "Take leave of all sour vain delight~"
//        		+ "I'm come to summon you away this night'.~"
//        		+ "    -The Grim Reaper");
//        add(LangUtil.tooltip("weapons.snake_eyes_katana.lore"), "Move with the wind, and you will never be heard.~"
//        		+ "    -Snake Eyes");
//        add(LangUtil.tooltip("weapons.storm_shadows_katana.lore"), "Most people will tell you that ninjas dont exist.~"
//        		+ "Thats what the ninjas want you to believe ...~"
//        		+ "    -Storm Shadow");
//        add(LangUtil.tooltip("weapons.oathbringer.lore"), "Life before death,~"
//        		+ "strength before weakness,~"
//        		+ "journey before destination.");
//
//        add(LangUtil.tooltip("weapons.callandor.lore"), "Who wields me wields destiny.~"
//        		+ "Take me, and begin the final journey.~"
//        		+ "    -from The Dragon Reborn");
//
//        add(LangUtil.tooltip("weapons.mjolnir.lore"), "'Whosoever holds this hammer,~"
//        		+ "if they be worthy, shall possess the power of Thor.'");
//
//        add(LangUtil.tooltip("weapons.headsmans_axe.lore"), "Let the good times roll.");
//
//        add(LangUtil.tooltip("weapons.axe_of_durin.lore"), "'... And my axe!'");
//
//        add(LangUtil.tooltip("weapons.power_attack_chance"), "Power Attack Chance: %s%%");
//        add(LangUtil.tooltip("weapons.power_attack_damage"), "Power Attack Damage: +%s");
//
//        /*
//         * screens
//         */
//        // chests
//        add(LangUtil.screen("wood_chest.name"), "Wood Chest");
//        add(LangUtil.screen("crate_chest.name"), "Crate");
//        add(LangUtil.screen("moldy_crate_chest.name"), "Moldy Crate");
//        add(LangUtil.screen("ironbound_chest.name"), "Ironbound Chest");
//        add(LangUtil.screen("pirate_chest.name"), "Pirate Chest");
//        add(LangUtil.screen("safe.name"), "Safe");
//        add(LangUtil.screen("iron_strongbox.name"), "Iron Strongbox");
//        add(LangUtil.screen("gold_strongbox.name"), "Gold Strongbox");
//        add(LangUtil.screen("dread_pirate_chest.name"), "Dread Pirate Chest");
//        add(LangUtil.screen("compressor_chest.name"), "Compressor Chest");
//        add(LangUtil.screen("skull_chest.name"), "Skull Chest");
//        add(LangUtil.screen("gold_skull_chest.name"), "Gold Skull Chest");
//        add(LangUtil.screen("crystal_skull_chest.name"), "Crystal Skull Chest");
//        add(LangUtil.screen("cauldron_chest.name"), "Cauldron Chest");
//        add(LangUtil.screen("viking_chest.name"), "Viking Chest");
//        add(LangUtil.screen("spider_chest.name"), "Spider Chest");
//        add(LangUtil.screen("cardboard_box.name"), "Cardboard Box");
//        add(LangUtil.screen("milk_crate.name"), "Milk Crate");
//        add(LangUtil.screen("barrel_chest.name"), "Treasure Barrel");
//        add(LangUtil.screen("vanilla_chest.name"), "Treasure Chest");
//        add(LangUtil.screen("wither_chest.name"), "Wither Chest");
//
//        add(LangUtil.screen("treasure_map.uncommon"), "Uncommon Treasure Map");
//        add(LangUtil.screen("treasure_map.scarce"), "Scarce Treasure Map");
//        add(LangUtil.screen("treasure_map.rare"), "Rare Treasure Map");
//        add(LangUtil.screen("treasure_map.epic"), "Epic Treasure Map");
//        add(LangUtil.screen("treasure_map.legendary"), "Legendary Treasure Map");
//        add(LangUtil.screen("treasure_map.mythical"), "Mythical Treasure Map");
//
//        /*
//         *  chat
//         */
//        // keys
        add(LangUtil.chat("key.key_break"), "開錠しようとしたがカギが壊れてしまった！");
        add(LangUtil.chat("key.key_not_fit"), "カギが合わないようだ");
        add(LangUtil.chat("key.key_unable_unlock"), "開錠に失敗した！");
//
//        /*
//         * patchouli
//         */
//        add("book.treasure2.landing", "Treasure2 adds to the thrill of discovery in Minecraft. This mod adds various new and rare chests of treasure to be discovered. The chests are secured by an assortment of locks and can only be unlocked by finding the corresponding keys.");
    }
}
