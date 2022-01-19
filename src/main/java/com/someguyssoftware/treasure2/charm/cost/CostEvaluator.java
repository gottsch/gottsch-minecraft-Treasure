package com.someguyssoftware.treasure2.charm.cost;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/*
 * Generic cost evaluator
 * @param amount cost requested
 * @return actual cost incurred
 */
public class CostEvaluator implements ICostEvaluator {
	@Override
	public double apply(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity, double amount) {
		
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
}
