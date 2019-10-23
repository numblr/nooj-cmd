nooj-cmd
========

Command line interface for [nooj4nlp](http://www.nooj-association.org).

Developer:
Thomas Baier <tbaier@math.bme.hu>

Maintainer:
Márton Miháltz <mihaltz.marton@nytud.mta.hu>

# Usage

Please download https://bitbucket.org/tkb-/nooj-cmd/downloads/nooj-cmd-1.0-with-Nooj-v3.1-20140421.jar (the .jar file in the Downloads section of the repo on Bitbucket).
This jar file contains nooj-cmd, Java NooJ (version specified in the filename) and all its dependencies in one package.

nooj-cmd assumes the usual NooJ data file hierarchy in its working directory, ie. folders like
<lang_code>/Lexical Analysis/
to hold the Java NooJ dictionary files (.jnod) and folders like
<lang_code>/Syntactic Analysis/
to hold the Java NooJ grammar files (.nog).
A different path to the root of the data folder hierarchy may be specified in the command line (see below).

Example: to run nooj-cmd on a file named "testtext.txt" with a Hungarian dictionary "teszt_szotar.jnod" and 2 grammar files "aggr_test.nog" and "nation_test.nog", use the following command:
```Shell
   java -jar nooj-cmd-1.0-with-Nooj-v3.1-20140421.jar -i testtext.txt -l hu -d teszt_szotar.jnod -g aggr_test.nog,nation_test.nog
```

For further details on usage and available options, run nooj-cmd with --help:
```Shell
   java -jar nooj-cmd-1.0-with-Nooj-v3.1-20140421.jar --help
```

# Build

This repository contains configuration files to build the project with maven (maven.apache.org) and uses bintray.com/maven/grand-hifi/nooj-releases as an online release repository.

For the build the nooj4nlp jar and a related pom file (*) need to be installed to the local maven repository on your machine (~/.m2/repository):
```Shell
   mvn install:install-file -Dfile=PATH_TO_JAR -DpomFile=PATH_TO_POM
```
or to the online repository:
```Shell
	mvn deploy:deploy-file -Dfile=PATH_TO_JAR -DpomFile=PATH_TO_POM -Durl=api.bintray.com/maven/grand-hifi/nooj-releases/nooj4nlp -DrepositoryId=ID_FROM_SETTINGS
```
The pom file must specify the dependencies for nooj4nlp and artifact coordinates and a version number used to identify it from nooj-cmd.

The project can be built with the command:
```Shell
	mvn clean compile
```
from the directory containing the pom.xml.

To create a new release update the version number in the pom.xml and run either the local build or deploy the release to the online repository. After that increment the version number, postfixed with "-SNAPSHOT" to continue working on the next version.

Jar and jar-with-all-dependencies files can be created with
```Shell
	mvn clean install
```
and are placed in the target folder and installed to the local maven repository on your machine.

To deploy a release to the online repository run
```Shell
	mvn clean deploy
```

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

* see: https://dl.bintray.com/grand-hifi/nooj-releases/net/nooj4nlp/nooj4nlp/v3.1-20140421/nooj4nlp-v3.1-20140421.pom
