cd %~dp0
call setenv.bat
reg delete "HKCU\SOFTWARE\Microsoft\Windows\CurrentVersion\Run" /v MyCollab /f
%wrapper_bat% -r %conf_file%
pause