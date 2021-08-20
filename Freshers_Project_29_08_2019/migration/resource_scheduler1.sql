
-- table for resources

CREATE TABLE `rs_resource` (
  `rs_resource_id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_name` varchar(50) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `description` varchar(80) DEFAULT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `time_limit` int(11) DEFAULT NULL,
  `is_allowed_multiple` tinyint(1) DEFAULT '1',
  `max_user_bookings` int(11) DEFAULT NULL,
  PRIMARY KEY (`rs_resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8

-- table for user

CREATE TABLE `rs_user` (
  `rs_user_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email_address` varchar(50) NOT NULL,
  `password` varchar(30) NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`rs_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8

-- table for reservation

CREATE TABLE `rs_reservation` (
  `rs_reservation_id` int(11) NOT NULL AUTO_INCREMENT,
  `rs_user_id` int(11) DEFAULT NULL,
  `rs_resource_id` int(11) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`rs_reservation_id`),
  KEY `rs_user_id` (`rs_user_id`),
  KEY `rs_resource_id` (`rs_resource_id`),
  CONSTRAINT `rs_reservation_ibfk_1` FOREIGN KEY (`rs_user_id`) REFERENCES `rs_user` (`rs_user_id`),
  CONSTRAINT `rs_reservation_ibfk_2` FOREIGN KEY (`rs_resource_id`) REFERENCES `rs_resource` (`rs_resource_id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8

