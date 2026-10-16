package com.someguyssoftware.treasure2.client.model.entity;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.model.ModelRenderer;

/**
 * The moldy crate mimic, ported from the 1.20.1 MoldyCrateChestMimicModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class MoldyCrateChestMimicModel extends MimicModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer eye1;
	private final ModelRenderer eyeSocket;
	private final ModelRenderer tongue;
	/** The big teeth's rest angle: folded back inside the closed crate. */
	private static final float BIG_TEETH_CLOSED_RADIANS = -0.8727F;
	private static final float BIG_TEETH_OPEN_RADIANS = 0.174533F;

	private final ModelRenderer bigTeeth;
	private final float bodyY;

	public MoldyCrateChestMimicModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = part(null, 0, 0, 0.0F, 24.0F, -7.0F);
		lid = part(body, 0, 17, 0.0F, -13.0F, 14.0F);
		lid.addBox(-7.0F, -2.0F, -14.0F, 14, 2, 14);
		eye1 = part(lid, 0, 0, 0.0F, -2.0F, -11.0F);
		eyeSocket = part(lid, 67, 31, 0.0F, 1.0F, 0.0F);
		eyeSocket.addBox(-3.0F, -2.9F, -13.0F, 6, 3, 13);
		part(eye1, 43, 34, -6.0F, 0.0F, 10.0F).addBox(4.0F, -0.2F, -13.2F, 4, 2, 3);
		part(eye1, 0, 20, -6.0F, 0.0F, 10.0F).addBox(5.0F, 0.0F, -14.0F, 2, 2, 1);
		part(lid, 17, 46, 5.0F, 6.0F, -4.5F, 0.5236F, 0.0F, 0.0F).addBox(0.9F, -7.0F, -0.5F, 0, 7, 6);
		part(lid, 17, 46, -6.0F, 6.0F, -4.5F, 0.5236F, 0.0F, 0.0F).addBox(0.1F, -7.0F, -0.5F, 0, 7, 6);
		ModelRenderer topTooth1 = part(lid, 0, 0, -4.2426F, -0.1213F, -13.4F, 0.4363F, 0.0F, 0.0F);
		part(topTooth1, 53, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth2 = part(lid, 0, 0, -2.2426F, -0.1213F, -13.5F, 0.4363F, 0.0F, 0.0F);
		part(topTooth2, 53, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth3 = part(lid, 0, 0, -0.2426F, -0.1213F, -13.6F, 0.4363F, 0.0F, 0.0F);
		part(topTooth3, 53, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth5 = part(lid, 0, 0, 3.7574F, -0.1213F, -13.4F, 0.4363F, 0.0F, 0.0F);
		part(topTooth5, 53, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth4 = part(lid, 0, 0, 1.7574F, -0.1213F, -13.5F, 0.4363F, 0.0F, 0.0F);
		part(topTooth4, 53, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer topTooth6 = part(lid, 0, 0, 6.0109F, -0.2929F, -11.3749F, 0.0F, -1.5708F, 0.4363F);
		part(topTooth6, 53, 52, -0.318F, 0.0607F, -0.2F, 0.0F, 0.0F, 0.7854F).addBox(-1.318F, -1.7678F, 0.2F, 3, 3, 0);
		ModelRenderer topTooth8 = part(lid, 0, 0, -5.9891F, -0.2929F, -11.3749F, 0.0F, -1.5708F, -0.4363F);
		part(topTooth8, 53, 52, -0.318F, 0.0607F, -0.2F, 0.0F, 0.0F, 0.7854F).addBox(-1.318F, -1.7678F, 0.2F, 3, 3, 0);
		ModelRenderer topTooth7 = part(lid, 0, 0, 6.534F, -0.3574F, -8.8536F, 0.0F, -1.5708F, 0.4363F);
		part(topTooth7, 53, 52, 1.1607F, 0.1251F, -0.277F, 0.0F, 0.0F, 0.7854F).addBox(-2.4092F, -0.7678F, 0.277F, 3, 3, 0);
		ModelRenderer topTooth9 = part(lid, 0, 0, -6.466F, -0.3574F, -8.8536F, 0.0F, -1.5708F, -0.4363F);
		part(topTooth9, 53, 52, 1.1607F, 0.1251F, -0.277F, 0.0F, 0.0F, 0.7854F).addBox(-2.4092F, -0.7678F, 0.277F, 3, 3, 0);
		base = part(body, 45, 5, 0.0F, -2.0F, 14.0F);
		base.addBox(-6.0F, -10.0F, -13.0F, 12, 10, 12);
		base.setTextureOffset(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14, 2, 14);
		base.mirror = true;
		base.addBox(-7.0F, -10.0F, -14.0F, 2, 10, 2);
		base.mirror = false;
		base.addBox(5.0F, -10.0F, -14.0F, 2, 10, 2);
		base.addBox(-7.0F, -10.0F, -2.0F, 2, 10, 2);
		base.mirror = true;
		base.addBox(5.0F, -10.0F, -2.0F, 2, 10, 2);
		base.mirror = false;
		base.setTextureOffset(0, 34).addBox(-7.0F, -11.0F, -14.0F, 14, 1, 14);
		part(base, 0, 50, 5.5F, -10.5F, -2.5F, -0.8029F, 0.0F, 0.0F).addBox(0.0F, 0.0F, 0.0F, 1, 15, 2);
		part(base, 0, 50, -6.5F, -9.5F, -13.5F, 0.8029F, 0.0F, 0.0F).addBox(0.0F, 0.0F, 0.0F, 1, 15, 2);
		part(base, 7, 50, 5.0F, -10.5F, 0.5F, 0.0F, 0.0F, 0.8029F).addBox(-1.0F, 0.0F, -2.0F, 2, 15, 1);
		part(base, 7, 50, -5.0F, -10.5F, -11.5F, 0.0F, 0.0F, -0.8029F).addBox(-1.0F, 0.0F, -2.0F, 2, 15, 1);
		ModelRenderer pad = part(base, 0, 17, 0.0F, -11.0F, -13.2F);
		pad.addBox(-2.0F, -0.2F, -1.0F, 4, 1, 1);
		ModelRenderer bottomTooth1 = part(pad, 0, 0, -3.5F, 3.0F, 0.7F, -0.2618F, 0.0F, 0.0F);
		part(bottomTooth1, 43, 30, 9.5F, -1.0F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-8.0F, 5.5F, 0.0F, 4, 4, 0);
		tongue = part(base, 5, 76, 0.0F, -11.0F, 1.0F, -0.1745F, 0.0F, 0.0F);
		tongue.addBox(-3.0F, 0.9F, -12.5F, 6, 1, 8);
		bigTeeth = part(base, 0, 0, 0.0F, -10.0F, -12.0F, BIG_TEETH_CLOSED_RADIANS, 0.0F, 0.0F);
		ModelRenderer bottomTooth2 = part(bigTeeth, 0, 0, -3.5F, 2.0F, -1.5F);
		part(bottomTooth2, 41, 57, 0.3076F, -3.2929F, 0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.0F, -1.7071F, 0.5F, 4, 4, 0);
		ModelRenderer bottomTooth5 = part(bigTeeth, 0, 0, 2.5F, 2.0F, -0.5F);
		part(bottomTooth5, 41, 57, 0.6612F, -2.9393F, -0.5F, 0.0F, 0.0F, 0.7854F).addBox(-2.0F, -2.0607F, 0.5F, 4, 4, 0);
		ModelRenderer bottomTooth4 = part(base, 0, 0, -4.3F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth4, 40, 52, 0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth3 = part(base, 0, 0, -0.3F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth3, 40, 52, 0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth6 = part(base, 0, 0, 3.7F, -11.5F, -13.5F, 0.2182F, 0.0F, 0.0F);
		part(bottomTooth6, 40, 52, 0.3607F, 0.9749F, 0.5F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth7 = part(base, 0, 0, 6.1109F, -10.909F, -11.0088F, 0.0F, -1.5708F, -0.4363F);
		part(bottomTooth7, 40, 52, 0.4926F, 0.2071F, -0.1F, 0.0F, 0.0F, 0.7854F).addBox(-1.9948F, -1.2981F, 0.1F, 3, 3, 0);
		ModelRenderer bottomTooth9 = part(base, 0, 0, -6.0891F, -10.909F, -11.0088F, 0.0F, -1.5708F, 0.4363F);
		part(bottomTooth9, 40, 52, 0.4926F, 0.2071F, -0.3F, 0.0F, 0.0F, 0.7854F).addBox(-1.9948F, -1.2981F, 0.3F, 3, 3, 0);
		ModelRenderer bottomTooth8 = part(base, 0, 0, 6.2839F, -10.779F, -8.8678F, 0.0F, -1.5708F, -0.4363F);
		part(bottomTooth8, 40, 52, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F).addBox(-1.5F, -1.5F, 0.0F, 3, 3, 0);
		ModelRenderer bottomTooth10 = part(base, 0, 0, -6.2839F, -10.7824F, -8.9729F, 0.0F, -1.5708F, 0.4363F);
		part(bottomTooth10, 40, 52, 1.1051F, 0.0035F, 0.4322F, 0.0F, 0.0F, 0.7854F).addBox(-2.2839F, -0.721F, -0.4322F, 3, 3, 0);

		bodyY = body.rotationPointY;
	}

	@Override
	public void activeAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks) {
		super.activeAnim(mimic, limbSwing, limbSwingAmount, ageInTicks);
		eye1.rotateAngleX = -1.003564F;
		eyeSocket.rotateAngleX = -0.174533F;
		tongue.rotateAngleX = -0.174533F; // 10 degrees
		bigTeeth.rotateAngleX = BIG_TEETH_OPEN_RADIANS;
	}

	@Override
	public void openAnim(MimicEntity mimic, float limbSwing, float limbSwingAmount, float ageInTicks, float timeSlice) {
		body.rotateAngleX = timeSlice * BODY_RADIANS;
		lid.rotateAngleX = timeSlice * getMaxOpenAngle();
		eye1.rotateAngleX = timeSlice * -1.003564F;
		eyeSocket.rotateAngleX = timeSlice * -0.174533F;
		tongue.rotateAngleX = timeSlice * -0.174533F;
		// swing up from the rest angle, not from 0, or a closed crate shows its teeth through the lid
		bigTeeth.rotateAngleX = BIG_TEETH_CLOSED_RADIANS + timeSlice * (BIG_TEETH_OPEN_RADIANS - BIG_TEETH_CLOSED_RADIANS);
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
