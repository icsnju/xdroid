#!/usr/bin/python

import os, os.path, sys

if len(sys.argv) < 2:
  print "Usage: script.py <monkey|dynodroid...>"
  sys.exit(1)

tool=sys.argv[1]
resultDir=os.path.abspath(os.curdir)+"/"+tool

def getLC(file):
  return float(sum(1 for line in open(file)))

def getDirs(root):
  return [name for name in os.listdir(root) if os.path.isdir(os.path.join(root, name))]

appDirs=getDirs(resultDir)

for appDir in appDirs:

  print appDir,

  appDir = resultDir + "/" + appDir

  allLinesFile=appDir+"/coverage/AllLines-"+tool+".txt"
  if os.path.isfile(allLinesFile):
    total=getLC(allLinesFile)

  dirs = getDirs(appDir)
  for d in dirs:
    covLinesFile = appDir+"/"+d+"/AllCovered-"+tool+".txt"
    if os.path.isfile(covLinesFile):
      covered=getLC(covLinesFile)
      coverage=covered/total*100
      print ",", coverage,
  print
