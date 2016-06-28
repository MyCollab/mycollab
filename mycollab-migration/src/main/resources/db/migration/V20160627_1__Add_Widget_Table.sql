CREATE TABLE `s_widgets` (
  `id` INT(11) UNSIGNED NOT NULL,
  `sAccountId` INT(11) NOT NULL,
  `extraTypeId` INT(11) NULL,
  `name` VARCHAR(100) NOT NULL,
  `displayText` VARCHAR(1000) NOT NULL,
  `queryId` INT(11) NULL,
  `queryText` TEXT NULL,
  `createdUser` VARCHAR(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_s_widgets_1_idx` (`sAccountId` ASC),
  INDEX `FK_s_widgets_2_idx` (`createdUser` ASC),
  CONSTRAINT `FK_s_widgets_1`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_s_widgets_2`
    FOREIGN KEY (`createdUser`)
    REFERENCES `s_user` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

ALTER TABLE `s_tag`
ADD COLUMN `createdTime` DATETIME NULL,
ADD COLUMN `createdUser` VARCHAR(45) COLLATE utf8mb4_unicode_ci NULL,
ADD INDEX `FK_s_tag_2_idx` (`createdUser` ASC);
ALTER TABLE `s_tag`
ADD CONSTRAINT `FK_s_tag_2`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
DROP TABLE `m_tracker_metadata`;
DROP TABLE `m_tracker_query`;

