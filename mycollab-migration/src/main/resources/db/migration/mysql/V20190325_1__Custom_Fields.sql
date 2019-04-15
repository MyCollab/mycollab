ALTER TABLE `m_tracker_bug`
DROP COLUMN `cus_dbl_03`,
DROP COLUMN `cus_dbl_02`,
DROP COLUMN `cus_dbl_01`,
DROP COLUMN `cus_time_04`,
DROP COLUMN `cus_time_03`,
DROP COLUMN `cus_time_02`,
DROP COLUMN `cus_time_01`,
DROP COLUMN `cus_str_05`,
DROP COLUMN `cus_str_04`,
DROP COLUMN `cus_str_03`,
DROP COLUMN `cus_str_02`,
DROP COLUMN `cus_str_01`,
DROP COLUMN `cus_int_10`,
DROP COLUMN `cus_int_09`,
DROP COLUMN `cus_int_08`,
DROP COLUMN `cus_int_07`,
DROP COLUMN `cus_int_06`,
DROP COLUMN `cus_int_05`,
DROP COLUMN `cus_int_04`,
DROP COLUMN `cus_int_03`,
DROP COLUMN `cus_int_02`,
DROP COLUMN `cus_int_01`;

ALTER TABLE `m_tracker_bug` RENAME TO  `m_prj_bug` ;
ALTER TABLE `m_prj_bug` 
DROP FOREIGN KEY `FK_m_tracker_bug_1`,
DROP FOREIGN KEY `FK_m_tracker_bug_2`,
DROP FOREIGN KEY `FK_m_tracker_bug_3`,
DROP FOREIGN KEY `FK_m_tracker_bug_4`,
DROP FOREIGN KEY `FK_m_tracker_bug_5`;
ALTER TABLE `m_prj_bug` 
;
ALTER TABLE `m_prj_bug` RENAME INDEX `FK_m_tracker_bug_5` TO `FK_m_prj_bug_5`;
ALTER TABLE `m_prj_bug` ALTER INDEX `FK_m_prj_bug_5` VISIBLE;
ALTER TABLE `m_prj_bug` 
ADD CONSTRAINT `FK_m_prj_bug_1`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_bug_2`
  FOREIGN KEY (`assignUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_bug_3`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_bug_4`
  FOREIGN KEY (`milestoneId`)
  REFERENCES `m_prj_milestone` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_bug_5`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;
  
ALTER TABLE `m_prj_bug` 
;
ALTER TABLE `m_prj_bug` RENAME INDEX `FK_m_tracker_bug_6` TO `FK_m_prj_bug_6`;
ALTER TABLE `m_prj_bug` ALTER INDEX `FK_m_prj_bug_6` VISIBLE;
ALTER TABLE `m_prj_bug` RENAME INDEX `FK_m_tracker_bug_4` TO `FK_m_prj_bug_4`;
ALTER TABLE `m_prj_bug` ALTER INDEX `FK_m_prj_bug_4` VISIBLE;
ALTER TABLE `m_prj_bug` RENAME INDEX `FK_m_tracker_bug_1` TO `FK_m_prj_bug_1`;
ALTER TABLE `m_prj_bug` ALTER INDEX `FK_m_prj_bug_1` VISIBLE;
ALTER TABLE `m_prj_bug` RENAME INDEX `FK_m_tracker_bug_2` TO `FK_m_prj_bug_2`;
ALTER TABLE `m_prj_bug` ALTER INDEX `FK_m_prj_bug_2` VISIBLE;  

ALTER TABLE `m_tracker_component`  RENAME TO  `m_prj_component` ;
ALTER TABLE `m_prj_component` 
DROP FOREIGN KEY `FK_m_tracker_component_1`,
DROP FOREIGN KEY `FK_m_tracker_component_2`,
DROP FOREIGN KEY `FK_m_tracker_component_3`,
DROP FOREIGN KEY `FK_m_tracker_component_4`;
ALTER TABLE `m_prj_component` 
;
ALTER TABLE `m_prj_component` RENAME INDEX `FK_m_tracker_component_1` TO `FK_m_prj_component_1`;
ALTER TABLE `m_prj_component` ALTER INDEX `FK_m_prj_component_1` VISIBLE;
ALTER TABLE `m_prj_component` RENAME INDEX `FK_m_tracker_component_4` TO `FK_m_prj_component_4`;
ALTER TABLE `m_prj_component` ALTER INDEX `FK_m_prj_component_4` VISIBLE;
ALTER TABLE `m_prj_component` RENAME INDEX `FK_m_tracker_component_2` TO `FK_m_prj_component_2`;
ALTER TABLE `m_prj_component` ALTER INDEX `FK_m_prj_component_2` VISIBLE;
ALTER TABLE `m_prj_component` RENAME INDEX `FK_m_tracker_component_3` TO `FK_m_prj_component_3`;
ALTER TABLE `m_prj_component` ALTER INDEX `FK_m_prj_component_3` VISIBLE;
ALTER TABLE `m_prj_component` 
ADD CONSTRAINT `FK_m_prj_component_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_component_2`
  FOREIGN KEY (`userlead`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_component_3`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_component_4`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;


ALTER TABLE `m_tracker_version` RENAME TO  `m_prj_version` ;
ALTER TABLE `m_prj_version` 
DROP FOREIGN KEY `FK_m_tracker_version_1`,
DROP FOREIGN KEY `FK_m_tracker_version_2`,
DROP FOREIGN KEY `FK_m_tracker_version_3`;
ALTER TABLE `m_prj_version` 
;
ALTER TABLE `m_prj_version` RENAME INDEX `FK_m_tracker_version_1` TO `FK_m_prj_version_1`;
ALTER TABLE `m_prj_version` ALTER INDEX `FK_m_prj_version_1` VISIBLE;
ALTER TABLE `m_prj_version` RENAME INDEX `FK_m_tracker_version_3` TO `FK_m_prj_version_3`;
ALTER TABLE `m_prj_version` ALTER INDEX `FK_m_prj_version_3` VISIBLE;
ALTER TABLE `m_prj_version` RENAME INDEX `FK_m_tracker_version_2` TO `FK_m_prj_version_2`;
ALTER TABLE `m_prj_version` ALTER INDEX `FK_m_prj_version_2` VISIBLE;
ALTER TABLE `m_prj_version` 
ADD CONSTRAINT `FK_m_prj_version_1`
  FOREIGN KEY (`projectId`)
  REFERENCES `m_prj_project` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_version_2`
  FOREIGN KEY (`createdUser`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK_m_prj_version_3`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `m_tracker_bug_related_item`
DROP FOREIGN KEY `FK_ m_tracker_bug_related_item_1`;
ALTER TABLE `m_tracker_bug_related_item`
ADD COLUMN `ticketType` VARCHAR(45) NULL,
CHANGE COLUMN `bugId` `ticketId` INT(10) UNSIGNED NOT NULL ,
DROP INDEX `FK_ m_tracker_bug_related_item_1` ;

ALTER TABLE `m_tracker_bug_related_item`
RENAME TO  `m_prj_ticket_relation` ;

UPDATE m_prj_ticket_relation
SET ticketType = 'Project-Bug'
WHERE id > 0;

ALTER TABLE `m_prj_ticket_relation`
CHANGE COLUMN `ticketType` `ticketType` VARCHAR(45) NOT NULL ;


ALTER TABLE `m_prj_ticket_relation`;
ALTER TABLE `m_prj_ticket_relation` RENAME INDEX `INDEX_m_tracker_bug_related_item_2` TO `INDEX_m_prj_ticket_relation_item_2`;
ALTER TABLE `m_prj_ticket_relation` ALTER INDEX `INDEX_m_prj_ticket_relation_item_2` VISIBLE;
ALTER TABLE `m_prj_ticket_relation` RENAME INDEX `INDEX_m_tracker_bug_related_item_3` TO `INDEX_m_prj_ticket_relation_item_3`;
ALTER TABLE `m_prj_ticket_relation` ALTER INDEX `INDEX_m_prj_ticket_relation_item_3` VISIBLE;

CREATE TABLE `m_prj_ticket_key` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `projectId` INT(10) UNSIGNED NOT NULL,
  `ticketId` INT(11) UNSIGNED NOT NULL,
  `ticketType` VARCHAR(45) NOT NULL,
  `ticketKey` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `PK_m_prj_ticket_id_1_idx` (`projectId` ASC) VISIBLE,
  CONSTRAINT `PK_m_prj_ticket_id_1`
    FOREIGN KEY (`projectId`)
    REFERENCES `m_prj_project` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

ALTER TABLE `m_prj_task` DROP COLUMN `taskkey`, DROP COLUMN `taskindex`;;
ALTER TABLE `m_prj_bug` DROP COLUMN `bugIndex`, DROP COLUMN `bugkey`;

ALTER TABLE `m_prj_ticket_relation`
ADD COLUMN `rel` VARCHAR(45) NULL AFTER `typeId`,
ADD COLUMN `comment` TEXT NULL AFTER `rel`,
CHANGE COLUMN `ticketType` `ticketType` VARCHAR(45) NOT NULL AFTER `ticketId`;

UPDATE m_prj_ticket_relation SET rel=type WHERE id > 0;

UPDATE m_prj_ticket_relation SET type='Project-Version' WHERE rel IN ('AffVersion', 'FixVersion') AND id > 0;
UPDATE m_prj_ticket_relation SET type='Project-Component' WHERE rel IN ('Component') AND id > 0;

ALTER TABLE `m_prj_ticket_relation`
CHANGE COLUMN `rel` `rel` VARCHAR(45) NOT NULL ;


ALTER TABLE `m_prj_version` DROP COLUMN `prjKey`;
ALTER TABLE `m_prj_message` DROP COLUMN `prjKey`;
ALTER TABLE `m_prj_component` DROP COLUMN `prjKey`;