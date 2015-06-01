    alter table bestallnings_historik 
        drop 
        foreign key FK_3l3eaox109t6d4bi3rcl7es2r;

    alter table bestallnings_historik 
        drop 
        foreign key FK_racfs8rki8hdk1bc2ej60asrn;

    alter table konsument_anslutning 
        drop 
        foreign key FK_hwsuphldbfy7pp0tkq3p0n02b;

    alter table konsument_anslutning_logisk_adresser 
        drop 
        foreign key FK_osnewlmpswncnbxxnvdo1o0bf;

    alter table konsument_anslutning_logisk_adresser 
        drop 
        foreign key FK_h95281jv3la0l6n7rlb37ho4a;

    alter table konsument_bestallning 
        drop 
        foreign key FK_lp1rq3vvn2npf8eap20udakr8;

    alter table producent_anslutning 
        drop 
        foreign key FK_e2thltb8nn1nev4jgjcs3m6k3;

    alter table producent_anslutning_logisk_adresser 
        drop 
        foreign key FK_8ntf7a0xavslt72uwxs2btvxk;

    alter table producent_anslutning_logisk_adresser 
        drop 
        foreign key FK_mlmhp7ww0p5ma74gscacl19o0;

    alter table producent_bestallning 
        drop 
        foreign key FK_ojg3jjfi31xi112psodvnnc32;

    alter table role_permissions 
        drop 
        foreign key FK_d4atqq8ege1sij0316vh2mxfu;

    alter table tjanste_komponent 
        drop 
        foreign key FK_3eob3upykvoy0nc3r7ac6gpmn;

    alter table user_permissions 
        drop 
        foreign key FK_525n7wn9ejoa648m26bm4rhtt;

    alter table user_roles 
        drop 
        foreign key FK_g1uebn6mqk9qiaw45vnacmyo2;

    alter table user_roles 
        drop 
        foreign key FK_5q4rc4fh1on6567qk69uesvyf;

    drop table if exists bestallnings_historik;

    drop table if exists drift_miljo;

    drop table if exists konsument_anslutning;

    drop table if exists konsument_anslutning_logisk_adresser;

    drop table if exists konsument_bestallning;

    drop table if exists logisk_adress;

    drop table if exists producent_anslutning;

    drop table if exists producent_anslutning_logisk_adresser;

    drop table if exists producent_bestallning;

    drop table if exists role;

    drop table if exists role_permissions;

    drop table if exists tjanste_komponent;

    drop table if exists user;

    drop table if exists user_permissions;

    drop table if exists user_roles;

    create table bestallnings_historik (
        id bigint not null auto_increment,
        version bigint not null,
        datum datetime not null,
        konsument_bestallning_id bigint,
        producent_bestallning_id bigint,
        senast_uppdaterad_av varchar(255) not null,
        status varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table drift_miljo (
        id bigint not null auto_increment,
        version bigint not null,
        namn varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table konsument_anslutning (
        id bigint not null auto_increment,
        version bigint not null,
        konsument_bestallning_id bigint not null,
        tjanste_kontrakt varchar(255) not null,
        valid_from_time datetime not null,
        valid_to_time datetime not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table konsument_anslutning_logisk_adresser (
        konsument_anslutning_id bigint not null,
        logisk_adress_id bigint not null,
        primary key (konsument_anslutning_id, logisk_adress_id)
    ) ENGINE=InnoDB;

    create table konsument_bestallning (
        id bigint not null auto_increment,
        version bigint not null,
        miljo varchar(255) not null,
        status varchar(255) not null,
        tjanste_komponent_id bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table logisk_adress (
        id bigint not null auto_increment,
        version bigint not null,
        hsa_id varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table producent_anslutning (
        id bigint not null auto_increment,
        version bigint not null,
        producent_bestallning_id bigint not null,
        riv_ta_profile varchar(255) not null,
        tjanste_kontrakt varchar(255) not null,
        url varchar(255) not null,
        valid_from_time datetime not null,
        valid_to_time datetime not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table producent_anslutning_logisk_adresser (
        logisk_adress_id bigint not null,
        producent_anslutning_id bigint not null,
        primary key (producent_anslutning_id, logisk_adress_id)
    ) ENGINE=InnoDB;

    create table producent_bestallning (
        id bigint not null auto_increment,
        version bigint not null,
        miljo varchar(255) not null,
        status varchar(255) not null,
        tjanste_komponent_id bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table role (
        id bigint not null auto_increment,
        version bigint not null,
        name varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table role_permissions (
        role_id bigint,
        permissions_string varchar(255)
    ) ENGINE=InnoDB;

    create table tjanste_komponent (
        id bigint not null auto_increment,
        version bigint not null,
        funktions_brevlada_epost varchar(255) not null,
        funktions_brevlada_telefon varchar(255) not null,
        hsa_id varchar(255) not null,
        huvud_ansvarig_epost varchar(255) not null,
        huvud_ansvarig_namn varchar(255) not null,
        huvud_ansvarig_telefon varchar(255) not null,
        ipadress varchar(255),
        namn varchar(255),
        teknisk_kontakt_epost varchar(255) not null,
        teknisk_kontakt_namn varchar(255) not null,
        teknisk_kontakt_telefon varchar(255),
        user_id bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table user (
        id bigint not null auto_increment,
        version bigint not null,
        datum_skapad datetime not null,
        datum_uppdaterad datetime,
        epost varchar(255) not null,
        namn varchar(255) not null,
        password_hash varchar(255) not null,
        telefon_nummer varchar(255),
        username varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table user_permissions (
        user_id bigint,
        permissions_string varchar(255)
    ) ENGINE=InnoDB;

    create table user_roles (
        role_id bigint not null,
        user_id bigint not null,
        primary key (user_id, role_id)
    ) ENGINE=InnoDB;

    alter table logisk_adress 
        add constraint UK_t0hh8k0lm3gmwpkueja279g16  unique (hsa_id);

    alter table role 
        add constraint UK_8sewwnpamngi6b1dwaa88askk  unique (name);

    alter table tjanste_komponent 
        add constraint UK_obytuo2gifsdm7fj3wtc8h4nv  unique (hsa_id);

    alter table user 
        add constraint UK_sb8bbouer5wak8vyiiy4pf2bx  unique (username);

    alter table bestallnings_historik 
        add constraint FK_3l3eaox109t6d4bi3rcl7es2r 
        foreign key (konsument_bestallning_id) 
        references konsument_bestallning (id);

    alter table bestallnings_historik 
        add constraint FK_racfs8rki8hdk1bc2ej60asrn 
        foreign key (producent_bestallning_id) 
        references producent_bestallning (id);

    alter table konsument_anslutning 
        add constraint FK_hwsuphldbfy7pp0tkq3p0n02b 
        foreign key (konsument_bestallning_id) 
        references konsument_bestallning (id);

    alter table konsument_anslutning_logisk_adresser 
        add constraint FK_osnewlmpswncnbxxnvdo1o0bf 
        foreign key (logisk_adress_id) 
        references logisk_adress (id);

    alter table konsument_anslutning_logisk_adresser 
        add constraint FK_h95281jv3la0l6n7rlb37ho4a 
        foreign key (konsument_anslutning_id) 
        references konsument_anslutning (id);

    alter table konsument_bestallning 
        add constraint FK_lp1rq3vvn2npf8eap20udakr8 
        foreign key (tjanste_komponent_id) 
        references tjanste_komponent (id);

    alter table producent_anslutning 
        add constraint FK_e2thltb8nn1nev4jgjcs3m6k3 
        foreign key (producent_bestallning_id) 
        references producent_bestallning (id);

    alter table producent_anslutning_logisk_adresser 
        add constraint FK_8ntf7a0xavslt72uwxs2btvxk 
        foreign key (producent_anslutning_id) 
        references producent_anslutning (id);

    alter table producent_anslutning_logisk_adresser 
        add constraint FK_mlmhp7ww0p5ma74gscacl19o0 
        foreign key (logisk_adress_id) 
        references logisk_adress (id);

    alter table producent_bestallning 
        add constraint FK_ojg3jjfi31xi112psodvnnc32 
        foreign key (tjanste_komponent_id) 
        references tjanste_komponent (id);

    alter table role_permissions 
        add constraint FK_d4atqq8ege1sij0316vh2mxfu 
        foreign key (role_id) 
        references role (id);

    alter table tjanste_komponent 
        add constraint FK_3eob3upykvoy0nc3r7ac6gpmn 
        foreign key (user_id) 
        references user (id);

    alter table user_permissions 
        add constraint FK_525n7wn9ejoa648m26bm4rhtt 
        foreign key (user_id) 
        references user (id);

    alter table user_roles 
        add constraint FK_g1uebn6mqk9qiaw45vnacmyo2 
        foreign key (user_id) 
        references user (id);

    alter table user_roles 
        add constraint FK_5q4rc4fh1on6567qk69uesvyf 
        foreign key (role_id) 
        references role (id);
