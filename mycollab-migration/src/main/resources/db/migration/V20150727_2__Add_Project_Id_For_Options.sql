ALTER TABLE `m_options` ADD COLUMN `extraId` INT(11) NULL;
ALTER TABLE `m_options` ADD COLUMN `isDefault` TINYINT(1) NOT NULL;
ALTER TABLE `m_options` ADD COLUMN `refOption` INT(11) NULL;
ALTER TABLE `m_options` ADD COLUMN `color` VARCHAR(6) NULL;
ALTER TABLE `m_options` CHANGE COLUMN `color` `color` VARCHAR(6) NOT NULL;
