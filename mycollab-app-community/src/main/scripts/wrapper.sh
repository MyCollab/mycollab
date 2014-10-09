#!/bin/bash
# -----------------------------------------------------------------------------
# execute a YAJSW command
#
# -----------------------------------------------------------------------------

"$java_exe" "$wrapper_java_options" "$wrapper_java_sys_options" -jar "$wrapper_jar" "$@" 
