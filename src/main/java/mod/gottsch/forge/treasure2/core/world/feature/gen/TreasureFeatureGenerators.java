package mod.gottsch.forge.treasure2.core.world.feature.gen;

import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenerator;

public class TreasureFeatureGenerators {

	public static final IFeatureGenerator SIMPLE_SURFACE_FEATURE_GENERATOR = new SimpleSurfaceFeatureGenerator();
	public static final IFeatureGenerator PIT_FEATURE_GENERATOR = new PitFeatureGenerator();
	public static final IFeatureGenerator SURFACE_STRUCTURE_FEATURE_GENERATOR = new SurfaceStructureFeatureGenerator();
}
