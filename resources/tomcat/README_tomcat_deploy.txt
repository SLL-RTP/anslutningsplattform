Deploying to Tomcat
====================
1. Put the setenv.sh file in $TOMCAT_HOME/bin and make sure it is executable
2. Configure the dir-path in setenv.sh (conf-dir expected to contain property files and resources)
3. Add necessary properties-files and resources to the conf-dir
4. Restart Tomcat (to load the config in setenv.sh)
5. Deploy the app by putting the war-file in $TOMCAT_HOME/webapps
6. Test the app
