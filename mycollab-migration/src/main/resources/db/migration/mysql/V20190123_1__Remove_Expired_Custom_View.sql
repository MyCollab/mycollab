DELETE FROM `s_table_customize_view` WHERE `id` > 0;

ALTER TABLE `m_prj_customize_view` DROP COLUMN `displayFile`;

ALTER TABLE `s_user`
CHANGE COLUMN `email` `email` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL ;

DROP TABLE `m_notification_item`;

ALTER TABLE `s_account_theme`
ADD COLUMN `actionBtnBorder` VARCHAR(6) NULL,
ADD COLUMN `optionBtnBorder` VARCHAR(6) NULL,
ADD COLUMN `dangerBtnBorder` VARCHAR(6) NULL;
