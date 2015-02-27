ALTER TABLE `s_tag_relationship`
DROP FOREIGN KEY `FK_s_tag_relationship_1`;
ALTER TABLE `s_tag_relationship`
CHANGE COLUMN `tagId` `name` VARCHAR(100) CHARACTER SET 'utf8mb4' NOT NULL ;

DROP TABLE `s_tag`;

ALTER TABLE `s_tag_relationship`
RENAME TO  `s_tag` ;

ALTER TABLE `s_tag`
CHANGE COLUMN `typerid` `typeid` VARCHAR(100) NOT NULL ;

ALTER TABLE `s_tag`
ADD COLUMN `sAccountId` INT(11) NOT NULL,
ADD INDEX `FK_s_tag_1_idx` (`sAccountId` ASC);
ALTER TABLE `s_tag`
ADD CONSTRAINT `FK_s_tag_1`
  FOREIGN KEY (`sAccountId`)
  REFERENCES `s_account` (`id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `s_tag`
ADD COLUMN `extraTypeId` INT(11) NULL;