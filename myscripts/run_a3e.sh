#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPDIR=$DIR/../subjects/
RESULTDIR=$DIR/../results/a3e/

#mkdir /home/vagrant/tools
#cp -r  /vagrant/tools/a3e /home/vagrant/tools/
TOOLDIR=$DIR/../tools/a3e

source $DIR/env.sh
echo "PATH=  $PATH"

cd $APPDIR
for p in `ls -d */`; do
#for p in `cat $DIR/projects.txt`; do

  # echo "Setting up AVD"
  # cd $DIR
  # ./setupEmu.sh android-10

  echo "@@@@@@ Processing project " $p "@@@@@@@"
  mkdir -p $RESULTDIR$p
  cd $APPDIR$p
  echo "** BUILDING APP " $app
  #ant clean
  #ant emma debug &> $RESULTDIR$p/build.log
  
  cp bin/coverage.em $RESULTDIR$p/
  app=`ls bin/*-debug.apk`
  apkname=`basename $app`

  echo "** PROCESSING APP " $app
  rm -r $TOOLDIR/apks/*.apk
  cp bin/*-debug.apk $TOOLDIR/apks/

  echo "** RUNNING LOGCAT"
  adb logcat &> $RESULTDIR$p/a3e.logcat &

  echo "** DUMPING INTERMEDIATE COVERAGE "
  cd $DIR
  ./dumpCoverage.sh $RESULTDIR$p &> $RESULTDIR$p/icoverage.log &

  echo "** RUNNING A3E FOR" $apkname
  cd $TOOLDIR
  gtimeout 1h bin/rec.rb apks/$apkname --no-rec -loop --dev DU2SSE1478031311 &> $RESULTDIR$p/a3e.log

  echo "-- FINISHED A3E -- "
  adb shell am broadcast -a edu.gatech.m3.emma.COLLECT_COVERAGE
  adb pull /mnt/sdcard/coverage.ec $RESULTDIR$p/coverage.ec

  NOW=$(date +"%m-%d-%Y-%H-%M")
  echo $NOW.$p >> $RESULTDIR/status.log

  killall dumpCoverage.sh
  killall adb
  rm -r $TOOLDIR/apks/*.apk

done
