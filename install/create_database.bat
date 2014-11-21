@echo off

REM Creates the srcalc database and Glassfish connectors to the database.

SETLOCAL ENABLEEXTENSIONS

REM Load configuration settings.
call config.bat

REM Note: the 'root' user should have auto-login ability

echo Creating 'srcalc' user...
echo CREATE USER 'srcalc'@'localhost' IDENTIFIED BY '%SRCALC_PASSWORD%'; | %MYSQL% -u root
IF ERRORLEVEL 1 goto Abort

echo Creating 'srcalc' database...
REM go into resources directory for reference to procedures_2013.csv
pushd resources
%MYSQL% -u root < create_database.sql
popd
IF ERRORLEVEL 1 goto Abort

echo Populating database with base data set...
%MYSQL% -u root srcalc < ..\srcalc\src\main\resources\sql\insert_base_data.sql
IF ERRORLEVEL 1 goto Abort

echo Creating Connection Pool in Glassfish...
REM Must use "call" because asadmin is a batch file.
call %ASADMIN% create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --validationtable=DUAL --isconnectvalidatereq=true --property password=%SRCALC_PASSWORD%:user=srcalc:url=jdbc\:mysql\://localhost\:3306/srcalc srcalcDbPool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% ping-connection-pool srcalcDbPool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% create-jdbc-resource --connectionpoolid srcalcDbPool jdbc/srcalcDB
IF ERRORLEVEL 1 goto Abort

goto End

:Abort
echo Aborting script due to error.

:End

