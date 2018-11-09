package com.someguyssoftware.treasure2.loot.conditions;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.someguyssoftware.treasure2.loot.TreasureLootContext;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.RandomValueRange;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2018
 *
 */
public class EntityHasScore implements TreasureLootCondition {
	private final Map<String, RandomValueRange> scores;
	private final TreasureLootContext.EntityTarget target;

	public EntityHasScore(Map<String, RandomValueRange> scoreIn, TreasureLootContext.EntityTarget targetIn) {
		this.scores = scoreIn;
		this.target = targetIn;
	}

	public boolean testCondition(Random rand, TreasureLootContext context) {
		Entity entity = context.getEntity(this.target);

		if (entity == null) {
			return false;
		} else {
			Scoreboard scoreboard = entity.world.getScoreboard();

			for (Entry<String, RandomValueRange> entry : this.scores.entrySet()) {
				if (!this.entityScoreMatch(entity, scoreboard, entry.getKey(), entry.getValue())) {
					return false;
				}
			}

			return true;
		}
	}

	protected boolean entityScoreMatch(Entity entityIn, Scoreboard scoreboardIn, String objectiveStr, RandomValueRange rand) {
		ScoreObjective scoreobjective = scoreboardIn.getObjective(objectiveStr);

		if (scoreobjective == null) {
			return false;
		} else {
			String s = entityIn instanceof EntityPlayerMP ? entityIn.getName() : entityIn.getCachedUniqueIdString();
			return !scoreboardIn.entityHasObjective(s, scoreobjective) ? false : rand.isInRange(scoreboardIn.getOrCreateScore(s, scoreobjective).getScorePoints());
		}
	}

	public static class Serializer extends TreasureLootCondition.Serializer<EntityHasScore> {
		protected Serializer() {
			super(new ResourceLocation("entity_scores"), EntityHasScore.class);
		}

		public void serialize(JsonObject json, EntityHasScore value, JsonSerializationContext context) {
			JsonObject jsonobject = new JsonObject();

			for (Entry<String, RandomValueRange> entry : value.scores.entrySet()) {
				jsonobject.add(entry.getKey(), context.serialize(entry.getValue()));
			}

			json.add("scores", jsonobject);
			json.add("entity", context.serialize(value.target));
		}

		public EntityHasScore deserialize(JsonObject json, JsonDeserializationContext context) {
			Set<Entry<String, JsonElement>> set = JsonUtils.getJsonObject(json, "scores").entrySet();
			Map<String, RandomValueRange> map = Maps.<String, RandomValueRange>newLinkedHashMap();

			for (Entry<String, JsonElement> entry : set) {
				map.put(entry.getKey(), JsonUtils.deserializeClass(entry.getValue(), "score", context, RandomValueRange.class));
			}

			return new EntityHasScore(map, (TreasureLootContext.EntityTarget) JsonUtils.deserializeClass(json, "entity", context, TreasureLootContext.EntityTarget.class));
		}
	}
}