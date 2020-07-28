CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `pass` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `type` enum('1','2','3','4','5') COLLATE utf8_spanish_ci NOT NULL DEFAULT 1,
  `creation` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

CREATE TABLE IF NOT EXISTS `chat` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id1` int NOT NULL,
  `user_id2` int NOT NULL,
  `content` text COLLATE utf8_spanish_ci,
  `type` enum('1','2','3','4','5') COLLATE utf8_spanish_ci NOT NULL DEFAULT 1,
  `creation` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

CREATE TABLE IF NOT EXISTS `couple` (
  `user_id1` int NOT NULL,
  `name1` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  `user_id2` int NOT NULL,
  `name2` varchar(10) COLLATE utf8_spanish_ci NOT NULL,
  PRIMARY KEY (`user_id1`, `user_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

CREATE TABLE IF NOT EXISTS `loc` (
  `user_id` int DEFAULT 0,
  `lat` DOUBLE DEFAULT 0,
  `lon` DOUBLE DEFAULT 0,
  `creation` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;


INSERT INTO `user`(`name`, `pass`, `type`) VALUES ('1', 'SVntVsW58patL5MUdv9FPw==', 1); //usuario principal: 1 narkol
INSERT INTO `user`(`name`, `pass`, `type`) VALUES ('prueba2', 'SVntVsW58patL5MUdv9FPw==', 2); //usuario comun: prueba2 narkol
INSERT INTO `user`(`name`, `pass`, `type`) VALUES ('prueba3', 'SVntVsW58patL5MUdv9FPw==', 2); //usuario comun: prueba3 narkol


INSERT INTO `chat`(`user_id1`, `user_id2`, `content`, `type`) VALUES (1, 2, 'VBB7KKdPo5d1htqVWuKNIg==', 1); //hola mensaje
INSERT INTO `chat`(`user_id1`, `user_id2`, `content`, `type`) VALUES (1, 2, 'VBB7KKdPo5d1htqVWuKNIg==', 1); //hola mensaje
INSERT INTO `chat`(`user_id1`, `user_id2`, `content`, `type`) VALUES (2, 1, 'VBB7KKdPo5d1htqVWuKNIg==', 1); //hola mensaje
INSERT INTO `chat`(`user_id1`, `user_id2`, `content`, `type`) VALUES (1, 3, 'VBB7KKdPo5d1htqVWuKNIg==', 1); //hola mensaje


INSERT INTO `couple`(`user_id1`, `user_id2`, `name1`, `name2`) VALUES (1, 2, '1', 'prueba2');
INSERT INTO `couple`(`user_id1`, `user_id2`, `name1`, `name2`) VALUES (1, 3, '1', 'prueba3');
INSERT INTO `couple`(`user_id1`, `user_id2`, `name1`, `name2`) VALUES (2, 1, 'prueba2', '1');