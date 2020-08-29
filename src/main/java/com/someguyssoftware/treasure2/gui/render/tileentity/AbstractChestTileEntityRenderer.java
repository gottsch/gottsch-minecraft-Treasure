/**
 * 
 */
package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public abstract class AbstractChestTileEntityRenderer extends TileEntityRenderer<AbstractTreasureChestTileEntity> implements ITreasureChestTileEntityRenderer {
	private ResourceLocation texture;
	private ITreasureChestModel model;

	public AbstractChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
	}


	/**
	 * render the tile entity - called every frame while the tileentity is in view of the player
	 *
	 * @param tileEntityIn the associated tile entity
	 * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
	 *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
	 *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
	 * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
	 *                        it is needed for you to render the view properly.
	 * @param renderBuffers    the buffer that you should render your model to
	 * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
	 * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
	 *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
	 *                        CreeperRenderer.getOverlayProgress()
	 */
	@Override
	public void render(AbstractTreasureChestTileEntity tileEntity, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {

		if (!(tileEntity instanceof AbstractTreasureChestTileEntity)) {
			return; // should never happen
		}

		World world = tileEntity.getWorld();
		boolean hasWorld = (world != null);
		BlockState state = tileEntity.getBlockState();
		Direction facing = Direction.NORTH;
		if (hasWorld) {
			facing = state.get(StandardChestBlock.FACING);
		}

		// push the current transformation matrix + normals matrix
		matrixStack.push(); 

		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
		final Vec3d TRANSLATION_OFFSET = new Vec3d(0.5, 1.5, 0.5);
		matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z); // translate
		matrixStack.scale(-1, -1, 1);
		float f = getHorizontalAngle(facing);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(-f));

		
		// TEST scale to half size to see if locks are rendered
//		matrixStack.scale(0.5F, 0.5F, 0.5F);
		
		// update the lid rotation
		updateModelRotationAngles(tileEntity, partialTicks);


		IVertexBuilder renderBuffer = renderTypeBuffer.getBuffer(model.getChestRenderType(getTexture()));
		model.renderAll(matrixStack, renderBuffer, combinedLight, combinedOverlay, tileEntity);
		matrixStack.pop();		

		//	////////////// render the locks //////////////////////////////////////
		renderLocks(tileEntity, matrixStack, renderTypeBuffer, combinedLight, combinedOverlay);
		//	////////////// end of render the locks //////////////////////////////////////
	}

	/**
	 * 
	 * @param tileEntity
	 * @param combinedOverlay 
	 * @param combinedLight 
	 * @param renderBuffer 
	 * @param matrixStack 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void renderLocks(AbstractTreasureChestTileEntity tileEntity, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
		 Treasure.LOGGER.debug("=====================================================================");
		if (tileEntity.getLockStates().isEmpty()) {
			return;
		}

		// render locks
		//		TODO okte.getLockStates().forEach(lockState -> {});
		for (LockState lockState : tileEntity.getLockStates()) {

			if (lockState.getLock() != null) {
				// convert lock to an item stack
				ItemStack lockStack = new ItemStack(lockState.getLock());
//				if (lockState.getSlot().getFace() != null) {
//					Treasure.LOGGER.info("Render LS:" + lockState);
//				}
				
				matrixStack.push();
				
				// NOTE when rotating the item to match the face of chest, must adjust the
				// amount of offset to the x,z axises and
				// not rotate() the item - rotate() just spins it in place, not around the axis
				// of the block
				matrixStack.translate(lockState.getSlot().getXOffset(), lockState.getSlot().getYOffset(), lockState.getSlot().getZOffset());

				matrixStack.rotate(Vector3f.YP.rotationDegrees(lockState.getSlot().getRotation()));
				matrixStack.scale(getLocksScaleModifier(), getLocksScaleModifier(), getLocksScaleModifier());
				Minecraft.getInstance().getItemRenderer().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderBuffer);
				matrixStack.pop();
			}
		}
	}

	/**
	 * 
	 * @param tileEntity
	 * @param partialTicks
	 */
	public void updateModelRotationAngles(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
		lidRotation = 1.0F - lidRotation;
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
		model.getLid().rotateAngleX = -(lidRotation * (float) Math.PI / getAngleModifier());
	}

	/**
	 * Modifies the max angle that the lid can swing.
	 * The max swing angle by default is 180 degrees. The max swing angle is divided the modifier.
	 * Increasing the size of the modifier reduces the size of the max swing angle.
	 * Ex:
	 * Return 2.0 = 90 degrees
	 * Return 3.0 = 60 degrees
	 * Return 4.0 = 45 degrees
	 * 
	 * @return
	 */
	public float getAngleModifier() {
		return 2.0F;
	}

	/**
	 *  Modifies teh scale of the Lock item(s).
	 * Ranges from 0.0F to x.xF.
	 * Ex:
	 * Return 1.0F = full size
	 * Return 0.5 = half size
	 * 
	 * @return
	 */
	public float getLocksScaleModifier() {
		return 0.5F;
	}
	
	/**
	 * Helper method since all my models face the opposite direction of vanilla models
	 * @param meta
	 * @return
	 */
	public int getHorizontalAngle(Direction facing) {
		switch (facing) {
		default:
		case NORTH:
			return 0;
		case SOUTH:
			return 180;
		case WEST:
			return 90;
		case EAST:
			return -90;
		}
	}

	/**
	 * @return the texture
	 */
	public ResourceLocation getTexture() {
		return texture;
	}

	/**
	 * @param texture
	 *            the texture to set
	 */
	public void setTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	/**
	 * @return the model
	 */
	public ITreasureChestModel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(ITreasureChestModel model) {
		this.model = model;
	}

}
