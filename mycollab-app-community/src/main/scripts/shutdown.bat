@echo off
rem -----------------------------------------------------------------------------
rem Start Script for the MYCOLLAB Server
rem -----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal

set "EXECUTABLE=mycollab.bat"

call "%EXECUTABLE%" --stop

:end
