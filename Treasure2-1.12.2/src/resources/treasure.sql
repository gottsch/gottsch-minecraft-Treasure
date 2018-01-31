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
	foreign key (container_id) references containers(id),
	foreign key (group_id) references groups(id)
);

-- -----------------------------------------------------
-- Add mod data
-- -----------------------------------------------------
insert into mods (name, prefix)
values
('Vanilla Minecraft', 'minecraft'),
('Treasure!', 'treasure2');

-- -----------------------------------------------------
-- Add item data
-- -----------------------------------------------------
insert into items (name, mc_name, type, damage, mod_id, )
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
('diamond_block', 'diamond_block', 'item', 0, 1),
('diamond_boots', 'diamond_boots', 'item', 0, 1),
('diamond_chestplate', 'diamond_chestplate', 'item', 0, 1),
('diamond_helmet', 'diamond_helmet', 'item', 0, 1),
('diamond_horse_armor', 'diamond_horse_armor', 'item', 0, 1),
('diamond_leggings', 'diamond_leggings', 'item', 0, 1),
('diamond_pickaxe', 'diamond_pickaxe', 'item', 0, 1),
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
('gold_horse_armor', 'gold_horse_armor', 'item', 0, 1),
('gold_nugget', 'gold_nugget', 'item', 0, 1),
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
('wool', 'wool', 'item', 0, 1)

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
('uncommon_potions'),
('rare_items'),
('rare_armor'),
('rare_tools'),
('rare_food'),
('rare_potions'),
('epic_items'),
('epic_records'),
('epic_food'),
('epic_armor'),
('epic_tools');

-- -----------------------------------------------------
-- Add  groups_has_items data
-- -----------------------------------------------------
insert into groups_has_items
(group_id, item_id, item_weight, min, max, min_enchants, max_enchants, ordering)
values
--'common_items'
(1, 12, 80.0, 2, 5, 0, 0, 0), -- bread
(1, 159, 25.0, 1, 3, 0, 0, 1), -- wool
(1, 155, 50.0, 1, 5, 0, 0, 2), -- torch
(1, 69, 20.0, 1, 3, 0, 0, 3), -- leather
(1, 122, 20.0, 1, 3, 0, 0, 4), -- rabbit_hide
(1, 52, 20.0, 1, 3, 0, 0, 5), -- gold_nugget
(1, 63, 30.0, 1, 5, 0, 0, 6), -- iron_ingot
(1, 2, 50.0, 5, 10, 0, 0, 7), -- arrow
(1, 36, 20.0, 1, 3, 0, 0, 8), -- dye0
(1, 8, 50.0, 1, 3, 0, 0, 9), -- bone
(1, 148, 35.0, 1, 3, 0, 0, 10), -- spider_eye
(1, 139, 50.0, 1, 1, 0, 0, 11), -- rotten_flesh
(1, 152, 50.0, 1, 3, 0, 0, 12), -- string
--'common_armor'
(2, 72, 20.0, 1, 1, 0, 0, 0), -- leather_chestplate
(2, 74, 20.0, 1, 1, 0, 0, 1), -- leather_leggings
(2, 70, 20.0, 1, 1, 0, 0, 2), -- leather_arms
(2, 73, 20.0, 1, 1, 0, 0, 3), -- leather_helmet
(2, 71, 20.0, 1, 1, 0, 0, 4), -- leather_boots
--'common_tools'
(3, 151, 25.0, 1, 1, 0, 0, 0), -- stone_sword
(3, 150, 25.0, 1, 1, 0, 0, 1), -- stone_pickaxe
(3, 149, 25.0, 1, 1, 0, 0, 2), -- stone_axe
--'common_food'
(4, 1, 80.0, 1, 5, 0, 0, 0), -- apple
(4, 157, 80.0, 2, 5, 0, 0, 1), -- wheat
(4, 158, 80.0, 3, 5, 0, 0, 2), -- wheat_seeds
(4, 153, 20.0, 1, 3, 0, 0, 3), -- sugar
(4, 13, 80.0, 1, 5, 0, 0, 4), -- brown_mushroom
(4, 135, 80.0, 1, 5, 0, 0, 5), -- red_mushroom
(4, 16, 35.0, 1, 5, 0, 0, 6), -- carrot
(4, 81, 25.0, 1, 2, 0, 0, 7), -- porkchop
(4, 120, 25.0, 1, 2, 0, 0, 8), -- rabbit
(4, 38, 80.0, 1, 3, 0, 0, 9), -- egg
(4, 17, 25.0, 1, 2, 0, 0, 10), -- chicken
(4, 47, 35.0, 1, 5, 0, 0, 11), -- fish0
(4, 48, 30.0, 1, 3, 0, 0, 12), -- fish1
(4, 3, 25.0, 1, 3, 0, 0, 13), -- beef
(4, 24, 15.0, 1, 2, 0, 0, 14), -- cookie
(4, 117, 15.0, 1, 1, 0, 0, 15), -- pumpkin
(4, 119, 50.0, 1, 2, 0, 0, 16), -- pumpkin_seeds
(4, 76, 10.0, 1, 1, 0, 0, 17), -- melon
(4, 77, 10.0, 1, 5, 0, 0, 18), -- melon_seeds
(4, 78, 25.0, 1, 5, 0, 0, 19), -- mutton
(4, 22, 10.0, 1, 2, 0, 0, 20), -- cocoa
--'common_potions'
(5, 104, 20.0, 1, 2, 0, 0, 0), -- night_vision
(5, 87, 20.0, 1, 1, 0, 0, 1), -- invisibility
(5, 99, 20.0, 1, 1, 0, 0, 2), -- strength
(5, 109, 20.0, 1, 1, 0, 0, 3), -- weakness
(5, 95, 20.0, 1, 1, 0, 0, 4), -- leaping
--'uncommon_items',
(6, 147, 15.0, 2, 6, 0, 0, 0), -- spectral_arrow
(6, 63, 20.0, 2, 7, 0, 0, 1), -- iron_ingot
(6, 80, 25.0, 1, 2, 0, 0, 2), -- paper
(6, 138, 50.0, 1, 2, 0, 0, 3), -- reeds
(6, 9, 25.0, 1, 2, 0, 0, 4), -- book
(6, 11, 30.0, 1, 1, 0, 0, 5), -- bowl
(6, 136, 30.0, 1, 2, 0, 0, 6), -- redstone
(6, 137, 40.0, 1, 2, 0, 0, 7), -- redstone_torch
(6, 21, 50.0, 1, 5, 0, 0, 8), -- coal
(6, 10, 25.0, 1, 1, 0, 0, 9), -- bow
(6, 19, 15.0, 1, 3, 0, 0, 10), -- clay
(6, 14, 50.0, 1, 1, 0, 0, 11), -- bucket
(6, 145, 35.0, 1, 3, 0, 0, 12), -- slime_ball
(6, 68, 35.0, 1, 3, 0, 0, 13), -- lead
(6, 45, 25.0, 1, 3, 0, 0, 14) -- fermented_spider_eye
--'uncommon_armor',
(7, 60, 12.5, 1, 1, 0, 0, 0), -- iron_chestplate
(7, 64, 20.0, 1, 1, 0, 0, 1), -- iron_leggings
(7, 59, 20.0, 1, 1, 0, 0, 2), -- iron_boots
(7, 61, 20.0, 1, 1, 0, 0, 3), -- iron_helmet
(7, 142, 20.0, 1, 1, 0, 0, 4), -- shield
--'uncommon_tools',
(8, 66, 35.0, 1, 1, 0, 0, 0), -- iron_sword
(8, 65, 25.0, 1, 1, 0, 0, 1), -- iron_pickaxe
(8, 57, 25.0, 1, 1, 0, 0, 2), -- iron_axe
(8, 53, 25.0, 1, 1, 0, 0, 3), -- gold_sword
--'uncommon_potions',
(9, 90, 20.0, 1, 2, 0, 0, 0), -- night_vision2
(9, 88, 20.0, 1, 1, 0, 0, 1), -- invisibility2
(9, 113, 20.0, 1, 1, 0, 0, 2), -- strength2
(9, 112, 20.0, 1, 1, 0, 0, 3), -- weakness2
(9, 92, 20.0, 1, 1, 0, 0, 4), -- leaping2
(9, 101, 60.0, 1, 1, 0, 0, 5), -- swiftness
(9, 105, 60.0, 1, 1, 0, 0, 6), -- water_breathing
--'rare_items',
(10, 154, 35.0, 1, 2, 0, 0, 0), -- tnt
(10, 6, 25.0, 2, 5, 0, 0, 1), -- blaze_powder
(10, 75, 25.0, 2, 5, 0, 0, 2), -- magma_cream
(10, 20, 25.0, 1, 1, 0, 0, 3), -- clock
(10, 23, 25.0, 1, 1, 0, 0, 4), -- compass
(10, 27, 10.0, 1, 1, 0, 0, 5), -- diamond_block
(10, 50, 25.0, 1, 5, 0, 0, 6), -- glowstone
(10, 43, 35.0, 1, 3, 0, 0, 7), -- ender_pearl
(10, 42, 35.0, 1, 3, 0, 0, 8), -- ender_eye
(10, 7, 25.0, 1, 3, 0, 0, 9), -- blaze_rod
(10, 49, 25.0, 1, 2, 0, 0, 10), -- ghast_tear
(10, 44, 50.0, 1, 3, 0, 0, 11), -- experience_bottle
(10, 25, 25.0, 1, 3, 0, 0, 12), -- diamond
(10, 115, 20.0, 1, 3, 0, 0, 13), -- prismarine_crystals
(10, 116, 20.0, 1, 3, 0, 0, 14), -- prismarine_shard
(10, 141, 20.0, 1, 1, 0, 0, 15), -- sea_lantern
(10, 79, 15.0, 1, 1, 0, 0, 16), -- nether_star
(10, 46, 35.0, 1, 5, 0, 0, 17), -- firework_charge
--'rare_armor',
(11, 29, 10.0, 1, 1, 0, 0, 0), -- diamond_chestplate
(11, 32, 10.0, 1, 1, 0, 0, 1), -- diamond_leggings
(11, 30, 10.0, 1, 1, 0, 0, 2), -- diamond_helmet
(11, 28, 10.0, 1, 1, 0, 0, 3), -- diamond_boots
(11, 60, 15.0, 1, 1, 1, 2, 4), -- iron_chestplate
(11, 64, 15.0, 1, 1, 1, 2, 5), -- iron_leggings
(11, 61, 20.0, 1, 1, 1, 2, 6), -- iron_helmet
(11, 59, 20.0, 1, 1, 1, 2, 7), -- iron_boots
(11, 62, 10.0, 1, 1, 0, 0, 8), -- iron_horse_armor
(11, 51, 10.0, 1, 1, 0, 0, 9), -- gold_horse_armor
--'rare_tools',
(12, 33, 50.0, 1, 1, 0, 0, 0), -- diamond_pickaxe
(12, 34, 50.0, 1, 1, 0, 0, 1), -- diamond_sword
(12, 66, 35.0, 1, 1, 1, 2, 2), -- iron_sword
--'rare_food',
(13, 121, 25.0, 1, 1, 0, 0, 0), -- rabbit_foot
(13, 82, 35.0, 1, 3, 0, 0, 1), -- potato
(13, 15, 50.0, 1, 1, 0, 0, 2), -- cake
(13, 118, 50.0, 1, 1, 0, 0, 3), -- pumpkin_pie
(13, 146, 15.0, 1, 1, 0, 0, 4), -- speckled_melon
(13, 4, 50.0, 1, 5, 0, 0, 5), -- beetroot
(13, 5, 50.0, 1, 3, 0, 0, 6), -- beetroot_seeds
--'rare_potions',
(14, 93, 20.0, 1, 2, 0, 0, 0), -- leaping3
(14, 103, 20.0, 1, 2, 0, 0, 1), -- swiftness3
(14, 108, 20.0, 1, 2, 0, 0, 2), -- fire_resistance2
(14, 96, 20.0, 1, 2, 0, 0, 3), -- healing2
(14, 111, 20.0, 1, 2, 0, 0, 4), -- harming2
(14, 89, 10.0, 1, 2, 0, 0, 5), -- regeneration3
(14, 114, 20.0, 1, 2, 0, 0, 6), -- strength3
(14, 97, 20.0, 1, 2, 0, 0, 7), -- luck
--'epic_items',
(15, 143, 15.0, 1, 1, 0, 0, 0), -- shulker_box
(15, 144, 20.0, 1, 1, 0, 0, 1), -- shulker_shell
(15, 35, 15.0, 1, 1, 0, 0, 2), -- dragon_breath
(15, 39, 10.0, 1, 1, 0, 0, 3), -- elytra
(15, 156, 10.0, 1, 1, 0, 0, 4), -- totem_of_undying
(15, 44, 50.0, 1, 3, 0, 0, 5), -- experience_bottle
(15, 40, 50.0, 1, 3, 0, 0, 6), -- emerald
(15, 41, 25.0, 1, 2, 0, 0, 7), -- end_crystal
(15, 18, 25.0, 1, 3, 0, 0, 8), -- chorus_fruit_popped
--'epic_records),
(16, 124, 10.0, 1, 1, 0, 0, 0), -- record_13
(16, 126, 10.0, 1, 1, 0, 0, 1), -- record_cat
(16, 125, 10.0, 1, 1, 0, 0, 2), -- record_blocks
(16, 127, 10.0, 1, 1, 0, 0, 3), -- record_chirp
(16, 128, 10.0, 1, 1, 0, 0, 4), -- record_far
(16, 129, 10.0, 1, 1, 0, 0, 5), -- record_mall
(16, 130, 10.0, 1, 1, 0, 0, 6), -- record_mellohi
(16, 131, 10.0, 1, 1, 0, 0, 7), -- record_stal
(16, 132, 10.0, 1, 1, 0, 0, 8), -- record_strad
(16, 134, 10.0, 1, 1, 0, 0, 9), -- record_ward
(16, 123, 10.0, 1, 1, 0, 0, 10), -- record_11
(16, 133, 10.0, 1, 1, 0, 0, 11), -- record_wait
--'epic_food',
(17, 54, 35.0, 1, 2, 0, 0, 0), -- golden_apple
(17, 55, 35.0, 1, 2, 0, 0, 1), -- golden_carrot
--'epic_armor',
(18, 29, 18.0, 1, 1, 1, 3, 0), -- diamond_chestplate
(18, 32, 22.0, 1, 1, 1, 3, 1), -- diamond_leggings
(18, 30, 22.0, 1, 1, 1, 3, 2), -- diamond_helmet
(18, 28, 22.0, 1, 1, 1, 3, 3), -- diamond_boots
(18, 60, 35.0, 1, 1, 2, 3, 4), -- iron_chestplate
(18, 64, 35.0, 1, 1, 2, 3, 5), -- iron_leggings
(18, 61, 35.0, 1, 1, 2, 3, 6), -- iron_helmet
(18, 59, 35.0, 1, 1, 2, 3, 7), -- iron_boots
(18, 31, 25.0, 1, 1, 0, 0, 8), -- diamond_horse_armor
--'epic_tools',
(19, 34, 50.0, 1, 1, 1, 2, 0), -- diamond_sword
(19, 66, 50.0, 1, 1, 2, 3, 1) -- iron_sword
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
('uncommon_chest2', 'UNCOMMO', 'BASIC', 2, 5, 5, 10),
('uncommon_chest3', 'UNCOMMON', 'BASIC', 2, 5, 5, 10),
('scarce_chest', 'UNCOMMON', 'BASIC', 3, 5, 7, 15),
('scarce_chest2', 'UNCOMMON', 'BASIC', 3, 5, 7, 15),
('scarce_chest3', 'UNCOMMON', 'BASIC', 3, 5, 7, 15),
('rare_chest', 'RARE', 'BASIC', 5, 8, 15, 25),
('epic_chest', 'EPIC', 'BASIC', 7, 10, 27, 27),

('wither_chest', 'SCARCE', 'WITHER', 1, 3, 10, 20),

-- -----------------------------------------------------
-- Add containers_has_groups data
-- -----------------------------------------------------
insert into containers_has_groups (container_id, group_id, group_weight, min_items, max_items, ordering)
values
-- general chest
(1, 1, 0, 3, 5, 0),--common_chest: common_items
(1, 2, 0, 1, 2, 1),--common_chest: common_armor
(1, 4, 0, 1, 3, 2),--common_chest: common_food
(1, 3, 0, 1, 2, 3),--common_chest: common_tools
(1, 5, 0, 1, 2, 4),--common_chest: common_potions
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

(4, 6, 0, 2, 4, 0),--uncommon_chest: uncommon_items
(4, 8, 0, 1, 1, 1),--uncommon_chest: uncommon_tools
(4, 7, 0, 1, 2, 2),--uncommon_chest: uncommon_armor
(4, 9, 0, 1, 2, 3),--uncommon_chest: uncommon_potions
(4, 2, 0, 1, 2, 4),--uncommon_chest: common_armor
(4, 3, 0, 1, 2, 5),--uncommon_chest: common_tools
(4, 1, 0, 3, 5, 6),--uncommon_chest: common_items
(4, 4, 0, 1, 3, 7),--uncommon_chest: common_food
(4, 5, 0, 1, 2, 8),--uncommon_chest: common_potions
(5, 6, 0, 2, 5, 0),--uncommon_chest2: uncommon_items
(5, 8, 0, 1, 1, 1),--uncommon_chest2: uncommon_tools
(5, 7, 0, 1, 2, 2),--uncommon_chest2: uncommon_armor
(5, 9, 0, 1, 2, 3),--uncommon_chest2: uncommon_potions
(5, 2, 0, 1, 2, 4),--uncommon_chest2: common_armor
(5, 3, 0, 1, 2, 5),--uncommon_chest2: common_tools
(5, 1, 0, 3, 5, 6),--uncommon_chest2: common_items
(5, 4, 0, 1, 3, 7),--uncommon_chest2: common_food
(5, 5, 0, 1, 2, 8),--uncommon_chest2: common_potions
(6, 6, 0, 3, 5, 0),--uncommon_chest3: uncommon_items
(6, 8, 0, 1, 1, 1),--uncommon_chest3: uncommon_tools
(6, 7, 0, 1, 2, 2),--uncommon_chest3: uncommon_armor
(6, 9, 0, 1, 2, 3),--uncommon_chest3: uncommon_potions
(6, 2, 0, 1, 2, 4),--uncommon_chest3: common_armor
(6, 3, 0, 1, 2, 5),--uncommon_chest3: common_tools
(6, 1, 0, 3, 5, 6),--uncommon_chest3: common_items
(6, 4, 0, 1, 3, 7),--uncommon_chest3: common_food
(6, 5, 0, 1, 2, 8),--uncommon_chest3: common_potions
(7, 10, 0, 2, 6, 0),--rare_chest: rare_items
(7, 11, 0, 1, 2, 1),--rare_chest: rare_armor
(7, 13, 0, 2, 4, 2),--rare_chest: rare_food
(7, 12, 0, 1, 1, 3),--rare_chest: rare_tools
(7, 14, 0, 1, 1, 4),--rare_chest: rare_potions
(7, 6, 0, 2, 5, 5),--rare_chest: uncommon_items
(7, 7, 0, 1, 1, 6),--rare_chest: uncommon_armor
(7, 8, 0, 1, 2, 7),--rare_chest: uncommon_tools
(7, 9, 0, 1, 2, 8),--rare_chest: uncommon_potions
(7, 1, 0, 3, 5, 9),--rare_chest: common_items
(7, 5, 0, 1, 2, 10),--rare_chest: common_potions
(8, 15, 0, 3, 5, 0),--epic_chest: epic_items
(8, 17, 0, 2, 2, 1),--epic_chest: epic_food
(8, 18, 0, 2, 3, 2),--epic_chest: epic_armor
(8, 19, 0, 1, 1, 3),--epic_chest: epic_tools
(8, 16, 0, 1, 1, 4),--epic_chest: epic_records
(8, 10, 0, 1, 3, 5),--epic_chest: rare_items
(8, 10, 0, 2, 4, 6),--epic_chest: rare_items
(8, 10, 0, 2, 4, 7),--epic_chest: rare_items
(8, 11, 0, 1, 1, 8),--epic_chest: rare_armor
(8, 13, 0, 2, 4, 9),--epic_chest: rare_food
(8, 12, 0, 1, 1, 10),--epic_chest: rare_tools
(8, 14, 0, 1, 3, 11),--epic_chest: rare_potions
(8, 9, 0, 1, 3, 12) --epic_chest: uncommon_potions