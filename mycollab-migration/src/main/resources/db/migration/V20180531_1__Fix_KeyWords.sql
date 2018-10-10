ALTER TABLE `m_prj_project`
DROP FOREIGN KEY `FK_m_prj_project_5`;
ALTER TABLE `m_prj_project`
CHANGE COLUMN `lead` `memLead` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;
ALTER TABLE `m_prj_project`
ADD CONSTRAINT `FK_m_prj_project_5`
  FOREIGN KEY (`memLead`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;