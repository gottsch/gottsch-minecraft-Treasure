/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.function.Supplier;

import com.someguyssoftware.treasure2.generator.chest.CauldronChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.EpicChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.GoldSkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.RareChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.ScarceChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.SkullChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.UncommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.WitherChestGenerator;

/**
 * @author Mark Gottschling on Sep 4, 2020
 *
 */
public enum ChestGeneratorType {
	COMMON(CommonChestGenerator::new),
	UNCOMMON(UncommonChestGenerator::new),
	SCARCE(ScarceChestGenerator::new),
	RARE(RareChestGenerator::new),
	EPIC(EpicChestGenerator::new),
	WITHER(WitherChestGenerator::new),
	SKULL(SkullChestGenerator::new),
	GOLD_SKULL(GoldSkullChestGenerator::new),
    CAULDRON(CauldronChestGenerator::new);
    
    private Supplier<IChestGenerator> factory;

    /**
     * 
     */
    ChestGeneratorType(Supplier<IChestGenerator> factory) {
        this.factory = factory;
    }

    /**
     * 
     */
    public IChestGenerator getChestGenerator() {
        return factory.get();
    }
}
