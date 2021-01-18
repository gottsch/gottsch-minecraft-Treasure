package com.someguyssoftware.treasure2.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ExampleItemCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {

    // capabilities for item
    private final LazyOptional<IEffectiveMaxDamageCapability> holder = LazyOptional.of(EffectiveMaxDamageCapability::new);

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return TreasureCapabilities.EFFECTIVE_MAX_DAMAGE_CAPABILITY.orEmpty(capability, holder);
	}


	@Override
	public CompoundNBT serializeNBT() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		// TODO Auto-generated method stub
		
	}
}