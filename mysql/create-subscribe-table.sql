create database API;
use API;

CREATE TABLE IF NOT EXISTS subscriptions (
    token VARCHAR(255) NOT NULL,
    transaction VARCHAR(255) NOT NULL,
    account VARCHAR(12) NOT NULL,
    secret VARCHAR(20) NOT NULL,
    issue_date DATE NOT NULL,
    expiration_date DATE NOT NULL,
    plan VARCHAR(2) NOT NULL,
    status TINYINT NOT NULL,
    eos_paid FLOAT NOT NULL,
    memo TEXT,
    PRIMARY KEY (token)
)  ENGINE=INNODB;