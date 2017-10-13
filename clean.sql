TRUNCATE TABLE characters;
TRUNCATE TABLE character_buffs;
truncate table character_career;

DELETE FROM `family` WHERE id>1; ALTER TABLE family AUTO_INCREMENT = 2;
TRUNCATE TABLE `family_building`;
TRUNCATE TABLE `family_craft_project`;
TRUNCATE TABLE `family_craft_thing`;
DELETE FROM  family_log WHERE id>1; ALTER TABLE family_log AUTO_INCREMENT = 2;
UPDATE family_log SET `text`='НОВЫЙ' where id=1;
DELETE FROM `family_resources` WHERE id>1; ALTER TABLE family_resources AUTO_INCREMENT = 2;

TRUNCATE TABLE feedback;
TRUNCATE TABLE fiancee;

truncate table item;

delete from project where author_id>1;
truncate table user_achievements;

