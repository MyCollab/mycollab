ALTER TABLE `m_prj_milestone`
ADD COLUMN `actualstartdate` DATETIME NULL,
ADD COLUMN `actualenddate` DATETIME NULL,
ADD COLUMN `deadline` DATETIME NULL;

ALTER TABLE `m_prj_project`
ADD COLUMN `deadline` DATETIME NULL;

ALTER TABLE `m_prj_milestone`
ADD COLUMN `ganttIndex` INT(5) UNSIGNED NULL;
