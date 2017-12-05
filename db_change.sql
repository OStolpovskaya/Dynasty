ALTER TABLE  `family` ADD  `game_view` ENUM(  'defView',  'onlyDaughters',  'onlySons' ) NOT NULL DEFAULT  'defView';
UPDATE  `dyn`.`project` SET  `thing_id` =  '97' WHERE  `project`.`id` =108;
UPDATE  `dyn`.`project` SET  `cost` =  '1000' WHERE  `project`.`id` =108;