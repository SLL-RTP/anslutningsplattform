use anslutningsplattform;

-- Create syntax for TABLE 'user'
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `datum_skapad` datetime NOT NULL,
  `datum_uppdaterad` datetime DEFAULT NULL,
  `epost` varchar(255) NOT NULL,
  `namn` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `telefon_nummer` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=INNODB AUTO_INCREMENT=3;

-- Create syntax for TABLE 'logisk_adress'
CREATE TABLE `logisk_adress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `hsa_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t0hh8k0lm3gmwpkueja279g16` (`hsa_id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'role'
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=INNODB AUTO_INCREMENT=3;

-- Create syntax for TABLE 'user_permissions'
CREATE TABLE `user_permissions` (
  `user_id` bigint(20) DEFAULT NULL,
  `permissions_string` varchar(255) DEFAULT NULL,
  KEY `FK_525n7wn9ejoa648m26bm4rhtt` (`user_id`),
  CONSTRAINT `FK_525n7wn9ejoa648m26bm4rhtt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'user_roles'
CREATE TABLE `user_roles` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_5q4rc4fh1on6567qk69uesvyf` (`role_id`),
  CONSTRAINT `FK_5q4rc4fh1on6567qk69uesvyf` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_g1uebn6mqk9qiaw45vnacmyo2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'role_permissions'
CREATE TABLE `role_permissions` (
  `role_id` bigint(20) DEFAULT NULL,
  `permissions_string` varchar(255) DEFAULT NULL,
  KEY `FK_d4atqq8ege1sij0316vh2mxfu` (`role_id`),
  CONSTRAINT `FK_d4atqq8ege1sij0316vh2mxfu` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'tjanste_komponent'
CREATE TABLE `tjanste_komponent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `funktions_brevlada_epost` varchar(255) NOT NULL,
  `funktions_brevlada_telefon` varchar(255) NOT NULL,
  `namn` varchar(255) NOT NULL,
  `hsa_id` varchar(255) NOT NULL,
  `ipadress` varchar(255) DEFAULT NULL,
  `teknisk_kontakt_epost` varchar(255) NOT NULL,
  `teknisk_kontakt_namn` varchar(255) NOT NULL,
  `teknisk_kontakt_telefon` varchar(255) DEFAULT NULL,
  `huvud_ansvarig_epost` varchar(255) NOT NULL,
  `huvud_ansvarig_namn` varchar(255) NOT NULL,
  `huvud_ansvarig_telefon` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_obytuo2gifsdm7fj3wtc8h4nv` (`hsa_id`),
  KEY `FK_3eob3upykvoy0nc3r7ac6gpmn` (`user_id`),
  CONSTRAINT `FK_3eob3upykvoy0nc3r7ac6gpmn` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3;

-- Create syntax for TABLE 'producent_bestallning'
CREATE TABLE `producent_bestallning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `miljo` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `tjanste_komponent_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ojg3jjfi31xi112psodvnnc32` (`tjanste_komponent_id`),
  CONSTRAINT `FK_ojg3jjfi31xi112psodvnnc32` FOREIGN KEY (`tjanste_komponent_id`) REFERENCES `tjanste_komponent` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3;

-- Create syntax for TABLE 'konsument_bestallning'
CREATE TABLE `konsument_bestallning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `miljo` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `tjanste_komponent_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lp1rq3vvn2npf8eap20udakr8` (`tjanste_komponent_id`),
  CONSTRAINT `FK_lp1rq3vvn2npf8eap20udakr8` FOREIGN KEY (`tjanste_komponent_id`) REFERENCES `tjanste_komponent` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'bestallnings_historik'
CREATE TABLE `bestallnings_historik` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `datum` datetime NOT NULL,
  `konsument_bestallning_id` bigint(20),
  `producent_bestallning_id` bigint(20),
  `senast_uppdaterad_av` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_3l3eaox109t6d4bi3rcl7es2r` (`konsument_bestallning_id`),
  KEY `FK_racfs8rki8hdk1bc2ej60asrn` (`producent_bestallning_id`),
  CONSTRAINT `FK_racfs8rki8hdk1bc2ej60asrn` FOREIGN KEY (`producent_bestallning_id`) REFERENCES `producent_bestallning` (`id`),
  CONSTRAINT `FK_3l3eaox109t6d4bi3rcl7es2r` FOREIGN KEY (`konsument_bestallning_id`) REFERENCES `konsument_bestallning` (`id`)
) ENGINE=INNODB;


-- Create syntax for TABLE 'konsument_anslutning'
CREATE TABLE `konsument_anslutning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `konsument_bestallning_id` bigint(20) NOT NULL,
  `tjanste_kontrakt` varchar(255) NOT NULL,
  `valid_from_time` datetime NOT NULL,
  `valid_to_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hwsuphldbfy7pp0tkq3p0n02b` (`konsument_bestallning_id`),
  CONSTRAINT `FK_hwsuphldbfy7pp0tkq3p0n02b` FOREIGN KEY (`konsument_bestallning_id`) REFERENCES `konsument_bestallning` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'konsument_anslutning_logisk_adresser'
CREATE TABLE `konsument_anslutning_logisk_adresser` (
  `konsument_anslutning_id` bigint(20) NOT NULL,
  `logisk_adress_id` bigint(20) NOT NULL,
  PRIMARY KEY (`konsument_anslutning_id`,`logisk_adress_id`),
  KEY `FK_osnewlmpswncnbxxnvdo1o0bf` (`logisk_adress_id`),
  CONSTRAINT `FK_h95281jv3la0l6n7rlb37ho4a` FOREIGN KEY (`konsument_anslutning_id`) REFERENCES `konsument_anslutning` (`id`),
  CONSTRAINT `FK_osnewlmpswncnbxxnvdo1o0bf` FOREIGN KEY (`logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=INNODB;


-- Create syntax for TABLE 'producent_anslutning'
CREATE TABLE `producent_anslutning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `producent_bestallning_id` bigint(20) NOT NULL,
  `riv_ta_profile` varchar(255) NOT NULL,
  `tjanste_kontrakt` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `valid_from_time` datetime NOT NULL,
  `valid_to_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_e2thltb8nn1nev4jgjcs3m6k3` (`producent_bestallning_id`),
  CONSTRAINT `FK_e2thltb8nn1nev4jgjcs3m6k3` FOREIGN KEY (`producent_bestallning_id`) REFERENCES `producent_bestallning` (`id`)
) ENGINE=INNODB;

-- Create syntax for TABLE 'producent_anslutning_logisk_adresser'
CREATE TABLE `producent_anslutning_logisk_adresser` (
  `producent_anslutning_id` bigint(20) NOT NULL,
  `logisk_adress_id` bigint(20) NOT NULL,
  PRIMARY KEY (`producent_anslutning_id`,`logisk_adress_id`),
  KEY `FK_mlmhp7ww0p5ma74gscacl19o0` (`logisk_adress_id`),
  CONSTRAINT `FK_8ntf7a0xavslt72uwxs2btvxk` FOREIGN KEY (`producent_anslutning_id`) REFERENCES `producent_anslutning` (`id`),
  CONSTRAINT `FK_mlmhp7ww0p5ma74gscacl19o0` FOREIGN KEY (`logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=INNODB;


-- Create syntax for TABLE 'drift_miljo'
CREATE TABLE `drift_miljo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `namn` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`namn`)
) ENGINE=INNODB;
