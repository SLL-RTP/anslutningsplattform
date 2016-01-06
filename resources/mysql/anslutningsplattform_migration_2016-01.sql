USE `anslutningsplattform`;

ALTER TABLE `tjanstekomponent`
CHANGE COLUMN `ipadress` `producent_ipadress` VARCHAR(255) NULL DEFAULT NULL,
ADD COLUMN `producent_dns_namn` VARCHAR(255) NULL DEFAULT NULL AFTER `producent_ipadress`,
ADD COLUMN `konsument_ipadress` VARCHAR(255) NULL DEFAULT NULL AFTER `producent_dns_namn`,
ADD COLUMN `huvudansvarig_kontakt_other_info` varchar(255) NULL DEFAULT NULL AFTER `huvudansvarig_kontakt_namn`,
ADD COLUMN `teknisk_kontakt_other_info` varchar(255) NULL DEFAULT NULL AFTER `teknisk_kontakt_namn`;

ALTER TABLE `personkontakt`
ADD COLUMN `other_info` VARCHAR(255) NULL DEFAULT NULL AFTER `namn`;

ALTER TABLE `konsumentbestallning`
ADD COLUMN `namn_pa_etjanst` VARCHAR(255) NULL DEFAULT NULL AFTER `version`;
