CREATE TABLE `m_prj_customize_view` (
  `id` INT(10) NOT NULL,
  `projectId` INT(10) UNSIGNED NOT NULL,
  `displayMessage` BIT(1) NOT NULL,
  `displayMilestone` BIT(1) NOT NULL,
  `displayTask` BIT(1) NOT NULL,
  `displayBug` BIT(1) NOT NULL,
  `displayStandup` BIT(1) NOT NULL,
  `displayProblem` BIT(1) NOT NULL,
  `displayRisk` BIT(1) NOT NULL,
  `displayTimeLogging` BIT(1) NOT NULL,
  `displayPage` BIT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_m_prj_customize_view_1_idx` (`projectId` ASC),
  CONSTRAINT `FK_m_prj_customize_view_1`
    FOREIGN KEY (`projectId`)
    REFERENCES `m_prj_project` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);