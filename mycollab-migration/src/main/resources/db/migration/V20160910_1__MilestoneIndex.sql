ALTER TABLE `m_prj_milestone`
ADD COLUMN `orderIndex` INT(5) UNSIGNED NULL,
ADD COLUMN `color` VARCHAR(6) NULL;

ALTER TABLE `m_prj_project`
ADD COLUMN `color` VARCHAR(6) NULL;

ALTER TABLE `m_prj_risk`
DROP COLUMN `level`,
ADD COLUMN `priority` VARCHAR(45) NULL;

UPDATE `m_prj_risk` SET `priority`='Medium' WHERE id > 0;

UPDATE `m_tracker_bug` SET `priority`='Urgent' WHERE `priority`='Blocker' AND id > 0;
UPDATE `m_tracker_bug` SET `priority`='High' WHERE `priority`='Critical' AND id > 0;
UPDATE `m_tracker_bug` SET `priority`='Medium' WHERE `priority`='Major' AND id > 0;
UPDATE `m_tracker_bug` SET `priority`='Low' WHERE `priority`='Minor' AND id > 0;
UPDATE `m_tracker_bug` SET `priority`='None' WHERE `priority`='Trivial' AND id > 0;

DELETE FROM `s_table_customize_view` WHERE id > 0;