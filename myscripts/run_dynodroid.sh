#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPDIR=/home/vagrant/subjects/
RESULTDIR=/vagrant/results/`hostname`/dynodroid/

mkdir /home/vagrant/tools
#cp -r /vagrant/tools/dynodroid /home/vagrant/tools/
TOOLDIR=/home/vagrant/tools/dynodroid/deploy/

source $DIR/env.sh
echo "PATH=  $PATH"

cd $APPDIR
for p in `ls -d */`; do
#for p in `cat $DIR/projects.txt`; do

  if grep -q $p $RESULTDIR/status.log; then \
    echo "Skipping" $p
    continue
  fi

  # AVD setup is performed by Dynodroid
  for avd in `android list avd -c`; do
    android delete avd -n $avd
  done
  rm -rf ~/.android/avd/*
  killall -9 emulator64-arm

  echo "@@@ Processing project "$p
  mkdir -p $RESULTDIR$p
  cd $APPDIR$p
  #ant clean
  #ant emma debug &> $RESULTDIR$p/build.log
  app=`ls bin/*-debug.apk`
  apkName=`basename $app`
  echo "** PROCESSING APP " $app
  rm -rf $TOOLDIR/apps/*
  rm -rf $TOOLDIR/workingDir/*
  cp bin/*-debug.apk $TOOLDIR/apps/

  echo "** RUNNING DYNODROID FOR" $app
  cd $DIR
  ./run_dyno.py $TOOLDIR $RESULTDIR$p $apkName
  echo "-- FINISHED DYNODROID"

  NOW=$(date +"%m-%d-%Y-%H-%M")
  echo $NOW.$p >> $RESULTDIR/status.log

  cp -r $TOOLDIR/workingDir/* $RESULTDIR$p/
  rm -r $TOOLDIR/workingDir/*
  
  cp $APPDIR$p/bin/coverage.em $RESULTDIR$p/

  killall adb
  rm -r $TOOLDIR/apps/*.apk

  kill -9 `ps aux | grep dumpCoverageAndDynodroidLogs.sh | awk '{print $2}' | head -n1`
  
done
