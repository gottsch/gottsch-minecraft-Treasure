/**
 * 
 */
package mod.gottsch.forge.treasure2.core.enums;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import mod.gottsch.forge.treasure2.core.generator.chest.CauldronChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.CommonChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.CrystalSkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.EpicChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.GoldSkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.LegendaryChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.MythicalChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.RareChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.ScarceChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.SkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.UncommonChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.WitherChestGenerator;

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
	LEGENDARY(LegendaryChestGenerator::new),
	MYTHICAL(MythicalChestGenerator::new),
	WITHER(WitherChestGenerator::new),
	SKULL(SkullChestGenerator::new),
	GOLD_SKULL(GoldSkullChestGenerator::new),
	CRYSTAL_SKULL(CrystalSkullChestGenerator::new),
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
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getNames() {
		List<String> names = EnumSet.allOf(ChestGeneratorType.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}