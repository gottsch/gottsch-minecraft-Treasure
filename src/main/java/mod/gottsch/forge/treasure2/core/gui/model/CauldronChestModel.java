package mod.gottsch.forge.treasure2.core.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

public class CauldronChestModel extends AbstractTreasureChestModel {
	// fields
	ModelRenderer front;
	ModelRenderer back;
	ModelRenderer left;
	ModelRenderer right;
	ModelRenderer bottom;
	ModelRenderer flFoot;
	ModelRenderer frFoot;
	ModelRenderer blFoot;
	ModelRenderer brFoot;
	ModelRenderer fl2;
	ModelRenderer fr2;
	ModelRenderer bl2;
	ModelRenderer br2;
	ModelRenderer lidLeft;
	ModelRenderer lidRight;

	public CauldronChestModel() {
		super(RenderType::entityCutout);

		texWidth = 128;
		texHeight = 128;

		front = new ModelRenderer(this, 0, 0);
		front.addBox(0F, 0F, 0F, 12, 13, 2);
		front.setPos(-6F, 8F, -8F);
		front.setTexSize(64, 32);
		front.mirror = true;
		setRotation(front, 0F, 0F, 0F);
		back = new ModelRenderer(this, 32, 0);
		back.addBox(0F, 0F, 0F, 12, 13, 2);
		back.setPos(-6F, 8F, 6F);
		back.setTexSize(64, 32);
		back.mirror = true;
		setRotation(back, 0F, 0F, 0F);
		left = new ModelRenderer(this, 0, 18);
		left.addBox(0F, 0F, 0F, 2, 13, 16);
		left.setPos(6F, 8F, -8F);
		left.setTexSize(64, 32);
		left.mirror = true;
		setRotation(left, 0F, 0F, 0F);
		right = new ModelRenderer(this, 38, 18);
		right.addBox(0F, 0F, 0F, 2, 13, 16);
		right.setPos(-8F, 8F, -8F);
		right.setTexSize(64, 32);
		right.mirror = true;
		setRotation(right, 0F, 0F, 0F);
		bottom = new ModelRenderer(this, 0, 50);
		bottom.addBox(0F, 0F, 0F, 12, 1, 12);
		bottom.setPos(-6F, 20F, -6F);
		bottom.setTexSize(64, 32);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		flFoot = new ModelRenderer(this, 0, 80);
		flFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		flFoot.setPos(4F, 21F, -8F);
		flFoot.setTexSize(64, 32);
		flFoot.mirror = true;
		setRotation(flFoot, 0F, 0F, 0F);
		frFoot = new ModelRenderer(this, 14, 80);
		frFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		frFoot.setPos(-8F, 21F, -8F);
		frFoot.setTexSize(64, 32);
		frFoot.mirror = true;
		setRotation(frFoot, 0F, 0F, 0F);
		blFoot = new ModelRenderer(this, 28, 80);
		blFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		blFoot.setPos(4F, 21F, 6F);
		blFoot.setTexSize(64, 32);
		blFoot.mirror = true;
		setRotation(blFoot, 0F, 0F, 0F);
		brFoot = new ModelRenderer(this, 42, 80);
		brFoot.addBox(0F, 0F, 0F, 4, 3, 2);
		brFoot.setPos(-8F, 21F, 6F);
		brFoot.setTexSize(64, 32);
		brFoot.mirror = true;
		setRotation(brFoot, 0F, 0F, 0F);
		fl2 = new ModelRenderer(this, 0, 88);
		fl2.addBox(0F, 0F, 0F, 2, 3, 2);
		fl2.setPos(6F, 21F, -6F);
		fl2.setTexSize(64, 32);
		fl2.mirror = true;
		setRotation(fl2, 0F, 0F, 0F);
		fr2 = new ModelRenderer(this, 10, 88);
		fr2.addBox(0F, 0F, 0F, 2, 3, 2);
		fr2.setPos(-8F, 21F, -6F);
		fr2.setTexSize(64, 32);
		fr2.mirror = true;
		setRotation(fr2, 0F, 0F, 0F);
		bl2 = new ModelRenderer(this, 20, 88);
		bl2.addBox(0F, 0F, 0F, 2, 3, 2);
		bl2.setPos(6F, 21F, 4F);
		bl2.setTexSize(64, 32);
		bl2.mirror = true;
		setRotation(bl2, 0F, 0F, 0F);
		br2 = new ModelRenderer(this, 30, 88);
		br2.addBox(0F, 0F, 0F, 2, 3, 2);
		br2.setPos(-8F, 21F, 4F);
		br2.setTexSize(64, 32);
		br2.mirror = true;
		setRotation(br2, 0F, 0F, 0F);
		lidLeft = new ModelRenderer(this, 0, 65);
		lidLeft.addBox(-6F, 0F, 0F, 6, 1, 12);
		lidLeft.setPos(6F, 9F, -6F);
		lidLeft.setTexSize(64, 32);
		lidLeft.mirror = true;
		setRotation(lidLeft, 0F, 0F, 0F);
		lidRight = new ModelRenderer(this, 38, 65);
		lidRight.addBox(0F, 0F, 0F, 6, 1, 12);
		lidRight.setPos(-6F, 9F, -6F);
		lidRight.setTexSize(64, 32);
		lidRight.mirror = true;
		setRotation(lidRight, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		// set the angles of the latch to same as the lib
		lidRight.zRot = -lidLeft.zRot;

		front.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		back.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		left.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		right.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lidLeft.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lidRight.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		flFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		fl2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		frFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		fr2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		blFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bl2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		brFoot.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		br2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);		
	}

	@Override
	public ModelRenderer getLid() {
		return lidLeft;
	}

}