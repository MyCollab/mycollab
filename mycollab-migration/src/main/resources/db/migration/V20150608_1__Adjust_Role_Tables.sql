ALTER TABLE `s_user_account`
DROP FOREIGN KEY `FK_s_user_account_3`;
ALTER TABLE `s_user_account`
ADD CONSTRAINT `FK_s_user_account_3`
  FOREIGN KEY (`roleId`)
  REFERENCES `s_roles` (`id`)
  ON DELETE SET NULL
  ON UPDATE SET NULL;

ALTER TABLE `m_prj_member`
CHANGE COLUMN `projectRoleId` `projectRoleId` INT(11) UNSIGNED NULL DEFAULT NULL ,
ADD INDEX `FK_m_prj_member_4_idx` (`projectRoleId` ASC);
ALTER TABLE `m_prj_member`
ADD CONSTRAINT `FK_m_prj_member_4`
  FOREIGN KEY (`projectRoleId`)
  REFERENCES `m_prj_role` (`id`)
  ON DELETE SET NULL
  ON UPDATE SET NULL;