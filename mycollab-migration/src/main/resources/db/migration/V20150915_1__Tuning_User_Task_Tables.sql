ALTER TABLE `s_user_account` DROP FOREIGN KEY `FK_s_user_account_3`, DROP FOREIGN KEY `FK_s_user_account_4`;
ALTER TABLE `s_user_account`
ADD CONSTRAINT `FK_s_user_account_3`
  FOREIGN KEY (`roleId`)
  REFERENCES `s_roles` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_s_user_account_4`
  FOREIGN KEY (`inviteUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;
ALTER TABLE `m_prj_task`
CHANGE COLUMN `duration` `duration` INT(11) NULL DEFAULT NULL ;