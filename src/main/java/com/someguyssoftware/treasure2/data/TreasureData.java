/**
 * 
 */
package com.someguyssoftware.treasure2.data;

import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.ChestEnvironment;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class TreasureData {
	// chest map by rarity and mapping flag - ** possible replacement for CHESTS_BY_RARITY **
	public static final Table<Rarity, ChestEnvironment, Block> CHESTS_BY_RARITY_FLAGS = HashBasedTable.create();
	
	// chest map by rarity
	public static final Multimap<Rarity, Block> CHESTS_BY_RARITY= ArrayListMultimap.create();
	
	// chest map by name
	public static final HashMap<String, Block> CHESTS_BY_NAME = new HashMap<>();
	
	public void initializeData() {
		// TODO finish later. but use meta data to populate the table map
		CHESTS_BY_RARITY_FLAGS.put(Rarity.COMMON, ChestEnvironment.SURFACE, TreasureBlocks.WOOD_CHEST);
	}
}
