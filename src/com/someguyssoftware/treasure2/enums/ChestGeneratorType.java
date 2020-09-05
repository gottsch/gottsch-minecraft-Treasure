/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * @author Mark Gottschling on Sep 4, 2020
 *
 */
public enum ChestGeneratorType {
    // TODO do we need Common, Uncommon etc or will they be going away?
	WITHER(new WitherChestGenerator()),
	SKULL(new SkullChestGenerator()),
	GOLD_SKULL(new GoldSkullChestGenerator()),
    CAULDRON(new CauldronChestGenerator());
    
    IChestGenerator chestGenerator;

    /**
     * 
     */
    ChestGeneratorType(IChestGenerator chestGenerator) {
        this.chestGenerator = chestGenerator;
    }

    /**
     * 
     */
    public IChestGenerator getChestGenerator() {
        return chestGenerator;
    }
}
