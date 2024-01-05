package mod.gottsch.forge.treasure2.core.tileentity;

import mod.gottsch.forge.treasure2.core.chest.ChestSlotCount;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.inventory.WitherChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * NOTE Wither ChestConfig does not have a "lid", but has doors, however, the use of the lidAngle from the super class
 * will still be used.  The rendered will use that value to determine how to render the doors.
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestTileEntity extends AbstractTreasureChestTileEntity {
    
	/**
	 * 
	 * @param texture
	 */
	public WitherChestTileEntity() {
		super(TreasureTileEntities.WITHER_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslationTextComponent("display.wither_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new WitherChestContainer(windowID, TreasureContainers.WITHER_CHEST_CONTAINER_TYPE, inventory, this);
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getNumberOfSlots() {
		return ChestSlotCount.WITHER.getSize();
	}
}
