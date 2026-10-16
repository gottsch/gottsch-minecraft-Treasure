package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * The wood chest mimic, ported from the 1.20.1 WoodChestMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class WoodChestMimicModel extends MimicModel {
	private static final float LATCH_RADIANS = 2.4870942F;
	private static final float EYE_RADIANS = -1.003564F;
	private static final float TONGUE_RADIANS = -0.174533F; // 10 degrees

	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer latch;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer eye2;
	private final ModelRenderer eye3;

	private final float bodyY;

	public WoodChestMimicModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = part(null, 0, 0, 0.0F, 24.0F, -7.0F);
		bodyY = body.rotationPointY;

		// lid
		lid = part(body, 0, 25, 0.0F, -9.0F, 14.0F);
		lid.addBox(-7.0F, -5.0F, -14.0F, 14, 5, 14);

		tooth(lid, -2.5F, -1.0F, -13.5F, 0F, 0F, 2.5F, -1.0F, -0.5F, 0, 35, -3.0F, 2.5F, 0.6F, 3);
		tooth(lid, -0.5F, -1.0F, -13.5F, 0F, 0F, 2.5F, -1.0F, -0.5F, 0, 35, -3.0F, 2.5F, 0.5F, 3);
		tooth(lid, 1.5F, -1.0F, -13.5F, 0F, 0F, 2.5F, -1.0F, -0.5F, 0, 35, -3.0F, 2.5F, 0.4F, 3);
		tooth(lid, 3.5F, -1.0F, -13.5F, 0F, 0F, 2.5F, -1.0F, -0.5F, 0, 35, -3.0F, 2.5F, 0.5F, 3);
		tooth(lid, 5.5F, -1.0F, -13.5F, 0F, 0F, 2.5F, -1.0F, -0.5F, 0, 35, -3.0F, 2.5F, 0.6F, 3);
		tooth(lid, 6.1109F, -0.2322F, -12.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, 0.5F, 3);
		tooth(lid, -6.8891F, -0.2322F, -12.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, 0.0F, 3);
		tooth(lid, 6.1109F, -0.2322F, -10.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, -0.1F, 3);
		tooth(lid, -7.8891F, -0.2322F, -10.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, -0.5F, 3);

		eye1 = part(lid, 0, 25, 4.5F, -2.1F, -12.1F);
		eye1.addBox(-1.5F, 0.0F, -2.0F, 3, 2, 2);
		eye2 = part(lid, 0, 25, -3.5F, -2.1F, -12.1F);
		eye2.mirror = true;
		eye2.addBox(-1.5F, 0.0F, -2.0F, 3, 2, 2);
		eye3 = part(lid, 0, 25, 0.5F, -3.1F, -12.1F);
		eye3.mirror = true;
		eye3.addBox(-1.5F, 0.0F, -2.0F, 3, 2, 2);

		part(lid, 0, 0, 6.0F, 2.0F, -4.5F, 0.5236F, 0F, 0F).addBox(0.0F, -4.0F, -2.5F, 0, 7, 6);
		part(lid, 0, 0, -6.0F, 2.0F, -4.5F, 0.5236F, 0F, 0F).addBox(0.0F, -4.0F, -2.5F, 0, 7, 6);

		// base
		base = part(body, 0, 0, 0.0F, -10.0F, 14.0F);
		base.addBox(-7.0F, 0.0F, -14.0F, 14, 10, 14);

		tooth(base, 6.1109F, 0.7678F, -12.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, 0.5F, 3);
		tooth(base, 6.1109F, 0.7678F, -10.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, -0.1F, 3);
		tooth(base, -6.8891F, 0.7678F, -12.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, 0.0F, 3);
		tooth(base, -7.8891F, 0.7678F, -10.4F, 0F, -1.5708F, 3.8891F, -1.7678F, -0.6F, 0, 35, -2.5F, 2.0F, -0.5F, 3);
		tooth(base, -3.5F, 0.0F, -12.5F, -0.2618F, 0F, 9.5F, -1.0F, -0.5F, 43, 30, -8.0F, 5.5F, 0.0F, 4);
		tooth(base, 4.7F, 0.5F, -13.5F, 0.2182F, 0F, -0.7F, -1.5F, -0.5F, 0, 35, 0.0F, -1.0F, 0.5F, 3);
		tooth(base, 0.0F, 0.4F, -13.5F, 0.2182F, 0F, 1.0F, -1.4F, -0.5F, 0, 35, -1.0F, 0.0F, 0.5F, 3);
		tooth(base, -5.3F, 0.5F, -13.5F, 0.2182F, 0F, -0.7F, -1.5F, -0.5F, 0, 35, 0.5F, -1.5F, 0.5F, 3);
		tooth(base, 3.5F, 0.0F, -12.5F, -0.2618F, 0F, 9.5F, -1.0F, -0.5F, 43, 30, -8.0F, 5.5F, 0.0F, 4);

		ModelRenderer bottomHinge1 = part(base, 7, 35, 3.0F, 0.0F, 0.0F);
		bottomHinge1.mirror = true;
		bottomHinge1.addBox(-1.0F, 0.0F, -0.9F, 2, 2, 1);
		part(base, 7, 35, -3.0F, 1.0F, 0.0F).addBox(-1.0F, -1.0F, -0.9F, 2, 2, 1);

		latch = part(base, 0, 30, 0.0F, 4.0F, -13.0F);
		latch.addBox(-2.0F, -6.0F, -1.2F, 4, 3, 1);
		latch.setTextureOffset(43, 25).addBox(-2.0F, -3.0F, -1.2F, 4, 3, 1);
		latch.setTextureOffset(7, 0).addBox(-1.0F, -5.0F, -2.0F, 2, 4, 1);

		tongue = part(base, 43, 0, 0.0F, 0.0F, 0.0F, TONGUE_RADIANS, 0F, 0F);
		tongue.addBox(-3.0F, 0.9F, -12.0F, 6, 1, 8);
	}

	@Override
	public void activeAnim(MimicEntity mimic, float ageInTicks) {
		super.activeAnim(mimic, ageInTicks);
		latch.rotateAngleX = LATCH_RADIANS;
		eye1.rotateAngleX = EYE_RADIANS;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = TONGUE_RADIANS;
	}

	@Override
	public void openAnim(MimicEntity mimic, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		latch.rotateAngleX = timeSlice * LATCH_RADIANS;
		eye1.rotateAngleX = timeSlice * EYE_RADIANS;
		eye2.rotateAngleX = eye1.rotateAngleX;
		eye3.rotateAngleX = eye1.rotateAngleX;
		tongue.rotateAngleX = timeSlice * TONGUE_RADIANS;
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
