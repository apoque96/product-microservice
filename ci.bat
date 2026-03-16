@echo off
echo Starting validation process...

REM Set environment variable for test profile
set SPRING_PROFILES_ACTIVE=test
echo Environment variable SPRING_PROFILES_ACTIVE set to: %SPRING_PROFILES_ACTIVE%

echo.
echo Step 1: Running tests with mvnw clean verify...
call mvnw clean verify
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Tests failed! Build stopped.
    exit /b 1
)
echo Tests completed successfully.

echo.
echo Step 2: Running PMD static code analysis...
call mvnw pmd:check
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: PMD analysis failed! Build stopped.
    exit /b 1
)
echo PMD analysis completed successfully.

echo.
echo All validations passed successfully!
exit /b 0
