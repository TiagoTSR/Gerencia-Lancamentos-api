CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `password` VARCHAR(150) NOT NULL,
    `account_non_expired` BIT(1) DEFAULT NULL,
    `account_non_locked` BIT(1) DEFAULT NULL,
    `credentials_non_expired` BIT(1) DEFAULT NULL,
    `enabled` BIT(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
