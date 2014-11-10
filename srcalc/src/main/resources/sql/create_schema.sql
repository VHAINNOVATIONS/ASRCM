-- Creates the SRCalc schema.
--
-- For now, this is HSQLDB. We'll find out what our actual database is later.
create table multi_select_option (id integer not null, value varchar(255), primary key (id))
create table multi_select_variable (display_type integer, id integer not null, primary key (id))
create table multi_select_variable_option (variable_id integer not null, option_id integer not null, option_index integer not null, primary key (multi_select_variable_id, option_index))
create table numerical_variable (max_value integer not null, min_value integer not null, id integer not null, primary key (id))
create table procedure (id integer not null, cpt_code varchar(255), description varchar(255), rvu integer not null, primary key (id))
create table specialty (id integer not null, name varchar(255), vista_id integer not null, primary key (id))
create table specialty_variable (specialty_id integer not null, variable_id integer not null)
create table variable (id integer not null, display_name varchar(255), help_text varchar(255), primary key (id))
alter table multi_select_variable_option add constraint UK_oqixasyjnl58oskaur3k9ocfd unique (multi_select_option_id)
alter table multi_select_variable add constraint FK_18hqfsy87bg9ucro7r0h6hl5t foreign key (id) references variable
alter table multi_select_variable_option add constraint FK_oqixasyjnl58oskaur3k9ocfd foreign key (multi_select_option_id) references multi_select_option
alter table multi_select_variable_option add constraint FK_eegsphy8l2td1odr6vd5w2qlh foreign key (multi_select_variable_id) references multi_select_variable
alter table numerical_variable add constraint FK_ntgiooontcrixh9umumtbf9yh foreign key (id) references variable
alter table specialty_variable add constraint FK_fnk6mxok15rfupep6nmrvcirn foreign key (variable_id) references variable
alter table specialty_variable add constraint FK_1hnl3lgui0b9rp6gwtrdn3n9n foreign key (specialty_id) references specialty
