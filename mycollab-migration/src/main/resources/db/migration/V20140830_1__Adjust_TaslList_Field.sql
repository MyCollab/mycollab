ALTER TABLE `m_prj_task_list` 
DROP FOREIGN KEY `PK_m_prj_task_list_4`;
ALTER TABLE `m_prj_task_list` 
CHANGE COLUMN `sAccountId` `sAccountId` INT(11) NOT NULL ;
ALTER TABLE `m_prj_task_list` 
ADD CONSTRAINT `PK_m_prj_task_list_4`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;