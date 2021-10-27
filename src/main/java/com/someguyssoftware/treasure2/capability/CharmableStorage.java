/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
@Deprecated
public class CharmableStorage implements Capability.IStorage<ICharmableCapability> {
    private static final String CHARM_INSTANCES_TAG = "charmInstances";
    private static final String SLOTS_TAG = "slots";
    private static final String CUSTOM_NAME_TAG = "customName";

	@Override
	public NBTBase writeNBT(Capability<ICharmableCapability> capability, ICharmableCapability charmCapabilityInstance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			mainTag.setInteger("slots", charmCapabilityInstance.getSlots());
			if (charmCapabilityInstance.getCustomName() != null) {
				mainTag.setString("customName", charmCapabilityInstance.getCustomName());
			}
		} catch (Exception e) {
			Treasure.logger.error("Unable to write instance to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<ICharmableCapability> capability, ICharmableCapability adornmentCapabilityInstance, EnumFacing side,
			NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
                int slots = tag.getInteger(SLOTS_TAG);
                adornmentCapabilityInstance.setSlots(slots);
                if (tag.hasKey(CUSTOM_NAME_TAG)) {
                	String customName = tag.getString(CUSTOM_NAME_TAG);
                    adornmentCapabilityInstance.setCustomName(customName);
                }
		} else {
			Treasure.logger.warn("Not a tag compound!");
		}
	}
}
