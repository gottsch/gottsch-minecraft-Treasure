/**
 * 
 */
package com.someguyssoftware.treasure2.loot.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.someguyssoftware.treasure2.item.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.item.charm.TreasureCharms;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

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
        ICharmCapability charmCap = null;
		if (stack.getItem() instanceof ICharmed) {
			charmCap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
		}
		else if (stack.getItem() instanceof ICharmable) {
			charmCap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
        }
        
        if (charmCap != null) {
		// if (stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
		// 	ICharmCapability provider = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmInstance> charmInstances = charmCap.getCharmInstances();

			if (!this.charms.isEmpty()) {
				for (ICharm charm : charms) {
					// ensure that the item doesn't already have the same charm or same type or exceeded the maximum charms.
					boolean hasCharm = false;
					for (ICharmInstance instance : charmInstances) {
						if (instance.getCharm().getType().equalsIgnoreCase(charm.getType()) ||
								instance.getCharm().getName().equals(charm.getName())) {
							Treasure.logger.debug("item already has charm -> {}", charm.getName());
							hasCharm = true;
							break;
						}
					}
					if (!hasCharm) {
						Treasure.logger.debug("giving item charm -> {}", charm.getName());
						charmInstances.add(charm.createInstance());
					}
				}
			}
			else {
				// randomly add a charm (in case loot table is misconfigured)
				List<ICharm> tempCharms = new ArrayList<>();
				// if charms list is empty, create a default list of minor charms
				for (ICharm c : TreasureCharmRegistry.values()) {
					if (c.getLevel() == CharmLevel.LEVEL1.getValue() || c.getLevel() == CharmLevel.LEVEL2.getValue()) {
						tempCharms.add(c);
					}
				}
				if (!tempCharms.isEmpty()) {
					// select a charm randomly					
					ICharm charm = tempCharms.get(rand.nextInt(tempCharms.size()));
					Treasure.logger.debug("giving item a random charm -> {}", charm.getName());
					charmInstances.add(charm.createInstance());
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
					jsonArray.add(new JsonPrimitive(charm.getName().toString()));
				}
				json.add("charms", jsonArray);
			}
		}

		/**
		 * 
		 */
		public SetCharms deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			Map<String, ICharm> charmsByType = new HashMap<>(10);
			List<ICharm> list = Lists.<ICharm>newArrayList();

			if (json.has("charms")) {
				for (JsonElement element : JsonUtils.getJsonArray(json, "charms")) {
					String charmName = JsonUtils.getString(element, "charm");
					Optional<ICharm> charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));

					if (!charm.isPresent()) {
						Treasure.logger.warn("Unknown charm '{}'", charmName);
						System.out.println("Unknown charm '" + charmName + "'");
					}

					// add to the map
					if (!charmsByType.containsKey(charm.get().getType())) {
						charmsByType.put(charm.get().getType(), charm.get());
						// add to the list of charms
						list.add(charm.get());
						Treasure.logger.debug("adding charm to charm list -> {}", charm.get().getName());
					}
				}
			}
			return new SetCharms(conditionsIn, list);
		}
	}
}
