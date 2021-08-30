package com.someguyssoftware.treasure2.capability;

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.CHARMABLE;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CharmableCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
	
    // capabilities for item
	private final ICharmableCapability instance;// = new CharmableCapability();
	
	/**
	 * 
	 */
	public CharmableCapabilityProvider() {
		instance = new CharmableCapability();
	}
	
	/**
	 * 
	 * @param capability
	 */
	public CharmableCapabilityProvider(ICharmableCapability capability) {
		instance = capability;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		if (capability == CHARMABLE) {
			return  (LazyOptional<T>) LazyOptional.of(() -> instance);
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = (CompoundNBT)CHARMABLE.getStorage().writeNBT(CHARMABLE, instance, null);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		CHARMABLE.getStorage().readNBT(CHARMABLE, instance, null, nbt);
	}
}
