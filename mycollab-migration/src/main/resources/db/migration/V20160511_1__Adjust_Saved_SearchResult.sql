ALTER TABLE `s_save_search_result`
CHANGE COLUMN `queryName` `queryName` VARCHAR(400) CHARACTER SET 'utf8mb4' NOT NULL ,
ADD COLUMN `isShared` TINYINT(1) NULL;