package com.someguyssoftware.treasure2.client.model;
//Made with Blockbench
//Paste this code into your mod.

import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Aug 27, 2019
 *
 */
public class OysterChestModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer hinge;
	private final ModelRenderer bottom;
	private ModelRenderer lid;

	public OysterChestModel() {
		textureWidth = 96;
		textureHeight = 96;

		hinge = new ModelRenderer(this);
		hinge.setRotationPoint(0.0F, 21.0F, 5.0F);
		hinge.cubeList.add(new ModelBox(hinge, 36, 54, -3.0F, -2.0F, 0.0F, 6, 3, 2, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(-1.0F, 24.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 0, 0, -5.0F, -1.0F, -5.0F, 12, 1, 7, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 18, 54, 6.0F, -3.0F, -5.0F, 2, 2, 7, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 50, 29, 4.0F, -3.0F, -6.0F, 2, 2, 9, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 56, 15, 2.0F, -3.0F, -7.0F, 2, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 28, 15, 0.0F, -3.0F, -7.0F, 2, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 15, -2.0F, -3.0F, -7.0F, 2, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 28, 29, -4.0F, -3.0F, -6.0F, 2, 2, 9, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 54, -6.0F, -3.0F, -5.0F, 2, 2, 7, 0.0F, false));

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 21.0F, 6.0F);
		lid.cubeList.add(new ModelBox(lid, 62, 43, 5.0F, -3.0F, -11.0F, 2, 3, 7, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 22, 43, 3.0F, -2.0F, -12.0F, 2, 2, 9, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 66, 0, 1.0F, -3.0F, -13.0F, 2, 3, 12, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 0, 29, -1.0F, -2.0F, -13.0F, 2, 2, 12, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 38, 0, -3.0F, -3.0F, -13.0F, 2, 3, 12, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 0, 43, -5.0F, -2.0F, -12.0F, 2, 2, 9, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 44, 43, -7.0F, -3.0F, -11.0F, 2, 3, 7, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		hinge.render(f5);
		bottom.render(f5);
		lid.render(f5);
	}
	
	@Override
	public void renderAll(AbstractTreasureChestTileEntity te) {
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