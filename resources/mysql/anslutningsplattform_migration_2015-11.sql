USE `anslutningsplattform`;
-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: rtp-t-db01.i.centrera.se    Database: anslutningsplattform
-- ------------------------------------------------------
-- Server version	5.1.73

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Just drop the old table`bestallning_nat`
--

DROP TABLE IF EXISTS `bestallning_nat`;

--
-- Table structure for table `bestallning`
--

DROP TABLE IF EXISTS `bestallning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bestallning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `bestallare_id` bigint(20) NOT NULL,
  `bestallare_roll` varchar(255) DEFAULT NULL,
  `driftmiljo_id` varchar(255) NOT NULL,
  `other_info` MEDIUMTEXT DEFAULT NULL,
  `producentbestallning_id` bigint(20) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_p1chcfduor1708ru1rbur44ex` (`bestallare_id`),
  KEY `FK_1o75o372k0p8fovb63umnrc81` (`driftmiljo_id`),
  KEY `FK_rmpt7srpkcwsy5nvikqi9at4c` (`producentbestallning_id`),
  CONSTRAINT `FK_rmpt7srpkcwsy5nvikqi9at4c` FOREIGN KEY (`producentbestallning_id`) REFERENCES `producentbestallning` (`id`),
  CONSTRAINT `FK_1o75o372k0p8fovb63umnrc81` FOREIGN KEY (`driftmiljo_id`) REFERENCES `driftmiljo` (`id`),
  CONSTRAINT `FK_p1chcfduor1708ru1rbur44ex` FOREIGN KEY (`bestallare_id`) REFERENCES `personkontakt` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bestallning_konsumentbestallning`
--

DROP TABLE IF EXISTS `bestallning_konsumentbestallning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bestallning_konsumentbestallning` (
  `bestallning_konsumentbestallningar_id` bigint(20) DEFAULT NULL,
  `konsumentbestallning_id` bigint(20) DEFAULT NULL,
  KEY `FK_bwccnjtdqqy52rr6ku1p28uhn` (`konsumentbestallning_id`),
  KEY `FK_c1gv5dhf9n1q4rbiv7oykaw49` (`bestallning_konsumentbestallningar_id`),
  CONSTRAINT `FK_c1gv5dhf9n1q4rbiv7oykaw49` FOREIGN KEY (`bestallning_konsumentbestallningar_id`) REFERENCES `bestallning` (`id`),
  CONSTRAINT `FK_bwccnjtdqqy52rr6ku1p28uhn` FOREIGN KEY (`konsumentbestallning_id`) REFERENCES `konsumentbestallning` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `driftmiljo`
--

DROP TABLE IF EXISTS `driftmiljo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driftmiljo` (
  `id` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  `namn` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `funktionkontakt`
--

DROP TABLE IF EXISTS `funktionkontakt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `funktionkontakt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epost` varchar(255) NOT NULL,
  `telefon` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hibernate_unique_key`
--

DROP TABLE IF EXISTS `hibernate_unique_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_unique_key` (
  `next_hi` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `konsumentanslutning`
--

DROP TABLE IF EXISTS `konsumentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konsumentanslutning` (
  `id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `giltig_fran_tid` datetime NOT NULL,
  `giltig_till_tid` datetime NOT NULL,
  `tjanstekontrakt_major_version` int(11) NOT NULL,
  `tjanstekontrakt_minor_version` int(11) NOT NULL,
  `tjanstekontrakt_namnrymd` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `konsumentanslutning_logisk_adress`
--

DROP TABLE IF EXISTS `konsumentanslutning_logisk_adress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konsumentanslutning_logisk_adress` (
  `konsumentanslutning_id` bigint(20) NOT NULL,
  `ny_logisk_adress_id` bigint(20) DEFAULT NULL,
  KEY `FK_thiyyhhohngeeoaa0dupkup9y` (`ny_logisk_adress_id`),
  CONSTRAINT `FK_thiyyhhohngeeoaa0dupkup9y` FOREIGN KEY (`ny_logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `konsumentbestallning`
--

DROP TABLE IF EXISTS `konsumentbestallning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konsumentbestallning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `tjanstekomponent_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ipdcd8itip119wxanacocy8ai` (`tjanstekomponent_id`),
  CONSTRAINT `FK_ipdcd8itip119wxanacocy8ai` FOREIGN KEY (`tjanstekomponent_id`) REFERENCES `tjanstekomponent` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `konsumentbestallning_konsumentanslutning`
--

DROP TABLE IF EXISTS `konsumentbestallning_konsumentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konsumentbestallning_konsumentanslutning` (
  `konsumentbestallning_konsumentanslutningar_id` bigint(20) DEFAULT NULL,
  `konsumentanslutning_id` bigint(20) DEFAULT NULL,
  KEY `FK_ikkl2sq1d45fnajb7ts6343f1` (`konsumentbestallning_konsumentanslutningar_id`),
  CONSTRAINT `FK_ikkl2sq1d45fnajb7ts6343f1` FOREIGN KEY (`konsumentbestallning_konsumentanslutningar_id`) REFERENCES `konsumentbestallning` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `konsumentbestallning_uppdaterad_konsumentanslutning`
--

DROP TABLE IF EXISTS `konsumentbestallning_uppdaterad_konsumentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `konsumentbestallning_uppdaterad_konsumentanslutning` (
  `konsumentbestallning_uppdaterad_konsumentanslutningar_id` bigint(20) DEFAULT NULL,
  `uppdaterad_konsumentanslutning_id` bigint(20) DEFAULT NULL,
  KEY `FK_jw4rdbbs70ag8iyht1a1sbgt9` (`uppdaterad_konsumentanslutning_id`),
  KEY `FK_b269u3pj9axutppqada09j2xs` (`konsumentbestallning_uppdaterad_konsumentanslutningar_id`),
  CONSTRAINT `FK_b269u3pj9axutppqada09j2xs` FOREIGN KEY (`konsumentbestallning_uppdaterad_konsumentanslutningar_id`) REFERENCES `konsumentbestallning` (`id`),
  CONSTRAINT `FK_jw4rdbbs70ag8iyht1a1sbgt9` FOREIGN KEY (`uppdaterad_konsumentanslutning_id`) REFERENCES `uppdaterad_konsumentanslutning` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `logisk_adress`
--

DROP TABLE IF EXISTS `logisk_adress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `logisk_adress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `hsa_id` varchar(255) NOT NULL,
  `namn` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t0hh8k0lm3gmwpkueja279g16` (`hsa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `nat`
--

DROP TABLE IF EXISTS `nat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nat` (
  `id` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  `namn` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `personkontakt`
--
-- NOT TOUCHING personkontakt in Nov 2015 update
--

-- DROP TABLE IF EXISTS `personkontakt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
/*
CREATE TABLE `personkontakt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `epost` varchar(255) NOT NULL,
  `hsa_id` varchar(255) DEFAULT NULL,
  `namn` varchar(255) NOT NULL,
  `telefon` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producentanslutning`
--

DROP TABLE IF EXISTS `producentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producentanslutning` (
  `id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `giltig_fran_tid` datetime NOT NULL,
  `giltig_till_tid` datetime NOT NULL,
  `rivta_profil` varchar(255) NOT NULL,
  `tjanstekontrakt_major_version` int(11) NOT NULL,
  `tjanstekontrakt_minor_version` int(11) NOT NULL,
  `tjanstekontrakt_namnrymd` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producentanslutning_logisk_adress`
--

DROP TABLE IF EXISTS `producentanslutning_logisk_adress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producentanslutning_logisk_adress` (
  `producentanslutning_id` bigint(20) NOT NULL,
  `ny_logisk_adress_id` bigint(20) DEFAULT NULL,
  KEY `FK_hy9ednsc537efvjvdi8r32lnv` (`ny_logisk_adress_id`),
  CONSTRAINT `FK_hy9ednsc537efvjvdi8r32lnv` FOREIGN KEY (`ny_logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producentbestallning`
--

DROP TABLE IF EXISTS `producentbestallning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producentbestallning` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `namn_pa_etjanst` varchar(255) DEFAULT NULL,
  `tjanstekomponent_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pt1cfj8tcs1nr1elo8nblviap` (`tjanstekomponent_id`),
  CONSTRAINT `FK_pt1cfj8tcs1nr1elo8nblviap` FOREIGN KEY (`tjanstekomponent_id`) REFERENCES `tjanstekomponent` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producentbestallning_producentanslutning`
--

DROP TABLE IF EXISTS `producentbestallning_producentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producentbestallning_producentanslutning` (
  `producentbestallning_producentanslutningar_id` bigint(20) DEFAULT NULL,
  `producentanslutning_id` bigint(20) DEFAULT NULL,
  KEY `FK_9ok28d0qtg5kyj0kkb4g6tdss` (`producentbestallning_producentanslutningar_id`),
  CONSTRAINT `FK_9ok28d0qtg5kyj0kkb4g6tdss` FOREIGN KEY (`producentbestallning_producentanslutningar_id`) REFERENCES `producentbestallning` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `producentbestallning_uppdaterad_producentanslutning`
--

DROP TABLE IF EXISTS `producentbestallning_uppdaterad_producentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producentbestallning_uppdaterad_producentanslutning` (
  `producentbestallning_uppdaterad_producentanslutningar_id` bigint(20) DEFAULT NULL,
  `uppdaterad_producentanslutning_id` bigint(20) DEFAULT NULL,
  KEY `FK_k8644roj2eyybojsc62ejkouy` (`uppdaterad_producentanslutning_id`),
  KEY `FK_6jp88iy2vrq2i3cd2fra0fqmu` (`producentbestallning_uppdaterad_producentanslutningar_id`),
  CONSTRAINT `FK_6jp88iy2vrq2i3cd2fra0fqmu` FOREIGN KEY (`producentbestallning_uppdaterad_producentanslutningar_id`) REFERENCES `producentbestallning` (`id`),
  CONSTRAINT `FK_k8644roj2eyybojsc62ejkouy` FOREIGN KEY (`uppdaterad_producentanslutning_id`) REFERENCES `uppdaterad_producentanslutning` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permissions`
--

DROP TABLE IF EXISTS `role_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permissions` (
  `role_id` bigint(20) DEFAULT NULL,
  `permissions_string` varchar(255) DEFAULT NULL,
  KEY `FK_d4atqq8ege1sij0316vh2mxfu` (`role_id`),
  CONSTRAINT `FK_d4atqq8ege1sij0316vh2mxfu` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tjanstekomponent`
--
-- NOT TOUCHING tjanstekomponent in Nov 2015 update
--

-- DROP TABLE IF EXISTS `tjanstekomponent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
/*
CREATE TABLE `tjanstekomponent` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `beskrivning` varchar(255) NOT NULL,
  `hsa_id` varchar(255) NOT NULL,
  `huvudansvarig_kontakt_epost` varchar(255) DEFAULT NULL,
  `huvudansvarig_kontakt_hsa_id` varchar(255) DEFAULT NULL,
  `huvudansvarig_kontakt_namn` varchar(255) DEFAULT NULL,
  `huvudansvarig_kontakt_telefon` varchar(255) DEFAULT NULL,
  `ipadress` varchar(255) DEFAULT NULL,
  `organisation` varchar(255) DEFAULT NULL,
  `ping_for_configurationurl` varchar(255) DEFAULT NULL,
  `teknisk_kontakt_epost` varchar(255) DEFAULT NULL,
  `teknisk_kontakt_hsa_id` varchar(255) DEFAULT NULL,
  `teknisk_kontakt_namn` varchar(255) DEFAULT NULL,
  `teknisk_kontakt_telefon` varchar(255) DEFAULT NULL,
  `teknisk_supportkontakt_epost` varchar(255) DEFAULT NULL,
  `teknisk_supportkontakt_telefon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hve38rq1bac4udieiyc44ahyl` (`hsa_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tjanstekomponent_driftmiljo`
--

DROP TABLE IF EXISTS `tjanstekomponent_driftmiljo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tjanstekomponent_driftmiljo` (
  `tjanstekomponent_driftmiljoer_id` bigint(20) DEFAULT NULL,
  `driftmiljo_id` varchar(255) DEFAULT NULL,
  KEY `FK_73v0bcg0siyrupmc9sx8xknf6` (`driftmiljo_id`),
  KEY `FK_clrm55eb6pj52u1u3c3vf8bjm` (`tjanstekomponent_driftmiljoer_id`),
  CONSTRAINT `FK_clrm55eb6pj52u1u3c3vf8bjm` FOREIGN KEY (`tjanstekomponent_driftmiljoer_id`) REFERENCES `tjanstekomponent` (`id`),
  CONSTRAINT `FK_73v0bcg0siyrupmc9sx8xknf6` FOREIGN KEY (`driftmiljo_id`) REFERENCES `driftmiljo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tjanstekomponent_nat`
--

DROP TABLE IF EXISTS `tjanstekomponent_nat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tjanstekomponent_nat` (
  `tjanstekomponent_nat_id` bigint(20) NOT NULL,
  `nat_id` varchar(255) DEFAULT NULL,
  KEY `FK_os5ybv4d17hr8raghl9irffb` (`nat_id`),
  KEY `FK_1bgrl8sldeev5ntxb2pqren71` (`tjanstekomponent_nat_id`),
  CONSTRAINT `FK_1bgrl8sldeev5ntxb2pqren71` FOREIGN KEY (`tjanstekomponent_nat_id`) REFERENCES `tjanstekomponent` (`id`),
  CONSTRAINT `FK_os5ybv4d17hr8raghl9irffb` FOREIGN KEY (`nat_id`) REFERENCES `nat` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uppdaterad_konsumentanslutning`
--

DROP TABLE IF EXISTS `uppdaterad_konsumentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uppdaterad_konsumentanslutning` (
  `id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `giltig_fran_tid` datetime NOT NULL,
  `giltig_till_tid` datetime NOT NULL,
  `tjanstekontrakt_major_version` int(11) NOT NULL,
  `tjanstekontrakt_minor_version` int(11) NOT NULL,
  `tjanstekontrakt_namnrymd` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uppdaterad_konsumentanslutning_logisk_adress`
--

DROP TABLE IF EXISTS `uppdaterad_konsumentanslutning_logisk_adress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uppdaterad_konsumentanslutning_logisk_adress` (
  `uppdaterad_konsumentanslutning_id` bigint(20) NOT NULL,
  `befintlig_logisk_adress_id` bigint(20) DEFAULT NULL,
  `borttagen_logisk_adress_id` bigint(20) DEFAULT NULL,
  KEY `FK_4fiw05jcy20w5lvpnpycf48mg` (`befintlig_logisk_adress_id`),
  KEY `FK_3po2edgdjxpqeu280eq20w3qe` (`uppdaterad_konsumentanslutning_id`),
  KEY `FK_421jmuokxw7fe3ifk5lag88wl` (`borttagen_logisk_adress_id`),
  CONSTRAINT `FK_421jmuokxw7fe3ifk5lag88wl` FOREIGN KEY (`borttagen_logisk_adress_id`) REFERENCES `logisk_adress` (`id`),
  CONSTRAINT `FK_3po2edgdjxpqeu280eq20w3qe` FOREIGN KEY (`uppdaterad_konsumentanslutning_id`) REFERENCES `uppdaterad_konsumentanslutning` (`id`),
  CONSTRAINT `FK_4fiw05jcy20w5lvpnpycf48mg` FOREIGN KEY (`befintlig_logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uppdaterad_producentanslutning`
--

DROP TABLE IF EXISTS `uppdaterad_producentanslutning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uppdaterad_producentanslutning` (
  `id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `giltig_fran_tid` datetime NOT NULL,
  `giltig_till_tid` datetime NOT NULL,
  `rivta_profil` varchar(255) NOT NULL,
  `tjanstekontrakt_major_version` int(11) NOT NULL,
  `tjanstekontrakt_minor_version` int(11) NOT NULL,
  `tjanstekontrakt_namnrymd` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `tidigare_rivta_profil` varchar(255) DEFAULT NULL,
  `tidigare_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uppdaterad_producentanslutning_logisk_adress`
--

DROP TABLE IF EXISTS `uppdaterad_producentanslutning_logisk_adress`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uppdaterad_producentanslutning_logisk_adress` (
  `uppdaterad_producentanslutning_id` bigint(20) NOT NULL,
  `befintlig_logisk_adress_id` bigint(20) DEFAULT NULL,
  `borttagen_logisk_adress_id` bigint(20) DEFAULT NULL,
  KEY `FK_kur93b91tc0n30kx2ce4mraaf` (`befintlig_logisk_adress_id`),
  KEY `FK_4mdt2udan62gn754opwscx1n8` (`uppdaterad_producentanslutning_id`),
  KEY `FK_6i51ot9khxu3pgp7iidcs9mje` (`borttagen_logisk_adress_id`),
  CONSTRAINT `FK_6i51ot9khxu3pgp7iidcs9mje` FOREIGN KEY (`borttagen_logisk_adress_id`) REFERENCES `logisk_adress` (`id`),
  CONSTRAINT `FK_4mdt2udan62gn754opwscx1n8` FOREIGN KEY (`uppdaterad_producentanslutning_id`) REFERENCES `uppdaterad_producentanslutning` (`id`),
  CONSTRAINT `FK_kur93b91tc0n30kx2ce4mraaf` FOREIGN KEY (`befintlig_logisk_adress_id`) REFERENCES `logisk_adress` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `datum_skapad` varchar(255) NOT NULL,
  `epost` varchar(255) NOT NULL,
  `namn` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_permissions`
--

DROP TABLE IF EXISTS `user_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_permissions` (
  `user_id` bigint(20) DEFAULT NULL,
  `permissions_string` varchar(255) DEFAULT NULL,
  KEY `FK_525n7wn9ejoa648m26bm4rhtt` (`user_id`),
  CONSTRAINT `FK_525n7wn9ejoa648m26bm4rhtt` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_roles` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_5q4rc4fh1on6567qk69uesvyf` (`role_id`),
  CONSTRAINT `FK_5q4rc4fh1on6567qk69uesvyf` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_g1uebn6mqk9qiaw45vnacmyo2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'anslutningsplattform'
--

--
-- Dumping routines for database 'anslutningsplattform'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-04 20:47:05

INSERT INTO `anslutningsplattform`.`hibernate_unique_key` (`next_hi`) VALUES (1);
