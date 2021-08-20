ALTER TABLE `rs_user` 
ADD COLUMN `gender` VARCHAR(1) NULL DEFAULT NULL AFTER `is_admin`,
ADD COLUMN `is_permanent_employee` TINYINT(1) NULL DEFAULT '0' AFTER `gender`;

ALTER TABLE `resource_scheduler`.`rs_resource` 
ADD COLUMN `is_permanent_employee` TINYINT(1) NULL DEFAULT '0' AFTER `max_user_bookings`;
