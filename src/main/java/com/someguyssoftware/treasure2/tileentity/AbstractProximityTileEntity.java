/**
 * 
 */
package com.someguyssoftware.treasure2.tileentity;

import java.util.Random;

import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.tileentity.AbstractModTileEntity;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public abstract class AbstractProximityTileEntity extends AbstractModTileEntity implements IProximityTileEntity, ITickable {
	private double proximity;
	private boolean isDead = false;
	
	/**
	 * 
	 */
	public AbstractProximityTileEntity() {}	
	
	/**
	 * 
	 */
	public AbstractProximityTileEntity(double proximity) {
		setProximity(proximity);
	}

	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);
		try {
			// read the custom name
			if (parentNBT.hasKey("proximity", 8)) {
				this.proximity = parentNBT.getDouble("proximity");
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error reading AbstractProximity properties from NBT:",  e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
	    super.writeToNBT(tag);
	    tag.setDouble("proximity", getProximity());
	    return tag;
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.util.ITickable#update()
	 */
	@Override
	public void update() {
        if (WorldInfo.isClientSide()) {
        	return;
        }      
        
    	// get all players within range
        EntityPlayer player = null;

        boolean isTriggered = false;         
        double proximitySq = getProximity() * getProximity();
        if (proximitySq < 1) proximitySq = 1;
        
        // for each player
        for (int playerIndex = 0; playerIndex < getWorld().playerEntities.size(); ++playerIndex) {
            player = (EntityPlayer)getWorld().playerEntities.get(playerIndex);
            // get the distance
            double distanceSq = player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D));
//            GottschCore.logger.debug("PTE @ -> {}, proximity -> {}, distance -> {}, triggered -> {}, dead -> {}, result -> {}", 
//            		this.pos, proximitySq, distanceSq, isTriggered, this.isDead, (!isTriggered && !this.isDead && (distanceSq < proximitySq)) ? "met" : "not met");
            if (!isTriggered && !this.isDead && (distanceSq < proximitySq)) {
            	GottschCore.logger.debug("PTE proximity was met.");
            	isTriggered = true;
            	// exectute action
            	execute(this.getWorld(), new Random(), new Coords(this.getPos()), new Coords(player.getPosition()));

            	// NOTE: does not self-destruct that is up to the execute action to perform
            }
            
            if (this.isDead) break;            
        }
	}

	@Override
	abstract public void execute(World world, Random random, Coords blockCoords, Coords playerCoords);
	
	/**
	 * @return the proximity
	 */
	@Override
	public double getProximity() {
		return proximity;
	}

	/**
	 * @param proximity the proximity to set
	 */
	@Override
	public void setProximity(double proximity) {
		this.proximity = proximity;
	}

	/**
	 * @return the isDead
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * @param isDead the isDead to set
	 */
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

}