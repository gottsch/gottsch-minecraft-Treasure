package mod.gottsch.forge.treasure2.core.gui.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mod.gottsch.forge.treasure2.core.tileentity.AbstractTreasureChestTileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MilkCrateModel extends AbstractTreasureChestModel {
	private final ModelRenderer northFace;
	private final ModelRenderer lid;
	private final ModelRenderer padBottom;
	private final ModelRenderer southFace;
	private final ModelRenderer eastFace;
	private final ModelRenderer westFace;
	private final ModelRenderer bottomFace;

	public MilkCrateModel() {
		super(RenderType::entityCutout);
		
		texWidth = 128;
		texHeight = 128;

		northFace = new ModelRenderer(this);
		northFace.setPos(0.0F, 14.0F, 7.0F);
		northFace.texOffs(41, 8).addBox(-7.0F, 7.0F, -14.0F, 14, 3, 1, false);
		northFace.texOffs(42, 5).addBox(-7.0F, 3.0F, -14.0F, 14, 3, 1, false);
		northFace.texOffs(42, 1).addBox(-7.0F, -0.9F, -14.0F, 14, 3, 1, false);
		northFace.texOffs(0, 0).addBox(5.1F, -1.0F, -14.1F, 2, 11, 2, false);
		northFace.texOffs(0, 0).addBox(-7.1F, -1.0F, -14.1F, 2, 11, 2, false);

		lid = new ModelRenderer(this);
		lid.setPos(0.0F, 13.0F, 7.0F);
		lid.texOffs(0, 0).addBox(-7.0F, -2.0F, -14.0F, 14, 2, 14, false);
		lid.texOffs(0, 16).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);
		lid.texOffs(8, 8).addBox(-1.0F, -1.0F, -15.0F, 2, 2, 1, false);
		
		padBottom = new ModelRenderer(this);
		padBottom.setPos(0.0F, 15.0F, 7.0F);
		padBottom.texOffs(0, 19).addBox(-2.0F, -2.0F, -14.2F, 4, 2, 1, false);

		southFace = new ModelRenderer(this);
		southFace.setPos(0.0F, 14.0F, 7.0F);
		southFace.texOffs(42, 8).addBox(-7.0F, 7.0F, -1.0F, 14, 3, 1, false);
		southFace.texOffs(42, 5).addBox(-7.0F, 3.0F, -1.0F, 14, 3, 1, false);
		southFace.texOffs(42, 1).addBox(-7.0F, -0.9F, -1.0F, 14, 3, 1, false);
		southFace.texOffs(0, 0).addBox(5.1F, -1.0F, -1.9F, 2, 11, 2, false);
		southFace.texOffs(0, 0).addBox(-7.1F, -1.0F, -1.9F, 2, 11, 2, false);

		eastFace = new ModelRenderer(this);
		eastFace.setPos(0.0F, 14.0F, 7.0F);
		eastFace.texOffs(0, 44).addBox(-7.0F, 7.0F, -13.0F, 1, 3, 12, false);
		eastFace.texOffs(26, 29).addBox(-7.0F, 3.0F, -13.0F, 1, 3, 12, false);
		eastFace.texOffs(0, 29).addBox(-7.0F, -0.9F, -13.0F, 1, 3, 12, false);

		westFace = new ModelRenderer(this);
		westFace.setPos(0.0F, 14.0F, 7.0F);
		westFace.texOffs(0, 44).addBox(6.0F, 7.0F, -13.0F, 1, 3, 12, false);
		westFace.texOffs(26, 29).addBox(6.0F, 3.0F, -13.0F, 1, 3, 12, false);
		westFace.texOffs(0, 29).addBox(6.0F, -0.9F, -13.0F, 1, 3, 12, false);

		bottomFace = new ModelRenderer(this);
		bottomFace.setPos(0.0F, 24.0F, 0.0F);
		bottomFace.texOffs(0, 16).addBox(-6.0F, -1.0F, -6.0F, 12, 1, 12, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void renderAll(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, AbstractTreasureChestTileEntity te) {
		northFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		southFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		eastFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		westFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		bottomFace.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
		padBottom.render(matrixStack, renderBuffer, combinedLight, combinedOverlay);
	}

	@Override
	public ModelRenderer getLid() {
		return lid;
	}
}