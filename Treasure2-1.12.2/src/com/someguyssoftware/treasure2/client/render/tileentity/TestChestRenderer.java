/**
 * 
 */
package com.someguyssoftware.treasure2.client.render.tileentity;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.client.model.TestChestModel;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.TestChestTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling onJan 5, 2018
 *
 */
@Deprecated
public class TestChestRenderer extends TileEntitySpecialRenderer<TestChestTileEntity> {
	private static final int NORTH = 2;
	private static final int SOUTH = 3;
	private static final int WEST = 4;
	private static final int EAST = 5;
	
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation(Treasure.MODID + ":textures/entity/chest/test-chest.png");

    private final TestChestModel model = new TestChestModel();
    
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
    public void render(TestChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    	
    	if (!(te instanceof TestChestTileEntity)) return; // should never happen
    	
    	TestChestTileEntity chestTE = (TestChestTileEntity) te	;
//    	LockState lockState = chestTE.getLockStates()[0];
//Treasure.logger.debug("Render lockState:" + lockState);
    	
    	int meta;

        if (te.hasWorld()) {
        	meta = te.getBlockMetadata();
        }
        else {
        	meta = 0;
        }
        
        int rotation = 0;
        
        if (meta == NORTH) { // NORTH
            rotation = 0;
        }

        if (meta == SOUTH) { // SOUTH
            rotation = 180;
        }

        if (meta == WEST) { // WEST
            rotation = -90;
        }

        if (meta == EAST) { // EAST
            rotation = 90;
        }
        
        this.bindTexture(TEXTURE_NORMAL);
        
        // get the item stack
        ItemStack stack = new ItemStack(Items.GOLDEN_APPLE, 1);
        ItemStack westStack = new ItemStack(Items.APPLE, 1);
        ItemStack eastStack = new ItemStack(Items.BAKED_POTATO, 1);
        ItemStack southStack = new ItemStack(Items.CARROT, 1);
        
//        ItemStack slotStack = new ItemStack(
//        		lockState.getLock()
////        		Items.POTATO
//        		);
        
        // TODO add destroy texture
        
        GlStateManager.pushMatrix();
        // initial position (centered moved up)
        // (chest entity were created in Techne and need different translations than vanilla tile entities)
        GlStateManager.translate((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        
		// This rotation part is very important! Without it, your model will render upside-down.
        // (rotate 180 degrees around the z-axis)
        GlStateManager.rotate(180F, 0F, 0F, 1.0F);     
        // rotate block to the correct direction that it is facing.
        GlStateManager.rotate((float)rotation, 0.0F, 1.0F, 0.0F);
        
//        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        
        // TODO if isLocked = true, don't set lidAngle.
 //       float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        
 //       f = 1.0F - f;
  //      f = 1.0F - f * f * f;
 //       model.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
        model.renderAll(null);
        GlStateManager.popMatrix();
        // end of rendering chest entity ////
        
        ////////////////////////////////////////////////////
        
        // render locks
        // TODO check locks and positions (NESW) from chest tile entity.
        
        // get item
        for (LockState lockState : chestTE.getLockStates()) {
        	if (lockState.getLock() != null) {
            ItemStack slotStack = new ItemStack(	lockState.getLock());

	        GlStateManager.pushMatrix();          
	        // NOTE when rotating the item to match the face of chest, must adjust the amount of offset to the x,z axises and 
	        // not rotate the item. rotating just spins it in place, not around the axis of the block (which is needed though to "face" the right direction)
	 
	        // TODO why is the lock item being updated and rendered properly, but not the offsets and rotation
	        // only on a load does it go into the right spot.... maybe it still isn't being set!!!
	        GlStateManager.translate(
	        		(float)x + lockState.getSlot().getXOffset(),
	        		(float)y + lockState.getSlot().getYOffset(),
	        		(float)z + lockState.getSlot().getZOffset()/* 0.95F*/); // NORTH
	        GlStateManager.rotate(lockState.getSlot().getRotation(), 0F, 1.0F, 0.0F);
	        // NOTE may want to rotate the item so that is faces the correct direction on every face.
	        // (rotate 180 degrees around the z-axis so default position faces the same direction as chest)
	//      GlStateManager.rotate(180F, 0F, 1.0F, 0.0F);
	        
	        GlStateManager.scale(0.5F, 0.5F, 0.5F);
	        // TODO for each locks
	
	        // TODO rotate in the right facing direction
	        // render items
	
	        if (lockState.getLock() == null || lockState.getLock() == Items.AIR) {
	        	Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);
	        }
	        else {
	        	Minecraft.getMinecraft().getRenderItem().renderItem(slotStack, ItemCameraTransforms.TransformType.NONE);
	        }
	        // TODO end of for each        
	        GlStateManager.popMatrix();
	        }
        }
        /////////////////////////
        
//        GlStateManager.pushMatrix();          
//        GlStateManager.translate((float)x + 0.05F, (float)y + 0.5F, (float)z + 0.5F); // WEST
//        GlStateManager.rotate(90F, 0F, 1.0F, 0.0F);
//        GlStateManager.scale(0.5F, 0.5F, 0.5F);
//        Minecraft.getMinecraft().getRenderItem().renderItem(westStack, ItemCameraTransforms.TransformType.NONE);
//        GlStateManager.popMatrix();
//        
//        /////////////////////////
//        
//        GlStateManager.pushMatrix();          
//        GlStateManager.translate((float)x + 0.95F, (float)y + 0.5F, (float)z + 0.5F); // EAST
//        GlStateManager.rotate(90F, 0F, 1.0F, 0.0F);
//        GlStateManager.scale(0.5F, 0.5F, 0.5F);
//        Minecraft.getMinecraft().getRenderItem().renderItem(eastStack, ItemCameraTransforms.TransformType.NONE);
//        GlStateManager.popMatrix();
//        /////////////////////////
//        
//        GlStateManager.pushMatrix();          
//        GlStateManager.translate((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.95F); // SOUTH
//        GlStateManager.rotate(180F, 0F, 1.0F, 0.0F);
//        GlStateManager.scale(0.5F, 0.5F, 0.5F);
//        Minecraft.getMinecraft().getRenderItem().renderItem(southStack, ItemCameraTransforms.TransformType.NONE);
//        GlStateManager.popMatrix();
        
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
