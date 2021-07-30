package com.someguyssoftware.treasure2.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class DurabilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    // capabilities for item
    private final LazyOptional<IDurabilityCapability> holder = LazyOptional.of(DurabilityCapability::new);

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return TreasureCapabilities.DURABILITY_CAPABILITY.orEmpty(capability, holder);
	}


	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = (CompoundNBT)TreasureCapabilities.DURABILITY_CAPABILITY.getStorage().writeNBT(TreasureCapabilities.DURABILITY_CAPABILITY, holder, null);
		return tag;
	}


	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		// TODO Auto-generated method stub
		
	}
}