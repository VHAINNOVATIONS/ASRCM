-- This script inserts dummy risk model configuration into the database for testing.

-- SPECIALTY table
INSERT INTO SPECIALTY (id, name) VALUES (1, 'General Surgery');
INSERT INTO SPECIALTY (id, name) VALUES (2, 'Neurosurgery');
INSERT INTO SPECIALTY (id, name) VALUES (3, 'Orthopedic');
INSERT INTO SPECIALTY (id, name) VALUES (4, 'Thoracic');
INSERT INTO SPECIALTY (id, name) VALUES (5, 'Urology');
INSERT INTO SPECIALTY (id, name) VALUES (6, 'Vascular');
INSERT INTO SPECIALTY (id, name) VALUES (7, 'Cardiac');

-- RISK_MODEL table
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (1, 'General Surgery 30-Day Mortality Risk', -1.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (2, 'Neurosurgery 30-Day Mortality Risk', -2.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (3, 'Orthopedic 30-Day Mortality Risk', -3.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (4, 'Thoracic 30-Day Mortality Risk', -4.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (5, 'Urology 30-Day Mortality Risk', -5.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (6, 'Vascular 30-Day Mortality Risk', -6.0);
INSERT INTO RISK_MODEL (id, display_name, constant) VALUES (7, 'Cardiac 30-Day Mortality Risk', -7.0);

-- SPECIALTY_RISK_MODEL table
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (1, 1);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (2, 2);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (3, 3);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (4, 4);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (5, 5);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (6, 6);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (7, 7);

-- VARIABLE_GROUP table
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (1, 'Planned Procedure', 0);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (2, 'Demographics', 1);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (3, 'BMI', 2);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (4, 'Medications', 3);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (5, 'Laboratory Values', 4);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (6, 'Clinical Conditions or Diseases - Recent', 5);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (7, 'Clinical Conditions or Diseases - History of', 6);

-- *** Variables ***

-- Cardiac Gender
INSERT INTO VARIABLE (id, display_name, variable_key, retrieval_key, variable_group, help_text) VALUES (1, 'Gender', 'gender', 0, 2, 'The Patient Gender');
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (1, 'Radio');
-- Intentionally reverse Female and Male to verify display order.
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (1, 'Female', 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (1, 'Male', 0);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (7, 1, 1, 48.0);

-- Procedure
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group, help_text) VALUES (3, 'Procedure', 'procedure', 1, 'the procedure to perform');
INSERT INTO PROCEDURE_VARIABLE (id) VALUES (3);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (1, 3, .31);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (2, 3, .32);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (3, 3, .33);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (4, 3, .34);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (5, 3, .35);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (6, 3, .36);

-- Age
INSERT INTO VARIABLE (id, display_name, variable_key, retrieval_key, variable_group, help_text) VALUES (2, 'Age', 'age', 1, 2, 'The Patient Age');
-- There is not really an upper limit on age, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (2, 18, TRUE, 120, TRUE, 'years');
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (1, 2, 0.21);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (2, 2, 0.22);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (3, 2, 0.23);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (4, 2, 0.24);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (5, 2, 0.25);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (6, 2, 0.26);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (7, 2, 0.27);

-- DNR
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group, help_text) VALUES (6, 'DNR', 'dnr', 2, 'Do Not Resuscitate');
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (6);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (1, 6, .61);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (2, 6, .62);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (3, 6, .63);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (4, 6, .64);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (5, 6, .65);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (6, 6, .66);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (7, 6, .67);

-- BMI
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group, help_text) VALUES (5, 'BMI', 'bmi', 3, 'Body Mass Index, deduced by visually examining the patient');
-- There is not really an upper limit on BMI, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (5, 0, FALSE, 499, TRUE, '');
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (1, 5, .51);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (2, 5, .52);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (3, 5, .53);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (4, 5, .54);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (5, 5, .55);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (6, 5, .56);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (7, 5, .57);

-- ASA Classification 1/2, 3, 4, 5
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (8, 'ASA Classification', 'asaClassification', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (8, 'Radio');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 1 or 2', 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 3', 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 4', 2);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 5', 3);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 8, 3, .81);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 8, 3, .84);


-- Preop Pneumonia
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (7, 'Preop Pneumonia', 'preopPneumonia', 6);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (7);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (1, 7, .71);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (2, 7, .72);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (4, 7, .74);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (6, 7, .76);

-- Alkaline Phosphatase Lab
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (9, 'Alkaline Phosphatase', 'alkalinePhosphatase', 5);
INSERT INTO DISCRETE_NUMERICAL_VAR (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (9, 10, TRUE, 750, TRUE, 'mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound_string, upper_inclusive) VALUES (9, 'WNL', '125.0', TRUE);
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound_string, upper_inclusive) VALUES (9, '>125mU/ml', '1e12', TRUE);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 9, 1, .91);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 9, 1, .94);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (5, 9, 1, .95);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (6, 9, 1, .96);

-- BUN Lab
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (10, 'BUN', 'bun', 5);
INSERT INTO DISCRETE_NUMERICAL_VAR (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (10, 2, TRUE, 90, TRUE, 'mg/dl');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound_string, upper_inclusive) VALUES (10, 'WNL', '25', TRUE);
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound_string, upper_inclusive) VALUES (10, '>25mg/dl', '1e12', TRUE);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 10, 1, 1.01);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (2, 10, 1, 1.02);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 10, 1, 1.04);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (5, 10, 1, 1.05);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (6, 10, 1, 1.06);

-- Shared Functional Status
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (11, 'Functional Status', 'functionalStatus', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (11, 'Radio');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (11, 'Independent', 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (11, 'Partially dependent', 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (11, 'Totally dependent', 2);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 11, 1, 1);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (2, 11, 2, 2);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (3, 11, 1, 3);

-- Age and functional status rule
INSERT INTO RULE (id, display_name, bypass_enabled, summand_expression) VALUES (1, 'Age multiplier for functional status', FALSE, '#coefficient * #age');
-- Functional Status matcher
INSERT INTO RULE_VALUE_MATCHER (rule_id, boolean_expression, expression_enabled, variable) VALUES (1, '#functionalStatus == "Totally dependent"', TRUE, 11);
-- Age matcher
INSERT INTO RULE_VALUE_MATCHER (rule_id, boolean_expression, expression_enabled, variable) VALUES (1, '', FALSE, 2);
INSERT INTO RISK_MODEL_DERIVED_TERM (risk_model_id, rule, coefficient) VALUES (1, 1, 1.5);

-- Dummy set of procedures because CPT Codes are copyrighted.
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 1, '26545', 'Repair right hand - you know, the thing with fingers', 5.05, 'Repair right hand', 'Standard', 1);
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 2, '26546', 'Repair left hand - you know, the thing with fingers', 10.06, 'Repair left hand', 'Standard', 1);
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 3, '10001', 'Repair right pinky phalanx', 2.45, 'Repair right finger', 'Complex', 0);
