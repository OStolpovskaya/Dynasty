-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Фев 23 2017 г., 03:05
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- База данных: `dyn`
--

-- --------------------------------------------------------

--
-- Структура таблицы `achievement`
--

DROP TABLE IF EXISTS `achievement`;
CREATE TABLE IF NOT EXISTS `achievement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Дамп данных таблицы `achievement`
--

INSERT INTO `achievement` (`id`, `name`) VALUES
(1, 'achiev.first_africa'),
(2, 'achiev.first_orient'),
(3, 'achiev.first_injun'),
(4, 'achiev.first_elf'),
(5, 'achiev.first_ork'),
(6, 'achiev.first_goblin'),
(7, 'achiev.first_dwarf'),
(8, 'achiev.first_vampire'),
(9, 'achiev.first_pixy'),
(10, 'achiev.first_troll');

-- --------------------------------------------------------

--
-- Структура таблицы `app_eyes`
--

DROP TABLE IF EXISTS `app_eyes`;
CREATE TABLE IF NOT EXISTS `app_eyes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `app_eyes`
--

INSERT INTO `app_eyes` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.eyes.round'),
(2, 'usual', 'app.eyes.narrow'),
(3, 'usual', 'app.eyes.almond-shaped'),
(4, 'usual', 'app.eyes.deep-set'),
(5, 'usual', 'app.eyes.bulging'),
(6, 'usual', 'app.eyes.heavy_eyelids'),
(7, 'rare', 'app.eyes.huge'),
(8, 'rare', 'app.eyes.bee');

-- --------------------------------------------------------

--
-- Структура таблицы `app_head`
--

DROP TABLE IF EXISTS `app_head`;
CREATE TABLE IF NOT EXISTS `app_head` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Дамп данных таблицы `app_head`
--

INSERT INTO `app_head` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.head.oval'),
(2, 'usual', 'app.head.triangle'),
(3, 'usual', 'app.head.round'),
(4, 'usual', 'app.head.square'),
(5, 'usual', 'app.head.pear-shaped'),
(6, 'rare', 'app.head.big_nape');

-- --------------------------------------------------------

--
-- Структура таблицы `app_height`
--

DROP TABLE IF EXISTS `app_height`;
CREATE TABLE IF NOT EXISTS `app_height` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `app_height`
--

INSERT INTO `app_height` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.height.very_low'),
(2, 'usual', 'app.height.low'),
(3, 'usual', 'app.height.middle'),
(4, 'usual', 'app.height.tall'),
(5, 'usual', 'app.height.very_tall'),
(6, 'rare', 'app.height.halfling'),
(7, 'rare', 'app.height.ultratall');

-- --------------------------------------------------------

--
-- Структура таблицы `app_skin_color`
--

DROP TABLE IF EXISTS `app_skin_color`;
CREATE TABLE IF NOT EXISTS `app_skin_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `app_skin_color`
--

INSERT INTO `app_skin_color` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.skin_color.fair'),
(2, 'usual', 'app.skin_color.swarthy'),
(3, 'usual', 'app.skin_color.dark'),
(4, 'usual', 'app.skin_color.olive'),
(5, 'usual', 'app.skin_color.copper'),
(6, 'rare', 'app.skin_color.pale'),
(7, 'rare', 'app.skin_color.light_green'),
(8, 'rare', 'app.skin_color.green'),
(9, 'rare', 'app.skin_color.bluish');

-- --------------------------------------------------------

--
-- Структура таблицы `character`
--

DROP TABLE IF EXISTS `character`;
CREATE TABLE IF NOT EXISTS `character` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family` int(11) DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `sex` enum('male','female') NOT NULL,
  `father` int(11) DEFAULT NULL,
  `spouse` int(11) DEFAULT NULL,
  `level` int(11) NOT NULL,
  `race` int(4) NOT NULL,
  `height` int(4) NOT NULL,
  `skin_color` int(4) NOT NULL,
  `hair_color` int(4) NOT NULL,
  `hair_type` int(4) NOT NULL,
  `hairstyle` int(4) NOT NULL,
  `head` int(4) NOT NULL,
  `ears` int(4) NOT NULL,
  `eyebrows` int(4) NOT NULL,
  `eyes` int(4) NOT NULL,
  `eye_color` int(4) NOT NULL,
  `nose` int(4) NOT NULL,
  `mouth` int(4) NOT NULL,
  `body` int(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `family`
--

DROP TABLE IF EXISTS `family`;
CREATE TABLE IF NOT EXISTS `family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `family_name` varchar(30) NOT NULL,
  `current` tinyint(1) NOT NULL,
  `male_lastname` varchar(30) NOT NULL,
  `female_lastname` varchar(30) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Дамп данных таблицы `family`
--

INSERT INTO `family` (`id`, `user_id`, `family_name`, `current`, `male_lastname`, `female_lastname`, `level`) VALUES
(1, 1, 'Случайновы', 1, 'Случайнов', 'Случайнова', 0),
(2, 13, 'Петровы', 1, 'Петров', 'Петрова', 0),
(3, 12, 'Верещагины', 1, 'Верещагин', 'Верещагина', 0);

-- --------------------------------------------------------

--
-- Структура таблицы `race`
--

DROP TABLE IF EXISTS `race`;
CREATE TABLE IF NOT EXISTS `race` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=12 ;

--
-- Дамп данных таблицы `race`
--

INSERT INTO `race` (`id`, `name`) VALUES
(1, 'race.human'),
(2, 'race.africa'),
(3, 'race.orient'),
(4, 'race.injun'),
(5, 'race.elf'),
(6, 'race.ork'),
(7, 'race.goblin'),
(8, 'race.dwarf'),
(9, 'race.vampire'),
(10, 'race.pixy'),
(11, 'race.troll');

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(60) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- Дамп данных таблицы `users`
--

INSERT INTO `users` (`userid`, `username`, `email`, `password`, `enabled`) VALUES
(1, 'priya', 'abc@abc.com', '$2a$04$CO93CT2ObgMiSnMAWwoBkeFObJlMYi/wzzOnPlsTP44r7qVq0Jln2', 1),
(2, 'naveen', 'def@def.com', '$2a$04$j3JpPUp6CTAe.kMWmdRNC.Wie58xDNPfcYz0DBJxWkucJ6ekJuiJm', 1),
(9, 'Omletik', 'o.stolpovskaya@gmail.com', '$2a$10$sCeLv63oNgjyI8VPfTmL/OCnx04mP3xdlT8XeNh43p26G3JHo1blm', 1),
(11, 'OlgaTheFirst', 'l.i.l.y@mail.ru', '$2a$10$.JsZb9j6gWMGJP6uQ.s/MefbTsVpic6q8JmNhyqU.R7kcCoK6EBQq', 1),
(12, 'Octopus', 'o.mokhnatova@gmail.com', '$2a$10$jh.V3BMLvdnvOJpR4NbqvO01fe9/8DqjjKLBm4NwzsejFLhlAIRyy', 1),
(13, 'Периметр', 'perimetr@gmail.com', '$2a$10$5w4/HBoX5.XsXctXJ.u6GODcArsvdueo.1a.Q0/kXVja7X8Xcvdd2', 1);

-- --------------------------------------------------------

--
-- Структура таблицы `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `role` enum('ROLE_USER','ROLE_ADMIN','ROLE_MODER') NOT NULL DEFAULT 'ROLE_USER',
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `uni_userid_role` (`role`,`userid`),
  KEY `fk_user_idx` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=15 ;

--
-- Дамп данных таблицы `user_roles`
--

INSERT INTO `user_roles` (`user_role_id`, `userid`, `role`) VALUES
(1, 1, 'ROLE_USER'),
(3, 2, 'ROLE_USER'),
(4, 9, 'ROLE_USER'),
(12, 11, 'ROLE_USER'),
(13, 12, 'ROLE_USER'),
(14, 13, 'ROLE_USER'),
(2, 1, 'ROLE_ADMIN');

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`);
