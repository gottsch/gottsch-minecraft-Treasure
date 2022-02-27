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
package com.someguyssoftware.treasure2.loot.function;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * Not to be confused with CharmRandomly. This
 * @author Mark Gottschling on Feb 21, 2022
 *
 */
public class RandomCharm extends LootFunction {
	private static final String LOCATION = new ResourceLocation("treasure2:random_charm");
	private static final String LEVELS = "levels";
	private static final String RARITY = "rarity";
	private static final String RARITIES = "rarities";
	private static final String DEFAULT_CHARM = "defaultCharm";

	// TODO need to register charmItems by level in TreasureItems.
	private RandomValueRange levels;
	private Rarity rarity;
	// private List<Rarity> rarities;
	private ICharm defaultCharm;

	/**
	 * 
	 * @param conditions
	 * @param levels
	 * @param defaultCharm
	 */
	public CharmRandomly(LootCondition[] conditions, RandomValueRange levels, Rarity rarity, ICharm defaultCharm) {
		super(conditions);
		this.levels = levels;
		this.rarity = rarity;
		this.defaultCharm = defaultCharm;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		
		// default charm
		ICharm defaultCharm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1))).get();
		ICharm charm = defaultCharm;
		Optional<List<ICharm>> charms = Optional.empty();

		// select random level
		int level = this.levels == null ? 1 : this.levels.generateInt(rand);

		// TODO if rarities property, select a random rarity

		if (rarity != null) {
			Optional<List<ICharm>> charms = TreasureCharmRegistry.get(rarity);
			if (charms.isPresent()) {
				Random random = new Random();
				charm = charms.get().get(random.nextInt(charms.get().size()));
				// override level with that of the selected rarity charm
				level = charm.getLevel();
			}
			else {
				charm = defaultCharm;
			}
		}
		else {
			// select a charm by level
			Optional<List<ICharm>> charms = TreasureCharmRegistry.get(level);
			if (charms.isPresent()) {
				Random random = new Random();
				charm = charms.get().get(random.nextInt(charms.get().size()));
			}
			else {
				charm = defaultCharm;
			}
		}

		// select first charm item that meets the minimum level == level
		// TODO for now, cycle through all ITEMS cache
		ItemStack charmStack = null;
		// TODO need to order charm items by max level
		for (Item item : TreasureItems.ITEMS.getValues()) {
			if (item instanceof CharmItem) {
				ItemStack itemStack = new ItemStack(item);
				// get the capability
				ICharmableCapability cap = itemStack.getCapability(TreasureCapabilities.CHARMABLE, null);
				if (cap != null) {
					if (cap.getMaxCharmLevel() == level) {
						charmStack = itemStack;
						break;
					}
				}
			}
		}

		if (charmStack == null) {
			// TODO default to level 1 item ie silver_charm
			// charmItem = TreasureItems.ITEMS.get(new ResourceLocation("treasure2:silver_charm"));
			charmStack = stack;
			charm = defaultCharm;
		}

		// add charm to charmStack
		ICharmableCapability cap = charmStack.getCapability(TreasureCapabilities.CHARMABLE, null);
		cap.getCharmEntities().get(InventoryType.INNATE).add(charm.createEntity());

		return charmStack;
	}


	public static class Serializer extends LootFunction.Serializer<RandomCharm> {
		public Serializer() {
			super(LOCATION, RandomCharm.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, RandomCharm value, JsonSerializationContext context) {
			if (json.has(LEVELS)) {
				json.add(LEVELS, context.serialize(value.levels));
			}
			if (json.has(RARITY)) {
				json.add(RARITY, new JsonPrimitive(value.rarity.name())); // TODO toString() ?
			}
			if (json.has(DEFAULT_CHARM)) {
				json.add(DEFAULT_CHARM, new JsonPrimitive(value.defaultCharm.getName().toString()));
			}
		}

		/**
		 * 
		 */
		public CharmRandomly deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			
			RandomValueRange levels = null;
			if (json.has(LEVELS)) {
				 levels = JsonUtils.deserializeClass(json, LEVELS, deserializationContext, RandomValueRange.class);	
			}
			// TODO potential create new RandomValueRange(1)

			Rarity rarity = null;
			if (json.has(RARITY)) {
				String rarityString = JsonUtils.getString(json, RARITY);
				try {
					rarity = Rarity.valueOf(typeString.toUpperCase());
				}
				catch(Exception e) {
					Treasure.logger.error("Unable to convert rarity {} to Rarity", typeString);
				}
			}

			Optional<ICharm> charm = Optional.empty();
			if (json.has(DEFAULT_CHARM)) {
				String charmName = JsonUtils.getString(json, DEFAULT_CHARM);
				charm = TreasureCharmRegistry.get(new ResourceLocation(charmName));
			}

			if (!charm.isPresent()) {
				// set a default
				charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(Charm.Builder.makeName(HealingCharm.TYPE, 1)));
			}
			
			return new RandomCharm(conditionsIn, levels, rarity, charm.get());
		}
	}
}