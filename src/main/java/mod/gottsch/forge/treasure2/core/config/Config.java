/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.config;

import mod.gottsch.forge.gottschcore.config.AbstractConfig;
import mod.gottsch.forge.treasure2.Treasure;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

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
					.comment(" Enable/disable white fog. ex gravestones")
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
//		public Integration integration;
		public Markers markers;
		public WitherTree witherTree;
		public Wells wells;
		public Mobs mobs;
		public Maps maps;

		/**
		 * 
		 * @param builder
		 */
		public ServerConfig(ForgeConfigSpec.Builder builder) {
			keysAndLocks = new KeysAndLocks(builder);	
			wealth = new Wealth(builder);
			effects = new Effects(builder);
//			integration = new Integration(builder);
			markers = new Markers(builder);
			witherTree = new WitherTree(builder);
			wells = new Wells(builder);
			mobs = new Mobs(builder);
			maps = new Maps(builder);
		}

		/*
		 * 
		 */
		// TODO is dimensionWhiteList still necessary?
//		public static class Integration {
//			public ConfigValue<List<? extends String>> dimensionsWhiteList;
//
//			public Integration(final ForgeConfigSpec.Builder builder)	 {
//				builder.comment(CATEGORY_DIV, " Integration properties", CATEGORY_DIV)
//				.push("integration");
//
//				dimensionsWhiteList = builder
//						.comment(" Permitted Dimensions for Treasure2 execution.",
//								" Treasure2 was designed for 'normal' overworld-type dimensions.",
//								" This setting does not use any wildcards (*). You must explicitly set the dimensions that are allowed.",
//								" ex. minecraft:overworld")
//						.defineList("dimensionsWhiteList", Arrays.asList(new String []{"minecraft:overworld"}), s -> s instanceof String);
//				builder.pop();
//			}
//		}

		/*
		 * 
		 */
		public static class KeysAndLocks {
			public BooleanValue enableKeyBreaks;
			public BooleanValue enableLockDrops;
			public ConfigValue<Integer> pilferersLockPickMaxUses;
			public ForgeConfigSpec.DoubleValue pilferersLockPickCommonSuccessProbability;
			public ForgeConfigSpec.DoubleValue pilferersLockPickUncommonSuccessProbability;

			public ConfigValue<Integer> thiefsLockPickMaxUses;
			public ForgeConfigSpec.DoubleValue thiefsLockPickCommonSuccessProbability;
			public ForgeConfigSpec.DoubleValue thiefsLockPickUncommonSuccessProbability;
			public ForgeConfigSpec.DoubleValue thiefsLockPickScarceSuccessProbability;

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
			public ConfigValue<Integer> topazKeyMaxUses;
			public ConfigValue<Integer> onyxKeyMaxUses;

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

				pilferersLockPickCommonSuccessProbability = builder
						.comment(" The success probability of a pilferers lock pick on a COMMON lock.")
						.defineInRange("pilferersLockPickCommonSuccessProbability", 48D, 1D, 100D);

				pilferersLockPickUncommonSuccessProbability = builder
						.comment(" The success probability of a pilferers lock pick on an UNCOMMON lock.")
						.defineInRange("pilferersLockPickUncommonSuccessProbability", 32D, 1D, 100D);

				thiefsLockPickMaxUses = builder
						.comment(" The maximum uses for a given thiefs lock pick.")
						.defineInRange("thiefsLockPickMaxUses", 10, 1, 32000);

				thiefsLockPickCommonSuccessProbability = builder
						.comment(" The success probability of a thiefs lock pick on a COMMON lock.")
						.defineInRange("thiefsLockPickCommonSuccessProbability", 60D, 1D, 100D);

				thiefsLockPickUncommonSuccessProbability = builder
						.comment(" The success probability of a thiefs lock pick on an UNCOMMON lock.")
						.defineInRange("thiefsLockPickUncommonSuccessProbability", 45D, 1D, 100D);

				thiefsLockPickScarceSuccessProbability = builder
						.comment(" The success probability of a thiefs lock pick on an SCARCE lock.")
						.defineInRange("thiefsLockPickScarceSuccessProbability", 30D, 1D, 100D);

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

				metallurgistsKeyMaxUses = builder
						.comment(" The maximum uses for a given metallurgists key.")
						.defineInRange("metallurgistsKeyMaxUses", 25, 1, 32000);

				diamondKeyMaxUses = builder
						.comment(" The maximum uses for a given diamond key.")
						.defineInRange("diamondKeyMaxUses", 20, 1, 32000);

				emeraldKeyMaxUses = builder
						.comment(" The maximum uses for a given emerald key.")
						.defineInRange("emeraldKeyMaxUses", 10, 1, 32000);

				topazKeyMaxUses = builder
						.comment(" The maximum uses for a given topaz key.")
						.defineInRange("topazKeyMaxUses", 7, 1, 32000);

				onyxKeyMaxUses = builder
						.comment(" The maximum uses for a given onyx key.")
						.defineInRange("onyxKeyMaxUses", 7, 1, 32000);

				rubyKeyMaxUses = builder
						.comment(" The maximum uses for a given ruby key.")
						.defineInRange("rubyKeyMaxUses", 5, 1, 32000);

				sapphireKeyMaxUses = builder
						.comment(" The maximum uses for a given sapphire key.")
						.defineInRange("sapphireKeyMaxUses", 5, 1, 32000);

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

			public ForgeConfigSpec.ConfigValue<Boolean> enableVanillaLootModifiers;
			public Wealth(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Treasure Loot and Valuables properties", CATEGORY_DIV)
				.push("wealth");

				enableVanillaLootModifiers = builder
						.comment(" Enable/Disable global loot modifiers that injects Treasure2 loot into vanilla loot tables.")
						.define("enableVanillaLootModifiers", true);
				
				builder.pop();
			}
		}

		/*
		 *
		 */
		public static class Markers {
			public ForgeConfigSpec.BooleanValue enableSpawner;
			public ForgeConfigSpec.ConfigValue<Integer> spawnerProbability;

			public Markers(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Gravestones and Markers properties", CATEGORY_DIV)
				.push("markers");

				enableSpawner = builder
						.comment(" Enable/disable whether gravestone markers can spawn mobs (ex. Bound Soul).")
						.define("enableSpawner", true);

				spawnerProbability = builder
						.comment(" The probability that a gravestone will spawn a mob.", " Currently gravestones can spawn Bound Souls.")
						.defineInRange("spawnerProbability", 15, 1, 100);

				builder.pop();
			}
		}

		/*
		 * 
		 */
		public static class WitherTree {
			public BooleanValue enablePoisonFog;
			public BooleanValue enableWitherFog;

			public WitherTree(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Wither Tree properties", CATEGORY_DIV)
				.push("witherTrees");

				enablePoisonFog = builder
						.comment(" Enable/disable poison fog around wither trees.")
						.define("enablePoisonFog", true);

				enableWitherFog = builder
						.comment(" Enable/disable wither fog around wither trees.")
						.define("enableWitherFog", true);

				builder.pop();
			}
		}

		/*
		 * 
		 */
		public static class Wells {

			public ConfigValue<Integer> scanForItemRadius;
			public ConfigValue<Integer> scanForWellRadius;
			public ConfigValue<Integer> scanMinBlockCount;
			public ConfigValue<Double> cloverProbability;

			public Wells(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Wells properties", CATEGORY_DIV)
				.push("wells");

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

				this.cloverProbability = builder
						.comment(" The probability that a well will generate a Clover.")
						.defineInRange("cloverProbability", 5.0, 0.0, 100.0);

				builder.pop();
			}
		}

		public static class Mobs {
			public ConfigValue<Boolean> enableMimics;
			public ConfigValue<Integer> mimicProbability;

			public Mobs(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Mob properties", CATEGORY_DIV)
				.push("mobs");

				enableMimics = builder
						.comment(" Enable/disable whether mimics can spawn.")
						.define("enableMimics", true);

				mimicProbability = builder
						.comment(" The probability that a mimic will spawn instead of a chest.", "  Not all chests have a mimic counterpart.")
						.defineInRange("probability", 12, 0, 100);

				builder.pop();
			}
		}
		
		public static class Maps {
			public ConfigValue<Boolean> enableMaps;
			public ConfigValue<Double> mapProbability;

			public Maps(final ForgeConfigSpec.Builder builder)	 {
				builder.comment(CATEGORY_DIV, " Map properties", CATEGORY_DIV)
				.push("maps");

				enableMaps = builder
						.comment(" Enable/disable whether a chest can contain a treasure map to another chest.")
						.define("enableMaps", true);

				mapProbability = builder
						.comment(" The probability that a chest will contain a treasure map to another chest.")
						.defineInRange("probability", 20D, 0D, 100D);

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
						.comment(" Enable/disable whether 'undiscovered' chests (ie spawned and not found) will display effects such as light source, particles, or glow.",
								" Note: due to changes in Forge, this config option will NOT affect the light that undiscovered chests produce in 1.19.2+.")
						.define("enableUndiscoveredEffects", true);

				builder.pop();
			}
		}
	}
}



