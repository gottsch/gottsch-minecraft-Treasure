package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestModel extends ModelBase implements ITreasureChestModel {
	// fields
	ModelRenderer bottom;
	ModelRenderer back;
	ModelRenderer top;
	ModelRenderer rside;
	ModelRenderer lside;
	ModelRenderer lfront;
	ModelRenderer rfront;
	ModelRenderer backBark;
	ModelRenderer backBark2;
	ModelRenderer sideBark;
	ModelRenderer root1;
	ModelRenderer root2;

	public WitherChestModel() {
		textureWidth = 64;
		textureHeight = 128;

		bottom = new ModelRenderer(this, 0, 0);
		bottom.addBox(-7F, 0F, -14F, 14, 4, 14);
		bottom.setRotationPoint(0F, 20F, 7F);
		bottom.setTextureSize(64, 128);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		back = new ModelRenderer(this, 0, 19);
		back.addBox(-7F, 0F, -4F, 14, 18, 4);
		back.setRotationPoint(0F, 2F, 7F);
		back.setTextureSize(64, 128);
		back.mirror = true;
		setRotation(back, 0F, 0F, 0F);
		top = new ModelRenderer(this, 0, 42);
		top.addBox(-7F, 0F, -14F, 14, 4, 14);
		top.setRotationPoint(0F, -2F, 7F);
		top.setTextureSize(64, 128);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		rside = new ModelRenderer(this, 0, 62);
		rside.addBox(-4F, 0F, -6F, 4, 18, 6);
		rside.setRotationPoint(7F, 2F, 3F);
		rside.setTextureSize(64, 128);
		rside.mirror = true;
		setRotation(rside, 0F, 0F, 0F);
		lside = new ModelRenderer(this, 21, 62);
		lside.addBox(0F, 0F, -6F, 4, 18, 6);
		lside.setRotationPoint(-7F, 2F, 3F);
		lside.setTextureSize(64, 128);
		lside.mirror = true;
		setRotation(lside, 0F, 0F, 0F);
		lfront = new ModelRenderer(this, 23, 88);
		lfront.addBox(0F, 0F, -10F, 7, 18, 4);
		lfront.setRotationPoint(-7F, 2F, 3F);
		lfront.setTextureSize(64, 128);
		lfront.mirror = true;
		setRotation(lfront, 0F, 0F, 0F);
		rfront = new ModelRenderer(this, 0, 88);
		rfront.addBox(-7F, 0F, -10F, 7, 18, 4);
		rfront.setRotationPoint(7F, 2F, 3F);
		rfront.setTextureSize(64, 128);
		rfront.mirror = true;
		setRotation(rfront, 0F, 0F, 0F);
		backBark = new ModelRenderer(this, 0, 112);
		backBark.addBox(-4F, 0F, -2F, 7, 6, 2);
		backBark.setRotationPoint(4F, -8F, 7F);
		backBark.setTextureSize(64, 128);
		backBark.mirror = true;
		setRotation(backBark, 0F, 0F, 0F);
		backBark2 = new ModelRenderer(this, 19, 112);
		backBark2.addBox(-2F, 0F, 0F, 4, 4, 2);
		backBark2.setRotationPoint(-2F, -6F, 5F);
		backBark2.setTextureSize(64, 128);
		backBark2.mirror = true;
		setRotation(backBark2, 0F, 0F, 0F);
		sideBark = new ModelRenderer(this, 32, 112);
		sideBark.addBox(0F, 0F, 0F, 2, 3, 8);
		sideBark.setRotationPoint(5F, -5F, -3F);
		sideBark.setTextureSize(64, 128);
		sideBark.mirror = true;
		setRotation(sideBark, 0F, 0F, 0F);
		root1 = new ModelRenderer(this, 47, 88);
		root1.addBox(0F, 0F, 0F, 1, 1, 3);
		root1.setRotationPoint(7F, 23F, -4F);
		root1.setTextureSize(64, 128);
		root1.mirror = true;
		setRotation(root1, 0F, 0F, 0F);
		root2 = new ModelRenderer(this, 47, 94);
		root2.addBox(0F, 0F, 0F, 6, 2, 1);
		root2.setRotationPoint(-4F, 22F, -8F);
		root2.setTextureSize(64, 128);
		root2.mirror = true;
		setRotation(root2, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		bottom.render(f5);
		back.render(f5);
		top.render(f5);
		rside.render(f5);
		lside.render(f5);
		lfront.render(f5);
		rfront.render(f5);
		backBark.render(f5);
		backBark2.render(f5);
		sideBark.render(f5);
		root1.render(f5);
		root2.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
	
	/**
	 * 
	 */
	public void renderAll(ITreasureChestTileEntity te) {
		rside.rotateAngleY = rfront.rotateAngleY;
		lfront.rotateAngleY = -(rfront.rotateAngleY);
		lside.rotateAngleY = lfront.rotateAngleY;
		
		bottom.render(0.0625F);
		back.render(0.0625F);
		top.render(0.0625F);
		rside.render(0.0625F);
		lside.render(0.0625F);
		lfront.render(0.0625F);
		rfront.render(0.0625F);
		backBark.render(0.0625F);
		backBark2.render(0.0625F);
		sideBark.render(0.0625F);
		root1.render(0.0625F);
		root2.render(0.0625F);
	}

	@Override
	public ModelRenderer getLid() {
		return rfront;
	}
	
	public ModelRenderer getRightFrontDoor() {
		return rfront;
	}
	
	public ModelRenderer getLeftFrontDoor() {
		return lfront;
	}
	
	public ModelRenderer getRightSideDoor() {
		return rside;
	}
	
	public ModelRenderer getLeftSideDoor() {
		return lside;
	}
}
