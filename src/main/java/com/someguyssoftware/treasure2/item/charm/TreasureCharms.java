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
	
	public static final ICharm SMALL_ABSOLUTE_SHIELDING;
	public static final ICharm MEDIUM_ABSOLUTE_SHIELDING;
	public static final ICharm LARGE_ABSOLUTE_SHIELDING;
	public static final ICharm GRAND_ABSOLUTE_SHIELDING;
	public static final ICharm WHALE_ABSOLUTE_SHIELDING;
	
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
    
//    @Deprecated
//	public static final Map<String, ICharm> REGISTRY = new HashMap<>();
	
	static {
		LESSER_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_healing"), CharmType.HEALING.getName(), 1, HealingCharm.class).withValue(20.0).build();
		HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "healing"), CharmType.HEALING.getName(), 2, HealingCharm.class).withValue(50.0).build();
		GREATER_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_healing"), CharmType.HEALING.getName(), 3, HealingCharm.class).withValue(100.0).build();
		GRAND_HEALING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_healing"), CharmType.HEALING.getName(), 4, HealingCharm.class).withValue(200.0).build();
		SALANDAARS_CONVALESCENCE = new Charm.Builder(new ResourceLocation(Treasure.MODID, "salandaars_convalescence"), CharmType.HEALING.getName(), 5, HealingCharm.class).withValue(300.0).build();
        WHALE_HEALING = new Charm.Builder(
            new ResourceLocation(Treasure.MODID, "whale_healing"), CharmType.HEALING.getName(), 99, HealingCharm.class).withValue(500.0).build();
        
            DURABLE_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "durable_shielding"), CharmType.SHIELDING.getName(), 1, ShieldingCharm.class).withValue(20.0).withPercent(0.5).build();
		STOUT_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "stout_shielding"), CharmType.SHIELDING.getName(), 2, ShieldingCharm.class).withValue(50.0).withPercent(0.6).build();
		HARDENED_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "hardened_shielding"), CharmType.SHIELDING.getName(), 3, ShieldingCharm.class).withValue(100.0).withPercent(0.7).build();
		POWERFUL_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "powerful_shielding"), CharmType.SHIELDING.getName(), 4, ShieldingCharm.class).withValue(200.0).withPercent(0.8).build();
		ARMADILLO_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "armadillo_shielding"), CharmType.SHIELDING.getName(), 5, ShieldingCharm.class).withValue(300.0).withPercent(0.8).build();
		
		// TODO add absolute Shielding Charms with lower amounts
		// 10 Units @ 100% shielding
		SMALL_ABSOLUTE_SHIELDING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "small_absolute_shielding"), CharmType.SHIELDING.getName(), 1, ShieldingCharm.class)
            .withValue(10.0).withPercent(100.0).build();
		MEDIUM_ABSOLUTE_SHIELDING = new Charm.Builder(
            new ResourceLocation(Treasure.MODID, "medium_absolute_shielding"), CharmType.SHIELDING.getName(), 2, ShieldingCharm.class)
            .withValue(20.0).withPercent(100.0).build();
		LARGE_ABSOLUTE_SHIELDING = new Charm.Builder(
            new ResourceLocation(Treasure.MODID, "large_absolute_shielding"), CharmType.SHIELDING.getName(), 3, ShieldingCharm.class)
            .withValue(30.0).withPercent(100.0).build();
		GRAND_ABSOLUTE_SHIELDING = new Charm.Builder(
            new ResourceLocation(Treasure.MODID, "grand_absolute_shielding"), CharmType.SHIELDING.getName(), 4, ShieldingCharm.class)
            .withValue(40.0).withPercent(100.0).build();
		WHALE_ABSOLUTE_SHIELDING = new Charm.Builder(
            new ResourceLocation(Treasure.MODID, "whale_absolute_shielding"), CharmType.SHIELDING.getName(), 99, ShieldingCharm.class)
            .withValue(100.0).withPercent(100.0).build();
	
		FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "fullness"), CharmType.FULLNESS.getName(), 1, FullnessCharm.class).withValue(20.0).build();
		SATED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "sated_fullness"), CharmType.FULLNESS.getName(), 2, FullnessCharm.class).withValue(50.0).build(); 
		OVERFED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "overfed_fullness"), CharmType.FULLNESS.getName(), 3, FullnessCharm.class).withValue(10.0).build(); 
		GORGED_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "gorged_fullness"), CharmType.FULLNESS.getName(), 4, FullnessCharm.class).withValue(200.0).build(); 
		BURSTING_FULLNESS = new Charm.Builder(new ResourceLocation(Treasure.MODID, "bursting_fullness"), CharmType.FULLNESS.getName(), 5, FullnessCharm.class).withValue(300.0).build(); 

		MINOR_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "minor_harvesting"), CharmType.HARVESTING.getName(), 1, HarvestingCharm.class).withValue(20.0).withPercent(2.0).build();
		HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "harvesting"), CharmType.HARVESTING.getName(), 2, HarvestingCharm.class).withValue(30.0).withPercent(3.0).build();
		MAJOR_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "major_harvesting"), CharmType.HARVESTING.getName(), 3, HarvestingCharm.class).withValue(40.0).withPercent(4.0).build();
		GRAND_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_harvesting"), CharmType.HARVESTING.getName(), 4, HarvestingCharm.class).withValue(50.0).withPercent(5.0).build();
		GLORIOUS_HARVESTING = new Charm.Builder(new ResourceLocation(Treasure.MODID, "glorious_harvesting"), CharmType.HARVESTING.getName(), 5, HarvestingCharm.class).withValue(75.0).withPercent(5.0).build();
		
		LESSER_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_illumination"), CharmType.ILLUMINATION.getName(), 1, IlluminationCharm.class).withValue(3.0).build();
		ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "illumination"), CharmType.ILLUMINATION.getName(), 2, IlluminationCharm.class).withValue(6.0).build();
		GREATER_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_illumination"), CharmType.ILLUMINATION.getName(), 3, IlluminationCharm.class).withValue(12.0).build();
		GRAND_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "grand_illumination"), CharmType.ILLUMINATION.getName(), 4, IlluminationCharm.class).withValue(20.0).build();
		// TEMP size=5
		GLORIOUS_ILLUMINATION = new Charm.Builder(new ResourceLocation(Treasure.MODID, "glorious_illumination"), CharmType.ILLUMINATION.getName(), 5, IlluminationCharm.class).withValue(5.0).build();
		
		// curses
		LESSER_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "lesser_decay"), CharmType.DECAY.getName(), 1, DecayCharm.class).withValue(20.0).build();
		DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "decay"), CharmType.DECAY.getName(), 2, DecayCharm.class).withValue(50.0).build();
		GREATER_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "greater_decay"), CharmType.DECAY.getName(), 3, DecayCharm.class).withValue(100.0).build();
		PERISHING_DECAY = new Charm.Builder(new ResourceLocation(Treasure.MODID, "perishing_decay"), CharmType.DECAY.getName(), 4, DecayCharm.class).withValue(200.0).build();
        
        TreasureCharmRegistry.register(LESSER_HEALING);
        TreasureCharmRegistry.register(HEALING);
        TreasureCharmRegistry.register(GREATER_HEALING);
        TreasureCharmRegistry.register(GRAND_HEALING);
        TreasureCharmRegistry.register(SALANDAARS_CONVALESCENCE);
        TreasureCharmRegistry.register(WHALE_HEALING);
        
        TreasureCharmRegistry.register(SMALL_ABSOLUTE_SHIELDING);
        TreasureCharmRegistry.register(MEDIUM_ABSOLUTE_SHIELDING);
        TreasureCharmRegistry.register(LARGE_ABSOLUTE_SHIELDING);
        TreasureCharmRegistry.register(GRAND_ABSOLUTE_SHIELDING);
        TreasureCharmRegistry.register(WHALE_ABSOLUTE_SHIELDING);

		// register all charms
//		REGISTRY.put(LESSER_HEALING.getName(), LESSER_HEALING);
//		REGISTRY.put(HEALING.getName(), HEALING);
//		REGISTRY.put(GREATER_HEALING.getName(), GREATER_HEALING);
//		REGISTRY.put(GRAND_HEALING.getName(), GRAND_HEALING);
//		REGISTRY.put(SALANDAARS_CONVALESCENCE.getName(), SALANDAARS_CONVALESCENCE);
		
        TreasureCharmRegistry.register(DURABLE_SHIELDING);
        TreasureCharmRegistry.register(STOUT_SHIELDING);
        TreasureCharmRegistry.register(HARDENED_SHIELDING);
        TreasureCharmRegistry.register(POWERFUL_SHIELDING);
        TreasureCharmRegistry.register(ARMADILLO_SHIELDING);
        
//		REGISTRY.put(DURABLE_SHIELDING.getName(), DURABLE_SHIELDING);
//		REGISTRY.put(STOUT_SHIELDING.getName(), STOUT_SHIELDING);
//		REGISTRY.put(HARDENED_SHIELDING.getName(), HARDENED_SHIELDING);
//		REGISTRY.put(POWERFUL_SHIELDING.getName(), POWERFUL_SHIELDING);
//		REGISTRY.put(ARMADILLO_SHIELDING.getName(), ARMADILLO_SHIELDING);
		
//		REGISTRY.put(SMALL_ABSOLUTED_SHIELDING.getName(), SMALL_ABSOLUTE_SHIELDING);
//		REGISTRY.put(MEDIUM_ABSOLUTED_SHIELDING.getName(), MEDIUM_ABSOLUTE_SHIELDING);
//		REGISTRY.put(LARGE_ABSOLUTED_SHIELDING.getName(), LARGE_ABSOLUTE_SHIELDING);
//		REGISTRY.put(GRAND_ABSOLUTED_SHIELDING.getName(), GRAND_ABSOLUTE_SHIELDING);
//		REGISTRY.put(WHALE_ABSOLUTED_SHIELDING.getName(), WHALE_ABSOLUTE_SHIELDING);
		
        TreasureCharmRegistry.register(FULLNESS);
        TreasureCharmRegistry.register(SATED_FULLNESS);
        TreasureCharmRegistry.register(OVERFED_FULLNESS);
        TreasureCharmRegistry.register(GORGED_FULLNESS);
        TreasureCharmRegistry.register(BURSTING_FULLNESS);
        
//		REGISTRY.put(FULLNESS.getName(), FULLNESS);
//		REGISTRY.put(SATED_FULLNESS.getName(), SATED_FULLNESS);
//		REGISTRY.put(OVERFED_FULLNESS.getName(), OVERFED_FULLNESS);
//		REGISTRY.put(GORGED_FULLNESS.getName(), GORGED_FULLNESS);
//		REGISTRY.put(BURSTING_FULLNESS.getName(), BURSTING_FULLNESS);
		
        TreasureCharmRegistry.register(MINOR_HARVESTING);
        TreasureCharmRegistry.register(HARVESTING);
        TreasureCharmRegistry.register(MAJOR_HARVESTING);
        TreasureCharmRegistry.register(GRAND_HARVESTING);
        TreasureCharmRegistry.register(GLORIOUS_HARVESTING);
        
//		REGISTRY.put(MINOR_HARVESTING.getName(), MINOR_HARVESTING);
//		REGISTRY.put(HARVESTING.getName(), HARVESTING);
//		REGISTRY.put(MAJOR_HARVESTING.getName(), MAJOR_HARVESTING);
//		REGISTRY.put(GRAND_HARVESTING.getName(), GRAND_HARVESTING);
//		REGISTRY.put(GLORIOUS_HARVESTING.getName(), GLORIOUS_HARVESTING);
		
        TreasureCharmRegistry.register(LESSER_ILLUMINATION);
        TreasureCharmRegistry.register(ILLUMINATION);
        TreasureCharmRegistry.register(GREATER_ILLUMINATION);
        TreasureCharmRegistry.register(GRAND_ILLUMINATION);
        TreasureCharmRegistry.register(GLORIOUS_ILLUMINATION);
        
//		REGISTRY.put(LESSER_ILLUMINATION.getName(), LESSER_ILLUMINATION);
//		REGISTRY.put(ILLUMINATION.getName(), ILLUMINATION);
//		REGISTRY.put(GREATER_ILLUMINATION.getName(), GREATER_ILLUMINATION);
//		REGISTRY.put(GRAND_ILLUMINATION.getName(), GRAND_ILLUMINATION);
//		REGISTRY.put(GLORIOUS_ILLUMINATION.getName(), GLORIOUS_ILLUMINATION);
		
        TreasureCharmRegistry.register(LESSER_DECAY);
        TreasureCharmRegistry.register(DECAY);
        TreasureCharmRegistry.register(GREATER_DECAY);
        TreasureCharmRegistry.register(PERISHING_DECAY);
        
//		REGISTRY.put(LESSER_DECAY.getName(), LESSER_DECAY);
//		REGISTRY.put(DECAY.getName(), DECAY);
//		REGISTRY.put(GREATER_DECAY.getName(), GREATER_DECAY);
//		REGISTRY.put(PERISHING_DECAY.getName(), PERISHING_DECAY);
	}
}
