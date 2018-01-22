#!/bin/sh

set -e

# Prior to running the script, please make sure you have done the following:
#  	1. cf is installed
#  	2. uaac is installed
# This script does the following:
#   1. Creates the following Predix Services: UAA, Asset, ACS, Time Series and Analytics
#   2. Creates a client with the appropriate permissions (scope and authorities)
#   3. Creates users, groups and assigns users to groups

main() {
	# disabling cf trace mode.
	export CF_TRACE=false
	welcome
	loginCf
	checkPrereq
	deployingApp
	createUAA
	getUAAEndpoint
	createClient
	createTimeseries
	createBlobstore
 	updateClient
	createUsers
	createGroups
	assignUsersToGroups
	output
}

loginCf()
{
   cf login -a https://api.system.aws-usw02-pr.ice.predix.io -u student10 -p 'PrediX20!7' -o Predix-Training -s Training5 || sadKitty

}

checkPrereq()
{
  {
	echo ""
    echo "Checking prerequisites ..."
    verifyCommand 'cf -v'
    verifyCommand 'uaac -v'
    echo ""
  }||
  {
    echo sadKitty
  }
}

# Verifies a given command existence
verifyCommand()
{
  x=$($1)
  # echo "x== $x"
  if [[ ${#x} -gt 5 ]];
  then
    echo "OK - $1"
  else
    echoc r "$1 not found!"
    echoc g "Please install: "
    echoc g "\t CF - https://github.com/cloudfoundry/cli"
    echoc g "\t UAAC -https://github.com/cloudfoundry/cf-uaac"
    sadKitty
  fi
}

deployingApp() {
	read -p "Enter a prefix for the services name:" prefix
	cd ../hello-predix/
	app_name=$prefix-hello-predix
	echo $app_name
	cf push $app_name --random-route || sadKitty
}

createUAA() {
	echo ""
	echo "Creating UAA service..."
	uaaname=$prefix-uaa
	cf create-service predix-uaa Free $uaaname -c '{"adminClientSecret":"admin_secret"}' || sadKitty
	echo ""
	echo "Binding $app_name app to $uaaname..."
	cf bs $app_name $uaaname || sadKitty
}

getUAAEndpoint() {
	  echo ""
	  echo "Getting UAA endpoint..."
	  {
		 	 env_cf_app=$(cf env $app_name)
			 uaa_uri=`echo $env_cf_app | egrep -o '"uri": "https?://[^ ]+"' | sed s/\"uri\":\ // | sed s/\"//g`

			 if [[ $uaa_uri == *"FAILED"* ]];
			 then
			   echo "Unable to find UAA endpoint for you!"
			   sadKitty
			   exit -1
			 fi

			 echo "UAA endpoint: $uaa_uri"
		} ||
	  {
	    sadKitty
	  }
}

createClient() {
		echo ""
		echo "Creating client..."
		uaac target $uaa_uri --skip-ssl-validation && uaac token client get admin -s admin_secret || sadKitty
		echo ""
		clientname=$prefix-client
		uaac client add $clientname -s secret --authorized_grant_types "authorization_code client_credentials password refresh_token" --autoapprove "openid scim.me" --authorities "clients.read clients.write scim.read scim.write" --redirect_uri "http://localhost:5000"
}

createTimeseries() {
	echo ""
	echo "Creating Timeseries service..."
	timeseriesname=$prefix-timeseries
	cf create-service predix-timeseries Free $timeseriesname -c '{"trustedIssuerIds":["'$uaa_uri'/oauth/token"]}' || sadKitty
	echo ""
	cf bs $app_name $timeseriesname || sadKitty
	timeseries_zone=`cf env $app_name|grep zone-http-header-value|sed 'n;d'|sed s/\"zone-http-header-value\":\ // |sed s/\"//g |sed s/\,//g|sed 's/ //g'` || sadKitty
}

createBlobstore() {
	echo ""
	echo "Creating Blobstore service..."
	blobstorename=$prefix-blobstore
	cf create-service predix-blobstore Tiered $blobstorename || sadKitty
	echo ""
	cf bs $app_name $blobstorename || sadKitty
}

updateClient() {
	echo ""
	echo "Updating client..."
	# uaac target $uaa_uri --skip-ssl-validation && uaac token client get admin -s admin_secret || sadKitty
	echo ""
  uaac client update $clientname --authorities "clients.read clients.write scim.write scim.read uaa.resource timeseries.zones.$timeseries_zone.query timeseries.zones.$timeseries_zone.user timeseries.zones.$timeseries_zone.ingest" --scope "openid uaa.none" --redirect_uri "http://localhost:5000"
}

createUsers() {
	echo ""
	echo "Creating users..."
	uaac user add app_admin --emails app_admin@gegrctest.com -p APP_admin18 || sadKitty
	uaac user add app_user --emails app_user@gegrctest.com -p APP_user18 || sadKitty
}

createGroups() {
	echo ""
	echo "Creating groups..."
	uaac group add "timeseries.zones.$timeseries_zone.user"
	uaac group add "timeseries.zones.$timeseries_zone.query"
	uaac group add "timeseries.zones.$timeseries_zone.ingest"
}

assignUsersToGroups() {
	echo ""
	echo "Assigning users to groups..."
	uaac member add "timeseries.zones.$timeseries_zone.user" app_admin
	uaac member add "timeseries.zones.$timeseries_zone.query" app_admin
	uaac member add "timeseries.zones.$timeseries_zone.ingest" app_admin

	uaac member add "timeseries.zones.$timeseries_zone.user" app_user
	uaac member add "timeseries.zones.$timeseries_zone.query" app_user
	uaac member add "timeseries.zones.$timeseries_zone.ingest" app_user
}

############################### ASCII ART ###############################
# Predix Training
welcome()
{
	cat <<"EOT"
	_____                 _  _     _______           _         _
  |  __ \               | |(_)   |__   __|         (_)       (_)
  | |__) |_ __  ___   __| | _ __  __| | _ __  __ _  _  _ __   _  _ __    __ _
  |  ___/| '__|/ _ \ / _` || |\ \/ /| || '__|/ _` || || '_ \ | || '_ \  / _` |
  | |    | |  |  __/| (_| || | >  < | || |  | (_| || || | | || || | | || (_| |
  |_|    |_|   \___| \__,_||_|/_/\_\|_||_|   \__,_||_||_| |_||_||_| |_| \__, |
                                                                         __/ |
                                                                        |___/

EOT
}

# sad kitty
sadKitty()
{
    cat <<"EOT"

    /\ ___ /\
   (  o   o  )
    \  >#<  /
    /       \
   /         \       ^
  |           |     //
   \         /    //
    ///  ///   --

EOT
echo ""
exit 1
}

output()
{
  cat <<EOF >./adbp-environment.txt
Hello Predix App Name      :  "$app_name"
UAA Name                   :  "$uaaname"
UAA URI                    :  "$uaa_uri"
UAA Admin Secret           :  admin_secret
Client Name                :  "$clientname"
Client Secret              :  secret
Timeseries Name            :  "$timeseriesname"
Blobstore Name             :  "$blobstorename"
App Admin User Name        :  app_admin
App Admin User Password    :  APP_admin18
App User Name              :  app_user
App User Password          :  APP_user18
EOF
 echo ""
 echo "Your services are now set up!"
 echo "A adbp-environment.txt file with all your environment details is created in the scripts directory"
}

main "$@"
