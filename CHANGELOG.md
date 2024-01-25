**# Changelog for Treasure2 1.20.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.7.1] - 2024-01-17

### Changed

- Fixed Key uses/durability not updating on client.

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