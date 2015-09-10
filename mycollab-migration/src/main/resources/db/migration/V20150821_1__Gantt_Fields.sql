ALTER TABLE `m_prj_task`
ADD COLUMN `ganttindex` INT(5) UNSIGNED NULL;

ALTER TABLE `m_prj_predecessor`
ADD COLUMN `lagDay` INT(3) NOT NULL DEFAULT 0;

ALTER TABLE `m_prj_predecessor`
DROP COLUMN `typeId`,
ADD COLUMN `sourceId` INT(11) UNSIGNED NOT NULL AFTER `lagDay`,
ADD COLUMN `descId` INT(11) UNSIGNED NOT NULL AFTER `sourceId`,
ADD INDEX `FK_m_prj_predecessor_2_idx` (`sourceId` ASC),
ADD INDEX `FK_m_prj_predecessor_3_idx` (`descId` ASC);
ALTER TABLE `m_prj_predecessor`
ADD CONSTRAINT `FK_m_prj_predecessor_2`
  FOREIGN KEY (`sourceId`)
  REFERENCES `m_prj_task` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_predecessor_3`
  FOREIGN KEY (`descId`)
  REFERENCES `m_prj_task` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
