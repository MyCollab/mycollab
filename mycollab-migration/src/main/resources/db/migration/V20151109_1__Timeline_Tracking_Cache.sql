CREATE TABLE `s_timeline_tracking_cache` (
  `id` INT(11) UNSIGNED NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `fieldval` VARCHAR(45) NULL,
  `extratypeid` INT(11) UNSIGNED NULL,
  `sAccountId` INT(11) NOT NULL,
  `forDay` DATE NULL,
  `fieldgroup` VARCHAR(45) NOT NULL,
  `count` INT(6) NOT NULL,
  PRIMARY KEY (`id`));