@echo off
setlocal
set SCRIPT_DIR=%~dp0
set CLASSES=%SCRIPT_DIR%imgcompare\imgcompare\target\classes
if not exist "%CLASSES%" (
  echo Classpath not found: "%CLASSES%". Build the project or make sure classes exist.
  exit /b 1
)
java -cp "%CLASSES%" com.imgcompare.Main %*
endlocal
