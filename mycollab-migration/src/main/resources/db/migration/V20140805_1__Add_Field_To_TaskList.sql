ALTER TABLE `m_prj_task_list` 
ADD COLUMN `createduser` VARCHAR(45) COLLATE utf8mb4_unicode_ci NULL,
ADD INDEX `PK_m_prj_task_list_5_idx` (`createduser` ASC);
ALTER TABLE `m_prj_task_list` 
ADD CONSTRAINT `PK_m_prj_task_list_5`
  FOREIGN KEY (`createduser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;