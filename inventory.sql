--
-- Table structure for table `playerInventory`
--

CREATE TABLE IF NOT EXISTS `playerInventory` (
  `inventory_id` int(10) NOT NULL AUTO_INCREMENT,
  `player_id` int(10) NOT NULL,
  `inventory_name` varchar(255) DEFAULT NULL,
  `inventory_type` varchar(255) NOT NULL,
  PRIMARY KEY (`inventory_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Table structure for table `playerInventory_items`
--

CREATE TABLE IF NOT EXISTS `playerInventory_items` (
  `item_id` int(10) NOT NULL AUTO_INCREMENT,
  `inventory_id` int(10) DEFAULT NULL,
  `item_slot` int(10) DEFAULT NULL,
  `item_material` int(10) DEFAULT NULL,
  `item_data` int(11) DEFAULT NULL,
  `item_meta` text,
  `item_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
