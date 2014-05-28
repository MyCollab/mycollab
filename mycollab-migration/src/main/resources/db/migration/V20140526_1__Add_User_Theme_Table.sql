CREATE TABLE `s_account_theme` (
  `id` INT NOT NULL AUTO_INCREMENT,
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
  `hTopMenuBg` VARCHAR(6) NULL,
  `hTopMenuBgSelected` VARCHAR(6) NULL,
  `hTopMenuText` VARCHAR(6) NULL,
  `hTopMenuTextSelected` VARCHAR(6) NULL,
  `actionBtn` VARCHAR(6) NULL,
  `actionBtnText` VARCHAR(6) NULL,
  `optionBtn` VARCHAR(6) NULL,
  `optionBtnText` VARCHAR(6) NULL,
  `clearBtn` VARCHAR(6) NULL,
  `clearBtnText` VARCHAR(6) NULL,
  `controlBtn` VARCHAR(6) NULL,
  `controlBtnText` VARCHAR(6) NULL,
  `dangerBtn` VARCHAR(6) NULL,
  `dangerBtnText` VARCHAR(6) NULL,
  `toggleBtn` VARCHAR(6) NULL,
  `toggleBtnSelected` VARCHAR(6) NULL,
  `toggleBtnText` VARCHAR(6) NULL,
  `toggleBtnTextSelected` VARCHAR(6) NULL,
  `isDefault` BIT(1) DEFAULT 0,
  CONSTRAINT `PK_s_account_theme_1` PRIMARY KEY (`id`));
ALTER TABLE `s_account_settings` 
ADD COLUMN `defaultThemeId` INT(11) NULL AFTER `defaultTimezone`,
ADD INDEX `FK_s_account_settings_2_idx` (`defaultThemeId` ASC);
ALTER TABLE `s_account_settings` 
ADD CONSTRAINT `FK_s_account_settings_2`
  FOREIGN KEY (`defaultThemeId`)
  REFERENCES `s_account_theme` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;