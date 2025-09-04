# demo-liberty-jdbc

A JDBC example with liberty.

## Build the container

1. Get `gradle:9.0.0` container from (https://github.com/thomasw64/gradle-container)
2. build the gradle container
3. build this container

## Alternative Setup

Copy the `jdbc-demo-0.1.war` from the release to `build/libs`

Also copy the Db2 JCC-4 drivers and the license file into `build/wlp/usr/servers/defaultServer/jdbc/`

## Download the JDBC Drivers

from here [JDBC Drivers on Fix Central](https://www.ibm.com/support/fixcentral/swg/selectFixes?parent=ibm~Information%2BManagement&product=ibm/Information+Management/IBM+Data+Server+Client+Packages&release=12.1&platform=All&function=fixId&fixids=*FP000*&includeSupersedes=0&source=fc) filter for `IBM Data Server Driver for JDBC and SQLJ`.

