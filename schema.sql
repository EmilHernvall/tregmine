-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: tregmine
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.04.2

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
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory` (
  `inventory_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `inventory_checksum` int(11) DEFAULT NULL,
  `inventory_x` int(11) DEFAULT NULL,
  `inventory_y` int(11) DEFAULT NULL,
  `inventory_z` int(11) DEFAULT NULL,
  `inventory_world` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `inventory_type` enum('block','player') COLLATE utf8_swedish_ci DEFAULT 'block',
  PRIMARY KEY (`inventory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,1,-219894647,-119,71,36,'world','block');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `player_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_name` varchar(46) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_password` varchar(64) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_email` varchar(255) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_confirmed` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '0',
  `player_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `player_wallet` bigint(20) DEFAULT '10000',
  UNIQUE KEY `uid` (`player_id`),
  KEY `player` (`player_name`),
  KEY `password` (`player_password`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'knipil',NULL,NULL,'0','2013-06-28 20:16:09',10000);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_home`
--

DROP TABLE IF EXISTS `player_home`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_home` (
  `home_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `home_name` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `home_x` double DEFAULT NULL,
  `home_y` double DEFAULT NULL,
  `home_z` double DEFAULT NULL,
  `home_pitch` double DEFAULT NULL,
  `home_yaw` double DEFAULT NULL,
  `home_world` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `home_time` double DEFAULT NULL,
  PRIMARY KEY (`home_id`),
  KEY `player_idx` (`player_id`,`home_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_home`
--

LOCK TABLES `player_home` WRITE;
/*!40000 ALTER TABLE `player_home` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_home` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_property`
--

DROP TABLE IF EXISTS `player_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_property` (
  `player_id` int(10) unsigned NOT NULL DEFAULT '0',
  `property_key` varchar(255) COLLATE utf8_swedish_ci NOT NULL DEFAULT '',
  `property_value` varchar(255) COLLATE utf8_swedish_ci DEFAULT NULL,
  `property_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`,`property_key`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_property`
--

LOCK TABLES `player_property` WRITE;
/*!40000 ALTER TABLE `player_property` DISABLE KEYS */;
INSERT INTO `player_property` VALUES (1,'ip','127.0.0.1','2013-06-30 19:22:46'),(1,'hostName','localhost','2013-06-30 19:22:46'),(1,'timezone','Europe/Stockholm','2013-06-30 19:22:46'),(1,'admin','true','2013-06-28 20:25:51'),(1,'donator','true','2013-06-28 20:26:02'),(1,'color','vampire','2013-06-30 19:22:46');
/*!40000 ALTER TABLE `player_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_report`
--

DROP TABLE IF EXISTS `player_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_report` (
  `report_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject_id` int(10) unsigned NOT NULL,
  `issuer_id` int(10) unsigned NOT NULL,
  `report_action` enum('kick','softwarn','hardwarn','ban','comment') NOT NULL,
  `report_message` text CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `report_timestamp` int(10) unsigned NOT NULL,
  `report_validuntil` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  KEY `idx_subject` (`subject_id`,`report_timestamp`),
  KEY `idx_issuer` (`issuer_id`,`report_timestamp`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_report`
--

LOCK TABLES `player_report` WRITE;
/*!40000 ALTER TABLE `player_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warps`
--

DROP TABLE IF EXISTS `warps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warps` (
  `name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `x` double NOT NULL,
  `y` double NOT NULL,
  `z` double NOT NULL,
  `pitch` float NOT NULL,
  `yaw` float NOT NULL,
  `world` varchar(16) CHARACTER SET latin1 NOT NULL COMMENT 'added for multi world support',
  UNIQUE KEY `name.uniqe` (`name`),
  KEY `name-index` (`name`,`x`,`y`,`z`,`pitch`,`yaw`,`world`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warps`
--

LOCK TABLES `warps` WRITE;
/*!40000 ALTER TABLE `warps` DISABLE KEYS */;
INSERT INTO `warps` VALUES ('test',-204.7451882545257,78,172.67658034940334,18,-193.05,'world');
/*!40000 ALTER TABLE `warps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone`
--

DROP TABLE IF EXISTS `zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone` (
  `zone_id` int(11) NOT NULL AUTO_INCREMENT,
  `zone_world` varchar(50) COLLATE utf8_swedish_ci NOT NULL DEFAULT 'world',
  `zone_name` varchar(32) COLLATE utf8_swedish_ci NOT NULL,
  `zone_enterdefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_placedefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_destroydefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_pvp` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  `zone_hostiles` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '1',
  `zone_entermessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_exitmessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_texture` text COLLATE utf8_swedish_ci,
  `zone_owner` varchar(24) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`zone_id`),
  UNIQUE KEY `name` (`zone_name`)
) ENGINE=MyISAM AUTO_INCREMENT=904 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone`
--

LOCK TABLES `zone` WRITE;
/*!40000 ALTER TABLE `zone` DISABLE KEYS */;
INSERT INTO `zone` VALUES (903,'world','great_woods','1','1','1','0','0','Welcome to great_woods!','Now leaving great_woods.',NULL,'knipil');
/*!40000 ALTER TABLE `zone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone_lot`
--

DROP TABLE IF EXISTS `zone_lot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone_lot` (
  `lot_id` int(10) NOT NULL AUTO_INCREMENT,
  `zone_id` int(10) NOT NULL,
  `lot_name` varchar(50) NOT NULL,
  `lot_x1` int(10) NOT NULL,
  `lot_y1` int(10) NOT NULL,
  `lot_x2` int(10) NOT NULL,
  `lot_y2` int(10) NOT NULL,
  `special` int(11) DEFAULT NULL,
  PRIMARY KEY (`lot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone_lot`
--

LOCK TABLES `zone_lot` WRITE;
/*!40000 ALTER TABLE `zone_lot` DISABLE KEYS */;
/*!40000 ALTER TABLE `zone_lot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone_lotuser`
--

DROP TABLE IF EXISTS `zone_lotuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone_lotuser` (
  `lot_id` int(10) NOT NULL DEFAULT '0',
  `user_id` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`lot_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone_lotuser`
--

LOCK TABLES `zone_lotuser` WRITE;
/*!40000 ALTER TABLE `zone_lotuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `zone_lotuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone_rect`
--

DROP TABLE IF EXISTS `zone_rect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone_rect` (
  `rect_id` int(10) NOT NULL AUTO_INCREMENT,
  `zone_id` int(10) DEFAULT NULL,
  `rect_x1` int(10) DEFAULT NULL,
  `rect_y1` int(10) DEFAULT NULL,
  `rect_x2` int(10) DEFAULT NULL,
  `rect_y2` int(10) DEFAULT NULL,
  PRIMARY KEY (`rect_id`),
  KEY `zone_id` (`zone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone_rect`
--

LOCK TABLES `zone_rect` WRITE;
/*!40000 ALTER TABLE `zone_rect` DISABLE KEYS */;
INSERT INTO `zone_rect` VALUES (1,903,-281,230,-11,-30);
/*!40000 ALTER TABLE `zone_rect` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone_user`
--

DROP TABLE IF EXISTS `zone_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone_user` (
  `zone_id` int(10) unsigned NOT NULL DEFAULT '0',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `user_perm` enum('owner','maker','allowed','banned') NOT NULL DEFAULT 'allowed',
  PRIMARY KEY (`zone_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone_user`
--

LOCK TABLES `zone_user` WRITE;
/*!40000 ALTER TABLE `zone_user` DISABLE KEYS */;
INSERT INTO `zone_user` VALUES (903,1,'owner');
/*!40000 ALTER TABLE `zone_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-06-30 21:26:13
