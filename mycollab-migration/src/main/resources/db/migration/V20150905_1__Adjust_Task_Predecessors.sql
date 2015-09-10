ALTER TABLE `m_prj_predecessor`
CHANGE COLUMN `type` `sourceType` VARCHAR(45) NOT NULL ,
ADD COLUMN `descType` VARCHAR(45) NULL;

UPDATE `m_prj_predecessor` SET `descType`='Project-Task' WHERE `id` > 0;

ALTER TABLE `m_prj_predecessor`
CHANGE COLUMN `descType` `descType` VARCHAR(45) NOT NULL ;