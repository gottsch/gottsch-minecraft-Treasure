/**
 * 
 */
package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.SafeTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author Mark Gottschling on Feb 19, 2018
 *
 */
public class SafeModel extends ModelBase implements ITreasureChestModel {

	// fields
	ModelRenderer safe;
	ModelRenderer lid;
	ModelRenderer post;
	public ModelRenderer handleA1;
	ModelRenderer handleA2;	
	ModelRenderer handleB1;
	ModelRenderer handleB2;
	ModelRenderer hinge1;
	ModelRenderer hinge2;
	ModelRenderer foot1;
	ModelRenderer foot2;
	ModelRenderer foot3;
	ModelRenderer foot4;

	/**
	 * 
	 */
	public SafeModel() {
		textureWidth = 64;
		textureHeight = 64;

		safe = new ModelRenderer(this, 0, 0);
		safe.addBox(0F, 0F, -9F, 12, 12, 9);
		safe.setRotationPoint(-6F, 11F, 6F);
		safe.setTextureSize(64, 64);
		safe.mirror = true;
		setRotation(safe, 0F, 0F, 0F);
		lid = new ModelRenderer(this, 0, 22);
		lid.addBox(0F, 0F, -3F, 10, 12, 3);
		lid.setRotationPoint(-6F, 11F, -3F);
		lid.setTextureSize(64, 64);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		post = new ModelRenderer(this, 43, 0);
		post.addBox(0F, 0F, 0F, 2, 12, 3);
		post.setRotationPoint(4F, 11F, -6F);
		post.setTextureSize(64, 64);
		post.mirror = true;
		setRotation(post, 0F, 0F, 0F);
		/*
		 * handle B - rotates around the y-axis 90 degress
		 */		
		handleB1 = new ModelRenderer(this, 27, 22);
		handleB1.addBox(-3.5F, -0.5F, -1F, 7, 1, 1);
		handleB1.setRotationPoint(-1F, 17F, -6F);
		handleB1.setTextureSize(64, 64);
		handleB1.mirror = true;
		setRotation(handleB1, 0F, 0F, 0F);
		handleB2 = new ModelRenderer(this, 27, 25);
		handleB2.addBox(-0.5F, -3.5F, -1F, 1, 7, 1);
		handleB2.setRotationPoint(-1F, 17F, -6F);
		handleB2.setTextureSize(64, 64);
		handleB2.mirror = true;
		setRotation(handleB2, 0F, 0F, 0F);
		
		/*
		 * handle A - rotates around the z-axis 180 degress
		 */
		handleA1 = new ModelRenderer(this, 27, 22);
		handleA1.addBox(-3.5F, -0.5F, -1F, 7, 1, 1);
		handleA1.setRotationPoint(-1F, 17F, -6F);
		handleA1.setTextureSize(64, 32);
		handleA1.mirror = true;
		setRotation(handleA1, 0F, 0F, 0F);
		handleA2 = new ModelRenderer(this, 27, 25);
		handleA2.addBox(-0.5F, -3.5F, -1F, 1, 7, 1);
		handleA2.setRotationPoint(-1F, 17F, -6F);
		handleA2.setTextureSize(64, 32);
		handleA2.mirror = true;
		setRotation(handleA2, 0F, 0F, 0F);
		handleB1 = new ModelRenderer(this, 27, 22);
		handleB1.addBox(1.5F, 1.5F, -4F, 7, 1, 1);
		handleB1.setRotationPoint(-6F, 15F, -3F);
		handleB1.setTextureSize(64, 32);
		handleB1.mirror = true;
		setRotation(handleB1, 0F, 0F, 0F);
		handleB2 = new ModelRenderer(this, 27, 25);
		handleB2.addBox(4.5F, -3.5F, -4F, 1, 7, 1);
		handleB2.setRotationPoint(-6F, 17F, -3F);
		handleB2.setTextureSize(64, 32);
		handleB2.mirror = true;
		setRotation(handleB2, 0F, 0F, 0F);
		hinge1 = new ModelRenderer(this, 54, 0);
		hinge1.addBox(0F, 0F, 0F, 1, 3, 1);
		hinge1.setRotationPoint(-7F, 12F, -3F);
		hinge1.setTextureSize(64, 32);
		hinge1.mirror = true;
		setRotation(hinge1, 0F, 0F, 0F);
		hinge2 = new ModelRenderer(this, 54, 5);
		hinge2.addBox(0F, 0F, 0F, 1, 3, 1);
		hinge2.setRotationPoint(-7F, 19F, -3F);
		hinge2.setTextureSize(64, 32);
		hinge2.mirror = true;
		setRotation(hinge2, 0F, 0F, 0F);
		foot1 = new ModelRenderer(this, 43, 16);
		foot1.addBox(0F, 0F, 0F, 2, 1, 2);
		foot1.setRotationPoint(4F, 23F, -6F);
		foot1.setTextureSize(64, 64);
		foot1.mirror = true;
		setRotation(foot1, 0F, 0F, 0F);
		foot2 = new ModelRenderer(this, 43, 16);
		foot2.addBox(0F, 0F, 0F, 2, 1, 2);
		foot2.setRotationPoint(4F, 23F, 4F);
		foot2.setTextureSize(64, 64);
		foot2.mirror = true;
		setRotation(foot2, 0F, 0F, 0F);
		foot3 = new ModelRenderer(this, 43, 16);
		foot3.addBox(0F, 0F, 0F, 2, 1, 2);
		foot3.setRotationPoint(-6F, 23F, -6F);
		foot3.setTextureSize(64, 64);
		foot3.mirror = true;
		setRotation(foot3, 0F, 0F, 0F);
		foot4 = new ModelRenderer(this, 43, 16);
		foot4.addBox(0F, 0F, 0F, 2, 1, 2);
		foot4.setRotationPoint(-6F, 23F, 4F);
		foot4.setTextureSize(64, 64);
		foot4.mirror = true;
		setRotation(foot4, 0F, 0F, 0F);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.someguyssoftware.treasure2.client.model.ITreasureChestModel#renderAll(com
	 * .someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity)
	 */
	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		SafeTileEntity ste = (SafeTileEntity) te;
		
		float scale = 0.0625F;

		if (ste.isHandleOpen && !ste.isLidClosed) {
			handleB1.rotateAngleY = lid.rotateAngleY;
			handleB2.rotateAngleY = lid.rotateAngleY;
			handleB1.render(scale);
			handleB2.render(scale);
		}
		else if (ste.isLidClosed) {			
			handleA2.rotateAngleZ = handleA1.rotateAngleZ;
			handleA1.render(scale);
			handleA2.render(scale);
		}
		
		safe.render(scale);
		lid.render(scale);
		post.render(scale);

		hinge1.render(scale);
		hinge2.render(scale);
		foot1.render(scale);
		foot2.render(scale);
		foot3.render(scale);
		foot4.render(scale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.treasure2.client.model.ITreasureChestModel#getLid()
	 */
	@Override
	public ModelRenderer getLid() {
		return lid;
	}

	/**
	 * 
	 */
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		safe.render(f5);
		lid.render(f5);
		post.render(f5);
		handleB1.render(f5);
		handleB2.render(f5);
		hinge1.render(f5);
		hinge2.render(f5);
		foot1.render(f5);
		foot2.render(f5);
		foot3.render(f5);
		foot4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
