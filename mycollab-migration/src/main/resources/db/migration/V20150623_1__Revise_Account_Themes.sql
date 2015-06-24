ALTER TABLE `s_account_theme` DROP COLUMN `logoPath`;
ALTER TABLE `s_account` ADD COLUMN `faviconPath` VARCHAR(225) NULL;
DROP TABLE `s_account_settings`;
ALTER TABLE `s_account_theme`
ADD COLUMN `sAccountId` INT(11) NULL,
ADD INDEX `FK_s_account_theme_1_idx` (`sAccountId` ASC);
ALTER TABLE `s_account_theme`
ADD CONSTRAINT `FK_s_account_theme_1`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
