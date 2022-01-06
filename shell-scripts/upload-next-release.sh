cd ${BASH_SOURCE:-$0:h:h}

VERSION=`mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v "^\["`
mvn versions:set -DnewVersion=${VERSION//-SNAPSHOT/}

mvn clean source:jar javadoc:jar deploy # -Dmaven.test.skip=true