/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.KeyRingItem;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Mark Gottschling on May 12, 2020
 *
 */
public class KeyRingStorage implements Capability.IStorage<IKeyRingCapability> {
	private static final String IS_OPEN_TAG = "isOpen";
	private static final String USED_ON_CHEST_TAG = "usedOnChest";
	@Override
	public NBTBase writeNBT(Capability<IKeyRingCapability> capability, IKeyRingCapability instance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			mainTag.setBoolean(IS_OPEN_TAG, instance.isOpen());
			mainTag.setBoolean(USED_ON_CHEST_TAG, instance.isUsedOnChest());
		} catch (Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IKeyRingCapability> capability, IKeyRingCapability instance, EnumFacing side,
			NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey(IS_OPEN_TAG)) {
				instance.setOpen(tag.getBoolean(IS_OPEN_TAG));
			}
			if (tag.hasKey(USED_ON_CHEST_TAG)) {
				instance.setUsedOnChest(tag.getBoolean(USED_ON_CHEST_TAG));
			}
		}		
	}
}
