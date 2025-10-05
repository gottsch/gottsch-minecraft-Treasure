/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
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
        add(TreasureItems.WOOD_KEY.get(), "木のカギ");
        add(TreasureItems.STONE_KEY.get(), "石のカギ");
        add(TreasureItems.LEAF_KEY.get(), "樹葉のカギ");
        add(TreasureItems.EMBER_KEY.get(), "残り火のカギ");
        add(TreasureItems.LIGHTNING_KEY.get(), "雷光のカギ");

        add(TreasureItems.IRON_KEY.get(), "鉄のカギ");
        add(TreasureItems.GOLD_KEY.get(), "金のカギ");
        add(TreasureItems.METALLURGISTS_KEY.get(), "金属博士のカギ");

        add(TreasureItems.DIAMOND_KEY.get(), "ダイヤモンドのカギ");
        add(TreasureItems.EMERALD_KEY.get(), "エメラルドのカギ");
        add(TreasureItems.TOPAZ_KEY.get(), "トパーズのカギ");
        add(TreasureItems.ONYX_KEY.get(), "オニキスのカギ");
        add(TreasureItems.RUBY_KEY.get(), "ルビーのカギ");
        add(TreasureItems.SAPPHIRE_KEY.get(), "サファイヤのカギ");
        add(TreasureItems.JEWELLED_KEY.get(), "宝石飾りのカギ");

        add(TreasureItems.SPIDER_KEY.get(), "クモのカギ");
        add(TreasureItems.WITHER_KEY.get(), "ウィザーのカギ");

        add(TreasureItems.SKELETON_KEY.get(), "骨のカギ");
        add(TreasureItems.PILFERERS_LOCK_PICK.get(), "コソ泥のピッキング");
        add(TreasureItems.THIEFS_LOCK_PICK.get(), "盗賊のピッキング");
        add(TreasureItems.ONE_KEY.get(), "マスターキー");

        // locks
        add(TreasureItems.WOOD_LOCK.get(), "木の錠前");
        add(TreasureItems.STONE_LOCK.get(), "石の錠前");
        add(TreasureItems.LEAF_LOCK.get(), "樹葉の錠前");
        add(TreasureItems.EMBER_LOCK.get(), "残り火の錠前");
        add(TreasureItems.IRON_LOCK.get(), "鉄の錠前");
        add(TreasureItems.GOLD_LOCK.get(), "金の錠前");

        add(TreasureItems.DIAMOND_LOCK.get(), "ダイヤモンドの錠前");
        add(TreasureItems.EMERALD_LOCK.get(), "エメラルドの錠前");
        add(TreasureItems.TOPAZ_LOCK.get(), "トパーズの錠前");
        add(TreasureItems.ONYX_LOCK.get(), "オニキスの上飴");
        add(TreasureItems.RUBY_LOCK.get(), "ルビーの錠前");
        add(TreasureItems.SAPPHIRE_LOCK.get(), "サファイヤの錠前");

        add(TreasureItems.SPIDER_LOCK.get(), "クモの錠前");
        add(TreasureItems.WITHER_LOCK.get(), "ウィザーの錠前");

        // key ring
        add(TreasureItems.KEY_RING.get(), "キーホルダー");

        // coins
        add(TreasureItems.COPPER_COIN.get(), "銅貨");
        add(TreasureItems.SILVER_COIN.get(), "銀貨");
        add(TreasureItems.GOLD_COIN.get(), "金貨");

        add(TreasureItems.TOPAZ.get(), "トパーズ");
        add(TreasureItems.ONYX.get(), "オニキス");
        add(TreasureItems.RUBY.get(), "ルビー");
        add(TreasureItems.SAPPHIRE.get(), "サファイヤ");
        add(TreasureItems.WHITE_PEARL.get(), "白真珠");
        add(TreasureItems.BLACK_PEARL.get(), "黒真珠");

        // other
        add(TreasureItems.TREASURE_TOOL.get(), "宝探しツール");
        add(TreasureItems.POUCH.get(), "小銭入れ");
        add(TreasureBlocks.SPANISH_MOSS.get(), "しだれコケ");
        add(TreasureBlocks.WISHING_WELL.get(), "願い井戸の石");
        add(TreasureBlocks.WISHING_WELL_COBBLESTONE.get(), "願い井戸の丸石");
        add(TreasureBlocks.WISHING_WELL_STONE_BRICKS.get(), "願い井戸の石");
        add(TreasureBlocks.WISHING_WELL_MOSSY_STONE_BRICKS.get(), "願い井戸の苔むした石");
        add(TreasureBlocks.DESERT_WISHING_WELL.get(), "砂漠の願い井戸の石");
        add(TreasureItems.EYE_PATCH.get(), "眼帯");
        add(TreasureItems.CLOVER.get(), "四葉のクローバー");

        // swords
        add(TreasureItems.COPPER_SHORT_SWORD.get(), "銅の短剣");
        add(TreasureItems.CHIPPED_COPPER_SHORT_SWORD.get(), "欠けた銅の短剣");
        add(TreasureItems.IRON_SHORT_SWORD.get(), "鉄の短剣");
        add(TreasureItems.CHIPPED_IRON_SHORT_SWORD.get(), "欠けた鉄の短剣");
        add(TreasureItems.STEEL_SHORT_SWORD.get(), "鋼の短剣");
        add(TreasureItems.CHIPPED_STEEL_SHORT_SWORD.get(), "欠けた鋼の短剣");
        add(TreasureItems.STEEL_SWORD.get(), "鋼の剣");
        add(TreasureItems.IRON_BROADSWORD.get(), "鉄の大剣");
        add(TreasureItems.STEEL_BROADSWORD.get(), "鋼の大剣");
        add(TreasureItems.COPPER_RAPIER.get(), "銅のレイピア");
//
//        // specials
        add(TreasureItems.SKULL_SWORD.get(), "ドクロの剣");
        add(TreasureItems.BLACK_SWORD.get(), "黒剣");
        add(TreasureItems.SWORD_OMENS.get(), "地の神の剣");
        add(TreasureItems.SWORD_POWER.get(), "閃光の剣");
        add(TreasureItems.CALLANDOR.get(), "霊剣カランドル");
        add(TreasureItems.ORCUS.get(), "オルクスの大鎌");
        add(TreasureItems.SNAKE_EYES_KATANA.get(), "妖刀蛇ノ目");
        add(TreasureItems.STORM_SHADOWS_KATANA.get(), "黒風丸");
        add(TreasureItems.OATHBRINGER.get(), "神剣オースブリンガー");
        add(TreasureItems.MJOLNIR.get(), "ミョッルニル");
        add(TreasureItems.AXE_DURIN.get(), "ドゥリンの戦斧");
        add(TreasureItems.HEADSMANS_AXE.get(), "首長の長斧");
//
        add(TreasureItems.STEEL_MACHETE.get(), "鋼のマチェット");
        add(TreasureItems.SHADOW_MACHETE.get(), "シャドーマチェット");
//
        add(TreasureItems.IRON_FALCHION.get(), "鉄のファルシオン");
        add(TreasureItems.STEEL_FALCHION.get(), "鋼のファルシオン");
        add(TreasureItems.SHADOW_FALCHION.get(), "シャドーファルシオン");
//
//        // maces/hammers
        add(TreasureItems.IRON_MACE.get(), "鉄のメイス");
        add(TreasureItems.STEEL_MACE.get(), "鋼のメイス");
//
//        // axes
        add(TreasureItems.COPPER_BROAD_AXE.get(), "銅の大斧");
        add(TreasureItems.IRON_BROAD_AXE.get(), "鉄の大斧");
        add(TreasureItems.STEEL_BROAD_AXE.get(),  "鋼の大斧");
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
        add(TreasureBlocks.WITHER_BRANCH.get(), "ウィザーの枝");
        add(TreasureBlocks.WITHER_ROOT.get(), "ウィザーの根");
        add(TreasureBlocks.WITHER_LOG.get(), "ウィザーの原木");
        add(TreasureBlocks.WITHER_BROKEN_LOG.get(), "壊れたウィザーの原木");
        add(TreasureBlocks.WITHER_PLANKS.get(), "ウィザーの幹");

        // current wither
        add(TreasureBlocks.WITHERWOOD_BRANCH.get(), "ウィザーウッドの枝");
        add(TreasureBlocks.WITHERWOOD_ROOT.get(), "ウィザーウッドの根");

        add(TreasureBlocks.WITHERWOOD_LOG.get(), "ウィザーウッドの原木");
        add(TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(), "壊れたウィザーウッドの原木");
        add(TreasureBlocks.WITHERWOOD_PLANKS.get(), "ウィザーウッドの幹");
        add(TreasureBlocks.WITHERWOOD_SLAB.get(), "ウィザーウッドのハーフブロック");
        add(TreasureBlocks.WITHERWOOD_STAIRS.get(), "ウィザーウッドの階段");
        add(TreasureBlocks.WITHERWOOD_WOOD.get(), "ウィザーウッドの板材");
        add(TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(), "樹皮を剥いだウィザーウッドの原木");
        add(TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(), "樹皮を剥いだウィザーウッド");
        add(TreasureBlocks.WITHERWOOD_FENCE.get(), "ウィザーウッドのフェンス");
        add(TreasureBlocks.WITHERWOOD_FENCE_GATE.get(), "ウィザーウッドのフェンスゲート");
        add(TreasureBlocks.WITHERWOOD_BUTTON.get(), "ウィザーウッドのボタン");
        add(TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get(), "ウィザーウッドの感圧版");
        add(TreasureBlocks.WITHERWOOD_DOOR.get(), "ウィザーウッドのドア");
        add(TreasureBlocks.WITHERWOOD_TRAPDOOR.get(), "ウィザーウッドのトラップドア");
        add(TreasureBlocks.WITHERWOOD_SIGN.get(), "ウィザーウッドの看板");
        add(TreasureBlocks.WITHERWOOD_HANGING_SIGN.get(), "ウィザーウッドの吊り看板");
        add(TreasureBlocks.WITHERWOOD_TWIG.get(), "ウィザーウッドの小枝");
//
        add(TreasureBlocks.STRANGLE_VINES.get(), "絞めつけるツタ");
        add(TreasureBlocks.STRANGLE_VINES_PLANT.get(), "絞めつけるツタ植物");

        // ore
        add(TreasureBlocks.TOPAZ_ORE.get(), "トパーズ鉱石");
        add(TreasureBlocks.ONYX_ORE.get(), "オニキス鉱石");
        add(TreasureBlocks.RUBY_ORE.get(), "ルビー鉱石");
        add(TreasureBlocks.SAPPHIRE_ORE.get(), "サファイヤ鉱石");

        add(TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get(), "深層トパーズ鉱石");
        add(TreasureBlocks.DEEPSLATE_ONYX_ORE.get(), "深層オニキス鉱石");
        add(TreasureBlocks.DEEPSLATE_RUBY_ORE.get(), "深層ルビー鉱石");
        add(TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), "深層サファイヤ鉱石");

        // gravestones
        add(TreasureBlocks.GRAVESTONE1_STONE.get(), "墓石");
        add(TreasureBlocks.GRAVESTONE1_COBBLESTONE.get(), "丸石の墓石");
        add(TreasureBlocks.GRAVESTONE1_MOSSY_COBBLESTONE.get(), "苔むした丸石の墓石");
        add(TreasureBlocks.GRAVESTONE1_POLISHED_GRANITE.get(), "花崗岩の墓石");
        add(TreasureBlocks.GRAVESTONE1_OBSIDIAN.get(), "黒曜石の墓石");
        add(TreasureBlocks.GRAVESTONE1_SMOOTH_QUARTZ.get(), "クォーツの墓石");
        add(TreasureBlocks.GRAVESTONE2_STONE.get(), "墓石");
        add(TreasureBlocks.GRAVESTONE2_COBBLESTONE.get(), "丸石の墓石");
        add(TreasureBlocks.GRAVESTONE2_MOSSY_COBBLESTONE.get(), "苔むした丸石の墓石");
        add(TreasureBlocks.GRAVESTONE2_POLISHED_GRANITE.get(), "花崗岩の墓石");
        add(TreasureBlocks.GRAVESTONE2_OBSIDIAN.get(), "黒曜石の墓石");
        add(TreasureBlocks.GRAVESTONE2_SMOOTH_QUARTZ.get(), "クォーツの墓石");
        add(TreasureBlocks.GRAVESTONE3_STONE.get(), "墓石");
        add(TreasureBlocks.GRAVESTONE3_COBBLESTONE.get(), "丸石の墓石");
        add(TreasureBlocks.GRAVESTONE3_MOSSY_COBBLESTONE.get(), "苔むした丸石の墓石");
        add(TreasureBlocks.GRAVESTONE3_POLISHED_GRANITE.get(), "花崗岩の墓石");
        add(TreasureBlocks.GRAVESTONE3_OBSIDIAN.get(), "黒曜石の墓石");
        add(TreasureBlocks.GRAVESTONE3_SMOOTH_QUARTZ.get(), "クォーツの墓石");
        add(TreasureBlocks.SKULL_CROSSBONES.get(), "ドクロと骨十字");
        add(TreasureBlocks.SKELETON.get(), "ガイコツ");
        add(TreasureBlocks.CLOVER.get(), "四葉のクローバー");

        add(TreasureBlocks.GRAVESTONE1_SPAWNER_STONE.get(), "墓石スポナー");
        add(TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE.get(), "丸石の墓石スポナー");
        add(TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN.get(), "黒曜石の墓石スポナー");

        add(TreasureBlocks.FALLING_GRASS.get(), "草");
        add(TreasureBlocks.FALLING_SAND.get(), "砂");
        add(TreasureBlocks.FALLING_RED_SAND.get(), "赤砂");

        // mobs
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

        /*
         *  Util.tooltips
         */
        // general
        add(LangUtil.tooltip("boolean.yes"), "Yes");
        add(LangUtil.tooltip("boolean.no"), "No");
        add(LangUtil.tooltip("hold_shift"), "[SHIFT]長押しで展開");
        add(LangUtil.tooltip("treasure_tool"), "Treasure2のレシピでよく必要になる");
        add(LangUtil.tooltip("pouch"), "コインや宝石、お守り等の小さい貴重品を入れておける");
        add(LangUtil.tooltip("wishable"), "願い井戸に投げ込むと何かが見つかるかもしれない");
        add(LangUtil.tooltip("clover"), "特定のブロックに対して使用できる。例えば、苔むした丸石に使うと願い井戸ブロックに変換できる。~このとき、半径４ブロックの隣接する範囲が全て変換される。");

        // keys and locks
        add(LangUtil.tooltip("key_lock.rarity"), "レア度: %s");
        add(LangUtil.tooltip("key_lock.category"), "タイプ: %s");
        add(LangUtil.tooltip("key_lock.craftable"), "作成可能: %s");
        add(LangUtil.tooltip("key_lock.breakable"), "破壊可能: %s");
        add(LangUtil.tooltip("key_lock.damageable"), "損傷可能: %s");
        add(LangUtil.tooltip("key_lock.accepts_keys"), "有効なカギ:");
        add(LangUtil.tooltip("key_lock.specials"), "特殊: %s");
        add(LangUtil.tooltip("key_lock.skeleton_key.specials"), "通常、上級、逸品およびレアの錠前を~開けることができる(ただしウィザーを除く)");
        add(LangUtil.tooltip("key_lock.ember_key.specials"), "木の錠前と樹葉の錠前を壊す");
        add(LangUtil.tooltip("key_lock.ember_lock.specials"), "破壊可否に関わらずすべての錠前を壊す(ただし残り火と雷光の錠前を除く)");
        add(LangUtil.tooltip("key_lock.lightning_key.specials"), "精霊タイプの全ての錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.metallurgists_key.specials"), "金属タイプの全ての錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.jewelled_key.specials"), "宝石タイプの全ての錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.pilferers_lock_pick.specials"), "通常(%s%%)と上級(%s%%)の錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.thiefs_lock_pick.specials"), "通常(%s%%)、上級(%s%%)および逸品(%s%%)の錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.one_key.specials"), "あらゆる錠前を開けることができる");
        add(LangUtil.tooltip("key_lock.one_key.lore"), "マスターキーは全てを支配する");
        add(LangUtil.tooltip("key_lock.key_ring"), "複数のカギを付けておける");

        // chests
        add(LangUtil.tooltip("chest.locked"), "施錠されている！");
        add(LangUtil.tooltip("chest.usage"), "願い井戸に投げ込むと全ての錠前を外すことができる・・代償はあるが");
        add(LangUtil.tooltip("chest.rarity"), "レア度: %s");
        add(LangUtil.tooltip("chest.max_locks"), "施錠上限: %s");
        add(LangUtil.tooltip("chest.container_size"), "収納数: %s");

        // capabilities
        add(LangUtil.tooltip("cap.durability.amount"), "耐久度: [%s/%s]");
        add(LangUtil.tooltip("cap.durability.amount.infinite"), "耐久度: 無限");
        add(LangUtil.tooltip("cap.durability.repairs"), "R[%s/%s]");
        add(LangUtil.tooltip("cap.spell.recharges"), "R[%s/%s]");

        // weapons
        add(LangUtil.tooltip("weapons.black_sword.lore"), "アバターの剣。~悪魔アルカディオンを招き入れし者。");
        add(LangUtil.tooltip("weapons.sword_of_omens.lore"), "'時を超え 今すぐに 旅立て'");
        add(LangUtil.tooltip("weapons.sword_of_power.lore"), "'人よ 生命よ 力を見る'");

        add(LangUtil.tooltip("weapons.orcus.lore"), "'美しき人よ、もはや豪奢な衣は無用。~誇りも喜びも捨て去るが一興。~今宵我と共に彼岸へ逝こう。~    - 死神の名の下に");
        add(LangUtil.tooltip("weapons.snake_eyes_katana.lore"), "風と共に出で来、また風と共に去りぬ~    - 蛇ノ目");
        add(LangUtil.tooltip("weapons.storm_shadows_katana.lore"), "ニンジャは実在しない。いいね？~    - 黒風");
        add(LangUtil.tooltip("weapons.oathbringer.lore"), "死の前に生が~弱さの前に強さが~目的地の前に旅がある");
        add(LangUtil.tooltip("weapons.callandor.lore"), "我を意のままに操る者こそが運命の支配者。~我を手に取り、最後の旅路に足を踏み出すのだ。~    -「竜王の再来」より");
        add(LangUtil.tooltip("weapons.mjolnir.lore"), "'汝にその資格があるならばこのハンマーを手に取れ。~雷神トールの力をその手にできるだろう'");
        add(LangUtil.tooltip("weapons.headsmans_axe.lore"), "良き時代を築こうじゃないか");
        add(LangUtil.tooltip("weapons.axe_of_durin.lore"), "'・・俺は斧を捧げよう'");
        add(LangUtil.tooltip("weapons.power_attack_chance"), "渾身の一撃発生確率: %s%%");
        add(LangUtil.tooltip("weapons.power_attack_damage"), "渾身の一撃ダメージ: +%s");

        /*
         * screens
         */
        // chests
        add(LangUtil.screen("wood_chest.name"), "木のチェスト");
        add(LangUtil.screen("crate_chest.name"), "木箱");
        add(LangUtil.screen("moldy_crate_chest.name"), "カビた木箱");
        add(LangUtil.screen("ironbound_chest.name"), "鉄張りのチェスト");
        add(LangUtil.screen("pirate_chest.name"), "海賊のチェスト");
        add(LangUtil.screen("safe.name"), "金庫");
        add(LangUtil.screen("iron_strongbox.name"), "鉄の保管庫");
        add(LangUtil.screen("gold_strongbox.name"), "金の保管庫");
        add(LangUtil.screen("dread_pirate_chest.name"), "大海賊のチェスト");
        add(LangUtil.screen("compressor_chest.name"), "圧縮チェスト");
        add(LangUtil.screen("skull_chest.name"), "ドクロのチェスト");
        add(LangUtil.screen("gold_skull_chest.name"), "金ドクロのチェスト");
        add(LangUtil.screen("crystal_skull_chest.name"), "水晶ドクロのチェスト");
        add(LangUtil.screen("cauldron_chest.name"), "大釜チェスト");
        add(LangUtil.screen("viking_chest.name"), "バイキングチェスト");
        add(LangUtil.screen("spider_chest.name"), "クモのチェスト");
        add(LangUtil.screen("cardboard_box.name"), "段ボール箱");
        add(LangUtil.screen("milk_crate.name"), "ミルク木箱");
        add(LangUtil.screen("barrel_chest.name"), "宝入りの樽");
        add(LangUtil.screen("vanilla_chest.name"), "宝箱");
        add(LangUtil.screen("wither_chest.name"), "ウィザーのチェスト");

        add(LangUtil.screen("treasure_map.uncommon"), "上級の宝の地図");
        add(LangUtil.screen("treasure_map.scarce"), "逸品の宝の地図");
        add(LangUtil.screen("treasure_map.rare"), "レアな宝の地図");
        add(LangUtil.screen("treasure_map.epic"), "至高の宝の地図");
        add(LangUtil.screen("treasure_map.legendary"), "伝説の宝の地図");
        add(LangUtil.screen("treasure_map.mythical"), "神々の宝の地図");

        /*
         *  chat
         */
        // keys
        add(LangUtil.chat("key.key_break"), "開錠しようとしたがカギが壊れてしまった！");
        add(LangUtil.chat("key.key_not_fit"), "カギが合わないようだ");
        add(LangUtil.chat("key.key_unable_unlock"), "開錠に失敗した！");

        /*
         * patchouli
         */
        add("book.treasure2.landing", "Treasure2はマインクラフトに新たな発見のスリルと楽しみを追加します。このMODは、様々な新しいレアな宝箱と財宝アイテムを追加します。宝箱は様々なロックで保護されており、解除するには対応するカギを見つける必要があります。");
    }
}
