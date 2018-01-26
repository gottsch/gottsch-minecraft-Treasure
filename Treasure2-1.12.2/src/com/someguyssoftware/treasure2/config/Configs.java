/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class Configs {
	private static TreasureConfig config;
	private static Map<Rarity, IChestConfig> defaultChestConfigs = new HashMap<>();
	private static Map<Rarity, IChestConfig> chestConfigs = new HashMap<>();
}
