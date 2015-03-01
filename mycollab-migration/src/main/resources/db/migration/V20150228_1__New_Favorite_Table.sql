CREATE TABLE `s_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastUpdatedTime` datetime NOT NULL,
  `createdTime` datetime NOT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_favorite_1_idx` (`createdUser`),
  KEY `FK_s_favorite_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_favorite_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_favorite_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
