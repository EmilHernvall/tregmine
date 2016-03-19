-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Mar 19, 2016 at 07:49 PM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tregmine`
--

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
  `blessedblock_world` varchar(32) CHARACTER SET utf8 COLLATE utf8_swedish_ci DEFAULT NULL
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
  `inventory_world` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `inventory_player` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `inventory_type` enum('block','player','player_armor') COLLATE utf8_swedish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  `item_meta` text CHARACTER SET utf8,
  `item_count` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  `auctionable` enum('no','yes') NOT NULL DEFAULT 'no',
  `link` varchar(255) NOT NULL,
  `round` enum('no','yes') NOT NULL DEFAULT 'no'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `item`
--

INSERT INTO `item` (`id`, `item_id`, `item_data`, `item_name`, `enchantable`, `sellable`, `item_value`, `auctionable`, `link`, `round`) VALUES
(1, 0, 0, 'Air', 'no', 'no', 0, 'no', '', 'no'),
(2, 1, 0, 'Stone', 'no', 'yes', 8, 'no', 'blocks/stone.png', 'yes'),
(3, 2, 0, 'Grass', 'no', 'yes', 5, 'no', 'blocks/grass_side.png', 'yes'),
(4, 3, 0, 'Dirt', 'no', 'yes', 3, 'no', 'blocks/dirt.png', 'yes'),
(5, 4, 0, 'Cobblestone', 'no', 'yes', 3, 'no', 'blocks/cobblestone.png', 'yes'),
(6, 5, 0, 'Wood Plank (Oak)', 'no', 'yes', 2, 'no', 'blocks/planks_oak.png', 'yes'),
(7, 5, 1, 'Wood Plank (Spruce)', 'no', 'yes', 2, 'no', 'blocks/planks_spruce.png', 'yes'),
(8, 5, 2, 'Wood Plank (Birch)', 'no', 'yes', 2, 'no', 'blocks/planks_birch.png', 'yes'),
(9, 5, 3, 'Wood Plank (Jungle)', 'no', 'yes', 2, 'no', 'blocks/planks_jungle.png', 'yes'),
(10, 6, 0, 'Sapling (Oak)', 'no', 'no', 0, 'no', 'blocks/sapling.png', 'no'),
(11, 6, 1, 'Sapling (Spruce)', 'no', 'no', 0, 'no', 'blocks/sapling_spruce.png', 'no'),
(12, 6, 2, 'Sapling (Birch)', 'no', 'no', 0, 'no', 'blocks/sapling_birch.png', 'no'),
(13, 6, 3, 'Sapling (Jungle)', 'no', 'no', 0, 'no', 'block/sapling_jungle.png', 'no'),
(14, 7, 0, 'Bedrock', 'no', 'no', 0, 'no', 'blocks/bedrock.png', 'yes'),
(15, 8, 0, 'Water', 'no', 'no', 0, 'no', 'blocks/water_flow.png', 'yes'),
(16, 9, 0, 'Water (Still)', 'no', 'no', 0, 'no', 'blocks/water_still.png', 'yes'),
(17, 10, 0, 'Lava', 'no', 'no', 0, 'no', 'blocks/lava_flow.png', 'yes'),
(18, 11, 0, 'Lava (Still)', 'no', 'no', 0, 'no', 'blocks/lava_still.png', 'yes'),
(19, 12, 0, 'Sand', 'no', 'yes', 3, 'no', 'blocks/sand.png', 'yes'),
(20, 13, 0, 'Gravel', 'no', 'yes', 7, 'no', 'blocks/gravel.png', 'yes'),
(21, 14, 0, 'Gold Ore', 'no', 'yes', 255, 'no', 'blocks/gold_ore.png', 'yes'),
(22, 15, 0, 'Iron Ore', 'no', 'yes', 65, 'no', 'blocks/iron_ore.png', 'yes'),
(23, 16, 0, 'Coal Ore', 'no', 'yes', 32, 'no', 'blocks/coal_ore.png', 'yes'),
(24, 17, 0, 'Log (Oak)', 'no', 'yes', 8, 'no', 'blocks/log_oak.png', 'yes'),
(25, 17, 1, 'Log (Spruce)', 'no', 'yes', 8, 'no', 'blocks/log_spruce.png', 'yes'),
(26, 17, 2, 'Log (Birch)', 'no', 'yes', 8, 'no', 'blocks/log_birch.png', 'yes'),
(27, 17, 3, 'Log (Jungle)', 'no', 'yes', 8, 'no', 'blocks/log_jungle.png', 'yes'),
(28, 18, 0, 'Leaves (Oak)', 'no', 'yes', 6, 'no', 'blocks/leaves_oak.png', 'yes'),
(29, 18, 1, 'Leaves (Spruce)', 'no', 'yes', 6, 'no', 'blocks/leaves_spruce.png', 'yes'),
(30, 18, 2, 'Leaves (Birch)', 'no', 'yes', 6, 'no', 'blocks/leaves_birch.png', 'yes'),
(31, 18, 3, 'Leaves (Jungle)', 'no', 'yes', 6, 'no', 'blocks/leaves_jungle.png', 'yes'),
(32, 19, 0, 'Sponge', 'no', 'no', 0, 'no', 'blocks/sponge.png', 'yes'),
(33, 20, 0, 'Glass', 'no', 'yes', 6, 'no', 'blocks/glass.png', 'yes'),
(34, 21, 0, 'Lapis Lazuli Ore', 'no', 'yes', 350, 'no', 'blocks/lapis_ore.png', 'yes'),
(35, 22, 0, 'Lapis Lazuli Block', 'no', 'yes', 495, 'no', 'blocks/lapis_block.png', 'yes'),
(36, 23, 0, 'Dispenser', 'no', 'yes', 56, 'no', 'blocks/dispenser_front_horizontal.png', 'yes'),
(37, 24, 0, 'Sandstone', 'no', 'yes', 8, 'no', 'blocks/sandstone_normal.png', 'yes'),
(38, 24, 1, 'Chiseled Sandstone', 'no', 'yes', 8, 'no', 'blocks/sandstone_carved.png', 'yes'),
(39, 24, 2, 'Smooth Sandstone', 'no', 'yes', 8, 'no', 'blocks/sandstone_smooth.png', 'yes'),
(40, 25, 0, 'Noteblock', 'no', 'yes', 45, 'no', 'blocks/noteblock.png', 'yes'),
(41, 27, 0, 'Powered Rail', 'no', 'yes', 266, 'no', 'blocks/rail_golden.png', 'no'),
(42, 28, 0, 'Detector Rail', 'no', 'yes', 78, 'no', 'blocks/rail_detector.png', 'no'),
(43, 29, 0, 'Sticky Piston', 'no', 'yes', 125, 'no', 'blocks/piston_top_sticky.png', 'yes'),
(44, 33, 0, 'Piston', 'no', 'yes', 123, 'no', 'blocks/piston_top_normal.png', 'yes'),
(45, 30, 0, 'Web', 'no', 'no', 0, 'no', 'blocks/web.png', 'no'),
(46, 31, 0, 'Dead Shrub', 'no', 'no', 0, 'no', 'blocks/deadbush.png', 'no'),
(47, 31, 1, 'Tall Grass', 'no', 'no', 0, 'no', 'blocks/tallgrass.png', 'no'),
(48, 31, 2, 'Fern', 'no', 'no', 0, 'no', 'blocks/fern.png', 'no'),
(49, 35, 0, 'White Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_white.png', 'yes'),
(50, 35, 1, 'Orange Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_orange.png', 'yes'),
(51, 35, 2, 'Magenta Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_magenta.png', 'yes'),
(52, 35, 3, 'Light Blue Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_light_blue.png', 'yes'),
(53, 35, 4, 'Yellow Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_yellow.png', 'yes'),
(54, 35, 5, 'Lime Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_lime.png', 'yes'),
(55, 35, 6, 'Pink Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_pink.png', 'yes'),
(56, 35, 7, 'Gray Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_gray.png', 'yes'),
(57, 35, 8, 'Light Gray Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_light_gray.png', 'yes'),
(58, 35, 9, 'Cyan Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_cyan.png', 'yes'),
(59, 35, 10, 'Purple Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_purple.png', 'yes'),
(60, 35, 11, 'Blue Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_blue.png', 'yes'),
(61, 35, 12, 'Brown Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_brown.png', 'yes'),
(62, 35, 13, 'Green Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_green.png', 'yes'),
(63, 35, 14, 'Red Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_red.png', 'yes'),
(64, 35, 15, 'Black Wool', 'no', 'no', 0, 'no', 'blocks/wool_colored_black.png', 'yes'),
(65, 37, 0, 'Dandelion', 'no', 'no', 0, 'no', 'blocks/flower_dandelion.png', 'no'),
(66, 38, 0, 'Rose', 'no', 'no', 0, 'no', 'blocks/flower_rose.png', 'no'),
(67, 39, 0, 'Brown Mushroom', 'no', 'no', 0, 'no', 'blocks/mushroom_brown.png', 'no'),
(68, 40, 0, 'Red Mushroom', 'no', 'no', 0, 'no', 'blocks/mushroom_red.png', 'no'),
(69, 41, 0, 'Gold Block', 'no', 'yes', 2340, 'no', 'blocks/gold_block.png', 'yes'),
(70, 42, 0, 'Iron Block', 'no', 'yes', 630, 'no', 'blocks/iron_block.png', 'yes'),
(71, 44, 0, 'Stone Slab', 'no', 'yes', 1, 'no', '', 'no'),
(72, 44, 1, 'Sandstone Slab', 'no', 'yes', 1, 'no', '', 'no'),
(73, 44, 2, 'Wooden Slab', 'no', 'yes', 1, 'no', '', 'no'),
(74, 44, 3, 'Cobblestone Slab', 'no', 'yes', 1, 'no', '', 'no'),
(75, 44, 4, 'Brick Slab', 'no', 'yes', 1, 'no', '', 'no'),
(76, 44, 5, 'Stone Brick Slab', 'no', 'yes', 1, 'no', '', 'no'),
(77, 44, 6, 'Nether Brick Slab', 'no', 'yes', 1, 'no', '', 'no'),
(78, 44, 7, 'Quartz Slab', 'no', 'yes', 1, 'no', '', 'no'),
(79, 45, 0, 'Bricks', 'no', 'yes', 28, 'no', 'blocks/brick.png', 'yes'),
(80, 46, 0, 'TNT', 'no', 'yes', 20, 'no', 'blocks/tnt_side.png', 'yes'),
(81, 47, 0, 'Bookshelf', 'no', 'yes', 21, 'no', 'blocks/bookshelf.png', 'yes'),
(82, 48, 0, 'Mossy Cobblestone', 'no', 'yes', 30, 'no', 'blocks/cobblestone_mossy.png', 'yes'),
(83, 49, 0, 'Obsidian', 'no', 'yes', 190, 'no', 'blocks/obsidian.png', 'yes'),
(84, 50, 0, 'Torch', 'no', 'yes', 5, 'no', 'blocks/torch_on.png', 'no'),
(85, 51, 0, 'Fire', 'no', 'no', 0, 'no', '', 'no'),
(86, 52, 0, 'Monster Spawner', 'no', 'no', 0, 'no', 'blocks/mob_spawner.png', 'no'),
(87, 53, 0, 'Oak Wood Stairs', 'no', 'yes', 4, 'no', '', 'no'),
(88, 54, 0, 'Chest', 'no', 'yes', 17, 'no', '', 'no'),
(89, 56, 0, 'Diamond Ore', 'no', 'yes', 430, 'no', 'blocks/diamond_ore.png', 'yes'),
(90, 57, 0, 'Diamond Block', 'no', 'yes', 3600, 'no', 'blocks/diamond_block.png', 'yes'),
(91, 58, 0, 'Workbench', 'no', 'yes', 9, 'no', 'blocks/crafting_table_front.png', 'yes'),
(92, 61, 0, 'Furnace', 'no', 'yes', 34, 'no', 'blocks/furnace_front_off.png', 'yes'),
(93, 64, 0, 'Ladder', 'no', 'no', 0, 'no', 'blocks/ladder.png', 'no'),
(94, 66, 0, 'Rails', 'no', 'yes', 27, 'no', 'blocks/rail_normal.png', 'no'),
(95, 67, 0, 'Cobblestone Stairs', 'no', 'yes', 7, 'no', '', 'no'),
(96, 69, 0, 'Lever', 'no', 'yes', 6, 'no', 'blocks/lever.png', 'no'),
(97, 70, 0, 'Stone Pressure Plate', 'no', 'yes', 17, 'no', '', 'no'),
(98, 72, 0, 'Wooden Pressure Plate', 'no', 'yes', 5, 'no', '', 'no'),
(99, 73, 0, 'Redstone Ore', 'no', 'yes', 120, 'no', 'blocks/redstone_ore.png', 'yes'),
(100, 76, 0, 'Redstone Torch', 'no', 'yes', 27, 'no', 'blocks/redstone_torch_on.png', 'no'),
(101, 77, 0, 'Stone Button', 'no', 'yes', 8, 'no', '', 'no'),
(102, 78, 0, 'Snow', 'no', 'no', 0, 'no', 'blocks/snow.png', 'yes'),
(103, 79, 0, 'Ice', 'no', 'yes', 8, 'no', 'blocks/ice.png', 'yes'),
(104, 81, 0, 'Cactus', 'no', 'no', 0, 'no', 'blocks/cactus_side.png', 'no'),
(105, 82, 0, 'Clay', 'no', 'yes', 12, 'no', 'blocks/clay.png', 'yes'),
(106, 83, 0, 'Sugar Cane', 'no', 'no', 0, 'no', 'blocks/reeds.png', 'no'),
(107, 84, 0, 'Jukebox', 'no', 'no', 0, 'no', 'blocks/jukebox_top.png', 'yes'),
(108, 85, 0, 'Fence', 'no', 'no', 0, 'no', '', 'no'),
(109, 86, 0, 'Pumpkin', 'no', 'no', 0, 'no', 'blocks/pumpkin_face_off.png', 'no'),
(110, 87, 0, 'Netherrack', 'no', 'yes', 3, 'no', 'blocks/netherrack.png', 'yes'),
(111, 88, 0, 'Soul Sand', 'no', 'yes', 6, 'no', 'blocks/soul_sand.png', 'yes'),
(112, 89, 0, 'Glowstone', 'no', 'yes', 12, 'no', 'blocks/glowstone.png', 'yes'),
(113, 90, 0, 'Portal', 'no', 'no', 0, 'no', 'blocks/portal.png', 'yes'),
(114, 91, 0, 'Jack-O-Lantern', 'no', 'no', 0, 'no', 'blocks/pumpkin_face_on.png', 'yes'),
(115, 93, 0, 'Repeater', 'no', 'no', 0, 'no', 'blocks/repeater_off.png', 'yes'),
(117, 96, 0, 'Trapdoor', 'no', 'no', 0, 'no', 'blocks/trapdoor.png', 'yes'),
(118, 97, 0, 'Silverfish (Stone)', 'no', 'no', 0, 'no', 'blocks/stone.png', 'yes'),
(119, 97, 1, 'Silverfish (Cobblestone)', 'no', 'no', 0, 'no', 'blocks/cobblestone.png', 'yes'),
(120, 97, 2, 'Silverfish (Stone Brick)', 'no', 'no', 0, 'no', 'blocks/stonebrick.png', 'yes'),
(121, 98, 0, 'Stone Brick', 'no', 'yes', 10, 'no', 'blocks/stonebrick.png', 'yes'),
(122, 98, 1, 'Stone Brick (Mossy)', 'no', 'yes', 10, 'no', 'blocks/stonebrick_mossy.png', 'yes'),
(123, 98, 2, 'Stone Brick (Cracked)', 'no', 'yes', 10, 'no', 'blocks/stonebrick_cracked.png', 'yes'),
(124, 98, 3, 'Stone Brick (Chiseled)', 'no', 'yes', 10, 'no', 'blocks/stonebrick_carved.png', 'yes'),
(125, 99, 0, 'Red Mushroom Top', 'no', 'no', 0, 'no', 'blocks/mushroom_block_skin_red.png', 'yes'),
(126, 100, 0, 'Brown Mushroom Top', 'no', 'no', 0, 'no', 'blocks/mushroom_block_skin_brown.png', 'yes'),
(127, 101, 0, 'Iron Bars', 'no', 'no', 0, 'no', 'blocks/iron_bars.png', 'no'),
(128, 102, 0, 'Glass Pane', 'no', 'yes', 3, 'no', 'blocks/glass.png', 'no'),
(129, 103, 0, 'Melon Block', 'no', 'no', 0, 'no', 'blocks/melon_side.png', 'yes'),
(130, 106, 0, 'Vines', 'no', 'no', 0, 'no', 'blocks/vine.png', 'no'),
(131, 107, 0, 'Fence Gate', 'no', 'no', 0, 'no', '', 'no'),
(132, 108, 0, 'Brick Stairs', 'no', 'no', 0, 'no', '', 'no'),
(133, 109, 0, 'Stone Brick Stairs', 'no', 'no', 0, 'no', '', 'no'),
(134, 110, 0, 'Mycelium', 'no', 'yes', 5, 'no', 'blocks/mycelium_side.png', 'yes'),
(135, 111, 0, 'Lily Pad', 'no', 'no', 0, 'no', '', 'no'),
(136, 112, 0, 'Nether Brick', 'no', 'yes', 10, 'no', 'blocks/nether_brick.png', 'yes'),
(137, 113, 0, 'Nether Brick Fence', 'no', 'no', 0, 'no', '', 'no'),
(138, 114, 0, 'Nether Brick Stairs', 'no', 'no', 0, 'no', '', 'no'),
(139, 115, 0, 'Nether Wart', 'no', 'no', 0, 'no', 'blocks/nether_wart_stage_0.png', 'no'),
(140, 116, 0, 'Enchantment Table', 'no', 'no', 0, 'no', 'blocks/enchanting_table_top.png', 'yes'),
(141, 117, 0, 'Brewing Stand', 'no', 'no', 0, 'no', 'blocks/brewing_stand.png', 'no'),
(142, 118, 0, 'Cauldron', 'no', 'no', 0, 'no', 'blocks/cauldron_side.png', 'no'),
(143, 121, 0, 'End Stone', 'no', 'yes', 12, 'no', 'blocks/end_stone.png', 'yes'),
(144, 122, 0, 'Dragon Egg', 'no', 'no', 0, 'no', '', 'no'),
(145, 123, 0, 'Redstone Lamp', 'no', 'no', 0, 'no', 'blocks/redstone_lamp_off.png', 'yes'),
(146, 126, 0, 'Oak Wood Slab', 'no', 'yes', 1, 'no', '', 'no'),
(147, 126, 1, 'Spruce Wood Slab', 'no', 'yes', 1, 'no', '', 'no'),
(148, 126, 2, 'Birch Wood Slab', 'no', 'yes', 1, 'no', '', 'no'),
(149, 126, 3, 'Jungle Wood Slab', 'no', 'yes', 1, 'no', '', 'no'),
(150, 128, 0, 'Sandstone Stairs', 'no', 'no', 0, 'no', '', 'no'),
(151, 129, 0, 'Emerald Ore', 'no', 'yes', 450, 'no', 'blocks/emerald_ore.png', 'yes'),
(152, 130, 0, 'Ender Chest', 'no', 'no', 0, 'no', '', 'no'),
(153, 131, 0, 'Tripwire Hook', 'no', 'no', 0, 'no', 'blocks/trip_wire_source.png', 'no'),
(154, 133, 0, 'Emerald Block', 'no', 'no', 0, 'no', 'blocks/emerald_block.png', 'yes'),
(155, 134, 0, 'Spruce Wood Stairs', 'no', 'no', 0, 'no', '', 'no'),
(156, 135, 0, 'Birch Wood Stairs', 'no', 'no', 0, 'no', '', 'no'),
(157, 136, 0, 'Jungle Wood Stairs', 'no', 'no', 0, 'no', '', 'no'),
(158, 137, 0, 'Command Block', 'no', 'no', 0, 'no', 'blocks/command_block.png', 'yes'),
(159, 138, 0, 'Beacon', 'no', 'no', 0, 'no', 'blocks/beacon.png', 'yes'),
(160, 139, 0, 'Cobblestone Wall', 'no', 'no', 0, 'no', '', 'no'),
(161, 139, 1, 'Cobblestone Wall (Mossy)', 'no', 'no', 0, 'no', '', 'no'),
(162, 310, 0, 'Diamond Helmet', 'no', 'no', 0, 'no', 'items/diamond_helmet.png', 'no'),
(163, 311, 0, 'Diamond Chestplate', 'no', 'no', 0, 'no', 'items/diamond_chestplate.png', 'no'),
(164, 312, 0, 'Diamond Leggings', 'no', 'no', 0, 'no', 'items/diamond_leggings.png', 'no'),
(165, 313, 0, 'Diamond Boots', 'no', 'no', 0, 'no', 'items/diamond_boots.png', 'no'),
(166, 298, 0, 'Leather Helmet', 'no', 'no', 0, 'no', 'items/leather_helmet.png', 'no'),
(167, 299, 0, 'Leather Chestplate', 'no', 'no', 0, 'no', 'items/leather_chestplate.png', 'no'),
(168, 300, 0, 'Leather Leggings', 'no', 'no', 0, 'no', 'items/leather_leggings.png', 'no'),
(169, 301, 0, 'Leather Boots', 'no', 'no', 0, 'no', 'items/leather_boots.png', 'no'),
(170, 302, 0, 'Chainmail Helmet', 'no', 'no', 0, 'no', 'items/chainmail_helmet.png', 'no'),
(171, 303, 0, 'Chainmail Chestplate', 'no', 'no', 0, 'no', 'items/chainmail_chestplate.png', 'no'),
(172, 304, 0, 'Chainmail Leggings', 'no', 'no', 0, 'no', 'items/chainmail_leggings.png', 'no'),
(173, 305, 0, 'Chainmail Boots', 'no', 'no', 0, 'no', 'items/chainmail_boots.png', 'no'),
(174, 306, 0, 'Iron Helmet', 'no', 'no', 0, 'no', 'items/iron_helmet.png', 'no'),
(175, 307, 0, 'Iron Chestplate', 'no', 'no', 0, 'no', 'items/iron_chestplate.png', 'no'),
(176, 308, 0, 'Iron Leggings', 'no', 'no', 0, 'no', 'items/iron_leggings.png', 'no'),
(177, 309, 0, 'Iron Boots', 'no', 'no', 0, 'no', 'items/iron_boots.png', 'no'),
(178, 314, 0, 'Golden Helmet', 'no', 'no', 0, 'no', 'items/gold_helmet.png', 'no'),
(179, 315, 0, 'Golden Chestplate', 'no', 'no', 0, 'no', 'items/gold_chestplate.png', 'no'),
(180, 316, 0, 'Golden Leggings', 'no', 'no', 0, 'no', 'items/gold_leggings.png', 'no'),
(181, 317, 0, 'Golden Boots', 'no', 'no', 0, 'no', 'items/gold_boots.png', 'no'),
(183, 568, 0, 'Wooden Sword', 'no', 'no', 0, 'no', 'items/wood_sword.png', 'no'),
(184, 569, 0, 'Wooden Shovel', 'no', 'no', 0, 'no', 'items/wood_shovel.png', 'no'),
(185, 570, 0, 'Wooden Pickaxe', 'no', 'no', 0, 'no', 'items/wood_pickaxe.png', 'no'),
(186, 571, 0, 'Wooden Axe', 'no', 'no', 0, 'no', 'items/wood_axe.png', 'no'),
(187, 152, 0, 'Redstone Block', 'no', 'yes', 225, 'no', '', 'no'),
(188, 153, 0, 'Quartz Ore', 'no', 'yes', 50, 'no', '', 'no'),
(189, 155, 0, 'Quartz Block', 'no', 'yes', 140, 'no', '', 'no'),
(190, 159, 0, 'Stained Clay', 'no', 'yes', 10, 'no', '', 'no'),
(191, 161, 0, 'Leaves (Acacia)', 'no', 'yes', 8, 'no', '', 'no'),
(192, 162, 0, 'Log (Acacia)', 'no', 'yes', 8, 'no', '', 'no'),
(193, 172, 0, 'Hardened Clay', 'no', 'yes', 10, 'no', '', 'no'),
(194, 173, 0, 'Coal Block', 'no', 'yes', 180, 'no', '', 'no'),
(195, 174, 0, 'Compressed Ice', 'no', 'yes', 24, 'no', '', 'no'),
(196, 263, 0, 'Coal', 'no', 'yes', 20, 'yes', '', 'no'),
(197, 264, 0, 'Diamond', 'no', 'yes', 400, 'no', '', 'no'),
(198, 265, 0, 'Iron Ingot', 'no', 'yes', 70, 'no', '', 'no'),
(199, 266, 0, 'Gold Ingot', 'no', 'yes', 260, 'no', '', 'no'),
(200, 318, 0, 'Flint', 'no', 'yes', 11, 'no', '', 'no'),
(201, 331, 0, 'Redstone Dust', 'no', 'yes', 25, 'no', '', 'no'),
(202, 336, 0, 'Brick', 'no', 'yes', 6, 'no', '', 'no'),
(203, 337, 0, 'Clay', 'no', 'yes', 3, 'no', '', 'no'),
(204, 348, 0, 'Glowstone Dust', 'no', 'yes', 3, 'no', '', 'no'),
(205, 371, 0, 'Gold Nugget', 'no', 'yes', 29, 'no', '', 'no'),
(206, 406, 0, 'Nether Quartz', 'no', 'yes', 35, 'no', '', 'no'),
(207, 26, 0, 'Bed Block', 'no', 'no', 0, 'no', '', 'no'),
(208, 32, 0, 'Dead Bush', 'no', 'no', 0, 'no', '', 'no'),
(209, 34, 0, 'Piston Extension', 'no', 'no', 0, 'no', '', 'no'),
(210, 36, 0, 'Piston Moving Piece', 'no', 'no', 0, 'no', '', 'no'),
(211, 43, 0, 'Double Step', 'no', 'no', 0, 'no', '', 'no'),
(212, 55, 0, 'Redstone Wire', 'no', 'no', 0, 'no', '', 'no'),
(213, 59, 0, 'Crops', 'no', 'no', 0, 'no', '', 'no'),
(214, 60, 0, 'Soil', 'no', 'no', 0, 'no', '', 'no'),
(215, 62, 0, 'Burning Furnace', 'no', 'no', 0, 'no', '', 'no'),
(216, 63, 0, 'Sign Post', 'no', 'no', 0, 'no', '', 'no'),
(217, 65, 0, 'Ladder', 'no', 'no', 0, 'no', '', 'no'),
(218, 68, 0, 'Wall Sign', 'no', 'no', 0, 'no', '', 'no'),
(219, 71, 0, 'Iron Door Block', 'no', 'no', 0, 'no', '', 'no'),
(220, 74, 0, 'Glowing Redstone Ore', 'no', 'no', 0, 'no', '', 'no'),
(221, 75, 0, 'Redstone Torch Off', 'no', 'no', 0, 'no', '', 'no'),
(222, 80, 0, 'Snow Block', 'no', 'no', 0, 'no', '', 'no'),
(223, 92, 0, 'Cake Block', 'no', 'no', 0, 'no', '', 'no'),
(224, 94, 0, 'Diode Block On', 'no', 'no', 0, 'no', '', 'no'),
(225, 95, 0, 'Locked Chest', 'no', 'no', 0, 'no', '', 'no'),
(226, 104, 0, 'Pumpkin Stem', 'no', 'no', 0, 'no', '', 'no'),
(227, 105, 0, 'Melon Stem', 'no', 'no', 0, 'no', '', 'no'),
(228, 119, 0, 'Ender Portal', 'no', 'no', 0, 'no', '', 'no'),
(229, 120, 0, 'Ender Portal Frame', 'no', 'no', 0, 'no', '', 'no'),
(230, 124, 0, 'Redstone Lamp On', 'no', 'no', 0, 'no', '', 'no'),
(231, 125, 0, 'Wood Double Step', 'no', 'no', 0, 'no', '', 'no'),
(232, 127, 0, 'Cocoa', 'no', 'no', 0, 'no', '', 'no'),
(233, 132, 0, 'Tripwire', 'no', 'no', 0, 'no', '', 'no'),
(234, 140, 0, 'Flower Pot', 'no', 'no', 0, 'no', '', 'no'),
(235, 141, 0, 'Carrot', 'no', 'no', 0, 'no', '', 'no'),
(236, 142, 0, 'Potato', 'no', 'no', 0, 'no', '', 'no'),
(237, 143, 0, 'Wood Button', 'no', 'no', 0, 'no', '', 'no'),
(238, 144, 0, 'Skull', 'no', 'no', 0, 'no', '', 'no'),
(239, 145, 0, 'Anvil', 'no', 'no', 0, 'no', '', 'no'),
(240, 146, 0, 'Trapped Chest', 'no', 'no', 0, 'no', '', 'no'),
(241, 147, 0, 'Gold Plate', 'no', 'no', 0, 'no', '', 'no'),
(242, 148, 0, 'Iron Plate', 'no', 'no', 0, 'no', '', 'no'),
(243, 149, 0, 'Redstone Comparator Off', 'no', 'no', 0, 'no', '', 'no'),
(244, 150, 0, 'Redstone Comparator On', 'no', 'no', 0, 'no', '', 'no'),
(245, 151, 0, 'Daylight Detector', 'no', 'no', 0, 'no', '', 'no'),
(246, 154, 0, 'Hopper', 'no', 'no', 0, 'no', '', 'no'),
(247, 156, 0, 'Quartz Stairs', 'no', 'no', 0, 'no', '', 'no'),
(248, 157, 0, 'Activator Rail', 'no', 'no', 0, 'no', '', 'no'),
(249, 158, 0, 'Dropper', 'no', 'no', 0, 'no', '', 'no'),
(250, 160, 0, 'Stained Glass Pane', 'no', 'no', 0, 'no', '', 'no'),
(251, 163, 0, 'Acacia Stairs', 'no', 'no', 0, 'no', '', 'no'),
(252, 164, 0, 'Dark Oak Stairs', 'no', 'no', 0, 'no', '', 'no'),
(253, 170, 0, 'Hay Block', 'no', 'no', 0, 'no', '', 'no'),
(254, 171, 0, 'Carpet', 'no', 'no', 0, 'no', '', 'no'),
(255, 175, 0, 'Double Plant', 'no', 'no', 0, 'no', '', 'no'),
(256, 256, 0, 'Iron Spade', 'no', 'no', 0, 'no', '', 'no'),
(257, 257, 0, 'Iron Pickaxe', 'no', 'no', 0, 'no', '', 'no'),
(258, 258, 0, 'Iron Axe', 'no', 'no', 0, 'no', '', 'no'),
(259, 259, 0, 'Flint And Steel', 'no', 'no', 0, 'no', '', 'no'),
(260, 260, 0, 'Apple', 'no', 'no', 0, 'no', '', 'no'),
(261, 261, 0, 'Bow', 'no', 'no', 0, 'no', '', 'no'),
(262, 262, 0, 'Arrow', 'no', 'no', 0, 'no', '', 'no'),
(263, 267, 0, 'Iron Sword', 'no', 'no', 0, 'no', '', 'no'),
(264, 268, 0, 'Wood Sword', 'no', 'no', 0, 'no', '', 'no'),
(265, 269, 0, 'Wood Spade', 'no', 'no', 0, 'no', '', 'no'),
(266, 270, 0, 'Wood Pickaxe', 'no', 'no', 0, 'no', '', 'no'),
(267, 271, 0, 'Wood Axe', 'no', 'no', 0, 'no', '', 'no'),
(268, 272, 0, 'Stone Sword', 'no', 'no', 0, 'no', '', 'no'),
(269, 273, 0, 'Stone Spade', 'no', 'no', 0, 'no', '', 'no'),
(270, 274, 0, 'Stone Pickaxe', 'no', 'no', 0, 'no', '', 'no'),
(271, 275, 0, 'Stone Axe', 'no', 'no', 0, 'no', '', 'no'),
(272, 276, 0, 'Diamond Sword', 'no', 'no', 0, 'no', '', 'no'),
(273, 277, 0, 'Diamond Spade', 'no', 'no', 0, 'no', '', 'no'),
(274, 278, 0, 'Diamond Pickaxe', 'no', 'no', 0, 'no', '', 'no'),
(275, 279, 0, 'Diamond Axe', 'no', 'no', 0, 'no', '', 'no'),
(276, 280, 0, 'Stick', 'no', 'no', 0, 'no', '', 'no'),
(277, 281, 0, 'Bowl', 'no', 'no', 0, 'no', '', 'no'),
(278, 282, 0, 'Mushroom Soup', 'no', 'no', 0, 'no', '', 'no'),
(279, 283, 0, 'Gold Sword', 'no', 'no', 0, 'no', '', 'no'),
(280, 284, 0, 'Gold Spade', 'no', 'no', 0, 'no', '', 'no'),
(281, 285, 0, 'Gold Pickaxe', 'no', 'no', 0, 'no', '', 'no'),
(282, 286, 0, 'Gold Axe', 'no', 'no', 0, 'no', '', 'no'),
(283, 287, 0, 'String', 'no', 'no', 0, 'no', '', 'no'),
(284, 288, 0, 'Feather', 'no', 'no', 0, 'no', '', 'no'),
(285, 289, 0, 'Sulphur', 'no', 'no', 0, 'no', '', 'no'),
(286, 290, 0, 'Wood Hoe', 'no', 'no', 0, 'no', '', 'no'),
(287, 291, 0, 'Stone Hoe', 'no', 'no', 0, 'no', '', 'no'),
(288, 292, 0, 'Iron Hoe', 'no', 'no', 0, 'no', '', 'no'),
(289, 293, 0, 'Diamond Hoe', 'no', 'no', 0, 'no', '', 'no'),
(290, 294, 0, 'Gold Hoe', 'no', 'no', 0, 'no', '', 'no'),
(291, 295, 0, 'Seeds', 'no', 'no', 0, 'no', '', 'no'),
(292, 296, 0, 'Wheat', 'no', 'no', 0, 'no', '', 'no'),
(293, 297, 0, 'Bread', 'no', 'no', 0, 'no', '', 'no'),
(294, 319, 0, 'Pork', 'no', 'no', 0, 'no', '', 'no'),
(295, 320, 0, 'Grilled Pork', 'no', 'no', 0, 'no', '', 'no'),
(296, 321, 0, 'Painting', 'no', 'no', 0, 'no', '', 'no'),
(297, 322, 0, 'Golden Apple', 'no', 'no', 0, 'no', '', 'no'),
(298, 323, 0, 'Sign', 'no', 'no', 0, 'no', '', 'no'),
(299, 324, 0, 'Wood Door', 'no', 'no', 0, 'no', '', 'no'),
(300, 325, 0, 'Bucket', 'no', 'no', 0, 'no', '', 'no'),
(301, 326, 0, 'Water Bucket', 'no', 'no', 0, 'no', '', 'no'),
(302, 327, 0, 'Lava Bucket', 'no', 'no', 0, 'no', '', 'no'),
(303, 328, 0, 'Minecart', 'no', 'no', 0, 'no', '', 'no'),
(304, 329, 0, 'Saddle', 'no', 'no', 0, 'no', '', 'no'),
(305, 330, 0, 'Iron Door', 'no', 'no', 0, 'no', '', 'no'),
(306, 332, 0, 'Snow Ball', 'no', 'no', 0, 'no', '', 'no'),
(307, 333, 0, 'Boat', 'no', 'no', 0, 'no', '', 'no'),
(308, 334, 0, 'Leather', 'no', 'no', 0, 'no', '', 'no'),
(309, 335, 0, 'Milk Bucket', 'no', 'no', 0, 'no', '', 'no'),
(310, 338, 0, 'Sugar Cane', 'no', 'no', 0, 'no', '', 'no'),
(311, 339, 0, 'Paper', 'no', 'no', 0, 'no', '', 'no'),
(312, 340, 0, 'Book', 'no', 'no', 0, 'no', '', 'no'),
(313, 341, 0, 'Slime Ball', 'no', 'no', 0, 'no', '', 'no'),
(314, 342, 0, 'Storage Minecart', 'no', 'no', 0, 'no', '', 'no'),
(315, 343, 0, 'Powered Minecart', 'no', 'no', 0, 'no', '', 'no'),
(316, 344, 0, 'Egg', 'no', 'no', 0, 'no', '', 'no'),
(317, 345, 0, 'Compass', 'no', 'no', 0, 'no', '', 'no'),
(318, 346, 0, 'Fishing Rod', 'no', 'no', 0, 'no', '', 'no'),
(319, 347, 0, 'Watch', 'no', 'no', 0, 'no', '', 'no'),
(320, 349, 0, 'Raw Fish', 'no', 'no', 0, 'no', '', 'no'),
(321, 350, 0, 'Cooked Fish', 'no', 'no', 0, 'no', '', 'no'),
(322, 351, 0, 'Ink Sack', 'no', 'no', 0, 'no', '', 'no'),
(323, 352, 0, 'Bone', 'no', 'no', 0, 'no', '', 'no'),
(324, 353, 0, 'Sugar', 'no', 'no', 0, 'no', '', 'no'),
(325, 354, 0, 'Cake', 'no', 'no', 0, 'no', '', 'no'),
(326, 355, 0, 'Bed', 'no', 'no', 0, 'no', '', 'no'),
(327, 356, 0, 'Diode', 'no', 'no', 0, 'no', '', 'no'),
(328, 357, 0, 'Cookie', 'no', 'no', 0, 'no', '', 'no'),
(329, 358, 0, 'Map', 'no', 'no', 0, 'no', '', 'no'),
(330, 359, 0, 'Shears', 'no', 'no', 0, 'no', '', 'no'),
(331, 360, 0, 'Melon', 'no', 'no', 0, 'no', '', 'no'),
(332, 361, 0, 'Pumpkin Seeds', 'no', 'no', 0, 'no', '', 'no'),
(333, 362, 0, 'Melon Seeds', 'no', 'no', 0, 'no', '', 'no'),
(334, 363, 0, 'Raw Beef', 'no', 'no', 0, 'no', '', 'no'),
(335, 364, 0, 'Cooked Beef', 'no', 'no', 0, 'no', '', 'no'),
(336, 365, 0, 'Raw Chicken', 'no', 'no', 0, 'no', '', 'no'),
(337, 366, 0, 'Cooked Chicken', 'no', 'no', 0, 'no', '', 'no'),
(338, 367, 0, 'Rotten Flesh', 'no', 'no', 0, 'no', '', 'no'),
(339, 368, 0, 'Ender Pearl', 'no', 'no', 0, 'no', '', 'no'),
(340, 369, 0, 'Blaze Rod', 'no', 'no', 0, 'no', '', 'no'),
(341, 370, 0, 'Ghast Tear', 'no', 'no', 0, 'no', '', 'no'),
(342, 372, 0, 'Nether Stalk', 'no', 'no', 0, 'no', '', 'no'),
(343, 373, 0, 'Potion', 'no', 'no', 0, 'no', '', 'no'),
(344, 374, 0, 'Glass Bottle', 'no', 'no', 0, 'no', '', 'no'),
(345, 375, 0, 'Spider Eye', 'no', 'no', 0, 'no', '', 'no'),
(346, 376, 0, 'Fermented Spider Eye', 'no', 'no', 0, 'no', '', 'no'),
(347, 377, 0, 'Blaze Powder', 'no', 'no', 0, 'no', '', 'no'),
(348, 378, 0, 'Magma Cream', 'no', 'no', 0, 'no', '', 'no'),
(349, 379, 0, 'Brewing Stand Item', 'no', 'no', 0, 'no', '', 'no'),
(350, 380, 0, 'Cauldron Item', 'no', 'no', 0, 'no', '', 'no'),
(351, 381, 0, 'Eye Of Ender', 'no', 'no', 0, 'no', '', 'no'),
(352, 382, 0, 'Speckled Melon', 'no', 'no', 0, 'no', '', 'no'),
(353, 383, 0, 'Monster Egg', 'no', 'no', 0, 'no', '', 'no'),
(354, 384, 0, 'Exp Bottle', 'no', 'no', 0, 'no', '', 'no'),
(355, 385, 0, 'Fireball', 'no', 'no', 0, 'no', '', 'no'),
(356, 386, 0, 'Book And Quill', 'no', 'no', 0, 'no', '', 'no'),
(357, 387, 0, 'Written Book', 'no', 'no', 0, 'no', '', 'no'),
(358, 388, 0, 'Emerald', 'no', 'no', 0, 'no', '', 'no'),
(359, 389, 0, 'Item Frame', 'no', 'no', 0, 'no', '', 'no'),
(360, 390, 0, 'Flower Pot Item', 'no', 'no', 0, 'no', '', 'no'),
(361, 391, 0, 'Carrot Item', 'no', 'no', 0, 'no', '', 'no'),
(362, 392, 0, 'Potato Item', 'no', 'no', 0, 'no', '', 'no'),
(363, 393, 0, 'Baked Potato', 'no', 'no', 0, 'no', '', 'no'),
(364, 394, 0, 'Poisonous Potato', 'no', 'no', 0, 'no', '', 'no'),
(365, 395, 0, 'Empty Map', 'no', 'no', 0, 'no', '', 'no'),
(366, 396, 0, 'Golden Carrot', 'no', 'no', 0, 'no', '', 'no'),
(367, 397, 0, 'Skull Item', 'no', 'no', 0, 'no', '', 'no'),
(368, 398, 0, 'Carrot Stick', 'no', 'no', 0, 'no', '', 'no'),
(369, 399, 0, 'Nether Star', 'no', 'no', 0, 'no', '', 'no'),
(370, 400, 0, 'Pumpkin Pie', 'no', 'no', 0, 'no', '', 'no'),
(371, 401, 0, 'Firework', 'no', 'no', 0, 'no', '', 'no'),
(372, 402, 0, 'Firework Charge', 'no', 'no', 0, 'no', '', 'no'),
(373, 403, 0, 'Enchanted Book', 'no', 'no', 0, 'no', '', 'no'),
(374, 404, 0, 'Redstone Comparator', 'no', 'no', 0, 'no', '', 'no'),
(375, 405, 0, 'Nether Brick Item', 'no', 'no', 0, 'no', '', 'no'),
(376, 407, 0, 'Explosive Minecart', 'no', 'no', 0, 'no', '', 'no'),
(377, 408, 0, 'Hopper Minecart', 'no', 'no', 0, 'no', '', 'no'),
(378, 417, 0, 'Iron Barding', 'no', 'no', 0, 'no', '', 'no'),
(379, 418, 0, 'Gold Barding', 'no', 'no', 0, 'no', '', 'no'),
(380, 419, 0, 'Diamond Barding', 'no', 'no', 0, 'no', '', 'no'),
(381, 420, 0, 'Leash', 'no', 'no', 0, 'no', '', 'no'),
(382, 421, 0, 'Name Tag', 'no', 'no', 0, 'no', '', 'no'),
(383, 422, 0, 'Command Minecart', 'no', 'no', 0, 'no', '', 'no'),
(384, 2256, 0, 'Gold Record', 'no', 'no', 0, 'no', '', 'no'),
(385, 2257, 0, 'Green Record', 'no', 'no', 0, 'no', '', 'no'),
(386, 2258, 0, 'Record 3', 'no', 'no', 0, 'no', '', 'no'),
(387, 2259, 0, 'Record 4', 'no', 'no', 0, 'no', '', 'no'),
(388, 2260, 0, 'Record 5', 'no', 'no', 0, 'no', '', 'no'),
(389, 2261, 0, 'Record 6', 'no', 'no', 0, 'no', '', 'no'),
(390, 2262, 0, 'Record 7', 'no', 'no', 0, 'no', '', 'no'),
(391, 2263, 0, 'Record 8', 'no', 'no', 0, 'no', '', 'no'),
(392, 2264, 0, 'Record 9', 'no', 'no', 0, 'no', '', 'no'),
(393, 2265, 0, 'Record 10', 'no', 'no', 0, 'no', '', 'no'),
(394, 2266, 0, 'Record 11', 'no', 'no', 0, 'no', '', 'no'),
(395, 2267, 0, 'Record 12', 'no', 'no', 0, 'no', '', 'no');

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
  `message_value` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `misc_message`
--

INSERT INTO `misc_message` (`message_type`, `message_value`) VALUES
('INSULT', 'Better luck next time.'),
('INSULT', 'Did you just really do that?'),
('INSULT', 'And you will never see heaven.'),
('INSULT', 'You just lost all your stuff.'),
('INSULT', 'Damnit again?'),
('INSULT', 'Why did you jump?'),
('INSULT', 'Never drive drunk.'),
('INSULT', 'and lost some cash.'),
('INSULT', 'and never saw the light.'),
('INSULT', 'Can someone clean that mess up?'),
('INSULT', 'Damnit not another one.'),
('INSULT', 'We have the power to revive you.'),
('INSULT', 'You just won the Darwin Award.'),
('INSULT', 'Next time you get banned!'),
('INSULT', 'He death man!'),
('INSULT', 'He always finds himself lost in thought - it''s an unfamiliar territory'),
('INSULT', 'He doesn''t know the meaning of the word "fear" - but then again he doesn''t know the meaning of most words'),
('INSULT', 'Oh my God, look at you. Anyone else hurt in the accident?'),
('INSULT', 'I''ve seen people like you before, but I had to pay admission!'),
('INSULT', 'He was happily married - but his wife wasn''t, until now.'),
('INSULT', 'I heard that you went to the haunted house and they offered you a job.'),
('INSULT', 'Can someone get that necrophilia of that corps.'),
('INSULT', 'I bet your brain feels as good as new, seeing that you''ve never used it.'),
('INSULT', 'If we were to kill everybody who hates you, it wouldn''t be murder, it would be genocide!'),
('INSULT', 'I don''t think you are a fool. But then, what''s my own humble opinion against thousands of others?'),
('INSULT', 'Nobody says that you are dumb. They just say you were sixteen years old before you learned how to wave goodbye.'),
('INSULT', 'Ordinarily people live and learn. You just died.'),
('INSULT', 'NOOOO I forgot to ask you if I could get your stuff, euhmm il take it anyway.'),
('INSULT', 'Ooohhh did I just release all those mobs on you?'),
('INSULT', 'I don''t hold your behavior against you because I realize it was caused by childhood trauma.'),
('INSULT', 'How was your date with death.'),
('INSULT', 'How was your date with the Grim Reper'),
('INSULT', 'Ohh it was you, I thought it was a zombie I killed'),
('INSULT', 'Any similarity between you and a human is purely coincidental!'),
('INSULT', 'Anyone who told you to be yourself couldn''t have given you worse advice.'),
('INSULT', 'Calling you stupid would be an insult to stupid people.'),
('INSULT', 'Do you ever wonder what life would be like if you''d had enough oxygen at birth?'),
('INSULT', 'Don''t you have a terribly empty feeling - in your skull?'),
('INSULT', 'Do you still love nature, despite what it did to you?'),
('INSULT', 'Pardon me, but you''ve obviously mistaken me for someone who gives a damn.'),
('INSULT', 'No 72 virgins for you.'),
('INSULT', 'If ignorance is bliss, you must be the happiest person alive.'),
('INSULT', 'Why can''t you just fucking die'),
('INSULT', 'Nice to meeting you again - welcome back'),
('INSULT', 'If you were any more stupid, You''d have to be watered twice a week.'),
('INSULT', 'He died of SHC - Spontaneous human combustion'),
('INSULT', 'Can someone go to Walmart and get a funeral casket'),
('INSULT', 'Strange no one ate your funeral, are you to stupid to have friends?'),
('INSULT', 'it was the Obama administration that got him killed'),
('INSULT', 'GAME OVER - PRESS SELECT TO CONTINUE'),
('INSULT', 'What did a cat do that to you'),
('INSULT', 'Wait, why arent you dead?'),
('INSULT', 'I think the grim reper loves you as do continue to die all the time'),
('INSULT', 'Either  he''s dead or my watch has stopped.'),
('INSULT', 'Luckily you had those extra live''s'),
('INSULT', 'Stop dying or you will be the only one this month in death''s collecbook'),
('INSULT', 'Stop dying you are harasing death'),
('INSULT', 'He is braindead - ooh no wait its unused'),
('INSULT', 'Did you have to build that high, and fall down?'),
('INSULT', 'There are three natural anaesthetics: Sleep, fainting, and death.'),
('INSULT', 'Didn''t you see that creeper crawling up on you from behind?'),
('INSULT', 'Did you order a hit man for suicide?'),
('INSULT', 'is dead, call 991'),
('INSULT', 'It was a Hit And Run'),
('INSULT', 'That why you never shall play with water'),
('INSULT', 'You are such a loser, you can''t even die normal'),
('INSULT', 'That was a trap, or you are just plain stupid'),
('INSULT', 'I shall revenge your living'),
('INSULT', 'I know what you did last summer'),
('INSULT', 'Spider wont make you spider man, as they are not radioactive'),
('INSULT', 'Scream!'),
('INSULT', 'Wow still alive? oh wait, Scream 4'),
('INSULT', 'Fucking a skeleton is considered necrophilia and is thus punishable by death.'),
('INSULT', 'You died that fast that I didn''t even notice it'),
('INSULT', 'Nice finally I found some Blood Stone'),
('INSULT', 'Strange can you die doing that?'),
('INSULT', 'You must be an inventor since you invent so many ways to die'),
('INSULT', 'Leave the poor mob alone'),
('INSULT', 'Why cant you just stay dead?'),
('INSULT', 'You died so many times so we have to rename the word die to your name'),
('INSULT', 'Did you see Elvis?'),
('INSULT', 'Mess with the best and die like the rest.'),
('INSULT', 'Camrenn still loves you!'),
('INSULT', 'Camrenn forced her love on you, thats why you died'),
('INSULT', 'Sorry it was our princess Camrenn who wish to have you killed'),
('INSULT', 'That was a suprice attack by Camrenn'),
('INSULT', 'Please understand Camrenn don''t want to marry you'),
('INSULT', 'Oh what''s that? your dead? ''what a shame'''),
('INSULT', 'That creeper just wanted a hug!'),
('INSULT', 'Is it day time yet?'),
('INSULT', 'Oops, you did it again!'),
('INSULT', 'Yes, arrows kill'),
('INSULT', 'Death before dishonor!'),
('INSULT', 'Live free or die!'),
('INSULT', 'no, you can''t swim in lava'),
('INSULT', 'And again you think... "I shall heal my self"... and again you die.'),
('INSULT', 'where is a pig when you need one...'),
('INSULT', 'You''ve got death!'),
('INSULT', 'It is dark, your exits were north, east, and west, you were eaten by a grue'),
('INSULT', 'Warning: 3 Lives Remaining!'),
('INSULT', 'Walk towards the light! No! The other light!'),
('INSULT', 'What do you think you''d accomplish by doing that?'),
('INSULT', 'The secret of flying? No, it''s not throwing yourself at the ground thinking you will miss...'),
('INSULT', 'Know what the last thing was that went through your head? Your ass!'),
('INSULT', 'Applying for work at the suicide hotline never required suicide attempts!'),
('INSULT', 'You found your first wild wolf huh? Next time, bring bones.'),
('INSULT', 'One of those creepers exploded again huh? Yeah, they creep me out too...'),
('INSULT', 'Monsters aren''t as nice as they look!'),
('INSULT', 'Don''t try to catch those things flying at you.'),
('INSULT', 'You just proved dying is easy'),
('INSULT', 'BANG! Creeper shot'),
('INSULT', 'Hello? Yes, this is death calling, I''d like a refund, this body has no brain'),
('INSULT', 'Unlike in the movies, pig crashes don''t let you walk away unharmed'),
('INSULT', 'Whos bright idea was it to poke that creeper?'),
('INSULT', 'BOOM goes the creeper!'),
('INSULT', 'You''re alive! Syke.'),
('INSULT', 'The giant green exploding creeper found you once, he can do it again (and again).'),
('INSULT', 'Was it Blackx''s fill tool that killed you (and the server) this time?.'),
('INSULT', 'Angry Wolf was not appeased.'),
('INSULT', 'Never gonna give you up, never gonna let you down... Looks like Rick let you down'),
('INSULT', 'TNT doesn''t work on this server yet you seem to have killed yourself with it'),
('INSULT', 'Only an oaf like you could have bungled that up'),
('INSULT', 'Quick, take his stuff!'),
('INSULT', 'Maybe you shouldn''t be using a gold sword...'),
('INSULT', 'That is why you should carry arrows when you use a bow'),
('INSULT', 'Looks like you got alittle *puts on glasses* creeped out'),
('INSULT', 'wait going going gone yup hes dead!!'),
('INSULT', 'I know you Had an itch,but couldn''t you wait until u were in private?'),
('INSULT', 'Feel Admins''s wrath'),
('INSULT', 'JAYT8 welcome you to the server by doing this.'),
('INSULT', 'Even the nether rejects you'),
('INSULT', 'You were smoth by the hammer of Thor'),
('INSULT', 'was smited by Josh121297 from senior heaven'),
('INSULT', 'You did see that ledge there...didn''t you?'),
('INSULT', 'Another one assassinated by Toddtedd.'),
('INSULT', 'Another one bites the dust.'),
('INSULT', 'You dun goofed.'),
('INSULT', 'You weren''t very lucky, punk.'),
('INSULT', 'HEADSHOT!'),
('INSULT', 'Pow! Right in the kisser!'),
('INSULT', 'Does that mean that the mushroom WAS Poisonous?'),
('INSULT', 'Well, there''s your problem. You''re dead!'),
('INSULT', 'Note to self: Do not dry hump TNT'),
('INSULT', 'Molesting creepers is a no-no.'),
('INSULT', 'Please keep your arms and legs inside the coffin at all times.'),
('INSULT', 'Bloody bugger to you, you beastly bastard.'),
('INSULT', 'You had one job...'),
('INSULT', 'You talked about Fight Club didn''t you.'),
('INSULT', 'You got the ol'' Onesey Twosey'),
('INSULT', 'Pow! Right in the kisser!'),
('QUIT', 'deserted from the battlefield with a hearty good bye!'),
('QUIT', 'stole the cookies and ran!'),
('QUIT', 'was eaten by a teenage mutant ninja platypus!'),
('QUIT', 'parachuted of the plane and into the unknown!'),
('QUIT', 'was eaten by a teenage mutant ninja creeper!'),
('QUIT', 'jumped off the plane with a cobble stone parachute!'),
('QUIT', 'built Rome in one day and now deserves a break!'),
('QUIT', 'will come back soon because Tregmine is awesome!'),
('QUIT', 'leaves the light and enter darkness.'),
('QUIT', 'disconnects from a better life.'),
('QUIT', 'already miss the best friends in the world!'),
('QUIT', 'will build something epic next time.'),
('QUIT', 'is not banned... yet!'),
('QUIT', 'has left our world!'),
('QUIT', 'went to browse Tregmine''s forums instead!'),
('QUIT', 'logged out by accident!'),
('QUIT', 'found the IRL warp!'),
('QUIT', 'left the game due to IRL chunk error issues!'),
('QUIT', 'left the Matrix. Say hi to Morpheus!'),
('QUIT', 'disconnected? What is this!? Impossibru!'),
('QUIT', 'found a lose cable and ate it.'),
('QUIT', 'found the true END of minecraft.'),
('QUIT', 'found love elswhere.'),
('QUIT', 'rage quit the server.'),
('QUIT', 'was not accidently banned by BlackX'),
('QUIT', 'got TROLLED by TheScavenger101'),
('QUIT', 'lost an epic rap battle with einand'),
('QUIT', 'was bored to death by knipil'),
('QUIT', 'went squid fishing with GeorgeBombadil'),
('QUIT', 'shouldn''t have joined a spelling bee with mejjad'),
('QUIT', 'was paralyzed by a gaze from mksen');

-- --------------------------------------------------------

--
-- Table structure for table `motd`
--

CREATE TABLE `motd` (
  `motd_id` int(10) UNSIGNED NOT NULL,
  `motd_timestamp` int(10) UNSIGNED NOT NULL,
  `motd_message` text CHARACTER SET utf8
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

CREATE TABLE `player` (
  `player_id` int(10) UNSIGNED NOT NULL,
  `player_name` varchar(46) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_password` varchar(64) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_email` varchar(255) COLLATE utf8_swedish_ci DEFAULT NULL,
  `player_confirmed` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '0',
  `player_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `player_wallet` bigint(20) DEFAULT '50000',
  `player_rank` enum('unverified','tourist','settler','resident','donator','guardian','builder','coder','junior_admin','senior_admin') COLLATE utf8_swedish_ci DEFAULT 'unverified',
  `player_flags` int(10) UNSIGNED DEFAULT NULL,
  `player_keywords` text COLLATE utf8_swedish_ci NOT NULL,
  `player_ignore` text COLLATE utf8_swedish_ci NOT NULL,
  `player_uuid` char(43) COLLATE utf8_swedish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_home`
--

CREATE TABLE `player_home` (
  `home_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `home_name` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `home_x` double DEFAULT NULL,
  `home_y` double DEFAULT NULL,
  `home_z` double DEFAULT NULL,
  `home_pitch` double DEFAULT NULL,
  `home_yaw` double DEFAULT NULL,
  `home_world` varchar(32) COLLATE utf8_swedish_ci DEFAULT NULL,
  `home_time` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_login`
--

CREATE TABLE `player_login` (
  `login_id` int(10) UNSIGNED NOT NULL,
  `player_id` int(10) UNSIGNED DEFAULT NULL,
  `login_timestamp` int(10) UNSIGNED DEFAULT NULL,
  `login_action` enum('login','logout') CHARACTER SET utf8 DEFAULT NULL,
  `login_country` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_city` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_ip` varchar(15) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_hostname` varchar(100) COLLATE utf8_swedish_ci DEFAULT NULL,
  `login_onlineplayers` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  `orelog_world` varchar(255) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_property`
--

CREATE TABLE `player_property` (
  `player_id` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `property_key` varchar(255) COLLATE utf8_swedish_ci NOT NULL DEFAULT '',
  `property_value` varchar(255) COLLATE utf8_swedish_ci DEFAULT NULL,
  `property_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player_report`
--

CREATE TABLE `player_report` (
  `report_id` int(10) UNSIGNED NOT NULL,
  `subject_id` int(10) UNSIGNED NOT NULL,
  `issuer_id` int(10) UNSIGNED NOT NULL,
  `report_action` enum('kick','softwarn','hardwarn','ban','comment') CHARACTER SET utf8 NOT NULL,
  `report_message` text COLLATE utf8_swedish_ci NOT NULL,
  `report_timestamp` int(10) UNSIGNED NOT NULL,
  `report_validuntil` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  `warp_name` varchar(45) COLLATE utf8_swedish_ci DEFAULT NULL,
  `warp_x` double DEFAULT NULL,
  `warp_y` double DEFAULT NULL,
  `warp_z` double DEFAULT NULL,
  `warp_pitch` double DEFAULT NULL,
  `warp_yaw` double DEFAULT NULL,
  `warp_world` varchar(45) COLLATE utf8_swedish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  `zone_world` varchar(50) COLLATE utf8_swedish_ci NOT NULL DEFAULT 'world',
  `zone_name` varchar(32) COLLATE utf8_swedish_ci NOT NULL,
  `zone_enterdefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_placedefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_destroydefault` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '1',
  `zone_pvp` enum('0','1') COLLATE utf8_swedish_ci NOT NULL DEFAULT '0',
  `zone_hostiles` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '1',
  `zone_communist` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '0',
  `zone_publicprofile` enum('0','1') COLLATE utf8_swedish_ci DEFAULT '0',
  `zone_entermessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_exitmessage` varchar(250) COLLATE utf8_swedish_ci NOT NULL,
  `zone_texture` text COLLATE utf8_swedish_ci,
  `zone_owner` varchar(24) COLLATE utf8_swedish_ci DEFAULT NULL,
  `zone_flags` int(10) UNSIGNED DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_swedish_ci;

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
  MODIFY `inventory_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `inventory_accesslog`
--
ALTER TABLE `inventory_accesslog`
  MODIFY `accesslog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `inventory_changelog`
--
ALTER TABLE `inventory_changelog`
  MODIFY `changelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `inventory_item`
--
ALTER TABLE `inventory_item`
  MODIFY `item_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `item`
--
ALTER TABLE `item`
  MODIFY `id` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=396;
--
-- AUTO_INCREMENT for table `mentorlog`
--
ALTER TABLE `mentorlog`
  MODIFY `mentorlog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `motd`
--
ALTER TABLE `motd`
  MODIFY `motd_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player`
--
ALTER TABLE `player`
  MODIFY `player_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_badge`
--
ALTER TABLE `player_badge`
  MODIFY `badge_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_chatlog`
--
ALTER TABLE `player_chatlog`
  MODIFY `chatlog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_givelog`
--
ALTER TABLE `player_givelog`
  MODIFY `givelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_home`
--
ALTER TABLE `player_home`
  MODIFY `home_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_login`
--
ALTER TABLE `player_login`
  MODIFY `login_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_orelog`
--
ALTER TABLE `player_orelog`
  MODIFY `orelog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_report`
--
ALTER TABLE `player_report`
  MODIFY `report_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_transaction`
--
ALTER TABLE `player_transaction`
  MODIFY `transaction_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `player_webcookie`
--
ALTER TABLE `player_webcookie`
  MODIFY `webcookie_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `shorturl`
--
ALTER TABLE `shorturl`
  MODIFY `urlID` int(11) NOT NULL AUTO_INCREMENT;
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
  MODIFY `warp_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `warp_log`
--
ALTER TABLE `warp_log`
  MODIFY `log_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `zone`
--
ALTER TABLE `zone`
  MODIFY `zone_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `zone_lot`
--
ALTER TABLE `zone_lot`
  MODIFY `lot_id` int(10) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `zone_profile`
--
ALTER TABLE `zone_profile`
  MODIFY `profile_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `zone_rect`
--
ALTER TABLE `zone_rect`
  MODIFY `rect_id` int(10) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
