package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

import net.minecraft.util.math.MathHelper;

/**
 * The cauldron chest mimic, ported from the 1.20.1 CauldronChestMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class CauldronChestMimicModel extends MimicModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer legs;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer eye2;
	private final ModelRenderer eye3;
	private final ModelRenderer rightLid;
	private final ModelRenderer leftLid;
	private final ModelRenderer spike1;
	private final ModelRenderer spike2;
	private final ModelRenderer spike3;
	private final ModelRenderer frontLeg1;
	private final ModelRenderer frontLeg2;
	private final ModelRenderer backLeg1;
	private final ModelRenderer backLeg2;
	private final ModelRenderer frontSwing1;
	private final ModelRenderer frontSwing2;
	private final ModelRenderer backSwing1;
	private final ModelRenderer backSwing2;
	private final float bodyY;

	public CauldronChestMimicModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = part(null, 0, 0, 0.0F, 24.0F, -8.0F);
		base = part(body, 42, 51, 0.0F, -3.0F, 0.0F);
		base.addBox(6.0F, -4.0F, 0.0F, 2, 4, 16);
		base.setTextureOffset(21, 46).addBox(-8.0F, -4.0F, 0.0F, 2, 4, 16);
		base.setTextureOffset(0, 20).addBox(-6.0F, -3.0F, 2.0F, 12, 3, 12);
		base.setTextureOffset(63, 60).addBox(-6.0F, -4.0F, 14.0F, 12, 4, 2);
		base.setTextureOffset(42, 72).addBox(-6.0F, -4.0F, 0.0F, 12, 4, 2);
		ModelRenderer bottomTooth5 = part(base, 0, 0, 4.5F, -7.0F, 2.5F, -0.2618F, 0.0F, 0.0F);
		part(bottomTooth5, 37, 26, 9.5F, 3.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.5F, 5.0F, 0.5F, 4, 4, 0);
		ModelRenderer bottomTooth6 = part(base, 0, 0, -4.5F, -7.0F, 2.5F, -0.2618F, 0.0F, 0.0F);
		part(bottomTooth6, 37, 26, 9.5F, 3.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.5F, 5.0F, 0.5F, 4, 4, 0);
		part(base, 50, 0, 3.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(4.0F, 1.5F, -3.0F, 4, 4, 0);
		part(base, 50, 0, 0.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(4.0F, 1.5F, -3.0F, 4, 4, 0);
		part(base, 50, 0, -3.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(4.0F, 1.5F, -3.0F, 4, 4, 0);
		part(base, 50, 0, -6.0F, -10.0F, 3.5F, 0.0F, 0.0F, 0.7854F).addBox(4.0F, 1.5F, -3.0F, 4, 4, 0);
		part(base, 0, 36, 6.0F, -3.0F, 11.5F, -0.5236F, 0.0F, 0.0F).addBox(1.0F, -5.0F, -4.0F, 0, 7, 6);
		part(base, 0, 36, -7.0F, -3.0F, 11.5F, -0.5236F, 0.0F, 0.0F).addBox(0.0F, -5.0F, -4.0F, 0, 7, 6);
		tongue = part(base, 21, 71, 0.0F, -7.0F, 15.0F, -0.1745F, 0.0F, 0.0F);
		tongue.addBox(-3.0F, 4.5F, -11.0F, 6, 1, 8);
		lid = part(body, 0, 0, 0.0F, -7.5F, 16.0F);
		lid.addBox(-6.0F, -7.6F, -14.0F, 12, 7, 12);
		lid.setTextureOffset(63, 48).addBox(-6.0F, -8.5F, -16.0F, 12, 9, 2);
		lid.setTextureOffset(0, 67).addBox(-6.0F, -8.5F, -2.0F, 12, 9, 2);
		lid.setTextureOffset(0, 36).addBox(6.0F, -8.5F, -16.0F, 2, 9, 16);
		lid.setTextureOffset(33, 20).addBox(-8.0F, -8.5F, -16.0F, 2, 9, 16);
		leftLid = part(lid, 54, 14, 6.0F, -7.5F, -14.0F);
		leftLid.addBox(-6.0F, -0.2F, 0.0F, 6, 1, 12);
		rightLid = part(lid, 49, 0, -6.0F, -7.5F, -14.0F);
		rightLid.addBox(0.0F, -0.2F, 0.0F, 6, 1, 12);
		ModelRenderer topTooth1 = part(lid, 0, 0, -3.5F, -0.5F, -14.5F);
		part(topTooth1, 37, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth2 = part(lid, 0, 0, -0.5F, -0.5F, -14.5F);
		part(topTooth2, 37, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth3 = part(lid, 0, 0, 2.5F, -0.5F, -14.5F);
		part(topTooth3, 37, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		ModelRenderer topTooth4 = part(lid, 0, 0, 5.5F, -0.5F, -14.5F);
		part(topTooth4, 37, 0, 2.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-3.0F, 2.5F, 0.6F, 3, 3, 0);
		eye1 = part(lid, 21, 36, 4.5F, -4.5F, -14.0F);
		eye1.addBox(-1.5F, 0.0F, -2.1F, 3, 2, 2);
		eye2 = part(lid, 0, 25, -3.5F, -4.5F, -14.0F);
		eye2.addBox(-1.5F, 0.0F, -2.1F, 3, 2, 2);
		eye3 = part(lid, 0, 20, 0.5F, -6.5F, -14.0F);
		eye3.addBox(-1.5F, 0.0F, -2.1F, 3, 2, 2);
		spike1 = part(lid, 58, 34, 0.0F, -7.5F, -14.0F);
		spike1.addBox(-1.0F, -0.1F, 0.0F, 2, 1, 12);
		spike2 = part(lid, 37, 0, 0.0F, -7.5F, -10.0F);
		spike2.addBox(-1.0F, -0.1F, 0.0F, 2, 1, 8);
		spike3 = part(lid, 21, 42, 0.0F, -7.5F, -6.0F);
		spike3.addBox(-1.0F, -0.1F, 0.0F, 2, 1, 4);
		legs = part(null, 0, 0, 0.0F, 24.0F, -8.0F);
		frontLeg1 = part(legs, 0, 0, 6.0F, 0.0F, 0.0F);
		frontSwing1 = part(frontLeg1, 37, 20, 0.0F, -3.0F, 2.0F);
		frontSwing1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 2);
		frontSwing1.setTextureOffset(54, 28).addBox(-2.0F, 0.0F, -2.0F, 4, 3, 2);
		frontLeg2 = part(legs, 0, 0, -6.0F, 0.0F, 0.0F);
		frontSwing2 = part(frontLeg2, 7, 36, 0.0F, -3.0F, 2.0F);
		frontSwing2.addBox(-2.0F, 0.0F, 0.0F, 2, 3, 2);
		frontSwing2.setTextureOffset(49, 14).addBox(-2.0F, 0.0F, -2.0F, 4, 3, 2);
		backLeg1 = part(legs, 0, 0, 6.4F, 0.0F, 11.9F);
		backSwing1 = part(backLeg1, 0, 0, -0.4F, -6.0F, 2.1F);
		backSwing1.addBox(-0.1F, 0.0F, -2.1F, 2, 6, 2);
		backSwing1.setTextureOffset(42, 46).addBox(-2.1F, 0.0F, -0.1F, 4, 6, 2);
		backLeg2 = part(legs, 0, 0, -6.0F, 0.0F, 12.0F);
		backSwing2 = part(backLeg2, 0, 0, 0.0F, -6.0F, 2.0F);
		backSwing2.addBox(-1.9F, 0.0F, -2.1F, 2, 6, 2);
		backSwing2.setTextureOffset(42, 46).addBox(-1.9F, 0.0F, -0.1F, 4, 6, 2);

		bodyY = body.rotationPointY;
	}

	@Override
	protected void renderModel(float scale) {
		body.render(scale);
		legs.render(scale);
	}

	/**
	 * The cauldron stands on its legs, so it doesn't bob.
	 */
	@Override
	public void bob(ModelRenderer part, float originY, float age) {
	}

	public void bobXRot(ModelRenderer part, float age, float radians, int direction) {
		part.rotateAngleX += direction * (MathHelper.sin(age * 0.05F) * radians + 0.05F);
	}

	@Override
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		super.activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		frontLeg1.rotateAngleX = body.rotateAngleX;
		frontLeg2.rotateAngleX = body.rotateAngleX;
		backLeg1.rotateAngleX = body.rotateAngleX;
		backLeg2.rotateAngleX = body.rotateAngleX;
		rightLid.rotateAngleZ = -2.26893F; // 130 degrees
		leftLid.rotateAngleZ = -rightLid.rotateAngleZ;
		spike1.rotateAngleX = 1.13446F; // 65 degrees
		spike2.rotateAngleX = 0.959931F; // 55 degrees
		spike3.rotateAngleX = 0.7854F; // 45 degrees
		eye1.rotateAngleX = -1.003564F;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = -0.174533F; // 10 degrees

		// walk
		frontSwing1.rotateAngleX = MathHelper.cos(limbSwing * 0.349066F) * 1.4F * limbSwingAmount;
		backSwing1.rotateAngleX = MathHelper.cos(limbSwing * 0.349066F + (float)Math.PI) * 1.4F * limbSwingAmount;
		frontSwing2.rotateAngleX = backSwing1.rotateAngleX;
		backSwing2.rotateAngleX = frontSwing1.rotateAngleX;

		bobXRot(spike1, ageInTicks, 0.26F, -1);
		bobXRot(spike2, ageInTicks, 0.26F, -1);
		bobXRot(spike3, ageInTicks, 0.26F, -1);
	}

	@Override
	public void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		frontLeg1.rotateAngleX = body.rotateAngleX;
		frontLeg2.rotateAngleX = body.rotateAngleX;
		backLeg1.rotateAngleX = body.rotateAngleX;
		backLeg2.rotateAngleX = body.rotateAngleX;
		rightLid.rotateAngleZ = timeSlice * -2.26893F;
		leftLid.rotateAngleZ = -rightLid.rotateAngleZ;
		spike1.rotateAngleX = timeSlice * 1.13446F;
		spike2.rotateAngleX = timeSlice * 0.959931F;
		spike3.rotateAngleX = timeSlice * 0.7854F;
		eye1.rotateAngleX = timeSlice * -1.003564F;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = timeSlice * -0.174533F;
		// the model is shared, so clear any walk left by an active cauldron
		frontSwing1.rotateAngleX = 0F;
		frontSwing2.rotateAngleX = 0F;
		backSwing1.rotateAngleX = 0F;
		backSwing2.rotateAngleX = 0F;
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
