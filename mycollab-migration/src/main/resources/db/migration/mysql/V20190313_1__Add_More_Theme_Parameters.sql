ALTER TABLE `s_account_theme`
ADD COLUMN `actionBtnBorder` VARCHAR(6) NULL,
ADD COLUMN `optionBtnBorder` VARCHAR(6) NULL,
ADD COLUMN `dangerBtnBorder` VARCHAR(6) NULL;

UPDATE s_account_theme SET actionBtnBorder='1F9DFE',  optionBtn='FFFFFF', optionBtnText='1F9DFE', optionBtnBorder='CCCCCC', dangerBtnBorder='D32F2F' WHERE id > 0;