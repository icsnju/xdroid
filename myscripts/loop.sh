#!/bin/bash

i=0
#for i in `seq 1 12`;
while true
do
  i=$((i+1))
  sleep 1 #sleep for 5 minutes
  echo $1/$i
done
