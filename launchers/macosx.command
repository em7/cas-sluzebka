#!/bin/sh

cd "`dirname "$0"`"

exec java \
    -d64 \
    -XstartOnFirstThread \
    -jar \
    cas-sluzebka.jar
