ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `summary` `name` VARCHAR(4000) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_task`
CHANGE COLUMN `taskname` `name` VARCHAR(400) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_risk`
CHANGE COLUMN `riskname` `name` VARCHAR(400) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;

ALTER TABLE `m_prj_milestone` ADD COLUMN `priority` VARCHAR(45) NULL;
UPDATE m_prj_risk SET priority='Medium' WHERE priority IS NULL AND id > 0;

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

UPDATE `s_account_theme` SET `topMenuBg`='FFFFFF', `topMenuBgSelected`='707070',
    `topMenuText`='707070', `topMenuTextSelected`='FFFFFF', `vTabsheetBg`='F9FFFF', `vTabsheetBgSelected`='707070',
    `actionBtn`='46ACE8', `dangerBtn`='E3793B'
    WHERE `isDefault`='1';

UPDATE m_prj_task SET priority='Medium' WHERE priority IS NULL AND id > 0;
UPDATE m_tracker_bug SET priority='Medium' WHERE priority IS NULL AND id > 0;

ALTER TABLE `m_prj_milestone`
DROP FOREIGN KEY `PK_m_prj_milestone_1`,
DROP FOREIGN KEY `PK_m_prj_milestone_4`;
ALTER TABLE `m_prj_milestone`
CHANGE COLUMN `startdate` `startDate` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `enddate` `endDate` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `owner` `assignUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `createduser` `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `deadline` `dueDate` DATETIME NULL DEFAULT NULL ;
ALTER TABLE `m_prj_milestone`
ADD CONSTRAINT `PK_m_prj_milestone_1`
  FOREIGN KEY (`assignUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `PK_m_prj_milestone_4`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_risk` ADD COLUMN `remainEstimate` DOUBLE NULL;

ALTER TABLE `m_tracker_component` CHANGE COLUMN `componentname` `name` VARCHAR(1000) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;
ALTER TABLE `m_tracker_version` CHANGE COLUMN `versionname` `name` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;
