-- Create the user yu71 with full access using mysql_native_password
CREATE USER IF NOT EXISTS 'yu71'@'%' IDENTIFIED WITH mysql_native_password BY '53cret';
CREATE USER IF NOT EXISTS 'yu71'@'localhost' IDENTIFIED WITH mysql_native_password BY '53cret';
GRANT ALL PRIVILEGES ON cutiaw_db.* TO 'yu71'@'%';
GRANT ALL PRIVILEGES ON cutiaw_db.* TO 'yu71'@'localhost';
FLUSH PRIVILEGES;
