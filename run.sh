#!/bin/bash

VERSION=1.0

if [ -f "code/core/target/imagetiler-"$VERSION"-jar-with-dependencies.jar" ]
then
	java -cp  "code/tiles/target/imagetiler-tiles-1.0.jar:code/core/target/imagetiler-"$VERSION"-jar-with-dependencies.jar" uk.co.danielrendall.imagetiler.ImageTilerApplication
else
	echo ImageTiler doesn''t appear to be built - maybe you need to run build.sh
fi

