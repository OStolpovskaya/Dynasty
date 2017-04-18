--dev 201285
TRUNCATE TABLE character_buffs;
truncate table character_career;
TRUNCATE TABLE fiancee;
TRUNCATE TABLE characters;

truncate table family_craft_project;
truncate table family_craft_thing;
truncate table family_log;

truncate table item;

DELETE FROM `family` WHERE `id`>1;
DELETE FROM `family_resources` WHERE `id`>1;

truncate table user_achievements;

