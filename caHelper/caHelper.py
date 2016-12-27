import subprocess
import sys
import os

fileLoc = "https://github.com/11182711114/Communication-Application/archive/fredrik.zip"
branch = "fredrik"
outputDir = os.getcwd()+"/app/"

outputChan = open(os.devnull, 'w')

if len(sys.argv)>1 and sys.argv[1] == "-v":
	outputChan = None

print "Downloading archive"

wget_return = subprocess.call("wget "+fileLoc,shell=True,stdout=outputChan,stderr=subprocess.STDOUT)

if wget_return != 0: 
	print "Something went wrong downloading"
	sys.exit()

print "Download successful"
print "unzipping " + os.getcwd() +"/" + branch +".zip"

unzip_return = subprocess.call("unzip -o " + os.getcwd() +"/" + branch + ".zip",shell=True,stdout=outputChan,stderr=subprocess.STDOUT)

if unzip_return != 0:
	print "Something went wrong unzipping, exiting"
	sys.exit()

print "unzipping successful"
print "Checking if " + outputDir + " exists"

if not os.path.exists(outputDir):
	print "making directory"
	os.makedirs(outputDir)

compilingDir = os.getcwd() + "/Communication-Application-"+branch+"/src/**/*.java"

print "Compiling: javac -d " + os.getcwd() +"/app/ " + compilingDir

javac_return = subprocess.call("javac -d " + os.getcwd() +"/app/ " + compilingDir,shell=True,stdout=outputChan,stderr=subprocess.STDOUT)

if javac_return != 0:
	print "Something went wrong compiling"
	sys.exit()

print "Compiling complete"
print "Cleaning..."
subprocess.call("rm -r " + os.getcwd() + "/"+branch+".zip && rm -r " + os.getcwd() + "/Communication-Application-"+branch,stdout=outputChan,stderr=subprocess.STDOUT)
