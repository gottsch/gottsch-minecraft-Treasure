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
public class DurabilityCapabilityStorage implements Capability.IStorage<IDurabilityCapability> {
	@Deprecated
    private static final String EFFECTIVE_MAX_DAMAGE_TAG = "effectiveMaxDamage";
	private static final String MAX_DURABILITY_TAG = "maxDurability";
	private static final String DURABILITY_TAG = "durability";
	private static final String INFINITE_TAG = "infinite";
    
	@Override
	public NBTBase writeNBT(Capability<IDurabilityCapability> capability, IDurabilityCapability instance, EnumFacing side) {
		NBTTagCompound mainTag = new NBTTagCompound();
		try {
			mainTag.setInteger(DURABILITY_TAG, instance.getDurability());
			mainTag.setInteger(MAX_DURABILITY_TAG, instance.getMaxDurability());
			mainTag.setBoolean(INFINITE_TAG, instance.isInfinite());
		} catch (Exception e) {
			logger.error("Unable to write state to NBT:", e);
		}
		return mainTag;
	}

	@Override
	public void readNBT(Capability<IDurabilityCapability> capability, IDurabilityCapability instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			
			if (tag.hasKey(MAX_DURABILITY_TAG)) {
				instance.setMaxDurability(tag.getInteger(MAX_DURABILITY_TAG));
			}
			
			if (tag.hasKey(DURABILITY_TAG)) {
				instance.setDurability(tag.getInteger(DURABILITY_TAG));
			}
			// legacy
			else if (tag.hasKey(EFFECTIVE_MAX_DAMAGE_TAG)) {
				instance.setDurability(tag.getInteger(EFFECTIVE_MAX_DAMAGE_TAG));
			}
			
			if (tag.hasKey(INFINITE_TAG)) {
				instance.setInfinite(tag.getBoolean(INFINITE_TAG));
			}
		}		
	}
}
