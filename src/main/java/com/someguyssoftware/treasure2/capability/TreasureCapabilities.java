package com.someguyssoftware.treasure2.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class TreasureCapabilities {
    // TODO this is where all capabilities will be

    /*
	 * NOTE Ensure to use interfaces in @CapabilityInject, the static capability and in the instance.
	 */
	@CapabilityInject(IEffectiveMaxDamageCapability.class)
    public static Capability<IEffectiveMaxDamageCapability> EFFECTIVE_MAX_DAMAGE_CAPABILITY = null;
    
    @CapabilityInject(ICharmableCapability.class)
    public static Capability<ICharmableCapability> CHARMABLE_CAPABILITY = null;
}