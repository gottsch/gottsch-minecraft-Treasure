package com.someguyssoftware.treasure2.tileentity;

/**
 * @author Mark Gottschling on 9/4/2020
 */
publice interface ITreasureChestTileEntity extends IInventory {

    public List<LockState> getLockStates();
    public void setLockStates(List<LockState> lockStates);

    public int getFacing();
    public void setFacing(int facing);
}