#!/bin/bash

if [ $# -eq 0 ]
  then
    echo "Usage: dumpCoverage.sh <outdir> <dynodroid_dir> <apk>"
    exit
fi

#for i in `seq 1 12`;
i=0
while true
do
  i=$((i+1))
  sleep 300 #sleep for 5 minutes
  echo $1/$i
  adb shell am broadcast -a edu.gatech.m3.emma.COLLECT_COVERAGE
  adb pull /mnt/sdcard/coverage.ec $1/coverage$i.ec

  echo "copying monkeyrunner output file"
  cp $2/workingDir/$3_WBT_RandomBiasBased_1000000/TestStrategy/WBT.log $1/WBT.$i.log
done
