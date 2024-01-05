package mod.gottsch.forge.treasure2.core.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Jan 15, 2018
 *
 */
public class BandedChestModel extends AbstractTreasureChestModel {

	ModelRenderer base;
	ModelRenderer RightBottomBand;
	ModelRenderer lid;
	ModelRenderer RightTopBand;
	ModelRenderer LeftTopBand;
	ModelRenderer FrontLeftTopBand;
	ModelRenderer BackRightTopBand;
	ModelRenderer BackLeftTopBand;
	ModelRenderer LeftBottomBand;
	ModelRenderer FrontRightTopBand;
	ModelRenderer Ledge;
	ModelRenderer Ledge2;
	ModelRenderer Ledge3;
    ModelRenderer hinge1;
    ModelRenderer hinge2;
    ModelRenderer pad;
    
	public BandedChestModel() {
		super(RenderType::entityCutout);
		
		texWidth = 128;
		texHeight = 128;

		base = new ModelRenderer(this, 0, 20);
		base.addBox(-7F, 0F, -14F, 14, 9, 14);
		base.setPos(0F, 14.5F, 7F);
		base.setTexSize(64, 32);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		RightBottomBand = new ModelRenderer(this, 0, 44);
		RightBottomBand.addBox(-1F, 0F, -15F, 2, 9, 15);
		RightBottomBand.setPos(-4F, 15F, 7.5F);
		RightBottomBand.setTexSize(64, 32);
		RightBottomBand.mirror = true;
		setRotation(RightBottomBand, 0F, 0F, 0F);
		lid = new ModelRenderer(this, 0, 0);
		lid.addBox(-7F, -5F, -14F, 14, 5, 14);
		lid.setPos(0F, 15.5F, 7F);
		lid.setTexSize(64, 32);
		lid.mirror = true;
		setRotation(lid, 0F, 0F, 0F);
		RightTopBand = new ModelRenderer(this, 0, 69);
		RightTopBand.addBox(0F, -5F, -15F, 2, 1, 15);
		RightTopBand.setPos(-5F, 15F, 7.5F);
		RightTopBand.setTexSize(64, 32);
		RightTopBand.mirror = true;
		setRotation(RightTopBand, 0F, 0F, 0F);
		LeftTopBand = new ModelRenderer(this, 36, 69);
		LeftTopBand.addBox(0F, -5F, -15F, 2, 1, 15);
		LeftTopBand.setPos(3F, 15F, 7.5F);
		LeftTopBand.setTexSize(64, 32);
		LeftTopBand.mirror = true;
		setRotation(LeftTopBand, 0F, 0F, 0F);
		FrontLeftTopBand = new ModelRenderer(this, 64, 0);
		FrontLeftTopBand.addBox(-1F, -4F, -15F, 2, 5, 1);
		FrontLeftTopBand.setPos(4F, 15F, 7.5F);
		FrontLeftTopBand.setTexSize(64, 32);
		FrontLeftTopBand.mirror = true;
		setRotation(FrontLeftTopBand, 0F, 0F, 0F);
		BackRightTopBand = new ModelRenderer(this, 57, 7);
		BackRightTopBand.addBox(-1F, -5F, -1F, 2, 5, 1);
		BackRightTopBand.setPos(-4F, 15F, 7.5F);
		BackRightTopBand.setTexSize(64, 32);
		BackRightTopBand.mirror = true;
		setRotation(BackRightTopBand, 0F, 0F, 0F);
		BackLeftTopBand = new ModelRenderer(this, 64, 7);
		BackLeftTopBand.addBox(-1F, -5F, -1F, 2, 5, 1);
		BackLeftTopBand.setPos(4F, 15F, 7.5F);
		BackLeftTopBand.setTexSize(64, 32);
		BackLeftTopBand.mirror = true;
		setRotation(BackLeftTopBand, 0F, 0F, 0F);
		LeftBottomBand = new ModelRenderer(this, 35, 44);
		LeftBottomBand.addBox(-1F, 0F, -15F, 2, 9, 15);
		LeftBottomBand.setPos(4F, 15F, 7.5F);
		LeftBottomBand.setTexSize(64, 32);
		LeftBottomBand.mirror = true;
		setRotation(LeftBottomBand, 0F, 0F, 0F);
		FrontRightTopBand = new ModelRenderer(this, 57, 0);
		FrontRightTopBand.addBox(-1F, -4F, -15F, 2, 5, 1);
		FrontRightTopBand.setPos(-4F, 15F, 7.5F);
		FrontRightTopBand.setTexSize(64, 32);
		FrontRightTopBand.mirror = true;
		setRotation(FrontRightTopBand, 0F, 0F, 0F);
		Ledge = new ModelRenderer(this, 58, 16);
		Ledge.addBox(-2F, -1F, -15F, 4, 1, 1);
		Ledge.setPos(0F, 15F, 7F);
		Ledge.setTexSize(64, 32);
		Ledge.mirror = true;
		setRotation(Ledge, 0F, 0F, 0F);
		Ledge2 = new ModelRenderer(this, 58, 16);
		Ledge2.addBox(0F, -1F, -9F, 1, 1, 4);
		Ledge2.setPos(7F, 15F, 7F);
		Ledge2.setTexSize(128, 128);
		Ledge2.mirror = true;
		setRotation(Ledge2, 0F, 0F, 0F);
		Ledge3 = new ModelRenderer(this, 0, 0);
		Ledge3.addBox(-1F, -1F, -9F, 1, 1, 4);
		Ledge3.setPos(-7F, 15F, 7F);
		Ledge3.setTexSize(128, 128);
		Ledge3.mirror = true;
		setRotation(Ledge3, 0F, 0F, 0F);
	      hinge1 = new ModelRenderer(this, 11, 86);
	      hinge1.addBox(-1F, 0F, -0.8F, 2, 2, 1);
	      hinge1.setPos(2F, 14.5F, 7F);
	      hinge1.setTexSize(128, 128);
	      hinge1.mirror = true;
	      setRotation(hinge1, 0F, 0F, 0F);
	      hinge2 = new ModelRenderer(this, 11, 86);
	      hinge2.addBox(-1F, 0F, -0.8F, 2, 2, 1);
	      hinge2.setPos(-2F, 14.5F, 7F);
	      hinge2.setTexSize(128, 128);
	      hinge2.mirror = true;
	      setRotation(hinge2, 0F, 0F, 0F);
	      pad = new ModelRenderer(this, 0, 86);
	      pad.addBox(-2F, -1F, -14.2F, 4, 4, 1);
	      pad.setPos(0F, 15.5F, 7F);
	      pad.setTexSize(128, 128);
	      pad.mirror = true;
	      setRotation(pad, 0F, 0F, 0F);
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		// set the angles of the latch to same as the lid
		RightTopBand.xRot = lid.xRot;
		LeftTopBand.xRot = lid.xRot;
		FrontLeftTopBand.xRot = lid.xRot;
		FrontRightTopBand.xRot = lid.xRot;
		BackLeftTopBand.xRot = lid.xRot;
		BackRightTopBand.xRot = lid.xRot;
		Ledge.xRot = lid.xRot;
		Ledge2.xRot = lid.xRot;
		Ledge3.xRot = lid.xRot;
				
		base.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		RightBottomBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		RightTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		LeftTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		FrontLeftTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		BackRightTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		BackLeftTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		LeftBottomBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		FrontRightTopBand.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		Ledge.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		Ledge2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		Ledge3.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	    hinge1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	    hinge2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	    pad.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	/**
	 * @return the base
	 */
	public ModelRenderer getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(ModelRenderer base) {
		this.base = base;
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
