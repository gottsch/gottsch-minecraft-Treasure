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
import com.someguyssoftware.treasure2.item.charm.CharmState;
import com.someguyssoftware.treasure2.item.charm.CharmStateFactory;
import com.someguyssoftware.treasure2.item.charm.CharmType;
import com.someguyssoftware.treasure2.item.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmState;
import com.someguyssoftware.treasure2.item.charm.TreasureCharms;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * 
 * @author Mark Gottschling on May 2, 2020
 *
 */
public class SetCharms extends LootFunction {
	private List<ICharm> charms;

	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public SetCharms(LootCondition[] conditions, @Nullable List<ICharm> charms) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {

		// ensure that the stack has charm capabilities
		if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmCapability provider = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmState> charmStates = provider.getCharmStates();

			if (!this.charms.isEmpty()) {
				for (ICharm charm : charms) {
					// ensure that the item doesn't already have the same charm or same type or exceeded the maximum charms.
					boolean hasCharm = false;
					for (ICharmState state : charmStates) {
						if (state.getCharm().getCharmType() == charm.getCharmType() ||
								state.getCharm().getName().equals(charm.getName())) {
							Treasure.logger.debug("item already has charm -> {}", charm.getName());
							hasCharm = true;
							break;
						}
					}
					if (!hasCharm) {
						Treasure.logger.debug("giving item charm -> {}", charm.getName());
						charmStates.add(CharmStateFactory.createCharmState(charm));
					}
				}
			}
			else {
				// randomly add a charm (in case loot table is misconfigured)
				List<ICharm> tempCharms = new ArrayList<>();
				// if charms list is empty, create a default list of minor charms
				for (ICharm c : TreasureCharms.REGISTRY.values()) {
					if (c.getCharmLevel() == CharmLevel.LEVEL1 || c.getCharmLevel() == CharmLevel.LEVEL2) {
						tempCharms.add(c);
					}
				}
				if (!tempCharms.isEmpty()) {
					// select a charm randomly					
					ICharm charm = tempCharms.get(rand.nextInt(tempCharms.size()));
					Treasure.logger.debug("giving item a random charm -> {}", charm.getName());
					charmStates.add(CharmStateFactory.createCharmState(charm));
				}
			}
		}
		return stack;
	}

	/**
	 * 
	 * @author Mark Gottschling on May 2, 2020
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<SetCharms> {
		public Serializer() {
			super(new ResourceLocation("treasure2:set_charms"), SetCharms.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, SetCharms value, JsonSerializationContext context) {
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
		public SetCharms deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			Map<CharmType, ICharm> charmsByType = new HashMap<>(10);
			List<ICharm> list = Lists.<ICharm>newArrayList();

			if (json.has("charms")) {
				for (JsonElement element : JsonUtils.getJsonArray(json, "charms")) {
					String charmName = JsonUtils.getString(element, "charm");
					ICharm charm = TreasureCharms.REGISTRY.get(charmName);

					if (charm == null) {
						Treasure.logger.warn("Unknown charm '{}'", charmName);
						System.out.println("Unknown charm '" + charmName + "'");
					}

					// add to the map
					if (!charmsByType.containsKey(charm.getCharmType())) {
						charmsByType.put(charm.getCharmType(), charm);
						// add to the list of charms
						list.add(charm);
						Treasure.logger.debug("adding charm to charm list -> {}", charm.getName());
					}
				}
			}
			return new SetCharms(conditionsIn, list);
		}
	}
}
