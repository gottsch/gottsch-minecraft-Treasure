package mod.gottsch.forge.treasure2.core.generator.witherTree;

import com.google.common.collect.Maps;
import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.ITreasureBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Mark Gottschling on 12/26/2024
 */
public class GreatWitherTreeGenerator implements IWitherTreeGenerator<GeneratorResult<? extends GeneratorData>> {

    private static final int NW = 0;
    private static final int N = 1;
    private static final int NE = 2;
    private static final int W = 3;
    private static final int CORE = 4;
    private static final int E = 5;
    private static final int SW = 6;
    private static final int S = 7;
    private static final int SE = 8;

    private static final int DEGREES = 360;
    private static final int MIN_SIZE = 11;

    @Override
    public Optional generate(IWorldGenContext context, ICoords coords) {
        /*
         * Setup
         */
        GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);

        // clear the area
        generateClearing(context, coords);

        Map<Integer, List<Direction>> trunkMatrix = buildTrunkMap();
        List<Direction> trunkTopMatrix = buildTrunkTopMap();
        ICoords[] trunkCoords = buildTrunkCoords(coords);

        // determine the max size of any trunk piece
        int maxSize = RandomHelper.randomInt(context.random(),
                Math.min(getMinSize(), Config.SERVER.witherTree.maxTrunkSize.get() + 2),
                Math.max(getMinSize(), Config.SERVER.witherTree.maxTrunkSize.get() + 2));

        // randomize the size of the working trunk piece
        int size = RandomHelper.randomInt(context.random(), Math.min(getMinSize(), maxSize), Math.max(getMinSize(), maxSize));
        int tallestSize = 0;

        for (int trunkIndex = 0; trunkIndex < trunkCoords.length; trunkIndex++) {
            // select the log
            BlockState trunkBlockState;
            trunkBlockState = (trunkIndex == CORE)
                    ? trunkBlockState = TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState()
                    : TreasureBlocks.WITHERWOOD_LOG.get().defaultBlockState();

            for (int y = 0; y < size; y++) {
                // add the trunk
                context.level().setBlock(trunkCoords[trunkIndex].up( y).toPos(),	trunkBlockState, 3);

                // add the decorations (branches, roots, top)
                if (trunkIndex != CORE) {
                    if (y == 0) {
                        addRoot(context, trunkCoords[trunkIndex], coords, trunkMatrix.get(trunkIndex));
                    } else if (y == size - 1 && trunkIndex % 2 == 0) {
                        addTop(context, trunkCoords[trunkIndex], coords, y + 1, trunkTopMatrix.get(trunkIndex));
                    } else if (y >= 3) {
                        addBranch(context, trunkCoords[trunkIndex], coords, y, size, trunkMatrix.get(trunkIndex));
                    }
                }
            }

            // store the tallest size.
            tallestSize = Math.max(size, tallestSize);

            // set the new size
            if (trunkIndex == CORE) {
                size = RandomHelper.randomInt(context.random(), Math.min(getMinSize(), tallestSize - 1), Math.max(getMinSize(), tallestSize - 1));
            } else {
                size = RandomHelper.randomInt(context.random(), Math.min(getMinSize(), maxSize), Math.max(getMinSize(),maxSize));
            }
        }
        return Optional.empty();
    }

    private Map<Integer, List<Direction>> buildTrunkMap() {
        Map<Integer, List<Direction>> map = Maps.newHashMap();

        map.put(NW, List.of(Direction.NORTH, Direction.WEST));
        map.put(N, List.of(Direction.NORTH));
        map.put(NE, List.of(Direction.NORTH, Direction.EAST));

        map.put(W, List.of(Direction.WEST));
        map.put(CORE, new ArrayList<>());
        map.put(E, List.of(Direction.EAST));

        map.put(SW, List.of(Direction.SOUTH, Direction.WEST));
        map.put(S, List.of(Direction.SOUTH));
        map.put(SE, List.of(Direction.SOUTH, Direction.EAST));

        return map;
    }

    private List<Direction> buildTrunkTopMap() {
        List<Direction> list = Lists.newArrayList();
        list.add(NW, Direction.EAST);
        list.add(N, Direction.SOUTH);
        list.add(NE, Direction.SOUTH);
        list.add(W, Direction.EAST);
        list.add(CORE, Direction.NORTH);
        list.add(E, Direction.WEST);
        list.add(SW, Direction.NORTH);
        list.add(S, Direction.NORTH);
        list.add(SE, Direction.WEST);
        return list;
    }

    private ICoords[] buildTrunkCoords(ICoords coords) {
        ICoords[] trunkCoords = new Coords[9];
        trunkCoords[NW] = coords;
        trunkCoords[N] = coords.east(1);
        trunkCoords[NE] = coords.east(2);
        trunkCoords[W] = coords.south(1);
        trunkCoords[CORE] = coords.add(1, 0, 1);
        trunkCoords[E] = coords.add(2, 0, 1);
        trunkCoords[SW] = coords.south(2);
        trunkCoords[S] = coords.add(1, 0, 2);
        trunkCoords[SE] = coords.add(2, 0, 2);

        return trunkCoords;
    }

    public int getMinSize() {
        return MIN_SIZE;
    }


}
