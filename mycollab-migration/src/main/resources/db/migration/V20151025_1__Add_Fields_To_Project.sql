ALTER TABLE `m_prj_project`
ADD COLUMN `ispublic` TINYINT(1) NULL,
ADD COLUMN `istemplate` TINYINT(1) NULL;

CREATE TABLE `s_timeline_tracking` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `typeid` INT(11) UNSIGNED NOT NULL,
  `fieldval` VARCHAR(45) NOT NULL,
  `fieldgroup` VARCHAR(45) NOT NULL,
  `extratypeid` INT(11) UNSIGNED NULL,
  `sAccountId` INT(11) NOT NULL,
  `forDay` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_s_timeline_tracking_1_idx` (`sAccountId` ASC),
  CONSTRAINT `FK_s_timeline_tracking_1`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

ALTER TABLE `m_prj_standup` CHANGE COLUMN `forday` `forday` DATE NOT NULL ;
