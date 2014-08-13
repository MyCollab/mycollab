ALTER TABLE `m_comment` 
CHANGE COLUMN `typeId` `typeId` VARCHAR(45) NULL DEFAULT NULL ;
ALTER TABLE `s_relay_email_notification` 
CHANGE COLUMN `typeid` `typeid` VARCHAR(45) NOT NULL ;