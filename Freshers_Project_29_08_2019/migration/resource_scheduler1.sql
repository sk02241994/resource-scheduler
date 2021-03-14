
-- using schema resource_scheduler 

-- create schema resource_scheduler;

-- table for user

create table rs_user (rs_user_id int(11) auto_increment,
						first_name varchar(50) not null,
                        last_name varchar(50) not null,
                        email_address varchar(50) not null,
                        password varchar(30) not null,
                        designation varchar(10) not null,
                        address varchar(70),
                        department varchar(50) not null,
                        is_active tinyint(1) not null,
                        created_by varchar(50),
                        created_date datetime,
                        updated_by varchar(50),
                        updated_date datetime,
                        primary key (rs_user_id));

-- table for rs_resource
                        
create table rs_resource (rs_resource_id int(11) auto_increment,
							resource_name varchar(50) not null,
                            is_active tinyint(1) not null,
                            created_by varchar(50),
                            created_date datetime,
                            updated_by varchar(50),
                            updated_date datetime,
                            primary key (rs_resource_id));

-- table for rs_reservation

create table rs_reservation (rs_reservation_id int(11) auto_increment,
								rs_user_id int(11),
                                rs_resource_id int(11),
                                start_date datetime not null,
                                end_date datetime not null,
                                created_by varchar(50),
                                created_date datetime,
                                updated_by varchar(50),
                                updated_date datetime,
                                primary key (rs_reservation_id),
                                foreign key (rs_user_id) references rs_user(rs_user_id),
                                foreign key (rs_resource_id) references rs_resource(rs_resource_id));
								
ALTER TABLE `rs_user` 
ADD COLUMN `is_admin` TINYINT(1) NULL DEFAULT 0 AFTER `updated_date`;