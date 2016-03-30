ALTER TABLE `m_prj_time_logging` ADD COLUMN `isOvertime` BIT(1) NULL;
ALTER TABLE `s_billing_plan`
DROP COLUMN `hasTimeTracking`,
DROP COLUMN `hasStandupMeetingEnable`,
DROP COLUMN `hasBugEnable`;