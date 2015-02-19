Automated Surgical Risk Calculator Developer Guide
==================================================

Introduction
------------

This Developer Guide is intended to assist new Automated Surgical Risk
Calculator developers in compiling and testing the application.

It provides instructions on copying the source code and installing all software
development languages, compilers, utilities, user interfaces, test harnesses,
3rd party libraries, APIs and frameworks.  It includes the directory layout for
the application, organization of source code, and descriptions of the
development processes by providing code and reference documentation for all
functions, methods, and configuration restraints within the software.

The Automated Surgical Risk Calculator application consists of a Java-based Web
Application accompanied by a supporting VistA patch for VistA integration.
Please see the [Project Readme](../README.md) for background on the application.

Web Application Developer Guide
-------------------------------

### Developer Workstation Setup
If you have not yet followed the Installation Guide, ensure that everything is installed and working properly from that guide first before attempting to set up your workstation.

**Running Programs**

The following programs/services will need to be running:
*   MySQL
*   Glassfish
*   Choice of browser (currently IE 7 and up are supported)

Git will need to be installed on the computer, if it is not already installed.

### Obtaining the Source Code and Dependencies

**Git**

Git can be obtained from git-scm.com. After git is installed, Egit for Eclipse may be installed so that there are multiple interfaces for dealing with repositories. In Eclipse go to `Help->Install New Software`. Click `Add`. Use http://download.eclipse.org/egit/updates/ for the location and an appropriate name.

The source repository currently resides at https://github.com/VHAINNOVATIONS/ASRCM.

**Gradle**

Adding dependencies in Gradle is done by opening the build.gradle file in the source code’s directory. There are multiple sections for dependencies in the build.gradle file and the new dependency should go in the appropriate section. The proper dependency path can be found by searching mvnrepository.com and then selecting the `Gradle` tab on the dependency’s page. 

**Downloads**

Dependencies listed in Gradle will be automatically downloaded and added to the project. If any new tools or technologies depend on javascript files in order to work, those javascript files should be placed into the `src\main\webapp\js\vendor` folder.
	TODO: Are there any other dependencies/codes that need to be downloaded or might be needed in the future?

**Filling Procedures in DB**

If the Installation Guide was followed properly, the srcalc database should be created properly. For updating changes to the database: 

1.  The proper files need to be in the `install/resources` folder starting at the top level of the repository. Currently procedures and models can be found on the ASRC sharepoint in the `Deliverables->Internal VA Files` folder. 
2.  Drop the srcalc database. This can be done through MySQL Workbench to make the process easier.
3.  Run `create_database.bat` in the `install` folder.

### Compiling and Testing the Application

**Building with Gradle**

The actual Eclipse project for the ASRC is located in the top level of the repository at the `srcalc` folder. The top level of the project folder contains the gradlew.bat and build.gradle files that are required for building the project. To use them, execute the command `gradlew build` in command prompt from the same directory. After a successful build, a .war file will be built in the path `srcalc\build\libs`. If there are many build errors such as problems with the build path, use the command `gradlew eclipse` to build an Eclipse project file.
	
**Glassfish**

Glassfish Server must be up and running in order to access the application and the administration tools. The administration tools are located at localhost:4848 by default and deployed applications are located at local:8080 by default. To deploy an application, go to the `Applications` task on the left hand toolbar. Click the `Deploy` button and select the appropriate .war file to deploy. If redeploying, find the application you wish to redeploy and click the `Redeploy` link on the right hand side of the application’s row. Select the appropriate .war file to deploy and wait for the application to launch after clicking `OK`.

**Testing/Compatibility**

As of right now the target browser versions for ASRC are Internet Explorer 9 and up.

### Enhancing the Application
	
**Coding**

Although the coding does not need to be done in Eclipse, the project is kept inside of an Eclipse project and that is the preferred method of developing. It also makes committing code and branching easier since the EGit plugin can be used.

Committing and pushing code to the repository can be done either through an Eclipse plugin, command line, or other git method. However, pushing the branch to the master branch needs to be approved first. After pushing the branch and fixing any conflicts that may occur, a pull request needs to be created in GitHub so that another developer can review the code.

**Directory Organization**



VistA Patch Developer Guide
---------------------------

### Developer Workstation Setup

### Obtaining the Source Code and Dependencies

### Testing the VistA Patch

### Enhancing the VistA Patch

