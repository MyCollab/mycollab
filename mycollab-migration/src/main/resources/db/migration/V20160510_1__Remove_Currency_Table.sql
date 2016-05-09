UPDATE `m_crm_campaign` SET currencyId=NULL WHERE id > 0;
ALTER TABLE `m_crm_campaign` DROP FOREIGN KEY `FK_m_crm_campaign_2`;
ALTER TABLE `m_crm_campaign` CHANGE COLUMN `currencyId` `currencyId` VARCHAR(4) NULL DEFAULT NULL ;

UPDATE `m_crm_contract` SET currencyid=NULL WHERE id > 0;
ALTER TABLE `m_crm_contract` DROP FOREIGN KEY `FK_m_crm_contract_4`;
ALTER TABLE `m_crm_contract` CHANGE COLUMN `currencyid` `currencyid` VARCHAR(4) NULL DEFAULT NULL ;

UPDATE `m_crm_opportunity` SET currencyid=NULL WHERE id > 0;
ALTER TABLE `m_crm_opportunity` DROP FOREIGN KEY `FK_m_crm_opportunity_8`;
ALTER TABLE `m_crm_opportunity` CHANGE COLUMN `currencyid` `currencyid` VARCHAR(4) NULL DEFAULT NULL ;

UPDATE `m_crm_product_catalog` SET currencyid=NULL WHERE id > 0;
ALTER TABLE `m_crm_product_catalog` DROP FOREIGN KEY `FK_m_crm_product_catalog_1`;
ALTER TABLE `m_crm_product_catalog` CHANGE COLUMN `currencyid` `currencyid` VARCHAR(4) NULL DEFAULT NULL ;

UPDATE `m_prj_project` SET currencyid=NULL WHERE id > 0;
ALTER TABLE `m_prj_project` DROP FOREIGN KEY `FK_m_prj_project_3`;
ALTER TABLE `m_prj_project` CHANGE COLUMN `currencyid` `currencyid` VARCHAR(4) NULL DEFAULT NULL ;

UPDATE `m_prj_invoice` SET currentId=NULL WHERE id > 0;
ALTER TABLE `m_prj_invoice` DROP FOREIGN KEY `FK_m_prj_invoice_1`;
ALTER TABLE `m_prj_invoice` CHANGE COLUMN `currentId` `currentId` VARCHAR(4) NULL DEFAULT NULL ;

DROP TABLE `s_currency`;


