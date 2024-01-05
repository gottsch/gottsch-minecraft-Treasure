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

import mod.gottsch.forge.treasure2.core.entity.monster.PirateChestMimic;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 */
public class PirateChestMimicModel<T extends Entity> extends EntityModel<T> {

	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer latch;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer eye2;
	private final ModelRenderer eye3;

	private float bodyY;
	
	/**
	 * 
	 * @param root
	 */
	public PirateChestMimicModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, -7.0F);
		

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, -9.0F, 14.0F);
		body.addChild(lid);
		lid.texOffs(0, 25).addBox(-7.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F, 0.0F, false);

		ModelRenderer topTooth1 = new ModelRenderer(this);
		topTooth1.setPos(-2.5F, -1.0F, -13.5F);
		lid.addChild(topTooth1);
		
		ModelRenderer t_r1 = new ModelRenderer(this);
		t_r1.setPos(2.5F, -1.0F, -0.5F);
		topTooth1.addChild(t_r1);
		setRotationAngle(t_r1, 0.0F, 0.0F, 0.7854F);
		t_r1.texOffs(7, 25).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth2 = new ModelRenderer(this);
		topTooth2.setPos(-0.5F, -1.0F, -13.5F);
		lid.addChild(topTooth2);
		
		ModelRenderer t_r2 = new ModelRenderer(this);
		t_r2.setPos(2.5F, -1.0F, -0.5F);
		topTooth2.addChild(t_r2);
		setRotationAngle(t_r2, 0.0F, 0.0F, 0.7854F);
		t_r2.texOffs(7, 25).addBox(-3.0F, 2.5F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth3 = new ModelRenderer(this);
		topTooth3.setPos(1.5F, -1.0F, -13.5F);
		lid.addChild(topTooth3);
		
		ModelRenderer t_r3 = new ModelRenderer(this);
		t_r3.setPos(2.5F, -1.0F, -0.5F);
		topTooth3.addChild(t_r3);
		setRotationAngle(t_r3, 0.0F, 0.0F, 0.7854F);
		t_r3.texOffs(7, 25).addBox(-3.0F, 2.5F, 0.4F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth4 = new ModelRenderer(this);
		topTooth4.setPos(3.5F, -1.0F, -13.5F);
		lid.addChild(topTooth4);
		

		ModelRenderer t_r4 = new ModelRenderer(this);
		t_r4.setPos(2.5F, -1.0F, -0.5F);
		topTooth4.addChild(t_r4);
		setRotationAngle(t_r4, 0.0F, 0.0F, 0.7854F);
		t_r4.texOffs(7, 25).addBox(-3.0F, 2.5F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth5 = new ModelRenderer(this);
		topTooth5.setPos(5.5F, -1.0F, -13.5F);
		lid.addChild(topTooth5);
		

		ModelRenderer t_r5 = new ModelRenderer(this);
		t_r5.setPos(2.5F, -1.0F, -0.5F);
		topTooth5.addChild(t_r5);
		setRotationAngle(t_r5, 0.0F, 0.0F, 0.7854F);
		t_r5.texOffs(7, 25).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth6 = new ModelRenderer(this);
		topTooth6.setPos(6.1109F, -0.2322F, -12.4F);
		lid.addChild(topTooth6);
		setRotationAngle(topTooth6, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r6 = new ModelRenderer(this);
		t_r6.setPos(3.8891F, -1.7678F, -0.6F);
		topTooth6.addChild(t_r6);
		setRotationAngle(t_r6, 0.0F, 0.0F, 0.7854F);
		t_r6.texOffs(7, 25).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth8 = new ModelRenderer(this);
		topTooth8.setPos(-6.8891F, -0.2322F, -12.4F);
		lid.addChild(topTooth8);
		setRotationAngle(topTooth8, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r7 = new ModelRenderer(this);
		t_r7.setPos(3.8891F, -1.7678F, -0.6F);
		topTooth8.addChild(t_r7);
		setRotationAngle(t_r7, 0.0F, 0.0F, 0.7854F);
		t_r7.texOffs(7, 25).addBox(-2.5F, 2.0F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth7 = new ModelRenderer(this);
		topTooth7.setPos(6.1109F, -0.2322F, -10.4F);
		lid.addChild(topTooth7);
		setRotationAngle(topTooth7, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r8 = new ModelRenderer(this);
		t_r8.setPos(3.8891F, -1.7678F, -0.6F);
		topTooth7.addChild(t_r8);
		setRotationAngle(t_r8, 0.0F, 0.0F, 0.7854F);
		t_r8.texOffs(7, 25).addBox(-2.5F, 2.0F, -0.1F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth9 = new ModelRenderer(this);
		topTooth9.setPos(-7.8891F, -0.2322F, -10.4F);
		lid.addChild(topTooth9);
		setRotationAngle(topTooth9, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r9 = new ModelRenderer(this);
		t_r9.setPos(3.8891F, -1.7678F, -0.6F);
		topTooth9.addChild(t_r9);
		setRotationAngle(t_r9, 0.0F, 0.0F, 0.7854F);
		t_r9.texOffs(7, 25).addBox(-2.5F, 2.0F, -0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		eye1 = new ModelRenderer(this);
		eye1.setPos(4.5F, -2.1F, -12.1F);
		lid.addChild(eye1);
		eye1.texOffs(0, 45).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		eye2 = new ModelRenderer(this);
		eye2.setPos(-3.5F, -2.1F, -12.1F);
		lid.addChild(eye2);
		eye2.texOffs(43, 30).addBox(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		eye3 = new ModelRenderer(this);
		eye3.setPos(0.5F, -3.1F, -12.1F);
		lid.addChild(eye3);
		eye3.texOffs(43, 25).addBox(-1.5F, 0.1F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		ModelRenderer skin1 = new ModelRenderer(this);
		skin1.setPos(6.0F, 2.0F, -4.5F);
		lid.addChild(skin1);
		setRotationAngle(skin1, 0.5236F, 0.0F, 0.0F);
		skin1.texOffs(0, 25).addBox(0.0F, -4.0F, -2.5F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		ModelRenderer skin2 = new ModelRenderer(this);
		skin2.setPos(-6.0F, 2.0F, -4.5F);
		lid.addChild(skin2);
		setRotationAngle(skin2, 0.5236F, 0.0F, 0.0F);
		skin2.texOffs(0, 0).addBox(0.0F, -4.0F, -2.5F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		base = new ModelRenderer(this);
		base.setPos(0.0F, -10.0F, 14.0F);
		body.addChild(base);
		base.texOffs(0, 0).addBox(-7.0F, 0.0F, -14.0F, 14.0F, 10.0F, 14.0F, 0.0F, false);

		ModelRenderer bottomTooth6 = new ModelRenderer(this);
		bottomTooth6.setPos(6.1109F, 0.7678F, -12.4F);
		base.addChild(bottomTooth6);
		setRotationAngle(bottomTooth6, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r10 = new ModelRenderer(this);
		t_r10.setPos(3.8891F, -1.7678F, -0.6F);
		bottomTooth6.addChild(t_r10);
		setRotationAngle(t_r10, 0.0F, 0.0F, 0.7854F);
		t_r10.texOffs(7, 25).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth7 = new ModelRenderer(this);
		bottomTooth7.setPos(6.1109F, 0.7678F, -10.4F);
		base.addChild(bottomTooth7);
		setRotationAngle(bottomTooth7, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r11 = new ModelRenderer(this);
		t_r11.setPos(3.8891F, -1.7678F, -0.6F);
		bottomTooth7.addChild(t_r11);
		setRotationAngle(t_r11, 0.0F, 0.0F, 0.7854F);
		t_r11.texOffs(7, 25).addBox(-2.5F, 2.0F, -0.1F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth8 = new ModelRenderer(this);
		bottomTooth8.setPos(-6.8891F, 0.7678F, -12.4F);
		base.addChild(bottomTooth8);
		setRotationAngle(bottomTooth8, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r12 = new ModelRenderer(this);
		t_r12.setPos(3.8891F, -1.7678F, -0.6F);
		bottomTooth8.addChild(t_r12);
		setRotationAngle(t_r12, 0.0F, 0.0F, 0.7854F);
		t_r12.texOffs(7, 25).addBox(-2.5F, 2.0F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth9 = new ModelRenderer(this);
		bottomTooth9.setPos(-7.8891F, 0.7678F, -10.4F);
		base.addChild(bottomTooth9);
		setRotationAngle(bottomTooth9, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r13 = new ModelRenderer(this);
		t_r13.setPos(3.8891F, -1.7678F, -0.6F);
		bottomTooth9.addChild(t_r13);
		setRotationAngle(t_r13, 0.0F, 0.0F, 0.7854F);
		t_r13.texOffs(7, 25).addBox(-2.5F, 2.0F, -0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth1 = new ModelRenderer(this);
		bottomTooth1.setPos(-3.5F, 0.0F, -12.5F);
		base.addChild(bottomTooth1);
		setRotationAngle(bottomTooth1, -0.2618F, 0.0F, 0.0F);
		
		ModelRenderer t_r14 = new ModelRenderer(this);
		t_r14.setPos(9.5F, -1.0F, -0.5F);
		bottomTooth1.addChild(t_r14);
		setRotationAngle(t_r14, 0.0F, 0.0F, 0.7854F);
		t_r14.texOffs(33, 45).addBox(-8.0F, 5.5F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth2 = new ModelRenderer(this);
		bottomTooth2.setPos(4.7F, 0.5F, -13.5F);
		base.addChild(bottomTooth2);
		setRotationAngle(bottomTooth2, 0.2182F, 0.0F, 0.0F);
		
		ModelRenderer t2_r1 = new ModelRenderer(this);
		t2_r1.setPos(-0.7F, -1.5F, -0.5F);
		bottomTooth2.addChild(t2_r1);
		setRotationAngle(t2_r1, 0.0F, 0.0F, 0.7854F);
		t2_r1.texOffs(7, 25).addBox(0.0F, -1.0F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth3 = new ModelRenderer(this);
		bottomTooth3.setPos(0.0F, 0.4F, -13.5F);
		base.addChild(bottomTooth3);
		setRotationAngle(bottomTooth3, 0.2182F, 0.0F, 0.0F);
		
		ModelRenderer t3_r1 = new ModelRenderer(this);
		t3_r1.setPos(1.0F, -1.4F, -0.5F);
		bottomTooth3.addChild(t3_r1);
		setRotationAngle(t3_r1, 0.0F, 0.0F, 0.7854F);
		t3_r1.texOffs(7, 25).addBox(-1.0F, 0.0F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth4 = new ModelRenderer(this);
		bottomTooth4.setPos(-5.3F, 0.5F, -13.5F);
		base.addChild(bottomTooth4);
		setRotationAngle(bottomTooth4, 0.2182F, 0.0F, 0.0F);
		
		ModelRenderer t3_r2 = new ModelRenderer(this);
		t3_r2.setPos(-0.7F, -1.5F, -0.5F);
		bottomTooth4.addChild(t3_r2);
		setRotationAngle(t3_r2, 0.0F, 0.0F, 0.7854F);
		t3_r2.texOffs(7, 25).addBox(0.5F, -1.5F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth5 = new ModelRenderer(this);
		bottomTooth5.setPos(3.5F, 0.0F, -12.5F);
		base.addChild(bottomTooth5);
		setRotationAngle(bottomTooth5, -0.2618F, 0.0F, 0.0F);
		
		ModelRenderer t_r15 = new ModelRenderer(this);
		t_r15.setPos(9.5F, -1.0F, -0.5F);
		bottomTooth5.addChild(t_r15);
		setRotationAngle(t_r15, 0.0F, 0.0F, 0.7854F);
		t_r15.texOffs(33, 45).addBox(-8.0F, 5.5F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomHinge1 = new ModelRenderer(this);
		bottomHinge1.setPos(3.0F, 0.0F, 0.0F);
		base.addChild(bottomHinge1);
		bottomHinge1.texOffs(43, 0).addBox(-1.0F, 0.0F, -0.9F, 2.0F, 2.0F, 1.0F, 0.0F, true);

		ModelRenderer bottomHinge2 = new ModelRenderer(this);
		bottomHinge2.setPos(-3.0F, 1.0F, 0.0F);
		base.addChild(bottomHinge2);
		bottomHinge2.texOffs(43, 0).addBox(-1.0F, -1.0F, -0.9F, 2.0F, 2.0F, 1.0F, 0.0F, false);

		latch = new ModelRenderer(this);
		latch.setPos(0.0F, 4.0F, -13.0F);
		base.addChild(latch);
		latch.texOffs(11, 45).addBox(-2.0F, -6.0F, -1.2F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		latch.texOffs(22, 45).addBox(-2.0F, -3.0F, -1.2F, 4.0F, 3.0F, 1.0F, 0.0F, false);
		latch.texOffs(7, 0).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 4.0F, 1.0F, 0.0F, false);

		tongue = new ModelRenderer(this);
		tongue.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(tongue);
		tongue.texOffs(43, 0).addBox(-3.0F, 0.9F, -12.0F, 6.0F, 1.0F, 8.0F, 0.0F, false);
		bodyY = body.y;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
	
	/**
	 * 
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		PirateChestMimic mimic = (PirateChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees
			
			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = -degToRad(mimic.getAmount() * 45);
			}
			else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			
			latch.xRot = 2.4870942F;
			eye1.xRot = -1.003564F;
			eye2.xRot = eye1.xRot;
			eye3.xRot = eye1.xRot;
			tongue.xRot = -0.174533F; // 10
			
			bob(body, bodyY, ageInTicks);
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				lid.xRot = mimic.getAmount() * -0.7854F;
				latch.xRot = mimic.getAmount() * 2.4870942F;
				eye1.xRot = mimic.getAmount() * -1.003564F;
				eye2.xRot = eye1.xRot;
				eye3.xRot = eye1.xRot;
				tongue.xRot = mimic.getAmount() * -0.174533F;
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