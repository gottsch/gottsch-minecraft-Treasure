/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
public class TreasureCharms {

	public static final ICharm LESSER_HEALING;
	public static final ICharm HEALING;
	public static final ICharm GREATER_HEALING;
	public static final ICharm GRAND_HEALING;
	public static final ICharm SALANDAARS_CONVALESCENCE;
	
	public static final ICharm DURABLE_SHIELDING;
	public static final ICharm STOUT_SHIELDING;
	public static final ICharm HARDENED_SHIELDING;
	public static final ICharm POWERFUL_SHIELDING;
	public static final ICharm ARMADILLO_SHIELDING;
	
	public static final ICharm FULLNESS;
	public static final ICharm SATED_FULLNESS;
	public static final ICharm OVERFED_FULLNESS;
	public static final ICharm GORGED_FULLNESS;
	
	public static final ICharm MINOR_HARVESTING;
	public static final ICharm HARVESTING;
	public static final ICharm MAJOR_HARVESTING;
	public static final ICharm GRAND_HARVESTING;
		
//	public static final ICharm ILLUMINATION;
	
	// curses
	public static final ICharm LESSER_DECAY;
	public static final ICharm DECAY;
	public static final ICharm GREATER_DECAY;
	public static final ICharm PERISHING_DECAY;
	
	public static final Map<String, ICharm> REGISTRY = new HashMap<>();
	
	static {
		/*
		 * NOTE don't forge to update the CharmBuilder to create the correct concrete Charm class.
		 */
		LESSER_HEALING = new CharmBuilder("lesser_healing", CharmType.HEALING, CharmLevel.LEVEL1).build();
		HEALING = new CharmBuilder("healing", CharmType.HEALING, CharmLevel.LEVEL2).build();
		GREATER_HEALING = new CharmBuilder("greater_healing", CharmType.HEALING, CharmLevel.LEVEL3).build();
		GRAND_HEALING = new CharmBuilder("grand_healing", CharmType.HEALING, CharmLevel.LEVEL4).build();
		SALANDAARS_CONVALESCENCE = new CharmBuilder("salandaars_convalescence", CharmType.HEALING, CharmLevel.LEVEL4).valueModifier(1.5).build();
		
		DURABLE_SHIELDING = new CharmBuilder("durable_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).build();
		STOUT_SHIELDING = new CharmBuilder("stout_shielding", CharmType.SHIELDING, CharmLevel.LEVEL2).build();
		HARDENED_SHIELDING = new CharmBuilder("hardened_shielding", CharmType.SHIELDING, CharmLevel.LEVEL3).build();
		POWERFUL_SHIELDING = new CharmBuilder("powerful_shielding", CharmType.SHIELDING, CharmLevel.LEVEL4).build();
		ARMADILLO_SHIELDING = new CharmBuilder("armidillo_shielding", CharmType.SHIELDING, CharmLevel.LEVEL4).valueModifier(1.5).build();
		
		FULLNESS = new CharmBuilder("fullness", CharmType.FULLNESS, CharmLevel.LEVEL1).build();
		SATED_FULLNESS = new CharmBuilder("sated_fullness", CharmType.FULLNESS, CharmLevel.LEVEL2).build(); 
		OVERFED_FULLNESS = new CharmBuilder("overfed_fullness", CharmType.FULLNESS, CharmLevel.LEVEL3).build(); 
		GORGED_FULLNESS = new CharmBuilder("gorged_fullness", CharmType.FULLNESS, CharmLevel.LEVEL4).build(); 
		
		MINOR_HARVESTING = new CharmBuilder("minor_havesting", CharmType.HARVESTING, CharmLevel.LEVEL1).build();
		HARVESTING = new CharmBuilder("havesting", CharmType.HARVESTING, CharmLevel.LEVEL2).build();
		MAJOR_HARVESTING = new CharmBuilder("major_havesting", CharmType.HARVESTING, CharmLevel.LEVEL3).build();
		GRAND_HARVESTING = new CharmBuilder("grand_havesting", CharmType.HARVESTING, CharmLevel.LEVEL4).build();
		
//		ILLUMINATION = new CharmBuilder("illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL1).build();
		
		// curses
		LESSER_DECAY = new CharmBuilder("lesser_decay", CharmType.DECAY, CharmLevel.LEVEL1).build();
		DECAY = new CharmBuilder("decay", CharmType.DECAY, CharmLevel.LEVEL2).build();
		GREATER_DECAY = new CharmBuilder("greater_decay", CharmType.DECAY, CharmLevel.LEVEL3).build();
		PERISHING_DECAY = new CharmBuilder("perishing_decay", CharmType.DECAY, CharmLevel.LEVEL4).build();
		
		// register all charms
		REGISTRY.put(LESSER_HEALING.getName(), LESSER_HEALING);
		REGISTRY.put(HEALING.getName(), HEALING);
		REGISTRY.put(GREATER_HEALING.getName(), GREATER_HEALING);
		REGISTRY.put(GRAND_HEALING.getName(), GRAND_HEALING);
		REGISTRY.put(SALANDAARS_CONVALESCENCE.getName(), SALANDAARS_CONVALESCENCE);
		
		REGISTRY.put(DURABLE_SHIELDING.getName(), DURABLE_SHIELDING);
		REGISTRY.put(STOUT_SHIELDING.getName(), STOUT_SHIELDING);
		REGISTRY.put(HARDENED_SHIELDING.getName(), HARDENED_SHIELDING);
		REGISTRY.put(POWERFUL_SHIELDING.getName(), POWERFUL_SHIELDING);
		REGISTRY.put(ARMADILLO_SHIELDING.getName(), ARMADILLO_SHIELDING);
		
		REGISTRY.put(FULLNESS.getName(), FULLNESS);
		REGISTRY.put(SATED_FULLNESS.getName(), SATED_FULLNESS);
		REGISTRY.put(OVERFED_FULLNESS.getName(), OVERFED_FULLNESS);
		REGISTRY.put(GORGED_FULLNESS.getName(), GORGED_FULLNESS);
		
		REGISTRY.put(MINOR_HARVESTING.getName(), MINOR_HARVESTING);
		REGISTRY.put(HARVESTING.getName(), HARVESTING);
		REGISTRY.put(MAJOR_HARVESTING.getName(), MAJOR_HARVESTING);
		REGISTRY.put(GRAND_HARVESTING.getName(), GRAND_HARVESTING);
		
//		REGISTRY.put(ILLUMINATION.getName(), ILLUMINATION);
		
		REGISTRY.put(LESSER_DECAY.getName(), LESSER_DECAY);
		REGISTRY.put(DECAY.getName(), DECAY);
		REGISTRY.put(GREATER_DECAY.getName(), GREATER_DECAY);
		REGISTRY.put(PERISHING_DECAY.getName(), PERISHING_DECAY);
	}
}
