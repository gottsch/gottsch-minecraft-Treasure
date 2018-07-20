package com.someguyssoftware.treasure2.generator.chest;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

public interface IChestGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chestRarity
	 * @param config
	 * @return
	 */
	boolean generate(World world, Random random, ICoords coords, Rarity chestRarity, IChestConfig config);

	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	public LootTable selectLootTable(Random random, final Rarity chestRarity);
}