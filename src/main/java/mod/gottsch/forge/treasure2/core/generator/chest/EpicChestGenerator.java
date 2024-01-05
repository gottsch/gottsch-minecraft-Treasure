/**
 * 
 */
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.chest.TreasureChestType;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class EpicChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public EpicChestGenerator() {}
	
	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.EPIC);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/**
	 * Epic will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
}