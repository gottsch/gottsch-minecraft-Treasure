/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import java.util.List;
import java.util.Random;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.translatable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;


/**
 * 
 * @author Mark Gottschling on Feb 7, 2018
 *
 */
public class ThiefsLockPick extends KeyItem {

	/**
	 * 
	 * @param properties
	 */
	public ThiefsLockPick(Item.Properties properties) {
		super(properties);
		// add the default fitsLock predicates
		addFitsLock(lock -> {
			IRarity rarity = KeyLockRegistry.getRarityByLock(lock);
			return 
					(rarity == Rarity.COMMON ||
					rarity == Rarity.UNCOMMON ||
					rarity == Rarity.SCARCE);
		});
	}

	/**
	 * 
	 * @param modID
	 * @param name
	 */
//	@Deprecated
//	public ThiefsLockPick(String modID, String name, Item.Properties properties) {
//		super(modID, name, properties);
//	}
	
	/**
	 * Format: (Additions)
	 * 
	 * Specials: [text] [color=gold]
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public  void appendHoverSpecials(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag) {

		Component.translatable s1 = Component.translatable(LangUtil.tooltip("key_lock.thiefs_lock_pick.specials"), 
				getSuccessProbability(), 
				(this.getSuccessProbability() - (this.getSuccessProbability()/4)),
				(getSuccessProbability()/2));
			
		Component.translatable s2 = Component.translatable(LangUtil.tooltip("key_lock.specials"), 
				ChatFormatting.GOLD + s1.getString());
		tooltip.add(s2);
	}

	/*
	 * If UNCOMMON lock, then this key has 25% less chance (ie x - x/4) of succeeding,
	 * If SCARCE lock, then this key has 50% less chance (ie x/2) of succeeding,
	 * @see com.someguyssoftware.treasure2.item.KeyItem#unlock(com.someguyssoftware.treasure2.item.LockItem)
	 */
	@Override
	public boolean unlock(LockItem lockItem) {
		if (lockItem.acceptsKey(this) || fitsLock(lockItem)) {
			Treasure.LOGGER.debug("Lock accepts key");
			if (lockItem.getRarity() == Rarity.COMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}
			}
			else if (lockItem.getRarity() == Rarity.UNCOMMON) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability() - (this.getSuccessProbability()/4))) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}
			else if (lockItem.getRarity() == Rarity.SCARCE) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability()/2)) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}		
		}
		return false;
	}
}
