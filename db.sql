-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Мар 23 2017 г., 18:59
-- Версия сервера: 5.5.25
-- Версия PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `dyn`
--

-- --------------------------------------------------------

--
-- Структура таблицы `achievement`
--

CREATE TABLE IF NOT EXISTS `achievement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `type` enum('newborn','famous_people') NOT NULL,
  `for_what` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- Дамп данных таблицы `achievement`
--

INSERT INTO `achievement` (`id`, `name`, `type`, `for_what`) VALUES
(1, 'achiev.first_africa', 'newborn', 'race.africa'),
(2, 'achiev.first_orient', 'newborn', 'race.orient'),
(3, 'achiev.first_injun', 'newborn', 'race.injun'),
(4, 'achiev.first_elf', 'newborn', 'race.elf'),
(5, 'achiev.first_ork', 'newborn', 'race.ork'),
(6, 'achiev.first_goblin', 'newborn', 'race.goblin'),
(7, 'achiev.first_dwarf', 'newborn', 'race.dwarf'),
(8, 'achiev.first_vampire', 'newborn', 'race.vampire'),
(9, 'achiev.first_pixy', 'newborn', 'race.pixy'),
(10, 'achiev.first_troll', 'newborn', 'race.troll'),
(11, 'achiev.first_medusa', 'newborn', 'race.medusa'),
(12, 'achiev.first_alien', 'newborn', 'race.alien'),
(13, 'achiev.first_werewolf', 'newborn', 'race.werewolf'),
(14, 'achiev.first_demon', 'newborn', 'race.demon'),
(15, 'achiev.first_minotaur', 'newborn', 'race.minotaur'),
(16, 'achiev.first_harpy', 'newborn', 'race.harpy');

-- --------------------------------------------------------

--
-- Структура таблицы `app_body`
--

CREATE TABLE IF NOT EXISTS `app_body` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(35) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Дамп данных таблицы `app_body`
--

INSERT INTO `app_body` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.body.wide_shoulders'),
(2, 'usual', 'app.body.middle'),
(3, 'usual', 'app.body.thin_shoulders'),
(4, 'rare', 'app.body.huge_torso'),
(5, 'rare', 'app.body.fine_with_butterfly_wings'),
(6, 'rare', 'app.body.feathered_with_wings');

-- --------------------------------------------------------

--
-- Структура таблицы `app_ears`
--

CREATE TABLE IF NOT EXISTS `app_ears` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `app_ears`
--

INSERT INTO `app_ears` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.ears.small'),
(2, 'usual', 'app.ears.middle'),
(3, 'usual', 'app.ears.big'),
(4, 'usual', 'app.ears.protruding'),
(5, 'rare', 'app.ears.high_long'),
(6, 'rare', 'app.ears.big_pointed'),
(7, 'rare', 'app.ears.none'),
(8, 'rare', 'app.ears.small_horns'),
(9, 'rare', 'app.ears.big_horns');

-- --------------------------------------------------------

--
-- Структура таблицы `app_eyebrows`
--

CREATE TABLE IF NOT EXISTS `app_eyebrows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `app_eyebrows`
--

INSERT INTO `app_eyebrows` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.eyebrows.arched'),
(2, 'usual', 'app.eyebrows.ascending'),
(3, 'usual', 'app.eyebrows.curved'),
(4, 'usual', 'app.eyebrows.horizontal'),
(5, 'usual', 'app.eyebrows.houselike'),
(6, 'usual', 'app.eyebrows.with_break'),
(7, 'rare', 'app.eyebrows.none');

-- --------------------------------------------------------

--
-- Структура таблицы `app_eyes`
--

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
-- Структура таблицы `app_eye_color`
--

CREATE TABLE IF NOT EXISTS `app_eye_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;

--
-- Дамп данных таблицы `app_eye_color`
--

INSERT INTO `app_eye_color` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.eye_color.aquamarine'),
(2, 'usual', 'app.eye_color.black'),
(3, 'usual', 'app.eye_color.blue'),
(4, 'usual', 'app.eye_color.dark_green'),
(5, 'usual', 'app.eye_color.green'),
(6, 'usual', 'app.eye_color.grey'),
(7, 'usual', 'app.eye_color.light_blue'),
(8, 'usual', 'app.eye_color.light_green'),
(9, 'usual', 'app.eye_color.violent'),
(10, 'usual', 'app.eye_color.walnut'),
(11, 'rare', 'app.eye_color.lumired'),
(12, 'rare', 'app.eye_color.lumiyellow');

-- --------------------------------------------------------

--
-- Структура таблицы `app_hair_color`
--

CREATE TABLE IF NOT EXISTS `app_hair_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  `color` varchar(7) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `app_hair_color`
--

INSERT INTO `app_hair_color` (`id`, `type`, `name`, `color`) VALUES
(1, 'usual', 'app.hair_color.fair', 'a38a74'),
(2, 'usual', 'app.hair_color.blond', 'fce8cf'),
(3, 'usual', 'app.hair_color.red', 'ab4b23'),
(4, 'usual', 'app.hair_color.auburn', '603628'),
(5, 'usual', 'app.hair_color.black', '0a0200'),
(6, 'usual', 'app.hair_color.dark', '18100d'),
(7, 'rare', 'app.hair_color.white', 'fffcf6'),
(8, 'rare', 'app.hair_color.blue', '1c3d5c'),
(9, 'rare', 'app.hair_color.green', '127441');

-- --------------------------------------------------------

--
-- Структура таблицы `app_hair_style`
--

CREATE TABLE IF NOT EXISTS `app_hair_style` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sex` enum('male','female') NOT NULL,
  `hair_type` int(11) NOT NULL,
  `name` varchar(35) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

--
-- Дамп данных таблицы `app_hair_style`
--

INSERT INTO `app_hair_style` (`id`, `sex`, `hair_type`, `name`) VALUES
(1, 'female', 1, 'app.hair_style.straight.bob'),
(2, 'female', 1, 'app.hair_style.straight.pony_tail'),
(3, 'female', 2, 'app.hair_style.curly.falls'),
(4, 'female', 2, 'app.hair_style.curly.tail'),
(5, 'female', 3, 'app.hair_style.wavy.falls'),
(6, 'female', 3, 'app.hair_style.wavy.tail'),
(7, 'female', 4, 'app.hair_style.thinning.bob'),
(8, 'female', 5, 'app.hair_style.none.none'),
(9, 'female', 6, 'app.hair_style.snakes.falls'),
(10, 'male', 2, 'app.hair_style.curly.falls'),
(12, 'male', 5, 'app.hair_style.none.none'),
(14, 'male', 6, 'app.hair_style.snakes.falls'),
(16, 'male', 1, 'app.hair_style.straight.bob'),
(18, 'male', 1, 'app.hair_style.straight.pony_tail'),
(20, 'male', 4, 'app.hair_style.thinning.bob'),
(22, 'male', 3, 'app.hair_style.wavy.falls'),
(24, 'male', 3, 'app.hair_style.wavy.tail');

-- --------------------------------------------------------

--
-- Структура таблицы `app_hair_type`
--

CREATE TABLE IF NOT EXISTS `app_hair_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

--
-- Дамп данных таблицы `app_hair_type`
--

INSERT INTO `app_hair_type` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.hair_type.straight'),
(2, 'usual', 'app.hair_type.curly'),
(3, 'usual', 'app.hair_type.wavy'),
(4, 'usual', 'app.hair_type.thinning'),
(5, 'rare', 'app.hair_type.none'),
(6, 'rare', 'app.hair_type.snakes');

-- --------------------------------------------------------

--
-- Структура таблицы `app_head`
--

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

CREATE TABLE IF NOT EXISTS `app_height` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

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
-- Структура таблицы `app_mouth`
--

CREATE TABLE IF NOT EXISTS `app_mouth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

--
-- Дамп данных таблицы `app_mouth`
--

INSERT INTO `app_mouth` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.mouth.angular'),
(2, 'usual', 'app.mouth.chubby'),
(3, 'usual', 'app.mouth.glamour'),
(4, 'usual', 'app.mouth.heart'),
(5, 'usual', 'app.mouth.middle'),
(6, 'usual', 'app.mouth.notprecise'),
(7, 'usual', 'app.mouth.straight'),
(8, 'usual', 'app.mouth.thin'),
(9, 'rare', 'app.mouth.lower_fangs'),
(10, 'rare', 'app.mouth.upper_fangs');

-- --------------------------------------------------------

--
-- Структура таблицы `app_nose`
--

CREATE TABLE IF NOT EXISTS `app_nose` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `app_nose`
--

INSERT INTO `app_nose` (`id`, `type`, `name`) VALUES
(1, 'usual', 'app.nose.big'),
(2, 'usual', 'app.nose.fleshy'),
(3, 'usual', 'app.nose.pointed'),
(4, 'usual', 'app.nose.straight'),
(5, 'usual', 'app.nose.upturned'),
(6, 'usual', 'app.nose.with_hump'),
(7, 'rare', 'app.nose.wide');

-- --------------------------------------------------------

--
-- Структура таблицы `app_skin_color`
--

CREATE TABLE IF NOT EXISTS `app_skin_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('usual','rare') NOT NULL,
  `name` varchar(30) NOT NULL,
  `color` varchar(7) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

--
-- Дамп данных таблицы `app_skin_color`
--

INSERT INTO `app_skin_color` (`id`, `type`, `name`, `color`) VALUES
(1, 'usual', 'app.skin_color.fair', 'e7c6aa'),
(2, 'usual', 'app.skin_color.swarthy', 'ab8c72'),
(3, 'usual', 'app.skin_color.dark', '826751'),
(4, 'usual', 'app.skin_color.olive', 'ebd9a2'),
(5, 'usual', 'app.skin_color.copper', 'dcb787'),
(6, 'rare', 'app.skin_color.pale', 'ece6de'),
(7, 'rare', 'app.skin_color.light_green', 'a1b090'),
(8, 'rare', 'app.skin_color.green', '61724f'),
(9, 'rare', 'app.skin_color.bluish', '88bbac');

-- --------------------------------------------------------

--
-- Структура таблицы `buff`
--

CREATE TABLE IF NOT EXISTS `buff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `type` enum('usual','rare') NOT NULL,
  `contradictory` int(11) DEFAULT NULL,
  `cost` int(11) NOT NULL DEFAULT '100',
  `description` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `buff`
--

INSERT INTO `buff` (`id`, `title`, `type`, `contradictory`, `cost`, `description`) VALUES
(1, 'buffs.title.fertility', 'usual', NULL, 600, 'buffs.description.fertility'),
(2, 'buffs.title.fiveChildren', 'rare', NULL, 100, 'buffs.description.fiveChildren'),
(3, 'buffs.title.dominantFather', 'usual', 4, 100, 'buffs.description.dominantFather'),
(4, 'buffs.title.dominantMother', 'usual', 3, 100, 'buffs.description.dominantMother'),
(5, 'buffs.title.manySons', 'usual', 6, 100, 'buffs.description.manySons'),
(6, 'buffs.title.manyDaughters', 'usual', 5, 100, 'buffs.description.manyDaughters'),
(7, 'buffs.title.geneticMod', 'usual', NULL, 100, 'buffs.description.geneticMod');

-- --------------------------------------------------------

--
-- Структура таблицы `characters`
--

CREATE TABLE IF NOT EXISTS `characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `family` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `sex` varchar(6) NOT NULL,
  `father` int(11) DEFAULT NULL,
  `spouse` int(11) DEFAULT NULL,
  `level` int(11) NOT NULL,
  `race` int(4) NOT NULL,
  `career` int(11) NOT NULL,
  `body` int(4) NOT NULL,
  `ears` int(4) NOT NULL,
  `eyebrows` int(4) NOT NULL,
  `eye_color` int(4) NOT NULL,
  `eyes` int(4) NOT NULL,
  `hair_color` int(4) NOT NULL,
  `hairstyle` int(4) NOT NULL,
  `hair_type` int(4) NOT NULL,
  `head` int(4) NOT NULL,
  `height` int(4) NOT NULL,
  `mouth` int(4) NOT NULL,
  `nose` int(4) NOT NULL,
  `skin_color` int(4) NOT NULL,
  `view` blob NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Дамп данных таблицы `characters`
--

INSERT INTO `characters` (`id`, `family`, `name`, `sex`, `father`, `spouse`, `level`, `race`, `career`, `body`, `ears`, `eyebrows`, `eye_color`, `eyes`, `hair_color`, `hairstyle`, `hair_type`, `head`, `height`, `mouth`, `nose`, `skin_color`, `view`) VALUES
(3, 50, 'Карл', 'male', NULL, 4, 0, 1, 1, 1, 3, 5, 7, 3, 5, 10, 2, 4, 2, 4, 5, 4, 0x89504e470d0a1a0a0000000d49484452000000c8000000c80806000000ad58ae9e0000359a4944415478daed5d075854d7d69da10c831515c58a151b46516c88150591228858b02b76634b34f66e34f62e76b1c61e63628b2d9628b6688c1a4b12d37d892f2fe57fc94b6274ff679db9975cae33c30c0c30e5ecefdb1f889429679fddd65e5ba31122448810214284081122448810214284081122448810214284081122448810214284081122448810214284081122448810214284081122448851494b4b2b94766253dccbc9514b7b746e99da35a1d9aeee892df6ca9adcbdf5aee1fda3975c3a9d1acabedd8da956f1e35ae96b6eaa5f8bafeb9816625a90a98778a58538845cbbb0a3d598611d36252785df8e8d6cf024a255bdffd50828fbbc6ae5d254af7625aa1f54996b83ba55b806d5aa48952b94c4d79e356958e3b7b62d831e0dec1d717cf5c257879728510246d38a6930535fa6ee925134653a9ae922a67399c6332d225e7d21f6206ed28ded895b3c3434b474728ff0f5f1518d6e468507ff1c165aeb5945ff121458dd9f9a35ae49491d9ad1ccf149b46ef1507a7d520f1adaaf1d318f416b170de55fc3bf5b86d6a28ae5fdc8b768212ac6b460016ff2299c9f7d5ef0ef62450afe51caafe8bf743add1cf6f75a301dc6f40c7321c43efe20e9474cbb4a5e4588903c116fa6359976f2f32b3e29b84ea50b8dea067c5bb572a9a705f2eba970c17c54a654316aca8ca279484d82c728e9578402d8c7ee89cda967e7965427b0020e3d1561873fac596d6ad3a20e15f72d4cf9f3e9a97cd9e254897912fc7fbe7c5e94cfdb8b6004cc1af947f5e7ee5a0d796a0d1f350663d9cbb4b66424eee2ed1292d38238df8b6931c930fa962d53e22233863f3c3cdc49abd592ced383f47a1df914ca4fa5983114f52940d2cd9ee140c3007c8b15e2a15468c31afc6bf899c285f271a319961c452bde18486b160e493ff41e4c754cbd24437053fc5efc9fde4d43de6e86ff97ff0efbff754c5f669ac83440f272428464db10e4b04956c4f9214c47f9f814dee45fbad82337c5a177970ea59b74583da5c3ec61e470cb5f97bf8683ad537c1fbe07073d9fa4de0a557e4dfe194fe97728ff0f5f930ce41ba65f30bdc5348569a0787b8558933bb84bb7aaacf9985661da9669320c82e92b4c67b05ce02bad2abc910f340ea8acea83acfe5a3e1307ded8f765a6de267e06bf4b364ea8de4b779f3d87ef984e635a5cbcf54232131fa6a8080d623a99e94ca6e3994e61ba8be965a531a8e37c9d919bded46d6fea60ebb36814d6188ffc37f0985928f89485703f172f5eb4af7411081192216c7297c2257fa623989ed5fe13ab93da3bb82b6e60759c6fec30cab981ec55f265f27df2efd66973ce48947fd7434ade59eef3bc7948e08de2c58b8b9c4448ba71f8318dd3187a052b993e923d829c0fb8293c8497c218f46ec6bd85fac02b2b4a3aadf1eff3507d9f6c8c3a6dce1a483e45f2eee9e941282a942f57e2697478f04e51e112794635e4104c3f61fa13d31fd41ec1da90c75b11baa84bad3a2349b6baeaa42cc9caffd69bf81b7262af53186e56bd88d2486124654a167dde2eacde49cd8b9d79212e22a599ce57ded6fc163572889507d19ca1282b486ac3d09bb8b9b58a72ac4ee59de4df2357a394a19abb118fe36e2684b3d450748adf5b9a1949cfce616f89a3e27a829e45124a9cf201f43271f8bd140d36adc288d4a1955ee50dcc1986fa40ea4c24f53aed8be5627508a6545b1989a7c27bd5ac56ee79c7a8902322dc722d29c5748da9104679e8dd5587544edc95de462e9d2abd8197d67609b4ba40e0a90aabf42acfe5918d70cbd8f32e57bad8f3766d78b855581c1dd71034c5f6cbdec3547cafbc49bd548d3b374512eda1cde85df4364ca0f56e19f30c6f0b926dd948bccdf447322b39eb1486890ba15041efe7d5aa96d9a63120828538b95467ba47595592431a0f9501688de425ca045979c37b66b32c2b1b83b70d7208d978958fd55351995376ecbd2d283be3f9152b5af059a952a53a68fec1708904de49a504d355ca43adbc7dd5a03f63e19297366318620be3508648d969342a3d9a9be6c5dc45ad993d76f9b1c193942be3fb7fece354a68399b6d618baefc2509c4cf0a6a6280fa3329cd2694d8735deaac45dbea9bd6d104ab91bc963d437bfa705f98d97aa92e6a6e8eb782a54694899f55b942197a787c77fd973ff92e939a6e398961347ca7904cd414049d29407513e503aad654d35756fc3cb821cc11223f134e1c934569673d5b98b29dc9787997e8bb1f04d5966668fe92ba6f73406584e7e71b49c43dca512efa77268a4bc49cd1d3cf581f2d266ec59d8a2f3adee45282b57993510b3d345b726875256eed8ebf835d3f73486c945ad385e8e2f00e38d560f1669cc547ed46190da109478260f1b845bca4a9ad22ba9c33b5bfd2d4f552e654dd54c7a2cbf325d2695cf8538b8f7c0ecc651f5b09225bd0373a154566e625b78184f233993b5486075fea3d558567c901f0b7b1d3fd318e64a3a684443d1a1055076cc3d3c765375bbbd2d340e7365516337b1a74a7536088d947fcb5857ddd8b09425a196b218e06e456e2279cf7f33c52cbc20847050d16b0cac1f47ad8db765c390c32b53798abaa32e7b29a5da2a7f90137a7795660690b464c84a59f6b6a6c2c59edf69c98b1412c7cdb104159618a6fb519eb4a45ae56da6aa64ee67953fa31cb3f55441da6d99ab2855dddccb6ac8a72c48785a503153e47228fd0ed018c608843880a089d598e971a6bf99cb37bc8d206695b7b12506626e2a50dd6fd1e7f0c4a067368dc4536b9931abf322f63a638c17630455454e62ff020a9e314cbfc90c61abbc09a1601f01811bf8a8362f1f4eed5ad7cb74823033d52906b0f416ce91673757511ab677164ac09995bfd5332552d28e1ec952a695c411b46f01cb202605bfb7245490bd464c447d7a7d5277dabd610cdd3ab78c1e5c49a119e392384f55a182f94cc2dead39746a461339b9f6ce8191daac782de5eb61694143364af67adf919a88dd8417b15f01f23496e9494b43231c04b0154e7eb5337d736b23fdeb6e2a3db9bf957eb8b7853e3cb398668eef4611ad82c8bf6c71ce6de56664d8c91a08bbba17a3cd81315b5d160d441d3a6955901573e3c6ecb9a4327da231105e88f9763b14cc2e74617a463e785edacca119380cf0128b66f64d370e5961248f3e5c47670ecea679d37a53626c130c14f1504c392eeb6d216a578d1c9687ad3cb5b60fb3b25a1850864eea2a992978bfa2f42b0cc44e050c25114c2f201676cfa47fa12ce1fa15f7a1164d02e9c8aea9dc209406a234946f3fde4497df9b4f1b960ea381bdda5283ba01e907c7d2612975c73c3b07d916d5284b1eabce48954cfd78550c8e3d3502ed6b778266d57ca680661f3477609573e1552a96a2ce71a1b463cd2bf4e5cdf5468de30543b9bd89ee7eb08256cd1f448deb57cb5688a43301f9b014fa919981d8d2f0f4aac6a8dec4f39072102f7124ed4790103641dec1f4174f0bd1b9f01c43fa46d2f977e772ef909971a8f5ca7b0b78c885dfe39ec503ad68b8199ddbc8cadc89bac3ef616464d716b3eb7a13f323d2c750a9512bc40e04b3094b105a59529e94df4ce411a85a7d7767b3d5c601fdfcfa5afef395ca1b58d7adc178a9c33df5d49f7b26dc5a96dcf6a678b7b2da8f31873d334294bd8f6998f024792f805b8304ee26d3bb9901ed94e8d872a57d69c9ec7ef4f86ed60ce4fb4fb6d0476797d2ea05836940af08beae00215b76211fca3c45be95b3924b789ba00dd264c12be92d281d2b8d446350d007b5149e246f0537d444a63f9a8bb7e5aa0cf28de621817cef46a3e0aa7470db049389b9a50a0ff4c9c595746ccf349a32a633bd54b37c066fa2cb4693d1d346307735138b471643414be667549e047b49ea8b639a775294e92a94173db5e6432a2cb2412804cf01e3405917cdc0ec18873a8187a1c0a374886e4c35aa96cb3288d0d8c154e612ba2cfebeac54cdd465634b47041446324d2310bf792600c8ad910dc45b357a2ac7f7584e53ba6451ead5a515cd9fde9bdeda3a81ee5f5e9d6def61cc48beb8b18e2e1e9d47eb970ca341bddb66003e5acb9de5a5354fc0602d1f56561a88e6927ebd053fc3de9b4b52a825a60ff340c056b29ae94f72a75779a0b0a5099b9e4ab090aa4f52183fb8c8396c6d18a642afdb1796d3ec89dd2d063d9acb233c4d24f1d6fc4e9d0acca8cf82a16a54dbacbc336746c19ec4c11ab16d374f04e8d174be2b9d8ad9a375f3dabc94bb78563fba746c5e962b56595518e3dc293d6d82e65527f1ca66a077167a24ea1d279971102b1957dc2dc849140682eefa086120792375991e960d44090cac5db302eddf3c8e3ebdba26d70d436920a8946955c46eb668de65b50bef6d8489de9aa9444b21fcaa999151a29a957706724469209c7dc4cbf3afba2f55a22befcdcf13c350e624efbf3d9b62da3648e7d45282ffb26a30ca43ea990dbc95a7913d2596125964f6b795dfc79e3b768fd4d108084aae0b4a88ef2951a7ecdfb7f45e9e7fd60faa4269c7f2d640a040081f7e730af5e8d4f285d1dcac56b8d49076cf6cc053d48b7f322b26585af2cd97b12f825911acb6133b1173b94998a0314cb361832b7058bf30fd9625e5bf84b1fce3ea8905796e2072a805e8fcc6652ff3a47de2a8445e3470d3646d02d018898356d579f7ce02d6cadd0290a3359530c5acc8a74ccf336d26aa59b92b3d34868dad3090ff30ddcd7450f3d05a8f13a21bd3f5538becc240e4700b707a180b008fe899ecddf45a86a4d99a32b031984a7649e62cc96bac2d152bbc0806aab0a35d0c53e5a20c647a43328eeb4ca3172c58903f3aa2de7ffa26b5e665567b311053e1d7b239fdd33d8997367bd52d63e8606b422e4b0c44997c7b6a2d9b369442ca8b1a033e4b78905c94b308a9a48ffdd1593f72e448f13ab52a3e9d36b62baf60d9b3817c766d0d0def1f6d536a2077ad7180a2256197d23b785bd838cc6cb440f21e8f99ced618d6e009c96501ed4c478db40d69f3eac9c3ab5529c3211fc660ec0019a2db8d990e4c0a9ed83f83befe68439e1808baf983fb44f286a6bb0dc8b095d3805941055b9a805b82eb521612348671dceaa28a6507d2af5beb0d3502cad2dbdb26a677cce5d15924ca981c9c33a9079f0a0caa559177d9cfbdf37a9e18080c78eda22154ab867f96e128e6c22ebdc260ac2de166f6bdba4caa5eaa322f1a85620cd71e24b17d93edc1752ad1ceb5aff0837f68fb24dab57e0caf1e01cd1b58dd1f4b61f8c74eed9b701693af6e6ec8904ce3dfc85fe06972129682df7d3f6d15ad5b3c945a35ad65f329404ba887b21b3e99fa3ec59cfabf98be260cc44ea47d64c3dd01954a516cdb06d4ac714d2a5bba1895295594e055c29ad5a65183dbd3f694d174eac04cbaf9fed217e64170ab1fd8329efaf70ce788df73efcee1de27270d05e3be28fd825dc5dd460b414d1d664b66f533abaa5962487ab7f4c1a91f3586a53bc240ec41dab5a9b7af945f516a5cbf2ab1708b433d504e3dba7b2a7d70e40d5e5e55b397a80d045ea56d585dee6522c3ea713c157efeea8985fcc6c7f7d8d260f0bb6094e8fec3a0f137b3cadc6ec9165b0f236b16d47d15736c94ba4c98edbd0c9e639b5440419faa9328efda89748a0dd95127b0020f5b40008704dc9ac32c43d6cf1e7a9d831ce18930db01c6457c3e7a487b5a396f2087ccc30bc1c35c3bb990cf969833bccc1415b7156f0ce44410f9f3e9393c1f7fd3125e2a4bbd889a5ad5cb2de362528d059badd49b75f5a67788a07b7e450aafc41e915c147337915baf2e2d5760f475cbaa91161d5854b58c41e0f16f18d785c37369f99cfe347e6447cebe08e3c32d5fb1bc1f379c86f502b8e18c7db9036d5a3e9c7b2be8becde3687fea385e2c38be773aa70cba7769159f65c7df3455f6c5cfbcdc3f8a421bd6e0f3ee3e85f26738b4d9d989ae572dfc94715898b244d10286bf67e358cef2326e440201ae539b3d5fd93033032aaa12f3f7a546ae9fe87dd88f789c3cb8a96644aba0671856423865ce38908c9f645e60f38a11dcdb18f334b2a13cbc9ac2994c4ebf358b8743c84f9037e060c170aa562e4d7abd0e40493e83a2d31914938c30a6f09641dcfbcc9ddc9397774d3d261835feffbd7dd33907172a6e2306c46428d92a4bc2d6f2fbca63bcb2c1f5ef114e47764da18fcf2fe38d4bf9d28047dcba7a14356d54837b32397731c7b9a53290831a034051482e8a253791b66f52d82394704fee9fc1ab51773e58f102e41d071f073ea96373ce7082be89a5040e72a50b61117e377213f45370fbefd9f81a5778117c84577965681ccf69e07190676036c592bf81838ac78d241e3f031a54ad6a4b961c8259127e792be879f038305d894285294f0b83c1dc3e7222304a7a65e24194f989c64068dd53e41ef6135ea5cbc841edb7f814ce4f3d3bb7e40c88ed231bf29b1ba18edc3cc40144d803283a1a75d35feb9a237323303adcc630463426c1c325dfd4563716d356f1a2036e75f5c21e370bf79fcb3f030f08af68c9e3df96328a1b880c5f31e6c98c947701ff9928aa57b91c425962442306254ed0b35007e14d119f0254a8607ef6b93705d6a8441b960d4f9f47473e8050ccdddd8d460e8cc912815c6e2b4acee8ebc0436a8cac8f366524caf007c507845508e31062c2703f3cbd9883288dfd4d84aa98cc544e6e2a67edf5c60d04e5dd29c24072572c822a8c1b37cebf6c69df6711adeb5364db161454af1e55ae1240818181542da03c8d1b99c8e7d3bffe78234fae3d3ddca977d7566673033936c70145d88698fdfa99a574f9e452ba7462195d39b984ae9f5ec22b5af054c855107e190bdb640a53844ea61276730acf871e0f721d783f3c8761c95126fb125e0ad809722640ed9b34accebd6b83ba5578a1012c91c8ad8c19091e23422d143f94b3ff1af32c8b40588f1247d60ee5d8b16345ab07f8ff3963ea707ae7ccfb347ada02aa50a1228d1b3f81927af4a0a6a18da8737c0b3ab6672aefb0fb162bc42b510883cc19078c0a070c87a97faf7614dfbe0d4544865378db088a8e89a48ef16d2821a609cf6950814275083b46509d521a0714e1dc8d334b2ce204562b8c1085016f6f1d87caac983b80e740c815d409b4977460d13c05c2197950f97225f8e7134676a449af74e2de1321141aa9285d1b33dab4e3f3791ee59ec99e798581a0cc3b5d63586824c49ee4e6857d8135aa957fb668d1143a74fe2a6d397c91a62e5a472b77bc43373e7b4c7ffcf13b4d9e389625e8ad68f880687ec8a2da04f392aea98419b325c865108e85340aa2d56b96519fc12328a0462d0aac538f9ab60aa776b171d4a2790825c6b76687a9210fed6a562f4fd3c6f7a7eb17f6d1bfbf3a9f3e1782b005215e568092302aa0019063a1c205c3458814d7ae517a29d84b354ebb70465fdeec04bc1e3d1cb0422237920b0d303290de21945257fff0981186b50cad95617583b1aebcb210c0741ed36aa2cc6b67326a60ccfa223e8568fdeac934f0e5e114d3b917cd5cb18519cb2dfaf2dfbfd1dfcf9ed3df7ffd46ed58f885be0612faf8a846bc1a65cc73a0d2d3bf672405542e4fddba26d0d5eb97e9ec87f768c02b93a94edd60da9cba854e9efd804e5c7b409f3dfe917efbf5097df9e98774f5d27bf4e1d5f33467ce6c4a4eee47ebd6ada52f3ebfc79363a089950b7bac551816423d24eef04638e428d922e4029ab97ad5b2e9093c0a10f2f7a1fa66cc432064847784278101291f177e0606a4ec9d98c36a79fc6320279836150692fb92dfdc7f262534ff16b7e191ddb368d1828954ccd7977c8a14a390d066b465eb367af2e407fae0fcfbb467eb3cea12df94f72f106620bf501b07c2aebeddda91af6f51ead3a7177df2c95d7af2cbef74e2faa7d47fd4242aeb5f91ea370aa1f1b316d2bb97eed1fdc7ff477ffdfd9c886435c8cf3fff4c43870ea5d8d818dab373030fbb6c0959c1e14769b955d3973818135e11c6020f63aac7a3ae56016203cc1a08f66070b2f7806795d74eeb325917adf2200798be248eab1dc9479776d50b6bfad233cc592054f8f4da3a3ab46b162d9fff0a45b66942813559b25eb32abd54ab1a6d5c3186a3684b9628c27b02ea9b15e107c08d850b17a4162d9ad3cd9b37e9e9d3bfe9e7dfffa2b407dfd3ca378f722381e2f353b7bea1cfbeff2f3d7df68f6128e5e9d3a714da2484da4584f2726f5612f4cc422f54a652160c66897b3c1fc642f865e9df41af050d4d849bb2374518386b42b70c219539226b15c91caa583ee254da918c7db9c336ecec408cae0c1310ce20b94575e9d481d97492e99cc93d78971bf827e5fc88d24050212a55b2040d1a38807efae9477ed0fff7d733faec87ffd2898fbea103e7efd2beb3b7e9c8d547748919cd935fff47cf9f67f41eb25cb97285ea07d7a5b54b46672939b79475feb1144a01da620d7b3d0c042cf5d1e1c13ca7c1eb01526ef594a1d2089461963ea3f7c02abc66a251686792dc3dfc11ca97e0a332771870b382641a4cef58fb7c570a29d4870d08e00e314d09394df1e2c568d0807ef4f6c1fdf4f9975fd1b96bb769cadca51416194be151b1141513cd42ba9974e1fd77e9deed34fafad15dfaea8b0774e9e239ea9fdc97199a1f4d9f348c1ed8e128300c09fd15e420b2f745ae840b42a30aa7bc8c509faa715e8874330b8585e4b25c3bbbe7a5c8d6f59e625f87391c16bc099a65a8eb234947a7d854b28cafa31cbb6ff378dab9762ccd9cd88706f6ed4009f16d292e2e8a6262db51747438c5b46b41316d439836a458a651e10da876602596d897a51ad52af0cad6f67553e8fee535b9c20d6c2ddc1e2115b8bb40f42d27e928f9ca5d7a6f33c352fa8ce3b53f31ed2b422b3b9491036277023488036f2eb440d289922dfa15188c4295cad2f005313912d86b2717d1d5534be9dae9e574f9c462bacafe8d100e8a9230927b78b15352a71a7f23af28502d81b04c1ddb855f1620bb40f9179708e0f71a23c044e5ecba7ac84a63188e2a264ea31d4aaf2e2d1f00a7642ebcc21bbf607a1f6e1c40a902da9d9d390e4756780e1803f05d98ba44f50a068eafe3a3c6089444bd0855093961df3f84691951d6b54379f8f0a157788b3a3f0246819abea90381412824a1184a42dd1fb7a72b1a875ca10268119e035d7840647059006e82791a8d090a20991b4bf38f62051e90bb05c549b453494b4b2b54ab86ff6f186c32b5390a582bcc70c8d37aa8fbdb5b3e90630c2aecd0ab87b5508000260be126baec320e0bec2f1d63434cae7f5355abfed21858138571388281005b640cc68da65ceaca911ca087556c30244be0decea018217e73ddab3cb4545e1e280363532f8c43ce8f90bb8111c6d42a6a5542febdc630f7218cc3110c24b0ba7103c1ad0978371a601897457917a196ab845280b48380023d0e255f315e1774f465e3906764e051649a20f56e130595e843a6573562dedcf10c4449398a1b11c40add135b705a1d3405115a999a7d70467d77e7649e84a72c1c6c76580bde05e8638dc2203c8c24e4ecff1f305dac11eb0c1c3307517a105463908c63bb2d4ac0809428e1e7aea0688222943247e68dce3b581e7962ae31195241b16602943ee5c4a973202122f7e888e0ffa08a05709e3c7d878381f987ea0165f8ed68ac63eeec2a0f7a992a67c3cb62c94ffa8c876986f6a752525e512378761d4fe2a31a7d8ef8591e7c423c1dd12a8853e7b46b5d8fe71dae52b5b2a61702e0245e3735c1b58a84010350c057551327cd41a547a7969750d3475546aed2604602937780710357847c44997fe0564592ea2c8683e7610d4011901c7060698cccb3ab90b9eb98d6d6881973c79521fda2e6a3c7b16afe207ee8115aa0f985aa15aa5750cc7f00120e2341528a92263ac9a872e514c236378d43de5c65499e85847dcdc221e9e40f6a0351ee0b61dfd34084550e2e589ed33c24f0ef31c3e2d32b59b84dd1104383102c8108b76030202840dc0d680a60272881c2981cd940f05c41f6864b02869f992701bc1da802533d0fe5265dd1eb701241a28e7c03d52bf56c07601598342c5aa420272800a871deb4debc79e85fb6386758b4f510536e2b8c1cc40ca620fc4a9a531430e0553db519cbb9f2be74054be278115a3989c446d4bf051a1be41aea4381f22f420a900fc06b4c1a9dc80d09b72ecabff6b4f4333b255d209483eb54e64360c60c1ea1152e07706bc963b49e2aae2bf78cb31d711a31f8e41c327a70ec9e2285f3731e2753640720961e3528968759382cc847d0077006542f9e03285041dc00861235941f790a86c5c0800203f150ad69d3be681c133406026a21ce201b578c1f0cd268103f9be39c45850b46e18c655f20947101a0f703ee2b0c43e1b580629416552b54fb3c8c780d157c7d9846c0d79d4b401a17d6b4d633902d9842f5ba4263103d204c0882dda45bc7e61c5e83ea1608e334124a57cdb3abfc5c63587a53589c282794f6910d9f80fd10a3b2aeda008491c0734c18d59133b92371c7bcb94601615752f428f78f680cfbcc0523a2b34a5cbb86f7c00f850eb1d1c3c3428def5d608af0fb4f52e99d1d93a90ecb35c0e30b823ce5021e351851a7d3cd652f5f4d51b1727219d4a7edbb88bfd1fccb7068401a7d63037d7c602edddc359bbebb6940b63ebeb5891ebcb7841e1c5f4cdf5cc3c24ee7f122d85152ab46791e6af5ee1a96ee39942bd9c084dfa06ed5cb1a015d770d59b7f2b509689621e656dea65f7eb09a2ea54ca0bd03bad3f62e1de9e3fd73b9915cdf329db677ed483b9212e9f49c9174f7ed79dc909e3878028f02042a5a08af602048dcbd5425dd4205f361d5da6fec652b2d92711791d387523aa20186f907197705cf707ace28dad629810ebf3680f60dea4147270ea6ab9ba7d19e7e49b437b91bbd35b41737949ddd3bd1d985afd2e7e7563a7c2806e80978ae50fa46595759d2454e826ad6903eed52c4a97121b970e142c1b66175ffc44e409994e1cbcb6be8d89c11f4f6ebc3e8e89ad7e8d094c1b4a35b27dadda72ba57688e3de636bb744dadc299e36b78fa5d4c4783ac40ce9ee3b0be8fb3ba90e1d6661d4166c93006cc230303486700b0882ce1d427f1427c60525312ef43b404ed014933bccaf0e88a5368d6b51083b203ddb85d0fa415d69f7901eb48d19c6ac5eb1141a5081dad56087a64e2d1ad8b83eadea10437b87f5e29e04bd1347ed9980c72b2126842f0ac2e6da2d2b47f04e3a6857c70c8b3f244e8b0bcad0e4c8a3b56af8a743df81d4ddb97c242dedd581e6f488a50e4d82a87ef58a14db3488e24283a87195f234253e9c362725d0e69e8934373e92ba304359d82596362f18ca5741db735f054d4040698c3547312c85c629c68e61189fb2e7015c1a262c3f3896d2539c161794918362dfa8e05f825771e4ad4e9753a7f2f009baa54b022d4e4ea0ee91213428b229a524b6a7cd2cd4da141b4b9b6262b8ae8a89a2e0726578fc0e2614c0e4ed3111079d0fa035af0def6074ac56deca0bda23207c013f81b1c44535fa1d9398e2b4b8a09c7a674d2cb877e74ee9c90f0747f49e5a467b07f4e039476afbf6fce3aee424dacef28fd484789ec06f4988631a4f5b3bc6d3bee1bd69f7cad1ccc8c672e203f9f7d8537e814d51005a22bf40d27dee9dccd95ac0700f5eb0feddc36f9b7909052183330b6ec684e8c6ffc51e7410a1c90dc27b4716d2a1d1c9b435b183c150e2dad35666103b59c28e0ad6c9d923e8e69bb3e9f65bf3e88b0f56f1f2b0bdce7ea0118ae59d0825d11805ef5766460c6f82300bacf6e34724ac1727c585a55fb7d61fa152831b53d945872739317338a5764da0e96d5ad2a4362d6879c7183abb742c7d999662f78d42e45387b64fe273e4e86360510e48b22d59638d706cca98ce7ca927d8f0c52971611935387633ca9b689665b8455968f2f5d5b5b47bf108f22b5a887c7d0a52429b0674e3c422bb6f0e221107ebbabcc679eee49e9cc5c5d2412fe0d33ac785526c44839f9997158d415796e307967447c3107c4fc688e23e3ebf9cc3c1174cedcdc28ed19ca2d39eab5448ae310588590f4c4422a4fad4ca653c003062e575bb36f51e8a13e2e272e7ce9d02516d82ff04dcdb18e33b0e9dbc8a196a8f7d0e3c261801e6e7310005084da7f64df86a34732c89e60c2486194878aba03be28408d1b0dbf2118809301fe1681d70a000d0c741ce803162904ebc3a348e93506475fa1123c6c24084a44bd78466693858d8f2e42886813c03f3e480caa01c0bda547841ec1004b62a3bc41220d58b89a82f0c4488413ac7357d0b31fb89fd331c82b607543cf018e8728358021380e8c320b9b6c50a371e62090311a2f0205bd151972127f6aac88150ba05660af074d963a0336e4b32091e62090311220b20276e6e5a0e39b1d7990d84541b97bd4ca10d6bf0cdbba8548182c81a0a516b7390a836f51e88d32144b33b756e47ad564beb970cb33b5a1f54a1b071171b66e135c0f2b83d6534af5ae554450da11a982563221afc4b9c0e219c8eb471fd6acfd11cb3276238206c610ce88663fb53d70e4df97e724bbae1d9a5047ab97f148534a8f62be666c40911a2e9dea9e50d00f9de98da8bcf46e425bd28126d949c0d94a755f8f0123e47752937f6a8c33b8117ab82bfdf1f583a244e8710ce95c562ee6f10c680d81ab82524c5b9dd0947b3127bd9c16a08083dbae180c1a059995b4d4a70f2620b57a50a25ffc4eb224e87102ef3e7cf0fe8d1a9c5e7ad9bd57e8683b972de409eb0e6c6e104cc05a4d2fdbab5e6e55b16de706f06346e6e780df5c6db6963bb629af0f9cdf3bbaa8a93212483b0f062757c54a3dfd17c43fc8fb56c18360224ded663b5f270122a5430ca72657cf9121fc044f20af785f072cbaa91e4eeee462860881321e40541ecdd2729ec72dbb0babf54ae5092d02701132316ef604d1b12e8ec180a0c03085bf431809b02a218b9064093e004ceeb150b683c7ab8bbd3c695533a8bd320c4a460a86af7e61903460e8abd9a1013f23b7214ac46401f02b901889ead01042294c2841f7a2ec9dddbf09e060c03aba94fe641dea32e0ec8b910aa673090f1aff4ea274e81108be4e1c3875ea307c7ee888f6efc4b8b2681bcf40aaf027203780274e2d38ecfe7500de42e50fc1b23ae38fc18c9c5ee11e419e09f02fe0b0501b03ba27294d75e03d53b7845ceee32348e0f5a952b536c9678e785582d678facea327270fb4bad9bd7fea360016fce6b8b0d54e12d83f8b6dcc8b07a7c651bfe8d4535c07a214cc3e62a800b31e107b0215850ec6963150c04c6cc571fb8bb3f2f50a0c05af16e0bc99657397d745ddb053306ceecd5a5d559e097dab4a8fd49f32635ef57f42ff12b0c045b74c126826e383ccdfb6fcfe6885b7b5dc6033a5618b256ab7deee7e7f7d6a3478ff4e29d166253618631128937e02b085de029502eb6f7fd86787c2844c02b320379e6e3e3f3e3be7dfbea8a7754884de59ddd4be2c10a62af00c8cc4abc654b177beee6e6f6b79797d7d33163c60c10efa8109b0afa07254b14e1ab961dcd40b097119b7ef57adddfdededecf9b3469b2fdcc993365c5bb2ac46672fcc8fa3034fd562f189ceb9df0ec26e9982fc18ae8122c44f4f32b4ea54b97a60913260cb0b19188053cae2c4008d7ac56ee19baf0682a3a9a1741b312c3593dbbb4a612257c293030f0f9ecd9b3bb9c3871c25fbcbb42b22d00f90556f7ff030d4024e78eba2e1a58b0ae1dc358d25e809a356bfa74e9d2a531d7ae5d138b3b85644f005509ace6ff3b50b1980674e49deaefbe3995a2221a53b972a5292828e8d99c397312c53b2c245b72f1e245ef362deafc0c5e5c74a61d790315a03487764ca6a4c4082a55d28f22c25b1c3970e0402df12e0bc99674886afc0da61481d772f4659f28341cd93d83a2239b51e952259f0f193264dc9d3b7774e25d169265898ea87fb7636c885d8df166975565e5bc2154ad8a3f952d53f2afe4e4e4d1b76edd2aa2312cf67413efb810ab0d04143d002d3acb5e7540f45f1b9e4001954a935ff1224f43424216f7eddb378e3dddda4c0b88775d88c512d526f8631808d0bdce6220280163700c707f802e8bf814f89be5258fead7af7f863de5108d58132dc45201701160453036fecbc157451b5bb100cc56a3e0aa54d4a7c0f3164d5efa2b2828e84c9f3e7d12e6cd9b8786a258db26c4b41c3e7cb87ca50a7e378169c27e0e476b165aa2e8ef60bd42d78466d4ab4b2baafb52c5bf753add9fbebebee7d84b10c654f44b84bc28696969651b366cb858afd7fd056612f442c014e26c06226fb242087964d7543efbc29e3e152952041f3f603a8e29365409b8bcabca937b5b5a3dbcb9aff38d0fde1c70fc9d8d63c78f1f3f70f19c917cc6bc52f99234a057049f2ab477987b76f312ccb9346d5c836a55f7e78c2c584ac45e9e2f98ee653a8869658dc05fb9869c3fb3b7e595331b1e9f3dbc82f66f7b9d664f1e40ad5a34a1c09a55a940817c54dcb70845b40aa239937af012ef0ff79cd738e4ca16c8ae872647f1b00b1389209e03e91d7bb9405f7a9f692ad3f64c7d457ee27ca23d7efc78fecba737a5a6a64c7b3c6644776a101c48fdba4750cac2a1746cef0c3a75700eeddf328d36ac9c400b67f4a577764cb2eb756db664611cdc2792ef54fff6ceb6f4af63400c9389401260763f7f3e3d8ce51ad3854c5b4af989a87839baac5fbdbcd9e553eb7ed8b86a1205d7ab4dc1756bd2d0fe0974f2e03cfaf676aa51ec92b15d87ce6a1c2098c37a69b0a0180bbd00b359b7782845b509e633fb3e85f2f3108cbdb4139936615a4c781407f4184c0b7efee1d6efc68eecfe3c34a42ef5496a436f6e9848772fada5efee6c75090330373372fff26a1e42b66a5a8bf30667d6813ff7ee1cee5941680142be32a58a129861d8eb3c470abd4a0943710cc3f0c0cdc66ebe5fbeb8b5ed79cae2d1f4d6d629cc2b6c7669a3502ac2a799e3bbf1a943905158cc09f6f1264e5801f20ab048c2484af8168691dc60ba0aad235c4ce218daa9c7d018e012bdae9d5cf41b0e010f993ec18d298c42496cbd78563f4e613462400cfbda36abbd0f72b3f7f64da7b9537a7256c98ae5fd60247fa0fec17438d36a4c0510328f0dc25d2a3b96641ac3f475a6c7aa0794a18b47de9058105d279fb044bfbab98152160ee64c907d92c2e8f1ddedd9460863a5c392d9fde41eca3752c56b33d38e52d82512f95c0c9dbc9982ad3c494a1211ff6ec05e10df628508240b755faa44b72eaca01573077046446118ff30be83ed048c90e89c7ffdb10d99ed59e8058f82a53d52a5eb6ba6579882d5b1a6e89fe48c4140d1c1adc8349ae968a6f399ee67fa00d59402f9f59c5e13b1707c54235e91d9bd610ccd9ed09d460e8ce189a8300e43920d7e2f5c1ea05abd7976558e341bb11b65f3f2e124f54cbe943cca0ea69da446a3b7f028d9f712281b064b46f132d36d4c3f62fa2bc898bdbd75048fd1b3734b1a352896268cecc8f78060a31326e7f046cd9ad08d279222bc321c5c802d41d88de1af4fafe7ec6b82b0ebe2d1793092094c6f31fd91e94da65b980e964ac37ed27b2dc442a3401e11c574bc540d798fe9558d01eaf06fa69f80277760afb6dc4b001601b6431800d610a891b693462772c31106b295230140b61d1d1ecc5fb7dcfabbd893086f8255d752950bde045b774f305dc0345274e54d0b2a1c15a4e47a0cd3b5d28bf8a36410308cabad9bd7e6712dc203d4e11147c31832837e0ceadd96460d6e6fd55a0367ec75803f181705cab9f3a7f7ce93315f6cdcc572a1217d23a97908ef9b3c9472940b5257be93741684479104215417c9e5c2f57e8b7c02cd27ecf6066dff82e97d685bca28be6710fc4ef012d6e0a1fa26b5a61e9d5a3a2c5d8fad6e70f43a0013c16b9ad7f3f0e8c81f7e730a617400b8afd2258b52e142f9386bbe54f5eac6b412532f57cd5350c90074fa0da6b799fe17cb6cb022ed15f606228f40ec8a8a08e26668564182c84fb044130c83aee839300c859d273502caf262853d3d3ebcbf8806e0d130ef8f0544f9bcbd602477a54b73a8c6c520f6b80db08ab81dd3dd4c7f02941a37fc86a5c3781d1d3309b69cd8c3f00f626e676023c90a8404d011bcc658dbf0c3fded76f958e155901f61651d2eb45a35fc6124df49e1f56e29f42ae1ccdec44d4abedb329dc4f41ccab180504f7fad2b7f71726ac602211616e03813d982a5655654f0ea0755a1fe3dc3e9f127dbedfe71cbbb1e31d908e470c37a01bc6ccfcecb65a96853d6d98cc44daa4ea04cbb522ad13e469e815b7debea511c459a93331628ffa25b0cda4d57300e1c3294b9f1bc8bfa14e007ed877bdb1ccec0916f22f744e80523011c869d9d454ceb3943c8251b464ba9a1878eeacf8048831800ab919174e7466549c61a6115b3b31b07627aec4e445809af8912b7b5f82abb7a3eb7377104047062780f81909042aec68e5ce9c2f04c6b29013f830a05c655d1dd46b288e598b9c96f7bf5d432de65c76e0f679e0644871cc801c0ced108c46bed2cb9143609a3e28548401affddc9b485a3791254a602994ed31886fcbf85c7c0000e660de0f67123e4f63cf737b7b7b25c27801c717581c5c978da2a0e3c44be01cff1f6f6c94ec9ba82ea2616924a95ae834c9b3a025218495311a91bba13cc1fc831304c03083442a9bc1e57eddaa12975886eec74952c18079e13d632005b854ef5f17d339dd64b22f280918436acc17b65ecbced91a295fcf66a1cf9983694bcc6e9527e45384214fbc2cf1c9ccdddbe3d8435305434c9b044c6999271141e30438eb1573400bfbfb7dde9f32c5cb6e8c6631db75eafe3c35a5263d1eecac0184aea8cb90b5419101b0223859965b87cbbea245fd9404d1a56e73d01f4591cdd6b20dcc05e76000e611cc0a4b9528f07cf1fe70c7015f95296461e2ad98391b8490f04b8a9344004e0dee1e65129b2d70308a60e74ea910b397255e7eca1d7396c04d54084b12bde18e8b2b0fd83db2650120b2b116e4965e01912943ecf2a5c3aa9c496c2f473780dc038c08281f14d7bae12e120a143bb63cd2b3c3c71248f81e93fa00c105a805801c93816737e74ceb5675cf03e029284323096ac6282949dcba54c1b4838ae5c1524e25d99becdf40700cb10f7024b83fabbfd2f8dd9c6e96a80ee05b2d5119a65484a91cb2d7d3d99da86d5a562450bf20b090d3431fcf58f82ed12e30f3893521918c97bb3dc3212b8abf24cc74a432fff064404b42f8e70d0943a715422a7f90792d45ee943652204301ae2f1a2a751b572691e22c20bba42229e154f024e61146350d2c764a9346d1a9ed315aefc52196d3dd3fb98f3ee12df9437a4001171b417f2e2b145bc4f30bc7f341fb2b2c71c039529a05b114a81f214e10320398edc11cfcde41dbbec51e19228888e6a0c3c5d8572a2b701160a10159f65fa1fc0a581eb41ebdf916278b54e7eb5334fea30e1662f5386402f03a4b766e1105eb644028ec9bf37d7bd2a0e7e16a88c36b1f71615555ce8ececbe2fcd1cd96c6316c61ec13e312ba052a974d63cd0b9a0e5efe8cb63d059075012138aa8bae5e5f341980748fa812de3b941006b042205bcc1e2b067afc2b567e358ee81a5aefb69a671b63212107dad014c04a533cc11a0c1e64c300dcc63e39606e50d3afdb9ed49e4a93aece1183da43d279ec0641d203922c7b01d701384db08a9a5aefb29c99314ca4e58e50f483160d298f0433882590d675b39065d36a73f7ff10041415508cdcd9c4cdcf1bb91b7a12c899c02d534cc3b2014c0acf8671f0a52899c3012786710d9499ee49ce449f265753e7c5cf97225080aaa1c47ab5259ab986043b30df57340f0418983d2aa2d0d05907e0c2fc10b037e0e83c06829cab6d856e58cc042bb0aa9d9ebbf67e36be944dbec8c1f91408e1ed63600e35022431d196e1ff9862bbc808f6ea4723c53b3c635798910b739cac0b81cac2d46a0448b3704de088dbde37ba7f3e598c82f2a5728c91b7ca0f9c48cca777744552a373dc9fed471bc0002e4073beb5ba566a2c570f9ea4c77c0c230b7013883abbd88b8c9416a809b1d6542500c61f4f3c8ae299cc51ccd287802180ea6dd9043608532422654f6e07df0262c67a11b7e0f5859d0b9c76b1acb3e47b8eaea9defbc4edc715901b45adc5002de2d1989bb2555ab5e808ea0890638832374c673b257022f02978c2a5ea0b4a70f873cb97b1b7ef8e16151f2060e0895120cf2e0fb0077c0cfe07bd1d9c5900fe0e78e36f2eaccab1ec0d38cf7137b27254fd238b38e3bc81452d07d4428e08af438c6bbb3dbf9e146e309c356203b834120a9c73c02cab12084004d118827e039d04045c8262a51761c527fb82e1d9787495376f67749231b2649b53b00ca80922e86519cb162655b28c836c9230823705822bdcbab795f0f9029297107c56d155306321f25308405978ecd132fa050975094dc010245682c011c5f65ea63cc408ee31bd0a472e5dc43a8eb298a2ec829412c82d926a6612f0c5c01228c8127741d9d7d07b850a1eae62daa94c82931c2210d5c65f422c8e651da75e4493ba142b393b4a3810bb21109d8d83c831701ac04df803ab178c184baa262280dfd2fccb7034d92618604dddd7d9bc789f04aa8cb2a469ac1a920f546764b805d8380a6f2ae1d0e0f09159a9bb908b80a404ac74ce2ba3464e5c60d24afb72f6567f78750a1b652e4e098f797261147a78319019bc8abc93ab4fe4fbf358b7f1446e258dc5c0072daeb5c7f56143c00c0df811554aa66193aeb6041c4004f5eb5fc41fe850126d1c1771ce3c0ae15406b70eb3a8b91c0498c1b91201bc8c2f4e9432c59c9edc389171934fd809803e20294a523cfb9bb92e23205368d1d1d9a32a6b3d32c47c5f97b7d52773951df9b0e3dc1d6d8dc0e6f808501b3086acf28af21cc1287cf716e5a0c7a614f3d665bf05e3a4ba20e3a536991e87969bd8256831986dc3410dc406014c1141fbc07b6d8a2cc260e9f6328a28d37a6f6a2a2450a72a8bfb36cee828160f210b340d2c6e5046e2020dccacd07825dd9e0d6d2e93c387c1c33efe2e039560e829921ccbde03061e6db19f2103caf0b87e7f2392066183f4adcd31eff0f57d8719ae15e7a2a0000000049454e44ae426082);
INSERT INTO `characters` (`id`, `family`, `name`, `sex`, `father`, `spouse`, `level`, `race`, `career`, `body`, `ears`, `eyebrows`, `eye_color`, `eyes`, `hair_color`, `hairstyle`, `hair_type`, `head`, `height`, `mouth`, `nose`, `skin_color`, `view`) VALUES
(4, 1, 'Зоя', 'female', NULL, 3, 0, 1, 2, 2, 4, 6, 4, 1, 3, 7, 4, 1, 5, 6, 3, 3, 0x89504e470d0a1a0a0000000d49484452000000c8000000c80806000000ad58ae9e000033f54944415478daed9d0978d5e595ff2f4b1612b22764df13b243f6952440420849d802049025b22302826c222a06171044b12e55eb86552b38ad6d6d3bfb38ed7fc67fffad33d34ec799eedad62e76a65667da9967ba70fee7f3dedfa56924244820f7debce779be4f4272b949ee7dcf7bb6ef39c7e5b262c58a152b56ac58b162c58a152b56ac58b162c58a152b56ac58b162c58a152b56ac58b162c58a152b56fc47c629c65f04e3ec4b64c51f0ffd40780efc044588224551ade8566c54ec54ec56dce87ce4df2b14a58a50ab28567c5d218214098ae98a398a1ec51a45af629d62b373f86f511c579c56fc55f2e440899d3451c2032788fefbe7fd91181ac0d7fe4c71403143116b15c58a2fb945dcec258e15d8ab78b27c4a88648407497c488039f8718a184554f0440999385e82268c9389e3c799cfa3f56b7951c152191f2a8dc99365664a9841537298d42684ca54fd5e8c3e86c7e973ff8de26e45a7628af3f3ad58f12a99a888775c232cc3d1b2b81049d29b1e0b103461bc84e9c714b50af9d1c152123b49a629f888e2a004f33222a43b274ad614c4c875d3a6c8ad354972a231551e9a95268fcc4e3778583f3fd59c2ab75427ca8abc68e167a0688ea27c5971489166ad89156f518a1845ad62abe221c55f4e510b8115c03264450449b5def88bb223654371acecab48902375c972b421458ecd700325e0f03fd396292fcccb92173bb2e46c47b69ced74e3a50b80eff378feffbaa25863555014fdf9df569c50342a22ada258190d0950642b562aee255e507c4ff1e304b518c53193646e7a845c5b182b07ab12e5bea6547398cf7464ffd1c1ef8f973e24f8bfcfb767c9f1c614e9ca8c1494537f8f9f28fe5ad1a798ed28b155142b575c8215f94e66e939c5bfa014c414b84a1cd0eba6c5190bf1446b867c629edb12bcd479e581f2e18aa194b86cfa7bfd50f123c5df2a6e573428c2aca258b9528a314d7183e2538eb5780ff7a92535dcb84e77d627cb93731ca5e8bc3a4a71213c3b3753ee51b7abb7300625f98ca3243f7114e536459722d771bf26d8b7d6cae508072843b1de518c7714bf527cad2b2b520e542698d8e18551568a0bb95dcfb5679ad8c6e54e037fd1f9ddc13f295e549c74e2262c4baccd7c59b9548970b96b161f55fc9be2edfca86059a8c1f6cdd589c65a9ce9f01ea5180ca7d5a2f4d525c9cabc6894e5938aafe316badcf5941f38b1ca1d8a1ac524fbb65b198e242976386e0987e81bed191126edfab406db677d403106c627b85e240bf6562478dcaf534e72e1fbcedff879c5f58a3c45a03d0256068b35ca14c79c38e31d52b4dba74f31aed4191f538cc1d2c3c4494fa905bc4be3a6b5053126eba67feb7f29de54bce0721737a36d406fc523f8dff14edaf665c705f91e71067506d2a8beae1883c52a58963b545116e8df9a161688a2fc52f18f4e400fd72bc81e0f2b598a238ad7498f72a3e28650b53edb91ed97ca315051701d2962ce489aeca9a590fdfaaccbcd0a48b047646c0a41699de251c76abc392b354c0e69106e628d4eff578efea082ffc0cc34e975dcae303749f25f15b73a9788cd748db178a355f18ae2bde4c901b27c6ab47c440f083efa4b634c3906d652c87ccd490b9788a0098658e972d3682aadcb357694639e93b9796b7a5c88a9803fde9a31a615636030ffa05e1690279dd8e42dc5279c74b0b5247e2c3424cd75b97b297e0ba316bed4c7f5d6b48af1c1d8844b6373499c50030a0d188fa23cae986a95c43f85caf84cc517b8114bd57210985275b60a7171976b8b2a8963497eea7273bba6d8e3e45f02359d8ebee7893920f4d153e1af29dc9106cc819ea9d11ea6f0579dec56983d56fe23a92e77f5f827900c6f288b57cb6195e35270aa394d66a7867be211ba18e939b1c5443f09cae9fdfe17fabcd715c69a4ab23df4979e06bea32e599ce4c6bb2e7731d15a113f9042c559c58fe054411bb107fec381786d75bee172bdadf84b45953d5ebe2d818ef5f816830ea0a87313dac3fee171c46d45bee404ecdb5cee0e4b2b3e2a998aa715ef74a8f578b4c55a8fcb05edc31d991128c96f9dd736c51e33df14024806ac7d373d3c50f694c75beb3142f591ddfa5a3ab3b9e82fe976d9ee449f1486153ca8f8fdecd43079d4c61e2306e2b8e6943014842ec57b5c6e7abc151f122abd70adbea2f82154126b3d460eb419930d74b959bff0d9caed91f32d0977b92bbe3fa5e9e96453ea154d7f8e055afc40378b0e4b7d7d5f73b9a7bbacb5c1ba6fc51e158a3f858dcaa4c22b5114a4c3107af85667d4cf58a3ac7c54dd2c5c5727e5cb4ce1387bf47c27b5cbc48eef31af8a8eb991eeed80a242cb2a7e38243e488f8cfe194b96a49f9b45ab2e2386caecd1f30d4927fd18a5d68356529a9f469abc07c9113e173371692c8ad49fc5e490d3638c158c9b457dc9e59efcb2dae5e6bc59f1f2e07c11e9c78cf040b9710453bbdc98f737a79a76dca991c13261fc78c94a8e93929c14090d0e325d78876b93c65432e0b19674694d33fcac9f298eda6c96f70b6b076e8bd05b1df787719c23551cdb5fe9eed9664874c4e41029ca4a96b59d0db26e7ea3e4a6c69b75046de9e1638acac265e0b8596fb9dc032f4aec11f46e81d2fe0a7b37b8e95fb8ccf6596217ea27f469d334c4f3a625c4c8ac8a7cd9b674b61cdeb4506eddb04016cf2c37fd1214cf3616c78e29578b417a2ef7ea856f2896b86c3395d70afdd2db38c485d1934c5ff5d9cb500c0ef9edea32417064d247e82475a3b25364655b8dec5fdb21b76f5e2447b62c36d8b3aa5daa8bb2ccda03f67f50b51f2b592d32797589933dbd22bb5db66fdd6b8595041f27686644e8131f92d24e107e5743b25ca34177811ef6a0c089121d112a0dd3738dd5b86de3c2f38ae1419f2acba645cd929f9168f682903da3e764a41304deda71c8fe137dedffc3e51eee60bb0dbd34385fee72785764992e751222fef4292708a71d373c7492c61a93a4203349163597cbbed5f3a46f8062f4078a736dd70c295025090b0936cb73f0cf9ff4f3de1352dbf4ad3b71c89f3a35282b5e268cf33f3961dc38b392e0b14b64ed72d3ef51a5f26c6a9aac073c3b658accad2b96eb8dd55830a862fcb1922c90f50b1a4d666b525080b0508771424c08f1e71ac94d5589327efcf8afb9dc5319e7bb6ca7a157096f06bb015fc5bdd9543c7cde1533b0d8a7b134374ab03ca46e93e3a2647665816c5e3c530ead9b7f51ab712110b85fb76496d41467497478a8895fa0da639dfc5549780dd99718e3de95b8c965d9bd5e2514a73670108919680b3d3b8c209cb6db7d8ed5202d1ca58779fad434593daf4e0e5edb75c98a313026d9b5b24d66a9a2c5464e168a96ac656367873fd649782da987048c1f87823089d1f2b2bc48a0b59f82f2c1a6d8e1f8fc04f0c40714fcb8f5329362a5bdae4476eba1ee9f9dba5cec5f334fda6a8a5549c24cf0ce365bba1a9ff1b3e01da55f9c1365d658bbdc2ba983edb1f422f70a2e14233209162f7643bb03f13459a62e15031c20331667271bab71f3baae11538cfed8a74a82f2a54c8932813f4ac99c297f52121222c45aacbbd6f7e3015b51f72ef76a1d05baa2984986557b76088221413c5683f8a0a6284bb676cfba60ea762471536fa7a99f507d2778cf8e089255f9316616b0bfec1d61980375207d3f9e72b9971059f112f7ea3ec88214f406ab7d7808860492d044126323a5b5bad0b8547d23e8521d19220d4cada454e39cc8b010035c4294c41f1484653cce78d2675c6ec2a8152fc95efd39a959b2570327b3733b7300893752e3a365bcfac8c41bcb5aaa4c35fc6a28467fdcbe6991ece86995e6f23c99121d2eac9086c6f2ac8f53535010fe8ec96e0539ad48b3c7d33bdc2b46607ebbf802ee153efe6d354926bb42259c1b1baac8da8e7ab965fdfcabae1ce7335c8abdabdb8d9260cdf2a282655759bc4f2b895190c2f30af28c5510ef101aa36e865a82127ccc595f40ac716f63aaf189b11604c6093111d25436d514fda8538c9672f45712682b24082605051a22e4ced2789f25397a1424d42a8857099992fb50100a7ddcc010e7c8108504071972614870a04c4d4b9025b32bcdad7db5e28de11614d7cd775353ccefa94ac2c2505fcc6e5dc0c54ab5c773f40572e2598a7cf469a024faef33410113cf4d9c3041a6448519866d6fe78c5175a9860adca1a64ccb4935962e27324876944ef1b97d25038274ab205e22ccdc7d050b12e5a638bc336edcb89f05054e3c47f53a3b798a6424c64ac3b45c933df206d76a304b02ad0525090c9868d2d5bb3526f1254be2b120a13648f72a6175f341c5ab8acf291e0b090939121a1c748e1a078cda9888c9865f85c2ac9c5b2b87ae8225c18d83c3b567d55c1367dc78cd5c93bd1a4a49362c6832cc613272c42464e59ef091b57028c89a3f28881d47ea4559ac64977b532d3bbd931a1a1a8a2605059c23fe9859916f52bbb85b0113271865e9689866e81f231d8b1074d35908ff6af99c6a69a92a94b2bc748953378f18030b3154311225f13081b18a9ec5a20ffa403191ce4d67c1ceaf151fb19574ef92f14e4dc4d5dddd9d327952d0b9fcf444d9b97c8e5ca35683142f5604f7856098c6276a1143ddeac3a5b7a37028406b759119e2408315e9dbcca438a324b08339f4abdaeb86b460e799c01a3b51edc79a5000852dfb82176fdf2583e82cd7f94fc52197ed2af44e79e5955742d2e2637e373d37cdb8381c600e263c280dde0db02e1c5ea81f97c2dae57107afed3456627b4f8bb9edbb1a4b4d659c34727060c0794b35af7e9a71af76ad6893f9fa9834b56480cf770fe172f173f87f33cbf325233cc82809d321199ffaf02cef5c530d4b9931482ef7bef92d2e4b77f7aa6a3accd108c524de9859e579ef4f890a97f6ba624314e456eeed6c30f41278501c627d9cb12c4b665518d7e862f1043c2a6ef5a5b32b8d32f03c497191a6db10a5e03901cf37a334d7288e276bc661c76ad069483b2ee381780e7e1f7eb7c198c3fc5c2afd74329ab6df09e38ca2342587992c179353bc89364fff7f9e7b3ed6775deece4eab205ea420018e92c492d95ab764de210e2bee5463d95453ffc092ac985323e90931e6607358e7d616cbf5cb5a06cd6e79dc9dba926c538547b1c68d1b27fa038d62e0b6f15c58a3393545b27161d3a00d563c173f8bc731150505ae2dce96deae868b5a319e8fc7f058a6a64c9a38de4c4e817049a60b4519edf884007ddbb42966e28bbefe7fa798e1b21d855e27dc58b3142f2ab66724c4be4f704e959ace3edc2ce20498bb648be82fe796e7e0725b73930f3ca4649f7252e3cfbb4e13278c37013fff26f8e6b0933ec6e5e2b986e3aa41a94791181b446c421281809edf6d304be6b1262bd425a4ae13afee5c5868b0e9809c93166e26a83cde327a8ac2f4167a41f475ff1fc5134ee2c48a9709568479bcac05fba6e2753dc8e770a5089a891118eec6a1ee3f9984db1bf7c77d93777ee030c3970a54e580e48882a070950519e6f1dcee9792e5f2b8531c787e16ae18cacb73e2b2616186223a620d7b5aabcdef00d991409e380545d95b91609ac5ae765b2f64d01a8d915ceea926fb6c80eebd99ac4ac5a715bf50fcde817185c2f5c6c5ff9fac013a56c19351daa81600be16413c5312fba77ff9fc8615734c1b2e556e826fba0309fe39ac7c1fd78d1885c7117c1fea6795fa1f6c68f53c7f7f06b1b10c1a87ace9a8373df01cfee11616f959d067c89251e321f990323950dad3234c8cc2ba076ef6e1b41e53b5c755630a25e073683bc39929c6630e56251a2575b9e7f32eb6ee95f74a90530fd9e17237ed7c3e7452d0ef2670f3ab62848604cbe4d01019af7144d2941899575f6214c54368643ae2c0a0d9e3de6ce99e696ef8438e2bc56126c0870049d04d818f4a3816ca04e0eac2a13c58215cbaaac24c13c45323e96f493cee1d96ec52d3ce5841b25d1e4521b58c0b48bc448c450d85f6dec72ee07e9d75d8cea48f294842d5a1371ff03955f1beda24b3d3f162c900326a3c96ce4c7dbd3fa528b2c7d0fb8376e21198be11b5c5396f64e5a74b7669ba54d4564a6151a104a92521d80e0e0ad2439d223929534cea9714ec50054413b8ab8b363d37d524013890e3d54205ab9b842bc7d7882da6e9f74902d06a8b6b8702d2974e1c41ac8105a0cd17f70ee5b85caa0af1120a4bfd8474323f0f8bc900efaecc48677f49d6f9b663dc220e36490b2c23890694bc5001b993ff4b0b33f38d59eb30d87e95c79de1d593dc9d84772926db23e843b2ad77e1e9ec865c496b5664a4c94d870e4a4973a94ccd9faa5625581213dc7c2d0ec4500ac24d7f7d4fab9497144880ba340101011219152971f17112151d25515151929c942861934335a89f28935459b8cd09ea39b054d4896966571598c22171117c3102f4cbadecf7398a82cb467cc43c2f2c1ac9042c0b83f098a1cbfcaf43fa917431e96a14b9a7b5ea7ccd8684030556ac20bf2bfdf370c398123970942a568869f6a47793dc0b3d57d9f4ae8fc9eeeb7a8e6536e5cad465a51259192fd10dc9d2b673a13cf2d4a3121313234d8d334c4c8205e91a4241883d6aa6e74b70b01e88a42499dbdd21b51a68a7976549c5cc6a696c6d96926925121717a7ae5ca85120accbb8712e99a8813e7102d57c2c0d168cc30b91924339d25c30dcbbedcb5aa5b361baa427c648e08471663dc33267fe175c35ac1bb1d38586e2f11c077a3b4c319502202ce33b062c08c21231a83bd69ddefdbccbee29f43d69af2ff987ccf26c69b9ae53f25796cbb4de5a39f8e861f9fbafbe26292929525b5323d929f143ba58580fe287e8c808898f8f97951b57495667918415444b6a76ba3cfec4e3f2e8e9c7e5e8c3c7e5f4e9d3b26bd72ea9aeae560b95602c0bca82554149b01c150519a6ed17e5b8122ce35bd48da37392be7bba2871fd9859853b850b45ec457cd4d72f9140568eaff58fc33c4c046a302bf2a2ff8861cc2c2cf6a587079af8834db751f6c4f9989417a4ff36415da8f93d0b65cddef572cfd327e51fbffd3579eb076f497e7ebe64676749a2c60c64b708d207531082730e5b4d65b91cbff7b8f4ec5f2353da338c8204874f92ecfc1c299e53266b0f6f945fbcffae20efbdf79ebcfdf6dbf2c52f7e516ebcf1468956378c409d9887609eec57df15621473fb9340c07da430c918558a8d04f4f0d048336311298412075144e57702fcbb7fff0c19385cc2060ddefbef5b21c0c7bac4b92d08f412bb5dca97e4bae5f33ecb0129ccc930b1467a7a9acc9ddb26c7ef39268f3dfaa8949595494a7292518e140d6ca94b0c766039541cb4ad1b7ae59bdfffa674df7c8dc47765494c63b25112c0e78b0eac909fbdfb8ef49773e7cec9d9b367a5adad4df2d213cc8d8e7b73b9c1f9456b269bdd3513620b327070c7a0b6605908e8a1b0907d23a1e0497fa3bc58185cbf6e0df851a23e47d9664ccf352e1aadcc9eeaf9eef27863595ceeea79ab4deffa98541566fe1e05a1ceb0a5a75d9675ce96eeae36a9afae90ecac4c89d11b3d212ec604d030712f766049db52c5aead2c9363aa6007efbf55f27bca24615e96c4b5a41924774d95431fed93f77ffd9f6885fcf637ff2bbf7aff97f2cedb6fc9138f3decb859134c60cec1bdfd2ab7ffa21c04df58120a9358b2fa6939b2b4a5d2580d6a4264d970c17003bb1d9e1a6e17d57e8271ac86a77a8ecb15e54eef3eabc8b427ce87a4ada6f81dd2aa04a9b80bb73b4d4cd43476aacbb07949ab349417cae4902073ab736087ba915992b371499bcc9fd72699591932253341620b1325a62441e2cb52a468e674d9bc638b7ce4230fc8896377cbceeb36c9a93b6e923d9b57c9d2f666c94a4d34ca41e608f7e66a772ee24e45858518fe5853599ec99eed77c89cfd53c62811bf27170256c8a320f9fd1404578b7a49b4bb93f380cb4d14b5e20b72e2c0d64da454a163e04e5ce8b0a010dc94a440db6a8b2fcae81d5873d8bbb6537a97b6cb8ca65a49ce4891383d4c896949929d9b25f979b9929b952ea5f9d9d2589e6f2c13412e1c30536d57e544d146a3fd175709d70e6b71402f8ac1e22dd2bdc41c300878fd5022089b2c05a242ef49efa2308e8b057bd7ae5df31569ad2a7c1ffa05f5800bb93164663a674c37d3dc49813259a4ef126b0e3cc76eb5029bd4bf5fbfb85936ea6dbc456fe81d2bdb4dad64971e2c0ee4c0acd0a88e1ac28a3ad6f4628fa30e5298956462141484cb8446300a82ec6ba47acee4182738ff82cb3dbccf8a2fc8abafbe1ac9fc5ba81d17e23671b8712da86e7bba0b4763c2a2b702e5c1e211ac5328e4355c3cb3c2b000ae9f3ec5743542886464aa432fb9cf6557aef98eb454177e9a2c0c35860bf58073ab37964e35ca81f5c0ddb8dd8b66658daa726c5a64325ec448d055a0e593fd82d745259e0c16ee1503c0c968b9dccd512ccbb1bb407c45f4f07f91d4257d1a1772ada055f07d620ffcf14ba1acfb3afa9c4c166ed3c0c62e62302c2b29687a4d486e603d2074eacb6a5c2a265662413614c57ac62cfd85a2c6a6777d4866944efd32d4756ec281078440998c15695d02506a1b63c9427826394e7788941eb2241577ea22c41df0b3500e8278623802f1992961e70b84c420b352c33c6b0e4eb8ecf412df54109461e0121b784f14c50838a98d78eb20b92b119c53b527838682c0d8f59018239db42f1f795d16349599c7623da897648607998c15bc2bdcab5b6b920c2f4b5feaef2956bb2c39d1b7a4b93cff356e41dc85feee033c2b0e01d56266635da96d52de080e3b695ad2dabc2ea49b290a6221a879604560116c5fd6626a46004a0a4d50ccba7a6ace1f8682b33bde190ec7a0be52eb5ef99874cfaefa0c2e146e439f737b128fe05ad1da4ae578b89d7bfe023a196110d33a4cf2a26f88395fb85e5893caf85039d198729ec18b9bc58e457d99df531c76d9de0fdf93fb0f6e5b0a85821656ac849bb4e7dec301ed64c3c2a661d53c3cdd7efea02054d049db62592fc618c0e5c49a5045c78d6218c4f34ea394c7bdca8d34a37dbea358698b833e2a35c5d9bf65f20707033782c921502c68221a2a6bc5212178e7a6a51e803feeeb0a42ba1bf78902e060711796c3a31cf4b6b3aeae7f8314c541b67439d4922f38ee95155f94e56d355fa21576594ba549e5e25a613d18430a07ea62d56d2c0ebc232af104b2fe1eaf7826ac4052a4ed16cbc1229f812db6f4b5b7b8478b321083d6da307bd27c54de7cf3cd602ae5a579e94641c8ebe36251fc221681ba4d70ea199470bb672e964368e4b0f03843839f12656e567fab87f07712acf3b7d103027b97a07c7f65c2055b6b5965e74c4efc9675affc40d49d7a97b9514b67579961044c3261fc0f302372f4e0d795e4987a00adb6344b11d813c0e356616db03a9343828d6b365c32a33754c3a9615c68909d67fa3ca95efe262e015a6fa18c309c8118e34203b2f91aab0d62dceed5cb8a127bc27c5c0eef5c731f714747fd3459a3d6c23359c4334397dc3fae175682f42f8fa547021a0a31086e15eb1378cc60bc2e6f04cc5b92120ccff6a474772e6f359f7309502035db75d5625035671ac9dcf47093a11a6cbc0f73b218f2a02feb3b8adb6cf6ca0f4444c6954e4dfb3dbddfbbaf69330785e1d3140b675516980384ab559697662ac864beb84d519626679e2f413a013e05b3dd233c58e18a51dad57a90ca2606e37787f6cfe500bd864c164138634f97b654198e55b85a0f966f0ea61cb8574c43718a835f572c71d9d65aff9019d3a7be4b3eff7a27b5e96943e5362530c54ad09bc10d4be10ceb41700e8911a21eae16053406c6f98c8bb5d9339eb4caf4c3b04517605560e592e6e56fe7230a42560a9ed5600a827bc5b660a773f04f5c76309cffc8bcfa92d7b93539e47f14a0aa7f7ea1038f7f4e4c42168ba6a13e1fae8790ce855ec3803aac1fd939fee63ee7352049818561b01c29ddc1c68ce27ae15e390a72d866affc486edfde7b372e138761b8b72f56838335d4ba345f4fed52e7218d5d1833c94c5d3c3b887bc590b9ac8820334bcb65f77ef897d07a4b5cd17f1eae855b41ba1c6e1a9bab68a31d6cadc1cabc688970cfbd7ac9e5de2a6cc55fe4eb5fff7a287e3681b93f54c447d2fda2571e66735b7ab83c31e7c29b748f37a69846297d297fa2b8c966affc50aa8bb27ec300029a848eae5f2027afe99053cbdbe5819e767968699bdcbf729edcd33b5feed83c76148458848404338397e4449de75bf507eb0ffa05e7ccbd62399165eefa9b3497e7fd9ca2e0ee4533e5a97933e54fea6ae493b535f2a99a6af9745595bc5c5d25cfcd9e210f75cf9107501c050a84e29c58db2577fa612c823565a40f1604fafac00c16ecddbb1a92655aac69abfd77459fcb3646f9a7cc9f31fdef2806ee6aab33caf1e9eaeaf34049cecea8732b4cad5b713ee92810e07bcfcc6d968f2e9e23275775c8dd1b5459fc2096f18cf521c5bbbe28f603bb434eabf558a5d6435fbe1f2afe46516f4f929fca476edb319f546f474d91dcb5a8450ed797c9cd15c5727b7db9dcdf54234f36d6cac79bebe58526c58c5a79b1a156fea4b6fabc85f1e09375fab896467970d95cb7a2f8b082d01f93953c459227079a6d54fd1584ccd5d186f3b1c78f9dd823dc9e243f152aead372527e579a9b2a3be6cf90964c751de2a2242b7cb24c55c5999118274b8ab2656d59beac576c2b2b909315d3e5918a5279b4a6423e5e5f6dac895114ac8ee2745bb3dcab16e54e1fb426d4406008c44787cbd4a86093c63d3b2073b5b6c0588f1f28fe5c516b630f3f978afc8cf72b3292e5546b833cd3542b8fccac937b9b6b647fed74595590258d3191521b152ee58ae2c830a9d28f6db1d152a03e7a557484accb4a937b8a0be585f23279d95114accd7d1aa7f85a704f8a97765a52bc5809767ef477af48f956c59b859c3f52dcea72ef9fb7e2cf32aba2e007d50971f2a2ba4e2febe17eb906aba0d08f67ea6be4e375d5f2d48c1a79b4b94eee9b5d27c75bebe599861a393ea352b697e6cb8adc745991952a5b3252e5786e8e9c292f9797aa2ae5be390d72c4472828fd1564617399216996a982e04ef5a795403b71b9e75dfd1f977b62bba5b5fbbbec5cbde0ec94c04079b0beca1d5b54579f77994c264bbf46007f4683f2338d75f292c621b8522813f1c819fdde0baa48cf299eadae9487a715cb96d42469cd4e91fdabda7d4e41e891090a0cf8808230d267b6bb29ea678a538a047b7ac680503084d1ba50adc1636d4d2663f5870c96634d4ce6ca1d9ca32c3cc6137300be8fe29cad57e8f78fb4d5cb9ee5ad3e373a0805a15f9f350cfd1584542ffb3ef4e5fa67c537142b5c96b53b76a4aa30f37fa7aa92dcd43d5b9e99d3640efd404bd21f9f72d2bc03b359589b273b66c9ddeb177855e04df1cfec671faa8abe71a1a1c3b31a9bed502808413a5b6fd920a52fd56f15a715e9f6d48c2169ab2dfa0ee43c888bc7d77699fac68b4df5f262a3dbad4219ceaa85305642f1fcac19725aadcd6955263ef2783e7f4a3f1eef9def15f510cf3a69d8ca2cbb81c63f14c9f2d0ba2e33b57da22a88c782d07fce3851675afb3f2916bbecbcddb125c70e6cdd420b2d2b980ff67618da0995f2136b3a1d74fd01fa75e827c7d62d50cc37758fbb362e52e8c7510ecab110f4b130b185fe0e4f87203de5b4d10e3560825e10a6df5303c1825031f7ec1a0c739312ef74d9aaf9d894ba929cdf108b5c3fc436296f9e3e422317fd2a283b16910e41862eac9bdf68da6d87622def5f3bcf7419a686059a85380c63b8b6d0bdcab9c44d2d69b0758f312a736b4b5ea74036d86a04af9dc8be7ebe19c63db33cdf580a7aebb102b8557406f65fe73c14689e62e32d3d1e4c29d93a2d4e6a13423d031968888ab427658c0acb75185450539c659aa27c8175cb0009ea16ccacc295227e60e20a07ddd3217829cf497b71ea94683321913ef3cecc0814e3df14af29daadf518e3d2549af7ab74d6390f98feee6dee140acc14124616318184c1d30c9d202867b4cf877dee6d1abb6045518e7e2b9c7fead43d92ed0919e3b26c4ecd5f33f6875bd91b0731f03be14e918a65e05d8ec64c4c771ca9c1115bf5b9e322c38c8be528c6fb8a2f2be6dbba8715d7ebafbf1e50989574ae223fc3ab665df5391926e22312090c9ac61524753b701bd4e500e59ba2c17d7050c07febcbf13f2e77bfc7019bb9b2725e5aaa0a7f49a0cb121d6fe855a7c0476cc04415c6a3a220100a51e091fefd702d71b1820303508edfb8dc9c2b3bcac7ca1fe4fe5bb62f625e1613134733584731680526e826c6602c2a413856e34ab97f64bd18221717158682bced72f77b04db5361e58f644e4dd17f7193b22ce66a5b11826ccfe4460a7d4c5ea1aec116ac1b2f33081f4e9ab7282b89e0ff772ef794441b985bf9a03cd4b7670ee34659794c2af56a0c87e3e0a30028252bd1b8c9e9976fab2936aecfd5481a503361f42ad9b1d3a74fc7d89360655099d730ed5b4c3837b377f5e0f65d617a08d56e8fc5a0e0d7303d57362c68baa442df48b8753dadd566c4eaaacec6bfb4a7c0ca45657665c1cf392ccde579c6ede9bb02695b52b4f0a4b01664a798d305658441d3a3912480c7452d88d9c5f6045819529acba7be8725414918663d12ee16071f776ac9ac0a33241a8b41004edc01676a34e7fdb2650b4b5696977ecebefb56862533cbf37e191b1966561fb0e893b8c43da377c12571a65030fe2f7146797ebad93b024d645173b959c1e00d0d569eb5d858b3a37b371db4efbe95e1294945de2f49b7e20a518ba09a0ddd833565b84414d90001354ac034780e3da953948a79b7ec23e1ff8604071ab66d6b75a1f9fe2d1f82377525c196296a419df5257f6fdf792bc396eb572f78a4b5bae83baa1cbf2ec94939c721879a424a18c5a190c7fe103260285361669249d19291824c487d455d177343337f8aea789f17f2bd505a2c487359fe2fecbb6ee543c91b6fbc1178effe2ddb6edab2e2b196aac2bfd6a0f64b8da553bfdc5c91ff7d8d277e111c18708ed885a09b207ce5dc5a73f0f0f16ff1f26927c4474c572cc94ef99d7da7ad8cb8345514ddc2e89ca52d9526562155eb4b031c3c3b18b188b7ddbc7f8e7d47ad8ca8dcbdffba756121c1b27698cb79bc9152bfb2adc6d05b12a223fefd631ffb58a37d57ad8c981cddb3699b2f2b08a0f5182b38cee5920913267ce2d5575fb574772b23a7201cae5e1f5610a82f66885cc044325ae79e7df6d919f69db532a216c49715e488b3d09475d834676d5bbbec7fac15b1327216c40f14c43de9a4436aa7e5495a5a9a7cf4a10716db77d7ca65cbcddb563d6a2c48976f2b8867d5f5ce555d121e16261b562e7af78d37deb0bb08ad5c9e7826355261f769e5703ebf6be72aa9afae90d4d4543976ecd80afb0e5bb92ca92cc8fc5f2825b4cafab28278d8c4f7ec5a2587f7df20090909d2d0d0f0fce73ef7b962fb2e5bf950c2b47886afb129f6406f87cf2a87a71e72646bb79cb8718d9c79e20159d1b3541213137fbf68d1a2fb3560cfb0efb6954b96835b56f5911a65b0c2956c8fbd1aee15b84315e4e88e1572eaa64df2e091bdd2d5da24b1d151d2d3d373cf6bafbd66a79d58b93429ce4efe042bcc60c4f6f9b07be5f91c05015892bbae5f26c7f6ac97792d4d9299992987b6f78a0a5ba698b838c181152b1796b367cf1646474cfe1a54f1054d656666952fd24c3ea0d828c9754b0ceedbbf41eebbfbb01416164a7676b6dc76c37aa9aaaa62db548b02de96ed65b7f24179fae9a7abf4c3df2a7e1e121c780e362c53427c52411cf4fffadddb7be4ceeb969a78e4a5274ec9cd7b6f90bcdc1c634936afea96f0f0706668fda3a24711684f8495f372f2e4c9058e727c372c34f8bfb1200c7af3450be2519281fdf0b859776f5f6edcac7bf7f4ca83b76c933bf66d95cad212898a8a92d94df504f0ffa1afc1e38a547b2aacb860b9767474dc101818f83c748cd0e020d340559697e6937b46065a91816ed69ddb96ca7dfbd61945c1ddc2a21cbb6d9f141414486464a4d4d554c9c48913ff9fbe34f3ece918c3f2f2cb2f97ae5ebd7a5f637981e99988981c22938202655a4eaae92fa7e5d6d716780ecc5e0dfa38276047419e38ba5f3e79e67969993d5bc2c2c2645663bde16de94b744411654fca1811ba07efbffffeee4d9b36dd3273e6cc67929393bf909a106b7acba19424c5459a810edb97b50c6b59a6af54d0070396838f4fde77447a962d93b8b838696baa95358bdbf5b29864da8ff565db6c0376ff15d2963057039f7beeb992aeaeae87b82103020264e284090ec61b0561862f83aecdb81e1f2726f677af70a370a98cc5f0b858aa180fdfb65d9e3876404e1eda21cb16764a4a72b22ceb6c917bf66f3233be9846893575b95746ef52c4dae3e45f8a816b50ab58dfd95429736735caf8f1e3252850adc5e4c9121f1b2de50599c6629c9f97bb7991ef2b86a31c8c273a76c32a138cf777ab8eee5c292f3c728f9c79ee19d977e30d52aec17964448474cf9b25c755393c931999ec420c163ac9ec18794371d006edfe21588c6cc51ec59f310234373b4baaabab252b2b4b56f62c911deb56c8a6ee163365910c95afbb53032d07d418133bf5b31a64af4eee5d271f3d7e5836aebf562a2b2b25312141727272e4c68d2be5847e6fe0f3e06a56ab6565ee97be96df23d1a7c8538cb7c7ccf72c0639fb0cc55ac5b38aef93a6cd4c8a95ce59f5525353a3a896273f728fdcbd6385b1167dfd6edc237ea2209ebfcb1384e34ee1629dba798b1cdebf532aca4a49e3caf4e9d3657bef3239b66f93bbc23ec87331178c662bdc507d4d7fe1a47fcb5c76c7bacf28067d0da58aed8aa7a1871057c444b867f14255efdbbe4aeaebeba5540fc523c76e55b7a3c76f1462307a09d929fe4e2e8393376d954d6b96699c912811ea4eedb8b6478eefdbe88e4d8635c6b4dd4c8b676e98cbbd6f840ba8dc6529295e2d410ae8da7b159f51fc845b6e4a54b8997cb87c4eb57135fa4cf57885ac5fbe40921213e4e8c11de660dc7e212a869f28889b98b8528eefdd2087b6af9379b31b253d354516b635cbe11bd67da80b82d712da8dbeced4477eea58922ae77db0e26556833ddf8b149fc58d62b0429a99649e2b2bda6acc0cab81013784bcd89818b9695bafb95d7db1be311ccbc1df7de2c016d9bd6995cc69ae939cac0cc9cdcd35eed4f17d1b2e7bce16090d7dddff850b49f1a2cbcdddb24ae225429c91e3e4e6bf3c7edc38498d8f96d96afed72f689483d70ebe83035f3b3d2d4d7a17b7e963babd765ce887558edb362e349cb13d5b7ba5b6b24ca6c4c54a5969a9ec5cd763fe76d2bd23d5dbce465e7dfdbfa4784bf1e78a0e975df136ea42b18a36d1d38a7fa2ea5d90916876ff0d67390de9cd0dea66b53794aaf558745517da5c49c5c01232b5e4fae56dd2ded264ac647272f225c719976a4918c1aaef03cb797eaff80b459b5592d111528af18aeb1dd3fe2ec1226b07181c3ddcd505e640ed5a2f1545b972e3ea79a64ee0d3d92a5572ac2037fadaae466335b0906b9774c8dd7b37fd71fde30aad5760fd0303be1d4bf239977b0f7b883db2574f029c401c4ed0eb0487d0cf7b5aabcc4a02dc8a4b79534fa80f3eb7a946ca0ab2644e4d91d7ecf2f8b0cab14f15bd7b56a5e46567487474b4ecdea881f98dbd57edf7e07262ea247dfb5c5c8e25e9544cb247f7ca0be69a2980cf28de547c87ec147b3b2ea70576df965512141468c878ac36606fa1afb95558bf5d2bdaa4b361ba4ccd4a9345739be5c8ee0da392bef65892ec646349de51fca9cbbd5937c21ee12b27e1ce4df48ae257ece468ad2e929dcb2fffc63fb1679d2c98d36846710607069818c69762119403ffbf522f8b82a959b2bd77a9b18ca3f93b614950122e1ca74ec2c29e0d2e4b72bc22f1469a136ffcade2c72cae817a8e4b31526fe85d7b364a6c74a4044c9c6016e1e0c77bbbd5e07220c55a919f6196e2b43556ebdf3172d9a991b0240cd863d990be6fef29bea2b84191690b8a2397c29daeb847f12dcc7561569261d81e1ae1809a00f6d0b63586b1caf6db15736a2e399eb99adc2a52d828056b0c68e65adbdd2ec7f7aef7badf15ebbe7151b3591eaaefdff715df549c524c7359fed66505e2198aa58a17143f547c97dd81c41b9792a5ba541cbd61b51465254b7176b2d91ae56d9c2c2c074d5bac834339e0951ddeb1f68a67a82e3781c06bc9fba7efe3bf297ee450531a6d41f1d22be250d2e72a1e567cd5f15fbf42bc6156355f853774594b9514a85b50579263b25a7d5e741bd3ee5b53946536e892a038b67badcf308a79ffe6d597a0245f53fc5af16945bb22cc1efda10582618d623f1559e82204e2d3d5e5e969ad96036b3baeea419d573fcd2ce8ac2bc936d9a1d1ea0be9737c79e28d85cd6566ef7a7a428c893d7cb15603b3811df2fa1ebfead44afe4a719d22c1aac0e0ee54a2a2d7c999bf3d292840f23312cfef181fad5880253314bdaaf5c686017cb50b896ec568352ba971fbd8a44b2fc62d9b97f97441f316270daceff56715ef93ae571c729231e3ac4afc21004f572c549ce4b6c66ad0f30c259d58e3562fd818db3963bae4a4c69b5b9b7de858932b5d48e4f9b918500c921204e364d6baf5e6f51722257124ac077def1f7182f7ef2a8e2a8a5cee06b7312d306f191ff328d5708601d0ab413be7aaf63ab9a9d7bb4883776c5f29b5c5d9e6a0129b10a3d0a37dbe4b6f041b9b601d538729ce4e31d934e833cc02be63eb12bfa4e15394a5c5d951108abf2f3af5ae3119977033d0c30cf3f635c52fe2d5a7862a824f3d1245bf2b096e70b25b50e9099299f24e3a186b47004aabeea5c62afcbd281aadacb81d0c8ac092b287dd588c413afbfc46494cf03ed704ef5c40e3c68df3f4971097248f25978b741e1d67f43033c6f21daaac1c02dc096f70a7860398b0ac514639288061f9989fc54d4fcc8205c47520db843bc69bcf54149400cbe8010321502c3864f4ac105fd0f9c873f23c47775ee3d78af1819d897ab9f0baf2f7f33ab0a4c871b92ac7028f0b9a08fd016730a3fcf1a428097c7d45312e585cdcbe5c3677cf96b6da6253cde6d68f8d0c3399267848581b14a7615aae89ab6656e4cb2c052e055f871140a68ec7f17dacd15d5e52051fad3436f512dcd9a8f050cf600852c1db14052e3f9c0bec199ab0c171a9fe8b209ce61a5f65cd5ebcbfe41ab96943b7b18a30836794e69aac53e9d434e392a1100097920ba2a5aad0c41b6359292ee4721197e072715602274e10a750cce5dae54fb1499493a12210ff0798b250a0e90ba7b9664cbdf1ce384faadde04a342cf99dcbb57ebed9248cf525367158c15f546c71ca023e1d9b404b5f8e62402e64680269ca6bbb66f8b44b65313af3bcb8540bdc84c71fbbdc0d72f72b4a7c95cb15e8706cbea0f86f5a60a99c92a1f256f29f85f7d74c362f9e696213a7fa0e4d8536eb5a5f8c4b72154f42488350477799550c8b91b02664fec8f0910471088fc425d5be6449425dee616ddfc76f5c3cb3c2e7fbbc2dbc8dcbd5699220a4d65dee595c4ffb8a2519e7f8859f670238243ffaa3ed9b6a71250278787ac4b62ef7e8d3971c25f16a4b32dea9757c9de2d9d216df5d956ce1fda0d88a92c091d333f703c5738ebb35c19b15641d85408887bebc2ad9c2777af149003969e05f3b4d5805de9a0286677533f465aac490eeec9b6871356212787c4ee0fea6439bf7cac110f47530abea57f0946ce6cae26a2a09ac8448373585e929d7380923afab7fdc898228eef2371a898577033634141e98d04ec5bdc5db827614e40eab2016a3534c5c68981ab0a28302278a43714af3460bf26bab2016a3d5ca4b660b4635995487241b645d2c0b0bcf18d935f3cc349ac92166a7fb19a785d7eb2cc8dd56412c468b92c260c15477a5fddb4e2f49b037c52028c89d56412c460b8c8da54bd3e92379c9a98d5817cbc2c233c9b19f15f99ed34312ec4d0a625d2c8b51b722cea8d3ff706828d9dea020471c05396a15c46254ad88c622345ab9dcb380ffd5e55ed537aa3c2d2ae97d4e0cf2c0a5b4d5f2c790c746a92c7fcbf7665b79eb7bc654193d8b9f70ce24759194d1e662ed75e8c79fa1b239dcdcf586854d662b2d830ef8a3ac92f806689f668b2ef0c6566a2edd85ee3deeacce60036fd368121929eb2f83eeaef8bf2c4ee91b86e5607784b3f4d180719bd63df30dcbc11a0626b790316286b0b75d6ca47c9953e672efb2fcaad38e316a0ac20f6e75c6fbfc33fedf502fd8ded5eda6a8c3fa66da73990d65d60d6cb67d24be506fe012648c113b4a9840e275bb54f4f77186647fc55192aed1a6c2b335e8f34e2be4ae8b354cd1880f979f2172cca0c5149a3de556397c4641387ca452995fc54ded8dcb86e06739e9de6f287a465b41229c912cef3a19adc0c15e5ca65430f1242c24d80c57f3b5cdb2d6c55a6cd6c031108fd6573a48bdf172dbded36256693845c36d9793c9fafff64d570a76b6a6690000000049454e44ae426082);

-- --------------------------------------------------------

--
-- Структура таблицы `character_buffs`
--

CREATE TABLE IF NOT EXISTS `character_buffs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `character_id` int(11) NOT NULL,
  `buff_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Дамп данных таблицы `character_buffs`
--

INSERT INTO `character_buffs` (`id`, `character_id`, `buff_id`) VALUES
(1, 3, 2);

-- --------------------------------------------------------

--
-- Структура таблицы `character_career`
--

CREATE TABLE IF NOT EXISTS `character_career` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vocation` int(11) NOT NULL,
  `intelligence` int(3) NOT NULL DEFAULT '0',
  `charisma` int(3) NOT NULL DEFAULT '0',
  `strength` int(3) NOT NULL DEFAULT '0',
  `creativity` int(3) NOT NULL DEFAULT '0',
  `education` int(11) NOT NULL,
  `profession` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Дамп данных таблицы `character_career`
--

INSERT INTO `character_career` (`id`, `vocation`, `intelligence`, `charisma`, `strength`, `creativity`, `education`, `profession`) VALUES
(1, 10, 1, 4, 5, 4, 1, NULL),
(2, 7, 4, 5, 4, 2, 1, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `education`
--

CREATE TABLE IF NOT EXISTS `education` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Дамп данных таблицы `education`
--

INSERT INTO `education` (`id`, `name`) VALUES
(1, 'edu.primary'),
(2, 'edu.secondary'),
(3, 'edu.higher');

-- --------------------------------------------------------

--
-- Структура таблицы `family`
--

CREATE TABLE IF NOT EXISTS `family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `family_name` varchar(30) NOT NULL,
  `current` tinyint(1) NOT NULL,
  `male_lastname` varchar(30) NOT NULL,
  `female_lastname` varchar(30) NOT NULL,
  `level` int(11) NOT NULL,
  `money` int(11) NOT NULL DEFAULT '100',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=51 ;

--
-- Дамп данных таблицы `family`
--

INSERT INTO `family` (`id`, `user_id`, `family_name`, `current`, `male_lastname`, `female_lastname`, `level`, `money`) VALUES
(1, 1, 'Случайновы', 0, 'Случайнов', 'Случайнова', 0, 760),
(46, 1, 'Админовы', 1, 'Админов', 'Админова', 1, 100),
(50, 9, 'Иваненко', 1, 'Иваненко', 'Иваненко', 0, 100);

-- --------------------------------------------------------

--
-- Структура таблицы `fiancee`
--

CREATE TABLE IF NOT EXISTS `fiancee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `character_id` int(11) NOT NULL,
  `cost` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `names_female`
--

CREATE TABLE IF NOT EXISTS `names_female` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=496 ;

--
-- Дамп данных таблицы `names_female`
--

INSERT INTO `names_female` (`id`, `value`) VALUES
(1, 'Августа'),
(2, 'Аврора'),
(3, 'Агата'),
(4, 'Агнесса'),
(5, 'Агния'),
(6, 'Агриппина'),
(7, 'Ада'),
(8, 'Аделия'),
(9, 'Адиля'),
(10, 'Адина'),
(11, 'Адинай'),
(12, 'Адриана'),
(13, 'Аза'),
(14, 'Азиза'),
(15, 'Аида'),
(16, 'Айжан'),
(17, 'Айта'),
(18, 'Акгюль'),
(19, 'Акулина'),
(20, 'Алана'),
(21, 'Алевтина'),
(22, 'Александра'),
(23, 'Алешан'),
(24, 'Алёна'),
(25, 'Алико'),
(26, 'Алина'),
(27, 'Алиса'),
(28, 'Алла'),
(29, 'Алсу'),
(30, 'Альберта'),
(31, 'Альбина'),
(32, 'Альжбета'),
(33, 'Альфреа'),
(34, 'Альфреда'),
(35, 'Амелия'),
(36, 'Амина'),
(37, 'Анабела'),
(38, 'Анастасия'),
(39, 'Ангелина'),
(40, 'Анжела'),
(41, 'Анисья'),
(42, 'Анита'),
(43, 'Анна'),
(44, 'Антонина'),
(45, 'Ануш'),
(46, 'Анфиса'),
(47, 'Анэля'),
(48, 'Аполлинария'),
(49, 'Апфия'),
(50, 'Арабелла'),
(51, 'Арзыгуль'),
(52, 'Ариадна'),
(53, 'Арина'),
(54, 'Арлета'),
(55, 'Архелия'),
(56, 'Асия'),
(57, 'Астра'),
(58, 'Ася'),
(59, 'Аурелия'),
(60, 'Афанасия'),
(61, 'Афина'),
(62, 'Аягуль'),
(63, 'Бабетта'),
(64, 'Багдагуль'),
(65, 'Барбара'),
(66, 'Баянсулу'),
(67, 'Беата'),
(68, 'Беатриса'),
(69, 'Белла'),
(70, 'Бенедикта'),
(71, 'Береслава'),
(72, 'Бернадетта'),
(73, 'Берта'),
(74, 'Бибиана'),
(75, 'Биргит'),
(76, 'Бирута'),
(77, 'Бланка'),
(78, 'Богдана'),
(79, 'Божена'),
(80, 'Борислава'),
(81, 'Ботогоз'),
(82, 'Бригитта'),
(83, 'Бронислава'),
(84, 'Бруна'),
(85, 'Валентина'),
(86, 'Валерия'),
(87, 'Валида'),
(88, 'Валия'),
(89, 'Ванда'),
(90, 'Варвара'),
(91, 'Варя'),
(92, 'Васила'),
(93, 'Василиса'),
(94, 'Васса'),
(95, 'Веджиха'),
(96, 'Венера'),
(97, 'Вера'),
(98, 'Вероника'),
(99, 'Веселина'),
(100, 'Веста'),
(101, 'Вета'),
(102, 'Вивиана'),
(103, 'Вида'),
(104, 'Виктория'),
(105, 'Вилора'),
(106, 'Вильгельмина'),
(107, 'Виолетта'),
(108, 'Виргиния'),
(109, 'Виринея'),
(110, 'Виталина'),
(111, 'Виталия'),
(112, 'Владислава'),
(113, 'Владлена'),
(114, 'Властилина'),
(115, 'Габриэлла'),
(116, 'Газама'),
(117, 'Галима'),
(118, 'Галина'),
(119, 'Гаянэ'),
(120, 'Гелена'),
(121, 'Гелла'),
(122, 'Геновефа'),
(123, 'Генриетта'),
(124, 'Георгина'),
(125, 'Гера'),
(126, 'Гертруда'),
(127, 'Гизелла'),
(128, 'Глафира'),
(129, 'Гликерия'),
(130, 'Глория'),
(131, 'Гольпира'),
(132, 'Гортензия'),
(133, 'Гражина'),
(134, 'Грета'),
(135, 'Гулара'),
(136, 'Гулия'),
(137, 'Гульмира'),
(138, 'Гульнара'),
(139, 'Гульназ'),
(140, 'Гульфиза'),
(141, 'Гюзель'),
(142, 'Дайна'),
(143, 'Далия'),
(144, 'Дамира'),
(145, 'Дана'),
(146, 'Даниэла'),
(147, 'Данута'),
(148, 'Дариа'),
(149, 'Дарина'),
(150, 'Дарига'),
(151, 'Дарья'),
(152, 'Дастагуль'),
(153, 'Дафна'),
(154, 'Дебора'),
(155, 'Джана'),
(156, 'Джамиля'),
(157, 'Джафара'),
(158, 'Джемма'),
(159, 'Джулия'),
(160, 'Джульетта'),
(161, 'Диана'),
(162, 'Дидилия'),
(163, 'Дильдара'),
(164, 'Дильмира'),
(165, 'Диля'),
(166, 'Диляра'),
(167, 'Дилярам'),
(168, 'Дина'),
(169, 'Динара'),
(170, 'Диодора'),
(171, 'Дионисия'),
(172, 'Доля'),
(173, 'Доминика'),
(174, 'Дора'),
(175, 'Доротея'),
(176, 'Ева'),
(177, 'Евгения'),
(178, 'Евдокия'),
(179, 'Евлалия'),
(180, 'Евлампия'),
(181, 'Евпраксия'),
(182, 'Екатерина'),
(183, 'Елена'),
(184, 'Елизавета'),
(185, 'Есения'),
(186, 'Ефимия'),
(187, 'Ефросиния'),
(188, 'Жанна'),
(189, 'Желя'),
(190, 'Жеральдина'),
(191, 'Жозефина'),
(192, 'Забава'),
(193, 'Заира'),
(194, 'Замира'),
(195, 'Зара'),
(196, 'Зарема'),
(197, 'Зарифа'),
(198, 'Земфира'),
(199, 'Зинаида'),
(200, 'Зинат'),
(201, 'Зита'),
(202, 'Злата'),
(203, 'Зоя'),
(204, 'Зульфия'),
(205, 'Зухра'),
(206, 'Иванна'),
(207, 'Иветта'),
(208, 'Ивона'),
(209, 'Изабелла'),
(210, 'Изольда'),
(211, 'Илзе'),
(212, 'Илона'),
(213, 'Инара'),
(214, 'Инга'),
(215, 'Индира'),
(216, 'Инесса'),
(217, 'Инна'),
(218, 'Иоанна'),
(219, 'Иоланта'),
(220, 'Ирада'),
(221, 'Ираида'),
(222, 'Ирена'),
(223, 'Ирина'),
(224, 'Ирма'),
(225, 'Исидора'),
(226, 'Июлия'),
(227, 'Ия'),
(228, 'Казимира'),
(229, 'Калерия'),
(230, 'Каллиопа'),
(231, 'Камила'),
(232, 'Камилла'),
(233, 'Капитолина'),
(234, 'Карима'),
(235, 'Карина'),
(236, 'Каролина'),
(237, 'Катарина'),
(238, 'Кира'),
(239, 'Кирилла'),
(240, 'Клавдия'),
(241, 'Клара'),
(242, 'Клариса'),
(243, 'Клена'),
(244, 'Клеопатра'),
(245, 'Климентина'),
(246, 'Клотильда'),
(247, 'Констанция'),
(248, 'Консуэлла'),
(249, 'Кора'),
(250, 'Кристина'),
(251, 'Ксаверта'),
(252, 'Ксения'),
(253, 'Куралай'),
(254, 'Лада'),
(255, 'Лайма'),
(256, 'Лара'),
(257, 'Лариса'),
(258, 'Латафат'),
(259, 'Лаура'),
(260, 'Лейла'),
(261, 'Леокадия'),
(262, 'Леонида'),
(263, 'Леонтина'),
(264, 'Леся'),
(265, 'Летиция'),
(266, 'Лиана'),
(267, 'Лидия'),
(268, 'Лилиана'),
(269, 'Лилия'),
(270, 'Лина'),
(271, 'Линда'),
(272, 'Лия'),
(273, 'Лола'),
(274, 'Лолита'),
(275, 'Луиза'),
(276, 'Лукреция'),
(277, 'Любава'),
(278, 'Любовь'),
(279, 'Любомила'),
(280, 'Любомира'),
(281, 'Людмила'),
(282, 'Люсьена'),
(283, 'Люция'),
(284, 'Мавра'),
(285, 'Магда'),
(286, 'Магдалена'),
(287, 'Магура'),
(288, 'Мадина'),
(289, 'Мадлена'),
(290, 'Майда'),
(291, 'Майя'),
(292, 'Малика'),
(293, 'Малуша'),
(294, 'Мальвина'),
(295, 'Манана'),
(296, 'Мануэла'),
(297, 'Маргарита'),
(298, 'Марианна'),
(299, 'Марина'),
(300, 'Марионелла'),
(301, 'Марица'),
(302, 'Мариэтта'),
(303, 'Мария'),
(304, 'Марлена'),
(305, 'Марселина'),
(306, 'Марта'),
(307, 'Марфа'),
(308, 'Марьям'),
(309, 'Марьяна'),
(310, 'Матильда'),
(311, 'Матрона'),
(312, 'Медея'),
(313, 'Медина'),
(314, 'Мелания'),
(315, 'Мелитина'),
(316, 'Милада'),
(317, 'Милана'),
(318, 'Милена'),
(319, 'Милица'),
(320, 'Милослава'),
(321, 'Мирдза'),
(322, 'Мирослава'),
(323, 'Мирра'),
(324, 'Михалина'),
(325, 'Млада'),
(326, 'Моника'),
(327, 'Муза'),
(328, 'Мэрилант'),
(329, 'Надежда'),
(330, 'Назира'),
(331, 'Наиля'),
(332, 'Наина'),
(333, 'Нана'),
(334, 'Наталья'),
(335, 'Нателла'),
(336, 'Нелли'),
(337, 'Неонила'),
(338, 'Ника'),
(339, 'Нила'),
(340, 'Нимфодора'),
(341, 'Нина'),
(342, 'Нинель'),
(343, 'Нонна'),
(344, 'Нора'),
(345, 'Норгул'),
(346, 'Одетта'),
(347, 'Оксана'),
(348, 'Октавия'),
(349, 'Октябрина'),
(350, 'Олеся'),
(351, 'Олимпия'),
(352, 'Ольвия'),
(353, 'Ольга'),
(354, 'Павла'),
(355, 'Памела'),
(356, 'Патриция'),
(357, 'Паула'),
(358, 'Паулина'),
(359, 'Пелагея'),
(360, 'Полина'),
(361, 'Прасковья'),
(362, 'Рада'),
(363, 'Радмила'),
(364, 'Радосвета'),
(365, 'Раиса'),
(366, 'Рахиль'),
(367, 'Рашам'),
(368, 'Ревекка'),
(369, 'Регина'),
(370, 'Резета'),
(371, 'Рема'),
(372, 'Рената'),
(373, 'Римма'),
(374, 'Роберта'),
(375, 'Рогнеда'),
(376, 'Роза'),
(377, 'Розамунда'),
(378, 'Розибуви'),
(379, 'Розмари'),
(380, 'Роксана'),
(381, 'Ростислава'),
(382, 'Руда'),
(383, 'Ружена'),
(384, 'Рузана'),
(385, 'Румия'),
(386, 'Русана'),
(387, 'Руслана'),
(388, 'Руфина'),
(389, 'Сабина'),
(390, 'Саида'),
(391, 'Саломея'),
(392, 'Салтанат'),
(393, 'Сания'),
(394, 'Санта'),
(395, 'Сарра'),
(396, 'Сати'),
(397, 'Светлана'),
(398, 'Северина'),
(399, 'Селена'),
(400, 'Серафима'),
(401, 'Сильвия'),
(402, 'Сима'),
(403, 'Симона'),
(404, 'Сиотвия'),
(405, 'Снежана'),
(406, 'Созия'),
(407, 'Софья'),
(408, 'Сталина'),
(409, 'Станислава'),
(410, 'Стелла'),
(411, 'Стефания'),
(412, 'Сусанна'),
(413, 'Суфия'),
(414, 'Таира'),
(415, 'Таисия'),
(416, 'Тала'),
(417, 'Тамара'),
(418, 'Татьяна'),
(419, 'Тахмина'),
(420, 'Теодора'),
(421, 'Тереза'),
(422, 'Томила'),
(423, 'Трифена'),
(424, 'Улдуза'),
(425, 'Улита'),
(426, 'Ульяна'),
(427, 'Устина'),
(428, 'Фаиза'),
(429, 'Фаина'),
(430, 'Фанни'),
(431, 'Фаня'),
(432, 'Фарида'),
(433, 'Фатима'),
(434, 'Фая'),
(435, 'Фекла'),
(436, 'Фелиция'),
(437, 'Феодора'),
(438, 'Феруза'),
(439, 'Филиппина'),
(440, 'Флора'),
(441, 'Флорентина'),
(442, 'Франсуаза'),
(443, 'Франческа'),
(444, 'Фредерика'),
(445, 'Фрида'),
(446, 'Халима'),
(447, 'Харита'),
(448, 'Хильда'),
(449, 'Хильдегарда'),
(450, 'Христина'),
(451, 'Христя'),
(452, 'Цветана'),
(453, 'Цецилия'),
(454, 'Чеслава'),
(455, 'Чулпан'),
(456, 'Шангуль'),
(457, 'Шахмира'),
(458, 'Ширин'),
(459, 'Эвелина'),
(460, 'Эдда'),
(461, 'Эдита'),
(462, 'Элахе'),
(463, 'Элеонора'),
(464, 'Элиана'),
(465, 'Элиза'),
(466, 'Элизабет'),
(467, 'Элина'),
(468, 'Элла'),
(469, 'Эллада'),
(470, 'Элоиза'),
(471, 'Эльвира'),
(472, 'Эльга'),
(473, 'Эльза'),
(474, 'Эльмира'),
(475, 'Эмилия'),
(476, 'Эмма'),
(477, 'Эрика'),
(478, 'Эрнестина'),
(479, 'Эсмеральда'),
(480, 'Эстер'),
(481, 'Юдита'),
(482, 'Юзефа'),
(483, 'Юлдуз'),
(484, 'Юлиана'),
(485, 'Юлия'),
(486, 'Юна'),
(487, 'Юния'),
(488, 'Юнона'),
(489, 'Юстина'),
(490, 'Юханна'),
(491, 'Ядвига'),
(492, 'Яна'),
(493, 'Янита'),
(494, 'Янка'),
(495, 'Ярослава');

-- --------------------------------------------------------

--
-- Структура таблицы `names_male`
--

CREATE TABLE IF NOT EXISTS `names_male` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=610 ;

--
-- Дамп данных таблицы `names_male`
--

INSERT INTO `names_male` (`id`, `value`) VALUES
(1, 'Абрам'),
(4, 'Август'),
(5, 'Августин'),
(6, 'Авдей'),
(8, 'Аверьян'),
(9, 'Авксентий'),
(10, 'Автандил'),
(11, 'Агап'),
(12, 'Агафон'),
(13, 'Агей'),
(14, 'Адам'),
(15, 'Адильхан'),
(16, 'Адис'),
(17, 'Адольф'),
(18, 'Адриан'),
(19, 'Азад'),
(20, 'Азамат'),
(21, 'Азар'),
(22, 'Азарий'),
(23, 'Азиз'),
(24, 'Акакий'),
(25, 'Аким'),
(26, 'Алан'),
(27, 'Александр'),
(28, 'Алексей'),
(29, 'Али'),
(30, 'Алим'),
(31, 'Алискер'),
(32, 'Алихан'),
(33, 'Алоис'),
(34, 'Алферий'),
(35, 'Альберт'),
(36, 'Альбин'),
(37, 'Альфред'),
(38, 'Алянур'),
(39, 'Амадей'),
(40, 'Амадеус'),
(41, 'Амаяк'),
(42, 'Амвросий'),
(43, 'Анастас'),
(44, 'Анатолий'),
(45, 'Анвар'),
(46, 'Ангел'),
(47, 'Андоим'),
(48, 'Андрей'),
(49, 'Андрон'),
(50, 'Андроп'),
(51, 'Анжей'),
(52, 'Аникита'),
(53, 'Анисим'),
(54, 'Антип'),
(55, 'Антон'),
(56, 'Ануфрий'),
(57, 'Анфим'),
(58, 'Аполлон'),
(59, 'Арам'),
(60, 'Ардалион'),
(61, 'Арий'),
(62, 'Аристарх'),
(63, 'Аристон'),
(64, 'Аркадий'),
(65, 'Арно'),
(66, 'Арнольд'),
(67, 'Арон'),
(68, 'Арсен'),
(69, 'Арслан'),
(70, 'Артамон'),
(71, 'Артем'),
(72, 'Артемий'),
(73, 'Артур'),
(74, 'Арулан'),
(75, 'Архип'),
(76, 'Аршак'),
(77, 'Аршиддин'),
(78, 'Арье'),
(79, 'Аскар'),
(80, 'Аскольд'),
(81, 'Атанас'),
(82, 'Афанасий'),
(83, 'Афиноген'),
(84, 'Африкан'),
(85, 'Ахмет'),
(86, 'Ашот'),
(87, 'Бадри'),
(88, 'Базан'),
(89, 'Байракдар'),
(90, 'Бари'),
(91, 'Барузда'),
(92, 'Батыр'),
(93, 'Бахрам'),
(94, 'Бахыт'),
(95, 'Бежен'),
(96, 'Бек'),
(97, 'Бенедикт'),
(98, 'Берек'),
(99, 'Бернар'),
(100, 'Бертран'),
(101, 'Богдан'),
(102, 'Боголюб'),
(103, 'Болеслав'),
(104, 'Бонифаций'),
(105, 'Боримир'),
(106, 'Борис'),
(107, 'Борислав'),
(108, 'Боян'),
(109, 'Брачислав'),
(110, 'Бронислав'),
(111, 'Бруно'),
(112, 'Будимир'),
(113, 'Булат'),
(114, 'Вавила'),
(115, 'Ваган'),
(116, 'Вадим'),
(117, 'Валентин'),
(118, 'Валерий'),
(119, 'Валерьян'),
(120, 'Вали'),
(121, 'Вальтер'),
(122, 'Варлаам'),
(123, 'Варсонофий'),
(124, 'Варфоломей'),
(125, 'Василий'),
(126, 'Васим'),
(127, 'Велизар'),
(128, 'Велор'),
(129, 'Венедикт'),
(130, 'Вениамин'),
(131, 'Викентий'),
(132, 'Виктор'),
(133, 'Вилен'),
(134, 'Вилли'),
(135, 'Вильгельм'),
(136, 'Вильям'),
(137, 'Винсент'),
(138, 'Виргилий'),
(139, 'Виссарион'),
(140, 'Вит'),
(141, 'Виталий'),
(142, 'Витаутас'),
(143, 'Витольд'),
(144, 'Владимир'),
(145, 'Владислав'),
(146, 'Владлен'),
(147, 'Влас'),
(148, 'Воислав'),
(149, 'Володар'),
(150, 'Вольга'),
(151, 'Вольдемар'),
(152, 'Всеволод'),
(153, 'Всеслав'),
(154, 'Вышеслав'),
(155, 'Вячеслав'),
(156, 'Габриель'),
(157, 'Гавриил'),
(158, 'Галактион'),
(159, 'Галим'),
(160, 'Гарри'),
(161, 'Гастон'),
(162, 'Гафур'),
(163, 'Гаян'),
(164, 'Гаяс'),
(165, 'Гедеон'),
(166, 'Гейдар'),
(167, 'Гелий'),
(168, 'Геннадий'),
(169, 'Генрих'),
(170, 'Георгий'),
(171, 'Геракл'),
(172, 'Геральд'),
(173, 'Герасим'),
(174, 'Герман'),
(175, 'Гермоген'),
(176, 'Геронтий'),
(177, 'Глеб'),
(178, 'Годфрид'),
(179, 'Гордей'),
(180, 'Гордон'),
(181, 'Горислав'),
(182, 'Гостомысл'),
(183, 'Градимир'),
(184, 'Грегор'),
(185, 'Григорий'),
(186, 'Гурий'),
(187, 'Гуруглы'),
(188, 'Густав'),
(189, 'Давид'),
(190, 'Давлат'),
(191, 'Дамир'),
(192, 'Даниил'),
(193, 'Дасий'),
(194, 'Дементий'),
(195, 'Демид'),
(196, 'Демьян'),
(197, 'Денис'),
(198, 'Джабир'),
(199, 'Джамал'),
(200, 'Джамил'),
(201, 'Джамшид'),
(202, 'Джанузак'),
(203, 'Джафар'),
(204, 'Джованни'),
(205, 'Джордан'),
(206, 'Диас'),
(207, 'Див'),
(208, 'Дид'),
(209, 'Дильмурат'),
(210, 'Динасий'),
(211, 'Дитмар'),
(212, 'Дмитрий'),
(213, 'Доброслав'),
(214, 'Добрыня'),
(215, 'Дональд'),
(216, 'Донат'),
(217, 'Донатос'),
(218, 'Дормидонт'),
(219, 'Дорофей'),
(220, 'Драгомил'),
(221, 'Драгомир'),
(222, 'Дружина'),
(223, 'Евгений'),
(224, 'Евграф'),
(225, 'Евдоким'),
(226, 'Евлампий'),
(227, 'Евсей'),
(228, 'Евстафий'),
(229, 'Евстигней'),
(230, 'Евстрат'),
(231, 'Егор'),
(232, 'Елдос'),
(233, 'Елизар'),
(234, 'Елисей'),
(235, 'Емельян'),
(236, 'Епифан'),
(237, 'Ералы'),
(238, 'Еремей'),
(239, 'Ермак'),
(240, 'Ермил'),
(241, 'Ермолай'),
(242, 'Ерофей'),
(243, 'Ефим'),
(244, 'Ефрем'),
(245, 'Жан'),
(246, 'Жантуар'),
(247, 'Ждан'),
(248, 'Жерар'),
(249, 'Жорж'),
(250, 'Загид'),
(251, 'Задмард'),
(252, 'Зайнал'),
(253, 'Закир'),
(254, 'Замир'),
(255, 'Зафар'),
(256, 'Захар'),
(257, 'Захария'),
(258, 'Звяга'),
(259, 'Зенон'),
(260, 'Зигмунд'),
(261, 'Зиновий'),
(262, 'Зосима'),
(263, 'Иакинф'),
(264, 'Ибрагим'),
(265, 'Иван'),
(266, 'Иво'),
(267, 'Игнат'),
(268, 'Игорь'),
(269, 'Иероним'),
(270, 'Измаил'),
(271, 'Израиль'),
(272, 'Изяслав'),
(273, 'Иларий'),
(274, 'Илиан'),
(275, 'Илларион'),
(276, 'Ильгам'),
(277, 'Илья'),
(278, 'Ильяр'),
(279, 'Иннокентий'),
(280, 'Ион'),
(281, 'Ионос'),
(282, 'Иосиф'),
(283, 'Ипат'),
(284, 'Ипполит'),
(285, 'Ираклий'),
(286, 'Иржи'),
(287, 'Ирмен'),
(288, 'Иса'),
(289, 'Исаак'),
(290, 'Исай'),
(291, 'Исидор'),
(292, 'Искандер'),
(293, 'Иувеналий'),
(294, 'Кадри'),
(295, 'Казимир'),
(296, 'Каллистрат'),
(297, 'Камилл'),
(298, 'Камиль'),
(299, 'Капитон'),
(300, 'Карен'),
(301, 'Карл'),
(302, 'Карло'),
(303, 'Карп'),
(304, 'Касим'),
(305, 'Касьян'),
(306, 'Ким'),
(307, 'Киприан'),
(308, 'Кирилл'),
(309, 'Кирьян'),
(310, 'Клавдий'),
(311, 'Клемент'),
(312, 'Клим'),
(313, 'Клод'),
(314, 'Кондрат'),
(315, 'Конкордий'),
(316, 'Конон'),
(317, 'Конрад'),
(318, 'Константин'),
(319, 'Корней'),
(320, 'Корнелий'),
(321, 'Кристиан'),
(322, 'Кристоф'),
(323, 'Кронид'),
(324, 'Ксанф'),
(325, 'Ксенофонт'),
(326, 'Кузьма'),
(327, 'Кустодий'),
(328, 'Лабута'),
(329, 'Лавр'),
(330, 'Лаврентий'),
(331, 'Ладимир'),
(332, 'Лазарь'),
(333, 'Лев'),
(334, 'Леван'),
(335, 'Лель'),
(336, 'Ленар'),
(337, 'Леон'),
(338, 'Леонард'),
(339, 'Леонид'),
(340, 'Леонтий'),
(341, 'Леопольд'),
(342, 'Леча'),
(343, 'Любомир'),
(344, 'Людвиг'),
(345, 'Лука'),
(346, 'Лукьян'),
(347, 'Люсьен'),
(348, 'Маврикий'),
(349, 'Мадияр'),
(350, 'Мадлен'),
(351, 'Май'),
(352, 'Макар'),
(353, 'Максим'),
(354, 'Максимилиан'),
(355, 'Малик'),
(356, 'Мамед'),
(357, 'Манил'),
(358, 'Мансур'),
(359, 'Мануил'),
(360, 'Марат'),
(361, 'Мариан'),
(362, 'Мариус'),
(363, 'Марк'),
(364, 'Маркелл'),
(365, 'Марсель'),
(366, 'Мартин'),
(367, 'Мартьян'),
(368, 'Маскут'),
(369, 'Матвей'),
(370, 'Махмуд'),
(371, 'Медард'),
(372, 'Мелентий'),
(373, 'Мелитон'),
(374, 'Мераб'),
(375, 'Меркурий'),
(376, 'Мефодий'),
(377, 'Мечеслав'),
(378, 'Милан'),
(379, 'Мирза'),
(380, 'Мирон'),
(381, 'Мирослав'),
(382, 'Мисаил'),
(383, 'Митрофан'),
(384, 'Михаил'),
(385, 'Михей'),
(386, 'Мичлов'),
(387, 'Модест'),
(388, 'Моисей'),
(389, 'Мстислав'),
(390, 'Мурат'),
(391, 'Муслим'),
(392, 'Мустафа'),
(393, 'Мухтар'),
(394, 'Назар'),
(395, 'Назарий'),
(396, 'Назым'),
(397, 'Наил'),
(398, 'Нарцисс'),
(399, 'Натан'),
(400, 'Наум'),
(401, 'Нестор'),
(402, 'Никандр'),
(403, 'Никанор'),
(404, 'Никита'),
(405, 'Никифор'),
(406, 'Никодим'),
(407, 'Николай'),
(408, 'Никон'),
(409, 'Нил'),
(410, 'Нисон'),
(411, 'Нифонт'),
(412, 'Норберт'),
(413, 'Норман'),
(414, 'Овидий'),
(415, 'Одинец'),
(416, 'Олан'),
(417, 'Олаф'),
(418, 'Олег'),
(419, 'Олесь'),
(420, 'Олимпий'),
(421, 'Онисим'),
(422, 'Орест'),
(423, 'Осип'),
(424, 'Оскар'),
(425, 'Остап'),
(426, 'Остромир'),
(427, 'Павел'),
(428, 'Памфил'),
(429, 'Панкрат'),
(430, 'Пантелей'),
(431, 'Парамон'),
(432, 'Парух'),
(433, 'Парфен'),
(434, 'Патрик'),
(435, 'Патрикей'),
(436, 'Пахом'),
(437, 'Петр'),
(438, 'Пимен'),
(439, 'Платон'),
(440, 'Поликарп'),
(441, 'Помпей'),
(442, 'Порфирий'),
(443, 'Потап'),
(444, 'Пров'),
(445, 'Прокл'),
(446, 'Прокофий'),
(447, 'Протас'),
(448, 'Прохор'),
(449, 'Равиль'),
(450, 'Радий'),
(451, 'Радислав'),
(452, 'Радомир'),
(453, 'Раис'),
(454, 'Райнгольд'),
(455, 'Ратибор'),
(456, 'Ратмир'),
(457, 'Рауф'),
(458, 'Рафаил'),
(459, 'Рафик'),
(460, 'Рахман'),
(461, 'Рашид'),
(462, 'Реймонд'),
(463, 'Рейнольд'),
(464, 'Рем'),
(465, 'Ринат'),
(466, 'Рифат'),
(467, 'Рихард'),
(468, 'Ричард'),
(469, 'Роберт'),
(470, 'Родион'),
(471, 'Ролан'),
(472, 'Роман'),
(473, 'Рональд'),
(474, 'Ростислав'),
(475, 'Рубен'),
(476, 'Рудольф'),
(477, 'Руслан'),
(478, 'Рустам'),
(479, 'Рюрик'),
(480, 'Сабир'),
(481, 'Савва'),
(482, 'Савелий'),
(483, 'Салман'),
(484, 'Самсон'),
(485, 'Самуил'),
(486, 'Сармат'),
(487, 'Сархат'),
(488, 'Саттар'),
(489, 'Святополк'),
(490, 'Святослав'),
(491, 'Севастьян'),
(492, 'Северин'),
(493, 'Северьян'),
(494, 'Семен'),
(495, 'Серапион'),
(496, 'Серафим'),
(497, 'Сергей'),
(498, 'Сила'),
(499, 'Сильвестр'),
(500, 'Сократ'),
(501, 'Соломон'),
(502, 'Софрон'),
(503, 'Спартак'),
(504, 'Спиридон'),
(505, 'Стакрат'),
(506, 'Станимир'),
(507, 'Станислав'),
(508, 'Степан'),
(509, 'Стивен'),
(510, 'Стоян'),
(511, 'Султан'),
(512, 'Сухраб'),
(513, 'Таврион'),
(514, 'Таис'),
(515, 'Талик'),
(516, 'Тамаз'),
(517, 'Танир'),
(518, 'Тарас'),
(519, 'Тариэль'),
(520, 'Твердислав'),
(521, 'Тельман'),
(522, 'Теодор'),
(523, 'Терентий'),
(524, 'Тибор'),
(525, 'Тигран'),
(526, 'Тигрий'),
(527, 'Тимон'),
(528, 'Тимофей'),
(529, 'Тимур'),
(530, 'Тит'),
(531, 'Тихон'),
(532, 'Томас'),
(533, 'Трифон'),
(534, 'Трофим'),
(535, 'Ульманас'),
(536, 'Ульрих'),
(537, 'Ульян'),
(538, 'Урман'),
(539, 'Устин'),
(540, 'Фаддей'),
(541, 'Фаиз'),
(542, 'Фалалей'),
(543, 'Фарид'),
(544, 'Фархад'),
(545, 'Федор'),
(546, 'Федот'),
(547, 'Феликс'),
(548, 'Фелициан'),
(549, 'Феодосий'),
(550, 'Феоктист'),
(551, 'Феофан'),
(552, 'Феофил'),
(553, 'Ферапонт'),
(554, 'Фердинанд'),
(555, 'Фидель'),
(556, 'Филарет'),
(557, 'Филимон'),
(558, 'Филипп'),
(559, 'Фирс'),
(560, 'Флор'),
(561, 'Флорентий'),
(562, 'Фома'),
(563, 'Фортунат'),
(564, 'Фотий'),
(565, 'Франц'),
(566, 'Фридрих'),
(567, 'Фуад'),
(568, 'Хаким'),
(569, 'Халид'),
(570, 'Халик'),
(571, 'Хамид'),
(572, 'Харвей'),
(573, 'Харитон'),
(574, 'Харольд'),
(575, 'Христиан'),
(576, 'Христос'),
(577, 'Христофор'),
(578, 'Худаяр'),
(579, 'Цезарь'),
(580, 'Чеслав'),
(581, 'Шами'),
(582, 'Шамиль'),
(583, 'Шакур'),
(584, 'Эдвард'),
(585, 'Эдгар'),
(586, 'Эдмунд'),
(587, 'Эдуард'),
(588, 'Эльдар'),
(589, 'Эмиль'),
(590, 'Эммануил'),
(591, 'Эразм'),
(592, 'Эраст'),
(593, 'Эрик'),
(594, 'Эрнест'),
(595, 'Юджин'),
(596, 'Юлиан'),
(597, 'Юлий'),
(598, 'Юрий'),
(599, 'Юхим'),
(600, 'Язид'),
(601, 'Яким'),
(602, 'Яков'),
(603, 'Ян'),
(604, 'Янис'),
(605, 'Януарий'),
(606, 'Яромир'),
(607, 'Ярополк'),
(608, 'Ярослав'),
(609, 'Ясон');

-- --------------------------------------------------------

--
-- Структура таблицы `profession`
--

CREATE TABLE IF NOT EXISTS `profession` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vocation` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `education` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `intelligence` int(11) NOT NULL,
  `charisma` int(11) NOT NULL,
  `strength` int(11) NOT NULL,
  `creativity` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=151 ;

--
-- Дамп данных таблицы `profession`
--

INSERT INTO `profession` (`id`, `vocation`, `name`, `education`, `level`, `intelligence`, `charisma`, `strength`, `creativity`) VALUES
(1, 1, 'Выгуливатель', 1, 1, 0, 0, 0, 0),
(2, 1, 'Аквариумист', 1, 2, 10, 5, 0, 0),
(3, 1, 'Грумер', 1, 3, 15, 10, 0, 15),
(4, 1, 'Смотритель в зоопарке', 1, 4, 20, 15, 0, 0),
(5, 1, 'Разводчик', 1, 5, 25, 25, 0, 0),
(6, 1, 'Зоопсихолог', 2, 6, 25, 35, 0, 0),
(7, 1, 'Дрессировщик', 2, 7, 45, 45, 30, 0),
(8, 1, 'Ветеринарный фельдшер', 3, 8, 45, 45, 0, 0),
(9, 1, 'Ветеринарный врач', 3, 9, 60, 45, 0, 0),
(10, 1, 'Кинолог', 3, 10, 75, 60, 30, 0),
(11, 2, 'Собиратель камушков', 1, 1, 0, 0, 0, 0),
(12, 2, 'Золотоискатель', 1, 2, 5, 0, 0, 0),
(13, 2, 'Шахтер', 1, 3, 10, 0, 0, 0),
(14, 2, 'Буровик', 1, 4, 15, 0, 0, 0),
(15, 2, 'Картограф', 2, 5, 15, 0, 0, 0),
(16, 2, 'Ювелир', 2, 6, 20, 0, 0, 20),
(17, 2, 'Почвовед', 2, 7, 35, 0, 0, 0),
(18, 2, 'Геолог', 3, 8, 35, 0, 0, 0),
(19, 2, 'Геонавигатор', 3, 9, 55, 0, 0, 0),
(20, 2, 'Геофизик', 3, 10, 75, 0, 0, 0),
(21, 4, 'санитар', 1, 1, 0, 0, 0, 0),
(22, 4, 'младшая медсестра', 1, 2, 15, 0, 0, 0),
(23, 4, 'старшая медсестра', 1, 3, 30, 0, 0, 0),
(24, 4, 'лаборант', 2, 4, 30, 0, 0, 0),
(25, 4, 'фельдшер', 2, 5, 50, 5, 0, 0),
(26, 4, 'интерн', 2, 6, 70, 10, 0, 0),
(27, 4, 'терапевт', 3, 7, 70, 10, 0, 0),
(28, 4, 'врач-специалист', 3, 8, 75, 20, 0, 0),
(29, 4, 'доктор медицинских наук', 3, 9, 80, 30, 0, 0),
(30, 4, 'министр здравоохранения', 3, 10, 85, 40, 0, 0),
(31, 3, 'Художник граффити', 1, 1, 0, 0, 0, 0),
(32, 3, 'Мастер вышивки', 1, 2, 5, 0, 0, 10),
(33, 3, 'Художник по ткани', 1, 3, 8, 0, 0, 20),
(34, 3, 'Художник-портретист', 1, 4, 10, 0, 0, 30),
(35, 3, 'Ландшафтный дизайн', 1, 5, 12, 0, 0, 40),
(36, 3, 'Дизайн интерьеров', 1, 6, 15, 0, 0, 50),
(37, 3, 'Дизайнер одежды', 1, 7, 20, 0, 0, 60),
(38, 3, 'Дизайнер шрифтов', 2, 8, 20, 0, 0, 60),
(39, 3, 'Дизайнер сайтов', 2, 9, 40, 0, 0, 70),
(40, 3, 'Технический дизайнер', 3, 10, 40, 0, 0, 70),
(41, 16, 'Ночной сторож', 1, 1, 0, 0, 0, 0),
(42, 16, 'Охранник', 1, 2, 5, 0, 10, 0),
(43, 16, 'Инспектор ГИБДД', 1, 3, 15, 0, 20, 0),
(44, 16, 'Полицейский', 2, 4, 15, 0, 20, 0),
(45, 16, 'Участковый полицейский', 2, 5, 25, 0, 30, 0),
(46, 16, 'Оперативник', 2, 6, 35, 0, 45, 0),
(47, 16, 'Пожарный', 2, 7, 40, 0, 60, 0),
(48, 16, 'Омоновец', 2, 8, 50, 0, 70, 0),
(49, 16, 'Спасатель МЧС', 2, 9, 60, 0, 80, 0),
(50, 16, 'Министр МЧС', 3, 10, 60, 20, 80, 0),
(51, 5, 'Лесник', 1, 1, 0, 0, 0, 0),
(52, 5, 'Пастух', 1, 2, 5, 0, 0, 0),
(53, 5, 'Рыболов', 1, 3, 10, 0, 0, 0),
(54, 5, 'Пчеловод', 1, 4, 15, 0, 0, 0),
(55, 5, 'Садовник', 1, 5, 20, 0, 0, 0),
(56, 5, 'Селекционер', 2, 6, 20, 0, 0, 0),
(57, 5, 'Ботаник', 2, 7, 30, 0, 0, 0),
(58, 5, 'Микробиолог', 2, 8, 40, 0, 0, 0),
(59, 5, 'Агроном', 3, 9, 40, 0, 0, 0),
(60, 5, 'Министр сельского хозяйства', 3, 10, 60, 40, 0, 0),
(61, 6, 'Инженер по охране труда и техн', 1, 1, 0, 0, 0, 0),
(62, 6, 'Инженер по качеству', 1, 2, 10, 0, 0, 0),
(63, 6, 'Инженер по стандартизации', 1, 3, 20, 0, 0, 0),
(64, 6, 'Инженер патентовед', 1, 4, 30, 0, 0, 0),
(65, 6, 'Инженер-проектировщик', 2, 5, 30, 0, 0, 0),
(66, 6, 'Инженер-механик', 2, 6, 40, 0, 0, 0),
(67, 6, 'Инженер по связи', 3, 7, 50, 0, 0, 0),
(68, 6, 'Инденер-конструктор', 3, 8, 60, 0, 0, 0),
(69, 6, 'Инженер по коммуникациям', 3, 9, 70, 0, 0, 0),
(70, 6, 'Инженер-энергетик', 3, 10, 80, 0, 0, 0),
(71, 7, 'Киберспортсмен', 1, 1, 0, 0, 0, 0),
(72, 7, 'Модератор', 1, 2, 10, 0, 0, 0),
(73, 7, 'Гейм-дизайнер', 1, 3, 20, 0, 0, 0),
(74, 7, 'Тестеровщик', 1, 4, 30, 0, 0, 0),
(75, 7, 'Системный администратор', 2, 5, 30, 0, 0, 0),
(76, 7, 'Администратор базы данных', 2, 6, 40, 0, 0, 0),
(77, 7, 'Системный аналитик', 3, 7, 40, 0, 0, 0),
(78, 7, 'Программист', 3, 8, 50, 0, 0, 0),
(79, 7, 'Специалист по информационной б', 3, 9, 60, 40, 0, 0),
(80, 7, 'Корпоративный архитектор', 3, 10, 70, 0, 0, 0),
(81, 8, 'Фотограф', 1, 1, 0, 0, 0, 0),
(82, 8, 'Костюмер', 1, 2, 10, 0, 0, 5),
(83, 8, 'Помощник по свету', 1, 3, 15, 0, 0, 10),
(84, 8, 'Актер', 1, 4, 20, 20, 0, 30),
(85, 8, 'Кинооператор', 2, 5, 20, 0, 0, 0),
(86, 8, 'Звукооператор', 2, 6, 30, 0, 0, 0),
(87, 8, 'Композитор', 2, 7, 40, 0, 0, 40),
(88, 8, 'Режиссер', 3, 8, 40, 30, 0, 40),
(89, 8, 'Драматург', 3, 9, 55, 40, 0, 55),
(90, 8, 'Кинопродюсер', 3, 10, 70, 50, 0, 70),
(91, 9, 'Дегустатор', 1, 1, 0, 0, 0, 0),
(92, 9, 'Шоколатье', 1, 2, 5, 0, 0, 10),
(93, 9, 'Пивовар', 1, 3, 10, 0, 0, 15),
(94, 9, 'Винодел', 1, 4, 15, 0, 0, 20),
(95, 9, 'Колбасник', 2, 5, 20, 10, 0, 20),
(96, 9, 'Пекарь', 2, 6, 20, 0, 0, 30),
(97, 9, 'Кондитер', 2, 7, 30, 0, 0, 40),
(98, 9, 'Повар', 2, 8, 40, 0, 0, 50),
(99, 9, 'Технолог', 3, 9, 40, 0, 0, 50),
(100, 9, 'Шеф-повар', 3, 10, 50, 0, 0, 70),
(101, 10, 'Мастер по ремонту одежды', 1, 1, 0, 0, 0, 0),
(102, 10, 'Мастер по ремонту обуви', 1, 2, 5, 0, 0, 0),
(103, 10, 'Скорняк', 1, 3, 10, 0, 0, 0),
(104, 10, 'Кожевник', 1, 4, 15, 0, 0, 5),
(105, 10, 'Швея', 1, 5, 20, 0, 0, 10),
(106, 10, 'Закройщик', 2, 6, 30, 0, 0, 15),
(107, 10, 'Ткач', 2, 7, 40, 0, 0, 20),
(108, 10, 'Обувщик', 2, 8, 50, 0, 0, 30),
(109, 10, 'Модельер-конструктор', 3, 9, 60, 0, 0, 50),
(110, 10, 'Стилист моды', 3, 10, 70, 30, 0, 70),
(111, 11, 'Библиотекарь', 1, 1, 0, 0, 0, 0),
(112, 11, 'Ведущий курсов грамматики', 1, 2, 10, 20, 0, 0),
(113, 11, 'Спич-райтер', 1, 3, 20, 30, 0, 20),
(114, 11, 'Консультант по языку', 1, 4, 30, 40, 0, 0),
(115, 11, 'Лингвист', 2, 5, 30, 50, 0, 0),
(116, 11, 'Переводчик', 2, 6, 40, 60, 0, 0),
(117, 11, 'Сурдопереводчик', 2, 7, 50, 70, 0, 0),
(118, 11, 'Консультант по древним языкам', 3, 8, 50, 80, 0, 0),
(119, 11, 'Синхронный переводчик', 3, 9, 60, 90, 0, 0),
(120, 11, 'Маркетинговый писатель', 3, 10, 70, 100, 0, 30),
(121, 12, 'Разносчик рекламы', 1, 1, 0, 0, 0, 0),
(122, 12, 'Промоутер', 1, 2, 5, 0, 0, 0),
(123, 12, 'Рекламный агент', 1, 3, 10, 0, 0, 0),
(124, 12, 'Мерчандайзер', 1, 4, 15, 5, 0, 0),
(125, 12, 'Копирайтер', 2, 5, 20, 10, 0, 30),
(126, 12, 'Маркетолог', 2, 6, 25, 20, 0, 40),
(127, 12, 'Бренд-менеджер', 2, 7, 30, 30, 0, 50),
(128, 12, 'PR-менеджер', 2, 8, 35, 40, 0, 60),
(129, 12, 'Арт-директор', 3, 9, 50, 50, 0, 70),
(130, 12, 'Директор по маркетингу', 3, 10, 70, 70, 0, 80),
(131, 14, 'HR-менеджер', 1, 1, 0, 0, 0, 0),
(132, 14, 'Сотрудник Отдела кадров', 1, 2, 10, 5, 0, 0),
(133, 14, 'CRM-менеджер', 1, 3, 20, 20, 0, 0),
(134, 14, 'Тимлид', 1, 4, 30, 30, 0, 0),
(135, 14, 'SMM-менеджер', 2, 5, 30, 30, 0, 20),
(136, 14, 'Бизнес-консультант ', 2, 6, 40, 40, 0, 30),
(137, 14, 'Продукт-менеджер', 2, 7, 50, 50, 0, 35),
(138, 14, 'Менеджер проекта', 3, 8, 60, 60, 0, 35),
(139, 14, 'Специалист по бизнес-процессам', 3, 9, 70, 70, 0, 40),
(140, 14, 'Директор', 3, 10, 80, 80, 0, 50),
(141, 15, 'Ассистент', 1, 1, 0, 0, 0, 0),
(142, 15, 'Лаборант', 1, 2, 5, 0, 0, 0),
(143, 15, 'Исследователь-теоретик', 1, 3, 10, 0, 0, 0),
(144, 15, 'Исследователь', 1, 4, 20, 0, 0, 0),
(145, 15, 'Экспериментатор', 2, 5, 30, 0, 0, 0),
(146, 15, 'Прикладной теоретик', 2, 6, 40, 0, 0, 0),
(147, 15, 'Аналитик научных данных', 2, 7, 50, 0, 0, 0),
(148, 15, 'Кандидат наук', 3, 8, 60, 20, 0, 0),
(149, 15, 'Доктор наук', 3, 9, 70, 40, 0, 0),
(150, 15, 'Член-корреспондент РАН', 3, 10, 80, 60, 0, 0);

-- --------------------------------------------------------

--
-- Структура таблицы `race`
--

CREATE TABLE IF NOT EXISTS `race` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

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
(11, 'race.troll'),
(12, 'race.medusa'),
(13, 'race.alien'),
(14, 'race.werewolf'),
(15, 'race.demon'),
(16, 'race.minotaur'),
(17, 'race.harpy'),
(18, 'race.gm-human');

-- --------------------------------------------------------

--
-- Структура таблицы `race_appearance`
--

CREATE TABLE IF NOT EXISTS `race_appearance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `race_id` int(11) NOT NULL,
  `body` int(4) DEFAULT NULL,
  `ears` int(4) DEFAULT NULL,
  `eyebrows` int(4) DEFAULT NULL,
  `eye_color` int(4) DEFAULT NULL,
  `eyes` int(4) DEFAULT NULL,
  `hair_color` int(4) DEFAULT NULL,
  `hair_type` int(4) DEFAULT NULL,
  `head` int(4) DEFAULT NULL,
  `height` int(4) DEFAULT NULL,
  `mouth` int(4) DEFAULT NULL,
  `nose` int(4) DEFAULT NULL,
  `skin_color` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=88 ;

--
-- Дамп данных таблицы `race_appearance`
--

INSERT INTO `race_appearance` (`id`, `race_id`, `body`, `ears`, `eyebrows`, `eye_color`, `eyes`, `hair_color`, `hair_type`, `head`, `height`, `mouth`, `nose`, `skin_color`) VALUES
(4, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 3, NULL),
(5, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 4, NULL),
(6, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 5, NULL),
(7, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 3, NULL),
(8, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 4, NULL),
(9, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 5, NULL),
(10, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 3, NULL),
(11, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 4, NULL),
(12, 5, 2, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 5, NULL),
(13, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 3, NULL),
(14, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 4, NULL),
(15, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 1, 7, NULL, 5, NULL),
(16, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 3, NULL),
(17, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 4, NULL),
(18, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 2, 7, NULL, 5, NULL),
(19, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 3, NULL),
(20, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 4, NULL),
(21, 5, 3, 5, NULL, NULL, NULL, NULL, NULL, 3, 7, NULL, 5, NULL),
(22, 3, NULL, NULL, NULL, NULL, 2, 5, 1, NULL, 1, NULL, NULL, 4),
(23, 3, NULL, NULL, NULL, NULL, 2, 5, 1, NULL, 2, NULL, NULL, 4),
(24, 2, NULL, NULL, NULL, NULL, NULL, 5, 2, NULL, NULL, 2, 7, 3),
(25, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 3, 5),
(26, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 4, 5),
(27, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 6, 5),
(28, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 3, 5),
(29, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 4, 5),
(30, 4, 1, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 6, 5),
(31, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 3, 5),
(32, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 4, 5),
(33, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 3, NULL, 6, 5),
(34, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 3, 5),
(35, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 4, 5),
(36, 4, 2, NULL, NULL, NULL, NULL, 5, 1, NULL, 4, NULL, 6, 5),
(37, 6, 4, 6, NULL, NULL, NULL, NULL, NULL, 4, 7, 9, NULL, 7),
(38, 6, 4, 6, NULL, NULL, NULL, NULL, NULL, 5, 7, 9, NULL, 7),
(39, 7, 2, 6, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, NULL, 8),
(40, 7, 3, 6, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, NULL, 8),
(41, 8, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 1, NULL),
(42, 8, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 2, NULL),
(43, 8, 4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 6, NULL),
(44, 9, NULL, NULL, NULL, 11, NULL, NULL, NULL, NULL, NULL, 10, NULL, 6),
(45, 10, 5, 1, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 4, NULL),
(46, 10, 5, 1, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 5, NULL),
(47, 10, 5, 2, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 4, NULL),
(48, 10, 5, 2, NULL, NULL, NULL, NULL, NULL, NULL, 6, NULL, 5, NULL),
(49, 11, NULL, 5, NULL, NULL, NULL, NULL, NULL, NULL, 7, 9, NULL, 9),
(50, 12, NULL, NULL, NULL, 12, NULL, NULL, 6, NULL, 4, NULL, NULL, 9),
(51, 12, NULL, NULL, NULL, 12, NULL, NULL, 6, NULL, 5, NULL, NULL, 9),
(52, 13, 2, 7, 7, NULL, 8, NULL, 5, 6, 6, 5, NULL, 6),
(53, 13, 2, 7, 7, NULL, 8, NULL, 5, 6, 6, 7, NULL, 6),
(54, 13, 2, 7, 7, NULL, 8, NULL, 5, 6, 6, 8, NULL, 6),
(55, 13, 3, 7, 7, NULL, 8, NULL, 5, 6, 6, 5, NULL, 6),
(56, 13, 3, 7, 7, NULL, 8, NULL, 5, 6, 6, 7, NULL, 6),
(57, 13, 3, 7, 7, NULL, 8, NULL, 5, 6, 6, 8, NULL, 6),
(58, 14, 4, 6, NULL, 12, NULL, NULL, NULL, NULL, 4, 9, NULL, NULL),
(59, 14, 4, 6, NULL, 12, NULL, NULL, NULL, NULL, 5, 9, NULL, NULL),
(60, 15, NULL, 8, 2, 11, NULL, NULL, NULL, NULL, 6, 10, 3, NULL),
(61, 15, NULL, 8, 2, 11, NULL, NULL, NULL, NULL, 6, 10, 4, NULL),
(62, 15, NULL, 8, 2, 11, NULL, NULL, NULL, NULL, 6, 10, 5, NULL),
(63, 15, NULL, 8, 2, 11, NULL, NULL, NULL, NULL, 6, 10, 6, NULL),
(64, 15, NULL, 8, 2, 12, NULL, NULL, NULL, NULL, 6, 10, 3, NULL),
(65, 15, NULL, 8, 2, 12, NULL, NULL, NULL, NULL, 6, 10, 4, NULL),
(66, 15, NULL, 8, 2, 12, NULL, NULL, NULL, NULL, 6, 10, 5, NULL),
(67, 15, NULL, 8, 2, 12, NULL, NULL, NULL, NULL, 6, 10, 6, NULL),
(68, 15, NULL, 8, 6, 11, NULL, NULL, NULL, NULL, 6, 10, 3, NULL),
(69, 15, NULL, 8, 6, 11, NULL, NULL, NULL, NULL, 6, 10, 4, NULL),
(70, 15, NULL, 8, 6, 11, NULL, NULL, NULL, NULL, 6, 10, 5, NULL),
(71, 15, NULL, 8, 6, 11, NULL, NULL, NULL, NULL, 6, 10, 6, NULL),
(72, 15, NULL, 8, 6, 12, NULL, NULL, NULL, NULL, 6, 10, 3, NULL),
(73, 15, NULL, 8, 6, 12, NULL, NULL, NULL, NULL, 6, 10, 4, NULL),
(74, 15, NULL, 8, 6, 12, NULL, NULL, NULL, NULL, 6, 10, 5, NULL),
(75, 15, NULL, 8, 6, 12, NULL, NULL, NULL, NULL, 6, 10, 6, NULL),
(78, 16, 4, 9, NULL, NULL, 7, NULL, NULL, NULL, 4, NULL, NULL, NULL),
(79, 16, 4, 9, NULL, NULL, 7, NULL, NULL, NULL, 5, NULL, NULL, NULL),
(80, 17, 6, 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL),
(81, 17, 6, 3, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL),
(82, 17, 6, 3, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL),
(83, 17, 6, 3, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL),
(84, 17, 6, 4, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL),
(85, 17, 6, 4, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL),
(86, 17, 6, 4, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL),
(87, 17, 6, 4, 6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `users`
--

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
-- Структура таблицы `user_achievements`
--

CREATE TABLE IF NOT EXISTS `user_achievements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Структура таблицы `user_roles`
--

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

-- --------------------------------------------------------

--
-- Структура таблицы `vocation`
--

CREATE TABLE IF NOT EXISTS `vocation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(35) NOT NULL,
  `start_salary` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- Дамп данных таблицы `vocation`
--

INSERT INTO `vocation` (`id`, `name`, `start_salary`) VALUES
(1, 'Работа с животными', 100),
(2, 'Геология', 200),
(3, 'Дизайн и прикладное искусство', 150),
(4, 'Медицина', 300),
(5, 'Животноводство и растениеводство', 125),
(6, 'Инженерное дело', 250),
(7, 'Информационные технологии (IT)', 275),
(8, 'Театр и кино', 175),
(9, 'Кулинария, пищевая промышленность', 150),
(10, 'Одежда', 125),
(11, 'Лингвистика, коммуникации', 175),
(12, 'Маркетинг, реклама и PR', 150),
(13, 'Педагогика и психология', 125),
(14, 'Менеджмент (управление)', 250),
(15, 'Наука', 275),
(16, 'Силовые структуры', 225);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
