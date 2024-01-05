package mod.gottsch.forge.treasure2.core.tileentity;

import mod.gottsch.forge.treasure2.core.chest.ChestSlotCount;
import mod.gottsch.forge.treasure2.core.inventory.CompressorChestContainer;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Mar 17, 2018
 *
 */
public class CompressorChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public CompressorChestTileEntity() {
		super(TreasureTileEntities.COMPRESSOR_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslationTextComponent("display.compressor_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new CompressorChestContainer(windowID, TreasureContainers.COMPRESSOR_CHEST_CONTAINER_TYPE, inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.COMPRESOR.getSize();
	}
}
