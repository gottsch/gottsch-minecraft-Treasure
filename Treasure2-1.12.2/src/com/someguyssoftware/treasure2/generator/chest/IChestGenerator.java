package com.someguyssoftware.treasure2.generator.chest;

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.world.World;

public interface IChestGenerator<RESULT extends IGeneratorResult<?>> {

	public GeneratorResult<GeneratorData> generate(World world, Random random, ICoords coords, Rarity chestRarity, IChestConfig config);
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param coords
	 * @param chestRarity
	 * @param config
	 * @return
	 */
//	boolean generate(World world, Random random, ICoords coords, Rarity chestRarity, IChestConfig config);

	// TODO IGeneratorResult generated(World world, Random random, ICoords coords, Rarity rarity, IChestConfig config);
	
	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	public LootTable selectLootTable(Random random, final Rarity chestRarity);

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	List<LootTable> buildLootTableList(Rarity rarity);

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	AbstractChestBlock selectChest(Random random, Rarity rarity);
}