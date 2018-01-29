INSERT INTO `app_skin_color` (`id`, `type`, `name`, `color`, `title`) VALUES
(10, 'rare', 'app.skin_color.grey', '968d8a', 'темно-серый*');
INSERT INTO `dyn`.`app_body` (`id`, `type`, `name`, `title`) VALUES (NULL, 'rare', 'app.body.wide_white_wings', 'широкие плечи с белыми крыльями*');
INSERT INTO `race` (`id`, `name`, `food`, `wood`, `metall`, `plastic`, `microelectronics`, `cloth`, `stone`, `chemical`) VALUES
(21, 'Хоббит', 0, 1, 0, 0, 0, 0, 0, 0),
(22, 'Полуорк', 0, 0, 1, 0, 0, 0, 0, 0),
(23, 'Дроу', 0, 0, 0, 0, 0, 0, 0, 1),
(24, 'Ангел', 0, 0, 0, 0, 0, 1, 0, 0);

INSERT INTO `race_appearance` (`id`, `race_id`, `body`, `ears`, `eyebrows`, `eye_color`, `eyes`, `hair_color`, `hair_type`, `head`, `height`, `mouth`, `nose`, `skin_color`) VALUES
(165, 21, 2, 6, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, NULL, NULL),
(166, 21, 3, 6, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, NULL, NULL),
(167, 22, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 9, NULL, 7),
(168, 22, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 13, NULL, 7),
(169, 22, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 9, NULL, 7),
(170, 22, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 13, NULL, 7),
(171, 22, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 9, NULL, 7),
(172, 22, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4, 13, NULL, 7),
(173, 22, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 9, NULL, 7),
(174, 22, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5, 13, NULL, 7),
(175, 23, NULL, 5, NULL, 11, NULL, 7, 1, NULL, 7, NULL, NULL, 10),
(176, 24, 8, 1, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 3, NULL),
(177, 24, 8, 1, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 4, NULL),
(178, 24, 8, 1, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 5, NULL),
(179, 24, 8, 1, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 8, NULL),
(180, 24, 8, 3, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 3, NULL),
(181, 24, 8, 3, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 4, NULL),
(182, 24, 8, 3, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 5, NULL),
(183, 24, 8, 3, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 8, NULL),
(184, 24, 8, 2, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 3, NULL),
(185, 24, 8, 2, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 4, NULL),
(186, 24, 8, 2, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 5, NULL),
(187, 24, 8, 2, NULL, NULL, NULL, NULL, NULL, NULL, 7, NULL, 8, NULL);
INSERT INTO `achievement` (`id`, `name`, `type`, `for_what`) VALUES
(48, 'Хоббит: первый новорожденный!', 'newborn', 21),
(49, 'Полуорк: первый новорожденный!', 'newborn', 22),
(50, 'Дроу: первый новорожденный!', 'newborn', 23),
(51, 'Ангел: первый новорожденный!', 'newborn', 24);

ALTER TABLE  `adventure` CHANGE  `text_desc`  `text_desc` VARCHAR( 1600 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
CHANGE  `text_success`  `text_success` VARCHAR( 1000 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
CHANGE  `text_failed`  `text_failed` VARCHAR( 800 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

