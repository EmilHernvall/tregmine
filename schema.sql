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
  `inventory_type` enum('block','player','player_armor') COLLATE utf8_swedish_ci DEFAULT 'block',
  PRIMARY KEY (`inventory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,1,-219894647,-119,71,36,'world','block'),(2,1,0,0,0,0,'','player'),(3,1,0,0,0,0,'','player_armor'),(4,3,0,0,0,0,'','player'),(5,3,0,0,0,0,'','player_armor');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

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
  `item_meta` text,
  `item_count` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `inventory_idx` (`inventory_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_item`
--

LOCK TABLES `inventory_item` WRITE;
/*!40000 ALTER TABLE `inventory_item` DISABLE KEYS */;
INSERT INTO `inventory_item` VALUES (120,2,0,276,13,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(121,2,1,269,1,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(122,2,2,340,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(123,2,3,339,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(124,2,4,345,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(125,2,5,280,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',7),(126,2,9,322,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',37),(127,3,0,313,1,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(128,3,1,312,1,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(129,3,2,311,1,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(130,3,3,310,1,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',1),(131,4,0,12,0,'',1),(132,4,1,367,0,'',2),(133,4,2,280,0,'meta:\n  ==: ItemMeta\n  meta-type: UNSPECIFIC\n  lore:\n  - §cSPAWNED\n  - \'§fby: knipil\'\n  - \'§fValue: §k0000§r§f Treg\'\n',4),(134,5,0,0,-1,'',0),(135,5,1,0,-1,'',0),(136,5,2,0,-1,'',0),(137,5,3,0,-1,'',0);
/*!40000 ALTER TABLE `inventory_item` ENABLE KEYS */;
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
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'knipil',NULL,NULL,'0','2013-06-28 20:16:09',7341),(2,'TheScavenger101',NULL,NULL,'0','2013-07-03 14:05:58',11336),(3,'mejjad',NULL,NULL,'0','2013-07-04 19:00:49',11323);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

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
  `chatlog_channel` varchar(64) DEFAULT NULL,
  `chatlog_message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`chatlog_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_chatlog`
--

LOCK TABLES `player_chatlog` WRITE;
/*!40000 ALTER TABLE `player_chatlog` DISABLE KEYS */;
INSERT INTO `player_chatlog` VALUES (1,3,1372964453,'GLOBAL','vilken ära'),(2,1,1372964456,'GLOBAL',':>'),(3,1,1372964508,'GLOBAL','ge mig två min att felsöka'),(4,3,1372964515,'GLOBAL','jag ska ingenstans'),(5,3,1372964526,'GLOBAL','well. går och lägger mig om 1,5h'),(6,3,1372964533,'GLOBAL','2h'),(7,1,1372964550,'GLOBAL','ah, ser felet'),(8,3,1372964708,'GLOBAL','nice'),(9,1,1372964710,'GLOBAL','såja. :)'),(10,3,1372964742,'GLOBAL','tybm'),(11,3,1372964745,'GLOBAL','tradea mig'),(12,3,1372964765,'GLOBAL','biddade 7,1'),(13,1,1372964778,'GLOBAL','Ah.'),(14,1,1372964805,'GLOBAL','ser felet. :)'),(15,1,1372964807,'GLOBAL','fixat.'),(16,1,1372964819,'GLOBAL','den skickade ett felmeddelande men avbröt inte'),(17,1,1372964855,'GLOBAL','men ganska smidigt, va?'),(18,3,1372964877,'GLOBAL',':)'),(19,3,1372964897,'GLOBAL','prova igen?'),(20,1,1372964936,'GLOBAL','får starta om bara'),(21,1,1372965327,'GLOBAL','stänger ned den här servern och laddar upp till test-servern');
/*!40000 ALTER TABLE `player_chatlog` ENABLE KEYS */;
UNLOCK TABLES;

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
  `givelog_data` int(10) unsigned DEFAULT NULL,
  `givelog_meta` text,
  `givelog_count` int(10) unsigned DEFAULT NULL,
  `givelog_timestamp` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`givelog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_givelog`
--

LOCK TABLES `player_givelog` WRITE;
/*!40000 ALTER TABLE `player_givelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_givelog` ENABLE KEYS */;
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
-- Table structure for table `player_login`
--

DROP TABLE IF EXISTS `player_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player_login` (
  `login_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned DEFAULT NULL,
  `login_timestamp` int(10) unsigned DEFAULT NULL,
  `login_action` enum('login','logout') DEFAULT NULL,
  PRIMARY KEY (`login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_login`
--

LOCK TABLES `player_login` WRITE;
/*!40000 ALTER TABLE `player_login` DISABLE KEYS */;
INSERT INTO `player_login` VALUES (1,1,1372933818,'login'),(2,1,1372933912,'logout'),(3,1,1372934104,'login'),(4,1,1372934151,'logout'),(5,1,1372935356,'login'),(6,1,1372935514,'logout'),(7,1,1372935536,'login'),(8,1,1372935636,'logout'),(9,1,1372935665,'login'),(10,1,1372935701,'logout'),(11,1,1372936656,'login'),(12,1,1372936757,'logout'),(13,1,1372940473,'login'),(14,1,1372940478,'logout'),(15,1,1372940504,'login'),(16,1,1372940509,'logout'),(17,1,1372940540,'login'),(18,1,1372940549,'logout'),(19,1,1372940881,'login'),(20,1,1372940885,'logout'),(21,1,1372940911,'login'),(22,1,1372940914,'logout'),(23,1,1372940941,'login'),(24,1,1372940945,'logout'),(25,1,1372941911,'login'),(26,1,1372941925,'logout'),(27,1,1372941950,'login'),(28,1,1372941969,'logout'),(29,1,1372942111,'login'),(30,1,1372942120,'logout'),(31,1,1372942266,'login'),(32,1,1372942289,'logout'),(33,1,1372942305,'login'),(34,1,1372942321,'logout'),(35,1,1372942354,'login'),(36,1,1372942393,'logout'),(37,1,1372942986,'login'),(38,1,1372943025,'logout'),(39,1,1372943058,'login'),(40,1,1372943070,'logout'),(41,1,1372943601,'login'),(42,1,1372943616,'logout'),(43,1,1372943626,'login'),(44,1,1372943640,'logout'),(45,1,1372955780,'login'),(46,1,1372955814,'logout'),(47,1,1372964418,'login'),(48,3,1372964449,'login'),(49,1,1372964600,'logout'),(50,3,1372964600,'logout'),(51,3,1372964608,'login'),(52,1,1372964633,'login'),(53,3,1372964989,'logout'),(54,1,1372964989,'logout'),(55,3,1372964996,'login'),(56,1,1372965009,'login'),(57,3,1372965068,'logout'),(58,1,1372965068,'logout'),(59,1,1372965101,'login'),(60,3,1372965108,'login'),(61,1,1372965230,'logout'),(62,1,1372965233,'login'),(63,1,1372965333,'logout'),(64,3,1372965393,'logout');
/*!40000 ALTER TABLE `player_login` ENABLE KEYS */;
UNLOCK TABLES;

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
  `orelog_world` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`orelog_id`),
  KEY `player_idx` (`player_id`,`orelog_timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_orelog`
--

LOCK TABLES `player_orelog` WRITE;
/*!40000 ALTER TABLE `player_orelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_orelog` ENABLE KEYS */;
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
INSERT INTO `player_property` VALUES (1,'ip','127.0.0.1','2013-07-04 19:13:53'),(1,'hostName','localhost','2013-07-04 19:13:53'),(1,'trusted','true','2013-06-30 20:36:28'),(1,'admin','true','2013-06-28 20:25:51'),(1,'donator','true','2013-06-28 20:26:02'),(1,'color','vampire','2013-07-04 19:13:53'),(1,'timezone','Europe/Stockholm','2013-07-04 19:13:53'),(2,'countryName','Sweden','2013-07-03 14:15:33'),(2,'city','Sundsbruk','2013-07-03 14:15:33'),(2,'ip','217.210.245.120','2013-07-03 14:15:33'),(2,'region','24','2013-07-03 14:15:33'),(2,'hostName','h120n6-sv-a13.ias.bredband.telia.com','2013-07-03 14:15:33'),(2,'color','trial','2013-07-03 14:15:33'),(2,'timezone','Europe/Stockholm','2013-07-03 14:15:33'),(3,'countryName','Sweden','2013-07-04 19:11:48'),(3,'city','Stockholm','2013-07-04 19:11:48'),(3,'ip','80.217.255.134','2013-07-04 19:11:48'),(3,'region','26','2013-07-04 19:11:48'),(3,'hostName','c80-217-255-134.bredband.comhem.se','2013-07-04 19:11:48'),(3,'color','white','2013-07-04 19:11:48'),(3,'timezone','Europe/Stockholm','2013-07-04 19:11:48'),(3,'admin','false','2013-07-04 19:12:16'),(3,'builder','false','2013-07-04 19:12:16'),(3,'child','false','2013-07-04 19:12:16'),(3,'donator','false','2013-07-04 19:12:16'),(3,'banned','false','2013-07-04 19:12:16'),(3,'trusted','true','2013-07-04 19:12:16');
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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_report`
--

LOCK TABLES `player_report` WRITE;
/*!40000 ALTER TABLE `player_report` DISABLE KEYS */;
INSERT INTO `player_report` VALUES (1,1,1,'hardwarn',' you are warned!',1372624179,1372624179),(2,1,1,'ban',' it\'s the end of the world as we know it',1372624498,1372624498);
/*!40000 ALTER TABLE `player_report` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_transaction`
--

LOCK TABLES `player_transaction` WRITE;
/*!40000 ALTER TABLE `player_transaction` DISABLE KEYS */;
INSERT INTO `player_transaction` VALUES (1,3,1,1372964706,7),(2,1,3,1372964739,1337),(3,3,1,1372964762,0),(4,3,1,1372965227,0);
/*!40000 ALTER TABLE `player_transaction` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `stats_blocks`
--

LOCK TABLES `stats_blocks` WRITE;
/*!40000 ALTER TABLE `stats_blocks` DISABLE KEYS */;
INSERT INTO `stats_blocks` VALUES (532537929,'knipil',-220,70,-460,1372882844214,0,12,'world'),(539206362,'knipil',-226,70,-459,1372882850114,1,12,'world'),(539206362,'knipil',-226,70,-459,1372882857215,0,12,'world'),(539206362,'knipil',-226,70,-459,1372882866612,1,3,'world'),(418929516,'knipil',-223,70,-456,1372935366804,1,54,'world'),(3139612096,'knipil',-221,70,-459,1372935584059,1,54,'world'),(3135293431,'knipil',-221,70,-458,1372935675096,1,54,'world'),(511645876,'mejjad',-201,77,163,1372964487324,0,17,'world'),(511645876,'mejjad',-201,77,163,1372964490618,0,17,'world'),(827021829,'mejjad',-201,75,162,1372964501666,0,2,'world'),(299904612,'mejjad',-212,78,157,1372964620088,0,17,'world'),(3651360990,'mejjad',-212,77,160,1372964626032,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964626035,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964626729,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964626730,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964626882,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964626884,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627233,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627383,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627580,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627583,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627783,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964627987,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628136,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628139,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628335,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628382,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628488,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628683,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628685,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964628829,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964629032,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964629235,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964629428,0,31,'world'),(3651360990,'mejjad',-212,77,160,1372964629578,0,31,'world'),(2697464904,'mejjad',-215,77,158,1372965009584,1,12,'world'),(3161387550,'mejjad',-215,77,160,1372965168217,1,12,'world'),(2829966202,'mejjad',-214,80,158,1372965179267,0,78,'world'),(1560295091,'mejjad',-217,77,160,1372965182268,0,31,'world');
/*!40000 ALTER TABLE `stats_blocks` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade`
--

LOCK TABLES `trade` WRITE;
/*!40000 ALTER TABLE `trade` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade` ENABLE KEYS */;
UNLOCK TABLES;

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
  `item_meta` text,
  `item_count` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trade_item`
--

LOCK TABLES `trade_item` WRITE;
/*!40000 ALTER TABLE `trade_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `trade_item` ENABLE KEYS */;
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

-- Dump completed on 2013-07-11 22:52:14
