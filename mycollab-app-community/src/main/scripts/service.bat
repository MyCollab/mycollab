@echo off

rem Guess MYCOLLAB_HOME if not defined
set "CURRENT_DIR=%cd%"
if not "%MYCOLLAB_HOME%" == "" goto gotHome
cd ..
set "MYCOLLAB_HOME=%cd%"
cd "%CURRENT_DIR%"
:gotHome

if exist "%MYCOLLAB_HOME%\bin\mycollab.bat" goto okHome
echo The MYCOLLAB_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end

:okHome
rem ----- Execute The Requested Command ---------------------------------------
set SERVICE_NAME=MyCollab5

if "%PROCESSOR_ARCHITECTURE%" == "X86" SET EXT_INSTALL=%MYCOLLAB_HOME%\bin\MyCollab7.exe
if "%PROCESSOR_ARCHITECTURE%" == "AMD64" SET EXT_INSTALL=%MYCOLLAB_HOME%\bin\MyCollab7amd64.exe
if "%PROCESSOR_ARCHITECTURE%" == "IA64" SET EXT_INSTALL=%MYCOLLAB_HOME%\bin\MyCollab7ia64.exe

REM Install service
IF NOT DEFINED EXT_INSTALL (
  echo "Can not find the procrun program"
  goto end
)

set PR_DEPENDSON=MySQL56

REM Service log configuration
set PR_DESCRIPTION=MyCollab, project management and collaboration software
set PR_LOGPREFIX=%SERVICE_NAME%
set PR_LOGPATH=%MYCOLLAB_HOME%\logs
set PR_STDOUTPUT=%MYCOLLAB_HOME%\logs\stdout.txt
set PR_STDERROR=%MYCOLLAB_HOME%\logs\stderr.txt
set PR_LOGLEVEL=Debug

REM Path to java installation
set PR_CLASSPATH=%MYCOLLAB_HOME%\executor.jar

REM Startup configuration
set PR_STARTUP=auto
set PR_STARTMODE=jvm
set PR_STARTCLASS=com.mycollab.runner.Executor
set PR_STARTPATH=%MYCOLLAB_HOME%
set PR_STARTMETHOD=start

REM Shutdown configuration
set PR_STOPMODE=jvm
set PR_STOPCLASS=com.mycollab.runner.Executor
set PR_STOPMETHOD=stop
set PR_STOPPATH=%MYCOLLAB_HOME%

REM JVM configuration
set PR_JVMMS=128
set PR_JVMMX=1024
set PR_JVMSS=4000
set PR_JVMOPTIONS=-XX:NewSize=256m#-XX:MaxPermSize=256m#-XX:+DisableExplicitGC#-XX:+CMSClassUnloadingEnabled#-XX:+UseConcMarkSweepGC

if ""%1"" == ""--install"" goto install
if ""%1"" == ""--uninstall"" goto uninstall

:install
%EXT_INSTALL% //IS//%SERVICE_NAME%
goto end

:uninstall
%EXT_INSTALL% //SS//%SERVICE_NAME%
%EXT_INSTALL% //DS//%SERVICE_NAME%
goto end

:end