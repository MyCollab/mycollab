ALTER TABLE `s_activitystream`
CHANGE COLUMN `typeId` `typeId` TEXT CHARACTER SET 'utf8mb4' NOT NULL ,
DROP INDEX `FK_m_crm_activitystream_5` ;
