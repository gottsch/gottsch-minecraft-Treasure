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
public class LanguageGen extends LanguageProvider {

    public LanguageGen(PackOutput output, String locale) {
        super(output, Treasure.MODID, locale);
    }
    
    @Override
    protected void addTranslations() {
    	// tabs
        add("itemGroup." + Treasure.MODID, "Treasure2");

        // keys
        add(TreasureItems.WOOD_KEY.get(), "Wood Key");
        add(TreasureItems.STONE_KEY.get(), "Stone Key");
        add(TreasureItems.LEAF_KEY.get(), "Leaf Key");
        add(TreasureItems.EMBER_KEY.get(), "Ember Key");
        add(TreasureItems.LIGHTNING_KEY.get(), "Lightning Key");
        
        add(TreasureItems.IRON_KEY.get(), "Iron Key");
        add(TreasureItems.GOLD_KEY.get(), "Gold Key");
        add(TreasureItems.METALLURGISTS_KEY.get(), "Metallurgists Key");
        
        add(TreasureItems.DIAMOND_KEY.get(), "Diamond Key");
        add(TreasureItems.EMERALD_KEY.get(), "Emerald Key");
        add(TreasureItems.TOPAZ_KEY.get(), "Topaz Key");
        add(TreasureItems.ONYX_KEY.get(), "Onyx Key");
        add(TreasureItems.RUBY_KEY.get(), "Ruby Key");
        add(TreasureItems.SAPPHIRE_KEY.get(), "Sapphire Key");
        add(TreasureItems.JEWELLED_KEY.get(), "Jewelled Key");
        
        add(TreasureItems.SPIDER_KEY.get(), "Spider Key");
        add(TreasureItems.WITHER_KEY.get(), "Wither Key");
        
        add(TreasureItems.SKELETON_KEY.get(), "Skeleton Key");        
        add(TreasureItems.PILFERERS_LOCK_PICK.get(), "Pilferer's Lock Pick");
        add(TreasureItems.THIEFS_LOCK_PICK.get(), "Thief's Lock Pick");
        add(TreasureItems.ONE_KEY.get(), "The One Key");
        // locks
        add(TreasureItems.WOOD_LOCK.get(), "Wood Lock");
        add(TreasureItems.STONE_LOCK.get(), "Stone Lock");
        add(TreasureItems.LEAF_LOCK.get(), "Leaf Lock");
        add(TreasureItems.EMBER_LOCK.get(), "Ember Lock");
        add(TreasureItems.IRON_LOCK.get(), "Iron Lock");
        add(TreasureItems.GOLD_LOCK.get(), "Gold Lock");
        
        add(TreasureItems.DIAMOND_LOCK.get(), "Diamond Lock");
        add(TreasureItems.EMERALD_LOCK.get(), "Emerald Lock");
        add(TreasureItems.TOPAZ_LOCK.get(), "Topaz Lock");
        add(TreasureItems.ONYX_LOCK.get(), "Onyx Lock");
        add(TreasureItems.RUBY_LOCK.get(), "Ruby Lock");
        add(TreasureItems.SAPPHIRE_LOCK.get(), "Sapphire Lock");
        
        add(TreasureItems.SPIDER_LOCK.get(), "Spider Lock");
        add(TreasureItems.WITHER_LOCK.get(), "Wither Lock");
        
        // key ring
        add(TreasureItems.KEY_RING.get(), "Key Ring");     
        
        // coins
        add(TreasureItems.COPPER_COIN.get(), "Copper Coin");
        add(TreasureItems.SILVER_COIN.get(), "Silver Coin");
        add(TreasureItems.GOLD_COIN.get(), "Gold Coin");
        
        add(TreasureItems.TOPAZ.get(), "Topaz");
        add(TreasureItems.ONYX.get(), "Onyx");
        add(TreasureItems.RUBY.get(), "Ruby");
        add(TreasureItems.SAPPHIRE.get(), "Sapphire");
        add(TreasureItems.WHITE_PEARL.get(), "White Pearl");
        add(TreasureItems.BLACK_PEARL.get(), "Black Pearl");
        
        // other
        add(TreasureItems.TREASURE_TOOL.get(), "Treasure Tool");
        add(TreasureItems.POUCH.get(), "Pouch");
        add(TreasureBlocks.SPANISH_MOSS.get(), "Spanish Moss");
        add(TreasureBlocks.WISHING_WELL.get(), "Wishing Well Stone");
        add(TreasureBlocks.DESERT_WISHING_WELL.get(), "Desert Wishing Well Stone");
        add(TreasureItems.EYE_PATCH.get(), "Eye Patch");
        
        // swords
        add(TreasureItems.COPPER_SHORT_SWORD.get(), "Copper Short Sword");
        add(TreasureItems.CHIPPED_COPPER_SHORT_SWORD.get(), "Chipped Copper Short Sword");
        add(TreasureItems.IRON_SHORT_SWORD.get(), "Iron Short Sword");
        add(TreasureItems.CHIPPED_IRON_SHORT_SWORD.get(), "Chipped Iron Short Sword");
        add(TreasureItems.STEEL_SHORT_SWORD.get(), "Steel Short Sword");
        add(TreasureItems.CHIPPED_STEEL_SHORT_SWORD.get(), "Chipped Steel Short Sword");
        add(TreasureItems.STEEL_SWORD.get(), "Steel Sword");
        add(TreasureItems.IRON_BROADSWORD.get(), "Iron Broadsword");
        add(TreasureItems.STEEL_BROADSWORD.get(), "Steel Broadsword");
        add(TreasureItems.COPPER_RAPIER.get(), "Copper Rapier");
        
        // specials
        add(TreasureItems.SKULL_SWORD.get(), "Skull Sword");
        add(TreasureItems.BLACK_SWORD.get(), "The Black Sword");
        add(TreasureItems.SWORD_OMENS.get(), "The Sword of Omens");
        add(TreasureItems.SWORD_POWER.get(), "The Sword of Power");
        add(TreasureItems.CALLANDOR.get(), "Callandor");
        add(TreasureItems.ORCUS.get(), "Orcus");
        add(TreasureItems.SNAKE_EYES_KATANA.get(), "Snake Eye's Katana");
        add(TreasureItems.STORM_SHADOWS_KATANA.get(), "Storm Shadow's Daisho");
        add(TreasureItems.OATHBRINGER.get(), "Oathbringer");
        add(TreasureItems.MJOLNIR.get(), "Mjolnir");
        add(TreasureItems.AXE_DURIN.get(), "Axe of Durin");
        add(TreasureItems.HEADSMANS_AXE.get(), "The Headsman's Axe");
        
        add(TreasureItems.STEEL_MACHETE.get(), "Steel Machete");
        add(TreasureItems.SHADOW_MACHETE.get(), "Shadow Machete");
        
        add(TreasureItems.IRON_FALCHION.get(), "Iron Falchion");
        add(TreasureItems.STEEL_FALCHION.get(), "Steel Falchion");
        add(TreasureItems.SHADOW_FALCHION.get(), "Shadow Falchion");
        
        // maces/hammers
        add(TreasureItems.IRON_MACE.get(), "Iron Mace");
        add(TreasureItems.STEEL_MACE.get(), "Steel Mace");
        
        // axes
        add(TreasureItems.COPPER_BROAD_AXE.get(), "Copper Broad Axe");
        add(TreasureItems.IRON_BROAD_AXE.get(), "Iron Broad Axe");
        add(TreasureItems.STEEL_BROAD_AXE.get(), "Steel Broad Axe");
        add(TreasureItems.IRON_DWARVEN_AXE.get(), "Iron Dwarven Axe");
        
        // chests
        add(TreasureBlocks.WOOD_CHEST.get(), "Wood Chest");
        add(TreasureBlocks.CRATE_CHEST.get(), "Crate");
        add(TreasureBlocks.MOLDY_CRATE_CHEST.get(), "Moldy Crate");
        add(TreasureBlocks.IRONBOUND_CHEST.get(), "Ironbound Chest");
        add(TreasureBlocks.SAFE.get(), "Safe Chest");
        add(TreasureBlocks.PIRATE_CHEST.get(), "Pirate Chest");
        add(TreasureBlocks.IRON_STRONGBOX.get(), "Iron Strongbox");
        add(TreasureBlocks.GOLD_STRONGBOX.get(), "Gold Strongbox");
        add(TreasureBlocks.DREAD_PIRATE_CHEST.get(), "Dread Pirate Chest");
        add(TreasureBlocks.COMPRESSOR_CHEST.get(), "Compressor Chest");
        add(TreasureBlocks.SKULL_CHEST.get(), "Skull Chest");
        add(TreasureBlocks.GOLD_SKULL_CHEST.get(), "Gold Skull Chest");
        add(TreasureBlocks.CRYSTAL_SKULL_CHEST.get(), "Crystal Chest");
        add(TreasureBlocks.CAULDRON_CHEST.get(), "Cauldron Chest");
        add(TreasureBlocks.SPIDER_CHEST.get(), "Spider Chest");
        add(TreasureBlocks.VIKING_CHEST.get(), "Viking Chest");
        add(TreasureBlocks.CARDBOARD_BOX.get(), "Cardboard Box");
        add(TreasureBlocks.MILK_CRATE.get(), "Milk Crate");
        add(TreasureBlocks.WITHER_CHEST.get(), "Wither Chest");
        add(TreasureBlocks.WITHER_CHEST_TOP.get(), "Wither Chest");
        add(TreasureBlocks.WITHER_BRANCH.get(), "Wither Branch");
        add(TreasureBlocks.WITHER_ROOT.get(), "Wither Root");
        add(TreasureBlocks.WITHER_LOG.get(), "Wither Log");
        add(TreasureBlocks.WITHER_BROKEN_LOG.get(), "Wither Broken Log");
        add(TreasureBlocks.WITHER_SOUL_LOG.get(), "Wither Soul Log");
        add(TreasureBlocks.WITHER_PLANKS.get(), "Wither Planks");
        
        // ore
        add(TreasureBlocks.TOPAZ_ORE.get(), "Topaz Ore");
        add(TreasureBlocks.ONYX_ORE.get(), "Onyx Ore");
        add(TreasureBlocks.RUBY_ORE.get(), "Ruby Ore");
        add(TreasureBlocks.SAPPHIRE_ORE.get(), "Sapphire Ore");
        
        add(TreasureBlocks.DEEPSLATE_TOPAZ_ORE.get(), "Deepslate Topaz Ore");
        add(TreasureBlocks.DEEPSLATE_ONYX_ORE.get(), "Deepslate Onyx Ore");
        add(TreasureBlocks.DEEPSLATE_RUBY_ORE.get(), "Deepslate Ruby Ore");
        add(TreasureBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), "Deepslate Sapphire Ore");
        
        // gravestones
        add(TreasureBlocks.GRAVESTONE1_STONE.get(), "Stone Gravestone");
        add(TreasureBlocks.GRAVESTONE1_COBBLESTONE.get(), "Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE1_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE1_POLISHED_GRANITE.get(), "Granite Gravestone");
        add(TreasureBlocks.GRAVESTONE1_OBSIDIAN.get(), "Obsidian Gravestone");
        add(TreasureBlocks.GRAVESTONE1_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
        add(TreasureBlocks.GRAVESTONE2_STONE.get(), "Stone Gravestone");
        add(TreasureBlocks.GRAVESTONE2_COBBLESTONE.get(), "Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE2_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE2_POLISHED_GRANITE.get(), "Granite Gravestone");
        add(TreasureBlocks.GRAVESTONE2_OBSIDIAN.get(), "Obsidian Gravestone");
        add(TreasureBlocks.GRAVESTONE2_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
        add(TreasureBlocks.GRAVESTONE3_STONE.get(), "Stone Gravestone");
        add(TreasureBlocks.GRAVESTONE3_COBBLESTONE.get(), "Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE3_MOSSY_COBBLESTONE.get(), "Mossy Cobblestone Gravestone");
        add(TreasureBlocks.GRAVESTONE3_POLISHED_GRANITE.get(), "Granite Gravestone");
        add(TreasureBlocks.GRAVESTONE3_OBSIDIAN.get(), "Obsidian Gravestone");
        add(TreasureBlocks.GRAVESTONE3_SMOOTH_QUARTZ.get(), "Quartz Gravestone");
        add(TreasureBlocks.SKULL_CROSSBONES.get(), "Skull and Crossbones");
        add(TreasureBlocks.SKELETON.get(), "Skeleton");
        
        add(TreasureBlocks.GRAVESTONE1_SPAWNER_STONE.get(), "Stone Gravestone Spawner");
        add(TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE.get(), "Cobblestone Gravestone Spawner");
        add(TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN.get(), "Obsidian Gravestone Spawner");
        
        // mobs
        add(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), "Bound Soul");
        add(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), "Wood Chest Mimic");
        add(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "Pirate Chest Mimic");
        add(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), "Viking Chest Mimic");
        add(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), "Cauldron Chest Mimic");
        add(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "Crate Chest Mimic");
        add(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), "Moldy Chest Mimic");
        
        // eggs
        add(TreasureItems.BOUND_SOUL_EGG.get(), "Bound Soul Spawn Egg");
        add(TreasureItems.WOOD_CHEST_MIMIC_EGG.get(), "Wood Chest Mimic Spawn Egg");
        add(TreasureItems.PIRATE_CHEST_MIMIC_EGG.get(), "Pirate Chest Mimic Spawn Egg");
        add(TreasureItems.VIKING_CHEST_MIMIC_EGG.get(), "Viking Chest Mimic Spawn Egg");
        add(TreasureItems.CAULDRON_CHEST_MIMIC_EGG.get(), "Cauldron Chest Mimic Spawn Egg");
        add(TreasureItems.CRATE_CHEST_MIMIC_EGG.get(), "Crate Chest Mimic Spawn Egg");
        add(TreasureItems.MOLDY_CRATE_CHEST_MIMIC_EGG.get(), "Moldy Crate Chest Mimic Spawn Egg");
        
        /*
         *  Util.tooltips
         */
        // general
        add(LangUtil.tooltip("boolean.yes"), "Yes");
        add(LangUtil.tooltip("boolean.no"), "No");
        add(LangUtil.tooltip("hold_shift"), "Hold [SHIFT] to expand");
        add(LangUtil.tooltip("treasure_tool"), "Required for most Treasure2 recipes");
        add(LangUtil.tooltip("pouch"), "Holds small valuables - coins, gems, charms, etc.");
        add(LangUtil.tooltip("wishable"), "Can be thrown into Wishing Wells for loot");
        
        // keys and locks
        add(LangUtil.tooltip("key_lock.rarity"), "Rarity: %s");
        add(LangUtil.tooltip("key_lock.category"), "Category: %s");
        add(LangUtil.tooltip("key_lock.craftable"), "Craftable: %s");
        add(LangUtil.tooltip("key_lock.breakable"), "Breakable: %s");
        add(LangUtil.tooltip("key_lock.damageable"), "Damageable: %s");
        add(LangUtil.tooltip("key_lock.accepts_keys"), "Accepts Keys:");
        add(LangUtil.tooltip("key_lock.specials"), "Specials: %s");
        add(LangUtil.tooltip("key_lock.skeleton_key.specials"), "Opens COMMON, UNCOMMON, SCARCE~and RARE locks (excluding Wither)");
        add(LangUtil.tooltip("key_lock.ember_key.specials"), "Destroys Wood and Leaf Locks");
        add(LangUtil.tooltip("key_lock.ember_lock.specials"), "Destroys All Keys regardless of breakability (excluding Ember and Lightning)");
        add(LangUtil.tooltip("key_lock.lightning_key.specials"), "Opens any lock in ELEMENTAL category");
        add(LangUtil.tooltip("key_lock.metallurgists_key.specials"), "Opens any lock in METALS category");
        add(LangUtil.tooltip("key_lock.jewelled_key.specials"), "Opens any lock in GEMS category");
        add(LangUtil.tooltip("key_lock.pilferers_lock_pick.specials"), "Opens COMMON (%s%%) and UNCOMMON (%s%%) locks");
        add(LangUtil.tooltip("key_lock.thiefs_lock_pick.specials"), "Opens COMMON (%s%%), UNCOMMON (%s%%) and SCARCE (%s%%) locks");
        add(LangUtil.tooltip("key_lock.one_key.specials"), "Opens ALL locks");
        add(LangUtil.tooltip("key_lock.one_key.lore"), "One Key to rule them all.");
        add(LangUtil.tooltip("key_lock.key_ring"), "Container for keys");
        
        // chests
        add(LangUtil.tooltip("chest.rarity"), "Rarity: %s");
        add(LangUtil.tooltip("chest.max_locks"), "Max Locks: %s");
        add(LangUtil.tooltip("chest.container_size"), "Inventory Size: %s");
        
        // capabilities
        add(LangUtil.tooltip("cap.durability.amount"), "Durability: [%s/%s]");
        add(LangUtil.tooltip("cap.durability.amount.infinite"), "Durability: Infinite");
        add(LangUtil.tooltip("cap.durability.repairs"), "R[%s/%s]");
        add(LangUtil.tooltip("cap.spell.recharges"), "R[%s/%s]");
        
        // weapons
        add(LangUtil.tooltip("weapons.black_sword.lore"), "Sword of the Avatar. Host of the daemon Arcadion.");
        add(LangUtil.tooltip("weapons.sword_of_omens.lore"), "'Thunder... Thunder... Thunder... ThunderCats HO!'");
        add(LangUtil.tooltip("weapons.sword_of_power.lore"), "'By the power of Grayskull.... I have the power!'");
        
        add(LangUtil.tooltip("weapons.orcus.lore"), "'Fair lady, throw those costly robes aside,~"
        		+ "No longer may you glory in your pride.~"
        		+ "Take leave of all sour vain delight~"
        		+ "I'm come to summon you away this night'.~"
        		+ "    -The Grim Reaper");
        add(LangUtil.tooltip("weapons.snake_eyes_katana.lore"), "Move with the wind, and you will never be heard.~" 
        		+ "    -Snake Eyes");
        add(LangUtil.tooltip("weapons.storm_shadows_katana.lore"), "Most people will tell you that ninjas dont exist.~"
        		+ "Thats what the ninjas want you to believe ...~"
        		+ "    -Storm Shadow");
        add(LangUtil.tooltip("weapons.oathbringer.lore"), "Life before death,~"
        		+ "strength before weakness,~"
        		+ "journey before destination.");
        
        add(LangUtil.tooltip("weapons.callandor.lore"), "Who wields me wields destiny.~"
        		+ "Take me, and begin the final journey.~"
        		+ "    -from The Dragon Reborn");
        
        add(LangUtil.tooltip("weapons.mjolnir.lore"), "'Whosoever holds this hammer,~"
        		+ "if they be worthy, shall possess the power of Thor.'");
        
        add(LangUtil.tooltip("weapons.headsmans_axe.lore"), "Let the good times roll.");
        
        add(LangUtil.tooltip("weapons.axe_of_durin.lore"), "'... And my axe!'");
        
        add(LangUtil.tooltip("weapons.power_attack_chance"), "Power Attack Chance: %s%%");
        add(LangUtil.tooltip("weapons.power_attack_damage"), "Power Attack Damage: +%s");
        
        /*
         * screens
         */
        // chests
        add(LangUtil.screen("wood_chest.name"), "Wood Chest");
        add(LangUtil.screen("crate_chest.name"), "Crate");
        add(LangUtil.screen("moldy_crate_chest.name"), "Moldy Crate");
        add(LangUtil.screen("ironbound_chest.name"), "Ironbound Chest");
        add(LangUtil.screen("pirate_chest.name"), "Pirate Chest");
        add(LangUtil.screen("safe.name"), "Safe");
        add(LangUtil.screen("iron_strongbox.name"), "Iron Strongbox");
        add(LangUtil.screen("gold_strongbox.name"), "Gold Strongbox");
        add(LangUtil.screen("dread_pirate_chest.name"), "Dread Pirate Chest");
        add(LangUtil.screen("compressor_chest.name"), "Compressor Chest");
        add(LangUtil.screen("skull_chest.name"), "Skull Chest");
        add(LangUtil.screen("gold_skull_chest.name"), "Gold Skull Chest");
        add(LangUtil.screen("crystal_skull_chest.name"), "Crystal Skull Chest");
        add(LangUtil.screen("cauldron_chest.name"), "Cauldron Chest");
        add(LangUtil.screen("viking_chest.name"), "Viking Chest");
        add(LangUtil.screen("spider_chest.name"), "Spider Chest");
        add(LangUtil.screen("cardboard_box.name"), "Cardboard Box");
        add(LangUtil.screen("milk_crate.name"), "Milk Crate");
        add(LangUtil.screen("wither_chest.name"), "Wither Chest");

        add(LangUtil.screen("treasure_map.uncommon"), "Uncommon Treasure Map");
        add(LangUtil.screen("treasure_map.scarce"), "Scarce Treasure Map");
        add(LangUtil.screen("treasure_map.rare"), "Rare Treasure Map");
        add(LangUtil.screen("treasure_map.epic"), "Epic Treasure Map");
        add(LangUtil.screen("treasure_map.legendary"), "Legendary Treasure Map");
        add(LangUtil.screen("treasure_map.mythical"), "Mythical Treasure Map");
        
        /*
         *  chat
         */
        // keys
        add(LangUtil.chat("key.key_break"), "Your key broke whilst attempting to unlock the lock!");
        add(LangUtil.chat("key.key_not_fit"), "Your key doesn't fit the lock!");
        add(LangUtil.chat("key.key_unable_unlock"), "Your key failed to unlock the lock!");


    }
}
