CREATE USER "app-user" WITH PASSWORD 'vaultMaster123';
CREATE DATABASE "app" OWNER "app-user";
GRANT ALL PRIVILEGES ON DATABASE "app" TO "app-user";