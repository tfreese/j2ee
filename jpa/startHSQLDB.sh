#!/bin/bash
#
# Thomas Freese

java -cp /home/tommy/.m2/repository/org/hsqldb/hsqldb/2.3.4/hsqldb-2.3.4.jar \
org.hsqldb.Server \
--props $(dirname $0)/hsqldb-server.properties