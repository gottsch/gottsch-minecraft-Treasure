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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;

import java.util.Map;

import com.google.common.collect.Maps;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Rarities:
 * Common = 1-4
 * Uncommon = 5-8
 * Scarce = 9-12
 * Rare = 13-16
 * Epic = 17-20
 * Legandary = 21-24
 * Mythical = 25+
 *
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public class TreasureCharms {
	// TODO move somewhere more generic
	public static final Map<Integer, Rarity> LEVEL_RARITY = Maps.newHashMap();
	
	static {
		Treasure.LOGGER.debug("creating charms...");
		for (int level = 1; level <= 25; level++) {
			LEVEL_RARITY.put(Integer.valueOf(level), level <= 4 ? Rarity.COMMON 
					: level <= 8 ? Rarity.UNCOMMON
							: level <= 12 ? Rarity.SCARCE 
									: level <=16 ? Rarity.RARE
										: level <= 20 ? Rarity .EPIC
											: level <=24 ? Rarity.LEGENDARY
												: Rarity.MYTHICAL);
			
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
			TreasureCharmRegistry.register(makeCheatDeath(level));
			// TODO harvesting			
			
		}
	}
	
	// TODO move static {} block into init as static block is not loaded until this class is referenced and thus might not be init when items are being created.
	public static void init() {}
	
	private static int getRecharges(Rarity rarity) {
		// TODO COMMON & UNCOMMON = 1 are TEMP
		return rarity == Rarity.SCARCE || rarity == Rarity.RARE ? 1 : rarity == Rarity.EPIC || rarity == Rarity.LEGENDARY ? 2 : rarity == Rarity.MYTHICAL ? 3 : 1;
	}
	
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
			$.frequency = Math.max(40, 100.0 - (Math.floor((level + 1) / 5D) * 10D)); // ie every 5 seconds, reducing by 1 second every 5th level
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeGreaterHealing(int level) {
		ICharm charm = new GreaterHealingCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = 2.0;
			$.frequency = Math.max(40, 100.0 - (Math.floor((level + 1) / 5D) * 10)); // ie every 5 seconds, reducing by 0.5 second every 5th level
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
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
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
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
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireResistence(int level) {
		ICharm charm =  new FireResistenceCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = level < 6 ? 0.3 : level < 11 ? 0.5 : 0.8;
			$.amount = level <= 4 ? 0.2 : level <=8  ? 0.4 : level <= 12 ? 0.6 :  0.8;
			$.effectStackable = true;
			$.priority = 1;
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
		})	.build();

		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	public static ICharm makeFireImmunity(int level) {
		ICharm charm =  new FireImmunityCharm.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.effectStackable = false;
			$.priority = 1;
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
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
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
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
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
		});
		ICharm charm = builder.withLifeCost(2.0).build();		
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeReflection(int level) {
		ICharm charm =  new ReflectionCharm.Builder(level).with($ -> {
			$.mana = level < 8 ? (level * 10.0 + 10.0) : ((level -7) * 10.0 + 10.0);
			$.amount = level < 3 ? 0.2 : level < 5 ? 0.35 : level < 7 ? 0.50 :  level <9 ? 0.65 : level < 11 ? 0.8 : level < 13 ? 0.95 : 1.1;
			$.range = level < 4 ? 2D : level < 7 ? 3D : level < 10 ? 4D : level < 13 ? 5D : 6D;
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
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
			$.rarity = LEVEL_RARITY.get(level);
			$.recharges = getRecharges($.rarity);
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeIllumination(int level) {
		ICharm charm =  new IlluminationCharm.Builder(level).with($ -> {
			$.mana =  Math.max(1, (level / 3)) * 3.0;
			$.effectStackable = false;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}

	public static ICharm makeCheatDeath(int level) {
		ICharm charm =  new CheatDeathCharm.Builder(level).with($ -> {
			$.mana =  (double) level;
			$.amount = 5.0 + Math.floor(level / 5D);
			$.effectStackable = false;
			$.rarity = LEVEL_RARITY.get(level);			
			$.recharges = 0;
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	// curses
	public static ICharm makeDecay(int level) {
		ICharm curse =  new DecayCurse.Builder(level).with($ -> {
			$.mana = level * 20.0;
			$.amount = 1.0;
			// in ticks (20 = 1 sec)
			$.frequency = Math.max(40, 100.0 - (Math.floor((level + 1) / 5D) * 10)); // ie every 5 seconds, reducing by 0.5 second every 5th level
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDecrepit(int level) {
		ICharm curse =  new DecrepitCurse.Builder(level).with($ -> {
			$.mana = level * 10.0 + 10.0;
//			$.amount = (double) ((level + (level % 2))/20);
			$.amount = level <= 4 ? 0.2 : level <=8  ? 0.3 : level <= 12 ? 0.4 :  0.5;
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDirtFill(int level) {
		ICharm curse =  new DirtFillCurse.Builder(level).with($ -> {
			$.mana = level * 25D;
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeDirtWalk(int level) {
		ICharm curse =  new DirtWalkCurse.Builder(level).with($ -> {
			$.mana = level * 25D;
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();		
		TreasureCharmRegistry.register(curse);
		return curse;
	}

	public static ICharm makeRuin(int level) {
		ICharm charm =  new RuinCurse.Builder(level).with($ -> {
			$.mana =  level * 20D;
			$.amount = 1.0;
			// in ticks (20 = 1 sec)
			$.frequency = Math.max(40, 100.0 - (Math.floor((level + 1) / 5D) * 10)); // ie every 5 seconds, reducing by 0.5 second every 5th level
			$.effectStackable = true;
			$.rarity = LEVEL_RARITY.get(level);
		})	.build();
		TreasureCharmRegistry.register(charm);
		return charm;
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @return
	 */
	public static ItemStack copyStack(final ItemStack source, final ItemStack dest) {
		ItemStack resultStack = dest.copy(); // <-- is this necessary?
		// save the source item
		ResourceLocation sourceItem = resultStack.getCapability(CHARMABLE, null).map(cap -> cap.getSourceItem()).orElse(null);

		source.getCapability(CHARMABLE).ifPresent(sourceCap -> {
			resultStack.getCapability(CHARMABLE).ifPresent(cap -> {
				cap.clearCharms();
			});
			sourceCap.copyTo(resultStack);
		});

		
		
//		if (dest.hasCapability(CHARMABLE, null)) {
//			resultStack.getCapability(CHARMABLE, null).clearCharms();			
//			source.getCapability(CHARMABLE, null).copyTo(resultStack);
//		}

		// reset the source item
		resultStack.getCapability(CHARMABLE, null).ifPresent(cap -> {
			cap.setSourceItem(sourceItem);
		});

		return resultStack;
	}
	
	/**
	 * This method is moot as charms will not have source items.
	 * @param stack
	 */
	@Deprecated
	public static void setHoverName(ItemStack stack) {

	}
}
