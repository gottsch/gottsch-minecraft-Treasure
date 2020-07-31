//Made with Blockbench
//Paste this code into your mod.

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class custom_model extends ModelBase {
	private final ModelRenderer chest;
	private final ModelRenderer headBone;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;

	public custom_model() {
		textureWidth = 64;
		textureHeight = 64;

		chest = new ModelRenderer(this);
		chest.setRotationPoint(-8.0F, 16.0F, 8.0F);
		chest.cubeList.add(new ModelBox(chest, 0, 15, 2.0F, -4.0F, -10.0F, 12, 5, 10, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 44, 44, 14.0F, -2.0F, -10.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 16, 44, 14.0F, -2.0F, -6.5F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 8, 44, 14.0F, -2.0F, -3.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 0, 44, 0.0F, -2.0F, -10.0F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 42, 13, 0.0F, -2.0F, -6.5F, 2, 10, 2, 0.0F, false));
		chest.cubeList.add(new ModelBox(chest, 26, 42, 0.0F, -2.0F, -3.0F, 2, 10, 2, 0.0F, false));

		headBone = new ModelRenderer(this);
		headBone.setRotationPoint(0.0F, 13.0F, 8.0F);
		headBone.cubeList.add(new ModelBox(headBone, 0, 30, -4.0F, -4.0F, -16.0F, 8, 8, 6, 0.0F, false));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 13.0F, 8.0F);
		setRotationAngle(bone, -0.3927F, 0.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 36, 36, 6.0F, 4.7507F, -10.8561F, 2, 10, 2, 0.0F, false));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 13.0F, 8.0F);
		setRotationAngle(bone2, -0.3927F, 0.0F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 28, 30, -8.0F, 4.7507F, -10.8561F, 2, 10, 2, 0.0F, false));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 13.0F, 8.0F);
		bone3.cubeList.add(new ModelBox(bone3, 0, 0, -6.0F, -5.0F, -10.0F, 12, 5, 10, 0.01F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		chest.render(f5);
		headBone.render(f5);
		bone.render(f5);
		bone2.render(f5);
		bone3.render(f5);
	}
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}