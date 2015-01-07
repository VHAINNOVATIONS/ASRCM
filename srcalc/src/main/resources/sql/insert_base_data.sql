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
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (1, 'Gender', 2);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (1, 'Radio');
-- Intentionally reverse Female and Male to verify display order.
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (1, 'Female');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (2, 'Male');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (1, 2, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (1, 1, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 1, 1);

-- Non-Cardiac Procedure
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (3, 'Procedure', 1);
INSERT INTO PROCEDURE_VARIABLE (id) VALUES (3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 3, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 3, 0);

-- Shared Age
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (2, 'Age', 2);
-- There is not really an upper limit on age, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (2, 0, 999, 'years');
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 2, 0);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 2, 1);

-- Shared DNR
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (6, 'DNR', 2);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (6);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 6, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 6, 2);

-- Shared Functional Status
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (4, 'Functional Status', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (4, 'Radio');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (3, 'Independent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (4, 'Partially dependent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (5, 'Totally dependent');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 3, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 4, 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 4, 5);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 4, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 4, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 4, 6);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 4, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 4, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 4, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 4, 4);

-- Shared BMI
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (5, 'BMI', 3);
-- There is not really an upper limit on BMI, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (5, 0, 499, '');
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 5, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 5, 3);

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
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 8, 4);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 8, 5);


-- Preop Pneumonia
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (7, 'Preop Pneumonia', 6);
INSERT INTO BOOLEAN_VARIABLE (id) VALUES (7);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 7, 6);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 7, 5);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 7, 7);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 7, 5);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 7, 5);

-- Alkaline Phosphatase Lab
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (9, 'Alkaline Phosphatase', 5);
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value, units) VALUES (9, 10, 750, 'mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR (id) VALUES (9);
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (10, 'WNL');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_id, lower_bound, lower_inclusive, upper_bound, upper_inclusive) VALUES (9, 10, -1e12, TRUE, 125.0, TRUE);
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (11, '>125mU/ml');
INSERT INTO DISCRETE_NUMERICAL_VAR_CATEGORY (variable_id, option_id, lower_bound, lower_inclusive, upper_bound, upper_inclusive) VALUES (9, 11, 125.0, FALSE, 1e12, TRUE);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 9, 4);

