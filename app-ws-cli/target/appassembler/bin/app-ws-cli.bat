@REM ----------------------------------------------------------------------------
@REM  Copyright 2001-2006 The Apache Software Foundation.
@REM
@REM  Licensed under the Apache License, Version 2.0 (the "License");
@REM  you may not use this file except in compliance with the License.
@REM  You may obtain a copy of the License at
@REM
@REM       http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.
@REM ----------------------------------------------------------------------------
@REM
@REM   Copyright (c) 2001-2006 The Apache Software Foundation.  All rights
@REM   reserved.

@echo off

set ERROR_CODE=0

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM -- 4NT shell
if "%eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto WinNTGetScriptDir

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto WinNTGetScriptDir

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of arguments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto Win9xGetScriptDir
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

:Win9xGetScriptDir
set SAVEDIR=%CD%
%0\
cd %0\..\.. 
set BASEDIR=%CD%
cd %SAVEDIR%
set SAVE_DIR=
goto repoSetup

:WinNTGetScriptDir
set BASEDIR=%~dp0\..

:repoSetup
set REPO=


if "%JAVACMD%"=="" set JAVACMD=java

if "%REPO%"=="" set REPO=%BASEDIR%\repo

set CLASSPATH="%BASEDIR%"\etc;"%REPO%"\org\springframework\boot\spring-boot-starter-actuator\1.5.12.RELEASE\spring-boot-starter-actuator-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter\1.5.12.RELEASE\spring-boot-starter-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot\1.5.12.RELEASE\spring-boot-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-autoconfigure\1.5.12.RELEASE\spring-boot-autoconfigure-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-logging\1.5.12.RELEASE\spring-boot-starter-logging-1.5.12.RELEASE.jar;"%REPO%"\ch\qos\logback\logback-classic\1.1.11\logback-classic-1.1.11.jar;"%REPO%"\ch\qos\logback\logback-core\1.1.11\logback-core-1.1.11.jar;"%REPO%"\org\slf4j\jcl-over-slf4j\1.7.25\jcl-over-slf4j-1.7.25.jar;"%REPO%"\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;"%REPO%"\org\slf4j\log4j-over-slf4j\1.7.25\log4j-over-slf4j-1.7.25.jar;"%REPO%"\org\springframework\spring-core\4.3.16.RELEASE\spring-core-4.3.16.RELEASE.jar;"%REPO%"\org\yaml\snakeyaml\1.17\snakeyaml-1.17.jar;"%REPO%"\org\springframework\boot\spring-boot-actuator\1.5.12.RELEASE\spring-boot-actuator-1.5.12.RELEASE.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-databind\2.8.11.1\jackson-databind-2.8.11.1.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-annotations\2.8.0\jackson-annotations-2.8.0.jar;"%REPO%"\com\fasterxml\jackson\core\jackson-core\2.8.11\jackson-core-2.8.11.jar;"%REPO%"\org\springframework\spring-context\4.3.16.RELEASE\spring-context-4.3.16.RELEASE.jar;"%REPO%"\org\springframework\spring-aop\4.3.16.RELEASE\spring-aop-4.3.16.RELEASE.jar;"%REPO%"\org\springframework\spring-beans\4.3.16.RELEASE\spring-beans-4.3.16.RELEASE.jar;"%REPO%"\org\springframework\spring-expression\4.3.16.RELEASE\spring-expression-4.3.16.RELEASE.jar;"%REPO%"\wsdl4j\wsdl4j\1.6.3\wsdl4j-1.6.3.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-thymeleaf\1.5.12.RELEASE\spring-boot-starter-thymeleaf-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-web\1.5.12.RELEASE\spring-boot-starter-web-1.5.12.RELEASE.jar;"%REPO%"\org\springframework\boot\spring-boot-starter-tomcat\1.5.12.RELEASE\spring-boot-starter-tomcat-1.5.12.RELEASE.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-core\8.5.29\tomcat-embed-core-8.5.29.jar;"%REPO%"\org\apache\tomcat\tomcat-annotations-api\8.5.29\tomcat-annotations-api-8.5.29.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-el\8.5.29\tomcat-embed-el-8.5.29.jar;"%REPO%"\org\apache\tomcat\embed\tomcat-embed-websocket\8.5.29\tomcat-embed-websocket-8.5.29.jar;"%REPO%"\org\hibernate\hibernate-validator\5.3.6.Final\hibernate-validator-5.3.6.Final.jar;"%REPO%"\javax\validation\validation-api\1.1.0.Final\validation-api-1.1.0.Final.jar;"%REPO%"\org\jboss\logging\jboss-logging\3.3.2.Final\jboss-logging-3.3.2.Final.jar;"%REPO%"\com\fasterxml\classmate\1.3.4\classmate-1.3.4.jar;"%REPO%"\org\springframework\spring-web\4.3.16.RELEASE\spring-web-4.3.16.RELEASE.jar;"%REPO%"\org\springframework\spring-webmvc\4.3.16.RELEASE\spring-webmvc-4.3.16.RELEASE.jar;"%REPO%"\org\thymeleaf\thymeleaf-spring4\2.1.6.RELEASE\thymeleaf-spring4-2.1.6.RELEASE.jar;"%REPO%"\org\thymeleaf\thymeleaf\2.1.6.RELEASE\thymeleaf-2.1.6.RELEASE.jar;"%REPO%"\ognl\ognl\3.0.8\ognl-3.0.8.jar;"%REPO%"\org\javassist\javassist\3.21.0-GA\javassist-3.21.0-GA.jar;"%REPO%"\org\unbescape\unbescape\1.1.0.RELEASE\unbescape-1.1.0.RELEASE.jar;"%REPO%"\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;"%REPO%"\nz\net\ultraq\thymeleaf\thymeleaf-layout-dialect\1.4.0\thymeleaf-layout-dialect-1.4.0.jar;"%REPO%"\org\codehaus\groovy\groovy\2.4.15\groovy-2.4.15.jar;"%REPO%"\org\webjars\bootstrap\3.3.7-1\bootstrap-3.3.7-1.jar;"%REPO%"\org\webjars\jquery\3.1.1\jquery-3.1.1.jar;"%REPO%"\app-ws-cli\app-ws-cli\1.0-SNAPSHOT\app-ws-cli-1.0-SNAPSHOT.jar

set ENDORSED_DIR=
if NOT "%ENDORSED_DIR%" == "" set CLASSPATH="%BASEDIR%"\%ENDORSED_DIR%\*;%CLASSPATH%

if NOT "%CLASSPATH_PREFIX%" == "" set CLASSPATH=%CLASSPATH_PREFIX%;%CLASSPATH%

@REM Reaching here means variables are defined and arguments have been captured
:endInit

%JAVACMD% %JAVA_OPTS%  -classpath %CLASSPATH% -Dapp.name="app-ws-cli" -Dapp.repo="%REPO%" -Dapp.home="%BASEDIR%" -Dbasedir="%BASEDIR%" sirs.app.ws.cli.AppClientApp %CMD_LINE_ARGS%
if %ERRORLEVEL% NEQ 0 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=%ERRORLEVEL%

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set CMD_LINE_ARGS=
goto postExec

:endNT
@REM If error code is set to 1 then the endlocal was done already in :error.
if %ERROR_CODE% EQU 0 @endlocal


:postExec

if "%FORCE_EXIT_ON_ERROR%" == "on" (
  if %ERROR_CODE% NEQ 0 exit %ERROR_CODE%
)

exit /B %ERROR_CODE%
