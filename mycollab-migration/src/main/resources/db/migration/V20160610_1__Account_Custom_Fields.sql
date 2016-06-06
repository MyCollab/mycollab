ALTER TABLE `s_account` ADD COLUMN `displayEmailPublicly` TINYINT(1) NULL;
UPDATE `s_account` SET displayEmailPublicly = 1 WHERE id > 0;
