ALTER TABLE discrete_numerical_var_category DROP FOREIGN KEY FK_ai58j7ktbfoadjqgqm0llm11e;
ALTER TABLE discrete_numerical_var_category DROP INDEX FK_ai58j7ktbfoadjqgqm0llm11e;
ALTER TABLE discrete_numerical_var_category DROP PRIMARY KEY;
ALTER TABLE discrete_numerical_var_category ADD upper_bound_string varchar(255);
UPDATE discrete_numerical_var_category SET upper_bound_string = cast(upper_bound as char(255));
alter table discrete_numerical_var_category add primary key(variable_id, option_value, upper_bound_string, upper_inclusive);
alter table discrete_numerical_var_category add index FK_ (variable_id), add constraint FK_ai58j7ktbfoadjqgqm0llm11e foreign key (variable_id) references discrete_numerical_var (id);
alter table discrete_numerical_var_category drop upper_bound;