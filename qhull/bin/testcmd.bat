@echo off
SetLocal

set B=2 3 4 5 6 7 8
set A=100 1000 10000 100000 1000000

FOR %%b in (%B%) do (
  FOR %%a in (%A% %%b) DO (
    echo %%a, %%b
    rbox %%a D%%b | qconvex s 2>>result.txt
  )
)