USE `anslutningsplattform`;

DROP TABLE IF EXISTS `tjanstekomponent_nat`;

ALTER TABLE `tjanstekomponent`
ADD COLUMN `nat_id` VARCHAR(255) NULL DEFAULT NULL AFTER `ipadress`,
ADD INDEX `FK_68hng8po96p0anxb73h5yor3y` (`nat_id` ASC);
ALTER TABLE `tjanstekomponent`
ADD CONSTRAINT `FK_68hng8po96p0anxb73h5yor3y`
  FOREIGN KEY (`nat_id`)
  REFERENCES `nat` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
