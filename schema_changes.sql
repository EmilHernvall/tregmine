ALTER TABLE user_settings DROP INDEX id;
ALTER TABLE user_settings ADD PRIMARY KEY (id, `key`);
ALTER TABLE zone CHANGE COLUMN texture zone_texture TEXT;
ALTER TABLE zone CHANGE COLUMN owner zone_owner VARCHAR (24);
