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
import com.someguyssoftware.treasure2.capability.CharmableCapabilityProvider;
import com.someguyssoftware.treasure2.capability.ICharmCapability;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.TreasureCharmRegistry;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.item.charm.CharmLevel;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;
import com.someguyssoftware.treasure2.item.charm.ICharmable;
import com.someguyssoftware.treasure2.item.charm.ICharmed;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

/**
 * @author Mark Gottschling on May 1, 2020
 * @param <ICharmType>
 *
 */
public class CharmRandomly extends LootFunction {
	private List<ICharm> charms;
	private RandomValueRange levels;

	/**
	 * 
	 * @param conditions
	 * @param charms
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
	}
	
	/**
	 * 
	 * @param conditions
	 * @param charms
	 * @param range
	 */
	public CharmRandomly(LootCondition[] conditions, @Nullable List<ICharm> charms, RandomValueRange levels) {
		super(conditions);
		this.charms = charms == null ? Collections.emptyList() : charms;
		this.levels = levels;
	}
	

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		ICharm charm = null;
		Treasure.logger.debug("selected item from charm pool -> {}", stack.getDisplayName());
		// ensure that the stack has charm capabilities
		ICharmCapability charmCap = null;
		if (stack.getItem() instanceof ICharmed) {
			Treasure.logger.debug("is an ICharmed");
			charmCap = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
		}
		else if (stack.getItem() instanceof ICharmable) {
			Treasure.logger.debug("is an ICharmable");
			charmCap = stack.getCapability(CharmableCapabilityProvider.CHARM_CAPABILITY, null);
		}
		if (charmCap != null) {
			Treasure.logger.debug("has charm cap");
//			provider = stack.getCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null);
			List<ICharmInstance> charmInstances = charmCap.getCharmInstances();
			List<ICharm> tempCharms = new ArrayList<>();
			
			if (this.charms.isEmpty()) {			
				// check the levels property
				if(levels != null) {
					int level = this.levels.generateInt(rand);
					// get all the charms from level
					Optional<List<ICharm>> levelCharms = TreasureCharmRegistry.get(level);
					if (levelCharms.isPresent()) {
						tempCharms.addAll(levelCharms.get());
					}
				}
				else {
					// if charms list is empty, create a default list of minor charms
					for (ICharm c : TreasureCharmRegistry.values()) {
						if (c.getLevel() == CharmLevel.LEVEL1.getValue() || c.getLevel() == CharmLevel.LEVEL2.getValue()) {
							tempCharms.add(c);
						}
					}
				}
				Treasure.logger.debug("temp charms size -> {}", tempCharms.size());
				if (!tempCharms.isEmpty()) {
					// select a charm randomly
					charm = tempCharms.get(rand.nextInt(tempCharms.size()));
					Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
				}
			}
			else {
				// check the levels property
				if(levels != null) {
					int level = this.levels.generateInt(rand);
					// get all the charms from level
					Optional<List<ICharm>> levelCharms = TreasureCharmRegistry.get(level);
					if (levelCharms.isPresent()) {
						tempCharms.addAll(levelCharms.get());
					}
				}
				// add the listed charms to the temp list
				tempCharms.addAll(charms);
				
				// select a charm randomly
				charm =tempCharms.get(rand.nextInt(tempCharms.size()));
				Treasure.logger.debug("selected charm for item -> {}", charm.getName().toString());
			}
			if (charm != null) {
				Treasure.logger.debug("charm is not null -> {}", charm.getName());
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
					Treasure.logger.debug("adding charm to charm instances.");
					charmInstances.add(charm.createInstance());
				}
			}
		}
		Treasure.logger.debug("returning charmed item -> {}", stack.getDisplayName());
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
                    jsonArray.add(new JsonPrimitive(charm.getName().toString()));
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
			
			RandomValueRange range = null;
			if (json.has("levels")) {
				 range = JsonUtils.deserializeClass(json, "levels", deserializationContext, RandomValueRange.class);	
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
			return new CharmRandomly(conditionsIn, list, range);
		}
	}
}
