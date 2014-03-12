-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 04, 2014 at 11:23 PM
-- Server version: 5.1.44
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tregmineserver`
--

-- --------------------------------------------------------

--
-- Table structure for table `bank`
--

CREATE TABLE IF NOT EXISTS `bank` (
  `bank_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `zone_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`bank_id`),
  UNIQUE KEY `idx_lot` (`zone_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `bank`
--

INSERT INTO `bank` (`bank_id`, `zone_id`) VALUES
(1, 14);

-- --------------------------------------------------------

--
-- Table structure for table `bank_account`
--

CREATE TABLE IF NOT EXISTS `bank_account` (
  `account_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bank_id` int(10) unsigned DEFAULT NULL,
  `player_id` int(10) unsigned DEFAULT NULL,
  `account_balance` int(10) unsigned DEFAULT NULL,
  `account_number` int(10) unsigned DEFAULT NULL,
  `account_pin` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `idx_accountnum` (`account_number`),
  KEY `idx_bank` (`bank_id`,`player_id`),
  KEY `idx_accountnum2` (`bank_id`,`account_number`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `bank_account`
--

INSERT INTO `bank_account` (`account_id`, `bank_id`, `player_id`, `account_balance`, `account_number`, `account_pin`) VALUES
(1, 1, 1, 4695, 1, '2222'),
(2, 1, 26, 20, 2, '1122');

-- --------------------------------------------------------

--
-- Table structure for table `bank_bankers`
--

CREATE TABLE IF NOT EXISTS `bank_bankers` (
  `bank_id` int(10) NOT NULL,
  `banker_id` char(36) NOT NULL,
  `banker_name` varchar(255) NOT NULL,
  `banker_x` int(11) DEFAULT NULL,
  `banker_y` int(11) DEFAULT NULL,
  `banker_z` int(11) DEFAULT NULL,
  `banker_world` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bank_bankers`
--

INSERT INTO `bank_bankers` (`bank_id`, `banker_id`, `banker_name`, `banker_x`, `banker_y`, `banker_z`, `banker_world`) VALUES
(1, 'a3268e5f-20cc-4003-e123-40ec947f76f2', 'Banker SpecialName', 1010, 65, 1007, 'world');

-- --------------------------------------------------------

--
-- Table structure for table `bank_transaction`
--

CREATE TABLE IF NOT EXISTS `bank_transaction` (
  `transaction_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `transaction_type` enum('deposit','withdrawal') NOT NULL,
  `transaction_amount` int(10) unsigned NOT NULL,
  `transaction_timestamp` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `idx_account` (`account_id`,`transaction_timestamp`),
  KEY `idx_player` (`player_id`,`transaction_timestamp`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `bank_transaction`
--

INSERT INTO `bank_transaction` (`transaction_id`, `account_id`, `player_id`, `transaction_type`, `transaction_amount`, `transaction_timestamp`) VALUES
(1, 1, 1, 'deposit', 5000, 1393967786),
(2, 1, 1, 'withdrawal', 300, 1393967797),
(3, 1, 1, 'deposit', 5, 1393972105),
(4, 1, 1, 'withdrawal', 10, 1393972109),
(5, 2, 1, 'deposit', 5, 1393972240),
(6, 2, 1, 'deposit', 1000, 1393972261),
(7, 2, 1, 'deposit', 1000, 1393972347),
(8, 2, 1, 'deposit', 1000, 1393972356),
(9, 2, 1, 'withdrawal', 1000, 1393972454),
(10, 2, 1, 'withdrawal', 1000, 1393972465),
(11, 2, 1, 'withdrawal', 1000, 1393972467),
(12, 2, 1, 'deposit', 1000, 1393972689),
(13, 2, 1, 'deposit', 100, 1393972930),
(14, 2, 1, 'deposit', 10, 1393973031),
(15, 2, 1, 'deposit', 5, 1393973245),
(16, 2, 1, 'withdrawal', 1100, 1393973252);
