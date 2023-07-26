# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.3.0] - 2023-07-26

### Changed

- Updated IChestGeneator.buildLootTableList() to use the ILootTableType param instead of defaulting to CHESTS.
- Updated IChestGenerator.fillChest() to only use the rarity that was selected for the chest for the injects.

## [3.2.0] - 2023-07-16

### Added 

- Added internationalization language entries for Rarities and Key/Lock Categories
- Actually added the Crate and Moldy Crate mimics (didn't get committed last time).
- Added name for Wither Chest top block.
-Added Callandor sword.

### Changed

- updated the topaz key texture to be more like the onyx key
- update Cauldron Mimic texture to include mouth skin

## [3.1.2] - 2023-07-13

### Changed

- Updated AbstractTreasureChestBlockEntity to use the sided call to CapabilityProvider#getCapability().
  This fixes the Jade crash issue.

## [3.1.1] - 2023-07-11

### Changed

- Added names for all mobs.
- Fixed polished granite gravestone recipes.

## [3.1.0] - 2023-06-25

### Changed

- updated the topaz key texture to be more like the onyx key
- update Cauldron Mimic texture to include mouth skin

## [3.0.0] - 2023-06-24

- Initial release for mc1.18.2 port.

### Added

- Added topaz and onyx keys and locks.
- Added a series of weapons (short swords, swords, axes, maces, staff), general and collectable, to populate treasure chests in lieu of charms.
- Re-added the eye-patch (removed in mc1.16.5).
- Re-added the Wood Chest and Pirate Chest Mimics (removed in mc1.16.5)
- Added Cauldron Chest and Viking Chest Mimics.
- Added chest effects (particles and light source) for 'undiscovered' chests. The effects are removed when the chest is 'discovered' - by attempting to unlock, open or breaking the chest.
- Added custom container screen for some of the chests.
- Added new marker structure.

### Changed

- Totally refactored the mod with a set of registries to enable easy mod extensibility and integration.
- Refactored configs. Externalized chest config to a separate, non-Forge managed, toml file.
- Removed all manifest files for data files (meta, structure, loot tables).
- All data files (meta, structure, loot tables) are not longer exposed to users' system by default.
- Added datapack support. ie. loot tables and structure nbts in a datapack will be registered by Treasure2.

### Removed

- Removed all Charms and charm-related content. They are being moved to a new stand-alone mod tentatively called 'GealdorCraft', which will have built-in itegration with Treasure2.