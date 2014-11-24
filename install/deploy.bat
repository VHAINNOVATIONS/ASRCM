@echo off

REM Sets up the VistALink connector in Glassfish.

SETLOCAL ENABLEEXTENSIONS

REM Load configuration settings.
call config.bat

echo Deploying the srcalc application...
REM Must use "call" because asadmin is a batch file.
call %ASADMIN% deploy --force=true srcalc.war
IF ERRORLEVEL 1 goto Abort

goto End

:Abort
echo Aborting script due to error.

:End

