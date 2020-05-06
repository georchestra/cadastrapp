#!/bin/sh

buildpath="$(cd $(dirname $0); pwd)"
srcpath="${buildpath}/../../addons/cadastrapp/js/"
releasepath="${srcpath}/build"
venv="${buildpath}/env"

#
# Command path definitions
#
python="/usr/bin/env python2"
virtualenv="/usr/bin/env virtualenv --python=${python}"
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
 ${python} -c "import pip"
 if [ $? != 0 ]; then
    curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
    ${python} get-pip.py
    rm get-pip.py
 fi
 ${python} -c "import virtualenv"
 if [ $? != 0 ]; then
   ${python} -m pip install virtualenv
 fi

 if  [ ! -d ${venv} ] || [ $? -eq 0 ]; then
     echo "creating virtual env and installing jstools..."
     rm -rf ${venv}
     ${python} -m virtualenv ${venv}
     ${venv}/bin/python -m pip install jstools==0.6
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
