@echo off
echo ========================================
echo          ECCS PROJECT BUILD
echo ========================================

echo Limpiando directorio target...
if exist target\classes rmdir /s /q target\classes
mkdir target\classes

echo.
echo Compilando proyecto...
javac -cp "lib\wildfly-openssl-2.2.5.Final.jar" ^
      --module-path "C:\Program Files\Java\jdk-24\jmods" ^
      --add-modules javafx.controls,javafx.graphics,javafx.base ^
      -d target\classes ^
      src\main\java\com\mx\eccs\*.java ^
      src\main\java\com\mx\eccs\auth\*.java ^
      src\main\java\com\mx\eccs\auth\components\*.java ^
      src\main\java\com\mx\eccs\arieserp\*.java ^
      src\main\java\com\mx\eccs\scorpioxl\*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ BUILD EXITOSO
    echo Archivos compilados en: target\classes
    echo.
    echo Para ejecutar:
    echo java --module-path "C:\Program Files\Java\jdk-24\jmods" --add-modules javafx.controls,javafx.graphics,javafx.base -cp "target\classes;lib\*" com.mx.eccs.App
) else (
    echo.
    echo ❌ BUILD FALLÓ
    echo Revisa los errores de compilación arriba
)

pause