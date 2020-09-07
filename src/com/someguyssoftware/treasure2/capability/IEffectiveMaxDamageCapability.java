package com.someguyssoftware.treasure2.capability;

/**
 * 
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public interface IEffectiveMaxDamageCapability {
    public static final EFFECTIVE_MAX_DAMAGE_MAX = 1000; 

	public int getEffectiveMaxDamage()

	public void setEffectiveMaxDamage(int damage);
}