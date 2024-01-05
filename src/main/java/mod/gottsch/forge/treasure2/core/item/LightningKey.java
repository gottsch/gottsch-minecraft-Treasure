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
 * @author Mark Gottschling on Sep 11, 2020
 *
 */
public class LightningKey extends KeyItem {

	/**
	 * 
	 * @param properties
	 */
	public LightningKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public LightningKey(String modID, String name, Item.Properties properties) {
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
						TextFormatting.GOLD + new TranslationTextComponent("tooltip.lightning_key.specials").getString())
				);	
	}

	/**
	 * This key can fits any lock from the with a Category of [ELEMENTAL].
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		Category category = lockItem.getCategory();
		if (category == Category.ELEMENTAL) return true;
		return false;
	}
}