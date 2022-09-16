package com.someguyssoftware.treasure2.charm.cost;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

/*
 * Generic cost evaluator
 * @param amount cost requested
 * @return actual cost incurred
 */
public class CostEvaluator implements ICostEvaluator {
	@Override
	public double apply(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity, double amount) {
		
		double cost = 0;
		if (entity.getMana() >= amount) {
			cost = amount;
			entity.setMana(MathHelper.clamp(entity.getMana() - amount, 0D, entity.getMana()));
		}
		else {
			cost = entity.getMana();
			entity.setMana(0);
		}
		return cost;
	}
	
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		ICostEvaluator.super.save(nbt);
		try {
			nbt.putString("costClass", getClass().getCanonicalName());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void load(CompoundNBT nbt) {
		ICostEvaluator.super.load(nbt);
		// NOTE don't load "class" here. the instance is already created.
	}
}
