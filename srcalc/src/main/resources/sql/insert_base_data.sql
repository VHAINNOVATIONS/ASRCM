-- This script inserts the base static data for srcalc.

-- SPECIALTY table
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (1, 50, 'General Surgery');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (2, 52, 'Neurosurgery');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (3, 54, 'Orthopedic');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (4, 58, 'Thoracic');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (5, 59, 'Urology');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (6, 62, 'Vascular');
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (7, 48, 'Cardiac');
-- TODO: validate assumption that other -> General Surgery
INSERT INTO SPECIALTY (id, vista_id, name) VALUES (8, 50, 'Other Non-Cardiac Specialty');

-- RISK_MODEL table
INSERT INTO RISK_MODEL (id, display_name) VALUES (1, 'General Surgery 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (2, 'Neurosurgery 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (3, 'Orthopedic 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (4, 'Thoracic 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (5, 'Urology 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (6, 'Vascular 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (7, 'Cardiac 30-Day Mortality Risk (FY2013)');
INSERT INTO RISK_MODEL (id, display_name) VALUES (8, 'Other Non-Cardiac Specialty 30-Day Mortality Risk (FY2013)');

-- SPECIALTY_RISK_MODEL table
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (1, 1);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (2, 2);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (3, 3);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (4, 4);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (5, 5);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (6, 6);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (7, 7);
INSERT INTO SPECIALTY_RISK_MODEL (specialty_id, risk_model_id) VALUES (8, 8);

-- VARIABLE_GROUP table
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (1, 'Planned Procedure', 0);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (2, 'Demographics', 1);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (3, 'BMI', 2);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (4, 'Medications', 3);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (5, 'Laboratory Values', 4);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (6, 'Clinical Conditions or Diseases - Recent', 5);
INSERT INTO VARIABLE_GROUP (id, name, display_order) VALUES (7, 'Clinical Conditions or Diseases - History of', 6);

-- *** Variables ***

-- Note: all the '48.0' coefficient values are placeholders until we determine
-- whether we can publish the FY2013 models to GitHub.

-- Cardiac Gender
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (1, 'Gender', 2);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (1, 'Radio');
-- Intentionally reverse Female and Male to verify display order.
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (1, 'Female');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (2, 'Male');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (1, 2, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (1, 1, 1);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (1, 1, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (7, 1);

-- Non-Cardiac Procedure
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (3, 'Procedure', 1);
INSERT INTO PROCEDURE_VARIABLE (id) VALUES (3);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (2, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 2);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (3, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 3);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (4, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (3, 4);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (5, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 5);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (6, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 6);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (7, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 7);
INSERT INTO PROCEDURE_TERM (id, variable, coefficient) VALUES (8, 3, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 8);

-- Shared Age
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (2, 'Age', 2);
-- There is not really an upper limit on age, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (2, 0, 999, 'years');
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (9, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 9);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (10, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 10);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (11, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (3, 11);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (12, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 12);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (13, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 13);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (14, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 14);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (15, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (7, 15);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (16, 2, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 16);

-- Shared DNR
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (6, 'DNR', 2);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (6);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (17, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 17);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (18, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 18);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (19, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (3, 19);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (20, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 20);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (21, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 21);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (22, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 22);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (23, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (7, 23);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (24, 6, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 24);

-- Shared Functional Status
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (4, 'Functional Status', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (4, 'Radio');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (3, 'Independent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (4, 'Partially dependent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (5, 'Totally dependent');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 3, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 4, 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 5, 2);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (25, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 25);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (26, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 26);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (27, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (3, 27);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (28, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 28);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (29, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 29);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (30, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 30);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (31, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (7, 31);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (32, 4, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 32);

-- Shared BMI
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (5, 'BMI', 3);
-- There is not really an upper limit on BMI, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (5, 0, 499, '');
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (33, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 33);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (34, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 34);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (35, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (3, 35);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (36, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 36);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (37, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 37);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (38, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 38);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (39, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (7, 39);
INSERT INTO NUMERICAL_TERM (id, variable, coefficient) VALUES (40, 5, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 40);

-- ASA Classification 1/2, 3, 4/5
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (8, 'ASA Classification', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (8, 'Radio');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (6, 'Class 1 or 2');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (7, 'Class 3');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (8, 'Class 4');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (9, 'Class 5');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (8, 6, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (8, 7, 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (8, 8, 2);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (8, 9, 3);
-- This particular vairable is only used for General and Thoracic.
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (41, 8, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 41);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (42, 8, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 42);


-- Preop Pneumonia
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (7, 'Preop Pneumonia', 6);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (7);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (43, 7, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 43);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (44, 7, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (2, 44);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (45, 7, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 45);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (46, 7, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 46);
INSERT INTO BOOLEAN_TERM (id, variable, coefficient) VALUES (47, 7, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 47);

-- Alkaline Phosphatase Lab
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (9, 'Alkaline Phosphatase', 5);
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (9, 10, 750, 'mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR (id) VALUES (9);
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (10, 'WNL');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_id, lower_bound, lower_inclusive, upper_bound, upper_inclusive) VALUES (9, 10, -1e12, TRUE, 125.0, TRUE);
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (11, '>125mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_id, lower_bound, lower_inclusive, upper_bound, upper_inclusive) VALUES (9, 11, 125.0, FALSE, 1e12, TRUE);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (48, 9, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (1, 48);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (49, 9, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (4, 49);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (50, 9, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (5, 50);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (51, 9, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (6, 51);
INSERT INTO DISCRETE_TERM (id, variable, option_index, coefficient) VALUES (52, 9, 1, 48.0);
INSERT INTO RISK_MODEL_TERM (risk_model_id, model_term_id) VALUES (8, 52);
