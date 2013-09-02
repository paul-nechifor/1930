#!/bin/bash

killall -9 python # This is the brain dead way of doing it.
./build.py $1
cd build/client && python -m SimpleHTTPServer 7777 &
java -jar build/server/server.jar
