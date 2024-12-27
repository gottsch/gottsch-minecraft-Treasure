# Changelog for Treasure2 1.20.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
``
## [3.10.4] - 2024-12-20

### Changed
- Enabled the LivingHurtEvent so critical damage can be inflicted!
- Fix critical weapon damage -> changed from multiple to addition.

## [3.10.3] - 2024-08-29

### Changed
- Fix game-crashing "chestGenerator" is null bug.
- Fix Skeletons (and other mobs) spawning without weapons around Wishing Wells.

## [3.10.2] - 2024-08-28

### Added
- Oh the Biomes We've Gone support.

## [3.10.1] - 2024-08-21

### Changed
- Fixed Well generation on certain blocks causing the game to crash.

## [3.10.0] - 2024-08-21

### Changed
- Fixed milk crate entity name.
- Smoothed Mimic activating animation, removing the need to sync angle amount.
- Refactored Mimic models.
- Fixed duplicate copper and iron short sword entries in pools/combat/uncommon pool json file.
- Fixed Wishables scan for validate location. Checks entire scan radius.
- Updated Desert Wishing Well Block to use correct sandstone textures.
- Update treasure2_structures-xxx-vx toml config file.
- Wishing Wells can spawn mobs
- Mob one-time spawners can be configured via the MobSet Configuration file.
- Improved the Wishing Well decorations placements.
- Better align hitbox for Ironbound chest
- Removed shadow on Chest inventory label
- Fixed the breaking of a merged key taking too much damage.

### Added
- Four leaf Clover item - can use this item to transform blocks into Wishing Wells.
- Four leaf Clover block - rare plant that appears around Wishing Wells.
- 4+ Well designs
- Barrel Chest
- Barrel Mimic
- MimicModel abstract class
- MobSet Configuration
- Mobs patchouli entry.

## [3.9.0] - 2024-07-28

### Changed
- Fixed Deferred generator block entities using the wrong config-based conditional statement.
- Updated treasure2-chests-1.20.1 toml file to v3.

### Added
- Added Deferred generators for Surface, Subaquatic structures, and Pits.


## [3.8.5] - 2024-07-23

### Changed
- Fixed pit shaft and pit room alignments! For real this time.
- Updated crypt3.nbt to include the missing bottom level which had the entrance marker.
- Updated treasure2-structures toml file.
- Updated StructureMarkerGenerator to use mcRotate() instead of rotate()
- Updated StructurePitGenerator to use mcRotate() instead of rotate()

### Added
- Added crypt5.nbt that is like crypt3 but with a full skeleton.
- Added mcRotate() to GeometryUtil.

## [3.8.4] - 2024-07-18

### Changed

- Fixed add patchouli guide book on first use.
- Added condition to KeyItem.isDamageable() to prevent exception due to mixin use.

## [3.8.3] - 2024-06-27

### Changed

- Added the Falling blocks names to the language file.

## [3.8.2] - 2024-05-20

### Changed

- Fixed crafted Keys from only having 0 durability/usage on server.
- Fixed reading datapacks in flat/exploded directory format.
- Fixed high-level loot tables to include high-level combat items.
- KeyItem now takes a default durability value in the constructor.

## [3.8.1] - 2024-02-15

### Changed

- Fixed the inventory size of chests display in inventory.
- Increased the success probability of Pilferers and Thiefs Locks Picks to 48/60 respectively.
- Fixed the IChestGenerator.buildLootTableList not using the correct key when searching for injectable loot tables.
- Fixed Legendary and Mythical loot tables to include respective weapons.
- Fixed Cauldron Chest not displaying a rarity.
- Update mimics to have a smoother chomp animation.

### Added

- Config options for Pilferers and Thiefs Lock Picks success probabilities for different rarities.
- Patchouli book support with entries for Chests, Keys, Locks and Key Ring. Added to your inventory on first join.

## [3.7.1] - 2024-01-25

### Changed

- Fixed key usage/damage not updating client when playing on dedicated server.
- Key usage/damage isn't increment in Creative mode.

### Added

- Supports Biomes O Plenty

## [3.7.0] - 2024-01-17

### Added

- Cardboard Box mimic.

## [3.6.2] - 2023-10-23

### Changed

- Properly referenced the treasure2-structures-1.20.1-v2.toml resource which caused crashes if not found.

## [3.6.1] - 2023-10-16

### Changed

- Fixed crash when removing item from crafting table (anything requiring the Treasure Tool.)
- Data generated all recipes.

## [3.6.0] - 2023-10-16

### Changed

- Updated to use Forge 47.2.0
- Fixed generation crashes.
- Fixed structure alignments.
- Fixed treasure tab name display.
- Updated SurfaceStructureFeatureGenerator to return an Optional.
- Updated treasure2-structures-1.20.1 to version 2.
- Updated sunken-ship structures.

### Added

- Blackrock and Deepslate variations of towers.
- Deferred Random Vanilla Spawner generation classes.
- Deferred Wither Tree generation classes. 
- GeometryUtil class.

## [3.5.0] - 2023-09-05

### Changed

- Fixed built-in structure registration.
- Update structure datapack registering to handle multiple datapacks (see loot tables)
- Update when selecting loot table/structure list based on category/type to merge built-in and datapacks, replacing built-ins with datapack elements when overlapped.
- Fixed PlayerEventHandler when checking for wishables - check that the item has an owner

## [3.4.0] - 2023-09-03

### Changed
- Fixed ChestRegistry registering chests by Rarity.
- Added Cauldron Chest to chest rarity tag.
- Fixed Emerald Key registration in CommonSetup.
- Changed Lock Item rarity tooltip color to BLUE to be the same as Key Item.
- Changed Treasure Chest Block Item rarity tooltip color to BLUE to be the same as Key & Lock Item.

## [3.3.0] - 2023-07-26

- TODO - see 1.19.2

## [3.2.0] - 2023-07-16

### Added 

- Port from 1.19.3-3.2.1**