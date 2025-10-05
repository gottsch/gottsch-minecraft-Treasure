/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.entity.WitherwoodGolemModel;
import mod.gottsch.forge.treasure2.client.renderer.entity.layer.WitherwoodGolemLayer;
import mod.gottsch.forge.treasure2.core.entity.monster.WitherwoodGolem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * @author Mark Gottschling on Aug 29, 2024
 *
 */
public class WitherwoodGolemRenderer extends MobRenderer<WitherwoodGolem, WitherwoodGolemModel<WitherwoodGolem>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation(Treasure.MODID, "textures/entity/mob/witherwood_golem.png");

    public WitherwoodGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new WitherwoodGolemModel<>(context.bakeLayer(WitherwoodGolemModel.LAYER_LOCATION)), 0.7F);
        this.addLayer(new WitherwoodGolemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(WitherwoodGolem golem) {
        return GOLEM_LOCATION;
    }

    @Override
    protected void setupRotations(WitherwoodGolem golem, PoseStack poseStack, float p_115016_, float p_115017_, float p_115018_) {
        super.setupRotations(golem, poseStack, p_115016_, p_115017_, p_115018_);
        if (!((double)golem.walkAnimation.speed() < 0.01D)) {
            float f = 13.0F;
            float f1 = golem.walkAnimation.position(p_115018_) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }

    @Override
    public void render(WitherwoodGolem golem, float p_116532_, float p_116533_, PoseStack matrixStack, MultiBufferSource bufferSource, int p_116536_) {
        super.render(golem, p_116532_, p_116533_, matrixStack, bufferSource, p_116536_);
    }
}
