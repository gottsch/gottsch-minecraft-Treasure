-- -----------------------------------------------------
-- Table mods
-- -----------------------------------------------------
create table if not exists mods(
	id int primary key auto_increment,
	name varchar(45) not null,
	prefix varchar(45) not null
);
create unique index if not exists mods_name_unique on mods(name asc);
create unique index if not exists mods_prefix_unique on mods(prefix asc);

-- -----------------------------------------------------
-- Table items
-- -----------------------------------------------------
create table if not exists items(
	id int primary key auto_increment,
	name varchar(45) not null,
	mc_name varchar(45) not null,
	type varchar(25) null,
	damage tinyint null default '0',
	mod_id int not null,
	foreign key (mod_id) references mods(id)
);
create unique index if not exists items_name_unique on items(name asc);

-- -----------------------------------------------------
-- Table groups
-- -----------------------------------------------------
create table if not exists groups (
	id int primary key auto_increment,
	name varchar(45) not null
);
create unique index if not exists groups_name_unique on groups(name asc);

-- -----------------------------------------------------
-- Table groups_has_items
-- -----------------------------------------------------
create table if not exists groups_has_items(
	id int primary key auto_increment,
	group_id int not null,
	item_id int not null,
	item_weight smallint not null default '0',
	min tinyint not null default '0',
	max tinyint not null default '0',
	min_enchants tinyint not null default '0',
	max_enchants tinyint not null default '0',
	ordering smallint not null default '0',
	foreign key (group_id) references groups(id),
	foreign key (item_id) references items(id)
);

-- -----------------------------------------------------
-- Table containers
-- -----------------------------------------------------
create table if not exists containers(
	id int primary key auto_increment,
	name varchar(45) not null,
	rarity varchar(15) not null default '0',
	category varchar(15) null,
	min_groups tinyint not null default '0',
	max_groups tinyint not null default '0',
	min_items tinyint not null default '0',
	max_items tinyint not null default '0'
);
create unique index if not exists container_name_unique on containers(name asc);

-- -----------------------------------------------------
-- Table containers_has_groups
-- -----------------------------------------------------
create table if not exists containers_has_groups(
	id int primary key auto_increment,
	container_id int not null,
	group_id int not null,
	group_weight smallint not null default '0',
	min_items tinyint not null default '0',
	max_items tinyint not null default '0',
	ordering smallint not null default '0',
	special boolean not null default false,
	foreign key (container_id) references containers(id),
	foreign key (group_id) references groups(id)
);

-- -----------------------------------------------------
-- Add mod data
-- -----------------------------------------------------
insert into mods (name, prefix)
values
('Vanilla Minecraft', 'minecraft'),
('Treasure2!', 'treasure2');

-- -----------------------------------------------------
-- Add item data
-- -----------------------------------------------------
insert into items (name, mc_name, type, damage, mod_id)
values
('apple', 'apple', 'item', 0, 1),
('arrow', 'arrow', 'item', 0, 1),
('beef', 'beef', 'item', 0, 1),
('beetroot', 'beetroot', 'item', 0, 1),
('beetroot_seeds', 'beetroot_seeds', 'item', 0, 1),
('blaze_powder', 'blaze_powder', 'item', 0, 1),
('blaze_rod', 'blaze_rod', 'item', 0, 1),
('bone', 'bone', 'item', 0, 1),
('book', 'book', 'item', 0, 1),
('bow', 'bow', 'item', 0, 1),
('bowl', 'bowl', 'item', 0, 1),
('bread', 'bread', 'item', 0, 1),
('brown_mushroom', 'brown_mushroom', 'item', 0, 1),
('bucket', 'bucket', 'item', 0, 1),
('cake', 'cake', 'item', 0, 1),
('carrot', 'carrot', 'item', 0, 1),
('chicken', 'chicken', 'item', 0, 1),
('chorus_fruit_popped', 'chorus_fruit_popped', 'item', 0, 1),
('clay', 'clay', 'item', 0, 1),
('clock', 'clock', 'item', 0, 1),
('coal', 'coal', 'item', 0, 1),
('cocoa', 'cocoa', 'item', 0, 1),
('compass', 'compass', 'item', 0, 1),
('cookie', 'cookie', 'item', 0, 1),
('diamond', 'diamond', 'item', 0, 1),
('diamond_arms', 'diamond_arms', 'item', 0, 1),
('diamond_axe', 'diamond_axe', 'item', 0, 1),
('diamond_block', 'diamond_block', 'item', 0, 1),
('diamond_boots', 'diamond_boots', 'item', 0, 1),
('diamond_chestplate', 'diamond_chestplate', 'item', 0, 1),
('diamond_helmet', 'diamond_helmet', 'item', 0, 1),
('diamond_horse_armor', 'diamond_horse_armor', 'item', 0, 1),
('diamond_leggings', 'diamond_leggings', 'item', 0, 1),
('diamond_pickaxe', 'diamond_pickaxe', 'item', 0, 1),
('diamond_shovel', 'diamond_shovel', 'item', 0, 1),
('diamond_sword', 'diamond_sword', 'item', 0, 1),
('dragon_breath', 'dragon_breath', 'item', 0, 1),
('dye0', 'dye', 'item', 0, 1),
('dye4', 'dye4', 'item', 4, 1),
('egg', 'egg', 'item', 0, 1),
('elytra', 'elytra', 'item', 0, 1),
('emerald', 'emerald', 'item', 0, 1),
('end_crystal', 'end_crystal', 'item', 0, 1),
('ender_eye', 'ender_pearl', 'item', 0, 1),
('ender_pearl', 'ender_pearl', 'item', 0, 1),
('experience_bottle', 'experience_bottle', 'item', 0, 1),
('fermented_spider_eye', 'fermented_spider_eye', 'item', 0, 1),
('firework_charge', 'firework_charge', 'item', 0, 1),
('fish0', 'fish', 'item', 0, 1),
('fish1', 'fish', 'item', 1, 1),
('ghast_tear', 'ghast_tear', 'item', 0, 1),
('glowstone', 'glowstone', 'item', 0, 1),
('gold_arms', 'gold_arms', 'item', 0, 1),
('gold_axe', 'gold_axe', 'item', 0, 1),
('gold_block', 'gold_block', 'item', 0, 1),
('gold_boots', 'gold_boots', 'item', 0, 1),
('gold_chestplate', 'gold_chestplate', 'item', 0, 1),
('gold_helmet', 'gold_helmet', 'item', 0, 1),
('gold_horse_armor', 'gold_horse_armor', 'item', 0, 1),
('gold_ingot', 'gold_ingot', 'item', 0, 1),
('gold_nugget', 'gold_nugget', 'item', 0, 1),
('gold_leggings', 'gold_leggings', 'item', 0, 1),
('gold_pickaxe', 'gold_pickaxe', 'item', 0, 1),
('gold_shovel', 'gold_shovel', 'item', 0, 1),
('gold_sword', 'gold_sword', 'item', 0, 1),
('golden_apple', 'golden_apple', 'item', 0, 1),
('golden_carrot', 'golden_carrot', 'item', 0, 1),
('iron_arms', 'iron_arms', 'item', 0, 1),
('iron_axe', 'iron_axe', 'item', 0, 1),
('iron_block', 'iron_block', 'item', 0, 1),
('iron_boots', 'iron_boots', 'item', 0, 1),
('iron_chestplate', 'iron_chestplate', 'item', 0, 1),
('iron_helmet', 'iron_helmet', 'item', 0, 1),
('iron_horse_armor', 'iron_horse_armor', 'item', 0, 1),
('iron_ingot', 'iron_ingot', 'item', 0, 1),
('iron_leggings', 'iron_leggings', 'item', 0, 1),
('iron_pickaxe', 'iron_pickaxe', 'item', 0, 1),
('iron_shovel', 'iron_shovel', 'item', 0, 1),
('iron_sword', 'iron_sword', 'item', 0, 1),
('lapis_ore', 'lapis_ore', 'item', 0, 1),
('lead', 'lead', 'item', 0, 1),
('leather', 'leather', 'item', 0, 1),
('leather_arms', 'leather_arms', 'item', 0, 1),
('leather_boots', 'leather_boots', 'item', 0, 1),
('leather_chestplate', 'leather_chestplate', 'item', 0, 1),
('leather_helmet', 'leather_helmet', 'item', 0, 1),
('leather_leggings', 'leather_leggings', 'item', 0, 1),
('magma_cream', 'magma_cream', 'item', 0, 1),
('melon', 'melon', 'item', 0, 1),
('melon_seeds', 'melon_seeds', 'item', 0, 1),
('mutton', 'mutton', 'item', 0, 1),
('nether_star', 'nether_star', 'item', 0, 1),
('paper', 'paper', 'item', 0, 1),
('porkchop', 'porkchop', 'item', 0, 1),
('potato', 'potato', 'item', 0, 1),
('regeneration', 'potion', 'regeneration', 0, 1),
('poison3', 'potion', 'strong_poison', 0, 1),
('poison2', 'potion', 'long_poison', 0, 1),
('fire_resistance', 'potion', 'fire_resistance', 0, 1),
('invisibility', 'potion', 'invisibility', 0, 1),
('invisibility2', 'potion', 'long_invisibility', 0, 1),
('regeneration3', 'potion', 'strong_regeneration', 0, 1),
('night_vision2', 'potion', 'long_night_vision', 0, 1),
('regeneration2', 'potion', 'long_regeneration', 0, 1),
('leaping2', 'potion', 'long_leaping', 0, 1),
('leaping3', 'potion', 'strong_leaping', 0, 1),
('poison', 'potion', 'poison', 0, 1),
('leaping', 'potion', 'leaping', 0, 1),
('healing2', 'potion', 'strong_healing', 0, 1),
('luck', 'potion', 'luck', 0, 1),
('slowness2', 'potion', 'long_slowness', 0, 1),
('strength', 'potion', 'strength', 0, 1),
('healing', 'potion', 'healing', 0, 1),
('swiftness', 'potion', 'swiftness', 0, 1),
('swiftness2', 'potion', 'long_swiftness', 0, 1),
('swiftness3', 'potion', 'strong_swiftness', 0, 1),
('night_vision', 'potion', 'night_vision', 0, 1),
('water_breathing', 'potion', 'water_breathing', 0, 1),
('slowness', 'potion', 'slowness', 0, 1),
('water_breathing2', 'potion', 'long_water_breathing', 0, 1),
('fire_resistance2', 'potion', 'long_fire_resistance', 0, 1),
('weakness', 'potion', 'weakness', 0, 1),
('harming', 'potion', 'harming', 0, 1),
('harming2', 'potion', 'strong_harming', 0, 1),
('weakness2', 'potion', 'long_weakness', 0, 1),
('strength2', 'potion', 'long_strength', 0, 1),
('strength3', 'potion', 'strong_strength', 0, 1),
('prismarine_crystals', 'prismarine_crystals', 'item', 0, 1),
('prismarine_shard', 'prismarine_shard', 'item', 0, 1),
('pumpkin', 'pumpkin', 'item', 0, 1),
('pumpkin_pie', 'pumpkin_pie', 'item', 0, 1),
('pumpkin_seeds', 'pumpkin_seeds', 'item', 0, 1),
('rabbit', 'rabbit', 'item', 0, 1),
('rabbit_foot', 'rabbit_foot', 'item', 0, 1),
('rabbit_hide', 'rabbit_hide', 'item', 0, 1),
('record_11', 'record_11', 'item', 0, 1),
('record_13', 'record_13', 'item', 0, 1),
('record_blocks', 'record_blocks', 'item', 0, 1),
('record_cat', 'record_cat', 'item', 0, 1),
('record_chirp', 'record_chirp', 'item', 0, 1),
('record_far', 'record_far', 'item', 0, 1),
('record_mall', 'record_mall', 'item', 0, 1),
('record_mellohi', 'record_mellohi', 'item', 0, 1),
('record_stal', 'record_stal', 'item', 0, 1),
('record_strad', 'record_strad', 'item', 0, 1),
('record_wait', 'record_wait', 'item', 0, 1),
('record_ward', 'record_ward', 'item', 0, 1),
('red_mushroom', 'red_mushroom', 'item', 0, 1),
('redstone', 'redstone', 'item', 0, 1),
('redstone_torch', 'redstone_torch', 'item', 0, 1),
('reeds', 'reeds', 'item', 0, 1),
('rotten_flesh', 'rotten_flesh', 'item', 0, 1),
('saddle', 'saddle', 'item', 0, 1),
('sea_lantern', 'sea_lantern', 'item', 0, 1),
('shield', 'shield', 'item', 0, 1),
('shulker_box', 'shulker_box', 'item', 0, 1),
('shulker_shell', 'shulker_shell', 'item', 0, 1),
('slime_ball', 'slime_ball', 'item', 0, 1),
('speckled_melon', 'speckled_melon', 'item', 0, 1),
('spectral_arrow', 'spectral_arrow', 'item', 0, 1),
('spider_eye', 'spider_eye', 'item', 0, 1),
('stone_axe', 'stone_axe', 'item', 0, 1),
('stone_pickaxe', 'stone_pickaxe', 'item', 0, 1),
('stone_sword', 'stone_sword', 'item', 0, 1),
('string', 'string', 'item', 0, 1),
('sugar', 'sugar', 'item', 0, 1),
('tnt', 'tnt', 'item', 0, 1),
('torch', 'torch', 'item', 0, 1),
('totem_of_undying', 'totem_of_undying', 'item', 0, 1),
('wheat', 'wheat', 'item', 0, 1),
('wheat_seeds', 'wheat_seeds', 'item', 0, 1),
('wool', 'wool', 'item', 0, 1),
('shears', 'shears', 'item', 0, 1),

-- treasure items
('silver_coin', 'silver_coin', 'item', 0, 2),
('gold_coin', 'gold_coin', 'item', 0, 2),
('skull_sword', 'skull_sword', 'item', 0, 2),
('eye_patch', 'eye_patch', 'item', 0, 2),

-- treasure keys
('wood_key', 'wood_key', 'key', 0, 2),
('stone_key', 'stone_key', 'key', 0, 2),
('iron_key', 'iron_key', 'key', 0, 2),
('gold_key', 'gold_key', 'key', 0, 2),
('diamond_key', 'diamond_key', 'key', 0, 2),
('emerald_key', 'emerald_key', 'key', 0, 2),
('metallurgists_key', 'metallurgists_key', 'key', 0, 2),
('skeleton_key', 'skeleton_key', 'key', 0, 2),
('pilferers_lock_pick', 'pilferers_lock_pick', 'key', 0, 2),
('thiefs_lock_pick', 'thiefs_lock_pick', 'key', 0, 2);
-- ADDITIONS GO HERE

-- -----------------------------------------------------
-- Add groups data
-- -----------------------------------------------------
insert into groups (name)
values
('common_items'),
('common_armor'),
('common_tools'),
('common_food'),
('common_potions'),
('uncommon_items'),
('uncommon_armor'),
('uncommon_tools'),
('uncommon_food'),
('uncommon_potions'),
('scarce_items'),
('scarce_armor'),
('scarce_tools'),
('scarce_food'),
('scarce_potions'),
('rare_items'),
('rare_armor'),
('rare_tools'),
('rare_food'),
('rare_potions'),
('epic_items'),
('epic_records'),
('epic_food'),
('epic_armor'),
('epic_tools'),

-- treasure keys
('common_keys'),
('uncommon_keys'),
('scarce_keys'),
('rare_keys'),
('epic_keys'),

-- treasure groups
('common_treasure'),
('uncommon_treasure'),
('scarce_treasure'),
('rare_treasure'),
('epic_treasure');

-- -----------------------------------------------------
-- Add  groups_has_items data
-- -----------------------------------------------------
insert into groups_has_items
(group_id, item_id, item_weight, min, max, min_enchants, max_enchants, ordering)
values
-- common_items
(1, 12, 80.0, 2, 5, 0, 0, 0), -- bread
(1, 172, 25.0, 1, 3, 0, 0, 1), -- wool
(1, 168, 50.0, 1, 5, 0, 0, 2), -- torch
(1, 82, 20.0, 1, 3, 0, 0, 3), -- leather
(1, 135, 20.0, 1, 3, 0, 0, 4), -- rabbit_hide
(1, 61, 20.0, 1, 3, 0, 0, 5), -- gold_nugget
(1, 75, 30.0, 1, 5, 0, 0, 6), -- iron_ingot
(1, 2, 50.0, 5, 10, 0, 0, 7), -- arrow
(1, 38, 20.0, 1, 3, 0, 0, 8), -- dye0
(1, 8, 50.0, 1, 3, 0, 0, 9), -- bone
(1, 161, 35.0, 1, 3, 0, 0, 10), -- spider_eye
(1, 152, 50.0, 1, 1, 0, 0, 11), -- rotten_flesh
(1, 165, 50.0, 1, 3, 0, 0, 12), -- string
-- common_armor
(2, 85, 20.0, 1, 1, 0, 0, 0), -- leather_chestplate
(2, 87, 20.0, 1, 1, 0, 0, 1), -- leather_leggings
(2, 83, 20.0, 1, 1, 0, 0, 2), -- leather_arms
(2, 86, 20.0, 1, 1, 0, 0, 3), -- leather_helmet
(2, 84, 20.0, 1, 1, 0, 0, 4), -- leather_boots
(2, 57, 10.0, 1, 1, 0, 0, 5), -- gold_chestplate
(2, 62, 10.0, 1, 1, 0, 0, 6), -- gold_leggings
(2, 53, 10.0, 1, 1, 0, 0, 7), -- gold_arms
(2, 58, 10.0, 1, 1, 0, 0, 8), -- gold_helmet
(2, 56, 10.0, 1, 1, 0, 0, 9), -- gold_boots
-- common_tools
(3, 164, 25.0, 1, 1, 0, 0, 0), -- stone_sword
(3, 163, 25.0, 1, 1, 0, 0, 1), -- stone_pickaxe
(3, 162, 25.0, 1, 1, 0, 0, 2), -- stone_axe
(3, 65, 15.0, 1, 1, 0, 0, 3), -- gold_sword
(3, 63, 15.0, 1, 1, 0, 0, 4), -- gold_pickaxe
(3, 54, 15.0, 1, 1, 0, 0, 5), -- gold_axe
-- common_food
(4, 1, 80.0, 1, 5, 0, 0, 0), -- apple
(4, 170, 80.0, 2, 5, 0, 0, 1), -- wheat
(4, 171, 80.0, 3, 5, 0, 0, 2), -- wheat_seeds
(4, 13, 80.0, 1, 5, 0, 0, 4), -- brown_mushroom
(4, 148, 80.0, 1, 5, 0, 0, 5), -- red_mushroom
(4, 94, 25.0, 1, 2, 0, 0, 7), -- porkchop
(4, 133, 25.0, 1, 2, 0, 0, 8), -- rabbit
(4, 40, 80.0, 1, 3, 0, 0, 9), -- egg
(4, 17, 25.0, 1, 2, 0, 0, 10), -- chicken
(4, 49, 35.0, 1, 5, 0, 0, 11), -- fish0
(4, 50, 30.0, 1, 3, 0, 0, 12), -- fish1
(4, 3, 25.0, 1, 3, 0, 0, 13), -- beef
(4, 130, 15.0, 1, 1, 0, 0, 15), -- pumpkin
(4, 132, 50.0, 1, 2, 0, 0, 16), -- pumpkin_seeds
(4, 89, 10.0, 1, 1, 0, 0, 17), -- melon
(4, 90, 10.0, 1, 5, 0, 0, 18), -- melon_seeds
(4, 91, 25.0, 1, 5, 0, 0, 19), -- mutton
-- common_potions
(5, 117, 20.0, 1, 2, 0, 0, 0), -- night_vision
(5, 108, 20.0, 1, 1, 0, 0, 4), -- leaping
-- uncommon_items
(6, 160, 15.0, 2, 6, 0, 0, 0), -- spectral_arrow
(6, 75, 20.0, 2, 7, 0, 0, 1), -- iron_ingot
(6, 93, 25.0, 1, 2, 0, 0, 2), -- paper
(6, 151, 50.0, 1, 2, 0, 0, 3), -- reeds
(6, 9, 25.0, 1, 2, 0, 0, 4), -- book
(6, 11, 30.0, 1, 1, 0, 0, 5), -- bowl
(6, 149, 30.0, 1, 2, 0, 0, 6), -- redstone
(6, 150, 40.0, 1, 2, 0, 0, 7), -- redstone_torch
(6, 21, 50.0, 1, 5, 0, 0, 8), -- coal
(6, 10, 25.0, 1, 1, 0, 0, 9), -- bow
(6, 19, 15.0, 1, 3, 0, 0, 10), -- clay
(6, 14, 50.0, 1, 1, 0, 0, 11), -- bucket
(6, 158, 35.0, 1, 3, 0, 0, 12), -- slime_ball
(6, 81, 35.0, 1, 3, 0, 0, 13), -- lead
(6, 47, 25.0, 1, 3, 0, 0, 14), -- fermented_spider_eye
-- uncommon_armor
(7, 72, 12.5, 1, 1, 0, 0, 0), -- iron_chestplate
(7, 76, 20.0, 1, 1, 0, 0, 1), -- iron_leggings
(7, 71, 20.0, 1, 1, 0, 0, 2), -- iron_boots
(7, 73, 20.0, 1, 1, 0, 0, 3), -- iron_helmet
(7, 155, 20.0, 1, 1, 0, 0, 4), -- shield
(7, 57, 10.0, 1, 1, 1, 2, 5), -- gold_chestplate
(7, 62, 10.0, 1, 1, 1, 2, 6), -- gold_leggings
(7, 53, 10.0, 1, 1, 1, 2, 7), -- gold_arms
(7, 58, 10.0, 1, 1, 1, 2, 8), -- gold_helmet
(7, 56, 10.0, 1, 1, 1, 2, 9), -- gold_boots
-- uncommon_tools
(8, 79, 35.0, 1, 1, 0, 0, 0), -- iron_sword
(8, 77, 25.0, 1, 1, 0, 0, 1), -- iron_pickaxe
(8, 69, 25.0, 1, 1, 0, 0, 2), -- iron_axe
(8, 78, 25.0, 1, 1, 0, 0, 3), -- iron_shovel
(8, 53, 25.0, 1, 1, 1, 2, 4), -- gold_sword
(8, 63, 15.0, 1, 1, 1, 2, 5), -- gold_pickaxe
(8, 54, 15.0, 1, 1, 1, 2, 6), -- gold_axe
-- uncommon food
(9, 166, 20.0, 1, 3, 0, 0, 3), -- sugar
(9, 16, 35.0, 1, 5, 0, 0, 6), -- carrot
(9, 22, 10.0, 1, 2, 0, 0, 20), -- cocoa
(9, 24, 15.0, 1, 2, 0, 0, 14), -- cookie
-- uncommon_potions
(10, 100, 20.0, 1, 1, 0, 0, 1), -- invisibility
(10, 112, 20.0, 1, 1, 0, 0, 2), -- strength
(10, 122, 20.0, 1, 1, 0, 0, 3), -- weakness
(10, 114, 60.0, 1, 1, 0, 0, 5), -- swiftness
(10, 118, 60.0, 1, 1, 0, 0, 6), -- water_breathing

-- scarce_items
(11, 2, 50.0, 10, 20, 0, 0, 0), -- arrow
(11, 167, 35.0, 1, 2, 0, 0, 1), -- tnt
(11, 6, 25.0, 1, 4, 0, 0, 2), -- blaze_powder
(11, 25, 15.0, 1, 2, 0, 0, 3), -- diamond
(11, 75, 20.0, 8, 12, 0, 0, 4), -- iron_ingot
(11, 70, 15.0, 1, 2, 0, 0, 5), -- iron_block
(11, 45, 15.0, 1, 1, 0, 0, 6), -- ender_pearl
--scarce_armor
(12, 85, 15.0, 1, 1, 1, 2, 0), -- leather_chestplate
(12, 87, 15.0, 1, 1, 1, 2, 1), -- leather_leggings
(12, 83, 15.0, 1, 1, 1, 2, 2), -- leather_arms
(12, 86, 15.0, 1, 1, 1, 2, 3), -- leather_helmet
(12, 84, 15.0, 1, 1, 1, 2, 4), -- leather_boots
(12, 72, 20.0, 1, 1, 1, 1, 5), -- iron_chestplate
(12, 76, 20.0, 1, 1, 1, 1, 6), -- iron_leggings
(12, 71, 20.0, 1, 1, 1, 1, 7), -- iron_boots
(12, 73, 20.0, 1, 1, 1, 1, 8), -- iron_helmet
--scarce_tools
(13, 79, 35.0, 1, 1, 1, 1, 0), -- iron_sword
(13, 10, 25.0, 1, 1, 1, 3, 0), -- bow
(13, 77, 25.0, 1, 1, 1, 1, 1), -- iron_pickaxe
(13, 69, 25.0, 1, 1, 1, 1, 2), -- iron_axe
(13, 78, 25.0, 1, 1, 1, 1, 3), -- iron_shovel
(13, 34, 15.0, 1, 1, 0, 0, 4), -- diamond_pickaxe
(13, 36, 10.0, 1, 1, 0, 0, 5), -- diamond_sword
-- scarce_food
(14, 15, 50.0, 1, 1, 0, 0, 1), -- cake
(14, 131, 50.0, 1, 1, 0, 0, 2), -- pumpkin_pie
-- scarce_potions
(15, 103, 20.0, 1, 2, 0, 0, 0), -- night_vision2
(15, 101, 20.0, 1, 2, 0, 0, 1), -- invisibility2
(15, 126, 20.0, 1, 2, 0, 0, 2), -- strength2
(15, 125, 20.0, 1, 2, 0, 0, 3), -- weakness2
(15, 105, 20.0, 1, 2, 0, 0, 4), -- leaping2
-- rare_items
(16, 167, 35.0, 2, 4, 0, 0, 0), -- tnt
(16, 6, 25.0, 2, 5, 0, 0, 1), -- blaze_powder
(16, 88, 25.0, 2, 5, 0, 0, 2), -- magma_cream
(16, 20, 25.0, 1, 1, 0, 0, 3), -- clock
(16, 23, 25.0, 1, 1, 0, 0, 4), -- compass
(16, 28, 10.0, 1, 1, 0, 0, 5), -- diamond_block
(16, 52, 25.0, 1, 5, 0, 0, 6), -- glowstone
(16, 45, 35.0, 1, 3, 0, 0, 7), -- ender_pearl
(16, 44, 35.0, 1, 3, 0, 0, 8), -- ender_eye
(16, 7, 25.0, 1, 3, 0, 0, 9), -- blaze_rod
(16, 51, 25.0, 1, 2, 0, 0, 10), -- ghast_tear
(16, 46, 50.0, 1, 3, 0, 0, 11), -- experience_bottle
(16, 25, 25.0, 1, 3, 0, 0, 12), -- diamond
(16, 128, 20.0, 1, 3, 0, 0, 13), -- prismarine_crystals
(16, 129, 20.0, 1, 3, 0, 0, 14), -- prismarine_shard
(16, 154, 20.0, 1, 1, 0, 0, 15), -- sea_lantern
(16, 92, 15.0, 1, 1, 0, 0, 16), -- nether_star
(16, 48, 35.0, 1, 5, 0, 0, 17), -- firework_charge
-- rare_armor
(17, 30, 10.0, 1, 1, 0, 0, 0), -- diamond_chestplate
(17, 33, 10.0, 1, 1, 0, 0, 1), -- diamond_leggings
(17, 31, 10.0, 1, 1, 0, 0, 2), -- diamond_helmet
(17, 29, 10.0, 1, 1, 0, 0, 3), -- diamond_boots
(17, 72, 15.0, 1, 1, 1, 2, 4), -- iron_chestplate
(17, 76, 15.0, 1, 1, 1, 2, 5), -- iron_leggings
(17, 73, 20.0, 1, 1, 1, 2, 6), -- iron_helmet
(17, 71, 20.0, 1, 1, 1, 2, 7), -- iron_boots
(17, 74, 10.0, 1, 1, 0, 0, 8), -- iron_horse_armor
(17, 59, 10.0, 1, 1, 0, 0, 9), -- gold_horse_armor
-- rare_tools
(18, 34, 50.0, 1, 1, 0, 0, 0), -- diamond_pickaxe
(18, 36, 50.0, 1, 1, 0, 0, 1), -- diamond_sword
(18, 79, 35.0, 1, 1, 1, 2, 2), -- iron_sword
-- rare_food
(19, 134, 25.0, 1, 1, 0, 0, 0), -- rabbit_foot
(19, 95, 35.0, 1, 3, 0, 0, 1), -- potato
(19, 159, 15.0, 1, 1, 0, 0, 4), -- speckled_melon
(19, 4, 50.0, 1, 5, 0, 0, 5), -- beetroot
(19, 5, 50.0, 1, 3, 0, 0, 6), -- beetroot_seeds
-- rare_potions
(20, 106, 20.0, 1, 2, 0, 0, 0), -- leaping3
(20, 116, 20.0, 1, 2, 0, 0, 1), -- swiftness3
(20, 121, 20.0, 1, 2, 0, 0, 2), -- fire_resistance2
(20, 109, 20.0, 1, 2, 0, 0, 3), -- healing2
(20, 124, 20.0, 1, 2, 0, 0, 4), -- harming2
(20, 102, 10.0, 1, 2, 0, 0, 5), -- regeneration3
(20, 127, 20.0, 1, 2, 0, 0, 6), -- strength3
(20, 110, 20.0, 1, 2, 0, 0, 7), -- luck
-- epic_items
(21, 156, 15.0, 1, 1, 0, 0, 0), -- shulker_box
(21, 157, 20.0, 1, 1, 0, 0, 1), -- shulker_shell
(21, 37, 15.0, 1, 1, 0, 0, 2), -- dragon_breath
(21, 41, 10.0, 1, 1, 0, 0, 3), -- elytra
(21, 169, 10.0, 1, 1, 0, 0, 4), -- totem_of_undying
(21, 46, 50.0, 1, 3, 0, 0, 5), -- experience_bottle
(21, 42, 50.0, 1, 3, 0, 0, 6), -- emerald
(21, 43, 25.0, 1, 2, 0, 0, 7), -- end_crystal
(21, 18, 25.0, 1, 3, 0, 0, 8), -- chorus_fruit_popped
-- epic_records
(22, 137, 10.0, 1, 1, 0, 0, 0), -- record_13
(22, 139, 10.0, 1, 1, 0, 0, 1), -- record_cat
(22, 138, 10.0, 1, 1, 0, 0, 2), -- record_blocks
(22, 140, 10.0, 1, 1, 0, 0, 3), -- record_chirp
(22, 141, 10.0, 1, 1, 0, 0, 4), -- record_far
(22, 142, 10.0, 1, 1, 0, 0, 5), -- record_mall
(22, 143, 10.0, 1, 1, 0, 0, 6), -- record_mellohi
(22, 144, 10.0, 1, 1, 0, 0, 7), -- record_stal
(22, 145, 10.0, 1, 1, 0, 0, 8), -- record_strad
(22, 147, 10.0, 1, 1, 0, 0, 9), -- record_ward
(22, 136, 10.0, 1, 1, 0, 0, 10), -- record_11
(22, 146, 10.0, 1, 1, 0, 0, 11), -- record_wait
-- epic_food
(23, 66, 35.0, 1, 2, 0, 0, 0), -- golden_apple
(23, 67, 35.0, 1, 2, 0, 0, 1), -- golden_carrot
-- epic_armor
(24, 30, 18.0, 1, 1, 1, 3, 0), -- diamond_chestplate
(24, 33, 22.0, 1, 1, 1, 3, 1), -- diamond_leggings
(24, 31, 22.0, 1, 1, 1, 3, 2), -- diamond_helmet
(24, 29, 22.0, 1, 1, 1, 3, 3), -- diamond_boots
(24, 72, 35.0, 1, 1, 2, 3, 4), -- iron_chestplate
(24, 76, 35.0, 1, 1, 2, 3, 5), -- iron_leggings
(24, 73, 35.0, 1, 1, 2, 3, 6), -- iron_helmet
(24, 71, 35.0, 1, 1, 2, 3, 7), -- iron_boots
(24, 32, 25.0, 1, 1, 0, 0, 8), -- diamond_horse_armor
-- epic_tools
(25, 36, 50.0, 1, 1, 1, 2, 0), -- diamond_sword
(25, 79, 50.0, 1, 1, 2, 3, 1), -- iron_sword

-- common_keys
(26, 174, 25.0, 1, 2, 0, 0, 1), -- wood_key
(26, 175, 25.0, 1, 2, 0, 0, 2), -- stone_key
(26, 186, 20.0, 1, 2, 0, 0, 3), -- pilferers lock pick;
(26, 176, 10.0, 1, 1, 0, 0, 4), -- iron_key
(26, 187, 10.0, 1, 1, 0, 0, 5), -- thiefs lock pick;

-- uncommon_keys (locks: iron, )
(26, 178, 15.0, 1, 2, 0, 0, 1), -- wood_key
(26, 179, 15.0, 1, 2, 0, 0, 2), -- stone_key
(26, 186, 10.0, 1, 2, 0, 0, 3), -- pilferers lock pick;
-- ...
(27, 180, 25.0, 1, 2, 0, 0, 1), -- iron_key
(27, 187, 20.0, 1, 2, 0, 0, 2), -- thiefs lock pick;
-- ..
(27, 177, 10.0, 1, 1, 0, 0, 1), -- gold_key
--(27, xxx, 10.0, 1, 1, 0, 0, 1) -- spider_key

-- scarce_keys (locks: gold, spider, )
(28, 180, 15.0, 1, 2, 0, 0, 1), -- iron_key
(28, 187, 10.0, 1, 2, 0, 0, 2), -- thiefs lock pick;
-- ..
(28, 181, 25.0, 1, 2, 0, 0, 1), -- gold_key
--(28, xxx, 20.0, 1, 1, 0, 0, 1) -- spider_key
-- ..
(28, 182, 10.0, 1, 1, 0, 0, 1), -- diamond_key

-- rare_keys
(29, 182, 25.0, 1, 2, 0, 0, 1), -- diamond_key

-- epic_keys
(30, 183, 25.0, 1, 2, 0, 0, 1), -- emerald_key

-- common_treasure
(31, 174, 25.0, 1, 1, 0, 0, 1), -- silver coin

-- uncommon_treasure
(32, 174, 25.0, 1, 2, 0, 0, 1), -- silver coin
(32, 175, 15.0, 1, 1, 0, 0, 2), -- gold coin

-- scarce_treasure
(33, 174, 25.0, 1, 4, 0, 0, 1), -- silver coin,
(33, 175, 20.0, 1, 2, 0, 0, 2), -- gold coin

-- rare_treasure
(34, 174, 25.0, 2, 6, 0, 0, 1), -- silver coin,
(34, 175, 25.0, 1, 3, 0, 0, 2), -- gold coin

-- epic_treasure
(35, 174, 20.0, 3, 10, 0, 0, 1), -- silver coin
(35, 175, 25.0, 2, 5, 0, 0, 2) -- gold coin
;

-- -----------------------------------------------------
-- Add containers data
-- -----------------------------------------------------
insert into containers (name, rarity, category, min_groups, max_groups, min_items, max_items)
values
('common_general', 'COMMON', 'BASIC', 1, 2, 5, 8),
('common_potion_food', 'COMMON', 'BASIC', 1, 2, 5, 8),
('common_armor_tools', 'COMMON', 'BASIC', 1, 2, 5, 8),
('uncommon_chest', 'UNCOMMON', 'BASIC', 2, 5, 5, 10),
('uncommon_chest2', 'UNCOMMON', 'BASIC', 2, 5, 5, 10),
('uncommon_chest3', 'UNCOMMON', 'BASIC', 2, 5, 5, 10),
('scarce_chest', 'SCARCE', 'BASIC', 3, 5, 7, 15),
('scarce_chest2', 'SCARCE', 'BASIC', 3, 5, 7, 15),
('scarce_chest3', 'SCARCE', 'BASIC', 3, 5, 7, 15),
('rare_chest', 'RARE', 'BASIC', 5, 8, 15, 25),
('epic_chest', 'EPIC', 'BASIC', 7, 10, 27, 27),

('wither_chest', 'SCARCE', 'WITHER', 1, 3, 10, 20);

-- -----------------------------------------------------
-- Add containers_has_groups data
-- -----------------------------------------------------
insert into containers_has_groups (container_id, group_id, group_weight, min_items, max_items, ordering)
values
-- general chest
(1, 1, 25, 1, 5, 0),--common_chest: common_items
(1, 2, 20, 1, 2, 1),--common_chest: common_armor
(1, 4, 20, 1, 3, 2),--common_chest: common_food
(1, 3, 20, 1, 2, 3),--common_chest: common_tools
(1, 5, 15, 1, 1, 4),--common_chest: common_potions
-- TODO need to add keys group or keys items with heaviest weight
-- TODO need to add treasure! group or items with heaviest weight ex. paintings, swords, locks, picks, books, etc.

-- potion / food chest
(2, 1, 20, 1, 4, 2),--common_chest2: common_items
(2, 4, 50, 2, 5, 0),--common_chest2: common_food
(2, 5, 30, 2, 5, 1),--common_chest2: common_potions

-- armor / tool chest
(3, 1, 20, 3, 5, 2),--common_chest3: common_items
(3, 2, 50, 2, 4, 0),--common_chest3: common_armor
(3, 3, 30, 1, 2, 1),--common_chest3: common_tools

-- general
(4, 6, 25, 2, 4, 0),--uncommon_chest: uncommon_items
(4, 8, 25, 1, 1, 1),--uncommon_chest: uncommon_tools
(4, 7, 20, 1, 2, 2),--uncommon_chest: uncommon_armor
(4, 9, 15, 1, 2, 2),--uncommon_chest: uncommon_food
(4, 10, 15, 1, 2, 3),--uncommon_chest: uncommon_potions
(4, 2, 10, 1, 2, 4),--uncommon_chest: common_armor
(4, 3, 10, 1, 2, 5),--uncommon_chest: common_tools
(4, 1, 10, 3, 5, 6),--uncommon_chest: common_items
(4, 4, 5, 1, 3, 7),--uncommon_chest: common_food
(4, 5, 5, 1, 2, 8),--uncommon_chest: common_potions

-- potion/fod
(5, 6, 20, 2, 5, 0),--uncommon_chest2: uncommon_items
(5, 8, 20, 1, 1, 1),--uncommon_chest2: uncommon_tools
(5, 7, 20, 1, 2, 2),--uncommon_chest2: uncommon_armor
(5, 9, 30, 1, 2, 2),--uncommon_chest: uncommon_food
(5, 10, 50, 1, 2, 3),--uncommon_chest2: uncommon_potions
(5, 2, 10, 1, 2, 4),--uncommon_chest2: common_armor
(5, 3, 10, 1, 2, 5),--uncommon_chest2: common_tools
(5, 1, 10, 3, 5, 6),--uncommon_chest2: common_items
(5, 4, 5, 1, 3, 7),--uncommon_chest2: common_food
(5, 5, 5, 1, 2, 8),--uncommon_chest2: common_potions

-- armor/tool
(6, 6, 20, 3, 5, 0),--uncommon_chest3: uncommon_items
(6, 8, 30, 1, 1, 1),--uncommon_chest3: uncommon_tools
(6, 7, 50, 1, 2, 2),--uncommon_chest3: uncommon_armor
(6, 9, 15, 1, 2, 2),--uncommon_chest: uncommon_food
(6, 10, 15, 1, 2, 3),--uncommon_chest3: uncommon_potions
(6, 2, 10, 1, 2, 4),--uncommon_chest3: common_armor
(6, 3, 10, 1, 2, 5),--uncommon_chest3: common_tools
(6, 1, 10, 3, 5, 6),--uncommon_chest3: common_items
(6, 4, 5, 1, 3, 7),--uncommon_chest3: common_food
(6, 5, 5, 1, 2, 8),--uncommon_chest3: common_potions

-- scarce_chest: items/armor/tools
(7, 11, 20, 2, 6, 0),--scarce_chest: scarce_items
(7, 12, 10, 1, 2, 1),--scarce_chest: scarce_armor
(7, 13, 10, 1, 1, 3),--scarce_chest: scarce_tools
(7, 6, 20, 2, 5, 5),--scarce_chest: uncommon_items
(7, 7, 10, 1, 1, 6),--scarce_chest: uncommon_armor
(7, 8, 10, 1, 2, 7),--scarce_chest: uncommon_tools
(7, 1, 20, 3, 5, 9),--scarce_chest: common_items

-- scarce_chest2: food/potions/items
(8, 11, 20, 2, 6, 0),--scarce_chest: scarce_items
(8, 14, 20, 2, 4, 2),--scarce_chest: scarce_food
(8, 15, 20, 1, 1, 4),--scarce_chest: scarce_potions
(8, 6, 20, 2, 3, 5),--scarce_chest: uncommon_items
(8, 9, 20, 1, 2, 8),--scarce_chest: uncommon_potions
(8, 1, 20, 1, 2, 9),--scarce_chest: common_items
(8, 5, 20, 1, 2, 10),--scarce_chest: common_potions

-- scarce_chest3: armor/tools
(9, 16, 10, 2, 6, 0),--rare_items
(9, 11, 20, 2, 6, 0),--scarce_chest: scarce_items
(9, 11, 20, 2, 6, 0),--scarce_chest: scarce_items
(9, 12, 20, 1, 2, 1),--scarce_chest: scarce_armor
(9, 12, 20, 1, 2, 1),--scarce_chest: scarce_armor

-- rare_chest
(10, 16, 35, 2, 6, 0),--rare_chest: rare_items
(10, 17, 30, 1, 2, 1),--rare_chest: rare_armor
(10, 19, 20, 1, 3, 2),--rare_chest: rare_food
(10, 18, 20, 1, 1, 3),--rare_chest: rare_tools
(10, 20, 15, 1, 1, 4),--rare_chest: rare_potions
(10, 6, 10, 1, 3, 5),--rare_chest: uncommon_items
(10, 7, 10, 1, 1, 6),--rare_chest: uncommon_armor
(10, 8, 10, 1, 2, 7),--rare_chest: uncommon_tools
(10, 10, 5, 1, 2, 8),--rare_chest: uncommon_potions
(10, 1, 5, 1, 2, 9),--rare_chest: common_items
(10, 5, 2, 1, 2, 10),--rare_chest: common_potions

(11, 21, 50, 3, 5, 0),--epic_chest: epic_items
(11, 23, 40, 2, 2, 1),--epic_chest: epic_food
(11, 24, 50, 2, 3, 2),--epic_chest: epic_armor
(11, 25, 20, 1, 1, 3),--epic_chest: epic_tools
(11, 22, 10, 1, 1, 4),--epic_chest: epic_records
(11, 16, 10, 1, 3, 5),--epic_chest: rare_items
(11, 17, 10, 1, 2, 8),--epic_chest: rare_armor
(11, 19, 10, 1, 2, 9),--epic_chest: rare_food
(11, 18, 10, 1, 1, 10),--epic_chest: rare_tools
(11, 20, 10, 1, 3, 11),--epic_chest: rare_potions
(11, 9, 5, 1, 2, 12); --epic_chest: uncommon_potions

-- -----------------------------------------------------
-- Add special containers_has_groups data
-- -----------------------------------------------------
insert into containers_has_groups (container_id, group_id, group_weight, min_items, max_items, ordering, special)
values
-- general chest
(1, 26, 25.0, 1, 2, 0, true),--common_chest: common_keys