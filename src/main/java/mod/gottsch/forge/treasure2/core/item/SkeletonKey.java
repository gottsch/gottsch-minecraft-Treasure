/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import mod.gottsch.forge.treasure2.core.enums.Category;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Feb 2, 2018
 *
 */
public class SkeletonKey extends KeyItem {

	public SkeletonKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	@Deprecated
	public SkeletonKey(String modID, String name, Item.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				new TranslationTextComponent("tooltip.label.specials", 
				TextFormatting.GOLD + new TranslationTextComponent("tooltip.skeleton_key.specials").getString())
			);	
	}
	
	/**
	 * This key can fits any lock from the with a Rarity of [COMMON, UNCOMMON, SCARCE, and RARE].
	 */
	@Override
	public boolean fitsLock(LockItem lockItem) {
		Rarity rarity = lockItem.getRarity();
		if (rarity == Rarity.COMMON ||
				rarity == Rarity.UNCOMMON ||
				(rarity == Rarity.SCARCE  && lockItem.getCategory() != Category.WITHER) ||
				rarity == Rarity.RARE) return true;

		return false;
	}	

}
