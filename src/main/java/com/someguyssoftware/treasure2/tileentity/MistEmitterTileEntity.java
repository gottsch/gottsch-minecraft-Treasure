/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.someguyssoftware.gottschcore.tileentity.AbstractModTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;


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
        PlayerEntity player = null;
        double proximitySq = getProximity() * getProximity();

        // clear list
        getPlayersWithinProximity().clear();
        setActive(false);
        
        // for each player
        for (int playerIndex = 0; playerIndex < getWorld().getPlayers().size(); ++playerIndex) {
            player = (PlayerEntity)getWorld().getPlayers().get(playerIndex);
            // get the distance
            double distanceSq = player.getDistanceSq((double)getPos().getX() + 0.5D, 
            		(double)getPos().getY() + 0.5D, (double)getPos().getZ() + 0.5D);
           
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
	public void read(CompoundNBT parentNBT) {
		super.read(parentNBT);
	}
	
	/**
	 * 
	 */
	@Override
	public CompoundNBT write(CompoundNBT tag) {
	    super.write(tag);
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