/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class EffectiveMaxDamageCapability implements IEffectiveMaxDamageCapability  {
	private int effectiveMaxDamage;
	
	/**
	 * 
	 */
	public EffectiveMaxDamageCapability() {
		
	}
	
	@Override
	public int getEffectiveMaxDamage() {
		return effectiveMaxDamage;
	}
	
	@Override
	public void setEffectiveMaxDamage(int maxDamage) {
		if (maxDamage > IEffectiveMaxDamageCapability.EFFECTIVE_MAX_DAMAGE_MAX) {
            this.effectiveMaxDamage = IEffectiveMaxDamageCapability.EFFECTIVE_MAX_DAMAGE_MAX;
        }
        else {
            this.effectiveMaxDamage = maxDamage;
        }
	}
}
