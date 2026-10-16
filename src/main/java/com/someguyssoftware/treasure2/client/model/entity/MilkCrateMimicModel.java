package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * The milk crate mimic, ported from the 1.20.1 MilkCrateMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class MilkCrateMimicModel extends MimicModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer eye;
	private final ModelRenderer tongue;
	private final float bodyY;

	public MilkCrateMimicModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = part(null, 0, 0, 0.0F, 24.0F, -7.0F);
		part(body, 0, 0, 6.5F, -9.5F, 10.0F).addBox(0.0F, -3.5F, -2.0F, 0, 7, 6);
		part(body, 0, 0, -6.5F, -9.5F, 10.0F).addBox(0.0F, -3.5F, -2.0F, 0, 7, 6);
		lid = part(body, 0, 0, 0.0F, -11.0F, 14.0F);
		lid.addBox(-7.0F, -2.0F, -14.0F, 14, 2, 14);
		lid.setTextureOffset(0, 31).addBox(-6.0F, 2.0F, -13.0F, 12, 0, 12);
		ModelRenderer northFace = part(lid, 30, 48, 0.0F, 1.0F, 0.0F);
		northFace.addBox(-7.0F, -1.0F, -14.0F, 14, 3, 1);
		northFace.setTextureOffset(0, 31).addBox(5.1F, -1.0F, -14.1F, 2, 3, 2);
		northFace.addBox(-7.1F, -1.0F, -14.1F, 2, 3, 2);
		part(lid, 37, 32, 0.0F, 0.0F, 0.0F).addBox(6.0F, 0.0F, -13.0F, 1, 3, 12);
		part(lid, 37, 32, 0.0F, 0.0F, 0.0F).addBox(-7.0F, 0.0F, -13.0F, 1, 3, 12);
		eye = part(lid, 57, 10, 0.0F, -2.0F, -13.0F);
		eye.addBox(-2.0F, -0.01F, -1.2F, 4, 4, 3);
		eye.setTextureOffset(37, 24).addBox(-1.0F, 1.0F, -2.0F, 2, 2, 1);
		ModelRenderer topTooth6 = part(lid, 0, 0, 5.5F, 7.0F, -10.6F);
		part(topTooth6, 37, 17, 6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F).addBox(-5.0F, 1.0F, -1.5F, 0, 3, 3);
		ModelRenderer topTooth7 = part(lid, 0, 0, -7.5F, 7.0F, -10.6F);
		part(topTooth7, 37, 17, 6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F).addBox(-5.0F, 1.0F, -1.5F, 0, 3, 3);
		ModelRenderer topTooth5 = part(lid, 0, 0, 5.5F, 7.0F, -6.6F);
		part(topTooth5, 37, 17, 6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F).addBox(-5.0F, 1.0F, -1.5F, 0, 3, 3);
		ModelRenderer topTooth8 = part(lid, 0, 0, -7.5F, 7.0F, -6.6F);
		part(topTooth8, 37, 17, 6.0F, -6.0F, -1.9F, 0.7854F, 0.0F, 0.0F).addBox(-5.0F, 1.0F, -1.5F, 0, 3, 3);
		ModelRenderer topRpwTeeth = part(lid, 0, 0, 0.0F, 2.5F, -13.0F, 0.2618F, 0.0F, 0.0F);
		ModelRenderer topTooth1 = part(topRpwTeeth, 0, 0, -3.0F, -1.5F, -0.5F);
		part(topTooth1, 7, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 3.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth2 = part(topRpwTeeth, 0, 0, 0.25F, -1.5F, -0.5F);
		part(topTooth2, 7, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 3.5F, 0.5F, 3, 3, 0);
		ModelRenderer topTooth3 = part(topRpwTeeth, 0, 0, 3.5F, -1.5F, -0.5F);
		part(topTooth3, 7, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 3.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth4 = part(topRpwTeeth, 0, 0, 6.5F, -1.5F, -0.5F);
		part(topTooth4, 7, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 3.5F, 0.5F, 3, 3, 0);
		base = part(body, 37, 19, 0.0F, 0.0F, 7.0F);
		base.addBox(-6.0F, -6.0F, -6.0F, 12, 0, 12);
		ModelRenderer basePart = part(base, 0, 0, 0.0F, 0.0F, 0.0F);
		ModelRenderer baseNorth = part(basePart, 30, 53, 0.0F, 0.0F, 0.0F);
		baseNorth.addBox(-7.0F, -3.0F, -7.0F, 14, 3, 1);
		baseNorth.setTextureOffset(52, 32).addBox(-7.0F, -7.0F, -7.0F, 14, 3, 1);
		baseNorth.setTextureOffset(0, 17).addBox(5.1F, -8.0F, -7.1F, 2, 8, 2);
		baseNorth.addBox(-7.1F, -8.0F, -7.1F, 2, 8, 2);
		part(basePart, 0, 17, 0.0F, 0.0F, 0.0F).addBox(-6.0F, -1.0F, -6.0F, 12, 1, 12);
		ModelRenderer westFace = part(basePart, 15, 48, 0.0F, -10.0F, 7.0F);
		westFace.addBox(6.0F, 7.0F, -13.0F, 1, 3, 12);
		westFace.setTextureOffset(0, 44).addBox(6.0F, 3.0F, -13.0F, 1, 3, 12);
		ModelRenderer eastFace = part(basePart, 15, 48, 0.0F, -10.0F, 7.0F);
		eastFace.addBox(-7.0F, 7.0F, -13.0F, 1, 3, 12);
		eastFace.setTextureOffset(0, 44).addBox(-7.0F, 3.0F, -13.0F, 1, 3, 12);
		ModelRenderer southFace = part(basePart, 52, 37, 0.0F, -10.0F, 7.0F);
		southFace.addBox(-7.0F, 7.0F, -1.0F, 14, 3, 1);
		southFace.setTextureOffset(52, 32).addBox(-7.0F, 3.0F, -1.0F, 14, 3, 1);
		southFace.setTextureOffset(30, 48).addBox(-7.0F, -0.99F, -1.0F, 14, 3, 1);
		southFace.setTextureOffset(42, 58).addBox(5.1F, -1.0F, -1.9F, 2, 11, 2);
		southFace.addBox(-7.1F, -1.0F, -1.9F, 2, 11, 2);
		ModelRenderer bottomTooth1 = part(base, 0, 0, -3.5F, -7.0F, -6.5F, -0.1745F, 0.0F, 0.0F);
		part(bottomTooth1, 0, 37, 9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.0F, 5.5F, 0.5F, 4, 4, 0);
		ModelRenderer bottomTooth2 = part(base, 0, 0, 3.5F, -7.0F, -6.5F, -0.1745F, 0.0F, 0.0F);
		part(bottomTooth2, 0, 37, 9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.0F, 5.5F, 0.5F, 4, 4, 0);
		ModelRenderer bottomTooth3 = part(base, 0, 0, 6.1109F, -9.2322F, -3.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth3, 7, 0, 2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth5 = part(base, 0, 0, -6.3891F, -9.2322F, -3.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth5, 7, 0, 2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.5F, 3, 3, 0);
		ModelRenderer bottomTooth4 = part(base, 0, 0, 6.1109F, -9.2322F, -1.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth4, 7, 0, 2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth6 = part(base, 0, 0, -7.3891F, -9.2322F, -1.4F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth6, 7, 0, 2.8891F, 1.2322F, -0.6F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth7 = part(base, 0, 0, 1.75F, -8.75F, -6.7F);
		part(bottomTooth7, 7, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 3.5F, 0.5F, 3, 3, 0);
		tongue = part(base, 43, 0, 0.0F, -6.0F, 7.0F, -0.1745F, 0.0F, 0.0F);
		tongue.addBox(-3.0F, 0.9F, -12.0F, 6, 1, 8);

		// the crate is open-sided, so the teeth show through it
		hideWhileDormant(topRpwTeeth, topTooth5, topTooth6, topTooth7, topTooth8,
				bottomTooth1, bottomTooth2, bottomTooth3, bottomTooth4, bottomTooth5, bottomTooth6, bottomTooth7);

		bodyY = body.rotationPointY;
	}

	@Override
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		super.activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		eye.rotateAngleX = -1.003564F;
		tongue.rotateAngleX = -0.174533F; // 10 degrees
	}

	@Override
	public void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		eye.rotateAngleX = timeSlice * -1.003564F;
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
