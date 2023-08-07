#!/bin/sh
#///////////////////////////////////////////////////////////////////////////////////////////////////////
#   FILE : Run_Servers.sh
#   AUTHOR : Pranav Sehgal
#   DESCRIPTION :   OPENS Postgress via Terminal so you can start DB
#                   OPENS a Terminal window and launches backend using mvn compile exec:java;
#                   OPENS a Terminal window and launches fronted using ng serve --open;
#///////////////////////////////////////////////////////////////////////////////////////////////////////

open  -a "Postgres"

osascript -e 'tell application "Terminal" to do script "cd Desktop/P3/Library/Project_UI/angular-workspace; 
ng serve --open;"'

cd Desktop/P3/Library/Project_API;
mvn compile exec:java;
sleep 5;
mvn compile exec:java;"'
