CREATE TABLE `m_notification_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeId` varchar(45) NOT NULL,
  `notificationUser` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `message` varchar(1000) NOT NULL,
  `isRead` tinyint(1) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `FK_m_notification_item_1_idx` (`notificationUser`),
  KEY `FK_m_notification_item_2_idx` (`sAccountId`),
  CONSTRAINT `FK_m_notification_item_1` FOREIGN KEY (`notificationUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_notification_item_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `m_prj_time_logging`
ADD COLUMN `isApproved` BIT(1) NULL,
ADD COLUMN `approveUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci  NULL,
ADD COLUMN `approveDate` DATETIME NULL,
ADD INDEX `FK_m_prj_time_logging_4_idx` (`approveUser` ASC);
ALTER TABLE `m_prj_time_logging`
ADD CONSTRAINT `FK_m_prj_time_logging_4`
  FOREIGN KEY (`approveUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;