/* Creates the srcalc database on MySQL. */

CREATE DATABASE srcalc;
USE srcalc;

create table cpt (id integer not null, cpt_code varchar(255), description varchar(255), rvu integer not null, primary key (id));
create table multi_select_option (id integer not null, option_value varchar(255), primary key (id));
create table multi_select_variable (display_type varchar(255), id integer not null, primary key (id));
create table multi_select_variable_option (variable_id integer not null, option_id integer not null, option_index integer not null, primary key (variable_id, option_index));
create table numerical_variable (max_value integer not null, min_value integer not null, id integer not null, primary key (id));
create table specialty (id integer not null, name varchar(255), vista_id integer not null, primary key (id));
create table specialty_variable (specialty_id integer not null, variable_id integer not null);
create table variable (id integer not null, display_name varchar(255), help_text varchar(255), primary key (id));
alter table multi_select_variable_option add constraint UK_ru3a3572ftqkwimf3nkrnuc5a unique (option_id);
alter table multi_select_variable add index FK_18hqfsy87bg9ucro7r0h6hl5t (id), add constraint FK_18hqfsy87bg9ucro7r0h6hl5t foreign key (id) references variable (id);
alter table multi_select_variable_option add index FK_ru3a3572ftqkwimf3nkrnuc5a (option_id), add constraint FK_ru3a3572ftqkwimf3nkrnuc5a foreign key (option_id) references multi_select_option (id);
alter table multi_select_variable_option add index FK_aho8l3stxs2pix74vg19xmman (variable_id), add constraint FK_aho8l3stxs2pix74vg19xmman foreign key (variable_id) references multi_select_variable (id);
alter table numerical_variable add index FK_ntgiooontcrixh9umumtbf9yh (id), add constraint FK_ntgiooontcrixh9umumtbf9yh foreign key (id) references variable (id);
alter table specialty_variable add index FK_fnk6mxok15rfupep6nmrvcirn (variable_id), add constraint FK_fnk6mxok15rfupep6nmrvcirn foreign key (variable_id) references variable (id);
alter table specialty_variable add index FK_1hnl3lgui0b9rp6gwtrdn3n9n (specialty_id), add constraint FK_1hnl3lgui0b9rp6gwtrdn3n9n foreign key (specialty_id) references specialty (id);

GRANT ALL PRIVILEGES ON srcalc.* TO 'srcalc'@'localhost';
