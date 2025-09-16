package mod.gottsch.forge.treasure2.core.block;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.StructureMobSetBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * This block is meant for Jigsaw Structures only. It's purpose is to act as a marker and
 * store data for ProximityMobSetSpawnerBlocks. A ProximityMobSetProcessor will replace
 * this block for the ProximityMobSetSpawnBlock, and use the data stored to configure
 * the new block.
 * @author by Mark Gottschling on 8/14/2025
 */
public class StructureMobSetBlock extends BaseEntityBlock implements ITreasureBlock {

    public StructureMobSetBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StructureMobSetBlockEntity(
                TreasureBlockEntities.STRUCTURE_MOB_SET.get(), pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

}
