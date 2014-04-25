nooj-cmd
========

Command line interface for nooj4nlp

#Build

This repository contains configuration files to build the project with maven (maven.apache.org)

For the build the open nooj jar needs to be installed to the local maven repository on your machine:
```Shell
	mvn install:install-file -Dfile=nooj4nlp-VERSION.jar -DgroupId=net.nooj4nlp -DartifactId=nooj4nlp -Dpackaging=jar -Dversion=VERSION
```
where nooj4nlp-VERSION.jar is the jar to be installed and VERSION is the nooj4nlp version number as specified in the pom.xml. The dependencies of nooj4nlp are already added  to the pom.xml.
The project can be built with the command:
```Shell
	mvn clean compile
```
from the directory containing the pom.xml. Jar and jar-with-all-dependencies files can be created with
```Shell
	mvn clean install
```
and are placed in the target folder and installed to the local maven repository on your machine.

To create a new release update the version number in the pom.xml and run
```Shell
	mvn clean deploy
```
After that increment the version number, postfixed with "_SNAPSHOT" to continue working on the next version.
