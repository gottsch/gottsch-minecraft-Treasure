/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import mod.gottsch.forge.treasure2.core.enums.Category;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Mar 25, 2018
 *
 */
public class JewelledKey extends KeyItem {

	public JewelledKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public JewelledKey(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslationTextComponent("tooltip.label.specials", 
				TextFormatting.GOLD + new TranslationTextComponent("tooltip.jewelled_key.specials").getString())
			);	
	}
	
	/**
	 * This key can fits any lock from the with a Rarity of [COMMON, UNCOMMON, SCARCE, and RARE].
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		if (lockItem.getCategory() == Category.GEMS) return true;
		return false;
	}	

}
