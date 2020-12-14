/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(ICharmCapability.class)
	public static Capability<ICharmCapability> CHARM_CAPABILITY = null;	
	private final ICharmCapability instance = new CharmCapability();
	
	/**
	 * 
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CHARM_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CHARM_CAPABILITY) {
			return CHARM_CAPABILITY.cast(this.instance);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return (NBTTagCompound) CHARM_CAPABILITY.getStorage().writeNBT(CHARM_CAPABILITY, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		CHARM_CAPABILITY.getStorage().readNBT(CHARM_CAPABILITY, this.instance, null, nbt);	
	}
}
