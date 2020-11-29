/**
 * 
 */
package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @author Mark Gottschling on Apr 19, 2020
 *
 */
public class VikingChestModel  extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer mainBox;
	private final ModelRenderer lid;
	private final ModelRenderer front;
	private final ModelRenderer side1;
	private final ModelRenderer side2;
	private final ModelRenderer back;

	public VikingChestModel() {
		textureWidth = 64;
		textureHeight = 64;

		mainBox = new ModelRenderer(this);
		mainBox.setRotationPoint(0.0F, 24.0F, 0.0F);
		mainBox.cubeList.add(new ModelBox(mainBox, 0, 0, -7.0F, -13.0F, -5.0F, 14, 10, 10, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 0, 20, -2.0F, -12.0F, -5.5F, 4, 4, 1, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 0, 33, -8.0F, -5.5F, -2.0F, 16, 2, 4, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 38, 10, -7.0F, -3.0F, -5.0F, 2, 3, 10, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 30, 33, 5.0F, -3.0F, -5.0F, 2, 3, 10, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 0, 5, 7.0F, -11.0F, -2.0F, 1, 1, 4, 0.0F, false));
		mainBox.cubeList.add(new ModelBox(mainBox, 0, 0, -8.0F, -11.0F, -2.0F, 1, 1, 4, 0.0F, false));

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, 12.0F, 5.0F);
		lid.cubeList.add(new ModelBox(lid, 38, 0, -1.0F, -3.5F, -10.75F, 2, 1, 8, 0.0F, false)); // topLatchTop
		lid.cubeList.add(new ModelBox(lid, 0, 25, -1.0F, -2.5F, -10.75F, 2, 4, 1, 0.0F, false)); // topLatchFront
		lid.cubeList.add(new ModelBox(lid, 0, 20, -7.0F, -3.0F, -10.0F, 14, 3, 10, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 38, 8, 5.5F, -2.0F, -10.5F, 1, 1, 1, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 38, 6, -6.5F, -2.0F, -10.5F, 1, 1, 1, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 38, 4, 5.5F, -2.0F, -0.5F, 1, 1, 1, 0.0F, false));
		lid.cubeList.add(new ModelBox(lid, 38, 2, -6.5F, -2.0F, -0.5F, 1, 1, 1, 0.0F, false));

		front = new ModelRenderer(this);
		front.setRotationPoint(0.0F, 24.0F, 0.0F);
		front.cubeList.add(new ModelBox(front, 38, 0, 5.5F, -2.0F, -5.5F, 1, 1, 1, 0.0F, false));
		front.cubeList.add(new ModelBox(front, 36, 35, 5.5F, -8.0F, -5.5F, 1, 1, 1, 0.0F, false));
		front.cubeList.add(new ModelBox(front, 36, 33, -6.5F, -8.0F, -5.5F, 1, 1, 1, 0.0F, false));
		front.cubeList.add(new ModelBox(front, 0, 35, -6.5F, -2.0F, -5.5F, 1, 1, 1, 0.0F, false));

		side1 = new ModelRenderer(this);
		side1.setRotationPoint(0.0F, 24.0F, 0.0F);
		side1.cubeList.add(new ModelBox(side1, 0, 33, 6.5F, -5.0F, -4.5F, 1, 1, 1, 0.0F, false));
		side1.cubeList.add(new ModelBox(side1, 4, 27, -7.5F, -11.0F, -4.5F, 1, 1, 1, 0.0F, false));
		side1.cubeList.add(new ModelBox(side1, 0, 27, 6.5F, -5.0F, 3.5F, 1, 1, 1, 0.0F, false));
		side1.cubeList.add(new ModelBox(side1, 4, 25, 6.5F, -11.0F, 3.5F, 1, 1, 1, 0.0F, false));

		side2 = new ModelRenderer(this);
		side2.setRotationPoint(0.0F, 24.0F, 0.0F);
		side2.cubeList.add(new ModelBox(side2, 0, 25, -7.5F, -5.0F, -4.5F, 1, 1, 1, 0.0F, false));
		side2.cubeList.add(new ModelBox(side2, 0, 7, 6.5F, -11.0F, -4.5F, 1, 1, 1, 0.0F, false));
		side2.cubeList.add(new ModelBox(side2, 6, 2, -7.5F, -5.0F, 3.5F, 1, 1, 1, 0.0F, false));
		side2.cubeList.add(new ModelBox(side2, 6, 0, -7.5F, -11.0F, 3.5F, 1, 1, 1, 0.0F, false));

		back = new ModelRenderer(this);
		back.setRotationPoint(0.0F, 24.0F, 0.0F);
		back.cubeList.add(new ModelBox(back, 6, 6, 5.5F, -2.0F, 4.5F, 1, 1, 1, 0.0F, false));
		back.cubeList.add(new ModelBox(back, 0, 5, 5.5F, -8.0F, 4.5F, 1, 1, 1, 0.0F, false));
		back.cubeList.add(new ModelBox(back, 0, 2, -6.5F, -8.0F, 4.5F, 1, 1, 1, 0.0F, false));
		back.cubeList.add(new ModelBox(back, 0, 0, -6.5F, -2.0F, 4.5F, 1, 1, 1, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		mainBox.render(f5);
		lid.render(f5);
		front.render(f5);
		side1.render(f5);
		side2.render(f5);
		back.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		mainBox.render(0.0625F);
		lid.render(0.0625F);
		side1.render(0.0625F);
		side2.render(0.0625F);
		front.render(0.0625F);
		back.render(0.0625F);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
