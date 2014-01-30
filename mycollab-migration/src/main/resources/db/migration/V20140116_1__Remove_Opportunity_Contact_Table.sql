DROP TABLE `m_crm_opportunities_contacts`;

CREATE TABLE `m_crm_contacts_leads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contactId` int(10) unsigned NOT NULL,
  `leadId` int(10) unsigned NOT NULL,
  `isConvertRel` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_contacts_leads_1_idx` (`contactId`),
  KEY `FK_m_crm_contacts_leads_2_idx` (`leadId`),
  CONSTRAINT `FK_m_crm_contacts_leads_1` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contacts_leads_2` FOREIGN KEY (`leadId`) REFERENCES `m_crm_lead` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;