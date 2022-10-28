CREATE TABLE Clients (
id UNIQUEIDENTIFIER PRIMARY KEY,
username varchar(50),
[password] varchar(50),
email varchar(50),
logCount integer
)

CREATE TABLE LOGS (
id UNIQUEIDENTIFIER PRIMARY KEY,
message varchar(1024),
logType integer,
logTypeName varchar(50),
dateOfLog DATE
)

ALTER TABLE Logs
ADD token UNIQUEIDENTIFIER