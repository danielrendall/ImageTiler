#!/bin/bash

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


java -jar ../code/core/target/ImageTiler.jar   -i $1 -t $2 -s $3 -o $1.svg -c "$4"
