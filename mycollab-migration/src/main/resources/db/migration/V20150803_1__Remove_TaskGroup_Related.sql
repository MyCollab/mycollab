ALTER TABLE `m_prj_task`
DROP FOREIGN KEY `FK_m_prj_task_2`;
ALTER TABLE `m_prj_task`
ADD COLUMN `milestoneId` INT(11) NULL,
ADD INDEX `FK_m_prj_task_7_idx` (`milestoneId` ASC);
ALTER TABLE `m_prj_task`
ADD CONSTRAINT `FK_m_prj_task_7`
  FOREIGN KEY (`milestoneId`)
  REFERENCES `m_prj_milestone` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;