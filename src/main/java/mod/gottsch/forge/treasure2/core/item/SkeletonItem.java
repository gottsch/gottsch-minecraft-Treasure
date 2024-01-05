/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.item.ModBlockItem;

import mod.gottsch.forge.treasure2.core.block.SkeletonBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

/**
 * @author Mark Gottschling on Feb 2, 2019
 *
 */
public class SkeletonItem extends ModBlockItem {
	public static final int MAX_STACK_SIZE = 1;

	/**
	 * 
	 * @param block
	 * @param properties
	 */
	public SkeletonItem(Block block, Item.Properties properties) {
		super(block, properties.stacksTo(MAX_STACK_SIZE).tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}
	
	/**
	 * 
	 */
	@Deprecated
	public SkeletonItem(String modID, String name, Block block, Item.Properties properties) {
		super(modID, name, block, properties.stacksTo(MAX_STACK_SIZE).tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}

	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		BlockPos blockPos = context.getClickedPos().relative(state.getValue(SkeletonBlock.FACING).getOpposite());
		BlockContext blockContext = new BlockContext(context.getLevel(), blockPos);
		if (blockContext.isAir() || blockContext.isReplaceable()) {
			return context.getLevel().setBlock(context.getClickedPos(), state, 26);
		}
		return false;
	}
}