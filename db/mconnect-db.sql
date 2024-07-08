/*
SQLyog Community v13.1.7 (64 bit)
MySQL - 8.0.31 : Database - mconnect-db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mconnect-db` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `mconnect-db`;

/*Table structure for table `ath_plotp_login_session` */

DROP TABLE IF EXISTS `ath_plotp_login_session`;

CREATE TABLE `ath_plotp_login_session` (
  `session_id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `mobile_no` varchar(16) DEFAULT NULL,
  `otp` varchar(8) DEFAULT NULL,
  `access_token_expire_on` int DEFAULT NULL,
  `access_token` text,
  `refresh_token` text,
  `otp_checked` tinyint DEFAULT NULL,
  `refresh_token_expire_on` int DEFAULT NULL,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `ath_plotp_login_session` */

/*Table structure for table `ath_plotp_outq` */

DROP TABLE IF EXISTS `ath_plotp_outq`;

CREATE TABLE `ath_plotp_outq` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mobile_no` varchar(16) NOT NULL,
  `otp` varchar(8) NOT NULL,
  `sms_text` text NOT NULL,
  `is_sent` tinyint NOT NULL DEFAULT '0',
  `sent_count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `ath_plotp_outq` */

/*Table structure for table `com_company` */

DROP TABLE IF EXISTS `com_company`;

CREATE TABLE `com_company` (
  `id` varchar(68) NOT NULL,
  `company_name` varchar(128) NOT NULL,
  `address` varchar(320) DEFAULT NULL,
  `icon_url` varchar(320) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `com_company` */

/*Table structure for table `com_employee` */

DROP TABLE IF EXISTS `com_employee`;

CREATE TABLE `com_employee` (
  `id` varchar(64) NOT NULL,
  `emp_id` varchar(48) NOT NULL,
  `emp_name` varchar(255) NOT NULL,
  `mobile_no` varchar(16) NOT NULL,
  `email` varchar(320) DEFAULT NULL,
  `position_id` int NOT NULL,
  `company_structure_id` int NOT NULL,
  `anyone_can_call` tinyint NOT NULL DEFAULT '1',
  `last_updated_time` int NOT NULL DEFAULT '0',
  `is_deactivated` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_EMP_ID` (`emp_id`),
  UNIQUE KEY `UNIQUE_MOBILE_NO` (`mobile_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `com_employee` */

insert  into `com_employee`(`id`,`emp_id`,`emp_name`,`mobile_no`,`email`,`position_id`,`company_structure_id`,`anyone_can_call`,`last_updated_time`,`is_deactivated`) values 
('u1','e1','Sampath Godamunne','+94763756823','sampath@microrsg.com',1,1,1,1720416264,0),
('u2','e2','Nandana Gamlath','+94713057295','nandana@microrsg.com',2,6,1,1720416264,0),
('u3','e3','Yohan Chathuranga','+94779811856','yc@micrornd.com',3,7,1,1720416264,0);

/*Table structure for table `com_position` */

DROP TABLE IF EXISTS `com_position`;

CREATE TABLE `com_position` (
  `position_id` int NOT NULL,
  `position_name` varchar(128) NOT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `com_position` */

insert  into `com_position`(`position_id`,`position_name`) values 
(1,'CEO'),
(2,'Architect'),
(3,'Tech Lead');

/*Table structure for table `com_structure` */

DROP TABLE IF EXISTS `com_structure`;

CREATE TABLE `com_structure` (
  `structure_id` int NOT NULL,
  `structure_parent_id` int NOT NULL,
  `structure_name` varchar(128) DEFAULT NULL,
  `structure_ref` varchar(64) DEFAULT NULL,
  `sort_index` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`structure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

/*Data for the table `com_structure` */

insert  into `com_structure`(`structure_id`,`structure_parent_id`,`structure_name`,`structure_ref`,`sort_index`) values 
(1,0,'Micro RSG','MRG',0),
(2,1,'Colombo Branch','CB',1),
(3,2,'Finance Department','F_D',2),
(4,3,'Account Division','A@D',3),
(5,1,'Kandy Branch','KB',5),
(6,2,'Development Department','D_D',2),
(7,6,'Backend Service','B@S',4);

/*Table structure for table `vcom_employee` */

DROP TABLE IF EXISTS `vcom_employee`;

/*!50001 DROP VIEW IF EXISTS `vcom_employee` */;
/*!50001 DROP TABLE IF EXISTS `vcom_employee` */;

/*!50001 CREATE TABLE  `vcom_employee`(
 `id` varchar(64) ,
 `emp_id` varchar(48) ,
 `emp_name` varchar(255) ,
 `mobile_no` varchar(16) ,
 `email` varchar(320) ,
 `position_id` int ,
 `company_structure_id` int ,
 `anyone_can_call` tinyint 
)*/;

/*View structure for view vcom_employee */

/*!50001 DROP TABLE IF EXISTS `vcom_employee` */;
/*!50001 DROP VIEW IF EXISTS `vcom_employee` */;

/*!50001 CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vcom_employee` AS select `com_employee`.`id` AS `id`,`com_employee`.`emp_id` AS `emp_id`,`com_employee`.`emp_name` AS `emp_name`,`com_employee`.`mobile_no` AS `mobile_no`,`com_employee`.`email` AS `email`,`com_employee`.`position_id` AS `position_id`,`com_employee`.`company_structure_id` AS `company_structure_id`,`com_employee`.`anyone_can_call` AS `anyone_can_call` from `com_employee` */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
