@echo off
rem -----------------------------------------------------------------------------
rem Start Script for the MYCOLLAB Server
rem -----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal

call mycollab.bat --start

:end
