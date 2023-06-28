/**
 * 
 */
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;

import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * 
 * @author Mark Gottschling on Jul 5, 2020
 *
 */
public class EmberKey extends KeyItem {

	/**
	 * 
	 * @param properties
	 */
	public EmberKey(Item.Properties properties) {
		super(properties);
	}
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
//	@Deprecated
//	public EmberKey(String modID, String name, Item.Properties properties) {
//		super(modID, name, properties);
//	}
	

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public  void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(
				Component.translatable(LangUtil.tooltip("key_lock.specials"), 
						ChatFormatting.GOLD + Component.translatable(LangUtil.tooltip("key_lock.ember_key.specials")).getString())
				);			
	}
}