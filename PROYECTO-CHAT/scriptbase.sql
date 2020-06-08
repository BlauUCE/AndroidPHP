CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `pass` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `type` enum('1','2','3','4','5') COLLATE utf8_spanish_ci NOT NULL DEFAULT 1,
  `creation` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

CREATE TABLE IF NOT EXISTS `chat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `content` text COLLATE utf8_spanish_ci,
  `type` enum('1','2','3','4','5') COLLATE utf8_spanish_ci NOT NULL DEFAULT 1,
  `creation` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

CREATE TABLE IF NOT EXISTS `couple` (
  `user_id1` int NOT NULL,
  `user_id2` int NOT NULL,
  PRIMARY KEY (`user_id1`, `user_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

INSERT INTO `user`(`name`, `pass`, `type`) VALUES ('admin', 'admin', 1);
INSERT INTO `user`(`name`, `pass`, `type`) VALUES ('prueba', 'prueba', 1);


INSERT INTO `chat`(`user_id`, `content`, `type`) VALUES (1, 'texto chat', 1);
INSERT INTO `chat`(`user_id`, `content`, `type`) VALUES (1, 'texto chat admin', 1);
INSERT INTO `chat`(`user_id`, `content`, `type`) VALUES (2, 'texto chat user 2', 1);


INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (1, 2);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (1, 3);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (1, 4);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (2, 3);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (2, 4);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (20, 1);
INSERT INTO `couple`(`user_id1`, `user_id2`) VALUES (20, 2);