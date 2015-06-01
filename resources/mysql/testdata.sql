--
-- Dumpning av testdata
--

use anslutningsplattform;

INSERT INTO `role` (`id`, `version`, `name`) VALUES
(3, 0, 'ADMINISTRATÖR'),
(4, 0, 'TJÄNSTEKOMPONENTANSVARIG');

INSERT INTO `role_permissions` (`role_id`, `permissions_string`) VALUES
(3, 'User:*'),
(3, 'Role:*'),
(3, 'Tjanstekomponent:index'),
(3, 'BestallningsHistorik:index'),
(3, 'LogiskAdress:index'),
(4, 'ProdcentAnslutning:*'),
(4, 'KonsumentAnslutning:*'),
(4, 'LogiskAdress:*'),
(4, 'Tjanstekomponent:*'),
(4, 'BestallningsHistorik:*');

INSERT INTO `user` (`id`, `version`, `datum_skapad`, `datum_uppdaterad`, `epost`, `namn`, `password_hash`, `telefon_nummer`, `username`) VALUES
(3, 0, '2014-11-21 14:47:55', NULL, 'admin@lorumipsum.nu', 'Agda Andersson', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', NULL, 'admin'),
(4, 0, '2014-11-21 14:47:55', NULL, 'user@lorumipsum.nu', 'Jöns Jönsson', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', NULL, 'user');


INSERT INTO `user_roles` (`role_id`, `user_id`) VALUES
(3, 3),
(4, 4);

INSERT INTO `tjanste_komponent` (`id`, `version`, `funktions_brevlada_epost`, `funktions_brevlada_telefon`, `hsa_id`, `ipadress`, `teknisk_kontakt_epost`, `teknisk_kontakt_namn`, `teknisk_kontakt_telefon`, `user_id`, `namn`, `huvud_ansvarig_epost`, `huvud_ansvarig_namn`, `huvud_ansvarig_telefon`) VALUES
(3, 0, 'funktionsbrevladan@lorumipsum.nu', '0987654321', 'HSASERVICES-123Q', NULL, 'kontakten@lorumipsum.nu', 'Tolvan Tolvansson', '1234567890', 4, 'HSASERVICES-123Q namn', 'mail@example.com', 'Frida Kranstege', '909'),
(4, 0, 'funktionsbrevladan@lorumipsum.nu', '0987654321', 'HSASERVICES-123Z', NULL, 'kontakten@lorumipsum.nu', 'Tolvan Tolvansson', '1234567890', 4, 'HSASERVICES-123Z namn', 'mail@example.com', 'Frida Kranstege', '909');

INSERT INTO `producent_bestallning` (`id`, `version`, `miljo`, `status`, `tjanste_komponent_id`) VALUES
(3, 0, 'TEST', 'NY', 3),
(4, 0, 'SIT', 'NY', 3);

INSERT INTO `drift_miljo` (`id`, `version`, `namn`) VALUES
(1, 0, 'TIT'),
(2, 0, 'SIT'),
(3, 0, 'PIT');
