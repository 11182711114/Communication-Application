import os
import subprocess
import sys

usage = "\"-v\" verbose mode\n \"-q\" quiet mode\n \"-b $branch\" branch\n \"-pU\" project URL root\n \"-CP $compilePath\" Compile path, e.g. \"src/**/*.java\""
branch = "fredrik"
outputDir = os.getcwd()+"/app/"
currentDir = os.getcwd() + "/"
projectRoot = "https://github.com/11182711114/Communication-Application"
compilePath = "src/**/*.java"

outputChan = open(os.devnull, 'w')

args = sys.argv
if len(args) > 1:
	for index in range(len(args)):
		arg = args[index]
		if arg == "-v": #verbose
			outputChan = None
		elif arg == "-q": #quiet
			with open(os.devnull, 'w') as output:
				sys.stdout = output
		elif arg == "-b": #branch
			branch = args[index+1]
		elif arg == "-pU":#project URL root
			projectRoot = args[index + 1]
		elif arg == "-CP":#class path, e.g. "src/**/*.java"
			compilePath = args[index+1]
		elif arg == "--help" or arg == "-h":
			print(usage)
		elif arg == "-o":#output directory
			outputDir = args[index+1]

if projectRoot.rindex('/') == len(projectRoot)-1:
	projectRootTMP = projectRoot[:len(projectRoot)-2]
	projectRoot = projectRootTMP

githubArchiveRelative = "/archive/"
githubStandardFileExtension = ".zip"
fileLoc = projectRoot + githubArchiveRelative + branch + githubStandardFileExtension

print("Downloading archive")
wget_return = subprocess.call("wget "+fileLoc, shell=True, stdout=outputChan, stderr=subprocess.STDOUT)
if wget_return != 0:
	print("Something went wrong downloading")
	sys.exit()
print("Download successful")

archivePath = currentDir+branch+githubStandardFileExtension
print("Trying to unzip " + archivePath)
unzip_return = subprocess.call("unzip -o " + archivePath, shell=True, stdout=outputChan, stderr=subprocess.STDOUT)
if unzip_return != 0:
	print("Something went wrong unzipping, exiting")
	sys.exit()
print("unzipping successful")

print("Checking if " + outputDir + " exists")
if not os.path.exists(outputDir):
	print("making directory")
	os.makedirs(outputDir)

compileRoot = currentDir + projectRoot[projectRoot.rindex('/'):] + branch
compilingDir = compileRoot + compilePath
print("Compiling: javac -d " + outputDir +" "+ compilingDir)
javac_return = subprocess.call("javac -d " + outputDir +" "+ compilingDir, shell=True, stdout=outputChan, stderr=subprocess.STDOUT)
if javac_return != 0:
	print("Something went wrong compiling")
	sys.exit()
print("Compiling complete")

print("Cleaning...")
cleaning_return = subprocess.call("rm -r " + archivePath + " && rm -r " + compileRoot, stdout=outputChan, stderr=subprocess.STDOUT)
if cleaning_return != 0:
	print("Cleaning failed")
print("Cleaning completed")
