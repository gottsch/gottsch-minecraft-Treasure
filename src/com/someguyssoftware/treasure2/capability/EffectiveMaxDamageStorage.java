/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import static com.someguyssoftware.treasure2.Treasure.logger;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class EffectiveMaxDamageStorage implements Capability.IStorage<IEffectiveMaxDamageCapability> {
    private static final String EFFECTIVE_MAX_DAMAGE_TAG = "effectiveMaxDamage";
    
	@Override
	public NBTBase writeNBT(Capability<IEffectiveMaxDamageCapability> capability, IEffectiveMaxDamageCapability instance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			mainTag.setInteger(EFFECTIVE_MAX_DAMAGE_TAG, instance.getEffectiveMaxDamage());
		} catch (Exception e) {
			logger.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IEffectiveMaxDamageCapability> capability, IEffectiveMaxDamageCapability instance, EnumFacing side,
			NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey(EFFECTIVE_MAX_DAMAGE_TAG)) {
				instance.setEffectiveMaxDamage(tag.getInteger(EFFECTIVE_MAX_DAMAGE_TAG));
			}
		}		
	}
}
