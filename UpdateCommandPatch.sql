CREATE TABLE IF NOT EXISTS `version` (
  `version_id` int(255) NOT NULL AUTO_INCREMENT,
  `version_number` varchar(255) NOT NULL,
  `version_string` varchar(255) NOT NULL,
  PRIMARY KEY (`version_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

INSERT INTO `version` (`version_id`, `version_number`, `version_string`) VALUES
(1, '1.0.0', 'Added toggleable flying (/fly)::Bonemeal Enhancements::Coloured names in messages::Added alert words (/alert)');

INSERT INTO `motd` (`motd_id`, `motd_timestamp`, `motd_message`) VALUES
(2, 1384080821, 'Use /update to find what was added!');
