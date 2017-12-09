ALTER TABLE  `family` ADD  `game_view` ENUM(  'defView',  'onlyDaughters',  'onlySons' ) NOT NULL DEFAULT  'defView';
UPDATE  `dyn`.`project` SET  `thing_id` =  '97' WHERE  `project`.`id` =108;
UPDATE  `dyn`.`project` SET  `cost` =  '1000' WHERE  `project`.`id` =108;
ALTER TABLE  `users` ADD  `reset_token` CHAR( 36 );
ALTER TABLE  `project` ADD  `purchased_times` INT NOT NULL ,
ADD  `produced_times` INT NOT NULL;
ALTER TABLE  `users` ADD  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
ADD  `last_login_date` TIMESTAMP NOT NULL;
UPDATE `users` SET `creation_date`=now();
UPDATE  `users` SET  `last_login_date` =  `creation_date`;