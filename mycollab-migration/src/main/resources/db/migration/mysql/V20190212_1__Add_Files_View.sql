ALTER TABLE `m_prj_customize_view`
CHANGE COLUMN `displayStandup` `displayFile` BIT(1) NOT NULL ;

ALTER TABLE `s_live_instances`
CHANGE COLUMN `installedDate` `installedDate` DATE NOT NULL ;