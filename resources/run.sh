#!/bin/bash

VERSION=1.0

if [ "X$1" == "X" ]
then
echo "No input file specified"
exit 1
fi

if [ "X$2" == "X" ]
then
echo "No tile type specified"
exit 1
fi

if [ "X$3" == "X" ]
then
echo "No scan format specified"
exit 1
fi


java -cp "../code/tiles/target/imagetiler-tiles-1.0.jar:../code/core/target/imagetiler-"$VERSION"-jar-with-dependencies.jar" uk.co.danielrendall.imagetiler.App -i $1 -t $2 -s $3 -o $1.svg -c "$4"
