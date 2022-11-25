package mod.gottsch.forge.treasure2.core.inventory;

import mod.gottsch.forge.treasure2.core.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class StandardChestContainerMenu extends AbstractChestContainerMenu {

	/**
	 * 
	 * @param containerId
	 * @param pos
	 * @param playerInventory
	 * @param player
	 */
	public StandardChestContainerMenu(int containerId, BlockPos pos, Inventory playerInventory, Player player) {
		super(containerId, TreasureContainers.STANDARD_CHEST_CONTAINER.get(), pos, playerInventory, player);
		
		if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
			setMenuInventoryYPos(19);
			setPlayerInventoryYPos(85);
			setHotbarYPos(143);
		}
		buildContainer();
	}
}
