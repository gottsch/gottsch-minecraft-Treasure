/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.item.charm.ICharmData;
import com.someguyssoftware.treasure2.item.charm.ICharmInstance;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
@Deprecated
public class CharmStorage implements Capability.IStorage<ICharmCapability> {
    private static final String CHARM_INSTANCES_TAG = "charmInstances";
    private static final String LEGACY_CHARM_INSTANCES_TAG = "charmStates";
    private static final String CHARM_TAG = "charm";
    private static final String CHARM_DATA_TAG = "data";
    private static final String LEGACY_CHARM_DATA_TAG = "data";

	@Override
	public NBTBase writeNBT(Capability<ICharmCapability> capability, ICharmCapability charmCapabilityInstance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			if (charmCapabilityInstance.getCharmInstances() != null) {
				NBTTagList instanceTags = new NBTTagList();
				for (ICharmInstance instance : charmCapabilityInstance.getCharmInstances()) {					
					// create tag for instance
					NBTTagCompound instanceTag = new NBTTagCompound();
					// create sub tag for charm
					NBTTagCompound charmTag = new NBTTagCompound();
					ICharm charm = instance.getCharm();
					charm.writeToNBT(charmTag);
					instanceTag.setTag(CHARM_TAG, charmTag);
					// create sub tag for data
					NBTTagCompound dataTag = new NBTTagCompound();
					ICharmData data = instance.getData();
					data.writeToNBT(dataTag);
					instanceTag.setTag(CHARM_DATA_TAG, dataTag);
//					Treasure.logger.debug("attempting to save charm instance -> {}", instance);

					// add the instance to the list
					instanceTags.appendTag(instanceTag);
				}
				// add the instance list of the main capabilities tag
				mainTag.removeTag(CHARM_INSTANCES_TAG);		
				mainTag.setTag(CHARM_INSTANCES_TAG, instanceTags);
			}
		} catch (Exception e) {
			Treasure.logger.error("Unable to write instance to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<ICharmCapability> capability, ICharmCapability charmCapabilityInstance, EnumFacing side,
			NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			// clear the states
			charmCapabilityInstance.getCharmInstances().clear();
            NBTTagCompound tag = (NBTTagCompound) nbt;
            NBTTagList instanceListTag = tag.getTagList(CHARM_INSTANCES_TAG, 10);
            if (instanceListTag == null || instanceListTag.tagCount() == 0) {
                instanceListTag = tag.getTagList(LEGACY_CHARM_INSTANCES_TAG, 10);
            }
			for (int index = 0; index < instanceListTag.tagCount(); index++) {
				NBTTagCompound instanceTag = instanceListTag.getCompoundTagAt(index);
				Optional<ICharm> charm = Charm.readFromNBT(instanceTag.getCompoundTag(CHARM_TAG));
				if (!charm.isPresent()) {
					continue;
				}
                
                NBTTagCompound dataTag = instanceTag.getCompoundTag(CHARM_DATA_TAG);
                if (dataTag == null) {
                    dataTag = instanceTag.getCompoundTag(LEGACY_CHARM_DATA_TAG);
                }
                /* Removed 10/27/21
                ICharmInstance instance = charm.get().createInstance();
                if (dataTag != null) {
                    instance.getData().readFromNBT(dataTag);
                }
//				Treasure.logger.debug("attempted to read {} charm instance -> {}", charm.getCharmType(), data);
				charmCapabilityInstance.getCharmInstances().add(instance);
				*/
			}
		} else {
			Treasure.logger.warn("Not a tag compound!");
		}
	}
}
