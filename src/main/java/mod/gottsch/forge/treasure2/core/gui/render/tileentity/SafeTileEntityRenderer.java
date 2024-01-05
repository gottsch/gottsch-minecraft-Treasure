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
package mod.gottsch.forge.treasure2.core.gui.render.tileentity;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.gui.model.SafeModel;
import mod.gottsch.forge.treasure2.core.gui.model.WitherChestModel;
import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import mod.gottsch.forge.treasure2.core.tileentity.SafeTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class SafeTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public SafeTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/safe.png"));
		setModel(new SafeModel());
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		SafeTileEntity ste = (SafeTileEntity) tileEntity;

		if (ste.isLidClosed) {
			float handleRotation = ste.prevHandleAngle + (ste.handleAngle - ste.prevHandleAngle) * partialTicks;
			handleRotation = 1.0F - handleRotation;
			handleRotation = 1.0F - handleRotation * handleRotation * handleRotation;
			((SafeModel)getModel()).getHandleA1().zRot = (handleRotation * (float)Math.PI / getAngleModifier());
		}
		else {
			// render handleB rotating around y-axis
		}
		float lidRotation = ste.prevLidAngle + (ste.lidAngle - ste.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().yRot = (lidRotation * (float)Math.PI / getAngleModifier());
	}

	@Override
	public float getLockScaleModifier() {
		return 0.3F;
	}
}
