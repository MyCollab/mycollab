ALTER TABLE `s_account`
DROP COLUMN `pricingEffectTo`,
DROP COLUMN `pricingEffectFrom`,
DROP COLUMN `pricing`,
DROP COLUMN `paymentMethod`;

UPDATE s_account_theme SET topMenuBg='ADADAD', vTabsheetBgSelected='B3B3B3', actionBtn='0083CE', optionBtn='A3A3A3', dangerBtn='C9510C' WHERE isDefault=1 AND id > 0