#!/bin/bash
#
# Thomas Freese

java -cp /home/tommy/.m2/repository/org/hsqldb/hsqldb/2.4.0/hsqldb-2.4.0.jar \
org.hsqldb.Server \
--props "$(dirname $0)"/hsqldb-server.properties
