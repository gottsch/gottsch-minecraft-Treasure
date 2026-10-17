package com.someguyssoftware.treasure2.client.model;

import com.someguyssoftware.treasure2.tileentity.ITreasureChestTileEntity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * A chest that looks like the vanilla chest, ported from the 1.20.1 VanillaChestModel.
 *
 * @author Mark Gottschling on 10/16/2026
 */
public class VanillaChestModel extends ModelBase implements ITreasureChestModel {
	private final ModelRenderer body;
	private final ModelRenderer lid;

	public VanillaChestModel() {
		textureWidth = 128;
		textureHeight = 128;

		body = new ModelRenderer(this, 0, 0);
		body.setRotationPoint(0.0F, 24.0F, -7.0F);

		lid = new ModelRenderer(this, 0, 25);
		lid.setRotationPoint(0.0F, -9.0F, 14.0F);
		lid.addBox(-7.0F, -5.0F, -14.0F, 14, 5, 14);
		body.addChild(lid);

		ModelRenderer base = new ModelRenderer(this, 0, 0);
		base.setRotationPoint(0.0F, -10.0F, 14.0F);
		base.addBox(-7.0F, 0.0F, -14.0F, 14, 10, 14);
		body.addChild(base);

		ModelRenderer latch = new ModelRenderer(this, 7, 0);
		latch.setRotationPoint(0.0F, 3.0F, -14.0F);
		latch.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 1);
		base.addChild(latch);
	}

	@Override
	public void renderAll(ITreasureChestTileEntity te) {
		body.render(0.0625F);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}
