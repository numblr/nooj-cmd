nooj-cmd
========

Command line interface for nooj4nlp

#Build

This repository contains configuration files to build the project with maven (maven.apache.org) and uses bintray.com/maven/grand-hifi/nooj-releases as an online release repository.

For the build the nooj4nlp jar and a related pom file (*) need to be installed to the local maven repository on your machine (~/.m2/repository):
```Shell
	mvn install:install-file -Dfile=nooj4nlp-VERSION.jar -DgroupId=net.nooj4nlp -DartifactId=nooj4nlp -Dpackaging=jar -Dversion=VERSION
```
or to the online repository:
```Shell
	mvn deploy:deploy-file -Dfile=nooj4nlp-VERSION.jar -DpomFile=PATH_TO_POM -Durl=api.bintray.com/maven/grand-hifi/nooj-releases/nooj4nlp -DrepositoryId=ID_FROM_SETTINGS
```
where nooj4nlp-VERSION.jar is the jar to be installed and VERSION is the nooj4nlp version number as specified in the pom.xml. The dependencies of nooj4nlp are already added  to the pom.xml.

The project can be built with the command:
```Shell
	mvn clean compile
```
from the directory containing the pom.xml.

Jar and jar-with-all-dependencies files can be created with
```Shell
	mvn clean install
```
and are placed in the target folder and installed to the local maven repository on your machine.

To deploy a release to the online repository run
```Shell
	mvn clean deploy
```

To create a new release update the version number in the pom.xml and run either the local build or deploy the release to the online repository. After that increment the version number, postfixed with "_SNAPSHOT" to continue working on the next version.

For deployments to the online repository the bintray user account credentials need to be specified in the maven settings xml (~/.m2/settings.xml):
```
  <servers>
    <server>
      <id>nooj-deploy-releases</id>
      <username>user name</username>
      <password>API KEY from bintray settings</password>
    </server>
  </servers>
```

* see: http://bintray.com/grand-hifi/nooj-releases/nooj4nlp/v3.1-20140421/