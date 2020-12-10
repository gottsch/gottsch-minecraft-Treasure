package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer base;
	private final ModelRenderer rightFlap;
	private final ModelRenderer leftFlap;
	private final ModelRenderer southFlap;
	private final ModelRenderer northFlap;

	public CardboardBoxModel() {
		textureWidth = 128;
		textureHeight = 128;

		base = new ModelRenderer(this);
		base.setRotationPoint(7.0F, 10.0F, -7.0F);
		base.cubeList.add(new ModelBox(base, 0, 0, -14.0F, 0.0F, 0.0F, 14, 14, 14, 0.0F, false));

		rightFlap = new ModelRenderer(this);
		rightFlap.setRotationPoint(7.0F, 10.0F, 0.0F);
		rightFlap.cubeList.add(new ModelBox(rightFlap, 28, 29, -7.0F, -1.0F, -7.0F, 7, 1, 14, 0.0F, false));

		leftFlap = new ModelRenderer(this);
		leftFlap.setRotationPoint(-7.0F, 10.0F, 0.0F);
		leftFlap.cubeList.add(new ModelBox(leftFlap, 0, 28, 0.0F, -1.0F, -7.0F, 7, 1, 14, 0.0F, false));

		southFlap = new ModelRenderer(this);
		southFlap.setRotationPoint(0.0F, 10.0F, 7.0F);
		southFlap.cubeList.add(new ModelBox(southFlap, 42, 7, -7.0F, -0.999F, -6.001F, 14, 1, 6, 0.0F, false));

		northFlap = new ModelRenderer(this);
		northFlap.setRotationPoint(0.0F, 10.0F, -7.0F);
		northFlap.cubeList.add(new ModelBox(northFlap, 42, 0, -7.0F, -0.999F, 0.001F, 14, 1, 6, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		base.render(f5);
		rightFlap.render(f5);
		leftFlap.render(f5);
		southFlap.render(f5);
		northFlap.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		float angle = 0.0625F;
		
		// set the angles of the latch to same as the lib
		rightFlap.rotateAngleZ = -leftFlap.rotateAngleZ;
		southFlap.rotateAngleX = -northFlap.rotateAngleX;
		
		base.render(angle);
		rightFlap.render(angle);
		leftFlap.render(angle);
		southFlap.render(angle);
		northFlap.render(angle);		
	}

	@Override
	public ModelRenderer getLid() {
		return leftFlap;
	}
	
	public ModelRenderer getInnerLid() {
		return northFlap;
	}
}