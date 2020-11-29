package com.someguyssoftware.treasure2.client.model;
//Made with Blockbench
//Paste this code into your mod.

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ClamChestModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer hinge;
	private final ModelRenderer bottom;
	private ModelRenderer lid;

	public ClamChestModel() {
		textureWidth = 96;
		textureHeight = 96;

		hinge = new ModelRenderer(this);
		hinge.setRotationPoint(0.0F, 21.0F, 6.0F);
		hinge.cubeList.add(new ModelBox(hinge, 22, 35, -3.0F, -1.0F, -1.0F, 8, 2, 2, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 40, 14, -6.0F, -1.0F, -4.0F, 12, 1, 8, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 48, 0, -6.0F, -3.0F, -7.0F, 12, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 44, 23, 6.0F, -3.0F, -6.0F, 1, 2, 10, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 23, -7.0F, -3.0F, -6.0F, 1, 2, 10, 0.0F, false));

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 21.0F, 6.0F);
		lid.cubeList.add(new ModelBox(lid, 0, 14, -6.0F, -3.0F, -10.0F, 12, 1, 8, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 0, 0, -6.0F, -2.0F, -13.0F, 12, 2, 12, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 0, 35, 6.0F, -2.0F, -12.0F, 1, 2, 10, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 22, 23, -7.0F, -2.0F, -12.0F, 1, 2, 10, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		hinge.render(f5);
		bottom.render(f5);
		lid.render(f5);
	}
	
	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		bottom.render(0.0625F);
		lid.render(0.0625F);
		hinge.render(0.0625F);	
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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