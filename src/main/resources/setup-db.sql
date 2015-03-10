-- DROP DATABASE IF EXISTS fullcontact;
-- CREATE DATABASE IF NOT EXISTS fullcontact;
-- USE fullcontact;
-- 
-- DROP TABLE IF EXISTS `email_contacts_atachments`;
-- DROP TABLE IF EXISTS `atachments`;
-- DROP TABLE IF EXISTS `emails`;
-- DROP TABLE IF EXISTS `phones`;
-- DROP TABLE IF EXISTS `contacts`;
-- DROP TABLE IF EXISTS `nationalities`;
-- DROP TABLE IF EXISTS `sexes`;
-- DROP TABLE IF EXISTS `family_status`;
-- DROP TABLE IF EXISTS `phones_types`;

CREATE TABLE `nationalities` (
  `nationality_id` INT(2) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nationality_value` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`nationality_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `sexes` (
  `sex_id` INT(1) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sex_value` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`sex_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `family_status` (
  `family_status_id` INT(1) UNSIGNED NOT NULL AUTO_INCREMENT,
  `family_status_value` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`family_status_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `contacts` (
  `contact_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `middle_name` VARCHAR(50) NOT NULL,
  `avatar_path` VARCHAR(255) DEFAULT NULL,
  `date_of_birth` DATE NOT NULL,
  `web_site` VARCHAR(30),
  `email_address` VARCHAR(50) NOT NULL,
  `current_employment` VARCHAR(50),
  `country` VARCHAR(30),
  `city` VARCHAR(30),
  `street` VARCHAR(50),
  `house` VARCHAR(10),
  `block` VARCHAR(10),
  `apartment` VARCHAR(10),
  `city_index` VARCHAR(20),
  `sex_id` INT(1) UNSIGNED NOT NULL,  
  `nationality_id` INT(2) UNSIGNED NOT NULL,
  `family_status_id` INT(1) UNSIGNED NOT NULL,
  PRIMARY KEY (`contact_id`),
  CONSTRAINT FOREIGN KEY (`nationality_id`)   REFERENCES `nationalities` (`nationality_id`)   ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FOREIGN KEY (`sex_id`) 	      REFERENCES `sexes` (`sex_id`) 		      ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT FOREIGN KEY (`family_status_id`) REFERENCES `family_status` (`family_status_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `emails`(
  `email_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `contact_id_sender` INT(11) UNSIGNED NOT NULL,
  `email_subject` VARCHAR(30) NOT NULL,
  `email_text` TEXT,
  `email_date_send` DATETIME NOT NULL,
  PRIMARY KEY (`email_id`)
--   CONSTRAINT FOREIGN KEY (`contact_id_sender`) REFERENCES `contacts` (`contact_id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `phones_types` (
  `phone_type_id` INT(1) UNSIGNED NOT NULL AUTO_INCREMENT,
  `phone_type_value` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`phone_type_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `phones`(
  `phone_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `contact_id` INT(11) UNSIGNED NOT NULL,
  `phone_value` VARCHAR(20) NOT NULL,
  `phone_type_id` INT(1) UNSIGNED NOT NULL,   
  `phone_comment` VARCHAR(100),
  `phone_country_code` VARCHAR(5) NOT NULL,
  `phone_operator_code` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`phone_id`),
  CONSTRAINT FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`)  ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`phone_type_id`) REFERENCES `phones_types` (`phone_type_id`) ON DELETE RESTRICT  
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `atachments`(
  `atachment_id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `contact_id` INT(11) UNSIGNED NOT NULL,
  `atachment_name` VARCHAR(255) NOT NULL,
  `atachment_upload_date` DATETIME NOT NULL,
  `atachment_comment` VARCHAR(100),
  PRIMARY KEY (`atachment_id`),
  CONSTRAINT FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;     

CREATE TABLE IF NOT EXISTS `emails_receivers`(
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email_id` INT(11) UNSIGNED NOT NULL,
  `contact_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`email_id`) REFERENCES `emails` (`email_id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`contact_id`) ON DELETE CASCADE  
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `emails_atachments`(
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `email_id` INT(11) UNSIGNED NOT NULL,
  `atachment_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT FOREIGN KEY (`email_id`) REFERENCES `emails` (`email_id`) ON DELETE CASCADE,
  CONSTRAINT FOREIGN KEY (`atachment_id`) REFERENCES `atachments` (`atachment_id`) ON DELETE RESTRICT  
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;  
  
INSERT INTO `nationalities` (`nationality_id`, `nationality_value`)
VALUES
  (1, 'Belarus'),
  (2, 'Russia'),
  (3, 'Ukrain'),
  (4, 'UniteState'),
  (5, 'France'); 
  
INSERT INTO `sexes` (`sex_id`, `sex_value`)
VALUES
  (1, 'Male'),
  (2, 'Female');
  
INSERT INTO `family_status` (`family_status_id`, `family_status_value`)
VALUES
  (1, 'Married'),
  (2, 'Divorced'),
  (3, 'Widower'),
  (4, 'Single');
  
INSERT INTO `contacts` (`contact_id`, `first_name`, `last_name`, `middle_name`, `date_of_birth`, `sex_id`, `nationality_id`, `family_status_id`, 
		        `web_site`, email_address, `current_employment`, `country`, `city`, `street`, `house`, `block`, `apartment`, `city_index`)
VALUES

    (1, 'Admin', 'Admin', 'Admin', '1999-06-06', 1, 3, 4,
        'fullcontact.com', 'pankaj1@mail.com', 'iTechArt',  'Belarus', 'Minsk', 'Surganova', '37', '2', '505', '6666'),

    (2, 'PankajFirst', 'PankajLast', 'PankajMidle', '2015-03-10', 1, 1, 1,
        'pankaj.com', 'shorin-roman@yandex.by', 'devcorp', 'India', 'Bangladesh', 'street1', 'house1', 'block1', 'apnt1', '1111'),

    (3, 'student', 'student', 'student', '2015-03-10', 1, 2, 2,
        'student.com', 'studentbntu@mail.ru',  'devcorpstudent', 'Indiastudent', 'Bangladeshstudent', 'street2', 'house2', 'block2', 'apart2', '2222'),    

    (4, 'LisaFirst', 'LisaLast', 'LisaMidle', '2007-12-4', 2, 3, 3,
        'Lisa.com', 'Lisa1@mail.com', 'devcorpLisa', 'IndiaLisa', 'BangladeshLisa', 'street3', 'house3', 'block3', 'apart3', '33333'),

    (5, 'JackFirst', 'JackLast', 'JackMidle', '2015-03-10', 1, 4, 4,
        'Jack.com', 'Jack1@mail.com', 'devcorpJack', 'IndiaJack', 'BangladeshJack', 'street4', 'house4', 'block4', 'apar4', '4444'),
        
    (6, 'RomaFirst', 'RomajLast', 'PankajMidle', '2010-12-4', 1, 4, 1,
        'Roma.com', 'Roma1@mail.com','devcorpRoma', 'IndiaRoma', 'BangladeshRoma', 'street5', 'house5', 'block5', 'apment5', '55555'), 

    (7, 'RomaFirst', 'BobjLast', 'PankajMidle', '2015-12-4', 2, 4, 4,
        'Bob.com', 'Bob1@mail.com', 'devcorpBob', 'IndiaBob', 'BangladeshBob', 'street8', 'house6', 'block6', 'apant6', '66666'),
    
    (8, 'NeoFirst', 'PankajLast', 'PankajMidle', '2015-12-4', 2, 4, 3,
        'Neo.com', 'Neo1@mail.com', 'devcorpNeo', 'IndiaNeo', 'BangladeshNeo', 'street7', 'house7', 'block7', 'apart7', '7777'),

    (9, 'MorpheosFirst', 'MorpheosLast', 'MorpheosMidle', '2015-12-4', 1, 2, 2,
        'Morpheos.com', 'Morpheos1@mail.com', 'devcorpMorpheos', 'IndiaMorpheos', 'BangladeshMorpheos', 'street8', 'house8', 'block8', 'apant8', '88888'),

    (10, 'TrinitiFirst', 'TrinitiLast', 'TrinitiMidle', '2015-12-4', 2, 3, 1,
        'Triniti.com', 'Triniti1@mail.com', 'devcorpTriniti', 'IndiaTriniti', 'BangladeshTriniti', 'street9', 'house9', 'block9', 'apartt9', '99999'),

    (11, 'TuckFirst', 'TuckLast', 'TuckMidle', '2015-12-4', 1, 4, 3,
        'Tuck.com', 'Tuck1@mail.com', 'devcorpTuck', 'IndiaTuck', 'BangladeshTuck', 'street10', 'house10', 'block10', 'apart10', '10000'),

    (12, 'GendolfFirst', 'GendolfLast', 'GendolfMidle', '2015-12-4', 2, 1, 4,
        'Gendolf.com','Gendolf1@mail.com', 'devcorpGendolf', 'IndiaGendolf', 'BangladeshGendolf', 'street11', 'house11', 'block11', 'apent11', '111111'),

    (13, 'AragornFirst', 'AragornLast', 'AragornMidle', '2015-12-4', 1, 4, 2,
        'Aragorn.com', 'Aragorn1@mail.com', 'devcorpAragorn', 'IndiaAragorn', 'BangladeshAragorn', 'street12', 'house12', 'block12', 'apent12', '122222'), 

    (14, 'BoramirFirst', 'BoramirLast', 'BoramirMidle', '2015-12-4', 1, 3, 3,
        'Boramir.com', 'Boramir1@mail.com', 'devcorpBoramir', 'IndiaBoramir', 'BangladeshBoramir', 'street13', 'house13', 'block13', 'apaent13', '133333'),

    (15, 'SamFirst', 'SamLast', 'SamMidle', '2015-12-4', 2, 4, 3,
        'Sam.com', 'Sam1@mail.com', 'devcorpSam', 'IndiaSam', 'BangladeshSam', 'street14', 'house14', 'block14', 'apent14', '1444'),  

    (16, 'KolyaFirst', 'KolyaLast', 'KolyaMidle', '2015-12-4', 1, 3, 4,
        'Kolya.com', 'Kolya1@mail.com',  'devcorpKolya', 'IndiaKolya', 'BangladeshKolya', 'street15', 'house15', 'block15', 'aparnt15', '15555'),
    
    (17, 'KolyaFirst17', 'KolyaLast17', 'KolyaMidle17', '2015-12-4', 1, 3, 4,
      'Kolya17.com', 'Kolya117@mail.com',  'devcorpKolya17', 'IndiaKolya17', 'BangladeshKolya', 'street15', 'house15', 'block15', 'apartt15', '15555'); 
        

    
-- INSERT INTO `emails` (`email_id`, `contact_id_sender`, `email_subject`, `email_text`, `email_date_send`)
-- VALUES
--   (1, 2, 'subject1', 'blobl text 1', now()),
--   (2, 2, 'subject2', 'blobl text 2', now()),
--   (3, 2, 'subject3', 'blobl text 3', now()),
--   (4, 2, 'subject4', 'blobl text 4', now()),
--   (5, 2, 'subject5', 'blobl text 5', now()),
--   (6, 2, 'subject6', 'blobl text 6', now()),
--   (7, 2, 'subject7', 'blobl text 7', now()),
--   (8, 2, 'subject8', 'blobl text 8', now()),
--   (9, 2, 'subject9', 'blobl text 9', now()),
--   (10, 2, 'subject10', 'blobl text 10', now()),
--   (11, 2, 'subject11', 'blobl text 11', now()),
--   (12, 2, 'subject12', 'blobl text 12', now()),
--   (13, 2, 'subject13', 'blobl text 13', now()),
--   (14, 2, 'subject14', 'blobl text 14', now()),
--   (15, 2, 'subject15', 'blobl text 15', now()),
--   (16, 2, 'subject15', 'blobl text 15', now()),
--   (17, 1, 'subject15', 'blobl text 15', now()),
--   (18, 2, 'subject15', 'blobl text 15', now()),
--   (19, 2, 'subject15', 'blobl text 15', now()),
--   (20, 2, 'subject15', 'blobl text 15', now()),
--   (21, 2, 'subject15', 'blobl text 15', now()),
--   (22, 2, 'subject15', 'blobl text 15', now()),
--   (23, 2, 'subject15', 'blobl text 15', now()),
--   (24, 2, 'subject15', 'blobl text 15', now()),
--   (25, 2, 'subject15', 'blobl text 15', now());
   
   
-- INSERT INTO `atachments` (`atachment_id`, `contact_id`, `atachment_name`, `atachment_upload_date`, `atachment_comment`)
-- VALUES
--   (1, 1, 'atachment_name11111', now(), 'atachment_comment1'),
--   (2, 2, 'atachment_name22222', now(), 'atachment_comment2'),
--   (3, 3, 'atachment_name33333', now(), 'atachment_comment3'),
--   (4, 4, 'atachment_name44444', now(), 'atachment_comment4'),
--   (5, 5, 'atachment_name55555', now(), 'atachment_comment5'),
--   (6, 6, 'atachment_name66666', now(), 'atachment_comment6'),
--   (7, 7, 'atachment_name7777', now(), 'atachment_comment7'),
--   (8, 8, 'atachment_name8888', now(), 'atachment_comment8'),
--   (9, 9, 'atachment_name99999', now(), 'atachment_comment19'),
--   (10, 10, 'atachment_name10000', now(), 'atachment_comment10'),
--   (11, 11, 'atachment_name21111', now(), 'atachment_comment11'),
--   (12, 12, 'atachment_name23333', now(), 'atachment_comment12'),
--   (13, 13, 'atachment_name24444', now(), 'atachment_comment13'),
--   (14, 14, 'atachment_name25555', now(), 'atachment_comment14'),
--   (15, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (16, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (17, 1, '95078d66-94c9-494d-a47e-419b3aed097b_1.jpeg', now(), 'atachment_comment15'),
--   (18, 1, 'cddf6628-4116-4e91-931f-771e8d6dc9c4_2.jpeg', now(), 'atachment_comment15'),
--   (19, 1, '0f0579c5-27c3-4d79-9ad9-33d8b84f1fe7_3.jpeg', now(), 'atachment_comment15'),
--   (20, 1, 'e4e94005-c34a-4f14-827c-66ebb0379936_4.jpeg', now(), 'atachment_comment15'),
--   (21, 1, 'ae8fe9e1-379c-455f-aefe-31f06ffa0d94_5.jpeg', now(), 'atachment_comment15'),
--   (22, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (23, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (24, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (25, 15, 'atachment_name26666', now(), 'atachment_comment15'),
--   (26, 15, 'atachment_name26666', now(), 'atachment_comment15');
   
   
-- INSERT INTO `emails_receivers` (`id`, `email_id`, `contact_id`)
-- VALUES
--   (1, 1, 2),
--   (2, 2, 2),
--   (3, 3, 3),
--   (4, 4, 4),
--   (5, 5, 5),
--   (6, 6, 6),
--   (7, 7, 7),
--   (8, 8, 8),
--   (9, 9, 9),
--   (10, 14, 10),
--   (11, 14, 11),
--   (12, 14, 12),
--   (13, 14, 13),
--   (14, 14, 14),
--   (15, 15, 15),
--   (16, 16, 2), 
--   (17, 17, 3),
--   (18, 17, 4),
--   (19, 17, 5),
--   (20, 20, 2),
--   (21, 20, 2),
--   (22, 20, 2),
--   (23, 20, 2);
  
   
   
-- INSERT INTO `emails_atachments` (`id`, `email_id`, `atachment_id`)
-- VALUES
--   (1, 1, 2),
--   (2, 2, 2),
--   (3, 3, 3),
--   (4, 4, 4),
--   (5, 5, 5),
--   (6, 6, 6),
--   (7, 7, 7),
--   (8, 8, 8),
--   (9, 9, 9),
--   (10, 10, 10),
--   (11, 11, 11),
--   (12, 16, 17),
--   (13, 17, 16),
--   (14, 17, 15),
--   (15, 19, 20),
--   (16, 19, 21);
   
 
INSERT INTO phones_types (`phone_type_id`, `phone_type_value`)
VALUES
  (1, 'Mobile'),
  (2, 'Home');
 
 
-- INSERT INTO `phones` (`phone_id`, `contact_id`, `phone_value`, `phone_type_id`, `phone_comment`, `phone_country_code`, `phone_operator_code`)		      
-- VALUES
--   (1, 1, '1111111', 1, 'comment1', '111', '123'),
--   (2, 2, '2222222', 1, 'comment2', '222', '123'),
--   (3, 3, '3333333', 1, 'comment3', '333', '123'),
--   (4, 4, '4444444', 2, 'comment4', '444', '123'),
--   (5, 5, '5555555', 2, 'comment5', '555', '123'),
--   (6, 6, '6666666', 2, 'comment6', '666', '123'),
--   (7, 7, '7777777', 1, 'comment7', '777', '123'),
--   (8, 8, '8888888', 1, 'comment8', '888', '123'),
--   (9, 9, '9999999', 2, 'comment9', '999', '123'),
--   (10, 10, '1000000', 1, 'comment10', '999', '123'),
--   (11, 11, '2111111', 1, 'comment11', '100', '123'),
--   (12, 12, '2333333', 2, 'comment12', '123', '123'),
--   (13, 13, '2444444', 1, 'comment13', '123', '123'),
--   (14, 14, '2555555', 1, 'comment14', '123', '123'),
--   (15, 15, '2666666', 2, 'comment15', '123', '123'),
--   (16, 1, '2666677', 2, 'comment15', '123', '123'),
--   (17, 1, '2666688', 2, 'comment15', '123', '123'),
--   (18, 1, '2666699', 2, 'comment15', '123', '123'),
--   (19, 1, '2666600', 2, 'comment15', '123', '123'),
--   (20, 1, '2666611', 2, 'comment15', '123', '123'),
--   (21, 1, '2666622', 2, 'comment15', '123', '123');
  
SET foreign_key_checks = 0;
SET foreign_key_checks = 1; 

commit;