USE `anslutningsplattform`;

ALTER TABLE `tjanstekomponent`
CHANGE COLUMN `ipadress` `producent_ipadress` VARCHAR(255) NULL DEFAULT NULL,
ADD COLUMN `producent_dns_namn` VARCHAR(255) NULL DEFAULT NULL AFTER `producent_ipadress`,
ADD COLUMN `konsument_ipadress` VARCHAR(255) NULL DEFAULT NULL AFTER `producent_dns_namn`;
