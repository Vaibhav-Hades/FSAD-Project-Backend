@echo off
REM PostgreSQL Migration Verification Script for Windows
REM AgriValue Connect Backend

setlocal enabledelayedexpansion
set ERRORS=0
set WARNINGS=0

echo.
echo ================================================
echo PostgreSQL Migration Verification Script
echo AgriValue Connect Backend
echo ================================================
echo.

echo [1/10] Checking for MySQL references in pom.xml...
findstr /R "mysql-connector" pom.xml >nul 2>&1
if %errorlevel% equ 0 (
    echo [FAIL] MySQL connector dependency found
    set /a ERRORS=!ERRORS!+1
) else (
    echo [PASS] No MySQL connector dependency
)
echo.

echo [2/10] Checking for PostgreSQL driver...
findstr /R "org.postgresql" pom.xml >nul 2>&1
if %errorlevel% equ 0 (
    echo [PASS] PostgreSQL driver configured
) else (
    echo [FAIL] PostgreSQL driver not found
    set /a ERRORS=!ERRORS!+1
)
echo.

echo [3/10] Checking environment variables in application.properties...
findstr "DB_URL" src\main\resources\application.properties >nul 2>&1
if %errorlevel% equ 0 (
    echo [PASS] Environment variable configuration found
) else (
    echo [FAIL] Environment variable configuration missing
    set /a ERRORS=!ERRORS!+1
)
echo.

echo [4/10] Checking PostgreSQL dialect...
findstr "PostgreSQLDialect" src\main\resources\application.properties >nul 2>&1
if %errorlevel% equ 0 (
    echo [PASS] PostgreSQL dialect configured
) else (
    echo [FAIL] PostgreSQL dialect not configured
    set /a ERRORS=!ERRORS!+1
)
echo.

echo [5/10] Checking for MySQL8Dialect references...
findstr "MySQL8Dialect" src\main\resources\application.properties >nul 2>&1
if %errorlevel% equ 0 (
    echo [FAIL] MySQL8Dialect reference found
    set /a ERRORS=!ERRORS!+1
) else (
    echo [PASS] No MySQL8Dialect references
)
echo.

echo [6/10] Checking development profile...
if exist "src\main\resources\application-dev.properties" (
    echo [PASS] Development profile exists
) else (
    echo [WARN] Development profile not found
    set /a WARNINGS=!WARNINGS!+1
)
echo.

echo [7/10] Checking production profile...
if exist "src\main\resources\application-prod.properties" (
    echo [PASS] Production profile exists
) else (
    echo [WARN] Production profile not found
    set /a WARNINGS=!WARNINGS!+1
)
echo.

echo [8/10] Checking .env.example...
if exist ".env.example" (
    echo [PASS] Environment variable template exists
) else (
    echo [WARN] .env.example not found
    set /a WARNINGS=!WARNINGS!+1
)
echo.

echo [9/10] Checking PostgreSQL driver class name...
findstr "org.postgresql.Driver" src\main\resources\application.properties >nul 2>&1
if %errorlevel% equ 0 (
    echo [PASS] PostgreSQL driver class name correct
) else (
    echo [FAIL] PostgreSQL driver class name incorrect
    set /a ERRORS=!ERRORS!+1
)
echo.

echo [10/10] Checking pom.xml structure...
findstr "postgresql" pom.xml >nul 2>&1
if %errorlevel% equ 0 (
    echo [PASS] PostgreSQL in pom.xml
) else (
    echo [FAIL] PostgreSQL not in pom.xml
    set /a ERRORS=!ERRORS!+1
)
echo.

echo ================================================
echo Verification Results
echo ================================================
echo Passed: %((10 - ERRORS - WARNINGS))%
echo Warnings: %WARNINGS%
echo Errors: %ERRORS%
echo.

if %ERRORS% equ 0 (
    echo [SUCCESS] Migration verification PASSED!
    echo.
    echo Next steps:
    echo 1. mvn clean install -DskipTests
    echo 2. Configure environment variables
    echo 3. Run: mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
    echo.
    exit /b 0
) else (
    echo [FAILED] Migration verification FAILED!
    echo Please fix the issues listed above.
    exit /b 1
)
