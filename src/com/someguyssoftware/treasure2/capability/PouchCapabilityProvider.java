/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import com.someguyssoftware.treasure2.inventory.KeyRingInventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	private static final String INVENTORY_TAG = "inventory";
	
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> INVENTORY_CAPABILITY = null;		
	private final ItemStackHandler inventory_instance = new ItemStackHandler(KeyRingInventory.INVENTORY_SIZE);
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound parentTag = new NBTTagCompound();
		NBTTagCompound inventoryTag = this.inventory_instance.serializeNBT();
		parentTag.setTag(INVENTORY_TAG, inventoryTag);
		return parentTag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
		this.inventory_instance.deserializeNBT(nbt.getCompoundTag(INVENTORY_TAG));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == INVENTORY_CAPABILITY ) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INVENTORY_CAPABILITY) {
			return INVENTORY_CAPABILITY.cast(this.inventory_instance);
		}
		return null;
	}
}
