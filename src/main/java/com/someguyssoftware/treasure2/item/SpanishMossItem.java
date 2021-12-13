/**
 * 
 */
package com.someguyssoftware.treasure2.item;

import com.someguyssoftware.gottschcore.block.BlockContext;
import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

/**
 * @author Mark Gottschling on Feb 5, 2021
 *
 */
public class SpanishMossItem  extends ModItem {

	/**
	 * 
	 */
	public SpanishMossItem(String modID, String name, Item.Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.MOD_ITEM_GROUP));
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
