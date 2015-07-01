ALTER TABLE `s_user_account` ADD COLUMN `lastModuleVisit` VARCHAR(45) NULL;
DROP TABLE `s_user_preference`;
DROP TABLE `s_user_information`;
DROP TABLE `s_user_account_invitation`;
ALTER TABLE `s_user_account`
ADD COLUMN `inviteUser` VARCHAR(45) COLLATE 'utf8mb4_unicode_ci' NULL,
ADD INDEX `FK_s_user_account_4_idx` (`inviteUser` ASC);
ALTER TABLE `s_user_account`
ADD CONSTRAINT `FK_s_user_account_4`
  FOREIGN KEY (`inviteUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE SET NULL;