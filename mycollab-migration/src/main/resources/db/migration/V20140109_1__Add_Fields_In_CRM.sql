ALTER TABLE `m_crm_contacts_opportunities` ADD COLUMN `decisionRole` VARCHAR(45) NULL;
ALTER TABLE `m_crm_accounts_leads` ADD COLUMN `isConvertRel` BIT(1) NULL;
