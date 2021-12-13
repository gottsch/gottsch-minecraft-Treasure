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
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.gui.model.WitherChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestBlockEntity;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class WitherChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public WitherChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/wither-chest.png"));
		setModel(new WitherChestModel());
	}
	
	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
        ((WitherChestModel)getModel()).getRightFrontDoor().yRot = -(lidRotation * (float)Math.PI / getAngleModifier());  
	}
}
