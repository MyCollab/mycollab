ALTER TABLE `m_audit_log`
CHANGE COLUMN `posteddate` `createdTime` DATETIME NOT NULL ;

ALTER TABLE `m_monitor_item`
CHANGE COLUMN `monitor_date` `createdTime` DATETIME NOT NULL ;

ALTER TABLE `m_options`
CHANGE COLUMN `createdtime` `createdTime` DATETIME NOT NULL ;

ALTER TABLE `m_prj_member`
CHANGE COLUMN `joinDate` `createdTime` DATETIME NOT NULL ;

ALTER TABLE `m_prj_member`
ADD COLUMN `lastUpdatedTime` DATETIME NULL AFTER `overtimeBillingRate`;

ALTER TABLE `m_prj_message` DROP COLUMN `posteddate`;

ALTER TABLE `m_prj_message`
DROP FOREIGN KEY `FK_m_prj_message_1`;

ALTER TABLE `m_prj_message`
CHANGE COLUMN `posteduser` `createdUser` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL ;
ALTER TABLE `m_prj_message`
ADD CONSTRAINT `FK_m_prj_message_1`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_message`
DROP FOREIGN KEY `FK_m_prj_message_2`;
ALTER TABLE `m_prj_message`
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_prj_message`
ADD CONSTRAINT `FK_m_prj_message_2`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_prj_milestone`
DROP COLUMN `orderIndex`,
DROP COLUMN `ganttIndex`,
CHANGE COLUMN `startDate` `startDate` DATE NULL DEFAULT NULL,
CHANGE COLUMN `endDate` `endDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `dueDate` `dueDate` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_prj_project`
CHANGE COLUMN `planStartDate` `planStartDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `planEndDate` `planEndDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `deadline` `deadline` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_prj_risk`
DROP COLUMN `ganttIndex`,
CHANGE COLUMN `raisedDate` `raisedDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `dueDate` `dueDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `startDate` `startDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `endDate` `endDate` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_prj_task`
DROP COLUMN `ganttindex`,
CHANGE COLUMN `startDate` `startDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `endDate` `endDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `dueDate` `dueDate` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_prj_time_logging`
CHANGE COLUMN `approveDate` `approveTime` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `dueDate` `dueDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `startDate` `startDate` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `endDate` `endDate` DATE NULL DEFAULT NULL ;

ALTER TABLE `s_account`
CHANGE COLUMN `trialFrom` `trialFrom` DATE NULL DEFAULT NULL ,
CHANGE COLUMN `trialTo` `trialTo` DATE NULL DEFAULT NULL ;

ALTER TABLE `s_roles`
ADD COLUMN `createdTime` DATETIME NULL AFTER `isDefault`,
ADD COLUMN `lastUpdatedTime` DATETIME NULL AFTER `createdTime`;

ALTER TABLE `m_tracker_bug` DROP COLUMN `ganttIndex`;

ALTER TABLE `s_user`
CHANGE COLUMN `birthday` `birthday` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_tracker_version`
CHANGE COLUMN `duedate` `duedate` DATE NULL DEFAULT NULL ;

ALTER TABLE `m_prj_invoice`
CHANGE COLUMN `issueDate` `issueDate` DATE NOT NULL ;


