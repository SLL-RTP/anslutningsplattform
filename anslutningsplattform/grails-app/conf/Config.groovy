import static java.lang.System.getProperty

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]


//System variable holding info about config dir for external resources
def SYS_VAR_CONFIG_DIR = 'se.skltp.ap.config.dir'
def EXT_RESOURCES_DIR = ''

environments {
    development {
        EXT_RESOURCES_DIR = 'grails-app/conf/config.dir.external/data'
    }
}
//Dir where external resources as override properties and caches are placed

if(getProperty(SYS_VAR_CONFIG_DIR)){
    EXT_RESOURCES_DIR = getProperty(SYS_VAR_CONFIG_DIR)
    println "System variable ${SYS_VAR_CONFIG_DIR} is set! External Resources will be loaded from ${EXT_RESOURCES_DIR}"

    grails.config.locations = [  "file:${EXT_RESOURCES_DIR}/${appName}-config-override.properties",
                                 "file:${EXT_RESOURCES_DIR}/${appName}-config-override.groovy"]

}else{
    println "NOTE! System variable ${SYS_VAR_CONFIG_DIR} is NOT set! External Resources will be loaded from ${EXT_RESOURCES_DIR}"
}


// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

// configure passing transaction's read-only attribute to Hibernate session, queries and criterias
// set "singleSession = false" OSIV mode in hibernate configuration after enabling
grails.hibernate.pass.readonly = false
// configure passing read-only to OSIV session by default, requires "singleSession = false" OSIV mode
grails.hibernate.osiv.readonly = false

environments {
    development {
        grails.logging.jul.usebridge = true

        //Cors must be addressed when running locally
        cors.enabled=true
        cors.url.pattern = '/api/*'
        cors.headers=[
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Credentials': true,
                'Access-Control-Allow-Headers': 'origin, authorization, accept, content-type, x-requested-with, x-ap-auth',
                'Access-Control-Allow-Methods': 'GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS',
                'Access-Control-Max-Age': 3600
        ]

        log4j = {
            appenders{
                console name: 'stdout', layout: pattern(conversionPattern: "%d [%t] %-5p %c %x - %m%n")
            }

            error stdout:'stackTrace'

            debug 'grails.app', 'se.skltp.av', 'se.skltp.ap'
            fatal 'org.hibernate.tool.hbm2ddl.SchemaExport' //Removing background noise
        }
		
		// SMTP config
		grails {
			mail {
				/*
				// gmail for testing locally
				host = "smtp.gmail.com"
				port = "465"
				//username = "youracount@gmail.com"
				//password = "yourpassword"
				username = "hakan.dahl.demo1@gmail.com"
				password = "SET_YOUR_PASSWORD"
				props = ["mail.smtp.auth":"true",
					"mail.smtp.socketFactory.port":"465",
					"mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
					"mail.smtp.socketFactory.fallback":"false"]
				*/
				// Basefarm mail config - can be used use from local workstation over VPN 
				host = "mailout.sth.basefarm.net"
				port = "25"
			}
		}
		//grails.mail.disabled=true
    }
    production {

        def catalinaBase = getProperty('catalina.base')
        def logDirectory = catalinaBase ? "${catalinaBase}/logs" : "."

        log4j = {
            appenders{
                rollingFile name: 'stdout', file: "${logDirectory}/${appName}.log".toString(), layout: pattern(conversionPattern: "%d [%t] %-5p %c %x - %m%n"), maxFileSize: '10MB', maxBackupIndex: 10
                rollingFile name: 'audit', file:"${logDirectory}/${appName}-audit.log".toString(), layout:pattern(conversionPattern: '%d: %m%n'), maxFileSize: '10MB',maxBackupIndex: 10
            }
            root {
                error 'stdout'
                additivity = false
            }
            error stdout:'stackTrace'

            info 'grails.app'

            info audit:'grails.app.controller.se.skltp.av', 'grails.app.controller.se.skltp.ap'
        }

        grails.logging.jul.usebridge = false
		
		// SMTP config
		grails {
			mail {
				// Basefarm mail config
				host = "mailout.sth.basefarm.net"
				port = "25"
			}
		}
    }
}

// log4j configuration
log4j.main = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    fatal 'org.hibernate.tool.hbm2ddl.SchemaExport'   //Removing background noise
    info 'grails.app', 'se.skltp.av', 'se.skltp.ap'
}

environments {
	development{
		// HSA cache
		hsa.hsacache.files = ["${EXT_RESOURCES_DIR}/hsacache.xml", "${EXT_RESOURCES_DIR}/hsacachecomplementary.xml"]
		
		// RIV TA cache
		rivta.cache.file = "${EXT_RESOURCES_DIR}/domains.xml"
		
		// TAK cache location
		tak.cache.location = "${EXT_RESOURCES_DIR}/tak"

        grails.gorm.failOnError = true //Lets fail for anything so we catch it in dev
		
		grails.mail.disabled = true //Disable sending mail in local dev for now

        // Token used by client to invoke backend
        api.auth.token = 'secret-token'
        x509.enabled = false
        x509.cert.header = 'x-vp-auth-cert'

        nat = [
                "xxx-net":"XXX-net",
                "sjunet":"Sjunet",
                "internet":"Internet"
        ]

        // Tak config
        //tak.sokvagvalsinfo.url = 'http://localhost:8090/tp-vagval-admin-services/SokVagvalsInfo/v2?wsdl'
        // Note: the sequence number must be surrounded by quotes to be a valid groovy identifier
        tak.env.id.'1' = 'dev-demo-prod'
        tak.env.name.'1' = 'DEV DEMO PROD'
        tak.env.url.'1' = 'http://prod.host.configure.me:8081/tak-services/SokVagvalsInfo/v2'

        tak.env.id.'2' = 'dev-demo-qa'
        tak.env.name.'2' = 'DEV DEMO QA'
        tak.env.url.'2' = 'http://qa.host.configure.me:8081/tak-services/SokVagvalsInfo/v2'

        tak.env.id.'3' = 'dev-demo-test'
        tak.env.name.'3' = 'DEV DEMO TEST'
        //tak.env.url.'3' = 'http://localhost:8081/tak-services/SokVagvalsInfo/v2'
        tak.env.url.'3' = 'http://test.host.configure.me:8081/tak-services/SokVagvalsInfo/v2'

        //tak.env.id.'4' = 'hd-rivta-box'
        //tak.env.name.'4' = 'HD RIVTA BOX'
        //tak.env.url.'4' = 'http://33.33.33.33:8080/tak-services/SokVagvalsInfo/v2'

        //tak.env.id.'5' = 'hd-local-test'
        //tak.env.name.'5' = 'HD lOCAL TEST'
        //tak.env.url.'5' = 'http://localhost:8081/tak-services/SokVagvalsInfo/v2'
	}

	production{
		// HSA cache
		hsa.hsacache.files = ["${EXT_RESOURCES_DIR}/hsacache.xml", "${EXT_RESOURCES_DIR}/hsacachecomplementary.xml"]

		// RIV TA cache
		rivta.cache.file = "${EXT_RESOURCES_DIR}/domains.xml"

		// TAK cache location
		tak.cache.location = "${EXT_RESOURCES_DIR}/tak"

		grails.mail.disabled = false

        //grails.mail.default.from = 'noreply.anslutningsplattform@ntjp.se'

        // Token used by client to invoke backend
        api.auth.token = 'secret-token'
        x509.enabled = true
        x509.cert.header = 'x-vp-auth-cert'
	}
}

//grails.mail.default.from = 'hakan.dahl.demo1@gmail.com'
//smtp.to.address = 'hakan.dahl.demo1@gmail.com'
smtp.to.address = 'johanna.essen@callistaenterprise.se'
smtp.from.address = ''