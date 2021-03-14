
-- insert data into rs_user table

insert into rs_user (first_name, last_name, email_address, password, designation, address, department, is_active, created_by, created_date, updated_by, updated_date) 
			values ('tim', 
					'burton', 
                    'tim.burton@gm.com', 
                    'admin', 
                    'manager', 
                    'earth', 
                    'IT', 
                    1,
                    'tim burton',
                    now(),
                    'tim burton',
                    now());

-- insert data into rs_resource table

insert into rs_resource (resource_name, is_active, created_by, created_date, updated_by, updated_date) 
			values ('conference Hall 1', 
					1, 
                    'tim burton', 
                    now(), 
                    'tim burton', 
                    now());

-- insert date into rs_reservation  table

insert into rs_reservation (rs_user_id, rs_resource_id, start_date, end_date, created_by, created_date, updated_by, updated_date) 
			values (1, 
					1, 
                    '2019-09-09 10:44:29', 
                    '2019-09-10 10:25:00',
                    'tim burton',
                    now(),
                    'tim burton',
                    now());