@echo off

REM Creates the srcalc database and populates it with some base data.

SETLOCAL ENABLEEXTENSIONS

REM Load configuration settings.
call config.bat

REM Note: the 'root' user should have auto-login ability

echo Creating 'srcalc' database...
REM go into resources directory for reference to procedures_2013.csv
pushd resources
%MYSQL% -u root < create_database.sql
popd
IF ERRORLEVEL 1 goto Abort

echo Populating database with base data set...
%MYSQL% -u root srcalc < ..\srcalc\src\main\resources\sql\insert_base_data.sql
IF ERRORLEVEL 1 goto Abort

goto End

:Abort
echo Aborting script due to error.

:End

