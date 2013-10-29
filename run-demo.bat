REM
REM This script creates and run the wicket-plugin demo.
REM

REM create artifacts using maven
call mvn clean install

REM create 'plugins' folder
cd demo/app
rmdir plugins /s/q
mkdir plugins
xcopy ..\plugins\plugin1\target\wicket-plugin-demo-plugin1-*.zip plugins /s
xcopy ..\plugins\plugin2\target\wicket-plugin-demo-plugin2-*.zip plugins /s

REM run demo
call mvn jetty:run
cd ..\..
