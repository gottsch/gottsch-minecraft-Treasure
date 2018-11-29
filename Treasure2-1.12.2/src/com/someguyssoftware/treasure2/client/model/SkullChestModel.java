package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Nov 28, 2018
 *
 */
public class SkullChestModel extends ModelBase implements ITreasureChestModel {
	// fields
	ModelRenderer top;
	ModelRenderer head;
	ModelRenderer jaw;
	ModelRenderer jawBottom;

	/**
	 * 
	 */
	public SkullChestModel() {
		textureWidth = 64;
		textureHeight = 64;

		top = new ModelRenderer(this, 0, 0);
		top.addBox(-3F, -6F, -5F, 6, 1, 6);
		top.setRotationPoint(0F, 21F, 2F);
		top.setTextureSize(64, 64);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		head = new ModelRenderer(this, 0, 8);
		head.addBox(-4F, -5F, -6F, 8, 5, 8);
		head.setRotationPoint(0F, 21F, 2F);
		head.setTextureSize(64, 64);
		head.mirror = true;
		setRotation(head, 0F, 0F, 0F);
		jaw = new ModelRenderer(this, 0, 22);
		jaw.addBox(-3F, 0F, -6F, 6, 1, 6);
		jaw.setRotationPoint(0F, 21F, 2F);
		jaw.setTextureSize(64, 64);
		jaw.mirror = true;
		setRotation(jaw, 0F, 0F, 0F);
		jawBottom = new ModelRenderer(this, 0, 30);
		jawBottom.addBox(-3F, 0F, -5F, 6, 2, 5);
		jawBottom.setRotationPoint(0F, 22F, 1F);
		jawBottom.setTextureSize(64, 64);
		jawBottom.mirror = true;
		setRotation(jawBottom, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		top.render(f5);
		head.render(f5);
		jaw.render(f5);
		jawBottom.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(AbstractTreasureChestTileEntity te) {
		head.rotateAngleX = top.rotateAngleX;
		jaw.rotateAngleX = top.rotateAngleX;
		
		// render all the parts
		top.render(0.0625F);
		head.render(0.0625F);
		jaw.render(0.0625F);
		jawBottom.render(0.0625F);
	}

	@Override
	public ModelRenderer getLid() {
		return top;
	}

	public void setLid(ModelRenderer lid) {
		this.top = lid;
	}
}
