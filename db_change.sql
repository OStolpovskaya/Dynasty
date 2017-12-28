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
/*====================================================================================================================*/
UPDATE house SET  `name` = CONCAT( UCASE( LEFT(  `name` , 1 ) ) , SUBSTRING(  `name` , 2 ) );
/*====================================================================================================================*/
CREATE TABLE IF NOT EXISTS `town_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family_id` int(11) NOT NULL,
  `text` varchar(700) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `family_id` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
UPDATE  `dyn`.`thing` SET  `parent` =  '34' WHERE  `thing`.`id` =44;
UPDATE  `dyn`.`thing` SET  `parent` =  '44' WHERE  `thing`.`id` =75;
ALTER TABLE  `town_news` ADD  `type` ENUM(  'newHouse',  'newFamily',  'achievement',  'newBuilding',  'common' ) NOT NULL DEFAULT  'common' AFTER  `family_id`
/*====================================================================================================================*/
CREATE TABLE IF NOT EXISTS `item_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` enum('newRequest','done') NOT NULL DEFAULT 'newRequest',
  `family_id` int(11) NOT NULL,
  `thing_id` int(11) NOT NULL,
  `project_id` int(11) DEFAULT NULL,
  `min_quality` int(11) NOT NULL,
  `deposit` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;
/*====================================================================================================================*/
ALTER TABLE  `user_achievements` ADD  `family_id` INT NOT NULL AFTER  `user_userid`;
update user_achievements targetTable

left join family sourceTable on
    targetTable.`user_userid`= sourceTable.user_id
set
    targetTable.`family_id`  = sourceTable.id
    where sourceTable.current=true;

ALTER TABLE  `item_request` ADD  `fulfiller_id` INT NULL DEFAULT NULL;

ALTER TABLE  `users` ADD  `type` ENUM(  'player',  'admin',  'bot' ) NOT NULL DEFAULT  'player';
UPDATE  `dyn`.`users` SET  `type` =  'admin' WHERE  `users`.`userid` =1;
UPDATE  `dyn`.`users` SET  `type` =  'bot' WHERE  `users`.`userid` >=26 and `users`.`userid`<=53;
/*====================================================================================================================*/
CREATE TABLE IF NOT EXISTS `user_neighbors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `neighbor_user_id` int(11) NOT NULL,
  `neighbor_family_id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
CREATE TABLE IF NOT EXISTS `mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` enum('unread','read') NOT NULL DEFAULT 'unread',
  `from_userid` int(11) NOT NULL,
  `to_userid` int(11) NOT NULL,
  `text` varchar(350) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
/*====================================================================================================================*/
ALTER TABLE  `achievement` CHANGE  `type`  `type` ENUM(  'newborn',  'famous_people',  'vocation10level',  'craft_master' ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;
INSERT INTO  `dyn`.`achievement` (
`id` ,
`name` ,
`type` ,
`for_what`
)
VALUES (
NULL ,  'Мастер крафта - изготовлено 50 предметов по проекту!',  'craft_master',  '50'
);