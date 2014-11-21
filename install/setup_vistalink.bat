@echo off

REM Sets up the VistALink connector in Glassfish.

SETLOCAL ENABLEEXTENSIONS

REM Load configuration settings.
call config.bat

REM Note: the 'root' user should have auto-login ability

echo Copying the VistALink configuration file...
copy resources\gov.va.med.vistalink.connectorConfig.xml "%GF_DOMAIN_DIR%\lib\classes\"
IF ERRORLEVEL 1 goto Abort

echo Deploying VistALink connector...
REM Must use "call" because asadmin is a batch file.
call %ASADMIN% deploy resources\vlj16.rar
IF ERRORLEVEL 1 goto Abort

echo Creating VistALink Connection Pool in Glassfish...
call %ASADMIN% create-connector-connection-pool --raname vlj16 --connectiondefinition javax.resource.cci.ConnectionFactory --property connectorJndiName=vlj/Asrc500 --description "VistALink connector to Asrc500 VistA" vljAsrc500Pool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% ping-connection-pool vljAsrc500Pool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% create-connector-resource --poolname vljAsrc500Pool vlj/Asrc500
IF ERRORLEVEL 1 goto Abort

goto End

:Abort
echo Aborting script due to error.

:End

