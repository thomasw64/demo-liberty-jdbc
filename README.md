# demo-liberty-jdbc

A JDBC example with liberty.

## Build the container

1. Build and get the [gradle-container](https://github.com/thomasw64/gradle-container). See the instructions there.
2. Build this container:
```
podman build -t demo-liberty-jdbc:latest https://github.com/thomasw64/demo-liberty-jdbc.git
```
3. Run the container:
```
podman run -it --rm -p 9080:9080 localhost/demo-liberty-jdbc:latest
```

## Alternative Setup

Copy the `jdbc-demo-0.1.war` from the release to `build/libs`

Also copy the Db2 JCC-4 drivers and the license file into `build/wlp/usr/servers/defaultServer/jdbc/`

## Download the JDBC Drivers

from here [JDBC Drivers on Fix Central](https://www.ibm.com/support/fixcentral/swg/selectFixes?parent=ibm~Information%2BManagement&product=ibm/Information+Management/IBM+Data+Server+Client+Packages&release=12.1&platform=All&function=fixId&fixids=*FP000*&includeSupersedes=0&source=fc) filter for `IBM Data Server Driver for JDBC and SQLJ`.

