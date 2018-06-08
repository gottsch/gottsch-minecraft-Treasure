/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Map;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.FogHeight;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * TODO this block is not necessary unless we can get the colliding working here
 * @author Mark Gottschling on Apr 18, 2018
 *
 */
public class WitherFogBlock extends FogBlock {
    
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 * @param map
	 */
	public WitherFogBlock(String modID, String name, Material material, Map<FogHeight, FogBlock> map) {
		super(modID, name, material, map);
	}
	
	/**
	 * TODO this never happens because there isn't a collision box
	 */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	// TODO inflict player with wither
        if (worldIn.isRemote) {
        	return;
        }
        
//        EntityPlayer player = null;
        
        // set the proximity to wither tree for activation
//        double activatingProximity = 10.0D;
        
//        for (int i = 0; i < worldIn.playerEntities.size(); ++i) {
//            player = (EntityPlayer)worldIn.playerEntities.get(i);
            // get the distance
//            double distance = player.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D));
            
            // if player is within range of activating the wither tree
//            if ((distance < 0.0D || distance < activatingProximity * activatingProximity)) {
            	
            	// TODO if mob has not spawned, then spawn it -- that is for the wither spawner log/heart block
            	
//            }

//        }
        
    }
}
