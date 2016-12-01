#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ $# -eq 0 ]
  then
    echo "Usage: genCoverageSummary.sh <monkey|dynodroid|...>"
    exit
fi


for app in `ls $1`; do
  cd $DIR
  for covFile in `ls $1/$app/*.ec`; do
    echo "- Processing " $DIR/${covFile%.ec}
    #mkdir -p $DIR/${covFile%.ec} #output directory
    if [ ! -d "$DIR/${covFile%.ec}" ]; then
      echo "ERROR: coverage directory not found" $DIR/${covFile%.ec}
      exit
    fi
    cd $DIR/${covFile%.ec}
    make -f $DIR/covres-makefile TOOL=$1 HTML_DIR=$DIR/${covFile%.ec}/coverage/_files cleanAll
    make -f $DIR/covres-makefile TOOL=$1 HTML_DIR=$DIR/${covFile%.ec}/coverage/_files
  done
done
