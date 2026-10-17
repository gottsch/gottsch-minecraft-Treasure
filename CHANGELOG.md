# Changelog for Treasure2 (Forge 1.12.2)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.4.0] - Unreleased

### 🛠️ Fixed
- Servers no longer fill their log with "sent a mist message for another player" warnings when several players stand in the same poison or wither mist (caused by the 2.3.3 mist fix).
- The in-game "update available" notice never worked for 1.12.2: the mod read the update file of a different Minecraft version. It now reads the 1.12.2 one.
- Each chest's own "Enable ..." config option now works. Before, most of them were tied to the moldy crate option: turning off the moldy crate stopped 16 kinds of chest from generating, and turning off any of those chests did nothing.
- Callandor could never be found: its loot entry gave Oathbringer instead. Mythical chests can now hold Callandor.

### ✨ Added
- Six new mimics: Viking chest, Cauldron chest, Crate, Moldy Crate, Cardboard Box and Milk Crate. Each can hide as its matching chest and has its own spawn egg.
- Mimics now make a quiet chattering sound now and then, so a careful listener can catch one.
- New Treasure Chest, which looks like a plain vanilla chest. It turns up among the common chests and, like them, can be a mimic: the new Chest Mimic, with its own spawn egg.
- New Amethyst Key and Lock, and Onyx Key and Lock. Both are scarce gem locks that open with their own key or a Jewelled Key. Both keys are crafted like the other gem keys, and can be found in uncommon and scarce treasure and in skull and gold skull chests.
- New config options for the lock picks' chance of opening a lock, one for each lock rarity they can open.

### ⚙️ Changed
- Mimics are no longer separate blocks. Any chest that has a mimic can turn out to be one, and a chest's mimic always matches the chest: a wood chest is always a wood mimic, a pirate chest always a pirate mimic.
- Killing a mimic drops the loot the chest would have held.
- Breaking or blowing up a mimic turns it into the mimic instead of dropping a chest.
- Mimic chances now follow the right settings for underwater chests. Before, underwater chests used the land chest settings.
- New `Enable mimics` config option to turn mimics off completely.
- `/t2-chest` has a new `-mimic` option.
- The old Wood Mimic and Pirate Mimic blocks are no longer generated or in the creative tab. Any already in a world still work.
- All mimics have new models, the Wood and Pirate mimics included, with eyes that glow in the dark. A mimic now opens up over a second when it wakes, then leans forward and chomps.
- A mimic that springs from a chest snaps its lid from the moment it appears. One hatched from a spawn egg sits with its lid shut, like a chest, until it first finds someone to attack or is used or hit.
- A mimic that is still pretending to be a chest now behaves like one: it is solid, can't be pushed, sits square, and an egg-hatched one faces whoever placed it. Its teeth no longer show through the lid.
- A mimic now faces the same way as the chest it came from, instead of turning round to face south when it appears.
- Lock picks are better at picking locks: the Pilferer's Lock Pick opens common locks 48% of the time (was 32%) and uncommon locks 32% (was 16%), and the Thief's Lock Pick opens common, uncommon and scarce locks 60%, 45% and 30% of the time (was 48%, 36% and 24%). Their tooltips show the chances from the config.
- Better filler loot in every chest:
  - Common, Uncommon and Scarce chests no longer roll Minecraft's dungeon loot, which put golden apples, enchanted golden apples and enchanted books in Common chests. Saddles, name tags and music discs now turn up from Uncommon chests, and golden apples from Rare.
  - Enchanted books scale with the chest: low-level books in Uncommon chests, up to level-30 books with treasure enchantments (Mending, Frost Walker) in Rare and better chests.
  - Legendary and Mythical chests have their own filler instead of Epic's: Nether Stars, beacons, elytra, totems, wither skeleton skulls, dragon heads, Mending books, enchanted golden apples and Treasure's extra-strong potions.
  - Diamond gear is now always enchanted at least as well as iron gear of the same rarity, and the Nether Star moved from Rare up to Epic.
  - Harmful potions (harming, poison, weakness) now come as splash potions instead of drinkable ones.
  - Common chests have far less junk, and more cooked food and coal.
  - The tool pools hold tools (pickaxes, axes, shovels, shears, fishing rods, flint and steel) instead of swords, and Epic and better chests can hold well-enchanted diamond tools.

---

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
