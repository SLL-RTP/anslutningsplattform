CREATE DATABASE IF NOT EXISTS anslutningsplattform character set utf8;
grant usage on anslutningsplattform.* to ap_user@'%' identified by 'ap_password';
grant all privileges on anslutningsplattform.* to ap_user@'%';
flush privileges;
