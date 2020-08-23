/**
 * 
 */
package com.someguyssoftware.treasure2.gui.render.tileentity;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.ITreasureChestModel;
import com.someguyssoftware.treasure2.gui.model.StandardChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;
import com.someguyssoftware.treasure2.util.AnimationTickCounter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling onJan 9, 2018
 *
 */
public abstract class TreasureChestTileEntityRenderer extends TileEntityRenderer<AbstractTreasureChestTileEntity> {
	private ResourceLocation texture;
	private StandardChestModel model;

	public TreasureChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
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
        
        // update the lid rotation
     	updateModelLidRotation(tileEntity, partialTicks);
     		
	    IVertexBuilder renderBuffer = renderTypeBuffer.getBuffer(model.getRenderType(getTexture()));
	    model.renderAll(matrixStack, renderBuffer, combinedLight, combinedOverlay, tileEntity);
	    matrixStack.pop();		
	}
	
	/**
	 * 
	 * @param destroyStage
	 */
//	public void applyDestroyGlState(int destroyStage) {
//		if (destroyStage >= 0) {
//			this.bindTexture(DESTROY_STAGES[destroyStage]);
//			GlStateManager.matrixMode(5890);
//			GlStateManager.pushMatrix();
//			GlStateManager.scale(4.0F, 4.0F, 1.0F);
//			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
//			GlStateManager.matrixMode(5888);
//		}
//	}

	/**
	 * 
	 * 
	 * @param destroyStage
	 */
//	public void popDestroyGlState(int destroyStage) {
//		if (destroyStage >= 0) {
//			GlStateManager.matrixMode(5890);
//			GlStateManager.popMatrix();
//			GlStateManager.matrixMode(5888);
//		}
//	}

	/**
	 * 
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 * @param partialTicks
	 * @param destroyStage
	 * @param alpha
	 */
//	public void render(AbstractTreasureChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

//		if (!(te instanceof AbstractTreasureChestTileEntity))
//			return; // should never happen
//
//		// apply the destory gl state (if any)
//		applyDestroyGlState(destroyStage);
//
//		// get the model
//		ITreasureChestModel model = getModel();
//		// bind the texture
//		this.bindTexture(getTexture());
//		// get the chest rotation
//		int meta = 0;
//		if (te.hasWorld()) {
//			meta = te.getBlockMetadata();
//		}
//		int rotation = getRotation(meta);
//
//		// start render matrix
//		GlStateManager.pushMatrix();
//		// initial position (centered moved up)
//		// (chest entity were created in Techne and need different translations than
//		// vanilla tile entities)
//		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
//
//		// This rotation part is very important! Without it, your model will render
//		// upside-down.
//		// (rotate 180 degrees around the z-axis)
//		GlStateManager.rotate(180F, 0F, 0F, 1.0F);
//		// rotate block to the correct direction that it is facing.
//		GlStateManager.rotate((float) rotation, 0.0F, 1.0F, 0.0F);
//
//		// update the lid rotation
//		updateModelLidRotation(te, partialTicks);
//
//		// TODO here we can call our method instead of the build-in render() method
//		// render the model
//		model.renderAll(te);
//
//		GlStateManager.popMatrix();
//		// end of rendering chest entity ////
//
//		// pop the destroy stage matrix
//		popDestroyGlState(destroyStage);
//
//		////////////// render the locks //////////////////////////////////////
//		if (!te.getLockStates().isEmpty()) {
//			renderLocks(te, x, y, z);
//		}
//		////////////// end of render the locks //////////////////////////////////////
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//	}

	// TODO move out to interface
	/**
	 * 
	 * @param tileEntity
	 * @param partialTicks
	 */
	public void updateModelLidRotation(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
//		Treasure.LOGGER.info("numUsing -> {}, prevLidAngle -> {}, lidAngle -> {}, partialTicks -> {}", tileEntity.numPlayersUsing, tileEntity.prevLidAngle, tileEntity.lidAngle, partialTicks);
		float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
//		Treasure.LOGGER.info("0. lidRotation -> {}", lidRotation);
		lidRotation = 1.0F - lidRotation;
//		Treasure.LOGGER.info("1. lidRotation -> {}", lidRotation);
		lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
//		Treasure.LOGGER.info("2. lidRotation -> {}", lidRotation);
		model.getLid().rotateAngleX = -(lidRotation * (float) Math.PI / getAngleModifier());
//		Treasure.LOGGER.info("3. lidRotation -> {}", -(lidRotation * (float) Math.PI / getAngleModifier()));
//		Treasure.LOGGER.info("new lid angle -> {}", model.getLid().rotateAngleX);
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
	 * 
	 * @param te
	 * @param x
	 * @param y
	 * @param z
	 */
//	public void renderLocks(AbstractTreasureChestTileEntity te, double x, double y, double z) {
//		// Treasure.logger.debug("=====================================================================");
//		// render locks
//		for (LockState lockState : te.getLockStates()) {
//			// Treasure.logger.debug("Render LS:" + lockState);
//			if (lockState.getLock() != null) {
//				// convert lock to an item stack
//				ItemStack lockStack = new ItemStack(lockState.getLock());
//
//				GlStateManager.pushMatrix();
//				// NOTE when rotating the item to match the face of chest, must adjust the
//				// amount of offset to the x,z axises and
//				// not rotate() the item - rotate() just spins it in place, not around the axis
//				// of the block
//				GlStateManager.translate((float) x + lockState.getSlot().getXOffset(), (float) y + lockState.getSlot().getYOffset(), (float) z + lockState.getSlot().getZOffset());
//				GlStateManager.rotate(lockState.getSlot().getRotation(), 0F, 1.0F, 0.0F);
//				GlStateManager.scale(0.5F, 0.5F, 0.5F);
//				Minecraft.getMinecraft().getRenderItem().renderItem(lockStack, ItemCameraTransforms.TransformType.NONE);
//				GlStateManager.popMatrix();
//			}
//		}
//	}

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
	public StandardChestModel getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(StandardChestModel model) {
		this.model = model;
	}

}
