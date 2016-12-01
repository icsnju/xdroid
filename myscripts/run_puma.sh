#!/bin/bash


DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPDIR=/home/vagrant/subjects/
RESULTDIR=/vagrant/results/`hostname`/puma/
export TOOLDIR=/home/vagrant/tools/PUMA

source $DIR/env.sh
echo "PATH=  $PATH"

cd $APPDIR

for p in `ls -d */`; do
#for p in `cat $DIR/projects.txt`; do

  # AVD setup
  echo "Setting up AVD"
  cd $DIR
  ./setupEmu.sh android-19

  echo "@@@@@@ Processing project " $p "@@@@@@@"
  mkdir -p $RESULTDIR$p
  cd $APPDIR$p

  echo "** Installing Apk"
  ant installd &> $RESULTDIR$p/install.log
  cp bin/coverage.em $RESULTDIR$p/
  app=`ls bin/*-debug.apk`
  
  echo "** PROCESSING APP " $app
  package=`aapt d badging $app | grep package: | awk -F\' '{print $2}'`
  appLabel=`aapt d badging $app | grep application-label: | awk -F\' '{print $2}'`
  echo $package > $TOOLDIR/app.info
  echo $appLabel >> $TOOLDIR/app.info
  
  echo "** RUNNING LOGCAT"
  adb logcat &> $RESULTDIR$p/tool.logcat &

  echo "** DUMPING INTERMEDIATE COVERAGE "
  cd $DIR
  ./dumpCoverage.sh $RESULTDIR$p &> $RESULTDIR$p/icoverage.log &

  # unlock device
  adb shell input keyevent 82

  echo "** RUNNING PUMA FOR" $package
  cd $TOOLDIR
  ./setup-phone.sh 
  date1=$(date +"%s")
  ./run.sh &> $RESULTDIR$p/tool.log
  date2=$(date +"%s")
  diff=$(($date2-$date1))
  echo "Ran PUMA for $(($diff / 60)) minutes and $(($diff % 60)) seconds."  

  echo "-- FINISHED PUMA"
  adb shell am broadcast -a edu.gatech.m3.emma.COLLECT_COVERAGE
  adb pull /mnt/sdcard/coverage.ec $RESULTDIR$p/coverage.ec

  NOW=$(date +"%m-%d-%Y-%H-%M")
  echo $NOW.$p >> $RESULTDIR/status.log

  adb kill-server
  kill -9 `ps | grep dumpCoverage.sh | awk '{print $1}'`

done
