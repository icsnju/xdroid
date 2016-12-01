#!/bin/bash

export DISPLAY=:0

export Z3_BIN=/home/vagrant/tools/z3/bin/z3
export JavaHeap=512m

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
APPDIR=/home/vagrant/subjects/
RESULTDIR=/vagrant/results/`hostname`/acteve/
export TOOLDIR=/home/vagrant/tools/acteve
A3T_DIR=$TOOLDIR

source $DIR/env.sh
echo "PATH=  $PATH"

cd $APPDIR

# Kill emulator, in case it is running
adb emu kill

# Delete any exisitng AVD
android delete avd -n acteve
echo no | android create avd -n acteve -t android-10 -c 4096M -b armeabi -f

for p in `ls -d */`; do
#for p in `cat $DIR/projects.txt`; do

  if grep -q $p $RESULTDIR/status.log; then \
    echo "Skipping" $p
    continue
  fi

  cd $DIR
  echo "Starting the emulator"
  emulator -avd acteve -system /home/vagrant/tools/acteve/a3t_sdk/system.img -ramdisk /home/vagrant/tools/acteve/ramdisk/ramdisk.img &

  date1=$(date +"%s")
  ./waitForEmu.sh
  date2=$(date +"%s")
  diff=$(($date2-$date1))
  echo ".. Emulator boot took $(($diff / 60)) minutes and $(($diff % 60)) seconds." 

  # Remove any partial coverage
  adb shell rm /mnt/sdcard/coverage.ec

  echo "@@@ Processing project "$p
  mkdir -p $RESULTDIR$p
  cd $APPDIR$p

  echo "Instrumenting and installing APK"
  # ant -f build_acteve.xml -Da3t= emma debug install
  ant -f build_acteve.xml -Da3t= installd &> $RESULTDIR$p/install.log

  app=`ls bin/*-debug.apk`

  echo "** PROCESSING APP " $app
  #PACKAGE=a2dp.Vol
  #MAINACT=.main
  package=`aapt d xmltree $app AndroidManifest.xml | grep package | awk 'BEGIN {FS="\""}{print $2}'`
  fullActivity=`aapt d badging $app | grep activity | awk -F"'" '{print $2}'`
  mainActivity=${fullActivity#$package}

  echo "** RUNNING LOGCAT"
  adb logcat &> $RESULTDIR$p/tool.logcat &

  echo "** DUMPING INTERMEDIATE COVERAGE "
  cd $DIR
  ./dumpCoverage.sh $RESULTDIR$p &> $RESULTDIR$p/icoverage.log &

  echo "Running Acteve"
  cd $TOOLDIR
  timeout 1h ant run -Da3t.monkey=test.txt -Da3t.pkg=$package -Da3t.mainact=$mainActivity -Da3t.out.dir=a3t_out -Da3t.userwait=4 -Da3t.rw.kind=id_field_write -Da3t.K=2 &> $RESULTDIR$p/tool.log

  echo "-- FINISHED ACTEVE for $p - $APP"

  echo "Copying Coverage Reports"
  adb pull /mnt/sdcard/coverage.ec $RESULTDIR$p/coverage.ec
  adb shell rm /mnt/sdcard/coverage.ec

  NOW=$(date +"%m-%d-%Y-%H-%M")
  echo $NOW.$p >> $RESULTDIR/status.log

  adb emu kill
  adb kill-server
  kill -9 `ps | grep dumpCoverage.sh | awk '{print $1}'`

done
