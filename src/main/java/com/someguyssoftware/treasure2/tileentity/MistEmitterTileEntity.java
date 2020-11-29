/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.someguyssoftware.gottschcore.tileentity.AbstractModTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * @author Mark Gottschling on Feb 16, 2020
 *
 */
public class MistEmitterTileEntity extends AbstractModTileEntity implements ITickable {
	public static final float DEFAULT_PROXIMITY = 5.0F;
	private float proximity = DEFAULT_PROXIMITY;
	private boolean active = false;
	private List<EntityPlayer> playersWithinProximity;

	/**
	 * 
	 */
	public MistEmitterTileEntity() {
		playersWithinProximity = Collections.synchronizedList(new ArrayList<>());
	}
	
	@Override
	public void update() {
        if (WorldInfo.isServerSide()) {
        	return;
        }

    	// get all players within range
        EntityPlayer player = null;
        double proximitySq = getProximity() * getProximity();

        // clear list
        getPlayersWithinProximity().clear();
        setActive(false);
        
        // for each player
        for (int playerIndex = 0; playerIndex < getWorld().playerEntities.size(); ++playerIndex) {
            player = (EntityPlayer)getWorld().playerEntities.get(playerIndex);
            // get the distance
            double distanceSq = player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D));
           
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
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);
	}
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
	    super.writeToNBT(tag);
	    return tag;
	}

	public synchronized List<EntityPlayer> getPlayersWithinProximity() {
		return playersWithinProximity;
	}

	private synchronized void setPlayersWithinProximity(List<EntityPlayer> playersWithinProximity) {
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
