package com.someguyssoftware.treasure2.charm.cost;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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
	
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		ICostEvaluator.super.save(nbt);
		try {
			nbt.setString("costClass", getClass().getCanonicalName());
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void load(NBTTagCompound nbt) {
		ICostEvaluator.super.load(nbt);
		// NOTE don't load "class" here. the instance is already created.
	}
}
