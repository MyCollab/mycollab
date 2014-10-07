cd %~dp0
call setenv.bat
%wrapper_bat% -i -t -y %conf_file%
pause


