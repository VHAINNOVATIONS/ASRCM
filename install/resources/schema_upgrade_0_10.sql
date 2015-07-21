-- Upgrades the DB schema from v0.9 to v0.10.

alter table specialty_risk_model drop index UK_g44r1aagpmd130bpvefwj08ve;

create table historical_calc (id integer not null auto_increment, seconds_to_first_run integer not null, specialty_name varchar(100) not null, start_timestamp datetime not null, user_station varchar(10) not null, primary key (id));
create table historical_calc_person_class (calc_id integer not null, provider_type varchar(80) not null, primary key (calc_id, provider_type));
alter table historical_calc_person_class add index FK_3o264tgux2bfssok1welbxabj (calc_id), add constraint FK_3o264tgux2bfssok1welbxabj foreign key (calc_id) references historical_calc (id);

create table signed_result (run_id integer not null, cpt_code varchar(5), patient_dfn integer not null, signature_timestamp datetime not null, primary key (run_id));
create table signed_result_input (result_id integer not null, variable_value varchar(255) not null, variable_key varchar(40) not null, primary key (result_id, variable_key));
create table signed_result_outcome (result_id integer not null, risk_result float not null, model_name varchar(80) not null, primary key (result_id, model_name));
alter table signed_result add index FK_fbh9rjcin6qo4vi5cyhvqneok (run_id), add constraint FK_fbh9rjcin6qo4vi5cyhvqneok foreign key (run_id) references historical_calc (id);
alter table signed_result_input add index FK_a7w1dts524sqt755yf1q8igvv (result_id), add constraint FK_a7w1dts524sqt755yf1q8igvv foreign key (result_id) references signed_result (run_id);
alter table signed_result_outcome add index FK_jbofpapc0i5qoixu28f4ekjm (result_id), add constraint FK_jbofpapc0i5qoixu28f4ekjm foreign key (result_id) references signed_result (run_id);
