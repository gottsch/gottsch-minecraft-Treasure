package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class MilkCrateModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer northFace;
	private final ModelRenderer lid;
	private final ModelRenderer padBottom;
	private final ModelRenderer southFace;
	private final ModelRenderer eastFace;
	private final ModelRenderer westFace;
	private final ModelRenderer bottomFace;

	public MilkCrateModel() {
		textureWidth = 128;
		textureHeight = 128;

		northFace = new ModelRenderer(this);
		northFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		northFace.setTextureOffset(41, 8).addBox(-7.0F, 7.0F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(42, 5).addBox(-7.0F, 3.0F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(42, 1).addBox(-7.0F, -0.9F, -14.0F, 14, 3, 1, false);
		northFace.setTextureOffset(0, 0).addBox(5.1F, -1.0F, -14.1F, 2, 11, 2, false);
		northFace.setTextureOffset(0, 0).addBox(-7.1F, -1.0F, -14.1F, 2, 11, 2, false);

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 13.0F, 7.0F);
		lid.setTextureOffset(0, 0).addBox(-7.0F, -2.0F, -14.0F, 14, 2, 14, false);
		lid.setTextureOffset(0, 16).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);
		lid.setTextureOffset(8, 8).addBox(-1.0F, -1.0F, -15.0F, 2, 2, 1, false);
		
		padBottom = new ModelRenderer(this);
		padBottom.setRotationPoint(0.0F, 15.0F, 7.0F);
		padBottom.setTextureOffset(0, 19).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);

		southFace = new ModelRenderer(this);
		southFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		southFace.setTextureOffset(42, 8).addBox(-7.0F, 7.0F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(42, 5).addBox(-7.0F, 3.0F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(42, 1).addBox(-7.0F, -0.9F, -1.0F, 14, 3, 1, false);
		southFace.setTextureOffset(0, 0).addBox(5.1F, -1.0F, -1.9F, 2, 11, 2, false);
		southFace.setTextureOffset(0, 0).addBox(-7.1F, -1.0F, -1.9F, 2, 11, 2, false);

		eastFace = new ModelRenderer(this);
		eastFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		eastFace.setTextureOffset(0, 44).addBox(-7.0F, 7.0F, -13.0F, 1, 3, 12, false);
		eastFace.setTextureOffset(26, 29).addBox(-7.0F, 3.0F, -13.0F, 1, 3, 12, false);
		eastFace.setTextureOffset(0, 29).addBox(-7.0F, -0.9F, -13.0F, 1, 3, 12, false);

		westFace = new ModelRenderer(this);
		westFace.setRotationPoint(0.0F, 14.0F, 7.0F);
		westFace.setTextureOffset(0, 44).addBox(6.0F, 7.0F, -13.0F, 1, 3, 12, false);
		westFace.setTextureOffset(26, 29).addBox(6.0F, 3.0F, -13.0F, 1, 3, 12, false);
		westFace.setTextureOffset(0, 29).addBox(6.0F, -0.9F, -13.0F, 1, 3, 12, false);

		bottomFace = new ModelRenderer(this);
		bottomFace.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottomFace.setTextureOffset(0, 16).addBox(-6.0F, -1.0F, -6.0F, 12, 1, 12, false);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		lid.render(f5);
		padBottom.render(f5);
		northFace.render(f5);
		southFace.render(f5);
		eastFace.render(f5);
		westFace.render(f5);
		bottomFace.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		float angle = 0.0625F;
		
		lid.render(angle);
		padBottom.render(angle);
		northFace.render(angle);
		southFace.render(angle);
		eastFace.render(angle);
		westFace.render(angle);
		bottomFace.render(angle);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}

}