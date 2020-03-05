#!/bin/sh

buildpath="$(cd $(dirname $0); pwd)"
srcpath="${buildpath}/../../addons/cadastrapp/js/"
releasepath="${srcpath}/build"
venv="${buildpath}/env"

#
# Command path definitions
#
python="/usr/bin/python"
mkdir="/bin/mkdir"
rm="/bin/rm"
sh="/bin/sh"
cp="/bin/cp"



#
# build
#
if [ -d ${releasepath} ]; then
    ${rm} -rf ${releasepath}
fi

${mkdir} -p ${releasepath}

(cd ${buildpath};
 ${venv}/bin/jsbuild -h > /dev/null
 if  [ ! -d ${venv} ] || [ $? -eq 0 ]; then
     echo "creating virtual env and installing jstools..."
     rm -rf ${venv}
     virtualenv ${venv}
     ${venv}/bin/pip install jstools==0.6
     echo "done."
 fi;

 echo "running jsbuild for main app..."
 ${venv}/bin/jsbuild -o "${releasepath}" main.cfg
 echo "done."
)

if [ ! -e ${releasepath}/cadastrapp.js ]; then
    echo "\033[01;31m[NOK]\033[00m jsbuild failure"
    exit 1
fi;

exit 0
