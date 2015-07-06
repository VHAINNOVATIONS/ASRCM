-- This script inserts dummy risk model configuration into the database for testing.

-- SPECIALTY table
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (1, 50, 'General Surgery');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (2, 52, 'Neurosurgery');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (3, 54, 'Orthopedic');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (4, 58, 'Thoracic');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (5, 59, 'Urology');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (6, 62, 'Vascular');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (7, 48, 'Cardiac');

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
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (1, 3, 3.1);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (2, 3, 3.2);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (3, 3, 3.3);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (4, 3, 3.4);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (5, 3, 3.5);
INSERT INTO RISK_MODEL_PROCEDURE_TERM (risk_model_id, variable, coefficient) VALUES (6, 3, 3.6);

-- Age
INSERT INTO VARIABLE (id, display_name, variable_key, retrieval_key, variable_group, help_text) VALUES (2, 'Age', 'age', 1, 2, 'The Patient Age');
-- There is not really an upper limit on age, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (2, 18, TRUE, 120, TRUE, 'years');
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (1, 2, 2.1);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (2, 2, 2.2);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (3, 2, 2.3);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (4, 2, 2.4);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (5, 2, 2.5);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (6, 2, 2.6);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (7, 2, 2.7);

-- DNR
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group, help_text) VALUES (6, 'DNR', 'dnr', 2, 'Do Not Resuscitate');
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (6);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (1, 6, 6.1);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (2, 6, 6.2);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (3, 6, 6.3);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (4, 6, 6.4);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (5, 6, 6.5);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (6, 6, 6.6);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (7, 6, 6.7);

-- BMI
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group, help_text) VALUES (5, 'BMI', 'bmi', 3, 'Body Mass Index, deduced by visually examining the patient');
-- There is not really an upper limit on BMI, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (5, 0, FALSE, 499, TRUE, '');
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (1, 5, 5.1);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (2, 5, 5.2);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (3, 5, 5.3);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (4, 5, 5.4);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (5, 5, 5.5);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (6, 5, 5.6);
INSERT INTO RISK_MODEL_NUMERICAL_TERM (risk_model_id, variable, coefficient) VALUES (7, 5, 5.7);

-- ASA Classification 1/2, 3, 4, 5
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (8, 'ASA Classification', 'asaClassification', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (8, 'Radio');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 1 or 2', 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 3', 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 4', 2);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_value, option_index) VALUES (8, 'Class 5', 3);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 8, 3, 8.1);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 8, 3, 8.4);


-- Preop Pneumonia
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (7, 'Preop Pneumonia', 'preopPneumonia', 6);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (7);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (1, 7, 7.1);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (2, 7, 7.2);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (4, 7, 7.4);
INSERT INTO RISK_MODEL_BOOLEAN_TERM (risk_model_id, variable, coefficient) VALUES (6, 7, 7.6);

-- Alkaline Phosphatase Lab
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (9, 'Alkaline Phosphatase', 'alkalinePhosphatase', 5);
INSERT INTO DISCRETE_NUMERICAL_VAR (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (9, 10, TRUE, 750, TRUE, 'mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound, upper_inclusive) VALUES (9, 'WNL', 125.0, TRUE);
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound, upper_inclusive) VALUES (9, '>125mU/ml', 1e12, TRUE);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 9, 1, 9.1);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 9, 1, 9.4);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (5, 9, 1, 9.5);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (6, 9, 1, 9.6);

-- BUN Lab
INSERT INTO VARIABLE (id, display_name, variable_key, variable_group) VALUES (10, 'BUN', 'bun', 5);
INSERT INTO DISCRETE_NUMERICAL_VAR (id, lower_bound, lower_inclusive, upper_bound, upper_inclusive, units) VALUES (10, 2, TRUE, 90, TRUE, 'mg/dl');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound, upper_inclusive) VALUES (10, 'WNL', 25, TRUE);
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_value, upper_bound, upper_inclusive) VALUES (10, '>25mg/dl', 1e12, TRUE);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (1, 10, 1, 10.1);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (2, 10, 1, 10.2);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (4, 10, 1, 10.4);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (5, 10, 1, 10.5);
INSERT INTO RISK_MODEL_DISCRETE_TERM (risk_model_id, variable, option_index, coefficient) VALUES (6, 10, 1, 10.6);

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
INSERT INTO RULE (id, display_name, required, summand_expression) VALUES (1, 'Age multiplier for functional status', TRUE, '#coefficient * #age');
-- Functional Status matcher
INSERT INTO RULE_VALUE_MATCHER (rule_id, boolean_expression, enabled, variable) VALUES (1, '#functionalStatus == "Totally dependent"', TRUE, 11);
-- Age matcher
INSERT INTO RULE_VALUE_MATCHER (rule_id, boolean_expression, enabled, variable) VALUES (1, '', FALSE, 2);
INSERT INTO RISK_MODEL_DERIVED_TERM (risk_model_id, rule, coefficient) VALUES (1, 1, 1.5);

-- Dummy set of procedures because CPT Codes are copyrighted.
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 1, '26545', 'Repair right hand - you know, the thing with fingers', 5.05, 'Repair right hand', 'Standard', 1);
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 2, '26546', 'Repair left hand - you know, the thing with fingers', 10.06, 'Repair left hand', 'Standard', 1);
INSERT INTO cpt (id, cpt_code, long_description, rvu, short_description, complexity, eligible) VALUES ( 3, '10001', 'Repair right pinky phalanx', 2.45, 'Repair right finger', 'Complex', 0);
