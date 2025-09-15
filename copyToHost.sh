#!/bin/bash
#
# URL=https://raw.githubusercontent.com/thomasw64/gradle-container/refs/heads/main/Dockerfile
#
function usage () {
    echo "$0 [<username>@]<hostname>"
}

function copyToHost () {
    echo " - Copy $1 on host".
    scp $1 $TARGETHOST:$TARGETDIR
}

function mkTargetDir () {
    echo " - Create $TARGETDIR on host."
    ssh $TARGETHOST mkdir -p $TARGETDIR
}

GITHUB_BASEURL=https://raw.githubusercontent.com/thomasw64/demo-liberty-jdbc/refs/heads/main
function getFromGithub () {
    [[ -f Dockerfile ]] || {
        echo " - Get $1 from GitHub"
        curl -O ${GITHUB_BASEURL}/$1
    }
}

echo "Copy to host $1"
echo " "
[[ -z $1 ]] && {
    usage
    exit
}

TARGETHOST=$1
TARGETDIR=$(basename $(pwd))

getFromGithub Dockerfile
mkTargetDir
copyToHost Dockerfile
