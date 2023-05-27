/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.config;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;

import mod.gottsch.forge.gottschcore.config.AbstractConfig;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

/**
 * 
 * @author Mark Gottschling on Nov 7, 2022
 *
 */
@EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config extends AbstractConfig {
	public static final String CATEGORY_DIV = "##############################";
	public static final String UNDERLINE_DIV = "------------------------------";

	public static final ForgeConfigSpec SERVER_SPEC;
	public static final ServerConfig SERVER;

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final ClientConfig CLIENT;
	
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final CommonConfig COMMON;
	
	// setup as a singleton
	public static Config instance = new Config();
		
	static {
		final Pair<CommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder()
				.configure(CommonConfig::new);
		COMMON_SPEC = commonSpecPair.getRight();
		COMMON = commonSpecPair.getLeft();
		
		final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
				.configure(ServerConfig::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();

		final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder()
				.configure(ClientConfig::new);
		CLIENT_SPEC = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();
	}
	
	private Config() {}
	
	/**
	 * 
	 */
	public static void register() {
		registerCommonConfig();
		registerClientConfig();
		registerServerConfig();
	}
	
	private static void registerCommonConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
	}
	
	private static void registerClientConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}
	
	private static void registerServerConfig() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
	}
	
	/*
	 * 
	 */
	public static class CommonConfig {
		public static Logging logging;
		public CommonConfig(ForgeConfigSpec.Builder builder) {
			logging = new Logging(builder);
		}
	}
	
	@Override
	public String getLogsFolder() {
		return CommonConfig.logging.folder.get();
	}
	
	@Override
	public String getLogSize() {
		return CommonConfig.logging.size.get();
	}
	
	@Override
	public String getLoggingLevel() {
		return CommonConfig.logging.level.get();
	}
	
	/*
	 * 
	 */
	public static class ClientConfig {
		public ClientGui gui;

		public ClientConfig(ForgeConfigSpec.Builder builder) {
			gui = new ClientGui(builder);
		}
	}
	
	public static class ClientGui {
		public BooleanValue enableCustomChestInventoryGui;
		public ForgeConfigSpec.BooleanValue enableFog;
		
		ClientGui(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " GUI properties", CATEGORY_DIV)
			.push("gui");

			enableCustomChestInventoryGui = builder
					.comment(" Enable/Disable whether to use Treasure2's custom guis for chest inventory screens.")
					.define("enableCustomChestInventoryGui", true);
						
			enableFog = builder
					.comment(" Enable/disable white fog.")
					.define("enableFog", true);
			
			builder.pop();
		}
	}
	/*
	 * 
	 */
	public static class ServerConfig {
		public KeysAndLocks keysAndLocks;
		public Wealth wealth;
		public Effects effects;
		public Integration integration;
		public Markers markers;
		public WitherTree witherTree;
		public Wells wells;
		public Pits pits;

		/**
		 * 
		 * @param builder
		 */
		public ServerConfig(ForgeConfigSpec.Builder builder) {
			keysAndLocks = new KeysAndLocks(builder);	
			wealth = new Wealth(builder);
			effects = new Effects(builder);
			integration = new Integration(builder);
			markers = new Markers(builder);
			witherTree = new WitherTree(builder);
			wells = new Wells(builder);
			pits = new Pits(builder);
		}
		
		/*
		 * 
		 */
		public static class Integration {
			public ConfigValue<List<? extends String>> dimensionsWhiteList;
			
			public Integration(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Integration properties", CATEGORY_DIV)
				.push("integration");
				
				dimensionsWhiteList = builder
						.comment(" Permitted Dimensions for Treasure2 execution.", 
								" Treasure2 was designed for 'normal' overworld-type dimensions.", 
								" This setting does not use any wildcards (*). You must explicitly set the dimensions that are allowed.", 
								" ex. minecraft:overworld")
						.defineList("Dimension White List:", Arrays.asList(new String []{"minecraft:overworld"}), s -> s instanceof String);
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class KeysAndLocks {
			public BooleanValue enableKeyBreaks;
			public BooleanValue enableLockDrops;
			public ConfigValue<Integer> pilferersLockPickMaxUses;
			public ConfigValue<Integer> thiefsLockPickMaxUses;
			public ConfigValue<Integer> woodKeyMaxUses;
			public ConfigValue<Integer> stoneKeyMaxUses;
			public ConfigValue<Integer> emberKeyMaxUses;
			public ConfigValue<Integer> leafKeyMaxUses;
			public ConfigValue<Integer> lightningKeyMaxUses;
			public ConfigValue<Integer> ironKeyMaxUses;
			public ConfigValue<Integer> goldKeyMaxUses;
			public ConfigValue<Integer> diamondKeyMaxUses;
			public ConfigValue<Integer> emeraldKeyMaxUses;
			public ConfigValue<Integer> rubyKeyMaxUses;
			public ConfigValue<Integer> sapphireKeyMaxUses;
			public ConfigValue<Integer> metallurgistsKeyMaxUses;
			public ConfigValue<Integer> skeletonKeyMaxUses;
			public ConfigValue<Integer> jewelledKeyMaxUses;
			public ConfigValue<Integer> spiderKeyMaxUses;
			public ConfigValue<Integer> witherKeyMaxUses;
			
			KeysAndLocks(final ForgeConfigSpec.Builder builder) {
				builder.comment(CATEGORY_DIV, " Keys and Locks properties", CATEGORY_DIV)
				.push("keysAndLocks");

				enableKeyBreaks = builder
						.comment(" Enable/Disable whether a Key can break when attempting to unlock a Lock.")
						.define("enableKeyBreaks", true);

				enableLockDrops = builder
						.comment(" Enable/Disable whether a Lock item is dropped when unlocked by Key item.")
						.define("enableLockDrops", true);
				
				pilferersLockPickMaxUses = builder
						.comment(" The maximum uses for a given pilferers lock pick.")
						.defineInRange("pilferersLockPickMaxUses", 10, 1, 32000);

				thiefsLockPickMaxUses = builder
						.comment(" The maximum uses for a given thiefs lock pick.")
						.defineInRange("thiefsLockPickMaxUses", 10, 1, 32000);

				woodKeyMaxUses = builder
						.comment(" The maximum uses for a given wooden key.")
						.defineInRange("woodKeyMaxUses", 20, 1, 32000);

				stoneKeyMaxUses = builder
						.comment(" The maximum uses for a given stone key.")
						.defineInRange("stoneKeyMaxUses", 10, 1, 32000);

				emberKeyMaxUses = builder
						.comment(" The maximum uses for a given ember key.")
						.defineInRange("emberKeyMaxUses", 15, 1, 32000);

				leafKeyMaxUses = builder
						.comment(" The maximum uses for a given leaf key.")
						.defineInRange("leafKeyMaxUses", 15, 1, 32000); 

				lightningKeyMaxUses = builder
						.comment(" The maximum uses for a given lightning key.")
						.defineInRange("lightningKeyMaxUses", 10, 1, 32000); 

				ironKeyMaxUses = builder
						.comment(" The maximum uses for a given iron key.")
						.defineInRange("ironKeyMaxUses", 10, 1, 32000);

				goldKeyMaxUses = builder
						.comment(" The maximum uses for a given gold key.")
						.defineInRange("goldKeyMaxUses", 15, 1, 32000);

				diamondKeyMaxUses = builder
						.comment(" The maximum uses for a given diamond key.")
						.defineInRange("diamondKeyMaxUses", 20, 1, 32000);

				emeraldKeyMaxUses = builder
						.comment(" The maximum uses for a given emerald key.")
						.defineInRange("emeraldKeyMaxUses", 10, 1, 32000);

				rubyKeyMaxUses = builder
						.comment(" The maximum uses for a given ruby key.")
						.defineInRange("rubyKeyMaxUses", 5, 1, 32000);

				sapphireKeyMaxUses = builder
						.comment(" The maximum uses for a given sapphire key.")
						.defineInRange("sapphireKeyMaxUses", 5, 1, 32000);

				metallurgistsKeyMaxUses = builder
						.comment(" The maximum uses for a given metallurgists key.")
						.defineInRange("metallurgistsKeyMaxUses", 25, 1, 32000);

				skeletonKeyMaxUses = builder
						.comment(" The maximum uses for a given skeleton key.")
						.defineInRange("skeletonKeyMaxUses", 5, 1, 32000);

				jewelledKeyMaxUses = builder
						.comment(" The maximum uses for a given jewelled key.")
						.defineInRange("jewelledKeyMaxUses", 5, 1, 32000);

				spiderKeyMaxUses = builder
						.comment(" The maximum uses for a given spider key.")
						.defineInRange("spiderKeyMaxUses", 5, 1, 32000);

				witherKeyMaxUses = builder
						.comment(" The maximum uses for a given wither key.")
						.defineInRange("witherKeyMaxUses", 5, 1, 32000);
				
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class Wealth {
			public ForgeConfigSpec.ConfigValue<Integer> wealthMaxStackSize;
			
			public Wealth(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Treasure Loot and Valuables properties", CATEGORY_DIV)
				.push("wealth");
				
				wealthMaxStackSize = builder
						.comment(" The maximum size of a wealth item stacks. ex. Coins, Gems, Pearls")
						.defineInRange("wealthMaxStackSize", 8, 1, 64);
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class Markers {
			public ForgeConfigSpec.BooleanValue enableMarkers;
			public ForgeConfigSpec.BooleanValue enableMarkerStructures;
			public ForgeConfigSpec.ConfigValue<Integer> minMarkersPerChest;
			public ForgeConfigSpec.ConfigValue<Integer> maxMarkersPerChest;
			public ForgeConfigSpec.ConfigValue<Integer> structureProbability;
			public ForgeConfigSpec.BooleanValue enableSpawner;
			public ForgeConfigSpec.ConfigValue<Integer> spawnerProbability;
			
			public Markers(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Gravestones and Markers properties", CATEGORY_DIV)
				.push("markers");
				
				enableMarkers = builder
						.comment(" Enable/disable whether chest markers (gravestones, bones)  are generated when generating treasure chests.")
						.define("enableMarkers", true);
				
				enableMarkerStructures = builder
						.comment(" Enable/disable whether structures (buildings) are generated when generating  treasure chests.")
						.define("enableMarkerStructures", true);
				
				minMarkersPerChest = builder
						.comment(" The minimum number of markers (gravestones, bones) per chest.")
						.defineInRange("minMarkersPerChest", 2, 1, 5);
				
				maxMarkersPerChest = builder
						.comment(" The maximum number of markers (gravestones, bones) per chest.")
						.defineInRange("maxMarkersPerChest", 5, 1, 10);
				
				structureProbability = builder
						.comment(" The probability that a marker will be a structure.")
						.defineInRange("structureProbability", 15, 1, 100);
				
				enableSpawner = builder
						.comment(" Enable/disable whether gravestone markers can spawn mobs (ex. Bound Soul).")
						.define("enableSpawner", true);
				
				spawnerProbability = builder
						.comment(" The probability that a gravestone will spawn a mob.", " Currently gravestones can spawn Bound Souls.")
						.defineInRange("spawnerProbability", 25, 1, 100);
								
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class WitherTree {
			public BooleanValue enableWitherTree;
			public ConfigValue<Integer> registrySize;
			public ConfigValue<Double> probability;
			public ConfigValue<Integer> minBlockDistance;
			public ConfigValue<Integer>	waitChunks;
			public BiomesConfig biomes;
			
			public ConfigValue<Integer> maxTrunkSize;
			public ConfigValue<Integer> minSupportingTrees;
			public ConfigValue<Integer> maxSupportingTrees;
			
			public WitherTree(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Wither Tree properties", CATEGORY_DIV)
				.push("witherTrees");
				
				enableWitherTree = builder
						.comment(" Enable/disable whether wither trees will spawn.")
						.define("enableWitherTree", true);
				
				registrySize = builder
						.comment(" The number of wither tree spawns that are monitored.",
								" Most recent additions replace least recent when the registry is full.",
								" This is the set of wither tree spawns used to measure distance between newly generated spawns.",
								" In general, a high number is better than a low number, especially in a multiplayer world.",
								" However, wither tree spawns have a default low probability/great distance, so the number can be",
								" a lower than that of chests, which spawn much more frequently.")
						.defineInRange("registrySize", 50, 25, 1000);
				
				this.probability = builder
						.comment(" The probability that a wither tree will generate at selected spawn location.",
								" Including a non-100 value increases the randomization of wither tree spawn placement.")
						.defineInRange("probability", 70.0, 0.0, 100.0);
				
				this.minBlockDistance = builder
						.comment(" The minimum distance, measured in blocks, that two wither tree spawns can be in proximity (ie radius).",
								" Note: Only wither trees in the registry are checked against this property.",
								" Default = 1000 blocks.")
						.defineInRange("minBlockDistance", 1000, 100, 32000);
				
				this.waitChunks = builder
						.comment(" The number of chunks that are generated in a new world before wither trees start to spawn.")
						.defineInRange("waitChunks", 500, 10, 32000);
				
				maxTrunkSize = builder
						.comment(" The maximum height a wither tree can reach.",
								" This is the high end of a calculated range. ex. size is randomized between minTrunkSize and maxTrunkSize.",
								" (The minimum is predefined.)")
						.defineInRange("Maximum trunk height (in blocks):", 13, 7, 20);
				
				minSupportingTrees = builder
						.comment(" The minimum number of supporting wither trees that surround the main tree in the grove.")
						.defineInRange("Minimum number of supporting trees:", 5, 0, 30);
				
				maxSupportingTrees = builder
						.comment(" The maximum number of supporting wither trees that surround the main tree in the grove.")
						.defineInRange("Maximum number of supporting trees:", 15, 0, 30);
				
				
				BiomesConfig.Data biomesData = new BiomesConfig.Data(new String[] {}, new String[] { "minecraft:ocean", "minecraft:deep_ocean", "minecraft:deep_frozen_ocean", "minecraft:cold_ocean",
						"minecraft:deep_cold_ocean", "minecraft:lukewarm_ocean", "minecraft:warm_ocean" },
				new String[] {}, new String[] { "minecraft:ocean", "minecraft:deep_ocean" });
				biomes = new BiomesConfig(builder, biomesData);
				
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class Wells {
			public BooleanValue enableWells;
			public ConfigValue<Integer> cacheSize;
			public ConfigValue<Double> probability;
			public ConfigValue<Integer> minBlockDistance;
			public ConfigValue<Integer>	waitChunks;
			public BiomesConfig biomes;
			
			public ConfigValue<Integer> scanForItemRadius;
			public ConfigValue<Integer> scanForWellRadius;
			public ConfigValue<Integer> scanMinBlockCount;
			
			public Wells(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Wells properties", CATEGORY_DIV)
				.push("wells");
				
				enableWells = builder
						.comment(" Enable/disable whether wells will spawn.")
						.define("enableWells", true);
				
				cacheSize = builder
						.comment(" The number of wells spawns that are monitored.",
								" Most recent additions replace least recent when the cache is full.",
								" This is the set of wells used to measure distance between newly generated wells.",
								" In general, a high number is better than a low number, especially in a multiplayer world.",
								" However, wells have a default low probability/great distance, so the number can be",
								" a lower than that of chests, which spawn much more frequently.")
						.defineInRange("cacheSize", 50, 25, 1000);
				
				this.probability = builder
						.comment(" The probability that a well will generate at selected spawn location.",
								" Including a non-100 value increases the randomization of well placement.")
						.defineInRange("probability", 85.0, 0.0, 100.0);
				
				this.minBlockDistance = builder
						.comment(" The minimum distance, measured in blocks, that two wells can be in proximity (ie radius).",
								" Note: Only wells in the registry are checked against this property.",
								" Default = 600 blocks, or 16 chunks.")
						.defineInRange("minBlockDistance", 600, 100, 32000);
				
				this.waitChunks = builder
						.comment(" The number of chunks that are generated in a new world before wells start to spawn.")
						.defineInRange("waitChunks", 100, 10, 32000);
				
				BiomesConfig.Data biomesData = new BiomesConfig.Data(new String[] {}, new String[] { "minecraft:ocean", "minecraft:deep_ocean", "minecraft:deep_frozen_ocean", "minecraft:cold_ocean",
						"minecraft:deep_cold_ocean", "minecraft:lukewarm_ocean", "minecraft:warm_ocean" },
				new String[] {}, new String[] { "minecraft:ocean", "minecraft:deep_ocean" });
				biomes = new BiomesConfig(builder, biomesData);
				
				this.scanForItemRadius = builder
						.comment(" The number of blocks in radius around player to scan for tossed/dropped wishables items.",
								"  Ex. if player is at (0, 0, 0), then scan range would be (-1, 0, -1) -> (1, 0, 1).")
						.defineInRange("scanForItemRadius", 4, 1, 10);
				
				this.scanForWellRadius = builder
						.comment(" The number of blocks in radius around wishable item to scan for a well.",
								"  Ex. if item is at (0, 0, 0), then scan range would be (-1, 0, -1) -> (1, 0, 1).")
						.defineInRange("scanForWellRadius", 1, 1, 10);
				
				this.scanMinBlockCount = builder
						.comment(" The number of blocks in radius around a wishable item (hortizontally) that are scanned to discover a well.",
								"  Ex. if item is at (0, 0, 0), then scan range would be (-1, 0, -1) -> (1, 0, 1).")
						.defineInRange("scanMinBlockCount", 2, 1, 8);
				
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class Pits {
			public ConfigValue<Integer> structureProbability;
			
			public Pits(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Pit properties", CATEGORY_DIV)
				.push("pits");
				
				structureProbability = builder
						.comment("The probability that a pit will contain a structure (treasure room(s), cavern etc.)")
						.defineInRange("structureProbability", 25, 0, 100);
				builder.pop();
			}
		}
		
		/*
		 * 
		 */
		public static class Effects {
			public BooleanValue enableUndiscoveredEffects;

			
			public Effects(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Effects and GUI Elements", CATEGORY_DIV)
				.push("effects");
				
				enableUndiscoveredEffects = builder
						.comment(" Enable/disable whether 'undiscovered' chests (ie spawned and not found) will display effects such as light source, particles, or glow.")
						.define("enableUndiscoveredEffects", true);
		
				builder.pop();
			}
		}
	}
	
	/**
	 * Chest Config
	 */
	public static final ForgeConfigSpec CHESTS_CONFIG_SPEC;
		
	// TODO remove the mapping by dimensions. remove the list in ChestConfigsHolder
	//	there should only be 1 chest config with a whitelist of dimensions to apply against,
	// NOT a whole config for each dimension.
	// TODO make this a map and part of the transform() method loads into a map
	// TODO either this is a multimap based on dimension or a singular ChestConfiguration
	// and the generators etc need to be mapped - not in a list.
	/*
	 * exposed chest configurations
	 */
	public static List<ChestConfiguration> chestConfigs;
	public static Map<ResourceLocation, ChestConfiguration> chestConfigMap;
	
	static {
		final Pair<ChestConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
				.configure(ChestConfig::new);
		CHESTS_CONFIG_SPEC = specPair.getRight();
	}
	
	/*
	 * Structure Configuration
	 */
	public static final ForgeConfigSpec STRUCTURE_CONFIG_SPEC;
	public static StructureConfiguration structureConfiguration;
	public static  Map<ResourceLocation, StructureConfiguration.StructMeta> structConfigMetaMap;
	
	
	static {
		final Pair<InternalStructureConfiguration, ForgeConfigSpec	> structSpecPair = new ForgeConfigSpec.Builder()
				.configure(InternalStructureConfiguration::new);
		STRUCTURE_CONFIG_SPEC = structSpecPair.getRight();
	}
	
	private static class ChestConfig {
		public ChestConfig(ForgeConfigSpec.Builder builder) {
			builder.comment("####", " rarities = common, uncommon, scarce, rare, epic, legendary, mythical", "####").define("chestConfigs", new ArrayList<>());
			builder.build();
		}
	}

	/**
	 * 
	 * @param configData
	 */
	public static void transform(CommentedConfig configData) {
		// convert the data to an object
      		ChestConfigsHolder holder = new ObjectConverter().toObject(configData, ChestConfigsHolder::new);
		// get the list from the holder and set the config property
		chestConfigs = holder.chestConfigs;
		
		// create the chest config map
		chestConfigMap = Maps.newHashMap();
		chestConfigs.forEach(config -> {
			config.getDimensions().forEach(dimension -> {
				chestConfigMap.put(ModUtil.asLocation(dimension), config);
			});
		});
	}
	
	/*
	 * 
	 */
	private static class InternalStructureConfiguration {
		public InternalStructureConfiguration(ForgeConfigSpec.Builder builder) {
			// NOTE having an ArrayList<>() here is a real bugger. Not sure how to create a Config()
			// NOTE this define() name must match the wrapper property in the toml file.
			builder.define("structureConfigs", new ArrayList<>());
			builder.build();
		}
	}
	
	/**
	 * 
	 * @param configData
	 * @return
	 */
	public static Optional<StructureConfiguration> transformStructureConfiguration(CommentedConfig configData) {
		StructureConfigurationHolder holder = new ObjectConverter().toObject(configData, StructureConfigurationHolder::new);
		if (holder == null || holder.structureConfigs == null || holder.structureConfigs.isEmpty()) {
			return Optional.empty();
		} else {
			structureConfiguration = holder.structureConfigs.get(0);
			structConfigMetaMap = new HashMap<>();
			structureConfiguration.structMetas.forEach(meta -> {
				structConfigMetaMap.put(ModUtil.asLocation(meta.getName()), meta);
			});
		}
		return Optional.ofNullable(holder.structureConfigs.get(0));
	}
	
	/**
	 * A temporary holder classes.
	 *
	 */
	private static class ChestConfigsHolder {
		public List<ChestConfiguration> chestConfigs;
	}
	
	private static class StructureConfigurationHolder {
		public List<StructureConfiguration> structureConfigs;
	}
}


