ALTER TABLE `s_activitystream`
ADD INDEX `FK_m_crm_activitystream_5` (`typeId`(100) ASC);

ALTER TABLE `m_comment`
CHANGE COLUMN `typeId` `typeId` TEXT CHARACTER SET 'utf8mb4' NULL DEFAULT NULL ,
DROP INDEX `INDEX_m_comment_3` ,
ADD INDEX `INDEX_m_comment_3` (`typeId`(100) ASC);