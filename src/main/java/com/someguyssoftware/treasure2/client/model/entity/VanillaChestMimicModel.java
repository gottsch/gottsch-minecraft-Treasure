package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * The vanilla chest mimic, ported from the 1.20.1 VanillaChestMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VanillaChestMimicModel extends MimicModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer eye1;
	private final ModelRenderer eye2;
	private final ModelRenderer eye3;
	private final ModelRenderer tongue;
	private final ModelRenderer latch;
	private final float bodyY;

	public VanillaChestMimicModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = part(null, 0, 0, 0.0F, 24.0F, -7.0F);
		lid = part(body, 0, 25, 0.0F, -9.0F, 14.0F);
		lid.addBox(-7.0F, -5.0F, -14.0F, 14, 5, 14);
		ModelRenderer topTooth1 = part(lid, 0, 0, -2.5F, -1.0F, -13.5F);
		part(topTooth1, 0, 35, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth2 = part(lid, 0, 0, -0.5F, -1.0F, -13.5F);
		part(topTooth2, 0, 35, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.5F, 3, 3, 0);
		ModelRenderer topTooth3 = part(lid, 0, 0, 1.5F, -1.0F, -13.5F);
		part(topTooth3, 0, 35, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.4F, 3, 3, 0);
		ModelRenderer topTooth4 = part(lid, 0, 0, 3.5F, -1.0F, -13.5F);
		part(topTooth4, 0, 35, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.5F, 3, 3, 0);
		ModelRenderer topTooth5 = part(lid, 0, 0, 5.5F, -1.0F, -13.5F);
		part(topTooth5, 0, 35, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth6 = part(lid, 0, 0, 6.1109F, -0.2322F, -12.4F, 0.0F, -1.5708F, 0.0F);
		part(topTooth6, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.5F, 3, 3, 0);
		ModelRenderer topTooth8 = part(lid, 0, 0, -6.8891F, -0.2322F, -12.4F, 0.0F, -1.5708F, 0.0F);
		part(topTooth8, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth7 = part(lid, 0, 0, 6.1109F, -0.2322F, -10.4F, 0.0F, -1.5708F, 0.0F);
		part(topTooth7, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, -0.1F, 3, 3, 0);
		ModelRenderer topTooth9 = part(lid, 0, 0, -7.8891F, -0.2322F, -10.4F, 0.0F, -1.5708F, 0.0F);
		part(topTooth9, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, -0.5F, 3, 3, 0);
		eye1 = part(lid, 43, 25, 4.5F, -2.1F, -12.1F);
		eye1.addBox(-2.5F, 0.0F, -2.0F, 3, 2, 2);
		eye3 = part(lid, 0, 25, 1.5F, -3.1F, -12.1F);
		eye3.addBox(-2.5F, 0.0F, -2.0F, 3, 2, 2);
		eye2 = part(lid, 0, 30, -2.5F, -2.1F, -12.1F);
		eye2.addBox(-2.5F, 0.0F, -2.0F, 3, 2, 2);
		part(lid, 0, 0, 6.0F, 2.0F, -4.5F, 0.5236F, 0.0F, 0.0F).addBox(0.0F, -4.0F, -2.5F, 0, 6, 6);
		part(lid, 0, 0, -6.0F, 2.0F, -4.5F, 0.5236F, 0.0F, 0.0F).addBox(0.0F, -4.0F, -2.5F, 0, 7, 6);
		base = part(body, 0, 0, 0.0F, -10.0F, 14.0F);
		base.addBox(-7.0F, 0.0F, -14.0F, 14, 10, 14);
		ModelRenderer bottomTooth6 = part(base, 0, 0, 6.1109F, 0.7678F, -12.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth6, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth7 = part(base, 0, 0, 6.1109F, 0.7678F, -10.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth7, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, -0.1F, 3, 3, 0);
		ModelRenderer bottomTooth8 = part(base, 0, 0, -6.8891F, 0.7678F, -12.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth8, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth9 = part(base, 0, 0, -7.8891F, 0.7678F, -10.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth9, 0, 35, 3.8891F, -1.7678F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, -0.5F, 3, 3, 0);
		ModelRenderer bottomTooth1 = part(base, 0, 0, -3.5F, 0.0F, -12.5F, -0.2618F, 0.0F, 0.0F);
		part(bottomTooth1, 43, 30, 9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.0F, 5.5F, 0.0F, 4, 4, 0);
		ModelRenderer bottomTooth2 = part(base, 0, 0, 4.7F, 0.5F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth2, 0, 35, -0.7F, -1.5F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.0F, -1.0F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth3 = part(base, 0, 0, 0.0F, 0.4F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth3, 0, 35, 1.0F, -1.4F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.0F, 0.0F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth4 = part(base, 0, 0, -5.3F, 0.5F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth4, 0, 35, -0.7F, -1.5F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.5F, -1.5F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth5 = part(base, 0, 0, 3.5F, 0.0F, -12.5F, -0.2618F, 0.0F, 0.0F);
		part(bottomTooth5, 43, 30, 9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.0F, 5.5F, 0.0F, 4, 4, 0);
		part(base, 0, 0, 3.0F, 0.0F, 0.0F);
		part(base, 0, 0, -3.0F, 1.0F, 0.0F);
		latch = part(base, 7, 0, 0.0F, 3.0F, -14.0F);
		latch.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 1);
		tongue = part(base, 43, 0, 0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F);
		tongue.addBox(-3.0F, 0.9F, -12.0F, 6, 1, 8);

		bodyY = body.rotationPointY;
	}

	@Override
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		super.activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		latch.rotateAngleX = 2.4870942F;
		eye1.rotateAngleX = -1.003564F;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = -0.174533F; // 10 degrees
	}

	@Override
	public void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		latch.rotateAngleX = timeSlice * 2.4870942F;
		eye1.rotateAngleX = timeSlice * -1.003564F;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = timeSlice * -0.174533F;
	}

	@Override
	public ModelRenderer getBody() {
		return body;
	}

	@Override
	public float getBodyY() {
		return bodyY;
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
