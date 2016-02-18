DROP TABLE `m_prj_problem`;
ALTER TABLE `m_prj_risk`
ADD COLUMN `startDate` DATETIME NULL,
ADD COLUMN `endDate` DATETIME NULL;
ALTER TABLE `m_prj_project`
DROP COLUMN `actualEndDate`,
DROP COLUMN `actualStartDate`;
ALTER TABLE `m_prj_milestone`
DROP COLUMN `actualenddate`,
DROP COLUMN `actualstartdate`;
ALTER TABLE `m_prj_task`
DROP COLUMN `actualEndDate`,
DROP COLUMN `actualStartDate`;
ALTER TABLE `m_prj_risk`
DROP COLUMN `prjKey`,
ADD COLUMN `milestoneId` INT(11) NULL,
ADD INDEX `FK_m_prj_risk1_5_idx` (`milestoneId` ASC);
ALTER TABLE `m_prj_risk`
ADD CONSTRAINT `FK_m_prj_risk1_5`
  FOREIGN KEY (`milestoneId`)
  REFERENCES `m_prj_milestone` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;
ALTER TABLE `m_prj_risk`
  ADD COLUMN `ganttIndex` INT(5) UNSIGNED NULL;
ALTER TABLE `m_tracker_bug`
  ADD COLUMN `ganttIndex` INT(5) UNSIGNED NULL;
ALTER TABLE `m_tracker_bug`
  ADD COLUMN `percentagecomplete` DOUBLE NULL;
ALTER TABLE `m_prj_risk`
  ADD COLUMN `percentagecomplete` DOUBLE NULL;
ALTER TABLE `m_prj_customize_view`
    DROP COLUMN `displayProblem`;
