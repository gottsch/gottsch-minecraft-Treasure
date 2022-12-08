package mod.gottsch.forge.treasure2.client.model.blockentity;

import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;


// NECESSARY?
/**
 * @author Mark Gottschling on Aug 24, 2020
 *
 */
public abstract class AbstractTreasureChestModel extends Model implements ITreasureChestModel {

	public AbstractTreasureChestModel(ModelPart root) {
		super(RenderType::entitySolid);
	}
	
	// TODO this is moot right now and does nothing
	@Override
	public final RenderType getChestRenderType(ResourceLocation location) {
		return super.renderType(location);
	}
	
	public final Function<ResourceLocation, RenderType> getChestRenderType() {
		return super.renderType;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer renderBuffer, int combinedLight, int combinedOverlay,
			float f, float g, float h, float i) {
		// do nothing
	}
}
