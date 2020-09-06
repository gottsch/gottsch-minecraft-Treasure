package com.someguyssoftware.treasure2.item;

/**
 * 
 * @author Mark Gottschling on Sep 5, 2018
 *
 */
public class EmberLock extends LockItem {

    /**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(
				I18n.translateToLocalFormatted("tooltip.label.specials", 
				TextFormatting.GOLD) + I18n.translateToLocal("tooltip.ember_lock.specials")
			);
    }
    
    @Override
    public boolean breaksKey(KeyItem key) {
        if (key == TreasureItems.WOOD_KEY) {
            return true;
        }
    }
}