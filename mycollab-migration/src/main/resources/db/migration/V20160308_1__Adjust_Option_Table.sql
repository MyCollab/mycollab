ALTER TABLE `m_options`
ADD COLUMN `fieldgroup` VARCHAR(45) NULL;
UPDATE `m_options` SET fieldgroup='status' WHERE id > 0;
ALTER TABLE `m_options`
CHANGE COLUMN `fieldgroup` `fieldgroup` VARCHAR(45) NOT NULL ;