ALTER TABLE `m_prj_task` 
ADD COLUMN `parentTaskId` INT(10) UNSIGNED NULL,
ADD INDEX `FK_m_prj_task_6_idx` (`parentTaskId` ASC);
ALTER TABLE `m_prj_task` 
ADD CONSTRAINT `FK_m_prj_task_6`
  FOREIGN KEY (`parentTaskId`)
  REFERENCES `m_prj_task` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `s_activitystream` 
DROP COLUMN `createdUserDisplayName`;  