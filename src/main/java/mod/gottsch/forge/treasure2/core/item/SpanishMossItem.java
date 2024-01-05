/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

/**
 * @author Mark Gottschling on Feb 5, 2021
 *
 */
@Deprecated
public class SpanishMossItem  extends ModItem {

	/**
	 * 
	 * @param properties
	 */
	public SpanishMossItem(Item.Properties properties) {
		super(properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}
	
	/**
	 * 
	 */
	@Deprecated
	public SpanishMossItem(String modID, String name, Item.Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		BlockPos blockPos = context.getClickedPos();
		if (!state.canBeReplaced(context)) {
			blockPos.relative(context.getClickedFace());
		}		
		BlockContext blockContext = new BlockContext(context.getLevel(), blockPos);
		if (blockContext.isAir() || blockContext.isReplaceable()) {
			return context.getLevel().setBlock(context.getClickedPos(), state, 26);
		}
		return false;	
	}
}
