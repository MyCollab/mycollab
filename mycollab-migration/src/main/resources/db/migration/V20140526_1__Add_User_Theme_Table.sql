CREATE TABLE `s_account_theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sAccountId` INT(11) NOT NULL,
  `logoPath` VARCHAR(255) NULL,
  `topMenuBg` VARCHAR(6) NULL,
  `topMenuBgSelected` VARCHAR(6) NULL,
  `topMenuText` VARCHAR(6) NULL,
  `topMenuTextSelected` VARCHAR(6) NULL,
  `tabsheetBg` VARCHAR(6) NULL,
  `tabsheetBgSelected` VARCHAR(6) NULL,
  `tabsheetText` VARCHAR(6) NULL,
  `tabsheetTextSelected` VARCHAR(6) NULL,
  `vTabsheetBg` VARCHAR(6) NULL,
  `vTabsheetBgSelected` VARCHAR(6) NULL,
  `vTabsheetText` VARCHAR(6) NULL,
  `vTabsheetTextSelected` VARCHAR(6) NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_s_account_theme_1` (`sAccountId` ASC),
  CONSTRAINT `FK_s_account_theme_1`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
ALTER TABLE `s_account_settings` 
ADD COLUMN `defaultThemeId` INT(11) NULL AFTER `defaultTimezone`,
ADD INDEX `FK_s_account_settings_2_idx` (`defaultThemeId` ASC);
ALTER TABLE `s_account_settings` 
ADD CONSTRAINT `FK_s_account_settings_2`
  FOREIGN KEY (`defaultThemeId`)
  REFERENCES `s_account_theme` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;