CREATE TABLE `m_prj_ticket_hierarchy` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `parentId` INT(10) NOT NULL,
  `parentType` VARCHAR(45) NOT NULL,
  `ticketId` INT(10) NOT NULL,
  `ticketType` VARCHAR(45) NOT NULL,
  `projectId` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_1_idx` (`projectId` ASC) VISIBLE,
  CONSTRAINT `FK_m_prj_ticket_hierarchy`
    FOREIGN KEY (`projectId`)
    REFERENCES `m_prj_project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

INSERT INTO m_prj_ticket_hierarchy(parentId, parentType, ticketId, ticketType, projectId)
    SELECT m_prj_task.parentTaskId as parentId, 'Project-Task' AS parentType, m_prj_task.id AS ticketId, 'Project-Task' AS ticketType,
        m_prj_task.projectId AS projectId
    FROM m_prj_task WHERE m_prj_task.parentTaskId IS NOT NULL;

ALTER TABLE `m_prj_task`
DROP FOREIGN KEY `FK_m_prj_task_6`;
ALTER TABLE `m_prj_task`
DROP COLUMN `parentTaskId`,
DROP INDEX `FK_m_prj_task_6_idx` ;