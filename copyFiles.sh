#!/bin/sh
#
mkdir -p build/libs
mkdir -p build/wlp/usr/servers/defaultServer/jdbc
cp demo-jdbc-0.1.war build/libs/
cp db2jcc*.jar build/wlp/usr/servers/defaultServer/jdbc
cp jcc*.jar build/wlp/usr/servers/defaultServer/jdbc
