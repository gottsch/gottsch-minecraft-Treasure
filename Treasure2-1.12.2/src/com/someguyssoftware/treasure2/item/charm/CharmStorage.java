/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmStorage implements Capability.IStorage<ICharmCapability> {


	@Override
	public NBTBase writeNBT(Capability<ICharmCapability> capability, ICharmCapability instance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			if (instance.getCharmStates() != null) {
				NBTTagList stateTags = new NBTTagList();
				for (ICharmState state : instance.getCharmStates()) {					
					// create tag for state
					NBTTagCompound stateTag = new NBTTagCompound();
					// create sub tag for charm
					NBTTagCompound charmTag = new NBTTagCompound();
					ICharm charm = state.getCharm();
					charm.writeToNBT(charmTag);
					stateTag.setTag("charm", charmTag);
					// create sub tag for vitals
					NBTTagCompound vitalsTag = new NBTTagCompound();
					ICharmVitals vitals = state.getVitals();
					vitals.writeToNBT(vitalsTag);
					stateTag.setTag("vitals", vitalsTag);
//					Treasure.logger.debug("attempting to save charm vitals -> {}", vitals);

					// add the state to the list
					stateTags.appendTag(stateTag);
				}
				// add the state list of the main capabilities tag
				mainTag.setTag("charmStates", stateTags);
				// add the other properties -> modifiers
				mainTag.setDouble("valueModifier", instance.getCharmValueModifier());
				mainTag.setDouble("percentModifier", instance.getCharmPercentModifier());

			}
		} catch (Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<ICharmCapability> capability, ICharmCapability instance, EnumFacing side,
			NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			// clear the states
			instance.getCharmStates().clear();
			NBTTagCompound tag = (NBTTagCompound) nbt;
			NBTTagList stateListTag = tag.getTagList("charmStates", 10);
			for (int index = 0; index < stateListTag.tagCount(); index++) {
				NBTTagCompound stateTag = stateListTag.getCompoundTagAt(index);
				ICharm charm = Charm.readFromNBT(stateTag.getCompoundTag("charm"));
				// TODO need a factory here because we don't know what kind of vitals and state is needed.
				ICharmVitals vitals = CharmStateFactory.createCharmVitals(charm);
//				ICharmVitals vitals = new CharmVitals();
				vitals.readFromNBT(stateTag.getCompoundTag("vitals"));
				Treasure.logger.debug("attempted to read {} charm state -> {}", charm.getCharmType(), vitals);
				ICharmState charmState = CharmStateFactory.createCharmState(charm, vitals);
//				ICharmState charmState = new CharmState(charm, vitals);
				instance.getCharmStates().add(charmState);
			}

			// TODO these 2 are modifier's owned by the Item. If they are not 1.0, then a new charm has to be recreated from the old charm incorporating these values.
			// load other props of charm capability
			if (tag.hasKey("valueModifier")) {
				instance.setCharmValueModifier(tag.getDouble("valueModifier"));
			}
			if (tag.hasKey("percentModifier")) {
				instance.setCharmPercentModifier(tag.getDouble("percentModifier"));
			}
		} else {
			Treasure.logger.warn("Not a tag compound!");
		}

	}

}
