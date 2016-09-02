ALTER TABLE `m_crm_campaign`
CHANGE COLUMN `impressionnote` `impressionnote` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `budget` `budget` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `actualCost` `actualCost` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `expectedRevenue` `expectedRevenue` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `expectedCost` `expectedCost` DOUBLE NULL DEFAULT NULL ,
CHANGE COLUMN `objective` `objective` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_opportunity`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_account`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_call`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_case`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ,
CHANGE COLUMN `resolution` `resolution` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_contact`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_lead`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL ;

ALTER TABLE `m_crm_meeting`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT
NULL ;

ALTER TABLE `m_crm_task`
CHANGE COLUMN `description` `description` MEDIUMTEXT CHARACTER SET 'utf8mb4' COLLATE utf8mb4_unicode_ci NULL DEFAULT
NULL ;








