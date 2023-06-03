/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.List;
import java.util.Random;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity.GenerationContext;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class WitherChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public WitherChestGenerator() {
		this(ChestGeneratorType.WITHER);
	}
	
	/**
	 * 
	 */
	public WitherChestGenerator(IChestGeneratorType type) {
		super(type);
	}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(ITreasureChestBlockEntity blockEntity, IRarity rarity) {
		GenerationContext generationContext = 
				((AbstractTreasureChestBlockEntity)blockEntity).new GenerationContext(rarity, ChestGeneratorType.WITHER);
		blockEntity.setGenerationContext(generationContext);
	}
	
	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
//	@Override
//	public Optional<LootTableShell> selectLootTable(Random random, final IRarity chestRarity) {
//		return TreasureLootTableRegistry.getSpecialLootTable(SpecialLootTables.WITHER_CHEST);
//	}
//
//	@Override
//	public Optional<LootTableShell> selectLootTable(Supplier<Random> factory, final IRarity rarity) {
//		return TreasureLootTableRegistry.getSpecialLootTable(SpecialLootTables.WITHER_CHEST);
//	}
	
	/**
	 * Always select a skull chest.
	 */
	@Override
	public StandardChestBlock  selectChest(final Random random, final IRarity rarity) {
		StandardChestBlock chest = (StandardChestBlock) TreasureBlocks.WITHER_CHEST.get();
		return chest;
	}
	
	/**
	 * will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, LockLayout type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
	
	/**
	 * Select Locks
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, AbstractTreasureChestBlock chest, ITreasureChestBlockEntity blockEntity, IRarity rarity) {
		// get the chest type
		LockLayout type = chest.getLockLayout();
		// get the lock states
		List<LockState> lockStates = blockEntity.getLockStates();	
		// determine the number of locks to add (must have at least 1 lock)
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());
		for (int i = 0; i < numLocks; i++) {
			LockItem lock = TreasureItems.WITHER_LOCK.get();
			// add the lock to the chest
			lockStates.get(i).setLock(lock);
		}
	}
	
	/**
	 * Don't place any markers
	 */
	@Override
	public void addMarkers(IWorldGenContext context, ICoords coords, boolean isSurfaceChest) {
		return;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	@Override
	@Deprecated
	public BlockEntity placeInWorld(IWorldGenContext context, AbstractTreasureChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		GeneratorUtil.replaceBlockWithChest(context, chest, chestCoords, false);
		// ensure that chest is of type WITHER_CHEST
		if (context.level().getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST.get()) {
			// add top placeholder
			context.level().setBlock(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.get().defaultBlockState(), 3);
		}
		// get the backing tile entity of the chest 
		BlockEntity te = (BlockEntity) context.level().getBlockEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestBlockEntity)) {
			// remove chest
			context.level().setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
	
	@Override
	public BlockEntity placeInWorld(IWorldGenContext context, ICoords chestCoords, AbstractTreasureChestBlock chest, BlockState state, boolean discovered) {
		// replace block @ coords
		GeneratorUtil.replaceBlockWithChest(context, chestCoords, chest, state, discovered);
		
		// ensure that chest is of type WITHER_CHEST
		if (context.level().getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST.get()) {
			// add top placeholder
			context.level().setBlock(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.get().defaultBlockState(), 3); // TODO this needs to rotate to state as well.
		}
		// get the backing tile entity of the chest 
		BlockEntity te = (BlockEntity) context.level().getBlockEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestBlockEntity)) {
			// remove chest
			context.level().setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}
