/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.loot.ILootGenerator;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootGenerators;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * 
 * @author Mark Gottschling Jun 12, 2023
 *
 */
public class TreasureLootModifier extends LootModifier {

    public static final Supplier<Codec<TreasureLootModifier>> CODEC = Suppliers.memoize(()
            -> RecordCodecBuilder.create(inst -> codecStart(inst)            		
            		.and(Codec.INT.fieldOf("count").forGetter(m -> m.count))
            		.and(ResourceLocation.CODEC.fieldOf("rarity").forGetter(m -> m.rarityName))
            		.and(Codec.DOUBLE.fieldOf("chance").forGetter(m -> m.chance))
            		.apply(inst, TreasureLootModifier::new)));

	// the number of items to add
	private final int count;
	private final ResourceLocation rarityName;
	private final double chance;
    
	protected TreasureLootModifier(LootItemCondition[] conditionsIn, int count, ResourceLocation rarityName, double chance) {
		super(conditionsIn);
		this.count = count;
		this.rarityName = rarityName;
		this.chance = chance;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
			LootContext context) {

//		IRarity rarity = TreasureApi.getRarity(this.rarity).orElse(Rarity.NONE);
		IRarity rarity = TreasureRarities.getRarityByName(this.rarityName).orElseGet(() -> TreasureRarities.UNKNOWN.get());

		if (Config.SERVER.wealth.enableVanillaLootModifiers.get()
			&& context.getRandom().nextDouble() < chance) {
//				&& RandomHelper.checkProbability(context.getLevel().getRandom(), chance * 100)) {
			Vec3 vec3 = context.getParam(LootContextParams.ORIGIN);
			// use this to supple to the LootGenerator
			ILootGenerator lootGenerator = TreasureLootGenerators.GLOBAL_MODIFIER;
			Pair<List<ItemStack>, List<ItemStack>> lootStacks = lootGenerator.generateLoot(
					context.getLevel(), 
					context.getLevel().getRandom(), 
//					LootTableType.CHESTS, // probably will need TreasureLootTableTypes here
					TreasureLootTableTypes.CHESTS.get(),
					rarity, 
					null, 
					new Coords(vec3));
			
			// grab the loot from the treasure pool stack
			for (int index = 0; index < Math.min(count, lootStacks.getLeft().size()); index++) {
				ItemStack outputStack = lootStacks.getLeft().get(index);
				generatedLoot.add(outputStack);
			}
		}
		return generatedLoot;
	}
}
