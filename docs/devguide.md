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

**Running Programs**

The following programs/services will need to be running:

*   MySQL
*   Glassfish
*   Choice of browser (currently IE 8 and up are supported)

See the Installation Guide for instructions on setting up these services. You need not
follow the Installation Guide verbatim: some deviation may be necessary depending on your
workstation setup.

Git will need to be installed on the computer, if it is not already installed.

### Obtaining the Source Code and Dependencies

**Git**

Git can be obtained from git-scm.com. After git is installed, Egit for Eclipse may be installed so that there are multiple interfaces for dealing with repositories. In Eclipse go to `Help->Install New Software`. Click `Add`. Use http://download.eclipse.org/egit/updates/ for the location and an appropriate name.

The source repository currently resides at https://github.com/VHAINNOVATIONS/ASRCM.

**Gradle**

Adding dependencies in Gradle is done by opening the build.gradle file in the source code's directory. There are multiple sections for dependencies in the build.gradle file and the new dependency should go in the appropriate section. The proper dependency path can be found by searching mvnrepository.com and then selecting the `Gradle` tab on the dependency's page. 

**Downloads**

Dependencies listed in Gradle will be automatically downloaded and added to the project. If any new tools or technologies depend on javascript files in order to work, those javascript files should be placed into the `src\main\webapp\js\vendor` folder.


### Compiling and Testing the Application

**Building with Gradle**

The Java web application is located in the top level of the
repository in the `srcalc` folder. This folder contains
the gradlew.bat and build.gradle files that are required for building the
project. To use them, execute the command `gradlew build` in command prompt from
the same directory. After a successful build, a .war file will be built in the
path `srcalc\build\libs`.
	
**Glassfish**

Glassfish Server must be up and running in order to access the application and the administration tools. The administration tools are located at localhost:4848 by default and deployed applications are located at local:8080 by default. To deploy an application, go to the `Applications` task on the left hand toolbar. Click the `Deploy` button and select the appropriate .war file to deploy. If redeploying, find the application you wish to redeploy and click the `Redeploy` link on the right hand side of the application's row. Select the appropriate .war file to deploy and wait for the application to launch after clicking `OK`.

**Testing/Compatibility**

As of right now the target browser versions for ASRC are Internet Explorer 8 and up.

### Enhancing the Application
	
**Coding**

Although the coding does not need to be done in Eclipse, the tool was initially
developed using Eclipse and Eclipse provides standard IDE features such as
graphical debugging and integrated source control via EGit. The Eclipse project
is not stored in this repository, but may be created automatically by running
the command `gradlew eclipse` in the `srcalc` directory.

Committing and pushing code to the repository can be done either through an Eclipse plugin, command line, or other git method. However, pushing the branch to the master branch needs to be approved first. After pushing the branch and fixing any conflicts that may occur, a pull request needs to be created in GitHub so that another developer can review the code.

**Code Standards**

Java code standards are primarily captured via a Checkstyle configuration [in this repository](../srcalc/config/checkstyle/checkstyle.xml). The build automatically performs these checks and warns of violations. Some notable features are:

* Opening and closing braces have their own lines.
* 4-space indents.
* Use spaces instead of tabs.
* All public types (classes and interfaces) must have a doc comment.

This repository includes [Eclipse Code Formatter Preferences](asrc_eclipse_format.xml) specifying the Java code format. Many of the formatting conventions are not checked with Checkstyle, but developers should still follow all the formatting conventions.

Compilation must not generate any errors or warnings. All tests must pass. Verify this with
`gradle clean build` before publishing any changes.

Logging statements are spread throughout the code. To assist in keeping level selection consistent, we use the following rough guidelines (based on [this blog post ](http://www.nurkiewicz.com/2010/05/clean-code-clean-logs-logging-levels.html)):

* `ERROR` - something terribly wrong had happened, that must be investigated immediately. No system can tolerate items logged on this level.

    Example: NPE, database unavailable, mission critical use case cannot be continued.

* `WARN` - the process might be continued, but take extra caution. Actually I always wanted to have two levels here: one for obvious problems where work-around exists (for example: "Current data unavailable, using cached values") and second (name it: ATTENTION) for potential problems and suggestions. 

    Example: "*Application running in development mode*" or "*Administration console is not secured with a password*". The application can tolerate warning messages, but they should always be justified and examined.

* `INFO` - Important business process has finished. In ideal world, administrator or advanced user should be able to understand INFO messages and quickly find out what the application is doing.

    For example if an application is all about booking airplane tickets, there should be only one INFO statement per each ticket saying "*[Who] booked ticket from [Where] to [Where]*". Other definition of INFO message: each action that changes the state of the application significantly (database update, external system request).

* `DEBUG` - Developers stuff. Example:
    
        log.debug("Message with id '{}' processed", message.getJMSMessageID());

* `TRACE` - Very detailed information, intended only for development. You might keep trace messages for a short period of time after deployment on production environment, but treat these log statements as temporary, that should or might be turned-off eventually. The distinction between DEBUG and TRACE is the most difficult, but if you put logging statement and remove it after the feature has been developed and tested, it should probably be on TRACE level.

**Directory Organization**

The `srcalc` folder contains the following directory structure:

* `config`: build configuration, such as the Checkstyle configuration file
* `gradle`: Gradle binaries and supporting files
* `src`: contains all source code, including Java source, resources, test scripts, etc.
  * `src/main/java`: Java application source code
  * `src/main/resources`: non-Java resources included on the application's classpath
  * `src/main/webapp`: non-Java resources included in the Web Application Archive (WAR), such as the `web.xml`
  * `src/test/java`: automated tests written in Java
  * `src/test/resources`: non-Java resources included on the classpath while running the tests
* Building the application will also produce a `build` directory containing build output. Nothing in this
  directory is ever included in the Git repository.

VistA Patch Developer Guide
---------------------------

### Developer Workstation Setup

### Obtaining the Source Code and Dependencies

### Testing the VistA Patch

### Enhancing the VistA Patch

