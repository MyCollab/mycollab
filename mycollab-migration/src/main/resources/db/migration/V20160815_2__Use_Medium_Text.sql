ALTER TABLE `m_audit_log`
CHANGE COLUMN `changeset` `changeset` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NOT NULL ;
ALTER TABLE `m_prj_task`
CHANGE COLUMN `notes` `notes` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `detail` `detail` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `environment` `environment` VARCHAR(4000) CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_tracker_component`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_tracker_version`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_invoice`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_time_logging`
CHANGE COLUMN `note` `note` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_message`
CHANGE COLUMN `message` `message` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_milestone`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_project`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;
ALTER TABLE `m_prj_risk`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_opportunity`
CHANGE COLUMN `amount` `amount` DOUBLE UNSIGNED NULL DEFAULT NULL ;







