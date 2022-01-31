/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.charm.cost;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.runestone.QualityRunestone;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Jan 21, 2022
 *
 */
public class QualityRuneCostReducerEvaluator implements ICostEvaluator {
	private ICostEvaluator evaluator;
	public QualityRuneCostReducerEvaluator() {
		evaluator = new CostEvaluator();
	}
	
	public double apply(World world, Random random, ICoords coords, EntityPlayer player, Event event, ICharmEntity entity, double cost) {
		return evaluator.apply(world, random, coords, player, event, entity, Math.max(1D, cost / QualityRunestone.MULTIPLIER));
	};
}
