ALTER TABLE `m_prj_risk`
ADD COLUMN `originalEstimate` DOUBLE NULL;

ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `estimateTime` `originalEstimate` DOUBLE NULL DEFAULT NULL;

ALTER TABLE `m_tracker_bug`
CHANGE COLUMN `estimateRemainTime` `remainEstimate` DOUBLE NULL DEFAULT NULL ;