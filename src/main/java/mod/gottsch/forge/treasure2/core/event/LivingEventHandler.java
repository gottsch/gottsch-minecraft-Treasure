/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.event;

import java.util.Random;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.weapon.Sword;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * 
 * @author Mark Gottschling May 25, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class LivingEventHandler {
	
	@SubscribeEvent
    public static void onItemAttributeModify(final ItemAttributeModifierEvent event){
//        ItemStack itemStack = event.getItemStack();
        
        // TODO is this adding extra messages on tooltip? Yes because it is random, so the tooltip doesn't always appear the same.
        // Might have to move this to the Damage or Hurt event
        // check for sword and axe
//        if (itemStack.getItem() instanceof Sword) { // || event.getItemStack().getItem() instanceof Axe) {
//        	Sword sword = (Sword) itemStack.getItem();
//        	if (RandomHelper.checkProbability(new Random(), sword.getCriticalChance())) {
//        		event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(IWeapon.CRITICAL_ATTACK_DAMAGE_UUID, "Critical attack damage", sword.getCriticalDamage(), AttributeModifier.Operation.ADDITION));
//        	}
//        }
	}
	
	// NOTE FINAL damage to be applied
	@SubscribeEvent
	public void checkCharmsInteractionWithDamage(LivingDamageEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getLevel())) {
			return;
		}
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void checkCharmsInteractionWithAttack(LivingHurtEvent event) {
		if (WorldInfo.isClientSide(event.getEntity().getLevel())) {
			return;
		}
		/*
		 *  TODO would have to check if the source is Player and check if the player is holding
		 *  a IWeapon in their main hand.
		 *
		 */
		if (event.getSource().getDirectEntity() instanceof Player) {
			Player player = (Player) event.getSource().getDirectEntity();
			ItemStack heldStack = player.getMainHandItem();
			if (heldStack != null & heldStack.getItem() instanceof Sword) {
				Sword sword = (Sword) heldStack.getItem();
				Treasure.LOGGER.debug("original damage -> {}", event.getAmount());
				if (RandomHelper.checkProbability(new Random(), sword.getCriticalChance())) {
					event.setAmount(event.getAmount() + sword.getCriticalDamage());
					Treasure.LOGGER.debug("new + critical damage -> {}", event.getAmount());
				}
			}
		}
		
	}	
}
