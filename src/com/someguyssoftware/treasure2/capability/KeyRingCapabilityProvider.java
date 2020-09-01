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
 * @author Mark Gottschling on May 11, 2020
 *
 */
public class KeyRingCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	private static final String INVENTORY_TAG = "inventory";
	private static final String STATE_TAG = "state";
	
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(IItemHandler.class)
	public static Capability<IItemHandler> KEY_RING_INVENTORY_CAPABILITY = null;	
	@CapabilityInject(IKeyRingCapability.class)
	public static Capability<IKeyRingCapability> KEY_RING_CAPABILITY = null;
	
	private final ItemStackHandler inventory_instance = new ItemStackHandler(KeyRingInventory.INVENTORY_SIZE);
	private final KeyRingCapability instance = new KeyRingCapability();
	
	/**
	 * 
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound parentTag = new NBTTagCompound();
		NBTTagCompound inventoryTag = this.inventory_instance.serializeNBT();
		parentTag.setTag(INVENTORY_TAG, inventoryTag);
		
		NBTTagCompound stateTag = (NBTTagCompound)KEY_RING_CAPABILITY.getStorage().writeNBT(KEY_RING_CAPABILITY, instance, null);
		parentTag.setTag(STATE_TAG, stateTag);
		return parentTag;
	}

	/**
	 * 
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {		
		this.inventory_instance.deserializeNBT(nbt.getCompoundTag(INVENTORY_TAG));
		KEY_RING_CAPABILITY.getStorage().readNBT(KEY_RING_CAPABILITY, this.instance, null, nbt.getCompoundTag(STATE_TAG));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == KEY_RING_INVENTORY_CAPABILITY || capability == KEY_RING_CAPABILITY) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == KEY_RING_INVENTORY_CAPABILITY) {
			return KEY_RING_INVENTORY_CAPABILITY.cast(this.inventory_instance);
		}
		else if (capability == KEY_RING_CAPABILITY) {
			return KEY_RING_CAPABILITY.cast(this.instance);
		}
		return null;
	}
}
