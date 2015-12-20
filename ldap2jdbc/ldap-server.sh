#!/bin/bash
#
# Thomas Freese
echo LDAP Server

cat ~/dokumente/linux/ldap/evolutionperson.schema > /tmp/addressbook.ldif
cat ~/dokumente/linux/ldap/layout.ldif >> /tmp/addressbook.ldif
cat ~/dokumente/linux/ldap/addressbook/*.ldif >> /tmp/addressbook.ldif


BASEDIR=$PWD #Verzeichnis des Callers, aktuelles Verzeichnis
#BASEDIR=$(dirname $0) #Verzeichnis des Skripts
cd $(dirname $0)
target/classes/de/freese/spring/ldap/Main.class
if [ ! -f target/classes/de/freese/spring/ldap/Main.class ]; then
    mvn -q compile
fi


mvn -q exec:java -Dexec.mainClass="de.freese.spring.ldap.Main" -Dexec.classpathScope=runtime -Dexec.daemonThreadJoinTimeout=120000 -Dexec.killAfter=-1

cd "$BASEDIR"
