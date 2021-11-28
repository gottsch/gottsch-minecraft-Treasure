/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.someguyssoftware.treasure2.charm;

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
	public static final ICharm LIFE_STRIKE_20;

	public static final ICharm REFLECTION_1;
	public static final ICharm REFLECTION_2;
	public static final ICharm REFLECTION_3;
	public static final ICharm REFLECTION_4;
	public static final ICharm REFLECTION_5;
	public static final ICharm REFLECTION_10;
	public static final ICharm REFLECTION_20;

	public static final ICharm DRAIN_1;
	public static final ICharm DRAIN_2;
	public static final ICharm DRAIN_3;
	public static final ICharm DRAIN_4;
	public static final ICharm DRAIN_5;
	public static final ICharm DRAIN_20;

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
	// [] CACTUS
	// [] DIRT (mound)


	static {
		LESSER_HEALING = new HealingCharm.Builder("lesser_healing", 1).with($ -> {
			$.value = 20.0;
			$.effectStackable = true;
		}).build();

		HEALING = new HealingCharm.Builder("healing", 2).with($ -> {
			$.value = 50.0;
			$.effectStackable = true;
		}).build();

		GREATER_HEALING = new HealingCharm.Builder("greater_healing", 3).with($ -> {
			$.value = 100.0;
			$.effectStackable = true;
		}).build();
				
		GRAND_HEALING = new HealingCharm.Builder("grand_healing", 4).with($ -> {
			$.value = 200.0;
			$.effectStackable = true;
		}).build();
				
		SALANDAARS_CONVALESCENCE = new HealingCharm.Builder("salandaars_convalescence", 5).with($ -> {
			$.value = 300.0;
			$.effectStackable = true;
		}).build();
				
		WHALE_HEALING = new HealingCharm.Builder("whale_healing", 99).with($ -> {
			$.value = 500.0;
			$.effectStackable = true;
		}).build();
				
		DURABLE_SHIELDING = new ShieldingCharm.Builder( "durable_shielding", 1).with($ -> {
			$.value = 20.0;
			$.percent = 0.5;
			$.effectStackable = true;
		}).build();
				
		STOUT_SHIELDING = new ShieldingCharm.Builder( "stout_shielding", 2).with($ -> {
			$.value = 50.0;
			$.percent = 0.6;
			$.effectStackable = true;
		}).build();

		HARDENED_SHIELDING = new ShieldingCharm.Builder( "hardened_shielding", 3).with($ -> {
			$.value = 100.0;
			$.percent = 0.7;
			$.effectStackable = true;
		}).build();
		
		POWERFUL_SHIELDING = new ShieldingCharm.Builder( "powerful_shielding", 4).with($ -> {
			$.value = 200.0;
			$.percent = 0.5;
			$.effectStackable = true;
		}).build();

		ARMADILLO_SHIELDING = new ShieldingCharm.Builder( "armadillo_shielding", 5).with($ -> {
			$.value = 300.0;
			$.percent = 0.8;
			$.effectStackable = true;
		}).build();

		SHIELDING_13 = new ShieldingCharm.Builder( "shielding_13", 13).with($ -> {
			$.value = 1000.0;
			$.percent = 0.8;
			$.effectStackable = true;
		}).build();

		// 10 Units @ 100% shielding
		ABSOLUTE_SHIELDING_1 = new ShieldingCharm.Builder( "absolute_shielding_1", 1).with($ -> {
			$.value = 10.0;
			$.percent = 1.0;
			$.effectStackable = false;
		}).build();

		ABSOLUTE_SHIELDING_2 = new ShieldingCharm.Builder( "absolute_shielding_2", 2).with($ -> {
			$.value = 20.0;
			$.percent = 1.0;
			$.effectStackable = false;
		}).build();
		
		ABSOLUTE_SHIELDING_3 = new ShieldingCharm.Builder( "absolute_shielding_3", 3).with($ -> {
			$.value = 30.0;
			$.percent = 1.0;
			$.effectStackable = false;
		}).build();

		ABSOLUTE_SHIELDING_4 = new ShieldingCharm.Builder( "absolute_shielding_4", 4).with($ -> {
			$.value = 40.0;
			$.percent = 1.0;
			$.effectStackable = false;
		}).build();

		ABSOLUTE_SHIELDING_13 = new ShieldingCharm.Builder( "absolute_shielding_13", 13).with($ -> {
			$.value = 250.0;
			$.percent = 1.0;
			$.effectStackable = false;
		}).build();

		FULLNESS = new FullnessCharm.Builder( "fullness", 1).with($ -> {
			$.value = 20.0;
			$.effectStackable = true;
		}).build();

		SATED_FULLNESS = new FullnessCharm.Builder( "sated_fullness", 2).with($ -> {
			$.value = 50.0;
			$.effectStackable = true;
		}).build();
	
		OVERFED_FULLNESS = new FullnessCharm.Builder( "overfed_fullness", 3).with($ -> {
			$.value = 100.0;
			$.effectStackable = true;
		}).build();

		GORGED_FULLNESS = new FullnessCharm.Builder( "gorged_fullness", 4).with($ -> {
			$.value = 200.0;
			$.effectStackable = true;
		}).build();

		BURSTING_FULLNESS = new FullnessCharm.Builder( "bursting_fullness", 5).with($ -> {
			$.value = 300.0;
			$.effectStackable = true;
		}).build();

		MINOR_HARVESTING = new HarvestingCharm.Builder( "minor_harvesting", 1).with($ -> {
			$.value = 20.0;
			$.percent = 2.0;
			$.effectStackable = false;
		}).build();

		HARVESTING = new HarvestingCharm.Builder( "harvesting", 2).with($ -> {
			$.value = 30.0;
			$.percent = 3.0;
			$.effectStackable = false;
		}).build();

		MAJOR_HARVESTING = new HarvestingCharm.Builder( "major_harvesting", 3).with($ -> {
			$.value = 40.0;
			$.percent = 4.0;
			$.effectStackable = false;
		}).build();

		GRAND_HARVESTING = new HarvestingCharm.Builder( "grand_harvesting", 4).with($ -> {
			$.value = 50.0;
			$.percent = 5.0;
			$.effectStackable = false;
		}).build();

		GLORIOUS_HARVESTING = new HarvestingCharm.Builder( "glorious_harvesting", 5).with($ -> {
			$.value = 75.0;
			$.percent = 5.0;
			$.effectStackable = false;
		}).build();

		LESSER_ILLUMINATION = new IlluminationCharm.Builder("lesser_illumination", 1).with($ -> {
			$.value = 3.0;
			$.effectStackable = false;
		}).build();

		ILLUMINATION = new IlluminationCharm.Builder("illumination", 2).with($ -> {
			$.value = 6.0;
			$.effectStackable = false;
		}).build();

		GREATER_ILLUMINATION = new IlluminationCharm.Builder("greater_illumination", 3).with($ -> {
			$.value = 12.0;
			$.effectStackable = false;
		}).build();

		GRAND_ILLUMINATION = new IlluminationCharm.Builder("grand_illumination", 4).with($ -> {
			$.value = 20.0;
			$.effectStackable = false;
		}).build();

		GLORIOUS_ILLUMINATION = new IlluminationCharm.Builder("glorious_illumination", 5).with($ -> {
			$.value = 30.0;
			$.effectStackable = false;
		}).build();

		// fire resistence: value = # of total durability/damage points to resist
		FIRE_RESISTENCE_1 = new FireResistenceCharm.Builder("fire_resistence_1", 1).with($ -> {
			$.value = 25.0;
			$.percent = 0.3;
			$.effectStackable = true;
		}).build();

		FIRE_RESISTENCE_2 = new FireResistenceCharm.Builder("fire_resistence_2", 2).with($ -> {
			$.value = 50.0;
			$.percent = 0.3;
			$.effectStackable = true;
		}).build();

		FIRE_RESISTENCE_3 = new FireResistenceCharm.Builder("fire_resistence_3", 3).with($ -> {
			$.value = 75.0;
			$.percent = 0.5;
			$.effectStackable = true;
		}).build();

		FIRE_RESISTENCE_4 = new FireResistenceCharm.Builder("fire_resistence_4", 4).with($ -> {
			$.value = 100.0;
			$.percent = 0.5;
			$.effectStackable = true;
		}).build();

		FIRE_RESISTENCE_10 = new FireResistenceCharm.Builder("fire_resistence_10", 10).with($ -> {
			$.value = 500.0;
			$.percent = 0.8;
			$.effectStackable = true;
		}).build();

		FIRE_RESISTENCE_13 = new FireResistenceCharm.Builder("fire_resistence_13", 13).with($ -> {
			$.value = 1000.0;
			$.percent = 0.8;
			$.effectStackable = true;
		}).build();

		FIRE_IMMUNITY_1 = new FireImmunityCharm.Builder("fire_immunity_1", 1).with($ -> {
			$.value = 25.0;
			$.effectStackable = false;
		}).build();

		FIRE_IMMUNITY_2 = 
				new FireImmunityCharm.Builder("fire_immunity_2", 2).with($ -> {
					$.value = 50.0;
					$.effectStackable = false;
				}).build();

		FIRE_IMMUNITY_3 = new FireImmunityCharm.Builder("fire_immunity_3", 3).with($ -> {
			$.value = 75.0;
			$.effectStackable = false;
		}).build();

		FIRE_IMMUNITY_4 = new FireImmunityCharm.Builder("fire_immunity_4", 4).with($ -> {
			$.value = 100.0;
			$.effectStackable = false;
		}).build();

		FIRE_IMMUNITY_13 = new FireImmunityCharm.Builder("fire_immunity_13", 13).with($ -> {
			$.value = 1000.0;
			$.effectStackable = false;
		}).build();

		LIFE_STRIKE_1 = new LifeStrikeCharm.Builder("life_strike_1", 1).with($ -> {
			$.value = 20.0;
			$.percent = 1.2;
			$.effectStackable = false;
		}).build();

		LIFE_STRIKE_2 = 
				new LifeStrikeCharm.Builder("life_strike_2", 2).with($ -> {
					$.value = 30.0;
					$.percent = 1.3;
					$.effectStackable = false;
				}).build();

		LIFE_STRIKE_3 = new LifeStrikeCharm.Builder("life_strike_3", 3).with($ -> {
			$.value = 40.0;
			$.percent = 1.4;
			$.effectStackable = false;
		}).build();

		LIFE_STRIKE_4 = new LifeStrikeCharm.Builder("life_strike_4", 4).with($ -> {
			$.value = 50.0;
			$.percent = 1.5;
			$.effectStackable = false;
		}).build();

		LIFE_STRIKE_10 = new LifeStrikeCharm.Builder("life_strike_10", 10).with($ -> {
			$.value = 50.0;
			$.percent = 2.0;
			$.effectStackable = false;
		}).build();
		
		LIFE_STRIKE_20 = new LifeStrikeCharm.Builder("life_strike_20", 20).with($ -> {
			$.value = 150.0;
			$.percent = 3.0;
			$.effectStackable = false;
		}).build();

		// reflection: value = # of uses, duration = range?, percent = % of damage
		// reflected
		REFLECTION_1 = new ReflectionCharm.Builder("reflection_1", 1).with($ -> {
			$.value = 20.0;
			$.percent = 0.2;
			$.duration = 2.0;
			$.effectStackable = true;
		}).build();

		REFLECTION_2 = new ReflectionCharm.Builder("reflection_2", 2).with($ -> {
			$.value = 30.0;
			$.percent = 0.25;
			$.duration = 2.0;
			$.effectStackable = true;
		}).build();

		REFLECTION_3 = new ReflectionCharm.Builder("reflection_3", 3).with($ -> {
			$.value = 40.0;
			$.percent = 0.3;
			$.duration = 3.0;
			$.effectStackable = true;
		}).build();

		REFLECTION_4 = new ReflectionCharm.Builder("reflection_4", 4).with($ -> {
			$.value = 50.0;
			$.percent = 0.35;
			$.duration = 3.0;
			$.effectStackable = true;
		}).build();

		REFLECTION_5 = new ReflectionCharm.Builder("reflection_5", 5).with($ -> {
			$.value = 50.0;
			$.percent = 0.4;
			$.duration = 3.0;
			$.effectStackable = true;
		}).build();

		REFLECTION_10 = new ReflectionCharm.Builder("reflection_10", 10).with($ -> {
			$.value = 80.0;
			$.percent = 0.65;
			$.duration = 5.0;
			$.effectStackable = true;
		}).build();
		
		REFLECTION_20 = new ReflectionCharm.Builder("reflection_20", 20).with($ -> {
			$.value = 200.0;
			$.percent = 2.00;
			$.duration = 10.0;
			$.effectStackable = true;
		}).build();

		// drains 1 health; value = uses, duration = range
		DRAIN_1 = new DrainCharm.Builder("drain_1", 1).with($ -> {
			$.value = 20.0;
			$.duration = 2.5;
			$.effectStackable = true;
		}).build();

		DRAIN_2 = new DrainCharm.Builder("drain_2", 2).with($ -> {
			$.value = 30.0;
			$.duration = 2.5;
			$.effectStackable = true;
		}).build();

		DRAIN_3 = new DrainCharm.Builder("drain_3", 3).with($ -> {
			$.value = 40.0;
			$.duration = 3.0;
			$.effectStackable = true;
		}).build();

		DRAIN_4 = new DrainCharm.Builder("drain_4", 4).with($ -> {
			$.value = 50.0;
			$.duration = 3.0;
			$.effectStackable = true;
		}).build();

		DRAIN_5 = new DrainCharm.Builder("drain_5", 5).with($ -> {
			$.value = 60.0;
			$.duration = 3.5;
			$.effectStackable = true;
		}).build();
		
		DRAIN_20 = new DrainCharm.Builder("drain_20", 20).with($ -> {
			$.value = 2000.0;
			$.duration = 10.0;
			$.effectStackable = true;
		}).build();

		// curses
		LESSER_DECAY = new DecayCharm.Builder("lesser_decay", 1).with($ -> {
					$.value = 20.0;
					$.effectStackable = true;
				}).build();

		DECAY = new DecayCharm.Builder("decay", 2).with($ -> {
			$.value = 50.0;
			$.effectStackable = true;
		}).build();

		GREATER_DECAY = new DecayCharm.Builder("greater_decay", 3).with($ -> {
			$.value = 100.0;
			$.effectStackable = true;
		}).build();

		PERISHING_DECAY = new DecayCharm.Builder("perishing_decay", 4).with($ -> {
			$.value = 200.0;
			$.effectStackable = true;
		}).build();

		// ruin: value = # of total durability/damage points to distribute, duration =
		// rate: 1 point per x seconds
		RUIN_1 = new RuinCharm.Builder("ruin_1", 1).with($ -> {
			$.value = 20.0;
			$.duration = 20.0;
			$.effectStackable = true;
		}).build();

		RUIN_2 = new RuinCharm.Builder("ruin_2", 2).with($ -> {
			$.value = 50.0;
			$.duration = 18.0;
			$.effectStackable = true;
		}).build();

		RUIN_3 = new RuinCharm.Builder("ruin_3", 3).with($ -> {
			$.value = 100.0;
			$.duration = 15.0;
			$.effectStackable = true;
		}).build();

		RUIN_4 = new RuinCharm.Builder("ruin_4", 4).with($ -> {
			$.value = 200.0;
			$.duration = 10.0;
			$.effectStackable = true;
		}).build();

		DECREPIT_1 = new DecrepitCharm.Builder("decrepit_1", 1).with($ -> {
					$.value = 20.0;
					$.percent = 1.10;
					$.duration = 20.0;
					$.effectStackable = true;
				}).build();

		DECREPIT_2 = new DecrepitCharm.Builder("decrepit_2", 2).with($ -> {
			$.value = 30.0;
			$.percent = 1.10;
			$.duration = 18.0;
			$.effectStackable = true;
		}).build();

		DECREPIT_3 = new DecrepitCharm.Builder("decrepit_3", 3).with($ -> {
			$.value = 40.0;
			$.percent = 1.20;
			$.duration = 15.0;
			$.effectStackable = true;
		}).build();

		DECREPIT_4 = new DecrepitCharm.Builder("decrepit_4", 4).with($ -> {
			$.value = 50.0;
			$.percent = 1.20;
			$.duration = 10.0;
			$.effectStackable = true;
		}).build();

		DIRT_FILL = new DirtFillCharm.Builder("dirt_fill_1", 1).with($ -> {
					$.value = 100.0;
					$.effectStackable = true;
				}).build();

		DIRT_FILL_2 = new DirtFillCharm.Builder("dirt_fill_2", 2).with($ -> {
			$.value = 150.0;
			$.effectStackable = true;
		}).build();

		DIRT_WALK = new DirtWalkCharm.Builder("dirt_walk_1", 1).with($ -> {
			$.value = 100.0;
			$.effectStackable = false;
		}).build();

		DIRT_WALK_2 = new DirtWalkCharm.Builder("dirt_walk_2", 2).with($ -> {
			$.value = 150.0;
			$.effectStackable = false;
		}).build();

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
		TreasureCharmRegistry.register(LIFE_STRIKE_20);

		TreasureCharmRegistry.register(REFLECTION_1);
		TreasureCharmRegistry.register(REFLECTION_2);
		TreasureCharmRegistry.register(REFLECTION_3);
		TreasureCharmRegistry.register(REFLECTION_4);
		TreasureCharmRegistry.register(REFLECTION_5);
		TreasureCharmRegistry.register(REFLECTION_10);
		TreasureCharmRegistry.register(REFLECTION_20);

		TreasureCharmRegistry.register(DRAIN_1);
		TreasureCharmRegistry.register(DRAIN_2);
		TreasureCharmRegistry.register(DRAIN_3);
		TreasureCharmRegistry.register(DRAIN_4);
		TreasureCharmRegistry.register(DRAIN_5);
		TreasureCharmRegistry.register(DRAIN_20);

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
