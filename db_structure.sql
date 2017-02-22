-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Фев 23 2017 г., 00:41
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- База данных: `dyn`
--

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
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`);
