/**
 * 
 */
package mod.gottsch.forge.treasure2.core.inventory;

import net.minecraft.inventory.IInventory;

/**
 * @author Mark Gottschling on Aug 21, 2020
 *
 */
public interface ITreasureContainer {

	IInventory getContents();

	void setContents(IInventory inventory);

}
