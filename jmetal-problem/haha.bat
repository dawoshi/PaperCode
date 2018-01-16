:first1
set "t=%time: =0%"
if "%t:~,5%" geq "05:00" if "%t:~,5%" leq "07:35" goto :first11
goto :first1
:first11
shutdown -s -f -t 900