#!/bin/bash

pushd code/core && mvn clean && mvn install && mvn -o assembly:single && popd
pushd code/tiles && mvn clean && mvn install  && popd

