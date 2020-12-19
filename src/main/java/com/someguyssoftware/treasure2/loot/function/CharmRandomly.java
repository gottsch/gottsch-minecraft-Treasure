/**
 * 
 */
package com.someguyssoftware.treasure2.loot.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.item.charm.CharmLevel;
import com.someguyssoftware.treasure2.item.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;
import com.someguyssoftware.treasure2.item.charm.TreasureCharms;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * @author Mark Gottschling on May 1, 2020
 * @param <ICharmType>
 *
 */
public class CharmRandomly extends LootFunction {
	private List<ICharm> charms;

	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		ICharm charm = null;

		// ensure that the stack has charm capabilities
		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability provider = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmInstance> charmInstances = provider.getCharmInstances();

			if (this.charms.isEmpty()) {
				List<ICharm> tempCharms = new ArrayList<>();
				// if charms list is empty, create a default list of minor charms
				for (ICharm c : TreasureCharms.REGISTRY.values()) {
					if (c.getLevel() == CharmLevel.LEVEL1.getValue() || c.getLevel() == CharmLevel.LEVEL2.getValue()) {
						tempCharms.add(c);
					}
				}
				if (!tempCharms.isEmpty()) {
					// select a charm randomly
					charm = tempCharms.get(rand.nextInt(tempCharms.size()));
				}
			}
			else {
				// select a charm randomly
				charm =this.charms.get(rand.nextInt(this.charms.size()));
				Treasure.logger.debug("selected charm for item -> {}", charm.getName());
			}
			if (charm != null) {
				// ensure that the item doesn't already have the same charm or same type or exceeded the maximum charms.
				boolean hasCharm = false;
				for (ICharmInstance instance : charmInstances) {
					if (instance.getCharm().getType().equalsIgnoreCase(charm.getType()) ||
							instance.getCharm().getName().equalsIgnoreCase(charm.getName())) {
								hasCharm = true;
								break;
							}
				}
				if (!hasCharm) {
					charmInstances.add(charm.createInstance());
				}
			}
		}
		return stack;
	}

	/**
	 * 
	 * @author Mark Gottschling on May 1, 2020
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<CharmRandomly> {
		public Serializer() {
			super(new ResourceLocation("treasure2:charm_randomly"), CharmRandomly.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, CharmRandomly value, JsonSerializationContext context) {
            if (!value.charms.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (ICharm charm : value.charms) {
                    jsonArray.add(new JsonPrimitive(charm.getName()));
                }
                json.add("charms", jsonArray);
            }
		}

		/**
		 * 
		 */
		public CharmRandomly deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			Map<String, ICharm> charmsByType = new HashMap<>(10);
			List<ICharm> list = Lists.<ICharm>newArrayList();

			if (json.has("charms")) {
				for (JsonElement element : JsonUtils.getJsonArray(json, "charms")) {
					String charmName = JsonUtils.getString(element, "charm");
					ICharm charm = TreasureCharms.REGISTRY.get(charmName);

					if (charm == null) {
						Treasure.logger.warn("Unknown charm '{}'", charmName);
					}

					// add to the map to prevent duplicates of same charm
					if (!charmsByType.containsKey(charm.getType())) {
						charmsByType.put(charm.getType(), charm);
						// add to the list of charms
						list.add(charm);
					}
				}
			}
			return new CharmRandomly(conditionsIn, list);
		}
	}
}
