package mod.gottsch.forge.treasure2.core.inventory;

import java.util.Set;
import java.util.stream.Collectors;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.item.Adornment;
import mod.gottsch.forge.treasure2.core.item.CharmItem;
import mod.gottsch.forge.treasure2.core.item.RunestoneItem;
import mod.gottsch.forge.treasure2.core.item.WealthItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchSlot extends Slot {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public PouchSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}


	@Override
    public boolean mayPlace(ItemStack stack) {
		Set<String> tags = stack.getItem().getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
		if (stack.getItem() instanceof WealthItem
				|| stack.getItem() instanceof CharmItem
				|| stack.getItem() instanceof Adornment
				|| 	stack.getItem() instanceof RunestoneItem
				|| tags.contains("pouch")) {
    		return true;
    	}
    	return false;
    }
}
