package mod.gottsch.forge.treasure2.core.tileentity;

import mod.gottsch.forge.treasure2.core.inventory.StandardChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class PirateChestTileEntity extends AbstractTreasureChestTileEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public PirateChestTileEntity() {
		super(TreasureTileEntities.PIRATE_CHEST_TILE_ENTITY_TYPE);
		setCustomName(new TranslationTextComponent("display.pirate_chest.name"));
	}
	
	/**
	 * 
	 * @param windowID
	 * @param inventory
	 * @param player
	 * @return
	 */
	public Container createServerContainer(int windowID, PlayerInventory inventory, PlayerEntity player) {
		return new StandardChestContainer(windowID, inventory, this);
	}
}
