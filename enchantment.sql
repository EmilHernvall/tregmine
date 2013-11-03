-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 03, 2013 at 12:46 PM
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
-- Table structure for table `enchantment`
--

DROP TABLE IF EXISTS `enchantment`;
CREATE TABLE IF NOT EXISTS `enchantment` (
  `old` varchar(255) NOT NULL,
  `new` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `enchantment`
--

INSERT INTO `enchantment` (`old`, `new`) VALUES
('ARROW_DAMAGE', 'Power'),
('ARROW_FIRE', 'Flame'),
('ARROW_INFINITE', 'Infinity'),
('ARROW_KNOCKBACK', 'Punch'),
('DAMAGE_ALL', 'Sharpness'),
('DAMAGE_ARTHROPODS', 'Bane of Arthropods'),
('DAMAGE_UNDEAD', 'Smite'),
('DIG_SPEED', 'Efficiency'),
('DURABILITY', 'Unbreaking'),
('FIRE_ASPECT', 'Fire Aspect'),
('KNOCKBACK', 'Knockback'),
('LOOT_BONUS_BLOCKS', 'Fortune'),
('LOOT_BONUS_MOBS', 'Looting'),
('OXYGEN', 'Respiration'),
('PROTECTION_ENVIRONMENTAL', 'Protection'),
('PROTECTION_EXPLOSIONS', 'Blast Protection'),
('PROTECTION_FIRE', 'Fire Protection'),
('PROTECTION_PROJECTILE', 'Projectile Protection'),
('PROTECTION_FALL', 'Feather Falling'),
('SILK_TOUCH', 'Silk Touch'),
('THORNS', 'Thorns'),
('WATER_WORKER', 'Aqua Affinity');
