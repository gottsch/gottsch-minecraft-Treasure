/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import com.someguyssoftware.treasure2.Treasure;

/**
 * @author Mark Gottschling on May 5, 2020
 *
 */
public class CharmStateFactory {

	/**
	 * 
	 * @param charm
	 * @return
	 */
	public static ICharmState createCharmState(ICharm charm) {
		ICharmState state = null;
		if (charm.getCharmType() == CharmType.ILLUMINATION) {			
			state =  new IlluminationCharmState(charm);
			Treasure.logger.debug("creating illumination charm state for charm -> {}", state);
		}
		else {
//			Treasure.logger.debug("creating charm state for charm -> {}", charm);
			state = new CharmState(charm);
		}
		return state;
	}
	
	/**
	 * 
	 * @param charm
	 * @param vitals
	 * @return
	 */
	public static ICharmState createCharmState(ICharm charm, ICharmVitals vitals) {
		ICharmState state = createCharmState(charm);
		state.setVitals(vitals);
		return state;
	}

	public static ICharmVitals createCharmVitals(ICharm charm) {
		ICharmVitals vitals = null;
		if (charm.getCharmType() == CharmType.ILLUMINATION) {
//			Treasure.logger.debug("creating illumination charm vitals for charm -> {}", charm);
			vitals =  new IlluminationCharmVitals();
		}
		else {
//			Treasure.logger.debug("creating charm vitals for charm -> {}", charm);
			vitals = new CharmVitals();
		}
		return vitals;
	}
}
