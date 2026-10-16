package com.someguyssoftware.treasure2.client.render.entity.layer;

import com.someguyssoftware.treasure2.entity.monster.MimicEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Draws a mimic's glowing eyes: the model again with its eyes texture, additive and full bright. The 1.12 version of
 * the 1.20.1 EyesLayer, modelled on vanilla LayerSpiderEyes.
 *
 * @author Mark Gottschling on 10/16/2026
 */
@SideOnly(Side.CLIENT)
public class MimicEyesLayer implements LayerRenderer<MimicEntity> {
	private static final int FULL_BRIGHT = 61680;

	private final RenderLiving<MimicEntity> renderer;
	private final ResourceLocation texture;

	public MimicEyesLayer(RenderLiving<MimicEntity> renderer, ResourceLocation texture) {
		this.renderer = renderer;
		this.texture = texture;
	}

	@Override
	public void doRenderLayer(MimicEntity mimic, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		renderer.bindTexture(texture);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(!mimic.isInvisible());

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(FULL_BRIGHT % 65536), (float)(FULL_BRIGHT / 65536));
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		renderer.getMainModel().render(mimic, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);

		int brightness = mimic.getBrightnessForRender();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(brightness % 65536), (float)(brightness / 65536));
		renderer.setLightmap(mimic);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
