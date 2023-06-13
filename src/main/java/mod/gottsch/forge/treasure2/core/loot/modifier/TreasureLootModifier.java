/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.loot.modifier;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.ThreadContext.ContextStack;

import com.google.gson.JsonObject;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.loot.ILootGenerator;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

/**
 * 
 * @author Mark Gottschling Jun 12, 2023
 *
 */
public class TreasureLootModifier extends LootModifier {

	// the number of items to add
	private final int count;
	private final IRarity rarity;
	private final double chance;

	protected TreasureLootModifier(LootItemCondition[] conditionsIn, int count, IRarity rarity, double chance) {
		super(conditionsIn);
		this.count = count;
		this.rarity = rarity;
		this.chance = chance;
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

		if (Config.SERVER.wealth.enableVanillaLootModifiers.get() && RandomHelper.checkProbability(context.getLevel().getRandom(), chance * 100)) {
			Vec3 vec3 = context.getParam(LootContextParams.ORIGIN);
			// use this to supple to the LootGenerator
			ILootGenerator lootGenerator = TreasureLootGenerators.GLOBAL_MODIFIER;
			Pair<List<ItemStack>, List<ItemStack>> lootStacks = lootGenerator.generateLoot(
					context.getLevel(), 
					context.getLevel().getRandom(), 
					LootTableType.CHESTS, 
					rarity, 
					null, 
					new Coords(vec3));
			
			// grab the loot from the treasure pool stack
			for (int index = 0; index < Math.min(count, lootStacks.getLeft().size()); index++) {
				ItemStack outputStack = lootStacks.getLeft().get(index);
				generatedLoot.add(outputStack);
			}
			
			// select one item from the generated loot and return
//			ItemStack resultStack = generatedLoot.get(context.getLevel().getRandom().nextInt(generatedLoot.size()));
//			generatedLoot.add(resultStack);
		}
		return generatedLoot;
	}

	/*
	 * 
	 */
	public static class Serializer extends GlobalLootModifierSerializer<TreasureLootModifier> {

		@Override
		public TreasureLootModifier read(ResourceLocation location, JsonObject object,	LootItemCondition[] conditions) {
			int count = GsonHelper.getAsInt(object, "count");
			String rarityStr = GsonHelper.getAsString(object, "rarity");
			IRarity rarity = TreasureApi.getRarity(rarityStr).orElse(Rarity.COMMON);
			double chance = GsonHelper.getAsDouble(object, "chance");
			
			return new TreasureLootModifier(conditions, count, rarity, chance);
		}

		@Override
		public JsonObject write(TreasureLootModifier instance) {
			JsonObject json = makeConditions(instance.conditions);
			json.addProperty("count", Integer.valueOf(instance.count));
			json.addProperty("rarity", instance.rarity.getName());
			json.addProperty("chance", Double.valueOf(instance.chance));
			return json;
		}

	}
}
