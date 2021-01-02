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

public class CharmableCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {
	/*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(ICharmableCapability.class)
    public static Capability<ICharmableCapability> CHARMABLE_CAPABILITY = null;
    private final ICharmableCapability charmableCapability = new CharmableCapability();
    @CapabilityInject(ICharmCapability.class)
    public static Capability<ICharmCapability> CHARM_CAPABILITY = null;	
	private final ICharmCapability charmCapability = new CharmCapability();
	
	/**
	 * 
	 */
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CHARMABLE_CAPABILITY) {
			return true;
		}
        if (capability == CHARM_CAPABILITY) {
			return true;
		}
		return false;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CHARMABLE_CAPABILITY) {
			return CHARMABLE_CAPABILITY.cast(this.charmableCapability);
		}
        if (capability == CHARM_CAPABILITY) {
			return CHARM_CAPABILITY.cast(this.charmCapability);
		}
		return null;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound parentTag = new NBTTagCompound();
		NBTTagCompound charmTag =  (NBTTagCompound) CHARM_CAPABILITY.getStorage().writeNBT(CHARM_CAPABILITY, this.charmCapability, null);
		NBTTagCompound adornmentTag = (NBTTagCompound) CHARMABLE_CAPABILITY.getStorage().writeNBT(CHARMABLE_CAPABILITY, charmableCapability, null);
		parentTag.setTag("charm", charmTag);
		parentTag.setTag("adornment", adornmentTag);
		return parentTag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag) {
		// TODO test for the child tags
		if (tag.hasKey("charm")) {
			CHARM_CAPABILITY.getStorage().readNBT(CHARM_CAPABILITY, this.charmCapability, null, tag.getTag("charm"));	
		}
		if (tag.hasKey("adornment")) {
			CHARMABLE_CAPABILITY.getStorage().readNBT(CHARMABLE_CAPABILITY, charmableCapability, null, tag.getTag("adornment"));
		}		
	}
}
