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
public class DurabilityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	private final DurabilityCapability instance = new DurabilityCapability();	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = (NBTTagCompound)TreasureCapabilities.DURABILITY.getStorage().writeNBT(TreasureCapabilities.DURABILITY, instance, null);
		return tag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
		TreasureCapabilities.DURABILITY.getStorage().readNBT(TreasureCapabilities.DURABILITY, this.instance, null, nbt);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.DURABILITY) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == TreasureCapabilities.DURABILITY) {
			return TreasureCapabilities.DURABILITY.cast(this.instance);
		}
		return null;
	}
}
