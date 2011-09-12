#!/bin/bash

CORE_VERSION=`grep "<version>" code/core/pom.xml | head -n 1 | cut -d '>' -f 2 | cut -d '<' -f 1`
TILES_VERSION=`grep "<version>" code/tiles/pom.xml | head -n 1 | cut -d '>' -f 2 | cut -d '<' -f 1`

if [ -f "code/core/target/imagetiler-"$CORE_VERSION"-jar-with-dependencies.jar" ]
then
	java -cp  "code/tiles/target/imagetiler-tiles-"$TILES_VERSION".jar:code/core/target/imagetiler-"$CORE_VERSION"-jar-with-dependencies.jar" uk.co.danielrendall.imagetiler.ImageTilerApplication
else
	echo ImageTiler doesn''t appear to be built - maybe you need to run build.sh
fi

