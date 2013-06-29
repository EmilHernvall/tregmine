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
