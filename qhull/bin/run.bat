set /a i=0;
for /l %%o in (1, 1, 2) do (
	for /l %%i in (10, 0, 100000) do (
	set /a i="i * 10"
	rbox %%i D3 | qconvex s 2>>result.txt
	   
	)
)