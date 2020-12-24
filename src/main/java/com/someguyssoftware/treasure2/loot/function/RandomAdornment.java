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
import com.someguyssoftware.treasure2.item.IAdornment;
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
 * @author Mark Gottschling on Dec 23, 2020
 * @param <ICharmType>
 *
 */
// TODO totally wrong!! can't random select adornment - that is the item.
// TODO break this into separate functions setAdornmentSlots() with random ability
// TODO levels could possibly move to CharmRandomly
public class RandomAdornment extends LootFunction {
    private List<IAdornment> adornments;
    private List<Integer> levels;
    private List<Integer> slots;
    private List<ICharm> charms;

	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
    public RandomAdornment(LootCondition[] conditions, @Nullable List<IAdornment> adornments, @Nullable List<Integer> levels, 
        @Nullable List<Integer> slots, @Nullable List<ICharm> charms) {
        super(conditions);
        this.adornments = adornments == null ? Collections.emptyList() : adornments;
        this.levels = levels == null ? Collections.emptyList() : levels;
        this.slots = slots == null ? Collections.emptyList() : slots;
		this.charms = charms == null ? Collections.emptyList() : charms;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        IAdornment adornment = null;
        ICharm charm = null;

		// ensure that the stack has charmable capabilities
		if (stack.hasCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null)) {
			ICharmableCapability capability = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmInstance> charmInstances = capability.getCharmInstances();

            // process adornments ie select one

			if (this.charms.isEmpty()) {
				List<ICharm> tempCharms = new ArrayList<>();
				// if charms list is empty, create a default list of minor charms
				for (ICharm c : TreasureCharmRegistry.values()) {
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
				Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
			}
			if (charm != null) {
				// ensure that the item doesn't already have the same charm or same type or exceeded the maximum charms.
				boolean hasCharm = false;
				for (ICharmInstance instance : charmInstances) {
					if (instance.getCharm().getType().equalsIgnoreCase(charm.getType()) ||
							instance.getCharm().getName().equals(charm.getName())) {
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
	 * @author Mark Gottschling on Dec 23, 2020
	 *
	 */
	public static class Serializer extends LootFunction.Serializer<RandomAdornment> {
		public Serializer() {
			super(new ResourceLocation("treasure2:random_adornment"), RandomAdornment.class);
		}

		/**
		 * 
		 */
		public void serialize(JsonObject json, RandomAdornment value, JsonSerializationContext context) {
            // write adornments
            if (!value.adornments.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (IAdornment adornment: value.adornments) {
                    jsonArray.add(new JsonPrimitive(adornment.getRegistryName()));
                }
                json.add("adornments", jsonArray);
            }

            // write levels
            if (!value.levels.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (Integer level : value.levels) {
                    jsonArray.add(new JsonPrimitive(level));
                }
                json.add("levels", jsonArray);
            }

            // write slots
            if (!value.slots.isEmpty()) {
                JsonArray jsonArray = new JsonArray();
                for (Integer slot : value.slots) {
                    jsonArray.add(new JsonPrimitive(slot));
                }
                json.add("slots", jsonArray);
            }

            // write charms
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
		public RandomAdornment deserialize(JsonObject json, JsonDeserializationContext deserializationContext,
				LootCondition[] conditionsIn) {
			Map<String, ICharm> charmsByType = new HashMap<>(10);
			
            List<IAdornment> adornments = Lists.<IAdornment>newArrayList();
            List<Integer> levels = Lists.<Integer>newArrayList();
            List<Integer> slots = Lists.<Integer>newArrayList();
            List<ICharm> list = Lists.<ICharm>newArrayList();

            if (json.has("adornments")) {
                for (JsonElement element : JsonUtils.getJsonArray(json, "adornments")) {
                    Spring name = JsonUtils.getString(element, "adornment");
                    // TODO get adornment from registry or AdornmentRegistry ?
                    Optional<IAdornment> adornment = ItemRegistry.get(ResourceLocationUtil.create(name));
                    if (!adornment.isPresent()) {
                        Treasure.logger.warn("Unknown adornment '{}'", name);
                    }
                    adornments.add(adornment.get());
                }
            }

            if (json.has("levels")) {
                for (JsonElement element : JsonUtils.getJsonArray(json, "levels")) {
                    Integer level = JsonUtils.getInteger(element, 1);
                    levels.add(level);
                }
            }

            if (json.has("slots")) {
                for (JsonElement element : JsonUtils.getJsonArray(json, "slots")) {
                    Integer slot = JsonUtils.getInteger(element, 1);
                    slots.add(slot);
                }
            }

			if (json.has("charms")) {
				for (JsonElement element : JsonUtils.getJsonArray(json, "charms")) {
					String charmName = JsonUtils.getString(element, "charm");
					Optional<ICharm> charm = TreasureCharmRegistry.get(ResourceLocationUtil.create(charmName));

					if (!charm.isPresent()) {
						Treasure.logger.warn("Unknown charm '{}'", charmName);
					}

					// add to the map to prevent duplicates of same charm
					if (!charmsByType.containsKey(charm.get().getType())) {
						charmsByType.put(charm.get().getType(), charm.get());
						// add to the list of charms
						list.add(charm.get());
					}
				}
			}
			return new RandomAdornment(conditionsIn, list);
		}
	}
}
