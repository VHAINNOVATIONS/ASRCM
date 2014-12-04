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
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 1, 0);

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
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value) VALUES (2, 0, 999);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 2, 1);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 2, 1);

-- Shared Functional Status
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (4, 'Functional Status', 6);
INSERT INTO MULTI_SELECT_VARIABLE (id, display_type) VALUES (4, 'Radio');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (3, 'Independent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (4, 'Partially dependent');
INSERT INTO MULTI_SELECT_OPTION (id, option_value) VALUES (5, 'Totally dependent');
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 3, 0);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 4, 1);
INSERT INTO MULTI_SELECT_VARIABLE_OPTION (variable_id, option_id, option_index) VALUES (4, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 4, 3);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 4, 3);

-- Shared BMI
INSERT INTO VARIABLE (id, display_name, variable_group) VALUES (5, 'BMI', 3);
-- There is not really an upper limit on BMI, but specify an unrealistically high
-- one to have some idea of significant digits.
INSERT INTO NUMERICAL_VARIABLE (id, min_value, max_value) VALUES (5, 0, 499);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (1, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (2, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (3, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (4, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (5, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (6, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (7, 5, 2);
INSERT INTO SPECIALTY_VARIABLE (specialty_id, variable_id, display_order) VALUES (8, 5, 2);
