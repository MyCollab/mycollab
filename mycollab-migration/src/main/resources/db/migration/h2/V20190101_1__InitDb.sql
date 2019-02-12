--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;


CREATE TABLE `QRTZ_JOB_DETAILS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `JOB_NAME` varchar(200)  NOT NULL,
  `JOB_GROUP` varchar(200)  NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250)  NOT NULL,
  `IS_DURABLE` varchar(1)  NOT NULL,
  `IS_NONCONCURRENT` varchar(1)  NOT NULL,
  `IS_UPDATE_DATA` varchar(1)  NOT NULL,
  `REQUESTS_RECOVERY` varchar(1)  NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;


CREATE TABLE `QRTZ_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `JOB_NAME` varchar(200)  NOT NULL,
  `JOB_GROUP` varchar(200)  NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16)  NOT NULL,
  `TRIGGER_TYPE` varchar(8)  NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`sched_name`, `job_name`, `job_group`)
) ;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;


CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ;



--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;


CREATE TABLE `QRTZ_CALENDARS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `CALENDAR_NAME` varchar(200)  NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ;


--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;


CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `CRON_EXPRESSION` varchar(200)  NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ;



--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;


CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `ENTRY_ID` varchar(95)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `INSTANCE_NAME` varchar(200)  NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16)  NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ;



--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;


CREATE TABLE `QRTZ_LOCKS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `LOCK_NAME` varchar(40)  NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ;



--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;


CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ;


--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;


CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `INSTANCE_NAME` varchar(200)  NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ;



--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;


CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;


CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `SCHED_NAME` varchar(120)  NOT NULL,
  `TRIGGER_NAME` varchar(200)  NOT NULL,
  `TRIGGER_GROUP` varchar(200)  NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ;


--
-- Table structure for table `s_user`
--

DROP TABLE IF EXISTS `s_user`;


CREATE TABLE `s_user` (
  `username` varchar(45) NOT NULL,
  `firstname` varchar(45) NOT NULL,
  `lastname` varchar(45) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `email` varchar(255) NULL,
  `website` varchar(255) DEFAULT NULL,
  `registeredTime` datetime DEFAULT NULL,
  `lastAccessedTime` datetime DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `timezone` varchar(45) DEFAULT NULL,
  `language` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `workPhone` varchar(20) DEFAULT NULL,
  `homePhone` varchar(20) DEFAULT NULL,
  `facebookAccount` varchar(45) DEFAULT NULL,
  `twitterAccount` varchar(45) DEFAULT NULL,
  `skypeContact` varchar(45) DEFAULT NULL,
  `avatarId` varchar(90) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `requestAd` tinyint(1) DEFAULT '1',
  `YYMMDDFormat` varchar(45) DEFAULT NULL,
  `humanDateFormat` varchar(45) DEFAULT NULL,
  `MMDDFormat` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`)
) ;

--
-- Table structure for table `s_account`
--

DROP TABLE IF EXISTS `s_account`;


CREATE TABLE `s_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `billingPlanId` int(11) DEFAULT NULL,
  `accountName` varchar(100) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `subDomain` varchar(45) DEFAULT NULL,
  `reminderStatus` varchar(45) DEFAULT NULL,
  `siteName` varchar(255) DEFAULT NULL,
  `logoPath` varchar(255) DEFAULT NULL,
  `defaultTimezone` varchar(45) DEFAULT NULL,
  `faviconPath` varchar(225) DEFAULT NULL,
  `defaultCurrencyId` varchar(4) DEFAULT NULL,
  `defaultYYMMDDFormat` varchar(45) DEFAULT NULL,
  `defaultHumanDateFormat` varchar(45) DEFAULT NULL,
  `defaultMMDDFormat` varchar(45) DEFAULT NULL,
  `defaultLanguageTag` varchar(10) DEFAULT NULL,
  `displayEmailPublicly` tinyint(1) DEFAULT NULL,
  `trialFrom` date DEFAULT NULL,
  `trialTo` date DEFAULT NULL,
  `paymentMethod` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subdomain_UNIQUE` (`subdomain`)
) ;

--
-- Table structure for table `s_activitystream`
--

DROP TABLE IF EXISTS `s_activitystream`;


CREATE TABLE `s_activitystream` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeId` varchar(100) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `action` varchar(45) DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `module` varchar(45) DEFAULT NULL,
  `nameField` text  ,
  `extraTypeId` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_activitystream_1` (`sAccountId`),
  KEY `FK_m_crm_activitystream_2_idx` (`createdUser`),
  KEY `FK_m_crm_activitystream_3` (`module`),
  KEY `FK_m_crm_activitystream_4` (`type`),
  KEY `FK_m_crm_activitystream_5` (`typeId`),
  CONSTRAINT `FK_m_crm_activitystream_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_activitystream_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;

--
-- Table structure for table `m_audit_log`
--

DROP TABLE IF EXISTS `m_audit_log`;


CREATE TABLE `m_audit_log` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `object_class` varchar(255) NOT NULL,
  `changeset` mediumtext NOT NULL,
  `createdTime` datetime NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeid` int(11) NOT NULL,
  `module` varchar(45) NOT NULL,
  `activityLogId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_audit_log_2` (`sAccountId`),
  KEY `FK_m_audit_log_1` (`createdUser`),
  KEY `FK_m_audit_log_3_idx` (`activityLogId`),
  KEY `INDEX_m_audit_log_4` (`type`) ,
  KEY `INDEX_m_audit_log_5` (`typeid`) ,
  KEY `INDEX_m_audit_log_6` (`module`) ,
  CONSTRAINT `FK_m_audit_log_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_audit_log_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_audit_log_3` FOREIGN KEY (`activityLogId`) REFERENCES `s_activitystream` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_comment`
--

DROP TABLE IF EXISTS `m_comment`;


CREATE TABLE `m_comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `comment` text  ,
  `createdUser` varchar(45) NOT NULL,
  `createdTime` datetime NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `typeId` varchar(100)  ,
  `sAccountId` int(11) DEFAULT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_comment_1` (`sAccountId`),
  KEY `INDEX_m_comment_2` (`extraTypeId`),
  KEY `INDEX_m_comment_4` (`type`),
  KEY `FK_m_comment_2` (`createdUser`),
  KEY `INDEX_m_comment_3` (`typeId`),
  CONSTRAINT `FK_m_comment_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_comment_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_customer`
--

DROP TABLE IF EXISTS `m_client`;


CREATE TABLE `m_client` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `website` varchar(255) DEFAULT NULL,
  `phoneOffice` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `alternatePhone` varchar(45) DEFAULT NULL,
  `annualRevenue` varchar(45) DEFAULT NULL,
  `billingAddress` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `postalCode` varchar(45) DEFAULT NULL,
  `description` mediumtext  ,
  `state` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `ownership` varchar(255) DEFAULT NULL,
  `shippingAddress` varchar(255) DEFAULT NULL,
  `shippingCity` varchar(100) DEFAULT NULL,
  `shippingPostalCode` varchar(45) DEFAULT NULL,
  `shippingState` varchar(45) DEFAULT NULL,
  `numemployees` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `assignUser` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `industry` varchar(45) DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `billingCountry` varchar(45) DEFAULT NULL,
  `shippingCountry` varchar(45) DEFAULT NULL,
  `avatarId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_crm_account_7` (`sAccountId`),
  KEY `FK_m_crm_account_6` (`createdUser`),
  KEY `FK_m_crm_account_8` (`assignUser`),
  CONSTRAINT `FK_m_crm_account_6` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_account_7` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_crm_account_8` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;


--
-- Table structure for table `m_form_custom_field_value`
--

DROP TABLE IF EXISTS `m_form_custom_field_value`;


CREATE TABLE `m_form_custom_field_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(45) NOT NULL,
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
  `text1` text  ,
  `text2` text  ,
  `text3` text  ,
  `text4` text  ,
  `text5` text  ,
  `textarea1` text  ,
  `textarea2` text  ,
  `textarea3` text  ,
  `textarea4` text  ,
  `textarea5` text  ,
  `bool1` bit(1) DEFAULT NULL,
  `bool2` bit(1) DEFAULT NULL,
  `bool3` bit(1) DEFAULT NULL,
  `bool4` bit(1) DEFAULT NULL,
  `bool5` bit(1) DEFAULT NULL,
  `pick1` varchar(100) DEFAULT NULL,
  `pick2` varchar(100) DEFAULT NULL,
  `pick3` varchar(100) DEFAULT NULL,
  `pick4` varchar(100) DEFAULT NULL,
  `pick5` varchar(100) DEFAULT NULL,
  `multipick1` text  ,
  `multipick2` text  ,
  `multipick3` text  ,
  `multipick4` text  ,
  `multipick5` text  ,
  PRIMARY KEY (`id`),
  KEY `INDEX_m_form_custom_field_value_1` (`module`) ,
  KEY `INDEX_m_form_custom_field_value_2` (`typeid`) 
) ;


--
-- Table structure for table `m_form_section`
--

DROP TABLE IF EXISTS `m_form_section`;


CREATE TABLE `m_form_section` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `layoutIndex` int(11) NOT NULL,
  `module` varchar(45) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `layoutType` int(2) NOT NULL,
  `isDeleteSection` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_form_section_1_idx` (`sAccountId`),
  CONSTRAINT `FK_m_form_section_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_form_section_field`
--

DROP TABLE IF EXISTS `m_form_section_field`;


CREATE TABLE `m_form_section_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sectionId` int(11) NOT NULL,
  `isMandatory` bit(1) NOT NULL,
  `fieldIndex` int(11) NOT NULL,
  `displayName` varchar(100) NOT NULL,
  `fieldFormat` varchar(200) NOT NULL,
  `fieldname` varchar(45) NOT NULL,
  `fieldType` varchar(1000) NOT NULL,
  `isRequired` bit(1) NOT NULL,
  `isCustom` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_form_section_field_2_idx` (`sectionId`),
  CONSTRAINT `FK_m_form_section_field_2` FOREIGN KEY (`sectionId`) REFERENCES `m_form_section` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_monitor_item`
--

DROP TABLE IF EXISTS `m_monitor_item`;


CREATE TABLE `m_monitor_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `createdTime` datetime NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeId` varchar(100) unsigned NOT NULL,
  `extraTypeId` int(10) unsigned DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_monitor_item_1` (`username`),
  KEY `FK_m_monitor_item_2_idx` (`sAccountId`),
  CONSTRAINT `FK_m_monitor_item_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_monitor_item_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `m_options`
--

DROP TABLE IF EXISTS `m_options`;


CREATE TABLE `m_options` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45)  NOT NULL,
  `typeVal` varchar(255)  NOT NULL,
  `description` text ,
  `orderIndex` int(3) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `createdTime` datetime NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `extraId` int(11) DEFAULT NULL,
  `isDefault` tinyint(1) NOT NULL,
  `refOption` int(11) DEFAULT NULL,
  `color` varchar(6)  NOT NULL,
  `fieldgroup` varchar(45)  NOT NULL,
  `isShow` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_options_1_idx` (`sAccountId`),
  KEY `FK_m_options_2_idx` (`createdUser`),
  CONSTRAINT `FK_m_options_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_options_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;

--
-- Table structure for table `m_prj_project`
--

DROP TABLE IF EXISTS `m_prj_project`;


CREATE TABLE `m_prj_project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `createUser` varchar(45) DEFAULT NULL,
  `clientId` int(10) unsigned DEFAULT NULL,
  `priority` varchar(45) DEFAULT NULL,
  `shortName` varchar(45) NOT NULL,
  `planStartDate` date DEFAULT NULL,
  `planEndDate` date DEFAULT NULL,
  `targetBudget` double DEFAULT NULL,
  `homePage` varchar(255) DEFAULT NULL,
  `actualBudget` double DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `description` mediumtext  ,
  `defaultBillingRate` double DEFAULT NULL,
  `defaultOvertimeBillingRate` double DEFAULT NULL,
  `currencyId` varchar(4) DEFAULT NULL,
  `progress` double DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `avatarId` varchar(100) DEFAULT NULL,
  `contextAsk` tinyint(1) DEFAULT '1',
  `deadline` date DEFAULT NULL,
  `isPublic` tinyint(1) DEFAULT NULL,
  `isTemplate` tinyint(1) DEFAULT NULL,
  `memLead` varchar(45) DEFAULT NULL,
  `color` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_project_project_1` (`clientId`),
  KEY `FK_m_prj_project_4` (`sAccountId`),
  KEY `FK_m_prj_project_3` (`currencyid`),
  KEY `FK_m_prj_project_2` (`createUser`),
  KEY `FK_m_prj_project_5` (`memLead`),
  CONSTRAINT `FK_m_prj_project_2` FOREIGN KEY (`createUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_project_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_project_5` FOREIGN KEY (`memLead`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_project_6` FOREIGN KEY (`clientId`) REFERENCES `m_client` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ;

--
-- Table structure for table `m_prj_role`
--

DROP TABLE IF EXISTS `m_prj_role`;


CREATE TABLE `m_prj_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) NOT NULL,
  `description` text  ,
  `sAccountId` int(11) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `isSystemRole` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_role_2` (`projectId`),
  KEY `FK_m_prj_role_1` (`sAccountId`),
  CONSTRAINT `FK_m_prj_role_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_role_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `m_prj_customize_view`
--

DROP TABLE IF EXISTS `m_prj_customize_view`;


CREATE TABLE `m_prj_customize_view` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `displayMessage` bit(1) NOT NULL,
  `displayMilestone` bit(1) NOT NULL,
  `displayStandup` bit(1) NOT NULL,
  `displayTimeLogging` bit(1) NOT NULL,
  `displayPage` bit(1) NOT NULL,
  `displayInvoice` bit(1) NOT NULL,
  `displayTicket` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_customize_view_1_idx` (`projectId`),
  CONSTRAINT `FK_m_prj_customize_view_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_invoice`
--

DROP TABLE IF EXISTS `m_prj_invoice`;


CREATE TABLE `m_prj_invoice` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `createdTime` datetime NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `assignUser` varchar(45) DEFAULT NULL,
  `amount` double NOT NULL,
  `currentId` varchar(4) DEFAULT NULL,
  `clientId` int(10) unsigned DEFAULT NULL,
  `contactUserFullName` varchar(100) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `note` varchar(400) DEFAULT NULL,
  `description` mediumtext  ,
  `type` varchar(45) NOT NULL,
  `noId` varchar(400) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `issueDate` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_invoice_1_idx` (`currentId`),
  KEY `FK_m_prj_invoice_2_idx` (`clientId`),
  KEY `FK_m_prj_invoice_3_idx` (`sAccountId`),
  KEY `FK_m_prj_invoice_4_idx` (`projectId`),
  CONSTRAINT `FK_m_prj_invoice_2` FOREIGN KEY (`clientId`) REFERENCES `m_client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_invoice_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_invoice_4` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_kanban_board`
--

DROP TABLE IF EXISTS `m_prj_kanban_board`;


CREATE TABLE `m_prj_kanban_board` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255)  NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `lead` varchar(45) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_kanban_board_1_idx` (`projectId`),
  KEY `FK_m_prj_kanban_board_2_idx` (`sAccountId`),
  KEY `FK_m_prj_kanban_board_3_idx` (`lead`),
  CONSTRAINT `FK_m_prj_kanban_board_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_kanban_board_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_kanban_board_3` FOREIGN KEY (`lead`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_member`
--

DROP TABLE IF EXISTS `m_prj_member`;


CREATE TABLE `m_prj_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `projectRoleId` int(11) unsigned DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `billingRate` double DEFAULT NULL,
  `overtimeBillingRate` double DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_member_2` (`projectId`),
  KEY `FK_m_prj_member_1` (`username`),
  KEY `FK_m_prj_member_3_idx` (`sAccountId`),
  KEY `FK_m_prj_member_4_idx` (`projectRoleId`),
  CONSTRAINT `FK_m_prj_member_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_member_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_member_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_member_4` FOREIGN KEY (`projectRoleId`) REFERENCES `m_prj_role` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ;


--
-- Table structure for table `m_prj_message`
--

DROP TABLE IF EXISTS `m_prj_message`;


CREATE TABLE `m_prj_message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(1000) NOT NULL,
  `message` mediumtext  ,
  `createdUser` varchar(45) DEFAULT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `category` varchar(45) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `isStick` bit(1) DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_message_2` (`projectId`),
  KEY `FK_m_prj_message_3` (`sAccountId`),
  KEY `FK_m_prj_message_1_idx` (`createdUser`),
  CONSTRAINT `FK_m_prj_message_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_message_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_message_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;


--
-- Table structure for table `m_prj_milestone`
--

DROP TABLE IF EXISTS `m_prj_milestone`;


CREATE TABLE `m_prj_milestone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` mediumtext  ,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `assignUser` varchar(45) DEFAULT NULL,
  `flag` varchar(45) DEFAULT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `color` varchar(6) DEFAULT NULL,
  `priority` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PK_m_prj_milestone_2` (`projectId`),
  KEY `PK_m_prj_milestone_1` (`assignUser`),
  KEY `PK_m_prj_milestone_3_idx` (`sAccountId`),
  KEY `PK_m_prj_milestone_4_idx` (`createdUser`),
  CONSTRAINT `PK_m_prj_milestone_1` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PK_m_prj_milestone_4` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_notifications`
--

DROP TABLE IF EXISTS `m_prj_notifications`;


CREATE TABLE `m_prj_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `level` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_notifications_1_idx` (`username`),
  KEY `FK_m_prj_notifications_2_idx` (`projectId`),
  KEY `FK_m_prj_notifications_3_idx` (`sAccountId`),
  CONSTRAINT `FK_m_prj_notifications_1` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_notifications_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_notifications_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `m_prj_risk`
--

DROP TABLE IF EXISTS `m_prj_risk`;


CREATE TABLE `m_prj_risk` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(400) NOT NULL,
  `description` mediumtext  ,
  `projectId` int(10) unsigned NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `assignUser` varchar(45) DEFAULT NULL,
  `consequence` varchar(45) DEFAULT NULL,
  `probability` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `raisedDate` date DEFAULT NULL,
  `dueDate` date DEFAULT NULL,
  `response` varchar(255) DEFAULT NULL,
  `resolution` varchar(4000) DEFAULT NULL,
  `source` varchar(45) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `milestoneId` int(11) DEFAULT NULL,
  `percentageComplete` double DEFAULT NULL,
  `priority` varchar(45) NOT NULL,
  `remainEstimate` double DEFAULT NULL,
  `originalEstimate` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_risk1_1` (`projectId`),
  KEY `FK_m_prj_risk1_4` (`sAccountId`),
  KEY `FK_m_prj_risk1_2` (`createdUser`),
  KEY `FK_m_prj_risk1_3` (`assignUser`),
  KEY `FK_m_prj_risk1_5_idx` (`milestoneId`),
  CONSTRAINT `FK_m_prj_risk1_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_3` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_risk1_5` FOREIGN KEY (`milestoneId`) REFERENCES `m_prj_milestone` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ;


--
-- Table structure for table `m_prj_role_permission`
--

DROP TABLE IF EXISTS `m_prj_role_permission`;


CREATE TABLE `m_prj_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` int(11) unsigned NOT NULL,
  `roleVal` text NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_role_permission_2` (`projectid`),
  KEY `FK_m_prj_role_permission_1` (`roleid`),
  CONSTRAINT `FK_m_prj_role_permission_1` FOREIGN KEY (`roleid`) REFERENCES `m_prj_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_role_permission_2` FOREIGN KEY (`projectid`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_standup`
--

DROP TABLE IF EXISTS `m_prj_standup`;


CREATE TABLE `m_prj_standup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `whatlastday` text  ,
  `whattoday` text  ,
  `whatproblem` text  ,
  `forday` date NOT NULL,
  `logBy` varchar(45) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_standup_3` (`projectId`),
  KEY `FK_m_prj_standup_1` (`sAccountId`),
  KEY `FK_m_prj_standup_2` (`logBy`),
  CONSTRAINT `FK_m_prj_standup_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_standup_2` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_standup_3` FOREIGN KEY (`logBy`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_task`
--

DROP TABLE IF EXISTS `m_prj_task`;


CREATE TABLE `m_prj_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(400) NOT NULL,
  `percentagecomplete` double NOT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `priority` varchar(45) NOT NULL,
  `duration` bigint(20) DEFAULT NULL,
  `isestimated` bit(1) DEFAULT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `dueDate` date DEFAULT NULL,
  `description` mediumtext  ,
  `taskindex` int(10) unsigned DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `assignUser` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `taskkey` int(11) DEFAULT NULL,
  `originalEstimate` double DEFAULT NULL,
  `remainEstimate` double DEFAULT NULL,
  `parentTaskId` int(10) unsigned DEFAULT NULL,
  `milestoneId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_task_1` (`projectId`),
  KEY `FK_m_prj_task_4` (`sAccountId`),
  KEY `FK_m_prj_task_3` (`assignUser`),
  KEY `FK_m_prj_task_5` (`createdUser`),
  KEY `FK_m_prj_task_6_idx` (`parentTaskId`),
  KEY `FK_m_prj_task_7_idx` (`milestoneId`),
  CONSTRAINT `FK_m_prj_task_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_3` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_5` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_6` FOREIGN KEY (`parentTaskId`) REFERENCES `m_prj_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_task_7` FOREIGN KEY (`milestoneId`) REFERENCES `m_prj_milestone` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_prj_time_logging`
--

DROP TABLE IF EXISTS `m_prj_time_logging`;


CREATE TABLE `m_prj_time_logging` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  `typeId` int(11) DEFAULT NULL,
  `note` mediumtext  ,
  `logValue` double NOT NULL,
  `loguser` varchar(45) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `logForDay` datetime NOT NULL,
  `isBillable` bit(1) NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `isOvertime` bit(1) DEFAULT NULL,
  `isApproved` bit(1) DEFAULT NULL,
  `approveUser` varchar(45) DEFAULT NULL,
  `approveTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_prj_time_logging_1` (`projectId`),
  KEY `FK_m_prj_time_logging_2_idx` (`sAccountId`),
  KEY `FK_m_prj_time_logging_3_idx` (`loguser`),
  KEY `FK_m_prj_time_logging_4_idx` (`approveUser`),
  CONSTRAINT `FK_m_prj_time_logging_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_time_logging_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_time_logging_3` FOREIGN KEY (`loguser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_time_logging_4` FOREIGN KEY (`approveUser`) REFERENCES `s_user` (`username`)
) ;


--
-- Table structure for table `m_tracker_bug`
--

DROP TABLE IF EXISTS `m_tracker_bug`;


CREATE TABLE `m_tracker_bug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(4000) NOT NULL,
  `detail` mediumtext  ,
  `assignUser` varchar(45) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `severity` varchar(45) DEFAULT NULL,
  `priority` varchar(45) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `dueDate` date DEFAULT NULL,
  `environment` varchar(4000) DEFAULT NULL,
  `resolution` varchar(45) DEFAULT NULL,
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
  `cus_str_01` varchar(255) DEFAULT NULL,
  `cus_str_02` varchar(255) DEFAULT NULL,
  `cus_str_03` varchar(255) DEFAULT NULL,
  `cus_str_04` varchar(255) DEFAULT NULL,
  `cus_str_05` varchar(255) DEFAULT NULL,
  `cus_time_01` datetime DEFAULT NULL,
  `cus_time_02` datetime DEFAULT NULL,
  `cus_time_03` datetime DEFAULT NULL,
  `cus_time_04` datetime DEFAULT NULL,
  `cus_dbl_01` double DEFAULT NULL,
  `cus_dbl_02` double DEFAULT NULL,
  `cus_dbl_03` double DEFAULT NULL,
  `projectId` int(10) unsigned NOT NULL,
  `resolveddate` datetime DEFAULT NULL,
  `description` mediumtext  ,
  `originalEstimate` double DEFAULT NULL,
  `remainEstimate` double DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `milestoneId` int(11) DEFAULT NULL,
  `bugkey` int(11) DEFAULT NULL,
  `bugIndex` int(10) unsigned DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  `percentagecomplete` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_bug_6` (`milestoneId`),
  KEY `FK_m_tracker_bug_4` (`projectId`),
  KEY `FK_m_tracker_bug_5` (`sAccountId`),
  KEY `FK_m_tracker_bug_1` (`assignUser`),
  KEY `FK_m_tracker_bug_2` (`createdUser`),
  CONSTRAINT `FK_m_tracker_bug_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_2` FOREIGN KEY (`assignUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_3` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_4` FOREIGN KEY (`milestoneId`) REFERENCES `m_prj_milestone` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_bug_5` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_tracker_bug_related_item`
--

DROP TABLE IF EXISTS `m_tracker_bug_related_item`;


CREATE TABLE `m_tracker_bug_related_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bugId` int(10) unsigned NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ m_tracker_bug_related_item_1` (`bugId`),
  KEY `INDEX_m_tracker_bug_related_item_2` (`type`) ,
  KEY `INDEX_m_tracker_bug_related_item_3` (`typeId`) ,
  CONSTRAINT `FK_m_tracker_bug_related_item_1` FOREIGN KEY (`bugId`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;


--
-- Table structure for table `m_tracker_component`
--

DROP TABLE IF EXISTS `m_tracker_component`;


CREATE TABLE `m_tracker_component` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `name` varchar(1000) NOT NULL,
  `userlead` varchar(45) DEFAULT NULL,
  `description` mediumtext  ,
  `createdUser` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_component_1` (`projectId`),
  KEY `FK_m_tracker_component_4` (`sAccountId`),
  KEY `FK_m_tracker_component_2` (`userlead`),
  KEY `FK_m_tracker_component_3` (`createdUser`),
  CONSTRAINT `FK_m_tracker_component_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_2` FOREIGN KEY (`userlead`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_3` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_component_4` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_tracker_related_bug`
--

DROP TABLE IF EXISTS `m_tracker_related_bug`;


CREATE TABLE `m_tracker_related_bug` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bugid` int(10) unsigned NOT NULL,
  `relatedid` int(10) unsigned NOT NULL,
  `relatetype` varchar(45) NOT NULL,
  `comment` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_related_bug_1` (`bugid`),
  KEY `FK_m_tracker_related_bug_2` (`relatedid`),
  CONSTRAINT `FK_m_tracker_related_bug_1` FOREIGN KEY (`bugid`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_related_bug_2` FOREIGN KEY (`relatedid`) REFERENCES `m_tracker_bug` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `m_tracker_version`
--

DROP TABLE IF EXISTS `m_tracker_version`;


CREATE TABLE `m_tracker_version` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `projectId` int(10) unsigned NOT NULL,
  `description` mediumtext  ,
  `duedate` date DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `prjKey` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m_tracker_version_1` (`projectId`),
  KEY `FK_m_tracker_version_3` (`sAccountId`),
  KEY `FK_m_tracker_version_2` (`createdUser`),
  CONSTRAINT `FK_m_tracker_version_1` FOREIGN KEY (`projectId`) REFERENCES `m_prj_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_version_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_m_tracker_version_3` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `s_account_theme`
--

DROP TABLE IF EXISTS `s_account_theme`;


CREATE TABLE `s_account_theme` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topMenuBg` varchar(6) DEFAULT NULL,
  `topMenuBgSelected` varchar(6) DEFAULT NULL,
  `topMenuText` varchar(6) DEFAULT NULL,
  `topMenuTextSelected` varchar(6) DEFAULT NULL,
  `vTabsheetBg` varchar(6) DEFAULT NULL,
  `vTabsheetBgSelected` varchar(6) DEFAULT NULL,
  `vTabsheetText` varchar(6) DEFAULT NULL,
  `vTabsheetTextSelected` varchar(6) DEFAULT NULL,
  `actionBtn` varchar(6) DEFAULT NULL,
  `actionBtnBorder` varchar(6) DEFAULT NULL,
  `actionBtnText` varchar(6) DEFAULT NULL,
  `optionBtn` varchar(6) DEFAULT NULL,
  `optionBtnBorder` varchar(6) DEFAULT NULL,
  `optionBtnText` varchar(6) DEFAULT NULL,
  `dangerBtn` varchar(6) DEFAULT NULL,
  `dangerBtnBorder` varchar(6) DEFAULT NULL,
  `dangerBtnText` varchar(6) DEFAULT NULL,
  `isDefault` bit(1) DEFAULT '0',
  `sAccountId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_account_theme_1_idx` (`sAccountId`),
  CONSTRAINT `FK_s_account_theme_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;


--
-- Dumping data for table `s_account_theme`
--

INSERT INTO `s_account_theme` VALUES (4, 'FFFFFF', '3F5166', '000000', 'F1F1F1', '001529', '0190FE', 'B8BECA', 'FFFFFF', '1F9DFE', 'FFFFFF', 'CCCCCC', 'FFFFFF', 'D32F2F', 'FFFFFF', 1, NULL);



--
-- Table structure for table `s_billing_plan`
--

DROP TABLE IF EXISTS `s_billing_plan`;


CREATE TABLE `s_billing_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `billingType` varchar(45) NOT NULL,
  `numUsers` int(11) NOT NULL,
  `volume` bigint(20) unsigned NOT NULL,
  `numProjects` int(11) NOT NULL,
  `pricing` double NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `shoppingUrl` varchar(400) DEFAULT NULL,
  `productPath` varchar(45) DEFAULT NULL,
  `bankTransferPath` varchar(400) DEFAULT NULL,
  `yearlyShoppingUrl` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;


--
-- Dumping data for table `s_billing_plan`
--

INSERT INTO `s_billing_plan` VALUES (6,'Community',99999999,999999999999,999999,0,NULL,NULL,NULL,NULL,NULL);

--
-- Table structure for table `s_billing_subscription`
--

DROP TABLE IF EXISTS `s_billing_subscription`;


CREATE TABLE `s_billing_subscription` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(400) DEFAULT NULL,
  `email` varchar(400)  NOT NULL,
  `billingId` int(11) NOT NULL,
  `name` varchar(400)  NOT NULL,
  `subReference` varchar(400)  NOT NULL,
  `accountId` int(11) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `status` varchar(45)  NOT NULL,
  `country` varchar(400) DEFAULT NULL,
  `city` varchar(400) DEFAULT NULL,
  `address` varchar(400) DEFAULT NULL,
  `state` varchar(400) DEFAULT NULL,
  `zipcode` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `contactName` varchar(400) DEFAULT NULL,
  `subscriptionCustomerUrl` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_billing_subscription_1_idx` (`billingId`),
  KEY `FK_s_billing_subscription_2_idx` (`accountId`),
  CONSTRAINT `FK_s_billing_subscription_1` FOREIGN KEY (`billingId`) REFERENCES `s_billing_plan` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `FK_s_billing_subscription_2` FOREIGN KEY (`accountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_billing_subscription_history`
--

DROP TABLE IF EXISTS `s_billing_subscription_history`;


CREATE TABLE `s_billing_subscription_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderId` varchar(45)  NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `subscriptionId` int(11) NOT NULL,
  `status` varchar(45)  NOT NULL,
  `expiredDate` datetime NOT NULL,
  `totalPrice` double NOT NULL,
  `productName` varchar(100)  NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ s_billing_subscription_history_1_idx` (`subscriptionId`),
  CONSTRAINT `FK_s_billing_subscription_history_1` FOREIGN KEY (`subscriptionId`) REFERENCES `s_billing_subscription` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_country`
--

DROP TABLE IF EXISTS `s_country`;


CREATE TABLE `s_country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `countryname` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ;



--
-- Table structure for table `s_customer_feedback`
--

DROP TABLE IF EXISTS `s_customer_feedback`;


CREATE TABLE `s_customer_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `reasonToLeave` text  ,
  `leaveType` int(11) DEFAULT NULL,
  `otherTool` varchar(400) DEFAULT NULL,
  `reasonToBack` text  ,
  PRIMARY KEY (`id`)
) ;



--
-- Table structure for table `s_customer_lead`
--

DROP TABLE IF EXISTS `s_customer_lead`;


CREATE TABLE `s_customer_lead` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45)  NOT NULL,
  `lastname` varchar(45)  NOT NULL,
  `company` varchar(100)  NOT NULL,
  `role` varchar(100)  NOT NULL,
  `phone` varchar(45)  NOT NULL,
  `country` varchar(100)  NOT NULL,
  `email` varchar(100)  NOT NULL,
  `registerDate` datetime DEFAULT NULL,
  `edition` varchar(45) DEFAULT NULL,
  `version` varchar(45) DEFAULT NULL,
  `valid` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;



--
-- Table structure for table `s_email_preference`
--

DROP TABLE IF EXISTS `s_email_preference`;


CREATE TABLE `s_email_preference` (
  `email` varchar(45) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `subscribe` tinyint(1) NOT NULL,
  PRIMARY KEY (`email`)
) ;



--
-- Table structure for table `s_favorite`
--

DROP TABLE IF EXISTS `s_favorite`;


CREATE TABLE `s_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `typeid` varchar(45) NOT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  `createdUser` varchar(45) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_favorite_1_idx` (`createdUser`),
  KEY `FK_s_favorite_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_favorite_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_favorite_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_live_instances`
--

DROP TABLE IF EXISTS `s_live_instances`;


CREATE TABLE `s_live_instances` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `appVersion` varchar(45)  NOT NULL,
  `javaVersion` varchar(45)  NOT NULL,
  `installedDate` datetime NOT NULL,
  `sysId` varchar(100)  NOT NULL,
  `sysProperties` varchar(100)  NOT NULL,
  `lastUpdatedDate` datetime NOT NULL,
  `numProjects` int(6) DEFAULT NULL,
  `numUsers` int(6) DEFAULT NULL,
  `edition` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;



--
-- Table structure for table `s_pro_edition_info`
--

DROP TABLE IF EXISTS `s_pro_edition_info`;


CREATE TABLE `s_pro_edition_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company` varchar(400) DEFAULT NULL,
  `email` varchar(400)  NOT NULL,
  `internalProductName` varchar(400) DEFAULT NULL,
  `name` varchar(400) DEFAULT NULL,
  `quantity` int(1) DEFAULT NULL,
  `issueDate` datetime NOT NULL,
  `type` varchar(45)  NOT NULL,
  `cost` double DEFAULT NULL,
  `orderId` varchar(100)  NOT NULL,
  `country` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `address1` varchar(400) DEFAULT NULL,
  `address2` varchar(400) DEFAULT NULL,
  `city` varchar(400) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;



--
-- Table structure for table `s_relay_email_notification`
--

DROP TABLE IF EXISTS `s_relay_email_notification`;


CREATE TABLE `s_relay_email_notification` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sAccountId` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeId` varchar(100) NOT NULL,
  `action` varchar(45) NOT NULL,
  `changeBy` varchar(45) NOT NULL,
  `changeComment` text  ,
  `extraTypeId` int(11) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_relay_email_notification_1_idx` (`sAccountId`),
  KEY `FK_s_relay_email_notification_2` (`typeId`),
  KEY `FK_s_relay_email_notification_3` (`type`),
  CONSTRAINT `FK_s_relay_email_notification_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `s_roles`
--

DROP TABLE IF EXISTS `s_roles`;


CREATE TABLE `s_roles` (
  `rolename` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isSystemRole` bit(1) DEFAULT NULL,
  `isDefault` bit(1) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_roles_1` (`sAccountId`),
  CONSTRAINT `FK_s_roles_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;

--
-- Table structure for table `s_role_permission`
--

DROP TABLE IF EXISTS `s_role_permission`;


CREATE TABLE `s_role_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roleid` int(11) NOT NULL,
  `roleVal` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_role_permission_1` (`roleid`),
  CONSTRAINT `FK_role_permission_1` FOREIGN KEY (`roleid`) REFERENCES `s_roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_save_search_result`
--

DROP TABLE IF EXISTS `s_save_search_result`;


CREATE TABLE `s_save_search_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `saveUser` varchar(45) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `queryText` text NOT NULL,
  `type` varchar(45) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  `queryName` varchar(400) NOT NULL,
  `isShared` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_save_search_result_1_idx` (`sAccountId`),
  KEY `FK_FK_s_save_search_result_2_idx` (`saveUser`),
  CONSTRAINT `FK_FK_s_save_search_result_2` FOREIGN KEY (`saveUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_save_search_result_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_table_customize_view`
--

DROP TABLE IF EXISTS `s_table_customize_view`;


CREATE TABLE `s_table_customize_view` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createdUser` varchar(45) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `viewId` varchar(45) NOT NULL,
  `viewInfo` text NOT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_table_customize_view_1_idx` (`createdUser`),
  KEY `FK_s_table_customize_view_2_idx` (`sAccountId`),
  CONSTRAINT `FK_s_table_customize_view_1` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_table_customize_view_2` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_tag`
--

DROP TABLE IF EXISTS `s_tag`;


CREATE TABLE `s_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `type` varchar(45) NOT NULL,
  `typeid` varchar(100) NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdUser` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_tag_relationship_1_idx` (`name`),
  KEY `FK_s_tag_1_idx` (`sAccountId`),
  KEY `FK_s_tag_2_idx` (`createdUser`),
  CONSTRAINT `FK_s_tag_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_tag_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_testimonial`
--

DROP TABLE IF EXISTS `s_testimonial`;


CREATE TABLE `s_testimonial` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `displayName` varchar(100) NOT NULL,
  `jobRole` varchar(100) NOT NULL,
  `company` varchar(100) DEFAULT NULL,
  `testimonial` text NOT NULL,
  `website` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;



--
-- Table structure for table `s_timeline_tracking`
--

DROP TABLE IF EXISTS `s_timeline_tracking`;


CREATE TABLE `s_timeline_tracking` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(45)  NOT NULL,
  `typeId` int(11) unsigned NOT NULL,
  `fieldval` varchar(45)  NOT NULL,
  `fieldgroup` varchar(45)  NOT NULL,
  `extratypeid` int(11) unsigned DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `forDay` date NOT NULL,
  `flag` tinyint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_s_timeline_tracking_1_idx` (`sAccountId`),
  CONSTRAINT `FK_s_timeline_tracking_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_timeline_tracking_cache`
--

DROP TABLE IF EXISTS `s_timeline_tracking_cache`;


CREATE TABLE `s_timeline_tracking_cache` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(45)  NOT NULL,
  `fieldval` varchar(45) DEFAULT NULL,
  `extratypeid` int(11) unsigned DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  `forDay` date DEFAULT NULL,
  `fieldgroup` varchar(45)  NOT NULL,
  `count` int(6) NOT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Table structure for table `s_user_account`
--

DROP TABLE IF EXISTS `s_user_account`;


CREATE TABLE `s_user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `accountId` int(11) NOT NULL,
  `isAccountOwner` bit(1) NOT NULL,
  `roleId` int(11) DEFAULT NULL,
  `registeredTime` datetime NOT NULL,
  `registerStatus` varchar(45) NOT NULL,
  `lastAccessedTime` datetime DEFAULT NULL,
  `registrationSource` varchar(45) DEFAULT NULL,
  `lastModuleVisit` varchar(45) DEFAULT NULL,
  `inviteUser` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_account_1` (`accountId`),
  KEY `FK_s_user_account_3` (`roleId`),
  KEY `FK_s_user_account_2_idx` (`username`),
  KEY `FK_s_user_account_4_idx` (`inviteUser`),
  CONSTRAINT `FK_s_user_account_1` FOREIGN KEY (`accountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_2` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_3` FOREIGN KEY (`roleId`) REFERENCES `s_roles` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_account_4` FOREIGN KEY (`inviteUser`) REFERENCES `s_user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ;


--
-- Table structure for table `s_user_permission`
--

DROP TABLE IF EXISTS `s_user_permission`;


CREATE TABLE `s_user_permission` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `module` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `hasPermission` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `s_user_permission` (`username`),
  CONSTRAINT `s_user_permission` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_user_tracking`
--

DROP TABLE IF EXISTS `s_user_tracking`;


CREATE TABLE `s_user_tracking` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `userAgent` text NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `sAccountId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_user_tracking_1` (`sAccountId`),
  KEY `FK_s_user_tracking_2` (`username`),
  CONSTRAINT `FK_s_user_tracking_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_user_tracking_2` FOREIGN KEY (`username`) REFERENCES `s_user` (`username`) ON UPDATE CASCADE
) ;



--
-- Table structure for table `s_widgets`
--

DROP TABLE IF EXISTS `s_widgets`;


CREATE TABLE `s_widgets` (
  `id` int(11) unsigned NOT NULL,
  `sAccountId` int(11) NOT NULL,
  `extraTypeId` int(11) DEFAULT NULL,
  `name` varchar(100)  NOT NULL,
  `displayText` varchar(1000)  NOT NULL,
  `queryId` int(11) DEFAULT NULL,
  `queryText` text ,
  `createdUser` varchar(45) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `lastUpdatedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_s_widgets_1_idx` (`sAccountId`),
  KEY `FK_s_widgets_2_idx` (`createdUser`),
  CONSTRAINT `FK_s_widgets_1` FOREIGN KEY (`sAccountId`) REFERENCES `s_account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_s_widgets_2` FOREIGN KEY (`createdUser`) REFERENCES `s_user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ;


-- Dump completed on 2019-01-01 23:12:34
