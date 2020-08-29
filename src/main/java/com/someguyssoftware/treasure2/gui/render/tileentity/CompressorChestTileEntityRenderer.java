package com.someguyssoftware.treasure2.gui.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.StandardChestBlock;
import com.someguyssoftware.treasure2.gui.model.CompressorChestModel;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// NOTE @OnlyIn extremely important! add to all Renderers
@OnlyIn(Dist.CLIENT)
public class CompressorChestTileEntityRenderer extends AbstractChestTileEntityRenderer {

	public CompressorChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
		setTexture(new ResourceLocation(Treasure.MODID + ":textures/entity/chest/compressor-chest.png"));
		setModel(new CompressorChestModel());
	}
	
	@Override
	public void render(AbstractTreasureChestTileEntity tileEntity, float partialTicks, MatrixStack matrixStack,
			IRenderTypeBuffer renderTypeBuffer, int combinedLight, int combinedOverlay) {

		if (!(tileEntity instanceof AbstractTreasureChestTileEntity)) {
			return; // should never happen
		}

		// TODO this block goes into method / use template pattern
		World world = tileEntity.getWorld();
		boolean hasWorld = (world != null);
		BlockState state = tileEntity.getBlockState();
		Direction facing = Direction.NORTH;
		if (hasWorld) {
			facing = state.get(StandardChestBlock.FACING);
		}

		// push the current transformation matrix + normals matrix
		matrixStack.push(); 

		// TODO this block goes into method / use template pattern
		// The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
		// at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
		// We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
		final Vec3d TRANSLATION_OFFSET = new Vec3d(0.5, 0.75, 0.5);
		matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z); // translate
		matrixStack.scale(-1, -1, 1);
		float f = getHorizontalAngle(facing);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(-f));	
		
		// shrink the size of the chest by half
		matrixStack.scale(0.5F, 0.5F, 0.5F);  
		
		//////////////// custom lid code /////////////
		updateModelRotationAngles(tileEntity, partialTicks);		
		//////////////// end of lid code //////////////
		
		// TODO this block goes into method / use template pattern
		IVertexBuilder renderBuffer = renderTypeBuffer.getBuffer(getModel().getChestRenderType(getTexture()));
		getModel().renderAll(matrixStack, renderBuffer, combinedLight, combinedOverlay, tileEntity);
		matrixStack.pop();		
		
		// TODO this block goes into method / use template pattern
        ////////////// render the locks //////////////////////////////////////
//        if (!te.getLockStates().isEmpty()) {
//        	renderLocks(te, x, y, z);
//        }
        ////////////// end of render the locks //////////////////////////////////////
		
	}
	
	@Override
	public void updateModelRotationAngles(AbstractTreasureChestTileEntity tileEntity, float partialTicks) {
        float lidRotation = tileEntity.prevLidAngle + (tileEntity.lidAngle - tileEntity.prevLidAngle) * partialTicks;
        lidRotation = 1.0F - lidRotation;
        lidRotation = 1.0F - lidRotation * lidRotation * lidRotation;
        getModel().getLid().rotateAngleX = -(lidRotation * (float)Math.PI / 2.0F);   	}
}
