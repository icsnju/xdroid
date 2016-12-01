#!/usr/bin/python

# called from run_dynodroid.sh

import sys, os, subprocess, time, signal
from threading import Timer

currDir = os.getcwd()
toolDir = sys.argv[1]
resultDir = sys.argv[2]
apkName = sys.argv[3]
global pLogcat
global pDumpCov
global p
global outFile
global started
started=False
global finished
finished=False

def timeout():
  global pLogcat
  global pDumpCov
  global p
  global outFile
  global finished
  print " ## Collect end coverage"
  subprocess.call("adb shell am broadcast -a edu.gatech.m3.emma.COLLECT_COVERAGE", shell=True)
  subprocess.call("adb pull /mnt/sdcard/coverage.ec %s/coverage.ec"%(resultDir), shell=True)
  print " ## Kill DYNODROID"
  os.kill(p.pid, signal.SIGKILL)
  os.kill(pLogcat.pid, signal.SIGKILL)
  os.kill(pDumpCov.pid, signal.SIGKILL)
  subprocess.call("killall -9 java", shell=True)
  print "  -- finished killing"
  outFile.close()
  subprocess.call("android delete avd -n emu1", shell=True)
  finished=True
  sys.exit(0)

def startMeasurements():
  global pLogcat
  global pDumpCov
  started=True
  print "====>> DYNODROID TOOL HAS STARTED"
  print " ## Capturing LOGCAT"
  with open("%s/tool.logcat"%(resultDir), 'w') as logCatFile:
    pLogcat = subprocess.Popen("%s/platform-tools/adb logcat"%(os.environ['ANDROID_HOME']), cwd=currDir, stdout=logCatFile, stderr=logCatFile, shell=True)

  print " ## Capturing intermediate coverage"
  with open("%s/icoverage.log"%(resultDir), 'w') as covLog:
    pDumpCov = subprocess.Popen("./dumpCoverageAndDynodroidLogs.sh %s %s %s"%(resultDir, toolDir, apkName), cwd=currDir, stdout=covLog, stderr=covLog, shell=True)

  #print "Run forever"
  print " ## Set Timeout of 1 hour"
  t = Timer(3600, timeout)
  t.start()

logFilePath="%s/tool.log"%(resultDir)
print "Logging tool output to",logFilePath

with open(logFilePath, "w") as logFile:
  p = subprocess.Popen("ant run", cwd=toolDir, stdout=logFile, stderr=logFile, shell=True)

while not os.path.exists(logFilePath):
    time.sleep(1)

# Implementing tail -f using python
# http://code.activestate.com/recipes/157035-tail-f-in-python/
outFile = open(logFilePath, "r")

while True:
  where = outFile.tell()
  line = outFile.readline()
  if not line and not finished:
    time.sleep(1)
    outFile.seek(where)
  else:
    #print line
    if "Preparation Sucessfull.." in line and not started:
      startMeasurements()

  if finished:
    print "Finished testing app"
    break

#print ":: Cleanup"
#timeout()
#outFile.close()
#subprocess.call("android delete avd -n emu1", shell=True)
