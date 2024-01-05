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

import mod.gottsch.forge.treasure2.core.entity.monster.VikingChestMimic;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jun 8, 2023
 *
 * @param <T>
 */
public class VikingChestMimicModel<T extends Entity> extends EntityModel<T> {

	private final ModelRenderer body;
	private final ModelRenderer lid;
	private final ModelRenderer base;
	private final ModelRenderer tongue;
	private final ModelRenderer eye1;
	private final ModelRenderer topLatch;
	private final ModelRenderer frontLatch;

	private float bodyY;

	/**
	 * 
	 * @param root
	 */
	public VikingChestMimicModel() {
		texWidth = 64;
		texHeight = 64;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, -5.0F);
		

		base = new ModelRenderer(this);
		base.setPos(8.0F, 0.0F, -3.0F);
		body.addChild(base);
		base.texOffs(0, 0).addBox(-15.0F, -13.0F, 3.0F, 14.0F, 10.0F, 10.0F, 0.0F, false);
		base.texOffs(22, 42).addBox(-10.0F, -12.0F, 2.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);
		base.texOffs(0, 35).addBox(-16.0F, -5.5F, 6.0F, 16.0F, 2.0F, 4.0F, 0.0F, false);
		base.texOffs(39, 11).addBox(-15.0F, -3.0F, 3.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
		base.texOffs(39, 11).addBox(-3.0F, -3.0F, 3.0F, 2.0F, 3.0F, 10.0F, 0.0F, false);
		base.texOffs(11, 42).addBox(-1.0F, -11.0F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		base.texOffs(0, 42).addBox(-16.0F, -11.0F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

		tongue = new ModelRenderer(this);
		tongue.setPos(-8.0F, -12.5F, 13.0F);
		base.addChild(tongue);
		tongue.texOffs(33, 35).addBox(-3.0F, -0.6F, -9.0F, 6.0F, 1.0F, 8.0F, 0.0F, false);

		ModelRenderer front = new ModelRenderer(this);
		front.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(front);
		front.texOffs(0, 21).addBox(-2.5F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.texOffs(0, 21).addBox(-2.5F, -8.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.texOffs(0, 21).addBox(-14.5F, -8.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		front.texOffs(0, 21).addBox(-14.5F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		ModelRenderer side1 = new ModelRenderer(this);
		side1.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(side1);
		side1.texOffs(0, 21).addBox(-1.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.texOffs(0, 21).addBox(-15.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.texOffs(0, 21).addBox(-1.5F, -5.0F, 11.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side1.texOffs(0, 21).addBox(-1.5F, -11.0F, 11.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		ModelRenderer side2 = new ModelRenderer(this);
		side2.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(side2);
		side2.texOffs(0, 21).addBox(-15.5F, -5.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.texOffs(0, 21).addBox(-1.5F, -11.0F, 3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.texOffs(0, 21).addBox(-15.5F, -5.0F, 11.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		side2.texOffs(0, 21).addBox(-15.5F, -11.0F, 11.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		ModelRenderer back = new ModelRenderer(this);
		back.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(back);
		back.texOffs(0, 21).addBox(-2.5F, -2.0F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 21).addBox(-2.5F, -8.0F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 21).addBox(-14.5F, -8.0F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		back.texOffs(0, 21).addBox(-14.5F, -2.0F, 12.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		ModelRenderer bottomTooth1 = new ModelRenderer(this);
		bottomTooth1.setPos(-5.0F, -13.0F, 3.5F);
		base.addChild(bottomTooth1);
		setRotationAngle(bottomTooth1, 0.0F, 0.0F, 0.7854F);
		bottomTooth1.texOffs(0, 0).addBox(-1.9393F, -1.7678F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth2 = new ModelRenderer(this);
		bottomTooth2.setPos(-11.0F, -13.0F, 3.5F);
		base.addChild(bottomTooth2);
		setRotationAngle(bottomTooth2, 0.0F, 0.0F, 0.7854F);
		bottomTooth2.texOffs(0, 0).addBox(-1.9393F, -1.7678F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth3 = new ModelRenderer(this);
		bottomTooth3.setPos(-14.182F, -12.2322F, 5.5F);
		base.addChild(bottomTooth3);
		setRotationAngle(bottomTooth3, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r1 = new ModelRenderer(this);
		t_r1.setPos(3.182F, -1.7678F, -0.5F);
		bottomTooth3.addChild(t_r1);
		setRotationAngle(t_r1, 0.0F, 0.0F, 0.7854F);
		t_r1.texOffs(0, 5).addBox(-3.0F, 2.0F, 0.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer bottomTooth4 = new ModelRenderer(this);
		bottomTooth4.setPos(-2.182F, -12.2322F, 5.5F);
		base.addChild(bottomTooth4);
		setRotationAngle(bottomTooth4, 0.0F, -1.5708F, 0.0F);
		
		ModelRenderer t_r2 = new ModelRenderer(this);
		t_r2.setPos(3.182F, -1.7678F, -0.5F);
		bottomTooth4.addChild(t_r2);
		setRotationAngle(t_r2, 0.0F, 0.0F, 0.7854F);
		t_r2.texOffs(0, 5).addBox(-3.0F, 2.0F, -0.1F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		frontLatch = new ModelRenderer(this);
		frontLatch.setPos(0.0F, -10.5F, -0.5F);
		body.addChild(frontLatch);
		frontLatch.texOffs(1, 49).addBox(-1.0F, -4.0F, -0.25F, 2.0F, 4.0F, 1.0F, 0.0F, false);

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, -12.0F, 10.0F);
		body.addChild(lid);
		lid.texOffs(0, 21).addBox(-7.0F, -3.0F, -10.0F, 14.0F, 3.0F, 10.0F, 0.0F, false);
		lid.texOffs(0, 21).addBox(5.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.texOffs(0, 21).addBox(-6.5F, -2.0F, -10.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.texOffs(0, 21).addBox(5.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		lid.texOffs(0, 21).addBox(-6.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		topLatch = new ModelRenderer(this);
		topLatch.setPos(0.0F, -3.0F, -2.75F);
		lid.addChild(topLatch);
		topLatch.texOffs(39, 0).addBox(-1.0F, -0.5F, -8.0F, 2.0F, 1.0F, 8.0F, 0.0F, false);

		ModelRenderer topTooth6 = new ModelRenderer(this);
		topTooth6.setPos(6.818F, -0.2322F, -7.5F);
		lid.addChild(topTooth6);
		setRotationAngle(topTooth6, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r3 = new ModelRenderer(this);
		t_r3.setPos(3.182F, -1.7678F, -0.5F);
		topTooth6.addChild(t_r3);
		setRotationAngle(t_r3, 0.0F, 0.0F, 0.7854F);
		t_r3.texOffs(0, 5).addBox(-2.5F, 2.0F, 1.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth7 = new ModelRenderer(this);
		topTooth7.setPos(-6.182F, -0.2322F, -7.5F);
		lid.addChild(topTooth7);
		setRotationAngle(topTooth7, 0.0F, -1.5708F, 0.0F);
		

		ModelRenderer t_r4 = new ModelRenderer(this);
		t_r4.setPos(3.182F, -1.7678F, -0.5F);
		topTooth7.addChild(t_r4);
		setRotationAngle(t_r4, 0.0F, 0.0F, 0.7854F);
		t_r4.texOffs(0, 5).addBox(-2.5F, 2.0F, 0.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth1 = new ModelRenderer(this);
		topTooth1.setPos(-2.5F, -6.0F, -11.5F);
		lid.addChild(topTooth1);
		

		ModelRenderer t_r5 = new ModelRenderer(this);
		t_r5.setPos(2.5F, -1.0F, -0.5F);
		topTooth1.addChild(t_r5);
		setRotationAngle(t_r5, 0.0F, 0.0F, 0.7854F);
		t_r5.texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth2 = new ModelRenderer(this);
		topTooth2.setPos(0.5F, -6.0F, -11.5F);
		lid.addChild(topTooth2);
		

		ModelRenderer t_r6 = new ModelRenderer(this);
		t_r6.setPos(2.5F, -1.0F, -0.5F);
		topTooth2.addChild(t_r6);
		setRotationAngle(t_r6, 0.0F, 0.0F, 0.7854F);
		t_r6.texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth4 = new ModelRenderer(this);
		topTooth4.setPos(3.5F, -6.0F, -11.5F);
		lid.addChild(topTooth4);
		

		ModelRenderer t_r7 = new ModelRenderer(this);
		t_r7.setPos(2.5F, -1.0F, -0.5F);
		topTooth4.addChild(t_r7);
		setRotationAngle(t_r7, 0.0F, 0.0F, 0.7854F);
		t_r7.texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth5 = new ModelRenderer(this);
		topTooth5.setPos(6.5F, -6.0F, -11.5F);
		lid.addChild(topTooth5);
		

		ModelRenderer t_r8 = new ModelRenderer(this);
		t_r8.setPos(2.5F, -1.0F, -0.5F);
		topTooth5.addChild(t_r8);
		setRotationAngle(t_r8, 0.0F, 0.0F, 0.7854F);
		t_r8.texOffs(0, 5).addBox(0.0F, 6.5F, 2.6F, 3.0F, 3.0F, 0.0F, 0.0F, false);

		ModelRenderer topTooth3 = new ModelRenderer(this);
		topTooth3.setPos(-1.0F, 1.0F, -8.0F);
		lid.addChild(topTooth3);
		setRotationAngle(topTooth3, 0.0F, 0.0F, 0.7854F);
		topTooth3.texOffs(0, 0).addBox(-2.5F, -4.0F, -1.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);

		eye1 = new ModelRenderer(this);
		eye1.setPos(0.0F, -3.0F, -8.1F);
		lid.addChild(eye1);
		eye1.texOffs(39, 25).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

		ModelRenderer skin1 = new ModelRenderer(this);
		skin1.setPos(6.0F, 1.0F, -3.0F);
		lid.addChild(skin1);
		setRotationAngle(skin1, 0.5236F, 0.0F, 0.0F);
		skin1.texOffs(5, 49).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);

		ModelRenderer skin2 = new ModelRenderer(this);
		skin2.setPos(-6.0F, 1.0F, -3.0F);
		lid.addChild(skin2);
		setRotationAngle(skin2, 0.5236F, 0.0F, 0.0F);
		skin2.texOffs(5, 49).addBox(0.0F, -3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, false);
		
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
		VikingChestMimic mimic = (VikingChestMimic)entity;
		if (mimic.isActive()) {
			body.xRot = 0.2618F; // 15 degrees

			// chomp lid
			if (mimic.hasTarget()) {
				lid.xRot = -degToRad(mimic.getAmount() * 45);
			} else {
//				lid.xRot = -degToRad(22.5f);
				bobMouth(lid, 22.5f, 25f, ageInTicks);
			}
			topLatch.xRot = -0.2618F;
			eye1.xRot = -1.003564F;
			tongue.xRot = -0.174533F; // 10
			frontLatch.xRot = 3.14159F;

			bob(body, bodyY, ageInTicks);
		} else {
			if (mimic.getAmount() < 1F) {
				body.xRot = mimic.getAmount() * 0.2618F;
				lid.xRot = mimic.getAmount() * -0.7854F;
				topLatch.xRot = mimic.getAmount() * -0.2618F;
				frontLatch.xRot = mimic.getAmount() * 3.14159F;
				eye1.xRot = mimic.getAmount() * -1.003564F;
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