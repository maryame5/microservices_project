-- Initialize MySQL databases for each microservice
CREATE DATABASE IF NOT EXISTS db_user;
CREATE DATABASE IF NOT EXISTS db_book;
CREATE DATABASE IF NOT EXISTS db_emprunter;

-- Create shared application user with privileges on all service databases
CREATE USER IF NOT EXISTS 'crm_user'@'%' IDENTIFIED BY 'crm_password';
GRANT ALL PRIVILEGES ON db_user.* TO 'crm_user'@'%';
GRANT ALL PRIVILEGES ON db_book.* TO 'crm_user'@'%';
GRANT ALL PRIVILEGES ON db_emprunter.* TO 'crm_user'@'%';
FLUSH PRIVILEGES;
