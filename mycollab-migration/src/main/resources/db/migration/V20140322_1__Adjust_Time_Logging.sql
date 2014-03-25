ALTER TABLE `m_prj_time_logging` 
CHANGE COLUMN `type` `type` VARCHAR(45) CHARACTER SET 'utf8mb4' NULL ,
CHANGE COLUMN `typeid` `typeid` INT(11) NULL ;

ALTER TABLE `m_prj_time_logging` 
ADD COLUMN `createdUser` VARCHAR(45) NULL;