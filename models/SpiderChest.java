//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class custom_model extends ModelBase {
	private final ModelRenderer bone2;
	private final ModelRenderer bone;

	public custom_model() {
		textureWidth = 64;
		textureHeight = 64;

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 12.0F, 8.0F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 22, -6.0F, -3.0F, -16.0F, 12, 3, 16, 0.01F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -6.0F, -13.0F, -8.0F, 12, 6, 16, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 8, 41, 6.0F, -10.0F, -7.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 41, 6.0F, -10.0F, -3.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 40, 22, 6.0F, -10.0F, 1.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 40, 0, 6.0F, -10.0F, 5.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 8, 22, -8.0F, -10.0F, -7.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 22, -8.0F, -10.0F, -3.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 8, 0, -8.0F, -10.0F, 1.0F, 2, 10, 2, 0.0F, false));
		bone.cubeList.add(new ModelBox(bone, 0, 0, -8.0F, -10.0F, 5.0F, 2, 10, 2, 0.0F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		bone2.render(f5);
		bone.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}