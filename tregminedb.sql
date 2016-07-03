-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 16, 2016 at 01:40 AM
-- Server version: 5.7.11-log
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tregmine_temp`
--

-- --------------------------------------------------------

--
-- Table structure for table `bank`
--

CREATE TABLE `bank` (
  `bank_id` int(10) UNSIGNED NOT NULL,
  `lot_id` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `bank_account`
--

CREATE TABLE `bank_account` (
  `account_id` int(10) UNSIGNED NOT NULL,
  `bank_id` int(10) UNSIGNED DEFAULT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `account_balance` int(10) UNSIGNED DEFAULT NULL,
  `account_number` int(10) UNSIGNED DEFAULT NULL,
  `account_pin` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `bank_transaction`
--

CREATE TABLE `bank_transaction` (
  `transaction_id` int(10) UNSIGNED NOT NULL,
  `account_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED NOT NULL,
  `transaction_type` enum('deposit','withdrawal') NOT NULL,
  `transaction_amount` int(10) UNSIGNED NOT NULL,
  `transaction_timestamp` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `blessedblock`
--

CREATE TABLE `blessedblock` (
  `blessedblock_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `blessedblock_checksum` int(11) DEFAULT NULL,
  `blessedblock_x` int(11) DEFAULT NULL,
  `blessedblock_y` int(11) DEFAULT NULL,
  `blessedblock_z` int(11) DEFAULT NULL,
  `blessedblock_world` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `block_prices`
--

CREATE TABLE `block_prices` (
  `blockid` double NOT NULL,
  `price` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `block_stats`
--

CREATE TABLE `block_stats` (
  `checksum` double NOT NULL,
  `player` varchar(46) NOT NULL,
  `x` double NOT NULL,
  `y` double NOT NULL,
  `z` double NOT NULL,
  `time` double NOT NULL,
  `status` tinyint(1) NOT NULL,
  `blockid` double NOT NULL,
  `world` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `donation`
--

CREATE TABLE `donation` (
  `donation_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `donation_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `donation_amount` char(10) DEFAULT NULL,
  `donation_paypalid` varchar(64) DEFAULT NULL,
  `donation_payerid` varchar(64) DEFAULT NULL,
  `donation_email` varchar(255) DEFAULT NULL,
  `donation_firstname` varchar(255) DEFAULT NULL,
  `donation_lastname` varchar(255) DEFAULT NULL,
  `donation_message` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `enchantment`
--

CREATE TABLE `enchantment` (
  `enchantment_name` varchar(255) NOT NULL,
  `enchantment_title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `fishyblock`
--

CREATE TABLE `fishyblock` (
  `fishyblock_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `fishyblock_created` int(10) UNSIGNED DEFAULT NULL,
  `fishyblock_status` enum('active','deleted') DEFAULT 'active',
  `fishyblock_material` int(10) UNSIGNED DEFAULT NULL,
  `fishyblock_data` int(11) DEFAULT NULL,
  `fishyblock_enchantments` text,
  `fishyblock_cost` int(10) UNSIGNED DEFAULT NULL,
  `fishyblock_inventory` int(10) UNSIGNED DEFAULT NULL,
  `fishyblock_world` varchar(50) DEFAULT NULL,
  `fishyblock_blockx` int(11) DEFAULT NULL,
  `fishyblock_blocky` int(11) DEFAULT NULL,
  `fishyblock_blockz` int(11) DEFAULT NULL,
  `fishyblock_signx` int(11) DEFAULT NULL,
  `fishyblock_signy` int(11) DEFAULT NULL,
  `fishyblock_signz` int(11) DEFAULT NULL,
  `fishyblock_storedenchants` enum('0','1') DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `fishyblock_costlog`
--

CREATE TABLE `fishyblock_costlog` (
  `costlog_id` int(10) UNSIGNED NOT NULL,
  `fishyblock_id` int(10) UNSIGNED DEFAULT NULL,
  `costlog_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `costlog_newcost` int(10) UNSIGNED DEFAULT NULL,
  `costlog_oldcost` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `fishyblock_transaction`
--

CREATE TABLE `fishyblock_transaction` (
  `transaction_id` int(10) UNSIGNED NOT NULL,
  `fishyblock_id` int(10) UNSIGNED DEFAULT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `transaction_type` enum('deposit','withdraw','buy') DEFAULT NULL,
  `transaction_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `transaction_amount` int(10) UNSIGNED DEFAULT NULL,
  `transaction_unitcost` int(10) UNSIGNED DEFAULT NULL,
  `transaction_totalcost` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inventory`
--

CREATE TABLE `inventory` (
  `inventory_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `inventory_checksum` int(11) DEFAULT NULL,
  `inventory_x` int(11) DEFAULT NULL,
  `inventory_y` int(11) DEFAULT NULL,
  `inventory_z` int(11) DEFAULT NULL,
  `inventory_world` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inventory_player` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `inventory_type` enum('block','player','player_armor') COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `inventory_accesslog`
--

CREATE TABLE `inventory_accesslog` (
  `accesslog_id` int(10) UNSIGNED NOT NULL,
  `inventory_id` int(10) UNSIGNED DEFAULT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `accesslog_timestamp` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inventory_changelog`
--

CREATE TABLE `inventory_changelog` (
  `changelog_id` int(10) UNSIGNED NOT NULL,
  `inventory_id` int(10) UNSIGNED DEFAULT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `changelog_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `changelog_slot` int(10) UNSIGNED DEFAULT NULL,
  `changelog_material` int(10) UNSIGNED DEFAULT NULL,
  `changelog_data` int(11) DEFAULT NULL,
  `changelog_meta` text,
  `changelog_amount` int(10) UNSIGNED DEFAULT NULL,
  `changelog_type` enum('add','remove') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `inventory_item`
--

CREATE TABLE `inventory_item` (
  `item_id` int(10) UNSIGNED NOT NULL,
  `inventory_id` int(10) UNSIGNED DEFAULT NULL,
  `item_slot` int(10) UNSIGNED DEFAULT NULL,
  `item_material` int(10) UNSIGNED DEFAULT NULL,
  `item_data` int(11) DEFAULT NULL,
  `item_meta` text COLLATE utf8_unicode_ci,
  `item_count` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `item`
--

CREATE TABLE `item` (
  `id` int(255) NOT NULL,
  `item_id` int(255) NOT NULL,
  `item_data` int(255) NOT NULL DEFAULT '0',
  `item_name` varchar(255) NOT NULL,
  `enchantable` enum('no','yes') NOT NULL DEFAULT 'no',
  `sellable` enum('no','yes') NOT NULL DEFAULT 'no',
  `item_value` int(255) NOT NULL DEFAULT '0',
  `mine_value` int(8) NOT NULL DEFAULT '0',
  `auctionable` enum('no','yes') NOT NULL DEFAULT 'no',
  `link` varchar(255) NOT NULL,
  `round` enum('no','yes') NOT NULL DEFAULT 'no',
  `item_sign` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `mentorlog`
--

CREATE TABLE `mentorlog` (
  `mentorlog_id` int(10) UNSIGNED NOT NULL,
  `student_id` int(10) UNSIGNED DEFAULT NULL,
  `mentor_id` int(10) UNSIGNED DEFAULT NULL,
  `mentorlog_resumed` int(10) UNSIGNED DEFAULT '0',
  `mentorlog_startedtime` int(10) UNSIGNED DEFAULT NULL,
  `mentorlog_completedtime` int(10) UNSIGNED DEFAULT '0',
  `mentorlog_cancelledtime` int(10) UNSIGNED DEFAULT '0',
  `mentorlog_status` enum('started','completed','cancelled') DEFAULT 'started',
  `mentorlog_channel` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `misc_message`
--

CREATE TABLE `misc_message` (
  `message_type` varchar(256) NOT NULL,
  `message_value` varchar(256) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `motd`
--

CREATE TABLE `motd` (
  `motd_id` int(10) UNSIGNED NOT NULL,
  `motd_timestamp` int(10) UNSIGNED NOT NULL,
  `motd_message` text CHARACTER SET utf8
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `player_id` int(10) UNSIGNED NOT NULL,
  `player_name` varchar(46) COLLATE utf8_unicode_ci DEFAULT NULL,
  `player_password` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `player_email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `player_confirmed` enum('0','1') COLLATE utf8_unicode_ci DEFAULT '0',
  `player_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `player_wallet` bigint(20) DEFAULT '50000',
  `player_rank` enum('unverified','tourist','settler','resident','donator','guardian','builder','coder','junior_admin','senior_admin') COLLATE utf8_unicode_ci DEFAULT 'unverified',
  `player_flags` int(10) UNSIGNED DEFAULT NULL,
  `player_keywords` text COLLATE utf8_unicode_ci NOT NULL,
  `player_ignore` text COLLATE utf8_unicode_ci,
  `player_uuid` char(43) COLLATE utf8_unicode_ci NOT NULL,
  `player_inventory` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `playerinventory`
--

CREATE TABLE `playerinventory` (
  `playerinventory_id` int(10) NOT NULL,
  `player_id` int(10) NOT NULL,
  `playerinventory_name` varchar(255) DEFAULT NULL,
  `playerinventory_type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `playerinventory_item`
--

CREATE TABLE `playerinventory_item` (
  `item_id` int(10) NOT NULL,
  `playerinventory_id` int(10) DEFAULT NULL,
  `item_slot` int(10) DEFAULT NULL,
  `item_material` int(10) DEFAULT NULL,
  `item_data` int(11) DEFAULT NULL,
  `item_meta` text,
  `item_count` int(10) DEFAULT NULL,
  `item_durability` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `player_badge`
--

CREATE TABLE `player_badge` (
  `badge_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED NOT NULL,
  `badge_name` varchar(255) NOT NULL,
  `badge_level` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `badge_timestamp` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `player_chatlog`
--

CREATE TABLE `player_chatlog` (
  `chatlog_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `chatlog_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `chatlog_channel` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `chatlog_message` varchar(255) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_givelog`
--

CREATE TABLE `player_givelog` (
  `givelog_id` int(10) UNSIGNED NOT NULL,
  `sender_id` int(10) UNSIGNED DEFAULT NULL,
  `recipient_id` int(10) UNSIGNED DEFAULT NULL,
  `givelog_material` int(10) UNSIGNED DEFAULT NULL,
  `givelog_data` int(11) DEFAULT NULL,
  `givelog_meta` text CHARACTER SET utf8,
  `givelog_count` int(10) UNSIGNED DEFAULT NULL,
  `givelog_timestamp` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_home`
--

CREATE TABLE `player_home` (
  `home_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `home_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `home_x` double DEFAULT NULL,
  `home_y` double DEFAULT NULL,
  `home_z` double DEFAULT NULL,
  `home_pitch` double DEFAULT NULL,
  `home_yaw` double DEFAULT NULL,
  `home_world` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `home_time` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_login`
--

CREATE TABLE `player_login` (
  `login_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `login_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `login_action` enum('login','logout') CHARACTER SET utf8 DEFAULT NULL,
  `login_country` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_city` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_ip` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_hostname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `login_onlineplayers` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_mail`
--

CREATE TABLE `player_mail` (
  `sender_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `receiver_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  `deleted` varchar(5) COLLATE utf8_unicode_ci NOT NULL,
  `mail_id` int(11) NOT NULL,
  `timestamp` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_orelog`
--

CREATE TABLE `player_orelog` (
  `orelog_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `orelog_material` int(10) UNSIGNED DEFAULT NULL,
  `orelog_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `orelog_x` int(11) DEFAULT NULL,
  `orelog_y` int(11) DEFAULT NULL,
  `orelog_z` int(11) DEFAULT NULL,
  `orelog_world` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_property`
--

CREATE TABLE `player_property` (
  `player_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `property_key` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `property_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `property_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_report`
--

CREATE TABLE `player_report` (
  `report_id` int(10) UNSIGNED NOT NULL,
  `subject_id` int(10) UNSIGNED NOT NULL,
  `issuer_id` int(10) UNSIGNED NOT NULL,
  `report_action` enum('kick','softwarn','hardwarn','ban','comment') CHARACTER SET utf8 NOT NULL,
  `report_message` text COLLATE utf8_unicode_ci NOT NULL,
  `report_timestamp` int(10) UNSIGNED NOT NULL,
  `report_validuntil` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_transaction`
--

CREATE TABLE `player_transaction` (
  `transaction_id` int(10) UNSIGNED NOT NULL,
  `sender_id` int(10) UNSIGNED DEFAULT NULL,
  `recipient_id` int(10) UNSIGNED DEFAULT NULL,
  `transaction_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `transaction_amount` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_webcookie`
--

CREATE TABLE `player_webcookie` (
  `webcookie_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `webcookie_nonce` char(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `shorturl`
--

CREATE TABLE `shorturl` (
  `urlID` int(11) NOT NULL,
  `link` varchar(256) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `staffnews`
--

CREATE TABLE `staffnews` (
  `id` int(10) NOT NULL,
  `username` varchar(64) NOT NULL,
  `text` text NOT NULL,
  `timestamp` int(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `staff_handbook`
--

CREATE TABLE `staff_handbook` (
  `rulenum` varchar(4) DEFAULT NULL,
  `rule` varchar(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `stats_blocks`
--

CREATE TABLE `stats_blocks` (
  `checksum` double NOT NULL,
  `player` varchar(46) NOT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `time` double NOT NULL,
  `status` smallint(6) NOT NULL,
  `blockid` double NOT NULL,
  `world` varchar(16) NOT NULL DEFAULT 'world'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `trade`
--

CREATE TABLE `trade` (
  `trade_id` int(10) UNSIGNED NOT NULL,
  `sender_id` int(10) UNSIGNED DEFAULT NULL,
  `recipient_id` int(10) UNSIGNED DEFAULT NULL,
  `trade_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `trade_amount` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `trade_item`
--

CREATE TABLE `trade_item` (
  `item_id` int(10) UNSIGNED NOT NULL,
  `trade_id` int(10) UNSIGNED DEFAULT NULL,
  `item_material` int(10) UNSIGNED DEFAULT NULL,
  `item_data` int(11) DEFAULT NULL,
  `item_meta` text CHARACTER SET utf8,
  `item_count` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `version`
--

CREATE TABLE `version` (
  `version_id` int(255) NOT NULL,
  `version_number` varchar(255) NOT NULL,
  `version_string` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `warp`
--

CREATE TABLE `warp` (
  `warp_id` int(10) UNSIGNED NOT NULL,
  `warp_name` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `warp_x` double DEFAULT NULL,
  `warp_y` double DEFAULT NULL,
  `warp_z` double DEFAULT NULL,
  `warp_pitch` double DEFAULT NULL,
  `warp_yaw` double DEFAULT NULL,
  `warp_world` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hidden` varchar(5) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'false'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `warp_log`
--

CREATE TABLE `warp_log` (
  `log_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `warp_id` int(10) UNSIGNED DEFAULT NULL,
  `log_timestamp` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone`
--

CREATE TABLE `zone` (
  `zone_id` int(11) NOT NULL,
  `zone_world` varchar(50) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL DEFAULT 'world',
  `zone_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `zone_enterdefault` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_placedefault` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_destroydefault` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_pvp` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  `zone_hostiles` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci DEFAULT '1',
  `zone_communist` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci DEFAULT '0',
  `zone_publicprofile` enum('0','1') CHARACTER SET utf8 COLLATE utf8_swedish_ci DEFAULT '0',
  `zone_entermessage` varchar(250) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `zone_exitmessage` varchar(250) CHARACTER SET utf8 COLLATE utf8_swedish_ci NOT NULL,
  `zone_texture` text CHARACTER SET utf8 COLLATE utf8_swedish_ci,
  `zone_owner` varchar(24) CHARACTER SET utf8 COLLATE utf8_swedish_ci DEFAULT NULL,
  `zone_flags` int(10) UNSIGNED DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone_lot`
--

CREATE TABLE `zone_lot` (
  `lot_id` int(10) NOT NULL,
  `zone_id` int(10) NOT NULL,
  `lot_name` varchar(50) NOT NULL,
  `lot_x1` int(10) NOT NULL,
  `lot_y1` int(10) NOT NULL,
  `lot_x2` int(10) NOT NULL,
  `lot_y2` int(10) NOT NULL,
  `special` int(11) DEFAULT NULL,
  `lot_flags` int(10) UNSIGNED NOT NULL DEFAULT '3'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone_lotuser`
--

CREATE TABLE `zone_lotuser` (
  `lot_id` int(10) NOT NULL DEFAULT '0',
  `user_id` int(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone_profile`
--

CREATE TABLE `zone_profile` (
  `profile_id` int(10) UNSIGNED NOT NULL,
  `zone_id` int(10) UNSIGNED DEFAULT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `profile_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `profile_text` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone_rect`
--

CREATE TABLE `zone_rect` (
  `rect_id` int(10) NOT NULL,
  `zone_id` int(10) DEFAULT NULL,
  `rect_x1` int(10) DEFAULT NULL,
  `rect_y1` int(10) DEFAULT NULL,
  `rect_x2` int(10) DEFAULT NULL,
  `rect_y2` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `zone_user`
--

CREATE TABLE `zone_user` (
  `zone_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `user_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `user_perm` enum('owner','maker','allowed','banned') NOT NULL DEFAULT 'allowed'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bank`
--
ALTER TABLE `bank`
  ADD PRIMARY KEY (`bank_id`),
  ADD UNIQUE KEY `idx_lot` (`lot_id`);

--
-- Indexes for table `bank_account`
--
ALTER TABLE `bank_account`
  ADD PRIMARY KEY (`account_id`),
  ADD KEY `idx_bank` (`bank_id`,`player_id`),
  ADD KEY `idx_accountnum2` (`bank_id`,`account_number`);

--
-- Indexes for table `bank_transaction`
--
ALTER TABLE `bank_transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `idx_account` (`account_id`,`transaction_timestamp`),
  ADD KEY `idx_player` (`player_id`,`transaction_timestamp`);

--
-- Indexes for table `blessedblock`
--
ALTER TABLE `blessedblock`
  ADD PRIMARY KEY (`blessedblock_id`);

--
-- Indexes for table `donation`
--
ALTER TABLE `donation`
  ADD PRIMARY KEY (`donation_id`),
  ADD KEY `idx_player` (`player_id`);

--
-- Indexes for table `enchantment`
--
ALTER TABLE `enchantment`
  ADD PRIMARY KEY (`enchantment_name`);

--
-- Indexes for table `fishyblock`
--
ALTER TABLE `fishyblock`
  ADD PRIMARY KEY (`fishyblock_id`),
  ADD KEY `player_idx` (`player_id`);

--
-- Indexes for table `fishyblock_costlog`
--
ALTER TABLE `fishyblock_costlog`
  ADD PRIMARY KEY (`costlog_id`),
  ADD KEY `idx_fishyblock` (`fishyblock_id`,`costlog_timestamp`);

--
-- Indexes for table `fishyblock_transaction`
--
ALTER TABLE `fishyblock_transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `idx_fishyblock` (`fishyblock_id`,`transaction_timestamp`),
  ADD KEY `idx_player` (`player_id`);

--
-- Indexes for table `inventory`
--
ALTER TABLE `inventory`
  ADD PRIMARY KEY (`inventory_id`),
  ADD KEY `idx_player` (`inventory_player`),
  ADD KEY `idx_coords` (`inventory_x`,`inventory_y`,`inventory_z`,`inventory_world`);

--
-- Indexes for table `inventory_accesslog`
--
ALTER TABLE `inventory_accesslog`
  ADD PRIMARY KEY (`accesslog_id`),
  ADD KEY `idx_inventory` (`inventory_id`,`accesslog_timestamp`);

--
-- Indexes for table `inventory_changelog`
--
ALTER TABLE `inventory_changelog`
  ADD PRIMARY KEY (`changelog_id`),
  ADD KEY `idx_inventory` (`inventory_id`,`changelog_timestamp`);

--
-- Indexes for table `inventory_item`
--
ALTER TABLE `inventory_item`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `inventory_idx` (`inventory_id`);

--
-- Indexes for table `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mentorlog`
--
ALTER TABLE `mentorlog`
  ADD PRIMARY KEY (`mentorlog_id`),
  ADD UNIQUE KEY `idx_student` (`student_id`,`mentor_id`),
  ADD UNIQUE KEY `idx_mentor` (`mentor_id`,`student_id`);

--
-- Indexes for table `misc_message`
--
ALTER TABLE `misc_message`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `motd`
--
ALTER TABLE `motd`
  ADD PRIMARY KEY (`motd_id`);

--
-- Indexes for table `player`
--
ALTER TABLE `player`
  ADD PRIMARY KEY (`player_id`),
  ADD UNIQUE KEY `name` (`player_name`);

--
-- Indexes for table `playerinventory`
--
ALTER TABLE `playerinventory`
  ADD PRIMARY KEY (`playerinventory_id`),
  ADD KEY `idx_player` (`player_id`);

--
-- Indexes for table `playerinventory_item`
--
ALTER TABLE `playerinventory_item`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `idx_inv` (`playerinventory_id`);

--
-- Indexes for table `player_badge`
--
ALTER TABLE `player_badge`
  ADD PRIMARY KEY (`badge_id`),
  ADD UNIQUE KEY `badge_idx` (`player_id`,`badge_name`);

--
-- Indexes for table `player_chatlog`
--
ALTER TABLE `player_chatlog`
  ADD PRIMARY KEY (`chatlog_id`);

--
-- Indexes for table `player_givelog`
--
ALTER TABLE `player_givelog`
  ADD PRIMARY KEY (`givelog_id`);

--
-- Indexes for table `player_home`
--
ALTER TABLE `player_home`
  ADD PRIMARY KEY (`home_id`),
  ADD KEY `player_idx` (`player_id`,`home_time`),
  ADD KEY `idx_player` (`home_name`);

--
-- Indexes for table `player_login`
--
ALTER TABLE `player_login`
  ADD PRIMARY KEY (`login_id`),
  ADD KEY `ip_idx` (`login_ip`),
  ADD KEY `player_idx` (`player_id`,`login_timestamp`);

--
-- Indexes for table `player_mail`
--
ALTER TABLE `player_mail`
  ADD UNIQUE KEY `mail_id` (`mail_id`);

--
-- Indexes for table `player_orelog`
--
ALTER TABLE `player_orelog`
  ADD PRIMARY KEY (`orelog_id`),
  ADD KEY `player_idx` (`player_id`,`orelog_timestamp`);

--
-- Indexes for table `player_property`
--
ALTER TABLE `player_property`
  ADD PRIMARY KEY (`player_id`,`property_key`),
  ADD KEY `key_idx` (`property_key`);

--
-- Indexes for table `player_report`
--
ALTER TABLE `player_report`
  ADD PRIMARY KEY (`report_id`),
  ADD KEY `idx_subject` (`subject_id`,`report_timestamp`),
  ADD KEY `idx_issuer` (`issuer_id`,`report_timestamp`);

--
-- Indexes for table `player_transaction`
--
ALTER TABLE `player_transaction`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `idx_sender` (`sender_id`,`transaction_timestamp`),
  ADD KEY `idx_recipient` (`recipient_id`,`transaction_timestamp`);

--
-- Indexes for table `player_webcookie`
--
ALTER TABLE `player_webcookie`
  ADD PRIMARY KEY (`webcookie_id`),
  ADD UNIQUE KEY `idx_nonce` (`webcookie_nonce`);

--
-- Indexes for table `shorturl`
--
ALTER TABLE `shorturl`
  ADD PRIMARY KEY (`urlID`),
  ADD UNIQUE KEY `urlID` (`urlID`);

--
-- Indexes for table `staffnews`
--
ALTER TABLE `staffnews`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `stats_blocks`
--
ALTER TABLE `stats_blocks`
  ADD KEY `checksum` (`world`,`checksum`,`time`),
  ADD KEY `coords` (`world`,`x`,`y`,`z`,`time`);

--
-- Indexes for table `trade`
--
ALTER TABLE `trade`
  ADD PRIMARY KEY (`trade_id`);

--
-- Indexes for table `trade_item`
--
ALTER TABLE `trade_item`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `idx_trade_id` (`trade_id`);

--
-- Indexes for table `version`
--
ALTER TABLE `version`
  ADD PRIMARY KEY (`version_id`);

--
-- Indexes for table `warp`
--
ALTER TABLE `warp`
  ADD PRIMARY KEY (`warp_id`),
  ADD UNIQUE KEY `name.uniqe` (`warp_name`);

--
-- Indexes for table `warp_log`
--
ALTER TABLE `warp_log`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `idx_warp` (`warp_id`,`log_timestamp`);

--
-- Indexes for table `zone`
--
ALTER TABLE `zone`
  ADD PRIMARY KEY (`zone_id`),
  ADD UNIQUE KEY `name` (`zone_name`);

--
-- Indexes for table `zone_lot`
--
ALTER TABLE `zone_lot`
  ADD PRIMARY KEY (`lot_id`);

--
-- Indexes for table `zone_lotuser`
--
ALTER TABLE `zone_lotuser`
  ADD PRIMARY KEY (`lot_id`,`user_id`);

--
-- Indexes for table `zone_profile`
--
ALTER TABLE `zone_profile`
  ADD PRIMARY KEY (`profile_id`),
  ADD KEY `idx_zone_id` (`zone_id`);

--
-- Indexes for table `zone_rect`
--
ALTER TABLE `zone_rect`
  ADD PRIMARY KEY (`rect_id`),
  ADD KEY `zone_id` (`zone_id`);

--
-- Indexes for table `zone_user`
--
ALTER TABLE `zone_user`
  ADD PRIMARY KEY (`zone_id`,`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bank`
--
ALTER TABLE `bank`
  MODIFY `bank_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `bank_account`
--
ALTER TABLE `bank_account`
  MODIFY `account_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `bank_transaction`
--
ALTER TABLE `bank_transaction`
  MODIFY `transaction_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `blessedblock`
--
ALTER TABLE `blessedblock`
  MODIFY `blessedblock_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `donation`
--
ALTER TABLE `donation`
  MODIFY `donation_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fishyblock`
--
ALTER TABLE `fishyblock`
  MODIFY `fishyblock_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fishyblock_costlog`
--
ALTER TABLE `fishyblock_costlog`
  MODIFY `costlog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fishyblock_transaction`
--
ALTER TABLE `fishyblock_transaction`
  MODIFY `transaction_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `inventory`
--
ALTER TABLE `inventory`
  MODIFY `inventory_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3354;
--
-- AUTO_INCREMENT for table `inventory_accesslog`
--
ALTER TABLE `inventory_accesslog`
  MODIFY `accesslog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9175;
--
-- AUTO_INCREMENT for table `inventory_changelog`
--
ALTER TABLE `inventory_changelog`
  MODIFY `changelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9255;
--
-- AUTO_INCREMENT for table `inventory_item`
--
ALTER TABLE `inventory_item`
  MODIFY `item_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=75488;
--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=399;
--
-- AUTO_INCREMENT for table `mentorlog`
--
ALTER TABLE `mentorlog`
  MODIFY `mentorlog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `misc_message`
--
ALTER TABLE `misc_message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=346;
--
-- AUTO_INCREMENT for table `motd`
--
ALTER TABLE `motd`
  MODIFY `motd_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `player_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;
--
-- AUTO_INCREMENT for table `playerinventory`
--
ALTER TABLE `playerinventory`
  MODIFY `playerinventory_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9245;
--
-- AUTO_INCREMENT for table `playerinventory_item`
--
ALTER TABLE `playerinventory_item`
  MODIFY `item_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=498587;
--
-- AUTO_INCREMENT for table `player_badge`
--
ALTER TABLE `player_badge`
  MODIFY `badge_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_chatlog`
--
ALTER TABLE `player_chatlog`
  MODIFY `chatlog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12020;
--
-- AUTO_INCREMENT for table `player_givelog`
--
ALTER TABLE `player_givelog`
  MODIFY `givelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=109;
--
-- AUTO_INCREMENT for table `player_home`
--
ALTER TABLE `player_home`
  MODIFY `home_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT for table `player_login`
--
ALTER TABLE `player_login`
  MODIFY `login_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1604;
--
-- AUTO_INCREMENT for table `player_mail`
--
ALTER TABLE `player_mail`
  MODIFY `mail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `player_orelog`
--
ALTER TABLE `player_orelog`
  MODIFY `orelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1827;
--
-- AUTO_INCREMENT for table `player_report`
--
ALTER TABLE `player_report`
  MODIFY `report_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `player_transaction`
--
ALTER TABLE `player_transaction`
  MODIFY `transaction_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_webcookie`
--
ALTER TABLE `player_webcookie`
  MODIFY `webcookie_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `shorturl`
--
ALTER TABLE `shorturl`
  MODIFY `urlID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `staffnews`
--
ALTER TABLE `staffnews`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `trade`
--
ALTER TABLE `trade`
  MODIFY `trade_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `trade_item`
--
ALTER TABLE `trade_item`
  MODIFY `item_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `version`
--
ALTER TABLE `version`
  MODIFY `version_id` int(255) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `warp`
--
ALTER TABLE `warp`
  MODIFY `warp_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=283;
--
-- AUTO_INCREMENT for table `warp_log`
--
ALTER TABLE `warp_log`
  MODIFY `log_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1135;
--
-- AUTO_INCREMENT for table `zone`
--
ALTER TABLE `zone`
  MODIFY `zone_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1039;
--
-- AUTO_INCREMENT for table `zone_lot`
--
ALTER TABLE `zone_lot`
  MODIFY `lot_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13140;
--
-- AUTO_INCREMENT for table `zone_profile`
--
ALTER TABLE `zone_profile`
  MODIFY `profile_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=158;
--
-- AUTO_INCREMENT for table `zone_rect`
--
ALTER TABLE `zone_rect`
  MODIFY `rect_id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1036;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
