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
	type varchar(15) null,
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
	rarity tinyint not null default '0',
	category varchar(45) null,
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
(group_id, item_id, item_weight, min, max, ordering)
values
(1, 1, 10, 1, 5, 1), -- torch
(1, 2, 10, 1, 3, 2), -- bread
(1, 3, 10, 1, 3, 3), -- wool
(1, 4, 10, 1, 3, 4), -- leather
(1, 5, 10, 1, 3, 5), -- rabbit hide
(1, 6, 10, 5, 10, 6), -- arrow
(1, 7, 10, 1, 3, 7) -- bone
;

-- -----------------------------------------------------
-- Add containers data
-- -----------------------------------------------------
insert into containers (name, rarity, category, min_groups, max_groups, min_items, max_items)
values
('common_chest', 0, 'general', 1, 2, 5, 10);

-- -----------------------------------------------------
-- Add containers_has_groups data
-- -----------------------------------------------------
insert into containers_has_groups (container_id, group_id, group_weight, min_items, max_items, ordering)
values
(1, 1, 50, 2, 5, 1); -- common_chest, common_items