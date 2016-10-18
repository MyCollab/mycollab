ALTER TABLE `m_prj_risk`
CHANGE COLUMN `priority` `priority` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL ;

ALTER TABLE `m_prj_task`
CHANGE COLUMN `priority` `priority` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL ;

ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `priority` `priority` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL ;

ALTER TABLE `s_roles` ADD COLUMN `isDefault` BIT(1) NULL;