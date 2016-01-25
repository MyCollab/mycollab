ALTER TABLE `m_prj_project`
DROP FOREIGN KEY `FK_m_prj_project_2`;
ALTER TABLE `m_prj_project`
CHANGE COLUMN `owner` `createUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
ADD COLUMN `lead` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL,
ADD INDEX `FK_m_prj_project_6_idx` (`lead` ASC);
ALTER TABLE `m_prj_project`
ADD CONSTRAINT `FK_m_prj_project_2`
  FOREIGN KEY (`createUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_project_5`
  FOREIGN KEY (`lead`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;