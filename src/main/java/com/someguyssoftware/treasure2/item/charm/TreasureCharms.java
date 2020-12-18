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
//		ICharm TEST_HEAL = new Charm.Builder("test_heal", CharmType.HEALING.getName(), 1, HealingCharm.class).withValue(20.0).build();
//		ICharm TEST_SHIELD = new Charm.Builder("test_shield", "shielding", 1, ShieldingCharm.class).withValue(20.0).withDuration(0.5).build();
//		ICharm SMALL_ABSOLUTE_SHIELDING = new Charm.Builder("small_absolute_shielding", CharmType.SHIELDING.getName(), 1, ShieldingCharm.class)
//				.withValue(10.0).withPercent(100.0).build();

		LESSER_HEALING = new Charm.Builder("lesser_healing", CharmType.HEALING.getName(), 1, HealingCharm.class).withValue(20.0).build();
		HEALING = new Charm.Builder("healing", CharmType.HEALING.getName(), 2, HealingCharm.class).withValue(50.0).build();
		GREATER_HEALING = new Charm.Builder("greater_healing", CharmType.HEALING.getName(), 3, HealingCharm.class).withValue(100.0).build();
		GRAND_HEALING = new Charm.Builder("grand_healing", CharmType.HEALING.getName(), 4, HealingCharm.class).withValue(200.0).build();
		SALANDAARS_CONVALESCENCE = new Charm.Builder("salandaars_convalescence", CharmType.HEALING.getName(), 5, HealingCharm.class).withValue(300.0).build();

		DURABLE_SHIELDING = new Charm.Builder("durable_shielding", CharmType.SHIELDING.getName(), 1, ShieldingCharm.class).withValue(20.0).withPercent(0.5).build();
		STOUT_SHIELDING = new Charm.Builder("stout_shielding", CharmType.SHIELDING.getName(), 2, ShieldingCharm.class).withValue(50.0).withPercent(0.6).build();
		HARDENED_SHIELDING = new Charm.Builder("hardened_shielding", CharmType.SHIELDING.getName(), 3, ShieldingCharm.class).withValue(100.0).withPercent(0.7).build();
		POWERFUL_SHIELDING = new Charm.Builder("powerful_shielding", CharmType.SHIELDING.getName(), 4, ShieldingCharm.class).withValue(200.0).withPercent(0.8).build();
		ARMADILLO_SHIELDING = new Charm.Builder("armadillo_shielding", CharmType.SHIELDING.getName(), 5, ShieldingCharm.class).withValue(300.0).withPercent(0.8).build();
		
		// TODO add absolute Shielding Charms with lower amounts
		// 10 Units @ 100% shielding
//		SMALL_ABSOLUTE_SHIELDING = new Charm.Builder("small_absolute_shielding", CharmType.SHIELDING, 1).valueModifier(0.5).percentModifier(2.0).build();
//		MEDIUM_ABSOLUTE_SHIELDING = new Charm.Builder("medium_absolute_shielding", CharmType.SHIELDING, 1).percentModifier(2.0).build();
//		LARGE_ABSOLUTE_SHIELDING = new Charm.Builder("large_absolute_shielding", CharmType.SHIELDING, 1).valueModifier(1.5).percentModifier(2.0).build();
//		GRAND_ABSOLUTE_SHEILDING = new Charm.Builder("grand_absolute_shielding", CharmType.SHIELDING, 1).valueModifier(2).percentModifier(2.0).build();
//		WHALE_ABSOLUTE_SHIELDING = new Charm.Builder("whale_absolute_shielding", CharmType.SHIELDING, 1).valueModifier(5).percentModifier(2.0).build();
	
		FULLNESS = new Charm.Builder("fullness", CharmType.FULLNESS.getName(), 1, FullnessCharm.class).withValue(20.0).build();
		SATED_FULLNESS = new Charm.Builder("sated_fullness", CharmType.FULLNESS.getName(), 2, FullnessCharm.class).withValue(50.0).build(); 
		OVERFED_FULLNESS = new Charm.Builder("overfed_fullness", CharmType.FULLNESS.getName(), 3, FullnessCharm.class).withValue(10.0).build(); 
		GORGED_FULLNESS = new Charm.Builder("gorged_fullness", CharmType.FULLNESS.getName(), 4, FullnessCharm.class).withValue(200.0).build(); 
		BURSTING_FULLNESS = new Charm.Builder("bursting_fullness", CharmType.FULLNESS.getName(), 5, FullnessCharm.class).withValue(300.0).build(); 

		MINOR_HARVESTING = new Charm.Builder("minor_harvesting", CharmType.HARVESTING.getName(), 1, HarvestingCharm.class).withValue(20.0).withPercent(2.0).build();
		HARVESTING = new Charm.Builder("harvesting", CharmType.HARVESTING.getName(), 2, HarvestingCharm.class).withValue(30.0).withPercent(3.0).build();
		MAJOR_HARVESTING = new Charm.Builder("major_harvesting", CharmType.HARVESTING.getName(), 3, HarvestingCharm.class).withValue(40.0).withPercent(4.0).build();
		GRAND_HARVESTING = new Charm.Builder("grand_harvesting", CharmType.HARVESTING.getName(), 4, HarvestingCharm.class).withValue(50.0).withPercent(5.0).build();
		GLORIOUS_HARVESTING = new Charm.Builder("glorious_harvesting", CharmType.HARVESTING.getName(), 5, HarvestingCharm.class).withValue(75.0).withPercent(5.0).build();
		
		LESSER_ILLUMINATION = new Charm.Builder("lesser_illumination", CharmType.ILLUMINATION.getName(), 1, IlluminationCharm.class).withValue(3.0).build();
		ILLUMINATION = new Charm.Builder("illumination", CharmType.ILLUMINATION.getName(), 2, IlluminationCharm.class).withValue(6.0).build();
		GREATER_ILLUMINATION = new Charm.Builder("greater_illumination", CharmType.ILLUMINATION.getName(), 3, IlluminationCharm.class).withValue(12.0).build();
		GRAND_ILLUMINATION = new Charm.Builder("grand_illumination", CharmType.ILLUMINATION.getName(), 4, IlluminationCharm.class).withValue(20.0).build();
		GLORIOUS_ILLUMINATION = new Charm.Builder("glorious_illumination", CharmType.ILLUMINATION.getName(), 5, IlluminationCharm.class).withValue(30.0).build();
		
		// curses
		LESSER_DECAY = new Charm.Builder("lesser_decay", CharmType.DECAY.getName(), 1, DecayCharm.class).withValue(20.0).build();
		DECAY = new Charm.Builder("decay", CharmType.DECAY.getName(), 2, DecayCharm.class).withValue(50.0).build();
		GREATER_DECAY = new Charm.Builder("greater_decay", CharmType.DECAY.getName(), 3, DecayCharm.class).withValue(100.0).build();
		PERISHING_DECAY = new Charm.Builder("perishing_decay", CharmType.DECAY.getName(), 4, DecayCharm.class).withValue(200.0).build();
		
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
		
//		REGISTRY.put(SMALL_ABSOLUTED_SHIELDING.getName(), SMALL_ABSOLUTE_SHIELDING);
//		REGISTRY.put(MEDIUM_ABSOLUTED_SHIELDING.getName(), MEDIUM_ABSOLUTE_SHIELDING);
//		REGISTRY.put(LARGE_ABSOLUTED_SHIELDING.getName(), LARGE_ABSOLUTE_SHIELDING);
//		REGISTRY.put(GRAND_ABSOLUTED_SHIELDING.getName(), GRAND_ABSOLUTE_SHIELDING);
//		REGISTRY.put(WHALE_ABSOLUTED_SHIELDING.getName(), WHALE_ABSOLUTE_SHIELDING);
		
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
