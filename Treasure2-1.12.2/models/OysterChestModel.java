//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ClamChestModel extends ModelBase {
	private final ModelRenderer hinge;
	private final ModelRenderer bottom;
	private final ModelRenderer top;

	public ClamChestModel() {
		textureWidth = 112;
		textureHeight = 112;

		hinge = new ModelRenderer(this);
		hinge.setRotationPoint(0.0F, 21.0F, 5.0F);
		hinge.cubeList.add(new ModelBox(hinge, 36, 55, -3.0F, -2.0F, 0.0F, 6, 3, 2, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 30, 0, -6.0F, -1.0F, -5.0F, 12, 1, 7, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 18, 55, 5.0F, -3.0F, -5.0F, 2, 2, 7, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 22, 45, 3.0F, -2.0F, -6.0F, 2, 1, 9, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 31, 1.0F, -3.0F, -7.0F, 2, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 68, 0, -1.0F, -2.0F, -8.0F, 2, 1, 13, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 56, 16, -3.0F, -3.0F, -7.0F, 2, 2, 12, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 45, -5.0F, -2.0F, -6.0F, 2, 1, 9, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 0, 55, -7.0F, -3.0F, -5.0F, 2, 2, 7, 0.0F, false));

		top = new ModelRenderer(this);
		top.setRotationPoint(0.0F, 21.0F, 6.0F);
		top.cubeList.add(new ModelBox(top, 62, 45, 5.0F, -3.0F, -11.0F, 2, 3, 7, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 50, 31, 3.0F, -2.0F, -12.0F, 2, 3, 9, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 28, 16, 1.0F, -3.0F, -13.0F, 2, 3, 12, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 0, 0, -1.0F, -2.0F, -14.0F, 2, 3, 13, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 0, 16, -3.0F, -3.0F, -13.0F, 2, 3, 12, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 28, 31, -5.0F, -2.0F, -12.0F, 2, 3, 9, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 44, 45, -7.0F, -3.0F, -11.0F, 2, 3, 7, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		hinge.render(f5);
		bottom.render(f5);
		top.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}