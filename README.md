# demo-liberty-jdbc
A JDBC example with liberty.

## Build the container

1. `gradlew war`
2. `gradlew installFeatures` and `gradlew installLiberty`
3. make sure the drivers are in the jdbc folder of the defaultServer
4. place the license jar into the jdbc folder
5. run `docker build -t name:latest .`
6. run the image

# Alternative

Copy the `jdbc-demo-0.1.war` from the release to `build/libs`

Also copy the Db2 JCC drivers into `build/wlp/usr/servers/defaultServer/jdbc/`

## Download the JDBC Drivers

from here (https://www.ibm.com/support/fixcentral/swg/selectFixes?parent=ibm~Information%2BManagement&product=ibm/Information+Management/IBM+Data+Server+Client+Packages&release=12.1&platform=All&function=fixId&fixids=*FP000*&includeSupersedes=0&source=fc) and store in `lib`.

