/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import com.someguyssoftware.gottschcore.config.AbstractConfig;
import com.someguyssoftware.gottschcore.mod.IMod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Reloading;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * 
 * @author Mark Gottschling on Aug 11, 2020
 *
 */
@EventBusSubscriber
public class TreasureConfig extends AbstractConfig {
	public static ForgeConfigSpec COMMON_CONFIG;

	static {
		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	private static IMod mod;

	/**
	 * 
	 * @param mod
	 */
	public TreasureConfig(IMod mod) {
		TreasureConfig.mod = mod;
	}

	public static class LockID {
		public static final String WOOD_LOCK_ID = "wood_lock";
		 public static final String STONE_LOCK_ID = "stone_lock";
		 public static final String IRON_LOCK_ID = "iron_lock";
		 public static final String GOLD_LOCK_ID = "gold_lock";
		 public static final String DIAMOND_LOCK_ID = "diamond_lock";
		 public static final String EMERALD_LOCK_ID = "emerald_lock";
		 public static final String RUBY_LOCK_ID = "ruby_lock";
		 public static final String SAPPHIRE_LOCK_ID = "sapphire_lock";
		 public static final String SPIDER_LOCK_ID = "spider_lock";
		 public static final String WITHER_LOCK_ID = "wither_lock";
	}
	
	public static class KeyID {
		public static final String WOOD_KEY_ID = "wood_key";
		 public static final String STONE_KEY_ID = "stone_key";
		 public static final String IRON_KEY_ID = "iron_key";
		 public static final String GOLD_KEY_ID = "gold_key";
		 public static final String DIAMOND_KEY_ID = "diamond_key";
		 public static final String EMERALD_KEY_ID = "emerald_key";
		 public static final String RUBY_KEY_ID = "ruby_key";
		 public static final String SAPPHIRE_KEY_ID = "sapphire_key";
		 public static final String JEWELLED_KEY_ID = "jewelled_key";
		 public static final String METALLURGISTS_KEY_ID = "metallurgists_key";
		 public static final String SKELETON_KEY_ID = "skeleton_key";
		 public static final String WITHER_KEY_ID = "wither_key";
		 public static final String SPIDER_KEY_ID = "spider_key";

		 public static final String PILFERERS_LOCK_PICK_ID = "pilferers_lock_pick";
		 public static final String THIEFS_LOCK_PICK_ID = "thiefs_lock_pick";
	}
	
	public static class ChestID {
		public static final String WOOD_CHEST_ID = "wood_chest";
		public static final String CRATE_CHEST_ID = "crate_chest";
		public static final String MOLDY_CRATE_CHEST_ID = "crate_chest_moldy";
		public static final String IRONBOUND_CHEST_ID = "ironbound_chest";
		public static final String PIRATE_CHEST_ID = "pirate_chest";
		public static final String IRON_STRONGBOX_ID = "iron_strongbox";
		public static final String GOLD_STRONGBOX_ID = "gold_strongbox";
		public static final String SAFE_ID = "safe";
		public static final String DREAD_PIRATE_CHEST_ID = "dread_pirate_chest";
		public static final String COMPRESSOR_CHEST_ID = "compressor_chest";
		public static final String WITHER_CHEST_ID = "wither_chest";
		public static final String WITHER_CHEST_TOP_ID = "wither_chest_top";
		public static final String SKULL_CHEST_ID = "skull_chest";
		public static final String GOLD_SKULL_CHEST_ID = "gold_skull_chest";
		public static final String CAULDRON_CHEST_ID = "cauldron_chest";
		public static final String SPIDER_CHEST_ID = "spider_chest";
		public static final String VIKING_CHEST_ID = "viking_chest";
	}
	
	public static class TileEntityID {
		public static final String WOOD_CHEST_TE_ID = "wood_chest_tile_entity_type";
		public static final String CRATE_CHEST_TE_ID = "crate_chest_tile_entity_type";
		public static final String MOLDY_CRATE_CHEST_TE_ID = "crate_chest_moldy_tile_entity_type";
		public static final String IRONBOUND_CHEST_TE_ID = "ironbound_chest_tile_entity_type";
		public static final String PIRATE_CHEST_TE_ID = "pirate_chest_tile_entity";
		public static final String IRON_STRONGBOX_TE_ID = "iron_strongbox_tile_entity";
		public static final String GOLD_STRONGBOX_TE_ID = "gold_strongbox_tile_entity";
		public static final String SAFE_TE_ID = "safe_tile_entity";
		public static final String DREAD_PIRATE_CHEST_TE_ID = "dread_pirate_chest_tile_entity";
		public static final String WHALE_BONE_PIRATE_CHEST_TE_ID = "whale_bone_pirate_chest_tile_entity";
		public static final String COMPRESSOR_CHEST_TE_ID = "compressor_chest_tile_entity";
		public static final String WITHER_CHEST_TE_ID = "wither_chest_tile_entity";
		public static final String SKULL_CHEST_TE_ID = "skull_chest_tile_entity";
		public static final String GOLD_SKULL_CHEST_TE_ID = "gold_skull_chest_tile_entity";
		public static final String CAULDRON_CHEST_TE_ID = "cauldron_chest_tile_entity";
		public static final String OYSTER_CHEST_TE_ID = "oyster_chest_tile_entity";
		public static final String CLAM_CHEST_TE_ID = "clam_chest_tile_entity";
		public static final String SPIDER_CHEST_TE_ID = "spider_chest_tile_entity";
		public static final String VIKING_CHEST_TE_ID = "viking_chest_tile_entity";
		public static final String PROXIMITY_SPAWNER_TE_ID = "proximity_spawner_tile_entity";
		public static final String GRAVESTONE_TE_ID = "gravestone_tile_entity";
		public static final String GRAVESTONE_PROXIMITY_SPAWNER_TE_ID = "gravestone_proximity_spawner_tile_entity";
	}
	
	public static class KeysAndLocks {
//		@Comment({ "Enable/Disable whether a Key can break when attempting to unlock a Lock." })
//		@Name("01. Enable key breaks:")
		public boolean enableKeyBreaks = true;

//		@Comment({ "Enable/Disable whether a Lock item is dropped when unlocked by Key item." })
//		@Name("02. Enable lock drops:")
		public boolean enableLockDrops = true;
		
//		@Comment({ "The maximum uses for a given pilferers lock pick." })
//		@Name("03. Pilferer's lockpick max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int pilferersLockPickMaxUses = 10;

//		@Comment({ "The maximum uses for a given thiefs lock pick." })
//		@Name("04. Thief's lockpick max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int thiefsLockPickMaxUses = 10;
		
//		@Comment({ "The maximum uses for a given wooden key." })
//		@Name("05. Wood key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int woodKeyMaxUses = 20;
		
//		@Comment({ "The maximum uses for a given stone key." })
//		@Name("06. Stone key max uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int stoneKeyMaxUses = 10;

//		@Comment({ "The maximum uses for a given iron key." })
//		@Name("07. Iron key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int ironKeyMaxUses = 10;

//		@Comment({ "The maximum uses for a given gold key." })
//		@Name("08. Gold key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int goldKeyMaxUses = 15;

//		@Comment({ "The maximum uses for a given diamond key." })
//		@Name("09. Diamond key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int diamondKeyMaxUses = 20;

//		@Comment({ "The maximum uses for a given emerald key." })
//		@Name("10. Emerald key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int emeraldKeyMaxUses = 10;

//		@Comment({ "The maximum uses for a given ruby key." })
//		@Name("11. Ruby key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int rubyKeyMaxUses = 5;

//		@Comment({ "The maximum uses for a given sapphire key." })
//		@Name("12. Sapphire key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int sapphireKeyMaxUses = 5;

//		@Comment({ "The maximum uses for a given metallurgists key." })
//		@Name("13. Metallurgists key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int metallurgistsKeyMaxUses = 25;

//		@Comment({ "The maximum uses for a given skeleton key." })
//		@Name("14. Skeleton key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int skeletonKeyMaxUses = 5;

//		@Comment({ "The maximum uses for a given jewelled key." })
//		@Name("15. Jewelled Key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int jewelledKeyMaxUses = 5;

//		@Comment({ "The maximum uses for a given spider key." })
//		@Name("16. Spider key max uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int spiderKeyMaxUses = 5;

//		@Comment({ "The maximum uses for a given wither key." })
//		@Name("17. Wither key max. uses:")
//		@RangeInt(min = 1, max = 32000)
//		@RequiresMcRestart
		public int witherKeyMaxUses = 5;		
	}
	
	public static final KeysAndLocks KEYS_LOCKS = new KeysAndLocks();
	
	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		TreasureConfig.loadConfig(TreasureConfig.COMMON_CONFIG,
				FMLPaths.CONFIGDIR.get().resolve(mod.getId() + "-common.toml"));
	}

	@SubscribeEvent
	public static void onReload(final Reloading configEvent) {
	}

	@Override
	public boolean isEnableVersionChecker() {
		return TreasureConfig.MOD.enableVersionChecker.get();
	}

	@Override
	public void setEnableVersionChecker(boolean enableVersionChecker) {
		TreasureConfig.MOD.enableVersionChecker.set(enableVersionChecker);
	}

	@Override
	public boolean isLatestVersionReminder() {
		return TreasureConfig.MOD.latestVersionReminder.get();
	}

	@Override
	public void setLatestVersionReminder(boolean latestVersionReminder) {
		TreasureConfig.MOD.latestVersionReminder.set(latestVersionReminder);
	}

	@Override
	public boolean isModEnabled() {
		return TreasureConfig.MOD.enabled.get();
	}

	@Override
	public void setModEnabled(boolean modEnabled) {
		TreasureConfig.MOD.enabled.set(modEnabled);
	}

	@Override
	public String getModsFolder() {
		return TreasureConfig.MOD.folder.get();
	}

	@Override
	public void setModsFolder(String modsFolder) {
		TreasureConfig.MOD.folder.set(modsFolder);
	}

	@Override
	public String getConfigFolder() {
		return TreasureConfig.MOD.configFolder.get();
	}

	@Override
	public void setConfigFolder(String configFolder) {
		TreasureConfig.MOD.configFolder.set(configFolder);
	}
}
