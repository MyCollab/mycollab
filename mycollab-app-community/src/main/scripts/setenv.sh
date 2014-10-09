#!/bin/bash
# -----------------------------------------------------------------------------
# Set java exe and conf file for all scripts
#
# -----------------------------------------------------------------------------

echo '++++++++++ YAJSW SET ENV ++++++++++'

#remember current dir
current=$(pwd)
# resolve links - $0 may be a softlink
PRGDIR=$(dirname $0)

cd "$PRGDIR"

# path to yajsw bin folder
PRGDIR=$(pwd)

cd ".."

# path to wrapper home
wrapper_home=$(pwd)
export wrapper_home

# return to original folder
cd "$current"

wrapper_jar="$wrapper_home"/lib/yajsw-wrapper-11.11.jar
export wrapper_jar

wrapper_app_jar="$wrapper_home"/lib/yajsw-wrapperApp-11.11.jar
export wrapper_app_jar

wrapper_java_sys_options=-Djna_tmpdir="$wrapper_home"/tmp
export wrapper_java_sys_options

wrapper_java_options=-Xmx30m
export wrapper_java_options

java_exe=java
export java_exe

# show java version
"$java_exe" -version

conf_file="$wrapper_home"/conf/wrapper.conf
export conf_file

conf_default_file="$wrapper_home"/conf/wrapper.conf.default
export conf_default_file

echo "wrapper home : $wrapper_home"
echo "configuration: $conf_file"

# show java version
"$java_exe" -version
echo '---------- YAJSW SET ENV ----------'