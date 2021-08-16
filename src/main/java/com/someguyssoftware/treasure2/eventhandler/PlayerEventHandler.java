package com.someguyssoftware.treasure2.eventhandler;

import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.CoinItem;
import com.someguyssoftware.treasure2.item.PearlItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author Mark Gottschling on Apr 26, 2018
 *
 */
//@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void onTossCoinEvent(ItemTossEvent event) {
		if (WorldInfo.isClientSide(event.getPlayer().level)) {
			return;
		}
		Treasure.LOGGER.debug("is remote? -> {}", !event.getPlayer().level.isClientSide);
Treasure.LOGGER.debug("{} tossing coin -> {}", event.getPlayer().getName().getString(), event.getEntityItem().getItem().getDisplayName());
		Item item = event.getEntityItem().getItem().getItem();
		if (item instanceof /*IWishable*/CoinItem || item instanceof PearlItem) {
			ItemStack stack = event.getEntityItem().getItem();
			CompoundNBT nbt = new CompoundNBT();
			nbt.putString(CoinItem.DROPPED_BY_KEY, event.getPlayer().getName().getString());
			Treasure.LOGGER.debug("adding tag to coin stack...");
			stack.setTag(nbt);			
		}		
	}
	
	@SubscribeEvent
	public void onItemInfo(ItemTooltipEvent event) {
		if (event.getItemStack().getItem() == Items.EMERALD) {
			event.getToolTip().add(new TranslationTextComponent("tooltip.label.coin").withStyle(TextFormatting.GOLD, TextFormatting.ITALIC));
		}
	}
}
