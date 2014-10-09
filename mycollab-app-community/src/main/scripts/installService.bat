cd %~dp0
call setenv.bat
reg add "HKCU\SOFTWARE\Microsoft\Windows\CurrentVersion\Run" /V "MyCollab" /t REG_SZ /F /D "%wrapper_home%\bin\sytemTrayIconW.bat"
%wrapperw_bat% -i -t -y %conf_file%
pause


