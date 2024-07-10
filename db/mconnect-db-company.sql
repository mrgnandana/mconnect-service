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
INSERT INTO `com_employee` VALUES ('u1','e1','Sampath Godamunne','+94763756823','sampath@microrsg.com',1,1,1,1720416264,0),('u2','e2','Nandana Gamlath','+94713057295','nandana@microrsg.com',2,6,1,1720416264,0),('u3','e3','Yohan Chathuranga','+94779811856','yc@micrornd.com',3,7,1,1720416264,0);
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
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_position`
--

LOCK TABLES `com_position` WRITE;
/*!40000 ALTER TABLE `com_position` DISABLE KEYS */;
INSERT INTO `com_position` VALUES (1,'CEO'),(2,'Architect'),(3,'Tech Lead');
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
  PRIMARY KEY (`structure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `com_structure`
--

LOCK TABLES `com_structure` WRITE;
/*!40000 ALTER TABLE `com_structure` DISABLE KEYS */;
INSERT INTO `com_structure` VALUES (1,0,'Micro RSG','MRG',0),(2,1,'Colombo Branch','CB',1),(3,2,'Finance Department','F_D',2),(4,3,'Account Division','A@D',3),(5,1,'Kandy Branch','KB',5),(6,2,'Development Department','D_D',2),(7,6,'Backend Service','B@S',4);
/*!40000 ALTER TABLE `com_structure` ENABLE KEYS */;
UNLOCK TABLES;

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
 1 AS `position_name`,
 1 AS `company_structure_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vcom_employee`
--

/*!50001 DROP VIEW IF EXISTS `vcom_employee`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `vcom_employee` AS select `ce`.`id` AS `id`,`ce`.`emp_id` AS `emp_id`,`ce`.`emp_name` AS `emp_name`,`ce`.`mobile_no` AS `mobile_no`,`ce`.`email` AS `email`,`ce`.`position_id` AS `position_id`,`ce`.`company_structure_id` AS `company_structure_id`,`ce`.`anyone_can_call` AS `anyone_can_call`,`ce`.`last_updated_time` AS `last_updated_time`,`ce`.`is_deactivated` AS `is_deactivated`,`cp`.`position_name` AS `position_name`,`cs`.`structure_name` AS `company_structure_name` from ((`com_employee` `ce` left join `com_position` `cp` on((`ce`.`position_id` = `cp`.`position_id`))) left join `com_structure` `cs` on((`ce`.`company_structure_id` = `cs`.`structure_id`))) */;
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

-- Dump completed on 2024-07-10 13:37:13
