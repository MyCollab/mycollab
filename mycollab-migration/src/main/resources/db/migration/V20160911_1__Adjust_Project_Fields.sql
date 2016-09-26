ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `summary` `name` VARCHAR(4000) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_task`
CHANGE COLUMN `taskname` `name` VARCHAR(400) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_risk`
CHANGE COLUMN `riskname` `name` VARCHAR(400) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_milestone` ADD COLUMN `priority` VARCHAR(45) NULL;

ALTER TABLE `m_tracker_bug`
DROP FOREIGN KEY `FK_m_tracker_bug_2`;
ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `assignuser` `assignUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL ;
ALTER TABLE `m_tracker_bug`
ADD CONSTRAINT `FK_m_tracker_bug_2`
  FOREIGN KEY (`assignUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_risk`
DROP FOREIGN KEY `FK_m_prj_risk1_3`;
ALTER TABLE `m_prj_risk`
CHANGE COLUMN `assigntouser` `assignUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_risk`
ADD CONSTRAINT `FK_m_prj_risk1_3`
  FOREIGN KEY (`assignUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_risk`
CHANGE COLUMN `datedue` `dueDate` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `startdate` `startDate` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `enddate` `endDate` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `duedate` `dueDate` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `m_prj_task`
CHANGE COLUMN `startdate` `startDate` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `enddate` `endDate` DATETIME NULL DEFAULT NULL,
CHANGE COLUMN `deadline` `dueDate` DATETIME NULL DEFAULT NULL,
CHANGE COLUMN `notes` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_prj_risk`
DROP FOREIGN KEY `FK_m_prj_risk1_2`;
ALTER TABLE `m_prj_risk`
CHANGE COLUMN `raisedbyuser` `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_risk`
ADD CONSTRAINT `FK_m_prj_risk1_2`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_tracker_bug`
DROP FOREIGN KEY `FK_m_tracker_bug_1`;
ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `logby` `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_tracker_bug`
ADD CONSTRAINT `FK_m_tracker_bug_1`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_task`
DROP FOREIGN KEY `FK_m_prj_task_5`;
ALTER TABLE `m_prj_task`
CHANGE COLUMN `logBy` `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_task`
ADD CONSTRAINT `FK_m_prj_task_5`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_customize_view`
DROP COLUMN `displayRisk`,
DROP COLUMN `displayBug`,
DROP COLUMN `displayTask`,
ADD COLUMN `displayTicket` BIT(1) NULL;

UPDATE `s_account_theme` SET `topMenuBg`='FFFFFF', `topMenuBgSelected`='4C4C4C',
    `topMenuText`='4C4C4C', `topMenuTextSelected`='FFFFFF', `vTabsheetBg`='F9FFFF', `vTabsheetBgSelected`='4C4C4C'
    WHERE `isDefault`='1';
