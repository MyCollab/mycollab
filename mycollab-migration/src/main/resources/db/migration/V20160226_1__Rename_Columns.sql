ALTER TABLE `m_tracker_bug`
DROP FOREIGN KEY `FK_m_tracker_bug_3`;
ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_tracker_bug`
ADD CONSTRAINT `FK_m_tracker_bug_3`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `s_relay_email_notification`
CHANGE COLUMN `typeid` `typeId` TEXT CHARACTER SET 'utf8mb4' NOT NULL ;
ALTER TABLE `m_prj_milestone`
DROP FOREIGN KEY `PK_m_prj_milestone_2`;
ALTER TABLE `m_prj_milestone`
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_prj_milestone`
ADD CONSTRAINT `PK_m_prj_milestone_2`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_prj_task`
DROP FOREIGN KEY `FK_m_prj_task_1`;
ALTER TABLE `m_prj_task`
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_prj_task`
ADD CONSTRAINT `FK_m_prj_task_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_prj_risk`
DROP FOREIGN KEY `FK_m_prj_risk1_1`;
ALTER TABLE `m_prj_risk`
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_prj_risk` 
DROP COLUMN `typeid`,
DROP COLUMN `type`;
ALTER TABLE `m_prj_risk`
ADD CONSTRAINT `FK_m_prj_risk1_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_prj_time_logging` 
CHANGE COLUMN `typeid` `typeId` INT(11) NULL DEFAULT NULL ;
ALTER TABLE `m_prj_role` 
DROP FOREIGN KEY `FK_m_prj_role_2`;
ALTER TABLE `m_prj_role` 
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_prj_role` 
ADD CONSTRAINT `FK_m_prj_role_2`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_monitor_item` 
  CHANGE COLUMN `typeid` `typeId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_tracker_component` 
DROP FOREIGN KEY `FK_m_tracker_component_1`;
ALTER TABLE `m_tracker_component` 
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_tracker_component` 
ADD CONSTRAINT `FK_m_tracker_component_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_tracker_version` 
DROP FOREIGN KEY `FK_m_tracker_version_1`;
ALTER TABLE `m_tracker_version` 
CHANGE COLUMN `projectid` `projectId` INT(10) UNSIGNED NOT NULL ;
ALTER TABLE `m_tracker_version` 
ADD CONSTRAINT `FK_m_tracker_version_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
ALTER TABLE `m_tracker_bug_related_item` 
DROP FOREIGN KEY `FK_ m_tracker_bug_related_item_1`;
ALTER TABLE `m_tracker_bug_related_item` 
CHANGE COLUMN `bugid` `bugId` INT(10) UNSIGNED NOT NULL ,
CHANGE COLUMN `typeid` `typeId` INT(11) NOT NULL ;
ALTER TABLE `m_tracker_bug_related_item` 
ADD CONSTRAINT `FK_ m_tracker_bug_related_item_1`
  FOREIGN KEY (`bugId`)
  REFERENCES `m_tracker_bug` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;  
ALTER TABLE `s_timeline_tracking` 
  CHANGE COLUMN `typeid` `typeId` INT(11) UNSIGNED NOT NULL ;
