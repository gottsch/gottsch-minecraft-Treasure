package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jan 22, 2020
 *
 */
public class SpiderChestModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer chest;
	private final ModelRenderer headBone;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private ModelRenderer lid;

	public SpiderChestModel() {
		textureWidth = 64;
		textureHeight = 64;

		chest = new ModelRenderer(this);
		chest.setRotationPoint(-8.0F, 16.0F, 8.0F);
		chest.cubeList.add(new ModelBox(chest, 0, 0, 2.0F, -4.0F, -10.0F, 12, 5, 10, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 28, 30, 14.0F, -2.0F, -10.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 16, 44, 14.0F, -2.0F, -6.5F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 8, 44, 14.0F, -2.0F, -3.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 42, 13, 0.0F, -2.0F, -10.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 26, 42, 0.0F, -2.0F, -6.5F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 36, 36, 0.0F, -2.0F, -3.0F, 2, 10, 2, 0.0F, false));

		headBone = new ModelRenderer(this);
		headBone.setRotationPoint(0.0F, 13.0F, 8.0F);
		headBone.cubeList.add(new ModelBox(headBone, 0, 30, -4.0F, -3.75F, -16.0F, 8, 8, 6, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 13.0F, 8.0F);
		setRotationAngle(bone, -0.3927F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 44, 44, 6.0F, 4.7507F, -10.8561F, 2, 10, 2, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 13.0F, 8.0F);
		setRotationAngle(bone2, -0.3927F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 44, -8.0F, 4.7507F, -10.8561F, 2, 10, 2, 0.0F, false));

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 13.0F, 8.0F);
		lid.cubeList.add(new ModelBox(lid, 0, 15, -6.0F, -5.0F, -10.0F, 12, 5, 10, 0.01F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		chest.render(f5);
		lid.render(f5);
		bone.render(f5);
		bone2.render(f5);
		headBone.render(f5);
	}
	
	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		headBone.rotateAngleX = lid.rotateAngleX;
		chest.render(0.0625F);
		bone.render(0.0625F);
		bone2.render(0.0625F);
		headBone.render(0.0625F);
		lid.render(0.0625F);
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	/**
	 * @return the lid
	 */
	@Override
	public ModelRenderer getLid() {
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public void setLid(ModelRenderer lid) {
		this.lid = lid;
	}
}