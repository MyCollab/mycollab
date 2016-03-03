CREATE TABLE `m_prj_kanban_board` (
  `id` INT(11) UNSIGNED NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `projectId` INT(10) UNSIGNED NOT NULL,
  `sAccountId` INT(11) NOT NULL,
  `lead` VARCHAR(45) COLLATE utf8mb4_unicode_ci NULL,
  `createdTime` DATETIME NOT NULL,
  `lastUpdatedTime` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_m_prj_kanban_board_1_idx` (`projectId` ASC),
  INDEX `FK_m_prj_kanban_board_2_idx` (`sAccountId` ASC),
  INDEX `FK_m_prj_kanban_board_3_idx` (`lead` ASC),
  CONSTRAINT `FK_m_prj_kanban_board_1`
    FOREIGN KEY (`projectId`)
    REFERENCES `m_prj_project` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_kanban_board_2`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_m_prj_kanban_board_3`
    FOREIGN KEY (`lead`)
    REFERENCES `s_user` (`username`)
    ON DELETE SET NULL
    ON UPDATE CASCADE);
ALTER TABLE `m_prj_project`
DROP FOREIGN KEY `FK_m_project_project_1`;
ALTER TABLE `m_prj_project`
CHANGE COLUMN `account` `accountId` INT(10) UNSIGNED NULL DEFAULT NULL ;
ALTER TABLE `m_prj_project`
ADD CONSTRAINT `FK_m_project_project_1`
  FOREIGN KEY (`accountId`)
  REFERENCES `m_crm_account` (`id`)
  ON DELETE SET NULL
  ON UPDATE CASCADE;