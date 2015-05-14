@echo off

SETLOCAL ENABLEEXTENSIONS

IF NOT DEFINED JAVA_HOME (
    ECHO JAVA_HOME must be defined.
    goto End
)

REM Load configuration settings.
call config.bat

START /WAIT mysql-installer-web-community-5.6.21.1.msi /quiet

"C:\Program Files (x86)\MySQL\MySQL Installer for Windows\MySQLInstallerConsole.exe" install -silent server;5.6.21;X64:*:serverid=0 Connector/J;5.1.33;X64

echo Creating 'srcalc' database user...
echo CREATE USER 'srcalc'@'localhost' IDENTIFIED BY '%SRCALC_PASSWORD%'; | %MYSQL% -u root
IF ERRORLEVEL 1 goto Abort

START /WAIT glassfish-3.1.2.2-web-windows.exe -j "%JAVA_HOME%" -a glassfish3_asrc.cfg -s

REM Change directory to the Glassfish install directory.
C:
cd \asrc\glassfish3\glassfish

REM Copy MySQL Connector/J library to Glassfish.
copy "C:\Program Files (x86)\MySQL\Connector.J 5.1\mysql-connector-java-5.1.33-bin.jar" .\domains\domain1\lib

REM Create a Windows Service to auto-start Glassfish.
bin\asadmin create-service

REM Start the Windows service.
sc start domain1

REM Configure Glassfish logging
REM Set VistALink to WARNING because INFO is far too verbose.
call %ASADMIN% set-log-levels gov.va.med.vistalink=WARNING
REM Set srcalc explicitly to INFO to facilitate changing it later.
call %ASADMIN% set-log-levels gov.va.med.srcalc=INFO
call %ASADMIN% set-log-attributes com.sun.enterprise.server.logging.GFFileHandler.maxHistoryFiles=10
REM Also set the Time Zone to Eastern. (Kludge until ASRC-281 is implemented.)
call %ASADMIN% create-jvm-options -Duser.timezone=America/New_York
REM Glassfish must be resarted after this change
net stop domain1
REM Wait a couple seconds to make sure Glassfish has completely stopped.
timeout /T 2 /NOBREAK
sc start domain1

REM Create the srcalc database.
call create_database.bat

echo Creating Connection Pool in Glassfish...
REM Must use "call" because asadmin is a batch file.
call %ASADMIN% create-jdbc-connection-pool --datasourceclassname com.mysql.jdbc.jdbc2.optional.MysqlDataSource --restype javax.sql.DataSource --validationtable=DUAL --isconnectvalidatereq=true --property password=%SRCALC_PASSWORD%:user=srcalc:url=jdbc\:mysql\://localhost\:3306/srcalc srcalcDbPool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% ping-connection-pool srcalcDbPool
IF ERRORLEVEL 1 goto Abort
call %ASADMIN% create-jdbc-resource --connectionpoolid srcalcDbPool jdbc/srcalcDB
IF ERRORLEVEL 1 goto Abort

call setup_vistalink.bat

REM We're ready. Deploy the application!
call deploy.bat

:End
