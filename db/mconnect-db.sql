-- MySQL dump 10.13  Distrib 8.0.38, for macos14 (arm64)
--
-- Host: 127.0.0.1    Database: mconnect-db
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ath_plotp_login_session`
--

DROP TABLE IF EXISTS `ath_plotp_login_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ath_plotp_login_session` (
  `session_id` varchar(64) NOT NULL,
  `user_id` varchar(64) DEFAULT NULL,
  `mobile_no` varchar(16) DEFAULT NULL,
  `otp` varchar(8) DEFAULT NULL,
  `access_token` text,
  `access_token_expire_on` int DEFAULT NULL,
  `refresh_token` text,
  `refresh_token_expire_on` int DEFAULT NULL,
  `otp_checked` tinyint DEFAULT NULL,
  `device_id` varchar(128) DEFAULT NULL,
  `created_at` int DEFAULT NULL,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ath_plotp_login_session`
--

LOCK TABLES `ath_plotp_login_session` WRITE;
/*!40000 ALTER TABLE `ath_plotp_login_session` DISABLE KEYS */;
INSERT INTO `ath_plotp_login_session` VALUES ('25aab8e9-2a89-44c3-b999-c95d801ced84','u2','+94713057295','213447',NULL,NULL,NULL,NULL,0,'d1',1720502055),('351dc831-1f52-4051-9d0f-54e7a2d33b39','u2','+94713057295','676554',NULL,NULL,NULL,NULL,1,'d2',1720510715),('3d8b7302-1dde-4504-9049-6147fc89282e','u2','+94713057295','524010',NULL,NULL,NULL,NULL,0,'d1',1720502177),('6faff33b-d50e-44f8-b73c-3798ef8bf27a','u2','+94713057295','489420',NULL,NULL,NULL,NULL,0,'d1',1720512244),('b70a7912-ea84-4658-a727-26f11e3b711f','u2','+94713057295','922567',NULL,NULL,NULL,NULL,1,'d1',1720442212);
/*!40000 ALTER TABLE `ath_plotp_login_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ath_plotp_outq`
--

DROP TABLE IF EXISTS `ath_plotp_outq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ath_plotp_outq` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mobile_no` varchar(16) NOT NULL,
  `otp` varchar(8) NOT NULL,
  `sms_text` text NOT NULL,
  `is_sent` tinyint NOT NULL DEFAULT '0',
  `sent_count` int NOT NULL DEFAULT '0',
  `session_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ath_plotp_outq`
--

LOCK TABLES `ath_plotp_outq` WRITE;
/*!40000 ALTER TABLE `ath_plotp_outq` DISABLE KEYS */;
INSERT INTO `ath_plotp_outq` VALUES (3,'+94713057295','922567','Use OTP: 922567',0,0,'b70a7912-ea84-4658-a727-26f11e3b711f'),(4,'+94713057295','213447','Use OTP: 213447',0,0,'25aab8e9-2a89-44c3-b999-c95d801ced84'),(5,'+94713057295','524010','Use OTP: 524010',0,0,'3d8b7302-1dde-4504-9049-6147fc89282e'),(6,'+94713057295','676554','Use OTP: 676554',0,0,'351dc831-1f52-4051-9d0f-54e7a2d33b39'),(7,'+94713057295','489420','Use OTP: 489420',0,0,'6faff33b-d50e-44f8-b73c-3798ef8bf27a');
/*!40000 ALTER TABLE `ath_plotp_outq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_company`
--

DROP TABLE IF EXISTS `com_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `com_company` (
  `id` varchar(68) NOT NULL,
  `company_name` varchar(128) NOT NULL,
  `address` varchar(320) DEFAULT NULL,
  `icon_url` varchar(320) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_company`
--

LOCK TABLES `com_company` WRITE;
/*!40000 ALTER TABLE `com_company` DISABLE KEYS */;
INSERT INTO `com_company` VALUES ('c1','MRG','Colombo','media/company_icon/c1.png');
/*!40000 ALTER TABLE `com_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_employee`
--

DROP TABLE IF EXISTS `com_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_EMP_ID` (`emp_id`),
  UNIQUE KEY `UNIQUE_MOBILE_NO` (`mobile_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_employee`
--

LOCK TABLES `com_employee` WRITE;
/*!40000 ALTER TABLE `com_employee` DISABLE KEYS */;
INSERT INTO `com_employee` VALUES ('u1','e1','Sampath Godamunne','+94763756823','sampath@microrsg.com',2,1,1,1720416264,0,0),('u2','e2','Nandana Gamlath','+94713057295','nandana@microrsg.com',3,6,1,1720416264,0,0),('u3','e3','Yohan Chathuranga','+94779811856','yc@micrornd.com',4,7,1,1720416264,0,0);
/*!40000 ALTER TABLE `com_employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_position`
--

DROP TABLE IF EXISTS `com_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `com_position` (
  `position_id` int NOT NULL,
  `position_name` varchar(128) NOT NULL,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_position`
--

LOCK TABLES `com_position` WRITE;
/*!40000 ALTER TABLE `com_position` DISABLE KEYS */;
INSERT INTO `com_position` VALUES (1,'NA',0),(2,'CEO',0),(3,'Architect',0),(4,'Tech Lead',0),(5,'SE',1);
/*!40000 ALTER TABLE `com_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `com_structure`
--

DROP TABLE IF EXISTS `com_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `com_structure` (
  `structure_id` int NOT NULL,
  `structure_parent_id` int NOT NULL,
  `structure_name` varchar(128) DEFAULT NULL,
  `structure_ref` varchar(64) DEFAULT NULL,
  `sort_index` int NOT NULL DEFAULT '0',
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`structure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_structure`
--

LOCK TABLES `com_structure` WRITE;
/*!40000 ALTER TABLE `com_structure` DISABLE KEYS */;
INSERT INTO `com_structure` VALUES (1,0,'Micro RSG','MRG',0,0),(2,1,'Colombo Branch','CB',1,0),(3,2,'Finance Department','F_D',2,0),(4,3,'Account Division','A@D',3,0),(5,1,'Kandy Branch','KB',5,0),(6,2,'Development Department','D_D',2,0),(7,6,'Backend Service','B@S',4,0);
/*!40000 ALTER TABLE `com_structure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mda_profile_image`
--

DROP TABLE IF EXISTS `mda_profile_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mda_profile_image` (
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image_location` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mda_profile_image`
--

LOCK TABLES `mda_profile_image` WRITE;
/*!40000 ALTER TABLE `mda_profile_image` DISABLE KEYS */;
INSERT INTO `mda_profile_image` VALUES ('u1','media/profile_images/u1.png');
/*!40000 ALTER TABLE `mda_profile_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vath_plotp_outq`
--

DROP TABLE IF EXISTS `vath_plotp_outq`;
/*!50001 DROP VIEW IF EXISTS `vath_plotp_outq`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vath_plotp_outq` AS SELECT 
 1 AS `id`,
 1 AS `mobile_no`,
 1 AS `otp`,
 1 AS `sms_text`,
 1 AS `is_sent`,
 1 AS `sent_count`,
 1 AS `session_id`,
 1 AS `user_id`,
 1 AS `otp_checked`,
 1 AS `device_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vath_users`
--

DROP TABLE IF EXISTS `vath_users`;
/*!50001 DROP VIEW IF EXISTS `vath_users`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vath_users` AS SELECT 
 1 AS `id`,
 1 AS `emp_id`,
 1 AS `emp_name`,
 1 AS `mobile_no`,
 1 AS `email`,
 1 AS `position_id`,
 1 AS `company_structure_id`,
 1 AS `anyone_can_call`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vcom_employee`
--

DROP TABLE IF EXISTS `vcom_employee`;
/*!50001 DROP VIEW IF EXISTS `vcom_employee`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vcom_employee` AS SELECT 
 1 AS `id`,
 1 AS `emp_id`,
 1 AS `emp_name`,
 1 AS `mobile_no`,
 1 AS `email`,
 1 AS `position_id`,
 1 AS `company_structure_id`,
 1 AS `anyone_can_call`,
 1 AS `last_updated_time`,
 1 AS `is_deactivated`,
 1 AS `deleted`,
 1 AS `position_name`,
 1 AS `company_structure_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vath_plotp_outq`
--

/*!50001 DROP VIEW IF EXISTS `vath_plotp_outq`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vath_plotp_outq` AS select `t`.`id` AS `id`,`t`.`mobile_no` AS `mobile_no`,`t`.`otp` AS `otp`,`t`.`sms_text` AS `sms_text`,`t`.`is_sent` AS `is_sent`,`t`.`sent_count` AS `sent_count`,`t`.`session_id` AS `session_id`,`s`.`user_id` AS `user_id`,`s`.`otp_checked` AS `otp_checked`,`s`.`device_id` AS `device_id` from (`ath_plotp_outq` `t` left join `ath_plotp_login_session` `s` on((`t`.`session_id` = `s`.`session_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vath_users`
--

/*!50001 DROP VIEW IF EXISTS `vath_users`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vath_users` AS select `vcom_employee`.`id` AS `id`,`vcom_employee`.`emp_id` AS `emp_id`,`vcom_employee`.`emp_name` AS `emp_name`,`vcom_employee`.`mobile_no` AS `mobile_no`,`vcom_employee`.`email` AS `email`,`vcom_employee`.`position_id` AS `position_id`,`vcom_employee`.`company_structure_id` AS `company_structure_id`,`vcom_employee`.`anyone_can_call` AS `anyone_can_call` from `vcom_employee` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vcom_employee`
--

/*!50001 DROP VIEW IF EXISTS `vcom_employee`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8mb3_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vcom_employee` AS select `ce`.`id` AS `id`,`ce`.`emp_id` AS `emp_id`,`ce`.`emp_name` AS `emp_name`,`ce`.`mobile_no` AS `mobile_no`,`ce`.`email` AS `email`,`ce`.`position_id` AS `position_id`,`ce`.`company_structure_id` AS `company_structure_id`,`ce`.`anyone_can_call` AS `anyone_can_call`,`ce`.`last_updated_time` AS `last_updated_time`,`ce`.`is_deactivated` AS `is_deactivated`,`ce`.`deleted` AS `deleted`,`cp`.`position_name` AS `position_name`,`cs`.`structure_name` AS `company_structure_name` from ((`com_employee` `ce` left join `com_position` `cp` on((`ce`.`position_id` = `cp`.`position_id`))) left join `com_structure` `cs` on((`ce`.`company_structure_id` = `cs`.`structure_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-10 13:39:57
