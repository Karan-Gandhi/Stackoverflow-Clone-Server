@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  StackOverflow Clone startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and STACK_OVERFLOW_CLONE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\StackOverflow Clone.jar;%APP_HOME%\lib\firebase-admin-7.0.0.jar;%APP_HOME%\lib\google-api-client-gson-1.30.10.jar;%APP_HOME%\lib\google-cloud-storage-1.110.0.jar;%APP_HOME%\lib\google-api-client-1.30.10.jar;%APP_HOME%\lib\google-cloud-firestore-1.35.0.jar;%APP_HOME%\lib\api-common-1.9.2.jar;%APP_HOME%\lib\google-auth-library-oauth2-http-0.20.0.jar;%APP_HOME%\lib\google-oauth-client-1.31.0.jar;%APP_HOME%\lib\google-http-client-jackson2-1.36.0.jar;%APP_HOME%\lib\google-http-client-gson-1.36.0.jar;%APP_HOME%\lib\google-http-client-1.36.0.jar;%APP_HOME%\lib\opencensus-contrib-http-util-0.24.0.jar;%APP_HOME%\lib\guava-29.0-android.jar;%APP_HOME%\lib\proto-google-cloud-firestore-admin-v1-1.35.0.jar;%APP_HOME%\lib\proto-google-cloud-firestore-v1-1.35.0.jar;%APP_HOME%\lib\proto-google-cloud-firestore-v1beta1-0.88.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\error_prone_annotations-2.3.4.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\animal-sniffer-annotations-1.18.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\netty-codec-http-4.1.50.Final.jar;%APP_HOME%\lib\netty-handler-4.1.50.Final.jar;%APP_HOME%\lib\netty-codec-4.1.50.Final.jar;%APP_HOME%\lib\netty-transport-4.1.50.Final.jar;%APP_HOME%\lib\checker-compat-qual-2.5.5.jar;%APP_HOME%\lib\httpclient-4.5.12.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\opencensus-api-0.24.0.jar;%APP_HOME%\lib\javax.annotation-api-1.3.2.jar;%APP_HOME%\lib\auto-value-annotations-1.7.2.jar;%APP_HOME%\lib\google-auth-library-credentials-0.20.0.jar;%APP_HOME%\lib\jackson-core-2.11.1.jar;%APP_HOME%\lib\google-api-services-storage-v1-rev20200430-1.30.9.jar;%APP_HOME%\lib\gson-2.8.6.jar;%APP_HOME%\lib\google-cloud-core-1.93.6.jar;%APP_HOME%\lib\proto-google-common-protos-1.18.0.jar;%APP_HOME%\lib\google-cloud-core-http-1.93.6.jar;%APP_HOME%\lib\google-http-client-appengine-1.35.0.jar;%APP_HOME%\lib\gax-httpjson-0.74.0.jar;%APP_HOME%\lib\gax-1.57.0.jar;%APP_HOME%\lib\grpc-context-1.30.0.jar;%APP_HOME%\lib\proto-google-iam-v1-0.13.0.jar;%APP_HOME%\lib\protobuf-java-3.12.2.jar;%APP_HOME%\lib\protobuf-java-util-3.12.2.jar;%APP_HOME%\lib\threetenbp-1.4.4.jar;%APP_HOME%\lib\google-cloud-core-grpc-1.93.6.jar;%APP_HOME%\lib\grpc-core-1.30.0.jar;%APP_HOME%\lib\annotations-4.1.1.4.jar;%APP_HOME%\lib\perfmark-api-0.19.0.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\opencensus-contrib-grpc-util-0.24.0.jar;%APP_HOME%\lib\grpc-protobuf-1.30.0.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.30.0.jar;%APP_HOME%\lib\grpc-api-1.30.0.jar;%APP_HOME%\lib\gax-grpc-1.57.0.jar;%APP_HOME%\lib\grpc-auth-1.30.0.jar;%APP_HOME%\lib\grpc-netty-shaded-1.30.0.jar;%APP_HOME%\lib\grpc-alts-1.30.0.jar;%APP_HOME%\lib\grpc-grpclb-1.30.0.jar;%APP_HOME%\lib\commons-lang3-3.5.jar;%APP_HOME%\lib\conscrypt-openjdk-uber-2.2.1.jar;%APP_HOME%\lib\grpc-stub-1.30.0.jar;%APP_HOME%\lib\netty-buffer-4.1.50.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.50.Final.jar;%APP_HOME%\lib\netty-common-4.1.50.Final.jar

@rem Execute StackOverflow Clone
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %STACK_OVERFLOW_CLONE_OPTS%  -classpath "%CLASSPATH%" com.karangandhi.stackoverflow.clone.App %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable STACK_OVERFLOW_CLONE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%STACK_OVERFLOW_CLONE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
