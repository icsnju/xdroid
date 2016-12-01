#!/bin/bash

SCALA_HOME=/home/vagrant/lib/scala-2.9.3

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPDIR=/vagrant/apks/
RESULTDIR=/vagrant/results/swifthand/

export CLASSPATH="$SCALA_HOME/lib/scala-library.jar:$JAVA_HOME/lib/tools.jar:$ADK_LIB/ddms.jar:$ADK_LIB/ddmlib.jar:$ADK_LIB/chimpchat.jar:$ADK_LIB/guava-15.0.jar:/vagrant/tools/SwiftHand/dist/SwiftHand-all.jar"

cd $APPDIR
for p in `ls -d */`; do
#for p in `cat $DIR/projects.txt`; do

  echo "Setting up AVD"
  cd $DIR
  ./setupEmu.sh android-16

  echo "@@@@@@ Processing project " $p "@@@@@@@"
  mkdir -p $RESULTDIR$p
  cd $APPDIR$p
  app=`ls *.modified.apk`

  echo "** RUNNING LOGCAT"
  adb logcat &> $RESULTDIR$p/tool.logcat &

  echo "** DUMPING INTERMEDIATE COVERAGE "
  cd $DIR
  ./dumpCoverage.sh $RESULTDIR$p &> $RESULTDIR$p/icoverage.log &


  echo "** RUNNING SWIFTHAND FOR" $app
  cd $APPDIR$p
  java edu.berkeley.wtchoi.swift.CommandLine $app swift 3600 0 $RESULTDIR$p &> $RESULTDIR$p/tool.log

  echo "-- FINISHED SWIFTHAND"
  adb shell am broadcast -a edu.gatech.m3.emma.COLLECT_COVERAGE
  adb pull /mnt/sdcard/coverage.ec $RESULTDIR$p/coverage.ec

  NOW=$(date +"%m-%d-%Y-%H-%M")
  echo $NOW.$p >> $RESULTDIR/status.log

  killall dumpCoverage.sh
  killall adb

done

