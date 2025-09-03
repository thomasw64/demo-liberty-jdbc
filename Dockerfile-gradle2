#    MIT License
#
#    Copyright (c) 2025 IBM, Author: Thomas Weinzettl
#
#    Permission is hereby granted, free of charge, to any person obtaining a copy
#    of this software and associated documentation files (the "Software"), to deal
#    in the Software without restriction, including without limitation the rights
#    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
#    copies of the Software, and to permit persons to whom the Software is
#    furnished to do so, subject to the following conditions:
#
#    The above copyright notice and this permission notice shall be included in all
#    copies or substantial portions of the Software.
#
#    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
#    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
#    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
#    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
#    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
#    SOFTWARE.
FROM localhost/gradle:9.0.0 AS build

RUN git clone https://github.com/thomasw64/demo-liberty-jdbc.git .
# COPY --chown=1001:0 . .

RUN mkdir -p .gradle ; \
    echo "org.gradle.daemon=false" >> .gradle/gradle.properties ; \
    gradle war ; \
    gradle copyJDBC ;

#############
FROM icr.io/appcafe/websphere-liberty:kernel-java17-openj9-ubi-minimal

COPY --chown=1001:0 src/main/liberty/config /config
COPY --chown=1001:0 --from=build /project/build/wlp/usr/servers/defaultServer/jdbc/* /opt/ibm/wlp/usr/servers/defaultServer/jdbc/
RUN features.sh

COPY --chown=1001:0 --from=build /project/build/libs/*.war /config/dropins
RUN configure.sh
