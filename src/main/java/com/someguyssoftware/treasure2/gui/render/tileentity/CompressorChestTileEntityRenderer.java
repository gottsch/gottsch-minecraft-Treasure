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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.CompressorChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestBlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class CompressorChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public CompressorChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/compressor-chest.png"));
		setModel(new CompressorChestModel());
	}

	@Override
	public void updateScale(MatrixStack matrixStack) {
		// shrink the size of the chest by half
		matrixStack.scale(0.5F, 0.5F, 0.5F);
	}

	@Override
	public void updateTranslation(MatrixStack matrixStack) {
		final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 0.75, 0.5);
		matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z);
	}

	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().xRot = -(lidRotation * (float)Math.PI / 2.0F);  
	}

	@Override
	public float getLockScaleModifier() {
		return 0.20F;
	}
}
