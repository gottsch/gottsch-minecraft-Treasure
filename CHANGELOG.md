# Changelog for Treasure2 1.19.4

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.5.1] - 2023-09-08

### Changed

- Fixed PlayerEventHandler when checking for wishables - check that the item has an owner

## [3.5.0] - 2023-09-05

### Changed

- Fixed built-in structure registration.
- Update structure datapack registering to handle multiple datapacks (see loot tables)
- Update when selecting loot table/structure list based on category/type to merge built-in and datapacks, replacing built-ins with datapack elements when overlapped.

## [3.4.0] - 2023-09-03

### Changed
- Fixed ChestRegistry registering chests by Rarity.
- Added Cauldron Chest to chest rarity tag.
- Fixed Emerald Key registration in CommonSetup.
- Changed Lock Item rarity tooltip color to BLUE to be the same as Key Item.
- Changed Treasure Chest Block Item rarity tooltip color to BLUE to be the same as Key & Lock Item.

## [3.3.0] - 2023-07-26

### Changed

- Moved registry of structure templates to TreasureTemplateRegistry.onWorldEvent()
- Updated IChestGeneator.buildLootTableList() to use the ILootTableType param instead of defaulting to CHESTS.
- Updated IChestGenerator.fillChest() to only use the rarity that was selected for the chest for the injects.

## [3.2.0] - 2023-07-16

### Added 

- Port from 1.19.3-3.2.1