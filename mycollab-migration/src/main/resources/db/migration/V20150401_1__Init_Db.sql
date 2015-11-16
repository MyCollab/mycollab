/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `m_audit_log`
--

DROP TABLE IF EXISTS `m_audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_audit_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `object_class` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `changeset` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `posteddate` datetime NOT NULL,
  `posteduser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` int(11) NOT NULL,
  `module` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `activityLogId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_audit_log_2` (`sAccountId`),
  KEY `FK_m_audit_log_1` (`posteduser`),
  KEY `FK_m_audit_log_3_idx` (`activityLogId`),
  KEY `INDEX_m_audit_log_4` (`type`) USING BTREE,
  KEY `INDEX_m_audit_log_5` (`typeid`) USING BTREE,
  KEY `INDEX_m_audit_log_6` (`module`) USING BTREE,
  CONSTRAINT `FK_m_audit_log_1` FOREIGN KEY (`posteduser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_audit_log_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_audit_log_3` FOREIGN KEY (`activityLogId`) REFERENCES `s_activitystream` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_audit_log`
--

LOCK TABLES `m_audit_log` WRITE;
/*!40000 ALTER TABLE `m_audit_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_comment`
--

DROP TABLE IF EXISTS `m_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `comment` text CHARACTER SET utf8mb4,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeId` text CHARACTER SET utf8mb4,
  `sAccountId` int(11) DEFAULT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_comment_1` (`sAccountId`),
  KEY `INDEX_m_comment_2` (`extraTypeId`) USING BTREE,
  KEY `INDEX_m_comment_4` (`type`) USING BTREE,
  KEY `FK_m_comment_2_idx` (`createdUser`),
  KEY `INDEX_m_comment_3` (`typeId`(100)),
  CONSTRAINT `FK_m_comment_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_comment_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_comment`
--

LOCK TABLES `m_comment` WRITE;
/*!40000 ALTER TABLE `m_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_account`
--

DROP TABLE IF EXISTS `m_crm_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_account` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phoneOffice` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fax` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `alternatePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `annualRevenue` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `billingAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `postalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `state` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ownership` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingCity` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingPostalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingState` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `numemployees` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `billingCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_account_7` (`sAccountId`),
  KEY `FK_m_crm_account_6` (`createdUser`),
  KEY `FK_m_crm_account_8` (`assignUser`),
  CONSTRAINT `FK_m_crm_account_6` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_account_7` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_account_8` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_account`
--

LOCK TABLES `m_crm_account` WRITE;
/*!40000 ALTER TABLE `m_crm_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_accounts_leads`
--

DROP TABLE IF EXISTS `m_crm_accounts_leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_accounts_leads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `accountId` int(10) unsigned NOT NULL,
  `leadId` int(10) unsigned NOT NULL,
  `createTime` datetime NOT NULL,
  `isConvertRel` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_accounts_leads_1` (`accountId`),
  KEY `FK_m_crm_accounts_leads_2` (`leadId`),
  CONSTRAINT `FK_m_crm_accounts_leads_1` FOREIGN KEY (`accountId`) REFERENCES `m_crm_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_accounts_leads_2` FOREIGN KEY (`leadId`) REFERENCES `m_crm_lead` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_accounts_leads`
--

LOCK TABLES `m_crm_accounts_leads` WRITE;
/*!40000 ALTER TABLE `m_crm_accounts_leads` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_accounts_leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_call`
--

DROP TABLE IF EXISTS `m_crm_call`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_call` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `startDate` datetime DEFAULT NULL,
  `durationInSeconds` int(11) DEFAULT NULL,
  `calltype` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `contactId` int(10) unsigned DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `purpose` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `result` text COLLATE utf8mb4_unicode_ci,
  `isClosed` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_call_2` (`contactId`),
  KEY `FK_m_crm_call_1` (`sAccountId`),
  KEY `FK_m_crm_call_3` (`createdUser`),
  KEY `FK_m_crm_call_4` (`assignUser`),
  CONSTRAINT `FK_m_crm_call_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_call_2` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_call_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_call_4` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_call`
--

LOCK TABLES `m_crm_call` WRITE;
/*!40000 ALTER TABLE `m_crm_call` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_call` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_campaign`
--

DROP TABLE IF EXISTS `m_crm_campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_campaign` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `campaignName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `currencyId` int(11) DEFAULT NULL,
  `impressionnote` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `budget` decimal(10,0) DEFAULT NULL,
  `actualCost` decimal(10,0) DEFAULT NULL,
  `expectedRevenue` decimal(10,0) DEFAULT NULL,
  `expectedCost` decimal(10,0) DEFAULT NULL,
  `objective` text COLLATE utf8mb4_unicode_ci,
  `description` text COLLATE utf8mb4_unicode_ci,
  `impression` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_campaign_6` (`sAccountId`),
  KEY `FK_m_crm_campaign_2` (`currencyId`),
  KEY `FK_m_crm_campaign_5` (`createdUser`),
  KEY `FK_m_crm_campaign_7` (`assignUser`),
  CONSTRAINT `FK_m_crm_campaign_2` FOREIGN KEY (`currencyId`) REFERENCES `s_currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaign_5` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaign_6` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaign_7` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_campaign`
--

LOCK TABLES `m_crm_campaign` WRITE;
/*!40000 ALTER TABLE `m_crm_campaign` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_campaign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_campaigns_accounts`
--

DROP TABLE IF EXISTS `m_crm_campaigns_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_campaigns_accounts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `campaignId` int(10) unsigned NOT NULL,
  `accountId` int(10) unsigned NOT NULL,
  `createdTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_campaigns_accounts_2` (`accountId`),
  KEY `FK_m_crm_campaigns_accounts_1` (`campaignId`),
  CONSTRAINT `FK_m_crm_campaigns_accounts_1` FOREIGN KEY (`campaignId`) REFERENCES `m_crm_campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaigns_accounts_2` FOREIGN KEY (`accountId`) REFERENCES `m_crm_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_campaigns_accounts`
--

LOCK TABLES `m_crm_campaigns_accounts` WRITE;
/*!40000 ALTER TABLE `m_crm_campaigns_accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_campaigns_accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_campaigns_contacts`
--

DROP TABLE IF EXISTS `m_crm_campaigns_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_campaigns_contacts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `campaignId` int(10) unsigned NOT NULL,
  `contactId` int(10) unsigned NOT NULL,
  `createdTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_campaigns_contacts_1` (`campaignId`),
  KEY `FK_m_crm_campaigns_contacts_2` (`contactId`),
  CONSTRAINT `FK_m_crm_campaigns_contacts_1` FOREIGN KEY (`campaignId`) REFERENCES `m_crm_campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaigns_contacts_2` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_campaigns_contacts`
--

LOCK TABLES `m_crm_campaigns_contacts` WRITE;
/*!40000 ALTER TABLE `m_crm_campaigns_contacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_campaigns_contacts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_campaigns_leads`
--

DROP TABLE IF EXISTS `m_crm_campaigns_leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_campaigns_leads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `campaignId` int(10) unsigned NOT NULL,
  `leadId` int(10) unsigned NOT NULL,
  `createdTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_campaigns_leads_1` (`campaignId`),
  KEY `FK_m_crm_campaigns_leads_2` (`leadId`),
  CONSTRAINT `FK_m_crm_campaigns_leads_1` FOREIGN KEY (`campaignId`) REFERENCES `m_crm_campaign` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_campaigns_leads_2` FOREIGN KEY (`leadId`) REFERENCES `m_crm_lead` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_campaigns_leads`
--

LOCK TABLES `m_crm_campaigns_leads` WRITE;
/*!40000 ALTER TABLE `m_crm_campaigns_leads` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_campaigns_leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_case`
--

DROP TABLE IF EXISTS `m_crm_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_case` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `subject` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `resolution` text COLLATE utf8mb4_unicode_ci,
  `accountId` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `reason` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `origin` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phonenumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_case_1` (`accountId`),
  KEY `FK_m_crm_case_3` (`sAccountId`),
  KEY `FK_m_crm_case_2` (`createdUser`),
  KEY `FK_m_crm_case_4` (`assignUser`),
  CONSTRAINT `FK_m_crm_case_1` FOREIGN KEY (`accountId`) REFERENCES `m_crm_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_case_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_case_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_case_4` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_case`
--

LOCK TABLES `m_crm_case` WRITE;
/*!40000 ALTER TABLE `m_crm_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_contact`
--

DROP TABLE IF EXISTS `m_crm_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_contact` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `prefix` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `leadSource` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `campaignId` int(10) unsigned DEFAULT NULL,
  `isCallable` bit(1) DEFAULT NULL,
  `officePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `homePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fax` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `assistant` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primCity` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primState` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primPostalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assistantPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `department` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherCity` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherState` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherPostalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `accountId` int(10) unsigned DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_contact_3` (`campaignId`),
  KEY `FK_m_crm_contact_7` (`sAccountId`),
  KEY `FK_m_crm_contact_6` (`createdUser`),
  KEY `FK_m_crm_contact_8` (`assignUser`),
  KEY `FK_m_crm_contact_9_idx` (`accountId`),
  CONSTRAINT `FK_m_crm_contact_3` FOREIGN KEY (`campaignId`) REFERENCES `m_crm_campaign` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contact_6` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contact_7` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contact_8` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contact_9` FOREIGN KEY (`accountId`) REFERENCES `m_crm_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_contact`
--

LOCK TABLES `m_crm_contact` WRITE;
/*!40000 ALTER TABLE `m_crm_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_contacts_cases`
--

DROP TABLE IF EXISTS `m_crm_contacts_cases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_contacts_cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contactId` int(10) unsigned NOT NULL,
  `caseId` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_contacts_cases_2` (`caseId`),
  KEY `FK_m_crm_contacts_cases_1` (`contactId`),
  CONSTRAINT `FK_m_crm_contacts_cases_1` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contacts_cases_2` FOREIGN KEY (`caseId`) REFERENCES `m_crm_case` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_contacts_cases`
--

LOCK TABLES `m_crm_contacts_cases` WRITE;
/*!40000 ALTER TABLE `m_crm_contacts_cases` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_contacts_cases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_contacts_leads`
--

DROP TABLE IF EXISTS `m_crm_contacts_leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_contacts_leads`
--

LOCK TABLES `m_crm_contacts_leads` WRITE;
/*!40000 ALTER TABLE `m_crm_contacts_leads` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_contacts_leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_contacts_opportunities`
--

DROP TABLE IF EXISTS `m_crm_contacts_opportunities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_contacts_opportunities` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contactId` int(10) unsigned NOT NULL,
  `opportunityId` int(10) unsigned NOT NULL,
  `createdTime` datetime NOT NULL,
  `decisionRole` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_contacts_opportunities_1` (`contactId`),
  KEY `FK_m_crm_contacts_opportunities_2` (`opportunityId`),
  CONSTRAINT `FK_m_crm_contacts_opportunities_1` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contacts_opportunities_2` FOREIGN KEY (`opportunityId`) REFERENCES `m_crm_opportunity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_contacts_opportunities`
--

LOCK TABLES `m_crm_contacts_opportunities` WRITE;
/*!40000 ALTER TABLE `m_crm_contacts_opportunities` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_contacts_opportunities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_contract`
--

DROP TABLE IF EXISTS `m_crm_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_contract` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contractname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `code` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accountid` int(10) unsigned DEFAULT NULL,
  `opportunityid` int(10) unsigned DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `customersigneddate` datetime DEFAULT NULL,
  `companysigneddate` datetime DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `contractvalue` decimal(10,0) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_contract_1` (`accountid`),
  KEY `FK_m_crm_contract_2` (`opportunityid`),
  KEY `FK_m_crm_contract_6` (`sAccountId`),
  KEY `FK_m_crm_contract_4` (`currencyid`),
  KEY `FK_m_crm_contract_5` (`createdUser`),
  KEY `FK_m_crm_contract_7` (`assignUser`),
  CONSTRAINT `FK_m_crm_contract_1` FOREIGN KEY (`accountid`) REFERENCES `m_crm_account` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contract_2` FOREIGN KEY (`opportunityid`) REFERENCES `m_crm_opportunity` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contract_4` FOREIGN KEY (`currencyid`) REFERENCES `s_currency` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contract_5` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contract_6` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_contract_7` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_contract`
--

LOCK TABLES `m_crm_contract` WRITE;
/*!40000 ALTER TABLE `m_crm_contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_customer`
--

DROP TABLE IF EXISTS `m_crm_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_customer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `leadsource` int(10) unsigned DEFAULT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `department` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `officePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `homePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assisstant` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assisstantPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `title` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_crm_customer_4` (`sAccountId`),
  KEY `FK_crm_customer_3` (`createdUser`),
  CONSTRAINT `FK_crm_customer_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_crm_customer_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_customer`
--

LOCK TABLES `m_crm_customer` WRITE;
/*!40000 ALTER TABLE `m_crm_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_lead`
--

DROP TABLE IF EXISTS `m_crm_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_lead` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `campaignId` int(10) unsigned DEFAULT NULL,
  `leadSourceDesc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `statusDesc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `referredBy` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prefixName` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `accountName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `department` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isCallable` bit(1) DEFAULT NULL,
  `officePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `homePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fax` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primState` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primCity` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primPostalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherState` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherCity` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherPostalCode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherCountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `industry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `noEmployees` int(11) DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_lead_2` (`campaignId`),
  KEY `FK_m_crm_lead_6` (`sAccountId`),
  KEY `FK_m_crm_lead_5` (`createdUser`),
  KEY `FK_m_crm_lead_7` (`assignUser`),
  CONSTRAINT `FK_m_crm_lead_2` FOREIGN KEY (`campaignId`) REFERENCES `m_crm_campaign` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_lead_5` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_lead_6` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_lead_7` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_lead`
--

LOCK TABLES `m_crm_lead` WRITE;
/*!40000 ALTER TABLE `m_crm_lead` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_meeting`
--

DROP TABLE IF EXISTS `m_crm_meeting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_meeting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeid` int(10) unsigned DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `isRecurrence` bit(1) DEFAULT NULL,
  `recurrenceType` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `recurrenceStartDate` datetime DEFAULT NULL,
  `recurrenceEndDate` datetime DEFAULT NULL,
  `contactType` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contactTypeId` int(10) unsigned DEFAULT NULL,
  `isClosed` bit(1) DEFAULT NULL,
  `isNotified` bit(1) DEFAULT NULL,
  `isNotifiedPrior` int(11) DEFAULT NULL,
  `recurrenceInfo` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_meeting_2` (`sAccountId`),
  KEY `FK_m_crm_meeting_1` (`createdUser`),
  CONSTRAINT `FK_m_crm_meeting_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_meeting_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_meeting`
--

LOCK TABLES `m_crm_meeting` WRITE;
/*!40000 ALTER TABLE `m_crm_meeting` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_meeting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_meeting_invitees`
--

DROP TABLE IF EXISTS `m_crm_meeting_invitees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_meeting_invitees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meetingId` int(11) NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isModerator` bit(1) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_meeting_invitees_1_idx` (`meetingId`),
  CONSTRAINT `FK_m_crm_meeting_invitees_1` FOREIGN KEY (`meetingId`) REFERENCES `m_crm_meeting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_meeting_invitees`
--

LOCK TABLES `m_crm_meeting_invitees` WRITE;
/*!40000 ALTER TABLE `m_crm_meeting_invitees` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_meeting_invitees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_note`
--

DROP TABLE IF EXISTS `m_crm_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_note` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeid` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_note_4` (`sAccountId`),
  KEY `FK_m_crm_note_3` (`createdUser`),
  CONSTRAINT `FK_m_crm_note_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_note_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_note`
--

LOCK TABLES `m_crm_note` WRITE;
/*!40000 ALTER TABLE `m_crm_note` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_notifications`
--

DROP TABLE IF EXISTS `m_crm_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `level` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_notifications_1_idx` (`username`),
  KEY `FK_m_crm_notifications_2_idx` (`sAccountId`),
  CONSTRAINT `FK_m_crm_notifications_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_notifications_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_notifications`
--

LOCK TABLES `m_crm_notifications` WRITE;
/*!40000 ALTER TABLE `m_crm_notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_opportunities_leads`
--

DROP TABLE IF EXISTS `m_crm_opportunities_leads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_opportunities_leads` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `opportunityId` int(10) unsigned NOT NULL,
  `leadId` int(10) unsigned NOT NULL,
  `createdTime` datetime NOT NULL,
  `isConvertRel` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_opportunities_leads_2` (`leadId`),
  KEY `FK_m_crm_opportunities_leads_1` (`opportunityId`),
  CONSTRAINT `FK_m_crm_opportunities_leads_1` FOREIGN KEY (`opportunityId`) REFERENCES `m_crm_opportunity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_opportunities_leads_2` FOREIGN KEY (`leadId`) REFERENCES `m_crm_lead` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_opportunities_leads`
--

LOCK TABLES `m_crm_opportunities_leads` WRITE;
/*!40000 ALTER TABLE `m_crm_opportunities_leads` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_opportunities_leads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_opportunity`
--

DROP TABLE IF EXISTS `m_crm_opportunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_opportunity` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `opportunityName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `accountid` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `expectedClosedDate` datetime DEFAULT NULL,
  `campaignid` int(10) unsigned DEFAULT NULL,
  `nextStep` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `probability` int(10) unsigned DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `opportunityType` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `salesStage` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_opportunity_2` (`campaignid`),
  KEY `FK_m_crm_opportunity_6` (`sAccountId`),
  KEY `FK_m_crm_opportunity_8` (`currencyid`),
  KEY `FK_m_crm_opportunity_5` (`createdUser`),
  KEY `FK_m_crm_opportunity_7` (`assignUser`),
  CONSTRAINT `FK_m_crm_opportunity_2` FOREIGN KEY (`campaignid`) REFERENCES `m_crm_campaign` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_opportunity_5` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_opportunity_6` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_opportunity_7` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_opportunity_8` FOREIGN KEY (`currencyid`) REFERENCES `s_currency` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_opportunity`
--

LOCK TABLES `m_crm_opportunity` WRITE;
/*!40000 ALTER TABLE `m_crm_opportunity` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_opportunity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_product`
--

DROP TABLE IF EXISTS `m_crm_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_product` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `productname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `accountid` int(10) unsigned DEFAULT NULL,
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quantity` int(10) unsigned DEFAULT NULL,
  `serialnumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assessnumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `contactid` int(10) unsigned DEFAULT NULL,
  `supportstartdate` datetime DEFAULT NULL,
  `supportenddate` datetime DEFAULT NULL,
  `salesdate` datetime DEFAULT NULL,
  `unitprice` double DEFAULT NULL,
  `description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `producturl` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supportcontact` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supportterm` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `supportdesc` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cost` double DEFAULT NULL,
  `listprice` double DEFAULT NULL,
  `groupid` int(10) unsigned DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `taxClass` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mftNumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vendorPartNumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_product_1` (`accountid`),
  KEY `FK_m_crm_product_2` (`contactid`),
  KEY `FK_m_crm_product_3` (`groupid`),
  KEY `FK_m_crm_product_5` (`sAccountId`),
  KEY `FK_m_crm_product_4` (`createdUser`),
  CONSTRAINT `FK_m_crm_product_1` FOREIGN KEY (`accountid`) REFERENCES `m_crm_account` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_product_2` FOREIGN KEY (`contactid`) REFERENCES `m_crm_contact` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_product_3` FOREIGN KEY (`groupid`) REFERENCES `m_crm_quote_group_product` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_product_4` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_product_5` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_product`
--

LOCK TABLES `m_crm_product` WRITE;
/*!40000 ALTER TABLE `m_crm_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_product_catalog`
--

DROP TABLE IF EXISTS `m_crm_product_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_product_catalog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `taxclass` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mft_partnumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vendor_partnumber` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `cost` double DEFAULT NULL,
  `listprice` double DEFAULT NULL,
  `discountprice` double DEFAULT NULL,
  `pricing_formula` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_available` datetime DEFAULT NULL,
  `availability` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quantity_in_stock` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `support_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `support_contact` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `support_desc` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `support_term` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `productname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_product_catalog_2` (`sAccountId`),
  KEY `FK_m_crm_product_catalog_1` (`currencyid`),
  CONSTRAINT `FK_m_crm_product_catalog_1` FOREIGN KEY (`currencyid`) REFERENCES `s_currency` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_product_catalog_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_product_catalog`
--

LOCK TABLES `m_crm_product_catalog` WRITE;
/*!40000 ALTER TABLE `m_crm_product_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_product_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_quote`
--

DROP TABLE IF EXISTS `m_crm_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_quote` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quotestage` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `opportunityid` int(10) unsigned DEFAULT NULL,
  `validuntil` datetime DEFAULT NULL,
  `shipping` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `billaccount` int(10) unsigned NOT NULL,
  `billcontact` int(10) unsigned DEFAULT NULL,
  `shipaccount` int(10) unsigned DEFAULT NULL,
  `shipcontact` int(10) unsigned DEFAULT NULL,
  `billingaddress` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `billingcity` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `billingstate` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `billingpostalcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `billingcountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingaddress` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shippingcity` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingstate` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingpostalcode` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shippingcountry` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `paymentterm` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `originalpodate` datetime DEFAULT NULL,
  `purchaseordernum` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_quote_4` (`shipaccount`),
  KEY `FK_m_crm_quote_6` (`billaccount`),
  KEY `FK_m_crm_quote_3` (`billcontact`),
  KEY `FK_m_crm_quote_5` (`shipcontact`),
  KEY `FK_m_crm_quote_2` (`opportunityid`),
  KEY `FK_m_crm_quote_8` (`sAccountId`),
  KEY `FK_m_crm_quote_7` (`createdUser`),
  KEY `FK_m_crm_quote_9` (`assignUser`),
  CONSTRAINT `FK_m_crm_quote_2` FOREIGN KEY (`opportunityid`) REFERENCES `m_crm_opportunity` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_3` FOREIGN KEY (`billcontact`) REFERENCES `m_crm_contact` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_4` FOREIGN KEY (`shipaccount`) REFERENCES `m_crm_account` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_5` FOREIGN KEY (`shipcontact`) REFERENCES `m_crm_contact` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_6` FOREIGN KEY (`billaccount`) REFERENCES `m_crm_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_7` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_m_crm_quote_8` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_9` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_quote`
--

LOCK TABLES `m_crm_quote` WRITE;
/*!40000 ALTER TABLE `m_crm_quote` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_quote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_quote_group_product`
--

DROP TABLE IF EXISTS `m_crm_quote_group_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_quote_group_product` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `groupname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `groupstate` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tax` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shipping` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quoteid` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_quote_group_product_2` (`quoteid`),
  CONSTRAINT `FK_m_crm_quote_group_product_1` FOREIGN KEY (`quoteid`) REFERENCES `m_crm_quote` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_quote_group_product_2` FOREIGN KEY (`quoteid`) REFERENCES `m_crm_quote` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_quote_group_product`
--

LOCK TABLES `m_crm_quote_group_product` WRITE;
/*!40000 ALTER TABLE `m_crm_quote_group_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_quote_group_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_target`
--

DROP TABLE IF EXISTS `m_crm_target`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_target` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `prefixname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `department` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `accountname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `isCallable` bit(1) DEFAULT NULL,
  `officePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mobile` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `homePhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otherPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fax` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assistant` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assistantPhone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primaryAddress` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primaryCity` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primaryState` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primaryPostal` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `primaryCountryId` int(11) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_target_4` (`sAccountId`),
  KEY `FK_m_crm_target_2` (`primaryCountryId`),
  KEY `FK_m_crm_target_3` (`createdUser`),
  KEY `FK_m_crm_target_5` (`assignUser`),
  CONSTRAINT `FK_m_crm_target_2` FOREIGN KEY (`primaryCountryId`) REFERENCES `s_country` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_target_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_m_crm_target_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_target_5` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_target`
--

LOCK TABLES `m_crm_target` WRITE;
/*!40000 ALTER TABLE `m_crm_target` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_target` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_target_list`
--

DROP TABLE IF EXISTS `m_crm_target_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_target_list` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_target_list_1` (`sAccountId`),
  KEY `FK_m_crm_target_list_2` (`createdUser`),
  KEY `FK_m_crm_target_list_3` (`assignUser`),
  CONSTRAINT `FK_m_crm_target_list_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_target_list_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_m_crm_target_list_3` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_target_list`
--

LOCK TABLES `m_crm_target_list` WRITE;
/*!40000 ALTER TABLE `m_crm_target_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_target_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_crm_task`
--

DROP TABLE IF EXISTS `m_crm_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_crm_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `startdate` datetime DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `contactId` int(10) unsigned DEFAULT NULL,
  `typeid` int(10) unsigned DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `isClosed` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_task_1` (`contactId`),
  KEY `FK_m_crm_task_5` (`sAccountId`),
  KEY `FK_m_crm_task_4` (`createdUser`),
  KEY `FK_m_crm_task_6` (`assignUser`),
  CONSTRAINT `FK_m_crm_task_1` FOREIGN KEY (`contactId`) REFERENCES `m_crm_contact` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_task_4` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_task_5` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_task_6` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_crm_task`
--

LOCK TABLES `m_crm_task` WRITE;
/*!40000 ALTER TABLE `m_crm_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_crm_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_ecm_activity_log`
--

DROP TABLE IF EXISTS `m_ecm_activity_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_ecm_activity_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `createdUserFullName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actionDesc` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `baseFolderPath` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_ecm_activity_log`
--

LOCK TABLES `m_ecm_activity_log` WRITE;
/*!40000 ALTER TABLE `m_ecm_activity_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_ecm_activity_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_ecm_driveinfo`
--

DROP TABLE IF EXISTS `m_ecm_driveinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_ecm_driveinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `usedVolume` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_ecm_driveinfo_1_idx` (`sAccountId`),
  CONSTRAINT `FK_m_ecm_driveinfo_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_ecm_driveinfo`
--

LOCK TABLES `m_ecm_driveinfo` WRITE;
/*!40000 ALTER TABLE `m_ecm_driveinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_ecm_driveinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_ecm_external_drive`
--

DROP TABLE IF EXISTS `m_ecm_external_drive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_ecm_external_drive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `storageName` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `accessToken` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `lastUpdatedTime` datetime NOT NULL,
  `folderName` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_ecm_external_drive`
--

LOCK TABLES `m_ecm_external_drive` WRITE;
/*!40000 ALTER TABLE `m_ecm_external_drive` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_ecm_external_drive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_form_custom_field_value`
--

DROP TABLE IF EXISTS `m_form_custom_field_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_form_custom_field_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` int(11) NOT NULL,
  `number1` double DEFAULT NULL,
  `number2` double DEFAULT NULL,
  `number3` double DEFAULT NULL,
  `number4` double DEFAULT NULL,
  `number5` double DEFAULT NULL,
  `int1` int(11) DEFAULT NULL,
  `int2` int(11) DEFAULT NULL,
  `int3` int(11) DEFAULT NULL,
  `int4` int(11) DEFAULT NULL,
  `int5` int(11) DEFAULT NULL,
  `date1` datetime DEFAULT NULL,
  `date2` datetime DEFAULT NULL,
  `date3` datetime DEFAULT NULL,
  `date4` datetime DEFAULT NULL,
  `date5` datetime DEFAULT NULL,
  `text1` text CHARACTER SET utf8mb4,
  `text2` text CHARACTER SET utf8mb4,
  `text3` text CHARACTER SET utf8mb4,
  `text4` text CHARACTER SET utf8mb4,
  `text5` text CHARACTER SET utf8mb4,
  `textarea1` text CHARACTER SET utf8mb4,
  `textarea2` text CHARACTER SET utf8mb4,
  `textarea3` text CHARACTER SET utf8mb4,
  `textarea4` text CHARACTER SET utf8mb4,
  `textarea5` text CHARACTER SET utf8mb4,
  `bool1` bit(1) DEFAULT NULL,
  `bool2` bit(1) DEFAULT NULL,
  `bool3` bit(1) DEFAULT NULL,
  `bool4` bit(1) DEFAULT NULL,
  `bool5` bit(1) DEFAULT NULL,
  `pick1` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pick2` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pick3` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pick4` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pick5` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `multipick1` text COLLATE utf8mb4_unicode_ci,
  `multipick2` text COLLATE utf8mb4_unicode_ci,
  `multipick3` text COLLATE utf8mb4_unicode_ci,
  `multipick4` text COLLATE utf8mb4_unicode_ci,
  `multipick5` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `INDEX_m_form_custom_field_value_1` (`module`) USING BTREE,
  KEY `INDEX_m_form_custom_field_value_2` (`typeid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_form_custom_field_value`
--

LOCK TABLES `m_form_custom_field_value` WRITE;
/*!40000 ALTER TABLE `m_form_custom_field_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_form_custom_field_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_form_section`
--

DROP TABLE IF EXISTS `m_form_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_form_section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `layoutIndex` int(11) NOT NULL,
  `module` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `layoutType` int(2) NOT NULL,
  `isDeleteSection` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_form_section_1_idx` (`sAccountId`),
  CONSTRAINT `FK_m_form_section_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_form_section`
--

LOCK TABLES `m_form_section` WRITE;
/*!40000 ALTER TABLE `m_form_section` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_form_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_form_section_field`
--

DROP TABLE IF EXISTS `m_form_section_field`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_form_section_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sectionId` int(11) NOT NULL,
  `isMandatory` bit(1) NOT NULL,
  `fieldIndex` int(11) NOT NULL,
  `displayName` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fieldFormat` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fieldname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fieldType` varchar(1000) CHARACTER SET utf8mb4 NOT NULL,
  `isRequired` bit(1) NOT NULL,
  `isCustom` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_form_section_field_2_idx` (`sectionId`),
  CONSTRAINT `FK_m_form_section_field_2` FOREIGN KEY (`sectionId`) REFERENCES `m_form_section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_form_section_field`
--

LOCK TABLES `m_form_section_field` WRITE;
/*!40000 ALTER TABLE `m_form_section_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_form_section_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_monitor_item`
--

DROP TABLE IF EXISTS `m_monitor_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_monitor_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `monitor_date` datetime NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` int(10) unsigned NOT NULL,
  `extraTypeId` int(10) unsigned DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_monitor_item_1` (`user`),
  KEY `FK_m_monitor_item_2_idx` (`sAccountId`),
  CONSTRAINT `FK_m_monitor_item_1` FOREIGN KEY (`user`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_monitor_item_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_monitor_item`
--

LOCK TABLES `m_monitor_item` WRITE;
/*!40000 ALTER TABLE `m_monitor_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_monitor_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_customize_view`
--

DROP TABLE IF EXISTS `m_prj_customize_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_customize_view` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `displayMessage` bit(1) NOT NULL,
  `displayMilestone` bit(1) NOT NULL,
  `displayTask` bit(1) NOT NULL,
  `displayBug` bit(1) NOT NULL,
  `displayStandup` bit(1) NOT NULL,
  `displayProblem` bit(1) NOT NULL,
  `displayRisk` bit(1) NOT NULL,
  `displayTimeLogging` bit(1) NOT NULL,
  `displayPage` bit(1) NOT NULL,
  `displayFile` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_customize_view_1_idx` (`projectId`),
  CONSTRAINT `FK_m_prj_customize_view_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_customize_view`
--

LOCK TABLES `m_prj_customize_view` WRITE;
/*!40000 ALTER TABLE `m_prj_customize_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_customize_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_member`
--

DROP TABLE IF EXISTS `m_prj_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `joinDate` datetime NOT NULL,
  `projectRoleId` int(11) DEFAULT NULL,
  `isAdmin` bit(1) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `billingRate` double DEFAULT NULL,
  `overtimeBillingRate` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_member_2` (`projectId`),
  KEY `FK_m_prj_member_1` (`username`),
  KEY `FK_m_prj_member_3_idx` (`sAccountId`),
  CONSTRAINT `FK_m_prj_member_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_member_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_member_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_member`
--

LOCK TABLES `m_prj_member` WRITE;
/*!40000 ALTER TABLE `m_prj_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_message`
--

DROP TABLE IF EXISTS `m_prj_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message` text COLLATE utf8mb4_unicode_ci,
  `posteddate` datetime NOT NULL,
  `posteduser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `projectid` int(10) unsigned NOT NULL,
  `category` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `isStick` bit(1) DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_message_2` (`projectid`),
  KEY `FK_m_prj_message_3` (`sAccountId`),
  KEY `FK_m_prj_message_1_idx` (`posteduser`),
  CONSTRAINT `FK_m_prj_message_1` FOREIGN KEY (`posteduser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_message_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_message_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_message`
--

LOCK TABLES `m_prj_message` WRITE;
/*!40000 ALTER TABLE `m_prj_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_milestone`
--

DROP TABLE IF EXISTS `m_prj_milestone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_milestone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `owner` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `flag` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `projectid` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createduser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_m_prj_milestone_2` (`projectid`),
  KEY `PK_m_prj_milestone_1` (`owner`),
  KEY `PK_m_prj_milestone_3_idx` (`sAccountId`),
  KEY `PK_m_prj_milestone_4_idx` (`createduser`),
  CONSTRAINT `PK_m_prj_milestone_1` FOREIGN KEY (`owner`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_4` FOREIGN KEY (`createduser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_milestone`
--

LOCK TABLES `m_prj_milestone` WRITE;
/*!40000 ALTER TABLE `m_prj_milestone` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_milestone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_notifications`
--

DROP TABLE IF EXISTS `m_prj_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `level` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_notifications_1_idx` (`username`),
  KEY `FK_m_prj_notifications_2_idx` (`projectId`),
  KEY `FK_m_prj_notifications_3_idx` (`sAccountId`),
  CONSTRAINT `FK_m_prj_notifications_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_notifications_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_notifications_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_notifications`
--

LOCK TABLES `m_prj_notifications` WRITE;
/*!40000 ALTER TABLE `m_prj_notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_problem`
--

DROP TABLE IF EXISTS `m_prj_problem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_problem` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `issuename` varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `projectid` int(10) unsigned NOT NULL,
  `raisedbyuser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assigntouser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `impact` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dateraised` datetime DEFAULT NULL,
  `datedue` datetime DEFAULT NULL,
  `actualstartdate` datetime DEFAULT NULL,
  `actualenddate` datetime DEFAULT NULL,
  `level` double unsigned DEFAULT NULL,
  `resolution` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `state` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `problemsource` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_risk_3` (`sAccountId`),
  KEY `FK_m_prj_risk_1` (`raisedbyuser`),
  KEY `FK_m_prj_problem_2` (`assigntouser`),
  KEY `FK_m_prj_problem_4_idx` (`projectid`),
  CONSTRAINT `FK_m_prj_problem_1` FOREIGN KEY (`raisedbyuser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_problem_2` FOREIGN KEY (`assigntouser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_problem_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_problem_4` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_problem`
--

LOCK TABLES `m_prj_problem` WRITE;
/*!40000 ALTER TABLE `m_prj_problem` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_problem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_project`
--

DROP TABLE IF EXISTS `m_prj_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `account` int(10) unsigned DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shortname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `planStartDate` datetime DEFAULT NULL,
  `planEndDate` datetime DEFAULT NULL,
  `targetBudget` double DEFAULT NULL,
  `homePage` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `actualBudget` double DEFAULT NULL,
  `projectType` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `projectStatus` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `defaultBillingRate` double DEFAULT NULL,
  `actualStartDate` datetime DEFAULT NULL,
  `actualEndDate` datetime DEFAULT NULL,
  `defaultOvertimeBillingRate` double DEFAULT NULL,
  `currencyid` int(11) DEFAULT NULL,
  `progress` double DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `avatarId` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_project_project_1` (`account`),
  KEY `FK_m_prj_project_4` (`sAccountId`),
  KEY `FK_m_prj_project_3` (`currencyid`),
  KEY `FK_m_prj_project_2` (`owner`),
  CONSTRAINT `FK_m_prj_project_2` FOREIGN KEY (`owner`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_project_3` FOREIGN KEY (`currencyid`) REFERENCES `s_currency` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_project_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_project_project_1` FOREIGN KEY (`account`) REFERENCES `m_crm_account` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_project`
--

LOCK TABLES `m_prj_project` WRITE;
/*!40000 ALTER TABLE `m_prj_project` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_risk`
--

DROP TABLE IF EXISTS `m_prj_risk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_risk` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `riskname` varchar(400) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `projectid` int(10) unsigned NOT NULL,
  `raisedbyuser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assigntouser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `consequence` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `probalitity` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dateraised` datetime DEFAULT NULL,
  `datedue` datetime DEFAULT NULL,
  `response` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `resolution` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level` double unsigned DEFAULT NULL,
  `source` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_risk1_1` (`projectid`),
  KEY `FK_m_prj_risk1_4` (`sAccountId`),
  KEY `FK_m_prj_risk1_2` (`raisedbyuser`),
  KEY `FK_m_prj_risk1_3` (`assigntouser`),
  CONSTRAINT `FK_m_prj_risk1_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_2` FOREIGN KEY (`raisedbyuser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_3` FOREIGN KEY (`assigntouser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_risk`
--

LOCK TABLES `m_prj_risk` WRITE;
/*!40000 ALTER TABLE `m_prj_risk` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_risk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_role`
--

DROP TABLE IF EXISTS `m_prj_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `sAccountId` int(11) NOT NULL,
  `projectid` int(10) unsigned NOT NULL,
  `isSystemRole` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_role_2` (`projectid`),
  KEY `FK_m_prj_role_1` (`sAccountId`),
  CONSTRAINT `FK_m_prj_role_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_role_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_role`
--

LOCK TABLES `m_prj_role` WRITE;
/*!40000 ALTER TABLE `m_prj_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_role_permission`
--

DROP TABLE IF EXISTS `m_prj_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) unsigned NOT NULL,
  `roleVal` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `projectid` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_role_permission_2` (`projectid`),
  KEY `FK_m_prj_role_permission_1` (`roleid`),
  CONSTRAINT `FK_m_prj_role_permission_1` FOREIGN KEY (`roleid`) REFERENCES `m_prj_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_role_permission_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_role_permission`
--

LOCK TABLES `m_prj_role_permission` WRITE;
/*!40000 ALTER TABLE `m_prj_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_standup`
--

DROP TABLE IF EXISTS `m_prj_standup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_standup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `whatlastday` text COLLATE utf8mb4_unicode_ci,
  `whattoday` text COLLATE utf8mb4_unicode_ci,
  `whatproblem` text COLLATE utf8mb4_unicode_ci,
  `forday` datetime NOT NULL,
  `logBy` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `lastUpdatedTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_standup_3` (`projectId`),
  KEY `FK_m_prj_standup_1` (`sAccountId`),
  KEY `FK_m_prj_standup_2` (`logBy`),
  CONSTRAINT `FK_m_prj_standup_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_standup_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_standup_3` FOREIGN KEY (`logBy`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_standup`
--

LOCK TABLES `m_prj_standup` WRITE;
/*!40000 ALTER TABLE `m_prj_standup` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_standup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_task`
--

DROP TABLE IF EXISTS `m_prj_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `taskname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `percentagecomplete` double NOT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `duration` double DEFAULT NULL,
  `isestimated` bit(1) DEFAULT NULL,
  `projectid` int(10) unsigned NOT NULL,
  `deadline` datetime DEFAULT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `taskindex` int(10) unsigned DEFAULT NULL,
  `actualStartDate` datetime DEFAULT NULL,
  `actualEndDate` datetime DEFAULT NULL,
  `tasklistid` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `assignUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `logBy` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `taskkey` int(11) DEFAULT NULL,
  `originalEstimate` double DEFAULT NULL,
  `remainEstimate` double DEFAULT NULL,
  `parentTaskId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_task_1` (`projectid`),
  KEY `FK_m_prj_task_2` (`tasklistid`),
  KEY `FK_m_prj_task_4` (`sAccountId`),
  KEY `FK_m_prj_task_3` (`assignUser`),
  KEY `FK_m_prj_task_5` (`logBy`),
  KEY `FK_m_prj_task_6_idx` (`parentTaskId`),
  CONSTRAINT `FK_m_prj_task_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_2` FOREIGN KEY (`tasklistid`) REFERENCES `m_prj_task_list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_3` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_5` FOREIGN KEY (`logBy`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_6` FOREIGN KEY (`parentTaskId`) REFERENCES `m_prj_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_task`
--

LOCK TABLES `m_prj_task` WRITE;
/*!40000 ALTER TABLE `m_prj_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_task_list`
--

DROP TABLE IF EXISTS `m_prj_task_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_task_list` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `projectid` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `milestoneId` int(11) DEFAULT NULL,
  `owner` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `groupIndex` int(11) DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createduser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_m_prj_task_list_2` (`milestoneId`),
  KEY `PK_m_prj_task_list_1` (`projectid`),
  KEY `PK_m_prj_task_list_3` (`owner`),
  KEY `PK_m_prj_task_list_4_idx` (`sAccountId`),
  KEY `PK_m_prj_task_list_5_idx` (`createduser`),
  CONSTRAINT `PK_m_prj_task_list_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_task_list_2` FOREIGN KEY (`milestoneId`) REFERENCES `m_prj_milestone` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_task_list_3` FOREIGN KEY (`owner`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_task_list_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_task_list_5` FOREIGN KEY (`createduser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_task_list`
--

LOCK TABLES `m_prj_task_list` WRITE;
/*!40000 ALTER TABLE `m_prj_task_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_task_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_prj_time_logging`
--

DROP TABLE IF EXISTS `m_prj_time_logging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_prj_time_logging` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `type` varchar(45) CHARACTER SET utf8mb4 DEFAULT NULL,
  `typeid` int(11) DEFAULT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `logValue` double NOT NULL,
  `loguser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime NOT NULL,
  `lastUpdatedTime` datetime NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `logForDay` datetime NOT NULL,
  `isBillable` bit(1) NOT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_time_logging_1` (`projectId`),
  KEY `FK_m_prj_time_logging_2_idx` (`sAccountId`),
  KEY `FK_m_prj_time_logging_3_idx` (`loguser`),
  CONSTRAINT `FK_m_prj_time_logging_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_time_logging_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_time_logging_3` FOREIGN KEY (`loguser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_prj_time_logging`
--

LOCK TABLES `m_prj_time_logging` WRITE;
/*!40000 ALTER TABLE `m_prj_time_logging` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_prj_time_logging` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_bug`
--

DROP TABLE IF EXISTS `m_tracker_bug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_bug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `summary` varchar(4000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `detail` text COLLATE utf8mb4_unicode_ci,
  `assignuser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `logby` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `severity` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duedate` datetime DEFAULT NULL,
  `environment` text COLLATE utf8mb4_unicode_ci,
  `resolution` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_int_01` int(10) unsigned DEFAULT NULL,
  `cus_int_02` int(10) unsigned DEFAULT NULL,
  `cus_int_03` int(10) unsigned DEFAULT NULL,
  `cus_int_04` int(10) unsigned DEFAULT NULL,
  `cus_int_05` int(10) unsigned DEFAULT NULL,
  `cus_int_06` int(10) unsigned DEFAULT NULL,
  `cus_int_07` int(10) unsigned DEFAULT NULL,
  `cus_int_08` int(10) unsigned DEFAULT NULL,
  `cus_int_09` int(10) unsigned DEFAULT NULL,
  `cus_int_10` int(10) unsigned DEFAULT NULL,
  `cus_str_01` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_str_02` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_str_03` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_str_04` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_str_05` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cus_time_01` datetime DEFAULT NULL,
  `cus_time_02` datetime DEFAULT NULL,
  `cus_time_03` datetime DEFAULT NULL,
  `cus_time_04` datetime DEFAULT NULL,
  `cus_dbl_01` double DEFAULT NULL,
  `cus_dbl_02` double DEFAULT NULL,
  `cus_dbl_03` double DEFAULT NULL,
  `projectid` int(10) unsigned NOT NULL,
  `resolveddate` datetime DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `estimateTime` double DEFAULT NULL,
  `estimateRemainTime` double DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `milestoneId` int(11) DEFAULT NULL,
  `bugkey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_bug_6` (`milestoneId`),
  KEY `FK_m_tracker_bug_4` (`projectid`),
  KEY `FK_m_tracker_bug_5` (`sAccountId`),
  KEY `FK_m_tracker_bug_1` (`assignuser`),
  KEY `FK_m_tracker_bug_2` (`logby`),
  CONSTRAINT `FK_m_tracker_bug_1` FOREIGN KEY (`logby`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_2` FOREIGN KEY (`assignuser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_3` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_4` FOREIGN KEY (`milestoneId`) REFERENCES `m_prj_milestone` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_5` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_bug`
--

LOCK TABLES `m_tracker_bug` WRITE;
/*!40000 ALTER TABLE `m_tracker_bug` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_bug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_bug_related_item`
--

DROP TABLE IF EXISTS `m_tracker_bug_related_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_bug_related_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bugid` int(10) unsigned NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ m_tracker_bug_related_item_1` (`bugid`),
  KEY `INDEX_m_tracker_bug_related_item_2` (`type`) USING BTREE,
  KEY `INDEX_m_tracker_bug_related_item_3` (`typeid`) USING BTREE,
  CONSTRAINT `FK_ m_tracker_bug_related_item_1` FOREIGN KEY (`bugid`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_bug_related_item`
--

LOCK TABLES `m_tracker_bug_related_item` WRITE;
/*!40000 ALTER TABLE `m_tracker_bug_related_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_bug_related_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_component`
--

DROP TABLE IF EXISTS `m_tracker_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_component` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectid` int(10) unsigned NOT NULL,
  `componentname` varchar(1000) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userlead` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_component_1` (`projectid`),
  KEY `FK_m_tracker_component_4` (`sAccountId`),
  KEY `FK_m_tracker_component_2` (`userlead`),
  KEY `FK_m_tracker_component_3` (`createdUser`),
  CONSTRAINT `FK_m_tracker_component_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_2` FOREIGN KEY (`userlead`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_component`
--

LOCK TABLES `m_tracker_component` WRITE;
/*!40000 ALTER TABLE `m_tracker_component` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_metadata`
--

DROP TABLE IF EXISTS `m_tracker_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_metadata` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectid` int(10) unsigned NOT NULL,
  `xmlstring` longtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_metadata_1` (`projectid`),
  CONSTRAINT `FK_m_tracker_metadata_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_metadata`
--

LOCK TABLES `m_tracker_metadata` WRITE;
/*!40000 ALTER TABLE `m_tracker_metadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_query`
--

DROP TABLE IF EXISTS `m_tracker_query`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_query` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sharetype` int(10) unsigned NOT NULL,
  `createddate` datetime DEFAULT NULL,
  `updateddate` datetime DEFAULT NULL,
  `owner` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `querytext` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `projectid` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_query_2` (`projectid`),
  KEY `FK_m_tracker_query_1` (`owner`),
  CONSTRAINT `FK_m_tracker_query_1` FOREIGN KEY (`owner`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_query_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_query`
--

LOCK TABLES `m_tracker_query` WRITE;
/*!40000 ALTER TABLE `m_tracker_query` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_query` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_related_bug`
--

DROP TABLE IF EXISTS `m_tracker_related_bug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_related_bug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bugid` int(10) unsigned NOT NULL,
  `relatedid` int(10) unsigned NOT NULL,
  `relatetype` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `comment` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_related_bug_1` (`bugid`),
  KEY `FK_m_tracker_related_bug_2` (`relatedid`),
  CONSTRAINT `FK_m_tracker_related_bug_1` FOREIGN KEY (`bugid`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_related_bug_2` FOREIGN KEY (`relatedid`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_related_bug`
--

LOCK TABLES `m_tracker_related_bug` WRITE;
/*!40000 ALTER TABLE `m_tracker_related_bug` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_related_bug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_tracker_version`
--

DROP TABLE IF EXISTS `m_tracker_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_tracker_version` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectid` int(10) unsigned NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `duedate` datetime DEFAULT NULL,
  `versionname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_version_1` (`projectid`),
  KEY `FK_m_tracker_version_3` (`sAccountId`),
  KEY `FK_m_tracker_version_2` (`createdUser`),
  CONSTRAINT `FK_m_tracker_version_1` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_version_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_version_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_tracker_version`
--

LOCK TABLES `m_tracker_version` WRITE;
/*!40000 ALTER TABLE `m_tracker_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `m_tracker_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_account`
--

DROP TABLE IF EXISTS `s_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `billingPlanId` int(11) DEFAULT NULL,
  `accountName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `paymentMethod` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pricing` double DEFAULT NULL,
  `pricingEffectFrom` datetime DEFAULT NULL,
  `pricingEffectTo` datetime DEFAULT NULL,
  `subdomain` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reminderStatus` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subdomain_UNIQUE` (`subdomain`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_account`
--

LOCK TABLES `s_account` WRITE;
/*!40000 ALTER TABLE `s_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_account_settings`
--

DROP TABLE IF EXISTS `s_account_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_account_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `logoPath` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `defaultTimezone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `defaultThemeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_account_settings_1` (`sAccountId`),
  KEY `FK_s_account_settings_2_idx` (`defaultThemeId`),
  CONSTRAINT `FK_s_account_settings_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_account_settings_2` FOREIGN KEY (`defaultThemeId`) REFERENCES `s_account_theme` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_account_settings`
--

LOCK TABLES `s_account_settings` WRITE;
/*!40000 ALTER TABLE `s_account_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_account_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_account_theme`
--

DROP TABLE IF EXISTS `s_account_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_account_theme` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logoPath` varchar(255) DEFAULT NULL,
  `topMenuBg` varchar(6) DEFAULT NULL,
  `topMenuBgSelected` varchar(6) DEFAULT NULL,
  `topMenuText` varchar(6) DEFAULT NULL,
  `topMenuTextSelected` varchar(6) DEFAULT NULL,
  `tabsheetBg` varchar(6) DEFAULT NULL,
  `tabsheetBgSelected` varchar(6) DEFAULT NULL,
  `tabsheetText` varchar(6) DEFAULT NULL,
  `tabsheetTextSelected` varchar(6) DEFAULT NULL,
  `vTabsheetBg` varchar(6) DEFAULT NULL,
  `vTabsheetBgSelected` varchar(6) DEFAULT NULL,
  `vTabsheetText` varchar(6) DEFAULT NULL,
  `vTabsheetTextSelected` varchar(6) DEFAULT NULL,
  `hTopMenuBg` varchar(6) DEFAULT NULL,
  `hTopMenuBgSelected` varchar(6) DEFAULT NULL,
  `hTopMenuText` varchar(6) DEFAULT NULL,
  `hTopMenuTextSelected` varchar(6) DEFAULT NULL,
  `actionBtn` varchar(6) DEFAULT NULL,
  `actionBtnText` varchar(6) DEFAULT NULL,
  `optionBtn` varchar(6) DEFAULT NULL,
  `optionBtnText` varchar(6) DEFAULT NULL,
  `clearBtn` varchar(6) DEFAULT NULL,
  `clearBtnText` varchar(6) DEFAULT NULL,
  `controlBtn` varchar(6) DEFAULT NULL,
  `controlBtnText` varchar(6) DEFAULT NULL,
  `dangerBtn` varchar(6) DEFAULT NULL,
  `dangerBtnText` varchar(6) DEFAULT NULL,
  `toggleBtn` varchar(6) DEFAULT NULL,
  `toggleBtnSelected` varchar(6) DEFAULT NULL,
  `toggleBtnText` varchar(6) DEFAULT NULL,
  `toggleBtnTextSelected` varchar(6) DEFAULT NULL,
  `isDefault` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_account_theme`
--

LOCK TABLES `s_account_theme` WRITE;
/*!40000 ALTER TABLE `s_account_theme` DISABLE KEYS */;
INSERT INTO `s_account_theme` VALUES (1,NULL,'575757','E4E4E4','FFFFFF','575757','F7F7F7','1C7DCE','525252','FFFFFF','7B7B7B','FFFFFF','FFFFFF','525252','47749D','FFFFFF','FFFFFF','535353','17D61B','FFFFFF','8B8B8B','FFFFFF','FAFAFA','525252','C06B11','FFFFFF','F64A46','FFFFFF','7B7B7B','1C7DCE','FFFFFF','FFFFFF','');
/*!40000 ALTER TABLE `s_account_theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_activitystream`
--

DROP TABLE IF EXISTS `s_activitystream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_activitystream` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeId` text CHARACTER SET utf8mb4 NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `action` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `module` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nameField` text COLLATE utf8mb4_unicode_ci,
  `extraTypeId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_activitystream_1` (`sAccountId`),
  KEY `FK_m_crm_activitystream_2_idx` (`createdUser`),
  KEY `FK_m_crm_activitystream_3` (`module`) USING BTREE,
  KEY `FK_m_crm_activitystream_4` (`type`) USING BTREE,
  KEY `FK_m_crm_activitystream_5` (`typeId`(100)),
  CONSTRAINT `FK_m_crm_activitystream_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_activitystream_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_activitystream`
--

LOCK TABLES `s_activitystream` WRITE;
/*!40000 ALTER TABLE `s_activitystream` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_activitystream` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_billing_plan`
--

DROP TABLE IF EXISTS `s_billing_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_billing_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `billingType` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numUsers` int(11) NOT NULL,
  `volume` bigint(20) unsigned NOT NULL,
  `numProjects` int(11) NOT NULL,
  `pricing` double NOT NULL,
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hasBugEnable` bit(1) DEFAULT NULL,
  `hasStandupMeetingEnable` bit(1) DEFAULT NULL,
  `hasTimeTracking` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_billing_plan`
--

LOCK TABLES `s_billing_plan` WRITE;
/*!40000 ALTER TABLE `s_billing_plan` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_billing_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_country`
--

DROP TABLE IF EXISTS `s_country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `countryname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_country`
--

LOCK TABLES `s_country` WRITE;
/*!40000 ALTER TABLE `s_country` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_currency`
--

DROP TABLE IF EXISTS `s_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_currency` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shortname` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `isocode` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `symbol` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '$',
  `conversionrate` double DEFAULT NULL,
  `fullname` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_currency`
--

LOCK TABLES `s_currency` WRITE;
/*!40000 ALTER TABLE `s_currency` DISABLE KEYS */;
INSERT INTO `s_currency` VALUES (118,'ALL','1','Lek',NULL,'Albania Lek'),(119,'AFN','2','؋',NULL,'Afghanistan Afghani'),(120,'ARS','3','$',NULL,'Argentina Peso'),(121,'AWG','4','ƒ',NULL,'Aruba Guilder'),(122,'AUD','5','$',NULL,'Australia Dollar'),(123,'AZN','6','ман',NULL,'Azerbaijan New Manat'),(124,'BSD','7','$',NULL,'Bahamas Dollar'),(125,'BBD','8','$',NULL,'Barbados Dollar'),(126,'BYR','9','p.',NULL,'Belarus Ruble'),(127,'BZD','10','BZ$',NULL,'Belize Dollar'),(128,'BMD','11','$',NULL,'Bermuda Dollar'),(129,'BOB','12','$b',NULL,'Bolivia Boliviano'),(130,'BAM','13','KM',NULL,'Bosnia and Herzegovina Convertible Marka'),(131,'BWP','14','P',NULL,'Botswana Pula'),(132,'BGN','15','лв',NULL,'Bulgaria Lev'),(133,'BRL','16','R$',NULL,'Brazil Real'),(134,'BND','17','$',NULL,'Brunei Darussalam Dollar'),(135,'KHR','18','៛',NULL,'Cambodia Riel'),(136,'CAD','19','$',NULL,'Canada Dollar'),(137,'KYD','20','$',NULL,'Cayman Islands Dollar'),(138,'CLP','21','$',NULL,'Chile Peso'),(139,'CNY','22','¥',NULL,'China Yuan Renminbi'),(140,'COP','23','$',NULL,'Colombia Peso'),(141,'CRC','24','₡',NULL,'Costa Rica Colon'),(142,'HRK','25','kn',NULL,'Croatia Kuna'),(143,'CUP','26','₱',NULL,'Cuba Peso'),(144,'CZK','27','Kč',NULL,'Czech Republic Koruna'),(145,'DKK','28','kr',NULL,'Denmark Krone'),(146,'DOP','29','RD$',NULL,'Dominican Republic Peso'),(147,'XCD','30','$',NULL,'East Caribbean Dollar'),(148,'EGP','31','£',NULL,'Egypt Pound'),(149,'SVC','32','$',NULL,'El Salvador Colon'),(150,'EEK','33','kr',NULL,'Estonia Kroon'),(151,'EUR','34','€',NULL,'Euro Member Countries'),(152,'FKP','35','£',NULL,'Falkland Islands (Malvinas) Pound'),(153,'FJD','36','$',NULL,'Fiji Dollar'),(154,'GHC','37','¢',NULL,'Ghana Cedis'),(155,'GIP','38','£',NULL,'Gibraltar Pound'),(156,'GTQ','39','Q',NULL,'Guatemala Quetzal'),(157,'GGP','40','£',NULL,'Guernsey Pound'),(158,'GYD','41','$',NULL,'Guyana Dollar'),(159,'HNL','42','L',NULL,'Honduras Lempira'),(160,'HKD','43','$',NULL,'Hong Kong Dollar'),(161,'HUF','44','Ft',NULL,'Hungary Forint'),(162,'ISK','45','kr',NULL,'Iceland Krona'),(163,'INR','46','',NULL,'India Rupee'),(164,'IDR','47','Rp',NULL,'Indonesia Rupiah'),(165,'IRR','48','﷼',NULL,'Iran Rial'),(166,'IMP','49','£',NULL,'Isle of Man Pound'),(167,'ILS','50','₪',NULL,'Israel Shekel'),(168,'JMD','51','J$',NULL,'Jamaica Dollar'),(169,'JPY','52','¥',NULL,'Japan Yen'),(170,'JEP','53','£',NULL,'Jersey Pound'),(171,'KZT','54','лв',NULL,'Kazakhstan Tenge'),(172,'KPW','55','₩',NULL,'Korea (North) Won'),(173,'KRW','56','₩',NULL,'Korea (South) Won'),(174,'KGS','57','лв',NULL,'Kyrgyzstan Som'),(175,'LAK','58','₭',NULL,'Laos Kip'),(176,'LVL','59','Ls',NULL,'Latvia Lat'),(177,'LBP','60','£',NULL,'Lebanon Pound'),(178,'LRD','61','$',NULL,'Liberia Dollar'),(179,'LTL','62','Lt',NULL,'Lithuania Litas'),(180,'MKD','63','ден',NULL,'Macedonia Denar'),(181,'MYR','64','RM',NULL,'Malaysia Ringgit'),(182,'MUR','65','₨',NULL,'Mauritius Rupee'),(183,'MXN','66','$',NULL,'Mexico Peso'),(184,'MNT','67','₮',NULL,'Mongolia Tughrik'),(185,'MZN','68','MT',NULL,'Mozambique Metical'),(186,'NAD','69','$',NULL,'Namibia Dollar'),(187,'NPR','70','₨',NULL,'Nepal Rupee'),(188,'ANG','71','ƒ',NULL,'Netherlands Antilles Guilder'),(189,'NZD','72','$',NULL,'New Zealand Dollar'),(190,'NIO','73','C$',NULL,'Nicaragua Cordoba'),(191,'NGN','74','₦',NULL,'Nigeria Naira'),(192,'KPW','75','₩',NULL,'Korea (North) Won'),(193,'NOK','76','kr',NULL,'Norway Krone'),(194,'OMR','77','﷼',NULL,'Oman Rial'),(195,'PKR','78','₨',NULL,'Pakistan Rupee'),(196,'PAB','79','B/',NULL,'Panama Balboa'),(197,'PYG','80','Gs',NULL,'Paraguay Guarani'),(198,'PEN','81','S/.',NULL,'Peru Nuevo Sol  '),(199,'PHP','82','₱',NULL,'Philippines Peso'),(200,'PLN','83','zł',NULL,'Poland Zloty'),(201,'QAR','84','﷼',NULL,'Qatar Riyal'),(202,'RON','85','lei',NULL,'Romania New Leu'),(203,'RUB','86','руб',NULL,'Russia Ruble'),(204,'SHP','87','£',NULL,'Saint Helena Pound'),(205,'SAR','88','﷼',NULL,'Saudi Arabia Riyal'),(206,'RSD','89','Дин.',NULL,'Serbia Dinar'),(207,'SCR','90','₨',NULL,'Seychelles Rupee'),(208,'SGD','91','$',NULL,'Singapore Dollar'),(209,'SBD','92','$',NULL,'Solomon Islands Dollar'),(210,'SOS','93','S',NULL,'Somalia Shilling'),(211,'ZAR','94','R',NULL,'South Africa Rand'),(212,'KRW','95','₩',NULL,'Korea (South) Won'),(213,'LKR','96','₨',NULL,'Sri Lanka Rupee'),(214,'SEK','97','kr',NULL,'Sweden Krona'),(215,'CHF','98','CHF',NULL,'Switzerland Franc'),(216,'SRD','99','$',NULL,'Suriname Dollar'),(217,'SYP','100','£',NULL,'Syria Pound'),(218,'TWD','101','NT$',NULL,'Taiwan New Dollar'),(219,'THB','102','฿',NULL,'Thailand Baht'),(220,'TTD','103','TT$',NULL,'Trinidad and Tobago Dollar'),(221,'TRY','104','',NULL,'Turkey Lira'),(222,'TRL','105','₤',NULL,'Turkey Lira'),(223,'TVD','106','$',NULL,'Tuvalu Dollar'),(224,'UAH','107','₴',NULL,'Ukraine Hryvna'),(225,'GBP','108','£',NULL,'United Kingdom Pound'),(226,'USD','109','$',NULL,'United States Dollar'),(227,'UYU','110','$U',NULL,'Uruguay Peso'),(228,'UZS','111','лв',NULL,'Uzbekistan Som'),(229,'VEF','112','Bs',NULL,'Venezuela Bolivar'),(230,'VND','113','₫',NULL,'Viet Nam Dong'),(231,'YER','114','﷼',NULL,'Yemen Rial'),(232,'ZWD','115','Z$',NULL,'Zimbabwe Dollar');
/*!40000 ALTER TABLE `s_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_customer_feedback`
--

DROP TABLE IF EXISTS `s_customer_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_customer_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reasonToLeave` text COLLATE utf8mb4_unicode_ci,
  `leaveType` int(11) DEFAULT NULL,
  `otherTool` varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reasonToBack` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_customer_feedback`
--

LOCK TABLES `s_customer_feedback` WRITE;
/*!40000 ALTER TABLE `s_customer_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_customer_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_favorite`
--

DROP TABLE IF EXISTS `s_favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastUpdatedTime` datetime NOT NULL,
  `createdTime` datetime NOT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_favorite_1_idx` (`createdUser`),
  KEY `FK_s_favorite_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_favorite_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_favorite_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_favorite`
--

LOCK TABLES `s_favorite` WRITE;
/*!40000 ALTER TABLE `s_favorite` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_relay_email_notification`
--

DROP TABLE IF EXISTS `s_relay_email_notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_relay_email_notification` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `typeid` text CHARACTER SET utf8mb4 NOT NULL,
  `action` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `changeBy` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `changeComment` text COLLATE utf8mb4_unicode_ci,
  `extraTypeId` int(11) DEFAULT NULL,
  `emailHandlerBean` varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_relay_email_notification_1_idx` (`sAccountId`),
  KEY `FK_s_relay_email_notification_2` (`typeid`(100)),
  KEY `FK_s_relay_email_notification_3` (`type`),
  CONSTRAINT `FK_s_relay_email_notification_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_relay_email_notification`
--

LOCK TABLES `s_relay_email_notification` WRITE;
/*!40000 ALTER TABLE `s_relay_email_notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_relay_email_notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_relay_mail`
--

DROP TABLE IF EXISTS `s_relay_mail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_relay_mail` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bodyContent` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `recipients` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `subject` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `fromName` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fromEmail` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `emailHandlerBean` varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_relay_mail_1` (`sAccountId`),
  CONSTRAINT `FK_s_relay_mail_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_relay_mail`
--

LOCK TABLES `s_relay_mail` WRITE;
/*!40000 ALTER TABLE `s_relay_mail` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_relay_mail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_report_bug_issue`
--

DROP TABLE IF EXISTS `s_report_bug_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_report_bug_issue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) DEFAULT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `userAgent` text COLLATE utf8mb4_unicode_ci,
  `errorTrace` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `ipaddress` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country_code` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_report_bug_issue_1` (`sAccountId`),
  KEY `FK_s_report_bug_issue_2` (`username`),
  CONSTRAINT `FK_s_report_bug_issue_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_s_report_bug_issue_2` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_report_bug_issue`
--

LOCK TABLES `s_report_bug_issue` WRITE;
/*!40000 ALTER TABLE `s_report_bug_issue` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_report_bug_issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_role_permission`
--

DROP TABLE IF EXISTS `s_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `roleVal` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_role_permission_1` (`roleid`),
  CONSTRAINT `FK_role_permission_1` FOREIGN KEY (`roleid`) REFERENCES `s_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_role_permission`
--

LOCK TABLES `s_role_permission` WRITE;
/*!40000 ALTER TABLE `s_role_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_roles`
--

DROP TABLE IF EXISTS `s_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_roles` (
  `rolename` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isSystemRole` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_roles_1` (`sAccountId`),
  CONSTRAINT `FK_s_roles_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_roles`
--

LOCK TABLES `s_roles` WRITE;
/*!40000 ALTER TABLE `s_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_save_search_result`
--

DROP TABLE IF EXISTS `s_save_search_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_save_search_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `saveUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `queryText` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `queryName` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_save_search_result_1_idx` (`sAccountId`),
  KEY `FK_FK_s_save_search_result_2_idx` (`saveUser`),
  CONSTRAINT `FK_FK_s_save_search_result_2` FOREIGN KEY (`saveUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_save_search_result_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_save_search_result`
--

LOCK TABLES `s_save_search_result` WRITE;
/*!40000 ALTER TABLE `s_save_search_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_save_search_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_table_customize_view`
--

DROP TABLE IF EXISTS `s_table_customize_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_table_customize_view` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdUser` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `viewId` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `viewInfo` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_table_customize_view_1_idx` (`createdUser`),
  KEY `FK_s_table_customize_view_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_table_customize_view_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_table_customize_view_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_table_customize_view`
--

LOCK TABLES `s_table_customize_view` WRITE;
/*!40000 ALTER TABLE `s_table_customize_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_table_customize_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_tag`
--

DROP TABLE IF EXISTS `s_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeid` varchar(100) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_tag_relationship_1_idx` (`name`),
  KEY `FK_s_tag_1_idx` (`sAccountId`),
  CONSTRAINT `FK_s_tag_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_tag`
--

LOCK TABLES `s_tag` WRITE;
/*!40000 ALTER TABLE `s_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user`
--

DROP TABLE IF EXISTS `s_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user` (
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `firstname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `middlename` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `lastname` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dateofbirth` datetime DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `website` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registeredTime` datetime DEFAULT NULL,
  `lastAccessedTime` datetime DEFAULT NULL,
  `company` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `timezone` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `language` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `workPhone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `homePhone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebookAccount` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `twitterAccount` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `skypeContact` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatarId` varchar(90) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user`
--

LOCK TABLES `s_user` WRITE;
/*!40000 ALTER TABLE `s_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_account`
--

DROP TABLE IF EXISTS `s_user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `accountId` int(11) NOT NULL,
  `isAccountOwner` bit(1) NOT NULL,
  `roleId` int(11) DEFAULT NULL,
  `registeredTime` datetime NOT NULL,
  `registerStatus` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lastAccessedTime` datetime DEFAULT NULL,
  `registrationSource` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_account_1` (`accountId`),
  KEY `FK_s_user_account_3` (`roleId`),
  KEY `FK_s_user_account_2_idx` (`username`),
  CONSTRAINT `FK_s_user_account_1` FOREIGN KEY (`accountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_2` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_3` FOREIGN KEY (`roleId`) REFERENCES `s_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_account`
--

LOCK TABLES `s_user_account` WRITE;
/*!40000 ALTER TABLE `s_user_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_account_invitation`
--

DROP TABLE IF EXISTS `s_user_account_invitation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_account_invitation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `accountId` int(11) NOT NULL,
  `invitationStatus` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sentDate` datetime DEFAULT NULL,
  `createdTime` datetime NOT NULL,
  `inviteUser` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_account_invitation_1_idx` (`username`),
  KEY `FK_s_user_account_invitation_2_idx` (`accountId`),
  CONSTRAINT `FK_s_user_account_invitation_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_invitation_2` FOREIGN KEY (`accountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_account_invitation`
--

LOCK TABLES `s_user_account_invitation` WRITE;
/*!40000 ALTER TABLE `s_user_account_invitation` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_account_invitation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_information`
--

DROP TABLE IF EXISTS `s_user_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_hr_employee_2` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_information`
--

LOCK TABLES `s_user_information` WRITE;
/*!40000 ALTER TABLE `s_user_information` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_permission`
--

DROP TABLE IF EXISTS `s_user_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_permission` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hasPermission` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `s_user_permission` (`username`),
  CONSTRAINT `s_user_permission` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_permission`
--

LOCK TABLES `s_user_permission` WRITE;
/*!40000 ALTER TABLE `s_user_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_preference`
--

DROP TABLE IF EXISTS `s_user_preference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_preference` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastModuleVisit` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lastAccessedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_preference_1` (`username`),
  KEY `FK_s_user_preference_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_user_preference_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_preference_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_preference`
--

LOCK TABLES `s_user_preference` WRITE;
/*!40000 ALTER TABLE `s_user_preference` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_preference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_user_tracking`
--

DROP TABLE IF EXISTS `s_user_tracking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_user_tracking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userAgent` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdTime` datetime NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_tracking_1` (`sAccountId`),
  KEY `FK_s_user_tracking_2` (`username`),
  CONSTRAINT `FK_s_user_tracking_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_tracking_2` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_user_tracking`
--

LOCK TABLES `s_user_tracking` WRITE;
/*!40000 ALTER TABLE `s_user_tracking` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_user_tracking` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-27 21:47:22
