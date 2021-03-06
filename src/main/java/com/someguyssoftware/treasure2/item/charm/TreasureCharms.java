/**
 * 
 */
package com.someguyssoftware.treasure2.item.charm;

import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Apr 25, 2020
 *
 */
@SuppressWarnings("deprecation")
public class TreasureCharms {

	public static final ICharm LESSER_HEALING;
	public static final ICharm HEALING;
	public static final ICharm GREATER_HEALING;
	public static final ICharm GRAND_HEALING;
	public static final ICharm SALANDAARS_CONVALESCENCE;
	public static final ICharm WHALE_HEALING;

	public static final ICharm DURABLE_SHIELDING;
	public static final ICharm STOUT_SHIELDING;
	public static final ICharm HARDENED_SHIELDING;
	public static final ICharm POWERFUL_SHIELDING;
    public static final ICharm ARMADILLO_SHIELDING;
    public static final ICharm SHIELDING_13;

	public static final ICharm ABSOLUTE_SHIELDING_1;
	public static final ICharm ABSOLUTE_SHIELDING_2;
	public static final ICharm ABSOLUTE_SHIELDING_3;
	public static final ICharm ABSOLUTE_SHIELDING_4;
	public static final ICharm ABSOLUTE_SHIELDING_13;

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

	public static final ICharm FIRE_RESISTENCE_1;
	public static final ICharm FIRE_RESISTENCE_2;
	public static final ICharm FIRE_RESISTENCE_3;
	public static final ICharm FIRE_RESISTENCE_4;
	public static final ICharm FIRE_RESISTENCE_10;
	public static final ICharm FIRE_RESISTENCE_13;
    
    public static final ICharm FIRE_IMMUNITY_1;
    public static final ICharm FIRE_IMMUNITY_2;
    public static final ICharm FIRE_IMMUNITY_3;
    public static final ICharm FIRE_IMMUNITY_4;
    public static final ICharm FIRE_IMMUNITY_13;
    
    public static final ICharm LIFE_STRIKE_1;
    public static final ICharm LIFE_STRIKE_2;
    public static final ICharm LIFE_STRIKE_3;
    public static final ICharm LIFE_STRIKE_4;
    public static final ICharm LIFE_STRIKE_10;
    
    public static final ICharm REFLECTION_1;
    public static final ICharm REFLECTION_2;
    public static final ICharm REFLECTION_3;
    public static final ICharm REFLECTION_4;
    public static final ICharm REFLECTION_5;
    public static final ICharm REFLECTION_10;
    
    public static final ICharm DRAIN_1;
    public static final ICharm DRAIN_2;
    public static final ICharm DRAIN_3;
    public static final ICharm DRAIN_4;
    public static final ICharm DRAIN_5;
    
	// curses
	public static final ICharm LESSER_DECAY;
	public static final ICharm DECAY;
	public static final ICharm GREATER_DECAY;
    public static final ICharm PERISHING_DECAY;

    public static final ICharm RUIN_1;
    public static final ICharm RUIN_2;
    public static final ICharm RUIN_3;
    public static final ICharm RUIN_4;
    
    public static final ICharm DECREPIT_1;
    public static final ICharm DECREPIT_2;
    public static final ICharm DECREPIT_3;
    public static final ICharm DECREPIT_4;
        
    public static final ICharm DIRT_FILL;
    public static final ICharm DIRT_FILL_2;

    public static final ICharm DIRT_WALK;
    public static final ICharm DIRT_WALK_2;

    // TODO add charms
    // [x] RUIN
    // [] CACTUS
    // [x] REFLECTION x%, y# of enemies, z blocks of range
    // [x] DRAIN drains 1 damage from each mob, z blocks of range
    // [x] DECREPIT (curse get hit for 2x the damage) 
    // [] DIRT (mound)
    // [x] DIRT FILL (inventory filler)
    // [x] DIRT WALK (space below turns to dirt)
    // [x] FIRE RESISTENCE
    // [x] FIRE IMMUNITY
    // [x] LIFESTRIKE

	//    @Deprecated
	//	public static final Map<String, ICharm> REGISTRY = new HashMap<>();

	static {
		LESSER_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_healing"), CharmType.HEALING.getName(), 1, HealingCharm.class).withValue(20.0).withAllowMultipleUpdates(true).build();
		HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "healing"), CharmType.HEALING.getName(), 2, HealingCharm.class).withValue(50.0).withAllowMultipleUpdates(true).build();
		GREATER_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_healing"), CharmType.HEALING.getName(), 3, HealingCharm.class).withValue(100.0).withAllowMultipleUpdates(true).build();
		GRAND_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_healing"), CharmType.HEALING.getName(), 4, HealingCharm.class).withValue(200.0).withAllowMultipleUpdates(true).build();
		SALANDAARS_CONVALESCENCE = new Charm.Builder(new ResourceLocation(Treasure.MODID, "salandaars_convalescence"), CharmType.HEALING.getName(), 5, HealingCharm.class).withValue(300.0).withAllowMultipleUpdates(true).build();
		WHALE_HEALING = new Charm.Builder(
				new ResourceLocation(Treasure.MODID, "whale_healing"), CharmType.HEALING.getName(), 99, HealingCharm.class).withValue(500.0).build();

		DURABLE_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "durable_shielding"), CharmType.SHIELDING.getName(), 1, ShieldingCharm.class).withValue(20.0).withPercent(0.5).withAllowMultipleUpdates(true).build();
		STOUT_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "stout_shielding"), CharmType.SHIELDING.getName(), 2, ShieldingCharm.class).withValue(50.0).withPercent(0.6).withAllowMultipleUpdates(true).build();
		HARDENED_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "hardened_shielding"), CharmType.SHIELDING.getName(), 3, ShieldingCharm.class).withValue(100.0).withPercent(0.7).withAllowMultipleUpdates(true).build();
		POWERFUL_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "powerful_shielding"), CharmType.SHIELDING.getName(), 4, ShieldingCharm.class).withValue(200.0).withPercent(0.8).withAllowMultipleUpdates(true).build();
		ARMADILLO_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "armadillo_shielding"), CharmType.SHIELDING.getName(), 5, ShieldingCharm.class).withValue(300.0).withPercent(0.8).withAllowMultipleUpdates(true).build();
        SHIELDING_13 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "shielding_13"), CharmType.SHIELDING.getName(), 13, ShieldingCharm.class).withValue(1000.0).withPercent(0.8).withAllowMultipleUpdates(true).build();

		// 10 Units @ 100% shielding
		ABSOLUTE_SHIELDING_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "absolute_shielding_1"), "absolute_shielding", 1, ShieldingCharm.class)
				.withValue(10.0).withPercent(1.0).withAllowMultipleUpdates(false).build();
		ABSOLUTE_SHIELDING_2 = new Charm.Builder(
				new ResourceLocation(Treasure.MODID, "absolute_shielding_2"), "absolute_shielding", 2, ShieldingCharm.class)
				.withValue(20.0).withPercent(1.0).withAllowMultipleUpdates(false).build();
		ABSOLUTE_SHIELDING_3 = new Charm.Builder(
				new ResourceLocation(Treasure.MODID, "absolute_shielding_3"), "absolute_shielding", 3, ShieldingCharm.class)
				.withValue(30.0).withPercent(1.0).withAllowMultipleUpdates(false).build();
		ABSOLUTE_SHIELDING_4 = new Charm.Builder(
				new ResourceLocation(Treasure.MODID, "absolute_shielding_4"), "absolute_shielding", 4, ShieldingCharm.class)
				.withValue(40.0).withPercent(1.0).withAllowMultipleUpdates(false).build();
		ABSOLUTE_SHIELDING_13 = new Charm.Builder(
				new ResourceLocation(Treasure.MODID, "absolute_shielding_13"), "absolute_shielding", 13, ShieldingCharm.class)
				.withValue(250.0).withPercent(1.0).withAllowMultipleUpdates(false).build();

        FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fullness"), CharmType.FULLNESS.getName(), 1, FullnessCharm.class)
                .withValue(20.0).withAllowMultipleUpdates(true).build();
		SATED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "sated_fullness"), CharmType.FULLNESS.getName(), 2, FullnessCharm.class).withValue(50.0).withAllowMultipleUpdates(true).build(); 
		OVERFED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "overfed_fullness"), CharmType.FULLNESS.getName(), 3, FullnessCharm.class).withValue(10.0).withAllowMultipleUpdates(true).build(); 
		GORGED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "gorged_fullness"), CharmType.FULLNESS.getName(), 4, FullnessCharm.class).withValue(200.0).withAllowMultipleUpdates(true).build(); 
		BURSTING_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "bursting_fullness"), CharmType.FULLNESS.getName(), 5, FullnessCharm.class).withValue(300.0).withAllowMultipleUpdates(true).build(); 

		MINOR_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "minor_harvesting"), CharmType.HARVESTING.getName(), 1, HarvestingCharm.class).withValue(20.0).withPercent(2.0).withAllowMultipleUpdates(false).build();
		HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "harvesting"), CharmType.HARVESTING.getName(), 2, HarvestingCharm.class).withValue(30.0).withPercent(3.0).withAllowMultipleUpdates(false).build();
		MAJOR_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "major_harvesting"), CharmType.HARVESTING.getName(), 3, HarvestingCharm.class).withValue(40.0).withPercent(4.0).withAllowMultipleUpdates(false).build();
		GRAND_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_harvesting"), CharmType.HARVESTING.getName(), 4, HarvestingCharm.class).withValue(50.0).withPercent(5.0).withAllowMultipleUpdates(false).build();
		GLORIOUS_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "glorious_harvesting"), CharmType.HARVESTING.getName(), 5, HarvestingCharm.class).withValue(75.0).withPercent(5.0).withAllowMultipleUpdates(false).build();

		LESSER_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_illumination"), CharmType.ILLUMINATION.getName(), 1, IlluminationCharm.class).withValue(3.0).withAllowMultipleUpdates(false).build();
		ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "illumination"), CharmType.ILLUMINATION.getName(), 2, IlluminationCharm.class).withValue(6.0).withAllowMultipleUpdates(false).build();
		GREATER_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_illumination"), CharmType.ILLUMINATION.getName(), 3, IlluminationCharm.class).withValue(12.0).withAllowMultipleUpdates(false).build();
		GRAND_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_illumination"), CharmType.ILLUMINATION.getName(), 4, IlluminationCharm.class).withValue(20.0).withAllowMultipleUpdates(false).build();
		GLORIOUS_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "glorious_illumination"), CharmType.ILLUMINATION.getName(), 5, IlluminationCharm.class).withValue(30.0).withAllowMultipleUpdates(false).build();

		// fire resistence: value = # of total durability/damage points to resist
		FIRE_RESISTENCE_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_1"), "fire_resistence", 1, FireResistenceCharm.class).withValue(25.0)
				.withPercent(0.3).withAllowMultipleUpdates(true).build();
		FIRE_RESISTENCE_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_2"), "fire_resistence", 2, FireResistenceCharm.class).withValue(50.0)
				.withPercent(0.3).withAllowMultipleUpdates(true).build();
		FIRE_RESISTENCE_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_3"), "fire_resistence", 3, FireResistenceCharm.class).withValue(75.0)
				.withPercent(0.5).withAllowMultipleUpdates(true).build();
		FIRE_RESISTENCE_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_4"), "fire_resistence", 4, FireResistenceCharm.class).withValue(100.0)
				.withPercent(0.5).withAllowMultipleUpdates(true).build();
		FIRE_RESISTENCE_10 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_10"), "fire_resistence", 10, FireResistenceCharm.class).withValue(500.0)
				.withPercent(0.8).withAllowMultipleUpdates(true).build();
		FIRE_RESISTENCE_13 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_resistence_13"), "fire_resistence", 13, FireResistenceCharm.class).withValue(1000.0)
				.withPercent(0.8).withAllowMultipleUpdates(true).build();
        
        FIRE_IMMUNITY_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_immunity_1"), "fire_immunity", 1, FireImmunityCharm.class).withValue(25.0)
                .withAllowMultipleUpdates(false).build();
        FIRE_IMMUNITY_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_immunity_2"), "fire_immunity", 2, FireImmunityCharm.class).withValue(50.0)
                .withAllowMultipleUpdates(false).build();
        FIRE_IMMUNITY_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_immunity_3"), "fire_immunity", 3, FireImmunityCharm.class).withValue(75.0)
                .withAllowMultipleUpdates(false).build();
        FIRE_IMMUNITY_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_immunity_4"), "fire_immunity", 4, FireImmunityCharm.class).withValue(100.0)
                .withAllowMultipleUpdates(false).build();
        FIRE_IMMUNITY_13 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fire_immunity_13"), "fire_immunity", 13, FireImmunityCharm.class).withValue(1000.0)
                .withAllowMultipleUpdates(false).build();

        LIFE_STRIKE_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "life_strike_1"), "life_strike", 1, LifeStrikeCharm.class).withValue(20.0).withPercent(1.20)
                .withAllowMultipleUpdates(false).build();
        LIFE_STRIKE_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "life_strike_2"), "life_strike", 2, LifeStrikeCharm.class).withValue(30.0).withPercent(1.30)
                .withAllowMultipleUpdates(false).build();
        LIFE_STRIKE_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "life_strike_3"), "life_strike", 3, LifeStrikeCharm.class).withValue(40.0).withPercent(1.40)
                .withAllowMultipleUpdates(false).build();
        LIFE_STRIKE_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "life_strike_4"), "life_strike", 4, LifeStrikeCharm.class).withValue(50.0).withPercent(1.50)
                .withAllowMultipleUpdates(false).build();        
        LIFE_STRIKE_10 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "life_strike_10"), "life_strike", 10, LifeStrikeCharm.class).withValue(50.0).withPercent(2.0)
                .withAllowMultipleUpdates(false).build();
        
     // reflection: value = # of uses, duration = range?, percent = % of damage reflected
        REFLECTION_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_1"), "reflection", 1, ReflectionCharm.class).withValue(20.0).withPercent(0.20)
                .withDuration(2.0).withAllowMultipleUpdates(true).build();
        REFLECTION_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_2"), "reflection", 2, ReflectionCharm.class).withValue(30.0).withPercent(0.25)
                .withDuration(2.0).withAllowMultipleUpdates(true).build();
        REFLECTION_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_3"), "reflection", 3, ReflectionCharm.class).withValue(40.0).withPercent(0.30)
                .withDuration(3.0).withAllowMultipleUpdates(true).build();
        REFLECTION_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_4"), "reflection", 4, ReflectionCharm.class).withValue(50.0).withPercent(0.35)
                .withDuration(3.0).withAllowMultipleUpdates(true).build();
        REFLECTION_5 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_5"), "reflection", 5, ReflectionCharm.class).withValue(50.0).withPercent(0.40)
                .withDuration(3.0).withAllowMultipleUpdates(true).build();
        REFLECTION_10 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "reflection_10"), "reflection", 10, ReflectionCharm.class).withValue(80.0).withPercent(0.65)
                .withDuration(5.0).withAllowMultipleUpdates(true).build();
        
        // drains 1 health; value = uses, duration = range
        DRAIN_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "drain_1"), "drain", 1, DrainCharm.class).withValue(20.0).withDuration(2.5)
                .withAllowMultipleUpdates(true).build();
        DRAIN_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "drain_2"), "drain", 2, DrainCharm.class).withValue(30.0).withDuration(2.5)
                .withAllowMultipleUpdates(true).build();
        DRAIN_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "drain_3"), "drain", 3, DrainCharm.class).withValue(40.0).withDuration(3.0)
                .withAllowMultipleUpdates(true).build();
        DRAIN_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "drain_4"), "drain", 4, DrainCharm.class).withValue(50.0).withDuration(3.0)
                .withAllowMultipleUpdates(true).build();
        DRAIN_5 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "drain_5"), "drain", 5, DrainCharm.class).withValue(60.0).withDuration(3.5)
                .withAllowMultipleUpdates(true).build();
        
        // curses
		LESSER_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_decay"), CharmType.DECAY.getName(), 1, DecayCharm.class).withValue(20.0).withAllowMultipleUpdates(true).build();
		DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decay"), CharmType.DECAY.getName(), 2, DecayCharm.class).withValue(50.0).withAllowMultipleUpdates(true).build();
		GREATER_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_decay"), CharmType.DECAY.getName(), 3, DecayCharm.class).withValue(100.0).withAllowMultipleUpdates(true).build();
		PERISHING_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "perishing_decay"), CharmType.DECAY.getName(), 4, DecayCharm.class).withValue(200.0).withAllowMultipleUpdates(true).build();

        // ruin: value = # of total durability/damage points to distribute, duration = rate: 1 point per x seconds
        RUIN_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "ruin_1"), "ruin", 1, RuinCharm.class).withValue(25.0)
                .withDuration(20.0).withAllowMultipleUpdates(true).build();
        RUIN_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "ruin_2"), "ruin", 2, RuinCharm.class).withValue(50.0)
                .withDuration(18.0).withAllowMultipleUpdates(true).build();
        RUIN_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "ruin_3"), "ruin", 3, RuinCharm.class).withValue(100.0)
                .withDuration(15.0).withAllowMultipleUpdates(true).build();
        RUIN_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "ruin_4"), "ruin", 4, RuinCharm.class).withValue(200.0)
                .withDuration(10.0).withAllowMultipleUpdates(true).build();
        
        DECREPIT_1 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decrepit_1"), "decrepit", 1, DecrepitCharm.class).withValue(20.0).withPercent(1.10)
                .withDuration(20.0).withAllowMultipleUpdates(true).build();
        DECREPIT_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decrepit_2"), "decrepit", 2, DecrepitCharm.class).withValue(30.0).withPercent(1.10)
                .withDuration(18.0).withAllowMultipleUpdates(true).build();
        DECREPIT_3 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decrepit_3"), "decrepit", 3, DecrepitCharm.class).withValue(40.0).withPercent(1.20)
                .withDuration(15.0).withAllowMultipleUpdates(true).build();
        DECREPIT_4 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decrepit_4"), "decrepit", 4, DecrepitCharm.class).withValue(50.0).withPercent(1.20)
                .withDuration(10.0).withAllowMultipleUpdates(true).build();
        
        DIRT_FILL = new Charm.Builder(new ResourceLocation(Treasure.MODID, "dirt_fill_1"), "dirt_fill", 1, DirtFillCharm.class)
                .withValue(100.0).withAllowMultipleUpdates(true).build();
        DIRT_FILL_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "dirt_fill_2"), "dirt_fill", 2, DirtFillCharm.class)
                .withValue(150.0).withAllowMultipleUpdates(true).build();

        DIRT_WALK = new Charm.Builder(new ResourceLocation(Treasure.MODID, "dirt_walk_1"), "dirt_walk", 1, DirtWalkCharm.class)
                .withValue(100.0).withAllowMultipleUpdates(false).build();
        DIRT_WALK_2 = new Charm.Builder(new ResourceLocation(Treasure.MODID, "dirt_walk_2"), "dirt_walk", 2, DirtWalkCharm.class)
                .withValue(150.0).withAllowMultipleUpdates(false).build();

		TreasureCharmRegistry.register(LESSER_HEALING);
		TreasureCharmRegistry.register(HEALING);
		TreasureCharmRegistry.register(GREATER_HEALING);
		TreasureCharmRegistry.register(GRAND_HEALING);
		TreasureCharmRegistry.register(SALANDAARS_CONVALESCENCE);
		TreasureCharmRegistry.register(WHALE_HEALING);

		TreasureCharmRegistry.register(ABSOLUTE_SHIELDING_1);
		TreasureCharmRegistry.register(ABSOLUTE_SHIELDING_2);
		TreasureCharmRegistry.register(ABSOLUTE_SHIELDING_3);
		TreasureCharmRegistry.register(ABSOLUTE_SHIELDING_4);
		TreasureCharmRegistry.register(ABSOLUTE_SHIELDING_13);

		TreasureCharmRegistry.register(DURABLE_SHIELDING);
		TreasureCharmRegistry.register(STOUT_SHIELDING);
		TreasureCharmRegistry.register(HARDENED_SHIELDING);
		TreasureCharmRegistry.register(POWERFUL_SHIELDING);
        TreasureCharmRegistry.register(ARMADILLO_SHIELDING);
        TreasureCharmRegistry.register(SHIELDING_13);

        TreasureCharmRegistry.register(FULLNESS);
		TreasureCharmRegistry.register(SATED_FULLNESS);
		TreasureCharmRegistry.register(OVERFED_FULLNESS);
		TreasureCharmRegistry.register(GORGED_FULLNESS);
		TreasureCharmRegistry.register(BURSTING_FULLNESS);

		TreasureCharmRegistry.register(MINOR_HARVESTING);
		TreasureCharmRegistry.register(HARVESTING);
		TreasureCharmRegistry.register(MAJOR_HARVESTING);
		TreasureCharmRegistry.register(GRAND_HARVESTING);
		TreasureCharmRegistry.register(GLORIOUS_HARVESTING);

		TreasureCharmRegistry.register(LESSER_ILLUMINATION);
		TreasureCharmRegistry.register(ILLUMINATION);
		TreasureCharmRegistry.register(GREATER_ILLUMINATION);
		TreasureCharmRegistry.register(GRAND_ILLUMINATION);
		TreasureCharmRegistry.register(GLORIOUS_ILLUMINATION);

		TreasureCharmRegistry.register(FIRE_RESISTENCE_1);
		TreasureCharmRegistry.register(FIRE_RESISTENCE_2);
		TreasureCharmRegistry.register(FIRE_RESISTENCE_3);
		TreasureCharmRegistry.register(FIRE_RESISTENCE_4);
		TreasureCharmRegistry.register(FIRE_RESISTENCE_10);
		TreasureCharmRegistry.register(FIRE_RESISTENCE_13);
        
        TreasureCharmRegistry.register(FIRE_IMMUNITY_1);
        TreasureCharmRegistry.register(FIRE_IMMUNITY_2);
        TreasureCharmRegistry.register(FIRE_IMMUNITY_3);
        TreasureCharmRegistry.register(FIRE_IMMUNITY_4);
        TreasureCharmRegistry.register(FIRE_IMMUNITY_13);
        
        TreasureCharmRegistry.register(LIFE_STRIKE_1);
        TreasureCharmRegistry.register(LIFE_STRIKE_2);
        TreasureCharmRegistry.register(LIFE_STRIKE_3);
        TreasureCharmRegistry.register(LIFE_STRIKE_4);
        TreasureCharmRegistry.register(LIFE_STRIKE_10);
        
        TreasureCharmRegistry.register(REFLECTION_1);
        TreasureCharmRegistry.register(REFLECTION_2);
        TreasureCharmRegistry.register(REFLECTION_3);
        TreasureCharmRegistry.register(REFLECTION_4);
        TreasureCharmRegistry.register(REFLECTION_5);
        TreasureCharmRegistry.register(REFLECTION_10);
        
        TreasureCharmRegistry.register(DRAIN_1);
        TreasureCharmRegistry.register(DRAIN_2);
        TreasureCharmRegistry.register(DRAIN_3);
        TreasureCharmRegistry.register(DRAIN_4);
        TreasureCharmRegistry.register(DRAIN_5);
        
		TreasureCharmRegistry.register(LESSER_DECAY);
		TreasureCharmRegistry.register(DECAY);
		TreasureCharmRegistry.register(GREATER_DECAY);
        TreasureCharmRegistry.register(PERISHING_DECAY);
        
        TreasureCharmRegistry.register(RUIN_1);
        TreasureCharmRegistry.register(RUIN_2);
        TreasureCharmRegistry.register(RUIN_3);
        TreasureCharmRegistry.register(RUIN_4);
        
        TreasureCharmRegistry.register(DECREPIT_1);
        TreasureCharmRegistry.register(DECREPIT_2);
        TreasureCharmRegistry.register(DECREPIT_3);
        TreasureCharmRegistry.register(DECREPIT_4);
        
        TreasureCharmRegistry.register(DIRT_FILL);
        TreasureCharmRegistry.register(DIRT_FILL_2);

        TreasureCharmRegistry.register(DIRT_WALK);
        TreasureCharmRegistry.register(DIRT_WALK_2);
    }
}
