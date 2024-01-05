package mod.gottsch.forge.treasure2.core.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Mar 13, 2018
 *
 */
public class DreadPirateChestModel extends AbstractTreasureChestModel {

	ModelRenderer lid;
	ModelRenderer baseMiddle;
	ModelRenderer baseSide1;
	ModelRenderer baseSide2;
	ModelRenderer bottom;
	ModelRenderer skullTop;
	ModelRenderer skullBottom;
	ModelRenderer topHinge1;
	ModelRenderer topHinge2;
	ModelRenderer bottomHinge1;
	ModelRenderer bottomHinge2;
	ModelRenderer corner1;
	ModelRenderer corner2;
	ModelRenderer handle1;
	ModelRenderer handle2;

	/**
	 * 
	 */
	public DreadPirateChestModel() {
		super(RenderType::entityCutout);
		
		texWidth = 128;
		texHeight = 128;

		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-7F, -4F, -14F, 14, 5, 14);
		lid.setPos(0F, 14F, 7F);
		lid.setTexSize(128, 128);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		baseMiddle = new ModelRenderer(this, 0, 21);
		baseMiddle.addBox(-3F, 0F, -11F, 6, 9, 12);
		baseMiddle.setPos(0F, 14F, 5F);
		baseMiddle.setTexSize(128, 128);
		baseMiddle.mirror = true;
		setRotation(baseMiddle, 0F, 0F, 0F);
		baseSide1 = new ModelRenderer(this, 0, 44);
		baseSide1.addBox(-2F, 0F, -12F, 4, 9, 14);
		baseSide1.setPos(5F, 14F, 5F);
		baseSide1.setTexSize(128, 128);
		baseSide1.mirror = true;
		setRotation(baseSide1, 0F, 0F, 0F);
		baseSide2 = new ModelRenderer(this, 0, 70);
		baseSide2.addBox(-2F, 0F, -12F, 4, 9, 14);
		baseSide2.setPos(-5F, 14F, 5F);
		baseSide2.setTexSize(128, 128);
		baseSide2.mirror = true;
		setRotation(baseSide2, 0F, 0F, 0F);
		bottom = new ModelRenderer(this, 0, 96);
		bottom.addBox(-7F, 0F, -14F, 14, 2, 14);
		bottom.setPos(0F, 22F, 7F);
		bottom.setTexSize(128, 128);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		skullTop = new ModelRenderer(this, 57, 0);
		skullTop.addBox(-3F, -3F, -15F, 6, 5, 2);
		skullTop.setPos(0F, 15F, 7F);
		skullTop.setTexSize(128, 128);
		skullTop.mirror = true;
		setRotation(skullTop, 0F, 0F, 0F);
		skullBottom = new ModelRenderer(this, 57, 8);
		skullBottom.addBox(-2F, 0F, -1F, 4, 3, 1);
		skullBottom.setPos(0F, 17F, -6F);
		skullBottom.setTexSize(128, 128);
		skullBottom.mirror = true;
		setRotation(skullBottom, 0F, 0F, 0F);
		topHinge1 = new ModelRenderer(this, 57, 14);
		topHinge1.addBox(-1F, -4.5F, -4.5F, 2, 4, 5);
		topHinge1.setPos(4F, 14F, 7F);
		topHinge1.setTexSize(128, 128);
		topHinge1.mirror = true;
		setRotation(topHinge1, 0F, 0F, 0F);
		topHinge2 = new ModelRenderer(this, 57, 14);
		topHinge2.addBox(-1F, -4.5F, -4.5F, 2, 4, 5);
		topHinge2.setPos(-4F, 14F, 7F);
		topHinge2.setTexSize(128, 128);
		topHinge2.mirror = true;
		setRotation(topHinge2, 0F, 0F, 0F);
		bottomHinge1 = new ModelRenderer(this, 57, 24);
		bottomHinge1.addBox(-1F, -0.5F, -0.5F, 2, 2, 1);
		bottomHinge1.setPos(4F, 14F, 7F);
		bottomHinge1.setTexSize(128, 128);
		bottomHinge1.mirror = true;
		setRotation(bottomHinge1, 0F, 0F, 0F);
		bottomHinge2 = new ModelRenderer(this, 57, 24);
		bottomHinge2.addBox(-1F, -0.5F, -0.5F, 2, 2, 1);
		bottomHinge2.setPos(-4F, 14F, 7F);
		bottomHinge2.setTexSize(128, 128);
		bottomHinge2.mirror = true;
		setRotation(bottomHinge2, 0F, 0F, 0F);
		corner1 = new ModelRenderer(this, 57, 28);
		corner1.addBox(-1F, -4.25F, -14.25F, 2, 2, 2);
		corner1.setPos(6.25F, 14F, 7F);
		corner1.setTexSize(64, 32);
		corner1.mirror = true;
		setRotation(corner1, 0F, 0F, 0F);
		corner2 = new ModelRenderer(this, 66, 28);
		corner2.addBox(-1F, -4.25F, -14.25F, 2, 2, 2);
		corner2.setPos(-6.3F, 14F, 7F);
		corner2.setTexSize(64, 32);
		corner2.mirror = true;
		setRotation(corner2, 0F, 0F, 0F);
		handle1 = new ModelRenderer(this, 57, 34);
		handle1.addBox(0F, 0F, 0F, 1, 2, 4);
		handle1.setPos(7F, 15F, -2F);
		handle1.setTexSize(128, 128);
		handle1.mirror = true;
		setRotation(handle1, 0F, 0F, 0F);
		handle2 = new ModelRenderer(this, 57, 34);
		handle2.addBox(0F, 0F, 0F, 1, 2, 4);
		handle2.setPos(-8F, 15F, -2F);
		handle2.setTexSize(128, 128);
		handle2.mirror = true;
		setRotation(handle2, 0F, 0F, 0F);
	}

	/**
	 * 
	 */
	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {

		// set the angles
		skullTop.xRot = lid.xRot;
		topHinge1.xRot = lid.xRot;
		topHinge2.xRot = lid.xRot;
		corner1.xRot = lid.xRot;
		corner2.xRot = lid.xRot;

		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		baseMiddle.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		baseSide1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		baseSide2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		skullTop.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		skullBottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		topHinge1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		topHinge2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottomHinge1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottomHinge2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		corner1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		corner2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		handle1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		handle2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}

}
