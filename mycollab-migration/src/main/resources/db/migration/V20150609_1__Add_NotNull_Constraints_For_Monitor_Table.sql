ALTER TABLE `m_monitor_item`
DROP FOREIGN KEY `FK_m_monitor_item_1`;
ALTER TABLE `m_monitor_item`
CHANGE COLUMN `user` `user` VARCHAR(45) COLLATE utf8mb4_unicode_ci NOT NULL ;
ALTER TABLE `m_monitor_item`
ADD CONSTRAINT `FK_m_monitor_item_1`
  FOREIGN KEY (`user`)
  REFERENCES `s_user` (`username`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;