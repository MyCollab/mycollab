ALTER TABLE `s_user` 
CHANGE COLUMN `password` `password` VARCHAR(100) CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ;

ALTER TABLE `m_prj_task_list` 
ADD COLUMN `key` INT(11) NULL;

ALTER TABLE `m_prj_risk` 
ADD COLUMN `key` INT(11) NULL;

ALTER TABLE `m_prj_problem` 
ADD COLUMN `key` INT(11) NULL ;

ALTER TABLE `m_prj_message` 
ADD COLUMN `key` INT(11) NULL;

ALTER TABLE `m_tracker_component` 
ADD COLUMN `key` INT(11) NULL;

ALTER TABLE `m_tracker_version` 
ADD COLUMN `key` INT(11) NULL;

ALTER TABLE `m_prj_milestone` 
ADD COLUMN `key` INT(11) NULL;
