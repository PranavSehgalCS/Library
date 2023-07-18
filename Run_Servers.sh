#!/bin/sh
#///////////////////////////////////////////////////////////////////////////////////////////////////////
#   FILE : Run_Servers.sh
#   AUTHOR : Pranav Sehgal
#   DESCRIPTION :   OPENS Postgress via Terminal so you can start DB
#                   OPENS a Terminal window and launches backend using mvn compile exec:java;
#					CLEARS Angular Cache from previous session
#                   OPENS a Terminal window and launches fronted using ng serve --open;
#///////////////////////////////////////////////////////////////////////////////////////////////////////

a=1;

open  -a "Postgres"

osascript -e 'tell application "Terminal" to do script "cd Desktop/P3/Library/Project_API;
mvn compile exec:java;
mvn compile exec:java;"'

sleep 5;

cd Desktop/P3/Skeleton_Server_SQL/Project_UI/angular-workspace/

osascript -e 'tell application "Terminal" to do script "cd Desktop/P3/Library/Project_UI/angular-workspace; 
ng serve --open;"'

