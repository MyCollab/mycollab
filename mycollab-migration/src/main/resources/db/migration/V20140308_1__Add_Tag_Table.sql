CREATE TABLE `s_tag` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `sAccountId` INT(11) NOT NULL,
  `lowercasename` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_s_tag_1_idx` (`sAccountId` ASC),
  CONSTRAINT `FK_s_tag_1`
    FOREIGN KEY (`sAccountId`)
    REFERENCES `s_account` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
CREATE TABLE `s_tag_relationship` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `tagId` INT(11) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `typerid` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK_s_tag_relationship_1_idx` (`tagId` ASC),
  CONSTRAINT `FK_s_tag_relationship_1`
    FOREIGN KEY (`tagId`)
    REFERENCES `s_tag` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);    