/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 4, 2024
 *
 */
public class OneKey extends KeyItem {

	public OneKey(Item.Properties properties) {
		super(properties);
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
						TextFormatting.GOLD + new TranslationTextComponent("tooltip.one_key.specials").getString())
				);
		tooltip.add(new StringTextComponent(""));
		tooltip.add(new TranslationTextComponent("tooltip.indent4",
				TextFormatting.LIGHT_PURPLE + new TranslationTextComponent("tooltip.one_key.lore").getString()));
		tooltip.add(new StringTextComponent(""));
	}

	/**
	 * This key can fits any lock.
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		return true;
	}	

}
