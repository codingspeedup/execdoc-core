cd ${BASH_SOURCE:-$0:h:h}
mvn versions:set -DnewVersion=`date +"%Y-%m-%d.%H"`-SNAPSHOT
