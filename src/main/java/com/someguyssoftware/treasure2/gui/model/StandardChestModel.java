package com.someguyssoftware.treasure2.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2018
 *
 */
public class StandardChestModel extends AbstractTreasureChestModel {

	//fields
	ModelRenderer base;
	ModelRenderer lid;
    ModelRenderer padTop;
    ModelRenderer padBottom;
    ModelRenderer Latch1;
    ModelRenderer Latch2;
    ModelRenderer Latch3;
    ModelRenderer hingeBottom1;
    ModelRenderer hingeBottom2;
    
	public StandardChestModel() {
		super(RenderType::entityCutout);

		texWidth = 128;
		texHeight = 64;

		base = new ModelRenderer(this, 0, 21);
		base.addBox(-7F, 0F, -14F, 14, 10, 14);
		base.setPos(0F, 14F, 7F);
		base.setTexSize(128, 64);
		base.mirror = true;

	      lid = new ModelRenderer(this, 0, 0);
	      lid.addBox(-7F, -5F, -14F, 14, 5, 14);
	      lid.setPos(0F, 15F, 7F);
	      lid.setTexSize(128, 64);
	      lid.mirror = true;

	      padTop = new ModelRenderer(this, 18, 47);
	      padTop.addBox(-2F, -3F, -14.2F, 4, 3, 1);
	      padTop.setPos(0F, 15F, 7F);
	      padTop.setTexSize(128, 64);
	      padTop.mirror = true;

	      padBottom = new ModelRenderer(this, 7, 47);
	      padBottom.addBox(-2F, 0F, -1.2F, 4, 3, 1);
	      padBottom.setPos(0F, 15F, -6F);
	      padBottom.setTexSize(128,64);
	      padBottom.mirror = true;

	      Latch1 = new ModelRenderer(this, 0, 47);
	      Latch1.addBox(-1F, -2F, -15F, 2, 4, 1);
	      Latch1.setPos(0F, 15F, 7F);
	      Latch1.setTexSize(128, 64);
	      Latch1.mirror = true;

	      Latch2 = new ModelRenderer(this, 0, 47);
	      Latch2.addBox(0F, -2F, -8F, 1, 4, 2);
	      Latch2.setPos(7F, 15F, 7F);
	      Latch2.setTexSize(128, 64);
	      Latch2.mirror = true;

	      Latch3 = new ModelRenderer(this, 0, 47);
	      Latch3.addBox(-1F, -2F, -8F, 1, 4, 2);
	      Latch3.setPos(-7F, 15F, 7F);
	      Latch3.setTexSize(128, 64);
	      Latch3.mirror = true;

	      hingeBottom1 = new ModelRenderer(this, 29, 47);
	      hingeBottom1.addBox(-1F, 0F, -0.8F, 2, 2, 1);
	      hingeBottom1.setPos(3F, 14F, 7F);
	      hingeBottom1.setTexSize(128, 64);
	      hingeBottom1.mirror = true;

	      hingeBottom2 = new ModelRenderer(this, 29, 47);
	      hingeBottom2.addBox(-1F, -1F, -0.8F, 2, 2, 1);
	      hingeBottom2.setPos(-3F, 15F, 7F);
	      hingeBottom2.setTexSize(128, 64);
	      hingeBottom2.mirror = true;

	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		// set the angles of the latch to same as the lid
		Latch1.xRot = lid.xRot;
		Latch2.xRot = lid.xRot;
		Latch3.xRot = lid.xRot;
		padTop.xRot = lid.xRot;
		
		base.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		padTop.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		padBottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		hingeBottom1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		hingeBottom2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		
		Latch1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		for (LockState state : te.getLockStates()) {
			if (state.getLock() != null) {
				switch(state.getSlot().getIndex()) {
					case 1:
						Latch3.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
						break;
					case 2:				
						Latch2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
						break;
				}
			}
		}
	}

	/**
	 * @return the base
	 */
	public ModelRenderer getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(ModelRenderer base) {
		this.base = base;
	}

	/**
	 * @return the lid
	 */
	@Override
	public ModelRenderer getLid() {
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(ModelRenderer lid) {
		this.lid = lid;
	}

}
