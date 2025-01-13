package mod.gottsch.forge.treasure2.core.world.feature.gen;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureGenContext;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;

import java.util.List;
import java.util.Optional;

public interface IWitherFeatureGenerator extends IFeatureGenerator {

    static final int X_CHUNKS = 3;
    static final int Z_CHUNKS = 3;
    static final int CHUNKS_IN_GRID = X_CHUNKS * Z_CHUNKS;


    int[][] generateGrid(IFeatureGenContext context, ICoords spawnCoords, IRarity rarity);

    public int getXChunks();
    public int getZChunks();
    public int getChunksInGrid();

    public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext featureGenContext, ICoords spawnCoords, IRarity rarity, ChestFeaturesConfiguration.ChestRarity chestRarity, int[][] grid, List<ICoords> queuedChunks);

}
