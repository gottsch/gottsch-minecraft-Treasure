# Changelog for Treasure2 (Forge 1.12.2)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.4.0] - Unreleased

### 🛠️ Fixed
- Servers no longer fill their log with "sent a mist message for another player" warnings when several players stand in the same poison or wither mist (caused by the 2.3.3 mist fix).
- The in-game "update available" notice never worked for 1.12.2: the mod read the update file of a different Minecraft version. It now reads the 1.12.2 one.

### ✨ Added
- Six new mimics: Viking chest, Cauldron chest, Crate, Moldy Crate, Cardboard Box and Milk Crate. Each can hide as its matching chest and has its own spawn egg.
- Mimics now make a quiet chattering sound now and then, so a careful listener can catch one.

### ⚙️ Changed
- Mimics are no longer separate blocks. Any chest that has a mimic can turn out to be one, and a chest's mimic always matches the chest: a wood chest is always a wood mimic, a pirate chest always a pirate mimic.
- Killing a mimic drops the loot the chest would have held.
- Breaking or blowing up a mimic turns it into the mimic instead of dropping a chest.
- Mimic chances now follow the right settings for underwater chests. Before, underwater chests used the land chest settings.
- New `Enable mimics` config option to turn mimics off completely.
- `/t2-chest` has a new `-mimic` option.
- The old Wood Mimic and Pirate Mimic blocks are no longer generated or in the creative tab. Any already in a world still work.
- A mimic that springs from a chest snaps its lid from the moment it appears. One hatched from a spawn egg sits with its lid shut, like a chest, until it first finds someone to attack.
- A mimic now faces the same way as the chest it came from, instead of turning round to face south when it appears.

---

## [2.3.3] - 2026-10-15

### 🛠️ Fixed
- Wither chests always opened empty. Their loot table could not be read by Minecraft 1.12.2, so opening one quietly failed and left the chest empty for good. Wither chests now have loot. (#414)
- A chest whose loot fails to generate is no longer left permanently empty. It stays sealed and tries again the next time it is opened.
- Fixed a world generation crash when decorating wishing wells with flowers ("Cannot set property ... to dandelion on block minecraft:red_flower"). (#357)
- On multiplayer servers, a modified client could poison or wither any other player through the mist effects. Mist now only ever affects the player who is standing in it.
- Keys, lock picks, charms, runestones and adornments no laonger lose their custom name, enchantments or other mods' data on the client.
- The White Pearl wishing well never gave its treasure items. It now does.
- The Treasure Tool now only stacks to 1, which prevents it from being duplicated when crafting.
- A chest in one dimension no longer stops chests from generating at the same coordinates in another dimension.

### ⚙️ Changed
- The mod jar is now named `treasure2-1.12.2-<version>.jar`, like the newer versions of Treasure2. Remove the old `Treasure2-mc1.12.2-...` jar when updating.
- Removed leftover duplicate loot table files that were never used.
- Modernized the build (Gradle 8 with RetroFuturaGradle). No effect on gameplay.

---

## [2.3.2] - 2024-12-20

### 🛠️ Fixed
- Fixed critical weapon damage -> changed from multiple to addition.
