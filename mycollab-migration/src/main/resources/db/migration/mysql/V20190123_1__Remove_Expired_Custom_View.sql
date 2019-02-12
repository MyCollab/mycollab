DELETE FROM `s_table_customize_view` WHERE `id` > 0;

ALTER TABLE `m_prj_customize_view` DROP COLUMN `displayFile`;

ALTER TABLE `s_user`
CHANGE COLUMN `email` `email` VARCHAR(255) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL ;

DROP TABLE `m_notification_item`;
DROP TABLE `s_timeline_tracking`;
DROP TABLE `s_timeline_tracking_cache`;
