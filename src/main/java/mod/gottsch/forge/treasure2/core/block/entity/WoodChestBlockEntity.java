package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class WoodChestBlockEntity extends AbstractTreasureChestBlockEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public WoodChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.WOOD_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("wood_chest.name"));
	}
    
	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}
}
