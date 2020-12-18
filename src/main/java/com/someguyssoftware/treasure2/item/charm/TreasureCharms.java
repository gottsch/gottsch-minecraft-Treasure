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
	public static final ICharm BURSTING_FULLNESS;
	
	public static final ICharm MINOR_HARVESTING;
	public static final ICharm HARVESTING;
	public static final ICharm MAJOR_HARVESTING;
	public static final ICharm GRAND_HARVESTING;
	public static final ICharm GLORIOUS_HARVESTING;
	
	public static final ICharm LESSER_ILLUMINATION;
	public static final ICharm ILLUMINATION;
	public static final ICharm GREATER_ILLUMINATION;
	public static final ICharm GRAND_ILLUMINATION;
	public static final ICharm GLORIOUS_ILLUMINATION;
	
	// curses
	public static final ICharm LESSER_DECAY;
	public static final ICharm DECAY;
	public static final ICharm GREATER_DECAY;
	public static final ICharm PERISHING_DECAY;
	
	public static final Map<String, ICharm> REGISTRY = new HashMap<>();
	
	static {
		/*
		 * NOTE don't forget to update the CharmBuilder to create the correct concrete Charm class.
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
		ARMADILLO_SHIELDING = new CharmBuilder("armadillo_shielding", CharmType.SHIELDING, CharmLevel.LEVEL4).valueModifier(1.5).build();
		// TODO add absolute Shielding Charms with lower amounts
		// 10 Units @ 100% shielding
		SMALL_ABSOLUTE_SHIELDING = new CharmBuilder("small_absolute_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).valueModifier(0.5).percentModifier(2.0).build();
		MEDIUM_ABSOLUTE_SHIELDING = new CharmBuilder("medium_absolute_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).percentModifier(2.0).build();
		LARGE_ABSOLUTE_SHIELDING = new CharmBuilder("large_absolute_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).valueModifier(1.5).percentModifier(2.0).build();
		GRAND_ABSOLUTE_SHEILDING = new CharmBuilder("grand_absolute_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).valueModifier(2).percentModifier(2.0).build();
		WHALE_ABSOLUTE_SHIELDING = new CharmBuilder("whale_absolute_shielding", CharmType.SHIELDING, CharmLevel.LEVEL1).valueModifier(5).percentModifier(2.0).build();
		
		FULLNESS = new CharmBuilder("fullness", CharmType.FULLNESS, CharmLevel.LEVEL1).build();
		SATED_FULLNESS = new CharmBuilder("sated_fullness", CharmType.FULLNESS, CharmLevel.LEVEL2).build(); 
		OVERFED_FULLNESS = new CharmBuilder("overfed_fullness", CharmType.FULLNESS, CharmLevel.LEVEL3).build(); 
		GORGED_FULLNESS = new CharmBuilder("gorged_fullness", CharmType.FULLNESS, CharmLevel.LEVEL4).build(); 
		BURSTING_FULLNESS = new CharmBuilder("bursting_fullness", CharmType.FULLNESS, CharmLevel.LEVEL4).valueModifier(1.5).build(); 
		
		MINOR_HARVESTING = new CharmBuilder("minor_harvesting", CharmType.HARVESTING, CharmLevel.LEVEL1).build();
		HARVESTING = new CharmBuilder("harvesting", CharmType.HARVESTING, CharmLevel.LEVEL2).build();
		MAJOR_HARVESTING = new CharmBuilder("major_harvesting", CharmType.HARVESTING, CharmLevel.LEVEL3).build();
		GRAND_HARVESTING = new CharmBuilder("grand_harvesting", CharmType.HARVESTING, CharmLevel.LEVEL4).build();
		GLORIOUS_HARVESTING = new CharmBuilder("glorious_harvesting", CharmType.HARVESTING, CharmLevel.LEVEL4).valueModifier(1.5).build();
		
		LESSER_ILLUMINATION = new CharmBuilder("lesser_illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL1).build();
		ILLUMINATION = new CharmBuilder("illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL2).build();
		GREATER_ILLUMINATION = new CharmBuilder("greater_illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL3).build();
		GRAND_ILLUMINATION = new CharmBuilder("grand_illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL4).build();
		GLORIOUS_ILLUMINATION = new CharmBuilder("glorious_illumination", CharmType.ILLUMINATION, CharmLevel.LEVEL4).valueModifier(1.5).build();
		
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
		
		REGISTRY.put(SMALL_ABSOLUTED_SHIELDING.getName(), SMALL_ABSOLUTE_SHIELDING);
		REGISTRY.put(MEDIUM_ABSOLUTED_SHIELDING.getName(), MEDIUM_ABSOLUTE_SHIELDING);
		REGISTRY.put(LARGE_ABSOLUTED_SHIELDING.getName(), LARGE_ABSOLUTE_SHIELDING);
		REGISTRY.put(GRAND_ABSOLUTED_SHIELDING.getName(), GRAND_ABSOLUTE_SHIELDING);
		REGISTRY.put(WHALE_ABSOLUTED_SHIELDING.getName(), WHALE_ABSOLUTE_SHIELDING);
		
		REGISTRY.put(FULLNESS.getName(), FULLNESS);
		REGISTRY.put(SATED_FULLNESS.getName(), SATED_FULLNESS);
		REGISTRY.put(OVERFED_FULLNESS.getName(), OVERFED_FULLNESS);
		REGISTRY.put(GORGED_FULLNESS.getName(), GORGED_FULLNESS);
		REGISTRY.put(BURSTING_FULLNESS.getName(), BURSTING_FULLNESS);
		
		REGISTRY.put(MINOR_HARVESTING.getName(), MINOR_HARVESTING);
		REGISTRY.put(HARVESTING.getName(), HARVESTING);
		REGISTRY.put(MAJOR_HARVESTING.getName(), MAJOR_HARVESTING);
		REGISTRY.put(GRAND_HARVESTING.getName(), GRAND_HARVESTING);
		REGISTRY.put(GLORIOUS_HARVESTING.getName(), GLORIOUS_HARVESTING);
		
		REGISTRY.put(LESSER_ILLUMINATION.getName(), LESSER_ILLUMINATION);
		REGISTRY.put(ILLUMINATION.getName(), ILLUMINATION);
		REGISTRY.put(GREATER_ILLUMINATION.getName(), GREATER_ILLUMINATION);
		REGISTRY.put(GRAND_ILLUMINATION.getName(), GRAND_ILLUMINATION);
		REGISTRY.put(GLORIOUS_ILLUMINATION.getName(), GLORIOUS_ILLUMINATION);
		
		REGISTRY.put(LESSER_DECAY.getName(), LESSER_DECAY);
		REGISTRY.put(DECAY.getName(), DECAY);
		REGISTRY.put(GREATER_DECAY.getName(), GREATER_DECAY);
		REGISTRY.put(PERISHING_DECAY.getName(), PERISHING_DECAY);
	}
}
