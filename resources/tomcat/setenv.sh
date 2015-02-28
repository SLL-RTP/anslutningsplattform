#export CATALINA_OPTS="$CATALINA_OPTS -Xms256m -Xmx512m -XX:MaxPermSize=256m"

# grails: use environment config "production"
export CATALINA_OPTS="$CATALINA_OPTS -Dgrails.env=production"
# anslutningsplattform: configure the conf-dir
export CATALINA_OPTS="$CATALINA_OPTS -Dse.skltp.ap.config.dir=/usr/local/skltp-anslutningsplattform/conf"
