ALTER TABLE `s_relay_email_notification`
CHANGE COLUMN `typeid` `typeid` TEXT CHARACTER SET 'utf8mb4' NOT NULL ,
ADD INDEX `FK_s_relay_email_notification_1_idx` (`sAccountId` ASC),
ADD INDEX `FK_s_relay_email_notification_2` (`typeid`(100) ASC),
ADD INDEX `FK_s_relay_email_notification_3` (`type` ASC);
ALTER TABLE `s_relay_email_notification`
ADD CONSTRAINT `FK_s_relay_email_notification_1`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;