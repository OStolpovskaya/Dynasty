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
ALTER TABLE  `family` ADD  `creation_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
UPDATE  `family` SET  `creation_date` = NOW( );
/*====================================================================================================================*/
UPDATE  `dyn`.`house` SET  `desc` =  'Дети персонажа будут больше похожи на отца, чем на мать.' WHERE  `house`.`id` =31;
/*остальные!!!*/
/*====================================================================================================================*/
ALTER TABLE  `dyn`.`family` ADD INDEX  `user` (  `user_id` );
ALTER TABLE  `dyn`.`characters` ADD INDEX  `appearance` (  `body` ,  `ears` ,  `eyebrows` ,  `eye_color` ,  `eyes` ,  `hair_color` ,  `hairstyle` ,  `hair_type` ,  `head` , `height` ,  `mouth` ,  `nose` ,  `skin_color` );
ALTER TABLE  `dyn`.`characters` ADD INDEX  `race` (  `race` );
ALTER TABLE  `dyn`.`characters` ADD INDEX  `career` (  `career` );
ALTER TABLE  `dyn`.`fiancee` ADD INDEX  `character` (  `character_id` );
/*====================================================================================================================*/
ALTER TABLE  `fiancee` ADD  `type` ENUM(  'usual',  'special' ) NOT NULL DEFAULT  'usual' AFTER  `cost`;