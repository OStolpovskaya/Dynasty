-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Фев 23 2017 г., 00:43
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- База данных: `dyn`
--

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

--
-- Дамп данных таблицы `app_height`
--

INSERT INTO `app_height` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.height.very_low'),
(2, 'usual', 'app.height.low'),
(3, 'usual', 'app.height.middle'),
(4, 'usual', 'app.height.tall'),
(5, 'usual', 'app.height.very_tall'),
(6, 'usual', 'app.height.halfling'),
(7, 'usual', 'app.height.ultratall'),
(8, 'usual', 'app.skin_color.fair');

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

--
-- Дамп данных таблицы `family`
--

INSERT INTO `family` (`id`, `user_id`, `family_name`, `current`, `male_lastname`, `female_lastname`, `level`) VALUES
(1, 1, 'Случайновы', 1, 'Случайнов', 'Случайнова', 0),
(2, 13, 'Петровы', 1, 'Петров', 'Петрова', 0),
(3, 12, 'Верещагины', 1, 'Верещагин', 'Верещагина', 0);

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
