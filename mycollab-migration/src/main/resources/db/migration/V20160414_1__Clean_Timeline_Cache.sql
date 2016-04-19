DELETE FROM `s_timeline_tracking_cache` WHERE id > 0;
ALTER TABLE `m_options` ADD COLUMN `isShow` BIT(1) NULL;
UPDATE `m_tracker_bug` SET `status` = 'ReOpen' WHERE `status` = 'ReOpened' AND id > 0;