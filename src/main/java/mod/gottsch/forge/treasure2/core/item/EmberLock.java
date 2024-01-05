package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jul 5, 2020
 *
 */
public class EmberLock extends LockItem {

	/**
	 * 
	 * @param properties
	 * @param keys
	 */
	public EmberLock(Item.Properties properties, KeyItem[] keys) {
		super(properties, keys);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param keys
	 */
	@Deprecated
	public EmberLock(String modID, String name, Item.Properties properties, KeyItem[] keys) {
		super(modID, name, properties, keys);
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

		tooltip.add(new TranslationTextComponent("tooltip.label.specials",
				TextFormatting.GOLD + new TranslationTextComponent("tooltip.ember_lock.specials").getString())
				);		
	}

	@Override
	public boolean breaksKey(KeyItem key) {
		if (key != TreasureItems.EMBER_KEY.get() && key != TreasureItems.LIGHTNING_KEY.get()) {
			return true;
		}
		return false;
	}
}