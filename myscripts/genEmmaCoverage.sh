#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ $# -eq 0 ]
  then
    echo "Usage: gen_coverage.sh <monkey|dynodroid|...>"
    exit
fi

for app in `ls $1`; do
  cd $DIR
  for covFile in `ls $1/$app/*.ec`; do
    echo "- Processing $1/$app/$covFile"
    mkdir -p $DIR/${covFile%.ec} #output directory
    cd $DIR/${covFile%.ec}
    srcDir=$DIR/../subjects/$app/src
    emFile=$DIR/../subjects/$app/bin/coverage.em
    java -cp $ANDROID_HOME/tools/lib/emma.jar emma report -r txt,html,xml -sp $srcDir -in $emFile -in $DIR/$covFile
  done
done
