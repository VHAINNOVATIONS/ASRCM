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

#### IDE

Although the coding does not need to be done in Eclipse, the tool was initially
developed using Eclipse and Eclipse provides standard IDE features such as
graphical debugging and integrated source control via EGit. The Eclipse project
is not stored in this repository, but may be created automatically by running
the command `gradlew eclipse` in the `srcalc` directory.

#### Directory Organization

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

#### Publishing Code

Committing and pushing code to the repository can be done either through an Eclipse plugin, command line, or other git method. However, pushing the branch to the master branch needs to be approved first. After pushing the branch and fixing any conflicts that may occur, a pull request needs to be created in GitHub so that another developer can review the code.

#### Code Standards

Java code standards are primarily captured via a Checkstyle configuration [in this repository](../srcalc/config/checkstyle/checkstyle.xml). The build automatically performs these checks and warns of violations. Some notable features are:

* Opening and closing braces have their own lines.
* 4-space indents.
* Use spaces instead of tabs.
* All public types (classes and interfaces) and methods must have a [doc comment](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html). (The customer requested this standard.)
* Instance variables are prefixed with "f" (for "field").

This repository includes [Eclipse Code Formatter Preferences](asrc_eclipse_format.xml) specifying the Java code format. Many of the formatting conventions are not checked with Checkstyle, but developers should still follow all the formatting conventions.

Additional code standards which Checkstyle does not capture are:

* Compilation must not generate any errors or warnings. All tests must pass. Verify this with
`gradle clean build javadoc` before publishing any changes.
  * Known issue: Gradle does not properly generate Javadocs and records spurious warnings for package links. See [this bug report](https://discuss.gradle.org/t/javadoc-task-doesnt-generate-package-links/10621). These warnings may be ignored.
* The `master` branch does not contain commented-out code.
* The `master` branch does not contain incomplete code.
* Automated tests provide as close to 100% code coverage as practical, with 90% coverage as a minimum. (Use `gradle jacocoTestReport` to generate a code coverage report.)

Although the above standards are important, any part of the standard may be waived with good justification.

#### Logging Levels

Every log statement has an associated level. In order to keep level selection consistent throughout the application, we use the following rough guidelines (based on [this blog post ](http://www.nurkiewicz.com/2010/05/clean-code-clean-logs-logging-levels.html)):

* `ERROR` - something terribly wrong had happened, that must be investigated immediately. No system can tolerate items logged on this level.

    Example: NPE, database unavailable, mission critical use case cannot be continued.

* `WARN` - the process might be continued, but take extra caution. Actually I always wanted to have two levels here: one for obvious problems where work-around exists (for example: "Current data unavailable, using cached values") and second (name it: ATTENTION) for potential problems and suggestions. 

    Example: "*Application running in development mode*" or "*Administration console is not secured with a password*". The application can tolerate warning messages, but they should always be justified and examined.

* `INFO` - Important business process has finished. In ideal world, administrator or advanced user should be able to understand INFO messages and quickly find out what the application is doing.

    For example if an application is all about booking airplane tickets, there should be only one INFO statement per each ticket saying "*[Who] booked ticket from [Where] to [Where]*". Other definition of INFO message: each action that changes the state of the application significantly (database update, external system request).

* `DEBUG` - Developers stuff. Example:
    
        log.debug("Message with id '{}' processed", message.getJMSMessageID());

* `TRACE` - Very detailed information, intended only for development. You might keep trace messages for a short period of time after deployment on production environment, but treat these log statements as temporary, that should or might be turned-off eventually. The distinction between DEBUG and TRACE is the most difficult, but if you put logging statement and remove it after the feature has been developed and tested, it should probably be on TRACE level.

VistA Patch Developer Guide
---------------------------

### Developer Workstation Setup

VistA Developer Software

The following programs need to be installed and configured for the appropriate VistA Development server:

*   Attachmate Reflection
*   InterSystems Cache Cube

Git will need to be installed on the computer, if it is not already installed, to retrieve the VistA KIDS Host file.

*For VA development projects:*

*   GFEs will already come with Reflection already installed. For Cache Cube, contact your OI&T office for instructions to obtain and install the latest licensed version of Cache for your GFE's operating system. 
*   Aside from acquiring a VistA development server and VistA and Cache developer accounts for that database, you will also need to have Forum access and be added as a Surgery package developer in Forum by a current Surgery package developer.

### Obtaining the Source Code and Dependencies

**Git**

Git can be obtained from git-scm.com. 

The source repository for the ASRC project currently resides at https://github.com/VHAINNOVATIONS/ASRCM. The KIDs Host File can be located at https://github.com/VHAINNOVATIONS/ASRCM/VistA/ZZASRC_1_1_SP11.KID.

Once the KIDs build has been copied to your GFE, it can be transferred via FTP to your VistA development server.

**Dependencies**

Although the ASRC patch is mainly new development using the Surgery namespace, there are some existing Surgery routines that needed to be modified as part of the operation requests process that will now interface with ASRC. 

The routines that were modified are: SRCUSS1 & SRSRQST1. Since the establishment of the Innovation VistA server that was used for the ASRC project, the Surgery patch `SR*3*177` has been released that contains ICD-10 related modifications to both SRCUSS1 and SRSRQST1. Therefore these modifications will need to be analyzed and the ASRC modifications will have to be merged into the `SR*3*177` version of these routines. 

Furthermore, routine SRSRQST1 is currently under development for patch `SR*3*184`. 

*For VA development projects:*

It is highly recommended that you use Forum tools such as "Display a Patch" and "Routines that overlap in patches" to determine if these routines were involved in other nationally released patches or patches that are currently underdevelopment. Any national released patches will need to be merged into your development environment. And for any patches that are currently under development using these routines, you will need to contact the patch developer(s) to coordinate release dates and code sharing.

**KIDS Installation**

Please refer to the ASRC Installation Guide for instructions on installing the ASRC KIDs host file.

Although any potential conflicts should be explained in the Dependencies section above, there is always chance that the new ASRC components could conflict with recent development from other Surgery projects. It's highly recommended that you compare the transport global from the ASRC host file with the current VistA development environment. All of the routines, options, and RPCs contain the sub-prefix of SRASRC or SR ASRC, so it's highly doubtful this will occur. However, there is a new ASRC file that will be included in this KIDs build. It's the SURGICAL RISK CALCULATIONS file (#136.1), and although it's doubtful that another project will create a new file by the same new, there's always the potential for a new file be created using the same file number: 136.1. 

### Testing the VistA Patch

If following the steps in the VistA section of the ASRC Installation guide, you should be able to verify whether or not you've had a successful installation. 

Outside of the installation, the true testing will be through the integration with the ASRC web application. Details on this can be found in the ASRC Installation and ASRC User Guides.

### Enhancing the VistA Patch

**ASRC VistA Components**

The ASRC Technical Guide contains detailed information on the VistA components that were added and modified for this KIDs build. All new VistA functionality for the ASRC tool was created with the following naming conventions:

*   All ASRC routines have the following prefix: SRASRC
*   All ASRC RPCs and the sole ASRC menu option have the following prefix: SR ASRC
*   The new ASRC file was named the SURGICAL RISK CALCULATIONS file (#136.1).

It is recommended that future VistA ASRC development should follow the same or a similar naming conventions. 

**Coding Standards**

The new functionality contained with the ASRC patch and any modifications and enhancements made to it need to be reviewed and must adhere to the Standards and Conventions set forth in the VistA SACC Guide.

**VA Development Project Considerations**

Considering the nature and limitations of the ASRC Innovation project, certain assumptions about VistA development had to be made. To move forward with the enhancement of the ASRC VistA software, some of the following will need to be considered and/or addressed:

*   An actual Surgery patch number for the ASRC VistA development and release will need to be generated in Forum.
*   The new Surgery ASRC file, SURGICAL RISK CALCULATIONS file (#136.1), will need to be submitted to the VA DBA for approval.
*   The Surgery package will need to be added as ICR subscribers for the following non-Surgery RPCs that were added to the SR ASRC menu option for use by the ASRC web application: ORQQPS LIST (ICR 1659), GMV EXTRACT REC (ICR 4416), GMV LATEST VM (ICR 4358). If these ICR subscriptions won't be granted for any reason, alternate solutions may need to be designed.
*   The Surgery package will need to be added as ICR subscribers for the following data/APIs that are used/referenced within the new Surgery ASRC routines: MAKE^TIUSRVP & SIGN^TIUSRVP2 (ICR 3535), RESULTS^LRPXAPI (ICR 4245), $$TESTNM^LRPXAPIU (ICR 4246), LIST^ORQQVS (ICR 1690), ^AUPNVHF (ICR 3084), SELECTED^VSIT (ICR 1905), NOTES^TIUSRVLV (ICR 2812), TGET^TIUSRVR1, OCL^PSOORRL. It was discovered late in development that ICR 1690 for LIST^ORQQVS had been withdrawn, so a request to reconsider its status may need to be made. If these ICR subscriptions won't be granted for any reason, alternate solutions may need to be designed.
   
