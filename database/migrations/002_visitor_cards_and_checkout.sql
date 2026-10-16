USE visitor_management;

ALTER TABLE visitors
    ADD COLUMN check_out_at DATETIME NULL AFTER check_in_at,
    ADD COLUMN visitor_photo MEDIUMBLOB NULL AFTER check_out_at,
    ADD INDEX idx_visitors_check_out_at (check_out_at, check_in_at);
