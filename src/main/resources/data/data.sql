/*--------------------------------------------------------------*/
/* 					table: user_role                      		*/
/*--------------------------------------------------------------*/
INSERT INTO schedulesys_db.user_role(id, name) values(1, 'ADMIN');
INSERT INTO schedulesys_db.user_role(id, name) values(2, 'SUPERVISOR');

/*--------------------------------------------------------------*/
/* 					table: employee_type                      	*/
/*--------------------------------------------------------------*/
insert into schedulesys_db.employee_type(id, name) values(1, 'Nurse');
insert into schedulesys_db.employee_type(id, name) values(2, 'Care Giver');
insert into schedulesys_db.employee_type(id, name) values(3, 'Aide');

/*--------------------------------------------------------------*/
/* 					table: schedule_sys_user                   	*/
/*--------------------------------------------------------------*/
-- $2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa = secret
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(01, 1, 'ezerbo', 'Boureima Edouard', 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'ezerbo@simplesoft.com', true, '54947df0-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(02, 1, 'asudam', 'Abhinav'         , 'SUDAM', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'asudam@simplesoft.com', true, '54947df1-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(03, 1, 'sballo', 'Saibou'          , 'BALLO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'sballo@simplesoft.com', true, '54947df2-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(04, 2, 'mzerbo', 'Michel'          , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'mzerbo@simplesoft.com', true, '54947df3-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(05, 2, 'fzerbo', 'Fred'            , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'fzerbo@simplesoft.com', true, '54947df4-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(06, 2, 'czerbo', 'Chacour'         , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'czerbo@simplesoft.com', true, '54947df5-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(07, 2, 'szerbo', 'Sherif'          , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'szerbo@simplesoft.com', true, '54947df6-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(08, 2, 'nzerbo', 'Naftal'          , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'nzerbo@simplesoft.com', true, '54947df7-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(09, 2, 'dzerbo', 'Djamie'          , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'dzerbo@simplesoft.com', true, '54947df8-0e9e-4471-a2f9-9af509fb5889', current_timestamp());
insert into schedulesys_db.schedule_sys_user(id, role_id, username, first_name, last_name, password, email_address, activated, activation_key, create_date) values(10, 2, 'lzerbo', 'Landolo'         , 'ZERBO', '$2a$10$iVlvexqblLTUt96cIlQWkOPmbAMdLVqG8ZipIsMGcGwpxTzY7QrNa', 'lzerbo@simplesoft.com', true, '54947df9-0e9e-4471-a2f9-9af509fb5889', current_timestamp());

/*--------------------------------------------------------------*/
/* 						table: position 	                   	*/
/*--------------------------------------------------------------*/
insert into schedulesys_db.position(id, name) values(1, 'rn');
insert into schedulesys_db.position(id, name) values(2, 'lpn');
insert into schedulesys_db.position(id, name) values(3, 'cna');
insert into schedulesys_db.position(id, name) values(4, 'chha');
insert into schedulesys_db.position(id, name) values(5, 'cma');
insert into schedulesys_db.position(id, name) values(6, 'cna/chha');
insert into schedulesys_db.position(id, name) values(7, 'cna/cma');
insert into schedulesys_db.position(id, name) values(8, 'cna/chha/cma');
insert into schedulesys_db.position(id, name) values(9, 'rn/lpn');
insert into schedulesys_db.position(id, name) values(10,'lpn/cna');


/*--------------------------------------------------------------*/
/* 						table: employee	                        */
/*--------------------------------------------------------------*/

insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(1, 1,   'Boureima Edouard'	, 'ZERBO'  , current_date() ,current_date(), false, true, 'comment on employee with id 1' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(2, 2,   'Abhinav'				, 'SUDAM'  , current_date() ,current_date(), false, true, 'comment on employee with id 2' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(3, 3,   'Saibou'				, 'BALLO'  , current_date() ,current_date(), false, true, 'comment on employee with id 3' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(4, 4,   'Mukayila'			, 'ALAO'   , current_date() ,current_date(), false, true, 'comment on employee with id 4' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(5, 5,   'Boukary'				, 'KANAZOE', current_date() ,current_date(), false, true, 'comment on employee with id 5' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(6, 6,   'Flora Madina'		, 'SANOGO' , current_date() ,current_date(), false, true, 'comment on employee with id 6' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(7, 7,   'Abdoulaye'			, 'BADIAGA', current_date() ,current_date(), false, true, 'comment on employee with id 7' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(8, 8,   'Hibrahim Sory'		, 'TRAORE' , current_date() ,current_date(), false, true, 'comment on employee with id 8' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(9, 9,   'Adama'				, 'DEMBELE', current_date() ,current_date(), false, true, 'comment on employee with id 9' ,1);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(10, 10, 'Souleymane'			, 'BELEM'  , current_date() ,current_date(), false, true, 'comment on employee with id 10',1);
                                                                                                               
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(11, 10, 'Claus'					, 'IBSEN'	, current_date(), current_date(), false, true, 'comment on employee with id 11', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(12, 10, 'James'					, 'STRACHAN', current_date(), current_date(), false, true, 'comment on employee with id 12', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(13, 10, 'Cheick Oumar Landolo'	, 'ZERBO'	, current_date(), current_date(), false, true, 'comment on employee with id 13', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(14, 10, 'Aziz'					, 'KONE'	, current_date(), current_date(), false, true, 'comment on employee with id 14', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(15, 10, 'Adama'					, 'BILINGUI', current_date(), current_date(), false, true, 'comment on employee with id 15', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(16, 10, 'Adama Stephane'			, 'SALOU'	, current_date(), current_date(), false, true, 'comment on employee with id 16', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(17, 10, 'Hamed'					, 'TRAORE'	, current_date(), current_date(), false, true, 'comment on employee with id 17', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(18, 10, 'Aboubacar'				, 'SORGHO'	, current_date(), current_date(), false, true, 'comment on employee with id 18', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(19, 10, 'Gerard'					, 'SORGHO'	, current_date(), current_date(), false, true, 'comment on employee with id 19', 2);
insert into schedulesys_db.employee (id, position_id, first_name, last_name, date_of_hire, last_day_of_work, ebc, insvc, comment, type_id) values(20, 10, 'Moussa'					, 'ZIDA'	, current_date(), current_date(), false, true, 'comment on employee with id 20', 2);


/*--------------------------------------------------------------*/
/* 						table: phone_number 	                */
/*--------------------------------------------------------------*/

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (1, '7187109836', 'PRIMARY'  , 'HOME'   , 1);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (2, '7187119836', 'SECONDARY', 'MOBILE' , 1);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (3, '7187129836', 'OTHER'    , 'HOME'   , 1);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (4, '7187139836', 'PRIMARY'  , 'MOBILE' , 2);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (5, '7187149836', 'SECONDARY', 'HOME'   , 2);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (6, '7187159836', 'OTHER'    , 'MOBILE' , 2);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (7, '7187169836', 'PRIMARY'  , 'HOME'   , 3);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (8, '7187179836', 'SECONDARY', 'MOBILE' , 3);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (9, '7187189836', 'OTHER'    , 'HOME'   , 3);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (10, '7181909836', 'PRIMARY'  , 'HOME'  , 4);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (11, '7181989836', 'SECONDARY', 'MOBILE', 4);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (12, '7181979836', 'OTHER'    , 'HOME'  , 4);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (13, '6187909836', 'PRIMARY'  , 'HOME'  , 5);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (14, '6187989836', 'SECONDARY', 'MOBILE', 5);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (15, '6187979836', 'OTHER'    , 'HOME'  , 5);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (16, '5187909836', 'PRIMARY'  , 'HOME'  , 6);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (17, '5187989836', 'SECONDARY', 'MOBILE', 6);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (18, '5187979836', 'OTHER'    , 'HOME'  , 6);
                                                                                          
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (19, '4187909836', 'PRIMARY'  , 'HOME'  , 7);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (20, '4187989836', 'SECONDARY', 'MOBILE', 7);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (21, '4187979836', 'OTHER'    , 'HOME'  , 7);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (22, '3187909836', 'PRIMARY'  , 'HOME'  , 8);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (23, '3187989836', 'SECONDARY', 'MOBILE', 8);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (24, '3187979836', 'OTHER'    , 'HOME'  , 8);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (25, '2187909836', 'PRIMARY'  , 'HOME'  , 9);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (26, '2187989836', 'SECONDARY', 'MOBILE', 9);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (27, '2187979836', 'OTHER'    , 'HOME'  , 9);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (28, '1187909836', 'PRIMARY'  , 'HOME'  , 10);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (29, '1187989836', 'SECONDARY', 'MOBILE', 10);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (30, '1187979836', 'OTHER'    , 'HOME'  , 10);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (31, '8187909836', 'PRIMARY'  , 'HOME'  , 11);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (32, '8187989836', 'SECONDARY', 'MOBILE', 11);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (33, '8187979836', 'OTHER'    , 'HOME'  , 11);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (34, '9187901136', 'PRIMARY'  , 'HOME'  , 12);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (35, '9186989836', 'SECONDARY', 'MOBILE', 12);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (36, '9187979836', 'OTHER'    , 'HOME'  , 12);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (37, '9287909836', 'PRIMARY'  , 'HOME'  , 13);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (38, '9287989836', 'SECONDARY', 'MOBILE', 13);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (39, '9287979836', 'OTHER'    , 'HOME'  , 13);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (40, '9487909836', 'PRIMARY'  , 'HOME'  , 14);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (41, '9587989836', 'SECONDARY', 'MOBILE', 14);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (42, '9680979836', 'OTHER'    , 'HOME'  , 14);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (43, '9186909836', 'PRIMARY'  , 'HOME'  , 15);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (44, '9186339836', 'SECONDARY', 'MOBILE', 15);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (45, '9188979836', 'OTHER'    , 'HOME'  , 15);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (46, '9187905836', 'PRIMARY'  , 'HOME'  , 16);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (47, '9187299836', 'SECONDARY', 'MOBILE', 16);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (48, '9187009836', 'OTHER'    , 'HOME'  , 16);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (49, '9187900836', 'PRIMARY'  , 'HOME'  , 17);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (50, '9187989836', 'SECONDARY', 'MOBILE', 17);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (51, '9185979836', 'OTHER'    , 'HOME'  , 17);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (52, '9787909866', 'PRIMARY'  , 'HOME'  , 18);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (53, '9987989872', 'SECONDARY', 'MOBILE', 18);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (54, '9387979800', 'OTHER'    , 'HOME'  , 18);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (55, '9387009036', 'PRIMARY'  , 'HOME'  , 19);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (56, '9787089036', 'SECONDARY', 'MOBILE', 19);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (57, '9687079036', 'OTHER'    , 'HOME'  , 19);

insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (58, '9507909836', 'PRIMARY'  , 'HOME'  , 20);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (59, '9407989836', 'SECONDARY', 'MOBILE', 20);
insert into schedulesys_db.phone_number (id, number, type, label, employee_id) values (60, '9207979836', 'OTHER'    , 'HOME'  , 20);


/*------------------------------------------------------------------*/
/* 						table: care_company_type                    */
/*------------------------------------------------------------------*/

insert into schedulesys_db.care_company_type(id, name) values (1, 'Facility');
insert into schedulesys_db.care_company_type(id, name) values (2, 'Private care');


/*------------------------------------------------------------------*/
/* 						table: care_company                         */
/*------------------------------------------------------------------*/

insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(01, 'Sunnyside'               , '7080 Samuel Morse Dr' , '9081899371', '9839838888', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(02, 'Brandywine'              , '650 S Exeter'         , '9081899322', '9839834444', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(03, 'Moutmouth'               , '442 Ridge Rd'         , '9081899113', '9839832222', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(04, 'Burnside'                , '1057 SIMPSON AVENUE'  , '9081899374', '9839838888', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(05, 'Bedford'                 , '229W 34TH STREET'     , '9081899325', '9839834444', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(06, 'Woodlawn'                , '834 East 155TH STREET', '9081899116', '9839832222', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(07, 'Beltville'               , '1138 MANOR AVENUE'    , '9081899377', '9839838888', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(08, 'Silver Spring'           , '1173 FULTON STREET'   , '9081899328', '9839834444', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(09, 'Jackson'                 , '15E 199TH STREET'     , '9081899119', '9839832222', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(10, 'Simpson'                 , '1075 GERARD AVENUE'   , '9081899110', '9839832222', 1);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(11, 'A Caring Hand'           , '7080 Samuel Morse Dr' , '9081899371', '9839838888', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(12, 'A Life Saver Home Care'  , '650 S Exeter'         , '9081899322', '9839834444', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(13, 'Age & The City Home Care', '442 Ridge Rd'         , '9081899113', '9839832222', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(14, 'All Care Hospice'        , '1057 SIMPSON AVENUE'  , '9081899374', '9839838888', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(15, 'Always Here Home Care'   , '229W 34TH STREET'     , '9081899325', '9839834444', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(16, 'Best Choice Home Care'   , '834 East 155TH STREET', '9081899116', '9839832222', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(17, 'Big Hearts Home Care'    , '1138 MANOR AVENUE'    , '9081899377', '9839838888', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(18, 'Cameo Caregivers'        , '1173 FULTON STREET'   , '9081899328', '9839834444', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(19, 'Caring Professionals'    , '15E 199TH STREET'     , '9081899119', '9839832222', 2);
insert into schedulesys_db.care_company (id, name, address, phone_number, fax, type_id) values(20,'Center Light Care'        , '1075 GERARD AVENUE'   , '9081899110', '9839832222', 2);


/*--------------------------------------------------------------*/
/* 					table: company_contact 	                    */
/*--------------------------------------------------------------*/

insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (1, 'contact-1-fn', 'contact-1-ln', 'contact-1-title', '1234569870','1789654321', 1);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (2, 'contact-2-fn', 'contact-2-ln', 'contact-2-title', '1234569872','1789654322', 2);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (3, 'contact-3-fn', 'contact-3-ln', 'contact-3-title', '1234569873','1789654323', 3);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (4, 'contact-4-fn', 'contact-4-ln', 'contact-4-title', '1234569874','1789654324', 4);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (5, 'contact-5-fn', 'contact-5-ln', 'contact-5-title', '1234569875','1789654325', 5);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (6, 'contact-6-fn', 'contact-6-ln', 'contact-6-title', '1234569876','1789654326', 6);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (7, 'contact-7-fn', 'contact-7-ln', 'contact-7-title', '1234569877','1789654327', 7);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (8, 'contact-8-fn', 'contact-8-ln', 'contact-8-title', '1234569878','1789654328', 8);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (9, 'contact-9-fn', 'contact-9-ln', 'contact-9-title', '1234569879','1789654329', 9);
insert into schedulesys_db.company_contact(id, first_name, last_name, title, phone_number, fax, company_id) values (10,'contact-10-fn','contact-10-ln','contact-10-title','1234569810','1789654311', 10);


/*--------------------------------------------------------------*/
/* 						TABLE: LICENSE_TYPE                     */
/*--------------------------------------------------------------*/

insert into schedulesys_db.license_type (id, name) values (1, 'rn');
insert into schedulesys_db.license_type (id, name) values (2, 'lpn');
insert into schedulesys_db.license_type (id, name) values (3, 'cna');
insert into schedulesys_db.license_type (id, name) values (4, 'cma');
insert into schedulesys_db.license_type (id, name) values (5, 'chha');


/*--------------------------------------------------------------*/
/* 						TABLE: LICENSE	                        */
/*--------------------------------------------------------------*/

insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (01, '111222333444551', 1, dateadd('DAY', 15, current_date()), 1);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (02, '111222333444552', 2, current_date(), 1);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (03, '111222333444553', 3, dateadd('DAY', -15, current_date()), 2);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (04, '111222333444554', 4, dateadd('DAY', 15, current_date()), 2);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (05, '111222333444555', 5, dateadd('DAY', 15, current_date()), 3);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (06, '111222333444556', 1, dateadd('DAY', 15, current_date()), 4);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (07, '111222333444557', 2, dateadd('DAY', 15, current_date()), 5);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (08, '111222333444558', 5, dateadd('DAY', 15, current_date()), 6);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (09, '111222333444559', 4, dateadd('DAY', 15, current_date()), 7);
insert into schedulesys_db.license (id, number, type_id, expiry_date, employee_id) values (10, '111222333444510', 3, dateadd('DAY', 15, current_date()), 8);


/*--------------------------------------------------------------*/
/* 					table: schedule_status                      */
/*--------------------------------------------------------------*/

insert into schedulesys_db.schedule_status(id, name) values(1, 'CONFIRMED');
insert into schedulesys_db.schedule_status(id, name) values(2, 'NOT CONFIRMED');
insert into schedulesys_db.schedule_status(id, name) values(3, 'TEXTED');
insert into schedulesys_db.schedule_status(id, name) values(4, 'LEFT MESSAGE');

/*--------------------------------------------------------------*/
/* 				table: schedule_post_status                     */
/*--------------------------------------------------------------*/

insert into schedulesys_db.schedule_post_status(id, name) values(1, 'ATTENDED');
insert into schedulesys_db.schedule_post_status(id, name) values(2, 'LATE');
insert into schedulesys_db.schedule_post_status(id, name) values(3, 'PENDING');
insert into schedulesys_db.schedule_post_status(id, name) values(4, 'ALWAYS LATE');
insert into schedulesys_db.schedule_post_status(id, name) values(5, 'DID NOT SHOW UP');
insert into schedulesys_db.schedule_post_status(id, name) values(6, 'UNKNOWN');


/*--------------------------------------------------------------*/
/*					table: schedule 			                */
/*--------------------------------------------------------------*/
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(01, 01, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 01);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(02, 02, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 02);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(03, 03, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 03);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(04, 04, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 04);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(05, 05, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 05);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(06, 06, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 06);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(07, 07, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 07);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(08, 08, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 08);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(09, 09, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 09);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(10, 10, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 10);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(11, 11, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 11);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(12, 12, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 12);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(13, 13, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 13);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(14, 14, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 14);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(15, 15, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 15);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(16, 16, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 16);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(17, 17, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 17);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(18, 18, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 18);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(19, 19, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 19);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(20, 20, current_timestamp(), dateadd('HOUR', 01, current_timestamp()),  current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 20);
                                                                                                                                                                                                                                                                                                                                                      
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(21, 01, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 20);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(22, 02, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 01);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(23, 03, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 02);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(24, 04, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 03);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(25, 05, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 04);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(26, 06, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 05);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(27, 07, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 06);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(28, 08, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 07);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(29, 09, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 08);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(30, 10, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 09);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(31, 11, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 10);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(32, 12, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 11);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(33, 13, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 12);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(34, 14, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 13);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(35, 15, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 14);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(36, 16, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 15);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(37, 17, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 16);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(38, 18, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 17);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(39, 19, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, true,  true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 18);
insert into schedulesys_db.schedule (id, care_company_id, shift_start_time, shift_end_time, schedule_date, status_id, hours_worked, overtime, post_status_id, time_sheet_received, paid, billed, comment, create_date, user_id, employee_id) values(40, 20, dateadd('HOUR', 02, current_timestamp()), dateadd('HOUR', 03, current_timestamp()), current_date(), 1, 10, 2, 1, false, true, false, 'comment on schedule', dateadd('DAY', -15, current_date()), 1, 19);


/*--------------------------------------------------------------*/
/* 							table: test                         */
/*--------------------------------------------------------------*/

insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (01, 'Drug test'    , true , true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (02, 'Flu vaccine'  , true , false, true);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (03, 'Tb test'      , false, false, true);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (04, 'Drug-2 test'  , false, false, true);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (05, 'Mmr titer'    , true , true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (06, 'Physical'     , false, true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (07, 'Sample test'  , false, true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (08, 'Sample-1 test', false, true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (09, 'Sample-2 test', false, true , false);
insert into schedulesys_db.test (id, name, can_be_waived, has_completion_date, has_expiry_date) values (10, 'Sample-3 test', false, true , false);


/*--------------------------------------------------------------*/
/* 					table: test_subcategory                     */
/*--------------------------------------------------------------*/

insert into schedulesys_db.test_subcategory (id, name, test_id) values (01, 'Annual ppd'      , 3);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (02, '2-step ppd'      , 3);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (03, 'quantiferon gold', 3);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (04, 'Sample-1'        , 1);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (05, 'Sample-2'        , 2);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (06, 'Sample-3'        , 4);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (07, 'Sample-4'        , 5);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (08, 'Sample-5'        , 6);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (09, 'Sample-6'        , 7);
insert into schedulesys_db.test_subcategory (id, name, test_id) values (10, 'Sample-7'        , 8);

/*--------------------------------------------------------------*/
/* 					table: test_occurrence                     	*/
/*--------------------------------------------------------------*/

insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (01, 01, 01, current_date(), dateadd('DAY', -15, current_date()), 4, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (02, 02, 02, current_date(), dateadd('DAY', -15, current_date()), 5, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (03, 03, 03, current_date(), dateadd('DAY', -15, current_date()), 3, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (04, 04, 04, current_date(), dateadd('DAY', -15, current_date()), 6, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (05, 05, 05, current_date(), dateadd('DAY', -15, current_date()), 7, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (06, 06, 06, current_date(), dateadd('DAY', -15, current_date()), 4, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (07, 07, 07, current_date(), dateadd('DAY', -15, current_date()), 5, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (08, 08, 08, current_date(), dateadd('DAY', -15, current_date()), 3, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (09, 09, 09, current_date(), dateadd('DAY', -15, current_date()), 6, true);
insert into schedulesys_db.test_occurrence (id, test_id, employee_id, completion_date, expiry_date, test_subcategory_id, is_applicable) values (10, 10, 10, current_date(), dateadd('DAY', -15, current_date()), 7, true);


/*==============================================================*/
/* 					table: schedule_update	                    */
/*==============================================================*/

insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (01, 01, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (02, 02, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (03, 03, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (04, 04, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (05, 05, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (06, 06, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (07, 07, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (08, 08, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (09, 09, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (10, 10, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (11, 01, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (12, 02, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (13, 03, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (14, 04, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (15, 05, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (16, 06, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (17, 07, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (18, 08, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (19, 09, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (20, 10, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (21, 01, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (22, 02, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (23, 03, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (24, 04, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (25, 05, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (26, 06, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (27, 07, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (28, 08, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (29, 09, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (30, 10, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (31, 01, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (32, 02, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (33, 03, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (34, 04, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (35, 05, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (36, 06, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (37, 07, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (38, 08, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (39, 09, current_timestamp());
insert into schedulesys_db.schedule_update (schedule_id, user_id, update_date) values (40, 10, current_timestamp());


/*==============================================================*/
/* 					table: service_request	                    */
/*==============================================================*/
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(01, 'zengutsu-01', '5678767601', 'abhinavsudam-01@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-01');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(02, 'zengutsu-02', '5678767602', 'abhinavsudam-02@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-02');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(03, 'zengutsu-03', '5678767603', 'abhinavsudam-03@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-03');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(04, 'zengutsu-04', '5678767604', 'abhinavsudam-04@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-04');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(05, 'zengutsu-05', '5678767605', 'abhinavsudam-05@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-05');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(06, 'zengutsu-06', '5678767606', 'abhinavsudam-06@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-06');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(07, 'zengutsu-07', '5678767607', 'abhinavsudam-07@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-07');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(08, 'zengutsu-08', '5678767608', 'abhinavsudam-08@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-08');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(09, 'zengutsu-09', '5678767609', 'abhinavsudam-09@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-09');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(10, 'zengutsu-10', '5678767610', 'abhinavsudam-10@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-10');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(11, 'zengutsu-11', '5678767611', 'abhinavsudam-11@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-11');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(12, 'zengutsu-12', '5678767612', 'abhinavsudam-12@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-12');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(13, 'zengutsu-13', '5678767613', 'abhinavsudam-13@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-13');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(14, 'zengutsu-14', '5678767614', 'abhinavsudam-14@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-14');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(15, 'zengutsu-15', '5678767615', 'abhinavsudam-15@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-15');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(16, 'zengutsu-16', '5678767616', 'abhinavsudam-16@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-16');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(17, 'zengutsu-17', '5678767617', 'abhinavsudam-17@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-17');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(18, 'zengutsu-18', '5678767618', 'abhinavsudam-18@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-18');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(19, 'zengutsu-19', '5678767619', 'abhinavsudam-19@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-19');
insert into schedulesys_db.service_request (id, name, phone_number, email_address, ccc, lis, bad, aw, lh, mp, laundry, se, mr, lt, ccg, ae, cpp, prlc, start_date, end_date, start_time, end_time, request_date, comment) values(20, 'zengutsu-20', '5678767620', 'abhinavsudam-20@gmail.com', false, false, false, false, false, false, false, false, false, false, false, false, false, false, current_date(), dateadd('DAY', 1, current_date()), current_timestamp(), timestampadd('HOUR', 5, current_timestamp()), current_date(), 'comment-20');
