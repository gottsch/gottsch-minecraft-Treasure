/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.block.entity;

import java.util.List;

import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity.GenerationContext;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;


/**
 * @author Mark Gottschling on Dec 15, 2020
 *
 */
public interface ITreasureChestBlockEntity { //extends Inventory {
    public List<LockState> getLockStates();
    public void setLockStates(List<LockState> lockStates);
    // TODO can be removed as a required method
	boolean hasLocks();
	
	// TODO change to heading
    public Direction getFacing();
    public void setFacing(int facing);
    
    public boolean isSealed();
    public void setSealed(boolean sealed);

	ResourceLocation getLootTable();
	void setLootTable(ResourceLocation lootTable);
	
	GenerationContext getGenerationContext();
	void setGenerationContext(GenerationContext generationContext);
	
	ResourceLocation getMimic();
	void setMimic(ResourceLocation mimic);

	int getInventorySize();
	
	//??
	void sendUpdates();
	
	public void tickClient();
	public void tickParticle();
	public void tickServer();
	
	boolean isLocked();
	Component getDefaultName();
	
	// wrap vanilla 
	public void saveAdditional(CompoundTag tag);

}
