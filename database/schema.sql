    CREATE DATABASE IF NOT EXISTS visitor_management
        CHARACTER SET utf8mb4
        COLLATE utf8mb4_unicode_ci;

    USE visitor_management;

    CREATE TABLE IF NOT EXISTS visitors (
        id BIGINT NOT NULL AUTO_INCREMENT,
        visitor_name VARCHAR(120) NOT NULL,
        mobile_number VARCHAR(25) NOT NULL,
        person_to_visit VARCHAR(120) NOT NULL,
        purpose VARCHAR(500) NOT NULL,
        check_in_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        check_out_at DATETIME NULL,
        visitor_photo MEDIUMBLOB NULL,
        PRIMARY KEY (id),
        INDEX idx_visitors_check_in_at (check_in_at),
        INDEX idx_visitors_check_out_at (check_out_at, check_in_at),
        INDEX idx_visitors_mobile_number (mobile_number)
    ) ENGINE=InnoDB;
