-- MySQL dump 10.13  Distrib 5.1.66, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: tregmine1
-- ------------------------------------------------------
-- Server version	5.1.66-0+squeeze1

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
  `inventory_player` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `inventory_type` enum('block','player','player_armor') COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `idx_player` (`inventory_player`)
) ENGINE=InnoDB AUTO_INCREMENT=34646 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inventory_item`
--

DROP TABLE IF EXISTS `inventory_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory_item` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `inventory_id` int(10) unsigned DEFAULT NULL,
  `item_slot` int(10) unsigned DEFAULT NULL,
  `item_material` int(10) unsigned DEFAULT NULL,
  `item_data` int(11) DEFAULT NULL,
  `item_meta` text CHARACTER SET latin1,
  `item_count` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `inventory_idx` (`inventory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=287631 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `item_id` int(10) unsigned DEFAULT NULL,
  `item_name` varchar(50) COLLATE utf8_swedish_ci DEFAULT NULL,
  `item_value` int(10) unsigned DEFAULT NULL,
  UNIQUE KEY `itemid` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `motd`
--

DROP TABLE IF EXISTS `motd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `motd` (
  `motd_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `motd_timestamp` int(10) unsigned NOT NULL,
  `motd_message` text CHARACTER SET latin1,
  PRIMARY KEY (`motd_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `player_wallet` bigint(20) DEFAULT '50000',
  `player_rank` enum('unverified','tourist','settler','resident','donator','guardian','builder','coder','junior_admin','senior_admin') COLLATE utf8_swedish_ci DEFAULT 'unverified',
  `player_flags` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`player_id`),
  UNIQUE KEY `name` (`player_name`)
) ENGINE=InnoDB AUTO_INCREMENT=44409 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_chatlog`
--

DROP TABLE IF EXISTS `player_chatlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_chatlog` (
  `chatlog_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `chatlog_timestamp` int(10) unsigned DEFAULT NULL,
  `chatlog_channel` varchar(64) CHARACTER SET latin1 DEFAULT NULL,
  `chatlog_message` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`chatlog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=176350 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_givelog`
--

DROP TABLE IF EXISTS `player_givelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_givelog` (
  `givelog_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned DEFAULT NULL,
  `recipient_id` int(10) unsigned DEFAULT NULL,
  `givelog_material` int(10) unsigned DEFAULT NULL,
  `givelog_data` int(11) DEFAULT NULL,
  `givelog_meta` text CHARACTER SET latin1,
  `givelog_count` int(10) unsigned DEFAULT NULL,
  `givelog_timestamp` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`givelog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12542 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `player_idx` (`player_id`,`home_time`),
  KEY `idx_player` (`home_name`)
) ENGINE=InnoDB AUTO_INCREMENT=33164 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_login`
--

DROP TABLE IF EXISTS `player_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_login` (
  `login_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `login_timestamp` int(10) unsigned DEFAULT NULL,
  `login_action` enum('login','logout') CHARACTER SET latin1 DEFAULT NULL,
  `login_country` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_city` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_ip` varchar(15) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_hostname` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_onlineplayers` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`login_id`),
  KEY `ip_idx` (`login_ip`)
) ENGINE=InnoDB AUTO_INCREMENT=31398 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_orelog`
--

DROP TABLE IF EXISTS `player_orelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_orelog` (
  `orelog_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `orelog_material` int(10) unsigned DEFAULT NULL,
  `orelog_timestamp` int(10) unsigned DEFAULT NULL,
  `orelog_x` int(11) DEFAULT NULL,
  `orelog_y` int(11) DEFAULT NULL,
  `orelog_z` int(11) DEFAULT NULL,
  `orelog_world` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`orelog_id`),
  KEY `player_idx` (`player_id`,`orelog_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=21641 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`player_id`,`property_key`),
  KEY `key_idx` (`property_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `report_action` enum('kick','softwarn','hardwarn','ban','comment') CHARACTER SET latin1 NOT NULL,
  `report_message` text COLLATE utf8_swedish_ci NOT NULL,
  `report_timestamp` int(10) unsigned NOT NULL,
  `report_validuntil` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`report_id`),
  KEY `idx_subject` (`subject_id`,`report_timestamp`),
  KEY `idx_issuer` (`issuer_id`,`report_timestamp`)
) ENGINE=InnoDB AUTO_INCREMENT=3728 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player_transaction`
--

DROP TABLE IF EXISTS `player_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_transaction` (
  `transaction_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned DEFAULT NULL,
  `recipient_id` int(10) unsigned DEFAULT NULL,
  `transaction_timestamp` int(10) unsigned DEFAULT NULL,
  `transaction_amount` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4933 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shorturl`
--

DROP TABLE IF EXISTS `shorturl`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shorturl` (
  `urlID` int(11) NOT NULL AUTO_INCREMENT,
  `link` varchar(256) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`urlID`),
  UNIQUE KEY `urlID` (`urlID`)
) ENGINE=InnoDB AUTO_INCREMENT=2610 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stats_blocks`
--

DROP TABLE IF EXISTS `stats_blocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stats_blocks` (
  `checksum` double NOT NULL,
  `player` varchar(46) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `time` double NOT NULL,
  `status` smallint(6) NOT NULL,
  `blockid` double NOT NULL,
  `world` varchar(16) NOT NULL DEFAULT 'world',
  KEY `status` (`status`),
  KEY `blockid` (`blockid`),
  KEY `world` (`world`),
  KEY `time` (`time`),
  KEY `player` (`player`),
  KEY `checksum` (`checksum`,`player`,`x`,`y`,`z`,`time`,`world`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade`
--

DROP TABLE IF EXISTS `trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade` (
  `trade_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender_id` int(10) unsigned DEFAULT NULL,
  `recipient_id` int(10) unsigned DEFAULT NULL,
  `trade_timestamp` int(10) unsigned DEFAULT NULL,
  `trade_amount` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`trade_id`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_item`
--

DROP TABLE IF EXISTS `trade_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_item` (
  `item_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trade_id` int(10) unsigned DEFAULT NULL,
  `item_material` int(10) unsigned DEFAULT NULL,
  `item_data` int(10) unsigned DEFAULT NULL,
  `item_meta` text CHARACTER SET latin1,
  `item_count` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `warp`
--

DROP TABLE IF EXISTS `warp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warp` (
  `warp_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `warp_name` varchar(45) COLLATE utf8_swedish_ci DEFAULT NULL,
  `warp_x` double DEFAULT NULL,
  `warp_y` double DEFAULT NULL,
  `warp_z` double DEFAULT NULL,
  `warp_pitch` double DEFAULT NULL,
  `warp_yaw` double DEFAULT NULL,
  `warp_world` varchar(45) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`warp_id`),
  UNIQUE KEY `name.uniqe` (`warp_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1130 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `warp_log`
--

DROP TABLE IF EXISTS `warp_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `warp_log` (
  `log_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `warp_id` int(10) unsigned DEFAULT NULL,
  `log_timestamp` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`log_id`),
  KEY `idx_warp` (`warp_id`,`log_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `zone_communist` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '0',
  `zone_entermessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_exitmessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_texture` text COLLATE utf8_swedish_ci,
  `zone_owner` varchar(24) COLLATE utf8_swedish_ci DEFAULT NULL,
  PRIMARY KEY (`zone_id`),
  UNIQUE KEY `name` (`zone_name`)
) ENGINE=InnoDB AUTO_INCREMENT=916 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=11435 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=915 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-08-14 22:13:37
