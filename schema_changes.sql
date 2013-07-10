ALTER TABLE user_settings DROP INDEX id;
ALTER TABLE user_settings ADD PRIMARY KEY (id, `key`);
ALTER TABLE zone CHANGE COLUMN texture zone_texture TEXT;
ALTER TABLE zone CHANGE COLUMN owner zone_owner VARCHAR (24);

ALTER TABLE chestbless RENAME inventory;
ALTER TABLE inventory ADD COLUMN inventory_id INTEGER UNSIGNED FIRST;
ALTER TABLE inventory CHANGE COLUMN checksum inventory_checksum int(11);
ALTER TABLE inventory CHANGE COLUMN world inventory_world varchar (32);
ALTER TABLE inventory CHANGE COLUMN player inventory_player varchar (32);
ALTER TABLE inventory ADD COLUMN player_id INT UNSIGNED AFTER inventory_id;
ALTER TABLE inventory DROP INDEX chs;
ALTER TABLE inventory DROP INDEX checksum;
ALTER TABLE inventory ADD PRIMARY KEY (inventory_id);
ALtER TABLE inventory MODIFY COLUMN inventory_id INTEGER UNSIGNED AUTO_INCREMENT;
ALTER TABLE inventory ADD COLUMN inventory_x INT AFTER inventory_checksum;
ALTER TABLE inventory ADD COLUMN inventory_y INT AFTER inventory_x;
ALTER TABLE inventory ADD COLUMN inventory_z INT AFTER inventory_y;
UPDATE inventory SET player_id = (SELECT uid FROM `user` WHERE player = inventory_player);
ALTER TABLE inventory ADD COLUMN inventory_type ENUM ('block', 'player') DEFAULT 'block';

ALTER TABLE home RENAME player_home;
ALTER TABLE player_home ADD COLUMN home_id INTEGER UNSIGNED FIRST;
ALTER TABLE player_home ADD PRIMARY KEY (home_id);
ALTER TABLE player_home MODIFY COLUMN home_id INT UNSIGNED AUTO_INCREMENT;
ALTER TABLE player_home CHANGE COLUMN name home_name VARCHAR (32),
                        CHANGE COLUMN x home_x DOUBLE,
                        CHANGE COLUMN y home_y DOUBLE,
                        CHANGE COLUMN z home_z DOUBLE,
                        CHANGE COLUMN pitch home_pitch DOUBLE,
                        CHANGE COLUMN yaw home_yaw DOUBLE,
                        CHANGE COLUMN world home_world VARCHAR (32),
                        CHANGE COLUMN time home_time DOUBLE;
ALTER TABLE player_home ADD COLUMN player_id INTEGER UNSIGNED AFTER home_id;
ALTER TABLE player_home ADD INDEX player_idx (player_id, home_time);
ALTER TABLE player_home DROP INDEX name;

ALTER TABLE `user` RENAME player;
ALTER TABLE player CHANGE COLUMN uid player_id INTEGER UNSIGNED AUTO_INCREMENT,
                   CHANGE COLUMN player player_name VARCHAR (46),
                   CHANGE COLUMN password player_password VARCHAR (64),
                   CHANGE COLUMN email player_email VARCHAR (255),
                   CHANGE COLUMN confirmed player_confirmed ENUM ('0', '1') DEFAULT '0',
                   CHANGE COLUMN time player_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE user_settings RENAME player_property;
ALTER TABLE player_property CHANGE COLUMN id player_id INTEGER UNSIGNED,
                            CHANGE COLUMN `key` property_key VARCHAR (255),
                            CHANGE COLUMN value property_value VARCHAR (255),
                            DROP COLUMN username,
                            CHANGE COLUMN created property_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE user_report RENAME player_report;

CREATE TABLE player_login (
    login_id INT UNSIGNED AUTO_INCREMENT,
    player_id INT UNSIGNED,
    login_timestamp INT UNSIGNED,
    login_action ENUM ('login', 'logout'),
    PRIMARY KEY (login_id)
);

CREATE TABLE player_transaction (
    transaction_id INT UNSIGNED AUTO_INCREMENT,
    sender_id INT UNSIGNED,
    recipient_id INT UNSIGNED,
    transaction_timestamp INT UNSIGNED,
    transaction_amount INT UNSIGNED,
    PRIMARY KEY (transaction_id)
);

CREATE TABLE player_chatlog (
    chatlog_id INT UNSIGNED AUTO_INCREMENT,
    player_id INT UNSIGNED,
    chatlog_timestamp INT UNSIGNED,
    chatlog_channel VARCHAR (64),
    chatlog_message VARCHAR (255),
    PRIMARY KEY (chatlog_id)
);

CREATE TABLE inventory_item (
    item_id INT UNSIGNED AUTO_INCREMENT,
    inventory_id INT UNSIGNED,
    item_slot INT UNSIGNED,
    item_material INT UNSIGNED,
    item_data INT UNSIGNED,
    item_meta TEXT,
    item_count INT UNSIGNED,
    PRIMARY KEY (item_id),
    INDEX inventory_idx (inventory_id)
);

CREATE TABLE trade (
    trade_id INT UNSIGNED AUTO_INCREMENT,
    sender_id INT UNSIGNED,
    recipient_id INT UNSIGNED,
    trade_timestamp INT UNSIGNED,
    trade_amount INT UNSIGNED,
    PRIMARY KEY (trade_id)
);

CREATE TABLE trade_item (
    item_id INT UNSIGNED AUTO_INCREMENT,
    trade_id INT UNSIGNED,
    item_material INT UNSIGNED,
    item_data INT UNSIGNED,
    item_meta TEXT,
    item_count INT UNSIGNED,
    PRIMARY KEY (item_id)
);

ALTER TABLE inventory MODIFY COLUMN inventory_type ENUM ('chest', 'player', 'player_armor');
ALTER TABLE inventory_item MODIFY COLUMN item_data INT;

CREATE TABLE player_orelog (
    orelog_id INT UNSIGNED AUTO_INCREMENT,
    player_id INT UNSIGNED,
    orelog_material INT UNSIGNED,
    orelog_timestamp INT UNSIGNED,
    PRIMARY KEY (orelog_id),
    INDEX player_idx (player_id, orelog_timestamp)
);

CREATE TABLE player_givelog (
    givelog_id INT UNSIGNED AUTO_INCREMENT,
    sender_id INT UNSIGNED,
    recipient_id INT UNSIGNED,
    givelog_material INT UNSIGNED,
    givelog_data INT UNSIGNED,
    givelog_meta TEXT,
    givelog_count INT UNSIGNED,
    givelog_timestamp INT UNSIGNED,
    PRIMARY KEY (givelog_id)
);
