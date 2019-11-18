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
		textureWidth = 96;
		textureHeight = 96;

		hinge = new ModelRenderer(this);
		hinge.setRotationPoint(0.0F, 21.0F, 6.0F);
		hinge.cubeList.add(new ModelBox(hinge, 0, 23, -2.0F, -1.0F, 0.0F, 7, 2, 2, 0.0F, false));

		bottom = new ModelRenderer(this);
		bottom.setRotationPoint(0.0F, 24.0F, 0.0F);
		bottom.cubeList.add(new ModelBox(bottom, 36, 14, -5.0F, -1.0F, -3.0F, 10, 1, 8, 0.0F, false));
		bottom.cubeList.add(new ModelBox(bottom, 48, 0, -6.0F, -3.0F, -6.0F, 12, 2, 12, 0.0F, false));

		top = new ModelRenderer(this);
		top.setRotationPoint(0.0F, 21.0F, 6.0F);
		top.cubeList.add(new ModelBox(top, 0, 0, -6.0F, -2.0F, -12.0F, 12, 2, 12, 0.0F, false));
		top.cubeList.add(new ModelBox(top, 0, 14, -5.0F, -3.0F, -9.0F, 10, 1, 8, 0.0F, false));
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