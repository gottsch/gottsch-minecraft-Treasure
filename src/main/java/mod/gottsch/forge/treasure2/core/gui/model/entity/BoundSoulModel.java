
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
 */package mod.gottsch.forge.treasure2.core.gui.model.entity;

import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoulEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;

public class BoundSoulModel extends BipedModel<BoundSoulEntity>{

	/**
	 * 
	 */
	public BoundSoulModel() {
		super(0, 0, 64, 64);
	}

	@Override
	public void setupAnim(BoundSoulEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
		super.setupAnim(p_225597_1_, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
		ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(p_225597_1_), this.attackTime, p_225597_4_);
	}

	public boolean isAggressive(BoundSoulEntity p_212850_1_) {
		return p_212850_1_.isAggressive();
	}
}
