/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestType;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.GenUtil;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableMaster2.SpecialLootTables;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class WitherChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public WitherChestGenerator() {}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.WITHER);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public Optional<LootTableShell> selectLootTable2(Random random, final Rarity chestRarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WITHER_CHEST));
	}

	@Override
	public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, final Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WITHER_CHEST));
	}
	/**
	 * Always select a wither chest.
	 */
	@Override
	public StandardChestBlock  selectChest(final Random random, final Rarity rarity) {
		StandardChestBlock chest = (StandardChestBlock) TreasureBlocks.WITHER_CHEST;
		return chest;
	}
	
	/**
	 * Wither will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
	
	/**
	 * 
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// get the chest type
		TreasureChestType type = chest.getChestType();
		// get the lock states
		List<LockState> lockStates = te.getLockStates();	
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
	public void addMarkers(IServerWorld world, ChunkGenerator generator, Random random, ICoords coords, boolean isSurfaceChest) {
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
	public TileEntity placeInWorld(IServerWorld world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);
		// ensure that chest is of type WITHER_CHEST
		if (world.getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST) {
			// add top placeholder
			world.setBlock(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.defaultBlockState(), 3);
		}
		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getBlockEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
	
	@Override
	public TileEntity placeInWorld(IServerWorld world, Random random, ICoords chestCoords, AbstractChestBlock chest, BlockState state) {
		// replace block @ coords
		GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		
		// ensure that chest is of type WITHER_CHEST
		if (world.getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST) {
			// add top placeholder
			world.setBlock(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.defaultBlockState(), 3); // TODO this needs to rotate to state as well.
		}
		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getBlockEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}