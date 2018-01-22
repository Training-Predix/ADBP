@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

REM Prior to running the script, please make sure you have done the following:
REM  	1. cf is installed
REM  	2. uaac is installed
REM This script does the following:
REM   1. Creates the following Predix Services: UAA, Time Series and Blobstore
REM   2. Creates a client with the appropriate permissions (scope and authorities)
REM   3. Creates users, groups and assigns users to groups

SET cfusername=student10
REM # Must escape password characters
SET cfpassword=PrediX20^^!7
SET cforg=Predix-Training
SET cfspace=Training5

REM ############################### WELCOME ASCII ART ###############################
REM #Predix Training
type predixtraining.txt
ECHO.

REM ############################### Requirements check ###############################
:requirements
ECHO.
ECHO.Checking prerequisites...
CALL :verifyCommand %%cf -v || GOTO :errorexit
CALL :verifyCommand %%uaac -v || GOTO :errorexit
ECHO.
GOTO :cflogin

REM ############################### Verify command exists ###############################
:verifyCommand
SET arg1=%1
SET command=%1 %2
SET ERRORLEVEL=
CALL !command! >nul 2>&1
if %ERRORLEVEL% == 0 (
    ECHO OK !arg1!
) else (
    ECHO.ERROR !arg1! not found!
    ECHO.
    ECHO.Please install:
    ECHO.     CF - https://github.com/cloudfoundry/cli
    ECHO.     UAAC - https://github.com/cloudfoundry/cf-uaac
    CALL :sadKitty
    EXIT /B 1
)
EXIT /B 0

REM ############################### LOGIN TO CF ###############################
:cflogin
  ECHO.Logging into Cloud Foundry...
  SET cfcmd=cf login -a https://api.system.aws-usw02-pr.ice.predix.io -u !cfusername! -p !cfpassword! -o !cforg! -s !cfspace!
  CALL !cfcmd! || GOTO :errorexit

REM ############################### PUSH Hello-Predix app to CF ###############################
:pushAPP
   ECHO.
   (SET /P prefix=Enter a prefix for the services name:
     SET appname=!prefix!-hello-predix
     cd ..
     cd hello-predix
     cf push !appname! --random-route
   ) || GOTO :errorexit

REM ############################### Create New UAA #####################################
:createUAA
  ECHO.
  ECHO.Creating UAA service...
  ECHO.
  SET uaaname=!prefix!-uaa
  SET createuaacmd=cf cs predix-uaa Free !uaaname! -c "{\"adminClientSecret\":\"admin_secret\"}" || GOTO :errorexit
  CALL !createuaacmd! || GOTO :errorexit

REM ############################### Binding App to new UAA ###############################
  :bindUAA
  ECHO.
  ECHO.Binding !appname! app to !uaaname!...
  SET binduaacmd=cf bind-service !appname! !uaaname! || GOTO :errorexit
  CALL !binduaacmd! || GOTO :errorexit

REM ############################### Getting UAA Endpoint #####################################
:gettingUAA
  ECHO.
  ECHO.Getting UAA Endpoint...
    FOR /F "delims=" %%a in ('cmd /c "cf env !appname!|find "predix-uaa"|find "uri""') do @set token1=%%a
    if not defined token1 (
        ECHO.ERROR Unable to find UAA Endpoint for you!
        GOTO :errorexit
        EXIT /B 1
    ) else (
        for /f "tokens=2,3 delims=:" %%a in ("!token1!") do (set utoken=%%a:%%b)
        if not "!utoken!"=="" (
            SET uri1=!utoken:"=!
            SET uri2=!uri1:,=!
            SET uaa_uri=!uri2: =!
            ECHO UAA endpoint:
            ECHO !uaa_uri!
        ) else (
            ECHO.ERROR Unable to find UAA Endpoint for you!
            GOTO :errorexit
            EXIT /B 1
        )
    )

REM ############################### Create Client #####################################
:createClient
  ECHO.
  SET settargetcmd=uaac target !uaa_uri! --skip-ssl-validation
  CALL !settargetcmd! || GOTO :errorexit
  SET gettokencmd=uaac token client get admin -s admin_secret
  CALL !gettokencmd! || GOTO :errorexit
  SET clientname=!prefix!-client
  SET getclientcmd=uaac client add !clientname! -s secret --authorized_grant_types "authorization_code client_credentials password refresh_token" --autoapprove "openid scim.me" --authorities "clients.read clients.write scim.read scim.write" --scope "openid" --redirect_uri "http://localhost:5000"
  REM SET getclientcmd=uaac client update !clientname! --authorized_grant_types "authorization_code client_credentials password refresh_token" --autoapprove "openid scim.me" --authorities "clients.read clients.write scim.read scim.write" --scope "openid"
  CALL !getclientcmd! || GOTO :errorexit

REM ############################### Create Time Series #####################################
:createTimeSeries
  ECHO.
	ECHO.Creating Time Series service...
  SET timeseriesname=!prefix!-timeseries
	SET createtimeseriescmd=cf create-service predix-timeseries Free !timeseriesname! -c "{\"trustedIssuerIds\":[\"!uaa_uri!/oauth/token\"]}"
  CALL !createtimeseriescmd! || GOTO :errorexit
  SET bindtimeseriescmd=cf bs !appname! !timeseriesname!
  CALL !bindtimeseriescmd! || GOTO :errorexit

REM ############################### Get Time Series Scope #####################################
:getTimeSeriesScope
  (FOR /F "delims=" %%a in ('cmd /c "cf env !appname!|find "zone-http-header-value""') do @set tsToken1=%%a) || GOTO :errorexit
  FOR /f "tokens=2 delims=:" %%a in ("!tsToken1!") do (set tsToken2=%%a) || GOTO :errorexit
  SET tsToken3=!tsToken2:"oauth-scope": "=!
  SET tsToken4=!tsToken3:"=!
  SET tsToken5=!tsToken4:,=!
  SET tsscope=!tsToken5: =!
  REM ECHO.!tsscope!

REM ############################### Create Blobstore #####################################
:createBlobstore
  ECHO.
	ECHO.Creating Blobstore service...
  SET blobstorename=!prefix!-blobstore
	SET createblobstorecmd=cf create-service predix-blobstore Tiered !blobstorename!
  CALL !createblobstorecmd! || GOTO :errorexit
  SET bindblobstorecmd=cf bs !appname! !blobstorename!
  CALL !bindblobstorecmd! || GOTO :errorexit

REM ############################### Update Client #####################################
:updateClient
  ECHO.
  ECHO.Updating client...
  REM SET settargetcmd=uaac target !uaa_uri! --skip-ssl-validation
  REM CALL !settargetcmd! || GOTO :errorexit
  REM SET gettokencmd=uaac token client get admin -s admin_secret
  REM CALL !gettokencmd! || GOTO :errorexit
  SET tsIngestScope=timeseries.zones.!tsscope!.ingest
  SET tsQueryScope=timeseries.zones.!tsscope!.query
  SET tsUserScope=timeseries.zones.!tsscope!.user
  SET getclientupdatecmd=uaac client update !clientname! --authorities "clients.read clients.write scim.read scim.write uaa.resource !tsIngestScope! !tsQueryScope! !tsUserScope!" --scope "uaa.none openid"
  CALL !getclientupdatecmd! || GOTO :errorexit

REM ############################### Create Users #####################################
:createUsers
  ECHO.
  ECHO.Creating users...
  SET createAppAdminCmd=uaac user add app_admin --emails app_admin@gegrctest.com -p APP_admin18
  CALL !createAppAdminCmd! || GOTO :errorexit
  SET createAppUserCmd=uaac user add app_user --emails app_user@gegrctest.com -p APP_user18
  CALL !createAppUserCmd! || GOTO :errorexit

REM ############################### Create Groups #####################################
:createGroups
  ECHO.
  ECHO.Creating groups...
  SET createTSIngestGroupCmd=uaac group add !tsIngestScope!
  CALL !createTSIngestGroupCmd! || GOTO :errorexit
  SET createTSUserGroupCmd=uaac group add !tsUserScope!
  CALL !createTSUserGroupCmd! || GOTO :errorexit
  SET createTSQueryGroupCmd=uaac group add !tsQueryScope!
  CALL !createTSQueryGroupCmd! || GOTO :errorexit

REM ############################### Assign Users to Groups #####################################
:assignUsersToGroups
  ECHO.
  ECHO.Assigning users to groups...
  SET assignTSIngestGroupAdminCmd=uaac member add !tsIngestScope! app_admin
  CALL !assignTSIngestGroupAdminCmd! || GOTO :errorexit
  SET assignTSUserGroupAdminCmd=uaac member add !tsUserScope! app_admin
  CALL !assignTSUserGroupAdminCmd! || GOTO :errorexit
  SET assignTSQueryGroupAdminCmd=uaac member add !tsQueryScope! app_admin

  ECHO.
  SET assignTSIngestGroupUserCmd=uaac member add !tsIngestScope! app_user
  CALL !assignTSIngestGroupUserCmd! || GOTO :errorexit
  SET assignTSUserGroupUserCmd=uaac member add !tsUserScope! app_user
  CALL !assignTSUserGroupUserCmd! || GOTO :errorexit
  SET assignTSQueryGroupUserCmd=uaac member add !tsQueryScope! app_user
  CALL !assignTSQueryGroupUserCmd! || GOTO :errorexit

  REM ############################### Output to File ###############################
  :outputToFile
    REM adbp-environment.txt
    cd ../scripts
    (
      echo Hello Predix App Name      :  !appname!
      echo UAA Name                   :  !uaaname!
      echo UAA URI                    :  !uaa_uri!
      echo UAA Admin Secret           :  admin_secret
      echo Client Name                :  !clientname!
      echo Client Secret              :  secret
      echo Timeseries Name            :  !timeseriesname!
      echo Blobstore Name             :  !blobstorename!
      echo App Admin User Name        :  app_admin
      echo App Admin User Password    :  APP_admin18
      echo App User Name              :  app_user
      echo App User Password          :  APP_user18
    ) > adbp-environment.txt
    ECHO.Your services are now set up^^!
    ECHO.A adbp-environment.txt file with all your environment details is created in your scripts directory
    EXIT /B 0

REM ############################### ERROR ENCOUNTERED, SAD KITTY ###############################
:errorexit
  ECHO.
  ECHO      /\ ___ /\
  ECHO     (  o   o  )
  ECHO      \  ^>#^<  /
  ECHO      /       \
  ECHO     /         \       ^^
  ECHO    ^|           ^|     //
  ECHO     \         /    //
  ECHO      ///  ///   --
  ECHO.
  Exit /B 0
