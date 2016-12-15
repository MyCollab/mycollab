CREATE TABLE `m_group` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(400) NOT NULL,
  `description` MEDIUMTEXT NULL,
  `sAccountId` INT(11) NOT NULL,
  `createdTime` DATETIME NOT NULL,
  `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_ m_group_1_idx` (`createdUser` ASC),
  CONSTRAINT `FK_ m_group_1`
    FOREIGN KEY (`createdUser`)
    REFERENCES `s_user` (`username`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);

CREATE TABLE `m_group_user` (
  `id` INT(11) UNSIGNED NOT NULL,
  `groupId` INT(11) UNSIGNED NOT NULL,
  `user` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` DATETIME NULL,
  `isOwner` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_ m_group_user_1_idx` (`groupId` ASC),
  INDEX `FK_ m_group_user_2_idx` (`user` ASC),
  CONSTRAINT `FK_ m_group_user_1`
    FOREIGN KEY (`groupId`)
    REFERENCES `m_group` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_ m_group_user_2`
    FOREIGN KEY (`user`)
    REFERENCES `s_user` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);