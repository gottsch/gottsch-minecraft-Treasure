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

import mod.gottsch.forge.treasure2.core.entity.monster.CauldronChestMimic;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;


/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 */
public class CauldronChestMimicModel<T extends Entity> extends EntityModel<T> {

	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer eye2;
	private final ModelRenderer eye3;
	private final ModelRenderer legs;
	private final ModelRenderer frontLeg1;
	private final ModelRenderer frontLeg2;
	private final ModelRenderer backLeg1;
	private final ModelRenderer backLeg2;
	private final ModelRenderer frontSwing1;
	private final ModelRenderer frontSwing2;
	private final ModelRenderer backSwing1;
	private final ModelRenderer backSwing2;
	
	private final ModelRenderer rightLid;
	private final ModelRenderer leftLid;
	private final ModelRenderer spike1;
	private final ModelRenderer spike2;
	private final ModelRenderer spike3;
	
	/**
	 * 
	 * @param root
	 */
	public CauldronChestMimicModel() {
		texWidth = 128;
		texHeight = 128;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, -8.0F);
		

		base = new ModelRenderer(this);
		base.setPos(0.0F, -3.0F, 0.0F);
		body.addChild(base);
		base.texOffs(42, 51).addBox(6.0F, -4.0F, 0.0F, 2.0F, 4.0F, 16.0F, 0.0F, false);
		base.texOffs(21, 46).addBox(-8.0F, -4.0F, 0.0F, 2.0F, 4.0F, 16.0F, 0.0F, false);
		base.texOffs(0, 20).addBox(-6.0F, -3.0F, 2.0F, 12.0F, 3.0F, 12.0F, 0.0F, false);
		base.texOffs(63, 60).addBox(-6.0F, -4.0F, 14.0F, 12.0F, 4.0F, 2.0F, 0.0F, false);
		base.texOffs(42, 72).addBox(-6.0F, -4.0F, 0.0F, 12.0F, 4.0F, 2.0F, 0.0F, false);

		ModelRenderer bottomTooth5 = new ModelRenderer(this);
		bottomTooth5.setPos(4.5F, -7.0F, 2.5F);
		base.addChild(bottomTooth5);
		setRotationAngle(bottomTooth5, -0.2618F, 0.0F, 0.0F);
		

		ModelRenderer t_r1 = new ModelRenderer(this);
		t_r1.setPos(9.5F, 3.0F, -0.5F);
		bottomTooth5.addChild(t_r1);
		setRotationAngle(t_r1, 0.0F, 0.0F, 0.7854F);
		t_r1.texOffs(37, 26).addBox(-8.5F, 5.0F, 0.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth6 = new ModelRenderer(this);
		bottomTooth6.setPos(-4.5F, -7.0F, 2.5F);
		base.addChild(bottomTooth6);
		setRotationAngle(bottomTooth6, -0.2618F, 0.0F, 0.0F);
		

		ModelRenderer t_r2 = new ModelRenderer(this);
		t_r2.setPos(9.5F, 3.0F, -0.5F);
		bottomTooth6.addChild(t_r2);
		setRotationAngle(t_r2, 0.0F, 0.0F, 0.7854F);
		t_r2.texOffs(37, 26).addBox(-8.5F, 5.0F, 0.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth1 = new ModelRenderer(this);
		bottomTooth1.setPos(3.0F, -10.0F, 3.5F);
		base.addChild(bottomTooth1);
		setRotationAngle(bottomTooth1, 0.0F, 0.0F, 0.7854F);
		bottomTooth1.texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth2 = new ModelRenderer(this);
		bottomTooth2.setPos(0.0F, -10.0F, 3.5F);
		base.addChild(bottomTooth2);
		setRotationAngle(bottomTooth2, 0.0F, 0.0F, 0.7854F);
		bottomTooth2.texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth3 = new ModelRenderer(this);
		bottomTooth3.setPos(-3.0F, -10.0F, 3.5F);
		base.addChild(bottomTooth3);
		setRotationAngle(bottomTooth3, 0.0F, 0.0F, 0.7854F);
		bottomTooth3.texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth4 = new ModelRenderer(this);
		bottomTooth4.setPos(-6.0F, -10.0F, 3.5F);
		base.addChild(bottomTooth4);
		setRotationAngle(bottomTooth4, 0.0F, 0.0F, 0.7854F);
		bottomTooth4.texOffs(50, 0).addBox(4.0F, 1.5F, -3.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer skin1 = new ModelRenderer(this);
		skin1.setPos(6.0F, -3.0F, 11.5F);
		base.addChild(skin1);
		setRotationAngle(skin1, -0.5236F, 0.0F, 0.0F);
		skin1.texOffs(0, 36).addBox(1.0F, -5.0F, -4.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);

		ModelRenderer skin2 = new ModelRenderer(this);
		skin2.setPos(-7.0F, -3.0F, 11.5F);
		base.addChild(skin2);
		setRotationAngle(skin2, -0.5236F, 0.0F, 0.0F);
		skin2.texOffs(0, 36).addBox(0.0F, -5.0F, -4.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);

		tongue = new ModelRenderer(this);
		tongue.setPos(0.0F, -7.0F, 15.0F);
		base.addChild(tongue);
		tongue.texOffs(21, 71).addBox(-3.0F, 4.5F, -11.0F, 6.0F, 1.0F, 8.0F, 0.0F, false);

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, -7.5F, 16.0F);
		body.addChild(lid);
		lid.texOffs(0, 0).addBox(-6.0F, -7.6F, -14.0F, 12.0F, 7.0F, 12.0F, 0.0F, false);
		lid.texOffs(63, 48).addBox(-6.0F, -8.5F, -16.0F, 12.0F, 9.0F, 2.0F, 0.0F, false);
		lid.texOffs(0, 67).addBox(-6.0F, -8.5F, -2.0F, 12.0F, 9.0F, 2.0F, 0.0F, false);
		lid.texOffs(0, 36).addBox(6.0F, -8.5F, -16.0F, 2.0F, 9.0F, 16.0F, 0.0F, false);
		lid.texOffs(33, 20).addBox(-8.0F, -8.5F, -16.0F, 2.0F, 9.0F, 16.0F, 0.0F, false);

		leftLid = new ModelRenderer(this);
		leftLid.setPos(6.0F, -7.5F, -14.0F);
		lid.addChild(leftLid);
		leftLid.texOffs(54, 14).addBox(-6.0F, 0.0F, 0.0F, 6.0F, 1.0F, 12.0F, 0.0F, false);

		rightLid = new ModelRenderer(this);
		rightLid.setPos(-6.0F, -7.5F, -14.0F);
		lid.addChild(rightLid);
		rightLid.texOffs(49, 0).addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 12.0F, 0.0F, false);

		ModelRenderer topTooth1 = new ModelRenderer(this);
		topTooth1.setPos(-3.5F, -0.5F, -14.5F);
		lid.addChild(topTooth1);
		
		ModelRenderer t_r3 = new ModelRenderer(this);
		t_r3.setPos(2.5F, -1.0F, -0.5F);
		topTooth1.addChild(t_r3);
		setRotationAngle(t_r3, 0.0F, 0.0F, 0.7854F);
		t_r3.texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth2 = new ModelRenderer(this);
		topTooth2.setPos(-0.5F, -0.5F, -14.5F);
		lid.addChild(topTooth2);
		
		ModelRenderer t_r4 = new ModelRenderer(this);
		t_r4.setPos(2.5F, -1.0F, -0.5F);
		topTooth2.addChild(t_r4);
		setRotationAngle(t_r4, 0.0F, 0.0F, 0.7854F);
		t_r4.texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth3 = new ModelRenderer(this);
		topTooth3.setPos(2.5F, -0.5F, -14.5F);
		lid.addChild(topTooth3);
		
		ModelRenderer t_r5 = new ModelRenderer(this);
		t_r5.setPos(2.5F, -1.0F, -0.5F);
		topTooth3.addChild(t_r5);
		setRotationAngle(t_r5, 0.0F, 0.0F, 0.7854F);
		t_r5.texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth4 = new ModelRenderer(this);
		topTooth4.setPos(5.5F, -0.5F, -14.5F);
		lid.addChild(topTooth4);
		
		ModelRenderer t_r6 = new ModelRenderer(this);
		t_r6.setPos(2.5F, -1.0F, -0.5F);
		topTooth4.addChild(t_r6);
		setRotationAngle(t_r6, 0.0F, 0.0F, 0.7854F);
		t_r6.texOffs(37, 0).addBox(-3.0F, 2.5F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		eye1 = new ModelRenderer(this);
		eye1.setPos(4.5F, -4.5F, -14.0F);
		lid.addChild(eye1);
		eye1.texOffs(21, 36).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		eye2 = new ModelRenderer(this);
		eye2.setPos(-3.5F, -4.5F, -14.0F);
		lid.addChild(eye2);
		eye2.texOffs(0, 25).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		eye3 = new ModelRenderer(this);
		eye3.setPos(0.5F, -6.5F, -14.0F);
		lid.addChild(eye3);
		eye3.texOffs(0, 20).addBox(-1.5F, 0.0F, -2.1F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		spike1 = new ModelRenderer(this);
		spike1.setPos(0.0F, -7.5F, -14.0F);
		lid.addChild(spike1);
		spike1.texOffs(58, 34).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 12.0F, 0.0F, false);

		spike2 = new ModelRenderer(this);
		spike2.setPos(0.0F, -7.5F, -10.0F);
		lid.addChild(spike2);
		spike2.texOffs(37, 0).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

		spike3 = new ModelRenderer(this);
		spike3.setPos(0.0F, -7.5F, -6.0F);
		lid.addChild(spike3);
		spike3.texOffs(21, 42).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

		legs = new ModelRenderer(this);
		legs.setPos(0.0F, 24.0F, -8.0F);
		
		frontLeg1 = new ModelRenderer(this);
		frontLeg1.setPos(6.0F, 0.0F, 0.0F);
		legs.addChild(frontLeg1);
		
		frontSwing1 = new ModelRenderer(this);
		frontSwing1.setPos(0.0F, -3.0F, 2.0F);
		frontLeg1.addChild(frontSwing1);
		frontSwing1.texOffs(37, 20).addBox(0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		frontSwing1.texOffs(54, 28).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

		frontLeg2 = new ModelRenderer(this);
		frontLeg2.setPos(-6.0F, 0.0F, 0.0F);
		legs.addChild(frontLeg2);
		
		frontSwing2 = new ModelRenderer(this);
		frontSwing2.setPos(0.0F, -3.0F, 2.0F);
		frontLeg2.addChild(frontSwing2);
		frontSwing2.texOffs(7, 36).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		frontSwing2.texOffs(49, 14).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 3.0F, 2.0F, 0.0F, false);

		backLeg1 = new ModelRenderer(this);
		backLeg1.setPos(6.4F, 0.0F, 11.9F);
		legs.addChild(backLeg1);
		
		backSwing1 = new ModelRenderer(this);
		backSwing1.setPos(-0.4F, -6.0F, 2.1F);
		backLeg1.addChild(backSwing1);
		backSwing1.texOffs(0, 0).addBox(-0.1F, 0.0F, -2.1F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		backSwing1.texOffs(42, 46).addBox(-2.1F, 0.0F, -0.1F, 4.0F, 6.0F, 2.0F, 0.0F, false);

		backLeg2 = new ModelRenderer(this);
		backLeg2.setPos(-6.0F, 0.0F, 12.0F);
		legs.addChild(backLeg2);
		
		backSwing2 = new ModelRenderer(this);
		backSwing2.setPos(0.0F, -6.0F, 2.0F);
		backLeg2.addChild(backSwing2);
		backSwing2.texOffs(0, 0).addBox(-1.9F, 0.0F, -2.1F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		backSwing2.texOffs(42, 46).addBox(-1.9F, 0.0F, -0.1F, 4.0F, 6.0F, 2.0F, 0.0F, false);
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
		CauldronChestMimic mimic = (CauldronChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees
			frontLeg1.xRot = body.xRot;
			frontLeg2.xRot = body.xRot;
			backLeg1.xRot = body.xRot;
			backLeg2.xRot = body.xRot;
			
			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = mimic.getAmount() * -0.7854F; // TODO could use sin or cos method like legs
			}
			else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			rightLid.zRot = -2.26893F; //130
			leftLid.zRot = -rightLid.zRot;
			spike1.xRot =  1.13446F; // 65
			spike2.xRot =  0.959931F; //
			spike3.xRot = 0.7854F; // 45
			
			eye1.xRot = -1.003564F;
			eye2.xRot = eye1.xRot;
			eye3.xRot = eye1.xRot;
			tongue.xRot = -0.174533F; // 10
			
			// swing legs
			frontSwing1.xRot = (float) (Math.cos(limbSwing * 0.349066f * 1f) * 1.4F * limbSwingAmount);
			backSwing1.xRot = (float) (Math.cos(limbSwing * 0.349066f  * 1f + (float)Math.PI) * 1.4F * limbSwingAmount);
			
			frontSwing2.xRot = backSwing1.xRot;
			backSwing2.xRot = frontSwing1.xRot;
			
			// bob spikes
			bob(spike1, ageInTicks, 0.26f, -1);
			bob(spike2, ageInTicks, 0.26f, -1);
			bob(spike3, ageInTicks, 0.26f, -1);
			
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				frontLeg1.xRot = body.xRot;
				frontLeg2.xRot = body.xRot;
				backLeg1.xRot = body.xRot;
				backLeg2.xRot = body.xRot;				
				
				lid.xRot = mimic.getAmount() * -0.7854F;
				rightLid.zRot = mimic.getAmount() * -2.26893F; //130
				leftLid.zRot = -rightLid.zRot;
				spike1.xRot =  mimic.getAmount() * 1.13446F; // 65
				spike2.xRot =  mimic.getAmount() * 0.959931F; //
				spike3.xRot = mimic.getAmount() * 0.7854F; // 45
				
				eye1.xRot = mimic.getAmount() * -1.003564F;
				eye2.xRot = eye1.xRot;
				eye3.xRot = eye1.xRot;
				tongue.xRot = mimic.getAmount() * -0.174533F;
			}
		}
	}

	public static void bob(ModelRenderer part, float age, float radians, int direction) {
		part.xRot += direction * (Math.sin(age * 0.05F) * radians + 0.05F);
	}
	
	public static void bobMouth(ModelRenderer mouth, float originRot, float maxRot, float age) {
		mouth.xRot = -(degToRad((float) (originRot + Math.cos(age * 0.25f) * 3f)));
	}
	
	@Override
	public void renderToBuffer(MatrixStack poseStack, IVertexBuilder vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	
	protected static float degToRad(float degrees) {
		return degrees * (float)Math.PI / 180 ;
	}
}