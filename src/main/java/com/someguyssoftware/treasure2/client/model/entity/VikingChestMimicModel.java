package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * The viking chest mimic, ported from the 1.20.1 VikingChestMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VikingChestMimicModel extends MimicModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer eye1;
	private final ModelRenderer tongue;
	private final ModelRenderer topLatch;
	private final ModelRenderer frontLatch;
	private final float bodyY;

	public VikingChestMimicModel() {
		textureWidth = 64;
		textureHeight = 64;

		body = part(null, 0, 0, 0.0F, 24.0F, -5.0F);
		base = part(body, 0, 0, 8.0F, 0.0F, -3.0F);
		base.addBox(-15.0F, -13.0F, 3.0F, 14, 10, 10);
		base.setTextureOffset(22, 42).addBox(-10.0F, -12.0F, 2.5F, 4, 4, 1);
		base.setTextureOffset(0, 35).addBox(-16.0F, -5.5F, 6.0F, 16, 2, 4);
		base.setTextureOffset(39, 11).addBox(-15.0F, -3.0F, 3.0F, 2, 3, 10);
		base.addBox(-3.0F, -3.0F, 3.0F, 2, 3, 10);
		base.setTextureOffset(11, 42).addBox(-1.0F, -11.0F, 6.0F, 1, 1, 4);
		base.setTextureOffset(0, 42).addBox(-16.0F, -11.0F, 6.0F, 1, 1, 4);
		tongue = part(base, 33, 35, -8.0F, -12.5F, 13.0F);
		tongue.addBox(-3.0F, -0.6F, -9.0F, 6, 1, 8);
		ModelRenderer front = part(base, 0, 21, 0.0F, 0.0F, 0.0F);
		front.addBox(-2.5F, -2.0F, 2.5F, 1, 1, 1);
		front.addBox(-2.5F, -8.0F, 2.5F, 1, 1, 1);
		front.addBox(-14.5F, -8.0F, 2.5F, 1, 1, 1);
		front.addBox(-14.5F, -2.0F, 2.5F, 1, 1, 1);
		ModelRenderer side1 = part(base, 0, 21, 0.0F, 0.0F, 0.0F);
		side1.addBox(-1.5F, -5.0F, 3.5F, 1, 1, 1);
		side1.addBox(-15.5F, -11.0F, 3.5F, 1, 1, 1);
		side1.addBox(-1.5F, -5.0F, 11.5F, 1, 1, 1);
		side1.addBox(-1.5F, -11.0F, 11.5F, 1, 1, 1);
		ModelRenderer side2 = part(base, 0, 21, 0.0F, 0.0F, 0.0F);
		side2.addBox(-15.5F, -5.0F, 3.5F, 1, 1, 1);
		side2.addBox(-1.5F, -11.0F, 3.5F, 1, 1, 1);
		side2.addBox(-15.5F, -5.0F, 11.5F, 1, 1, 1);
		side2.addBox(-15.5F, -11.0F, 11.5F, 1, 1, 1);
		ModelRenderer back = part(base, 0, 21, 0.0F, 0.0F, 0.0F);
		back.addBox(-2.5F, -2.0F, 12.5F, 1, 1, 1);
		back.addBox(-2.5F, -8.0F, 12.5F, 1, 1, 1);
		back.addBox(-14.5F, -8.0F, 12.5F, 1, 1, 1);
		back.addBox(-14.5F, -2.0F, 12.5F, 1, 1, 1);
		// the base teeth and topTooth3 sit 0.7 lower than 1.20.1's, so their tips stay inside the closed lid
		part(base, 0, 0, -5.0F, -12.3F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.9393F, -1.7678F, 0.0F, 4, 4, 0);
		part(base, 0, 0, -11.0F, -12.3F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.9393F, -1.7678F, 0.0F, 4, 4, 0);
		ModelRenderer bottomTooth3 = part(base, 0, 0, -14.182F, -12.2322F, 5.5F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth3, 0, 5, 3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.0F, 0.6F, 3, 3, 0);
		ModelRenderer bottomTooth4 = part(base, 0, 0, -2.182F, -12.2322F, 5.5F, 0.0F, -1.5708F, 0.0F);
		part(bottomTooth4, 0, 5, 3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.0F, -0.1F, 3, 3, 0);
		frontLatch = part(body, 1, 49, 0.0F, -10.5F, -0.5F);
		frontLatch.addBox(-1.0F, -4.0F, -0.25F, 2, 4, 1);
		lid = part(body, 0, 21, 0.0F, -12.0F, 10.0F);
		lid.addBox(-7.0F, -3.0F, -10.0F, 14, 3, 10);
		lid.addBox(5.5F, -2.0F, -10.5F, 1, 1, 1);
		lid.addBox(-6.5F, -2.0F, -10.5F, 1, 1, 1);
		lid.addBox(5.5F, -2.0F, -0.5F, 1, 1, 1);
		lid.addBox(-6.5F, -2.0F, -0.5F, 1, 1, 1);
		topLatch = part(lid, 39, 0, 0.0F, -3.0F, -2.75F);
		topLatch.addBox(-1.0F, -0.5F, -8.0F, 2, 1, 8);
		ModelRenderer topTooth6 = part(lid, 0, 0, 6.818F, -0.2322F, -7.5F, 0.0F, -1.5708F, 0.0F);
		part(topTooth6, 0, 5, 3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 1.0F, 3, 3, 0);
		ModelRenderer topTooth7 = part(lid, 0, 0, -6.182F, -0.2322F, -7.5F, 0.0F, -1.5708F, 0.0F);
		part(topTooth7, 0, 5, 3.182F, -1.7678F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, 2.0F, 0.5F, 3, 3, 0);
		ModelRenderer topTooth1 = part(lid, 0, 0, -2.5F, -6.0F, -11.5F);
		part(topTooth1, 0, 5, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.0F, 6.5F, 2.6F, 3, 3, 0);
		ModelRenderer topTooth2 = part(lid, 0, 0, 0.5F, -6.0F, -11.5F);
		part(topTooth2, 0, 5, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.0F, 6.5F, 2.6F, 3, 3, 0);
		ModelRenderer topTooth4 = part(lid, 0, 0, 3.5F, -6.0F, -11.5F);
		part(topTooth4, 0, 5, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.0F, 6.5F, 2.6F, 3, 3, 0);
		ModelRenderer topTooth5 = part(lid, 0, 0, 6.5F, -6.0F, -11.5F);
		part(topTooth5, 0, 5, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(0.0F, 6.5F, 2.6F, 3, 3, 0);
		part(lid, 0, 0, -1.0F, 1.7F, -8.0F, 0.0F, 0.0F, 0.7854F).addBox(-2.5F, -4.0F, -1.5F, 4, 4, 0);
		eye1 = part(lid, 39, 25, 0.0F, -3.0F, -8.1F);
		eye1.addBox(-2.0F, 0.0F, -2.0F, 4, 2, 2);
		part(lid, 5, 49, 6.0F, 1.0F, -3.0F, 0.5236F, 0.0F, 0.0F).addBox(0.0F, -3.0F, -3.0F, 0, 6, 6);
		part(lid, 5, 49, -6.0F, 1.0F, -3.0F, 0.5236F, 0.0F, 0.0F).addBox(0.0F, -3.0F, -3.0F, 0, 6, 6);

		bodyY = body.rotationPointY;
	}

	@Override
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		super.activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		topLatch.rotateAngleX = -0.2618F;
		eye1.rotateAngleX = -1.003564F;
		tongue.rotateAngleX = -0.174533F; // 10 degrees
		frontLatch.rotateAngleX = 3.14159F;
	}

	@Override
	public void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		topLatch.rotateAngleX = timeSlice * -0.2618F;
		frontLatch.rotateAngleX = timeSlice * 3.14159F;
		eye1.rotateAngleX = timeSlice * -1.003564F;
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
