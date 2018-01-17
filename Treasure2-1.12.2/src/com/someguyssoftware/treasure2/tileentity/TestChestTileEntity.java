package com.someguyssoftware.treasure2.tileentity;

import javax.annotation.Nullable;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TestChest;
import com.someguyssoftware.treasure2.lock.LockState;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

@Deprecated
public class TestChestTileEntity extends TileEntity {
	/*
	 * An array of lockStates the chest has. The array should be the size of the max allowed for the chestType.
	 */	
	private LockState[] lockStates;
	
	public TestChestTileEntity() {
		lockStates = new LockState[1];
	}
	
	/**
	 * Sync client and server states
	 */
	public void sendUpdates() {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	

	/**
	 * 
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound parentNBT) {
		try {
			parentNBT = super.writeToNBT(parentNBT);
			if (lockStates != null) {
				NBTTagList list = new NBTTagList();
				// write custom tile entity properties
				for (int i = 0; i < lockStates.length; i++) {
					if (lockStates[i] != null) {
						Treasure.logger.debug("writing lock state:" + this.getLockStates()[i]);
						NBTTagCompound stateNBT = new NBTTagCompound();
						stateNBT = lockStates[i].writeToNBT(stateNBT);
						list.appendTag(stateNBT);
					}
					parentNBT.setTag("lockStates", list);
				}		
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Error writing to NBT:",  e);
		}
		return parentNBT;
	}
	
	/**
	 * 
	 */
	@Override
	public void readFromNBT(NBTTagCompound parentNBT) {
		super.readFromNBT(parentNBT);
		
		try {
			// read the colliding player list
			if (parentNBT.hasKey("lockStates")) {
				if (this.getLockStates() == null) {
					TestChest block = (TestChest) this.getWorld().getBlockState(this.getPos()).getBlock();
					setLockStates(new LockState[block.getChestType().getMaxLocks()]);
				}
				NBTTagList list = parentNBT.getTagList("lockStates", Constants.NBT.TAG_COMPOUND);
				for (int i = 0; i < list.tagCount(); i++) {				
					NBTTagCompound c = list.getCompoundTagAt(i);
					this.getLockStates()[i] = LockState.readFromNBT(c);
					Treasure.logger.debug("Loaded lockstate:" + this.getLockStates()[i]);
				}
			}
		}
		catch(Exception e) {
			Treasure.logger.error("Error reading to NBT:",  e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
	}
	
	/**
	 * 
	 */
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
//		IBlockState state = this.world.getBlockState(this.pos);
//		this.world.notifyBlockUpdate(this.pos, state, state, 3); // <--- this should notify client of changes.
	}

	/* 
	 * Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
	 */
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return nbtTagCompound;
	}

	/* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
 */
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	/**
	 * @return the lockStates
	 */
	public LockState[] getLockStates() {
		return lockStates;
	}

	/**
	 * @param lockStates the lockStates to set
	 */
	public void setLockStates(LockState[] locks) {
		this.lockStates = locks;
	}
}
