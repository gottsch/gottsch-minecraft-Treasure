/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.someguyssoftware.gottschcore.tileentity.AbstractModTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;


/**
 * @author Mark Gottschling on Feb 16, 2020
 *
 */
public class MistEmitterTileEntity extends AbstractModTileEntity implements ITickableTileEntity {
	public static final float DEFAULT_PROXIMITY = 5.0F;
	private float proximity = DEFAULT_PROXIMITY;
	private boolean active = false;
	private List<PlayerEntity> playersWithinProximity;

	/**
	 * 
	 */
	public MistEmitterTileEntity(TileEntityType<?> type) {
		super(type);
		playersWithinProximity = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public void tick() {
        if (WorldInfo.isServerSide()) {
        	return;
        }

    	// get all players within range
		int x = getBlockPos().getX();
		int y = getBlockPos().getY();
		int z = getBlockPos().getZ();
		float radius = 5.0F;
        double proximitySq = getProximity() * getProximity();

        // clear list
        getPlayersWithinProximity().clear();
        setActive(false);
        
        // for each player
		for(PlayerEntity player : getLevel().getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB((double)((float)x - radius), (double)((float)y - radius), (double)((float)z - radius), 
		(double)((float)(x + 1) + radius), (double)((float)(y + 1) + radius), (double)((float)(z + 1) + radius)))) {
            // get the distance
            double distanceSq = player.distanceToSqr(x + 0.5D, y + 0.5D, z + 0.5D);
           
            if (distanceSq < proximitySq) {
            	// add player to list
            	getPlayersWithinProximity().add(player); // TODO this should be wrapped in a synchronized block
            	setActive(true);
            }
        }
	}
	
	/**
	 * 
	 */
	@Override
	public void load(BlockState state, CompoundNBT parentNBT) {
		super.load(state, parentNBT);
	}
	
	/**
	 * 
	 */
	@Override
	public CompoundNBT save(CompoundNBT tag) {
	    super.save(tag);
	    return tag;
	}

	public synchronized List<PlayerEntity> getPlayersWithinProximity() {
		return playersWithinProximity;
	}

	private synchronized void setPlayersWithinProximity(List<PlayerEntity> playersWithinProximity) {
		this.playersWithinProximity = playersWithinProximity;
	}

	public synchronized boolean isActive() {
		return active;
	}

	public synchronized void setActive(boolean active) {
		this.active = active;
	}

	public float getProximity() {
		return proximity;
	}

	public void setProximity(float proximity) {
		this.proximity = proximity;
	}
}