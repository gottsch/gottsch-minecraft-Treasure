/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on May 5, 2020
 *
 */
public class IlluminationCharmState extends CharmState {

	/**
	 * 
	 * @param charm
	 */
	public IlluminationCharmState(ICharm charm) {
		super(charm);
		setVitals(new IlluminationCharmVitals(charm.getMaxValue(), charm.getMaxDuration(), charm.getMaxPercent()));
//		Treasure.logger.debug("illum vitals -> {}", getVitals().getClass().getSimpleName());
	}

	@Override
	public String toString() {
		return "IlluminationCharmState [getCharm()=" + getCharm() + ", getVitals()=" + getVitals() + "]";
	}
}
