--dev 201285
TRUNCATE TABLE character_buffs;
truncate table character_career;
TRUNCATE TABLE fiancee;
TRUNCATE TABLE characters;

truncate table family_craft_project;
truncate table family_craft_thing;

truncate table item;

DELETE FROM `family` WHERE `id`>1; ALTER TABLE family AUTO_INCREMENT = 2;
DELETE FROM `family_resources` WHERE `id`>1; ALTER TABLE family_resources AUTO_INCREMENT = 2;
DELETE FROM  family_log; WHERE `id`>1; ALTER TABLE family_log AUTO_INCREMENT = 2;

truncate table user_achievements;

