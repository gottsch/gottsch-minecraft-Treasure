package com.someguyssoftware.treasure2.capability;

import static com.someguyssoftware.treasure2.capability.DurabilityCapability.DURABILITY_CAPABILITY;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

// TODO new way - instead of creating a class, attach it in an event and inline the code?
public class DurabilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {


    // capabilities for item
//    private final LazyOptional<IDurabilityCapability> instance = LazyOptional.of(DurabilityCapability::new);
	private final IDurabilityCapability instance = new DurabilityCapability();
//    private final IDurabilityCapability instance = DURABILITY_CAPABILITY.getDefaultInstance();

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
		return DURABILITY_CAPABILITY.orEmpty(capability, LazyOptional.of(() -> instance));
	}


	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = (CompoundNBT)DURABILITY_CAPABILITY.getStorage().writeNBT(DURABILITY_CAPABILITY, instance, null);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		DURABILITY_CAPABILITY.getStorage().readNBT(DURABILITY_CAPABILITY, instance, null, nbt);
	}
}