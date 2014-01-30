@echo off
rem -----------------------------------------------------------------------------
rem Start Script for the MYCOLLAB Server
rem -----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal

rem Guess MYCOLLAB_HOME if not defined
set "CURRENT_DIR=%cd%"
if not "%MYCOLLAB_HOME%" == "" goto gotHome
set "MYCOLLAB_HOME=%CURRENT_DIR%"
echo MyCollab Home %MYCOLLAB_HOME%
if exist "%MYCOLLAB_HOME%\mycollab.bat" goto okHome
cd ..
set "MYCOLLAB_HOME=%cd%"
cd "%CURRENT_DIR%"
:gotHome
if exist "%MYCOLLAB_HOME%\mycollab.bat" goto okHome
echo The MYCOLLAB_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome

set "EXECUTABLE=%MYCOLLAB_HOME%\mycollab.bat"

rem Check that target executable exists
if exist "%EXECUTABLE%" goto okExec
echo Cannot find "%EXECUTABLE%"
echo This file is needed to run this program
goto end
:okExec

rem Get remaining unshifted command line arguments and save them in the
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

call "%EXECUTABLE%" start %CMD_LINE_ARGS%

:end