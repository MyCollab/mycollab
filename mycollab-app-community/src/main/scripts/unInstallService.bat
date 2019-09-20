@echo off
rem -----------------------------------------------------------------------------
rem Uninstall MyCollab service script
rem -----------------------------------------------------------------------------

if "%OS%" == "Windows_NT" setlocal

call service.bat --uninstall

:end