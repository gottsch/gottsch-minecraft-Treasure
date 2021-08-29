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
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.CardboardBoxModel;
import com.someguyssoftware.treasure2.gui.model.CauldronChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CardboardBoxTileEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class CardboardBoxTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public CardboardBoxTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/cardboard-box.png"));
		setModel(new CardboardBoxModel());
	}
	
	@Override
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		CardboardBoxTileEntity cte = (CardboardBoxTileEntity) tileEntity;
		
		// update in the inner lid
		float innerLidRotation = cte.prevInnerLidAngle + (cte.innerLidAngle - cte.prevInnerLidAngle) * partialTicks;
		innerLidRotation = 1.0F - innerLidRotation;
		innerLidRotation = 1.0F - innerLidRotation * innerLidRotation * innerLidRotation;
		((CardboardBoxModel)getModel()).getInnerLid().xRot = (innerLidRotation * (float) Math.PI / getAngleModifier()); // not negated
		
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		getModel().getLid().zRot = -(lidRotation * (float) Math.PI / getAngleModifier());
	}
	
	@Override
	public float getAngleModifier() {
		return 0.8F;
	}

	@Override
	public void updateLockRotation(MatrixStack matrixStack, LockState lockState) {
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(90)); // NOTE changed from Y to X axis
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(lockState.getSlot().getRotation()));  // NOTE now Z axis is the Y axis since we rotated on the X axis first.	
	}

	@Override
	public float getLockScaleModifier() {
		return 0.35F;
	}
}
