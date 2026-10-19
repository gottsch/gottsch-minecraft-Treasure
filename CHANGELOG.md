# Changelog for Treasure2 1.20.1

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.2.0] - 2026-10-19

### Added
- **The Wither Grove has been rebuilt.** The great wither tree now stands at the heart of a whole grove: 10 to 20 withered trees spread over a ragged clearing around it, tallest near the centre and smaller toward the edge. Every grove is different. The trees are grown fresh for each grove rather than copied from a handful of fixed shapes, and they keep the familiar witherwood look: straight trunks, thin branches spiralling up them, twigs, hanging moss and a broken top.
- **Groves have their own biome, the Wither Grove.** It is painted over the grove as it generates, so the surrounding forest's trees, flowers and grass no longer grow up through it. The sky, fog, water and foliage turn grey and sickly, ash drifts in the air, and only the grove's own creatures spawn there: wither skeletons, strays, skeletons, spiders and the occasional Witherwood Golem.
- **The grove floor is blighted.** Each tree stands in a patch of dead ground: Withered Grass, coarse dirt, podzol, mud and mycelium, with Withered Leaf Litter scattered over it. Roots turn the dirt beneath them into rooted dirt. Mossy and deepslate boulders lie half-sunk between the trees, along with fallen hollow logs, some beside the stump they broke from.
- **Strangle vines wrap the trunks.** About half the trees have a strangle vine winding up the trunk, and some are choked by vines at the base. Strangle Vines also creep over the ground round the trees, lie along fallen logs, and cover and climb the boulders.
- **More to find among the trees:** bracket fungus on the trunks, cobwebs in the forks of branches, soul lanterns hanging on chains, Grave Thorns around the clearings (thicker toward the edge), and, at night, faint drifting Wisps between the trees.
- **Suspicious gravel near some trunks.** Brush it for coins, gems, a Wither Key, bones and other oddments.
- **Roots hang from the pit and its rooms** beneath the great wither tree, wherever there is earth, wood or mud brick overhead, and they come out differently in every grove. Other dungeons can use the same `treasure2:hanging_roots_processor` in their processor lists.
- New blocks:
  - **Strangle Vines**: a tangle of vine that clings flat to any face of a block, like glow lichen. It catches at anything pushing through it, slowing and scratching it, but leaves the grove's monsters alone. Only shears take it.
  - **Strangled Witherwood Log**: a witherwood log wrapped in strangle vine. Stack them, each turned a face further round, and the vine spirals up the column. It drops a plain log unless broken with Silk Touch, and an axe strips the vine off with the bark.
  - **Hollow Witherwood Log**: a rotted-out log, lying or standing. Small creatures can pass through it.
  - **Bracket Fungus**: one to three shelves on the side of a log; place more on it to add shelves.
  - **Grave Thorns**: a dead bramble that slows and scratches anything walking through it, except the monsters that live among it. Only shears gather it whole.
  - **Withered Grass Block**: dead grass that never spreads. It drops dirt unless broken with Silk Touch, and can be made into a path or tilled.
  - **Withered Leaf Litter**: fallen dead leaves, one to four to a block, that lie on any solid surface.
  - **Wisp**: an invisible glow that only shows itself at night. It's placed by the grove; there is no item.
- Recipes:
  - **Hollow Witherwood Log** ×8: eight witherwood logs in a ring, with the middle left empty.
  - **Strangled Witherwood Log**: a witherwood log and Strangle Vines.
  - A hollow log makes 2 witherwood planks, and a strangled log makes 4, like any witherwood log.
- Bracket Fungus burns in a furnace, a little longer than a stick. The grove's other new blocks can't be crafted; they're found there.
- Witherwood logs, wood and broken tops can now be mossy, and the grove grows moss where it would grow on a real tree: low on the trunk. The mossy bark texture has been redrawn so the moss can actually be seen.
- Japanese names for the new blocks and the Wither Grove biome.

### Changed
- The tall strangle vine plant is now called **Blightstalk**, so that the name Strangle Vines can go to the new clinging vine. It is the same plant, and nothing changes in existing worlds.
- Wither Groves no longer generate in rivers, lakes or the sea, or with their trees standing in water.
- Wither Groves no longer generate on top of villages.
- Grove trees no longer hang in the air or sink into the ground on slopes.
- In existing worlds, witherwood logs that showed moss at random now show plain bark. Moss is now part of the block rather than chosen by its position, and old logs load as unmossed.
- New commands for building and testing groves: `/t2 wither_tree <pos> [wither|great] [seed] [scale]` grows a single wither tree, and `/t2 wither_grove <pos>` places a whole grove.
- **Chest loot overhaul**, the same changes as Treasure2 2.4.0 for 1.12.2:
  - **Legendary and Mythical chests have filler of their own.** They used to share Epic's items, food and potions, with Rare as the lesser tier. Now they hold Mending and other high-level enchanted books, nether stars, beacons, elytra, totems, wither skeleton skulls, dragon heads, diamond and emerald blocks, end crystals and shulker boxes, as well as enchanted golden apples and Treasure2's strongest potions. Their lesser tier is now the tier just below: Epic for Legendary, Legendary for Mythical.
  - **Enchanted books at every tier from Uncommon up**, stronger the rarer the chest. Uncommon chests can also hold saddles, name tags and music discs.
  - **Tools are tools.** Swords have left the tools pools (they're still among the weapons) and shovels, shears, fishing rods and flint and steel have joined them. Epic and better chests now find enchanted diamond and netherite tools.
  - Common chests have less junk: fewer bones, rotten flesh, spider eyes and string, less raw food, and some cooked meat and baked potatoes.
  - Golden apples now start at Rare. Enchanted golden apples are found only in Legendary and Mythical chests.
  - **More valuables in every chest.** About two in five item rolls now turn up ores, ingots, gems or their blocks, rising with the chest's rarity: coal, raw ores and lapis in Common chests; diamonds, emeralds, gold blocks and netherite in Epic and better. A Legendary chest now holds about 10 diamonds' worth on average, and a Mythical one about 19.
  - **Fuller chests.** Every chest rolls more items, and Rare chests now roll as many as Epic ones. Chests average about 22 stacks, up from about 20.
- The Treasure2 level III potions (healing, strength, swiftness, leaping, regeneration and splash poison) are back. The Epic chest's loot called for them, but they were never registered in 1.20.1, so each one came out as an Uncraftable Potion with no effect.

## [4.1.0] - 2026-10-18

### Added
- **Other mods can now put real Treasure2 chests in their own structures.** Until now the only way was to build a Treasure2 chest block into the structure file itself and run Treasure2's chest processor over it — which makes Treasure2 a hard requirement, and leaves that structure broken for anyone playing without it. A mod can now keep a marker of its own and ask Treasure2 for a chest while its structure is being built, so Treasure2 can stay an optional companion instead of a dependency.
- **A chest placed that way is a real one, not an imitation.** Its rarity, which chest it is, its locks, its loot and any mimic are all chosen by Treasure2 exactly as they are for Treasure2's own structures, and it is recorded the same way, so everything that keeps track of chests knows it is there. A mod can also ask for a particular rarity — a boss room, say — or name the loot table itself, and the rest of the chest is still Treasure2's to decide. Asking for a specific rarity deliberately does not disturb the rarity balance for chests generated normally.

### Changed
- The chest processor's `loot_table` setting now does something. It could always be written into a processor list, and was quietly ignored — a pack that set it got whatever loot table the chest's rarity would have given anyway, with nothing to say the line had no effect. None of Treasure2's own files set it, so no chest generated by Treasure2 changes; a datapack that did set it will now get the loot table it asked for.
- The chest processor's `alt_loot_table` setting now works as a fallback. If no loot table can be found for a chest's rarity when it's first opened, the chest is filled from that table instead of staying sealed and empty. It was silently ignored before. Treasure2's ruins, wells, dungeons and surface chests set it: vanilla dungeon loot, or a handful of coins.
- On a server, a modified client could make poison and wither mist hit any player it liked, wherever they were. Mist now only ever affects the player actually standing in it.
- A chest could open completely empty and stay that way if its loot couldn't be generated the first time it was opened. It now stays sealed and tries again the next time it's opened, instead of being emptied for good.
- Breaking or blowing up a mimic chest no longer defuses it. The mimic springs, just as if the chest had been opened, so a locked mimic can no longer be picked up and carried off as a chest.
- Clients are no longer told which chests are mimics, so a mod or NBT viewer can't give them away.
- Key rings and pouches no longer lose their custom name, enchantments or other mods' data on the client.
- Update notices work again. Since 4.0.4 the mod had been checking the wrong update file, so no new version was ever announced in game.
- Treasure2 now loads after GottschCore, the library it depends on. It had been set to load before it.
- A mimic no longer makes its ambient sound while it is still dormant. It goes quiet until it is sprung from a chest or finds a target.
- The big teeth of crate and moldy crate mimics no longer poke up through the lid while the mimic opens.
- The Viking chest mimic's teeth no longer poke through its lid.
- Lock pick tooltips now show the chances set in the config, the same ones used when picking a lock. They showed made-up chances before, so the Pilferer's Lock Pick claimed 24% on uncommon locks when the real chance is 32%, and on a server they could show 0%.
- Callandor can now be found. Its place in the mythical loot pool handed out Oathbringer instead, so Callandor never dropped.
- Better gear is now enchanted better. Rare diamond armor came unenchanted while Rare iron armor was heavily enchanted, Epic iron armor was enchanted more than Epic diamond armor, and the Rare and Scarce diamond swords were weaker than the iron ones. Diamond now always comes enchanted at least as well as iron.
- The Epic iron sword no longer stacks three rounds of enchanting.
- The Nether Star no longer turns up in Rare chests, where it was more likely than a diamond block. It now starts at Epic.
- Harmful potions in loot are now splash potions: Weakness, Poison, Harming and Treasure2's Extra Strong Poison. As drinkable potions they only ever hurt whoever drank them.
- Common chests no longer hold a useless Mundane Potion; it's a Healing potion now. The Regeneration potion in Common chests turns up five times as often (it was 1 in 31).

## [4.0.6] - 2026-09-06

### Changed
- Fixed a dedicated server crash on startup when installed alongside Equipment Standards Overhaul (ESO). The eye patch armor material was missing its material name on the server side, which caused datapack loading to fail before the world could load.

## [4.0.5] - 2026-08-13

### Changed
- Structures and chests were noticeably rarer when playing with Biomes O' Plenty. Several BOP biomes were never registered as valid generation biomes, so any terrain they covered was quietly skipped. Bayou, Bog, Marsh, Fungal Jungle and Overgrown Greens now generate treasure like their vanilla counterparts.
- Forest and jungle wishing wells never appeared in Biomes O' Plenty forests or jungles. They now do.
- Biomes We've Gone jungles were being offered forest wishing wells instead of jungle wishing wells. They now get jungle wells.

## [4.0.4] - 2026-07-19

### Changed
- Locked chests could sometimes open up completely empty, even though they clearly had a lock on them. Loot generation is now much more reliable, so a locked chest should always have something inside.
- Chests found underwater (shipwrecks, sunken ruins) were quietly using the same rarity and loot rules as chests found on land. They now use their own underwater rules, so what you find down there should feel more distinct from land loot.

## [4.0.3] - 2025-11-11

### Changed
- Removed the decay-test structure from the structure list to prevent spawning.
- Removed all test structure related files.

## [4.0.2] - 2025-10-29

### Changed
- Fixed Wishables not adding loot items when adding custom loot tables.
- Updated license to the correct one.
- Updated update.json to the correct version.

## [4.0.1] - 2025-10-05

### Changed
- Fixed server crash caused by non-existent library.

## [4.0.0] - 2025-10-03

## This version is a breaking change! Backup your world first.

### Changed

- **All structure, pit, marker, well, witherwood grove, etc. generation uses _Jigsaw Structures_.**
- PreGen compatibility. 
- No cascading nor outside bounds errors. 
- No additional generation lag.
- Able to locate using vanilla command /locate structure
- Changed License to OSL-3.0.
- Expanded Mob Sets and Mob Set based Blocks.
- Removed Deferred generation system.
- Removed all non-standard config toml files. Uses /data json files instead.
- Removed most Config options. Uses /data json files instead.
- Made the One Key more rare.
- Fixed reflection called in Constructor error on startup.
- Fixed all asset errors on startup.
- Requires GottschCore 2.5.1+

### Added
- **Bone, Celestial, and Infernal Chests**
- Bone Key, Bone Lock (not accessible to players ie part of Bone Chest).
- Multiple new surface structures (mausoleum, ponds, statues, natural area, etc)
- Reduced and cleanup up codebase.

## [3.12.1] - 2025-04-23

### Changed
- Update Treasure Tool to only stack to 1. This may prevent the duplicate tools on crafting issue.
- Fix KeyItem.getShareTag() bug.

## [3.12.0] - 2025-03-16

### Added
- Japanese translations by okazako
- 
## [3.11.1] - 2025-01-13

### Changed
- Fixed server crash when generating Witherwood Tree groves.
- Increases the build area (height) for Witherwood Tree groves, so that the tops actually are generated.
- Updated to use Forge 47.3.0
- Removed Patchouli manual added to inventory on first join. It can be found on the Treasure2 tab.

## [3.11.0] - 2025-01-06 (Wither Tree Update)

### Changed
- Renamed all Wither block variants to Witherwood.
- Updated Witherwood textures.
- Updated loot table for Wither Tree chest.
- Wither Tree is now a 3x3 instead of 2x2, and removed the Wither Soul block.
- All Wither Tree groves use pits with structures.
- Wither Tree groves spawn Witherwood Golems and other mobs.
- Poison fog (green) and Wither fog (black) inflicts poison and wither effects again.
- Spanish moss has smalled hitbox.

### Added
- Witherwood blocks - planks, wood, stairs, slab, stripped log, stripped wood, door, trapdoor, fence, fence gate, button, pressure plate, sign, hanging sign.
- Strangle Vine block(s).
- Witherwood Twig.
- Recipes for all new witherwood blocks.
- Added Witherwood Golem.
- Added 5 new Witherwood themed pit structures.

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