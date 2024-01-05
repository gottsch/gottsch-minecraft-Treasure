/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.gui.model.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.entity.monster.MoldyCrateChestMimic;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jun 26, 2023
 *
 * @param <T>
 */
public class MoldyCrateChestMimicModel<T extends Entity> extends EntityModel<T> {

	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer eyeSocket;
	private final ModelRenderer bigTeeth;


	private float bodyY;

	public MoldyCrateChestMimicModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, -7.0F);
		

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, -13.0F, 14.0F);
		body.addChild(lid);
		lid.texOffs(0, 17).addBox(-7.0F, -2.0F, -14.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);

		eyeSocket = new ModelRenderer(this);
		eyeSocket.setPos(0.0F, 1.0F, 0.0F);
		lid.addChild(eyeSocket);
		eyeSocket.texOffs(67, 31).addBox(-3.0F, -2.9F, -13.0F, 6.0F, 3.0F, 13.0F, 0.0F, false);

		eye1 = new ModelRenderer(this);
		eye1.setPos(0.0F, -2.0F, -11.0F);
		lid.addChild(eye1);
		

		ModelRenderer padTop = new ModelRenderer(this);
		padTop.setPos(-6.0F, 0.0F, 10.0F);
		eye1.addChild(padTop);
		padTop.texOffs(43, 34).addBox(4.0F, -0.2F, -13.2F, 4.0F, 2.0F, 3.0F, 0.0F, false);

		ModelRenderer latch1 = new ModelRenderer(this);
		latch1.setPos(-6.0F, 0.0F, 10.0F);
		eye1.addChild(latch1);
		latch1.texOffs(0, 20).addBox(5.0F, 0.0F, -14.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		ModelRenderer skin1 = new ModelRenderer(this);
		skin1.setPos(5.0F, 6.0F, -4.5F);
		lid.addChild(skin1);
		setRotationAngle(skin1, 0.5236F, 0.0F, 0.0F);
		skin1.texOffs(17, 46).addBox(0.9F, -7.0F, -0.5F, 0.0F, 7.0F, 6.0F, 0.0F, false);

		ModelRenderer skin2 = new ModelRenderer(this);
		skin2.setPos(-6.0F, 6.0F, -4.5F);
		lid.addChild(skin2);
		setRotationAngle(skin2, 0.5236F, 0.0F, 0.0F);
		skin2.texOffs(17, 46).addBox(0.1F, -7.0F, -0.5F, 0.0F, 7.0F, 6.0F, 0.0F, false);

		ModelRenderer topTooth1 = new ModelRenderer(this);
		topTooth1.setPos(-4.2426F, -0.1213F, -13.4F);
		lid.addChild(topTooth1);
		setRotationAngle(topTooth1, 0.4363F, 0.0F, 0.0F);
		
		ModelRenderer t_r1 = new ModelRenderer(this);
		t_r1.setPos(0.0F, 0.0F, 0.0F);
		topTooth1.addChild(t_r1);
		setRotationAngle(t_r1, 0.0F, 0.0F, 0.7854F);
		t_r1.texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth2 = new ModelRenderer(this);
		topTooth2.setPos(-2.2426F, -0.1213F, -13.5F);
		lid.addChild(topTooth2);
		setRotationAngle(topTooth2, 0.4363F, 0.0F, 0.0F);
		
		ModelRenderer t_r2 = new ModelRenderer(this);
		t_r2.setPos(0.0F, 0.0F, 0.0F);
		topTooth2.addChild(t_r2);
		setRotationAngle(t_r2, 0.0F, 0.0F, 0.7854F);
		t_r2.texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth3 = new ModelRenderer(this);
		topTooth3.setPos(-0.2426F, -0.1213F, -13.6F);
		lid.addChild(topTooth3);
		setRotationAngle(topTooth3, 0.4363F, 0.0F, 0.0F);
		

		ModelRenderer t_r3 = new ModelRenderer(this);
		t_r3.setPos(0.0F, 0.0F, 0.0F);
		topTooth3.addChild(t_r3);
		setRotationAngle(t_r3, 0.0F, 0.0F, 0.7854F);
		t_r3.texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth5 = new ModelRenderer(this);
		topTooth5.setPos(3.7574F, -0.1213F, -13.4F);
		lid.addChild(topTooth5);
		setRotationAngle(topTooth5, 0.4363F, 0.0F, 0.0F);
		

		ModelRenderer t_r4 = new ModelRenderer(this);
		t_r4.setPos(0.0F, 0.0F, 0.0F);
		topTooth5.addChild(t_r4);
		setRotationAngle(t_r4, 0.0F, 0.0F, 0.7854F);
		t_r4.texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth4 = new ModelRenderer(this);
		topTooth4.setPos(1.7574F, -0.1213F, -13.5F);
		lid.addChild(topTooth4);
		setRotationAngle(topTooth4, 0.4363F, 0.0F, 0.0F);
		
		ModelRenderer t_r5 = new ModelRenderer(this);
		t_r5.setPos(0.0F, 0.0F, 0.0F);
		topTooth4.addChild(t_r5);
		setRotationAngle(t_r5, 0.0F, 0.0F, 0.7854F);
		t_r5.texOffs(53, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth6 = new ModelRenderer(this);
		topTooth6.setPos(6.0109F, -0.2929F, -11.3749F);
		lid.addChild(topTooth6);
		setRotationAngle(topTooth6, 0.0F, -1.5708F, 0.4363F);
		

		ModelRenderer t_r6 = new ModelRenderer(this);
		t_r6.setPos(-0.318F, 0.0607F, -0.2F);
		topTooth6.addChild(t_r6);
		setRotationAngle(t_r6, 0.0F, 0.0F, 0.7854F);
		t_r6.texOffs(53, 52).addBox(-1.318F, -1.7678F, 0.2F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth8 = new ModelRenderer(this);
		topTooth8.setPos(-5.9891F, -0.2929F, -11.3749F);
		lid.addChild(topTooth8);
		setRotationAngle(topTooth8, 0.0F, -1.5708F, -0.4363F);
		

		ModelRenderer t_r7 = new ModelRenderer(this);
		t_r7.setPos(-0.318F, 0.0607F, -0.2F);
		topTooth8.addChild(t_r7);
		setRotationAngle(t_r7, 0.0F, 0.0F, 0.7854F);
		t_r7.texOffs(53, 52).addBox(-1.318F, -1.7678F, 0.2F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth7 = new ModelRenderer(this);
		topTooth7.setPos(6.534F, -0.3574F, -8.8536F);
		lid.addChild(topTooth7);
		setRotationAngle(topTooth7, 0.0F, -1.5708F, 0.4363F);
		
		ModelRenderer t_r8 = new ModelRenderer(this);
		t_r8.setPos(1.1607F, 0.1251F, -0.277F);
		topTooth7.addChild(t_r8);
		setRotationAngle(t_r8, 0.0F, 0.0F, 0.7854F);
		t_r8.texOffs(53, 52).addBox(-2.4092F, -0.7678F, 0.277F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth9 = new ModelRenderer(this);
		topTooth9.setPos(-6.466F, -0.3574F, -8.8536F);
		lid.addChild(topTooth9);
		setRotationAngle(topTooth9, 0.0F, -1.5708F, -0.4363F);
		

		ModelRenderer t_r9 = new ModelRenderer(this);
		t_r9.setPos(1.1607F, 0.1251F, -0.277F);
		topTooth9.addChild(t_r9);
		setRotationAngle(t_r9, 0.0F, 0.0F, 0.7854F);
		t_r9.texOffs(53, 52).addBox(-2.4092F, -0.7678F, 0.277F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		base = new ModelRenderer(this);
		base.setPos(0.0F, -2.0F, 14.0F);
		body.addChild(base);
		base.texOffs(45, 5).addBox(-6.0F, -10.0F, -13.0F, 12.0F, 10.0F, 12.0F, 0.0F, false);
		base.texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 2.0F, 14.0F, 0.0F, false);
		base.texOffs(0, 0).addBox(-7.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		base.texOffs(0, 0).addBox(5.0F, -10.0F, -14.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		base.texOffs(0, 0).addBox(-7.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, false);
		base.texOffs(0, 0).addBox(5.0F, -10.0F, -2.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		base.texOffs(0, 34).addBox(-7.0F, -11.0F, -14.0F, 14.0F, 1.0F, 14.0F, 0.0F, false);

		ModelRenderer crossWest = new ModelRenderer(this);
		crossWest.setPos(5.5F, -10.5F, -2.5F);
		base.addChild(crossWest);
		setRotationAngle(crossWest, -0.8029F, 0.0F, 0.0F);
		crossWest.texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, 0.0F, false);

		ModelRenderer crossEast = new ModelRenderer(this);
		crossEast.setPos(-6.5F, -9.5F, -13.5F);
		base.addChild(crossEast);
		setRotationAngle(crossEast, 0.8029F, 0.0F, 0.0F);
		crossEast.texOffs(0, 50).addBox(0.0F, 0.0F, 0.0F, 1.0F, 15.0F, 2.0F, 0.0F, false);

		ModelRenderer crossSouth = new ModelRenderer(this);
		crossSouth.setPos(5.0F, -10.5F, 0.5F);
		base.addChild(crossSouth);
		setRotationAngle(crossSouth, 0.0F, 0.0F, 0.8029F);
		crossSouth.texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, 0.0F, false);

		ModelRenderer cross = new ModelRenderer(this);
		cross.setPos(-5.0F, -10.5F, -11.5F);
		base.addChild(cross);
		setRotationAngle(cross, 0.0F, 0.0F, -0.8029F);
		cross.texOffs(7, 50).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 15.0F, 1.0F, 0.0F, false);

		ModelRenderer pad = new ModelRenderer(this);
		pad.setPos(0.0F, -11.0F, -13.2F);
		base.addChild(pad);
		pad.texOffs(0, 17).addBox(-2.0F, -0.2F, -1.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		ModelRenderer bottomTooth1 = new ModelRenderer(this);
		bottomTooth1.setPos(-3.5F, 3.0F, 0.7F);
		pad.addChild(bottomTooth1);
		setRotationAngle(bottomTooth1, -0.2618F, 0.0F, 0.0F);
		

		ModelRenderer t_r10 = new ModelRenderer(this);
		t_r10.setPos(9.5F, -1.0F, -0.5F);
		bottomTooth1.addChild(t_r10);
		setRotationAngle(t_r10, 0.0F, 0.0F, 0.7854F);
		t_r10.texOffs(43, 30).addBox(-8.0F, 5.5F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		tongue = new ModelRenderer(this);
		tongue.setPos(0.0F, -11.0F, 1.0F);
		base.addChild(tongue);
		setRotationAngle(tongue, -0.1745F, 0.0F, 0.0F);
		tongue.texOffs(5, 76).addBox(-3.0F, 0.9F, -12.5F, 6.0F, 1.0F, 8.0F, 0.0F, false);

		bigTeeth = new ModelRenderer(this);
		bigTeeth.setPos(0.0F, -10.0F, -12.0F);
		base.addChild(bigTeeth);
		setRotationAngle(bigTeeth, -0.8727F, 0.0F, 0.0F);
		

		ModelRenderer bottomTooth2 = new ModelRenderer(this);
		bottomTooth2.setPos(-3.5F, 2.0F, -1.5F);
		bigTeeth.addChild(bottomTooth2);
		

		ModelRenderer t_r11 = new ModelRenderer(this);
		t_r11.setPos(0.3076F, -3.2929F, 0.5F);
		bottomTooth2.addChild(t_r11);
		setRotationAngle(t_r11, 0.0F, 0.0F, 0.7854F);
		t_r11.texOffs(41, 57).addBox(-2.0F, -1.7071F, 0.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth5 = new ModelRenderer(this);
		bottomTooth5.setPos(2.5F, 2.0F, -0.5F);
		bigTeeth.addChild(bottomTooth5);
		

		ModelRenderer t_r12 = new ModelRenderer(this);
		t_r12.setPos(0.6612F, -2.9393F, -0.5F);
		bottomTooth5.addChild(t_r12);
		setRotationAngle(t_r12, 0.0F, 0.0F, 0.7854F);
		t_r12.texOffs(41, 57).addBox(-2.0F, -2.0607F, 0.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth4 = new ModelRenderer(this);
		bottomTooth4.setPos(-4.3F, -11.5F, -13.5F);
		base.addChild(bottomTooth4);
		setRotationAngle(bottomTooth4, 0.2182F, 0.0F, 0.0F);
		

		ModelRenderer t3_r1 = new ModelRenderer(this);
		t3_r1.setPos(0.3607F, 0.9749F, 0.5F);
		bottomTooth4.addChild(t3_r1);
		setRotationAngle(t3_r1, 0.0F, 0.0F, 0.7854F);
		t3_r1.texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth3 = new ModelRenderer(this);
		bottomTooth3.setPos(-0.3F, -11.5F, -13.5F);
		base.addChild(bottomTooth3);
		setRotationAngle(bottomTooth3, 0.2182F, 0.0F, 0.0F);
		
		ModelRenderer t4_r1 = new ModelRenderer(this);
		t4_r1.setPos(0.3607F, 0.9749F, 0.5F);
		bottomTooth3.addChild(t4_r1);
		setRotationAngle(t4_r1, 0.0F, 0.0F, 0.7854F);
		t4_r1.texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth6 = new ModelRenderer(this);
		bottomTooth6.setPos(3.7F, -11.5F, -13.5F);
		base.addChild(bottomTooth6);
		setRotationAngle(bottomTooth6, 0.2182F, 0.0F, 0.0F);
		

		ModelRenderer t5_r1 = new ModelRenderer(this);
		t5_r1.setPos(0.3607F, 0.9749F, 0.5F);
		bottomTooth6.addChild(t5_r1);
		setRotationAngle(t5_r1, 0.0F, 0.0F, 0.7854F);
		t5_r1.texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth7 = new ModelRenderer(this);
		bottomTooth7.setPos(6.1109F, -10.909F, -11.0088F);
		base.addChild(bottomTooth7);
		setRotationAngle(bottomTooth7, 0.0F, -1.5708F, -0.4363F);
		
		ModelRenderer t_r13 = new ModelRenderer(this);
		t_r13.setPos(0.4926F, 0.2071F, -0.1F);
		bottomTooth7.addChild(t_r13);
		setRotationAngle(t_r13, 0.0F, 0.0F, 0.7854F);
		t_r13.texOffs(40, 52).addBox(-1.9948F, -1.2981F, 0.1F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth9 = new ModelRenderer(this);
		bottomTooth9.setPos(-6.0891F, -10.909F, -11.0088F);
		base.addChild(bottomTooth9);
		setRotationAngle(bottomTooth9, 0.0F, -1.5708F, 0.4363F);
		
		ModelRenderer t_r14 = new ModelRenderer(this);
		t_r14.setPos(0.4926F, 0.2071F, -0.3F);
		bottomTooth9.addChild(t_r14);
		setRotationAngle(t_r14, 0.0F, 0.0F, 0.7854F);
		t_r14.texOffs(40, 52).addBox(-1.9948F, -1.2981F, 0.3F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth8 = new ModelRenderer(this);
		bottomTooth8.setPos(6.2839F, -10.779F, -8.8678F);
		base.addChild(bottomTooth8);
		setRotationAngle(bottomTooth8, 0.0F, -1.5708F, -0.4363F);
		
		ModelRenderer t_r15 = new ModelRenderer(this);
		t_r15.setPos(0.0F, 0.0F, 0.0F);
		bottomTooth8.addChild(t_r15);
		setRotationAngle(t_r15, 0.0F, 0.0F, 0.7854F);
		t_r15.texOffs(40, 52).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth10 = new ModelRenderer(this);
		bottomTooth10.setPos(-6.2839F, -10.7824F, -8.9729F);
		base.addChild(bottomTooth10);
		setRotationAngle(bottomTooth10, 0.0F, -1.5708F, 0.4363F);
		
		ModelRenderer t_r16 = new ModelRenderer(this);
		t_r16.setPos(1.1051F, 0.0035F, 0.4322F);
		bottomTooth10.addChild(t_r16);
		setRotationAngle(t_r16, 0.0F, 0.0F, 0.7854F);
		t_r16.texOffs(40, 52).addBox(-2.2839F, -0.721F, -0.4322F, 3.0F, 3.0F, 0.0F, 0.0F, false);
		
		bodyY = body.y;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		MoldyCrateChestMimic mimic = (MoldyCrateChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees
			
			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = -degToRad(mimic.getAmount() * 45);
			} else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			eye1.xRot = -1.003564F;
			eyeSocket.xRot = -0.174533F;
			tongue.xRot = -0.174533F; // 10
			bigTeeth.xRot = 0.174533F; // -10
			
			bob(body, bodyY, ageInTicks);
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				lid.xRot = mimic.getAmount() * -0.7854F;
				eye1.xRot = mimic.getAmount() * -1.003564F;
				eyeSocket.xRot = mimic.getAmount() * -0.174533F;
				tongue.xRot = mimic.getAmount() * -0.174533F;
				bigTeeth.xRot = mimic.getAmount() * 0.174533F;
			}
		}
	}

	public static void bob(ModelRenderer part, float originY, float age) {
		part.y = (float) (originY + (Math.cos(age * 0.25F) * 0.5F + 0.05F));
	}
	
	public static void bobMouth(ModelRenderer mouth, float originRot, float maxRot, float age) {
		mouth.xRot = -(degToRad((float) (originRot + Math.cos(age * 0.25f) * 3f)));
	}
	
	@Override
	public void renderToBuffer(MatrixStack poseStack, IVertexBuilder vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	protected static float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180 ;
	}
}