package mod.gottsch.forge.treasure2.core.block;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;

public class WitherwoodRotatedPillarBlock extends RotatedPillarBlock implements ITreasureBlock {

    public WitherwoodRotatedPillarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if(context.getItemInHand().getItem() instanceof AxeItem) {
            if(state.is(TreasureBlocks.WITHERWOOD_LOG.get())) {
                return TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }

            if(state.is(TreasureBlocks.WITHERWOOD_WOOD.get())) {
                return TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }

        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
