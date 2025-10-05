/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;


/**
 * 
 * @author Mark Gottschling on Feb 7, 2018
 *
 */
public class ThiefsLockPick extends KeyItem {
	/*
	 * The probability of a successful unlocking uncommon locks
	 */
	private double uncommonSuccessProbability;
	/*
	 * The probability of a successful unlocking scarce locks
	 */
	private double scarceSuccessProbability;

	/**
	 * 
	 * @param properties
	 */
	public ThiefsLockPick(Item.Properties properties) {
		this(properties, DEFAULT_MAX_USES);
	}

	public ThiefsLockPick(Item.Properties properties, int durability) {
		super(properties, durability);

		// add the default fitsLock predicates
		addFitsLock((level, lock) -> {
			return RarityTagAssociationRegistry.getLockRarity(lock, level.registryAccess())
					.map(rarity ->
							rarity == TreasureRarities.COMMON.get()
							|| rarity == TreasureRarities.UNCOMMON.get()
							|| rarity == TreasureRarities.SCARCE.get()
					)
					.orElse(false);
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

		Component s1 = Component.translatable(LangUtil.tooltip("key_lock.thiefs_lock_pick.specials"), 
				getSuccessProbability(), 
				(this.getSuccessProbability() - (this.getSuccessProbability()/4)),
				(getSuccessProbability()/2));
			
		Component s2 = Component.translatable(LangUtil.tooltip("key_lock.specials"), 
				ChatFormatting.GOLD + s1.getString());
		tooltip.add(s2);
	}

	/*
	 * If UNCOMMON lock, then this key has 25% less chance (ie x - x/4) of succeeding,
	 * If SCARCE lock, then this key has 50% less chance (ie x/2) of succeeding,
	 * @see com.someguyssoftware.treasure2.item.KeyItem#unlock(com.someguyssoftware.treasure2.item.LockItem)
	 */
	@Override
	public boolean unlock(Level level, LockItem lockItem) {
		if (lockItem.acceptsKey(this) || fitsLock(level, lockItem)) {
			Treasure.LOGGER.debug("Lock accepts key");

			IRarity rarity = lockItem.getRarity(level.registryAccess());
			if (rarity == TreasureRarities.COMMON.get()) {
				if (RandomHelper.checkProbability(new Random(), this.getSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}
			}
			else if (rarity == TreasureRarities.UNCOMMON.get()) {
				if (RandomHelper.checkProbability(new Random(), this.getUncommonSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}
			else if (rarity == TreasureRarities.SCARCE.get()) {
				if (RandomHelper.checkProbability(new Random(), this.getScarceSuccessProbability())) {
					Treasure.LOGGER.debug("Unlock attempt met probability");
					return true;
				}				
			}		
		}
		return false;
	}

	public ThiefsLockPick setSuccessProbability(double commonProbability, double uncommonProbability, double scarceProbability) {
		setSuccessProbability(commonProbability);
		setUncommonSuccessProbability(uncommonProbability);
		setScarceSuccessProbability(scarceProbability);

		return this;
	}

	public double getUncommonSuccessProbability() {
		return uncommonSuccessProbability;
	}

	public void setUncommonSuccessProbability(double uncommonSuccessProbability) {
		this.uncommonSuccessProbability = uncommonSuccessProbability;
	}

	public double getScarceSuccessProbability() {
		return scarceSuccessProbability;
	}

	public void setScarceSuccessProbability(double scarceSuccessProbability) {
		this.scarceSuccessProbability = scarceSuccessProbability;
	}
}
