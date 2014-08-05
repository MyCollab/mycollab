ALTER TABLE `m_prj_milestone` 
ADD COLUMN `createduser` VARCHAR(45) COLLATE utf8mb4_unicode_ci NULL,
ADD INDEX `PK_m_prj_milestone_4_idx` (`createduser` ASC);
ALTER TABLE `m_prj_milestone` 
ADD CONSTRAINT `PK_m_prj_milestone_4`
  FOREIGN KEY (`createduser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;