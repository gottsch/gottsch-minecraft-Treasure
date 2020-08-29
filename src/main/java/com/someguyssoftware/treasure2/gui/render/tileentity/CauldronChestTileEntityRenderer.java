package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.CauldronChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.tileentity.CauldronChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class CauldronChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public CauldronChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/cauldron-chest.png"));
		setModel(new CauldronChestModel());
	}

	@Override
	public void updateModelRotationAngles(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		CauldronChestTileEntity cte = (CauldronChestTileEntity) tileEntity;
		float lidRotation = cte.prevLidAngle + (cte.lidAngle - cte.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		// NOTE positive rotation here (getLid() returns lidLeft property)
		getModel().getLid().rotateAngleZ = (lidRotation * (float)Math.PI / getAngleModifier());
	}

	@Override
	public void renderLocks(AbstractTreasureChestTileEntity te, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
		//	 Treasure.LOGGER.debug("=====================================================================");
		if (te.getLockStates().isEmpty()) {
			return;
		}

		// render locks
		for (LockState lockState : te.getLockStates()) {
			if (lockState.getLock() != null) {
				// convert lock to an item stack
				ItemStack lockStack = new ItemStack(lockState.getLock());

				matrixStack.push();

				// NOTE when rotating the item to match the face of chest, must adjust the
				// amount of offset to the x,z axises and
				// not rotate() the item - rotate() just spins it in place, not around the axis
				// of the block
				matrixStack.translate(lockState.getSlot().getXOffset(), lockState.getSlot().getYOffset(), lockState.getSlot().getZOffset());

				// rotate the locks on the x axis to lay flat
				matrixStack.rotate(Vector3f.XP.rotationDegrees(90)); // NOTE changed from Y to X axis
				matrixStack.rotate(Vector3f.ZP.rotationDegrees(lockState.getSlot().getRotation()));  // NOTE now Z axis is the Y axis since we rotated on the X axis first.
				matrixStack.scale(0.35F, 0.35F, 0.35F);
				Minecraft.getInstance().getItemRenderer().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderBuffer);
				matrixStack.pop();

			}
		}
	}
}
