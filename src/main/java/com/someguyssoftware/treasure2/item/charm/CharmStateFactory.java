/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

/**
 * @author Mark Gottschling on May 5, 2020
 *
 */
@Deprecated
public class CharmStateFactory {

	/**
	 * 
	 * @param charm
	 * @return
	 */
	public static ICharmState createCharmState(ICharm charm) {
		ICharmState state = null;

		ICharmVitals vitals = createCharmVitals(charm);
		state = new CharmState(charm, vitals);
//		if (charm.getCharmType() == CharmType.ILLUMINATION)
//			Treasure.logger.debug("creating charm state for charm -> {}", state);

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

	/**
	 * 
	 * @param charm
	 * @return
	 */
	public static ICharmVitals createCharmVitals(ICharm charm) {
		ICharmVitals vitals = null;
		if (charm.getCharmType() == CharmType.ILLUMINATION) {
//			Treasure.logger.debug("creating illumination charm vitals for charm -> {}", charm);
			vitals = new IlluminationCharmVitals(charm);
		} else {
//			Treasure.logger.debug("creating charm vitals for charm -> {}", charm);
			vitals = new CharmVitals(charm);
		}
		return vitals;
	}
}
