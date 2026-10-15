# Changelog for Treasure2 (Forge 1.12.2)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.3.3] - 2026-10-15

### 🛠️ Fixed
- Wither chests always opened empty. Their loot table could not be read by Minecraft 1.12.2, so opening one quietly failed and left the chest empty for good. Wither chests now have loot. (#414)
- A chest whose loot fails to generate is no longer left permanently empty. It stays sealed and tries again the next time it is opened.
- Fixed a world generation crash when decorating wishing wells with flowers ("Cannot set property ... to dandelion on block minecraft:red_flower"). (#357)
- On multiplayer servers, a modified client could poison or wither any other player through the mist effects. Mist now only ever affects the player who is standing in it.
- Keys, lock picks, charms, runestones and adornments no longer lose their custom name, enchantments or other mods' data on the client.
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
