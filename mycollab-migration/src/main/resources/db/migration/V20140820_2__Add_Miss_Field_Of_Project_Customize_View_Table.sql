ALTER TABLE `m_prj_customize_view` 
ADD COLUMN `displayFile` BIT(1) NOT NULL;

ALTER TABLE `m_prj_customize_view` 
CHANGE COLUMN `id` `id` INT(10) NOT NULL AUTO_INCREMENT ;