package mod.gottsch.forge.treasure2.core.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestModel extends AbstractTreasureChestModel {
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

	/**
	 * 
	 */
	public WitherChestModel() {
		super(RenderType::entityCutout);
		texWidth = 64;
		texHeight = 128;

		bottom = new ModelRenderer(this, 0, 0);
		bottom.addBox(-7F, 0F, -14F, 14, 4, 14);
		bottom.setPos(0F, 20F, 7F);
		bottom.setTexSize(64, 128);
		bottom.mirror = true;
		setRotation(bottom, 0F, 0F, 0F);
		back = new ModelRenderer(this, 0, 19);
		back.addBox(-7F, 0F, -4F, 14, 18, 4);
		back.setPos(0F, 2F, 7F);
		back.setTexSize(64, 128);
		back.mirror = true;
		setRotation(back, 0F, 0F, 0F);
		top = new ModelRenderer(this, 0, 42);
		top.addBox(-7F, 0F, -14F, 14, 4, 14);
		top.setPos(0F, -2F, 7F);
		top.setTexSize(64, 128);
		top.mirror = true;
		setRotation(top, 0F, 0F, 0F);
		rside = new ModelRenderer(this, 0, 62);
		rside.addBox(-4F, 0F, -6F, 4, 18, 6);
		rside.setPos(7F, 2F, 3F);
		rside.setTexSize(64, 128);
		rside.mirror = true;
		setRotation(rside, 0F, 0F, 0F);
		lside = new ModelRenderer(this, 21, 62);
		lside.addBox(0F, 0F, -6F, 4, 18, 6);
		lside.setPos(-7F, 2F, 3F);
		lside.setTexSize(64, 128);
		lside.mirror = true;
		setRotation(lside, 0F, 0F, 0F);
		lfront = new ModelRenderer(this, 23, 88);
		lfront.addBox(0F, 0F, -10F, 7, 18, 4);
		lfront.setPos(-7F, 2F, 3F);
		lfront.setTexSize(64, 128);
		lfront.mirror = true;
		setRotation(lfront, 0F, 0F, 0F);
		rfront = new ModelRenderer(this, 0, 88);
		rfront.addBox(-7F, 0F, -10F, 7, 18, 4);
		rfront.setPos(7F, 2F, 3F);
		rfront.setTexSize(64, 128);
		rfront.mirror = true;
		setRotation(rfront, 0F, 0F, 0F);
		backBark = new ModelRenderer(this, 0, 112);
		backBark.addBox(-4F, 0F, -2F, 7, 6, 2);
		backBark.setPos(4F, -8F, 7F);
		backBark.setTexSize(64, 128);
		backBark.mirror = true;
		setRotation(backBark, 0F, 0F, 0F);
		backBark2 = new ModelRenderer(this, 19, 112);
		backBark2.addBox(-2F, 0F, 0F, 4, 4, 2);
		backBark2.setPos(-2F, -6F, 5F);
		backBark2.setTexSize(64, 128);
		backBark2.mirror = true;
		setRotation(backBark2, 0F, 0F, 0F);
		sideBark = new ModelRenderer(this, 32, 112);
		sideBark.addBox(0F, 0F, 0F, 2, 3, 8);
		sideBark.setPos(5F, -5F, -3F);
		sideBark.setTexSize(64, 128);
		sideBark.mirror = true;
		setRotation(sideBark, 0F, 0F, 0F);
		root1 = new ModelRenderer(this, 47, 88);
		root1.addBox(0F, 0F, 0F, 1, 1, 3);
		root1.setPos(7F, 23F, -4F);
		root1.setTexSize(64, 128);
		root1.mirror = true;
		setRotation(root1, 0F, 0F, 0F);
		root2 = new ModelRenderer(this, 47, 94);
		root2.addBox(0F, 0F, 0F, 6, 2, 1);
		root2.setPos(-4F, 22F, -8F);
		root2.setTexSize(64, 128);
		root2.mirror = true;
		setRotation(root2, 0F, 0F, 0F);
	}
	
	/**
	 * 
	 */
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		rside.yRot = rfront.yRot;
		lfront.yRot = -(rfront.yRot);
		lside.yRot = lfront.yRot;
		
		bottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		back.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		top.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		rside.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lside.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lfront.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		rfront.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		backBark.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		backBark2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		sideBark.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		root1.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		root2.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
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
