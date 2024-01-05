/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import com.someguyssoftware.gottschcore.item.ModItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jul 29, 2018
 *
 */
public class TreasureToolItem extends ModItem {

	public TreasureToolItem(Item.Properties properties) {
		super(properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public TreasureToolItem(String modID, String name, Item.Properties properties) {
		super(modID, name, properties.tab(TreasureItemGroups.TREASURE_ITEM_GROUP));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);	
		tooltip.add(new TranslationTextComponent("tooltip.label.treasure_tool"));
	}	
	
	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}

	/**
	 * Required to prevent item consumpution in recipe
	 */
	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
        if (!hasContainerItem(itemStack)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(this);
	}
}
