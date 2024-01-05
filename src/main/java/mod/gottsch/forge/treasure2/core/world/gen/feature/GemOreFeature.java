package mod.gottsch.forge.treasure2.core.world.gen.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2021
 *
 */
public class GemOreFeature extends OreFeature {
	private Map<String, Integer> chunksSinceLastDimensionOre	= new HashMap<>();
	
	public GemOreFeature(Codec<OreFeatureConfig> configFactory) {
		super(configFactory);
		// NOTE ensure to set the registry name
		this.setRegistryName(Treasure.MODID, "gem_ore");
	}

	@Override
	public boolean place(ISeedReader seedReader, ChunkGenerator generator, Random rand, BlockPos pos, OreFeatureConfig config) {

		// inspect block to determine generation probability
		double prob = 0;
		if (config.state.getBlock() == TreasureBlocks.RUBY_ORE) {
//			Treasure.LOGGER.debug("ruby gem");
			prob = TreasureConfig.GEMS_AND_ORES.rubyGenProbability.get();
		}
		else if (config.state.getBlock() == TreasureBlocks.SAPPHIRE_ORE) {
//			Treasure.LOGGER.debug("sapphire gem: view size -> {}", TreasureConfig.GEMS_AND_ORES.sapphireOreVeinSize.get());
			prob = TreasureConfig.GEMS_AND_ORES.sapphireGenProbability.get();
		}
		else if (config.state.getBlock() == TreasureBlocks.TOPAZ_ORE) {
			prob = TreasureConfig.GEMS_AND_ORES.topazGenProbability.get();
		}
		else if (config.state.getBlock() == TreasureBlocks.ONYX_ORE) {
			prob = TreasureConfig.GEMS_AND_ORES.onyxGenProbability.get();
		}
//		Treasure.LOGGER.debug("config probability -> {}", prob);
// TODO add Topaz and Onyx
		
		// test the probability
		if (!RandomHelper.checkProbability(rand, prob)) {
//			Treasure.LOGGER.debug("probability NOT met");
			return false;
		}
		
		return super.place(seedReader, generator, rand, pos, config);
	}
}
