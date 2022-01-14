/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.charm;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
//import com.someguyssoftware.treasure2.charm.HealingCharm.Builder;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.item.ItemStack;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public class TreasureCharms {
	
	static {
		Treasure.logger.debug("creating charms...");
		for (int level = 1; level <= 25; level++) {
			TreasureCharmRegistry.register(makeHealing(level));
			TreasureCharmRegistry.register(makeGreaterHealing(level));
			TreasureCharmRegistry.register(makeShielding(level));
			TreasureCharmRegistry.register(makeAegis(level));
			TreasureCharmRegistry.register(makeFireResistence(level));
			TreasureCharmRegistry.register(makeFireImmunity(level));
			TreasureCharmRegistry.register(makeSatiety(level));
			TreasureCharmRegistry.register(makeLifeStrike(level));
			TreasureCharmRegistry.register(makeReflection(level));
			TreasureCharmRegistry.register(makeDrain(level));
			TreasureCharmRegistry.register(makeIllumination(level));
			TreasureCharmRegistry.register(makeDecay(level));
			TreasureCharmRegistry.register(makeDecrepit(level));
			TreasureCharmRegistry.register(makeDirtFill(level));
			TreasureCharmRegistry.register(makeDirtWalk(level));
			TreasureCharmRegistry.register(makeRuin(level));
			// TODO harvesting			
		}
	}
	
	public static void init() {}
	
	// TODO add charms
	// [] CACTUS
	// [] DIRT (mound)
	
	/**
	 * Convenience method to build Healing Charm.
	 * @param level
	 * @return
	 */
	public static ICharm makeHealing(int level) {
		ICharm charm =  new HealingCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = 1.0;
			$.frequency = Math.max(20, 100.0 - (Math.floor((level + 1) / 5) *20)); // ie every 5 seconds, reducing by 1 second every 5th level
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeGreaterHealing(int level) {
		ICharm charm = new GreaterHealingCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = 2.0;
			$.frequency = Math.max(20, 100.0 - (Math.floor((level + 1) / 5) *20)); // ie every 5 seconds, reducing by 1 second every 5th level
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeShielding(int level) {
		ICharm charm =  new ShieldingCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = level < 4 ? 0.5 : level < 7 ? 0.6 : level < 10 ? 0.7 :  0.8;
			$.cooldown = 0.0;
			$.effectStackable = true;
			$.priority = 2;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeAegis(int level) {
		ICharm charm = new AegisCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.cooldown = Math.max(20, 100.0 - (Math.floor((level + 1) / 5) *10)); // ie every 5 seconds, reducing by 0.5 second every 5th level
			$.effectStackable = false;
			$.priority = 2;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireResistence(int level) {
		ICharm charm =  new FireResistenceCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = level < 6 ? 0.3 : level < 11 ? 0.5 : 0.8;
			$.effectStackable = true;
			$.priority = 1;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireImmunity(int level) {
		ICharm charm =  new FireImmunityCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.effectStackable = false;
			$.priority = 1;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeSatiety(int level) {
		ICharm charm =  new SatietyCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = 1.0;
			$.frequency = Math.max(20, 100.0 - (Math.floor((level + 1) / 5) *20)); // ie every 5 seconds, reducing by 1 second every 5th level
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	// TODO probably should have a cooldown
	public static ICharm makeLifeStrike(int level) {
		LifeStrikeCharm.Builder builder = (LifeStrikeCharm.Builder) new LifeStrikeCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = level < 4 ? 0.2 : level < 7 ? 0.4 : level < 10 ? 0.6 :  level < 13 ? 0.8 : 1.0;
//			$.percent = level < 4 ? 1.2 : level < 7 ? 1.4 : level < 10 ? 1.6 :  level < 13 ? 1.8 : 2.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		});
		ICharm charm = builder.withLifeCost(2.0).build();		
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeReflection(int level) {
		ICharm charm =  new ReflectionCharm.Builder(level).with($ -> {
			$.mana = level < 8 ? (level * 10.0 + 10.0) : ((level -7) * 10.0 + 10.0);
			$.amount = level < 3 ? 0.2 : level < 5 ? 0.35 : level < 7 ? 0.50 :  level <9 ? 0.65 : level < 11 ? 0.8 : level < 13 ? 0.95 : 1.1;
//			$.percent = level < 3 ? 0.2 : level < 5 ? 0.35 : level < 7 ? 0.50 :  level <9 ? 0.65 : level < 11 ? 0.8 : level < 13 ? 0.95 : 1.1;
			$.range = level < 4 ? 2D : level < 7 ? 3D : level < 10 ? 4D : level < 13 ? 5D : 6D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeDrain(int level) {
		ICharm charm =  new DrainCharm.Builder(level).with($ -> {
			$.mana =  level < 8 ? (level * 10.0 + 10.0) : ((level -7) * 10.0 + 10.0);
			$.frequency = Math.max(20, 100.0 - (Math.floor((level + 1) / 5) *20)); // ie every 5 seconds, reducing by 1 second every 5th level
			$.range = level < 4 ? 2D : level < 7 ? 3D : level < 10 ? 4D : level < 13 ? 5D : 6D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeIllumination(int level) {
		ICharm charm =  new IlluminationCharm.Builder(level).with($ -> {
			$.mana =  Math.max(1, (level / 3)) * 3.0;
			$.effectStackable = false;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	// curses
	public static ICharm makeDecay(int level) {
		ICharm curse =  new DecayCurse.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDecrepit(int level) {
		ICharm curse =  new DecrepitCurse.Builder(level).with($ -> {
			$.mana = level * 10.0 + 10.0;
			$.amount = (double) ((level + (level % 2))/20);
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDirtFill(int level) {
		ICharm curse =  new DirtFillCurse.Builder(level).with($ -> {
			$.mana = level * 25D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDirtWalk(int level) {
		ICharm curse =  new DirtWalkCurse.Builder(level).with($ -> {
			$.mana = level * 25D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeRuin(int level) {
		ICharm charm =  new RuinCurse.Builder(level).with($ -> {
			$.mana =  level * 20D;
			// in ticks (20 = 1 sec)
			$.frequency =	Math.max(200, 400.0 - (Math.floor((level + 1) / 5) *40)); // level < 4 ? 20D : level < 7 ? 17D : level < 10 ? 15D : level < 13 ? 13D : 10D;
			$.effectStackable = true;
			$.rarity = level < 4 ? Rarity.COMMON : level < 7 ? Rarity.UNCOMMON : level < 10 ? Rarity.SCARCE : level < 13 ? Rarity.RARE : Rarity .EPIC;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	/**
	 * This method is moot as charms will not have source items.
	 * @param stack
	 */
	public static void setHoverName(ItemStack stack) {
		if (stack.hasCapability(TreasureCapabilities.CHARMABLE, null)) {
			// TODO build name based on highest level charm ex. Copper Ring of the Moon
//			ICharmableCapability cap = stack.getCapability(TreasureCapabilities.CHARMABLE, null);
//			// check first if it is charmed - charmed names supercede source item names
//			if (cap.getSourceItem() != null && !cap.getSourceItem().equals(Items.AIR.getRegistryName())) {
//				Item sourceItem = ForgeRegistries.ITEMS.getValue(cap.getSourceItem());
//				stack.setStackDisplayName(
//						I18n.translateToLocal(sourceItem.getUnlocalizedName(new ItemStack(sourceItem)))
//						+ " "
//						+ stack.getDisplayName());
//			}
		}
	}
}
