ALTER TABLE `m_ecm_external_drive`
ADD COLUMN `shareAccountId` INT(11) NULL,
ADD INDEX `FK_m_ecm_external_drive_1_idx` (`shareAccountId` ASC);
ALTER TABLE `m_ecm_external_drive`
ADD CONSTRAINT `FK_m_ecm_external_drive_1`
  FOREIGN KEY (`shareAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
CREATE TABLE `m_options` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `typeVal` VARCHAR(255) NOT NULL,
  `description` TEXT NULL,
  `orderIndex` INT(3) NULL,
  `sAccountId` INT(11) NOT NULL,
  `createdtime` DATETIME NOT NULL,
  `createdUser` VARCHAR(45) COLLATE 'utf8mb4_unicode_ci' NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_m_options_1_idx` (`sAccountId` ASC),
  INDEX `FK_m_options_2_idx` (`createdUser` ASC),
  CONSTRAINT `FK_m_options_1`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_m_options_2`
    FOREIGN KEY (`createdUser`)
    REFERENCES `s_user` (`username`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);