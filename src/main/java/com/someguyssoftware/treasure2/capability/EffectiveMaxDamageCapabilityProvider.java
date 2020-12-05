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
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class EffectiveMaxDamageCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(IEffectiveMaxDamageCapability.class)
	public static Capability<IEffectiveMaxDamageCapability> EFFECTIVE_MAX_DAMAGE_CAPABILITY = null;
	private final EffectiveMaxDamageCapability instance = new EffectiveMaxDamageCapability();
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = (NBTTagCompound)EFFECTIVE_MAX_DAMAGE_CAPABILITY.getStorage().writeNBT(EFFECTIVE_MAX_DAMAGE_CAPABILITY, instance, null);
		return tag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
		EFFECTIVE_MAX_DAMAGE_CAPABILITY.getStorage().readNBT(EFFECTIVE_MAX_DAMAGE_CAPABILITY, this.instance, null, nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == EFFECTIVE_MAX_DAMAGE_CAPABILITY) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == EFFECTIVE_MAX_DAMAGE_CAPABILITY) {
			return EFFECTIVE_MAX_DAMAGE_CAPABILITY.cast(this.instance);
		}
		return null;
	}
}
